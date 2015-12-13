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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public class CmnFastMathTest extends AbstractFastMathTezt {

    //--------------------------------------------------------------------------
    // PUBLIC METHODS
    //--------------------------------------------------------------------------

    public void test_E() {
        assertEquals(Math.E, CmnFastMath.E);
    }

    public void test_PI() {
        assertEquals(Math.PI, CmnFastMath.PI);
    }

    public void test_PI_SUP() {
        final long expectedBits = Double.doubleToRawLongBits(Math.PI)+1;
        final long actualBits = Double.doubleToRawLongBits(CmnFastMath.PI_SUP);
        assertEquals(expectedBits, actualBits);
    }

    /*
     * logarithms
     */
    
    public void test_log2_int() {
        for (int value : new int[]{Integer.MIN_VALUE,0}) {
            try {
                CmnFastMath.log2(value);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        for (int p=0;p<=30;p++) {
            int pot = (1<<p);

            if (p != 0) {
                assertEquals(p-1, CmnFastMath.log2(pot-1));
            }
            assertEquals(p, CmnFastMath.log2(pot));
            assertEquals(p, CmnFastMath.log2(pot+pot-1));
            if (p != 30) {
                assertEquals(p+1, CmnFastMath.log2(pot+pot));
            }
        }
    }

    public void test_log2_long() {
        for (long value : new long[]{Long.MIN_VALUE,0}) {
            try {
                CmnFastMath.log2(value);
                assertTrue(false);
            } catch (IllegalArgumentException e) {
                // ok
            }
        }

        for (int p=0;p<=62;p++) {
            long pot = (1L<<p);

            if (p != 0) {
                assertEquals(p-1, CmnFastMath.log2(pot-1));
            }
            assertEquals(p, CmnFastMath.log2(pot));
            assertEquals(p, CmnFastMath.log2(pot+pot-1));
            if (p != 62) {
                assertEquals(p+1, CmnFastMath.log2(pot+pot));
            }
        }
    }

    /*
     * powers
     */
    
    public void test_twoPow_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            int value = randomIntWhatever();
            double ref = StrictMath.pow(2,value);
            double res = CmnFastMath.twoPow(value);
            assertEquals(ref, res);
        }
    }

    public void test_pow2_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            int value = randomIntWhatever();
            int ref = value*value;
            int res = CmnFastMath.pow2(value);
            assertEquals(ref, res);
        }
    }

    public void test_pow2_long() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            long value = randomLongWhatever();
            long ref = value*value;
            long res = CmnFastMath.pow2(value);
            assertEquals(ref, res);
        }
    }

    public void test_pow3_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            int value = randomIntWhatever();
            int ref = value*value*value;
            int res = CmnFastMath.pow3(value);
            assertEquals(ref, res);
        }
    }

    public void test_pow3_long() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            long value = randomLongWhatever();
            long ref = value*value*value;
            long res = CmnFastMath.pow3(value);
            assertEquals(ref, res);
        }
    }
    
    /*
     * absolute values
     */

    public void test_abs_int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            int value = randomIntWhatever();
            int ref = Math.abs(value);
            int res = CmnFastMath.abs(value);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_abs_long() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            long value = randomLongWhatever();
            long ref = Math.abs(value);
            long res = CmnFastMath.abs(value);
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

        assertEquals(Integer.MAX_VALUE, CmnFastMath.toIntExact((long)Integer.MAX_VALUE));
        try {
            CmnFastMath.toIntExact(((long)Integer.MAX_VALUE)+1L);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_toInt_long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MAX_VALUE, CmnFastMath.toInt((long)Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, CmnFastMath.toInt(Long.MAX_VALUE));
    }

    /*
     * ranges
     */
    
    public void test_toRange_3int() {
        assertEquals(0, CmnFastMath.toRange(0, 2, -1));
        assertEquals(0, CmnFastMath.toRange(0, 2, 0));
        assertEquals(1, CmnFastMath.toRange(0, 2, 1));
        assertEquals(2, CmnFastMath.toRange(0, 2, 2));
        assertEquals(2, CmnFastMath.toRange(0, 2, 3));
    }

    public void test_toRange_3long() {
        assertEquals(0L, CmnFastMath.toRange(0L, 2L, -1L));
        assertEquals(0L, CmnFastMath.toRange(0L, 2L, 0L));
        assertEquals(1L, CmnFastMath.toRange(0L, 2L, 1L));
        assertEquals(2L, CmnFastMath.toRange(0L, 2L, 2L));
        assertEquals(2L, CmnFastMath.toRange(0L, 2L, 3L));
    }

    /*
     * unary operators (increment,decrement,negate)
     */

    public void test_incrementExact_int() {
        try {
            CmnFastMath.incrementExact(Integer.MAX_VALUE);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
        
        assertEquals(Integer.MIN_VALUE + 1, CmnFastMath.incrementExact(Integer.MIN_VALUE));
        assertEquals(Integer.MAX_VALUE, CmnFastMath.incrementExact(Integer.MAX_VALUE - 1));
        
        for (int v = -3; v <= 3; v++) {
            assertEquals(v + 1, CmnFastMath.incrementExact(v));
        }
    }

    public void test_incrementExact_long() {
        try {
            CmnFastMath.incrementExact(Long.MAX_VALUE);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
        
        assertEquals(Long.MIN_VALUE + 1L, CmnFastMath.incrementExact(Long.MIN_VALUE));
        assertEquals(Long.MAX_VALUE, CmnFastMath.incrementExact(Long.MAX_VALUE - 1L));
        
        for (long v = -3L; v <= 3L; v++) {
            assertEquals(v + 1L, CmnFastMath.incrementExact(v));
        }
    }

    public void test_incrementBounded_int() {
        assertEquals(Integer.MIN_VALUE + 1, CmnFastMath.incrementBounded(Integer.MIN_VALUE));
        assertEquals(Integer.MAX_VALUE, CmnFastMath.incrementBounded(Integer.MAX_VALUE - 1));
        assertEquals(Integer.MAX_VALUE, CmnFastMath.incrementBounded(Integer.MAX_VALUE));
        
        for (int v = -3; v <= 3; v++) {
            assertEquals(v + 1, CmnFastMath.incrementBounded(v));
        }
    }

    public void test_incrementBounded_long() {
        assertEquals(Long.MIN_VALUE + 1L, CmnFastMath.incrementBounded(Long.MIN_VALUE));
        assertEquals(Long.MAX_VALUE, CmnFastMath.incrementBounded(Long.MAX_VALUE - 1L));
        assertEquals(Long.MAX_VALUE, CmnFastMath.incrementBounded(Long.MAX_VALUE));
        
        for (long v = -3L; v <= 3L; v++) {
            assertEquals(v + 1L, CmnFastMath.incrementBounded(v));
        }
    }

    public void test_decrementExact_int() {
        try {
            CmnFastMath.decrementExact(Integer.MIN_VALUE);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
        
        assertEquals(Integer.MAX_VALUE - 1, CmnFastMath.decrementExact(Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, CmnFastMath.decrementExact(Integer.MIN_VALUE + 1));
        
        for (int v = -3; v <= 3; v++) {
            assertEquals(v - 1, CmnFastMath.decrementExact(v));
        }
    }

    public void test_decrementExact_long() {
        try {
            CmnFastMath.decrementExact(Long.MIN_VALUE);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
        
        assertEquals(Long.MAX_VALUE - 1L, CmnFastMath.decrementExact(Long.MAX_VALUE));
        assertEquals(Long.MIN_VALUE, CmnFastMath.decrementExact(Long.MIN_VALUE + 1L));
        
        for (long v = -3L; v <= 3L; v++) {
            assertEquals(v - 1L, CmnFastMath.decrementExact(v));
        }
    }

    public void test_decrementBounded_int() {
        assertEquals(Integer.MAX_VALUE - 1, CmnFastMath.decrementBounded(Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, CmnFastMath.decrementBounded(Integer.MIN_VALUE + 1));
        assertEquals(Integer.MIN_VALUE, CmnFastMath.decrementBounded(Integer.MIN_VALUE));
        
        for (int v = -3; v <= 3; v++) {
            assertEquals(v - 1, CmnFastMath.decrementBounded(v));
        }
    }

    public void test_decrementBounded_long() {
        assertEquals(Long.MAX_VALUE - 1L, CmnFastMath.decrementBounded(Long.MAX_VALUE));
        assertEquals(Long.MIN_VALUE, CmnFastMath.decrementBounded(Long.MIN_VALUE + 1L));
        assertEquals(Long.MIN_VALUE, CmnFastMath.decrementBounded(Long.MIN_VALUE));
        
        for (long v = -3L; v <= 3L; v++) {
            assertEquals(v - 1L, CmnFastMath.decrementBounded(v));
        }
    }

    public void test_negateExact_int() {
        try {
            CmnFastMath.negateExact(Integer.MIN_VALUE);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
        
        assertEquals(-Integer.MAX_VALUE, CmnFastMath.negateExact(Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, CmnFastMath.negateExact(Integer.MIN_VALUE + 1));
        
        for (int v = -3; v <= 3; v++) {
            assertEquals(-v, CmnFastMath.negateExact(v));
        }
    }

    public void test_negateExact_long() {
        try {
            CmnFastMath.negateExact(Long.MIN_VALUE);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
        
        assertEquals(-Long.MAX_VALUE, CmnFastMath.negateExact(Long.MAX_VALUE));
        assertEquals(Long.MAX_VALUE, CmnFastMath.negateExact(Long.MIN_VALUE + 1L));
        
        for (long v = -3L; v <= 3L; v++) {
            assertEquals(-v, CmnFastMath.negateExact(v));
        }
    }

    public void test_negateBounded_int() {
        assertEquals(-Integer.MAX_VALUE, CmnFastMath.negateBounded(Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, CmnFastMath.negateBounded(Integer.MIN_VALUE + 1));
        assertEquals(Integer.MAX_VALUE, CmnFastMath.negateBounded(Integer.MIN_VALUE));
        
        for (int v = -3; v <= 3; v++) {
            assertEquals(-v, CmnFastMath.negateBounded(v));
        }
    }

    public void test_negateBounded_long() {
        assertEquals(-Long.MAX_VALUE, CmnFastMath.negateBounded(Long.MAX_VALUE));
        assertEquals(Long.MAX_VALUE, CmnFastMath.negateBounded(Long.MIN_VALUE + 1L));
        assertEquals(Long.MAX_VALUE, CmnFastMath.negateBounded(Long.MIN_VALUE));
        
        for (long v = -3L; v <= 3L; v++) {
            assertEquals(-v, CmnFastMath.negateBounded(v));
        }
    }

    /*
     * binary operators (+,-,*)
     */

    public void test_addExact_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MAX_VALUE, CmnFastMath.addExact(Integer.MAX_VALUE-1, 1));
        try {
            CmnFastMath.addExact(Integer.MAX_VALUE, 1);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_addExact_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MAX_VALUE, CmnFastMath.addExact(Long.MAX_VALUE-1L, 1L));
        try {
            CmnFastMath.addExact(Long.MAX_VALUE, 1L);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_addBounded_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MAX_VALUE, CmnFastMath.addBounded(Integer.MAX_VALUE-1, 1));
        assertEquals(Integer.MAX_VALUE, CmnFastMath.addBounded(Integer.MAX_VALUE, 1));
    }

    public void test_addBounded_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MAX_VALUE, CmnFastMath.addBounded(Long.MAX_VALUE-1L, 1L));
        assertEquals(Long.MAX_VALUE, CmnFastMath.addBounded(Long.MAX_VALUE, 1L));
    }

    public void test_subtractExact_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MIN_VALUE, CmnFastMath.subtractExact(Integer.MIN_VALUE+1, 1));
        try {
            CmnFastMath.subtractExact(Integer.MIN_VALUE, 1);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_subtractExact_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MIN_VALUE, CmnFastMath.subtractExact(Long.MIN_VALUE+1L, 1L));
        try {
            CmnFastMath.subtractExact(Long.MIN_VALUE, 1L);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_subtractBounded_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MIN_VALUE, CmnFastMath.subtractBounded(Integer.MIN_VALUE+1, 1));
        assertEquals(Integer.MIN_VALUE, CmnFastMath.subtractBounded(Integer.MIN_VALUE, 1));
    }

    public void test_subtractBounded_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MIN_VALUE, CmnFastMath.subtractBounded(Long.MIN_VALUE+1L, 1L));
        assertEquals(Long.MIN_VALUE, CmnFastMath.subtractBounded(Long.MIN_VALUE, 1L));
    }

    public void test_multiplyExact_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MIN_VALUE, CmnFastMath.multiplyExact(Integer.MIN_VALUE/2, 2));
        try {
            CmnFastMath.multiplyExact(Integer.MIN_VALUE, 2);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_multiplyExact_long_int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MIN_VALUE, CmnFastMath.multiplyExact(Long.MIN_VALUE/2L, 2));
        try {
            CmnFastMath.multiplyExact(Long.MIN_VALUE, 2);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_multiplyExact_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MIN_VALUE, CmnFastMath.multiplyExact(Long.MIN_VALUE/2L, 2L));
        try {
            CmnFastMath.multiplyExact(Long.MIN_VALUE, 2L);
            assertTrue(false);
        } catch (ArithmeticException e) {
            // ok
        }
    }

    public void test_multiplyBounded_2int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Integer.MIN_VALUE, CmnFastMath.multiplyBounded(Integer.MIN_VALUE/2, 2));
        assertEquals(Integer.MIN_VALUE, CmnFastMath.multiplyBounded(Integer.MIN_VALUE, 2));
        assertEquals(Integer.MAX_VALUE, CmnFastMath.multiplyBounded(Integer.MAX_VALUE, 2));
    }

    public void test_multiplyBounded_long_int() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MIN_VALUE, CmnFastMath.multiplyBounded(Long.MIN_VALUE/2L, 2));
        assertEquals(Long.MIN_VALUE, CmnFastMath.multiplyBounded(Long.MIN_VALUE, 2));
        assertEquals(Long.MAX_VALUE, CmnFastMath.multiplyBounded(Long.MAX_VALUE, 2));
    }

    public void test_multiplyBounded_2long() {
        /*
         * quick test (delegates)
         */

        assertEquals(Long.MIN_VALUE, CmnFastMath.multiplyBounded(Long.MIN_VALUE/2L, 2L));
        assertEquals(Long.MIN_VALUE, CmnFastMath.multiplyBounded(Long.MIN_VALUE, 2L));
        assertEquals(Long.MAX_VALUE, CmnFastMath.multiplyBounded(Long.MAX_VALUE, 2L));
    }

    public void test_multiplyFull_2int() {
        final long LIMIN = Integer.MIN_VALUE;
        final long LIMAX = Integer.MAX_VALUE;
        assertEquals(NumbersUtils.timesExact(LIMIN, LIMIN), CmnFastMath.multiplyFull(Integer.MIN_VALUE, Integer.MIN_VALUE));
        assertEquals(NumbersUtils.timesExact(LIMAX, LIMIN), CmnFastMath.multiplyFull(Integer.MAX_VALUE, Integer.MIN_VALUE));
        assertEquals(NumbersUtils.timesExact(LIMIN, LIMAX), CmnFastMath.multiplyFull(Integer.MIN_VALUE, Integer.MAX_VALUE));
        assertEquals(NumbersUtils.timesExact(LIMAX, LIMAX), CmnFastMath.multiplyFull(Integer.MAX_VALUE, Integer.MAX_VALUE));
        assertEquals(2L * LIMAX, CmnFastMath.multiplyFull(2, Integer.MAX_VALUE));
        assertEquals(2L * LIMIN, CmnFastMath.multiplyFull(2, Integer.MIN_VALUE));
    }
    
    public void test_multiplyHigh_2long() {
        assertEquals(0L, CmnFastMath.multiplyHigh(1L, 1L));
        assertEquals(0L, CmnFastMath.multiplyHigh(1L, Long.MAX_VALUE));

        assertEquals(0L, CmnFastMath.multiplyHigh(-1L, -1L));
        assertEquals(0L, CmnFastMath.multiplyHigh(-1L, -Long.MAX_VALUE));
        assertEquals(0L, CmnFastMath.multiplyHigh(-1L, Long.MIN_VALUE));

        assertEquals(-1L, CmnFastMath.multiplyHigh(-1L, 1L));
        assertEquals(-1L, CmnFastMath.multiplyHigh(-1L, Long.MAX_VALUE));
        
        assertEquals(-1L, CmnFastMath.multiplyHigh(1L, -1L));
        assertEquals(-1L, CmnFastMath.multiplyHigh(1L, -Long.MAX_VALUE));
        assertEquals(-1L, CmnFastMath.multiplyHigh(1L, Long.MIN_VALUE));
        
        /*
         * Testing with one operand being a signed power of two.
         */
        
        for (long longPos : new long[]{
                0x1234567890ABCDEFL,
                0x7DCBA098E654321FL,
        }) {
            // Because the 128 bits of our complete results
            // are not multiples of 2^64.
            final long bonusBit = 1L;
            
            // pos = neg * neg
            check_multiplyHigh_2long_bothWays(
                    (longPos >>> 1),
                    -longPos,
                    Long.MIN_VALUE);

            // neg = pos * neg
            check_multiplyHigh_2long_bothWays(
                    -(longPos >>> 1) - bonusBit,
                    longPos,
                    Long.MIN_VALUE);

            for (int p = 2; p <= 63; p++) {
                for (long sign : new long[]{1L, -1L}) {
                    // pos = pos * pos = neg * neg
                    {
                        final long ref = (longPos >>> p);
                        check_multiplyHigh_2long_bothWays(
                                ref,
                                sign * longPos,
                                sign * (Long.MIN_VALUE >>> (p-1)));
                    }
                    // neg = neg * pos = pos * neg
                    {
                        final long ref = -(longPos >>> p) - bonusBit;
                        check_multiplyHigh_2long_bothWays(
                                ref,
                                (-sign) * longPos,
                                sign * (Long.MIN_VALUE >>> (p-1)));
                    }
                }
            }
        }
        
        /*
         * Testing with random values against BigDecimal.
         */
        
        final Random random = new Random(123456789L);
        final NumbersTestUtils utils = new NumbersTestUtils(random);
        for (int i = 0; i < 10 * 1000; i++) {
            final long v1 = utils.randomLongWhatever();
            final long v2 = utils.randomLongWhatever();
            
            final BigDecimal big1 = BigDecimal.valueOf(v1);
            final BigDecimal big2 = BigDecimal.valueOf(v2);
            final BigDecimal bigProd = big1.multiply(big2);
            final BigDecimal twoPow64 = new BigDecimal(new BigInteger("10000000000000000", 16));
            final BigDecimal bigRefD = bigProd.divide(twoPow64);

            final boolean isInteger = !bigRefD.toString().contains(".");
            
            final int signum = bigRefD.signum();
            
            final BigInteger bigRef;
            if ((!isInteger) && (signum < 0)) {
                bigRef = bigRefD.toBigInteger().subtract(BigInteger.valueOf(1L));
            } else {
                bigRef = bigRefD.toBigInteger();
            }
            
            final String refStr = bigRef.toString();

            final String resStr = Long.toString(CmnFastMath.multiplyHigh(v1, v2));
            
            if (!refStr.equals(resStr)) {
                System.out.println();
                System.out.println("v1 = " + v1);
                System.out.println("v2 = " + v2);
                System.out.println("bigProd = " + bigProd);
                System.out.println("twoPow64 = " + twoPow64);
                System.out.println("bigRefD = " + bigRefD);
                System.out.println("isInteger = " + isInteger);
                System.out.println("signum = " + signum);
                System.out.println("ref = " + refStr);
                System.out.println("res = " + resStr);
                assertTrue(false);
            }
        }
    }

    /*
     * binary operators (/,%)
     */

    public void test_floorDiv_2int() {
        assertEquals(Integer.MIN_VALUE, CmnFastMath.floorDiv(Integer.MIN_VALUE,-1));
        
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final int x = randomIntWhatever();
            final int y = randomIntWhatever();
            if (y == 0) {
                try {
                    CmnFastMath.floorDiv(x,y);
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
                final int actual = CmnFastMath.floorDiv(x,y);
                assertEquals(expected, actual);
            }
        }
    }

    public void test_floorDiv_long_int() {
        assertEquals(Long.MIN_VALUE, CmnFastMath.floorDiv(Long.MIN_VALUE,-1));
        
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final long x = randomLongWhatever();
            final int y = randomIntWhatever();
            if (y == 0) {
                try {
                    CmnFastMath.floorDiv(x,y);
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
                final long actual = CmnFastMath.floorDiv(x,y);
                assertEquals(expected, actual);
            }
        }
    }

    public void test_floorDiv_2long() {
        assertEquals(Long.MIN_VALUE, CmnFastMath.floorDiv(Long.MIN_VALUE,-1L));
        
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final long x = randomLongWhatever();
            final long y = randomLongWhatever();
            if (y == 0) {
                try {
                    CmnFastMath.floorDiv(x,y);
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
                final long actual = CmnFastMath.floorDiv(x,y);
                assertEquals(expected, actual);
            }
        }
    }

    public void test_floorMod_2int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final int x = randomIntWhatever();
            final int y = randomIntWhatever();
            if (y == 0) {
                try {
                    CmnFastMath.floorMod(x,y);
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
                final int actual = CmnFastMath.floorMod(x,y);
                assertEquals(expected, actual);
                
                // identity
                assertEquals(x - CmnFastMath.floorDiv(x, y) * y, actual);
            }
        }
    }

    public void test_floorMod_long_int() {
        assertEquals(Integer.MIN_VALUE+2, CmnFastMath.floorMod(Long.MIN_VALUE+2L, Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE+1, CmnFastMath.floorMod(Long.MIN_VALUE+1L, Integer.MIN_VALUE));
        assertEquals(0, CmnFastMath.floorMod(Long.MIN_VALUE, Integer.MIN_VALUE));
        //
        assertEquals(-3, CmnFastMath.floorMod(Long.MAX_VALUE-2L, Integer.MIN_VALUE));
        assertEquals(-2, CmnFastMath.floorMod(Long.MAX_VALUE-1L, Integer.MIN_VALUE));
        assertEquals(-1, CmnFastMath.floorMod(Long.MAX_VALUE, Integer.MIN_VALUE));
        //
        assertEquals(1, CmnFastMath.floorMod(Long.MIN_VALUE+3L, Integer.MAX_VALUE));
        assertEquals(0, CmnFastMath.floorMod(Long.MIN_VALUE+2L, Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE-1, CmnFastMath.floorMod(Long.MIN_VALUE+1L, Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE-2, CmnFastMath.floorMod(Long.MIN_VALUE, Integer.MAX_VALUE));
        //
        assertEquals(Integer.MAX_VALUE-2, CmnFastMath.floorMod(Long.MAX_VALUE-3L, Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE-1, CmnFastMath.floorMod(Long.MAX_VALUE-2L, Integer.MAX_VALUE));
        assertEquals(0, CmnFastMath.floorMod(Long.MAX_VALUE-1L, Integer.MAX_VALUE));
        assertEquals(1, CmnFastMath.floorMod(Long.MAX_VALUE, Integer.MAX_VALUE));
        
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final long x = randomLongWhatever();
            final int y = randomIntWhatever();
            if (y == 0) {
                try {
                    CmnFastMath.floorMod(x,y);
                    assertTrue(false);
                } catch (ArithmeticException e) {
                    // ok
                }
            } else {
                final long theoretical;
                final boolean exact = ((x/y)*y == x);
                if (exact || ((x^y) >= 0)) {
                    // exact or same sign
                    theoretical = x%y;
                } else {
                    // different signs and not exact
                    theoretical = x%y + y;
                }
                // Never overflows.
                final int expected = NumbersUtils.asInt(theoretical);
                final int actual = CmnFastMath.floorMod(x,y);
                assertEquals(expected, actual);
                    
                // identity
                assertEquals(x - CmnFastMath.floorDiv(x, y) * y, actual);
            }
        }
    }

    public void test_floorMod_2long() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            final long x = randomIntWhatever();
            final long y = randomIntWhatever();
            if (y == 0) {
                try {
                    CmnFastMath.floorMod(x,y);
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
                long actual = CmnFastMath.floorMod(x,y);
                assertEquals(expected, actual);
                
                // identity
                assertEquals(x - CmnFastMath.floorDiv(x, y) * y, actual);
            }
        }
    }
    
    /*
     * Non-redefined public values and treatments.
     */
    
    public void test_min_2int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            int a = randomIntWhatever();
            int b = randomIntWhatever();
            int ref = Math.min(a,b);
            int res = CmnFastMath.min(a,b);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_min_2long() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            long a = randomLongWhatever();
            long b = randomLongWhatever();
            long ref = Math.min(a,b);
            long res = CmnFastMath.min(a,b);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_max_2int() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            int a = randomIntWhatever();
            int b = randomIntWhatever();
            int ref = Math.max(a,b);
            int res = CmnFastMath.max(a,b);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    public void test_max_2long() {
        for (int i=0;i<NBR_OF_VALUES_SMALL;i++) {
            long a = randomLongWhatever();
            long b = randomLongWhatever();
            long ref = Math.max(a,b);
            long res = CmnFastMath.max(a,b);
            boolean ok = (ref == res);
            assertTrue(ok);
        }
    }

    //--------------------------------------------------------------------------
    // PRIVATE METHODS
    //--------------------------------------------------------------------------
    
    private static void check_multiplyHigh_2long_bothWays(long ref, long v1, long v2) {
        check_multiplyHigh_2long(ref, v1, v2);
        check_multiplyHigh_2long(ref, v2, v1);
    }
    
    private static void check_multiplyHigh_2long(long ref, long v1, long v2) {
        final long res = CmnFastMath.multiplyHigh(v1, v2);
        if (res != ref) {
            System.out.print("v1 = " + NumbersUtils.toStringBits(v1));
            System.out.println(" v2 = " + NumbersUtils.toStringBits(v2));
            System.out.println("ref = " + NumbersUtils.toStringBits(ref));
            System.out.println("res = " + NumbersUtils.toStringBits(res));
            System.out.print("v1 = " + v1);
            System.out.println(" v2 = " + v2);
            System.out.println("ref = " + ref);
            System.out.println("res = " + res);
        }
        assertEquals(ref, res);
    }
}
