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

/**
 * Copy-paste from FastMathTest, replacing FastMath with StrictFastMath
 * and updating copySign tests.
 */
public class StrictFastMathTest extends AbstractFastMathTezt {

    //--------------------------------------------------------------------------
    // PUBLIC METHODS
    //--------------------------------------------------------------------------

    /*
     * trigonometry
     */
    
    public void test_sin_double() {
        final double oneDegRad = StrictMath.toRadians(1.0);
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhateverOrPiIsh();
            value = knownBadValues_trigNorm(i, value);
            double ref = StrictMath.sin(value);
            double res = StrictFastMath.sin(value);
            double refValueNormalizedMPP = StrictMath.atan2(StrictMath.sin(value),StrictMath.cos(value));
            boolean is_0_1 = Math.abs(refValueNormalizedMPP) < oneDegRad;
            boolean is_179_180 = Math.abs(Math.abs(refValueNormalizedMPP) - Math.PI) < oneDegRad;
            helper.process(
                    ref,
                    res,
                    TOL_SIN_COS_ABS,
                    ((is_0_1 || is_179_180) ? TOL_SIN_COS_REL_BAD : TOL_SIN_COS_REL),
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_sinQuick_double() {
        final double bound = Integer.MAX_VALUE * (2*Math.PI/(1<<11)) - 2.0;
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleUniform(-bound,bound);
            double ref = StrictMath.sin(value);
            double res = StrictFastMath.sinQuick(value);
            helper.process(
                    ref,
                    res,
                    TOL_SINQUICK_COSQUICK_ABS,
                    Double.NaN,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_cos_double() {
        final double oneDegRad = StrictMath.toRadians(1.0);
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhateverOrPiIsh();
            value = knownBadValues_trigNorm(i, value);
            double ref = StrictMath.cos(value);
            double res = StrictFastMath.cos(value);
            double refValueNormalizedMPP = StrictMath.atan2(StrictMath.sin(value),StrictMath.cos(value));
            boolean is_89_91 = Math.abs(Math.abs(refValueNormalizedMPP) - Math.PI/2) < oneDegRad;
            helper.process(
                    ref,
                    res,
                    TOL_SIN_COS_ABS,
                    (is_89_91 ? TOL_SIN_COS_REL_BAD : TOL_SIN_COS_REL),
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_cosQuick_double() {
        final double bound = Integer.MAX_VALUE * (2*Math.PI/(1<<11));
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleUniform(-bound,bound);
            double ref = StrictMath.cos(value);
            double res = StrictFastMath.cosQuick(value);
            helper.process(
                    ref,
                    res,
                    TOL_SINQUICK_COSQUICK_ABS,
                    Double.NaN,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }
    
    public void test_sinAndCos_double_DoubleWrapper() {
        final DoubleWrapper tmpCos = new DoubleWrapper();
        final double oneDegRad = StrictMath.toRadians(1.0);
        final MyDoubleResHelper sinHelper = new MyDoubleResHelper("sin");
        final MyDoubleResHelper cosHelper = new MyDoubleResHelper("cos");
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhateverOrPiIsh();
            value = knownBadValues_trigNorm(i, value);
            double refSin = StrictMath.sin(value);
            double refCos = StrictMath.cos(value);
            double resSin = StrictFastMath.sinAndCos(value, tmpCos);
            double resCos = tmpCos.value;
            double refValueNormalizedMPP = StrictMath.atan2(StrictMath.sin(value),StrictMath.cos(value));
            boolean is_0_1 = Math.abs(refValueNormalizedMPP) < oneDegRad;
            boolean is_89_91 = Math.abs(Math.abs(refValueNormalizedMPP) - Math.PI/2) < oneDegRad;
            boolean is_179_180 = Math.abs(Math.abs(refValueNormalizedMPP) - Math.PI) < oneDegRad;
            sinHelper.process(
                    refSin,
                    resSin,
                    TOL_SIN_COS_ABS,
                    ((is_0_1 || is_179_180) ? TOL_SIN_COS_REL_BAD : TOL_SIN_COS_REL),
                    value);
            cosHelper.process(
                    refCos,
                    resCos,
                    TOL_SIN_COS_ABS,
                    (is_89_91 ? TOL_SIN_COS_REL_BAD : TOL_SIN_COS_REL),
                    value);
            assertTrue(sinHelper.lastOK());
            assertTrue(cosHelper.lastOK());
        }
        sinHelper.finalLogIfNeeded();
        cosHelper.finalLogIfNeeded();
    }

    public void test_tan_double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhateverOrPiIsh();
            value = knownBadValues_trigNorm(i, value);
            double ref = StrictMath.tan(value);
            double res = StrictFastMath.tan(value);
            final double relTol;
            if (Math.abs(ref) < 1e-5) {
                relTol = TOL_SIN_COS_REL_BAD;
            } else if (Math.abs(ref) < 30.0) {
                relTol = 4e-15;
            } else if (Math.abs(ref) < 1e7) {
                relTol = 1.5e-9;
            } else {
                relTol = 0.8;
            }
            boolean log = helper.process(
                    ref,
                    res,
                    Double.NaN,
                    relTol,
                    value);
            if (log) {
                System.out.println("atan(ref) = "+StrictMath.atan(ref));
                System.out.println("atan(res) = "+StrictMath.atan(res));
            }
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_asin_double() {
        assertEquals(Math.PI/2, StrictFastMath.asin(1.0));
        assertEquals(-Math.PI/2, StrictFastMath.asin(-1.0));
        assertEquals(Double.NaN, StrictFastMath.asin(-1.1));
        assertEquals(Double.NaN, StrictFastMath.asin(1.1));
        assertEquals(Double.NaN, StrictFastMath.asin(Double.NaN));
        
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = this.randomDoubleWhatever(-1.0, 1.0);
            double ref = StrictMath.asin(value);
            double res = StrictFastMath.asin(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    ((Math.abs(Math.abs(value)-0.5) < 0.4) ? TOL_1EM15 : 1e-13),
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_asinInRange_double() {
        assertEquals(-Math.PI/2, StrictFastMath.asinInRange(-1.1));
        assertEquals(Math.PI/2, StrictFastMath.asinInRange(1.1));
        assertEquals(StrictFastMath.asin(-0.1), StrictFastMath.asinInRange(-0.1));
        assertEquals(StrictFastMath.asin(0.1), StrictFastMath.asinInRange(0.1));
        assertEquals(Double.NaN, StrictFastMath.asinInRange(Double.NaN));
    }

    public void test_acos_double() {
        assertEquals(0.0, StrictFastMath.acos(1.0));
        assertEquals(Math.PI, StrictFastMath.acos(-1.0));
        assertEquals(Double.NaN, StrictFastMath.acos(-1.1));
        assertEquals(Double.NaN, StrictFastMath.acos(1.1));
        assertEquals(Double.NaN, StrictFastMath.acos(Double.NaN));
        
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = this.randomDoubleWhatever(-1.0, 1.0);
            double ref = StrictMath.acos(value);
            double res = StrictFastMath.acos(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    ((Math.abs(value) < 0.85) ? TOL_1EM15 : 1e-8),
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_acosInRange_double() {
        assertEquals(Math.PI, StrictFastMath.acosInRange(-1.1));
        assertEquals(0.0, StrictFastMath.acosInRange(1.1));
        assertEquals(StrictFastMath.acos(-0.1), StrictFastMath.acosInRange(-0.1));
        assertEquals(StrictFastMath.acos(0.1), StrictFastMath.acosInRange(0.1));
        assertEquals(Double.NaN, StrictFastMath.acosInRange(Double.NaN));
    }

    public void test_atan_double() {
        assertEquals(Math.PI/4, StrictFastMath.atan(1.0));
        assertEquals(-Math.PI/4, StrictFastMath.atan(-1.0));
        
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = StrictMath.atan(value);
            double res = StrictFastMath.atan(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM14,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_atan2_2double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double y = randomDoubleWhatever();
            double x = randomDoubleWhatever();
            double ref = StrictMath.atan2(y,x);
            double res = StrictFastMath.atan2(y,x);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM14,
                    y,
                    x);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }
    
    public void test_toRadians_double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double value = randomDoubleWhatever();
            double ref = StrictMath.toRadians(value);
            double res = StrictFastMath.toRadians(value);
            if ((ref == 0.0) && (Math.abs(res) > 0.0)) {
                // Might underflow before us, due to its
                // computation being done in two steps.
                assertTrue(value / 180.0 == 0.0);
                --i;continue;
            }
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM15,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }
    
    public void test_toDegrees_double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double value = randomDoubleWhatever();
            double ref = StrictMath.toDegrees(value);
            double res = StrictFastMath.toDegrees(value);
            if (Double.isInfinite(ref) && (!Double.isInfinite(res))) {
                // Might overflow before us, due to its
                // computation being done in two steps.
                --i;continue;
            }
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM15,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_toRadians_boolean_2int_double() {
        final double before60 = StrictFastMath.nextDown(60.0);
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            boolean sign = this.random.nextBoolean();
            int degrees = this.random.nextInt(181);
            int minutes = this.random.nextInt(60);
            double seconds = randomDoubleUniform(0.0, before60);
            double ref = (sign ? 1 : -1) * StrictMath.toRadians(degrees + (1.0/60) * (minutes + (1.0/60) * seconds));
            double res = StrictFastMath.toRadians(sign, degrees, minutes, seconds);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM15,
                    sign,
                    degrees,
                    minutes,
                    seconds);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_toDegrees_boolean_2int_double() {
        final double before60 = StrictFastMath.nextDown(60.0);
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            boolean sign = this.random.nextBoolean();
            int degrees = this.random.nextInt(181);
            int minutes = this.random.nextInt(60);
            double seconds = randomDoubleUniform(0.0, before60);
            double ref = (sign ? 1 : -1) * (degrees + (1.0/60) * (minutes + (1.0/60) * seconds));
            double res = StrictFastMath.toDegrees(sign, degrees, minutes, seconds);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM15,
                    sign,
                    degrees,
                    minutes,
                    seconds);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    /**
     * Supposes that toRadians(boolean,int,int,double) works.
     */
    public void test_toDMS_double_IntWrapper_IntWrapper_DoubleWrapper() {
        IntWrapper resDegrees = new IntWrapper();
        IntWrapper resMinutes = new IntWrapper();
        DoubleWrapper resSeconds = new DoubleWrapper();
        final double before60 = StrictFastMath.nextDown(60.0);
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            boolean refSign = this.random.nextBoolean();
            int refDegrees = this.random.nextInt(181);
            int refMinutes = this.random.nextInt(60);
            double refSeconds = randomDoubleUniform(0.0, before60);
            double value = StrictFastMath.toRadians(refSign, refDegrees, refMinutes, refSeconds);
            boolean resSign = StrictFastMath.toDMS(value, resDegrees, resMinutes, resSeconds);
            double resRad = StrictFastMath.toRadians(resSign, resDegrees.value, resMinutes.value, resSeconds.value);
            double refRad = refMod(value, resRad, 2*Math.PI);
            boolean log = helper.process(
                    refRad,
                    resRad,
                    Double.NaN,
                    TOL_1EM15,
                    value);
            if (log) {
                System.out.println("refSign = "+refSign);
                System.out.println("resSign = "+resSign);
                System.out.println("refDegrees = "+refDegrees);
                System.out.println("resDegrees = "+resDegrees);
                System.out.println("refMinutes = "+refMinutes);
                System.out.println("resMinutes = "+resMinutes);
                System.out.println("refSeconds = "+refSeconds);
                System.out.println("resSeconds = "+resSeconds);
            }
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_isInClockwiseDomain_3double() {
        assertTrue(StrictFastMath.isInClockwiseDomain(0.0, 2*Math.PI, 0.0));
        assertTrue(StrictFastMath.isInClockwiseDomain(0.0, 2*Math.PI, -Math.PI));
        assertTrue(StrictFastMath.isInClockwiseDomain(0.0, 2*Math.PI, Math.PI));
        assertTrue(StrictFastMath.isInClockwiseDomain(0.0, 2*Math.PI, 2*Math.PI));
        assertTrue(StrictFastMath.isInClockwiseDomain(-Math.PI, 2*Math.PI, -Math.PI));
        assertTrue(StrictFastMath.isInClockwiseDomain(-Math.PI, 2*Math.PI, 0.0));
        assertTrue(StrictFastMath.isInClockwiseDomain(-Math.PI, 2*Math.PI, Math.PI));

        // always in
        for (int i=-10;i<10;i++) {
            double startAngRad = StrictMath.toRadians(55.0*i);
            double spanAngRad = Math.PI/2;
            double angRad = startAngRad + Math.PI/3;
            assertTrue(StrictFastMath.isInClockwiseDomain(startAngRad, spanAngRad, angRad));
        }

        // never in
        for (int i=-10;i<10;i++) {
            double startAngRad = StrictMath.toRadians(55.0*i);
            double spanAngRad = Math.PI/3;
            double angRad = startAngRad + Math.PI/2;
            assertFalse(StrictFastMath.isInClockwiseDomain(startAngRad, spanAngRad, angRad));
        }

        // small angular values
        assertTrue(StrictFastMath.isInClockwiseDomain(0.0, 2*Math.PI, -1e-10));
        assertFalse(StrictFastMath.isInClockwiseDomain(0.0, 2*Math.PI, -1e-20));
        assertTrue(StrictFastMath.isInClockwiseDomain(0.0, 2*StrictFastMath.PI_SUP, -1e-20));
        assertTrue(StrictFastMath.isInClockwiseDomain(1e-10, 2*Math.PI, -1e-20));
        assertFalse(StrictFastMath.isInClockwiseDomain(1e-20, 2*Math.PI, -1e-20));
        assertTrue(StrictFastMath.isInClockwiseDomain(1e-20, 2*StrictFastMath.PI_SUP, -1e-20));

        // NaN
        assertFalse(StrictFastMath.isInClockwiseDomain(Double.NaN, Math.PI, Math.PI/2));
        assertFalse(StrictFastMath.isInClockwiseDomain(Double.NaN, 3*Math.PI, Math.PI/2));
        assertFalse(StrictFastMath.isInClockwiseDomain(0.0, Math.PI, Double.NaN));
        assertFalse(StrictFastMath.isInClockwiseDomain(0.0, 3*Math.PI, Double.NaN));
        assertFalse(StrictFastMath.isInClockwiseDomain(0.0, Double.NaN, Math.PI/2));
    }

    /*
     * hyperbolic trigonometry
     */

    public void test_sinh_double() {
        assertEquals(Double.NEGATIVE_INFINITY, StrictFastMath.sinh(-711.0));
        assertEquals(Double.POSITIVE_INFINITY, StrictFastMath.sinh(711.0));
        assertEquals(Double.NaN, StrictFastMath.sinh(Double.NaN));

        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = random.nextBoolean() ? randomDoubleWhatever() : randomDoubleWhatever(-711,711);
            value = knownBadValues_sinh_cosh_tanh(i, value);
            double ref = StrictMath.sinh(value);
            double res = StrictFastMath.sinh(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_SINH_COSH_REL,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }
    
    public void test_cosh_double() {
        assertEquals(Double.POSITIVE_INFINITY, StrictFastMath.cosh(-711.0));
        assertEquals(Double.POSITIVE_INFINITY, StrictFastMath.cosh(711.0));
        assertEquals(Double.NaN, StrictFastMath.cosh(Double.NaN));

        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = random.nextBoolean() ? randomDoubleWhatever() : randomDoubleWhatever(-711,711);
            value = knownBadValues_sinh_cosh_tanh(i, value);
            double ref = StrictMath.cosh(value);
            double res = StrictFastMath.cosh(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_SINH_COSH_REL,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_coshm1_double() {
        assertEquals(-0.0,StrictFastMath.coshm1(-0.0));
        assertEquals(0.0,StrictFastMath.coshm1(0.0));
        assertEquals(Double.NaN,StrictFastMath.coshm1(Double.NaN));
        
        for (double tiny : new double[]{StrictMath.pow(2, -28),StrictMath.sqrt(2*Double.MIN_VALUE)}) {
            assertEquals(0.5 * tiny*tiny,StrictFastMath.coshm1(tiny));
        }
        
        /*
         * Testing
         * StrictMath.cosh(value) ~= coshm1(value) + 1
         */
        
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = StrictMath.cosh(value);
            double res = StrictFastMath.coshm1(value) + 1;
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM15,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }
    
    public void test_sinhAndCosh_double_DoubleWrapper() {
        final DoubleWrapper tmpCosh = new DoubleWrapper();
        final MyDoubleResHelper sinhHelper = new MyDoubleResHelper("sinh");
        final MyDoubleResHelper coshHelper = new MyDoubleResHelper("cosh");
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = random.nextBoolean() ? randomDoubleWhatever() : randomDoubleWhatever(-711,711);
            value = knownBadValues_sinh_cosh_tanh(i, value);
            double refSinh = StrictMath.sinh(value);
            double refCosh = StrictMath.cosh(value);
            double resSinh = StrictFastMath.sinhAndCosh(value, tmpCosh);
            double resCosh = tmpCosh.value;
            sinhHelper.process(
                    refSinh,
                    resSinh,
                    Double.NaN,
                    TOL_SINH_COSH_REL,
                    value);
            coshHelper.process(
                    refCosh,
                    resCosh,
                    Double.NaN,
                    TOL_SINH_COSH_REL,
                    value);
            assertTrue(sinhHelper.lastOK());
            assertTrue(coshHelper.lastOK());
        }
        sinhHelper.finalLogIfNeeded();
        coshHelper.finalLogIfNeeded();
    }

    public void test_tanh_double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            value = knownBadValues_sinh_cosh_tanh(i, value);
            double ref = StrictMath.tanh(value);
            double res = StrictFastMath.tanh(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM15,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_asinh_double() {
        assertEquals(-0.0, StrictFastMath.asinh(-0.0));
        assertEquals(0.0, StrictFastMath.asinh(0.0));
        assertEquals(Double.NEGATIVE_INFINITY, StrictFastMath.asinh(Double.NEGATIVE_INFINITY));
        assertEquals(Double.POSITIVE_INFINITY, StrictFastMath.asinh(Double.POSITIVE_INFINITY));
        assertEquals(Double.NaN, StrictFastMath.asinh(Double.NaN));
        
        /*
         * Testing asinh(StrictMath.sinh(value)) ~= value.
         */
        
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever(-711.0, 711.0);
            double sinhValue = StrictMath.sinh(value);
            if (NumbersUtils.isNaNOrInfinite(sinhValue)) {
                --i;continue;
            }
            double asinhSinhValue = StrictFastMath.asinh(sinhValue);
            helper.process(
                    asinhSinhValue,
                    value,
                    Double.NaN,
                    TOL_1EM14,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }
    
    public void test_acosh_double() {
        assertEquals(Double.NaN, StrictFastMath.acosh(Double.NaN));
        assertEquals(Double.NaN, StrictFastMath.acosh(-10.0));
        assertEquals(Double.NaN, StrictFastMath.acosh(0.0));
        assertEquals(Double.NaN, StrictFastMath.acosh(0.9));
        assertEquals(0.0, StrictFastMath.acosh(1.0));
        
        assertEquals(Double.POSITIVE_INFINITY, StrictFastMath.acosh(Double.POSITIVE_INFINITY));
        
        /*
         * Testing
         * StrictMath.cosh(acosh(value)) ~= value
         * or
         * acosh(StrictMath.cosh(value)) ~= value
         * (only considering best rel delta of both,
         * to avoid bad delta only due to double precision loss,
         * either for cosh(small values), or acosh(big values)).
         */
        
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            // Only using positive-x-half of cosh.
            double value = randomDoubleWhatever(0.0, 711.0);
            double coshValue = StrictMath.cosh(value);
            if (NumbersUtils.isNaNOrInfinite(coshValue)) {
                --i;continue;
            }
            double acoshCoshValue = StrictFastMath.acosh(coshValue);
            double coshAcoshCoshValue = StrictMath.cosh(acoshCoshValue);
            
            double relDelta1 = relDelta(value, acoshCoshValue);
            double relDelta2 = relDelta(coshValue, coshAcoshCoshValue);
            boolean use1 = (relDelta1 < relDelta2);

            boolean log = helper.process(
                    (use1 ? value : coshValue),
                    (use1 ? acoshCoshValue : coshAcoshCoshValue),
                    Double.NaN,
                    TOL_1EM15,
                    value);
            if (log) {
                System.out.println("value =          "+value);
                System.out.println("coshValue =          "+coshValue);
                System.out.println("acoshCoshValue = "+acoshCoshValue);
                System.out.println("coshAcoshCoshValue = "+coshAcoshCoshValue);
                System.out.println("relDelta1 = "+relDelta1);
                System.out.println("relDelta2 = "+relDelta2);
            }
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_acosh1p_double() {
        assertEquals(Double.NaN,StrictFastMath.acosh1p(Double.NaN));
        assertEquals(0.0,StrictFastMath.acosh1p(0.0));
        assertEquals(-0.0,StrictFastMath.acosh1p(-0.0));
        
        /*
         * Testing
         * acosh1p(StrictMath.cosh(value)-1) ~= value
         * (only considering best rel delta of both,
         * to avoid bad delta only due to double precision loss,
         * for cosh(small values)).
         */
        
        {
            final MyDoubleResHelper helper = new MyDoubleResHelper("cosh as ref");
            for (int i=0;i<NBR_OF_VALUES;i++) {
                // Only using positive-x-half of cosh.
                double value = randomDoubleWhatever(0.0, 711.0);
                double coshValueM1 = StrictMath.cosh(value) - 1;
                if (NumbersUtils.isNaNOrInfinite(coshValueM1)) {
                    --i;continue;
                }
                double acosh1pCoshValueM1 = StrictFastMath.acosh1p(coshValueM1); // acosh(1+cosh(value)-1) = value
                double coshAcosh1pCoshValueM1M1 = StrictMath.cosh(acosh1pCoshValueM1)-1; // cosh(acosh(1+cosh(value)-1))-1 = cosh(value)-1
                double relDelta1 = relDelta(value, acosh1pCoshValueM1);
                double relDelta2 = relDelta(coshValueM1, coshAcosh1pCoshValueM1M1);
                boolean use1 = (relDelta1 < relDelta2);

                boolean log = helper.process(
                        (use1 ? value : coshValueM1),
                        (use1 ? acosh1pCoshValueM1 : coshAcosh1pCoshValueM1M1),
                        Double.NaN,
                        TOL_1EM14,
                        value);
                if (log) {
                    System.out.println("value =              "+value);
                    System.out.println("coshValueM1 =              "+coshValueM1);
                    System.out.println("acosh1pCoshValueM1 = "+acosh1pCoshValueM1);
                    System.out.println("coshAcosh1pCoshValueM1M1 = "+coshAcosh1pCoshValueM1M1);
                    System.out.println("relDelta1 = "+relDelta1);
                    System.out.println("relDelta2 = "+relDelta2);
                }
                assertTrue(helper.lastOK());
            }
            helper.finalLogIfNeeded();
        }
        
        /*
         * Testing against coshm1, for tiny values.
         * (only considering best rel delta of both,
         * to avoid bad delta only due to double precision loss).
         */
        
        {
            final MyDoubleResHelper helper = new MyDoubleResHelper("coshm1 as ref");
            for (int i=0;i<NBR_OF_VALUES;i++) {
                // Only using positive-x-half of cosh.
                double value = randomDoubleWhatever(0.0, Double.MAX_VALUE);
                double coshm1Value = StrictFastMath.coshm1(value); // cosh(value)-1
                if ((coshm1Value == 0.0) || Double.isInfinite(coshm1Value)) {
                    // Underflow or overflow.
                    --i;continue;
                }
                double acosh1pCoshm1Value = StrictFastMath.acosh1p(coshm1Value); // acosh(1+cosh(value)-1) = value
                double coshm1Acosh1pCoshm1Value = StrictFastMath.coshm1(acosh1pCoshm1Value); // cosh(acosh(1+cosh(value)-1))-1 = cosh(value)-1
                double relDelta1 = relDelta(value, acosh1pCoshm1Value);
                double relDelta2 = relDelta(coshm1Value, coshm1Acosh1pCoshm1Value);
                boolean use1 = (relDelta1 < relDelta2);

                boolean log = helper.process(
                        (use1 ? value : coshm1Value),
                        (use1 ? acosh1pCoshm1Value : coshm1Acosh1pCoshm1Value),
                        Double.NaN,
                        1e-7,
                        value);
                if (log) {
                    System.out.println("value =              "+value);
                    System.out.println("coshm1Value =              "+coshm1Value);
                    System.out.println("acosh1pCoshm1Value = "+acosh1pCoshm1Value);
                    System.out.println("coshm1Acosh1pCoshm1Value = "+coshm1Acosh1pCoshm1Value);
                    System.out.println("relDelta1 = "+relDelta1);
                    System.out.println("relDelta2 = "+relDelta2);
                }
                assertTrue(helper.lastOK());
            }
            helper.finalLogIfNeeded();
        }
    }

    public void test_atanh_double() {
        assertEquals(Double.NaN, StrictFastMath.atanh(Double.NaN));
        
        assertEquals(Double.NaN, StrictFastMath.atanh(-1.1));
        assertEquals(Double.NEGATIVE_INFINITY, StrictFastMath.atanh(-1.0));
        
        assertEquals(Double.POSITIVE_INFINITY, StrictFastMath.atanh(1.0));
        assertEquals(Double.NaN, StrictFastMath.atanh(1.1));
        
        /*
         * Testing
         * StrictMath.tanh(atanh(value)) ~= value
         * or
         * atanh(StrictMath.tanh(value)) ~= value
         * (only considering best rel delta of both,
         * to avoid bad delta only due to double precision loss,
         * either for tanh(big values), or atanh(values near +-1)).
         */
        
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever(-711.0, 711.0);
            double tanhValue = StrictMath.tanh(value);
            if (!(Math.abs(tanhValue) < 1.0)) {
                // NaN or limit.
                --i;continue;
            }
            double atanhTanhValue = StrictFastMath.atanh(tanhValue);
            double tanhAtanhTanhValue = StrictMath.tanh(atanhTanhValue);
            double relDelta1 = relDelta(value, atanhTanhValue);
            double relDelta2 = relDelta(tanhValue, tanhAtanhTanhValue);
            boolean use1 = (relDelta1 < relDelta2);

            boolean log = helper.process(
                    (use1 ? value : tanhValue),
                    (use1 ? atanhTanhValue : tanhAtanhTanhValue),
                    Double.NaN,
                    TOL_1EM14,
                    value);
            if (log) {
                System.out.println("value =          "+value);
                System.out.println("tanhValue =          "+tanhValue);
                System.out.println("atanhTanhValue = "+atanhTanhValue);
                System.out.println("tanhAtanhTanhValue = "+tanhAtanhTanhValue);
                System.out.println("relDelta1 = "+relDelta1);
                System.out.println("relDelta2 = "+relDelta2);
            }
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }
    
    /*
     * exponentials
     */
    
    public void test_exp_double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = this.random.nextBoolean() ? randomDoubleWhatever() : randomDoubleWhatever(-746.0, 710.0);
            double ref = StrictMath.exp(value);
            double res = StrictFastMath.exp(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM15,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_expQuick_double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever(-700.0, 700.0);
            value = knownBadValues_expQuick_double(i, value);
            double ref = StrictMath.exp(value);
            double res = StrictFastMath.expQuick(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    2.94e-2,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_expm1_double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = this.random.nextBoolean() ? randomDoubleWhatever() : randomDoubleWhatever(-746.0, 710.0);
            double ref = StrictMath.expm1(value);
            double res = StrictFastMath.expm1(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM15,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }
    
    /*
     * logarithms
     */

    public void test_log_double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            value = knownBadValues_logQuick_double(i, value);
            double ref = StrictMath.log(value);
            double res = StrictFastMath.log(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM14,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_logQuick_double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever(DOUBLE_MIN_NORMAL, Double.MAX_VALUE);
            double ref = StrictMath.log(value);
            double res = StrictFastMath.logQuick(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    1.9e-3,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_log10_double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = StrictMath.log10(value);
            double res = StrictFastMath.log10(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM14,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_log1p_double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = StrictMath.log1p(value);
            double res = StrictFastMath.log1p(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM14,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_log2_int() {
        for (int value : new int[]{Integer.MIN_VALUE,0}) {
            try {
                StrictFastMath.log2(value);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        for (int p=0;p<=30;p++) {
            int pot = (1<<p);

            if (p != 0) {
                assertEquals(p-1, StrictFastMath.log2(pot-1));
            }
            assertEquals(p, StrictFastMath.log2(pot));
            assertEquals(p, StrictFastMath.log2(pot+pot-1));
            if (p != 30) {
                assertEquals(p+1, StrictFastMath.log2(pot+pot));
            }
        }
    }

    public void test_log2_long() {
        for (long value : new long[]{Long.MIN_VALUE,0}) {
            try {
                StrictFastMath.log2(value);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        for (int p=0;p<=62;p++) {
            long pot = (1L<<p);

            if (p != 0) {
                assertEquals(p-1, StrictFastMath.log2(pot-1));
            }
            assertEquals(p, StrictFastMath.log2(pot));
            assertEquals(p, StrictFastMath.log2(pot+pot-1));
            if (p != 62) {
                assertEquals(p+1, StrictFastMath.log2(pot+pot));
            }
        }
    }
    
    /*
     * powers
     */
    
    public void test_pow_2double() {
        assertEquals(1.0, StrictFastMath.pow(0.0,0.0));
        assertEquals(0.0, StrictFastMath.pow(0.0,2.0));
        assertEquals(0.0, StrictFastMath.pow(-0.0,2.0));
        assertEquals(Double.POSITIVE_INFINITY, StrictFastMath.pow(0.0,-2.0));
        assertEquals(0.0, StrictFastMath.pow(0.0,3.0));
        assertEquals(-0.0, StrictFastMath.pow(-0.0,3.0));
        assertEquals(Double.POSITIVE_INFINITY, StrictFastMath.pow(0.0,-3.0));
        assertEquals(4.0, StrictFastMath.pow(2.0,2.0), TOL_1EM15);
        assertEquals(8.0, StrictFastMath.pow(2.0,3.0), TOL_1EM15);
        assertEquals(1.0/4.0, StrictFastMath.pow(2.0,-2.0), TOL_1EM15);
        assertEquals(1.0/8.0, StrictFastMath.pow(2.0,-3.0), TOL_1EM15);
        assertEquals(Double.POSITIVE_INFINITY, StrictFastMath.pow(Double.NEGATIVE_INFINITY,2.0));
        assertEquals(Double.NEGATIVE_INFINITY, StrictFastMath.pow(Double.NEGATIVE_INFINITY,3.0));
        assertEquals(0.0, StrictFastMath.pow(Double.NEGATIVE_INFINITY,-2.0));
        assertEquals(-0.0, StrictFastMath.pow(Double.NEGATIVE_INFINITY,-3.0));
        assertEquals(Double.POSITIVE_INFINITY, StrictFastMath.pow(-2.0,(1L<<40))); // even power
        assertEquals(Double.NEGATIVE_INFINITY, StrictFastMath.pow(-2.0,(1L<<40)+1)); // odd power
        assertEquals(Double.NaN, StrictFastMath.pow(Double.NaN,1.0));
        assertEquals(Double.NaN, StrictFastMath.pow(1.0,Double.NaN));
        assertEquals(Double.NaN, StrictFastMath.pow(Double.NaN,-1.0));
        assertEquals(Double.NaN, StrictFastMath.pow(-1.0,Double.NaN));

        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = randomDoubleWhatever();
            double b = randomDoubleWhatever();
            a = knownBadValues_pow_2double_a(i, a);
            b = knownBadValues_pow_2double_b(i, b);
            double ref = StrictMath.pow(a,b);
            double res = StrictFastMath.pow(a,b);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    3e-12,
                    a,
                    b);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_powQuick_2double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = randomDoubleWhatever(DOUBLE_MIN_NORMAL, Double.MAX_VALUE);
            double b = randomDoubleWhatever(-Double.MAX_VALUE, Double.MAX_VALUE);
            a = knownBadValues_powQuick_2double_a(i, a);
            b = knownBadValues_powQuick_2double_b(i, b);
            double ref = StrictMath.pow(a, b);
            double res = StrictFastMath.powQuick(a,b);
            double absRef = Math.abs(ref);
            final double relTol;
            if ((absRef > 1e-10) && (absRef < 1e10)) {
                relTol = 1e-2;
            } else if ((absRef > 1e-40) && (absRef < 1e40)) {
                relTol = 6e-2;
            } else {
                --i;continue;
            }
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    relTol,
                    a,
                    b);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_powFast_double_int() {
        assertEquals(1.0, StrictFastMath.powFast(1.0,Integer.MIN_VALUE));
        assertEquals(Double.POSITIVE_INFINITY, StrictFastMath.powFast(Double.MIN_VALUE,Integer.MIN_VALUE));
        assertEquals(Double.NaN, StrictFastMath.powFast(Double.NaN,1));
        
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = randomDoubleWhatever(-10.0,10.0);
            int b = randomIntUniform(-10,10);
            double ref = StrictMath.pow(a,b);
            double res = StrictFastMath.powFast(a,b);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    2e-15,
                    a,
                    b);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_twoPow_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            int value = randomIntWhatever();
            double ref = StrictMath.pow(2,value);
            double res = StrictFastMath.twoPow(value);
            assertEquals(ref, res);
        }
    }

    public void test_pow2_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            int value = randomIntWhatever();
            int ref = value*value;
            int res = StrictFastMath.pow2(value);
            assertEquals(ref, res);
        }
    }

    public void test_pow2_long() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            long value = randomLongWhatever();
            long ref = value*value;
            long res = StrictFastMath.pow2(value);
            assertEquals(ref, res);
        }
    }

    public void test_pow2_float() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float value = randomFloatWhatever();
            float ref = value*value;
            float res = StrictFastMath.pow2(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM15,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_pow2_double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double value = randomDoubleWhatever();
            double ref = value*value;
            double res = StrictFastMath.pow2(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM15,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_pow3_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            int value = randomIntWhatever();
            int ref = value*value*value;
            int res = StrictFastMath.pow3(value);
            assertEquals(ref, res);
        }
    }

    public void test_pow3_long() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            long value = randomLongWhatever();
            long ref = value*value*value;
            long res = StrictFastMath.pow3(value);
            assertEquals(ref, res);
        }
    }

    public void test_pow3_float() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float value = randomFloatWhatever();
            float ref = value*value*value;
            float res = StrictFastMath.pow3(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM15,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_pow3_double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double value = randomDoubleWhatever();
            double ref = value*value*value;
            double res = StrictFastMath.pow3(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM15,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }
    
    /*
     * roots
     */

    public void test_sqrt_double() {
        assertEquals(-0.0, StrictFastMath.sqrt(-0.0));
        assertEquals(0.0, StrictFastMath.sqrt(0.0));
        assertEquals(Double.NaN, StrictFastMath.sqrt(Double.NaN));
        
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = StrictMath.sqrt(value);
            double res = StrictFastMath.sqrt(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM15,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_sqrtQuick_double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever(DOUBLE_MIN_NORMAL, Double.MAX_VALUE);
            value = knownBadValues_sqrtQuick_double(i, value);
            double ref = StrictMath.sqrt(value);
            double res = StrictFastMath.sqrtQuick(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    3.41e-2,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_invSqrtQuick_double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever(DOUBLE_MIN_NORMAL, Double.MAX_VALUE);
            value = knownBadValues_invSqrtQuick_double(i, value);
            double ref = 1/StrictMath.sqrt(value);
            double res = StrictFastMath.invSqrtQuick(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    3.44e-2,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_cbrt_double() {
        assertEquals(-0.0, StrictFastMath.cbrt(-0.0));
        assertEquals(0.0, StrictFastMath.cbrt(0.0));
        assertEquals(Double.NaN, StrictFastMath.cbrt(Double.NaN));
        
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhatever();
            double ref = StrictMath.cbrt(value);
            double res = StrictFastMath.cbrt(value);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM15,
                    value);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_hypot_2double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double x = randomDoubleWhatever();
            double y = this.random.nextBoolean() ? x * randomDoubleUniform(1e-16, 1e16) : randomDoubleWhatever();
            double ref = StrictMath.hypot(x,y);
            double res = StrictFastMath.hypot(x,y);
            helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM15,
                    x,
                    y);
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }

    public void test_hypot_3double() {
        final MyDoubleResHelper helper = new MyDoubleResHelper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double x = randomDoubleWhatever();
            double y = this.random.nextBoolean() ? x * randomDoubleUniform(1e-16, 1e16) : randomDoubleWhatever();
            double z = this.random.nextBoolean() ? y * randomDoubleUniform(1e-16, 1e16) : randomDoubleWhatever();
            double xy = StrictMath.hypot(x,y);
            double xz = StrictMath.hypot(x,z);
            double yz = StrictMath.hypot(y,z);
            double xyz = StrictMath.hypot(xy,z);
            double xzy = StrictMath.hypot(xz,y);
            double yzx = StrictMath.hypot(yz,x);
            // max(+Infinity,NaN) = NaN
            double ref = Math.max(xyz, Math.max(xzy, yzx));
            double res = StrictFastMath.hypot(x,y,z);
            boolean log = helper.process(
                    ref,
                    res,
                    Double.NaN,
                    TOL_1EM15,
                    x,
                    y,
                    z);
            if (log) {
                System.out.println("xy =  "+xy);
                System.out.println("xz =  "+xz);
                System.out.println("yz =  "+yz);
                System.out.println("xyz = "+xyz);
                System.out.println("xzy = "+xzy);
                System.out.println("yzx = "+yzx);
            }
            assertTrue(helper.lastOK());
        }
        helper.finalLogIfNeeded();
    }
    
    /*
     * absolute values
     */

    public void test_abs_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            int value = randomIntWhatever();
            int ref = Math.abs(value);
            int res = StrictFastMath.abs(value);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_abs_long() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            long value = randomLongWhatever();
            long ref = Math.abs(value);
            long res = StrictFastMath.abs(value);
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

        assertEquals(Integer.MAX_VALUE, StrictFastMath.toIntExact((long)Integer.MAX_VALUE));
        try {
            StrictFastMath.toIntExact(((long)Integer.MAX_VALUE)+1L);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_toInt_long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MAX_VALUE, StrictFastMath.toInt((long)Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, StrictFastMath.toInt(Long.MAX_VALUE));
    }

    public void test_floor_float() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float value = randomFloatWhatever();
            float ref = (float)Math.floor(value);
            float res = StrictFastMath.floor(value);
            assertEquals(ref, res);
        }
    }

    public void test_floor_double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.floor(value);
            double res = StrictFastMath.floor(value);
            assertEquals(ref, res);
        }
    }
    
    public void test_ceil_float() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float value = randomFloatWhatever();
            float ref = (float)Math.ceil(value);
            float res = StrictFastMath.ceil(value);
            assertEquals(ref, res);
        }
    }

    public void test_ceil_double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.ceil(value);
            double res = StrictFastMath.ceil(value);
            assertEquals(ref, res);
        }
    }

    public void test_round_float() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
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
            int res = StrictFastMath.round(value);
            boolean ok = (ref == res);
            if (!ok) {
                printCallerName();
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
            }
            assertTrue(ok);
        }
    }

    public void test_round_double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
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
            long res = StrictFastMath.round(value);
            boolean ok = (ref == res);
            if (!ok) {
                printCallerName();
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
            }
            assertTrue(ok);
        }
    }

    public void test_roundEven_float() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
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
            int res = StrictFastMath.roundEven(value);
            boolean ok = (ref == res);
            if (!ok) {
                printCallerName();
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
            }
            assertTrue(ok);
        }
    }

    public void test_roundEven_double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
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
            long res = StrictFastMath.roundEven(value);
            boolean ok = (ref == res);
            if (!ok) {
                printCallerName();
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
            }
            assertTrue(ok);
        }
    }

    public void test_rint_float() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float value = randomFloatWhatever();
            float ref = (float)Math.rint((double)value);
            float res = StrictFastMath.rint(value);
            boolean ok = equivalent(ref,res);
            if (!ok) {
                printCallerName();
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
            }
            assertTrue(ok);
        }
    }

    public void test_rint_double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.rint(value);
            double res = StrictFastMath.rint(value);
            boolean ok = equivalent(ref,res);
            if (!ok) {
                printCallerName();
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
        assertEquals(0, StrictFastMath.toRange(0, 2, -1));
        assertEquals(0, StrictFastMath.toRange(0, 2, 0));
        assertEquals(1, StrictFastMath.toRange(0, 2, 1));
        assertEquals(2, StrictFastMath.toRange(0, 2, 2));
        assertEquals(2, StrictFastMath.toRange(0, 2, 3));
    }

    public void test_toRange_3long() {
        assertEquals(0L, StrictFastMath.toRange(0L, 2L, -1L));
        assertEquals(0L, StrictFastMath.toRange(0L, 2L, 0L));
        assertEquals(1L, StrictFastMath.toRange(0L, 2L, 1L));
        assertEquals(2L, StrictFastMath.toRange(0L, 2L, 2L));
        assertEquals(2L, StrictFastMath.toRange(0L, 2L, 3L));
    }

    public void test_toRange_3float() {
        assertEquals(0.0f, StrictFastMath.toRange(0.0f, 2.0f, -1.0f));
        assertEquals(0.0f, StrictFastMath.toRange(0.0f, 2.0f, 0.0f));
        assertEquals(1.0f, StrictFastMath.toRange(0.0f, 2.0f, 1.0f));
        assertEquals(2.0f, StrictFastMath.toRange(0.0f, 2.0f, 2.0f));
        assertEquals(2.0f, StrictFastMath.toRange(0.0f, 2.0f, 3.0f));
    }

    public void test_toRange_3double() {
        assertEquals(0.0, StrictFastMath.toRange(0.0, 2.0, -1.0));
        assertEquals(0.0, StrictFastMath.toRange(0.0, 2.0, 0.0));
        assertEquals(1.0, StrictFastMath.toRange(0.0, 2.0, 1.0));
        assertEquals(2.0, StrictFastMath.toRange(0.0, 2.0, 2.0));
        assertEquals(2.0, StrictFastMath.toRange(0.0, 2.0, 3.0));
    }

    /*
     * binary operators (+,-,*)
     */

    public void test_addExact_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MAX_VALUE, StrictFastMath.addExact(Integer.MAX_VALUE-1, 1));
        try {
            StrictFastMath.addExact(Integer.MAX_VALUE, 1);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_addExact_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MAX_VALUE, StrictFastMath.addExact(Long.MAX_VALUE-1L, 1L));
        try {
            StrictFastMath.addExact(Long.MAX_VALUE, 1L);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_addBounded_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MAX_VALUE, StrictFastMath.addBounded(Integer.MAX_VALUE-1, 1));
        assertEquals(Integer.MAX_VALUE, StrictFastMath.addBounded(Integer.MAX_VALUE, 1));
    }

    public void test_addBounded_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MAX_VALUE, StrictFastMath.addBounded(Long.MAX_VALUE-1L, 1L));
        assertEquals(Long.MAX_VALUE, StrictFastMath.addBounded(Long.MAX_VALUE, 1L));
    }

    public void test_subtractExact_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MIN_VALUE, StrictFastMath.subtractExact(Integer.MIN_VALUE+1, 1));
        try {
            StrictFastMath.subtractExact(Integer.MIN_VALUE, 1);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_subtractExact_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MIN_VALUE, StrictFastMath.subtractExact(Long.MIN_VALUE+1L, 1L));
        try {
            StrictFastMath.subtractExact(Long.MIN_VALUE, 1L);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_subtractBounded_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MIN_VALUE, StrictFastMath.subtractBounded(Integer.MIN_VALUE+1, 1));
        assertEquals(Integer.MIN_VALUE, StrictFastMath.subtractBounded(Integer.MIN_VALUE, 1));
    }

    public void test_subtractBounded_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MIN_VALUE, StrictFastMath.subtractBounded(Long.MIN_VALUE+1L, 1L));
        assertEquals(Long.MIN_VALUE, StrictFastMath.subtractBounded(Long.MIN_VALUE, 1L));
    }

    public void test_multiplyExact_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MIN_VALUE, StrictFastMath.multiplyExact(Integer.MIN_VALUE/2, 2));
        try {
            StrictFastMath.multiplyExact(Integer.MIN_VALUE, 2);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_multiplyExact_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MIN_VALUE, StrictFastMath.multiplyExact(Long.MIN_VALUE/2L, 2L));
        try {
            StrictFastMath.multiplyExact(Long.MIN_VALUE, 2L);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_multiplyBounded_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MIN_VALUE, StrictFastMath.multiplyBounded(Integer.MIN_VALUE/2, 2));
        assertEquals(Integer.MIN_VALUE, StrictFastMath.multiplyBounded(Integer.MIN_VALUE, 2));
    }

    public void test_multiplyBounded_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MIN_VALUE, StrictFastMath.multiplyBounded(Long.MIN_VALUE/2L, 2L));
        assertEquals(Long.MIN_VALUE, StrictFastMath.multiplyBounded(Long.MIN_VALUE, 2L));
    }

    /*
     * binary operators (/,%)
     */

    public void test_floorDiv_2int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final int x = randomIntWhatever();
            int y = randomIntWhatever();
            if (y == 0) {
                try {
                    StrictFastMath.floorDiv(x,y);
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
                final int actual = StrictFastMath.floorDiv(x,y);
                assertEquals(expected, actual);
            }
        }
    }

    public void test_floorDiv_2long() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final long x = randomLongWhatever();
            long y = randomLongWhatever();
            if (y == 0) {
                try {
                    StrictFastMath.floorDiv(x,y);
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
                final long actual = StrictFastMath.floorDiv(x,y);
                assertEquals(expected, actual);
            }
        }
    }

    public void test_floorMod_2int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final int x = randomIntWhatever();
            int y = randomIntWhatever();
            if (y == 0) {
                try {
                    StrictFastMath.floorMod(x,y);
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
                final int actual = StrictFastMath.floorMod(x,y);
                assertEquals(expected, actual);
                
                // identity
                assertEquals(x - StrictFastMath.floorDiv(x, y) * y, StrictFastMath.floorMod(x,y));
            }
        }
    }

    public void test_floorMod_2long() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            long x = randomIntWhatever();
            long y = randomIntWhatever();
            if (y == 0) {
                try {
                    StrictFastMath.floorMod(x,y);
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
                long actual = StrictFastMath.floorMod(x,y);
                assertEquals(expected, actual);
                
                // identity
                assertEquals(x - StrictFastMath.floorDiv(x, y) * y, StrictFastMath.floorMod(x,y));
            }
        }
    }

    public void test_remainder_2double() {
        /* Can have that kind of failure with Java 5 or 6,
         * but it's just a "%" bug (bug_id=8015396).
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
            double res = StrictFastMath.remainder(a,b);
            boolean ok = equivalent(ref,res);
            if (!ok) {
                printCallerName();
                System.out.println("a = "+a);
                System.out.println("b = "+b);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
                System.out.println("IEEE = "+Math.IEEEremainder(a,b));
            }
            assertTrue(ok);
        }
    }
    
    public void test_normalizeMinusPiPi_double() {
        final MyDoubleResHelper normHelper = new MyDoubleResHelper("norm");
        final MyDoubleResHelper sinHelper = new MyDoubleResHelper("sin");
        final MyDoubleResHelper cosHelper = new MyDoubleResHelper("cos");
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhateverOrPiIsh();
            value = knownBadValues_trigNorm(i, value);
            double res = StrictFastMath.normalizeMinusPiPi(value);
            if (NumbersUtils.isInRange(-Math.PI, Math.PI, value)) {
                // Unchanged if already in range.
                assertEquals(value, res);
                continue;
            }
            double refSin = StrictMath.sin(value);
            double refCos = StrictMath.cos(value);
            double ref = StrictMath.atan2(refSin, refCos);
            double resSin = StrictMath.sin(res);
            double resCos = StrictMath.cos(res);
            double relTolSin;
            double relTolCos;
            if (Math.abs(res) <= Math.PI/4) {
                relTolSin = TOL_TRIG_NORM_REL;
                relTolCos = TOL_1EM15;
            } else {
                // Relative delta might be bad due to large ULPs.
                relTolSin = Double.POSITIVE_INFINITY;
                relTolCos = Double.POSITIVE_INFINITY;
            }
            boolean logNorm = normHelper.process(
                    ref,
                    res,
                    TOL_1EM15,
                    TOL_TRIG_NORM_REL,
                    value);
            boolean logSin = sinHelper.process(
                    refSin,
                    resSin,
                    TOL_1EM15,
                    relTolSin,
                    value);
            boolean logCos = cosHelper.process(
                    refCos,
                    resCos,
                    TOL_1EM15,
                    relTolCos,
                    value);
            if (logNorm || logSin || logCos) {
                System.out.println("ref = "+StrictMath.toDegrees(ref)+" deg");
                System.out.println("res = "+StrictMath.toDegrees(res)+" deg");
                System.out.println("refSin = "+refSin);
                System.out.println("resSin = "+resSin);
                System.out.println("refCos = "+refCos);
                System.out.println("resCos = "+resCos);
            }
            assertTrue(normHelper.lastOK());
            assertTrue(sinHelper.lastOK());
            assertTrue(cosHelper.lastOK());
            boolean rangeOK = (Double.isNaN(res) || NumbersUtils.isInRange(-Math.PI, Math.PI, res));
            if (!rangeOK) {
                System.out.println("value = "+value);
                System.out.println("res = "+res);
            }
            assertTrue(rangeOK);
        }
        normHelper.finalLogIfNeeded();
        sinHelper.finalLogIfNeeded();
        cosHelper.finalLogIfNeeded();
    }

    public void test_normalizeMinusPiPiFast_double() {
        final MyDoubleResHelper sinHelper = new MyDoubleResHelper("sin");
        final MyDoubleResHelper cosHelper = new MyDoubleResHelper("cos");
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhateverOrPiIsh();
            if (Math.abs(value) > MAX_VALUE_FAST_TRIG_NORM) {
                value = randomDoubleUniform(-MAX_VALUE_FAST_TRIG_NORM,MAX_VALUE_FAST_TRIG_NORM);
            }
            double res = StrictFastMath.normalizeMinusPiPiFast(value);
            if (NumbersUtils.isInRange(-Math.PI, Math.PI, value)) {
                // Unchanged if already in range.
                assertEquals(value, res);
                continue;
            }
            double refSin = StrictMath.sin(value);
            double resSin = StrictMath.sin(res);
            double refCos = StrictMath.cos(value);
            double resCos = StrictMath.cos(res);
            boolean logSin = sinHelper.process(
                    refSin,
                    resSin,
                    TOL_FAST_TRIG_NORM_ABS,
                    Double.NaN,
                    value);
            boolean logCos = cosHelper.process(
                    refCos,
                    resCos,
                    TOL_FAST_TRIG_NORM_ABS,
                    Double.NaN,
                    value);
            if (logSin || logCos) {
                System.out.println("refSin = "+refSin);
                System.out.println("resSin = "+resSin);
                System.out.println("refCos = "+refCos);
                System.out.println("resCos = "+resCos);
            }
            assertTrue(sinHelper.lastOK());
            assertTrue(cosHelper.lastOK());
            boolean rangeOK = (Double.isNaN(res) || NumbersUtils.isInRange(-Math.PI, Math.PI, res));
            if (!rangeOK) {
                System.out.println("value = "+value);
                System.out.println("res = "+res);
            }
            assertTrue(rangeOK);
        }
        sinHelper.finalLogIfNeeded();
        cosHelper.finalLogIfNeeded();
    }

    public void test_normalizeZeroTwoPi_double() {
        final MyDoubleResHelper normHelper = new MyDoubleResHelper("norm");
        final MyDoubleResHelper sinHelper = new MyDoubleResHelper("sin");
        final MyDoubleResHelper cosHelper = new MyDoubleResHelper("cos");
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhateverOrPiIsh();
            value = knownBadValues_trigNorm(i, value);
            double res = StrictFastMath.normalizeZeroTwoPi(value);
            if (NumbersUtils.isInRange(0.0, 2*Math.PI, value)) {
                // Unchanged if already in range.
                assertEquals(value, res);
                continue;
            }
            double refSin = StrictMath.sin(value);
            double refCos = StrictMath.cos(value);
            double ref = StrictMath.atan2(refSin, refCos);
            if (ref < 0.0) {
                ref = NumbersUtils.plus2PI_strict(ref);
            }
            double resSin = StrictMath.sin(res);
            double resCos = StrictMath.cos(res);
            double relTolSin;
            double relTolCos;
            if (Math.abs(res) <= Math.PI/4) {
                relTolSin = TOL_TRIG_NORM_REL;
                relTolCos = TOL_1EM15;
            } else {
                // Relative delta might be bad due to large ULPs.
                relTolSin = Double.POSITIVE_INFINITY;
                relTolCos = Double.POSITIVE_INFINITY;
            }
            boolean logNorm = normHelper.process(
                    ref,
                    res,
                    TOL_1EM15,
                    TOL_TRIG_NORM_REL,
                    value);
            boolean logSin = sinHelper.process(
                    refSin,
                    resSin,
                    TOL_1EM15,
                    relTolSin,
                    value);
            boolean logCos = cosHelper.process(
                    refCos,
                    resCos,
                    TOL_1EM15,
                    relTolCos,
                    value);
            if (logNorm || logSin || logCos) {
                System.out.println("ref = "+StrictMath.toDegrees(ref)+" deg");
                System.out.println("res = "+StrictMath.toDegrees(res)+" deg");
                System.out.println("refSin = "+refSin);
                System.out.println("resSin = "+resSin);
                System.out.println("refCos = "+refCos);
                System.out.println("resCos = "+resCos);
            }
            assertTrue(normHelper.lastOK());
            assertTrue(sinHelper.lastOK());
            assertTrue(cosHelper.lastOK());
            boolean rangeOK = (Double.isNaN(res) || NumbersUtils.isInRange(0.0, 2*Math.PI, res));
            if (!rangeOK) {
                System.out.println("value = "+value);
                System.out.println("res = "+res);
            }
            assertTrue(rangeOK);
        }
        normHelper.finalLogIfNeeded();
        sinHelper.finalLogIfNeeded();
        cosHelper.finalLogIfNeeded();
    }

    public void test_normalizeZeroTwoPiFast_double() {
        final MyDoubleResHelper sinHelper = new MyDoubleResHelper("sin");
        final MyDoubleResHelper cosHelper = new MyDoubleResHelper("cos");
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhateverOrPiIsh();
            if (Math.abs(value) > MAX_VALUE_FAST_TRIG_NORM) {
                value = randomDoubleUniform(-MAX_VALUE_FAST_TRIG_NORM,MAX_VALUE_FAST_TRIG_NORM);
            }
            double res = StrictFastMath.normalizeZeroTwoPiFast(value);
            if (NumbersUtils.isInRange(0.0, 2*Math.PI, value)) {
                // Unchanged if already in range.
                assertEquals(value, res);
                continue;
            }
            double refSin = StrictMath.sin(value);
            double resSin = StrictMath.sin(res);
            double refCos = StrictMath.cos(value);
            double resCos = StrictMath.cos(res);
            boolean logSin = sinHelper.process(
                    refSin,
                    resSin,
                    TOL_FAST_TRIG_NORM_ABS,
                    Double.NaN,
                    value);
            boolean logCos = cosHelper.process(
                    refCos,
                    resCos,
                    TOL_FAST_TRIG_NORM_ABS,
                    Double.NaN,
                    value);
            if (logSin || logCos) {
                System.out.println("refSin = "+refSin);
                System.out.println("resSin = "+resSin);
                System.out.println("refCos = "+refCos);
                System.out.println("resCos = "+resCos);
            }
            assertTrue(sinHelper.lastOK());
            assertTrue(cosHelper.lastOK());
            boolean rangeOK = (Double.isNaN(res) || NumbersUtils.isInRange(0.0, 2*Math.PI, res));
            if (!rangeOK) {
                System.out.println("value = "+value);
                System.out.println("res = "+res);
            }
            assertTrue(rangeOK);
        }
        sinHelper.finalLogIfNeeded();
        cosHelper.finalLogIfNeeded();
    }

    public void test_normalizeMinusHalfPiHalfPi_double() {
        final MyDoubleResHelper normHelper = new MyDoubleResHelper("norm");
        final MyDoubleResHelper sinHelper = new MyDoubleResHelper("sin");
        final MyDoubleResHelper cosHelper = new MyDoubleResHelper("cos");
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhateverOrPiIsh();
            value = knownBadValues_trigNorm(i, value);
            double res = StrictFastMath.normalizeMinusHalfPiHalfPi(value);
            if (NumbersUtils.isInRange(-Math.PI/2, Math.PI/2, value)) {
                // Unchanged if already in range.
                assertEquals(value, res);
                continue;
            }
            double refSin = StrictMath.sin(value);
            double refCos = StrictMath.cos(value);
            if (refCos < 0.0) {
                refSin = -refSin;
                refCos = -refCos;
            }
            double ref = StrictMath.atan2(refSin, refCos);
            double resSin = StrictMath.sin(res);
            double resCos = StrictMath.cos(res);
            double relTolSin;
            double relTolCos;
            if (Math.abs(res) <= Math.PI/4) {
                relTolSin = TOL_TRIG_NORM_REL;
                relTolCos = TOL_1EM15;
            } else {
                // Sin close to 1, so accurate.
                relTolSin = TOL_1EM15;
                // Relative delta might be bad due to large ULPs.
                relTolCos = Double.POSITIVE_INFINITY;
            }
            boolean logNorm = normHelper.process(
                    ref,
                    res,
                    TOL_1EM15,
                    TOL_TRIG_NORM_REL,
                    value);
            boolean logSin = sinHelper.process(
                    refSin,
                    resSin,
                    TOL_1EM15,
                    relTolSin,
                    value);
            boolean logCos = cosHelper.process(
                    refCos,
                    resCos,
                    TOL_1EM15,
                    relTolCos,
                    value);
            if (logNorm || logSin || logCos) {
                System.out.println("ref = "+StrictMath.toDegrees(ref)+" deg");
                System.out.println("res = "+StrictMath.toDegrees(res)+" deg");
                System.out.println("refSin = "+refSin);
                System.out.println("resSin = "+resSin);
                System.out.println("refCos = "+refCos);
                System.out.println("resCos = "+resCos);
            }
            assertTrue(normHelper.lastOK());
            assertTrue(sinHelper.lastOK());
            assertTrue(cosHelper.lastOK());
            boolean rangeOK = (Double.isNaN(res) || NumbersUtils.isInRange(-Math.PI/2, Math.PI/2, res));
            if (!rangeOK) {
                System.out.println("value = "+value);
                System.out.println("res = "+res);
            }
            assertTrue(rangeOK);
        }
        normHelper.finalLogIfNeeded();
        sinHelper.finalLogIfNeeded();
        cosHelper.finalLogIfNeeded();
    }

    public void test_normalizeMinusHalfPiHalfPiFast_double() {
        final MyDoubleResHelper sinHelper = new MyDoubleResHelper("sin");
        final MyDoubleResHelper cosHelper = new MyDoubleResHelper("cos");
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = randomDoubleWhateverOrPiIsh();
            if (Math.abs(value) > MAX_VALUE_FAST_TRIG_NORM) {
                value = randomDoubleUniform(-MAX_VALUE_FAST_TRIG_NORM,MAX_VALUE_FAST_TRIG_NORM);
            }
            double res = StrictFastMath.normalizeMinusHalfPiHalfPiFast(value);
            if (NumbersUtils.isInRange(-Math.PI/2, Math.PI/2, value)) {
                // Unchanged if already in range.
                assertEquals(value, res);
                continue;
            }
            double refSin = StrictMath.sin(value);
            double refCos = StrictMath.cos(value);
            if (refCos < 0.0) {
                refSin = -refSin;
                refCos = -refCos;
            }
            double resSin = StrictMath.sin(res);
            double resCos = StrictMath.cos(res);
            // Normalization of value done in sin or cos
            // can end up in the left side of the trigonometric circle,
            // and we normalize into its right side,
            // so cos signs can be different, but their
            // absolute values should be close.
            boolean sinSignDiff = Math.signum(refSin) != Math.signum(resSin);
            boolean cosSignDiff = Math.signum(refCos) != Math.signum(resCos);
            if (sinSignDiff) {
                refSin = -refSin;
            }
            if (cosSignDiff) {
                refCos = -refCos;
            }
            boolean logSin = sinHelper.process(
                    refSin,
                    resSin,
                    TOL_FAST_TRIG_NORM_ABS,
                    Double.NaN,
                    value);
            boolean logCos = cosHelper.process(
                    refCos,
                    resCos,
                    TOL_FAST_TRIG_NORM_ABS,
                    Double.NaN,
                    value);
            if (logSin || logCos) {
                System.out.println("refSin = "+refSin);
                System.out.println("resSin = "+resSin);
                System.out.println("refCos = "+refCos);
                System.out.println("resCos = "+resCos);
            }
            assertTrue(sinHelper.lastOK());
            assertTrue(cosHelper.lastOK());
            boolean rangeOK = (Double.isNaN(res) || NumbersUtils.isInRange(-Math.PI/2, Math.PI/2, res));
            // Either both diff or none diff.
            if (sinSignDiff ^ cosSignDiff) {
                // If only one sign changes,
                // we must be very close to +-PI/2 or 0 or PI.
                rangeOK &= (Math.abs(refSin) < TOL_FAST_TRIG_NORM_ABS) || (Math.abs(refCos) < TOL_FAST_TRIG_NORM_ABS);
            }
            if (!rangeOK) {
                System.out.println("value = "+value);
                System.out.println("res = "+res);
            }
            assertTrue(rangeOK);
        }
        sinHelper.finalLogIfNeeded();
        cosHelper.finalLogIfNeeded();
    }
    
    /*
     * floating points utils
     */

    public void test_isNaNOrInfinite_float() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float value = randomFloatWhatever();
            boolean ref = Float.isNaN(value) || Float.isInfinite(value);
            boolean res = StrictFastMath.isNaNOrInfinite(value);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_isNaNOrInfinite_double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double value = randomDoubleWhatever();
            boolean ref = Double.isNaN(value) || Double.isInfinite(value);
            boolean res = StrictFastMath.isNaNOrInfinite(value);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_getExponent_float() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float value = randomFloatWhatever();
            int ref = Math.getExponent(value);
            int res = StrictFastMath.getExponent(value);
            boolean ok = (ref == res);
            if (!ok) {
                printCallerName();
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
            }
            assertTrue(ok);
        }
    }

    public void test_getExponent_double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double value = randomDoubleWhatever();
            int ref = Math.getExponent(value);
            int res = StrictFastMath.getExponent(value);
            boolean ok = (ref == res);
            if (!ok) {
                printCallerName();
                System.out.println("value = "+value);
                System.out.println("ref = "+ref);
                System.out.println("res = "+res);
            }
            assertTrue(ok);
        }
    }

    public void test_signum_float() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float value = randomFloatWhatever();
            float ref = Math.signum(value);
            float res = StrictFastMath.signum(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_signum_double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.signum(value);
            double res = StrictFastMath.signum(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_signFromBit_float() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float value = randomFloatWhatever();
            int ref = (Float.floatToRawIntBits(value) < 0 ? -1 : 1);
            int res = StrictFastMath.signFromBit(value);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_signFromBit_double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double value = randomDoubleWhatever();
            long ref = (Double.doubleToRawLongBits(value) < 0 ? -1L : 1L);
            long res = StrictFastMath.signFromBit(value);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }
    
    public void test_copySign_2float() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float a = randomFloatWhatever();
            float b = randomFloatWhatever();
            float ref = StrictMath.copySign(a,b);
            float res = StrictFastMath.copySign(a,b);
            assertEquals(ref, res);
        }
    }

    public void test_copySign_2double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double a = randomDoubleWhatever();
            double b = randomDoubleWhatever();
            double ref = StrictMath.copySign(a,b);
            double res = StrictFastMath.copySign(a,b);
            assertEquals(ref, res);
        }
    }

    public void test_ulp_float() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float value = randomFloatWhatever();
            float ref = Math.ulp(value);
            float res = StrictFastMath.ulp(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_ulp_double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.ulp(value);
            double res = StrictFastMath.ulp(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_nextAfter_float_double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float a = randomFloatWhatever();
            double b = randomDoubleWhatever();
            float ref = Math.nextAfter(a,b);
            float res = StrictFastMath.nextAfter(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_nextAfter_2double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double a = randomDoubleWhatever();
            double b = randomDoubleWhatever();
            double ref = Math.nextAfter(a,b);
            double res = StrictFastMath.nextAfter(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_nextDown_float() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float value = randomFloatWhatever();
            float ref = Math.nextAfter(value,Double.NEGATIVE_INFINITY);
            float res = StrictFastMath.nextDown(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_nextDown_double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.nextAfter(value,Double.NEGATIVE_INFINITY);
            double res = StrictFastMath.nextDown(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_nextUp_float() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float value = randomFloatWhatever();
            float ref = Math.nextAfter(value,Double.POSITIVE_INFINITY);
            float res = StrictFastMath.nextUp(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_nextUp_double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.nextAfter(value,Double.POSITIVE_INFINITY);
            double res = StrictFastMath.nextUp(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_scalb_float_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float a = randomFloatWhatever();
            int b = randomIntWhatever();
            float ref = Math.scalb(a,b);
            float res = StrictFastMath.scalb(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_scalb_double_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double a = randomDoubleWhatever();
            int b = randomIntWhatever();
            double ref = Math.scalb(a,b);
            double res = StrictFastMath.scalb(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    /*
     * Non-redefined public values and treatments.
     */

    public void test_E() {
        assertEquals(Math.E, StrictFastMath.E);
    }

    public void test_PI() {
        assertEquals(Math.PI, StrictFastMath.PI);
    }

    public void test_abs_float() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float value = randomFloatWhatever();
            float ref = Math.abs(value);
            float res = StrictFastMath.abs(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_abs_double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double value = randomDoubleWhatever();
            double ref = Math.abs(value);
            double res = StrictFastMath.abs(value);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_min_2int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            int a = randomIntWhatever();
            int b = randomIntWhatever();
            int ref = Math.min(a,b);
            int res = StrictFastMath.min(a,b);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_min_2long() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            long a = randomLongWhatever();
            long b = randomLongWhatever();
            long ref = Math.min(a,b);
            long res = StrictFastMath.min(a,b);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_min_2float() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float a = randomFloatWhatever();
            float b = randomFloatWhatever();
            float ref = Math.min(a,b);
            float res = StrictFastMath.min(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_min_2double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double a = randomDoubleWhatever();
            double b = randomDoubleWhatever();
            double ref = Math.min(a,b);
            double res = StrictFastMath.min(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_max_2int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            int a = randomIntWhatever();
            int b = randomIntWhatever();
            int ref = Math.max(a,b);
            int res = StrictFastMath.max(a,b);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_max_2long() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            long a = randomLongWhatever();
            long b = randomLongWhatever();
            long ref = Math.max(a,b);
            long res = StrictFastMath.max(a,b);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_max_2float() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            float a = randomFloatWhatever();
            float b = randomFloatWhatever();
            float ref = Math.max(a,b);
            float res = StrictFastMath.max(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_max_2double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double a = randomDoubleWhatever();
            double b = randomDoubleWhatever();
            double ref = Math.max(a,b);
            double res = StrictFastMath.max(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_IEEEremainder_2double() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double a = randomDoubleWhatever();
            double b = randomDoubleWhatever();
            double ref = Math.IEEEremainder(a,b);
            double res = StrictFastMath.IEEEremainder(a,b);
            boolean ok = equivalent(ref,res);
            assertTrue(ok);
        }
    }

    public void test_random() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            double res = StrictFastMath.random();
            assertTrue((res >= 0.0) && (res < 1.0));
        }
    }
}
