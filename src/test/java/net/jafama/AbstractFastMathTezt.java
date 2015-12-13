/*
 * Copyright 2014 Jeff Hain
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.jafama;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import junit.framework.TestCase;

/**
 * Stuffs to test FastMath and StrictFastMath.
 * 
 * TODO Run tests:
 * - while using redefined logs and sqrt, for worse case.
 * - with different seeds, and better or various Randoms,
 *   for better chance to trigger some bad cases.
 */
abstract class AbstractFastMathTezt extends TestCase {

    /*
     * To ensure that some of these tests can run with Java 5, we use (Strict)FastMath
     * versions of nextAfter/nextUp/nextDown/scalb, which are tested against
     * their Java 6+ implementations.
     * 
     * For some methods, not testing them against JDK ones, either because they
     * are not available in lowest required Java version, or because their
     * semantics might change depending on Java version.
     * 
     * Epsilons are chosen for tests to pass even if using redefined log(double)
     * and sqrt(double) methods. Since by default they might delegate to Math or
     * StrictMath, the accuracy of depending methods might be better than used
     * epsilons could let believe.
     */

    //--------------------------------------------------------------------------
    // CONFIGURATION
    //--------------------------------------------------------------------------
    
    static final int NBR_OF_VALUES = 10 * 1000 * 1000;
    
    static final int NBR_OF_VALUES_SMALL = 100 * 1000;
    
    static final boolean LOG_WHEN_WORSE = false;
    
    static final boolean LOG_AT_END = true;
    
    final Random random = new Random(123456789L);
    
    //--------------------------------------------------------------------------
    // PACKAGE-PRIVATE CLASSES
    //--------------------------------------------------------------------------
    
    static class MyDoubleResHelper {
        private final String name;
        private final MyDeltaHelper absHelper = new MyDeltaHelper("abs");
        private final MyDeltaHelper relHelper = new MyDeltaHelper("rel");
        private boolean lastOK = true;
        public MyDoubleResHelper() {
            this(null);
        }
        public MyDoubleResHelper(String name) {
            this.name = name;
        }
        /**
         * @param ref Expected result.
         * @param res Actual result.
         * @param absTol NaN if not to be used.
         * @param relTol NaN if not to be used.
         * @return true if did log, which allows to piggy-back specific logs,
         *         false otherwise.
         */
        public boolean process(
                double ref,
                double res,
                double absTol,
                double relTol,
                Object... args) {
            this.absHelper.process(
                    ref,
                    res,
                    absTol,
                    NumbersTestUtils.absDelta(ref, res),
                    args);
            this.relHelper.process(
                    ref,
                    res,
                    relTol,
                    NumbersTestUtils.relDelta(ref, res),
                    args);
            boolean absDeltaOK = this.absHelper.lastOK();
            boolean relDeltaOK = this.relHelper.lastOK();
            this.lastOK = absDeltaOK && relDeltaOK;
            
            final boolean logAbsDelta = this.absHelper.lastKOOrGotWorseAndMustLog();
            final boolean logRelDelta = this.relHelper.lastKOOrGotWorseAndMustLog();
            
            boolean log = logAbsDelta || logRelDelta;
            if (log) {
                this.logCallerMethodName(1);
                if (logAbsDelta) {
                    this.absHelper.log(absTol);
                }
                if (logRelDelta) {
                    this.relHelper.log(relTol);
                }
            }
            return log;
        }
        public boolean lastOK() {
            return this.lastOK;
        }
        public void finalLogIfNeeded() {
            if (LOG_AT_END) {
                this.logCallerMethodName(1);
                this.absHelper.log(Double.NaN);
                this.relHelper.log(Double.NaN);
            }
        }
        private void logCallerMethodName(int depth) {
            String suffix = (this.name == null) ? null : "("+this.name+")";
            printCallerMethodName(1+depth,suffix);
        }
    }
    
    //--------------------------------------------------------------------------
    // PRIVATE CLASSES
    //--------------------------------------------------------------------------

    private static class MyDeltaData {
        private final Object[] args;
        private final double ref;
        private final double res;
        private final double delta;
        public MyDeltaData(
                double ref,
                double res,
                double delta,
                Object... args) {
            this.args = args;
            this.ref = ref;
            this.res = res;
            this.delta = delta;
        }
    }
    
    private static class MyDeltaHelper {
        private final String deltaType;
        /*
         * Using TreeMap to make sure logs are sorted by tolerance.
         */
        private final TreeMap<Double,MyDeltaData> deltaByTol = new TreeMap<Double,MyDeltaData>();
        private boolean lastOK = true;
        private boolean lastGotWorse = false;
        public MyDeltaHelper(String deltaType) {
            this.deltaType = deltaType;
        }
        public void process(
                double ref,
                double res,
                double tol,
                double delta,
                Object... args) {
            if (ignoreTol(tol)) {
                return;
            }
            boolean deltaOK = (delta <= tol);
            this.lastOK = deltaOK;
            
            final MyDeltaData prevData = this.deltaByTol.get(tol);
            
            final boolean deltaForTolGotWorse = (prevData != null) && (delta > prevData.delta);
            MyDeltaData newData = prevData;
            if ((prevData == null) || deltaForTolGotWorse) {
                newData = new MyDeltaData(ref, res, delta, args);
                this.deltaByTol.put(tol, newData);
            }

            this.lastGotWorse = ((prevData == null) || deltaForTolGotWorse);
        }
        public boolean lastOK() {
            return this.lastOK;
        }
        /**
         * @return true if last delta is KO, or if it got worse
         *         and must be logged in this case.
         */
        public boolean lastKOOrGotWorseAndMustLog() {
            return (!this.lastOK) || (LOG_WHEN_WORSE && this.lastGotWorse);
        }
        /**
         * @param onlyTol If NaN, not used.
         */
        public void log(double onlyTol) {
            boolean printedHeader = false;
            final TreeMap<Double,MyDeltaData> map = this.deltaByTol;
            for (Map.Entry<Double,MyDeltaData> entry : map.entrySet()) {
                final double tol = entry.getKey();
                if ((!Double.isNaN(onlyTol)) && (tol != onlyTol)) {
                    continue;
                }
                if (!printedHeader) {
                    System.out.println("worse "+this.deltaType+" deltas:");
                    printedHeader = true;
                }
                final MyDeltaData data = entry.getValue();
                System.out.println(" "+this.deltaType+" "+data.delta+" (tol = "+tol+"):");
                if (data.args.length == 1) {
                    System.out.println("  arg = "+data.args[0]);
                } else {
                    System.out.println("  args = "+Arrays.toString(data.args));
                }
                System.out.println("  ref = "+data.ref);
                System.out.println("  res = "+data.res);
                if (!(data.delta <= tol)) {
                    System.out.println("  (ko)");
                }
            }
        }
        private static boolean ignoreTol(double tol) {
            return Double.isNaN(tol);
        }
    }
    
    //--------------------------------------------------------------------------
    // MEMBERS
    //--------------------------------------------------------------------------

    /**
     * Double.MIN_NORMAL since Java 6.
     */
    static final double DOUBLE_MIN_NORMAL = 0x1.0p-1022; // 2.2250738585072014E-308

    static final double TOL_1EM15 = 1e-15;
    static final double TOL_1EM14 = 1e-14;
    
    static final double TOL_TRIG_NORM_REL = 2e-6;
    static final double MAX_VALUE_FAST_TRIG_NORM = ((1L<<52) * (Math.PI/2)) / 1e2;
    static final double TOL_FAST_TRIG_NORM_ABS = 3e-8;

    static final double TOL_SIN_COS_ABS = 1e-15;
    static final double TOL_SIN_COS_REL = 1e-13;
    static final double TOL_SIN_COS_REL_BAD = TOL_TRIG_NORM_REL;

    static final double TOL_SINQUICK_COSQUICK_ABS = 1.6e-3;

    static final double TOL_SINH_COSH_REL = 2e-15;
    
    final NumbersTestUtils utils = new NumbersTestUtils(this.random);

    //--------------------------------------------------------------------------
    // PACKAGE-PRIVATE METHODS
    //--------------------------------------------------------------------------

    static void printCallerName() {
        printCallerMethodName(1,null);
    }
    
    /**
     * Beware that depending on whether methods are private, etc.,
     * intermediary method calls such as to "access§0" might be done
     * in-between method calls that are explicit in the code.
     * 
     * Not private, to avoid accessor call.
     * 
     * @param depth 0 for caller method name, 1 for caller's caller name, etc.
     */
    static void printCallerMethodName(int depth, String suffix) {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        String callerName = ste[2+depth].getMethodName();
        System.out.println();
        System.out.println(callerName+"()"+((suffix == null) ? "" : " "+suffix));
    }

    /*
     * Known bad values: return a known bad value if i is small,
     * else the specified value.
     */
    
    /**
     * For trigonometric normalization treatments (and thus sin/cos/tan).
     */
    static double knownBadValues_trigNorm(int i, double value) {
        switch (i) {
        case 0: return 100.51351120207333;
        case 1: return 11081.968085537996;
        case 2: return 45487.120031326616;
        case 3: return 46066.74387591393;
        case 4: return 91553.86390724055; // Bad relative error for tan.
        case 5: return 183107.7278144811;
        case 6: return 366215.4556289622;
        case 7: return 411122.9518157015; // Close to and below 2^18*(PI/2).
        case 8: return 803697.9406046089;
        case 9: return 822245.903631403; // Close to and below 2^19*(PI/2).
        case 10: return 181948.48012530647;
        case 11: return 366215.4556289622;
        case 12: return 1275957.8562554945;
        case 13: return 2525556.179351044; // 1st fall back in heavyRemainderPiO2.
        case 14: return 3082994.0966187096;
        case 15: return 3307531.577552406; // Bad relative error if using non-heavy remainder up to 2^20*PI/2.
        case 16: return 3.4227747215167313E187;
        case 17: return 1.0771979426769142E303; // 1st fall back in heavyRemainderPiO2.
        }
        return value;
    }

    static double knownBadValues_sinh_cosh_tanh(int i, double value) {
        switch (i) {
        case 0: return 710.2379261611511;
        }
        return value;
    }
    
    static double knownBadValues_expQuick_double(int i, double value) {
        switch (i) {
        case 0: return 570.4899600655233;
        }
        return value;
    }
    
    static double knownBadValues_logQuick_double(int i, double value) {
        switch (i) {
        case 0: return 1.1599646758315496;
        }
        return value;
    }

    static double knownBadValues_pow_2double_a(int i, double value) {
        switch (i) {
        case 0: return 1.1730957031249998;
        case 1: return 1.1486768182367084;
        }
        return value;
    }
    
    static double knownBadValues_pow_2double_b(int i, double value) {
        switch (i) {
        case 0: return 3392.000000000001;
        case 1: return -4095.999999999999;
        }
        return value;
    }
    
    static double knownBadValues_powQuick_2double_a(int i, double value) {
        switch (i) {
        case 0: return 0.8704959112675812;
        case 1: return 0.71518550383034;
        }
        return value;
    }
    
    static double knownBadValues_powQuick_2double_b(int i, double value) {
        switch (i) {
        case 0: return -243.48760843276978;
        case 1: return -58.947157092392445;
        }
        return value;
    }
    
    static double knownBadValues_sqrtQuick_double(int i, double value) {
        switch (i) {
        case 0: return 2.27137101342377E133;
        }
        return value;
    }
    
    static double knownBadValues_invSqrtQuick_double(int i, double value) {
        switch (i) {
        case 0: return 1.676259113596385E308;
        }
        return value;
    }
    
    /*
     * 
     */
    
    /**
     * @return true if a and b are both +0.0 or both -0.0,
     *         or are non-zero and equals (==), or are both NaN,
     *         false otherwise.
     */
    static boolean equivalent(float a, float b) {
        if ((a == 0) && (b == 0)) {
            return Float.floatToRawIntBits(a) == Float.floatToRawIntBits(b);
        } else {
            return (a == b) || (Float.isNaN(a) && Float.isNaN(b));
        }
    }

    /**
     * @return true if a and b are both +0.0 or both -0.0,
     *         or are non-zero and equals (==), or are both NaN,
     *         false otherwise.
     */
    static boolean equivalent(double a, double b) {
        if ((a == 0) && (b == 0)) {
            return Double.doubleToRawLongBits(a) == Double.doubleToRawLongBits(b);
        } else {
            return (a == b) || (Double.isNaN(a) && Double.isNaN(b));
        }
    }

    /*
     * uniform
     */

    int randomIntUniform(int min, int max) {
        return this.utils.randomIntUniform(min, max);
    }
    
    double randomDoubleUniform(double min, double max) {
        return this.utils.randomDoubleUniform(min, max);
    }

    /*
     * whatever
     */

    int randomIntWhatever() {
        return this.utils.randomIntWhatever();
    }

    long randomLongWhatever() {
        return this.utils.randomLongWhatever();
    }

    float randomFloatWhatever() {
        return this.utils.randomFloatWhatever();
    }

    double randomDoubleWhatever() {
        return this.utils.randomDoubleWhatever();
    }

    double randomDoubleWhatever(double min, double max) {
        return this.utils.randomDoubleWhatever(min, max);
    }

    /**
     * To have special cases at-or-near k*PI/4.
     */
    double randomDoubleWhateverOrPiIsh() {
        int u18 = 1 + this.random.nextInt(8);
        return this.utils.randomDoubleWhatever() * (u18 * (Math.PI/4));
    }
    
    /*
     * 
     */

    static double absDelta(double a, double b) {
        return NumbersTestUtils.absDelta(a, b);
    }

    static double relDelta(double a, double b) {
        return NumbersTestUtils.relDelta(a, b);
    }
    
    /*
     * 
     */

    /**
     * To rework reference result and avoid bad error
     * when res ~= ref +- mod.
     */
    static double refMod(double ref, double res, double mod) {
        if (ref < res - mod * 0.5) {
            ref += mod;
        }
        if (ref > res + mod * 0.5) {
            ref -= mod;
        }
        return ref;
    }
    
    /*
     * 
     */
    
    static double getExpectedResult_remainder_2double(double a, double b) {
        double expected = Math.IEEEremainder(a,b);
        
        final double div = a/b;
        
        if (NumbersUtils.isEquidistant(div)) {
            
            /*
             * Computing IEEE "n".
             */
            
            // div being equidistant, rint will return
            // the closest and even mathematical integer,
            // which is the "n" used by IEEE algorithm.
            final double nIEEE = Math.rint(div);
            
            /*
             * Computing FastMath "n".
             */
            
            final double divToZero = FastMath.nextAfter(div, 0.0);
            // If div is equally close to surrounding integers,
            // this will be the value of lowest magnitude.
            final double nFastMath = Math.rint(divToZero);
            
            if (nIEEE != nFastMath) {
                assertEquals(Math.abs(nIEEE), Math.abs(nFastMath)+1.0);
                
                /*
                 * Computing result corresponding to FastMath's "n".
                 */
                
                double newExpectedIfInRange = expected - (nFastMath - nIEEE) * b;
                
                // Eventually scaling up, far enough from +-Double.MIN_VALUE
                // to avoid precision loss when dividing by two, which could
                // be huge for small subnormal values, and make us provide
                // wrong expected result.
                final boolean scaleUp = (Math.abs(b) < 1.0);
                if (scaleUp) {
                    b *= (1L<<54);
                    newExpectedIfInRange *= (1L<<54);
                }
                final double absBound = Math.abs(b/2);
                final boolean newExpectedInRange = (newExpectedIfInRange >= -absBound) && (newExpectedIfInRange <= absBound);
                if (scaleUp) {
                    b /= (1L<<54);
                    newExpectedIfInRange /= (1L<<54);
                }

                if (newExpectedInRange) {
                    expected = newExpectedIfInRange;
                }
            }
        }

        return expected;
    }
}
