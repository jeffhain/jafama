/*
 * Copyright 2012-2015 Jeff Hain
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

/**
 * FastMath micro benchmarks.
 */
public class FastMathPerf extends AbstractFastMathPerf {

    /*
     * For each method with floating-point arguments, terminating with a
     * micro-benchmark with whatever type of values, to detect eventual
     * JIT-optimization-related JVM-crashes due to bad handling of NaNs
     * and such in optimized code (can happen with Java 6u29).
     */

    //--------------------------------------------------------------------------
    // PUBLIC METHODS
    //--------------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println(TestUtils.getJVMInfo());
        newRun(args);
    }

    public static void newRun(String[] args) {
        new FastMathPerf().run(args);
    }

    public FastMathPerf() {
    }

    //--------------------------------------------------------------------------
    // PRIVATE METHODS
    //--------------------------------------------------------------------------

    private static void settle() {
        TestUtils.settleAndNewLine();
    }

    private void run(String[] args) {
        System.out.println("--- "+FastMathPerf.class.getSimpleName()+"... ---");
        System.out.println("number of calls = "+NBR_OF_CALLS);
        System.out.println("number of random values = "+NBR_OF_VALUES);
        printLoopOverhead();

        settle();
        testClassLoad();

        /*
         * trigonometry
         */

        settle();
        test_sin_double();
        settle();
        test_sinQuick_double();
        settle();
        test_cos_double();
        settle();
        test_cosQuick_double();
        settle();
        test_sinAndCos_double_DoubleWrapper();
        settle();
        test_tan_double();

        settle();
        test_asin_double();
        settle();
        test_acos_double();
        settle();
        test_atan_double();
        settle();
        test_atan2_2double();

        settle();
        test_toRadians_double();
        settle();
        test_toDegrees_double();

        settle();
        test_isInClockwiseDomain_3double();

        /*
         * hyperbolic trigonometry
         */

        settle();
        test_sinh_double();
        settle();
        test_cosh_double();
        settle();
        test_coshm1_double();
        settle();
        test_sinhAndCosh_double_DoubleWrapper();
        settle();
        test_tanh_double();

        settle();
        test_asinh_double();
        settle();
        test_acosh_double();
        settle();
        test_acosh1p_double();
        settle();
        test_atanh_double();

        /*
         * exponentials
         */

        settle();
        test_exp_double();
        settle();
        test_expQuick_double();
        settle();
        test_expm1_double();

        /*
         * logarithms
         */

        settle();
        test_log_double();
        settle();
        test_logQuick_double();
        settle();
        test_log10_double();
        settle();
        test_log1p_double();

        /*
         * powers
         */

        settle();
        test_pow_2double();
        settle();
        test_powQuick_2double();
        settle();
        test_powFast_double_int();

        /*
         * roots
         */

        settle();
        test_sqrt_double();
        settle();
        test_sqrtQuick_double();
        settle();
        test_invSqrtQuick_double();
        settle();
        test_cbrt_double();

        settle();
        test_hypot_2double();
        settle();
        test_hypot_3double();

        /*
         * close values
         */

        settle();
        test_floor_float();
        settle();
        test_floor_double();

        settle();
        test_ceil_float();
        settle();
        test_ceil_double();

        settle();
        test_round_float();
        settle();
        test_round_double();

        settle();
        test_roundEven_float();
        settle();
        test_roundEven_double();

        settle();
        test_rint_float();
        settle();
        test_rint_double();

        /*
         * binary operators (/,%)
         */

        settle();
        test_remainder_2double();

        settle();
        test_normalizeMinusPiPi();
        settle();
        test_normalizeMinusPiPiFast();
        settle();
        test_normalizeZeroTwoPi();
        settle();
        test_normalizeZeroTwoPiFast();
        settle();
        test_normalizeMinusHalfPiHalfPi();
        settle();
        test_normalizeMinusHalfPiHalfPiFast();

        /*
         * floating points utils
         */

        settle();
        test_getExponent_float();
        settle();
        test_getExponent_double();

        settle();
        test_signum_float();
        settle();
        test_signum_double();
        settle();
        test_signFromBit_float();
        settle();
        test_signFromBit_double();
        settle();
        test_copySign_2float();
        settle();
        test_copySign_2double();

        settle();
        test_ulp_float();
        settle();
        test_ulp_double();

        settle();
        test_nextAfter_float_double();
        settle();
        test_nextAfter_2double();
        settle();
        test_nextUp_float();
        settle();
        test_nextUp_double();
        settle();
        test_nextDown_float();
        settle();
        test_nextDown_double();

        settle();
        test_scalb_float_int();
        settle();
        test_scalb_double_int();

        System.out.println("--- ..."+FastMathPerf.class.getSimpleName()+" ---");
    }

    /*
     * 
     */

    private void testClassLoad() {
        int dummy = 0;

        System.out.println("--- testing FastMath class load (if not loaded already) ---");

        // Making sure Math and StrictMath are loaded already.
        if (Math.sin(0.0) != StrictMath.sin(0.0)) {
            System.out.println("can't happen");
        }

        startTimer();
        dummy += FastMath.abs(0); // Does not use tables.
        System.out.println("FastMath class load without tables init took "+getElapsedSeconds()+" s");

        startTimer();
        FastMath.initTables();
        System.out.println("FastMath.initTables() took "+getElapsedSeconds()+" s");

        useDummy(dummy);
    }

    /*
     * trigonometry
     */

    private void test_sin_double() {
        double dummy = 0.0;

        System.out.println("--- testing sin(double) ---");

        for (double[] args : new double[][]{
                new double[]{0,2*Math.PI},
                new double[]{-10,10},
                new double[]{-100,100},
                new double[]{-DOUBLE_1E6,DOUBLE_1E6},
                new double[]{-DOUBLE_1E12,DOUBLE_1E12},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.sin(values[j]);
            }
            System.out.println("Loop on     Math.sin(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.sin(values[j]);
            }
            System.out.println("Loop on FastMath.sin(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.sin(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_sinQuick_double() {
        double dummy = 0.0;

        System.out.println("--- testing sinQuick(double) ---");

        for (double[] args : new double[][]{
                new double[]{-DOUBLE_1E6,DOUBLE_1E6}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.sin(values[j]);
            }
            System.out.println("Loop on          Math.sin(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.sinQuick(values[j]);
            }
            System.out.println("Loop on FastMath.sinQuick(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.sinQuick(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_cos_double() {
        double dummy = 0.0;

        System.out.println("--- testing cos(double) ---");

        for (double[] args : new double[][]{
                new double[]{0,2*Math.PI},
                new double[]{-10,10},
                new double[]{-100,100},
                new double[]{-DOUBLE_1E6,DOUBLE_1E6},
                new double[]{-DOUBLE_1E12,DOUBLE_1E12},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.cos(values[j]);
            }
            System.out.println("Loop on     Math.cos(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.cos(values[j]);
            }
            System.out.println("Loop on FastMath.cos(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.cos(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_cosQuick_double() {
        double dummy = 0.0;

        System.out.println("--- testing cosQuick(double) ---");

        for (double[] args : new double[][]{
                new double[]{-DOUBLE_1E6,DOUBLE_1E6}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.cos(values[j]);
            }
            System.out.println("Loop on          Math.cos(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.cosQuick(values[j]);
            }
            System.out.println("Loop on FastMath.cosQuick(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.cosQuick(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_sinAndCos_double_DoubleWrapper() {
        DoubleWrapper cosine = new DoubleWrapper();
        double dummy = 0.0;

        System.out.println("--- testing sinAndCos(double,DoubleWrapper) ---");

        for (double[] args : new double[][]{
                new double[]{0,2*Math.PI},
                new double[]{-10,10},
                new double[]{-100,100},
                new double[]{-DOUBLE_1E6,DOUBLE_1E6},
                new double[]{-DOUBLE_1E12,DOUBLE_1E12},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            double sine = FastMath.sinAndCos(values[j],cosine);
            dummy += sine + cosine.value;
            }
            System.out.println("Loop on FastMath.sinAndCos(double,DoubleWrapper), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.sinAndCos(values[j],cosine);
            }
        }

        useDummy(dummy);
    }

    private void test_tan_double() {
        double dummy = 0.0;

        System.out.println("--- testing tan(double) ---");

        for (double[] args : new double[][]{
                new double[]{-Math.PI/2,Math.PI/2},
                new double[]{-10,10},
                new double[]{-100,100},
                new double[]{-DOUBLE_1E6,DOUBLE_1E6},
                new double[]{-DOUBLE_1E12,DOUBLE_1E12},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.tan(values[j]);
            }
            System.out.println("Loop on     Math.tan(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.tan(values[j]);
            }
            System.out.println("Loop on FastMath.tan(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.tan(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_asin_double() {
        double dummy = 0.0;

        System.out.println("--- testing asin(double) ---");

        {
            final double[] values = newDoubleTab(new InterfaceDoubleGenerator() {
                public double newValue() {
                    return Math.cos(randomDoubleUniform(0.0,2*Math.PI));
                }
            });

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.asin(values[j]);
            }
            System.out.println("Loop on     Math.asin(double) took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.asin(values[j]);
            }
            System.out.println("Loop on FastMath.asin(double) took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.asin(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_acos_double() {
        double dummy = 0.0;

        System.out.println("--- testing acos(double) ---");

        {
            final double[] values = newDoubleTab(new InterfaceDoubleGenerator() {
                public double newValue() {
                    return Math.cos(randomDoubleUniform(0.0,2*Math.PI));
                }
            });

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.acos(values[j]);
            }
            System.out.println("Loop on     Math.acos(double) took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.acos(values[j]);
            }
            System.out.println("Loop on FastMath.acos(double) took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.acos(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_atan_double() {
        double dummy = 0.0;

        System.out.println("--- testing atan(double) ---");

        {
            final double[] values = newDoubleTab(new InterfaceDoubleGenerator() {
                public double newValue() {
                    return Math.tan(randomDoubleUniform(-Math.PI/2,Math.PI/2));
                }
            });

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.atan(values[j]);
            }
            System.out.println("Loop on     Math.atan(double) took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.atan(values[j]);
            }
            System.out.println("Loop on FastMath.atan(double) took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.atan(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_atan2_2double() {
        double dummy = 0.0;

        System.out.println("--- testing atan2(double,double) ---");

        for (double[] args : new double[][]{
                new double[]{-10,10},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.atan2(values[j],values[MASK-j]);
            }
            System.out.println("Loop on     Math.atan2(double,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.atan2(values[j],values[MASK-j]);
            }
            System.out.println("Loop on FastMath.atan2(double,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.atan2(values[j],values[MASK-j]);
            }
        }

        useDummy(dummy);
    }

    private void test_toRadians_double() {
        double dummy = 0.0;

        System.out.println("--- testing toRadians(double) ---");

        for (double[] args : new double[][]{
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.toRadians(values[j]);
            }
            System.out.println("Loop on     Math.toRadians(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.toRadians(values[j]);
            }
            System.out.println("Loop on FastMath.toRadians(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s... ");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.toRadians(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_toDegrees_double() {
        double dummy = 0.0;

        System.out.println("--- testing toDegrees(double) ---");

        for (double[] args : new double[][]{
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.toDegrees(values[j]);
            }
            System.out.println("Loop on     Math.toDegrees(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.toDegrees(values[j]);
            }
            System.out.println("Loop on FastMath.toDegrees(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s... ");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.toDegrees(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_isInClockwiseDomain_3double() {
        boolean dummy = false;

        System.out.println("--- testing isInClockwiseDomain(double,double,double) ---");

        for (double[][] args123 : new double[][][]{
                new double[][]{new double[]{-Math.PI,Math.PI},new double[]{0,2*Math.PI},new double[]{Math.PI,Math.PI}},
                new double[][]{new double[]{-Math.PI,Math.PI},new double[]{0,2*Math.PI},new double[]{-100,100}},
                new double[][]{new double[]{-100,100},new double[]{0,2*Math.PI},new double[]{Math.PI,Math.PI}},
                new double[][]{new double[]{-100,100},new double[]{0,2*Math.PI},new double[]{-100,100}}}) {

            final double[] args1 = args123[0];
            final double[] args2 = args123[1];
            final double[] args3 = args123[2];

            final double[] values1 = randomDoubleTabSmart(args1);
            final double[] values2 = randomDoubleTabSmart(args2);
            final double[] values3 = randomDoubleTabSmart(args3);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy &= FastMath.isInClockwiseDomain(
                    values1[j],
                    values2[j],
                    values3[j]);
            }
            System.out.println("Loop on FastMath.isInClockwiseDomain(double,double,double), args in "+toStringSmart(args1,args2,args3)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values1 = randomDoubleTabSmart(new double[]{});
            final double[] values2 = randomDoubleTabSmart(new double[]{});
            final double[] values3 = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy &= FastMath.isInClockwiseDomain(
                    values1[j],
                    values2[j],
                    values3[j]);
            }
        }

        useDummy(dummy);
    }

    /*
     * hyperbolic trigonometry
     */

    private void test_sinh_double() {
        double dummy = 0.0;

        System.out.println("--- testing sinh(double) ---");

        for (double[] args : new double[][]{
                new double[]{-1,1},
                new double[]{-10,10},
                new double[]{-700,700},
                new double[]{-750,720},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.sinh(values[j]);
            }
            System.out.println("Loop on     Math.sinh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.sinh(values[j]);
            }
            System.out.println("Loop on FastMath.sinh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.sinh(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_cosh_double() {
        double dummy = 0.0;

        System.out.println("--- testing cosh(double) ---");

        for (double[] args : new double[][]{
                new double[]{-1,1},
                new double[]{-10,10},
                new double[]{-700,700},
                new double[]{-750,720},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.cosh(values[j]);
            }
            System.out.println("Loop on     Math.cosh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.cosh(values[j]);
            }
            System.out.println("Loop on FastMath.cosh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.cosh(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_coshm1_double() {
        double dummy = 0.0;

        System.out.println("--- testing coshm1(double) ---");

        for (double[] args : new double[][]{
                new double[]{-1,1},
                new double[]{-10,10},
                new double[]{-700,700},
                new double[]{-750,720},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.coshm1(values[j]);
            }
            System.out.println("Loop on FastMath.coshm1(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.coshm1(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_sinhAndCosh_double_DoubleWrapper() {
        DoubleWrapper hcosine = new DoubleWrapper();
        double dummy = 0.0;

        System.out.println("--- testing sinhAndCosh(double,DoubleWrapper) ---");

        for (double[] args : new double[][]{
                new double[]{-1,1},
                new double[]{-10,10},
                new double[]{-700,700},
                new double[]{-750,720},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            double hsine = FastMath.sinhAndCosh(values[j],hcosine);
            dummy += hsine + hcosine.value;
            }
            System.out.println("Loop on FastMath.sinhAndCosh(double,DoubleWrapper), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.sinhAndCosh(values[j],hcosine);
            }
        }

        useDummy(dummy);
    }

    private void test_tanh_double() {
        double dummy = 0.0;

        System.out.println("--- testing tanh(double) ---");

        for (double[] args : new double[][]{
                new double[]{-1,1},
                new double[]{-10,10},
                new double[]{-700,700},
                new double[]{-750,720},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.tanh(values[j]);
            }
            System.out.println("Loop on     Math.tanh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.tanh(values[j]);
            }
            System.out.println("Loop on FastMath.tanh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.tanh(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_asinh_double() {
        double dummy = 0.0;

        System.out.println("--- testing asinh(double) ---");

        for (double[] args : new double[][]{
                new double[]{-0.04,0.04},
                new double[]{-DOUBLE_1E6,DOUBLE_1E6},
                new double[]{-DOUBLE_1E12,DOUBLE_1E12},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.asinh(values[j]);
            }
            System.out.println("Loop on FastMath.asinh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.asinh(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_acosh_double() {
        double dummy = 0.0;

        System.out.println("--- testing acosh(double) ---");

        for (double[] args : new double[][]{
                new double[]{1,DOUBLE_1E6},
                new double[]{1,DOUBLE_1E12},
                new double[]{0,1000,1}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.acosh(values[j]);
            }
            System.out.println("Loop on FastMath.acosh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.acosh(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_acosh1p_double() {
        double dummy = 0.0;

        System.out.println("--- testing acosh1p(double) ---");

        for (double[] args : new double[][]{
                new double[]{1,DOUBLE_1E6},
                new double[]{1,DOUBLE_1E12},
                new double[]{0,1000,1}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.acosh1p(values[j]);
            }
            System.out.println("Loop on FastMath.acosh1p(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.acosh1p(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_atanh_double() {
        double dummy = 0.0;

        System.out.println("--- testing atanh(double) ---");

        for (double[] args : new double[][]{
                new double[]{-1,1},
                new double[]{-1000,0,0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.atanh(values[j]);
            }
            System.out.println("Loop on FastMath.atanh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.atanh(values[j]);
            }
        }

        useDummy(dummy);
    }

    /*
     * exponentials
     */

    private void test_exp_double() {
        double dummy = 0.0;

        System.out.println("--- testing exp(double) ---");

        for (double[] args : new double[][]{
                new double[]{-1,1},
                new double[]{-10,10},
                new double[]{-700,700},
                new double[]{-750,720},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.exp(values[j]);
            }
            System.out.println("Loop on     Math.exp(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.exp(values[j]);
            }
            System.out.println("Loop on FastMath.exp(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.exp(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_expQuick_double() {
        double dummy = 0.0;

        System.out.println("--- testing expQuick(double) ---");

        for (double[] args : new double[][]{
                new double[]{-1,1},
                new double[]{-10,10},
                new double[]{-700,700}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.exp(values[j]);
            }
            System.out.println("Loop on          Math.exp(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.expQuick(values[j]);
            }
            System.out.println("Loop on FastMath.expQuick(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.expQuick(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_expm1_double() {
        double dummy = 0.0;

        System.out.println("--- testing expm1(double) ---");

        for (double[] args : new double[][]{
                new double[]{-1,1},
                new double[]{-10,10},
                new double[]{-700,700},
                new double[]{-750,720},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.expm1(values[j]);
            }
            System.out.println("Loop on     Math.expm1(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.expm1(values[j]);
            }
            System.out.println("Loop on FastMath.expm1(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.expm1(values[j]);
            }
        }

        useDummy(dummy);
    }

    /*
     * logarithms
     */

    private void test_log_double() {
        double dummy = 0.0;

        System.out.println("--- testing log(double) ---");

        for (double[] args : new double[][]{
                new double[]{0.1,10}, // values around 1, such as log(x) goes around -1 and 0
                new double[]{1}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.log(values[j]);
            }
            System.out.println("Loop on     Math.log(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.log(values[j]);
            }
            System.out.println("Loop on FastMath.log(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.log(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_logQuick_double() {
        double dummy = 0.0;

        System.out.println("--- testing logQuick(double) ---");

        for (double[] args : new double[][]{
                new double[]{0.1,10},
                new double[]{2}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.log(values[j]);
            }
            System.out.println("Loop on          Math.log(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.logQuick(values[j]);
            }
            System.out.println("Loop on FastMath.logQuick(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.logQuick(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_log10_double() {
        double dummy = 0.0;

        System.out.println("--- testing log10(double) ---");

        for (double[] args : new double[][]{
                new double[]{0.1,10},
                new double[]{1}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.log10(values[j]);
            }
            System.out.println("Loop on     Math.log10(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.log10(values[j]);
            }
            System.out.println("Loop on FastMath.log10(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.log10(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_log1p_double() {
        double dummy = 0.0;

        System.out.println("--- testing log1p(double) ---");

        for (double[] args : new double[][]{
                new double[]{0.1-1,10-1}, // values around 0, such as log1p(x) goes around -1 and 0
                new double[]{1}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.log1p(values[j]);
            }
            System.out.println("Loop on     Math.log1p(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.log1p(values[j]);
            }
            System.out.println("Loop on FastMath.log1p(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.log1p(values[j]);
            }
        }

        useDummy(dummy);
    }

    /*
     * powers
     */

    private void test_pow_2double() {
        double dummy = 0.0;

        System.out.println("--- testing pow(double,double) ---");

        for (double[][] args12 : new double[][][]{
                new double[][]{new double[]{0.1,10},new double[]{-100,100}}, // for comparison with powQuick
                new double[][]{new double[]{-100,100},new double[]{-100,100}},
                new double[][]{new double[]{0},new double[]{0}}}) {

            final double[] args1 = args12[0];
            final double[] args2 = args12[1];

            final double[] values1 = randomDoubleTabSmart(args1);
            final double[] values2 = randomDoubleTabSmart(args2);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.pow(values1[j],values2[j]);
            }
            System.out.println("Loop on     Math.pow(double,double), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.pow(values1[j],values2[j]);
            }
            System.out.println("Loop on FastMath.pow(double,double), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.pow(values[j],values[MASK-j]);
            }
        }

        useDummy(dummy);
    }

    private void test_powQuick_2double() {
        double dummy = 0.0;

        System.out.println("--- testing powQuick(double,double) ---");

        for (double[][] args12 : new double[][][]{
                new double[][]{new double[]{0.1,10},new double[]{-100,100}},
                new double[][]{new double[]{Double.MIN_VALUE,Double.MAX_VALUE},new double[]{0}}}) {

            final double[] args1 = args12[0];
            final double[] args2 = args12[1];

            final double[] values1 = randomDoubleTabSmart(args1);
            final double[] values2 = randomDoubleTabSmart(args2);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.pow(values1[j],values2[j]);
            }
            System.out.println("Loop on          Math.pow(double,double), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.powQuick(values1[j],values2[j]);
            }
            System.out.println("Loop on FastMath.powQuick(double,double), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.powQuick(values[j],values[MASK-j]);
            }
        }

        useDummy(dummy);
    }

    private void test_powFast_double_int() {
        double dummy = 0.0;

        System.out.println("--- testing powFast(double,int) ---");

        for (double[][] args12 : new double[][][]{
                new double[][]{new double[]{-10,10},new double[]{0,10}},
                new double[][]{new double[]{-10,10},new double[]{-10,10}},
                new double[][]{new double[]{-10,10,0},new double[]{-10,10}},
                new double[][]{new double[]{-100,100,0},new double[]{-10,10}},
                new double[][]{new double[]{-10,10},new double[]{-100,100}}}) {

            final double[] args1 = args12[0];
            final int[] args2 = toIntTab(args12[1]);

            final double[] values1 = randomDoubleTabSmart(args1);
            final int[] values2 = randomIntTabSmart(args2);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.pow(values1[j],(double)values2[j]);
            }
            System.out.println("Loop on      Math.pow(double,double), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.powFast(values1[j],values2[j]);
            }
            System.out.println("Loop on FastMath.powFast(double,int), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values1 = randomDoubleTabSmart(new double[]{});
            final int[] values2 = randomIntTabSmart(new int[]{0});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.powFast(values1[j],values2[j]);
            }
        }

        useDummy(dummy);
    }

    /*
     * roots
     */

    private void test_sqrt_double() {
        double dummy = 0.0;

        System.out.println("--- testing sqrt(double) ---");

        for (double[] args : new double[][]{
                new double[]{0,10},
                new double[]{0,DOUBLE_1E12},
                new double[]{1}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.sqrt(values[j]);
            }
            System.out.println("Loop on     Math.sqrt(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.sqrt(values[j]);
            }
            System.out.println("Loop on FastMath.sqrt(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.sqrt(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_sqrtQuick_double() {
        double dummy = 0.0;

        System.out.println("--- testing sqrtQuick(double) ---");

        for (double[] args : new double[][]{
                new double[]{2}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.sqrt(values[j]);
            }
            System.out.println("Loop on          Math.sqrt(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.sqrtQuick(values[j]);
            }
            System.out.println("Loop on FastMath.sqrtQuick(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.sqrtQuick(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_invSqrtQuick_double() {
        double dummy = 0.0;

        System.out.println("--- testing invSqrtQuick(double) ---");

        for (double[] args : new double[][]{
                new double[]{2}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += 1/Math.sqrt(values[j]);
            }
            System.out.println("Loop on           1/Math.sqrt(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.invSqrtQuick(values[j]);
            }
            System.out.println("Loop on FastMath.invSqrtQuick(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.invSqrtQuick(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_cbrt_double() {
        double dummy = 0.0;

        System.out.println("--- testing cbrt(double) ---");

        for (double[] args : new double[][]{
                new double[]{-10,10},
                new double[]{-DOUBLE_1E12,DOUBLE_1E12},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.cbrt(values[j]);
            }
            System.out.println("Loop on     Math.cbrt(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.cbrt(values[j]);
            }
            System.out.println("Loop on FastMath.cbrt(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.cbrt(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_hypot_2double() {
        double dummy = 0.0;

        System.out.println("--- testing hypot(double,double) ---");

        for (double[] args : new double[][]{
                new double[]{0}}) {

            final double[] valuesX = randomDoubleTabSmart(args);

            for (int k=0;k<2;k++) {
                final boolean similarMagnitudes = (k == 0);
                final double[] valuesY;
                final String bonus;
                if (similarMagnitudes) {
                    valuesY = new double[valuesX.length];
                    for (int i=0;i<valuesX.length;i++) {
                        double valueX = valuesX[MASK-i];
                        double valueY = valueX + Math.pow(2, 18 * randomDoubleUniform(-1.0,1.0));
                        valuesY[i] = valueY;
                    }
                    bonus = " (close magnitudes)";
                } else {
                    valuesY = randomDoubleTabSmart(args);
                    bonus = "";
                }

                startTimer();
                for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
                dummy += Math.hypot(valuesX[j],valuesY[j]);
                }
                System.out.println("Loop on     Math.hypot(double,double), args in "+toStringSmart(args)+bonus+", took "+getElapsedSeconds()+" s");

                startTimer();
                for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
                dummy += FastMath.hypot(valuesX[j],valuesY[j]);
                }
                System.out.println("Loop on FastMath.hypot(double,double), args in "+toStringSmart(args)+bonus+", took "+getElapsedSeconds()+" s");
            }
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.hypot(values[j],values[MASK-j]);
            }
        }

        useDummy(dummy);
    }

    private void test_hypot_3double() {
        double dummy = 0.0;

        System.out.println("--- testing hypot(double,double,double) ---");

        for (double[] args : new double[][]{
                new double[]{0}}) {

            final double[] valuesX = randomDoubleTabSmart(args);

            for (int k=0;k<3;k++) {
                final boolean similarMagnitudes = (k <= 1);
                final boolean zeroZ = (k == 1);
                final double[] valuesY;
                final double[] valuesZ;
                final String bonus;
                if (similarMagnitudes) {
                    valuesY = new double[valuesX.length];
                    valuesZ = new double[valuesX.length];
                    for (int i=0;i<valuesX.length;i++) {
                        double valueX = valuesX[MASK-i];
                        double valueY = valueX + Math.pow(2, 18 * randomDoubleUniform(-1.0,1.0));
                        double valueZ;
                        if (zeroZ) {
                            valueZ = 0.0;
                        } else {
                            valueZ = valueY + Math.pow(2, 18 * randomDoubleUniform(-1.0,1.0));
                        }
                        valuesY[i] = valueY;
                        valuesZ[i] = valueZ;
                    }
                    if (zeroZ) {
                        bonus = " (close magnitudes, z = 0.0)";
                    } else {
                        bonus = " (close magnitudes)";
                    }
                } else {
                    valuesY = randomDoubleTabSmart(args);
                    valuesZ = randomDoubleTabSmart(args);
                    bonus = "";
                }

                startTimer();
                for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
                dummy += FastMath.hypot(valuesX[j],valuesY[j],valuesZ[j]);
                }
                System.out.println("Loop on FastMath.hypot(double,double,double), args in "+toStringSmart(args)+bonus+", took "+getElapsedSeconds()+" s");
            }
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.hypot(values[j],values[MASK-j],values[MASK-(j/2)]);
            }
        }

        useDummy(dummy);
    }

    /*
     * close values
     */

    private void test_floor_float() {
        float dummy = 0.0f;

        System.out.println("--- testing floor(float) ---");

        for (float[] args : new float[][]{
                new float[]{-10,10},
                new float[]{-(float)DOUBLE_1E6,(float)DOUBLE_1E6},
                new float[]{-FLOAT_COMMA_LIMIT,FLOAT_COMMA_LIMIT},
                new float[]{0}}) {

            final float[] values = randomFloatTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.floor(values[j]);
            }
            System.out.println("Loop on FastMath.floor(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.floor(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_floor_double() {
        double dummy = 0.0;

        System.out.println("--- testing floor(double) ---");

        for (double[] args : new double[][]{
                new double[]{-10,10},
                new double[]{-DOUBLE_1E6,DOUBLE_1E6},
                new double[]{-DOUBLE_COMMA_LIMIT,DOUBLE_COMMA_LIMIT},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.floor(values[j]);
            }
            System.out.println("Loop on     Math.floor(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.floor(values[j]);
            }
            System.out.println("Loop on FastMath.floor(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.floor(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_ceil_float() {
        float dummy = 0.0f;

        System.out.println("--- testing ceil(float) ---");

        for (float[] args : new float[][]{
                new float[]{-10,10},
                new float[]{-(float)DOUBLE_1E6,(float)DOUBLE_1E6},
                new float[]{-FLOAT_COMMA_LIMIT,FLOAT_COMMA_LIMIT},
                new float[]{0}}) {

            final float[] values = randomFloatTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.ceil(values[j]);
            }
            System.out.println("Loop on FastMath.ceil(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.ceil(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_ceil_double() {
        double dummy = 0.0;

        System.out.println("--- testing ceil(double) ---");

        for (double[] args : new double[][]{
                new double[]{-10,10},
                new double[]{-DOUBLE_1E6,DOUBLE_1E6},
                new double[]{-DOUBLE_COMMA_LIMIT,DOUBLE_COMMA_LIMIT},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.ceil(values[j]);
            }
            System.out.println("Loop on     Math.ceil(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.ceil(values[j]);
            }
            System.out.println("Loop on FastMath.ceil(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.ceil(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_round_float() {
        int dummy = 0;

        System.out.println("--- testing round(float) ---");

        for (float[] args : new float[][]{
                new float[]{-10,10},
                new float[]{-(float)DOUBLE_1E6,(float)DOUBLE_1E6},
                new float[]{-FLOAT_COMMA_LIMIT,FLOAT_COMMA_LIMIT},
                new float[]{0}}) {

            final float[] values = randomFloatTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.round(values[j]);
            }
            System.out.println("Loop on     Math.round(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.round(values[j]);
            }
            System.out.println("Loop on FastMath.round(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.round(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_round_double() {
        long dummy = 0;

        System.out.println("--- testing round(double) ---");

        for (double[] args : new double[][]{
                new double[]{-10,10},
                new double[]{-DOUBLE_1E6,DOUBLE_1E6},
                new double[]{-DOUBLE_COMMA_LIMIT,DOUBLE_COMMA_LIMIT},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.round(values[j]);
            }
            System.out.println("Loop on     Math.round(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.round(values[j]);
            }
            System.out.println("Loop on FastMath.round(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.round(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_roundEven_float() {
        int dummy = 0;

        System.out.println("--- testing roundEven(float) ---");

        for (float[] args : new float[][]{
                new float[]{-10,10},
                new float[]{-(float)DOUBLE_1E6,(float)DOUBLE_1E6},
                new float[]{-FLOAT_COMMA_LIMIT,FLOAT_COMMA_LIMIT},
                new float[]{0}}) {

            final float[] values = randomFloatTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.round(values[j]);
            }
            System.out.println("Loop on         Math.round(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.roundEven(values[j]);
            }
            System.out.println("Loop on FastMath.roundEven(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.roundEven(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_roundEven_double() {
        long dummy = 0;

        System.out.println("--- testing roundEven(double) ---");

        for (double[] args : new double[][]{
                new double[]{-10,10},
                new double[]{-DOUBLE_1E6,DOUBLE_1E6},
                new double[]{-DOUBLE_COMMA_LIMIT,DOUBLE_COMMA_LIMIT},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.round(values[j]);
            }
            System.out.println("Loop on         Math.round(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.roundEven(values[j]);
            }
            System.out.println("Loop on FastMath.roundEven(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.roundEven(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_rint_float() {
        float dummy = 0;

        System.out.println("--- testing rint(float) ---");

        for (float[] args : new float[][]{
                new float[]{-10,10},
                new float[]{-(float)DOUBLE_1E6,(float)DOUBLE_1E6},
                new float[]{-FLOAT_COMMA_LIMIT,FLOAT_COMMA_LIMIT},
                new float[]{0}}) {

            final float[] values = randomFloatTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.rint(values[j]);
            }
            System.out.println("Loop on FastMath.rint(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.rint(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_rint_double() {
        double dummy = 0;

        System.out.println("--- testing rint(double) ---");

        for (double[] args : new double[][]{
                new double[]{-10,10},
                new double[]{-DOUBLE_1E6,DOUBLE_1E6},
                new double[]{-DOUBLE_COMMA_LIMIT,DOUBLE_COMMA_LIMIT},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.rint(values[j]);
            }
            System.out.println("Loop on     Math.rint(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.rint(values[j]);
            }
            System.out.println("Loop on FastMath.rint(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.rint(values[j]);
            }
        }

        useDummy(dummy);
    }

    /*
     * binary operators (/,%)
     */

    private void test_remainder_2double() {
        double dummy = 0.0;

        System.out.println("--- testing remainder(double,double) ---");

        for (double[] args : new double[][]{
                new double[]{-10,10},
                new double[]{-DOUBLE_1E6,DOUBLE_1E6},
                new double[]{-DOUBLE_1E12,DOUBLE_1E12},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.IEEEremainder(values[j],values[MASK-j]);
            }
            System.out.println("Loop on Math.IEEEremainder(double,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.remainder(values[j],values[MASK-j]);
            }
            System.out.println("Loop on FastMath.remainder(double,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.remainder(values[j],values[MASK-j]);
            }
        }

        useDummy(dummy);
    }

    private void test_normalizeMinusPiPi() {
        double dummy = 0.0;

        System.out.println("--- testing normalizeMinusPiPi(double) ---");

        for (double[] args : new double[][]{
                new double[]{-Math.PI,Math.PI},
                new double[]{-2*Math.PI,2*Math.PI},
                new double[]{-100,100},
                new double[]{-DOUBLE_1E12,DOUBLE_1E12},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.normalizeMinusPiPi(values[j]);
            }
            System.out.println("Loop on FastMath.normalizeMinusPiPi(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.normalizeMinusPiPi(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_normalizeMinusPiPiFast() {
        double dummy = 0.0;

        System.out.println("--- testing normalizeMinusPiPiFast(double) ---");

        for (double[] args : new double[][]{
                new double[]{-Math.PI,Math.PI},
                new double[]{-2*Math.PI,2*Math.PI},
                new double[]{-100,100},
                new double[]{-DOUBLE_1E12,DOUBLE_1E12},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.normalizeMinusPiPiFast(values[j]);
            }
            System.out.println("Loop on FastMath.normalizeMinusPiPiFast(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.normalizeMinusPiPiFast(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_normalizeZeroTwoPi() {
        double dummy = 0.0;

        System.out.println("--- testing normalizeZeroTwoPi(double) ---");

        for (double[] args : new double[][]{
                new double[]{0,2*Math.PI},
                new double[]{-2*Math.PI,2*Math.PI},
                new double[]{-100,100},
                new double[]{-DOUBLE_1E12,DOUBLE_1E12},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.normalizeZeroTwoPi(values[j]);
            }
            System.out.println("Loop on FastMath.normalizeZeroTwoPi(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.normalizeZeroTwoPi(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_normalizeZeroTwoPiFast() {
        double dummy = 0.0;

        System.out.println("--- testing normalizeZeroTwoPiFast(double) ---");

        for (double[] args : new double[][]{
                new double[]{0,2*Math.PI},
                new double[]{-2*Math.PI,2*Math.PI},
                new double[]{-100,100},
                new double[]{-DOUBLE_1E12,DOUBLE_1E12},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.normalizeZeroTwoPiFast(values[j]);
            }
            System.out.println("Loop on FastMath.normalizeZeroTwoPiFast(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.normalizeZeroTwoPiFast(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_normalizeMinusHalfPiHalfPi() {
        double dummy = 0.0;

        System.out.println("--- testing normalizeMinusHalfPiHalfPi(double) ---");

        for (double[] args : new double[][]{
                new double[]{-Math.PI/2,Math.PI/2},
                new double[]{-2*Math.PI,2*Math.PI},
                new double[]{-100,100},
                new double[]{-DOUBLE_1E12,DOUBLE_1E12},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.normalizeMinusHalfPiHalfPi(values[j]);
            }
            System.out.println("Loop on FastMath.normalizeMinusHalfPiHalfPi(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.normalizeMinusHalfPiHalfPi(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_normalizeMinusHalfPiHalfPiFast() {
        double dummy = 0.0;

        System.out.println("--- testing normalizeMinusHalfPiHalfPiFast(double) ---");

        for (double[] args : new double[][]{
                new double[]{-Math.PI/2,Math.PI/2},
                new double[]{-2*Math.PI,2*Math.PI},
                new double[]{-100,100},
                new double[]{-DOUBLE_1E12,DOUBLE_1E12},
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.normalizeMinusHalfPiHalfPiFast(values[j]);
            }
            System.out.println("Loop on FastMath.normalizeMinusHalfPiHalfPiFast(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.normalizeMinusHalfPiHalfPiFast(values[j]);
            }
        }

        useDummy(dummy);
    }

    /*
     * floating points utils
     */

    private void test_getExponent_float() {
        int dummy = 0;

        System.out.println("--- testing getExponent(float) ---");

        for (float[] args : new float[][]{
                new float[]{0}}) {

            final float[] values = randomFloatTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.getExponent(values[j]);
            }
            System.out.println("Loop on     Math.getExponent(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.getExponent(values[j]);
            }
            System.out.println("Loop on FastMath.getExponent(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s... ");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.getExponent(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_getExponent_double() {
        int dummy = 0;

        System.out.println("--- testing getExponent(double) ---");

        for (double[] args : new double[][]{
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.getExponent(values[j]);
            }
            System.out.println("Loop on     Math.getExponent(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.getExponent(values[j]);
            }
            System.out.println("Loop on FastMath.getExponent(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s... ");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.getExponent(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_signum_float() {
        float dummy = 0;

        System.out.println("--- testing signum(float) ---");

        for (float[] args : new float[][]{
                new float[]{0}}) {

            final float[] values = randomFloatTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.signum(values[j]);
            }
            System.out.println("Loop on     Math.signum(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.signum(values[j]);
            }
            System.out.println("Loop on FastMath.signum(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.signum(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_signum_double() {
        double dummy = 0;

        System.out.println("--- testing signum(double) ---");

        for (double[] args : new double[][]{
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.signum(values[j]);
            }
            System.out.println("Loop on     Math.signum(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.signum(values[j]);
            }
            System.out.println("Loop on FastMath.signum(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.signum(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_signFromBit_float() {
        int dummy = 0;

        System.out.println("--- testing signFromBit(float) ---");

        for (float[] args : new float[][]{
                new float[]{0}}) {

            final float[] values = randomFloatTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.signFromBit(values[j]);
            }
            System.out.println("Loop on FastMath.signFromBit(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.signFromBit(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_signFromBit_double() {
        long dummy = 0;

        System.out.println("--- testing signFromBit(double) ---");

        for (double[] args : new double[][]{
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.signFromBit(values[j]);
            }
            System.out.println("Loop on FastMath.signFromBit(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.signFromBit(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_copySign_2float() {
        float dummy = 0;

        System.out.println("--- testing copySign(float,float) ---");

        for (float[] args : new float[][]{
                new float[]{0}}) {

            final float[] values = randomFloatTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.copySign(values[j],values[MASK-j]);
            }
            System.out.println("Loop on     Math.copySign(float,float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.copySign(values[j],values[MASK-j]);
            }
            System.out.println("Loop on FastMath.copySign(float,float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.copySign(values[j],values[MASK-j]);
            }
        }

        useDummy(dummy);
    }

    private void test_copySign_2double() {
        double dummy = 0;

        System.out.println("--- testing copySign(double,double) ---");

        for (double[] args : new double[][]{
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.copySign(values[j],values[MASK-j]);
            }
            System.out.println("Loop on     Math.copySign(double,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.copySign(values[j],values[MASK-j]);
            }
            System.out.println("Loop on FastMath.copySign(double,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.copySign(values[j],values[MASK-j]);
            }
        }

        useDummy(dummy);
    }

    private void test_ulp_float() {
        float dummy = 0;

        System.out.println("--- testing ulp(float) ---");

        for (float[] args : new float[][]{
                new float[]{0}}) {

            final float[] values = randomFloatTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.ulp(values[j]);
            }
            System.out.println("Loop on     Math.ulp(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.ulp(values[j]);
            }
            System.out.println("Loop on FastMath.ulp(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.ulp(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_ulp_double() {
        double dummy = 0;

        System.out.println("--- testing ulp(double) ---");

        for (double[] args : new double[][]{
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.ulp(values[j]);
            }
            System.out.println("Loop on     Math.ulp(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.ulp(values[j]);
            }
            System.out.println("Loop on FastMath.ulp(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.ulp(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_nextAfter_float_double() {
        float dummy = 0;

        System.out.println("--- testing nextAfter(float,double) ---");

        {
            final float[] args1 = new float[]{0};
            final double[] args2 = new double[]{0};

            final float[] values1 = randomFloatTabSmart(args1);
            final double[] values2 = randomDoubleTabSmart(args2);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.nextAfter(values1[j],values2[j]);
            }
            System.out.println("Loop on     Math.nextAfter(float,double), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.nextAfter(values1[j],values2[j]);
            }
            System.out.println("Loop on FastMath.nextAfter(float,double), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values1 = randomFloatTabSmart(new float[]{});
            final double[] values2 = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.nextAfter(values1[j],values2[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_nextAfter_2double() {
        double dummy = 0;

        System.out.println("--- testing nextAfter(double,double) ---");

        for (double[] args : new double[][]{
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.nextAfter(values[j],values[MASK-j]);
            }
            System.out.println("Loop on     Math.nextAfter(double,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.nextAfter(values[j],values[MASK-j]);
            }
            System.out.println("Loop on FastMath.nextAfter(double,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values1 = randomDoubleTabSmart(new double[]{});
            final double[] values2 = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.nextAfter(values1[j],values2[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_nextDown_float() {
        float dummy = 0;

        System.out.println("--- testing nextDown(float) ---");

        for (float[] args : new float[][]{
                new float[]{0}}) {

            final float[] values = randomFloatTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.nextDown(values[j]);
            }
            System.out.println("Loop on FastMath.nextDown(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.nextDown(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_nextDown_double() {
        double dummy = 0;

        System.out.println("--- testing nextDown(double) ---");

        for (double[] args : new double[][]{
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.nextDown(values[j]);
            }
            System.out.println("Loop on FastMath.nextDown(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.nextDown(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_nextUp_float() {
        float dummy = 0;

        System.out.println("--- testing nextUp(float) ---");

        for (float[] args : new float[][]{
                new float[]{0}}) {

            final float[] values = randomFloatTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.nextUp(values[j]);
            }
            System.out.println("Loop on     Math.nextUp(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.nextUp(values[j]);
            }
            System.out.println("Loop on FastMath.nextUp(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.nextUp(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_nextUp_double() {
        double dummy = 0;

        System.out.println("--- testing nextUp(double) ---");

        for (double[] args : new double[][]{
                new double[]{0}}) {

            final double[] values = randomDoubleTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.nextUp(values[j]);
            }
            System.out.println("Loop on     Math.nextUp(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.nextUp(values[j]);
            }
            System.out.println("Loop on FastMath.nextUp(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.nextUp(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_scalb_float_int() {
        float dummy = 0;

        System.out.println("--- testing scalb(float,int) ---");

        {
            final float[] args1 = new float[]{0};
            final float[] values1 = randomFloatTabSmart(args1);

            for (int[] args2 : new int[][]{
                    new int[]{-10,10},
                    new int[]{-50,50},
                    new int[]{-127*4,127*4}}) {

                final int[] values2 = randomIntTabSmart(args2);

                startTimer();
                for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
                dummy += Math.scalb(values1[j],values2[j]);
                }
                System.out.println("Loop on     Math.scalb(float,int), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");

                startTimer();
                for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
                dummy += FastMath.scalb(values1[j],values2[j]);
                }
                System.out.println("Loop on FastMath.scalb(float,int), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
            }
        }

        // NaN-crash test.
        {
            final float[] values1 = randomFloatTabSmart(new float[]{});
            final int[] values2 = randomIntTabSmart(new int[]{0});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.scalb(values1[j],values2[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_scalb_double_int() {
        double dummy = 0;

        System.out.println("--- testing scalb(double,int) ---");

        {
            final double[] args1 = new double[]{0};
            final double[] values1 = randomDoubleTabSmart(args1);

            for (int[] args2 : new int[][]{
                    new int[]{-10,10},
                    new int[]{-500,500},
                    new int[]{-1023*4,1023*4}}) {

                final int[] values2 = randomIntTabSmart(args2);

                startTimer();
                for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
                dummy += Math.scalb(values1[j],values2[j]);
                }
                System.out.println("Loop on     Math.scalb(double,int), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");

                startTimer();
                for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
                dummy += FastMath.scalb(values1[j],values2[j]);
                }
                System.out.println("Loop on FastMath.scalb(double,int), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
            }
        }

        // NaN-crash test.
        {
            final double[] values1 = randomDoubleTabSmart(new double[]{});
            final int[] values2 = randomIntTabSmart(new int[]{0});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += FastMath.scalb(values1[j],values2[j]);
            }
        }

        useDummy(dummy);
    }
}
