/*
 * Copyright 2012-2013 Jeff Hain
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

import java.util.Random;

import junit.framework.TestCase;

public class FastMathTest extends TestCase {

    /*
     * To ensure that some of these tests can run with Java 5, we use FastMath
     * versions of nextAfter/nextUp/nextDown/scalb, which are tested against
     * their Java 6+ implementations.
     * 
     * For some methods, not testing them against JDK ones, either because they
     * are not available in lowest required Java version, or because their
     * semantics might change depending on Java version.
     * 
     * Epsilons are chosen for tests to pass even if using redefined log(double)
     * and sqrt(double) methods. Since by default they delegate to Math, the
     * accuracy of depending methods might be better than used epsilons could
     * let believe.
     */

    //--------------------------------------------------------------------------
    // CONFIGURATION
    //--------------------------------------------------------------------------
    
    private static final int NBR_OF_VALUES = 1000 * 1000;

    private static final boolean SETTING = false;
    
    private final Random random = new Random(123456789L);
    
    //--------------------------------------------------------------------------
    // MEMBERS
    //--------------------------------------------------------------------------

    /**
     * Double.MIN_NORMAL since Java 6.
     */
    private static final double DOUBLE_MIN_NORMAL = 0x1.0p-1022; // 2.2250738585072014E-308

    private static final double EPSILON_1EM15 = 1e-15;
    private static final double EPSILON_1EM14 = 1e-14;
    private static final double EPSILON_1EM13 = 1e-13;
    private static final double EPSILON_1EM12 = 1e-12;
    private static final double EPSILON_1EM9 = 1e-9;
    private static final double EPSILON_1EM8 = 1e-8;
    private static final double EPSILON_1EM7 = 1e-7;
    
    private static final double MAX_VALUE_FAST_NORM = ((1L<<52) * (2*Math.PI)) / 1e2;
    private static final double EPSILON_FAST_NORM = 1e-7;

    private final NumbersTestUtils utils = new NumbersTestUtils(this.random);

    //--------------------------------------------------------------------------
    // PUBLIC METHODS
    //--------------------------------------------------------------------------
    
    /*
     * trigonometry
     */
    
    public void test_sin_double() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever() * (Math.PI/2);
            double ref = StrictMath.sin(value);
            double res = FastMath.sin(value);
            double minDelta = minDelta(ref, res);
            boolean ok = (minDelta < EPSILON_1EM14);
            if ((!ok) || (SETTING && (minDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_sin_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("minDelta = "+minDelta);
            }
            worseDelta = Math.max(worseDelta, minDelta);
            assertTrue(ok);
        }
    }

    public void test_sinQuick_double() {
        final double bound = Integer.MAX_VALUE * (2*Math.PI/(1<<11));
        final double epsilon = 1.6e-3;
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleUniform(-bound,bound);
            double ref = StrictMath.sin(value);
            double res = FastMath.sinQuick(value);
            double minDelta = minDelta(ref, res);
            boolean ok = (minDelta < epsilon);
            if ((!ok) || (SETTING && (minDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_sinQuick_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("minDelta = "+minDelta);
            }
            worseDelta = Math.max(worseDelta, minDelta);
            assertTrue(ok);
        }
    }

    public void test_cos_double() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever() * (Math.PI/2);
            double ref = StrictMath.cos(value);
            double res = FastMath.cos(value);
            double minDelta = minDelta(ref, res);
            boolean ok = (minDelta < EPSILON_1EM14);
            if ((!ok) || (SETTING && (minDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_cos_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("minDelta = "+minDelta);
            }
            worseDelta = Math.max(worseDelta, minDelta);
            assertTrue(ok);
        }
    }

    public void test_cosQuick_double() {
        final double bound = Integer.MAX_VALUE * (2*Math.PI/(1<<11));
        final double epsilon = 1.6e-3;
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleUniform(-bound,bound);
            double ref = StrictMath.cos(value);
            double res = FastMath.cosQuick(value);
            double minDelta = minDelta(ref, res);
            boolean ok = (minDelta < epsilon);
            if ((!ok) || (SETTING && (minDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_cosQuick_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("minDelta = "+minDelta);
            }
            worseDelta = Math.max(worseDelta, minDelta);
            assertTrue(ok);
        }
    }

    public void test_sinAndCos_double_DoubleWrapper() {
        final DoubleWrapper tmpCos = new DoubleWrapper();
        double worseDeltaSin = 0.0;
        double worseDeltaCos = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever() * (Math.PI/2);
            double refSin = StrictMath.sin(value);
            double refCos = StrictMath.cos(value);
            double resSin = FastMath.sinAndCos(value, tmpCos);
            double resCos = tmpCos.value;
            double minDeltaSin = minDelta(refSin, resSin);
            double minDeltaCos = minDelta(refCos, resCos);
            boolean ok = (minDeltaSin < EPSILON_1EM14) && (minDeltaCos < EPSILON_1EM14);
            if ((!ok) || (SETTING && ((minDeltaSin > worseDeltaSin) || (minDeltaCos > worseDeltaCos)))) {
                System.out.println();
                System.out.println("test_sinAndCos_double_DoubleWrapper()");
                System.out.println("value = "+value);
                System.out.println("refSin = "+refSin);
                System.out.println("refCos = "+refCos);
                System.out.println("resSin = "+resSin);
                System.out.println("resCos = "+resCos);
                System.out.println("minDeltaSin = "+minDeltaSin);
                System.out.println("minDeltaCos = "+minDeltaCos);
            }
            worseDeltaSin = Math.max(worseDeltaSin, minDeltaSin);
            worseDeltaCos = Math.max(worseDeltaCos, minDeltaCos);
            assertTrue(ok);
        }
    }

    public void test_tan_double() {
        double worseDelta1 = 0.0;
        double worseDelta2 = 0.0;
        double worseDelta3 = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever() * (Math.PI/2);
            double ref = StrictMath.tan(value);
            final double epsilon;
            final int epsilonId;
            if (Math.abs(ref) < 25.0) {
                epsilon = EPSILON_1EM14;
                epsilonId = 1;
            } else if (Math.abs(ref) < 1e7) {
                epsilon = EPSILON_1EM8;
                epsilonId = 2;
            } else {
                epsilon = 0.8;
                epsilonId = 3;
            }
            double res = FastMath.tan(value);
            double minDelta = minDelta(ref, res);
            boolean ok = (minDelta < epsilon);
            if ((!ok) || (SETTING && (minDelta > ((epsilonId == 1) ? worseDelta1 : ((epsilonId == 2) ? worseDelta2 : worseDelta3))))) {
                System.out.println();
                System.out.println("test_tan_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("atan(ref) = "+StrictMath.atan(ref));
                System.out.println("atan(res) = "+StrictMath.atan(res));
                System.out.println("minDelta = "+minDelta+" (max allowed="+epsilon+")");
            }
            if (epsilonId == 1) {
                worseDelta1 = Math.max(worseDelta1, minDelta);
            } else if (epsilonId == 2) {
                worseDelta2 = Math.max(worseDelta2, minDelta);
            } else {
                worseDelta3 = Math.max(worseDelta3, minDelta);
            }
            assertTrue(ok);
        }
    }

    public void test_asin_double() {
        assertEquals(Math.PI/2, FastMath.asin(1.0));
        assertEquals(-Math.PI/2, FastMath.asin(-1.0));
        assertEquals(Double.NaN, FastMath.asin(-1.1));
        assertEquals(Double.NaN, FastMath.asin(1.1));
        assertEquals(Double.NaN, FastMath.asin(Double.NaN));
        
        double worseDelta1 = 0.0;
        double worseDelta2 = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = this.randomDoubleWhatever(-1.0, 1.0);
            double ref = StrictMath.asin(value);
            double res = FastMath.asin(value);
            // Could use better epsilons if using min delta.
            double relDelta = relDelta(ref, res);
            final double epsilon;
            final int epsilonId;
            if (Math.abs(Math.abs(value)-0.5) < 0.4) {
                epsilon = EPSILON_1EM15;
                epsilonId = 1;
            } else {
                epsilon = EPSILON_1EM13;
                epsilonId = 2;
            }
            boolean ok = (relDelta < epsilon);
            if ((!ok) || (SETTING && (relDelta > ((epsilonId == 1) ? worseDelta1 : worseDelta2)))) {
                System.out.println();
                System.out.println("test_asin_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta+" (max allowed="+epsilon+")");
            }
            if (epsilonId == 1) {
                worseDelta1 = Math.max(worseDelta1, relDelta);
            } else {
                worseDelta2 = Math.max(worseDelta2, relDelta);
            }
            assertTrue(ok);
        }
    }

    public void test_asinInRange_double() {
        assertEquals(-Math.PI/2, FastMath.asinInRange(-1.1));
        assertEquals(Math.PI/2, FastMath.asinInRange(1.1));
        assertEquals(FastMath.asin(-0.1), FastMath.asinInRange(-0.1));
        assertEquals(FastMath.asin(0.1), FastMath.asinInRange(0.1));
        assertEquals(Double.NaN, FastMath.asinInRange(Double.NaN));
    }

    public void test_acos_double() {
        assertEquals(0.0, FastMath.acos(1.0));
        assertEquals(Math.PI, FastMath.acos(-1.0));
        assertEquals(Double.NaN, FastMath.acos(-1.1));
        assertEquals(Double.NaN, FastMath.acos(1.1));
        assertEquals(Double.NaN, FastMath.acos(Double.NaN));
        
        double worseDelta1 = 0.0;
        double worseDelta2 = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = this.randomDoubleWhatever(-1.0, 1.0);
            double ref = StrictMath.acos(value);
            double res = FastMath.acos(value);
            // Could use better epsilons if using min delta.
            double relDelta = relDelta(ref, res);
            final double epsilon;
            final int epsilonId;
            if (Math.abs(value) < 0.85) {
                epsilon = EPSILON_1EM15;
                epsilonId = 1;
            } else {
                epsilon = EPSILON_1EM8;
                epsilonId = 2;
            }
            boolean ok = (relDelta < epsilon);
            if ((!ok) || (SETTING && (relDelta > ((epsilonId == 1) ? worseDelta1 : worseDelta2)))) {
                System.out.println();
                System.out.println("test_acos_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta+" (max allowed="+epsilon+")");
            }
            if (epsilonId == 1) {
                worseDelta1 = Math.max(worseDelta1, relDelta);
            } else {
                worseDelta2 = Math.max(worseDelta2, relDelta);
            }
            assertTrue(ok);
        }
    }

    public void test_acosInRange_double() {
        assertEquals(Math.PI, FastMath.acosInRange(-1.1));
        assertEquals(0.0, FastMath.acosInRange(1.1));
        assertEquals(FastMath.acos(-0.1), FastMath.acosInRange(-0.1));
        assertEquals(FastMath.acos(0.1), FastMath.acosInRange(0.1));
        assertEquals(Double.NaN, FastMath.acosInRange(Double.NaN));
    }

    public void test_atan_double() {
        assertEquals(Math.PI/4, FastMath.atan(1.0));
        assertEquals(-Math.PI/4, FastMath.atan(-1.0));
        
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = StrictMath.atan(value);
            double res = FastMath.atan(value);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < EPSILON_1EM14);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_atan_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_atan2_2double() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double y = randomDoubleWhatever();
            double x = randomDoubleWhatever();
            double ref = StrictMath.atan2(y,x);
            double res = FastMath.atan2(y,x);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < EPSILON_1EM14);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_atan2_2double()");
                System.out.println("x = "+x);
                System.out.println("y = "+y);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }
    
    public void test_toRadians_double() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.toRadians(value);
            double res = FastMath.toRadians(value);
            if ((ref == 0.0) && (Math.abs(res) > 0.0)) {
                // Math might underflow before us, due to its
                // computation being done in two steps.
                assertTrue(value / 180.0 == 0.0);
                --i;continue;
            }
            double relDelta = relDelta(ref,res);
            boolean ok = (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_toRadians_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }
    
    public void test_toDegrees_double() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.toDegrees(value);
            double res = FastMath.toDegrees(value);
            if (Double.isInfinite(ref) && (!Double.isInfinite(res))) {
                // Math might overflow before us, due to its
                // computation being done in two steps.
                --i;continue;
            }
            double relDelta = relDelta(ref,res);
            boolean ok = (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_toDegrees_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_toRadians_boolean_2int_double() {
        final double before60 = FastMath.nextDown(60.0);
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            boolean sign = this.random.nextBoolean();
            int degrees = this.random.nextInt(181);
            int minutes = this.random.nextInt(60);
            double seconds = randomDoubleUniform(0.0, before60);
            double ref = (sign ? 1 : -1) * Math.toRadians(degrees + (1.0/60) * (minutes + (1.0/60) * seconds));
            double res = FastMath.toRadians(sign, degrees, minutes, seconds);
            double relDelta = relDelta(ref,res);
            boolean ok = (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_toRadians_boolean_2int_double()");
                System.out.println("sign = "+sign);
                System.out.println("degrees = "+degrees);
                System.out.println("minutes = "+minutes);
                System.out.println("seconds = "+seconds);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_toDegrees_boolean_2int_double() {
        final double before60 = FastMath.nextDown(60.0);
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            boolean sign = this.random.nextBoolean();
            int degrees = this.random.nextInt(181);
            int minutes = this.random.nextInt(60);
            double seconds = randomDoubleUniform(0.0, before60);
            double ref = (sign ? 1 : -1) * (degrees + (1.0/60) * (minutes + (1.0/60) * seconds));
            double res = FastMath.toDegrees(sign, degrees, minutes, seconds);
            double relDelta = relDelta(ref,res);
            boolean ok = (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_toDegrees_boolean_2int_double()");
                System.out.println("sign = "+sign);
                System.out.println("degrees = "+degrees);
                System.out.println("minutes = "+minutes);
                System.out.println("seconds = "+seconds);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    /**
     * Supposes that FastMath.toRadians(boolean,int,int,double) works.
     */
    public void test_toDMS_double_IntWrapper_IntWrapper_DoubleWrapper() {
        IntWrapper resDegrees = new IntWrapper();
        IntWrapper resMinutes = new IntWrapper();
        DoubleWrapper resSeconds = new DoubleWrapper();
        
        final double before60 = FastMath.nextDown(60.0);
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            boolean refSign = this.random.nextBoolean();
            int refDegrees = this.random.nextInt(181);
            int refMinutes = this.random.nextInt(60);
            double refSeconds = randomDoubleUniform(0.0, before60);
            double refRad = FastMath.toRadians(refSign, refDegrees, refMinutes, refSeconds);
            boolean resSign = FastMath.toDMS(refRad, resDegrees, resMinutes, resSeconds);
            double resRad = FastMath.toRadians(resSign, resDegrees.value, resMinutes.value, resSeconds.value);
            
            refRad = refMod(refRad, resRad, 2*Math.PI);
            
            double relDelta = relDelta(refRad,resRad);
            boolean ok =
                NumbersUtils.isInRange(0, 180, resDegrees.value)
                && NumbersUtils.isInRange(0, 59, resMinutes.value)
                && NumbersUtils.isInRange(0.0, before60, resSeconds.value)
                && (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_toDMS_double_IntWrapper_IntWrapper_DoubleWrapper()");
                System.out.println("refSign = "+refSign);
                System.out.println("refDegrees = "+refDegrees);
                System.out.println("refMinutes = "+refMinutes);
                System.out.println("refSeconds = "+refSeconds);
                System.out.println("refRad = "+refRad);
                System.out.println("resSign = "+resSign);
                System.out.println("resDegrees = "+resDegrees);
                System.out.println("resMinutes = "+resMinutes);
                System.out.println("resSeconds = "+resSeconds);
                System.out.println("resRad = "+resRad);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_isInClockwiseDomain_3double() {
        assertTrue(FastMath.isInClockwiseDomain(0.0, 2*Math.PI, 0.0));
        assertTrue(FastMath.isInClockwiseDomain(0.0, 2*Math.PI, -Math.PI));
        assertTrue(FastMath.isInClockwiseDomain(0.0, 2*Math.PI, Math.PI));
        assertTrue(FastMath.isInClockwiseDomain(0.0, 2*Math.PI, 2*Math.PI));
        assertTrue(FastMath.isInClockwiseDomain(-Math.PI, 2*Math.PI, -Math.PI));
        assertTrue(FastMath.isInClockwiseDomain(-Math.PI, 2*Math.PI, 0.0));
        assertTrue(FastMath.isInClockwiseDomain(-Math.PI, 2*Math.PI, Math.PI));

        // always in
        for (int i=-10;i<10;i++) {
            double startAngRad = Math.toRadians(55.0*i);
            double spanAngRad = Math.PI/2;
            double angRad = startAngRad + Math.PI/3;
            assertTrue(FastMath.isInClockwiseDomain(startAngRad, spanAngRad, angRad));
        }

        // never in
        for (int i=-10;i<10;i++) {
            double startAngRad = Math.toRadians(55.0*i);
            double spanAngRad = Math.PI/3;
            double angRad = startAngRad + Math.PI/2;
            assertFalse(FastMath.isInClockwiseDomain(startAngRad, spanAngRad, angRad));
        }

        // small angular values
        assertTrue(FastMath.isInClockwiseDomain(0.0, 2*Math.PI, -1e-10));
        assertFalse(FastMath.isInClockwiseDomain(0.0, 2*Math.PI, -1e-20));
        assertTrue(FastMath.isInClockwiseDomain(0.0, 2*FastMath.PI_SUP, -1e-20));
        assertTrue(FastMath.isInClockwiseDomain(1e-10, 2*Math.PI, -1e-20));
        assertFalse(FastMath.isInClockwiseDomain(1e-20, 2*Math.PI, -1e-20));
        assertTrue(FastMath.isInClockwiseDomain(1e-20, 2*FastMath.PI_SUP, -1e-20));

        // NaN
        assertFalse(FastMath.isInClockwiseDomain(Double.NaN, Math.PI, Math.PI/2));
        assertFalse(FastMath.isInClockwiseDomain(Double.NaN, 3*Math.PI, Math.PI/2));
        assertFalse(FastMath.isInClockwiseDomain(0.0, Math.PI, Double.NaN));
        assertFalse(FastMath.isInClockwiseDomain(0.0, 3*Math.PI, Double.NaN));
        assertFalse(FastMath.isInClockwiseDomain(0.0, Double.NaN, Math.PI/2));
    }

    /*
     * hyperbolic trigonometry
     */

    public void test_sinh_double() {
        assertEquals(Double.NEGATIVE_INFINITY, FastMath.sinh(-711.0));
        assertEquals(Double.POSITIVE_INFINITY, FastMath.sinh(711.0));
        assertEquals(Double.NaN, FastMath.sinh(Double.NaN));

        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = random.nextBoolean() ? randomDoubleWhatever() : randomDoubleWhatever(-711,711);
            double ref = StrictMath.sinh(value);
            double res = FastMath.sinh(value);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_sinh_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }
    
    public void test_cosh_double() {
        assertEquals(Double.POSITIVE_INFINITY, FastMath.cosh(-711.0));
        assertEquals(Double.POSITIVE_INFINITY, FastMath.cosh(711.0));
        assertEquals(Double.NaN, FastMath.cosh(Double.NaN));

        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = random.nextBoolean() ? randomDoubleWhatever() : randomDoubleWhatever(-711,711);
            double ref = StrictMath.cosh(value);
            double res = FastMath.cosh(value);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_cosh_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_coshm1_double() {
        assertEquals(-0.0,FastMath.coshm1(-0.0));
        assertEquals(0.0,FastMath.coshm1(0.0));
        assertEquals(Double.NaN,FastMath.coshm1(Double.NaN));
        
        for (double tiny : new double[]{StrictMath.pow(2, -28),StrictMath.sqrt(2*Double.MIN_VALUE)}) {
            assertEquals(0.5 * tiny*tiny,FastMath.coshm1(tiny));
        }
        
        /*
         * Testing
         * StrictMath.cosh(value) ~= coshm1(value) + 1
         */
        
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = StrictMath.cosh(value);
            double res = FastMath.coshm1(value) + 1;
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_coshm1_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_sinhAndCosh_double_DoubleWrapper() {
        final DoubleWrapper tmpCosh = new DoubleWrapper();
        double worseDeltaSinh = 0.0;
        double worseDeltaCosh = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double refSinh = StrictMath.sinh(value);
            double refCosh = StrictMath.cosh(value);
            double resSinh = FastMath.sinhAndCosh(value, tmpCosh);
            double resCosh = tmpCosh.value;
            double relDeltaSinh = relDelta(refSinh, resSinh);
            double relDeltaCosh = relDelta(refCosh, resCosh);
            boolean ok = (relDeltaSinh < EPSILON_1EM15) && (relDeltaCosh < EPSILON_1EM15);
            if ((!ok) || (SETTING && ((relDeltaSinh > worseDeltaSinh) || (relDeltaCosh > worseDeltaCosh)))) {
                System.out.println();
                System.out.println("test_sinhAndCosh_double_DoubleWrapper()");
                System.out.println("value = "+value);
                System.out.println("refSinh = "+refSinh);
                System.out.println("refCosh = "+refCosh);
                System.out.println("resSinh = "+resSinh);
                System.out.println("resCosh = "+resCosh);
                System.out.println("relDeltaSinh = "+relDeltaSinh);
                System.out.println("relDeltaCosh = "+relDeltaCosh);
            }
            worseDeltaSinh = Math.max(worseDeltaSinh, relDeltaSinh);
            worseDeltaCosh = Math.max(worseDeltaCosh, relDeltaCosh);
            assertTrue(ok);
        }
    }

    public void test_tanh_double() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = StrictMath.tanh(value);
            double res = FastMath.tanh(value);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_tanh_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_asinh_double() {
        assertEquals(-0.0, FastMath.asinh(-0.0));
        assertEquals(0.0, FastMath.asinh(0.0));
        assertEquals(Double.NEGATIVE_INFINITY, FastMath.asinh(Double.NEGATIVE_INFINITY));
        assertEquals(Double.POSITIVE_INFINITY, FastMath.asinh(Double.POSITIVE_INFINITY));
        assertEquals(Double.NaN, FastMath.asinh(Double.NaN));
        
        /*
         * Testing asinh(StrictMath.sinh(value)) ~= value.
         */
        
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever(-711.0, 711.0);
            double sinhValue = StrictMath.sinh(value);
            if (NumbersUtils.isNaNOrInfinite(sinhValue)) {
                --i;continue;
            }
            double asinhSinhValue = FastMath.asinh(sinhValue);
            double relDelta = relDelta(asinhSinhValue, value);
            boolean ok = (relDelta < EPSILON_1EM14);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_asinh_double()");
                System.out.println("value          = "+value);
                System.out.println("sinhValue = "+sinhValue);
                System.out.println("asinhSinhValue = "+asinhSinhValue);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }
    
    public void test_acosh_double() {
        assertEquals(Double.NaN, FastMath.acosh(Double.NaN));
        assertEquals(Double.NaN, FastMath.acosh(-10.0));
        assertEquals(Double.NaN, FastMath.acosh(0.0));
        assertEquals(Double.NaN, FastMath.acosh(0.9));
        assertEquals(0.0, FastMath.acosh(1.0));
        
        assertEquals(Double.POSITIVE_INFINITY, FastMath.acosh(Double.POSITIVE_INFINITY));
        
        /*
         * Testing
         * StrictMath.cosh(acosh(value)) ~= value
         * or
         * acosh(StrictMath.cosh(value)) ~= value
         * (only considering best min delta of both,
         * to avoid bad delta only due to double precision loss,
         * either for cosh(small values), or acosh(big values)).
         */
        
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            // Only using positive-x-half of cosh.
            double value = randomDoubleWhatever(0.0, 711.0);
            double coshValue = StrictMath.cosh(value);
            if (NumbersUtils.isNaNOrInfinite(coshValue)) {
                --i;continue;
            }
            double acoshCoshValue = FastMath.acosh(coshValue);
            double coshAcoshCoshValue = StrictMath.cosh(acoshCoshValue);
            double relDelta1 = relDelta(value, acoshCoshValue);
            double relDelta2 = relDelta(coshValue, coshAcoshCoshValue);
            double relDelta = Math.min(relDelta1, relDelta2);
            boolean ok = (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_acosh_double()");
                System.out.println("value          = "+value);
                System.out.println("coshValue          = "+coshValue);
                System.out.println("acoshCoshValue = "+acoshCoshValue);
                System.out.println("coshAcoshCoshValue = "+coshAcoshCoshValue);
                System.out.println("relDelta(value, acoshCoshValue)         = "+relDelta1);
                System.out.println("relDelta(coshValue, coshAcoshCoshValue) = "+relDelta2);
                System.out.println("relDelta                                = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_acosh1p_double() {
        assertEquals(Double.NaN,FastMath.acosh1p(Double.NaN));
        assertEquals(0.0,FastMath.acosh1p(0.0));
        assertEquals(-0.0,FastMath.acosh1p(-0.0));
        
        /*
         * Testing
         * acosh1p(StrictMath.cosh(value)-1) ~= value
         * (only considering best min delta of both,
         * to avoid bad delta only due to double precision loss,
         * for cosh(small values)).
         */
        
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            // Only using positive-x-half of cosh.
            double value = randomDoubleWhatever(0.0, 711.0);
            double coshValueM1 = StrictMath.cosh(value) - 1;
            if (NumbersUtils.isNaNOrInfinite(coshValueM1)) {
                --i;continue;
            }
            double acosh1pCoshValueM1 = FastMath.acosh1p(coshValueM1); // acosh(1+cosh(value)-1) = value
            double coshAcosh1pCoshValueM1M1 = StrictMath.cosh(acosh1pCoshValueM1)-1; // cosh(acosh(1+cosh(value)-1))-1 = cosh(value)-1
            double relDelta1 = relDelta(value, acosh1pCoshValueM1);
            double relDelta2 = relDelta(coshValueM1, coshAcosh1pCoshValueM1M1);
            double relDelta = Math.min(relDelta1, relDelta2);
            boolean ok = (relDelta < EPSILON_1EM14);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_acosh1p_double() (1)");
                System.out.println("value              = "+value);
                System.out.println("coshValueM1              = "+coshValueM1);
                System.out.println("acosh1pCoshValueM1 = "+acosh1pCoshValueM1);
                System.out.println("coshAcosh1pCoshValueM1M1 = "+coshAcosh1pCoshValueM1M1);
                System.out.println("relDelta(value, acosh1pCoshValueM1)             = "+relDelta1);
                System.out.println("relDelta(coshValueM1, coshAcosh1pCoshValueM1M1) = "+relDelta2);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
        
        /*
         * Testing against coshm1, for tiny values.
         * (only considering best min delta of both,
         * to avoid bad delta only due to double precision loss).
         */
        
        worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            // Only using positive-x-half of cosh.
            double value = randomDoubleWhatever(0.0, Double.MAX_VALUE);
            double coshm1Value = FastMath.coshm1(value); // cosh(value)-1
            if ((coshm1Value == 0.0) || Double.isInfinite(coshm1Value)) {
                // Underflow or overflow.
                --i;continue;
            }
            double acosh1pCoshm1Value = FastMath.acosh1p(coshm1Value); // acosh(1+cosh(value)-1) = value
            double coshm1Acosh1pCoshm1Value = FastMath.coshm1(acosh1pCoshm1Value); // cosh(acosh(1+cosh(value)-1))-1 = cosh(value)-1
            double relDelta1 = relDelta(value, acosh1pCoshm1Value);
            double relDelta2 = relDelta(coshm1Value, coshm1Acosh1pCoshm1Value);
            double relDelta = Math.min(relDelta1, relDelta2);
            boolean ok = (relDelta < EPSILON_1EM7);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_acosh1p_double() (2)");
                System.out.println("value              = "+value);
                System.out.println("coshm1Value              = "+coshm1Value);
                System.out.println("acosh1pCoshm1Value = "+acosh1pCoshm1Value);
                System.out.println("coshm1Acosh1pCoshm1Value = "+coshm1Acosh1pCoshm1Value);
                System.out.println("relDelta(value, acosh1pCoshm1Value)             = "+relDelta1);
                System.out.println("relDelta(coshm1Value, coshm1Acosh1pCoshm1Value) = "+relDelta2);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_atanh_double() {
        assertEquals(Double.NaN, FastMath.atanh(Double.NaN));
        
        assertEquals(Double.NaN, FastMath.atanh(-1.1));
        assertEquals(Double.NEGATIVE_INFINITY, FastMath.atanh(-1.0));
        
        assertEquals(Double.POSITIVE_INFINITY, FastMath.atanh(1.0));
        assertEquals(Double.NaN, FastMath.atanh(1.1));
        
        /*
         * Testing
         * StrictMath.tanh(atanh(value)) ~= value
         * or
         * atanh(StrictMath.tanh(value)) ~= value
         * (only considering best min delta of both,
         * to avoid bad delta only due to double precision loss,
         * either for tanh(big values), or atanh(values near +-1)).
         */
        
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever(-711.0, 711.0);
            double tanhValue = StrictMath.tanh(value);
            if (!(Math.abs(tanhValue) < 1.0)) {
                // NaN or limit.
                --i;continue;
            }
            double atanhTanhValue = FastMath.atanh(tanhValue);
            double tanhAtanhTanhValue = StrictMath.tanh(atanhTanhValue);
            double relDelta1 = relDelta(value, atanhTanhValue);
            double relDelta2 = relDelta(tanhValue, tanhAtanhTanhValue);
            double relDelta = Math.min(relDelta1, relDelta2);
            boolean ok = (relDelta < EPSILON_1EM14);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_atanh_double()");
                System.out.println("value          = "+value);
                System.out.println("tanhValue          = "+tanhValue);
                System.out.println("atanhTanhValue = "+atanhTanhValue);
                System.out.println("tanhAtanhTanhValue = "+tanhAtanhTanhValue);
                System.out.println("relDelta(value, atanhTanhValue)         = "+relDelta1);
                System.out.println("relDelta(tanhValue, tanhAtanhTanhValue) = "+relDelta2);
                System.out.println("relDelta                                = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }
    
    /*
     * exponentials
     */
    
    public void test_exp_double() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = this.random.nextBoolean() ? randomDoubleWhatever() : randomDoubleWhatever(-746.0, 710.0);
            double ref = StrictMath.exp(value);
            double res = FastMath.exp(value);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_exp_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_expQuick_double() {
        final double relEpsilon = 2.94e-2;
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever(-700.0, 700.0);
            double ref = StrictMath.exp(value);
            double res = FastMath.expQuick(value);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < relEpsilon);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_expQuick_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_expm1_double() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = this.random.nextBoolean() ? randomDoubleWhatever() : randomDoubleWhatever(-746.0, 710.0);
            double ref = StrictMath.expm1(value);
            double res = FastMath.expm1(value);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_expm1_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }
    
    /*
     * logarithms
     */

    public void test_log_double() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = StrictMath.log(value);
            double res = FastMath.log(value);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < EPSILON_1EM14);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_log_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_logQuick_double() {
        final double relEpsilon = 1.9e-3;
        final double minEpsilon = 2.8e-4;
        double worseRelDelta = 0.0;
        double worseMinDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever(DOUBLE_MIN_NORMAL, Double.MAX_VALUE);
            double ref = StrictMath.log(value);
            double res = FastMath.logQuick(value);
            double relDelta = relDelta(ref, res);
            double minDelta = minDelta(ref, res);
            boolean ok = (relDelta < relEpsilon) && (minDelta < minEpsilon);
            if ((!ok) || (SETTING && ((relDelta > worseRelDelta) || (minDelta > worseMinDelta)))) {
                System.out.println();
                System.out.println("test_logQuick_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
                System.out.println("minDelta = "+minDelta);
            }
            worseRelDelta = Math.max(worseRelDelta, relDelta);
            worseMinDelta = Math.max(worseMinDelta, minDelta);
            assertTrue(ok);
        }
    }

    public void test_log10_double() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = StrictMath.log10(value);
            double res = FastMath.log10(value);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < EPSILON_1EM14);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_log10_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_log1p_double() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = StrictMath.log1p(value);
            double res = FastMath.log1p(value);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < EPSILON_1EM14);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_log1p_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_log2_int() {
        for (int value : new int[]{Integer.MIN_VALUE,0}) {
            try {
                FastMath.log2(value);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        for (int p=0;p<=30;p++) {
            int pot = (1<<p);

            if (p != 0) {
                assertEquals(p-1, FastMath.log2(pot-1));
            }
            assertEquals(p, FastMath.log2(pot));
            assertEquals(p, FastMath.log2(pot+pot-1));
            if (p != 30) {
                assertEquals(p+1, FastMath.log2(pot+pot));
            }
        }
    }

    public void test_log2_long() {
        for (long value : new long[]{Long.MIN_VALUE,0}) {
            try {
                FastMath.log2(value);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        for (int p=0;p<=62;p++) {
            long pot = (1L<<p);

            if (p != 0) {
                assertEquals(p-1, FastMath.log2(pot-1));
            }
            assertEquals(p, FastMath.log2(pot));
            assertEquals(p, FastMath.log2(pot+pot-1));
            if (p != 62) {
                assertEquals(p+1, FastMath.log2(pot+pot));
            }
        }
    }
    
    /*
     * powers
     */
    
    public void test_pow_2double() {
        assertEquals(1.0, FastMath.pow(0.0,0.0));
        assertEquals(0.0, FastMath.pow(0.0,2.0));
        assertEquals(0.0, FastMath.pow(-0.0,2.0));
        assertEquals(Double.POSITIVE_INFINITY, FastMath.pow(0.0,-2.0));
        assertEquals(0.0, FastMath.pow(0.0,3.0));
        assertEquals(-0.0, FastMath.pow(-0.0,3.0));
        assertEquals(Double.POSITIVE_INFINITY, FastMath.pow(0.0,-3.0));
        assertEquals(4.0, FastMath.pow(2.0,2.0), EPSILON_1EM15);
        assertEquals(8.0, FastMath.pow(2.0,3.0), EPSILON_1EM15);
        assertEquals(1.0/4.0, FastMath.pow(2.0,-2.0), EPSILON_1EM15);
        assertEquals(1.0/8.0, FastMath.pow(2.0,-3.0), EPSILON_1EM15);
        assertEquals(Double.POSITIVE_INFINITY, FastMath.pow(Double.NEGATIVE_INFINITY,2.0));
        assertEquals(Double.NEGATIVE_INFINITY, FastMath.pow(Double.NEGATIVE_INFINITY,3.0));
        assertEquals(0.0, FastMath.pow(Double.NEGATIVE_INFINITY,-2.0));
        assertEquals(-0.0, FastMath.pow(Double.NEGATIVE_INFINITY,-3.0));
        assertEquals(Double.POSITIVE_INFINITY, FastMath.pow(-2.0,(1L<<40))); // even power
        assertEquals(Double.NEGATIVE_INFINITY, FastMath.pow(-2.0,(1L<<40)+1)); // odd power
        assertEquals(Double.NaN, FastMath.pow(Double.NaN,1.0));
        assertEquals(Double.NaN, FastMath.pow(1.0,Double.NaN));
        assertEquals(Double.NaN, FastMath.pow(Double.NaN,-1.0));
        assertEquals(Double.NaN, FastMath.pow(-1.0,Double.NaN));

        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = randomDoubleWhatever();
            double b = randomDoubleWhatever();
            double ref = StrictMath.pow(a,b);
            double res = FastMath.pow(a,b);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < EPSILON_1EM12);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_pow_2double()");
                System.out.println("a = "+a);
                System.out.println("b = "+b);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_powQuick_2double() {
        double worseDelta1 = 0.0;
        double worseDelta2 = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = randomDoubleWhatever(DOUBLE_MIN_NORMAL, Double.MAX_VALUE);
            double b = randomDoubleWhatever(-Double.MAX_VALUE, Double.MAX_VALUE);
            double ref = StrictMath.pow(a, b);
            double absRef = Math.abs(ref);
            final double relEpsilon;
            final int epsilonId;
            if ((absRef > 1e-10) && (absRef < 1e10)) {
                relEpsilon = 4e-3;
                epsilonId = 1;
            } else if ((absRef > 1e-50) && (absRef < 1e50)) {
                relEpsilon = 4e-2;
                epsilonId = 2;
            } else {
                --i;continue;
            }
            double res = FastMath.powQuick(a,b);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < relEpsilon);
            if ((!ok) || (SETTING && (relDelta > ((epsilonId == 1) ? worseDelta1 : worseDelta2)))) {
                System.out.println();
                System.out.println("test_powQuick_2double()");
                System.out.println("a = "+a);
                System.out.println("b = "+b);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta+" (max allowed="+relEpsilon+")");
            }
            if (epsilonId == 1) {
                worseDelta1 = Math.max(worseDelta1, relDelta);
            } else {
                worseDelta2 = Math.max(worseDelta2, relDelta);
            }
            assertTrue(ok);
        }
    }

    public void test_powFast_double_int() {
        assertEquals(1.0, FastMath.powFast(1.0,Integer.MIN_VALUE));
        assertEquals(Double.POSITIVE_INFINITY, FastMath.powFast(Double.MIN_VALUE,Integer.MIN_VALUE));
        assertEquals(Double.NaN, FastMath.powFast(Double.NaN,1));
        
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = randomDoubleWhatever(-10.0,10.0);
            int b = randomIntUniform(-10,10);
            double ref = StrictMath.pow(a,b);
            double res = FastMath.powFast(a,b);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < EPSILON_1EM14);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_powFast_double_int()");
                System.out.println("a = "+a);
                System.out.println("b = "+b);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_twoPow_int() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            int value = randomIntWhatever();
            double ref = StrictMath.pow(2,value);
            double res = FastMath.twoPow(value);
            assertEquals(ref, res);
        }
    }

    public void test_pow2_int() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            int value = randomIntWhatever();
            int ref = value*value;
            int res = FastMath.pow2(value);
            assertEquals(ref, res);
        }
    }

    public void test_pow2_long() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            long value = randomLongWhatever();
            long ref = value*value;
            long res = FastMath.pow2(value);
            assertEquals(ref, res);
        }
    }

    public void test_pow2_float() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = randomFloatWhatever();
            float ref = value*value;
            float res = FastMath.pow2(value);
            double relDelta = relDelta(ref,res);
            boolean ok = (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_pow2_float()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_pow2_double() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = value*value;
            double res = FastMath.pow2(value);
            double relDelta = relDelta(ref,res);
            boolean ok = (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_pow2_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_pow3_int() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            int value = randomIntWhatever();
            int ref = value*value*value;
            int res = FastMath.pow3(value);
            assertEquals(ref, res);
        }
    }

    public void test_pow3_long() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            long value = randomLongWhatever();
            long ref = value*value*value;
            long res = FastMath.pow3(value);
            assertEquals(ref, res);
        }
    }

    public void test_pow3_float() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = randomFloatWhatever();
            float ref = value*value*value;
            float res = FastMath.pow3(value);
            double relDelta = relDelta(ref,res);
            boolean ok = (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_pow3_float()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_pow3_double() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = value*value*value;
            double res = FastMath.pow3(value);
            double relDelta = relDelta(ref,res);
            boolean ok = (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_pow3_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }
    
    /*
     * roots
     */

    public void test_sqrt_double() {
        assertEquals(-0.0, FastMath.sqrt(-0.0));
        assertEquals(0.0, FastMath.sqrt(0.0));
        assertEquals(Double.NaN, FastMath.sqrt(Double.NaN));
        
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = StrictMath.sqrt(value);
            double res = FastMath.sqrt(value);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_sqrt_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_sqrtQuick_double() {
        final double epsilon = 3.41e-2;
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever(DOUBLE_MIN_NORMAL, Double.MAX_VALUE);
            double ref = StrictMath.sqrt(value);
            double res = FastMath.sqrtQuick(value);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < epsilon);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_sqrtQuick_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_invSqrtQuick_double() {
        final double epsilon = 3.44e-2;
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever(DOUBLE_MIN_NORMAL, Double.MAX_VALUE);
            double ref = 1/StrictMath.sqrt(value);
            double res = FastMath.invSqrtQuick(value);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < epsilon);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_invSqrtQuick_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_cbrt_double() {
        assertEquals(-0.0, FastMath.cbrt(-0.0));
        assertEquals(0.0, FastMath.cbrt(0.0));
        assertEquals(Double.NaN, FastMath.cbrt(Double.NaN));
        
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = StrictMath.cbrt(value);
            double res = FastMath.cbrt(value);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < EPSILON_1EM15);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_cbrt_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }

    public void test_hypot_2double() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double x = randomDoubleWhatever();
            double y = this.random.nextBoolean() ? x * randomDoubleUniform(1e-16, 1e16) : randomDoubleWhatever();
            double ref = StrictMath.hypot(x,y);
            double res = FastMath.hypot(x,y);
            double relDelta = relDelta(ref, res);
            boolean ok = (relDelta < EPSILON_1EM14);
            if ((!ok) || (SETTING && (relDelta > worseDelta))) {
                System.out.println();
                System.out.println("test_hypot_2double()");
                System.out.println("x = "+x);
                System.out.println("y = "+y);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("relDelta = "+relDelta);
            }
            worseDelta = Math.max(worseDelta, relDelta);
            assertTrue(ok);
        }
    }
    
    /*
     * absolute values
     */

    public void test_abs_int() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            int value = randomIntWhatever();
            int ref = Math.abs(value);
            int res = FastMath.abs(value);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_abs_long() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            long value = randomLongWhatever();
            long ref = Math.abs(value);
            long res = FastMath.abs(value);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    /*
     * close values
     */

    public void test_toIntExact_long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MAX_VALUE, FastMath.toIntExact((long)Integer.MAX_VALUE));
        try {
            FastMath.toIntExact(((long)Integer.MAX_VALUE)+1L);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_toInt_long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MAX_VALUE, FastMath.toInt((long)Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, FastMath.toInt(Long.MAX_VALUE));
    }

    public void test_floor_float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = randomFloatWhatever();
            float ref = (float)Math.floor(value);
            float res = FastMath.floor(value);
            assertEquals(ref, res);
        }
    }

    public void test_floor_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.floor(value);
            double res = FastMath.floor(value);
            assertEquals(ref, res);
        }
    }
    
    public void test_ceil_float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = randomFloatWhatever();
            float ref = (float)Math.ceil(value);
            float res = FastMath.ceil(value);
            assertEquals(ref, res);
        }
    }

    public void test_ceil_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.ceil(value);
            double res = FastMath.ceil(value);
            assertEquals(ref, res);
        }
    }

    public void test_round_float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = randomFloatWhatever();
            int ref;
            if (NumbersUtils.isMathematicalInteger(value) || NumbersUtils.isNaNOrInfinite(value)) {
                ref = (int)value; // exact, or closest int, or 0 if NaN
            } else {
                boolean neg = (value < 0);
                int lowerMag = (int)value;
                float postCommaPart = value - lowerMag;
                if (neg) {
                    ref = (postCommaPart < -0.5f) ? lowerMag-1 : lowerMag;
                } else {
                    ref = (postCommaPart < 0.5f) ? lowerMag : lowerMag+1;
                }
            }
            int res = FastMath.round(value);
            boolean ok = (ref == res);
            if (!ok) {
                System.out.println();
                System.out.println("test_round_float()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
            }
            assertTrue(ok);
        }
    }

    public void test_round_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            long ref;
            if (NumbersUtils.isMathematicalInteger(value) || NumbersUtils.isNaNOrInfinite(value)) {
                ref = (long)value; // exact, or closest int, or 0 if NaN
            } else {
                boolean neg = (value < 0);
                long lowerMag = (long)value;
                double postCommaPart = value - lowerMag;
                if (neg) {
                    ref = (postCommaPart < -0.5) ? lowerMag-1 : lowerMag;
                } else {
                    ref = (postCommaPart < 0.5) ? lowerMag : lowerMag+1;
                }
            }
            long res = FastMath.round(value);
            boolean ok = (ref == res);
            if (!ok) {
                System.out.println();
                System.out.println("test_round_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
            }
            assertTrue(ok);
        }
    }

    public void test_roundEven_float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = randomFloatWhatever();
            int ref;
            if (NumbersUtils.isMathematicalInteger(value) || NumbersUtils.isNaNOrInfinite(value)) {
                ref = (int)value; // exact, or closest int, or 0 if NaN
            } else {
                boolean neg = (value < 0);
                int lowerMag = (int)value;
                if (NumbersUtils.isEquidistant(value)) {
                    ref = (((lowerMag&1) == 0) ? lowerMag : lowerMag + (neg ? -1 : 1));
                } else {
                    float postCommaPart = value - lowerMag;
                    if (neg) {
                        ref = (postCommaPart < -0.5f) ? lowerMag-1 : lowerMag;
                    } else {
                        ref = (postCommaPart < 0.5f) ? lowerMag : lowerMag+1;
                    }
                }
            }
            int res = FastMath.roundEven(value);
            boolean ok = (ref == res);
            if (!ok) {
                System.out.println();
                System.out.println("test_roundEven_float()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
            }
            assertTrue(ok);
        }
    }

    public void test_roundEven_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            long ref;
            if (NumbersUtils.isMathematicalInteger(value) || NumbersUtils.isNaNOrInfinite(value)) {
                ref = (long)value; // exact, or closest int, or 0 if NaN
            } else {
                boolean neg = (value < 0);
                long lowerMag = (long)value;
                if (NumbersUtils.isEquidistant(value)) {
                    ref = (((lowerMag&1) == 0) ? lowerMag : lowerMag + (neg ? -1 : 1));
                } else {
                    double postCommaPart = value - lowerMag;
                    if (neg) {
                        ref = (postCommaPart < -0.5) ? lowerMag-1 : lowerMag;
                    } else {
                        ref = (postCommaPart < 0.5) ? lowerMag : lowerMag+1;
                    }
                }
            }
            long res = FastMath.roundEven(value);
            boolean ok = (ref == res);
            if (!ok) {
                System.out.println();
                System.out.println("test_roundEven_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
            }
            assertTrue(ok);
        }
    }

    public void test_rint_float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = randomFloatWhatever();
            float ref = (float)Math.rint((double)value);
            float res = FastMath.rint(value);
            boolean ok = equivalent(ref,res);
            if (!ok) {
                System.out.println();
                System.out.println("test_rint_float()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
            }
            assertTrue(ok);
        }
    }

    public void test_rint_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.rint(value);
            double res = FastMath.rint(value);
            boolean ok = equivalent(ref,res);
            if (!ok) {
                System.out.println();
                System.out.println("test_rint_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
            }
            assertTrue(ok);
        }
    }
    
    /*
     * ranges
     */
    
    public void test_toRange_3int() {
        assertEquals(0, FastMath.toRange(0, 2, -1));
        assertEquals(0, FastMath.toRange(0, 2, 0));
        assertEquals(1, FastMath.toRange(0, 2, 1));
        assertEquals(2, FastMath.toRange(0, 2, 2));
        assertEquals(2, FastMath.toRange(0, 2, 3));
    }

    public void test_toRange_3long() {
        assertEquals(0L, FastMath.toRange(0L, 2L, -1L));
        assertEquals(0L, FastMath.toRange(0L, 2L, 0L));
        assertEquals(1L, FastMath.toRange(0L, 2L, 1L));
        assertEquals(2L, FastMath.toRange(0L, 2L, 2L));
        assertEquals(2L, FastMath.toRange(0L, 2L, 3L));
    }

    public void test_toRange_3float() {
        assertEquals(0.0f, FastMath.toRange(0.0f, 2.0f, -1.0f));
        assertEquals(0.0f, FastMath.toRange(0.0f, 2.0f, 0.0f));
        assertEquals(1.0f, FastMath.toRange(0.0f, 2.0f, 1.0f));
        assertEquals(2.0f, FastMath.toRange(0.0f, 2.0f, 2.0f));
        assertEquals(2.0f, FastMath.toRange(0.0f, 2.0f, 3.0f));
    }

    public void test_toRange_3double() {
        assertEquals(0.0, FastMath.toRange(0.0, 2.0, -1.0));
        assertEquals(0.0, FastMath.toRange(0.0, 2.0, 0.0));
        assertEquals(1.0, FastMath.toRange(0.0, 2.0, 1.0));
        assertEquals(2.0, FastMath.toRange(0.0, 2.0, 2.0));
        assertEquals(2.0, FastMath.toRange(0.0, 2.0, 3.0));
    }

    /*
     * binary operators (+,-,*)
     */

    public void test_addExact_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MAX_VALUE, FastMath.addExact(Integer.MAX_VALUE-1, 1));
        try {
            FastMath.addExact(Integer.MAX_VALUE, 1);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_addExact_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MAX_VALUE, FastMath.addExact(Long.MAX_VALUE-1L, 1L));
        try {
            FastMath.addExact(Long.MAX_VALUE, 1L);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_addBounded_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MAX_VALUE, FastMath.addBounded(Integer.MAX_VALUE-1, 1));
        assertEquals(Integer.MAX_VALUE, FastMath.addBounded(Integer.MAX_VALUE, 1));
    }

    public void test_addBounded_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MAX_VALUE, FastMath.addBounded(Long.MAX_VALUE-1L, 1L));
        assertEquals(Long.MAX_VALUE, FastMath.addBounded(Long.MAX_VALUE, 1L));
    }

    public void test_subtractExact_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MIN_VALUE, FastMath.subtractExact(Integer.MIN_VALUE+1, 1));
        try {
            FastMath.subtractExact(Integer.MIN_VALUE, 1);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_subtractExact_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MIN_VALUE, FastMath.subtractExact(Long.MIN_VALUE+1L, 1L));
        try {
            FastMath.subtractExact(Long.MIN_VALUE, 1L);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_subtractBounded_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MIN_VALUE, FastMath.subtractBounded(Integer.MIN_VALUE+1, 1));
        assertEquals(Integer.MIN_VALUE, FastMath.subtractBounded(Integer.MIN_VALUE, 1));
    }

    public void test_subtractBounded_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MIN_VALUE, FastMath.subtractBounded(Long.MIN_VALUE+1L, 1L));
        assertEquals(Long.MIN_VALUE, FastMath.subtractBounded(Long.MIN_VALUE, 1L));
    }

    public void test_multiplyExact_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MIN_VALUE, FastMath.multiplyExact(Integer.MIN_VALUE/2, 2));
        try {
            FastMath.multiplyExact(Integer.MIN_VALUE, 2);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_multiplyExact_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MIN_VALUE, FastMath.multiplyExact(Long.MIN_VALUE/2L, 2L));
        try {
            FastMath.multiplyExact(Long.MIN_VALUE, 2L);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_multiplyBounded_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MIN_VALUE, FastMath.multiplyBounded(Integer.MIN_VALUE/2, 2));
        assertEquals(Integer.MIN_VALUE, FastMath.multiplyBounded(Integer.MIN_VALUE, 2));
    }

    public void test_multiplyBounded_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MIN_VALUE, FastMath.multiplyBounded(Long.MIN_VALUE/2L, 2L));
        assertEquals(Long.MIN_VALUE, FastMath.multiplyBounded(Long.MIN_VALUE, 2L));
    }

    /*
     * binary operators (/,%)
     */

    public void test_floorDiv_2int() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            final int x = randomIntWhatever();
            int y = randomIntWhatever();
            if (y == 0) {
                try {
                    FastMath.floorDiv(x,y);
                    assertTrue(false);
                } catch (ArithmeticException e) {
                    // ok
                }
            } else {
                final int expected;
                final boolean exact = ((x/y)*y == x);
                if (exact || ((x^y) >= 0)) {
                    // exact or same sign
                    expected = x/y;
                } else {
                    // different signs and not exact
                    expected = x/y - 1;
                }
                final int actual = FastMath.floorDiv(x,y);
                assertEquals(expected, actual);
            }
        }
    }

    public void test_floorDiv_2long() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            final long x = randomLongWhatever();
            long y = randomLongWhatever();
            if (y == 0) {
                try {
                    FastMath.floorDiv(x,y);
                    assertTrue(false);
                } catch (ArithmeticException e) {
                    // ok
                }
            } else {
                final long expected;
                final boolean exact = ((x/y)*y == x);
                if (exact || ((x^y) >= 0)) {
                    // exact or same sign
                    expected = x/y;
                } else {
                    // different signs and not exact
                    expected = x/y - 1;
                }
                final long actual = FastMath.floorDiv(x,y);
                assertEquals(expected, actual);
            }
        }
    }

    public void test_floorMod_2int() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            final int x = randomIntWhatever();
            int y = randomIntWhatever();
            if (y == 0) {
                try {
                    FastMath.floorMod(x,y);
                    assertTrue(false);
                } catch (ArithmeticException e) {
                    // ok
                }
            } else {
                final int expected;
                final boolean exact = ((x/y)*y == x);
                if (exact || ((x^y) >= 0)) {
                    // exact or same sign
                    expected = x%y;
                } else {
                    // different signs and not exact
                    expected = x%y + y;
                }
                final int actual = FastMath.floorMod(x,y);
                assertEquals(expected, actual);
                
                // identity
                assertEquals(x - FastMath.floorDiv(x, y) * y, FastMath.floorMod(x,y));
            }
        }
    }

    public void test_floorMod_2long() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            long x = randomIntWhatever();
            long y = randomIntWhatever();
            if (y == 0) {
                try {
                    FastMath.floorMod(x,y);
                    assertTrue(false);
                } catch (ArithmeticException e) {
                    // ok
                }
            } else {
                long expected;
                boolean exact = ((x/y)*y == x);
                if (exact || ((x^y) >= 0)) {
                    // exact or same sign
                    expected = x%y;
                } else {
                    // different signs and not exact
                    expected = x%y + y;
                }
                long actual = FastMath.floorMod(x,y);
                assertEquals(expected, actual);
                
                // identity
                assertEquals(x - FastMath.floorDiv(x, y) * y, FastMath.floorMod(x,y));
            }
        }
    }

    public void test_remainder_2double() {
        /* Can have that kind of failure with Java 5 or 6,
         * but it's just a "%" bug.
test_remainder_2double()
a = -1.7976931348623157E308
b = -5.9728871583771424E-300
ref = -4.55688172866467E-305
res = NaN
IEEE = -4.55688172866467E-305
         */
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = randomDoubleWhatever();
            double b = randomDoubleWhatever();
            double ref = getExpectedResult_remainder_2double(a,b);
            double res = FastMath.remainder(a,b);
            boolean ok = equivalent(ref,res);
            if (!ok) {
                System.out.println();
                System.out.println("test_remainder_2double()");
                System.out.println("a = "+a);
                System.out.println("b = "+b);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("IEEE = "+Math.IEEEremainder(a,b));
            }
            assertTrue(ok);
        }
    }

    public void test_normalizeMinusPiPi() {
        double worseDelta1 = 0.0;
        double worseDelta2 = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            // * PI to have values modulo-close to PI
            double value = randomDoubleWhatever() * Math.PI;
            double res = FastMath.normalizeMinusPiPi(value);
            if (NumbersUtils.isInRange(-Math.PI, Math.PI, value)) {
                // Unchanged if already in range.
                assertEquals(value, res);
            } else {
                assertTrue(Double.isNaN(res) || NumbersUtils.isInRange(-Math.PI, Math.PI, res));
                double ref = StrictMath.atan2(StrictMath.sin(value),StrictMath.cos(value));
                ref = refMod(ref, res, 2*Math.PI);
                // Using minDelta, for reducing huge values might give
                // bad relative error near 0.
                final boolean nearEdge = (Math.abs(ref) < 0.1) || (Math.abs(ref-Math.PI) < 0.1) || (Math.abs(ref+Math.PI) < 0.1);
                final double epsilon;
                final int epsilonId;
                if (nearEdge) {
                    epsilon = EPSILON_1EM9;
                    epsilonId = 1;
                } else {
                    epsilon = EPSILON_1EM15;
                    epsilonId = 2;
                }
                double minDelta = minDelta(ref,res);
                boolean ok = (minDelta < epsilon);
                if ((!ok) || (SETTING && (minDelta > ((epsilonId == 1) ? worseDelta1 : worseDelta2)))) {
                    System.out.println();
                    System.out.println("test_normalizeMinusPiPi()");
                    System.out.println("value = "+value);
                    System.out.println("ref = "+ref);
                    System.out.println("res = "+res);
                    System.out.println("minDelta = "+minDelta+" (max allowed="+epsilon+")");
                }
                if (epsilonId == 1) {
                    worseDelta1 = Math.max(worseDelta1, minDelta);
                } else {
                    worseDelta2 = Math.max(worseDelta2, minDelta);
                }
                assertTrue(ok);
            }
        }
    }

    public void test_normalizeMinusPiPiFast() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever() * Math.PI;
            if (Math.abs(value) > MAX_VALUE_FAST_NORM) {
                value = randomDoubleUniform(-MAX_VALUE_FAST_NORM,MAX_VALUE_FAST_NORM);
            }
            double res = FastMath.normalizeMinusPiPiFast(value);
            if (NumbersUtils.isInRange(-Math.PI, Math.PI, value)) {
                assertEquals(value, res);
            } else {
                assertTrue(Double.isNaN(res) || NumbersUtils.isInRange(-Math.PI, Math.PI, res));
                double ref = StrictMath.atan2(StrictMath.sin(value),StrictMath.cos(value));
                ref = refMod(ref, res, 2*Math.PI);
                double minDelta = minDelta(ref,res);
                boolean ok = (minDelta < EPSILON_FAST_NORM);
                if ((!ok) || (SETTING && (minDelta > worseDelta))) {
                    System.out.println();
                    System.out.println("test_normalizeMinusPiPiFast()");
                    System.out.println("value = "+value);
                    System.out.println("ref = "+ref);
                    System.out.println("res = "+res);
                    System.out.println("minDelta = "+minDelta);
                }
                worseDelta = Math.max(worseDelta, minDelta);
                assertTrue(ok);
            }
        }
    }

    public void test_normalizeZeroTwoPi() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever() * (2*Math.PI);
            double res = FastMath.normalizeZeroTwoPi(value);
            if (NumbersUtils.isInRange(0.0, 2*Math.PI, value)) {
                assertEquals(value, res);
            } else {
                assertTrue(Double.isNaN(res) || NumbersUtils.isInRange(0.0, 2*Math.PI, res));
                double ref = StrictMath.atan2(StrictMath.sin(value),StrictMath.cos(value));
                if (ref < 0.0) {
                    ref += 2*Math.PI;
                }
                ref = refMod(ref, res, 2*Math.PI);
                double minDelta = minDelta(ref,res);
                boolean ok = (minDelta < EPSILON_1EM15);
                if ((!ok) || (SETTING && (minDelta > worseDelta))) {
                    System.out.println();
                    System.out.println("test_normalizeZeroTwoPi()");
                    System.out.println("value = "+value);
                    System.out.println("ref = "+ref);
                    System.out.println("res = "+res);
                    System.out.println("minDelta = "+minDelta);
                }
                worseDelta = Math.max(worseDelta, minDelta);
                assertTrue(ok);
            }
        }
    }

    public void test_normalizeZeroTwoPiFast() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever() * (2*Math.PI);
            if (Math.abs(value) > MAX_VALUE_FAST_NORM) {
                value = randomDoubleUniform(-MAX_VALUE_FAST_NORM,MAX_VALUE_FAST_NORM);
            }
            double res = FastMath.normalizeZeroTwoPiFast(value);
            if (NumbersUtils.isInRange(0.0, 2*Math.PI, value)) {
                assertEquals(value, res);
            } else {
                assertTrue(Double.isNaN(res) || NumbersUtils.isInRange(0.0, 2*Math.PI, res));
                double ref = StrictMath.atan2(StrictMath.sin(value),StrictMath.cos(value));
                if (ref < 0.0) {
                    ref += 2*Math.PI;
                }
                ref = refMod(ref, res, 2*Math.PI);
                double minDelta = minDelta(ref,res);
                boolean ok = (minDelta < EPSILON_FAST_NORM);
                if ((!ok) || (SETTING && (minDelta > worseDelta))) {
                    System.out.println();
                    System.out.println("test_normalizeZeroTwoPiFast()");
                    System.out.println("value = "+value);
                    System.out.println("ref = "+ref);
                    System.out.println("res = "+res);
                    System.out.println("minDelta = "+minDelta);
                }
                worseDelta = Math.max(worseDelta, minDelta);
                assertTrue(ok);
            }
        }
    }

    public void test_normalizeMinusHalfPiHalfPi() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever() * (Math.PI/2);
            double res = FastMath.normalizeMinusHalfPiHalfPi(value);
            if (NumbersUtils.isInRange(-Math.PI/2, Math.PI/2, value)) {
                assertEquals(value, res);
            } else {
                assertTrue(Double.isNaN(res) || NumbersUtils.isInRange(-Math.PI/2, Math.PI/2, res));
                double ref = StrictMath.atan(StrictMath.tan(value));
                ref = refMod(ref, res, Math.PI);
                double minDelta = minDelta(ref,res);
                boolean ok = (minDelta < EPSILON_1EM15);
                if ((!ok) || (SETTING && (minDelta > worseDelta))) {
                    System.out.println();
                    System.out.println("test_normalizeMinusHalfPiHalfPi()");
                    System.out.println("value = "+value);
                    System.out.println("ref = "+ref);
                    System.out.println("res = "+res);
                    System.out.println("minDelta = "+minDelta);
                }
                worseDelta = Math.max(worseDelta, minDelta);
                assertTrue(ok);
            }
        }
    }

    public void test_normalizeMinusHalfPiHalfPiFast() {
        double worseDelta = 0.0;
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever() * (Math.PI/2);
            if (Math.abs(value) > MAX_VALUE_FAST_NORM) {
                value = randomDoubleUniform(-MAX_VALUE_FAST_NORM,MAX_VALUE_FAST_NORM);
            }
            double res = FastMath.normalizeMinusHalfPiHalfPiFast(value);
            if (NumbersUtils.isInRange(-Math.PI/2, Math.PI/2, value)) {
                assertEquals(value, res);
            } else {
                assertTrue(Double.isNaN(res) || NumbersUtils.isInRange(-Math.PI/2, Math.PI/2, res));
                double ref = StrictMath.atan(StrictMath.tan(value));
                ref = refMod(ref, res, Math.PI);
                double minDelta = minDelta(ref,res);
                boolean ok = (minDelta < EPSILON_FAST_NORM);
                if ((!ok) || (SETTING && (minDelta > worseDelta))) {
                    System.out.println();
                    System.out.println("test_normalizeMinusHalfPiHalfPiFast()");
                    System.out.println("value = "+value);
                    System.out.println("ref = "+ref);
                    System.out.println("res = "+res);
                    System.out.println("minDelta = "+minDelta);
                }
                worseDelta = Math.max(worseDelta, minDelta);
                assertTrue(ok);
            }
        }
    }
    
    /*
     * floating points utils
     */

    public void test_isNaNOrInfinite_float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = randomFloatWhatever();
            boolean ref = Float.isNaN(value) || Float.isInfinite(value);
            boolean res = FastMath.isNaNOrInfinite(value);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_isNaNOrInfinite_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            boolean ref = Double.isNaN(value) || Double.isInfinite(value);
            boolean res = FastMath.isNaNOrInfinite(value);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_getExponent_float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = randomFloatWhatever();
            int ref = Math.getExponent(value);
            int res = FastMath.getExponent(value);
            boolean ok = (ref == res);
            if (!ok) {
                System.out.println();
                System.out.println("test_getExponent_float()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
            }
            assertTrue(ok);
        }
    }

    public void test_getExponent_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            int ref = Math.getExponent(value);
            int res = FastMath.getExponent(value);
            boolean ok = (ref == res);
            if (!ok) {
                System.out.println();
                System.out.println("test_getExponent_double()");
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
            }
            assertTrue(ok);
        }
    }

    public void test_signum_float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = randomFloatWhatever();
            float ref = Math.signum(value);
            float res = FastMath.signum(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_signum_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.signum(value);
            double res = FastMath.signum(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_signFromBit_float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = randomFloatWhatever();
            int ref = (Float.floatToRawIntBits(value) < 0 ? -1 : 1);
            int res = FastMath.signFromBit(value);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_signFromBit_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            long ref = (Double.doubleToRawLongBits(value) < 0 ? -1L : 1L);
            long res = FastMath.signFromBit(value);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }
    
    public void test_copySign_2float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float a = randomFloatWhatever();
            float b = randomFloatWhatever();
            float ref = StrictMath.copySign(a,b);
            float res = FastMath.copySign(a,b);
            if (Double.isNaN(b)) {
                // We use Math.copySign(...) spec.
                assertEquals(Math.abs(ref), Math.abs(res));
            } else {
                assertEquals(ref, res);
            }
        }
    }

    public void test_copySign_2double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = randomDoubleWhatever();
            double b = randomDoubleWhatever();
            double ref = StrictMath.copySign(a,b);
            double res = FastMath.copySign(a,b);
            if (Double.isNaN(b)) {
                // We use Math.copySign(...) spec.
                assertEquals(Math.abs(ref), Math.abs(res));
            } else {
                assertEquals(ref, res);
            }
        }
    }

    public void test_ulp_float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = randomFloatWhatever();
            float ref = Math.ulp(value);
            float res = FastMath.ulp(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_ulp_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.ulp(value);
            double res = FastMath.ulp(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_nextAfter_float_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float a = randomFloatWhatever();
            double b = randomDoubleWhatever();
            float ref = Math.nextAfter(a,b);
            float res = FastMath.nextAfter(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_nextAfter_2double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = randomDoubleWhatever();
            double b = randomDoubleWhatever();
            double ref = Math.nextAfter(a,b);
            double res = FastMath.nextAfter(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_nextDown_float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = randomFloatWhatever();
            float ref = Math.nextAfter(value,Double.NEGATIVE_INFINITY);
            float res = FastMath.nextDown(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_nextDown_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.nextAfter(value,Double.NEGATIVE_INFINITY);
            double res = FastMath.nextDown(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_nextUp_float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = randomFloatWhatever();
            float ref = Math.nextAfter(value,Double.POSITIVE_INFINITY);
            float res = FastMath.nextUp(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_nextUp_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.nextAfter(value,Double.POSITIVE_INFINITY);
            double res = FastMath.nextUp(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_scalb_float_int() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float a = randomFloatWhatever();
            int b = randomIntWhatever();
            float ref = Math.scalb(a,b);
            float res = FastMath.scalb(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_scalb_double_int() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = randomDoubleWhatever();
            int b = randomIntWhatever();
            double ref = Math.scalb(a,b);
            double res = FastMath.scalb(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    /*
     * non-redefined java.lang.Math public values and treatments
     */

    public void test_E() {
        assertEquals(Math.E, FastMath.E);
    }

    public void test_PI() {
        assertEquals(Math.PI, FastMath.PI);
    }

    public void test_abs_float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = randomFloatWhatever();
            float ref = Math.abs(value);
            float res = FastMath.abs(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_abs_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.abs(value);
            double res = FastMath.abs(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_min_2int() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            int a = randomIntWhatever();
            int b = randomIntWhatever();
            int ref = Math.min(a,b);
            int res = FastMath.min(a,b);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_min_2long() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            long a = randomLongWhatever();
            long b = randomLongWhatever();
            long ref = Math.min(a,b);
            long res = FastMath.min(a,b);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_min_2float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float a = randomFloatWhatever();
            float b = randomFloatWhatever();
            float ref = Math.min(a,b);
            float res = FastMath.min(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_min_2double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = randomDoubleWhatever();
            double b = randomDoubleWhatever();
            double ref = Math.min(a,b);
            double res = FastMath.min(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_max_2int() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            int a = randomIntWhatever();
            int b = randomIntWhatever();
            int ref = Math.max(a,b);
            int res = FastMath.max(a,b);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_max_2long() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            long a = randomLongWhatever();
            long b = randomLongWhatever();
            long ref = Math.max(a,b);
            long res = FastMath.max(a,b);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_max_2float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float a = randomFloatWhatever();
            float b = randomFloatWhatever();
            float ref = Math.max(a,b);
            float res = FastMath.max(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_max_2double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = randomDoubleWhatever();
            double b = randomDoubleWhatever();
            double ref = Math.max(a,b);
            double res = FastMath.max(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_IEEEremainder_2double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = randomDoubleWhatever();
            double b = randomDoubleWhatever();
            double ref = Math.IEEEremainder(a,b);
            double res = FastMath.IEEEremainder(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_random() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double res = FastMath.random();
            assertTrue((res >= 0.0) && (res < 1.0));
        }
    }

    //--------------------------------------------------------------------------
    // PRIVATE METHODS
    //--------------------------------------------------------------------------

    private static boolean equivalent(float a, float b) {
        if ((a == 0) && (b == 0)) {
            return Float.floatToRawIntBits(a) == Float.floatToRawIntBits(b);
        } else {
            return (a == b) || (Float.isNaN(a) && Float.isNaN(b));
        }
    }

    private static boolean equivalent(double a, double b) {
        if ((a == 0) && (b == 0)) {
            return Double.doubleToRawLongBits(a) == Double.doubleToRawLongBits(b);
        } else {
            return (a == b) || (Double.isNaN(a) && Double.isNaN(b));
        }
    }

    /*
     * uniform
     */

    private int randomIntUniform(int min, int max) {
        return this.utils.randomIntUniform(min, max);
    }
    
    private double randomDoubleUniform(double min, double max) {
        return this.utils.randomDoubleUniform(min, max);
    }

    /*
     * whatever
     */

    private int randomIntWhatever() {
        return this.utils.randomIntWhatever();
    }

    private long randomLongWhatever() {
        return this.utils.randomLongWhatever();
    }

    private float randomFloatWhatever() {
        return this.utils.randomFloatWhatever();
    }

    private double randomDoubleWhatever() {
        return this.utils.randomDoubleWhatever();
    }

    private double randomDoubleWhatever(double min, double max) {
        return this.utils.randomDoubleWhatever(min, max);
    }

    /*
     * 
     */

    private static double relDelta(double a, double b) {
        return NumbersTestUtils.relDelta(a, b);
    }

    /**
     * Used for methods involving normalization, such as sin/cos/normalizeXXX/etc.,
     * for a tiny error on normalizing huge values, can give a large relative
     * error if result is of small magnitude.
     * 
     * Also used for logQuick, which relative error gets worse by an order
     * of magnitude for arguments close to 1.
     */
    private static double minDelta(double a, double b) {
        return NumbersTestUtils.minDelta(a, b);
    }

    /*
     * 
     */
    
    /**
     * To rework modulo reference results, to avoid bad error
     * To rework reference result and avoid bad error
     * when res ~= ref +- mod.
     */
    private static double refMod(double ref, double res, double mod) {
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

    private static double getExpectedResult_remainder_2double(double a, double b) {
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
