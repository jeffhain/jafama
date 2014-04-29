/*
 * Copyright 2012 Jeff Hain
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

public class NumbersUtilsTest extends TestCase {

    //--------------------------------------------------------------------------
    // CONFIGURATION
    //--------------------------------------------------------------------------

    private static final int NBR_OF_VALUES_BIG = 1000 * 1000;

    private static final int NBR_OF_VALUES_SMALL = 10 * 1000;
    
    private static final double ACCURATE_PI_OP_SIN_EPSILON = 1e-10;
    private static final double ACCURATE_PI_OP_DEFAULT_EPSILON = 3e-15;

    //--------------------------------------------------------------------------
    // MEMBERS
    //--------------------------------------------------------------------------
    
    private static final List<Integer> EVEN_INT_VALUES;
    static {
        ArrayList<Integer> values = new ArrayList<Integer>();
        values.add(0);
        values.add(-2);
        values.add(-4);
        values.add(-6);
        values.add(2);
        values.add(4);
        values.add(6);
        values.add(Integer.MIN_VALUE);
        values.add(Integer.MIN_VALUE+2);
        values.add(Integer.MIN_VALUE+4);
        values.add(Integer.MAX_VALUE-1);
        values.add(Integer.MAX_VALUE-3);
        values.add(Integer.MAX_VALUE-5);
        EVEN_INT_VALUES = Collections.unmodifiableList(values);
    }

    private static final List<Long> EVEN_LONG_VALUES;
    static {
        ArrayList<Long> values = new ArrayList<Long>();
        values.add(0L);
        values.add(-4L);
        values.add(-6L);
        values.add(2L);
        values.add(4L);
        values.add(6L);
        values.add(Long.MIN_VALUE);
        values.add(Long.MIN_VALUE+2);
        values.add(Long.MIN_VALUE+4);
        values.add(Long.MAX_VALUE-1);
        values.add(Long.MAX_VALUE-3);
        values.add(Long.MAX_VALUE-5);
        EVEN_LONG_VALUES = Collections.unmodifiableList(values);
    }

    private static final List<Integer> ODD_INT_VALUES;
    static {
        ArrayList<Integer> values = new ArrayList<Integer>();
        values.add(-1);
        values.add(-3);
        values.add(-5);
        values.add(1);
        values.add(3);
        values.add(5);
        values.add(Integer.MIN_VALUE+1);
        values.add(Integer.MIN_VALUE+3);
        values.add(Integer.MIN_VALUE+5);
        values.add(Integer.MAX_VALUE);
        values.add(Integer.MAX_VALUE-2);
        values.add(Integer.MAX_VALUE-4);
        ODD_INT_VALUES = Collections.unmodifiableList(values);
    }

    private static final List<Long> ODD_LONG_VALUES;
    static {
        ArrayList<Long> values = new ArrayList<Long>();
        values.add(-1L);
        values.add(-3L);
        values.add(-5L);
        values.add(1L);
        values.add(3L);
        values.add(5L);
        values.add(Long.MIN_VALUE+1);
        values.add(Long.MIN_VALUE+3);
        values.add(Long.MIN_VALUE+5);
        values.add(Long.MAX_VALUE);
        values.add(Long.MAX_VALUE-2);
        values.add(Long.MAX_VALUE-4);
        ODD_LONG_VALUES = Collections.unmodifiableList(values);
    }

    private final Random random = new Random(123456789L);
    
    private final NumbersTestUtils utils = new NumbersTestUtils(this.random);

    //--------------------------------------------------------------------------
    // PUBLIC METHODS
    //--------------------------------------------------------------------------
    
    public void test_equal_2float() {
        assertTrue(NumbersUtils.equal(-0.0f, +0.0f));
        
        assertTrue(NumbersUtils.equal(1.1f, 1.1f));
        assertFalse(NumbersUtils.equal(1.1f, -1.1f));
        assertFalse(NumbersUtils.equal(1.1f, 1.2f));
        
        assertTrue(NumbersUtils.equal(Float.NaN, Float.NaN));
        assertFalse(NumbersUtils.equal(Float.NaN, 1.1f));
        assertFalse(NumbersUtils.equal(1.1f, Float.NaN));
    }
    
    public void test_equal_2double() {
        assertTrue(NumbersUtils.equal(-0.0, +0.0));
        
        assertTrue(NumbersUtils.equal(1.1, 1.1));
        assertFalse(NumbersUtils.equal(1.1, -1.1));
        assertFalse(NumbersUtils.equal(1.1, 1.2));
        
        assertTrue(NumbersUtils.equal(Double.NaN, Double.NaN));
        assertFalse(NumbersUtils.equal(Double.NaN, 1.1));
        assertFalse(NumbersUtils.equal(1.1, Double.NaN));
    }
    
    public void test_isMathematicalInteger_float() {
        assertFalse(NumbersUtils.isMathematicalInteger(Float.NaN));
        assertFalse(NumbersUtils.isMathematicalInteger(Float.NEGATIVE_INFINITY));
        assertFalse(NumbersUtils.isMathematicalInteger(Float.POSITIVE_INFINITY));
        
        for (float matInt : new float[]{-2,-1,0,1,2,(1<<23)-2}) {
            float matIntMUlp = Float.intBitsToFloat(Float.floatToRawIntBits(matInt)-1);
            float matIntPUlp = Float.intBitsToFloat(Float.floatToRawIntBits(matInt)+1);
            assertFalse(NumbersUtils.isMathematicalInteger(matIntMUlp));
            assertTrue(NumbersUtils.isMathematicalInteger(matInt));
            assertFalse(NumbersUtils.isMathematicalInteger(matIntPUlp));
        }

        for (int i=0;i<NBR_OF_VALUES_BIG;i++) {
            int matInt = this.utils.randomIntUniMag();
            float value = (float)matInt;
            assertTrue(NumbersUtils.isMathematicalInteger(value));
            if (Math.abs(value) <= (1<<23)-2) {
                // If adding or removing an ulp,
                // must no longer be a mathematical integer.
                int pm1 = -1 + 2 * this.random.nextInt(2);
                value = Float.intBitsToFloat(Float.floatToRawIntBits(value)+pm1);
                assertFalse(NumbersUtils.isMathematicalInteger(value));
            }
        }
    }

    public void test_isMathematicalInteger_double() {
        assertFalse(NumbersUtils.isMathematicalInteger(Double.NaN));
        assertFalse(NumbersUtils.isMathematicalInteger(Double.NEGATIVE_INFINITY));
        assertFalse(NumbersUtils.isMathematicalInteger(Double.POSITIVE_INFINITY));
        
        for (double matInt : new double[]{-2,-1,0,1,2,(1L<<52)-2}) {
            double matIntMUlp = Double.longBitsToDouble(Double.doubleToRawLongBits(matInt)-1);
            double matIntPUlp = Double.longBitsToDouble(Double.doubleToRawLongBits(matInt)+1);
            assertFalse(NumbersUtils.isMathematicalInteger(matIntMUlp));
            assertTrue(NumbersUtils.isMathematicalInteger(matInt));
            assertFalse(NumbersUtils.isMathematicalInteger(matIntPUlp));
        }

        for (int i=0;i<NBR_OF_VALUES_BIG;i++) {
            long matInt = this.utils.randomLongUniMag();
            double value = (double)matInt;
            assertTrue(NumbersUtils.isMathematicalInteger(value));
            if (Math.abs(value) <= (1L<<52)-2) {
                // If adding or removing an ulp,
                // must no longer be a mathematical integer.
                int pm1 = -1 + 2 * this.random.nextInt(2);
                value = Double.longBitsToDouble(Double.doubleToRawLongBits(value)+pm1);
                assertFalse(NumbersUtils.isMathematicalInteger(value));
            }
        }
    }

    public void test_isEquidistant_float() {
        assertFalse(NumbersUtils.isEquidistant(Float.NaN));
        assertFalse(NumbersUtils.isEquidistant(Float.NEGATIVE_INFINITY));
        assertFalse(NumbersUtils.isEquidistant(Float.POSITIVE_INFINITY));

        for (float equi : new float[]{-2.5f,-1.5f,-0.5f,0.5f,1.5f,2.5f,(1<<23)-0.5f,(1<<23)-1.5f}) {
            float equiMUlp = Float.intBitsToFloat(Float.floatToRawIntBits(equi)-1);
            float equiPUlp = Float.intBitsToFloat(Float.floatToRawIntBits(equi)+1);
            assertFalse(NumbersUtils.isEquidistant(equiMUlp));
            assertTrue(NumbersUtils.isEquidistant(equi));
            assertFalse(NumbersUtils.isEquidistant(equiPUlp));
        }

        for (int i=0;i<NBR_OF_VALUES_BIG;i++) {
            int matInt = this.utils.randomIntUniMag();
            float value = (float)matInt;
            // Not equidistant since mathematical integer.
            assertFalse(NumbersUtils.isEquidistant(value));
            if (Math.abs(value) <= (1<<23)) {
                // Going half a unit towards zero (unless we start from 0).
                value = value + ((value > 0) ? -0.5f : 0.5f);
                // Now must be equidistant.
                assertTrue(NumbersUtils.isEquidistant(value));
                // If adding or removing an ulp,
                // must no longer be equidistant.
                int pm1 = -1 + 2 * this.random.nextInt(2);
                value = Float.intBitsToFloat(Float.floatToRawIntBits(value)+pm1);
                assertFalse(NumbersUtils.isEquidistant(value));
            }
        }
    }

    public void test_isEquidistant_double() {
        assertFalse(NumbersUtils.isEquidistant(Double.NaN));
        assertFalse(NumbersUtils.isEquidistant(Double.NEGATIVE_INFINITY));
        assertFalse(NumbersUtils.isEquidistant(Double.POSITIVE_INFINITY));
        
        for (double equi : new double[]{-2.5,-1.5,-0.5,0.5,1.5,2.5,(1L<<52)-0.5,(1L<<52)-1.5}) {
            double equiMUlp = Double.longBitsToDouble(Double.doubleToRawLongBits(equi)-1);
            double equiPUlp = Double.longBitsToDouble(Double.doubleToRawLongBits(equi)+1);
            assertFalse(NumbersUtils.isEquidistant(equiMUlp));
            assertTrue(NumbersUtils.isEquidistant(equi));
            assertFalse(NumbersUtils.isEquidistant(equiPUlp));
        }

        for (int i=0;i<NBR_OF_VALUES_BIG;i++) {
            long matInt = this.utils.randomLongUniMag();
            double value = (double)matInt;
            // Not equidistant since mathematical integer.
            assertFalse(NumbersUtils.isEquidistant(value));
            if (Math.abs(value) <= (1L<<52)) {
                // Going half a unit towards zero (unless we start from 0).
                value = value + ((value > 0) ? -0.5 : 0.5);
                // Now must be equidistant.
                assertTrue(NumbersUtils.isEquidistant(value));
                // If adding or removing an ulp,
                // must no longer be equidistant.
                int pm1 = -1 + 2 * this.random.nextInt(2);
                value = Double.longBitsToDouble(Double.doubleToRawLongBits(value)+pm1);
                assertFalse(NumbersUtils.isEquidistant(value));
            }
        }
    }

    public void test_isNaNOrInfinite_float() {
        assertTrue(NumbersUtils.isNaNOrInfinite(Float.NaN));
        assertTrue(NumbersUtils.isNaNOrInfinite(Float.NEGATIVE_INFINITY));
        assertTrue(NumbersUtils.isNaNOrInfinite(Float.POSITIVE_INFINITY));
        
        assertFalse(NumbersUtils.isNaNOrInfinite(Float.MIN_VALUE));
        assertFalse(NumbersUtils.isNaNOrInfinite(NumbersUtils.FLOAT_MIN_NORMAL));
        assertFalse(NumbersUtils.isNaNOrInfinite(Float.MAX_VALUE));
        assertFalse(NumbersUtils.isNaNOrInfinite(-1.1f));
        assertFalse(NumbersUtils.isNaNOrInfinite(-1.0f));
        assertFalse(NumbersUtils.isNaNOrInfinite(-0.1f));
        assertFalse(NumbersUtils.isNaNOrInfinite(-0.0f));
        assertFalse(NumbersUtils.isNaNOrInfinite(0.0f));
        assertFalse(NumbersUtils.isNaNOrInfinite(0.1f));
        assertFalse(NumbersUtils.isNaNOrInfinite(1.0f));
        assertFalse(NumbersUtils.isNaNOrInfinite(1.1f));
    }
    
    public void test_isNaNOrInfinite_double() {
        assertTrue(NumbersUtils.isNaNOrInfinite(Double.NaN));
        assertTrue(NumbersUtils.isNaNOrInfinite(Double.NEGATIVE_INFINITY));
        assertTrue(NumbersUtils.isNaNOrInfinite(Double.POSITIVE_INFINITY));
        
        assertFalse(NumbersUtils.isNaNOrInfinite(Double.MIN_VALUE));
        assertFalse(NumbersUtils.isNaNOrInfinite(NumbersUtils.DOUBLE_MIN_NORMAL));
        assertFalse(NumbersUtils.isNaNOrInfinite(Double.MAX_VALUE));
        assertFalse(NumbersUtils.isNaNOrInfinite(-1.1));
        assertFalse(NumbersUtils.isNaNOrInfinite(-1.0));
        assertFalse(NumbersUtils.isNaNOrInfinite(-0.1));
        assertFalse(NumbersUtils.isNaNOrInfinite(-0.0));
        assertFalse(NumbersUtils.isNaNOrInfinite(0.0));
        assertFalse(NumbersUtils.isNaNOrInfinite(0.1));
        assertFalse(NumbersUtils.isNaNOrInfinite(1.0));
        assertFalse(NumbersUtils.isNaNOrInfinite(1.1));
    }

    /*
     * 
     */
    
    public void test_isInRange_3int() {
        assertTrue(NumbersUtils.isInRange(3, 7, 3));
        assertTrue(NumbersUtils.isInRange(3, 7, 5));
        assertTrue(NumbersUtils.isInRange(3, 7, 7));
        
        assertFalse(NumbersUtils.isInRange(3, 7, Integer.MIN_VALUE));
        assertFalse(NumbersUtils.isInRange(3, 7, 2));
        assertFalse(NumbersUtils.isInRange(3, 7, 8));
        assertFalse(NumbersUtils.isInRange(3, 7, Integer.MAX_VALUE));
        
        assertFalse(NumbersUtils.isInRange(7, 3, Integer.MIN_VALUE));
        assertFalse(NumbersUtils.isInRange(7, 3, 2));
        assertFalse(NumbersUtils.isInRange(7, 3, 3));
        assertFalse(NumbersUtils.isInRange(7, 3, 5));
        assertFalse(NumbersUtils.isInRange(7, 3, 7));
        assertFalse(NumbersUtils.isInRange(7, 3, 8));
        assertFalse(NumbersUtils.isInRange(7, 3, Integer.MAX_VALUE));
    }

    public void test_isInRange_3long() {
        assertTrue(NumbersUtils.isInRange(3L, 7L, 3L));
        assertTrue(NumbersUtils.isInRange(3L, 7L, 5L));
        assertTrue(NumbersUtils.isInRange(3L, 7L, 7L));
        
        assertFalse(NumbersUtils.isInRange(3L, 7L, Long.MIN_VALUE));
        assertFalse(NumbersUtils.isInRange(3L, 7L, 2L));
        assertFalse(NumbersUtils.isInRange(3L, 7L, 8L));
        assertFalse(NumbersUtils.isInRange(3L, 7L, Long.MAX_VALUE));
        
        assertFalse(NumbersUtils.isInRange(7L, 3L, Long.MIN_VALUE));
        assertFalse(NumbersUtils.isInRange(7L, 3L, 2L));
        assertFalse(NumbersUtils.isInRange(7L, 3L, 3L));
        assertFalse(NumbersUtils.isInRange(7L, 3L, 5L));
        assertFalse(NumbersUtils.isInRange(7L, 3L, 7L));
        assertFalse(NumbersUtils.isInRange(7L, 3L, 8L));
        assertFalse(NumbersUtils.isInRange(7L, 3L, Long.MAX_VALUE));
    }

    public void test_isInRange_3float() {
        assertTrue(NumbersUtils.isInRange(3.f, 7.f, 3.f));
        assertTrue(NumbersUtils.isInRange(3.f, 7.f, 5.f));
        assertTrue(NumbersUtils.isInRange(3.f, 7.f, 7.f));
        
        assertFalse(NumbersUtils.isInRange(3.f, 7.f, Float.NEGATIVE_INFINITY));
        assertFalse(NumbersUtils.isInRange(3.f, 7.f, 2.f));
        assertFalse(NumbersUtils.isInRange(3.f, 7.f, 8.f));
        assertFalse(NumbersUtils.isInRange(3.f, 7.f, Float.POSITIVE_INFINITY));
        
        assertFalse(NumbersUtils.isInRange(7.f, 3.f, Float.NEGATIVE_INFINITY));
        assertFalse(NumbersUtils.isInRange(7.f, 3.f, 2.f));
        assertFalse(NumbersUtils.isInRange(7.f, 3.f, 3.f));
        assertFalse(NumbersUtils.isInRange(7.f, 3.f, 5.f));
        assertFalse(NumbersUtils.isInRange(7.f, 3.f, 7.f));
        assertFalse(NumbersUtils.isInRange(7.f, 3.f, 8.f));
        assertFalse(NumbersUtils.isInRange(7.f, 3.f, Float.POSITIVE_INFINITY));
        
        assertFalse(NumbersUtils.isInRange(7.f, 3.f, Float.NaN));
        assertFalse(NumbersUtils.isInRange(Float.NaN, 7.f, 5.f));
        assertFalse(NumbersUtils.isInRange(3.f, Float.NaN, 5.f));
        assertFalse(NumbersUtils.isInRange(3.f, 7.f, Float.NaN));
        
        assertFalse(NumbersUtils.isInRange(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NaN));
    }

    public void test_isInRange_3double() {
        assertTrue(NumbersUtils.isInRange(3., 7., 3.));
        assertTrue(NumbersUtils.isInRange(3., 7., 5.));
        assertTrue(NumbersUtils.isInRange(3., 7., 7.));
        
        assertFalse(NumbersUtils.isInRange(3., 7., Double.NEGATIVE_INFINITY));
        assertFalse(NumbersUtils.isInRange(3., 7., 2.));
        assertFalse(NumbersUtils.isInRange(3., 7., 8.));
        assertFalse(NumbersUtils.isInRange(3., 7., Double.POSITIVE_INFINITY));
        
        assertFalse(NumbersUtils.isInRange(7., 3., Double.NEGATIVE_INFINITY));
        assertFalse(NumbersUtils.isInRange(7., 3., 2.));
        assertFalse(NumbersUtils.isInRange(7., 3., 3.));
        assertFalse(NumbersUtils.isInRange(7., 3., 5.));
        assertFalse(NumbersUtils.isInRange(7., 3., 7.));
        assertFalse(NumbersUtils.isInRange(7., 3., 8.));
        assertFalse(NumbersUtils.isInRange(7., 3., Double.POSITIVE_INFINITY));
        
        assertFalse(NumbersUtils.isInRange(7., 3., Double.NaN));
        assertFalse(NumbersUtils.isInRange(Double.NaN, 7., 5.));
        assertFalse(NumbersUtils.isInRange(3., Double.NaN, 5.));
        assertFalse(NumbersUtils.isInRange(3., 7., Double.NaN));
        
        assertFalse(NumbersUtils.isInRange(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN));
    }

    /*
     * 
     */
    
    public void test_checkIsInRange_3int() {
        try {
            NumbersUtils.checkIsInRange(3, 7, 2);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }
        
        assertTrue(NumbersUtils.checkIsInRange(3, 7, 5));
    }
    
    public void test_checkIsInRange_3long() {
        try {
            NumbersUtils.checkIsInRange(3L, 7L, 2L);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }

        assertTrue(NumbersUtils.checkIsInRange(3L, 7L, 5L));
    }
    
    public void test_checkIsInRange_3float() {
        try {
            NumbersUtils.checkIsInRange(3.f, 7.f, 2.f);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }

        assertTrue(NumbersUtils.checkIsInRange(3.f, 7.f, 5.f));
    }
    
    public void test_checkIsInRange_3double() {
        try {
            NumbersUtils.checkIsInRange(3., 7., 2.);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }

        assertTrue(NumbersUtils.checkIsInRange(3., 7., 5.));
    }
    
    /*
     * 
     */

    public void test_isInRangeSigned_int_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,0,33,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.isInRangeSigned(0, bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
        
        assertTrue(NumbersUtils.isInRangeSigned(Integer.MIN_VALUE, 32));
        assertTrue(NumbersUtils.isInRangeSigned(0, 32));
        assertTrue(NumbersUtils.isInRangeSigned(Integer.MAX_VALUE, 32));

        assertFalse(NumbersUtils.isInRangeSigned(Integer.MIN_VALUE, 8));
        assertFalse(NumbersUtils.isInRangeSigned(-129, 8));
        //
        assertTrue(NumbersUtils.isInRangeSigned(-128, 8));
        assertTrue(NumbersUtils.isInRangeSigned(0, 8));
        assertTrue(NumbersUtils.isInRangeSigned(127, 8));
        //
        assertFalse(NumbersUtils.isInRangeSigned(128, 8));
        assertFalse(NumbersUtils.isInRangeSigned(Integer.MAX_VALUE, 8));

        assertFalse(NumbersUtils.isInRangeSigned(Integer.MIN_VALUE, 1));
        assertFalse(NumbersUtils.isInRangeSigned(-2, 1));
        //
        assertTrue(NumbersUtils.isInRangeSigned(-1, 1));
        assertTrue(NumbersUtils.isInRangeSigned(0, 1));
        //
        assertFalse(NumbersUtils.isInRangeSigned(1, 1));
        assertFalse(NumbersUtils.isInRangeSigned(Integer.MAX_VALUE, 1));
    }

    public void test_isInRangeSigned_long_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,0,65,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.isInRangeSigned(0L, bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
        
        assertTrue(NumbersUtils.isInRangeSigned(Long.MIN_VALUE, 64));
        assertTrue(NumbersUtils.isInRangeSigned(0L, 64));
        assertTrue(NumbersUtils.isInRangeSigned(Long.MAX_VALUE, 64));

        assertFalse(NumbersUtils.isInRangeSigned(Long.MIN_VALUE, 8));
        assertFalse(NumbersUtils.isInRangeSigned(-129L, 8));
        //
        assertTrue(NumbersUtils.isInRangeSigned(-128L, 8));
        assertTrue(NumbersUtils.isInRangeSigned(0L, 8));
        assertTrue(NumbersUtils.isInRangeSigned(127L, 8));
        //
        assertFalse(NumbersUtils.isInRangeSigned(128L, 8));
        assertFalse(NumbersUtils.isInRangeSigned(Long.MAX_VALUE, 8));

        assertFalse(NumbersUtils.isInRangeSigned(Long.MIN_VALUE, 1));
        assertFalse(NumbersUtils.isInRangeSigned(-2L, 1));
        //
        assertTrue(NumbersUtils.isInRangeSigned(-1L, 1));
        assertTrue(NumbersUtils.isInRangeSigned(0L, 1));
        //
        assertFalse(NumbersUtils.isInRangeSigned(1L, 1));
        assertFalse(NumbersUtils.isInRangeSigned(Long.MAX_VALUE, 1));
    }

    public void test_isInRangeUnsigned_int_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,0,32,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.isInRangeUnsigned(0, bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
        
        assertFalse(NumbersUtils.isInRangeUnsigned(Integer.MIN_VALUE, 31));
        assertFalse(NumbersUtils.isInRangeUnsigned(-1, 31));
        //
        assertTrue(NumbersUtils.isInRangeUnsigned(0, 31));
        assertTrue(NumbersUtils.isInRangeUnsigned(Integer.MAX_VALUE, 31));
        
        assertFalse(NumbersUtils.isInRangeUnsigned(Integer.MIN_VALUE, 8));
        assertFalse(NumbersUtils.isInRangeUnsigned(-1, 8));
        //
        assertTrue(NumbersUtils.isInRangeUnsigned(0, 8));
        assertTrue(NumbersUtils.isInRangeUnsigned(128, 8));
        assertTrue(NumbersUtils.isInRangeUnsigned(255, 8));
        //
        assertFalse(NumbersUtils.isInRangeUnsigned(256, 8));
        assertFalse(NumbersUtils.isInRangeUnsigned(Integer.MAX_VALUE, 8));

        assertFalse(NumbersUtils.isInRangeUnsigned(Integer.MIN_VALUE, 1));
        assertFalse(NumbersUtils.isInRangeUnsigned(-1, 1));
        //
        assertTrue(NumbersUtils.isInRangeUnsigned(0, 1));
        assertTrue(NumbersUtils.isInRangeUnsigned(1, 1));
        //
        assertFalse(NumbersUtils.isInRangeUnsigned(2, 1));
        assertFalse(NumbersUtils.isInRangeUnsigned(Integer.MAX_VALUE, 1));
    }

    public void test_isInRangeUnsigned_long_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,0,64,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.isInRangeUnsigned(0L, bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        assertFalse(NumbersUtils.isInRangeUnsigned(Long.MIN_VALUE, 63));
        assertFalse(NumbersUtils.isInRangeUnsigned(-1L, 63));
        //
        assertTrue(NumbersUtils.isInRangeUnsigned(0L, 63));
        assertTrue(NumbersUtils.isInRangeUnsigned(Long.MAX_VALUE, 63));
        
        assertFalse(NumbersUtils.isInRangeUnsigned(Long.MIN_VALUE, 8));
        assertFalse(NumbersUtils.isInRangeUnsigned(-1L, 8));
        //
        assertTrue(NumbersUtils.isInRangeUnsigned(0L, 8));
        assertTrue(NumbersUtils.isInRangeUnsigned(128L, 8));
        assertTrue(NumbersUtils.isInRangeUnsigned(255L, 8));
        //
        assertFalse(NumbersUtils.isInRangeUnsigned(256L, 8));
        assertFalse(NumbersUtils.isInRangeUnsigned(Long.MAX_VALUE, 8));

        assertFalse(NumbersUtils.isInRangeUnsigned(Long.MIN_VALUE, 1));
        assertFalse(NumbersUtils.isInRangeUnsigned(-1L, 1));
        //
        assertTrue(NumbersUtils.isInRangeUnsigned(0L, 1));
        assertTrue(NumbersUtils.isInRangeUnsigned(1L, 1));
        //
        assertFalse(NumbersUtils.isInRangeUnsigned(2L, 1));
        assertFalse(NumbersUtils.isInRangeUnsigned(Long.MAX_VALUE, 1));
    }
    
    /*
     * 
     */
    
    public void test_checkIsInRangeSigned_int_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,0,33,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.checkIsInRangeSigned(0, bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
        
        assertTrue(NumbersUtils.checkIsInRangeSigned(-128, 8));
        assertTrue(NumbersUtils.checkIsInRangeSigned(127, 8));
        try {
            NumbersUtils.checkIsInRangeSigned(-129, 8);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }
        try {
            NumbersUtils.checkIsInRangeSigned(128, 8);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }
    }
    
    public void test_checkIsInRangeSigned_long_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,0,65,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.checkIsInRangeSigned(0L, bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        assertTrue(NumbersUtils.checkIsInRangeSigned(-128L, 8));
        assertTrue(NumbersUtils.checkIsInRangeSigned(127L, 8));
        try {
            NumbersUtils.checkIsInRangeSigned(-129L, 8);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }
        try {
            NumbersUtils.checkIsInRangeSigned(128L, 8);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }
    }
    
    public void test_checkIsInRangeUnsigned_int_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,0,32,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.checkIsInRangeUnsigned(0, bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
        
        assertTrue(NumbersUtils.checkIsInRangeUnsigned(0, 8));
        assertTrue(NumbersUtils.checkIsInRangeUnsigned(255, 8));
        try {
            NumbersUtils.checkIsInRangeUnsigned(-1, 8);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }
        try {
            NumbersUtils.checkIsInRangeUnsigned(256, 8);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }
    }
    
    public void test_checkIsInRangeUnsigned_long_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,0,64,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.checkIsInRangeUnsigned(0L, bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
        
        assertTrue(NumbersUtils.checkIsInRangeUnsigned(0L, 8));
        assertTrue(NumbersUtils.checkIsInRangeUnsigned(255L, 8));
        try {
            NumbersUtils.checkIsInRangeUnsigned(-1L, 8);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }
        try {
            NumbersUtils.checkIsInRangeUnsigned(256L, 8);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }
    }
    
    /*
     * 
     */
    
    public void test_toRange_3int() {
        assertEquals(10,NumbersUtils.toRange(10, 5, 9));
        assertEquals(5,NumbersUtils.toRange(10, 5, 11));
        
        for (int min=-10;min<=10;min++) {
            for (int max=min;max<=10;max++) {
                assertEquals(min, NumbersUtils.toRange(min, max, min-1));
                for (int value=min;value<=max;value++) {
                    assertEquals(value, NumbersUtils.toRange(min, max, value));
                }
                assertEquals(max, NumbersUtils.toRange(min, max, max+1));
            }
        }
    }

    public void test_toRange_3long() {
        assertEquals(10L,NumbersUtils.toRange(10L, 5L, 9L));
        assertEquals(5L,NumbersUtils.toRange(10L, 5L, 11L));
        
        for (long min=-10;min<=10;min++) {
            for (long max=min;max<=10;max++) {
                assertEquals(min, NumbersUtils.toRange(min, max, min-1));
                for (long value=min;value<=max;value++) {
                    assertEquals(value, NumbersUtils.toRange(min, max, value));
                }
                assertEquals(max, NumbersUtils.toRange(min, max, max+1));
            }
        }
    }

    public void test_toRange_3float() {
        assertEquals(10.0f,NumbersUtils.toRange(10.0f, 5.0f, 9.0f));
        assertEquals(5.0f,NumbersUtils.toRange(10.0f, 5.0f, 11.0f));
        
        for (float min=-10;min<=10;min++) {
            for (float max=min;max<=10;max++) {
                assertEquals(min, NumbersUtils.toRange(min, max, min-1));
                for (float value=min;value<=max;value++) {
                    assertEquals(value, NumbersUtils.toRange(min, max, value));
                }
                assertEquals(max, NumbersUtils.toRange(min, max, max+1));
            }
        }
    }

    public void test_toRange_3double() {
        assertEquals(10.0,NumbersUtils.toRange(10.0, 5.0, 9.0));
        assertEquals(5.0,NumbersUtils.toRange(10.0, 5.0, 11.0));
        
        for (double min=-10;min<=10;min++) {
            for (double max=min;max<=10;max++) {
                assertEquals(min, NumbersUtils.toRange(min, max, min-1));
                for (double value=min;value<=max;value++) {
                    assertEquals(value, NumbersUtils.toRange(min, max, value));
                }
                assertEquals(max, NumbersUtils.toRange(min, max, max+1));
            }
        }
    }

    /*
     * 
     */

    public void test_intMaskMSBits0_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,-1,33,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.intMaskMSBits0(bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        assertEquals(0xFFFFFFFF,NumbersUtils.intMaskMSBits0(0));
        assertEquals(0x7FFFFFFF,NumbersUtils.intMaskMSBits0(1));
        assertEquals(1,NumbersUtils.intMaskMSBits0(31));
        assertEquals(0,NumbersUtils.intMaskMSBits0(32));
    }
    
    public void test_intMaskMSBits1_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,-1,33,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.intMaskMSBits1(bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        assertEquals(0,NumbersUtils.intMaskMSBits1(0));
        assertEquals(0x80000000,NumbersUtils.intMaskMSBits1(1));
        assertEquals(0xFFFFFFFE,NumbersUtils.intMaskMSBits1(31));
        assertEquals(0xFFFFFFFF,NumbersUtils.intMaskMSBits1(32));
    }

    public void test_intMaskLSBits0_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,-1,33,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.intMaskLSBits0(bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        assertEquals(0xFFFFFFFF,NumbersUtils.intMaskLSBits0(0));
        assertEquals(0xFFFFFFFE,NumbersUtils.intMaskLSBits0(1));
        assertEquals(0x80000000,NumbersUtils.intMaskLSBits0(31));
        assertEquals(0,NumbersUtils.intMaskLSBits0(32));
    }

    public void test_intMaskLSBits1_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,-1,33,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.intMaskLSBits1(bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        assertEquals(0,NumbersUtils.intMaskLSBits1(0));
        assertEquals(1,NumbersUtils.intMaskLSBits1(1));
        assertEquals(0x7FFFFFFF,NumbersUtils.intMaskLSBits1(31));
        assertEquals(0xFFFFFFFF,NumbersUtils.intMaskLSBits1(32));
    }

    /*
     * 
     */
    
    public void test_longMaskMSBits0_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,-1,65,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.longMaskMSBits0(bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        assertEquals(0xFFFFFFFFFFFFFFFFL,NumbersUtils.longMaskMSBits0(0));
        assertEquals(0x7FFFFFFFFFFFFFFFL,NumbersUtils.longMaskMSBits0(1));
        assertEquals(1L,NumbersUtils.longMaskMSBits0(63));
        assertEquals(0L,NumbersUtils.longMaskMSBits0(64));
    }
    
    public void test_longMaskMSBits1_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,-1,65,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.longMaskMSBits1(bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        assertEquals(0L,NumbersUtils.longMaskMSBits1(0));
        assertEquals(0x8000000000000000L,NumbersUtils.longMaskMSBits1(1));
        assertEquals(0xFFFFFFFFFFFFFFFEL,NumbersUtils.longMaskMSBits1(63));
        assertEquals(0xFFFFFFFFFFFFFFFFL,NumbersUtils.longMaskMSBits1(64));
    }

    public void test_longMaskLSBits0_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,-1,65,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.longMaskLSBits0(bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        assertEquals(0xFFFFFFFFFFFFFFFFL,NumbersUtils.longMaskLSBits0(0));
        assertEquals(0xFFFFFFFFFFFFFFFEL,NumbersUtils.longMaskLSBits0(1));
        assertEquals(0x8000000000000000L,NumbersUtils.longMaskLSBits0(63));
        assertEquals(0L,NumbersUtils.longMaskLSBits0(64));
    }

    public void test_longMaskLSBits1_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,-1,65,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.longMaskLSBits1(bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        assertEquals(0L,NumbersUtils.longMaskLSBits1(0));
        assertEquals(1L,NumbersUtils.longMaskLSBits1(1));
        assertEquals(0x7FFFFFFFFFFFFFFFL,NumbersUtils.longMaskLSBits1(63));
        assertEquals(0xFFFFFFFFFFFFFFFFL,NumbersUtils.longMaskLSBits1(64));
    }

    /*
     * 
     */
    
    public void test_byteAsUnsigned_byte() {
        final long signedMin = Byte.MIN_VALUE;
        final long signedMax = Byte.MAX_VALUE;
        final long unsignedMax = 2*signedMax+1;
        
        assertEquals(signedMax+1,NumbersUtils.byteAsUnsigned((byte)signedMin));
        assertEquals(unsignedMax,NumbersUtils.byteAsUnsigned((byte)-1));
        assertEquals(0,NumbersUtils.byteAsUnsigned((byte)0));
        assertEquals(1,NumbersUtils.byteAsUnsigned((byte)1));
        assertEquals(signedMax,NumbersUtils.byteAsUnsigned((byte)signedMax));
    }
    
    public void test_shortAsUnsigned(short value) {
        final long signedMin = Short.MIN_VALUE;
        final long signedMax = Short.MAX_VALUE;
        final long unsignedMax = 2*signedMax+1;
        
        assertEquals(signedMax+1,NumbersUtils.shortAsUnsigned((short)signedMin));
        assertEquals(unsignedMax,NumbersUtils.shortAsUnsigned((short)-1));
        assertEquals(0,NumbersUtils.shortAsUnsigned((short)0));
        assertEquals(1,NumbersUtils.shortAsUnsigned((short)1));
        assertEquals(signedMax,NumbersUtils.shortAsUnsigned((short)signedMax));
    }
    
    public void test_intAsUnsigned(int value) {
        final long signedMin = Integer.MIN_VALUE;
        final long signedMax = Integer.MAX_VALUE;
        final long unsignedMax = 2*signedMax+1;
        
        assertEquals(signedMax+1,NumbersUtils.intAsUnsigned((int)signedMin));
        assertEquals(unsignedMax,NumbersUtils.intAsUnsigned((int)-1));
        assertEquals(0,NumbersUtils.intAsUnsigned((int)0));
        assertEquals(1,NumbersUtils.intAsUnsigned((int)1));
        assertEquals(signedMax,NumbersUtils.intAsUnsigned((int)signedMax));
    }

    /*
     * 
     */
    
    public void test_isValidBitSizeForSignedInt_int() {
        assertFalse(NumbersUtils.isValidBitSizeForSignedInt(Integer.MIN_VALUE));
        assertFalse(NumbersUtils.isValidBitSizeForSignedInt(-1));
        assertFalse(NumbersUtils.isValidBitSizeForSignedInt(0));
        
        assertTrue(NumbersUtils.isValidBitSizeForSignedInt(1));
        assertTrue(NumbersUtils.isValidBitSizeForSignedInt(32));
        
        assertFalse(NumbersUtils.isValidBitSizeForSignedInt(33));
        assertFalse(NumbersUtils.isValidBitSizeForSignedInt(Integer.MAX_VALUE));
    }
    
    public void test_isValidBitSizeForSignedLong_int() {
        assertFalse(NumbersUtils.isValidBitSizeForSignedLong(Integer.MIN_VALUE));
        assertFalse(NumbersUtils.isValidBitSizeForSignedLong(-1));
        assertFalse(NumbersUtils.isValidBitSizeForSignedLong(0));
        
        assertTrue(NumbersUtils.isValidBitSizeForSignedLong(1));
        assertTrue(NumbersUtils.isValidBitSizeForSignedLong(64));
        
        assertFalse(NumbersUtils.isValidBitSizeForSignedLong(65));
        assertFalse(NumbersUtils.isValidBitSizeForSignedLong(Integer.MAX_VALUE));
    }
    
    public void test_isValidBitSizeForUnsignedInt_int() {
        assertFalse(NumbersUtils.isValidBitSizeForUnsignedInt(Integer.MIN_VALUE));
        assertFalse(NumbersUtils.isValidBitSizeForUnsignedInt(-1));
        assertFalse(NumbersUtils.isValidBitSizeForUnsignedInt(0));
        
        assertTrue(NumbersUtils.isValidBitSizeForUnsignedInt(1));
        assertTrue(NumbersUtils.isValidBitSizeForUnsignedInt(31));
        
        assertFalse(NumbersUtils.isValidBitSizeForUnsignedInt(32));
        assertFalse(NumbersUtils.isValidBitSizeForUnsignedInt(Integer.MAX_VALUE));
    }

    public void test_isValidBitSizeForUnsignedLong_int() {
        assertFalse(NumbersUtils.isValidBitSizeForUnsignedLong(Integer.MIN_VALUE));
        assertFalse(NumbersUtils.isValidBitSizeForUnsignedLong(-1));
        assertFalse(NumbersUtils.isValidBitSizeForUnsignedLong(0));
        
        assertTrue(NumbersUtils.isValidBitSizeForUnsignedLong(1));
        assertTrue(NumbersUtils.isValidBitSizeForUnsignedLong(63));
        
        assertFalse(NumbersUtils.isValidBitSizeForUnsignedLong(64));
        assertFalse(NumbersUtils.isValidBitSizeForUnsignedLong(Integer.MAX_VALUE));
    }

    /*
     * 
     */
    
    public void test_checkBitSizeForSignedInt_int() {
        assertEquals(true,NumbersUtils.checkBitSizeForSignedInt(32));
        
        try {
            NumbersUtils.checkBitSizeForSignedInt(33);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    public void test_checkBitSizeForSignedLong_int() {
        assertEquals(true,NumbersUtils.checkBitSizeForSignedLong(64));
        
        try {
            NumbersUtils.checkBitSizeForSignedLong(65);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    public void test_checkBitSizeForUnsignedInt_int() {
        assertEquals(true,NumbersUtils.checkBitSizeForUnsignedInt(31));
        
        try {
            NumbersUtils.checkBitSizeForUnsignedInt(32);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }
    }
    
    public void test_checkBitSizeForUnsignedLong_int() {
        assertEquals(true,NumbersUtils.checkBitSizeForUnsignedLong(63));
        
        try {
            NumbersUtils.checkBitSizeForUnsignedLong(64);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /*
     * 
     */
    
    public void test_minSignedIntForBitSize_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,0,33,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.minSignedIntForBitSize(bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
        
        assertEquals(-1,NumbersUtils.minSignedIntForBitSize(1));
        assertEquals(-2,NumbersUtils.minSignedIntForBitSize(2));
        assertEquals(-4,NumbersUtils.minSignedIntForBitSize(3));
        assertEquals(Integer.MIN_VALUE>>1,NumbersUtils.minSignedIntForBitSize(31));
        assertEquals(Integer.MIN_VALUE,NumbersUtils.minSignedIntForBitSize(32));
    }

    public void test_minSignedLongForBitSize_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,0,65,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.minSignedLongForBitSize(bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
        
        assertEquals(-1L,NumbersUtils.minSignedLongForBitSize(1));
        assertEquals(-2L,NumbersUtils.minSignedLongForBitSize(2));
        assertEquals(-4L,NumbersUtils.minSignedLongForBitSize(3));
        assertEquals(Long.MIN_VALUE>>1,NumbersUtils.minSignedLongForBitSize(63));
        assertEquals(Long.MIN_VALUE,NumbersUtils.minSignedLongForBitSize(64));
    }

    public void test_maxSignedIntForBitSize_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,0,33,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.maxSignedIntForBitSize(bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
        
        assertEquals(0,NumbersUtils.maxSignedIntForBitSize(1));
        assertEquals(1,NumbersUtils.maxSignedIntForBitSize(2));
        assertEquals(3,NumbersUtils.maxSignedIntForBitSize(3));
        assertEquals(Integer.MAX_VALUE>>1,NumbersUtils.maxSignedIntForBitSize(31));
        assertEquals(Integer.MAX_VALUE,NumbersUtils.maxSignedIntForBitSize(32));
    }
    
    public void test_maxSignedLongForBitSize_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,0,65,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.maxSignedLongForBitSize(bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
        
        assertEquals(0L,NumbersUtils.maxSignedLongForBitSize(1));
        assertEquals(1L,NumbersUtils.maxSignedLongForBitSize(2));
        assertEquals(3L,NumbersUtils.maxSignedLongForBitSize(3));
        assertEquals(Long.MAX_VALUE>>1,NumbersUtils.maxSignedLongForBitSize(63));
        assertEquals(Long.MAX_VALUE,NumbersUtils.maxSignedLongForBitSize(64));
    }
    
    public void test_maxUnsignedIntForBitSize_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,0,32,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.maxUnsignedIntForBitSize(bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
        
        assertEquals(1,NumbersUtils.maxUnsignedIntForBitSize(1));
        assertEquals(3,NumbersUtils.maxUnsignedIntForBitSize(2));
        assertEquals(7,NumbersUtils.maxUnsignedIntForBitSize(3));
        assertEquals(Integer.MAX_VALUE>>1,NumbersUtils.maxUnsignedIntForBitSize(30));
        assertEquals(Integer.MAX_VALUE,NumbersUtils.maxUnsignedIntForBitSize(31));
    }

    public void test_maxUnsignedLongForBitSize_int() {
        for (int bitSize : new int[]{Integer.MIN_VALUE,0,64,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.maxUnsignedLongForBitSize(bitSize);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
        
        assertEquals(1L,NumbersUtils.maxUnsignedLongForBitSize(1));
        assertEquals(3L,NumbersUtils.maxUnsignedLongForBitSize(2));
        assertEquals(7L,NumbersUtils.maxUnsignedLongForBitSize(3));
        assertEquals(Long.MAX_VALUE>>1,NumbersUtils.maxUnsignedLongForBitSize(62));
        assertEquals(Long.MAX_VALUE,NumbersUtils.maxUnsignedLongForBitSize(63));
    }

    /*
     * 
     */
    
    public void test_bitSizeForSignedValue_int() {
        /*
         * Considering
         * minSignedIntForBitSize
         * and
         * maxSignedIntForBitSize
         * work.
         */
        for (int bitSize=1;bitSize<=32;bitSize++) {
            int min = NumbersUtils.minSignedIntForBitSize(bitSize);
            int max = NumbersUtils.maxSignedIntForBitSize(bitSize);
            
            assertEquals(bitSize, NumbersUtils.bitSizeForSignedValue(min));
            assertEquals(bitSize, NumbersUtils.bitSizeForSignedValue(max));
            
            if (bitSize != 32) {
                // higher bit size for exterior values
                assertEquals(bitSize+1, NumbersUtils.bitSizeForSignedValue(min-1));
                assertEquals(bitSize+1, NumbersUtils.bitSizeForSignedValue(max+1));
            }
        }
    }
    
    public void test_bitSizeForSignedValue_long() {
        /*
         * Considering
         * minSignedLongForBitSize
         * and
         * maxSignedLongForBitSize
         * work.
         */
        for (int bitSize=1;bitSize<=64;bitSize++) {
            long min = NumbersUtils.minSignedLongForBitSize(bitSize);
            long max = NumbersUtils.maxSignedLongForBitSize(bitSize);
            
            assertEquals(bitSize, NumbersUtils.bitSizeForSignedValue(min));
            assertEquals(bitSize, NumbersUtils.bitSizeForSignedValue(max));
            
            if (bitSize != 64) {
                // higher bit size for exterior values
                assertEquals(bitSize+1, NumbersUtils.bitSizeForSignedValue(min-1));
                assertEquals(bitSize+1, NumbersUtils.bitSizeForSignedValue(max+1));
            }
        }
    }
    
    public void test_bitSizeForUnsignedValue_int() {
        /*
         * Considering
         * maxUnsignedIntForBitSize
         * work.
         */
        for (int bitSize=1;bitSize<=31;bitSize++) {
            int max = NumbersUtils.maxUnsignedIntForBitSize(bitSize);
            
            assertEquals(bitSize, NumbersUtils.bitSizeForUnsignedValue(max));
            
            if (bitSize != 31) {
                // higher bit size for exterior values
                assertEquals(bitSize+1, NumbersUtils.bitSizeForUnsignedValue(max+1));
            }
        }
    }
    
    public void test_bitSizeForUnsignedValue_long() {
        /*
         * Considering
         * maxUnsignedLongForBitSize
         * work.
         */
        for (int bitSize=1;bitSize<=63;bitSize++) {
            long max = NumbersUtils.maxUnsignedLongForBitSize(bitSize);
            
            assertEquals(bitSize, NumbersUtils.bitSizeForUnsignedValue(max));
            
            if (bitSize != 63) {
                // higher bit size for exterior values
                assertEquals(bitSize+1, NumbersUtils.bitSizeForUnsignedValue(max+1));
            }
        }
    }
    
    /*
     * 
     */
    
    public void test_signum_int() {
        assertEquals(-1,NumbersUtils.signum(Integer.MIN_VALUE));
        assertEquals(-1,NumbersUtils.signum(-1));
        assertEquals(0,NumbersUtils.signum(0));
        assertEquals(1,NumbersUtils.signum(1));
        assertEquals(1,NumbersUtils.signum(Integer.MAX_VALUE));
    }
    
    public void test_signum_long() {
        assertEquals(-1,NumbersUtils.signum(Long.MIN_VALUE));
        assertEquals(-1,NumbersUtils.signum(-1L));
        assertEquals(0,NumbersUtils.signum(0L));
        assertEquals(1,NumbersUtils.signum(1L));
        assertEquals(1,NumbersUtils.signum(Long.MAX_VALUE));
    }

    public void test_isEven_int() {
        for (Integer even : EVEN_INT_VALUES) {
            assertTrue(NumbersUtils.isEven(even));
        }
        
        for (Integer odd : ODD_INT_VALUES) {
            assertFalse(NumbersUtils.isEven(odd));
        }
    }

    public void test_isEven_long() {
        for (Long even : EVEN_LONG_VALUES) {
            assertTrue(NumbersUtils.isEven(even));
        }
        
        for (Long odd : ODD_LONG_VALUES) {
            assertFalse(NumbersUtils.isEven(odd));
        }
    }

    public void test_isOdd_int() {
        for (Integer even : EVEN_INT_VALUES) {
            assertFalse(NumbersUtils.isOdd(even));
        }
        
        for (Integer odd : ODD_INT_VALUES) {
            assertTrue(NumbersUtils.isOdd(odd));
        }
    }

    public void test_isOdd_long() {
        for (Long even : EVEN_LONG_VALUES) {
            assertFalse(NumbersUtils.isOdd(even));
        }
        
        for (Long odd : ODD_LONG_VALUES) {
            assertTrue(NumbersUtils.isOdd(odd));
        }
    }

    public void test_haveSameEvenness_2int() {
        for (Integer even : EVEN_INT_VALUES) {
            for (Integer odd : ODD_INT_VALUES) {
                assertFalse(NumbersUtils.haveSameEvenness(even, odd));
                assertFalse(NumbersUtils.haveSameEvenness(odd, even));
            }
        }
        
        for (Integer even1 : EVEN_INT_VALUES) {
            for (Integer even2 : EVEN_INT_VALUES) {
                assertTrue(NumbersUtils.haveSameEvenness(even1, even2));
            }
        }
        
        for (Integer odd1 : ODD_INT_VALUES) {
            for (Integer odd2 : ODD_INT_VALUES) {
                assertTrue(NumbersUtils.haveSameEvenness(odd1, odd2));
            }
        }
    }
    
    public void test_haveSameEvenness_2long() {
        for (Long even : EVEN_LONG_VALUES) {
            for (Long odd : ODD_LONG_VALUES) {
                assertFalse(NumbersUtils.haveSameEvenness(even, odd));
                assertFalse(NumbersUtils.haveSameEvenness(odd, even));
            }
        }
        
        for (Long even1 : EVEN_LONG_VALUES) {
            for (Long even2 : EVEN_LONG_VALUES) {
                assertTrue(NumbersUtils.haveSameEvenness(even1, even2));
            }
        }
        
        for (Long odd1 : ODD_LONG_VALUES) {
            for (Long odd2 : ODD_LONG_VALUES) {
                assertTrue(NumbersUtils.haveSameEvenness(odd1, odd2));
            }
        }
    }
    
    public void test_haveSameSign_2int() {
        for (int i=1;i<10;i++) {
            for (int j=1;j<10;j++) {
                assertTrue(NumbersUtils.haveSameSign(i, j));
                assertTrue(NumbersUtils.haveSameSign(-i, -j));
                assertFalse(NumbersUtils.haveSameSign(i, -j));
                assertFalse(NumbersUtils.haveSameSign(-i, j));
            }
            assertTrue(NumbersUtils.haveSameSign(0, i));
            assertTrue(NumbersUtils.haveSameSign(i, 0));
            assertFalse(NumbersUtils.haveSameSign(0, -i));
            assertFalse(NumbersUtils.haveSameSign(-i, 0));
        }
        
        assertTrue(NumbersUtils.haveSameSign(Integer.MAX_VALUE, Integer.MAX_VALUE));
        assertTrue(NumbersUtils.haveSameSign(Integer.MIN_VALUE, Integer.MIN_VALUE));
        assertTrue(NumbersUtils.haveSameSign(0, Integer.MAX_VALUE));
        assertTrue(NumbersUtils.haveSameSign(Integer.MAX_VALUE, 0));
        assertFalse(NumbersUtils.haveSameSign(Integer.MIN_VALUE, Integer.MAX_VALUE));
        assertFalse(NumbersUtils.haveSameSign(Integer.MIN_VALUE, 0));
        assertFalse(NumbersUtils.haveSameSign(Integer.MAX_VALUE, Integer.MIN_VALUE));
        assertFalse(NumbersUtils.haveSameSign(0, Integer.MIN_VALUE));
    }
    
    public void test_haveSameSign_2long() {
        for (long i=1;i<10;i++) {
            for (long j=1;j<10;j++) {
                assertTrue(NumbersUtils.haveSameSign(i, j));
                assertTrue(NumbersUtils.haveSameSign(-i, -j));
                assertFalse(NumbersUtils.haveSameSign(i, -j));
                assertFalse(NumbersUtils.haveSameSign(-i, j));
            }
            assertTrue(NumbersUtils.haveSameSign(0L, i));
            assertTrue(NumbersUtils.haveSameSign(i, 0L));
            assertFalse(NumbersUtils.haveSameSign(0L, -i));
            assertFalse(NumbersUtils.haveSameSign(-i, 0L));
        }
        
        assertTrue(NumbersUtils.haveSameSign(Long.MAX_VALUE, Long.MAX_VALUE));
        assertTrue(NumbersUtils.haveSameSign(Long.MIN_VALUE, Long.MIN_VALUE));
        assertTrue(NumbersUtils.haveSameSign(0L, Long.MAX_VALUE));
        assertTrue(NumbersUtils.haveSameSign(Long.MAX_VALUE, 0L));
        assertFalse(NumbersUtils.haveSameSign(Long.MIN_VALUE, Long.MAX_VALUE));
        assertFalse(NumbersUtils.haveSameSign(Long.MIN_VALUE, 0L));
        assertFalse(NumbersUtils.haveSameSign(Long.MAX_VALUE, Long.MIN_VALUE));
        assertFalse(NumbersUtils.haveSameSign(0L, Long.MIN_VALUE));
    }
    
    public void test_isPowerOfTwo_int() {
        for (int i=-128;i<=128;i++) {
            final boolean expected = (i > 0) && (i == Integer.highestOneBit(i));
            assertEquals(expected, NumbersUtils.isPowerOfTwo(i));
        }
        
        for (int k=0;k<=30;k++) {
            assertTrue(NumbersUtils.isPowerOfTwo(1<<k));
        }
        
        assertFalse(NumbersUtils.isPowerOfTwo(Integer.MIN_VALUE));
        assertFalse(NumbersUtils.isPowerOfTwo(Integer.MIN_VALUE+1));
        assertFalse(NumbersUtils.isPowerOfTwo(Integer.MIN_VALUE+2));

        assertFalse(NumbersUtils.isPowerOfTwo(Integer.MAX_VALUE));
        assertFalse(NumbersUtils.isPowerOfTwo(Integer.MAX_VALUE-1));
        assertFalse(NumbersUtils.isPowerOfTwo(Integer.MAX_VALUE-2));
    }
    
    public void test_isPowerOfTwo_long() {
        for (long i=-128;i<=128;i++) {
            final boolean expected = (i > 0) && (i == Long.highestOneBit(i));
            assertEquals(expected, NumbersUtils.isPowerOfTwo(i));
        }
        
        for (int k=0;k<=62;k++) {
            assertTrue(NumbersUtils.isPowerOfTwo(1L<<k));
        }
        
        assertFalse(NumbersUtils.isPowerOfTwo(Long.MIN_VALUE));
        assertFalse(NumbersUtils.isPowerOfTwo(Long.MIN_VALUE+1));
        assertFalse(NumbersUtils.isPowerOfTwo(Long.MIN_VALUE+2));

        assertFalse(NumbersUtils.isPowerOfTwo(Long.MAX_VALUE));
        assertFalse(NumbersUtils.isPowerOfTwo(Long.MAX_VALUE-1));
        assertFalse(NumbersUtils.isPowerOfTwo(Long.MAX_VALUE-2));
    }

    public void test_isSignedPowerOfTwo_int() {
        for (int i=-128;i<=128;i++) {
            final boolean expected =
                (i != 0)
                && ((i == Integer.MIN_VALUE)
                        || (Math.abs(i) == Integer.highestOneBit(Math.abs(i))));
            assertEquals(expected, NumbersUtils.isSignedPowerOfTwo(i));
        }
        
        for (int k=0;k<=30;k++) {
            assertTrue(NumbersUtils.isSignedPowerOfTwo(1<<k));
            assertTrue(NumbersUtils.isSignedPowerOfTwo(-(1<<k)));
        }
        assertTrue(NumbersUtils.isSignedPowerOfTwo(Integer.MIN_VALUE));
        
        assertFalse(NumbersUtils.isSignedPowerOfTwo(Integer.MIN_VALUE+1));
        assertFalse(NumbersUtils.isSignedPowerOfTwo(Integer.MIN_VALUE+2));

        assertFalse(NumbersUtils.isSignedPowerOfTwo(Integer.MAX_VALUE));
        assertFalse(NumbersUtils.isSignedPowerOfTwo(Integer.MAX_VALUE-1));
        assertFalse(NumbersUtils.isSignedPowerOfTwo(Integer.MAX_VALUE-2));
    }

    public void test_isSignedPowerOfTwo_long() {
        for (long i=-128;i<=128;i++) {
            final boolean expected =
                (i != 0)
                && ((i == Long.MIN_VALUE)
                        || (Math.abs(i) == Long.highestOneBit(Math.abs(i))));
            assertEquals(expected, NumbersUtils.isSignedPowerOfTwo(i));
        }
        
        for (int k=0;k<=62;k++) {
            assertTrue(NumbersUtils.isSignedPowerOfTwo(1L<<k));
            assertTrue(NumbersUtils.isSignedPowerOfTwo(-(1L<<k)));
        }
        assertTrue(NumbersUtils.isSignedPowerOfTwo(Long.MIN_VALUE));
        
        assertFalse(NumbersUtils.isSignedPowerOfTwo(Long.MIN_VALUE+1));
        assertFalse(NumbersUtils.isSignedPowerOfTwo(Long.MIN_VALUE+2));

        assertFalse(NumbersUtils.isSignedPowerOfTwo(Long.MAX_VALUE));
        assertFalse(NumbersUtils.isSignedPowerOfTwo(Long.MAX_VALUE-1));
        assertFalse(NumbersUtils.isSignedPowerOfTwo(Long.MAX_VALUE-2));
    }
    
    public void test_floorPowerOfTwo_int() {
        for (int i : new int[]{
                Integer.MIN_VALUE,
                Integer.MIN_VALUE+1,
                -1,
                0}) {
            try {
                NumbersUtils.floorPowerOfTwo(i);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        for (int i : new int[]{(1<<30)+1,Integer.MAX_VALUE}) {
            assertEquals(1<<30, NumbersUtils.floorPowerOfTwo(i));
        }

        for (int i=1;i<=128;i++) {
            final int expected = 1<<((int)Math.floor(Math.log(i)/Math.log(2)));
            assertEquals(expected, NumbersUtils.floorPowerOfTwo(i));
        }

        for (int k=0;k<=30;k++) {
            final int pot = (1<<k);
            if (k == 0) {
                // underflow
            } else {
                assertEquals(pot/2, NumbersUtils.floorPowerOfTwo(pot-1));
            }
            assertEquals(pot, NumbersUtils.floorPowerOfTwo(pot));
            if (k == 0) {
                // floorPowerOfTwo(2) is not 1, but still 2
                assertEquals(pot+1, NumbersUtils.floorPowerOfTwo(pot+1));
            } else {
                assertEquals(pot, NumbersUtils.floorPowerOfTwo(pot+1));
            }
        }
    }

    public void test_floorPowerOfTwo_long() {
        for (long i : new long[]{
                Long.MIN_VALUE,
                Long.MIN_VALUE+1,
                -1L,
                0L}) {
            try {
                NumbersUtils.floorPowerOfTwo(i);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        for (long i : new long[]{(1L<<62)+1,Long.MAX_VALUE}) {
            assertEquals(1L<<62, NumbersUtils.floorPowerOfTwo(i));
        }

        for (long i=1;i<=128;i++) {
            final int expected = 1<<((int)Math.floor(Math.log(i)/Math.log(2)));
            assertEquals(expected, NumbersUtils.floorPowerOfTwo(i));
        }
        
        for (int k=0;k<=62;k++) {
            final long pot = (1L<<k);
            if (k == 0) {
                // underflow
            } else {
                assertEquals(pot/2, NumbersUtils.floorPowerOfTwo(pot-1));
            }
            assertEquals(pot, NumbersUtils.floorPowerOfTwo(pot));
            if (k == 0) {
                // floorPowerOfTwo(2) is not 1, but still 2
                assertEquals(pot+1, NumbersUtils.floorPowerOfTwo(pot+1));
            } else {
                assertEquals(pot, NumbersUtils.floorPowerOfTwo(pot+1));
            }
        }
    }

    public void test_ceilingPowerOfTwo_int() {
        for (int i : new int[]{
                Integer.MIN_VALUE,
                Integer.MIN_VALUE+1,
                -1,
                (1<<30)+1,
                Integer.MAX_VALUE-1,
                Integer.MAX_VALUE}) {
            try {
                NumbersUtils.ceilingPowerOfTwo(i);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        for (int i=0;i<=128;i++) {
            final int expected = (i == 0) ? 1 : 1<<((int)Math.ceil(Math.log(i)/Math.log(2)));
            assertEquals(expected, NumbersUtils.ceilingPowerOfTwo(i));
        }
        
        for (int k=0;k<=30;k++) {
            final int pot = (1<<k);
            if (k == 1) {
                // ceilingPowerOfTwo(1) is not 2, but still 1
            } else {
                assertEquals(pot, NumbersUtils.ceilingPowerOfTwo(pot-1));
            }
            assertEquals(pot, NumbersUtils.ceilingPowerOfTwo(pot));
            if (k == 30) {
                // overflow
            } else {
                assertEquals(2*pot, NumbersUtils.ceilingPowerOfTwo(pot+1));
            }
        }
    }
    
    public void test_ceilingPowerOfTwo_long() {
        for (long i : new long[]{
                Long.MIN_VALUE,
                Long.MIN_VALUE+1,
                -1,
                (1L<<62)+1,
                Long.MAX_VALUE-1,
                Long.MAX_VALUE}) {
            try {
                NumbersUtils.ceilingPowerOfTwo(i);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        for (long i=0;i<=128;i++) {
            final long expected = (i == 0) ? 1 : 1L<<((int)Math.ceil(Math.log(i)/Math.log(2)));
            assertEquals(expected, NumbersUtils.ceilingPowerOfTwo(i));
        }
        
        for (int k=0;k<=62;k++) {
            final long pot = (1L<<k);
            if (k == 1) {
                // ceilingPowerOfTwo(1) is not 2, but still 1
            } else {
                assertEquals(pot, NumbersUtils.ceilingPowerOfTwo(pot-1));
            }
            assertEquals(pot, NumbersUtils.ceilingPowerOfTwo(pot));
            if (k == 62) {
                // overflow
            } else {
                assertEquals(2*pot, NumbersUtils.ceilingPowerOfTwo(pot+1));
            }
        }
    }
    
    public void test_meanLow_2int() {
        for (int a=-100;a<=100;a++) {
            for (int b=-100;b<=100;b++) {
                int expected = (int)Math.floor((a+b)/2.0);
                if (false) {
                    // Expected value can also be computed like this
                    // (only integer arithmetic).
                    if (((a|b) < 0) && NumbersUtils.isOdd(a+b)) {
                        // if a+b is odd (inexact),
                        // and one is negative (rounded to the lower)
                        expected = (a+b-1)/2;
                    } else {
                        // if a+b is even (exact),
                        // or both >= 0 (rounded to the lower)
                        expected = (a+b)/2;
                    }
                }
                assertEquals(expected, NumbersUtils.meanLow(a, b));
            }
        }

        assertEquals(Integer.MIN_VALUE, NumbersUtils.meanLow(Integer.MIN_VALUE, Integer.MIN_VALUE)); // exact
        assertEquals(Integer.MIN_VALUE, NumbersUtils.meanLow(Integer.MIN_VALUE, Integer.MIN_VALUE+1)); // rounded-
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.meanLow(Integer.MIN_VALUE, Integer.MIN_VALUE+2)); // exact
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.meanLow(Integer.MIN_VALUE, Integer.MIN_VALUE+3)); // rounded-

        assertEquals(-3, NumbersUtils.meanLow(Integer.MIN_VALUE, Integer.MAX_VALUE-4)); // rounded-
        assertEquals(-2, NumbersUtils.meanLow(Integer.MIN_VALUE, Integer.MAX_VALUE-3)); // exact
        assertEquals(-2, NumbersUtils.meanLow(Integer.MIN_VALUE, Integer.MAX_VALUE-2)); // rounded-
        assertEquals(-1, NumbersUtils.meanLow(Integer.MIN_VALUE, Integer.MAX_VALUE-1)); // exact
        assertEquals(-1, NumbersUtils.meanLow(Integer.MIN_VALUE, Integer.MAX_VALUE)); // rounded-
        assertEquals(0, NumbersUtils.meanLow(Integer.MIN_VALUE+1, Integer.MAX_VALUE)); // exact
        assertEquals(0, NumbersUtils.meanLow(Integer.MIN_VALUE+2, Integer.MAX_VALUE)); // rounded-
        assertEquals(1, NumbersUtils.meanLow(Integer.MIN_VALUE+3, Integer.MAX_VALUE)); // exact
        assertEquals(1, NumbersUtils.meanLow(Integer.MIN_VALUE+4, Integer.MAX_VALUE)); // rounded-
        assertEquals(2, NumbersUtils.meanLow(Integer.MIN_VALUE+5, Integer.MAX_VALUE)); // exact
        assertEquals(2, NumbersUtils.meanLow(Integer.MIN_VALUE+6, Integer.MAX_VALUE)); // rounded-

        assertEquals(Integer.MAX_VALUE, NumbersUtils.meanLow(Integer.MAX_VALUE, Integer.MAX_VALUE)); // exact
        assertEquals(Integer.MAX_VALUE-1, NumbersUtils.meanLow(Integer.MAX_VALUE-1, Integer.MAX_VALUE)); // rounded-
        assertEquals(Integer.MAX_VALUE-1, NumbersUtils.meanLow(Integer.MAX_VALUE-2, Integer.MAX_VALUE)); // exact
        assertEquals(Integer.MAX_VALUE-2, NumbersUtils.meanLow(Integer.MAX_VALUE-3, Integer.MAX_VALUE)); // rounded-
    }

    public void test_meanLow_2long() {
        for (long a=-100;a<=100;a++) {
            for (long b=-100;b<=100;b++) {
                long expected = (long)Math.floor((a+b)/2.0);
                assertEquals(expected, NumbersUtils.meanLow(a, b));
            }
        }

        assertEquals(Long.MIN_VALUE, NumbersUtils.meanLow(Long.MIN_VALUE, Long.MIN_VALUE)); // exact
        assertEquals(Long.MIN_VALUE, NumbersUtils.meanLow(Long.MIN_VALUE, Long.MIN_VALUE+1)); // rounded-
        assertEquals(Long.MIN_VALUE+1, NumbersUtils.meanLow(Long.MIN_VALUE, Long.MIN_VALUE+2)); // exact
        assertEquals(Long.MIN_VALUE+1, NumbersUtils.meanLow(Long.MIN_VALUE, Long.MIN_VALUE+3)); // rounded-

        assertEquals(-3L, NumbersUtils.meanLow(Long.MIN_VALUE, Long.MAX_VALUE-4)); // rounded-
        assertEquals(-2L, NumbersUtils.meanLow(Long.MIN_VALUE, Long.MAX_VALUE-3)); // exact
        assertEquals(-2L, NumbersUtils.meanLow(Long.MIN_VALUE, Long.MAX_VALUE-2)); // rounded-
        assertEquals(-1L, NumbersUtils.meanLow(Long.MIN_VALUE, Long.MAX_VALUE-1)); // exact
        assertEquals(-1L, NumbersUtils.meanLow(Long.MIN_VALUE, Long.MAX_VALUE)); // rounded-
        assertEquals(0L, NumbersUtils.meanLow(Long.MIN_VALUE+1, Long.MAX_VALUE)); // exact
        assertEquals(0L, NumbersUtils.meanLow(Long.MIN_VALUE+2, Long.MAX_VALUE)); // rounded-
        assertEquals(1L, NumbersUtils.meanLow(Long.MIN_VALUE+3, Long.MAX_VALUE)); // exact
        assertEquals(1L, NumbersUtils.meanLow(Long.MIN_VALUE+4, Long.MAX_VALUE)); // rounded-
        assertEquals(2L, NumbersUtils.meanLow(Long.MIN_VALUE+5, Long.MAX_VALUE)); // exact
        assertEquals(2L, NumbersUtils.meanLow(Long.MIN_VALUE+6, Long.MAX_VALUE)); // rounded-

        assertEquals(Long.MAX_VALUE, NumbersUtils.meanLow(Long.MAX_VALUE, Long.MAX_VALUE)); // exact
        assertEquals(Long.MAX_VALUE-1, NumbersUtils.meanLow(Long.MAX_VALUE-1, Long.MAX_VALUE)); // rounded-
        assertEquals(Long.MAX_VALUE-1, NumbersUtils.meanLow(Long.MAX_VALUE-2, Long.MAX_VALUE)); // exact
        assertEquals(Long.MAX_VALUE-2, NumbersUtils.meanLow(Long.MAX_VALUE-3, Long.MAX_VALUE)); // rounded-
    }

    public void test_meanSml_2int() {
        for (int a=-100;a<=100;a++) {
            for (int b=-100;b<=100;b++) {
                int expected = (a+b)/2;
                assertEquals(expected, NumbersUtils.meanSml(a, b));
            }
        }

        assertEquals(Integer.MIN_VALUE, NumbersUtils.meanSml(Integer.MIN_VALUE, Integer.MIN_VALUE)); // exact
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.meanSml(Integer.MIN_VALUE, Integer.MIN_VALUE+1)); // rounded+
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.meanSml(Integer.MIN_VALUE, Integer.MIN_VALUE+2)); // exact
        assertEquals(Integer.MIN_VALUE+2, NumbersUtils.meanSml(Integer.MIN_VALUE, Integer.MIN_VALUE+3)); // rounded+

        assertEquals(-2, NumbersUtils.meanSml(Integer.MIN_VALUE, Integer.MAX_VALUE-4)); // rounded+
        assertEquals(-2, NumbersUtils.meanSml(Integer.MIN_VALUE, Integer.MAX_VALUE-3)); // exact
        assertEquals(-1, NumbersUtils.meanSml(Integer.MIN_VALUE, Integer.MAX_VALUE-2)); // rounded+
        assertEquals(-1, NumbersUtils.meanSml(Integer.MIN_VALUE, Integer.MAX_VALUE-1)); // exact
        assertEquals(0, NumbersUtils.meanSml(Integer.MIN_VALUE, Integer.MAX_VALUE)); // rounded+
        assertEquals(0, NumbersUtils.meanSml(Integer.MIN_VALUE+1, Integer.MAX_VALUE)); // exact
        assertEquals(0, NumbersUtils.meanSml(Integer.MIN_VALUE+2, Integer.MAX_VALUE)); // rounded-
        assertEquals(1, NumbersUtils.meanSml(Integer.MIN_VALUE+3, Integer.MAX_VALUE)); // exact
        assertEquals(1, NumbersUtils.meanSml(Integer.MIN_VALUE+4, Integer.MAX_VALUE)); // rounded-
        assertEquals(2, NumbersUtils.meanSml(Integer.MIN_VALUE+5, Integer.MAX_VALUE)); // exact
        assertEquals(2, NumbersUtils.meanSml(Integer.MIN_VALUE+6, Integer.MAX_VALUE)); // rounded-

        assertEquals(Integer.MAX_VALUE, NumbersUtils.meanSml(Integer.MAX_VALUE, Integer.MAX_VALUE)); // exact
        assertEquals(Integer.MAX_VALUE-1, NumbersUtils.meanSml(Integer.MAX_VALUE-1, Integer.MAX_VALUE)); // rounded-
        assertEquals(Integer.MAX_VALUE-1, NumbersUtils.meanSml(Integer.MAX_VALUE-2, Integer.MAX_VALUE)); // exact
        assertEquals(Integer.MAX_VALUE-2, NumbersUtils.meanSml(Integer.MAX_VALUE-3, Integer.MAX_VALUE)); // rounded-
    }
    
    public void test_meanSml_2long() {
        for (long a=-100;a<=100;a++) {
            for (long b=-100;b<=100;b++) {
                long expected = (a+b)/2;
                assertEquals(expected, NumbersUtils.meanSml(a, b));
            }
        }

        assertEquals(Long.MIN_VALUE, NumbersUtils.meanSml(Long.MIN_VALUE, Long.MIN_VALUE)); // exact
        assertEquals(Long.MIN_VALUE+1, NumbersUtils.meanSml(Long.MIN_VALUE, Long.MIN_VALUE+1)); // rounded+
        assertEquals(Long.MIN_VALUE+1, NumbersUtils.meanSml(Long.MIN_VALUE, Long.MIN_VALUE+2)); // exact
        assertEquals(Long.MIN_VALUE+2, NumbersUtils.meanSml(Long.MIN_VALUE, Long.MIN_VALUE+3)); // rounded+

        assertEquals(-2L, NumbersUtils.meanSml(Long.MIN_VALUE, Long.MAX_VALUE-4)); // rounded+
        assertEquals(-2L, NumbersUtils.meanSml(Long.MIN_VALUE, Long.MAX_VALUE-3)); // exact
        assertEquals(-1L, NumbersUtils.meanSml(Long.MIN_VALUE, Long.MAX_VALUE-2)); // rounded+
        assertEquals(-1L, NumbersUtils.meanSml(Long.MIN_VALUE, Long.MAX_VALUE-1)); // exact
        assertEquals(0L, NumbersUtils.meanSml(Long.MIN_VALUE, Long.MAX_VALUE)); // rounded+
        assertEquals(0L, NumbersUtils.meanSml(Long.MIN_VALUE+1, Long.MAX_VALUE)); // exact
        assertEquals(0L, NumbersUtils.meanSml(Long.MIN_VALUE+2, Long.MAX_VALUE)); // rounded-
        assertEquals(1L, NumbersUtils.meanSml(Long.MIN_VALUE+3, Long.MAX_VALUE)); // exact
        assertEquals(1L, NumbersUtils.meanSml(Long.MIN_VALUE+4, Long.MAX_VALUE)); // rounded-
        assertEquals(2L, NumbersUtils.meanSml(Long.MIN_VALUE+5, Long.MAX_VALUE)); // exact
        assertEquals(2L, NumbersUtils.meanSml(Long.MIN_VALUE+6, Long.MAX_VALUE)); // rounded-

        assertEquals(Long.MAX_VALUE, NumbersUtils.meanSml(Long.MAX_VALUE, Long.MAX_VALUE)); // exact
        assertEquals(Long.MAX_VALUE-1, NumbersUtils.meanSml(Long.MAX_VALUE-1, Long.MAX_VALUE)); // rounded-
        assertEquals(Long.MAX_VALUE-1, NumbersUtils.meanSml(Long.MAX_VALUE-2, Long.MAX_VALUE)); // exact
        assertEquals(Long.MAX_VALUE-2, NumbersUtils.meanSml(Long.MAX_VALUE-3, Long.MAX_VALUE)); // rounded-
    }

    public void test_negHalfWidth_2int() {
        // min > max
        try {
            NumbersUtils.negHalfWidth(2, 1);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }
        
        for (int min=-100;min<=100;min++) {
            for (int max=min;max<=100;max++) {
                int expected = -((max-min+1)/2);
                assertEquals(expected, NumbersUtils.negHalfWidth(min, max));
            }
        }

        assertEquals(0, NumbersUtils.negHalfWidth(Integer.MIN_VALUE, Integer.MIN_VALUE)); // rounded
        assertEquals(-1, NumbersUtils.negHalfWidth(Integer.MIN_VALUE, Integer.MIN_VALUE+1)); // exact
        assertEquals(-1, NumbersUtils.negHalfWidth(Integer.MIN_VALUE, Integer.MIN_VALUE+2)); // rounded
        assertEquals(-2, NumbersUtils.negHalfWidth(Integer.MIN_VALUE, Integer.MIN_VALUE+3)); // exact
        
        assertEquals(Integer.MIN_VALUE+2, NumbersUtils.negHalfWidth(Integer.MIN_VALUE, Integer.MAX_VALUE-3)); // rounded
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.negHalfWidth(Integer.MIN_VALUE, Integer.MAX_VALUE-2)); // exact
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.negHalfWidth(Integer.MIN_VALUE, Integer.MAX_VALUE-1)); // rounded
        assertEquals(Integer.MIN_VALUE, NumbersUtils.negHalfWidth(Integer.MIN_VALUE, Integer.MAX_VALUE)); // exact
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.negHalfWidth(Integer.MIN_VALUE+1, Integer.MAX_VALUE)); // rounded
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.negHalfWidth(Integer.MIN_VALUE+2, Integer.MAX_VALUE)); // exact
        assertEquals(Integer.MIN_VALUE+2, NumbersUtils.negHalfWidth(Integer.MIN_VALUE+3, Integer.MAX_VALUE)); // rounded

        assertEquals(0, NumbersUtils.negHalfWidth(Integer.MAX_VALUE, Integer.MAX_VALUE)); // rounded
        assertEquals(-1, NumbersUtils.negHalfWidth(Integer.MAX_VALUE-1, Integer.MAX_VALUE)); // exact
        assertEquals(-1, NumbersUtils.negHalfWidth(Integer.MAX_VALUE-2, Integer.MAX_VALUE)); // rounded
        assertEquals(-2, NumbersUtils.negHalfWidth(Integer.MAX_VALUE-3, Integer.MAX_VALUE)); // exact
    }

    public void test_negHalfWidth_2long() {
        // min > max
        try {
            NumbersUtils.negHalfWidth(2L, 1L);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // ok
        }
        
        for (long min=-100;min<=100;min++) {
            for (long max=min;max<=100;max++) {
                long expected = -((max-min+1)/2);
                assertEquals(expected, NumbersUtils.negHalfWidth(min, max));
            }
        }

        assertEquals(0L, NumbersUtils.negHalfWidth(Long.MIN_VALUE, Long.MIN_VALUE)); // rounded
        assertEquals(-1L, NumbersUtils.negHalfWidth(Long.MIN_VALUE, Long.MIN_VALUE+1)); // exact
        assertEquals(-1L, NumbersUtils.negHalfWidth(Long.MIN_VALUE, Long.MIN_VALUE+2)); // rounded
        assertEquals(-2L, NumbersUtils.negHalfWidth(Long.MIN_VALUE, Long.MIN_VALUE+3)); // exact
        
        assertEquals(Long.MIN_VALUE+2, NumbersUtils.negHalfWidth(Long.MIN_VALUE, Long.MAX_VALUE-3)); // rounded
        assertEquals(Long.MIN_VALUE+1, NumbersUtils.negHalfWidth(Long.MIN_VALUE, Long.MAX_VALUE-2)); // exact
        assertEquals(Long.MIN_VALUE+1, NumbersUtils.negHalfWidth(Long.MIN_VALUE, Long.MAX_VALUE-1)); // rounded
        assertEquals(Long.MIN_VALUE, NumbersUtils.negHalfWidth(Long.MIN_VALUE, Long.MAX_VALUE)); // exact
        assertEquals(Long.MIN_VALUE+1, NumbersUtils.negHalfWidth(Long.MIN_VALUE+1, Long.MAX_VALUE)); // rounded
        assertEquals(Long.MIN_VALUE+1, NumbersUtils.negHalfWidth(Long.MIN_VALUE+2, Long.MAX_VALUE)); // exact
        assertEquals(Long.MIN_VALUE+2, NumbersUtils.negHalfWidth(Long.MIN_VALUE+3, Long.MAX_VALUE)); // rounded

        assertEquals(0L, NumbersUtils.negHalfWidth(Long.MAX_VALUE, Long.MAX_VALUE)); // rounded
        assertEquals(-1L, NumbersUtils.negHalfWidth(Long.MAX_VALUE-1, Long.MAX_VALUE)); // exact
        assertEquals(-1L, NumbersUtils.negHalfWidth(Long.MAX_VALUE-2, Long.MAX_VALUE)); // rounded
        assertEquals(-2L, NumbersUtils.negHalfWidth(Long.MAX_VALUE-3, Long.MAX_VALUE)); // exact
    }
    
    public void test_moduloSignedPowerOfTwo_2int() {
        for (int opSign=-1;opSign<=1;opSign+=2) {
            for (int p=0;p<=31;p++) {
                int npot = Integer.MIN_VALUE>>(31-p);
                // sign has no effect if npot is min possible value
                int spot = opSign * npot;
                for (int a=-100;a<=100;a++) {
                    int expected = a % spot;
                    assertEquals(expected, NumbersUtils.moduloSignedPowerOfTwo(a, spot));
                }
            }
        }
        
        final int p30 = (1<<30);
        final int np31 = Integer.MIN_VALUE;
        final int p31m1 = Integer.MAX_VALUE;
        
        ArrayList<Integer> sp30s = new ArrayList<Integer>();
        sp30s.add(p30);
        sp30s.add(-p30);
        
        /*
         * reasonable powers
         */
        
        for (Integer sp30 : sp30s) {
            assertEquals(0, NumbersUtils.moduloSignedPowerOfTwo(np31, sp30));
            assertEquals(-p30+1, NumbersUtils.moduloSignedPowerOfTwo(np31+1, sp30));
            assertEquals(-p30+2, NumbersUtils.moduloSignedPowerOfTwo(np31+2, sp30));
            assertEquals(-p30+3, NumbersUtils.moduloSignedPowerOfTwo(np31+3, sp30));
            assertEquals(-3, NumbersUtils.moduloSignedPowerOfTwo(-p30-3, sp30));
            assertEquals(-2, NumbersUtils.moduloSignedPowerOfTwo(-p30-2, sp30));
            assertEquals(-1, NumbersUtils.moduloSignedPowerOfTwo(-p30-1, sp30));
            //
            assertEquals(0, NumbersUtils.moduloSignedPowerOfTwo(-p30, sp30));
            //
            assertEquals(-p30+1, NumbersUtils.moduloSignedPowerOfTwo(-p30+1, sp30));
            assertEquals(-p30+2, NumbersUtils.moduloSignedPowerOfTwo(-p30+2, sp30));
            assertEquals(-p30+3, NumbersUtils.moduloSignedPowerOfTwo(-p30+3, sp30));
            assertEquals(-3, NumbersUtils.moduloSignedPowerOfTwo(-3, sp30));
            assertEquals(-2, NumbersUtils.moduloSignedPowerOfTwo(-2, sp30));
            assertEquals(-1, NumbersUtils.moduloSignedPowerOfTwo(-1, sp30));
            //
            assertEquals(0, NumbersUtils.moduloSignedPowerOfTwo(0, sp30));
            //
            assertEquals(1, NumbersUtils.moduloSignedPowerOfTwo(1, sp30));
            assertEquals(2, NumbersUtils.moduloSignedPowerOfTwo(2, sp30));
            assertEquals(3, NumbersUtils.moduloSignedPowerOfTwo(3, sp30));
            assertEquals(p30-3, NumbersUtils.moduloSignedPowerOfTwo(p30-3, sp30));
            assertEquals(p30-2, NumbersUtils.moduloSignedPowerOfTwo(p30-2, sp30));
            assertEquals(p30-1, NumbersUtils.moduloSignedPowerOfTwo(p30-1, sp30));
            //
            assertEquals(0, NumbersUtils.moduloSignedPowerOfTwo(p30, sp30));
            //
            assertEquals(1, NumbersUtils.moduloSignedPowerOfTwo(p30+1, sp30));
            assertEquals(2, NumbersUtils.moduloSignedPowerOfTwo(p30+2, sp30));
            assertEquals(3, NumbersUtils.moduloSignedPowerOfTwo(p30+3, sp30));
            assertEquals(p30-3, NumbersUtils.moduloSignedPowerOfTwo(p31m1-2, sp30));
            assertEquals(p30-2, NumbersUtils.moduloSignedPowerOfTwo(p31m1-1, sp30));
            assertEquals(p30-1, NumbersUtils.moduloSignedPowerOfTwo(p31m1, sp30));
        }
        
        /*
         * Integer.MIN_VALUE power
         * (value never changed, except if Integer.MIN_VALUE itself, in which case it gives 0)
         */
        
        assertEquals(0, NumbersUtils.moduloSignedPowerOfTwo(np31, np31));
        assertEquals(np31+1, NumbersUtils.moduloSignedPowerOfTwo(np31+1, np31));
        assertEquals(np31+2, NumbersUtils.moduloSignedPowerOfTwo(np31+2, np31));
        assertEquals(np31+3, NumbersUtils.moduloSignedPowerOfTwo(np31+3, np31));
        assertEquals(-p30-3, NumbersUtils.moduloSignedPowerOfTwo(-p30-3, np31));
        assertEquals(-p30-2, NumbersUtils.moduloSignedPowerOfTwo(-p30-2, np31));
        assertEquals(-p30-1, NumbersUtils.moduloSignedPowerOfTwo(-p30-1, np31));
        //
        assertEquals(-p30, NumbersUtils.moduloSignedPowerOfTwo(-p30, np31));
        //
        assertEquals(-p30+1, NumbersUtils.moduloSignedPowerOfTwo(-p30+1, np31));
        assertEquals(-p30+2, NumbersUtils.moduloSignedPowerOfTwo(-p30+2, np31));
        assertEquals(-p30+3, NumbersUtils.moduloSignedPowerOfTwo(-p30+3, np31));
        assertEquals(-3, NumbersUtils.moduloSignedPowerOfTwo(-3, np31));
        assertEquals(-2, NumbersUtils.moduloSignedPowerOfTwo(-2, np31));
        assertEquals(-1, NumbersUtils.moduloSignedPowerOfTwo(-1, np31));
        //
        assertEquals(0, NumbersUtils.moduloSignedPowerOfTwo(0, np31));
        //
        assertEquals(1, NumbersUtils.moduloSignedPowerOfTwo(1, np31));
        assertEquals(2, NumbersUtils.moduloSignedPowerOfTwo(2, np31));
        assertEquals(3, NumbersUtils.moduloSignedPowerOfTwo(3, np31));
        assertEquals(p30-3, NumbersUtils.moduloSignedPowerOfTwo(p30-3, np31));
        assertEquals(p30-2, NumbersUtils.moduloSignedPowerOfTwo(p30-2, np31));
        assertEquals(p30-1, NumbersUtils.moduloSignedPowerOfTwo(p30-1, np31));
        //
        assertEquals(p30, NumbersUtils.moduloSignedPowerOfTwo(p30, np31));
        //
        assertEquals(p30+1, NumbersUtils.moduloSignedPowerOfTwo(p30+1, np31));
        assertEquals(p30+2, NumbersUtils.moduloSignedPowerOfTwo(p30+2, np31));
        assertEquals(p30+3, NumbersUtils.moduloSignedPowerOfTwo(p30+3, np31));
        assertEquals(p31m1-2, NumbersUtils.moduloSignedPowerOfTwo(p31m1-2, np31));
        assertEquals(p31m1-1, NumbersUtils.moduloSignedPowerOfTwo(p31m1-1, np31));
        assertEquals(p31m1, NumbersUtils.moduloSignedPowerOfTwo(p31m1, np31));
    }
    
    public void test_moduloSignedPowerOfTwo_2long() {
        for (int opSign=-1;opSign<=1;opSign+=2) {
            for (long p=0;p<=63;p++) {
                long npot = Long.MIN_VALUE>>(63-p);
                // sign has no effect if npot is min possible value
                long spot = opSign * npot;
                for (long a=-100;a<=100;a++) {
                    long expected = a % spot;
                    assertEquals(expected, NumbersUtils.moduloSignedPowerOfTwo(a, spot));
                }
            }
        }
        
        final long p62 = (1L<<62);
        final long np63 = Long.MIN_VALUE;
        final long p63m1 = Long.MAX_VALUE;
        
        ArrayList<Long> sp62s = new ArrayList<Long>();
        sp62s.add(p62);
        sp62s.add(-p62);
        
        /*
         * reasonable powers
         */
        
        for (Long sp62 : sp62s) {
            assertEquals(0, NumbersUtils.moduloSignedPowerOfTwo(np63, sp62));
            assertEquals(-p62+1, NumbersUtils.moduloSignedPowerOfTwo(np63+1, sp62));
            assertEquals(-p62+2, NumbersUtils.moduloSignedPowerOfTwo(np63+2, sp62));
            assertEquals(-p62+3, NumbersUtils.moduloSignedPowerOfTwo(np63+3, sp62));
            assertEquals(-3, NumbersUtils.moduloSignedPowerOfTwo(-p62-3, sp62));
            assertEquals(-2, NumbersUtils.moduloSignedPowerOfTwo(-p62-2, sp62));
            assertEquals(-1, NumbersUtils.moduloSignedPowerOfTwo(-p62-1, sp62));
            //
            assertEquals(0, NumbersUtils.moduloSignedPowerOfTwo(-p62, sp62));
            //
            assertEquals(-p62+1, NumbersUtils.moduloSignedPowerOfTwo(-p62+1, sp62));
            assertEquals(-p62+2, NumbersUtils.moduloSignedPowerOfTwo(-p62+2, sp62));
            assertEquals(-p62+3, NumbersUtils.moduloSignedPowerOfTwo(-p62+3, sp62));
            assertEquals(-3, NumbersUtils.moduloSignedPowerOfTwo(-3, sp62));
            assertEquals(-2, NumbersUtils.moduloSignedPowerOfTwo(-2, sp62));
            assertEquals(-1, NumbersUtils.moduloSignedPowerOfTwo(-1, sp62));
            //
            assertEquals(0, NumbersUtils.moduloSignedPowerOfTwo(0, sp62));
            //
            assertEquals(1, NumbersUtils.moduloSignedPowerOfTwo(1, sp62));
            assertEquals(2, NumbersUtils.moduloSignedPowerOfTwo(2, sp62));
            assertEquals(3, NumbersUtils.moduloSignedPowerOfTwo(3, sp62));
            assertEquals(p62-3, NumbersUtils.moduloSignedPowerOfTwo(p62-3, sp62));
            assertEquals(p62-2, NumbersUtils.moduloSignedPowerOfTwo(p62-2, sp62));
            assertEquals(p62-1, NumbersUtils.moduloSignedPowerOfTwo(p62-1, sp62));
            //
            assertEquals(0, NumbersUtils.moduloSignedPowerOfTwo(p62, sp62));
            //
            assertEquals(1, NumbersUtils.moduloSignedPowerOfTwo(p62+1, sp62));
            assertEquals(2, NumbersUtils.moduloSignedPowerOfTwo(p62+2, sp62));
            assertEquals(3, NumbersUtils.moduloSignedPowerOfTwo(p62+3, sp62));
            assertEquals(p62-3, NumbersUtils.moduloSignedPowerOfTwo(p63m1-2, sp62));
            assertEquals(p62-2, NumbersUtils.moduloSignedPowerOfTwo(p63m1-1, sp62));
            assertEquals(p62-1, NumbersUtils.moduloSignedPowerOfTwo(p63m1, sp62));
        }
        
        /*
         * Long.MIN_VALUE power
         * (value never changed, except if Long.MIN_VALUE itself, in which case it gives 0)
         */
        
        assertEquals(0, NumbersUtils.moduloSignedPowerOfTwo(np63, np63));
        assertEquals(np63+1, NumbersUtils.moduloSignedPowerOfTwo(np63+1, np63));
        assertEquals(np63+2, NumbersUtils.moduloSignedPowerOfTwo(np63+2, np63));
        assertEquals(np63+3, NumbersUtils.moduloSignedPowerOfTwo(np63+3, np63));
        assertEquals(-p62-3, NumbersUtils.moduloSignedPowerOfTwo(-p62-3, np63));
        assertEquals(-p62-2, NumbersUtils.moduloSignedPowerOfTwo(-p62-2, np63));
        assertEquals(-p62-1, NumbersUtils.moduloSignedPowerOfTwo(-p62-1, np63));
        //
        assertEquals(-p62, NumbersUtils.moduloSignedPowerOfTwo(-p62, np63));
        //
        assertEquals(-p62+1, NumbersUtils.moduloSignedPowerOfTwo(-p62+1, np63));
        assertEquals(-p62+2, NumbersUtils.moduloSignedPowerOfTwo(-p62+2, np63));
        assertEquals(-p62+3, NumbersUtils.moduloSignedPowerOfTwo(-p62+3, np63));
        assertEquals(-3, NumbersUtils.moduloSignedPowerOfTwo(-3, np63));
        assertEquals(-2, NumbersUtils.moduloSignedPowerOfTwo(-2, np63));
        assertEquals(-1, NumbersUtils.moduloSignedPowerOfTwo(-1, np63));
        //
        assertEquals(0, NumbersUtils.moduloSignedPowerOfTwo(0, np63));
        //
        assertEquals(1, NumbersUtils.moduloSignedPowerOfTwo(1, np63));
        assertEquals(2, NumbersUtils.moduloSignedPowerOfTwo(2, np63));
        assertEquals(3, NumbersUtils.moduloSignedPowerOfTwo(3, np63));
        assertEquals(p62-3, NumbersUtils.moduloSignedPowerOfTwo(p62-3, np63));
        assertEquals(p62-2, NumbersUtils.moduloSignedPowerOfTwo(p62-2, np63));
        assertEquals(p62-1, NumbersUtils.moduloSignedPowerOfTwo(p62-1, np63));
        //
        assertEquals(p62, NumbersUtils.moduloSignedPowerOfTwo(p62, np63));
        //
        assertEquals(p62+1, NumbersUtils.moduloSignedPowerOfTwo(p62+1, np63));
        assertEquals(p62+2, NumbersUtils.moduloSignedPowerOfTwo(p62+2, np63));
        assertEquals(p62+3, NumbersUtils.moduloSignedPowerOfTwo(p62+3, np63));
        assertEquals(p63m1-2, NumbersUtils.moduloSignedPowerOfTwo(p63m1-2, np63));
        assertEquals(p63m1-1, NumbersUtils.moduloSignedPowerOfTwo(p63m1-1, np63));
        assertEquals(p63m1, NumbersUtils.moduloSignedPowerOfTwo(p63m1, np63));
    }

    public void test_log2_int() {
        for (int value : new int[]{Integer.MIN_VALUE,0}) {
            try {
                NumbersUtils.log2(value);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
        
        for (int p=0;p<=30;p++) {
            int pot = (1<<p);
            
            if (p != 0) {
                assertEquals(p-1, NumbersUtils.log2(pot-1));
            }
            assertEquals(p, NumbersUtils.log2(pot));
            assertEquals(p, NumbersUtils.log2(pot+pot-1));
            if (p != 30) {
                assertEquals(p+1, NumbersUtils.log2(pot+pot));
            }
        }
    }
    
    public void test_log2_long() {
        for (long value : new long[]{Long.MIN_VALUE,0}) {
            try {
                NumbersUtils.log2(value);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        for (int p=0;p<=62;p++) {
            long pot = (1L<<p);
            
            if (p != 0) {
                assertEquals(p-1, NumbersUtils.log2(pot-1));
            }
            assertEquals(p, NumbersUtils.log2(pot));
            assertEquals(p, NumbersUtils.log2(pot+pot-1));
            if (p != 62) {
                assertEquals(p+1, NumbersUtils.log2(pot+pot));
            }
        }
    }

    public void test_abs_int() {
        for (int value : new int[]{
                Integer.MIN_VALUE,
                Integer.MIN_VALUE+1,
                -1,
                0,
                1,
                Integer.MAX_VALUE-1,
                Integer.MAX_VALUE
        }) {
            assertEquals(Math.abs(value), NumbersUtils.abs(value));
        }
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final int value = this.utils.randomIntWhatever();
            assertEquals(Math.abs(value), NumbersUtils.abs(value));
        }
    }

    public void test_abs_long() {
        for (long value : new long[]{
                Long.MIN_VALUE,
                Long.MIN_VALUE+1,
                -1L,
                0L,
                1L,
                Long.MAX_VALUE-1,
                Long.MAX_VALUE
        }) {
            assertEquals(Math.abs(value), NumbersUtils.abs(value));
        }
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final long value = this.utils.randomLongWhatever();
            assertEquals(Math.abs(value), NumbersUtils.abs(value));
        }
    }

    public void test_absNeg_int() {
        for (int value : new int[]{
                Integer.MIN_VALUE,
                Integer.MIN_VALUE+1,
                -1,
                0,
                1,
                Integer.MAX_VALUE-1,
                Integer.MAX_VALUE
        }) {
            assertEquals(-Math.abs(value), NumbersUtils.absNeg(value));
        }
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final int value = this.utils.randomIntWhatever();
            assertEquals(-Math.abs(value), NumbersUtils.absNeg(value));
        }
    }

    public void test_absNeg_long() {
        for (long value : new long[]{
                Long.MIN_VALUE,
                Long.MIN_VALUE+1,
                -1L,
                0L,
                1L,
                Long.MAX_VALUE-1,
                Long.MAX_VALUE
        }) {
            assertEquals(-Math.abs(value), NumbersUtils.absNeg(value));
        }
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final long value = this.utils.randomLongWhatever();
            assertEquals(-Math.abs(value), NumbersUtils.absNeg(value));
        }
    }

    public void test_twoPow(int power) {
        for (int p=-1074;p<=1023;p++) {
            assertEquals(Math.pow(2.0,p), NumbersUtils.twoPow(p));
        }
        assertEquals(Double.NEGATIVE_INFINITY, NumbersUtils.twoPow(-1075));
        assertEquals(Double.POSITIVE_INFINITY, NumbersUtils.twoPow(1024));
    }

    public void test_intHash_long() {
        
        /*
         * preserving value if in int range
         */
        
        long[] valuesInIntRange = new long[]{
                Integer.MIN_VALUE, Integer.MAX_VALUE,
                Integer.MIN_VALUE+1, Integer.MAX_VALUE-1,
                Integer.MIN_VALUE+2, Integer.MAX_VALUE-2,
                -37,37,
                -4,4,
                -3,3,
                -2,2,
                -1,1,
                0};
        for (int i=0;i<valuesInIntRange.length;i++) {
            long value = valuesInIntRange[i];
            assertEquals(value, NumbersUtils.intHash(value));
        }
        
        /*
         * hashing if out of int range
         */
        
        assertNotSame("hash", Long.MIN_VALUE, NumbersUtils.intHash(Long.MIN_VALUE));
        
        assertNotSame("hash", Long.MAX_VALUE, NumbersUtils.intHash(Long.MAX_VALUE));
        assertNotSame("hash", -Long.MAX_VALUE, NumbersUtils.intHash(-Long.MAX_VALUE));
        
        assertNotSame("hash", Long.MAX_VALUE-1, NumbersUtils.intHash(Long.MAX_VALUE-1));
        assertNotSame("hash", -(Long.MAX_VALUE-1), NumbersUtils.intHash(-(Long.MAX_VALUE-1)));
    }
    
    public void test_asByte_int() {
        for (int value : new int[]{Integer.MIN_VALUE,Byte.MIN_VALUE-1,Byte.MAX_VALUE+1,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.asByte(value);
                assertTrue(false);
            } catch (ArithmeticException e) {
                // ok
            }
        }
        
        assertEquals(Byte.MIN_VALUE, NumbersUtils.asByte(Byte.MIN_VALUE));
        assertEquals(Byte.MIN_VALUE+1, NumbersUtils.asByte(Byte.MIN_VALUE+1));
        for (int a=-100;a<=100;a++) {
            assertEquals(a, NumbersUtils.asByte(a));
        }
        assertEquals(Byte.MAX_VALUE-1, NumbersUtils.asByte(Byte.MAX_VALUE-1));
        assertEquals(Byte.MAX_VALUE, NumbersUtils.asByte((int)Byte.MAX_VALUE));
    }
    
    public void test_asInt_long() {
        for (long value : new long[]{Long.MIN_VALUE,Integer.MIN_VALUE-1L,Integer.MAX_VALUE+1L,Long.MAX_VALUE}) {
            try {
                NumbersUtils.asInt(value);
                assertTrue(false);
            } catch (ArithmeticException e) {
                // ok
            }
        }
        
        assertEquals(Integer.MIN_VALUE, NumbersUtils.asInt((long)Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.asInt(Integer.MIN_VALUE+1L));
        for (long a=-100;a<=100;a++) {
            assertEquals(a, NumbersUtils.asInt(a));
        }
        assertEquals(Integer.MAX_VALUE-1, NumbersUtils.asInt(Integer.MAX_VALUE-1L));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.asInt((long)Integer.MAX_VALUE));
    }

    public void test_toInt_long() {
        assertEquals(Integer.MIN_VALUE, NumbersUtils.toInt(Long.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE, NumbersUtils.toInt(Integer.MIN_VALUE-1L));
        assertEquals(Integer.MIN_VALUE, NumbersUtils.toInt((long)Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.toInt(Integer.MIN_VALUE+1L));
        for (long a=-100;a<=100;a++) {
            assertEquals(a, NumbersUtils.toInt(a));
        }
        assertEquals(Integer.MAX_VALUE-1, NumbersUtils.toInt(Integer.MAX_VALUE-1L));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.toInt((long)Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.toInt(Integer.MAX_VALUE+1L));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.toInt(Long.MAX_VALUE));
    }

    public void test_plusExact_2int() {
        for (int b : new int[]{-1,Integer.MIN_VALUE}) {
            try {
                NumbersUtils.plusExact(Integer.MIN_VALUE,b);
                assertTrue(false);
            } catch (ArithmeticException e) {
                // ok
            }
        }
        for (int b : new int[]{1,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.plusExact(Integer.MAX_VALUE,b);
                assertTrue(false);
            } catch (ArithmeticException e) {
                // ok
            }
        }
        
        assertEquals(Integer.MIN_VALUE, NumbersUtils.plusExact(Integer.MIN_VALUE,0));
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.plusExact(Integer.MIN_VALUE,1));
        assertEquals(Integer.MIN_VALUE+2, NumbersUtils.plusExact(Integer.MIN_VALUE,2));
        assertEquals(Integer.MIN_VALUE+3, NumbersUtils.plusExact(Integer.MIN_VALUE,3));
        assertEquals(-4, NumbersUtils.plusExact(Integer.MIN_VALUE,Integer.MAX_VALUE-3));
        assertEquals(-3, NumbersUtils.plusExact(Integer.MIN_VALUE,Integer.MAX_VALUE-2));
        assertEquals(-2, NumbersUtils.plusExact(Integer.MIN_VALUE,Integer.MAX_VALUE-1));
        assertEquals(-1, NumbersUtils.plusExact(Integer.MIN_VALUE,Integer.MAX_VALUE));
        for (int a=-100;a<=100;a++) {
            for (int b=-100;b<=100;b++) {
                assertEquals(a+b, NumbersUtils.plusExact(a,b));
            }
        }
        assertEquals(-1, NumbersUtils.plusExact(Integer.MAX_VALUE,Integer.MIN_VALUE));
        assertEquals(0, NumbersUtils.plusExact(Integer.MAX_VALUE,Integer.MIN_VALUE+1));
        assertEquals(1, NumbersUtils.plusExact(Integer.MAX_VALUE,Integer.MIN_VALUE+2));
        assertEquals(2, NumbersUtils.plusExact(Integer.MAX_VALUE,Integer.MIN_VALUE+3));
        assertEquals(Integer.MAX_VALUE-3, NumbersUtils.plusExact(Integer.MAX_VALUE,-3));
        assertEquals(Integer.MAX_VALUE-2, NumbersUtils.plusExact(Integer.MAX_VALUE,-2));
        assertEquals(Integer.MAX_VALUE-1, NumbersUtils.plusExact(Integer.MAX_VALUE,-1));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.plusExact(Integer.MAX_VALUE,0));
    }

    public void test_plusExact_2long() {
        for (long b : new long[]{-1,Long.MIN_VALUE}) {
            try {
                NumbersUtils.plusExact(Long.MIN_VALUE,b);
                assertTrue(false);
            } catch (ArithmeticException e) {
                // ok
            }
        }
        for (long b : new long[]{1,Long.MAX_VALUE}) {
            try {
                NumbersUtils.plusExact(Long.MAX_VALUE,b);
                assertTrue(false);
            } catch (ArithmeticException e) {
                // ok
            }
        }
        
        assertEquals(Long.MIN_VALUE, NumbersUtils.plusExact(Long.MIN_VALUE,0));
        assertEquals(Long.MIN_VALUE+1, NumbersUtils.plusExact(Long.MIN_VALUE,1));
        assertEquals(Long.MIN_VALUE+2, NumbersUtils.plusExact(Long.MIN_VALUE,2));
        assertEquals(Long.MIN_VALUE+3, NumbersUtils.plusExact(Long.MIN_VALUE,3));
        assertEquals(-4, NumbersUtils.plusExact(Long.MIN_VALUE,Long.MAX_VALUE-3));
        assertEquals(-3, NumbersUtils.plusExact(Long.MIN_VALUE,Long.MAX_VALUE-2));
        assertEquals(-2, NumbersUtils.plusExact(Long.MIN_VALUE,Long.MAX_VALUE-1));
        assertEquals(-1, NumbersUtils.plusExact(Long.MIN_VALUE,Long.MAX_VALUE));
        for (long a=-100;a<=100;a++) {
            for (long b=-100;b<=100;b++) {
                assertEquals(a+b, NumbersUtils.plusExact(a,b));
            }
        }
        assertEquals(-1, NumbersUtils.plusExact(Long.MAX_VALUE,Long.MIN_VALUE));
        assertEquals(0, NumbersUtils.plusExact(Long.MAX_VALUE,Long.MIN_VALUE+1));
        assertEquals(1, NumbersUtils.plusExact(Long.MAX_VALUE,Long.MIN_VALUE+2));
        assertEquals(2, NumbersUtils.plusExact(Long.MAX_VALUE,Long.MIN_VALUE+3));
        assertEquals(Long.MAX_VALUE-3, NumbersUtils.plusExact(Long.MAX_VALUE,-3));
        assertEquals(Long.MAX_VALUE-2, NumbersUtils.plusExact(Long.MAX_VALUE,-2));
        assertEquals(Long.MAX_VALUE-1, NumbersUtils.plusExact(Long.MAX_VALUE,-1));
        assertEquals(Long.MAX_VALUE, NumbersUtils.plusExact(Long.MAX_VALUE,0));
    }

    public void test_plusBounded_2int() {
        assertEquals(Integer.MIN_VALUE, NumbersUtils.plusBounded(Integer.MIN_VALUE,Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE, NumbersUtils.plusBounded(Integer.MIN_VALUE,-1));
        //
        assertEquals(Integer.MIN_VALUE, NumbersUtils.plusBounded(Integer.MIN_VALUE,0));
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.plusBounded(Integer.MIN_VALUE,1));
        assertEquals(Integer.MIN_VALUE+2, NumbersUtils.plusBounded(Integer.MIN_VALUE,2));
        assertEquals(Integer.MIN_VALUE+3, NumbersUtils.plusBounded(Integer.MIN_VALUE,3));
        assertEquals(-4, NumbersUtils.plusBounded(Integer.MIN_VALUE,Integer.MAX_VALUE-3));
        assertEquals(-3, NumbersUtils.plusBounded(Integer.MIN_VALUE,Integer.MAX_VALUE-2));
        assertEquals(-2, NumbersUtils.plusBounded(Integer.MIN_VALUE,Integer.MAX_VALUE-1));
        assertEquals(-1, NumbersUtils.plusBounded(Integer.MIN_VALUE,Integer.MAX_VALUE));
        for (int a=-100;a<=100;a++) {
            for (int b=-100;b<=100;b++) {
                assertEquals(a+b, NumbersUtils.plusBounded(a,b));
            }
        }
        assertEquals(-1, NumbersUtils.plusBounded(Integer.MAX_VALUE,Integer.MIN_VALUE));
        assertEquals(0, NumbersUtils.plusBounded(Integer.MAX_VALUE,Integer.MIN_VALUE+1));
        assertEquals(1, NumbersUtils.plusBounded(Integer.MAX_VALUE,Integer.MIN_VALUE+2));
        assertEquals(2, NumbersUtils.plusBounded(Integer.MAX_VALUE,Integer.MIN_VALUE+3));
        assertEquals(Integer.MAX_VALUE-3, NumbersUtils.plusBounded(Integer.MAX_VALUE,-3));
        assertEquals(Integer.MAX_VALUE-2, NumbersUtils.plusBounded(Integer.MAX_VALUE,-2));
        assertEquals(Integer.MAX_VALUE-1, NumbersUtils.plusBounded(Integer.MAX_VALUE,-1));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.plusBounded(Integer.MAX_VALUE,0));
        //
        assertEquals(Integer.MAX_VALUE, NumbersUtils.plusBounded(Integer.MAX_VALUE,1));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.plusBounded(Integer.MAX_VALUE,Integer.MAX_VALUE));
    }

    public void test_plusBounded_2long() {
        assertEquals(Long.MIN_VALUE, NumbersUtils.plusBounded(Long.MIN_VALUE,Long.MIN_VALUE));
        assertEquals(Long.MIN_VALUE, NumbersUtils.plusBounded(Long.MIN_VALUE,-1));
        //
        assertEquals(Long.MIN_VALUE, NumbersUtils.plusBounded(Long.MIN_VALUE,0));
        assertEquals(Long.MIN_VALUE+1, NumbersUtils.plusBounded(Long.MIN_VALUE,1));
        assertEquals(Long.MIN_VALUE+2, NumbersUtils.plusBounded(Long.MIN_VALUE,2));
        assertEquals(Long.MIN_VALUE+3, NumbersUtils.plusBounded(Long.MIN_VALUE,3));
        assertEquals(-4, NumbersUtils.plusBounded(Long.MIN_VALUE,Long.MAX_VALUE-3));
        assertEquals(-3, NumbersUtils.plusBounded(Long.MIN_VALUE,Long.MAX_VALUE-2));
        assertEquals(-2, NumbersUtils.plusBounded(Long.MIN_VALUE,Long.MAX_VALUE-1));
        assertEquals(-1, NumbersUtils.plusBounded(Long.MIN_VALUE,Long.MAX_VALUE));
        for (long a=-100;a<=100;a++) {
            for (long b=-100;b<=100;b++) {
                assertEquals(a+b, NumbersUtils.plusBounded(a,b));
            }
        }
        assertEquals(-1, NumbersUtils.plusBounded(Long.MAX_VALUE,Long.MIN_VALUE));
        assertEquals(0, NumbersUtils.plusBounded(Long.MAX_VALUE,Long.MIN_VALUE+1));
        assertEquals(1, NumbersUtils.plusBounded(Long.MAX_VALUE,Long.MIN_VALUE+2));
        assertEquals(2, NumbersUtils.plusBounded(Long.MAX_VALUE,Long.MIN_VALUE+3));
        assertEquals(Long.MAX_VALUE-3, NumbersUtils.plusBounded(Long.MAX_VALUE,-3));
        assertEquals(Long.MAX_VALUE-2, NumbersUtils.plusBounded(Long.MAX_VALUE,-2));
        assertEquals(Long.MAX_VALUE-1, NumbersUtils.plusBounded(Long.MAX_VALUE,-1));
        assertEquals(Long.MAX_VALUE, NumbersUtils.plusBounded(Long.MAX_VALUE,0));
        //
        assertEquals(Long.MAX_VALUE, NumbersUtils.plusBounded(Long.MAX_VALUE,1));
        assertEquals(Long.MAX_VALUE, NumbersUtils.plusBounded(Long.MAX_VALUE,Long.MAX_VALUE));
    }

    public void test_minusExact_2int() {
        for (int b : new int[]{1,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.minusExact(Integer.MIN_VALUE,b);
                assertTrue(false);
            } catch (ArithmeticException e) {
                // ok
            }
        }
        for (int b : new int[]{-1,Integer.MIN_VALUE}) {
            try {
                NumbersUtils.minusExact(Integer.MAX_VALUE,b);
                assertTrue(false);
            } catch (ArithmeticException e) {
                // ok
            }
        }
        
        assertEquals(Integer.MIN_VALUE, NumbersUtils.minusExact(Integer.MIN_VALUE,0));
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.minusExact(Integer.MIN_VALUE,-1));
        assertEquals(Integer.MIN_VALUE+2, NumbersUtils.minusExact(Integer.MIN_VALUE,-2));
        assertEquals(Integer.MIN_VALUE+3, NumbersUtils.minusExact(Integer.MIN_VALUE,-3));
        assertEquals(-3, NumbersUtils.minusExact(Integer.MIN_VALUE,Integer.MIN_VALUE+3));
        assertEquals(-2, NumbersUtils.minusExact(Integer.MIN_VALUE,Integer.MIN_VALUE+2));
        assertEquals(-1, NumbersUtils.minusExact(Integer.MIN_VALUE,Integer.MIN_VALUE+1));
        assertEquals(0, NumbersUtils.minusExact(Integer.MIN_VALUE,Integer.MIN_VALUE));
        for (int a=-100;a<=100;a++) {
            for (int b=-100;b<=100;b++) {
                assertEquals(a-b, NumbersUtils.minusExact(a,b));
            }
        }
        assertEquals(0, NumbersUtils.minusExact(Integer.MAX_VALUE,Integer.MAX_VALUE));
        assertEquals(1, NumbersUtils.minusExact(Integer.MAX_VALUE,Integer.MAX_VALUE-1));
        assertEquals(2, NumbersUtils.minusExact(Integer.MAX_VALUE,Integer.MAX_VALUE-2));
        assertEquals(3, NumbersUtils.minusExact(Integer.MAX_VALUE,Integer.MAX_VALUE-3));
        assertEquals(Integer.MAX_VALUE-3, NumbersUtils.minusExact(Integer.MAX_VALUE,3));
        assertEquals(Integer.MAX_VALUE-2, NumbersUtils.minusExact(Integer.MAX_VALUE,2));
        assertEquals(Integer.MAX_VALUE-1, NumbersUtils.minusExact(Integer.MAX_VALUE,1));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.minusExact(Integer.MAX_VALUE,0));
    }

    public void test_minusExact_2long() {
        for (long b : new long[]{1,Long.MAX_VALUE}) {
            try {
                NumbersUtils.minusExact(Long.MIN_VALUE,b);
                assertTrue(false);
            } catch (ArithmeticException e) {
                // ok
            }
        }
        for (long b : new long[]{-1,Long.MIN_VALUE}) {
            try {
                NumbersUtils.minusExact(Long.MAX_VALUE,b);
                assertTrue(false);
            } catch (ArithmeticException e) {
                // ok
            }
        }
        
        assertEquals(Long.MIN_VALUE, NumbersUtils.minusExact(Long.MIN_VALUE,0));
        assertEquals(Long.MIN_VALUE+1, NumbersUtils.minusExact(Long.MIN_VALUE,-1));
        assertEquals(Long.MIN_VALUE+2, NumbersUtils.minusExact(Long.MIN_VALUE,-2));
        assertEquals(Long.MIN_VALUE+3, NumbersUtils.minusExact(Long.MIN_VALUE,-3));
        assertEquals(-3, NumbersUtils.minusExact(Long.MIN_VALUE,Long.MIN_VALUE+3));
        assertEquals(-2, NumbersUtils.minusExact(Long.MIN_VALUE,Long.MIN_VALUE+2));
        assertEquals(-1, NumbersUtils.minusExact(Long.MIN_VALUE,Long.MIN_VALUE+1));
        assertEquals(0, NumbersUtils.minusExact(Long.MIN_VALUE,Long.MIN_VALUE));
        for (long a=-100;a<=100;a++) {
            for (long b=-100;b<=100;b++) {
                assertEquals(a-b, NumbersUtils.minusExact(a,b));
            }
        }
        assertEquals(0, NumbersUtils.minusExact(Long.MAX_VALUE,Long.MAX_VALUE));
        assertEquals(1, NumbersUtils.minusExact(Long.MAX_VALUE,Long.MAX_VALUE-1));
        assertEquals(2, NumbersUtils.minusExact(Long.MAX_VALUE,Long.MAX_VALUE-2));
        assertEquals(3, NumbersUtils.minusExact(Long.MAX_VALUE,Long.MAX_VALUE-3));
        assertEquals(Long.MAX_VALUE-3, NumbersUtils.minusExact(Long.MAX_VALUE,3));
        assertEquals(Long.MAX_VALUE-2, NumbersUtils.minusExact(Long.MAX_VALUE,2));
        assertEquals(Long.MAX_VALUE-1, NumbersUtils.minusExact(Long.MAX_VALUE,1));
        assertEquals(Long.MAX_VALUE, NumbersUtils.minusExact(Long.MAX_VALUE,0));
    }
    
    public void test_minusBounded_2int() {
        assertEquals(Integer.MIN_VALUE, NumbersUtils.minusBounded(Integer.MIN_VALUE,Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, NumbersUtils.minusBounded(Integer.MIN_VALUE,1));
        //
        assertEquals(Integer.MIN_VALUE, NumbersUtils.minusBounded(Integer.MIN_VALUE,0));
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.minusBounded(Integer.MIN_VALUE,-1));
        assertEquals(Integer.MIN_VALUE+2, NumbersUtils.minusBounded(Integer.MIN_VALUE,-2));
        assertEquals(Integer.MIN_VALUE+3, NumbersUtils.minusBounded(Integer.MIN_VALUE,-3));
        assertEquals(-3, NumbersUtils.minusBounded(Integer.MIN_VALUE,Integer.MIN_VALUE+3));
        assertEquals(-2, NumbersUtils.minusBounded(Integer.MIN_VALUE,Integer.MIN_VALUE+2));
        assertEquals(-1, NumbersUtils.minusBounded(Integer.MIN_VALUE,Integer.MIN_VALUE+1));
        assertEquals(0, NumbersUtils.minusBounded(Integer.MIN_VALUE,Integer.MIN_VALUE));
        for (int a=-100;a<=100;a++) {
            for (int b=-100;b<=100;b++) {
                assertEquals(a-b, NumbersUtils.minusBounded(a,b));
            }
        }
        assertEquals(0, NumbersUtils.minusBounded(Integer.MAX_VALUE,Integer.MAX_VALUE));
        assertEquals(1, NumbersUtils.minusBounded(Integer.MAX_VALUE,Integer.MAX_VALUE-1));
        assertEquals(2, NumbersUtils.minusBounded(Integer.MAX_VALUE,Integer.MAX_VALUE-2));
        assertEquals(3, NumbersUtils.minusBounded(Integer.MAX_VALUE,Integer.MAX_VALUE-3));
        assertEquals(Integer.MAX_VALUE-3, NumbersUtils.minusBounded(Integer.MAX_VALUE,3));
        assertEquals(Integer.MAX_VALUE-2, NumbersUtils.minusBounded(Integer.MAX_VALUE,2));
        assertEquals(Integer.MAX_VALUE-1, NumbersUtils.minusBounded(Integer.MAX_VALUE,1));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.minusBounded(Integer.MAX_VALUE,0));
        //
        assertEquals(Integer.MAX_VALUE, NumbersUtils.minusBounded(Integer.MAX_VALUE,-1));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.minusBounded(Integer.MAX_VALUE,Integer.MIN_VALUE));
    }

    public void test_minusBounded_2long() {
        assertEquals(Long.MIN_VALUE, NumbersUtils.minusBounded(Long.MIN_VALUE,Long.MAX_VALUE));
        assertEquals(Long.MIN_VALUE, NumbersUtils.minusBounded(Long.MIN_VALUE,1));
        //
        assertEquals(Long.MIN_VALUE, NumbersUtils.minusBounded(Long.MIN_VALUE,0));
        assertEquals(Long.MIN_VALUE+1, NumbersUtils.minusBounded(Long.MIN_VALUE,-1));
        assertEquals(Long.MIN_VALUE+2, NumbersUtils.minusBounded(Long.MIN_VALUE,-2));
        assertEquals(Long.MIN_VALUE+3, NumbersUtils.minusBounded(Long.MIN_VALUE,-3));
        assertEquals(-3, NumbersUtils.minusBounded(Long.MIN_VALUE,Long.MIN_VALUE+3));
        assertEquals(-2, NumbersUtils.minusBounded(Long.MIN_VALUE,Long.MIN_VALUE+2));
        assertEquals(-1, NumbersUtils.minusBounded(Long.MIN_VALUE,Long.MIN_VALUE+1));
        assertEquals(0, NumbersUtils.minusBounded(Long.MIN_VALUE,Long.MIN_VALUE));
        for (long a=-100;a<=100;a++) {
            for (long b=-100;b<=100;b++) {
                assertEquals(a-b, NumbersUtils.minusBounded(a,b));
            }
        }
        assertEquals(0, NumbersUtils.minusBounded(Long.MAX_VALUE,Long.MAX_VALUE));
        assertEquals(1, NumbersUtils.minusBounded(Long.MAX_VALUE,Long.MAX_VALUE-1));
        assertEquals(2, NumbersUtils.minusBounded(Long.MAX_VALUE,Long.MAX_VALUE-2));
        assertEquals(3, NumbersUtils.minusBounded(Long.MAX_VALUE,Long.MAX_VALUE-3));
        assertEquals(Long.MAX_VALUE-3, NumbersUtils.minusBounded(Long.MAX_VALUE,3));
        assertEquals(Long.MAX_VALUE-2, NumbersUtils.minusBounded(Long.MAX_VALUE,2));
        assertEquals(Long.MAX_VALUE-1, NumbersUtils.minusBounded(Long.MAX_VALUE,1));
        assertEquals(Long.MAX_VALUE, NumbersUtils.minusBounded(Long.MAX_VALUE,0));
        //
        assertEquals(Long.MAX_VALUE, NumbersUtils.minusBounded(Long.MAX_VALUE,-1));
        assertEquals(Long.MAX_VALUE, NumbersUtils.minusBounded(Long.MAX_VALUE,Long.MIN_VALUE));
    }

    public void test_timesExact_2int() {
        final int bitSize = 32;
        
        for (long[] factors : newInRangeFactors(bitSize)) {
            final int a = NumbersUtils.asInt(factors[0]);
            final int b = NumbersUtils.asInt(factors[1]);
            assertEquals(a*b, NumbersUtils.timesExact(a,b));
        }

        for (long[] factors : newAboveRangeFactors(bitSize)) {
            final int a = NumbersUtils.asInt(factors[0]);
            final int b = NumbersUtils.asInt(factors[1]);
            try {
                NumbersUtils.timesExact(a,b);
                assertTrue(false);
            } catch (ArithmeticException e) {
                // ok
            }
        }

        for (long[] factors : newBelowRangeFactors(bitSize)) {
            final int a = NumbersUtils.asInt(factors[0]);
            final int b = NumbersUtils.asInt(factors[1]);
            try {
                NumbersUtils.timesExact(a,b);
                assertTrue(false);
            } catch (ArithmeticException e) {
                // ok
            }
        }
    }

    public void test_timesExact_2long() {
        final int bitSize = 64;
        
        for (long[] factors : newInRangeFactors(bitSize)) {
            final long a = factors[0];
            final long b = factors[1];
            assertEquals(a*b, NumbersUtils.timesExact(a,b));
        }

        for (long[] factors : newAboveRangeFactors(bitSize)) {
            final long a = factors[0];
            final long b = factors[1];
            try {
                NumbersUtils.timesExact(a,b);
                assertTrue(false);
            } catch (ArithmeticException e) {
                // ok
            }
        }

        for (long[] factors : newBelowRangeFactors(bitSize)) {
            final long a = factors[0];
            final long b = factors[1];
            try {
                NumbersUtils.timesExact(a,b);
                assertTrue(false);
            } catch (ArithmeticException e) {
                // ok
            }
        }
    }
    
    public void test_timesBounded_2int() {
        final int bitSize = 32;
        
        for (long[] factors : newInRangeFactors(bitSize)) {
            final int a = NumbersUtils.asInt(factors[0]);
            final int b = NumbersUtils.asInt(factors[1]);
            assertEquals(a*b, NumbersUtils.timesBounded(a,b));
        }

        for (long[] factors : newAboveRangeFactors(bitSize)) {
            final int a = NumbersUtils.asInt(factors[0]);
            final int b = NumbersUtils.asInt(factors[1]);
            assertEquals(Integer.MAX_VALUE, NumbersUtils.timesBounded(a,b));
        }

        for (long[] factors : newBelowRangeFactors(bitSize)) {
            final int a = NumbersUtils.asInt(factors[0]);
            final int b = NumbersUtils.asInt(factors[1]);
            assertEquals(Integer.MIN_VALUE, NumbersUtils.timesBounded(a,b));
        }
    }

    public void test_timesBounded_2long() {
        final int bitSize = 64;
        
        for (long[] factors : newInRangeFactors(bitSize)) {
            final long a = factors[0];
            final long b = factors[1];
            assertEquals(a*b, NumbersUtils.timesBounded(a,b));
        }

        for (long[] factors : newAboveRangeFactors(bitSize)) {
            final long a = factors[0];
            final long b = factors[1];
            assertEquals(Long.MAX_VALUE, NumbersUtils.timesBounded(a,b));
        }

        for (long[] factors : newBelowRangeFactors(bitSize)) {
            final long a = factors[0];
            final long b = factors[1];
            assertEquals(Long.MIN_VALUE, NumbersUtils.timesBounded(a,b));
        }
    }

    /*
     * 
     */
    
    public void test_pow2_int() {
        ArrayList<Integer> values = new ArrayList<Integer>();
        values.add(0);
        values.add(3);
        values.add(Integer.MIN_VALUE);
        values.add(Integer.MAX_VALUE);
        for (Integer value : values) {
            assertEquals(value * value, NumbersUtils.pow2(value));
        }
    }

    public void test_pow2_long() {
        ArrayList<Long> values = new ArrayList<Long>();
        values.add(0L);
        values.add(3L);
        values.add(Long.MIN_VALUE);
        values.add(Long.MAX_VALUE);
        for (Long value : values) {
            assertEquals(value * value, NumbersUtils.pow2(value));
        }
    }

    public void test_pow2_float() {
        this.test_pow2_float(false);
    }

    public void test_pow2_strict_float() {
        this.test_pow2_float(true);
    }

    public void test_pow2_float(boolean strict) {
        ArrayList<Float> values = new ArrayList<Float>();
        values.add(0.0f);
        values.add(0.1f);
        values.add(Float.MIN_VALUE);
        values.add(NumbersUtils.FLOAT_MIN_NORMAL);
        values.add(Float.MAX_VALUE);
        values.add(Float.NaN);
        values.add(Float.NEGATIVE_INFINITY);
        values.add(Float.POSITIVE_INFINITY);
        for (Float value : values) {
            assertEquals(value * value, (strict ? NumbersUtils.pow2_strict(value) : NumbersUtils.pow2(value)));
        }
    }

    public void test_pow2_double() {
        this.test_pow2_double(false);
    }
    
    public void test_pow2_strict_double() {
        this.test_pow2_double(true);
    }
    
    public void test_pow2_double(boolean strict) {
        ArrayList<Double> values = new ArrayList<Double>();
        values.add(0.0);
        values.add(0.1);
        values.add(Double.MIN_VALUE);
        values.add(NumbersUtils.DOUBLE_MIN_NORMAL);
        values.add(Double.MAX_VALUE);
        values.add(Double.NaN);
        values.add(Double.NEGATIVE_INFINITY);
        values.add(Double.POSITIVE_INFINITY);
        for (Double value : values) {
            assertEquals(value * value, (strict ? NumbersUtils.pow2_strict(value) : NumbersUtils.pow2(value)));
        }
    }

    public void test_pow3_int() {
        ArrayList<Integer> values = new ArrayList<Integer>();
        values.add(0);
        values.add(3);
        values.add(Integer.MIN_VALUE);
        values.add(Integer.MAX_VALUE);
        for (Integer value : values) {
            assertEquals(value * value * value, NumbersUtils.pow3(value));
        }
    }

    public void test_pow3_long() {
        ArrayList<Long> values = new ArrayList<Long>();
        values.add(0L);
        values.add(3L);
        values.add(Long.MIN_VALUE);
        values.add(Long.MAX_VALUE);
        for (Long value : values) {
            assertEquals(value * value * value, NumbersUtils.pow3(value));
        }
    }

    public void test_pow3_float() {
        this.test_pow3_float(false);
    }
    
    public void test_pow3_strict_float() {
        this.test_pow3_float(true);
    }
    
    public void test_pow3_float(boolean strict) {
        ArrayList<Float> values = new ArrayList<Float>();
        values.add(0.0f);
        values.add(0.1f);
        values.add(Float.MIN_VALUE);
        values.add(NumbersUtils.FLOAT_MIN_NORMAL);
        values.add(Float.MAX_VALUE);
        values.add(Float.NaN);
        values.add(Float.NEGATIVE_INFINITY);
        values.add(Float.POSITIVE_INFINITY);
        for (Float value : values) {
            assertEquals(value * value * value, (strict ? NumbersUtils.pow3_strict(value) : NumbersUtils.pow3(value)));
        }
    }

    public void test_pow3_double() {
        this.test_pow3_double(false);
    }
    
    public void test_pow3_strict_double() {
        this.test_pow3_double(true);
    }
    
    public void test_pow3_double(boolean strict) {
        ArrayList<Double> values = new ArrayList<Double>();
        values.add(0.0);
        values.add(0.1);
        values.add(Double.MIN_VALUE);
        values.add(NumbersUtils.DOUBLE_MIN_NORMAL);
        values.add(Double.MAX_VALUE);
        values.add(Double.NaN);
        values.add(Double.NEGATIVE_INFINITY);
        values.add(Double.POSITIVE_INFINITY);
        for (Double value : values) {
            assertEquals(value * value * value, (strict ? NumbersUtils.pow3_strict(value) : NumbersUtils.pow3(value)));
        }
    }
    
    /*
     * 
     */

    public void test_plus2PI_double() {
        this.test_plus2PI_double(false);
    }
    
    public void test_plus2PI_strict_double() {
        this.test_plus2PI_double(true);
    }
    
    public void test_plus2PI_double(boolean strict) {
        {
            // Using StrictMath.sin as a measure of the angle, since
            // around PI we have sin(x) ~= x.
            // 2* because at 2*Math.PI we have twice the ULP of Math.PI.
            final double expected = 2*StrictMath.sin(Math.PI);
            final double value = -2*Math.PI;
            final double actual = (strict ? NumbersUtils.plus2PI_strict(value) : NumbersUtils.plus2PI(value));
            final double relDelta = NumbersTestUtils.relDelta(expected, actual);
            assertEquals(0.0, relDelta, ACCURATE_PI_OP_SIN_EPSILON);
        }
        if (strict) {
            assertEquals(2*Math.PI, NumbersUtils.plus2PI_strict(0.0));
            assertEquals(3*Math.PI, NumbersUtils.plus2PI_strict(Math.PI));
        } else {
            assertEquals(2*Math.PI, NumbersUtils.plus2PI(0.0));
            assertEquals(3*Math.PI, NumbersUtils.plus2PI(Math.PI));
        }
        
        // Testing monotonicity around threshold.
        if (strict) {
            double a = NumbersUtils.plus2PI_strict(Math.nextAfter(-Math.PI, Double.NEGATIVE_INFINITY));
            double b = NumbersUtils.plus2PI_strict(-Math.PI);
            double c = NumbersUtils.plus2PI_strict(Math.nextAfter(-Math.PI, Double.POSITIVE_INFINITY));
            assertTrue(a <= b);
            assertTrue(b <= c);
        } else {
            double a = NumbersUtils.plus2PI(Math.nextAfter(-Math.PI, Double.NEGATIVE_INFINITY));
            double b = NumbersUtils.plus2PI(-Math.PI);
            double c = NumbersUtils.plus2PI(Math.nextAfter(-Math.PI, Double.POSITIVE_INFINITY));
            assertTrue(a <= b);
            assertTrue(b <= c);
        }
        
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final double value = this.utils.randomDoubleWhatever();
            final double ref = value + 2*Math.PI;
            final double actual = (strict ? NumbersUtils.plus2PI_strict(value) : NumbersUtils.plus2PI(value));
            
            // Testing is close to +2*Math.PI.
            final double minDelta = NumbersTestUtils.minDelta(ref, actual);
            assertEquals(0.0, minDelta, ACCURATE_PI_OP_DEFAULT_EPSILON);
        }
    }

    public void test_minus2PI_double() {
        this.test_minus2PI_double(false);
    }
    
    public void test_minus2PI_strict_double() {
        this.test_minus2PI_double(true);
    }
    
    public void test_minus2PI_double(boolean strict) {
        {
            // Using StrictMath.sin as a measure of the angle, since
            // around PI we have sin(x) ~= x.
            // 2* because at 2*Math.PI we have twice the ULP of Math.PI.
            final double expected = -2*StrictMath.sin(Math.PI);
            final double value = 2*Math.PI;
            final double actual = (strict ? NumbersUtils.minus2PI_strict(value) : NumbersUtils.minus2PI(value));
            final double relDelta = NumbersTestUtils.relDelta(expected, actual);
            assertEquals(0.0, relDelta, ACCURATE_PI_OP_SIN_EPSILON);
        }
        if (strict) {
            assertEquals(-2*Math.PI, NumbersUtils.minus2PI_strict(0.0));
            assertEquals(-3*Math.PI, NumbersUtils.minus2PI_strict(-Math.PI));
        } else {
            assertEquals(-2*Math.PI, NumbersUtils.minus2PI(0.0));
            assertEquals(-3*Math.PI, NumbersUtils.minus2PI(-Math.PI));
        }
        
        // Testing monotonicity around threshold.
        if (strict) {
            double a = NumbersUtils.minus2PI_strict(Math.nextAfter(Math.PI, Double.NEGATIVE_INFINITY));
            double b = NumbersUtils.minus2PI_strict(Math.PI);
            double c = NumbersUtils.minus2PI_strict(Math.nextAfter(Math.PI, Double.POSITIVE_INFINITY));
            assertTrue(a <= b);
            assertTrue(b <= c);
        } else {
            double a = NumbersUtils.minus2PI(Math.nextAfter(Math.PI, Double.NEGATIVE_INFINITY));
            double b = NumbersUtils.minus2PI(Math.PI);
            double c = NumbersUtils.minus2PI(Math.nextAfter(Math.PI, Double.POSITIVE_INFINITY));
            assertTrue(a <= b);
            assertTrue(b <= c);
        }
        
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final double value = this.utils.randomDoubleWhatever();
            final double ref = value - 2*Math.PI;
            final double actual = (strict ? NumbersUtils.minus2PI_strict(value) : NumbersUtils.minus2PI(value));
            
            // Testing is close to -2*Math.PI.
            final double minDelta = NumbersTestUtils.minDelta(ref, actual);
            assertEquals(0.0, minDelta, ACCURATE_PI_OP_DEFAULT_EPSILON);
        }
    }
    
    public void test_plusPI_double() {
        this.test_plusPI_double(false);
    }
    
    public void test_plusPI_strict_double() {
        this.test_plusPI_double(true);
    }
    
    public void test_plusPI_double(boolean strict) {
        {
            // Using StrictMath.sin as a measure of the angle, since
            // around PI we have sin(x) ~= x.
            final double expected = StrictMath.sin(Math.PI);
            final double value = -Math.PI;
            final double actual = (strict ? NumbersUtils.plusPI_strict(value) : NumbersUtils.plusPI(value));
            final double relDelta = NumbersTestUtils.relDelta(expected, actual);
            assertEquals(0.0, relDelta, ACCURATE_PI_OP_SIN_EPSILON);
        }
        if (strict) {
            assertEquals(Math.PI, NumbersUtils.plusPI_strict(0.0));
            assertEquals(2*Math.PI, NumbersUtils.plusPI_strict(Math.PI));
        } else {
            assertEquals(Math.PI, NumbersUtils.plusPI(0.0));
            assertEquals(2*Math.PI, NumbersUtils.plusPI(Math.PI));
        }
        
        // Testing monotonicity around threshold.
        if (strict) {
            double a = NumbersUtils.plusPI_strict(Math.nextAfter(-Math.PI/2, Double.NEGATIVE_INFINITY));
            double b = NumbersUtils.plusPI_strict(-Math.PI/2);
            double c = NumbersUtils.plusPI_strict(Math.nextAfter(-Math.PI/2, Double.POSITIVE_INFINITY));
            assertTrue(a <= b);
            assertTrue(b <= c);
        } else {
            double a = NumbersUtils.plusPI(Math.nextAfter(-Math.PI/2, Double.NEGATIVE_INFINITY));
            double b = NumbersUtils.plusPI(-Math.PI/2);
            double c = NumbersUtils.plusPI(Math.nextAfter(-Math.PI/2, Double.POSITIVE_INFINITY));
            assertTrue(a <= b);
            assertTrue(b <= c);
        }
        
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final double value = this.utils.randomDoubleWhatever();
            final double ref = value + Math.PI;
            final double actual = (strict ? NumbersUtils.plusPI_strict(value) : NumbersUtils.plusPI(value));
            
            // Testing is close to +Math.PI.
            final double minDelta = NumbersTestUtils.minDelta(ref, actual);
            assertEquals(0.0, minDelta, ACCURATE_PI_OP_DEFAULT_EPSILON);
        }
    }
    
    public void test_minusPI_double() {
        this.test_minusPI_double(false);
    }
    
    public void test_minusPI_strict_double() {
        this.test_minusPI_double(true);
    }
    
    public void test_minusPI_double(boolean strict) {
        {
            // Using StrictMath.sin as a measure of the angle, since
            // around PI we have sin(x) ~= x.
            final double expected = -StrictMath.sin(Math.PI);
            final double value = Math.PI;
            final double actual = (strict ? NumbersUtils.minusPI_strict(value) : NumbersUtils.minusPI(value));
            final double relDelta = NumbersTestUtils.relDelta(expected, actual);
            assertEquals(0.0, relDelta, ACCURATE_PI_OP_SIN_EPSILON);
        }
        if (strict) {
            assertEquals(-Math.PI, NumbersUtils.minusPI_strict(0.0));
            assertEquals(-2*Math.PI, NumbersUtils.minusPI_strict(-Math.PI));
        } else {
            assertEquals(-Math.PI, NumbersUtils.minusPI(0.0));
            assertEquals(-2*Math.PI, NumbersUtils.minusPI(-Math.PI));
        }
        
        // Testing monotonicity around threshold.
        if (strict) {
            double a = NumbersUtils.minusPI_strict(Math.nextAfter(Math.PI/2, Double.NEGATIVE_INFINITY));
            double b = NumbersUtils.minusPI_strict(Math.PI/2);
            double c = NumbersUtils.minusPI_strict(Math.nextAfter(Math.PI/2, Double.POSITIVE_INFINITY));
            assertTrue(a <= b);
            assertTrue(b <= c);
        } else {
            double a = NumbersUtils.minusPI(Math.nextAfter(Math.PI/2, Double.NEGATIVE_INFINITY));
            double b = NumbersUtils.minusPI(Math.PI/2);
            double c = NumbersUtils.minusPI(Math.nextAfter(Math.PI/2, Double.POSITIVE_INFINITY));
            assertTrue(a <= b);
            assertTrue(b <= c);
        }
        
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final double value = this.utils.randomDoubleWhatever();
            final double ref = value - Math.PI;
            final double actual = (strict ? NumbersUtils.minusPI_strict(value) : NumbersUtils.minusPI(value));
            
            // Testing is close to -Math.PI.
            final double minDelta = NumbersTestUtils.minDelta(ref, actual);
            assertEquals(0.0, minDelta, ACCURATE_PI_OP_DEFAULT_EPSILON);
        }
    }

    public void test_plusPIO2_double() {
        this.test_plusPIO2_double(false);
    }
    
    public void test_plusPIO2_strict_double() {
        this.test_plusPIO2_double(true);
    }
    
    public void test_plusPIO2_double(boolean strict) {
        {
            // Using StrictMath.sin as a measure of the angle, since
            // around PI we have sin(x) ~= x.
            // 0.5* because at Math.PI/2 we have half the ULP of Math.PI.
            final double expected = 0.5*StrictMath.sin(Math.PI);
            final double value = -Math.PI/2;
            final double actual = (strict ? NumbersUtils.plusPIO2_strict(value) : NumbersUtils.plusPIO2(value));
            final double relDelta = NumbersTestUtils.relDelta(expected, actual);
            assertEquals(0.0, relDelta, ACCURATE_PI_OP_SIN_EPSILON);
        }
        if (strict) {
            assertEquals(Math.PI/2, NumbersUtils.plusPIO2_strict(0.0));
            assertEquals(Math.PI, NumbersUtils.plusPIO2_strict(Math.PI/2));
        } else {
            assertEquals(Math.PI/2, NumbersUtils.plusPIO2(0.0));
            assertEquals(Math.PI, NumbersUtils.plusPIO2(Math.PI/2));
        }
        
        // Testing monotonicity around threshold.
        if (strict) {
            double a = NumbersUtils.plusPIO2_strict(Math.nextAfter(-Math.PI/4, Double.NEGATIVE_INFINITY));
            double b = NumbersUtils.plusPIO2_strict(-Math.PI/4);
            double c = NumbersUtils.plusPIO2_strict(Math.nextAfter(-Math.PI/4, Double.POSITIVE_INFINITY));
            assertTrue(a <= b);
            assertTrue(b <= c);
        } else {
            double a = NumbersUtils.plusPIO2(Math.nextAfter(-Math.PI/4, Double.NEGATIVE_INFINITY));
            double b = NumbersUtils.plusPIO2(-Math.PI/4);
            double c = NumbersUtils.plusPIO2(Math.nextAfter(-Math.PI/4, Double.POSITIVE_INFINITY));
            assertTrue(a <= b);
            assertTrue(b <= c);
        }
        
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final double value = this.utils.randomDoubleWhatever();
            final double ref = value + Math.PI/2;
            final double actual = (strict ? NumbersUtils.plusPIO2_strict(value) : NumbersUtils.plusPIO2(value));
            
            // Testing is close to +Math.PI/2.
            final double minDelta = NumbersTestUtils.minDelta(ref, actual);
            assertEquals(0.0, minDelta, ACCURATE_PI_OP_DEFAULT_EPSILON);
        }
    }
    
    public void test_minusPIO2_double() {
        this.test_minusPIO2_double(false);
    }
    
    public void test_minusPIO2_strict_double() {
        this.test_minusPIO2_double(true);
    }
    
    public void test_minusPIO2_double(boolean strict) {
        {
            // Using StrictMath.sin as a measure of the angle, since
            // around PI we have sin(x) ~= x.
            // 0.5* because at Math.PI/2 we have half the ULP of Math.PI.
            final double expected = -0.5*StrictMath.sin(Math.PI);
            final double value = Math.PI/2;
            final double actual = (strict ? NumbersUtils.minusPIO2_strict(value) : NumbersUtils.minusPIO2(value));
            final double relDelta = NumbersTestUtils.relDelta(expected, actual);
            assertEquals(0.0, relDelta, ACCURATE_PI_OP_SIN_EPSILON);
        }
        if (strict) {
            assertEquals(-Math.PI/2, NumbersUtils.minusPIO2_strict(0.0));
            assertEquals(-Math.PI, NumbersUtils.minusPIO2_strict(-Math.PI/2));
        } else {
            assertEquals(-Math.PI/2, NumbersUtils.minusPIO2(0.0));
            assertEquals(-Math.PI, NumbersUtils.minusPIO2(-Math.PI/2));
        }
        
        // Testing monotonicity around threshold.
        if (strict) {
            double a = NumbersUtils.minusPIO2_strict(Math.nextAfter(Math.PI/4, Double.NEGATIVE_INFINITY));
            double b = NumbersUtils.minusPIO2_strict(Math.PI/4);
            double c = NumbersUtils.minusPIO2_strict(Math.nextAfter(Math.PI/4, Double.POSITIVE_INFINITY));
            assertTrue(a <= b);
            assertTrue(b <= c);
        } else {
            double a = NumbersUtils.minusPIO2(Math.nextAfter(Math.PI/4, Double.NEGATIVE_INFINITY));
            double b = NumbersUtils.minusPIO2(Math.PI/4);
            double c = NumbersUtils.minusPIO2(Math.nextAfter(Math.PI/4, Double.POSITIVE_INFINITY));
            assertTrue(a <= b);
            assertTrue(b <= c);
        }
        
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final double value = this.utils.randomDoubleWhatever();
            final double ref = value - Math.PI/2;
            final double actual = (strict ? NumbersUtils.minusPIO2_strict(value) : NumbersUtils.minusPIO2(value));
            
            // Testing is close to -Math.PI/2.
            final double minDelta = NumbersTestUtils.minDelta(ref, actual);
            assertEquals(0.0, minDelta, ACCURATE_PI_OP_DEFAULT_EPSILON);
        }
    }

    /*
     * 
     */
    
    public void test_checkRadix_int() {
        for (int radix : new int[]{Integer.MIN_VALUE,1,37,Integer.MAX_VALUE}) {
            try {
                NumbersUtils.checkRadix(radix);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        for (int radix : new int[]{2,36}) {
            assertTrue(NumbersUtils.checkRadix(radix));
        }
    }

    public void test_computeNbrOfChars_int_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final int value = random.nextInt();
            final int radix = Character.MIN_RADIX + random.nextInt(Character.MAX_RADIX-Character.MIN_RADIX+1);
            final int expected = Integer.toString(value, radix).length();
            assertEquals(expected,NumbersUtils.computeNbrOfChars(value, radix));
        }
    }
    
    public void test_computeNbrOfChars_long_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final long value = random.nextLong();
            final int radix = Character.MIN_RADIX + random.nextInt(Character.MAX_RADIX-Character.MIN_RADIX+1);
            final int expected = Long.toString(value, radix).length();
            assertEquals(expected,NumbersUtils.computeNbrOfChars(value, radix));
        }
    }
    
    public void test_computeNbrOfChars_int_int_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final int value = random.nextInt();
            final int radix = Character.MIN_RADIX + random.nextInt(Character.MAX_RADIX-Character.MIN_RADIX+1);
            final int nbrOfChars = Integer.toString(value, radix).length();
            final int oneIfNeg = ((value < 0) ? 1 : 0);
            final int nbrOfDigits = nbrOfChars - oneIfNeg;
            final int paddingUpTo = nbrOfDigits + (10-random.nextInt(20));
            final int expected = oneIfNeg + Math.max(nbrOfDigits, paddingUpTo);
            assertEquals(expected,NumbersUtils.computeNbrOfChars(value, radix, paddingUpTo));
        }
    }
    
    public void test_computeNbrOfChars_long_int_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final long value = random.nextLong();
            final int radix = Character.MIN_RADIX + random.nextInt(Character.MAX_RADIX-Character.MIN_RADIX+1);
            final int nbrOfChars = Long.toString(value, radix).length();
            final int oneIfNeg = ((value < 0) ? 1 : 0);
            final int nbrOfDigits = nbrOfChars - oneIfNeg;
            final int paddingUpTo = nbrOfDigits + (10-random.nextInt(20));
            final int expected = oneIfNeg + Math.max(nbrOfDigits, paddingUpTo);
            assertEquals(expected,NumbersUtils.computeNbrOfChars(value, radix, paddingUpTo));
        }
    }
    
    public void test_computeNbrOfDigits_int_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final int value = random.nextInt();
            final int radix = Character.MIN_RADIX + random.nextInt(Character.MAX_RADIX-Character.MIN_RADIX+1);
            final int nbrOfChars = Integer.toString(value, radix).length();
            final int oneIfNeg = ((value < 0) ? 1 : 0);
            final int expected = nbrOfChars - oneIfNeg;
            assertEquals(expected,NumbersUtils.computeNbrOfDigits(value, radix));
        }
    }
    
    public void test_computeNbrOfDigits_long_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final long value = random.nextLong();
            final int radix = Character.MIN_RADIX + random.nextInt(Character.MAX_RADIX-Character.MIN_RADIX+1);
            final int nbrOfChars = Long.toString(value, radix).length();
            final int oneIfNeg = ((value < 0) ? 1 : 0);
            final int expected = nbrOfChars - oneIfNeg;
            assertEquals(expected,NumbersUtils.computeNbrOfDigits(value, radix));
        }
    }
    
    public void test_computeNbrOfDigits_int_int_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final int value = random.nextInt();
            final int radix = Character.MIN_RADIX + random.nextInt(Character.MAX_RADIX-Character.MIN_RADIX+1);
            final int nbrOfChars = Integer.toString(value, radix).length();
            final int oneIfNeg = ((value < 0) ? 1 : 0);
            final int nbrOfDigits = nbrOfChars - oneIfNeg;
            final int paddingUpTo = nbrOfDigits + (10-random.nextInt(20));
            final int expected = Math.max(nbrOfDigits, paddingUpTo);
            assertEquals(expected,NumbersUtils.computeNbrOfDigits(value, radix, paddingUpTo));
        }
    }

    public void test_computeNbrOfDigits_long_int_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final long value = random.nextLong();
            final int radix = Character.MIN_RADIX + random.nextInt(Character.MAX_RADIX-Character.MIN_RADIX+1);
            final int nbrOfChars = Long.toString(value, radix).length();
            final int oneIfNeg = ((value < 0) ? 1 : 0);
            final int nbrOfDigits = nbrOfChars - oneIfNeg;
            final int paddingUpTo = nbrOfDigits + (10-random.nextInt(20));
            final int expected = Math.max(nbrOfDigits, paddingUpTo);
            assertEquals(expected,NumbersUtils.computeNbrOfDigits(value, radix, paddingUpTo));
        }
    }

    public void test_toString_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final int value = random.nextInt();
            final String expected = Integer.toString(value);
            assertEquals(expected,NumbersUtils.toString(value));
        }
    }

    public void test_toString_long() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final long value = random.nextLong();
            final String expected = Long.toString(value);
            assertEquals(expected,NumbersUtils.toString(value));
        }
    }

    public void test_toString_int_int() {
        for (int radix=Character.MIN_RADIX;radix<=Character.MAX_RADIX;radix++) {
            assertEquals(Integer.toString(Integer.MIN_VALUE, radix).toUpperCase(),NumbersUtils.toString(Integer.MIN_VALUE, radix));
            assertEquals(Integer.toString(Integer.MAX_VALUE, radix).toUpperCase(),NumbersUtils.toString(Integer.MAX_VALUE, radix));
        }
        
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final int value = random.nextInt();
            final int radix = Character.MIN_RADIX + random.nextInt(Character.MAX_RADIX-Character.MIN_RADIX+1);
            final String expected = Integer.toString(value, radix).toUpperCase();
            assertEquals(expected,NumbersUtils.toString(value, radix));
        }
    }

    public void test_toString_long_int() {
        for (int radix=Character.MIN_RADIX;radix<=Character.MAX_RADIX;radix++) {
            assertEquals(Long.toString(Long.MIN_VALUE, radix).toUpperCase(),NumbersUtils.toString(Long.MIN_VALUE, radix));
            assertEquals(Long.toString(Long.MAX_VALUE, radix).toUpperCase(),NumbersUtils.toString(Long.MAX_VALUE, radix));
        }
        
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final long value = random.nextLong();
            final int radix = Character.MIN_RADIX + random.nextInt(Character.MAX_RADIX-Character.MIN_RADIX+1);
            final String expected = Long.toString(value, radix).toUpperCase();
            assertEquals(expected,NumbersUtils.toString(value, radix));
        }
    }

    public void test_toString_int_int_int() {
        assertEquals("-FF",NumbersUtils.toString(-255, 16, 0));
        assertEquals("-0FF",NumbersUtils.toString(-255, 16, 3));
        // Allowing huge padding.
        assertEquals("-000000000000000000000000000000000FF",NumbersUtils.toString(-255, 16, 35));
        
        assertEquals("FF",NumbersUtils.toString(255, 16, 0));
        assertEquals("0FF",NumbersUtils.toString(255, 16, 3));
    }

    public void test_toString_long_int_int() {
        assertEquals("-FF",NumbersUtils.toString(-255L, 16, 0));
        assertEquals("-0FF",NumbersUtils.toString(-255L, 16, 3));
        // Allowing huge padding.
        assertEquals("-00000000000000000000000000000000000000000000000000000000000000000FF",NumbersUtils.toString(-255L, 16, 67));
        
        assertEquals("FF",NumbersUtils.toString(255L, 16, 0));
        assertEquals("0FF",NumbersUtils.toString(255L, 16, 3));
    }

    /*
     * 
     */
    
    public void test_checkBitPositionsByte_2int() {
        final int maxBisPos = 8;
        
        for (int[] range : new int[][]{{0,maxBisPos+1},{-1,maxBisPos},{-1,maxBisPos+1},{5,4}}) {
            try {
                NumbersUtils.checkBitPositionsByte(range[0], range[1]);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        for (int[] range : new int[][]{{0, 0},{0,maxBisPos},{maxBisPos,maxBisPos}}) {
            assertTrue(NumbersUtils.checkBitPositionsByte(range[0], range[1]));
        }
    }
    
    public void test_checkBitPositionsShort_2int() {
        final int maxBisPos = 16;
        
        for (int[] range : new int[][]{{0,maxBisPos+1},{-1,maxBisPos},{-1,maxBisPos+1},{5,4}}) {
            try {
                NumbersUtils.checkBitPositionsShort(range[0], range[1]);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        for (int[] range : new int[][]{{0, 0},{0,maxBisPos},{maxBisPos,maxBisPos}}) {
            assertTrue(NumbersUtils.checkBitPositionsShort(range[0], range[1]));
        }
    }
    
    public void test_checkBitPositionsInt_2int() {
        final int maxBisPos = 32;
        
        for (int[] range : new int[][]{{0,maxBisPos+1},{-1,maxBisPos},{-1,maxBisPos+1},{5,4}}) {
            try {
                NumbersUtils.checkBitPositionsInt(range[0], range[1]);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        for (int[] range : new int[][]{{0, 0},{0,maxBisPos},{maxBisPos,maxBisPos}}) {
            assertTrue(NumbersUtils.checkBitPositionsInt(range[0], range[1]));
        }
    }
    
    public void test_checkBitPositionsLong_2int() {
        final int maxBisPos = 64;
        
        for (int[] range : new int[][]{{0,maxBisPos+1},{-1,maxBisPos},{-1,maxBisPos+1},{5,4}}) {
            try {
                NumbersUtils.checkBitPositionsLong(range[0], range[1]);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        for (int[] range : new int[][]{{0, 0},{0,maxBisPos},{maxBisPos,maxBisPos}}) {
            assertTrue(NumbersUtils.checkBitPositionsLong(range[0], range[1]));
        }
    }
    
    public void test_toStringBits_byte() {
        assertEquals("00010010",NumbersUtils.toStringBits((byte)0x12));
        assertEquals("11110010",NumbersUtils.toStringBits((byte)0xF2));
    }
    
    public void test_toStringBits_short() {
        assertEquals("0001001000110100",NumbersUtils.toStringBits((short)0x1234));
        assertEquals("1111001000110100",NumbersUtils.toStringBits((short)0xF234));
    }
    
    public void test_toStringBits_int() {
        assertEquals("00010010001101000101011001111000",NumbersUtils.toStringBits(0x12345678));
        assertEquals("11110010001101000101011001111000",NumbersUtils.toStringBits(0xF2345678));
    }
    
    public void test_toStringBits_long() {
        assertEquals("0001001000110100010101100111100000010011010101110010010001101000",NumbersUtils.toStringBits(0x1234567813572468L));
        assertEquals("1111001000110100010101100111100000010011010101110010010001101000",NumbersUtils.toStringBits(0xF234567813572468L));
    }

    public void test_toStringBits_byte_2int_2boolean() {
        final byte bits = (byte)0x12;
        final int maxBitPosExcl = 8;
        int first;
        int lastExcl;
        boolean bigEndian;
        boolean padding;
        /*
         * ko
         */
        for (int[] range : new int[][]{{0,maxBitPosExcl+1},{-1,maxBitPosExcl},{-1,maxBitPosExcl+1},{5,4}}) {
            try {
                NumbersUtils.toStringBits(bits, first = range[0], lastExcl = range[1], bigEndian = true, padding = false);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
        /*
         * big endian
         */
        assertEquals("00010010",NumbersUtils.toStringBits(bits, first = 0, lastExcl = maxBitPosExcl, bigEndian = true, padding = true));
        assertEquals("00010010",NumbersUtils.toStringBits(bits, first = 0, lastExcl = maxBitPosExcl, bigEndian = true, padding = false));
        assertEquals("__01001_",NumbersUtils.toStringBits(bits, first = 2, lastExcl = maxBitPosExcl-1, bigEndian = true, padding = true));
        assertEquals("01001",NumbersUtils.toStringBits(bits, first = 2, lastExcl = maxBitPosExcl-1, bigEndian = true, padding = false));
        /*
         * little endian
         */
        assertEquals("01001000",NumbersUtils.toStringBits(bits, first = 0, lastExcl = maxBitPosExcl, bigEndian = false, padding = true));
        assertEquals("01001000",NumbersUtils.toStringBits(bits, first = 0, lastExcl = maxBitPosExcl, bigEndian = false, padding = false));
        assertEquals("_10010__",NumbersUtils.toStringBits(bits, first = 1, lastExcl = maxBitPosExcl-2, bigEndian = false, padding = true));
        assertEquals("10010",NumbersUtils.toStringBits(bits, first = 1, lastExcl = maxBitPosExcl-2, bigEndian = false, padding = false));
    }

    public void test_toStringBits_short_2int_2boolean() {
        final short bits = (short)0x12;
        final int maxBitPosExcl = 16;
        int first;
        int lastExcl;
        boolean bigEndian;
        boolean padding;
        /*
         * ko
         */
        for (int[] range : new int[][]{{0,maxBitPosExcl+1},{-1,maxBitPosExcl},{-1,maxBitPosExcl+1},{5,4}}) {
            try {
                NumbersUtils.toStringBits(bits, first = range[0], lastExcl = range[1], bigEndian = true, padding = false);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
        /*
         * big endian
         */
        assertEquals("0000000000010010",NumbersUtils.toStringBits(bits, first = 0, lastExcl = maxBitPosExcl, bigEndian = true, padding = true));
        assertEquals("0000000000010010",NumbersUtils.toStringBits(bits, first = 0, lastExcl = maxBitPosExcl, bigEndian = true, padding = false));
        assertEquals("__0000000001001_",NumbersUtils.toStringBits(bits, first = 2, lastExcl = maxBitPosExcl-1, bigEndian = true, padding = true));
        assertEquals("0000000001001",NumbersUtils.toStringBits(bits, first = 2, lastExcl = maxBitPosExcl-1, bigEndian = true, padding = false));
        /*
         * little endian
         */
        assertEquals("0100100000000000",NumbersUtils.toStringBits(bits, first = 0, lastExcl = maxBitPosExcl, bigEndian = false, padding = true));
        assertEquals("0100100000000000",NumbersUtils.toStringBits(bits, first = 0, lastExcl = maxBitPosExcl, bigEndian = false, padding = false));
        assertEquals("_1001000000000__",NumbersUtils.toStringBits(bits, first = 1, lastExcl = maxBitPosExcl-2, bigEndian = false, padding = true));
        assertEquals("1001000000000",NumbersUtils.toStringBits(bits, first = 1, lastExcl = maxBitPosExcl-2, bigEndian = false, padding = false));
    }

    public void test_toStringBits_int_2int_2boolean() {
        final int bits = (int)0x12;
        final int maxBitPosExcl = 32;
        int first;
        int lastExcl;
        boolean bigEndian;
        boolean padding;
        /*
         * ko
         */
        for (int[] range : new int[][]{{0,maxBitPosExcl+1},{-1,maxBitPosExcl},{-1,maxBitPosExcl+1},{5,4}}) {
            try {
                NumbersUtils.toStringBits(bits, first = range[0], lastExcl = range[1], bigEndian = true, padding = false);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
        /*
         * big endian
         */
        assertEquals("00000000000000000000000000010010",NumbersUtils.toStringBits(bits, first = 0, lastExcl = maxBitPosExcl, bigEndian = true, padding = true));
        assertEquals("00000000000000000000000000010010",NumbersUtils.toStringBits(bits, first = 0, lastExcl = maxBitPosExcl, bigEndian = true, padding = false));
        assertEquals("__00000000000000000000000001001_",NumbersUtils.toStringBits(bits, first = 2, lastExcl = maxBitPosExcl-1, bigEndian = true, padding = true));
        assertEquals("00000000000000000000000001001",NumbersUtils.toStringBits(bits, first = 2, lastExcl = maxBitPosExcl-1, bigEndian = true, padding = false));
        /*
         * little endian
         */
        assertEquals("01001000000000000000000000000000",NumbersUtils.toStringBits(bits, first = 0, lastExcl = maxBitPosExcl, bigEndian = false, padding = true));
        assertEquals("01001000000000000000000000000000",NumbersUtils.toStringBits(bits, first = 0, lastExcl = maxBitPosExcl, bigEndian = false, padding = false));
        assertEquals("_10010000000000000000000000000__",NumbersUtils.toStringBits(bits, first = 1, lastExcl = maxBitPosExcl-2, bigEndian = false, padding = true));
        assertEquals("10010000000000000000000000000",NumbersUtils.toStringBits(bits, first = 1, lastExcl = maxBitPosExcl-2, bigEndian = false, padding = false));
    }

    public void test_toStringBits_long_2int_2boolean() {
        final long bits = (long)0x12;
        final int maxBitPosExcl = 64;
        int first;
        int lastExcl;
        boolean bigEndian;
        boolean padding;
        /*
         * ko
         */
        for (int[] range : new int[][]{{0,maxBitPosExcl+1},{-1,maxBitPosExcl},{-1,maxBitPosExcl+1},{5,4}}) {
            try {
                NumbersUtils.toStringBits(bits, first = range[0], lastExcl = range[1], bigEndian = true, padding = false);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }
        /*
         * big endian
         */
        assertEquals("0000000000000000000000000000000000000000000000000000000000010010",NumbersUtils.toStringBits(bits, first = 0, lastExcl = maxBitPosExcl, bigEndian = true, padding = true));
        assertEquals("0000000000000000000000000000000000000000000000000000000000010010",NumbersUtils.toStringBits(bits, first = 0, lastExcl = maxBitPosExcl, bigEndian = true, padding = false));
        assertEquals("__0000000000000000000000000000000000000000000000000000000001001_",NumbersUtils.toStringBits(bits, first = 2, lastExcl = maxBitPosExcl-1, bigEndian = true, padding = true));
        assertEquals("0000000000000000000000000000000000000000000000000000000001001",NumbersUtils.toStringBits(bits, first = 2, lastExcl = maxBitPosExcl-1, bigEndian = true, padding = false));
        /*
         * little endian
         */
        assertEquals("0100100000000000000000000000000000000000000000000000000000000000",NumbersUtils.toStringBits(bits, first = 0, lastExcl = maxBitPosExcl, bigEndian = false, padding = true));
        assertEquals("0100100000000000000000000000000000000000000000000000000000000000",NumbersUtils.toStringBits(bits, first = 0, lastExcl = maxBitPosExcl, bigEndian = false, padding = false));
        assertEquals("_1001000000000000000000000000000000000000000000000000000000000__",NumbersUtils.toStringBits(bits, first = 1, lastExcl = maxBitPosExcl-2, bigEndian = false, padding = true));
        assertEquals("1001000000000000000000000000000000000000000000000000000000000",NumbersUtils.toStringBits(bits, first = 1, lastExcl = maxBitPosExcl-2, bigEndian = false, padding = false));
    }
    
    /*
     * 
     */
    
    public void test_toStringCSN_double() {
        
        /*
         * Testing that bounds are correctly handled
         * (errors like < instead of <= could make it not so).
         */
        
        assertEquals("1.0E-3",NumbersUtils.toStringCSN(NumbersUtils.NO_CSN_MIN_BOUND_INCL));
        assertEquals("1.0E7",NumbersUtils.toStringCSN(NumbersUtils.NO_CSN_MAX_BOUND_EXCL));

        /*
         * zeros
         */

        assertEquals("0.0E0",NumbersUtils.toStringCSN(0.0));
        assertEquals("-0.0E0",NumbersUtils.toStringCSN(-0.0));

        /*
         * 
         */
        
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final double value = this.utils.randomDoubleWhatever();
            
            final String resString = NumbersUtils.toStringCSN(value);
            final boolean quickCheckOK =
                (resString.contains("E") || Double.isNaN(value) || Double.isInfinite(value))
                && (!resString.startsWith("."))
                && (!resString.contains(".E"));
            if (!quickCheckOK) {
                System.out.println("value =     "+value);
                System.out.println("resString = "+resString);
            }
            assertTrue(quickCheckOK);
            
            final double resValue = Double.parseDouble(resString);
            if (!NumbersUtils.equal(value, resValue)) {
                System.out.println("value =     "+value);
                System.out.println("resString = "+resString);
                System.out.println("resValue =  "+resValue);
            }
            assertEquals(value, resValue);
        }
    }
    
    public void test_toStringNoCSN_double() {
        
        /*
         * Testing that bounds are correctly handled
         * (errors like < instead of <= could make it not so).
         */
        
        // Does not have bug 4428022 (does not return "0.0010").
        assertEquals("0.001",NumbersUtils.toStringNoCSN(NumbersUtils.NO_CSN_MIN_BOUND_INCL));
        assertEquals("10000000.0",NumbersUtils.toStringNoCSN(NumbersUtils.NO_CSN_MAX_BOUND_EXCL));
        
        /*
         * zeros
         */

        assertEquals("0.0",NumbersUtils.toStringNoCSN(0.0));
        assertEquals("-0.0",NumbersUtils.toStringNoCSN(-0.0));

        /*
         * 
         */
        
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final double value = this.utils.randomDoubleWhatever();
            
            final String resString = NumbersUtils.toStringNoCSN(value);
            final boolean quickCheckOK =
                (!resString.contains("E"))
                && (!resString.startsWith("."))
                && (!resString.endsWith("."));
            if (!quickCheckOK) {
                System.out.println("value =     "+value);
                System.out.println("resString = "+resString);
            }
            assertTrue(quickCheckOK);
            
            final double resValue = Double.parseDouble(resString);
            if (!NumbersUtils.equal(value, resValue)) {
                System.out.println("value =     "+value);
                System.out.println("resString = "+resString);
                System.out.println("resValue =  "+resValue);
            }
            assertEquals(value, resValue);
        }
    }

    //--------------------------------------------------------------------------
    // PRIVATE METHODS
    //--------------------------------------------------------------------------

    /**
     * Useful not to each time test both foo(a,b) and foo(b,a):
     * foo(a,b) suffices.
     * 
     * @return An array twice as long as the specified array,
     *         containing original pairs and permuted pairs
     *         (i.e. {b,a} for each {a,b} pair).
     */
    private static long[][] originalAndPermuted(long[][] pairs) {
        final long[][] result = new long[2*pairs.length][];
        for (int i=0;i<pairs.length;i++) {
            final long[] pair = pairs[i];
            result[i] = pair;
            result[i+pairs.length] = new long[]{pair[1],pair[0]};
        }
        return result;
    }

    private static long mid(int bitSize) {
        // Need even number of bits for ou mid trick.
        if ((bitSize < 8) || (bitSize > 64) || ((bitSize&1) != 0)) {
            throw new IllegalArgumentException();
        }
        // Mean magnitude (to test special cases around these magnitudes),
        // as an even number such as
        // 2 * mid*mid - 1
        // = 2 * (1L<<(bitSize-2)) - 1
        // = (1L<<(bitSize-1)) - 1
        // = max
        return (1L<<((bitSize/2)-1));
    }
    
    /**
     * @return Array of factors which product is in range
     *         for a signed integer type with the specified number of bits.
     */
    private static long[][] newInRangeFactors(int bitSize) {
        if ((bitSize < 8) || (bitSize > 64)) {
            throw new IllegalArgumentException();
        }
        final long min = (Long.MIN_VALUE>>(64-bitSize));
        final long max = (Long.MAX_VALUE>>(64-bitSize));
        final long mid = mid(bitSize);
        final long[][] pairs = new long[][]{
                // -hi, +lo
                {min,1},
                {min/2,2},
                {min/3,3},
                // -hi, -lo
                {(min+1),-1},
                {(min+1)/2,-2},
                {(min+1)/3,-3},
                // -hi, 0
                {min,0},
                // +hi, 0
                {max,0},
                // +hi, +lo
                {max,1},
                {max/2,2},
                {max/3,3},
                // +hi, -lo
                {max,-1},
                {max/2,-2},
                {max/3,-3},
                // mid (+,+), product < max
                {2*mid,mid-1},
                {2*mid-1,mid},
                {2*mid-1,mid-1},
                // mid (-,-), product < max
                {-2*mid,-mid+1},
                {-2*mid+1,-mid},
                {-2*mid+1,-mid+1},
                // mid (+,-), product = min
                {2*mid,-mid},
                {4*mid,-mid/2},
                // mid (-,+), product = min
                {-2*mid,mid},
                {-4*mid,mid/2}};
        return originalAndPermuted(pairs);
    }

    /**
     * @return Array of factors which product is above range
     *         for a signed integer type with the specified number of bits.
     */
    private static long[][] newAboveRangeFactors(int bitSize) {
        if ((bitSize < 8) || (bitSize > 64)) {
            throw new IllegalArgumentException();
        }
        final long min = (Long.MIN_VALUE>>(64-bitSize));
        final long max = (Long.MAX_VALUE>>(64-bitSize));
        final long mid = mid(bitSize);
        final long[][] pairs = new long[][]{
                // -hi, -lo
                {min,-1},
                {min/2,-2},
                // -hi, -hi
                {min,min},
                // +hi, +hi
                {max,max},
                // +hi, +lo
                {max,2},
                {max/2,3},
                {max/3,4},
                // mid (+,+), product > max
                {2*mid,mid+1},
                {2*mid+1,mid},
                {2*mid+1,mid+1},
                // mid (-,-), product > max
                {-2*mid,-mid-1},
                {-2*mid-1,-mid},
                {-2*mid-1,-mid-1}};
        return originalAndPermuted(pairs);
    }

    /**
     * @return Array of factors which product is below range
     *         for a signed integer type with the specified number of bits.
     */
    private static long[][] newBelowRangeFactors(int bitSize) {
        if ((bitSize < 8) || (bitSize > 64)) {
            throw new IllegalArgumentException();
        }
        final long min = (Long.MIN_VALUE>>(64-bitSize));
        final long max = (Long.MAX_VALUE>>(64-bitSize));
        final long mid = mid(bitSize);
        final long[][] pairs = new long[][]{
                // -hi, +lo
                {min,2},
                {min/2,3},
                {min/3,4},
                // -hi, +hi
                {min,max},
                // +hi, -lo
                {max,-2},
                {max/2,-3},
                {max/3,-4},
                // mid (+,-), product < min
                {2*mid,-mid-1},
                {2*mid+1,-mid},
                {2*mid+1,-mid-1},
                // mid (-,+), product < min
                {-2*mid,mid+1},
                {-2*mid-1,mid},
                {-2*mid-1,mid+1}};
        return originalAndPermuted(pairs);
    }
}
