package odk.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

public class TestNumbersUtilsForJUnit extends TestCase {

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

    //--------------------------------------------------------------------------
    // PUBLIC METHODS
    //--------------------------------------------------------------------------
    
    /*
     * min/max ranges
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
        assertTrue(NumbersUtils.checkIsInRange(3, 7, 5));
        
        try {
            NumbersUtils.checkIsInRange(3, 7, 2);
            assertTrue(false);
        } catch (Exception e) {
        }
    }
    
    public void test_checkIsInRange_3long() {
        assertTrue(NumbersUtils.checkIsInRange(3L, 7L, 5L));
        
        try {
            NumbersUtils.checkIsInRange(3L, 7L, 2L);
            assertTrue(false);
        } catch (Exception e) {
        }
    }
    
    public void test_checkIsInRange_3float() {
        assertTrue(NumbersUtils.checkIsInRange(3.f, 7.f, 5.f));
        
        try {
            NumbersUtils.checkIsInRange(3.f, 7.f, 2.f);
            assertTrue(false);
        } catch (Exception e) {
        }
    }
    
    public void test_checkIsInRange_3double() {
        assertTrue(NumbersUtils.checkIsInRange(3., 7., 5.));
        
        try {
            NumbersUtils.checkIsInRange(3., 7., 2.);
            assertTrue(false);
        } catch (Exception e) {
        }
    }

    /*
     * bitwise ranges
     */

    public void test_isInRangeSigned_int_int() {
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
        assertTrue(NumbersUtils.checkIsInRangeSigned(0, 8));
        
        try {
            NumbersUtils.checkIsInRangeSigned(128, 8);
            assertTrue(false);
        } catch (Exception e) {
        }
    }
    
    public void test_checkIsInRangeSigned_long_int() {
        assertTrue(NumbersUtils.checkIsInRangeSigned(0L, 8));
        
        try {
            NumbersUtils.checkIsInRangeSigned(128L, 8);
            assertTrue(false);
        } catch (Exception e) {
        }
    }
    
    public void test_checkIsInRangeUnsigned_int_int() {
        assertTrue(NumbersUtils.checkIsInRangeUnsigned(0, 8));
        
        try {
            NumbersUtils.checkIsInRangeUnsigned(-1, 8);
            assertTrue(false);
        } catch (Exception e) {
        }
    }
    
    public void test_checkIsInRangeUnsigned_long_int() {
        assertTrue(NumbersUtils.checkIsInRangeUnsigned(0L, 8));
        
        try {
            NumbersUtils.checkIsInRangeUnsigned(-1L, 8);
            assertTrue(false);
        } catch (Exception e) {
        }
    }
    
    /*
     * masks
     */

    public void test_leftBit0IntMask_int() {
        assertEquals(0xFFFFFFFF,NumbersUtils.leftBit0IntMask(0));
        assertEquals(0x7FFFFFFF,NumbersUtils.leftBit0IntMask(1));
        assertEquals(1,NumbersUtils.leftBit0IntMask(31));
        assertEquals(0,NumbersUtils.leftBit0IntMask(32));
    }
    
    public void test_leftBit1IntMask_int() {
        assertEquals(0,NumbersUtils.leftBit1IntMask(0));
        assertEquals(0x80000000,NumbersUtils.leftBit1IntMask(1));
        assertEquals(0xFFFFFFFE,NumbersUtils.leftBit1IntMask(31));
        assertEquals(0xFFFFFFFF,NumbersUtils.leftBit1IntMask(32));
    }

    public void test_rightBit0IntMask_int() {
        assertEquals(0xFFFFFFFF,NumbersUtils.rightBit0IntMask(0));
        assertEquals(0xFFFFFFFE,NumbersUtils.rightBit0IntMask(1));
        assertEquals(0x80000000,NumbersUtils.rightBit0IntMask(31));
        assertEquals(0,NumbersUtils.rightBit0IntMask(32));
    }

    public void test_rightBit1IntMask_int() {
        assertEquals(0,NumbersUtils.rightBit1IntMask(0));
        assertEquals(1,NumbersUtils.rightBit1IntMask(1));
        assertEquals(0x7FFFFFFF,NumbersUtils.rightBit1IntMask(31));
        assertEquals(0xFFFFFFFF,NumbersUtils.rightBit1IntMask(32));
    }

    /*
     * 
     */
    
    public void test_leftBit0LongMask_int() {
        assertEquals(0xFFFFFFFFFFFFFFFFL,NumbersUtils.leftBit0LongMask(0));
        assertEquals(0x7FFFFFFFFFFFFFFFL,NumbersUtils.leftBit0LongMask(1));
        assertEquals(1L,NumbersUtils.leftBit0LongMask(63));
        assertEquals(0L,NumbersUtils.leftBit0LongMask(64));
    }
    
    public void test_leftBit1LongMask_int() {
        assertEquals(0L,NumbersUtils.leftBit1LongMask(0));
        assertEquals(0x8000000000000000L,NumbersUtils.leftBit1LongMask(1));
        assertEquals(0xFFFFFFFFFFFFFFFEL,NumbersUtils.leftBit1LongMask(63));
        assertEquals(0xFFFFFFFFFFFFFFFFL,NumbersUtils.leftBit1LongMask(64));
    }

    public void test_rightBit0LongMask_int() {
        assertEquals(0xFFFFFFFFFFFFFFFFL,NumbersUtils.rightBit0LongMask(0));
        assertEquals(0xFFFFFFFFFFFFFFFEL,NumbersUtils.rightBit0LongMask(1));
        assertEquals(0x8000000000000000L,NumbersUtils.rightBit0LongMask(63));
        assertEquals(0L,NumbersUtils.rightBit0LongMask(64));
    }

    public void test_rightBit1LongMask_int() {
        assertEquals(0L,NumbersUtils.rightBit1LongMask(0));
        assertEquals(1L,NumbersUtils.rightBit1LongMask(1));
        assertEquals(0x7FFFFFFFFFFFFFFFL,NumbersUtils.rightBit1LongMask(63));
        assertEquals(0xFFFFFFFFFFFFFFFFL,NumbersUtils.rightBit1LongMask(64));
    }

    /*
     * bitwise ranges
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
        } catch (Exception e) {
        }
    }

    public void test_checkBitSizeForSignedLong_int() {
        assertEquals(true,NumbersUtils.checkBitSizeForSignedLong(64));
        
        try {
            NumbersUtils.checkBitSizeForSignedLong(65);
            assertTrue(false);
        } catch (Exception e) {
        }
    }

    public void test_checkBitSizeForUnsignedInt_int() {
        assertEquals(true,NumbersUtils.checkBitSizeForUnsignedInt(31));
        
        try {
            NumbersUtils.checkBitSizeForUnsignedInt(32);
            assertTrue(false);
        } catch (Exception e) {
        }
    }
    
    public void test_checkBitSizeForUnsignedLong_int() {
        assertEquals(true,NumbersUtils.checkBitSizeForUnsignedLong(63));
        
        try {
            NumbersUtils.checkBitSizeForUnsignedLong(64);
            assertTrue(false);
        } catch (Exception e) {
        }
    }

    /*
     * 
     */
    
    public void test_minSignedIntForBitSize_int() {
        assertEquals(Integer.MIN_VALUE,NumbersUtils.minSignedIntForBitSize(32));
        assertEquals(Integer.MIN_VALUE>>1,NumbersUtils.minSignedIntForBitSize(31));
        assertEquals(-1,NumbersUtils.minSignedIntForBitSize(1));
    }

    public void test_maxSignedIntForBitSize_int() {
        assertEquals(Integer.MAX_VALUE,NumbersUtils.maxSignedIntForBitSize(32));
        assertEquals(Integer.MAX_VALUE>>1,NumbersUtils.maxSignedIntForBitSize(31));
        assertEquals(0,NumbersUtils.maxSignedIntForBitSize(1));
    }
    
    public void test_minSignedLongForBitSize_int() {
        assertEquals(Long.MIN_VALUE,NumbersUtils.minSignedLongForBitSize(64));
        assertEquals(Long.MIN_VALUE>>1,NumbersUtils.minSignedLongForBitSize(63));
        assertEquals(-1L,NumbersUtils.minSignedLongForBitSize(1));
    }

    public void test_maxSignedLongForBitSize_int() {
        assertEquals(Long.MAX_VALUE,NumbersUtils.maxSignedLongForBitSize(64));
        assertEquals(Long.MAX_VALUE>>1,NumbersUtils.maxSignedLongForBitSize(63));
        assertEquals(0L,NumbersUtils.maxSignedLongForBitSize(1));
    }
    
    public void test_maxUnsignedIntForBitSize_int() {
        assertEquals(Integer.MAX_VALUE,NumbersUtils.maxUnsignedIntForBitSize(31));
        assertEquals(Integer.MAX_VALUE>>1,NumbersUtils.maxUnsignedIntForBitSize(30));
        assertEquals(1,NumbersUtils.maxUnsignedIntForBitSize(1));
    }

    public void test_maxUnsignedLongForBitSize_int() {
        assertEquals(Long.MAX_VALUE,NumbersUtils.maxUnsignedLongForBitSize(63));
        assertEquals(Long.MAX_VALUE>>1,NumbersUtils.maxUnsignedLongForBitSize(62));
        assertEquals(1L,NumbersUtils.maxUnsignedLongForBitSize(1));
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
     * integer functions
     */
    
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
    
    /**
     * @param a A value.
     * @param b A value.
     * @return True if the specified values are both >= 0 or both < 0, false otherwise.
     */
    public static boolean haveSameSign(int a, int b) {
        return ((a^b) < 0);
    }

    /**
     * @param a A value.
     * @param b A value.
     * @return True if the specified values are both >= 0 or both < 0, false otherwise.
     */
    public static boolean haveSameSign(long a, long b) {
        return ((a^b) < 0);
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
         * Integer.MIW_VALUE power
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
         * Long.MIW_VALUE power
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
        ArrayList<Integer> values = new ArrayList<Integer>();
        values.add(Integer.MIN_VALUE);
        values.add(Integer.MIN_VALUE+1);
        values.add(Integer.MIN_VALUE+2);
        values.add(Integer.MIN_VALUE+3);
        values.add(-3);
        values.add(-2);
        values.add(-1);
        values.add(0);
        values.add(1);
        values.add(2);
        values.add(3);
        values.add(Integer.MAX_VALUE-1);
        values.add(Integer.MAX_VALUE-2);
        values.add(Integer.MAX_VALUE-3);
        values.add(Integer.MAX_VALUE);
        for (Integer value : values) {
            assertEquals(Math.abs(value), NumbersUtils.abs(value));
        }
    }

    public void test_abs_long() {
        ArrayList<Long> values = new ArrayList<Long>();
        values.add(Long.MIN_VALUE);
        values.add(Long.MIN_VALUE+1);
        values.add(Long.MIN_VALUE+2);
        values.add(Long.MIN_VALUE+3);
        values.add(-3L);
        values.add(-2L);
        values.add(-1L);
        values.add(0L);
        values.add(1L);
        values.add(2L);
        values.add(3L);
        values.add(Long.MAX_VALUE-1);
        values.add(Long.MAX_VALUE-2);
        values.add(Long.MAX_VALUE-3);
        values.add(Long.MAX_VALUE);
        for (Long value : values) {
            assertEquals(Math.abs(value), NumbersUtils.abs(value));
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

    public void test_toIntSafe_long() {
        try {
            assertEquals(Integer.MIN_VALUE, NumbersUtils.toIntSafe(Long.MIN_VALUE));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Integer.MIN_VALUE, NumbersUtils.toIntSafe(Integer.MIN_VALUE-1L));
            assertTrue(false);
        } catch (Exception e) {
        }
        assertEquals(Integer.MIN_VALUE, NumbersUtils.toIntSafe((long)Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.toIntSafe(Integer.MIN_VALUE+1L));
        for (long a=-100;a<=100;a++) {
            assertEquals(a, NumbersUtils.toIntSafe(a));
        }
        assertEquals(Integer.MAX_VALUE-1, NumbersUtils.toIntSafe(Integer.MAX_VALUE-1L));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.toIntSafe((long)Integer.MAX_VALUE));
        try {
            assertEquals(Integer.MAX_VALUE, NumbersUtils.toIntSafe(Integer.MAX_VALUE+1L));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Integer.MAX_VALUE, NumbersUtils.toIntSafe(Long.MAX_VALUE));
            assertTrue(false);
        } catch (Exception e) {
        }
    }

    public void test_plusNoModulo_2int() {
        assertEquals(Integer.MIN_VALUE, NumbersUtils.plusNoModulo(Integer.MIN_VALUE,Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE, NumbersUtils.plusNoModulo(Integer.MIN_VALUE,-1));
        //
        assertEquals(Integer.MIN_VALUE, NumbersUtils.plusNoModulo(Integer.MIN_VALUE,0));
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.plusNoModulo(Integer.MIN_VALUE,1));
        assertEquals(Integer.MIN_VALUE+2, NumbersUtils.plusNoModulo(Integer.MIN_VALUE,2));
        assertEquals(Integer.MIN_VALUE+3, NumbersUtils.plusNoModulo(Integer.MIN_VALUE,3));
        assertEquals(-4, NumbersUtils.plusNoModulo(Integer.MIN_VALUE,Integer.MAX_VALUE-3));
        assertEquals(-3, NumbersUtils.plusNoModulo(Integer.MIN_VALUE,Integer.MAX_VALUE-2));
        assertEquals(-2, NumbersUtils.plusNoModulo(Integer.MIN_VALUE,Integer.MAX_VALUE-1));
        assertEquals(-1, NumbersUtils.plusNoModulo(Integer.MIN_VALUE,Integer.MAX_VALUE));
        for (int a=-100;a<=100;a++) {
            for (int b=-100;b<=100;b++) {
                assertEquals(a+b, NumbersUtils.plusNoModulo(a,b));
            }
        }
        assertEquals(-1, NumbersUtils.plusNoModulo(Integer.MAX_VALUE,Integer.MIN_VALUE));
        assertEquals(0, NumbersUtils.plusNoModulo(Integer.MAX_VALUE,Integer.MIN_VALUE+1));
        assertEquals(1, NumbersUtils.plusNoModulo(Integer.MAX_VALUE,Integer.MIN_VALUE+2));
        assertEquals(2, NumbersUtils.plusNoModulo(Integer.MAX_VALUE,Integer.MIN_VALUE+3));
        assertEquals(Integer.MAX_VALUE-3, NumbersUtils.plusNoModulo(Integer.MAX_VALUE,-3));
        assertEquals(Integer.MAX_VALUE-2, NumbersUtils.plusNoModulo(Integer.MAX_VALUE,-2));
        assertEquals(Integer.MAX_VALUE-1, NumbersUtils.plusNoModulo(Integer.MAX_VALUE,-1));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.plusNoModulo(Integer.MAX_VALUE,0));
        //
        assertEquals(Integer.MAX_VALUE, NumbersUtils.plusNoModulo(Integer.MAX_VALUE,1));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.plusNoModulo(Integer.MAX_VALUE,Integer.MAX_VALUE));
    }

    public void test_plusNoModuloSafe_2int() {
        try {
            assertEquals(Integer.MIN_VALUE, NumbersUtils.plusNoModuloSafe(Integer.MIN_VALUE,Integer.MIN_VALUE));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Integer.MIN_VALUE, NumbersUtils.plusNoModuloSafe(Integer.MIN_VALUE,-1));
            assertTrue(false);
        } catch (Exception e) {
        }
        //
        assertEquals(Integer.MIN_VALUE, NumbersUtils.plusNoModuloSafe(Integer.MIN_VALUE,0));
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.plusNoModuloSafe(Integer.MIN_VALUE,1));
        assertEquals(Integer.MIN_VALUE+2, NumbersUtils.plusNoModuloSafe(Integer.MIN_VALUE,2));
        assertEquals(Integer.MIN_VALUE+3, NumbersUtils.plusNoModuloSafe(Integer.MIN_VALUE,3));
        assertEquals(-4, NumbersUtils.plusNoModuloSafe(Integer.MIN_VALUE,Integer.MAX_VALUE-3));
        assertEquals(-3, NumbersUtils.plusNoModuloSafe(Integer.MIN_VALUE,Integer.MAX_VALUE-2));
        assertEquals(-2, NumbersUtils.plusNoModuloSafe(Integer.MIN_VALUE,Integer.MAX_VALUE-1));
        assertEquals(-1, NumbersUtils.plusNoModuloSafe(Integer.MIN_VALUE,Integer.MAX_VALUE));
        for (int a=-100;a<=100;a++) {
            for (int b=-100;b<=100;b++) {
                assertEquals(a+b, NumbersUtils.plusNoModuloSafe(a,b));
            }
        }
        assertEquals(-1, NumbersUtils.plusNoModuloSafe(Integer.MAX_VALUE,Integer.MIN_VALUE));
        assertEquals(0, NumbersUtils.plusNoModuloSafe(Integer.MAX_VALUE,Integer.MIN_VALUE+1));
        assertEquals(1, NumbersUtils.plusNoModuloSafe(Integer.MAX_VALUE,Integer.MIN_VALUE+2));
        assertEquals(2, NumbersUtils.plusNoModuloSafe(Integer.MAX_VALUE,Integer.MIN_VALUE+3));
        assertEquals(Integer.MAX_VALUE-3, NumbersUtils.plusNoModuloSafe(Integer.MAX_VALUE,-3));
        assertEquals(Integer.MAX_VALUE-2, NumbersUtils.plusNoModuloSafe(Integer.MAX_VALUE,-2));
        assertEquals(Integer.MAX_VALUE-1, NumbersUtils.plusNoModuloSafe(Integer.MAX_VALUE,-1));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.plusNoModuloSafe(Integer.MAX_VALUE,0));
        //
        try {
            assertEquals(Integer.MAX_VALUE, NumbersUtils.plusNoModuloSafe(Integer.MAX_VALUE,1));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Integer.MAX_VALUE, NumbersUtils.plusNoModuloSafe(Integer.MAX_VALUE,Integer.MAX_VALUE));
            assertTrue(false);
        } catch (Exception e) {
        }
    }

    public void test_plusNoModulo_2long() {
        assertEquals(Long.MIN_VALUE, NumbersUtils.plusNoModulo(Long.MIN_VALUE,Long.MIN_VALUE));
        assertEquals(Long.MIN_VALUE, NumbersUtils.plusNoModulo(Long.MIN_VALUE,-1));
        //
        assertEquals(Long.MIN_VALUE, NumbersUtils.plusNoModulo(Long.MIN_VALUE,0));
        assertEquals(Long.MIN_VALUE+1, NumbersUtils.plusNoModulo(Long.MIN_VALUE,1));
        assertEquals(Long.MIN_VALUE+2, NumbersUtils.plusNoModulo(Long.MIN_VALUE,2));
        assertEquals(Long.MIN_VALUE+3, NumbersUtils.plusNoModulo(Long.MIN_VALUE,3));
        assertEquals(-4, NumbersUtils.plusNoModulo(Long.MIN_VALUE,Long.MAX_VALUE-3));
        assertEquals(-3, NumbersUtils.plusNoModulo(Long.MIN_VALUE,Long.MAX_VALUE-2));
        assertEquals(-2, NumbersUtils.plusNoModulo(Long.MIN_VALUE,Long.MAX_VALUE-1));
        assertEquals(-1, NumbersUtils.plusNoModulo(Long.MIN_VALUE,Long.MAX_VALUE));
        for (long a=-100;a<=100;a++) {
            for (long b=-100;b<=100;b++) {
                assertEquals(a+b, NumbersUtils.plusNoModulo(a,b));
            }
        }
        assertEquals(-1, NumbersUtils.plusNoModulo(Long.MAX_VALUE,Long.MIN_VALUE));
        assertEquals(0, NumbersUtils.plusNoModulo(Long.MAX_VALUE,Long.MIN_VALUE+1));
        assertEquals(1, NumbersUtils.plusNoModulo(Long.MAX_VALUE,Long.MIN_VALUE+2));
        assertEquals(2, NumbersUtils.plusNoModulo(Long.MAX_VALUE,Long.MIN_VALUE+3));
        assertEquals(Long.MAX_VALUE-3, NumbersUtils.plusNoModulo(Long.MAX_VALUE,-3));
        assertEquals(Long.MAX_VALUE-2, NumbersUtils.plusNoModulo(Long.MAX_VALUE,-2));
        assertEquals(Long.MAX_VALUE-1, NumbersUtils.plusNoModulo(Long.MAX_VALUE,-1));
        assertEquals(Long.MAX_VALUE, NumbersUtils.plusNoModulo(Long.MAX_VALUE,0));
        //
        assertEquals(Long.MAX_VALUE, NumbersUtils.plusNoModulo(Long.MAX_VALUE,1));
        assertEquals(Long.MAX_VALUE, NumbersUtils.plusNoModulo(Long.MAX_VALUE,Long.MAX_VALUE));
    }

    public void test_plusNoModuloSafe_2long() {
        try {
            assertEquals(Long.MIN_VALUE, NumbersUtils.plusNoModuloSafe(Long.MIN_VALUE,Long.MIN_VALUE));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Long.MIN_VALUE, NumbersUtils.plusNoModuloSafe(Long.MIN_VALUE,-1));
            assertTrue(false);
        } catch (Exception e) {
        }
        //
        assertEquals(Long.MIN_VALUE, NumbersUtils.plusNoModuloSafe(Long.MIN_VALUE,0));
        assertEquals(Long.MIN_VALUE+1, NumbersUtils.plusNoModuloSafe(Long.MIN_VALUE,1));
        assertEquals(Long.MIN_VALUE+2, NumbersUtils.plusNoModuloSafe(Long.MIN_VALUE,2));
        assertEquals(Long.MIN_VALUE+3, NumbersUtils.plusNoModuloSafe(Long.MIN_VALUE,3));
        assertEquals(-4, NumbersUtils.plusNoModuloSafe(Long.MIN_VALUE,Long.MAX_VALUE-3));
        assertEquals(-3, NumbersUtils.plusNoModuloSafe(Long.MIN_VALUE,Long.MAX_VALUE-2));
        assertEquals(-2, NumbersUtils.plusNoModuloSafe(Long.MIN_VALUE,Long.MAX_VALUE-1));
        assertEquals(-1, NumbersUtils.plusNoModuloSafe(Long.MIN_VALUE,Long.MAX_VALUE));
        for (long a=-100;a<=100;a++) {
            for (long b=-100;b<=100;b++) {
                assertEquals(a+b, NumbersUtils.plusNoModuloSafe(a,b));
            }
        }
        assertEquals(-1, NumbersUtils.plusNoModuloSafe(Long.MAX_VALUE,Long.MIN_VALUE));
        assertEquals(0, NumbersUtils.plusNoModuloSafe(Long.MAX_VALUE,Long.MIN_VALUE+1));
        assertEquals(1, NumbersUtils.plusNoModuloSafe(Long.MAX_VALUE,Long.MIN_VALUE+2));
        assertEquals(2, NumbersUtils.plusNoModuloSafe(Long.MAX_VALUE,Long.MIN_VALUE+3));
        assertEquals(Long.MAX_VALUE-3, NumbersUtils.plusNoModuloSafe(Long.MAX_VALUE,-3));
        assertEquals(Long.MAX_VALUE-2, NumbersUtils.plusNoModuloSafe(Long.MAX_VALUE,-2));
        assertEquals(Long.MAX_VALUE-1, NumbersUtils.plusNoModuloSafe(Long.MAX_VALUE,-1));
        assertEquals(Long.MAX_VALUE, NumbersUtils.plusNoModuloSafe(Long.MAX_VALUE,0));
        //
        try {
            assertEquals(Long.MAX_VALUE, NumbersUtils.plusNoModuloSafe(Long.MAX_VALUE,1));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Long.MAX_VALUE, NumbersUtils.plusNoModuloSafe(Long.MAX_VALUE,Long.MAX_VALUE));
            assertTrue(false);
        } catch (Exception e) {
        }
    }

    public void test_minusNoModulo_2int() {
        assertEquals(Integer.MIN_VALUE, NumbersUtils.minusNoModulo(Integer.MIN_VALUE,Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE, NumbersUtils.minusNoModulo(Integer.MIN_VALUE,1));
        //
        assertEquals(Integer.MIN_VALUE, NumbersUtils.minusNoModulo(Integer.MIN_VALUE,0));
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.minusNoModulo(Integer.MIN_VALUE,-1));
        assertEquals(Integer.MIN_VALUE+2, NumbersUtils.minusNoModulo(Integer.MIN_VALUE,-2));
        assertEquals(Integer.MIN_VALUE+3, NumbersUtils.minusNoModulo(Integer.MIN_VALUE,-3));
        assertEquals(-3, NumbersUtils.minusNoModulo(Integer.MIN_VALUE,Integer.MIN_VALUE+3));
        assertEquals(-2, NumbersUtils.minusNoModulo(Integer.MIN_VALUE,Integer.MIN_VALUE+2));
        assertEquals(-1, NumbersUtils.minusNoModulo(Integer.MIN_VALUE,Integer.MIN_VALUE+1));
        assertEquals(0, NumbersUtils.minusNoModulo(Integer.MIN_VALUE,Integer.MIN_VALUE));
        for (int a=-100;a<=100;a++) {
            for (int b=-100;b<=100;b++) {
                assertEquals(a-b, NumbersUtils.minusNoModulo(a,b));
            }
        }
        assertEquals(0, NumbersUtils.minusNoModulo(Integer.MAX_VALUE,Integer.MAX_VALUE));
        assertEquals(1, NumbersUtils.minusNoModulo(Integer.MAX_VALUE,Integer.MAX_VALUE-1));
        assertEquals(2, NumbersUtils.minusNoModulo(Integer.MAX_VALUE,Integer.MAX_VALUE-2));
        assertEquals(3, NumbersUtils.minusNoModulo(Integer.MAX_VALUE,Integer.MAX_VALUE-3));
        assertEquals(Integer.MAX_VALUE-3, NumbersUtils.minusNoModulo(Integer.MAX_VALUE,3));
        assertEquals(Integer.MAX_VALUE-2, NumbersUtils.minusNoModulo(Integer.MAX_VALUE,2));
        assertEquals(Integer.MAX_VALUE-1, NumbersUtils.minusNoModulo(Integer.MAX_VALUE,1));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.minusNoModulo(Integer.MAX_VALUE,0));
        //
        assertEquals(Integer.MAX_VALUE, NumbersUtils.minusNoModulo(Integer.MAX_VALUE,-1));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.minusNoModulo(Integer.MAX_VALUE,Integer.MIN_VALUE));
    }

    public void test_minusNoModuloSafe_2int() {
        try {
            assertEquals(Integer.MIN_VALUE, NumbersUtils.minusNoModuloSafe(Integer.MIN_VALUE,Integer.MAX_VALUE));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Integer.MIN_VALUE, NumbersUtils.minusNoModuloSafe(Integer.MIN_VALUE,1));
            assertTrue(false);
        } catch (Exception e) {
        }
        //
        assertEquals(Integer.MIN_VALUE, NumbersUtils.minusNoModuloSafe(Integer.MIN_VALUE,0));
        assertEquals(Integer.MIN_VALUE+1, NumbersUtils.minusNoModuloSafe(Integer.MIN_VALUE,-1));
        assertEquals(Integer.MIN_VALUE+2, NumbersUtils.minusNoModuloSafe(Integer.MIN_VALUE,-2));
        assertEquals(Integer.MIN_VALUE+3, NumbersUtils.minusNoModuloSafe(Integer.MIN_VALUE,-3));
        assertEquals(-3, NumbersUtils.minusNoModuloSafe(Integer.MIN_VALUE,Integer.MIN_VALUE+3));
        assertEquals(-2, NumbersUtils.minusNoModuloSafe(Integer.MIN_VALUE,Integer.MIN_VALUE+2));
        assertEquals(-1, NumbersUtils.minusNoModuloSafe(Integer.MIN_VALUE,Integer.MIN_VALUE+1));
        assertEquals(0, NumbersUtils.minusNoModuloSafe(Integer.MIN_VALUE,Integer.MIN_VALUE));
        for (int a=-100;a<=100;a++) {
            for (int b=-100;b<=100;b++) {
                assertEquals(a-b, NumbersUtils.minusNoModuloSafe(a,b));
            }
        }
        assertEquals(0, NumbersUtils.minusNoModuloSafe(Integer.MAX_VALUE,Integer.MAX_VALUE));
        assertEquals(1, NumbersUtils.minusNoModuloSafe(Integer.MAX_VALUE,Integer.MAX_VALUE-1));
        assertEquals(2, NumbersUtils.minusNoModuloSafe(Integer.MAX_VALUE,Integer.MAX_VALUE-2));
        assertEquals(3, NumbersUtils.minusNoModuloSafe(Integer.MAX_VALUE,Integer.MAX_VALUE-3));
        assertEquals(Integer.MAX_VALUE-3, NumbersUtils.minusNoModuloSafe(Integer.MAX_VALUE,3));
        assertEquals(Integer.MAX_VALUE-2, NumbersUtils.minusNoModuloSafe(Integer.MAX_VALUE,2));
        assertEquals(Integer.MAX_VALUE-1, NumbersUtils.minusNoModuloSafe(Integer.MAX_VALUE,1));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.minusNoModuloSafe(Integer.MAX_VALUE,0));
        //
        try {
            assertEquals(Integer.MAX_VALUE, NumbersUtils.minusNoModuloSafe(Integer.MAX_VALUE,-1));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Integer.MAX_VALUE, NumbersUtils.minusNoModuloSafe(Integer.MAX_VALUE,Integer.MIN_VALUE));
            assertTrue(false);
        } catch (Exception e) {
        }
    }

    public void test_minusNoModulo_2long() {
        assertEquals(Long.MIN_VALUE, NumbersUtils.minusNoModulo(Long.MIN_VALUE,Long.MAX_VALUE));
        assertEquals(Long.MIN_VALUE, NumbersUtils.minusNoModulo(Long.MIN_VALUE,1));
        //
        assertEquals(Long.MIN_VALUE, NumbersUtils.minusNoModulo(Long.MIN_VALUE,0));
        assertEquals(Long.MIN_VALUE+1, NumbersUtils.minusNoModulo(Long.MIN_VALUE,-1));
        assertEquals(Long.MIN_VALUE+2, NumbersUtils.minusNoModulo(Long.MIN_VALUE,-2));
        assertEquals(Long.MIN_VALUE+3, NumbersUtils.minusNoModulo(Long.MIN_VALUE,-3));
        assertEquals(-3, NumbersUtils.minusNoModulo(Long.MIN_VALUE,Long.MIN_VALUE+3));
        assertEquals(-2, NumbersUtils.minusNoModulo(Long.MIN_VALUE,Long.MIN_VALUE+2));
        assertEquals(-1, NumbersUtils.minusNoModulo(Long.MIN_VALUE,Long.MIN_VALUE+1));
        assertEquals(0, NumbersUtils.minusNoModulo(Long.MIN_VALUE,Long.MIN_VALUE));
        for (long a=-100;a<=100;a++) {
            for (long b=-100;b<=100;b++) {
                assertEquals(a-b, NumbersUtils.minusNoModulo(a,b));
            }
        }
        assertEquals(0, NumbersUtils.minusNoModulo(Long.MAX_VALUE,Long.MAX_VALUE));
        assertEquals(1, NumbersUtils.minusNoModulo(Long.MAX_VALUE,Long.MAX_VALUE-1));
        assertEquals(2, NumbersUtils.minusNoModulo(Long.MAX_VALUE,Long.MAX_VALUE-2));
        assertEquals(3, NumbersUtils.minusNoModulo(Long.MAX_VALUE,Long.MAX_VALUE-3));
        assertEquals(Long.MAX_VALUE-3, NumbersUtils.minusNoModulo(Long.MAX_VALUE,3));
        assertEquals(Long.MAX_VALUE-2, NumbersUtils.minusNoModulo(Long.MAX_VALUE,2));
        assertEquals(Long.MAX_VALUE-1, NumbersUtils.minusNoModulo(Long.MAX_VALUE,1));
        assertEquals(Long.MAX_VALUE, NumbersUtils.minusNoModulo(Long.MAX_VALUE,0));
        //
        assertEquals(Long.MAX_VALUE, NumbersUtils.minusNoModulo(Long.MAX_VALUE,-1));
        assertEquals(Long.MAX_VALUE, NumbersUtils.minusNoModulo(Long.MAX_VALUE,Long.MIN_VALUE));
    }

    public void test_minusNoModuloSafe_2long() {
        try {
            assertEquals(Long.MIN_VALUE, NumbersUtils.minusNoModuloSafe(Long.MIN_VALUE,Long.MAX_VALUE));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Long.MIN_VALUE, NumbersUtils.minusNoModuloSafe(Long.MIN_VALUE,1));
            assertTrue(false);
        } catch (Exception e) {
        }
        //
        assertEquals(Long.MIN_VALUE, NumbersUtils.minusNoModuloSafe(Long.MIN_VALUE,0));
        assertEquals(Long.MIN_VALUE+1, NumbersUtils.minusNoModuloSafe(Long.MIN_VALUE,-1));
        assertEquals(Long.MIN_VALUE+2, NumbersUtils.minusNoModuloSafe(Long.MIN_VALUE,-2));
        assertEquals(Long.MIN_VALUE+3, NumbersUtils.minusNoModuloSafe(Long.MIN_VALUE,-3));
        assertEquals(-3, NumbersUtils.minusNoModuloSafe(Long.MIN_VALUE,Long.MIN_VALUE+3));
        assertEquals(-2, NumbersUtils.minusNoModuloSafe(Long.MIN_VALUE,Long.MIN_VALUE+2));
        assertEquals(-1, NumbersUtils.minusNoModuloSafe(Long.MIN_VALUE,Long.MIN_VALUE+1));
        assertEquals(0, NumbersUtils.minusNoModuloSafe(Long.MIN_VALUE,Long.MIN_VALUE));
        for (long a=-100;a<=100;a++) {
            for (long b=-100;b<=100;b++) {
                assertEquals(a-b, NumbersUtils.minusNoModuloSafe(a,b));
            }
        }
        assertEquals(0, NumbersUtils.minusNoModuloSafe(Long.MAX_VALUE,Long.MAX_VALUE));
        assertEquals(1, NumbersUtils.minusNoModuloSafe(Long.MAX_VALUE,Long.MAX_VALUE-1));
        assertEquals(2, NumbersUtils.minusNoModuloSafe(Long.MAX_VALUE,Long.MAX_VALUE-2));
        assertEquals(3, NumbersUtils.minusNoModuloSafe(Long.MAX_VALUE,Long.MAX_VALUE-3));
        assertEquals(Long.MAX_VALUE-3, NumbersUtils.minusNoModuloSafe(Long.MAX_VALUE,3));
        assertEquals(Long.MAX_VALUE-2, NumbersUtils.minusNoModuloSafe(Long.MAX_VALUE,2));
        assertEquals(Long.MAX_VALUE-1, NumbersUtils.minusNoModuloSafe(Long.MAX_VALUE,1));
        assertEquals(Long.MAX_VALUE, NumbersUtils.minusNoModuloSafe(Long.MAX_VALUE,0));
        //
        try {
            assertEquals(Long.MAX_VALUE, NumbersUtils.minusNoModuloSafe(Long.MAX_VALUE,-1));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Long.MAX_VALUE, NumbersUtils.minusNoModuloSafe(Long.MAX_VALUE,Long.MIN_VALUE));
            assertTrue(false);
        } catch (Exception e) {
        }
    }

    public void test_timesNoModulo_2int() {
        /*
         * in range
         */
        
        for (int a=-100;a<=100;a++) {
            for (int b=-100;b<=100;b++) {
                assertEquals(a*b, NumbersUtils.timesNoModulo(a,b));
            }
        }
        
        assertEquals(Integer.MIN_VALUE, NumbersUtils.timesNoModulo(Integer.MIN_VALUE,1));
        assertEquals(Integer.MIN_VALUE, NumbersUtils.timesNoModulo(Integer.MIN_VALUE/2,2));
        
        assertEquals(Integer.MAX_VALUE, NumbersUtils.timesNoModulo(Integer.MAX_VALUE,1));
        assertEquals(Integer.MAX_VALUE/2 * 2, NumbersUtils.timesNoModulo(Integer.MAX_VALUE/2,2));
        assertEquals(Integer.MAX_VALUE/3 * 3, NumbersUtils.timesNoModulo(Integer.MAX_VALUE/3,3));
        
        /*
         * negative overflow
         */
        
        assertEquals(Integer.MIN_VALUE, NumbersUtils.timesNoModulo(Integer.MIN_VALUE,2));
        assertEquals(Integer.MIN_VALUE, NumbersUtils.timesNoModulo(Integer.MIN_VALUE/2,3));
        assertEquals(Integer.MIN_VALUE, NumbersUtils.timesNoModulo(Integer.MIN_VALUE,Integer.MAX_VALUE));
        
        /*
         * positive overflow
         */
        
        assertEquals(Integer.MAX_VALUE, NumbersUtils.timesNoModulo(Integer.MIN_VALUE,-1));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.timesNoModulo(Integer.MIN_VALUE,-2));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.timesNoModulo(Integer.MIN_VALUE,-3));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.timesNoModulo(Integer.MIN_VALUE,Integer.MIN_VALUE));
        assertEquals(Integer.MAX_VALUE, NumbersUtils.timesNoModulo(Integer.MAX_VALUE,Integer.MAX_VALUE));
    }

    public void test_timesNoModuloSafe_2int() {
        /*
         * in range
         */
        
        for (int a=-100;a<=100;a++) {
            for (int b=-100;b<=100;b++) {
                assertEquals(a*b, NumbersUtils.timesNoModuloSafe(a,b));
            }
        }
        
        assertEquals(Integer.MIN_VALUE, NumbersUtils.timesNoModuloSafe(Integer.MIN_VALUE,1));
        assertEquals(Integer.MIN_VALUE, NumbersUtils.timesNoModuloSafe(Integer.MIN_VALUE/2,2));
        
        assertEquals(Integer.MAX_VALUE, NumbersUtils.timesNoModuloSafe(Integer.MAX_VALUE,1));
        assertEquals(Integer.MAX_VALUE/2 * 2, NumbersUtils.timesNoModuloSafe(Integer.MAX_VALUE/2,2));
        assertEquals(Integer.MAX_VALUE/3 * 3, NumbersUtils.timesNoModuloSafe(Integer.MAX_VALUE/3,3));
        
        /*
         * negative overflow
         */
        
        try {
            assertEquals(Integer.MIN_VALUE, NumbersUtils.timesNoModuloSafe(Integer.MIN_VALUE,2));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Integer.MIN_VALUE, NumbersUtils.timesNoModuloSafe(Integer.MIN_VALUE/2,3));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Integer.MIN_VALUE, NumbersUtils.timesNoModuloSafe(Integer.MIN_VALUE,Integer.MAX_VALUE));
            assertTrue(false);
        } catch (Exception e) {
        }
        
        /*
         * positive overflow
         */
        
        try {
            assertEquals(Integer.MAX_VALUE, NumbersUtils.timesNoModuloSafe(Integer.MIN_VALUE,-1));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Integer.MAX_VALUE, NumbersUtils.timesNoModuloSafe(Integer.MIN_VALUE,-2));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Integer.MAX_VALUE, NumbersUtils.timesNoModuloSafe(Integer.MIN_VALUE,-3));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Integer.MAX_VALUE, NumbersUtils.timesNoModuloSafe(Integer.MIN_VALUE,Integer.MIN_VALUE));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Integer.MAX_VALUE, NumbersUtils.timesNoModuloSafe(Integer.MAX_VALUE,Integer.MAX_VALUE));
            assertTrue(false);
        } catch (Exception e) {
        }
    }

    public void test_timesNoModulo_2long() {
        /*
         * in range
         */
        
        for (long a=-100;a<=100;a++) {
            for (long b=-100;b<=100;b++) {
                assertEquals(a*b, NumbersUtils.timesNoModulo(a,b));
            }
        }
        
        assertEquals(Long.MIN_VALUE, NumbersUtils.timesNoModulo(Long.MIN_VALUE,1));
        assertEquals(Long.MIN_VALUE, NumbersUtils.timesNoModulo(Long.MIN_VALUE/2,2));
        
        assertEquals(Long.MAX_VALUE, NumbersUtils.timesNoModulo(Long.MAX_VALUE,1));
        assertEquals(Long.MAX_VALUE/2 * 2, NumbersUtils.timesNoModulo(Long.MAX_VALUE/2,2));
        assertEquals(Long.MAX_VALUE/3 * 3, NumbersUtils.timesNoModulo(Long.MAX_VALUE/3,3));
        
        /*
         * negative overflow
         */
        
        assertEquals(Long.MIN_VALUE, NumbersUtils.timesNoModulo(Long.MIN_VALUE,2));
        assertEquals(Long.MIN_VALUE, NumbersUtils.timesNoModulo(Long.MIN_VALUE/2,3));
        assertEquals(Long.MIN_VALUE, NumbersUtils.timesNoModulo(Long.MIN_VALUE,Long.MAX_VALUE));
        
        /*
         * positive overflow
         */
        
        assertEquals(Long.MAX_VALUE, NumbersUtils.timesNoModulo(Long.MIN_VALUE,-1));
        assertEquals(Long.MAX_VALUE, NumbersUtils.timesNoModulo(Long.MIN_VALUE,-2));
        assertEquals(Long.MAX_VALUE, NumbersUtils.timesNoModulo(Long.MIN_VALUE,-3));
        assertEquals(Long.MAX_VALUE, NumbersUtils.timesNoModulo(Long.MIN_VALUE,Long.MIN_VALUE));
        assertEquals(Long.MAX_VALUE, NumbersUtils.timesNoModulo(Long.MAX_VALUE,Long.MAX_VALUE));
    }

    public void test_timesNoModuloSafe_2long() {
        /*
         * in range
         */
        
        for (long a=-100;a<=100;a++) {
            for (long b=-100;b<=100;b++) {
                assertEquals(a*b, NumbersUtils.timesNoModuloSafe(a,b));
            }
        }
        
        assertEquals(Long.MIN_VALUE, NumbersUtils.timesNoModuloSafe(Long.MIN_VALUE,1));
        assertEquals(Long.MIN_VALUE, NumbersUtils.timesNoModuloSafe(Long.MIN_VALUE/2,2));
        
        assertEquals(Long.MAX_VALUE, NumbersUtils.timesNoModuloSafe(Long.MAX_VALUE,1));
        assertEquals(Long.MAX_VALUE/2 * 2, NumbersUtils.timesNoModuloSafe(Long.MAX_VALUE/2,2));
        assertEquals(Long.MAX_VALUE/3 * 3, NumbersUtils.timesNoModuloSafe(Long.MAX_VALUE/3,3));
        
        /*
         * negative overflow
         */
        
        try {
            assertEquals(Long.MIN_VALUE, NumbersUtils.timesNoModuloSafe(Long.MIN_VALUE,2));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Long.MIN_VALUE, NumbersUtils.timesNoModuloSafe(Long.MIN_VALUE/2,3));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Long.MIN_VALUE, NumbersUtils.timesNoModuloSafe(Long.MIN_VALUE,Long.MAX_VALUE));
            assertTrue(false);
        } catch (Exception e) {
        }
        
        /*
         * positive overflow
         */
        
        try {
            assertEquals(Long.MAX_VALUE, NumbersUtils.timesNoModuloSafe(Long.MIN_VALUE,-1));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Long.MAX_VALUE, NumbersUtils.timesNoModuloSafe(Long.MIN_VALUE,-2));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Long.MAX_VALUE, NumbersUtils.timesNoModuloSafe(Long.MIN_VALUE,-3));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Long.MAX_VALUE, NumbersUtils.timesNoModuloSafe(Long.MIN_VALUE,Long.MIN_VALUE));
            assertTrue(false);
        } catch (Exception e) {
        }
        try {
            assertEquals(Long.MAX_VALUE, NumbersUtils.timesNoModuloSafe(Long.MAX_VALUE,Long.MAX_VALUE));
            assertTrue(false);
        } catch (Exception e) {
        }
    }
    
    /*
     * integer and floating point functions
     */
    
    public void test_isNaNOrInfinite_double() {
        assertTrue(NumbersUtils.isNaNOrInfinite(Double.NaN));
        assertTrue(NumbersUtils.isNaNOrInfinite(Double.NEGATIVE_INFINITY));
        assertTrue(NumbersUtils.isNaNOrInfinite(Double.POSITIVE_INFINITY));
        
        assertFalse(NumbersUtils.isNaNOrInfinite(Double.MIN_VALUE));
        assertFalse(NumbersUtils.isNaNOrInfinite(Double.MIN_NORMAL));
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
        ArrayList<Float> values = new ArrayList<Float>();
        values.add(0.0f);
        values.add(0.1f);
        values.add(Float.MIN_VALUE);
        values.add(Float.MIN_NORMAL);
        values.add(Float.MAX_VALUE);
        values.add(Float.NaN);
        values.add(Float.NEGATIVE_INFINITY);
        values.add(Float.POSITIVE_INFINITY);
        for (Float value : values) {
            assertEquals(value * value, NumbersUtils.pow2(value));
        }
    }

    public void test_pow2_double() {
        ArrayList<Double> values = new ArrayList<Double>();
        values.add(0.0);
        values.add(0.1);
        values.add(Double.MIN_VALUE);
        values.add(Double.MIN_NORMAL);
        values.add(Double.MAX_VALUE);
        values.add(Double.NaN);
        values.add(Double.NEGATIVE_INFINITY);
        values.add(Double.POSITIVE_INFINITY);
        for (Double value : values) {
            assertEquals(value * value, NumbersUtils.pow2(value));
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
        ArrayList<Float> values = new ArrayList<Float>();
        values.add(0.0f);
        values.add(0.1f);
        values.add(Float.MIN_VALUE);
        values.add(Float.MIN_NORMAL);
        values.add(Float.MAX_VALUE);
        values.add(Float.NaN);
        values.add(Float.NEGATIVE_INFINITY);
        values.add(Float.POSITIVE_INFINITY);
        for (Float value : values) {
            assertEquals(value * value * value, NumbersUtils.pow3(value));
        }
    }

    public void test_pow3_double() {
        ArrayList<Double> values = new ArrayList<Double>();
        values.add(0.0);
        values.add(0.1);
        values.add(Double.MIN_VALUE);
        values.add(Double.MIN_NORMAL);
        values.add(Double.MAX_VALUE);
        values.add(Double.NaN);
        values.add(Double.NEGATIVE_INFINITY);
        values.add(Double.POSITIVE_INFINITY);
        for (Double value : values) {
            assertEquals(value * value * value, NumbersUtils.pow3(value));
        }
    }

    public void test_toRange_3int() {
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
}
