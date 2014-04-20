package odk.lang;

/*
 * =============================================================================
 * Copyright (C) 2009 oma
 * 
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.Random;

import junit.framework.TestCase;

public strictfp class TestFastMathForJUnit extends TestCase {

    //--------------------------------------------------------------------------
    // MEMBERS
    //--------------------------------------------------------------------------

    private static final double DEFAULT_EPSILON = 1e-10;
    private static final double HUGE_EPSILON = 1e-7;

    private static final int NBR_OF_VALUES = 1000;
    private static final double A_LOT = 1e10;
    private final double[] anglesMinusPiPi;
    private final double[] anglesZeroTwoPi;
    private final double[] anglesMinusHalfPiHalfPi;
    private final double[] valuesDoubleMinusOneOne;
    private final double[] valuesDoubleMinusTenTen;
    private final double[] valuesDoubleMinusHundredHundred;
    private final double[] valuesDoubleMinusALotALot;
    private final double[] valuesDoubleAllMagnitudes;
    private final float[] valuesFloatAllMagnitudes;
    private final int[] valuesIntAllMagnitudes;
    private final long[] valuesLongAllMagnitudes;

    /**
     * For determinism.
     */
    private Random myRandom = new Random();
    
    //--------------------------------------------------------------------------
    // MAIN METHODS
    //--------------------------------------------------------------------------

    public TestFastMathForJUnit() {
        anglesMinusPiPi = new double[NBR_OF_VALUES];
        anglesZeroTwoPi = new double[NBR_OF_VALUES];
        anglesMinusHalfPiHalfPi = new double[NBR_OF_VALUES];
        valuesDoubleMinusOneOne = new double[NBR_OF_VALUES];
        valuesDoubleMinusTenTen = new double[NBR_OF_VALUES];
        valuesDoubleMinusHundredHundred = new double[NBR_OF_VALUES];
        valuesDoubleMinusALotALot = new double[NBR_OF_VALUES];
        valuesDoubleAllMagnitudes = new double[NBR_OF_VALUES];
        valuesFloatAllMagnitudes = new float[NBR_OF_VALUES];
        valuesIntAllMagnitudes = new int[NBR_OF_VALUES];
        valuesLongAllMagnitudes = new long[NBR_OF_VALUES];
        
        this.init();
    }
    
    private void init() {
        myRandom.setSeed(123456789);
        for (int i=0;i<NBR_OF_VALUES;i++) {
            anglesMinusPiPi[i] = (2.0*myRandom.nextDouble()-1.0) * Math.PI;
            anglesZeroTwoPi[i] = myRandom.nextDouble() * (2*Math.PI);
            anglesMinusHalfPiHalfPi[i] = randomMinusOneOne() * (Math.PI/2);
            valuesDoubleMinusOneOne[i] = randomMinusOneOne();
            valuesDoubleMinusTenTen[i] = randomMinusOneOne() * 10.0;
            valuesDoubleMinusHundredHundred[i] = randomMinusOneOne() * 100.0;
            valuesDoubleMinusALotALot[i] = randomMinusOneOne() * A_LOT;
            
            valuesDoubleAllMagnitudes[i] = Double.longBitsToDouble(randomUniform());
            if (Double.isNaN(valuesDoubleAllMagnitudes[i])) {
                // Replacing NaNs (we do not want them) with subnormals.
                valuesDoubleAllMagnitudes[i] = Double.MIN_NORMAL * myRandom.nextDouble();
            }
            // We want infinities.
            if (myRandom.nextDouble() < 0.1) {
                if (myRandom.nextDouble() < 0.5) {
                    valuesDoubleAllMagnitudes[i] = Double.NEGATIVE_INFINITY;
                } else {
                    valuesDoubleAllMagnitudes[i] = Double.POSITIVE_INFINITY;
                }
            }
            
            valuesFloatAllMagnitudes[i] = Float.intBitsToFloat((int)(myRandom.nextDouble() * 2.0 * Integer.MAX_VALUE));
            if (Float.isNaN(valuesFloatAllMagnitudes[i])) {
                // Replacing NaNs (we do not want them) with subnormals.
                valuesFloatAllMagnitudes[i] = Float.MIN_NORMAL * myRandom.nextFloat();
            }
            // We want infinities.
            if (myRandom.nextDouble() < 0.1) {
                if (myRandom.nextDouble() < 0.5) {
                    valuesFloatAllMagnitudes[i] = Float.NEGATIVE_INFINITY;
                } else {
                    valuesFloatAllMagnitudes[i] = Float.POSITIVE_INFINITY;
                }
            }
            
            valuesIntAllMagnitudes[i] = (int)(randomMinusOneOne() * Math.pow(2.0, randomMinusOneOne() * 32));
            valuesLongAllMagnitudes[i] = (long)(randomMinusOneOne() * Math.pow(2.0, randomMinusOneOne() * 64));
        }
    }

    //--------------------------------------------------------------------------
    // TEST METHODS
    //--------------------------------------------------------------------------

    public void test_cos_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            assertTrue(minDelta(Math.cos(value), FastMath.cos(value)) < DEFAULT_EPSILON);
        }
        assertEquals(Double.NaN, FastMath.cos(Double.NaN));
    }

    public void test_cosQuick_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleMinusHundredHundred[i];
            assertTrue(minDelta(Math.cos(value), FastMath.cosQuick(value)) < 2*1.5e-3);
        }
    }

    public void test_sin_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            assertTrue(minDelta(Math.sin(value), FastMath.sin(value)) < DEFAULT_EPSILON);
        }
        assertEquals(Double.NaN, FastMath.sin(Double.NaN));
    }

    public void test_sinQuick_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleMinusHundredHundred[i];
            assertTrue(minDelta(Math.sin(value), FastMath.sinQuick(value)) < 2*1.5e-3);
        }
    }

    public void test_sinAndCos_double_DoubleWrapper_DoubleWrapper() {
        DoubleWrapper tmpSin = new DoubleWrapper();
        DoubleWrapper tmpCos = new DoubleWrapper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            FastMath.sinAndCos(value, tmpSin, tmpCos);
            assertTrue(minDelta(Math.sin(value), tmpSin.value) < DEFAULT_EPSILON);
            assertTrue(minDelta(Math.cos(value), tmpCos.value) < DEFAULT_EPSILON);
        }
        FastMath.sinAndCos(Double.NaN, tmpSin, tmpCos);
        assertEquals(Double.NaN, tmpSin.value);
        assertEquals(Double.NaN, tmpCos.value);
    }

    public void test_tan_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            assertTrue(minDelta(Math.tan(value), FastMath.tan(value)) < DEFAULT_EPSILON);
        }
        assertEquals(Double.NaN, FastMath.tan(Double.NaN));
    }

    public void test_acos_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleMinusOneOne[i];
            assertTrue(minDelta(Math.acos(value), FastMath.acos(value)) < DEFAULT_EPSILON);
        }
        assertEquals(0.0,FastMath.acos(1.0));
        assertEquals(Math.PI,FastMath.acos(-1.0));
        assertEquals(Double.NaN,FastMath.acos(-1.1));
        assertEquals(Double.NaN,FastMath.acos(1.1));
        assertEquals(Double.NaN, FastMath.acos(Double.NaN));
    }

    public void test_acosInRange_double() {
        assertEquals(Math.PI,FastMath.acosInRange(-1.1));
        assertEquals(FastMath.acos(0.1),FastMath.acosInRange(0.1));
        assertEquals(0.0,FastMath.acosInRange(1.1));
        assertEquals(Double.NaN, FastMath.acosInRange(Double.NaN));
    }
    
    public void test_asin_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleMinusOneOne[i];
            assertTrue(minDelta(Math.asin(value), FastMath.asin(value)) < DEFAULT_EPSILON);
        }
        assertEquals(Math.PI/2,FastMath.asin(1.0));
        assertEquals(-Math.PI/2,FastMath.asin(-1.0));
        assertEquals(Double.NaN,FastMath.asin(-1.1));
        assertEquals(Double.NaN,FastMath.asin(1.1));
        assertEquals(Double.NaN, FastMath.asin(Double.NaN));
    }

    public void test_asinInRange_double() {
        assertEquals(-Math.PI/2,FastMath.asinInRange(-1.1));
        assertEquals(FastMath.asin(0.1),FastMath.asinInRange(0.1));
        assertEquals(Math.PI/2,FastMath.asinInRange(1.1));
        assertEquals(Double.NaN, FastMath.asinInRange(Double.NaN));
    }
    
    public void test_atan_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            assertTrue(minDelta(Math.atan(value), FastMath.atan(value)) < DEFAULT_EPSILON);
        }
        assertEquals(Math.PI/4, FastMath.atan(1.0));
        assertEquals(-Math.PI/4, FastMath.atan(-1.0));
        assertEquals(Double.NaN,FastMath.atan(Double.NaN));
    }

    public void test_atan2_double_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double y = valuesDoubleAllMagnitudes[i];
            double x = valuesDoubleAllMagnitudes[NBR_OF_VALUES-1-i];
            assertTrue(minDelta(Math.atan2(y,x), FastMath.atan2(y,x)) < DEFAULT_EPSILON);
        }
        double[] specialValuesTab = new double[] {
                Double.NEGATIVE_INFINITY,
                -2.0,
                -1.0,
                -0.0,
                0.0,
                1.0,
                2.0,
                Double.POSITIVE_INFINITY,
                Double.NaN
        };
        for (int i=0;i<specialValuesTab.length;i++) {
            double y = specialValuesTab[i];
            for (int j=0;j<specialValuesTab.length;j++) {
                double x = specialValuesTab[j];
                assertEquals(Math.atan2(y,x),FastMath.atan2(y,x));
            }
        }
    }

    public void test_cosh_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            assertTrue(minDelta(Math.cosh(value), FastMath.cosh(value)) < DEFAULT_EPSILON);
        }
        assertEquals(Double.NaN,FastMath.cosh(Double.NaN));
    }

    public void test_sinh_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            assertTrue(minDelta(Math.sinh(value), FastMath.sinh(value)) < DEFAULT_EPSILON);
        }
        assertEquals(Double.NaN,FastMath.sinh(Double.NaN));
    }

    public void test_sinhAndCosh_double_DoubleWrapper_DoubleWrapper() {
        DoubleWrapper tmpSinh = new DoubleWrapper();
        DoubleWrapper tmpCosh = new DoubleWrapper();
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            FastMath.sinhAndCosh(value, tmpSinh, tmpCosh);
            assertTrue(minDelta(Math.sinh(value), tmpSinh.value) < DEFAULT_EPSILON);
            assertTrue(minDelta(Math.cosh(value), tmpCosh.value) < DEFAULT_EPSILON);
        }
        FastMath.sinhAndCosh(Double.NaN, tmpSinh, tmpCosh);
        assertEquals(Double.NaN, tmpSinh.value);
        assertEquals(Double.NaN, tmpCosh.value);
    }

    public void test_tanh_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            assertTrue(minDelta(Math.tanh(value), FastMath.tanh(value)) < DEFAULT_EPSILON);
        }
        assertEquals(Double.NaN,FastMath.tanh(Double.NaN));
    }

    public void test_pow_double_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = valuesDoubleAllMagnitudes[i];
            double b = valuesDoubleAllMagnitudes[NBR_OF_VALUES-1-i];
            assertTrue(minDelta(Math.pow(a,b), FastMath.pow(a,b)) < DEFAULT_EPSILON);
        }
        // with mathematical integers as value
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = (double)(int)valuesFloatAllMagnitudes[i];
            double b = valuesDoubleAllMagnitudes[NBR_OF_VALUES-1-i];
            assertTrue(minDelta(Math.pow(a,b), FastMath.pow(a,b)) < DEFAULT_EPSILON);
        }
        // with mathematical integers as power
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = valuesDoubleAllMagnitudes[i];
            double b = (double)(int)valuesFloatAllMagnitudes[NBR_OF_VALUES-1-i];
            assertTrue(minDelta(Math.pow(a,b), FastMath.pow(a,b)) < DEFAULT_EPSILON);
        }
        assertEquals(1.0, FastMath.pow(0.0,0.0));
        assertEquals(0.0, FastMath.pow(0.0,2.0));
        assertEquals(0.0, FastMath.pow(-0.0,2.0));
        assertEquals(Double.POSITIVE_INFINITY, FastMath.pow(0.0,-2.0));
        assertEquals(0.0, FastMath.pow(0.0,3.0));
        assertEquals(-0.0, FastMath.pow(-0.0,3.0));
        assertEquals(Double.POSITIVE_INFINITY, FastMath.pow(0.0,-3.0));
        assertEquals(4.0, FastMath.pow(2.0,2.0), DEFAULT_EPSILON);
        assertEquals(8.0, FastMath.pow(2.0,3.0), DEFAULT_EPSILON);
        assertEquals(1.0/4.0, FastMath.pow(2.0,-2.0), DEFAULT_EPSILON);
        assertEquals(1.0/8.0, FastMath.pow(2.0,-3.0), DEFAULT_EPSILON);
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
    }

    public void test_powFast_double_int() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = valuesDoubleMinusTenTen[i];
            int b = (int)Math.round(valuesDoubleMinusTenTen[NBR_OF_VALUES-1-i]);
            assertTrue(minDelta(Math.pow(a,b), FastMath.powFast(a,b)) < DEFAULT_EPSILON);
        }
        assertEquals(1.0, FastMath.powFast(1.0,Integer.MIN_VALUE));
        assertEquals(Double.POSITIVE_INFINITY, FastMath.powFast(Double.MIN_VALUE,Integer.MIN_VALUE));
        assertEquals(Double.NaN, FastMath.powFast(Double.NaN,1));
    }

    public void test_twoPow_int() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            int value = (int)valuesFloatAllMagnitudes[i];
            assertTrue(minDelta(Math.pow(2,value), FastMath.twoPow(value)) < DEFAULT_EPSILON);
        }
    }

    public void test_pow2_int() {
        assertEquals(2*2, FastMath.pow2(2));
    }

    public void test_pow2_long() {
        assertEquals(2L*2L, FastMath.pow2(2L));
    }

    public void test_pow2_float() {
        assertEquals(2.1f*2.1f, FastMath.pow2(2.1f));
    }

    public void test_pow2_double() {
        assertEquals(2.1*2.1, FastMath.pow2(2.1));
    }

    public void test_pow3_int() {
        assertEquals(2*2*2, FastMath.pow3(2));
    }

    public void test_pow3_long() {
        assertEquals(2L*2L*2L, FastMath.pow3(2L));
    }

    public void test_pow3_float() {
        assertEquals(2.1f*2.1f*2.1f, FastMath.pow3(2.1f));
    }

    public void test_pow3_double() {
        assertEquals(2.1*2.1*2.1, FastMath.pow3(2.1));
    }

    public void test_exp_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            assertTrue(minDelta(Math.exp(value), FastMath.exp(value)) < DEFAULT_EPSILON);
        }
        assertEquals(Double.NaN, FastMath.exp(Double.NaN));
    }

    public void test_expm1_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            assertTrue(minDelta(Math.expm1(value), FastMath.expm1(value)) < DEFAULT_EPSILON);
        }
        assertEquals(Double.NaN, FastMath.expm1(Double.NaN));
    }

    public void test_log_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = Math.abs(valuesDoubleAllMagnitudes[i]);
            assertTrue(minDelta(Math.log(value), FastMath.log(value)) < DEFAULT_EPSILON);
        }
        assertEquals(Double.NaN, FastMath.log(Double.NaN));
    }

    public void test_log1p_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = Math.abs(valuesDoubleAllMagnitudes[i])-1;
            assertTrue(minDelta(Math.log1p(value), FastMath.log1p(value)) < DEFAULT_EPSILON);
        }
        assertEquals(Double.NaN, FastMath.log1p(Double.NaN));
    }

    public void test_sqrt_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = Math.abs(valuesDoubleAllMagnitudes[i]);
            assertTrue(minDelta(Math.sqrt(value), FastMath.sqrt(value)) < DEFAULT_EPSILON);
        }
        assertEquals(-0.0, FastMath.sqrt(-0.0));
        assertEquals(0.0, FastMath.sqrt(0.0));
        assertEquals(Double.NaN, FastMath.sqrt(Double.NaN));
    }

    public void test_cbrt_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            assertTrue(minDelta(Math.cbrt(value), FastMath.cbrt(value)) < DEFAULT_EPSILON);
        }
        assertEquals(-0.0, FastMath.cbrt(-0.0));
        assertEquals(0.0, FastMath.cbrt(0.0));
        assertEquals(Double.NaN, FastMath.cbrt(Double.NaN));
    }
    
    public void test_remainder_double_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double a = valuesDoubleAllMagnitudes[i];
            double b = valuesDoubleAllMagnitudes[NBR_OF_VALUES-1-i];
            assertTrue(minDelta(Math.IEEEremainder(a,b), FastMath.remainder(a,b)) < DEFAULT_EPSILON);
        }
        assertEquals(Double.NaN, FastMath.remainder(Double.NaN,1.0));
        assertEquals(Double.NaN, FastMath.remainder(1.0,Double.NaN));
    }

    public void test_normalizeMinusPiPi() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            double refNorm = Math.atan2(Math.sin(value),Math.cos(value));
            double fastNorm = FastMath.normalizeMinusPiPi(value);
            assertTrue(minDelta(refNorm,fastNorm) < DEFAULT_EPSILON);
        }
        assertEquals(Double.NaN, FastMath.normalizeMinusPiPi(Double.NaN));
    }

    public void test_normalizeMinusPiPiFast() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            if (Math.abs(value) > A_LOT) {
                value = valuesDoubleMinusALotALot[i];
            }
            double refNorm = Math.atan2(Math.sin(value),Math.cos(value));
            double fastNorm = FastMath.normalizeMinusPiPiFast(value);
            assertTrue(minDelta(refNorm,fastNorm) < HUGE_EPSILON);
        }
        assertEquals(Double.NaN, FastMath.normalizeMinusPiPiFast(Double.NaN));
    }

    public void test_normalizeZeroTwoPi() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            double refNorm = Math.atan2(Math.sin(value),Math.cos(value));
            if (refNorm < 0.0) {
                refNorm += 2*Math.PI;
            }
            double fastNorm = FastMath.normalizeZeroTwoPi(value);
            assertTrue(minDelta(refNorm,fastNorm) < DEFAULT_EPSILON);
        }
        assertEquals(Double.NaN, FastMath.normalizeZeroTwoPi(Double.NaN));
    }

    public void test_normalizeZeroTwoPiFast() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            if (Math.abs(value) > A_LOT) {
                value = valuesDoubleMinusALotALot[i];
            }
            double refNorm = Math.atan2(Math.sin(value),Math.cos(value));
            if (refNorm < 0.0) {
                refNorm += 2*Math.PI;
            }
            double fastNorm = FastMath.normalizeZeroTwoPiFast(value);
            assertTrue(minDelta(refNorm,fastNorm) < HUGE_EPSILON);
        }
        assertEquals(Double.NaN, FastMath.normalizeZeroTwoPiFast(Double.NaN));
    }

    public void test_normalizeMinusHalfPiHalfPi() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            assertTrue(minDelta(Math.atan(Math.tan(value)), FastMath.normalizeMinusHalfPiHalfPi(value)) < DEFAULT_EPSILON);
        }
        assertEquals(Double.NaN, FastMath.normalizeMinusHalfPiHalfPi(Double.NaN));
    }

    public void test_normalizeMinusHalfPiHalfPiFast() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            if (Math.abs(value) > A_LOT) {
                value = valuesDoubleMinusALotALot[i];
            }
            assertTrue(minDelta(Math.atan(Math.tan(value)), FastMath.normalizeMinusHalfPiHalfPiFast(value)) < HUGE_EPSILON);
        }
        assertEquals(Double.NaN, FastMath.normalizeMinusHalfPiHalfPiFast(Double.NaN));
    }

    public void test_hypot_double_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double x = valuesDoubleAllMagnitudes[i];
            double y = valuesDoubleAllMagnitudes[NBR_OF_VALUES-1-i];
            assertTrue(minDelta(Math.hypot(x,y), FastMath.hypot(x,y)) < DEFAULT_EPSILON);
        }
        assertEquals(Double.POSITIVE_INFINITY, FastMath.hypot(Double.POSITIVE_INFINITY,Double.NaN));
        assertEquals(Double.POSITIVE_INFINITY, FastMath.hypot(Double.NaN,Double.POSITIVE_INFINITY));
        assertEquals(Double.NaN, FastMath.hypot(Double.NaN,1.0));
        assertEquals(Double.NaN, FastMath.hypot(1.0,Double.NaN));
    }

    public void test_ceil_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            assertTrue(Math.ceil(value) == FastMath.ceil(value));
        }
        assertEquals(-0.0, FastMath.ceil(-0.0));
        assertEquals(0.0, FastMath.ceil(0.0));
        assertEquals(-1.0, FastMath.ceil(-1.0));
        assertEquals(1.0, FastMath.ceil(1.0));
        assertEquals(-5.0, FastMath.ceil(-5.0));
        assertEquals(5.0, FastMath.ceil(5.0));
        assertEquals(Double.NaN, FastMath.ceil(Double.NaN));
    }

    public void test_ceil_float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = valuesFloatAllMagnitudes[i];
            assertTrue(Math.ceil(value) == FastMath.ceil(value));
        }
        assertEquals(-0.0f, FastMath.ceil(-0.0f));
        assertEquals(0.0f, FastMath.ceil(0.0f));
        assertEquals(-1.0f, FastMath.ceil(-1.0f));
        assertEquals(1.0f, FastMath.ceil(1.0f));
        assertEquals(-5.0f, FastMath.ceil(-5.0f));
        assertEquals(5.0f, FastMath.ceil(5.0f));
        assertEquals(Float.NaN, FastMath.ceil(Float.NaN));
    }

    public void test_floor_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            assertTrue(Math.floor(value) == FastMath.floor(value));
        }
        assertEquals(-0.0, FastMath.floor(-0.0));
        assertEquals(0.0, FastMath.floor(0.0));
        assertEquals(-1.0, FastMath.floor(-1.0));
        assertEquals(1.0, FastMath.floor(1.0));
        assertEquals(-5.0, FastMath.floor(-5.0));
        assertEquals(5.0, FastMath.floor(5.0));
        assertEquals(Double.NaN, FastMath.floor(Double.NaN));
    }

    public void test_floor_float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = valuesFloatAllMagnitudes[i];
            assertTrue(Math.floor(value) == FastMath.floor(value));
        }
        assertEquals(-0.0f, FastMath.floor(-0.0f));
        assertEquals(0.0f, FastMath.floor(0.0f));
        assertEquals(-1.0f, FastMath.floor(-1.0f));
        assertEquals(1.0f, FastMath.floor(1.0f));
        assertEquals(-5.0f, FastMath.floor(-5.0f));
        assertEquals(5.0f, FastMath.floor(5.0f));
        assertEquals(Float.NaN, FastMath.floor(Float.NaN));
    }

    public void test_round_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            assertTrue(Math.round(value) == FastMath.round(value));
        }
        assertEquals(0L, FastMath.round(Double.NaN));
    }

    public void test_round_float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = valuesFloatAllMagnitudes[i];
            assertTrue(Math.round(value) == FastMath.round(value));
        }
        assertEquals(0, FastMath.round(Float.NaN));
    }

    public void test_getExponent_double() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            double value = valuesDoubleAllMagnitudes[i];
            assertEquals(Math.getExponent(value), FastMath.getExponent(value));
        }
    }

    public void test_getExponent_float() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            float value = valuesFloatAllMagnitudes[i];
            assertEquals(Math.getExponent(value), FastMath.getExponent(value));
        }
    }

    public void test_toDegrees_double() {
        assertEquals(Math.toDegrees(1.1), FastMath.toDegrees(1.1), DEFAULT_EPSILON);
    }

    public void test_toRadians_double() {
        assertEquals(Math.toRadians(1.1), FastMath.toRadians(1.1), DEFAULT_EPSILON);
    }

    public void test_toRadians_boolean_int_int_double() {
        assertEquals(Math.toRadians(1+2*(1.0/60)+3*(1.0/3600)), FastMath.toRadians(true,1,2,3), DEFAULT_EPSILON);
        assertEquals(-Math.toRadians(1+2*(1.0/60)+3*(1.0/3600)), FastMath.toRadians(false,1,2,3), DEFAULT_EPSILON);
    }

    public void test_toDegrees_boolean_int_int_double() {
        assertEquals((1+2*(1.0/60)+3*(1.0/3600)), FastMath.toDegrees(true,1,2,3), DEFAULT_EPSILON);
        assertEquals(-(1+2*(1.0/60)+3*(1.0/3600)), FastMath.toDegrees(false,1,2,3), DEFAULT_EPSILON);
    }

    public void test_toDMS_double_IntWrapper_IntWrapper_DoubleWrapper() {
        boolean sign;
        IntWrapper degrees = new IntWrapper();
        IntWrapper minutes = new IntWrapper();
        DoubleWrapper seconds = new DoubleWrapper();
        
        sign = FastMath.toDMS(Math.toRadians(1+2*(1.0/60)+3.1*(1.0/3600)), degrees, minutes, seconds);
        assertEquals(true, sign);
        assertEquals(1, degrees.value);
        assertEquals(2, minutes.value);
        assertEquals(3.1, seconds.value, DEFAULT_EPSILON);

        sign = FastMath.toDMS(Math.toRadians(-(1+2*(1.0/60)+3.1*(1.0/3600))), degrees, minutes, seconds);
        assertEquals(false, sign);
        assertEquals(1, degrees.value);
        assertEquals(2, minutes.value);
        assertEquals(3.1, seconds.value, DEFAULT_EPSILON);

        sign = FastMath.toDMS(-Math.PI, degrees, minutes, seconds);
        assertEquals(false, sign);
        assertEquals(180, degrees.value);
        assertEquals(0, minutes.value);
        assertEquals(0.0, seconds.value);

        sign = FastMath.toDMS(-Math.PI/2, degrees, minutes, seconds);
        assertEquals(false, sign);
        assertEquals(90, degrees.value);
        assertEquals(0, minutes.value);
        assertEquals(0.0, seconds.value);

        sign = FastMath.toDMS(0.0, degrees, minutes, seconds);
        assertEquals(0, degrees.value);
        assertEquals(0, minutes.value);
        assertEquals(0.0, seconds.value);

        sign = FastMath.toDMS(Math.PI/2, degrees, minutes, seconds);
        assertEquals(true, sign);
        assertEquals(90, degrees.value);
        assertEquals(0, minutes.value);
        assertEquals(0.0, seconds.value);

        sign = FastMath.toDMS(Math.PI, degrees, minutes, seconds);
        assertEquals(true, sign);
        assertEquals(180, degrees.value);
        assertEquals(0, minutes.value);
        assertEquals(0.0, seconds.value);
    }
    
    public void test_abs_int() {
        for (int i=0;i<NBR_OF_VALUES;i++) {
            int value = (int)valuesDoubleMinusHundredHundred[i];
            assertTrue(Math.abs(value) == FastMath.abs(value));
        }
    }
    
    public void test_toInt_long() {
        assertEquals(3, FastMath.toInt(3L));
        assertEquals(Integer.MAX_VALUE, FastMath.toInt((long)Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, FastMath.toInt((long)Integer.MIN_VALUE));
        assertEquals(Integer.MAX_VALUE, FastMath.toInt(Long.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, FastMath.toInt(Long.MIN_VALUE));
    }
    
    public void test_toIntSafe_long() {
        boolean exceptionOccured;
        
        assertEquals(3, FastMath.toIntSafe(3L));
        assertEquals(Integer.MAX_VALUE, FastMath.toIntSafe((long)Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, FastMath.toIntSafe((long)Integer.MIN_VALUE));

        try {
            FastMath.toIntSafe(((long)Integer.MAX_VALUE)+1L);
            exceptionOccured = false;
        } catch (ArithmeticException e) {
            exceptionOccured = true;
        }
        assertTrue(exceptionOccured);

        try {
            FastMath.toIntSafe(((long)Integer.MIN_VALUE)-1L);
            exceptionOccured = false;
        } catch (ArithmeticException e) {
            exceptionOccured = true;
        }
        assertTrue(exceptionOccured);
    }

    public void test_plusNoModulo_int_int() {
        assertEquals(3, FastMath.plusNoModulo(1, 2));
        assertEquals(-1, FastMath.plusNoModulo(1, -2));
        
        assertEquals(Integer.MAX_VALUE, FastMath.plusNoModulo(Integer.MAX_VALUE, 1));
        assertEquals(Integer.MIN_VALUE, FastMath.plusNoModulo(Integer.MIN_VALUE, -1));
    }

    public void test_plusNoModuloSafe_int_int() {
        boolean exceptionOccured;
        
        assertEquals(3, FastMath.plusNoModuloSafe(1, 2));
        assertEquals(-1, FastMath.plusNoModuloSafe(1, -2));
        
        try {
            FastMath.plusNoModuloSafe(Integer.MAX_VALUE, 1);
            exceptionOccured = false;
        } catch (ArithmeticException e) {
            exceptionOccured = true;
        }
        assertTrue(exceptionOccured);
        
        try {
            FastMath.plusNoModuloSafe(Integer.MIN_VALUE, -1);
            exceptionOccured = false;
        } catch (ArithmeticException e) {
            exceptionOccured = true;
        }
        assertTrue(exceptionOccured);
    }

    public void test_plusNoModulo_long_long() {
        assertEquals(3L, FastMath.plusNoModulo(1L, 2L));
        assertEquals(-1L, FastMath.plusNoModulo(1L, -2L));
        
        assertEquals(Long.MAX_VALUE, FastMath.plusNoModulo(Long.MAX_VALUE, 1L));
        assertEquals(Long.MIN_VALUE, FastMath.plusNoModulo(Long.MIN_VALUE, -1L));
    }

    public void test_plusNoModuloSafe_long_long() {
        boolean exceptionOccured;
        
        assertEquals(3L, FastMath.plusNoModuloSafe(1L, 2L));
        assertEquals(-1L, FastMath.plusNoModuloSafe(1L, -2L));
        
        try {
            FastMath.plusNoModuloSafe(Long.MAX_VALUE, 1L);
            exceptionOccured = false;
        } catch (ArithmeticException e) {
            exceptionOccured = true;
        }
        assertTrue(exceptionOccured);
        
        try {
            FastMath.plusNoModuloSafe(Long.MIN_VALUE, -1L);
            exceptionOccured = false;
        } catch (ArithmeticException e) {
            exceptionOccured = true;
        }
        assertTrue(exceptionOccured);
    }

    public void test_minusNoModulo_int_int() {
        assertEquals(-1, FastMath.minusNoModulo(1, 2));
        assertEquals(3, FastMath.minusNoModulo(1, -2));
        
        assertEquals(Integer.MAX_VALUE, FastMath.minusNoModulo(Integer.MAX_VALUE, -1));
        assertEquals(Integer.MIN_VALUE, FastMath.minusNoModulo(Integer.MIN_VALUE, 1));
    }

    public void test_minusNoModuloSafe_int_int() {
        boolean exceptionOccured;
        
        assertEquals(-1, FastMath.minusNoModuloSafe(1, 2));
        assertEquals(3, FastMath.minusNoModuloSafe(1, -2));
        
        try {
            FastMath.minusNoModuloSafe(Integer.MAX_VALUE, -1);
            exceptionOccured = false;
        } catch (ArithmeticException e) {
            exceptionOccured = true;
        }
        assertTrue(exceptionOccured);
        
        try {
            FastMath.minusNoModuloSafe(Integer.MIN_VALUE, 1);
            exceptionOccured = false;
        } catch (ArithmeticException e) {
            exceptionOccured = true;
        }
        assertTrue(exceptionOccured);
    }

    public void test_minusNoModulo_long_long() {
        assertEquals(-1L, FastMath.minusNoModulo(1L, 2L));
        assertEquals(3L, FastMath.minusNoModulo(1L, -2L));
        
        assertEquals(Long.MAX_VALUE, FastMath.minusNoModulo(Long.MAX_VALUE, -1L));
        assertEquals(Long.MIN_VALUE, FastMath.minusNoModulo(Long.MIN_VALUE, 1L));
    }

    public void test_minusNoModuloSafe_long_long() {
        boolean exceptionOccured;
        
        assertEquals(-1L, FastMath.minusNoModuloSafe(1L, 2L));
        assertEquals(3L, FastMath.minusNoModuloSafe(1L, -2L));
        
        try {
            FastMath.minusNoModuloSafe(Long.MAX_VALUE, -1L);
            exceptionOccured = false;
        } catch (ArithmeticException e) {
            exceptionOccured = true;
        }
        assertTrue(exceptionOccured);
        
        try {
            FastMath.minusNoModuloSafe(Long.MIN_VALUE, 1L);
            exceptionOccured = false;
        } catch (ArithmeticException e) {
            exceptionOccured = true;
        }
        assertTrue(exceptionOccured);
    }

    public void test_timesNoModulo_int_int() {
        assertEquals(6, FastMath.timesNoModulo(2, 3));
        assertEquals(-6, FastMath.timesNoModulo(-2, 3));
        
        assertEquals(0, FastMath.timesNoModulo(1, 0));
        
        assertEquals(Integer.MAX_VALUE, FastMath.timesNoModulo(Integer.MIN_VALUE, -1));
        
        assertEquals(Integer.MAX_VALUE, FastMath.timesNoModulo(Integer.MAX_VALUE, 2));
        assertEquals(Integer.MIN_VALUE, FastMath.timesNoModulo(Integer.MIN_VALUE, 2));
    }

    public void test_timesNoModuloSafe_int_int() {
        boolean exceptionOccured;
        
        assertEquals(6, FastMath.timesNoModuloSafe(2, 3));
        assertEquals(-6, FastMath.timesNoModuloSafe(-2, 3));

        assertEquals(0, FastMath.timesNoModuloSafe(1, 0));
        
        try {
            FastMath.timesNoModuloSafe(Integer.MIN_VALUE, -1);
            exceptionOccured = false;
        } catch (ArithmeticException e) {
            exceptionOccured = true;
        }
        assertTrue(exceptionOccured);

        try {
            FastMath.timesNoModuloSafe(Integer.MAX_VALUE, 2);
            exceptionOccured = false;
        } catch (ArithmeticException e) {
            exceptionOccured = true;
        }
        assertTrue(exceptionOccured);
        
        try {
            FastMath.timesNoModuloSafe(Integer.MIN_VALUE, 2);
            exceptionOccured = false;
        } catch (ArithmeticException e) {
            exceptionOccured = true;
        }
        assertTrue(exceptionOccured);
    }

    public void test_timesNoModulo_long_long() {
        assertEquals(6L, FastMath.timesNoModulo(2L, 3L));
        assertEquals(-6L, FastMath.timesNoModulo(-2L, 3L));
        
        assertEquals(0L, FastMath.timesNoModulo(1L, 0L));
        
        assertEquals(Long.MAX_VALUE, FastMath.timesNoModulo(Long.MIN_VALUE, -1L));
        
        assertEquals(Long.MAX_VALUE, FastMath.timesNoModulo(Long.MAX_VALUE, 2L));
        assertEquals(Long.MIN_VALUE, FastMath.timesNoModulo(Long.MIN_VALUE, 2L));
    }

    public void test_timesNoModuloSafe_long_long() {
        boolean exceptionOccured;
        
        assertEquals(6L, FastMath.timesNoModuloSafe(2L, 3L));
        assertEquals(-6L, FastMath.timesNoModuloSafe(-2L, 3L));

        assertEquals(0L, FastMath.timesNoModuloSafe(1L, 0L));

        try {
            FastMath.timesNoModuloSafe(Long.MIN_VALUE, -1L);
            exceptionOccured = false;
        } catch (ArithmeticException e) {
            exceptionOccured = true;
        }
        assertTrue(exceptionOccured);

        try {
            FastMath.timesNoModuloSafe(Long.MAX_VALUE, 2L);
            exceptionOccured = false;
        } catch (ArithmeticException e) {
            exceptionOccured = true;
        }
        assertTrue(exceptionOccured);
        
        try {
            FastMath.timesNoModuloSafe(Long.MIN_VALUE, 2L);
            exceptionOccured = false;
        } catch (ArithmeticException e) {
            exceptionOccured = true;
        }
        assertTrue(exceptionOccured);
    }
    
    public void test_inRange_int_int_int() {
        assertEquals(0, FastMath.inRange(0, 2, -1));
        assertEquals(0, FastMath.inRange(0, 2, 0));
        assertEquals(1, FastMath.inRange(0, 2, 1));
        assertEquals(2, FastMath.inRange(0, 2, 2));
        assertEquals(2, FastMath.inRange(0, 2, 3));
    }

    public void test_inRange_long_long_long() {
        assertEquals(0L, FastMath.inRange(0L, 2L, -1L));
        assertEquals(0L, FastMath.inRange(0L, 2L, 0L));
        assertEquals(1L, FastMath.inRange(0L, 2L, 1L));
        assertEquals(2L, FastMath.inRange(0L, 2L, 2L));
        assertEquals(2L, FastMath.inRange(0L, 2L, 3L));
    }

    public void test_inRange_float_float_float() {
        assertEquals(0.0f, FastMath.inRange(0.0f, 2.0f, -1.0f));
        assertEquals(0.0f, FastMath.inRange(0.0f, 2.0f, 0.0f));
        assertEquals(1.0f, FastMath.inRange(0.0f, 2.0f, 1.0f));
        assertEquals(2.0f, FastMath.inRange(0.0f, 2.0f, 2.0f));
        assertEquals(2.0f, FastMath.inRange(0.0f, 2.0f, 3.0f));
    }

    public void test_inRange_double_double_double_double() {
        assertEquals(0.0, FastMath.inRange(0.0, 2.0, -1.0));
        assertEquals(0.0, FastMath.inRange(0.0, 2.0, 0.0));
        assertEquals(1.0, FastMath.inRange(0.0, 2.0, 1.0));
        assertEquals(2.0, FastMath.inRange(0.0, 2.0, 2.0));
        assertEquals(2.0, FastMath.inRange(0.0, 2.0, 3.0));
    }

    public void test_isInClockwiseDomain_double_double_double() {
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
        assertTrue(FastMath.isInClockwiseDomain(1e-10, 2*Math.PI, -1e-20));
        assertFalse(FastMath.isInClockwiseDomain(1e-20, 2*Math.PI, -1e-20));
        
        // NaN
        assertFalse(FastMath.isInClockwiseDomain(Double.NaN, Math.PI, Math.PI/2));
        assertFalse(FastMath.isInClockwiseDomain(Double.NaN, 3*Math.PI, Math.PI/2));
        assertFalse(FastMath.isInClockwiseDomain(0.0, Math.PI, Double.NaN));
        assertFalse(FastMath.isInClockwiseDomain(0.0, 3*Math.PI, Double.NaN));
        assertFalse(FastMath.isInClockwiseDomain(0.0, Double.NaN, Math.PI/2));
    }

    /*
     * Not-redefined java.lang.Math public values and treatments.
     * ===> fast "tests" just for coverage.
     */
    
    public void test_E() {
        assertEquals(Math.E, FastMath.E);
    }

    public void test_PI() {
        assertEquals(Math.PI, FastMath.PI);
    }

    public void test_abs_double() {
        assertEquals(Math.abs(1.0), FastMath.abs(1.0));
    }

    public void test_abs_float() {
        assertEquals(Math.abs(1.0f), FastMath.abs(1.0f));
    }

    public void test_abs_long() {
        assertEquals(Math.abs(1L), FastMath.abs(1L));
    }

    public void test_copySign_double_double() {
        assertEquals(Math.copySign(1.0,1.0), FastMath.copySign(1.0,1.0));
    }

    public void test_copySign_float_float() {
        assertEquals(Math.copySign(1.0f,1.0f), FastMath.copySign(1.0f,1.0f));
    }

    public void test_IEEEremainder_double_double() {
        assertEquals(Math.IEEEremainder(1.0,1.0), FastMath.IEEEremainder(1.0,1.0));
    }

    public void test_log10_double() {
        assertEquals(Math.log10(1.0), FastMath.log10(1.0));
    }

    public void test_max_double_double() {
        assertEquals(Math.max(1.0,1.0), FastMath.max(1.0,1.0));
    }

    public void test_max_float_float() {
        assertEquals(Math.max(1.0f,1.0f), FastMath.max(1.0f,1.0f));
    }

    public void test_max_int_int() {
        assertEquals(Math.max(1,1), FastMath.max(1,1));
    }

    public void test_max_long_long() {
        assertEquals(Math.max(1L,1L), FastMath.max(1L,1L));
    }

    public void test_min_double_double() {
        assertEquals(Math.min(1.0,1.0), FastMath.min(1.0,1.0));
    }

    public void test_min_float_float() {
        assertEquals(Math.min(1.0f,1.0f), FastMath.min(1.0f,1.0f));
    }

    public void test_min_int_int() {
        assertEquals(Math.min(1,1), FastMath.min(1,1));
    }

    public void test_min_long_long() {
        assertEquals(Math.min(1L,1L), FastMath.min(1L,1L));
    }

    public void test_nextAfter_double_double() {
        assertEquals(Math.nextAfter(1.0,1.0), FastMath.nextAfter(1.0,1.0));
    }

    public void test_nextAfter_float_float() {
        assertEquals(Math.nextAfter(1.0f,1.0f), FastMath.nextAfter(1.0f,1.0f));
    }

    public void test_nextUp_double() {
        assertEquals(Math.nextUp(1.0), FastMath.nextUp(1.0));
    }

    public void test_nextUp_float() {
        assertEquals(Math.nextUp(1.0f), FastMath.nextUp(1.0f));
    }

    public void test_random() {
        assertTrue((FastMath.random() >= 0.0) && (FastMath.random() <= 1.0));
    }

    public void test_rint_double() {
        assertEquals(Math.rint(1.0), FastMath.rint(1.0));
    }

    public void test_scalb_double_int() {
        assertEquals(Math.scalb(1.0,1), FastMath.scalb(1.0,1));
    }

    public void test_scalb_float_int() {
        assertEquals(Math.scalb(1.0f,1), FastMath.scalb(1.0f,1));
    }

    public void test_signum_double() {
        assertEquals(Math.signum(1.0), FastMath.signum(1.0));
    }

    public void test_signum_float() {
        assertEquals(Math.signum(1.0f), FastMath.signum(1.0f));
    }

    public void test_ulp_double() {
        assertEquals(Math.ulp(1.0), FastMath.ulp(1.0));
    }

    public void test_ulp_float() {
        assertEquals(Math.ulp(1.0f), FastMath.ulp(1.0f));
    }

    //--------------------------------------------------------------------------
    // MISCELLANEOUS METHODS
    //--------------------------------------------------------------------------

    private double randomMinusOneOne() {
        return (myRandom.nextDouble() - 0.5) * 2.0;
    }

    private long randomUniform() {
        long tmp = (long)(myRandom.nextDouble() * 2.0 * Integer.MAX_VALUE);
        return tmp | (((long)(myRandom.nextDouble() * 2.0 * Integer.MAX_VALUE))<<32);
    }

    private static double absDelta(double a, double b) {
        if (a == b) {
            return 0.0;
        }
        if (Double.isNaN(a)) {
            return (Double.isNaN(b)) ? 0.0 : Double.POSITIVE_INFINITY;
        } else if (Double.isNaN(b)) {
            return Double.POSITIVE_INFINITY;
        }
        return Math.abs(a-b);
    }

    private static double relDelta(double a, double b) {
        if (a == b) {
            return 0.0;
        }
        if (signsAreDifferent(a,b)) {
            return Double.POSITIVE_INFINITY;
        }
        if (Double.isNaN(a)) {
            return (Double.isNaN(b)) ? 0.0 : Double.POSITIVE_INFINITY;
        } else if (Double.isNaN(b)) {
            return Double.POSITIVE_INFINITY;
        }
        if (Math.abs(a) < 1.0) {
            // Division seems to behave weird for very low values.
            a = Math.scalb(a,100);
            b = Math.scalb(b,100);
        }
        return Math.abs(a-b) / Math.max(Math.abs(a), Math.abs(b));
    }

    private static double minDelta(double a, double b) {
        return Math.min(absDelta(a,b), relDelta(a,b));
    }

    /**
     * @return True if value signs are different. Returns false if either
     *         value is NaN.
     */
    private static boolean signsAreDifferent(double a, double b) {
        return ((a > 0) && (b < 0)) || ((a < 0) && (b > 0));
    }
}
