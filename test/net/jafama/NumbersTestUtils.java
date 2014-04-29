/*
 * Copyright 2013 Jeff Hain
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

/**
 * Utility methods to test numbers-related treatments.
 * Randomness of numbers might not be of high quality
 * (i.e. not quite uniform, etc.), but that shouldn't
 * hurt for most testing usages.
 */
public class NumbersTestUtils {

    //--------------------------------------------------------------------------
    // CONFIGURATION
    //--------------------------------------------------------------------------

    private static final double MASK_PROBA_EXPONENT = 0.75;

    private static final double MASK_PROBA_MANTISSA = 0.75;

    private static final double MASK_PROBA_INT_LONG = 0.5;

    private static final double FORCE_INFINITY_OR_ZERO_PROBA = 0.1;

    private static final double FORCE_NEAR_INT_OR_HALF_INT_PROBA = 0.75;

    //--------------------------------------------------------------------------
    // MEMBERS
    //--------------------------------------------------------------------------
    
    /**
     * Double.MIN_NORMAL since Java 6.
     */
    private static final double DOUBLE_MIN_NORMAL = Double.longBitsToDouble(0x0010000000000000L); // 2.2250738585072014E-308

    /**
     * Float.MIN_NORMAL since Java 6.
     */
    private static final float FLOAT_MIN_NORMAL = Float.intBitsToFloat(0x00800000); // 1.17549435E-38f

    private final Random random;
    
    //--------------------------------------------------------------------------
    // PUBLIC METHODS
    //--------------------------------------------------------------------------

    /**
     * Uses a default and specific Random instance.
     */
    public NumbersTestUtils() {
        this(new Random());
    }

    /**
     * @param random Random instance to use.
     */
    public NumbersTestUtils(Random random) {
        this.random = random;
    }
    
    /*
     * deltas
     */
    
    /**
     * @return |a-b|, or 0 if values are both NaN,
     *         both +Infinity or both -Infinity,
     *         or +Infinity if one value is NaN
     *         and the other not.
     */
    public static double absDelta(double a, double b) {
        if (a == b) {
            return 0.0;
        }
        if (Double.isNaN(a)) {
            return Double.isNaN(b) ? 0.0 : Double.POSITIVE_INFINITY;
        } else if (Double.isNaN(b)) {
            return Double.POSITIVE_INFINITY;
        }
        return Math.abs(a-b);
    }
    
    /**
     * @return |a-b|/max(|a|,|b|), or 0 if values are both NaN,
     *         both +Infinity or both -Infinity, or +Infinity
     *         if (((a < 0) and (b > 0)) or ((a > 0) and (b < 0))),
     *         or if a value is NaN and the other not.
     */
    public static double relDelta_raw(double a, double b) {
        if (a == b) {
            return 0.0;
        }
        if (((a > 0) && (b < 0)) || ((a < 0) && (b > 0))) {
            return Double.POSITIVE_INFINITY;
        }
        if (Double.isNaN(a)) {
            return (Double.isNaN(b)) ? 0.0 : Double.POSITIVE_INFINITY;
        } else if (Double.isNaN(b)) {
            return Double.POSITIVE_INFINITY;
        }
        if (Double.isInfinite(a)) {
            return (Double.isInfinite(b)) ? 0.0 : Double.POSITIVE_INFINITY;
        } else if (Double.isInfinite(b)) {
            return Double.POSITIVE_INFINITY;
        }
        return Math.abs(a-b) / Math.max(Math.abs(a), Math.abs(b));
    }

    /**
     * If arguments max magnitude is < Double.MIN_NORMAL, relative delta might
     * be bad due to double precision loss, so we multiply the delta by
     * maxMagnitude/Double.MIN_NORMAL (which is in [0,1]) before returning it.
     * For example this causes relDelta(0,1e-320) to be not 1 but about 4.5e-13.
     * 
     * Similarly, if relative delta is infinite due to a value being +-Infinity
     * and the other being close to +-Double.MAX_VALUE (with the same sign),
     * we return relative delta between the finite value and +-Double.MAX_VALUE
     * of ame sign.
     * 
     * @return relDelta_raw(a,b), unless betterified due to above logic.
     */
    public static double relDelta(double a, double b) {
        
        double delta = relDelta_raw(a,b);
        
        if (Double.isInfinite(delta)) {
            if ((a == Double.NEGATIVE_INFINITY) && (b < -Double.MAX_VALUE/(1L<<52))) {
                // Still +Infinity if b is -Infinity.
                return relDelta_raw(-Double.MAX_VALUE, b);
            }
            if ((a == Double.POSITIVE_INFINITY) && (b > Double.MAX_VALUE/(1L<<52))) {
                // Still +Infinity if b is +Infinity.
                return relDelta_raw(Double.MAX_VALUE, b);
            }
            if ((a < -Double.MAX_VALUE/(1L<<52)) && (b == Double.NEGATIVE_INFINITY)) {
                // Still +Infinity if a is -Infinity.
                return relDelta_raw(a, -Double.MAX_VALUE);
            }
            if ((a > Double.MAX_VALUE/(1L<<52)) && (b == Double.POSITIVE_INFINITY)) {
                // Still +Infinity if a is +Infinity.
                return relDelta_raw(a, Double.MAX_VALUE);
            }
        }
        
        final double maxMag = Math.max(Math.abs(a), Math.abs(b));
        if (maxMag < DOUBLE_MIN_NORMAL) {
            delta *= (maxMag/DOUBLE_MIN_NORMAL);
        }
        
        return delta;
    }

    /**
     * @return min(relDelta(a,b), absDelta(a,b)).
     */
    public static double minDelta(double a, double b) {
        return Math.min(relDelta(a,b), absDelta(a,b));
    }
    
    /*
     * random uniform
     */

    public int randomIntUniform() {
        return this.random.nextInt();
    }

    public long randomLongUniform() {
        return this.random.nextLong();
    }

    /**
     * @throws IllegalArgumentException if min > max.
     */
    public int randomIntUniform(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException();
        }
        final double width = max-(double)min;
        return (int)Math.rint(min + width * this.random.nextDouble());
    }

    /**
     * @throws IllegalArgumentException if min > max.
     */
    public long randomLongUniform(long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException();
        }
        final double width = max-(double)min;
        return (long)Math.rint(min + width * this.random.nextDouble());
    }

    /**
     * @throws IllegalArgumentException if min > max,
     *         or if max-min is NaN.
     */
    public float randomFloatUniform(float min, float max) {
        checkMinMax(min, max);
        final float width = max-min;
        if (width == Float.POSITIVE_INFINITY) {
            if (this.random.nextBoolean()) {
                return this.randomFloatUniform(min, 0.0f);
            } else {
                return this.randomFloatUniform(0.0f, max);
            }
        } else {
            return (float)(min + width * this.random.nextDouble());
        }
    }
    
    /**
     * @throws IllegalArgumentException if min > max,
     *         or if max-min is NaN.
     */
    public double randomDoubleUniform(double min, double max) {
        checkMinMax(min, max);
        final double width = max-min;
        if (width == Double.POSITIVE_INFINITY) {
            if (this.random.nextBoolean()) {
                return this.randomDoubleUniform(min, 0.0);
            } else {
                return this.randomDoubleUniform(0.0, max);
            }
        } else {
            return min + width * this.random.nextDouble();
        }
    }

    /*
     * random magnitudes
     */

    public int randomIntUniMag() {
        return this.random.nextInt() >> this.random.nextInt(32);
    }

    public long randomLongUniMag() {
        return this.random.nextLong() >> this.random.nextInt(64);
    }

    /**
     * @return Value of uniform sign, exponent and mantissa
     *         (very low chance of +-0.0), except NaN or +-Infinity.
     */
    public float randomFloatUniMag() {
        float tmp;
        do {
            final int signBit = this.random.nextInt()<<31;
            final int exponentBits = ((int)this.randomLongUniformMasked(8, 0.0))<<23;
            final int mantissaBits = (int)this.randomLongUniformMasked(23, 0.0);
            tmp = Float.intBitsToFloat(signBit|exponentBits|mantissaBits);
        } while (Float.isNaN(tmp) || Float.isInfinite(tmp));
        return tmp;
    }
    
    /**
     * @return Value of uniform sign, exponent and mantissa
     *         (very low chance of +-0.0), except NaN or +-Infinity.
     */
    public double randomDoubleUniMag() {
        double tmp;
        do {
            final long signBit = ((long)this.random.nextInt())<<63;
            final long exponentBits = this.randomLongUniformMasked(11, 0.0)<<52;
            final long mantissaBits = this.randomLongUniformMasked(52, 0.0);
            tmp = Double.longBitsToDouble(signBit|exponentBits|mantissaBits);
        } while (Double.isNaN(tmp) || Double.isInfinite(tmp));
        return tmp;
    }

    /*
     * random whatever (various kinds of mantissa, exponents, and special cases)
     */
    
    public int randomIntWhatever() {
        return (int)this.randomLongUniformMasked(32, MASK_PROBA_INT_LONG);
    }

    public long randomLongWhatever() {
        return this.randomLongUniformMasked(64, MASK_PROBA_INT_LONG);
    }

    /**
     * Has chances to return various kinds of NaNs, +-Infinity, +-0.0f,
     * subnormal values, values at or close to mathematical integers,
     * values exactly or about equidistant between adjacent mathematical integers,
     * and values corresponding to various kinds of exponents and mantissa.
     */
    public float randomFloatWhatever() {
        final int signBit = this.random.nextInt()<<31;
        int exponentBits = ((int)this.randomLongUniformMasked(8, MASK_PROBA_EXPONENT))<<23;
        int mantissaBits = (int)this.randomLongUniformMasked(23, MASK_PROBA_MANTISSA);
        
        final boolean forceInfZero = (this.random.nextDouble() < FORCE_INFINITY_OR_ZERO_PROBA);
        if (forceInfZero) {
            exponentBits = this.random.nextBoolean() ? (0xFF<<23) : 0;
            mantissaBits = 0;
        } else {
            if (this.random.nextBoolean()) {
                // Making sure we have many values with digits around comma.
                final int exponent = 1 + this.random.nextInt(23);
                exponentBits = (exponent + 127)<<23;
            }
        }
        
        float value = Float.intBitsToFloat(signBit|exponentBits|mantissaBits);
        
        final boolean forceNearIntOrHalfInt =
                (!forceInfZero)
                && (Math.abs(value) >= 0.5f)
                && (Math.abs(value) <= (float)(1<<23))
                && (this.random.nextDouble() < FORCE_NEAR_INT_OR_HALF_INT_PROBA);
        if (forceNearIntOrHalfInt) {
            final int m22 = -2 + this.random.nextInt(5);
            if (this.random.nextBoolean()) {
                // forcing near int (+-2ulp)
                value = Math.round(value);
                value = Float.intBitsToFloat(Float.floatToRawIntBits(value)+m22);
            } else {
                // forcing near half int (+-2ulp)
                value = Math.round(value) + 0.5f;
                value = Float.intBitsToFloat(Float.floatToRawIntBits(value)+m22);
            }
        }

        return value;
    }

    /**
     * Has chances to return various kinds of NaNs, +-Infinity, +-0.0,
     * subnormal values, values at or close to mathematical integers,
     * values exactly or about equidistant between adjacent mathematical integers,
     * and values corresponding to various kinds of exponents and mantissa.
     */
    public double randomDoubleWhatever() {
        final long signBit = ((long)this.random.nextInt())<<63;
        long exponentBits = this.randomLongUniformMasked(11, MASK_PROBA_EXPONENT)<<52;
        long mantissaBits = this.randomLongUniformMasked(52, MASK_PROBA_MANTISSA);
        
        final boolean forceInfZero = (this.random.nextDouble() < FORCE_INFINITY_OR_ZERO_PROBA);
        if (forceInfZero) {
            exponentBits = this.random.nextBoolean() ? (0x7FFL<<52) : 0;
            mantissaBits = 0;
        } else {
            if (this.random.nextBoolean()) {
                // Making sure we have many values with digits around comma.
                final int exponent = 1 + this.random.nextInt(52);
                exponentBits = (exponent + 1023L)<<52;
            }
        }

        double value = Double.longBitsToDouble(signBit|exponentBits|mantissaBits);
        
        final boolean forceNearIntOrHalfInt =
                (!forceInfZero)
                && (Math.abs(value) >= 0.5)
                && (Math.abs(value) <= (double)(1L<<52))
                && (this.random.nextDouble() < FORCE_NEAR_INT_OR_HALF_INT_PROBA);
        if (forceNearIntOrHalfInt) {
            final int m22 = -2 + this.random.nextInt(5);
            if (this.random.nextBoolean()) {
                // forcing near int (+-2ulp)
                value = Math.round(value);
                value = Double.longBitsToDouble(Double.doubleToRawLongBits(value)+m22);
            } else {
                // forcing near half int (+-2ulp)
                value = Math.round(value) + 0.5;
                value = Double.longBitsToDouble(Double.doubleToRawLongBits(value)+m22);
            }
        }
        
        return value;
    }

    /**
     * @return Values either uniform, or of uniform magnitude,
     *         or randomly close to bounds.
     * @throws IllegalArgumentException if min > max,
     *         or if max-min is NaN.
     */
    public float randomFloatWhatever(float min, float max) {
        checkMinMax(min, max);
        final float width = max-min;
        if (width == Float.POSITIVE_INFINITY) {
            if (this.random.nextBoolean()) {
                return this.randomFloatWhatever(min, 0.0f);
            } else {
                return this.randomFloatWhatever(0.0f, max);
            }
        }
        final double u01 = this.random.nextDouble();
        if (u01 < 0.25) {
            // Uniform.
            return this.randomFloatUniform(min, max);
        } else if (u01 < 0.5) {
            // Uniform exponent, if possible.
            // Working either on positive or negative side,
            // as possible and at random if both sides are possible.
            final boolean canWorkNegSide = (min < 0.0f);
            final boolean canWorkPosSide = (max > 0.0f);
            if (canWorkNegSide || canWorkPosSide) {
                final boolean positiveSide;
                if (canWorkNegSide && canWorkPosSide) {
                    positiveSide = this.random.nextBoolean();
                } else {
                    positiveSide = canWorkPosSide;
                }
                final int signBit = (positiveSide ? 0 : Integer.MIN_VALUE);

                final int minBoundBits = Float.floatToRawIntBits(min);
                final int maxBoundBits = Float.floatToRawIntBits(max);
                final int minBoundExponent = ((minBoundBits>>23)&0xFF)-127;
                final int maxBoundExponent = ((maxBoundBits>>23)&0xFF)-127;
                
                // Using +-1 on exponents to make sure random mantissa
                // doesn't get us out of range.
                final int resultMinExponent;
                final int resultMaxExponent;
                if (positiveSide) {
                    resultMinExponent = (min <= 0.0) ? -127 : minBoundExponent + 1;
                    resultMaxExponent = maxBoundExponent - 1;
                } else {
                    resultMinExponent = (max >= 0.0) ? -127 : maxBoundExponent + 1;
                    resultMaxExponent = minBoundExponent - 1;
                }
                
                if (resultMinExponent <= resultMaxExponent) {
                    final int exponent = this.randomIntUniform(resultMinExponent, resultMaxExponent);
                    final int exponentBits = (exponent+127)<<23;
                    final int mantissaBits = (this.randomIntWhatever()&0x007FFFFF);
                    final float result = Float.intBitsToFloat(signBit|exponentBits|mantissaBits);
                    if ((result < min) || (result > max)) {
                        throw new AssertionError(result);
                    }
                    return result;
                } else {
                    // Falling back to uniform.
                    return this.randomFloatUniform(min, max);
                }
            } else {
                return this.random.nextBoolean() ? -0.0f : 0.0f;
            }
        } else if (u01 < 0.75) {
            // Higher probability of being close to bounds.
            // In [0.5,1], with more values close to 1.
            float factor = 1 - 1.0f/(1<<(1+this.random.nextInt(25)));
            if (this.random.nextBoolean()) {
                // In [0,0.5] (for being close to min).
                factor = 1-factor;
            }
            return min + width * factor;
        } else {
            // Up to 99 ulps from bounds.
            float bound = this.random.nextBoolean() ? min : max;
            int maxNbrOfUlps = Math.min(99, (int)Math.floor(width/Math.ulp(bound)));
            int nbrOfUlps = this.random.nextInt(maxNbrOfUlps+1);
            float delta = Math.min(width, nbrOfUlps * Math.ulp(bound));
            float result = (bound == min) ? min + delta : max - delta;
            if (result < min) {
                result = min;
            }
            if (result > max) {
                result = max;
            }
            return result;
        }
    }

    /**
     * @return Values either uniform, or of uniform magnitude,
     *         or randomly close to bounds.
     * @throws IllegalArgumentException if min > max,
     *         or if max-min is NaN.
     */
    public double randomDoubleWhatever(double min, double max) {
        checkMinMax(min, max);
        final double width = max-min;
        if (width == Double.POSITIVE_INFINITY) {
            if (this.random.nextBoolean()) {
                return this.randomDoubleWhatever(min, 0.0);
            } else {
                return this.randomDoubleWhatever(0.0, max);
            }
        }
        final double u01 = this.random.nextDouble();
        if (u01 < 0.25) {
            // Uniform.
            return this.randomDoubleUniform(min, max);
        } else if (u01 < 0.5) {
            // Uniform exponent, if possible.
            // Working either on positive or negative side,
            // as possible and at random if both sides are possible.
            final boolean canWorkNegSide = (min < 0.0);
            final boolean canWorkPosSide = (max > 0.0);
            if (canWorkNegSide || canWorkPosSide) {
                final boolean positiveSide;
                if (canWorkNegSide && canWorkPosSide) {
                    positiveSide = this.random.nextBoolean();
                } else {
                    positiveSide = canWorkPosSide;
                }
                final long signBit = (positiveSide ? 0 : Long.MIN_VALUE);

                final long minBoundBits = Double.doubleToRawLongBits(min);
                final long maxBoundBits = Double.doubleToRawLongBits(max);
                final int minBoundExponent = ((int)(minBoundBits>>52)&0x7FF)-1023;
                final int maxBoundExponent = ((int)(maxBoundBits>>52)&0x7FF)-1023;
                
                // Using +-1 on exponents to make sure random mantissa
                // doesn't get us out of range.
                final int resultMinExponent;
                final int resultMaxExponent;
                if (positiveSide) {
                    resultMinExponent = (min <= 0.0) ? -1023 : minBoundExponent + 1;
                    resultMaxExponent = maxBoundExponent - 1;
                } else {
                    resultMinExponent = (max >= 0.0) ? -1023 : maxBoundExponent + 1;
                    resultMaxExponent = minBoundExponent - 1;
                }
                
                if (resultMinExponent <= resultMaxExponent) {
                    final int exponent = this.randomIntUniform(resultMinExponent, resultMaxExponent);
                    final long exponentBits = (exponent+1023L)<<52;
                    final long mantissaBits = (this.randomLongWhatever()&0x000FFFFFFFFFFFFFL);
                    final double result = Double.longBitsToDouble(signBit|exponentBits|mantissaBits);
                    if ((result < min) || (result > max)) {
                        throw new AssertionError(result);
                    }
                    return result;
                } else {
                    // Falling back to uniform.
                    return this.randomDoubleUniform(min, max);
                }
            } else {
                return this.random.nextBoolean() ? -0.0 : 0.0;
            }
        } else if (u01 < 0.75) {
            // Higher probability of being close to bounds.
            // In [0.5,1], with more values close to 1.
            double factor = 1 - 1.0/(1L<<(1+this.random.nextInt(54)));
            if (this.random.nextBoolean()) {
                // In [0,0.5] (for being close to min).
                factor = 1-factor;
            }
            return min + width * factor;
        } else {
            // Up to 99 ulps from bounds.
            double bound = this.random.nextBoolean() ? min : max;
            int maxNbrOfUlps = Math.min(99, (int)Math.floor(width/Math.ulp(bound)));
            int nbrOfUlps = this.random.nextInt(maxNbrOfUlps+1);
            double delta = Math.min(width, nbrOfUlps * Math.ulp(bound));
            double result = (bound == min) ? min + delta : max - delta;
            if (result < min) {
                result = min;
            }
            if (result > max) {
                result = max;
            }
            return result;
        }
    }
    
    //--------------------------------------------------------------------------
    // PRIVATE METHODS
    //--------------------------------------------------------------------------

    /**
     * @throws IllegalArgumentException if min > max,
     *         or if max-min is NaN.
     */
    private static void checkMinMax(float min, float max) {
        final float width = max-min;
        if ((min > max) || Float.isNaN(width)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @throws IllegalArgumentException if min > max,
     *         or if max-min is NaN.
     */
    private static void checkMinMax(double min, double max) {
        final double width = max-min;
        if ((min > max) || Double.isNaN(width)) {
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * Uniform LSBits, possibly with uniform-length
     * 0-or-1 masking at start or end of range,
     * or on whole range.
     * 
     * MSBits out of range are set to 0.
     * 
     * @param n Number of random LSBits, in [1,64].
     * @param maskProba Probability for left-masking, and probability
     *        for right-masking, each being randomly 0-or-1 masking.
     * @return A long with the specified number of random LSBits.
     */
    private long randomLongUniformMasked(int n, double maskProba) {
        if ((n <= 0) || (n > 64)) {
            throw new IllegalArgumentException();
        }
        final long bitsMask = ((-1L)>>>(64-n));
        long bits = this.random.nextLong();
        if ((maskProba == 1.0) || (this.random.nextDouble() < maskProba)) {
            // right-masking
            final int shift = this.random.nextInt(n);
            if (this.random.nextBoolean()) {
                // right-masking with 0
                bits &= (bitsMask<<shift);
            } else {
                // right-masking with 1
                bits |= ~(bitsMask<<shift);
            }
        }
        if ((maskProba == 1.0) || (this.random.nextDouble() < maskProba)) {
            // left-masking
            final int shift = this.random.nextInt(n);
            if (this.random.nextBoolean()) {
                // left-masking with 0
                bits &= (bitsMask>>>shift);
            } else {
                // left-masking with 1
                bits |= ~(bitsMask>>>shift);
            }
        }
        return bits & bitsMask;
    }
    
    /*
     * TODO for settings
     */

    private static void ___main(String[] args) {
        NumbersTestUtils utils = new NumbersTestUtils(new Random(123456789L));
        final int n = 10 * 1000;
        {
            System.out.println();
            System.out.println("randomFloatUniMag()");
            int zeroCounter = 0;
            int subnormalCounter = 0;
            int otherCounter = 0;
            for (int i=0;i<n;i++) {
                float value = utils.randomFloatUniMag();
                if (value == 0.0) {
                    zeroCounter++;
                } else if (Math.abs(value) < FLOAT_MIN_NORMAL) {
                    subnormalCounter++;
                } else {
                    otherCounter++;
                }
            }
            System.out.println("zeroCounter = "+zeroCounter);
            System.out.println("subnormalCounter = "+subnormalCounter);
            System.out.println("otherCounter = "+otherCounter);
        }
        {
            System.out.println();
            System.out.println("randomDoubleUniMag()");
            int zeroCounter = 0;
            int subnormalCounter = 0;
            int otherCounter = 0;
            for (int i=0;i<n;i++) {
                double value = utils.randomDoubleUniMag();
                if (value == 0.0) {
                    zeroCounter++;
                } else if (Math.abs(value) < DOUBLE_MIN_NORMAL) {
                    subnormalCounter++;
                } else {
                    otherCounter++;
                }
            }
            System.out.println("zeroCounter = "+zeroCounter);
            System.out.println("subnormalCounter = "+subnormalCounter);
            System.out.println("otherCounter = "+otherCounter);
        }
        {
            System.out.println();
            System.out.println("randomIntWhatever()");
            int zeroCounter = 0;
            int m1Counter = 0;
            int minCounter = 0;
            int maxCounter = 0;
            int otherCounter = 0;
            for (int i=0;i<n;i++) {
                int value = utils.randomIntWhatever();
                if (value == 0) {
                    zeroCounter++;
                } else if (value == -1) {
                    m1Counter++;
                } else if (value == Integer.MIN_VALUE) {
                    minCounter++;
                } else if (value == Integer.MAX_VALUE) {
                    maxCounter++;
                } else {
                    otherCounter++;
                }
            }
            System.out.println("zeroCounter = "+zeroCounter);
            System.out.println("m1Counter = "+m1Counter);
            System.out.println("minCounter = "+minCounter);
            System.out.println("maxCounter = "+maxCounter);
            System.out.println("otherCounter = "+otherCounter);
        }
        {
            System.out.println();
            System.out.println("randomLongWhatever()");
            int zeroCounter = 0;
            int m1Counter = 0;
            int minCounter = 0;
            int maxCounter = 0;
            int otherCounter = 0;
            for (int i=0;i<n;i++) {
                long value = utils.randomLongWhatever();
                if (value == 0) {
                    zeroCounter++;
                } else if (value == -1) {
                    m1Counter++;
                } else if (value == Long.MIN_VALUE) {
                    minCounter++;
                } else if (value == Long.MAX_VALUE) {
                    maxCounter++;
                } else {
                    otherCounter++;
                }
            }
            System.out.println("zeroCounter = "+zeroCounter);
            System.out.println("m1Counter = "+m1Counter);
            System.out.println("minCounter = "+minCounter);
            System.out.println("maxCounter = "+maxCounter);
            System.out.println("otherCounter = "+otherCounter);
        }
        {
            System.out.println();
            System.out.println("randomFloatWhatever()");
            int nanCounter = 0;
            int ninfCounter = 0;
            int pinfCounter = 0;
            int zeroCounter = 0;
            int subnormalCounter = 0;
            int smallMatIntCounter = 0;
            int nearSmallMatIntCounter = 0;
            int otherCounter = 0;
            for (int i=0;i<n;i++) {
                float value = utils.randomFloatWhatever();
                if (value != value) {
                    nanCounter++;
                } else if (value == Float.NEGATIVE_INFINITY) {
                    ninfCounter++;
                } else if (value == Float.POSITIVE_INFINITY) {
                    pinfCounter++;
                } else if (value == 0.0) {
                    zeroCounter++;
                } else if (Math.abs(value) < FLOAT_MIN_NORMAL) {
                    subnormalCounter++;
                } else {
                    if ((Math.abs(value) >= 0.5f) && (Math.abs(value) < (1<<23))) {
                        if (value == (int)value) {
                            smallMatIntCounter++;
                        } else {
                            float nextDown = Float.intBitsToFloat(Float.floatToRawIntBits(value)-1);
                            float nextUp = Float.intBitsToFloat(Float.floatToRawIntBits(value)+1);
                            if ((nextDown == (int)nextDown) || (nextUp == (int)nextUp)) {
                                nearSmallMatIntCounter++;
                            } else {
                                otherCounter++;
                            }
                        }
                    } else {
                        otherCounter++;
                    }
                }
            }
            System.out.println("nanCounter = "+nanCounter);
            System.out.println("ninfCounter = "+ninfCounter);
            System.out.println("pinfCounter = "+pinfCounter);
            System.out.println("zeroCounter = "+zeroCounter);
            System.out.println("subnormalCounter = "+subnormalCounter);
            System.out.println("smallMatIntCounter = "+smallMatIntCounter);
            System.out.println("nearSmallMatIntCounter = "+nearSmallMatIntCounter);
            System.out.println("otherCounter = "+otherCounter);
        }
        {
            System.out.println();
            System.out.println("randomDoubleWhatever()");
            int nanCounter = 0;
            int ninfCounter = 0;
            int pinfCounter = 0;
            int zeroCounter = 0;
            int subnormalCounter = 0;
            int smallMatIntCounter = 0;
            int nearSmallMatIntCounter = 0;
            int otherCounter = 0;
            for (int i=0;i<n;i++) {
                double value = utils.randomDoubleWhatever();
                if (value != value) {
                    nanCounter++;
                } else if (value == Double.NEGATIVE_INFINITY) {
                    ninfCounter++;
                } else if (value == Double.POSITIVE_INFINITY) {
                    pinfCounter++;
                } else if (value == 0.0) {
                    zeroCounter++;
                } else if (Math.abs(value) < DOUBLE_MIN_NORMAL) {
                    subnormalCounter++;
                } else {
                    if ((Math.abs(value) >= 0.5) && (Math.abs(value) < (1L<<52))) {
                        if (value == (long)value) {
                            smallMatIntCounter++;
                        } else {
                            double nextDown = Double.longBitsToDouble(Double.doubleToRawLongBits(value)-1);
                            double nextUp = Double.longBitsToDouble(Double.doubleToRawLongBits(value)+1);
                            if ((nextDown == (long)nextDown) || (nextUp == (long)nextUp)) {
                                nearSmallMatIntCounter++;
                            } else {
                                otherCounter++;
                            }
                        }
                    } else {
                        otherCounter++;
                    }
                }
            }
            System.out.println("nanCounter = "+nanCounter);
            System.out.println("ninfCounter = "+ninfCounter);
            System.out.println("pinfCounter = "+pinfCounter);
            System.out.println("zeroCounter = "+zeroCounter);
            System.out.println("subnormalCounter = "+subnormalCounter);
            System.out.println("smallMatIntCounter = "+smallMatIntCounter);
            System.out.println("nearSmallMatIntCounter = "+nearSmallMatIntCounter);
            System.out.println("otherCounter = "+otherCounter);
        }
    }
}
