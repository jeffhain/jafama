package odk.lang;

/**
 * Class containing various basic utility methods to deal with numbers.
 * This class is meant to be light (no look-up tables or such).
 */
public strictfp final class NumbersUtils {

    //--------------------------------------------------------------------------
    // MEMBERS
    //--------------------------------------------------------------------------
    
    private static final int MIN_DOUBLE_EXPONENT = -1074;
    private static final int MAX_DOUBLE_EXPONENT = 1023;
    
    //--------------------------------------------------------------------------
    // PUBLIC METHODS
    //--------------------------------------------------------------------------

    /*
     * Check methods return boolean if success,
     * for it allows to use them in assertions.
     */

    /*
     * min/max ranges
     */
    
    /**
     * @return True if the specified value is in the specified range (inclusive), false otherwise.
     */
    public static boolean isInRange(int min, int max, int a) {
        return (min <= a) && (a <= max);
    }

    /**
     * @return True if the specified value is in the specified range (inclusive), false otherwise.
     */
    public static boolean isInRange(long min, long max, long a) {
        return (min <= a) && (a <= max);
    }

    /**
     * Returns false if any value is NaN.
     * 
     * @return True if the specified value is in the specified range (inclusive), false otherwise.
     */
    public static boolean isInRange(float min, float max, float a) {
        return (min <= a) && (a <= max);
    }

    /**
     * Returns false if any value is NaN.
     * 
     * @return True if the specified value is in the specified range (inclusive), false otherwise.
     */
    public static boolean isInRange(double min, double max, double a) {
        return (min <= a) && (a <= max);
    }

    /*
     * 
     */
    
    /**
     * @return True if the specified value is in the specified range (inclusive), nothing otherwise.
     * @throws IllegalArgumentException if it does not return true.
     */
    public static boolean checkIsInRange(int min, int max, int a) {
        if (!isInRange(min, max, a)) {
            throw new IllegalArgumentException(a+" not in ["+min+","+max+"]");
        }
        return true;
    }
    
    /**
     * @return True if the specified value is in the specified range (inclusive), nothing otherwise.
     * @throws IllegalArgumentException if it does not return true.
     */
    public static boolean checkIsInRange(long min, long max, long a) {
        if (!isInRange(min, max, a)) {
            throw new IllegalArgumentException(a+" not in ["+min+","+max+"]");
        }
        return true;
    }
    
    /**
     * Throws exception if any value is NaN.
     * 
     * @return True if the specified value is in the specified range (inclusive), nothing otherwise.
     * @throws IllegalArgumentException if it does not return true.
     */
    public static boolean checkIsInRange(float min, float max, float a) {
        if (!isInRange(min, max, a)) {
            throw new IllegalArgumentException(a+" not in ["+min+","+max+"]");
        }
        return true;
    }
    
    /**
     * Throws exception if any value is NaN.
     * 
     * @return True if the specified value is in the specified range (inclusive), nothing otherwise.
     * @throws IllegalArgumentException if it does not return true.
     */
    public static boolean checkIsInRange(double min, double max, double a) {
        if (!isInRange(min, max, a)) {
            throw new IllegalArgumentException(a+" not in ["+min+","+max+"]");
        }
        return true;
    }

    /*
     * bitwise ranges
     */

    /**
     * @param a A value.
     * @param bitSize A number of bits, in [1,32].
     * @return True if the specified value can be represented as a signed integer
     *         on the specified number of bits, false otherwise.
     */
    public static boolean isInRangeSigned(int a, int bitSize) {
        return (minSignedIntForBitSize(bitSize) <= a) && (a <= maxSignedIntForBitSize(bitSize));
    }

    /**
     * @param a A value.
     * @param bitSize A number of bits, in [1,64].
     * @return True if the specified value can be represented as a signed integer
     *         on the specified number of bits, false otherwise.
     */
    public static boolean isInRangeSigned(long a, int bitSize) {
        return (minSignedLongForBitSize(bitSize) <= a) && (a <= maxSignedLongForBitSize(bitSize));
    }

    /**
     * @param a A value.
     * @param bitSize A number of bits, in [1,31].
     * @return True if the specified value can be represented as an unsigned integer
     *         on the specified number of bits, false otherwise.
     */
    public static boolean isInRangeUnsigned(int a, int bitSize) {
        return (0 <= a) && (a <= maxUnsignedIntForBitSize(bitSize));
    }

    /**
     * @param a A value.
     * @param bitSize A number of bits, in [1,63].
     * @return True if the specified value can be represented as an unsigned integer
     *         on the specified number of bits, false otherwise.
     */
    public static boolean isInRangeUnsigned(long a, int bitSize) {
        return (0 <= a) && (a <= maxUnsignedLongForBitSize(bitSize));
    }
    
    /*
     * 
     */
    
    /**
     * @param a A value.
     * @param bitSize A number of bits, in [1,32].
     * @return True if the specified value can be represented as a signed integer
     *         on the specified number of bits, nothing otherwise.
     * @throws IllegalArgumentException if it does not return true.
     */
    public static boolean checkIsInRangeSigned(int a, int bitSize) {
        if (!isInRangeSigned(a, bitSize)) {
            throw new IllegalArgumentException(a+" does not fit as signed value on "+bitSize+" bits");
        }
        return true;
    }
    
    /**
     * @param a A value.
     * @param bitSize A number of bits, in [1,64].
     * @return True if the specified value can be represented as a signed integer
     *         on the specified number of bits, nothing otherwise.
     * @throws IllegalArgumentException if it does not return true.
     */
    public static boolean checkIsInRangeSigned(long a, int bitSize) {
        if (!isInRangeSigned(a, bitSize)) {
            throw new IllegalArgumentException(a+" does not fit as signed value on "+bitSize+" bits");
        }
        return true;
    }
    
    /**
     * @param a A value.
     * @param bitSize A number of bits, in [1,31].
     * @return True if the specified value can be represented as an unsigned integer
     *         on the specified number of bits, nothing otherwise.
     * @throws IllegalArgumentException if it does not return true.
     */
    public static boolean checkIsInRangeUnsigned(int a, int bitSize) {
        if (!isInRangeUnsigned(a, bitSize)) {
            throw new IllegalArgumentException(a+" does not fit as unsigned value on "+bitSize+" bits");
        }
        return true;
    }
    
    /**
     * @param a A value.
     * @param bitSize A number of bits, in [1,63].
     * @return True if the specified value can be represented as an unsigned integer
     *         on the specified number of bits, nothing otherwise.
     * @throws IllegalArgumentException if it does not return true.
     */
    public static boolean checkIsInRangeUnsigned(long a, int bitSize) {
        if (!isInRangeUnsigned(a, bitSize)) {
            throw new IllegalArgumentException(a+" does not fit as unsigned value on "+bitSize+" bits");
        }
        return true;
    }
    
    /*
     * masks
     */

    /**
     * @param nbrOfBits A number of bits, in [0,32].
     * @return Mask with the specified number of left bits set with 0,
     *         and other bits set with 1.
     */
    public static int leftBit0IntMask(int nbrOfBits) {
        assert(checkIsInRange(0, 32, nbrOfBits));
        if (nbrOfBits == 32) { // >>> is weird for full bit size
            return 0;
        }
        return (-1)>>>nbrOfBits;
    }
    
    /**
     * @param nbrOfBits A number of bits, in [0,32].
     * @return Mask with the specified number of left bits set with 1,
     *         and other bits set with 0.
     */
    public static int leftBit1IntMask(int nbrOfBits) {
        return ~leftBit0IntMask(nbrOfBits);
    }

    /**
     * @param nbrOfBits A number of bits, in [0,32].
     * @return Mask with the specified number of right bits set with 0,
     *         and other bits set with 1.
     */
    public static int rightBit0IntMask(int nbrOfBits) {
        return ~leftBit0IntMask(32-nbrOfBits);
    }

    /**
     * @param nbrOfBits A number of bits, in [0,32].
     * @return Mask with the specified number of right bits set with 1,
     *         and other bits set with 0.
     */
    public static int rightBit1IntMask(int nbrOfBits) {
        return leftBit0IntMask(32-nbrOfBits);
    }

    /*
     * 
     */
    
    /**
     * @param nbrOfBits A number of bits, in [0,64].
     * @return Mask with the specified number of left bits set with 0,
     *         and other bits set with 1.
     */
    public static long leftBit0LongMask(int nbrOfBits) {
        assert(checkIsInRange(0, 64, nbrOfBits));
        if (nbrOfBits == 64) { // >>> is weird for full bit size
            return 0;
        }
        return (-1L)>>>nbrOfBits;
    }
    
    /**
     * @param nbrOfBits A number of bits, in [0,64].
     * @return Mask with the specified number of left bits set with 1,
     *         and other bits set with 0.
     */
    public static long leftBit1LongMask(int nbrOfBits) {
        return ~leftBit0LongMask(nbrOfBits);
    }

    /**
     * @param nbrOfBits A number of bits, in [0,64].
     * @return Mask with the specified number of right bits set with 0,
     *         and other bits set with 1.
     */
    public static long rightBit0LongMask(int nbrOfBits) {
        return ~leftBit0LongMask(64-nbrOfBits);
    }

    /**
     * @param nbrOfBits A number of bits, in [0,64].
     * @return Mask with the specified number of right bits set with 1,
     *         and other bits set with 0.
     */
    public static long rightBit1LongMask(int nbrOfBits) {
        return leftBit0LongMask(64-nbrOfBits);
    }

    /*
     * bitwise ranges
     */
    
    /**
     * @param bitSize A value.
     * @return True if a signed int value can be read in the specified bit size,
     *         i.e. if it is in [1,32], false otherwise.
     */
    public static boolean isValidBitSizeForSignedInt(int bitSize) {
        return (bitSize >= 1) && (bitSize <= 32);
    }
    
    /**
     * @param bitSize A value.
     * @return True if a signed long value can be read in the specified bit size,
     *         i.e. if it is in [1,64], false otherwise.
     */
    public static boolean isValidBitSizeForSignedLong(int bitSize) {
        return (bitSize >= 1) && (bitSize <= 64);
    }
    
    /**
     * @param bitSize A value.
     * @return True if an unsigned int value can be read in the specified bit size,
     *         i.e. if it is in [1,31], false otherwise.
     */
    public static boolean isValidBitSizeForUnsignedInt(int bitSize) {
        return (bitSize >= 1) && (bitSize <= 31);
    }

    /**
     * @param bitSize A value.
     * @return True if an unsigned long value can be read in the specified bit size,
     *         i.e. if it is in [1,63], false otherwise.
     */
    public static boolean isValidBitSizeForUnsignedLong(int bitSize) {
        return (bitSize >= 1) && (bitSize <= 63);
    }

    /*
     * 
     */
    
    /**
     * @param bitSize A value.
     * @return True if a signed int value can be read in the specified bit size,
     *         i.e. if it is in [1,32], nothing otherwise.
     * @throws IllegalArgumentException if it does not return true.
     */
    public static boolean checkBitSizeForSignedInt(int bitSize) {
        if (!isValidBitSizeForSignedInt(bitSize)) {
            throw new IllegalArgumentException("bit size ["+bitSize+"] must be in [1,32] for signed int values");
        }
        return true;
    }

    /**
     * @param bitSize A value.
     * @return True if a signed long value can be read in the specified bit size,
     *         i.e. if it is in [1,64], nothing otherwise.
     * @throws IllegalArgumentException if it does not return true.
     */
    public static boolean checkBitSizeForSignedLong(int bitSize) {
        if (!isValidBitSizeForSignedLong(bitSize)) {
            throw new IllegalArgumentException("bit size ["+bitSize+"] must be in [1,64] for signed long values");
        }
        return true;
    }

    /**
     * @param bitSize A value.
     * @return True if an unsigned int value can be read in the specified bit size,
     *         i.e. if it is in [1,31], nothing otherwise.
     * @throws IllegalArgumentException if it does not return true.
     */
    public static boolean checkBitSizeForUnsignedInt(int bitSize) {
        if (!isValidBitSizeForUnsignedInt(bitSize)) {
            throw new IllegalArgumentException("bit size ["+bitSize+"] must be in [1,31] for unsigned int values");
        }
        return true;
    }

    /**
     * @param bitSize A value.
     * @return True if an unsigned long value can be read in the specified bit size,
     *         i.e. if it is in [1,63], nothing otherwise.
     * @throws IllegalArgumentException if it does not return true.
     */
    public static boolean checkBitSizeForUnsignedLong(int bitSize) {
        if (!isValidBitSizeForUnsignedLong(bitSize)) {
            throw new IllegalArgumentException("bit size ["+bitSize+"] must be in [1,63] for unsigned long values");
        }
        return true;
    }

    /*
     * 
     */
    
    /**
     * @param bitSize A value in [1,32].
     * @return The min signed int value that can be stored on the specified number of bits.
     */
    public static int minSignedIntForBitSize(int bitSize) {
        assert(checkBitSizeForSignedInt(bitSize));
        // i.e. (-1<<(bitSize-1))
        return (Integer.MIN_VALUE>>(32-bitSize));
    }
    
    /**
     * @param bitSize A value in [1,32].
     * @return The max signed int value that can be stored on the specified number of bits.
     */
    public static int maxSignedIntForBitSize(int bitSize) {
        assert(checkBitSizeForSignedInt(bitSize));
        // i.e. (1<<(bitSize-1))-1
        return (Integer.MAX_VALUE>>(32-bitSize));
    }

    /**
     * @param bitSize A value in [1,64].
     * @return The min signed long value that can be stored on the specified number of bits.
     */
    public static long minSignedLongForBitSize(int bitSize) {
        assert(checkBitSizeForSignedLong(bitSize));
        // i.e. (-1L<<(bitSize-1))
        return (Long.MIN_VALUE>>(64-bitSize));
    }
    
    /**
     * @param bitSize A value in [1,64].
     * @return The max signed long value that can be stored on the specified number of bits.
     */
    public static long maxSignedLongForBitSize(int bitSize) {
        assert(checkBitSizeForSignedLong(bitSize));
        // i.e. (1L<<(bitSize-1))-1
        return (Long.MAX_VALUE>>(64-bitSize));
    }

    /**
     * @param bitSize A value in [1,31].
     * @return The max unsigned int value that can be stored on the specified number of bits.
     */
    public static int maxUnsignedIntForBitSize(int bitSize) {
        assert(checkBitSizeForUnsignedLong(bitSize));
        // i.e. (1<<bitSize)-1
        return (Integer.MAX_VALUE>>(31-bitSize));
    }

    /**
     * @param bitSize A value in [1,63].
     * @return The max unsigned long value that can be stored on the specified number of bits.
     */
    public static long maxUnsignedLongForBitSize(int bitSize) {
        assert(checkBitSizeForUnsignedLong(bitSize));
        // i.e. (1L<<bitSize)-1
        return (Long.MAX_VALUE>>(63-bitSize));
    }
    
    /*
     * 
     */
    
    /**
     * @param value An integer value.
     * @return The number of bits required to store the specified value as a signed integer,
     *         i.e. a result in [1,32].
     */
    public static int bitSizeForSignedValue(int value) {
        if (value > 0) {
            return 33-Integer.numberOfLeadingZeros(value);
        } else if (value == 0) {
            return 1;
        } else {
            // Works for Integer.MIN_VALUE as well.
            return 33-Integer.numberOfLeadingZeros(-value-1);
        }
    }
    
    /**
     * @param value An integer value.
     * @return The number of bits required to store the specified value as a signed integer,
     *         i.e. a result in [1,64].
     */
    public static int bitSizeForSignedValue(long value) {
        if (value > 0) {
            return 65-Long.numberOfLeadingZeros(value);
        } else if (value == 0) {
            return 1;
        } else {
            // Works for Long.MIN_VALUE as well.
            return 65-Long.numberOfLeadingZeros(-value-1);
        }
    }
    
    /**
     * @param value An integer value in [0,Integer.MAX_VALUE].
     * @return The number of bits required to store the specified value as an unsigned integer,
     *         i.e. a result in [1,31].
     * @throws IllegalArgumentException if the specified value is < 0.
     */
    public static int bitSizeForUnsignedValue(int value) {
        if (value > 0) {
            return 32-Integer.numberOfLeadingZeros(value);
        } else {
            if (value == 0) {
                return 1;
            } else {
                throw new IllegalArgumentException("unsigned value ["+value+"] must be >= 0");
            }
        }
    }
    
    /**
     * @param value An integer value in [0,Long.MAX_VALUE].
     * @return The number of bits required to store the specified value as an unsigned integer,
     *         i.e. a result in [1,63].
     * @throws IllegalArgumentException if the specified value is < 0.
     */
    public static int bitSizeForUnsignedValue(long value) {
        if (value > 0) {
            return 64-Long.numberOfLeadingZeros(value);
        } else {
            if (value == 0) {
                return 1;
            } else {
                throw new IllegalArgumentException("unsigned value ["+value+"] must be >= 0");
            }
        }
    }
    
    /*
     * integer functions
     */
    
    /**
     * @param a A value.
     * @return True if the specified value is even, false otherwise.
     */
    public static boolean isEven(int a) {
        return ((a&1) == 0);
    }
    
    /**
     * @param a A value.
     * @return True if the specified value is even, false otherwise.
     */
    public static boolean isEven(long a) {
        // faster to work on ints
        return isEven((int)a);
    }

    /**
     * @param a A value.
     * @return True if the specified value is odd, false otherwise.
     */
    public static boolean isOdd(int a) {
        return ((a&1) != 0);
    }

    /**
     * @param a A value.
     * @return True if the specified value is odd, false otherwise.
     */
    public static boolean isOdd(long a) {
        // faster to work on ints
        return isOdd((int)a);
    }

    /**
     * @param a A value.
     * @param b A value.
     * @return True if the specified values are both even or both odd, false otherwise.
     */
    public static boolean haveSameEvenness(int a, int b) {
        return (((a^b)&1) == 0);
    }

    /**
     * @param a A value.
     * @param b A value.
     * @return True if the specified values are both even or both odd, false otherwise.
     */
    public static boolean haveSameEvenness(long a, long b) {
        // faster to work on ints
        return haveSameEvenness((int)a, (int)b);
    }

    /**
     * @param a A value.
     * @param b A value.
     * @return True if the specified values are both >= 0 or both < 0, false otherwise.
     */
    public static boolean haveSameSign(int a, int b) {
        return ((a^b) >= 0);
    }

    /**
     * @param a A value.
     * @param b A value.
     * @return True if the specified values are both >= 0 or both < 0, false otherwise.
     */
    public static boolean haveSameSign(long a, long b) {
        return ((a^b) >= 0);
    }

    /**
     * @param a A value.
     * @return True if the specified value is a power of two,
     *         i.e. a value of the form 2^k, with k >= 0.
     */
    public static boolean isPowerOfTwo(int a) {
        if (a <= 0) {
            return false;
        }
        if (false) {
            // also works
            return (a & -a) == a;
        }
        return (a & (a-1)) == 0;
    }

    /**
     * @param a A value.
     * @return True if the specified value is a power of two,
     *         i.e. a value of the form 2^k, with k >= 0.
     */
    public static boolean isPowerOfTwo(long a) {
        if (a <= 0) {
            return false;
        }
        if (false) {
            // also works
            return (a & -a) == a;
        }
        return (a & (a-1)) == 0;
    }

    /**
     * @return True if the specified value is a signed power of two,
     *         i.e. a value of the form +-2^k, with k >= 0.
     */
    public static boolean isSignedPowerOfTwo(int a) {
        if (a > 0) {
            return (a & (a-1)) == 0;
        } else {
            if (a == -a) {
                // a is 0 or Integer.MIN_VALUE
                return (a != 0);
            }
            return ((-a) & (-a-1)) == 0;
        }
    }

    /**
     * @return True if the specified value is a signed power of two,
     *         i.e. a value of the form +-2^k, with k >= 0.
     */
    public static boolean isSignedPowerOfTwo(long a) {
        if (a > 0) {
            return (a & (a-1)) == 0;
        } else {
            if (a == -a) {
                // a is 0 or Long.MIN_VALUE
                return (a != 0);
            }
            return ((-a) & (-a-1)) == 0;
        }
    }
    
    /**
     * @param a A value.
     * @param b A value.
     * @return Mean without overflow, rounded to the lowest value (i.e. mathematical floor((a+b)/2), using floating point division).
     */
    public static int meanLow(int a, int b) {
        return (a & b) + ((a ^ b) >> 1);
    }
    
    /**
     * @param a A value.
     * @param b A value.
     * @return Mean without overflow, rounded to the lowest value (i.e. mathematical floor((a+b)/2), using floating point division).
     */
    public static long meanLow(long a, long b) {
        return (a & b) + ((a ^ b) >> 1);
    }

    /**
     * @param a A value.
     * @param b A value.
     * @return Mean without overflow, rounded to the value of smallest magnitude (i.e. mathematical (a+b)/2, using integer division).
     */
    public static int meanSml(int a, int b) {
        int result = meanLow(a,b);
        if (!haveSameEvenness(a, b)) {
            // inexact
            if (((a&b) < 0) || (((a|b) < 0) && (a+b < 0))) {
                // both < 0, or only one is < 0 and it has the largest magnitude
                result++;
            }
        }
        return result;
    }
    
    /**
     * @param a A value.
     * @param b A value.
     * @return Mean without overflow, rounded to the value of smallest magnitude (i.e. mathematical (a+b)/2, using integer division).
     */
    public static long meanSml(long a, long b) {
        long result = meanLow(a,b);
        if (!haveSameEvenness(a, b)) {
            // inexact
            if (((a&b) < 0) || (((a|b) < 0) && (a+b < 0))) {
                // both < 0, or only one is < 0 and it has the largest magnitude
                result++;
            }
        }
        return result;
    }

    /**
     * Useful because a positive int value could not represent half the width
     * of full int range width, which is mathematically Integer.MAX_VALUE+1.
     * 
     * @param min A value <= max.
     * @param max A value >= min.
     * @return Minus half the range width (inclusive, and rounded to the value of smaller magnitude)
     *         between the specified bounds.
     */
    public static int negHalfWidth(int min, int max) {
        assert(min <= max);
        int mean = meanLow(min, max);
        return min - mean - ((min^max)&1);
    }

    /**
     * Useful because a positive long value could not represent half the width
     * of full long range width, which is mathematically Long.MAX_VALUE+1.
     * 
     * @param min A value <= max.
     * @param max A value >= min.
     * @return Minus half the range width (inclusive, and rounded to the value of smaller magnitude)
     *         between the specified bounds.
     */
    public static long negHalfWidth(long min, long max) {
        assert(min <= max);
        long mean = meanLow(min, max);
        return min - mean - ((min^max)&1);
    }
    
    /**
     * @param a A value.
     * @param spot A signed power of two (i.e. a value of the form +-2^k, k >= 0).
     * @return a % spot, i.e. a value in ]-|spot|,|spot|[.
     */
    public static int moduloSignedPowerOfTwo(int a, int spot) {
        assert(isSignedPowerOfTwo(spot));
        if (spot == Integer.MIN_VALUE) {
            return (a != Integer.MIN_VALUE) ? a : 0;
        } else {
            int s = (a>>31);
            return ((((a+s) ^ s) & (abs(spot)-1)) + s) ^ s;
        }
    }
    
    /**
     * @param a A value.
     * @param spot A signed power of two (i.e. a value of the form +-2^k, k >= 0).
     * @return a % spot, i.e. a value in ]-|spot|,|spot|[.
     */
    public static long moduloSignedPowerOfTwo(long a, long spot) {
        assert(isSignedPowerOfTwo(spot));
        if (spot == Long.MIN_VALUE) {
            return (a != Long.MIN_VALUE) ? a : 0;
        } else {
            long s = (a>>63);
            return ((((a+s) ^ s) & (abs(spot)-1)) + s) ^ s;
        }
    }

    /**
     * @param value An integer value in [1,Integer.MAX_VALUE].
     * @return The integer part of the logarithm, in base 2, of the specified value,
     *         i.e. a result in [0,30]
     * @throws IllegalArgumentException if the specified value is <= 0.
     */
    public static int log2(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("value ["+value+"] must be > 0");
        }
        return 31-Integer.numberOfLeadingZeros(value);
    }
    
    /**
     * @param value An integer value in [1,Long.MAX_VALUE].
     * @return The integer part of the logarithm, in base 2, of the specified value,
     *         i.e. a result in [0,62]
     * @throws IllegalArgumentException if the specified value is <= 0.
     */
    public static int log2(long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("value ["+value+"] must be > 0");
        }
        return 63-Long.numberOfLeadingZeros(value);
    }

    /**
     * Faster than java.lang.Math.abs(int).
     * 
     * @param value An int value.
     * @return The absolute value, except if value is Integer.MIN_VALUE, for which it returns Integer.MIN_VALUE.
     */
    public static int abs(int value) {
        return (value^(value>>31))-(value>>31);
    }

    /**
     * A little faster than java.lang.Math.abs(long).
     * 
     * @param value A long value.
     * @return The absolute value, except if value is Long.MIN_VALUE, for which it returns Long.MIN_VALUE.
     */
    public static long abs(long value) {
        return (value^(value>>63))-(value>>63);
    }

    /**
     * FastMath class has a faster version of this method (using look-up tables).
     * 
     * Returns the exact result, provided it's in double range,
     * i.e. if power is in [-1074,1023].
     * 
     * @param power A power.
     * @return 2^power.
     */
    public static double twoPow(int power) {
        if (power <= -MAX_DOUBLE_EXPONENT) { // Not normal.
            if (power >= MIN_DOUBLE_EXPONENT) { // Subnormal.
                return Double.longBitsToDouble(0x0008000000000000L>>(-(power+MAX_DOUBLE_EXPONENT)));
            } else { // Underflow.
                return 0.0;
            }
        } else if (power > MAX_DOUBLE_EXPONENT) { // Overflow.
            return Double.POSITIVE_INFINITY;
        } else { // Normal.
            return Double.longBitsToDouble(((long)(power+MAX_DOUBLE_EXPONENT))<<52);
        }
    }

    /**
     * If the specified value is in int range, the returned value is identical.
     * 
     * @param value A long value.
     * @return An int hash of the specified value.
     */
    public static int intHash(long value) {
        if (false) {
            // also works
            int hash = ((int)(value>>32)) ^ ((int)value);
            if (value < 0) {
                hash = -hash-1;
            }
            return hash;
        }
        int hash = ((int)(value>>32)) + ((int)value);
        if (value < 0) {
            hash++;
        }
        return hash;
    }
    
    /**
     * @param value A long value.
     * @return The closest int value in [Integer.MIN_VALUE,Integer.MAX_VALUE] range.
     */
    public static int toInt(long value) {
        if (value < (long)Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        } else if (value > (long)Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else {
            return (int)value;
        }
    }

    /**
     * @param value A long value.
     * @return If value is in [Integer.MIN_VALUE,Integer.MAX_VALUE] range, this value as int,
     *         otherwise throws an exception.
     */
    public static int toIntSafe(long value) {
        if ((value < (long)Integer.MIN_VALUE) || (value > (long)Integer.MAX_VALUE)) {
            throw new ArithmeticException("overflow: "+value);
        } else {
            return (int)value;
        }
    }

    /**
     * @param a An int value.
     * @param b An int value.
     * @return The int value of [Integer.MIN_VALUE,Integer.MAX_VALUE] range which is the closest to mathematical result of a+b.
     */
    public static int plusNoModulo(int a, int b) {
        return toInt(((long)a) + ((long)b));
    }

    /**
     * @param a An int value.
     * @param b An int value.
     * @return The mathematical result of a+b, if it is in [Integer.MIN_VALUE,Integer.MAX_VALUE] range,
     *         otherwise throws an exception.
     */
    public static int plusNoModuloSafe(int a, int b) {
        if ((a^b) < 0) { // test if a and b signs are different
            return a + b;
        } else {
            int sum = a + b;
            if ((a^sum) < 0) {
                throw new ArithmeticException("overflow: "+a+"+"+b);
            } else {
                return sum;
            }
        }
    }

    /**
     * @param a A long value.
     * @param b A long value.
     * @return The long value of [Long.MIN_VALUE,Long.MAX_VALUE] range which is the closest to mathematical result of a+b.
     */
    public static long plusNoModulo(long a, long b) {
        // Algorithm tested on int type, for which it's faster with cast to long.
        if ((a^b) < 0) { // test if a and b signs are different
            return a + b;
        } else {
            long sum = a + b;
            if ((a^sum) < 0) {
                return (sum >= 0) ? Long.MIN_VALUE : Long.MAX_VALUE;
            } else {
                return sum;
            }
        }
    }

    /**
     * @param a A long value.
     * @param b A long value.
     * @return The mathematical result of a+b, if it is in [Long.MIN_VALUE,Long.MAX_VALUE] range,
     *         otherwise throws an exception.
     */
    public static long plusNoModuloSafe(long a, long b) {
        // Algorithm tested on int type.
        if ((a^b) < 0) { // test if a and b signs are different
            return a + b;
        } else {
            long sum = a + b;
            if ((a^sum) < 0) {
                throw new ArithmeticException("overflow: "+a+"+"+b);
            } else {
                return sum;
            }
        }
    }

    /**
     * @param a An int value.
     * @param b An int value.
     * @return The int value of [Integer.MIN_VALUE,Integer.MAX_VALUE] range which is the closest to mathematical result of a-b.
     */
    public static int minusNoModulo(int a, int b) {
        return toInt(((long)a) - ((long)b));
    }

    /**
     * @param a An int value.
     * @param b An int value.
     * @return The mathematical result of a-b, if it is in [Integer.MIN_VALUE,Integer.MAX_VALUE] range,
     *         otherwise throws an exception.
     */
    public static int minusNoModuloSafe(int a, int b) {
        if ((a^b) >= 0) { // test if a and b signs are identical
            return a - b;
        } else {
            int diff = a - b;
            if ((a^diff) < 0) {
                throw new ArithmeticException("overflow: "+a+"-"+b);
            } else {
                return diff;
            }
        }
    }

    /**
     * @param a A long value.
     * @param b A long value.
     * @return The long value of [Long.MIN_VALUE,Long.MAX_VALUE] range which is the closest to mathematical result of a-b.
     */
    public static long minusNoModulo(long a, long b) {
        // Algorithm tested on int type, for which it's faster with cast to long.
        if ((a^b) >= 0) { // test if a and b signs are identical
            return a - b;
        } else {
            long diff = a - b;
            if ((a^diff) < 0) {
                return (diff >= 0) ? Long.MIN_VALUE : Long.MAX_VALUE;
            } else {
                return diff;
            }
        }
    }

    /**
     * @param a A long value.
     * @param b A long value.
     * @return The mathematical result of a-b, if it is in [Long.MIN_VALUE,Long.MAX_VALUE] range,
     *         otherwise throws an exception.
     */
    public static long minusNoModuloSafe(long a, long b) {
        // Algorithm tested on int type.
        if ((a^b) >= 0) { // test if a and b signs are identical
            return a - b;
        } else {
            long diff = a - b;
            if ((a^diff) < 0) {
                throw new ArithmeticException("overflow: "+a+"-"+b);
            } else {
                return diff;
            }
        }
    }

    /**
     * @param a An int value.
     * @param b An int value.
     * @return The int value of [Integer.MIN_VALUE,Integer.MAX_VALUE] range which is the closest to mathematical result of a*b.
     */
    public static int timesNoModulo(int a, int b) {
        if (false) {
            /*
             * slower
             */
            return toInt(((long)a) * ((long)b));
        }
        return (int)(a * (double)b);
    }

    /**
     * @param a An int value.
     * @param b An int value.
     * @return The mathematical result of a*b, if it is in [Integer.MIN_VALUE,Integer.MAX_VALUE] range,
     *         otherwise throws an exception.
     */
    public static int timesNoModuloSafe(int a, int b) {
        if (false) {
            /*
             * slower
             */
            if (b == 0) {
                return 0;
            }
            int product = a * b;
            if ((product == Integer.MIN_VALUE) && ((a^b) >= 0)) {
                // product negative, but a and b have the same sign:
                // that means total would be -Integer.MIN_VALUE,
                // which does not exist as int.
                throw new ArithmeticException("overflow: "+a+"*"+b);
            } else {
                if (product / b != a) {
                    throw new ArithmeticException("overflow: "+a+"*"+b);
                } else {
                    return product;
                }
            }
        }
        double product = a * (double)b;
        if ((product >= (double)Integer.MIN_VALUE) && (product <= (double)Integer.MAX_VALUE)) {
            return (int)product;
        } else {
            throw new ArithmeticException("overflow: "+a+"*"+b);
        }
    }

    /**
     * @param a A long value.
     * @param b A long value.
     * @return The long value of [Long.MIN_VALUE,Long.MAX_VALUE] range which is the closest to mathematical result of a*b.
     */
    public static long timesNoModulo(long a, long b) {
        // Algorithm tested on int type, for which it's faster to do another way.
        if (b == 0) {
            return 0;
        }
        long product = a * b;
        if ((product == Long.MIN_VALUE) && ((a^b) >= 0)) {
            // product negative, but a and b have the same sign:
            // that means total would be -Long.MIN_VALUE,
            // which does not exist as long.
            return Long.MAX_VALUE;
        } else {
            if (product / b != a) {
                return ((a^b) >= 0) ? Long.MAX_VALUE : Long.MIN_VALUE;
            } else {
                return product;
            }
        }
    }

    /**
     * @param a A long value.
     * @param b A long value.
     * @return The mathematical result of a*b, if it is in [Long.MIN_VALUE,Long.MAX_VALUE] range,
     *         otherwise throws an exception.
     */
    public static long timesNoModuloSafe(long a, long b) {
        // Algorithm tested on int type.
        if (b == 0) {
            return 0;
        }
        long product = a * b;
        if ((product == Long.MIN_VALUE) && ((a^b) >= 0)) {
            // product negative, but a and b have the same sign:
            // that means total would be -Long.MIN_VALUE,
            // which does not exist as long.
            throw new ArithmeticException("overflow: "+a+"*"+b);
        } else {
            if (product / b != a) {
                throw new ArithmeticException("overflow: "+a+"*"+b);
            } else {
                return product;
            }
        }
    }
    
    /*
     * integer and floating point functions
     */
    
    /**
     * @param value A value.
     * @return True if the specified value is NaN, positive of negative infinity, false otherwise.
     */
    public static boolean isNaNOrInfinite(double value) {
        // value-value is not equal to 0.0 (and is NaN) <-> value is NaN or +-infinity
        return !(value-value == 0.0);
    }

    /**
     * @param value A value.
     * @return value*value.
     */
    public static int pow2(int value) {
        return value*value;
    }

    /**
     * @param value A value.
     * @return value*value.
     */
    public static long pow2(long value) {
        return value*value;
    }

    /**
     * @param value A value.
     * @return value*value.
     */
    public static float pow2(float value) {
        return value*value;
    }

    /**
     * @param value A value.
     * @return value*value.
     */
    public static double pow2(double value) {
        return value*value;
    }

    /**
     * @param value A value.
     * @return value*value*value.
     */
    public static int pow3(int value) {
        return value*value*value;
    }

    /**
     * @param value A value.
     * @return value*value*value.
     */
    public static long pow3(long value) {
        return value*value*value;
    }

    /**
     * @param value A value.
     * @return value*value*value.
     */
    public static float pow3(float value) {
        return value*value*value;
    }

    /**
     * @param value A value.
     * @return value*value*value.
     */
    public static double pow3(double value) {
        return value*value*value;
    }

    /**
     * Designed to be fast: not supposed to handle min > max in any relevant way.
     * 
     * @param min A value.
     * @param max A value.
     * @param value A value.
     * @return minValue if value <= minValue, else maxValue if value >= maxValue, else value.
     */
    public static int toRange(int min, int max, int value) {
        if (value <= min) {
            return min;
        } else if (value >= max) {
            return max;
        } else {
            return value;
        }
    }

    /**
     * Designed to be fast: not supposed to handle min > max in any relevant way.
     * 
     * @param min A value.
     * @param max A value.
     * @param value A value.
     * @return minValue if value <= minValue, else maxValue if value >= maxValue, else value.
     */
    public static long toRange(long min, long max, long value) {
        if (value <= min) {
            return min;
        } else if (value >= max) {
            return max;
        } else {
            return value;
        }
    }

    /**
     * Designed to be fast: not supposed to handle min > max or NaN in any relevant way.
     * 
     * @param min A value.
     * @param max A value.
     * @param value A value.
     * @return minValue if value <= minValue, else maxValue if value >= maxValue, else value.
     */
    public static float toRange(float min, float max, float value) {
        if (value <= min) {
            return min;
        } else if (value >= max) {
            return max;
        } else {
            return value;
        }
    }

    /**
     * Designed to be fast: not supposed to handle min > max or NaN in any relevant way.
     * 
     * @param min A value.
     * @param max A value.
     * @param value A value.
     * @return minValue if value <= minValue, else maxValue if value >= maxValue, else value.
     */
    public static double toRange(double min, double max, double value) {
        if (value <= min) {
            return min;
        } else if (value >= max) {
            return max;
        } else {
            return value;
        }
    }
}
