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
/*
 * =============================================================================
 * Notice of fdlibm package this program is partially derived from:
 *
 * Copyright (C) 1993 by Sun Microsystems, Inc. All rights reserved.
 *
 * Developed at SunSoft, a Sun Microsystems, Inc. business.
 * Permission to use, copy, modify, and distribute this
 * software is freely granted, provided that this notice 
 * is preserved.
 * =============================================================================
 */
package net.jafama;

/**
 * Class providing math treatments that:
 * - are meant to be faster than java.lang.Math class equivalents (if any),
 * - are still somehow accurate and robust (handling of NaN and such),
 * - do not (or not directly) generate objects at run time (no "new").
 * 
 * Other than optimized treatments, a valuable feature of this class is the
 * presence of angles normalization methods, derived from those used in
 * java.lang.Math (for which, sadly, no API is provided, letting everyone
 * with the terrible responsibility to write their own ones).
 * 
 * Non-redefined methods of java.lang.Math class are also available,
 * for easy replacement.
 * 
 * Use of look-up tables: around 1 Mo total, and initialized on class load.
 * 
 * Depending on JVM, or JVM options, these treatments can actually be slower
 * than Math ones.
 * In particular, they can be slower if not optimized by the JIT, which you
 * can see with -Xint JVM option.
 * Another cause of slowness can be cache-misses on look-up tables.
 * Also, look-up tables initialization, done on class load, typically
 * takes multiple hundreds of milliseconds (and is about twice slower
 * in J6 than in J5, and in J7 than in J6, possibly due to intrinsifications
 * preventing optimizations such as use of hardware sqrt, and Math delegating
 * to StrictMath with JIT optimizations not yet up during class load).
 * As a result, you might want to make these treatments not use tables,
 * and delegate to corresponding Math methods, when they are available in the
 * lowest supported Java version, by using the appropriate property (see below).
 * 
 * These treatments are not strictfp: if you want identical results
 * across various architectures, you must roll your own implementation
 * by adding strictfp and using StrictMath instead of Math.
 * 
 * Methods with same signature than Math ones, are meant to return
 * "good" approximations on all range.
 * Methods terminating with "Fast" are meant to return "good" approximation
 * on a reduced range only.
 * Methods terminating with "Quick" are meant to be quick, but do not
 * return a good approximation, and might only work on a reduced range.
 * 
 * Properties:
 * 
 * - jafama.usejdk (boolean, default is false):
 *   If true, redefined Math methods, as well as their "Fast" or "Quick" terminated counterparts,
 *   delegate to Math, when available in required Java version.
 *   
 * - jafama.fastlog (boolean, default is false):
 *   If true, using redefined log(double), else Math.log(double).
 *   False by default because Math.log(double) seems usually fast
 *   (redefined log(double) might be even faster, but is less accurate).
 *   
 * - jafama.fastsqrt (boolean, default is false):
 *   If true, using redefined sqrt(double), else Math.sqrt(double).
 *   False by default because Math.sqrt(double) seems usually fast.
 * 
 * --- words, words, words ---
 * 
 * "0x42BE0000 percents of the folks out there
 * are completely clueless about floating-point."
 * 
 * The difference between precision and accuracy:
 * "3.177777777777777 is a precise (16 digits)
 * but inaccurate (only correct up to the second digit)
 * approximation of PI=3.141592653589793(etc.)."
 */
public final class FastMath {

    /*
     * For trigonometric functions, use of look-up tables and Taylor-Lagrange formula
     * with 4 derivatives (more take longer to compute and don't add much accuracy,
     * less require larger tables (which use more memory, take more time to initialize,
     * and are slower to access (at least on the machine they were developed on))).
     * 
     * For angles reduction of cos/sin/tan functions:
     * - for small values, instead of reducing angles, and then computing the best index
     *   for look-up tables, we compute this index right away, and use it for reduction,
     * - for large values, treatments derived from fdlibm package are used, as done in
     *   java.lang.Math. They are faster but still "slow", so if you work with
     *   large numbers and need speed over accuracy for them, you might want to use
     *   normalizeXXXFast treatments before your function, or modify cos/sin/tan
     *   so that they call the fast normalization treatments instead of the accurate ones.
     *   NB: If an angle is huge (like PI*1e20), in double precision format its last digits
     *       are zeros, which most likely is not the case for the intended value, and doing
     *       an accurate reduction on a very inaccurate value is most likely pointless.
     *       But it gives some sort of coherence that could be needed in some cases.
     * 
     * Multiplication on double appears to be about as fast (or not much slower) than call
     * to <double_array>[<index>], and regrouping some doubles in a private class, to use
     * index only once, does not seem to speed things up, so:
     * - for uniformly tabulated values, to retrieve the parameter corresponding to
     *   an index, we recompute it rather than using an array to store it,
     * - for cos/sin, we recompute derivatives divided by (multiplied by inverse of)
     *   factorial each time, rather than storing them in arrays.
     * 
     * Lengths of look-up tables are usually of the form 2^n+1, for their values to be
     * of the form (<a_constant> * k/2^n, k in 0 .. 2^n), so that particular values
     * (PI/2, etc.) are "exactly" computed, as well as for other reasons.
     * 
     * Most math treatments I could find on the web, including "fast" ones,
     * usually take care of special cases (NaN, etc.) at the beginning, and
     * then deal with the general case, which adds a useless overhead for the
     * general (and common) case. In this class, special cases are only dealt
     * with when needed, and if the general case does not already handle them.
     */

    //--------------------------------------------------------------------------
    // CONFIGURATION
    //--------------------------------------------------------------------------

    private static final boolean USE_JDK_MATH = getBooleanProperty("jafama.usejdk", false);

    /**
     * Used for both log(double) and log10(double).
     */
    private static final boolean USE_REDEFINED_LOG = getBooleanProperty("jafama.fastlog", false);

    private static final boolean USE_REDEFINED_SQRT = getBooleanProperty("jafama.fastsqrt", false);

    /**
     * Set it to true if FastMath.sqrt(double) is slow
     * (more tables, but less calls to FastMath.sqrt(double)).
     */
    private static final boolean USE_POWTABS_FOR_ASIN = false;
    
    /**
     * Using two pow tab can just make things barely faster,
     * but could relatively hurt in case of cache-misses,
     * especially for methods that otherwise wouldn't rely
     * on any tab, so we don't use it.
     */
    private static final boolean USE_TWO_POW_TAB = false;

    /**
     * Because on some architectures, some casts can be slow,
     * especially for large values.
     * Might make things a bit slower for latest architectures,
     * but not as much as it makes them faster for older ones.
     */
    private static final boolean ANTI_SLOW_CASTS = true;
    
    /**
     * If some methods get JIT-optimized, they might crash
     * if they contain "(var == xxx)" with var being NaN
     * (can happen with Java 6u29).
     * 
     * The crash does not happen if we replace "==" with "<" or ">".
     * 
     * Only the code that has been observed to trigger the bug
     * has been modified.
     */
    private static final boolean ANTI_JIT_OPTIM_CRASH_ON_NAN = true;
    
    //--------------------------------------------------------------------------
    // GENERAL CONSTANTS
    //--------------------------------------------------------------------------

    /**
     * High approximation of PI, which is further from PI
     * than the low approximation Math.PI:
     *              PI ~= 3.14159265358979323846...
     *         Math.PI ~= 3.141592653589793
     * FastMath.PI_SUP ~= 3.1415926535897936
     */
    public static final double PI_SUP = Double.longBitsToDouble(Double.doubleToRawLongBits(Math.PI)+1);

    private static final double ONE_DIV_F2 = 1/2.0;
    private static final double ONE_DIV_F3 = 1/6.0;
    private static final double ONE_DIV_F4 = 1/24.0;

    private static final float TWO_POW_23_F = (float)NumbersUtils.twoPow(23);

    private static final double TWO_POW_24 = NumbersUtils.twoPow(24);
    private static final double TWO_POW_N24 = NumbersUtils.twoPow(-24);

    private static final double TWO_POW_26 = NumbersUtils.twoPow(26);
    private static final double TWO_POW_N26 = NumbersUtils.twoPow(-26);

    // First double value (from zero) such as (value+-1/value == value).
    private static final double TWO_POW_27 = NumbersUtils.twoPow(27);
    private static final double TWO_POW_N27 = NumbersUtils.twoPow(-27);

    private static final double TWO_POW_N28 = NumbersUtils.twoPow(-28);

    private static final double TWO_POW_52 = NumbersUtils.twoPow(52);

    private static final double TWO_POW_N55 = NumbersUtils.twoPow(-55);

    private static final double TWO_POW_66 = NumbersUtils.twoPow(66);

    private static final double TWO_POW_450 = NumbersUtils.twoPow(450);
    private static final double TWO_POW_N450 = NumbersUtils.twoPow(-450);

    private static final double TWO_POW_512 = NumbersUtils.twoPow(512);
    private static final double TWO_POW_N512 = NumbersUtils.twoPow(-512);

    private static final double TWO_POW_750 = NumbersUtils.twoPow(750);
    private static final double TWO_POW_N750 = NumbersUtils.twoPow(-750);

    /**
     * Double.MIN_NORMAL since Java 6.
     */
    private static final double DOUBLE_MIN_NORMAL = Double.longBitsToDouble(0x0010000000000000L); // 2.2250738585072014E-308

    // Not storing float/double mantissa size in constants,
    // for 23 and 52 are shorter to read and more
    // bitwise-explicit than some constant's name.
    
    private static final int MIN_DOUBLE_EXPONENT = -1074;
    private static final int MIN_DOUBLE_NORMAL_EXPONENT = -1022;
    private static final int MAX_DOUBLE_EXPONENT = 1023;

    private static final int MIN_FLOAT_NORMAL_EXPONENT = -126;
    private static final int MAX_FLOAT_EXPONENT = 127;

    private static final double LOG_2 = StrictMath.log(2.0);
    private static final double LOG_TWO_POW_27 = StrictMath.log(TWO_POW_27);
    private static final double LOG_DOUBLE_MAX_VALUE = StrictMath.log(Double.MAX_VALUE);

    private static final double INV_LOG_10 = 1.0/StrictMath.log(10.0);

    private static final double DOUBLE_BEFORE_60 = Double.longBitsToDouble(Double.doubleToRawLongBits(60.0)-1);

    //--------------------------------------------------------------------------
    // CONSTANTS FOR NORMALIZATIONS
    //--------------------------------------------------------------------------

    /*
     * Table of constants for 1/(2*PI), 282 Hex digits (enough for normalizing doubles).
     * 1/(2*PI) approximation = sum of ONE_OVER_TWOPI_TAB[i]*2^(-24*(i+1)).
     */
    private static final double ONE_OVER_TWOPI_TAB[] = {
        0x28BE60, 0xDB9391, 0x054A7F, 0x09D5F4, 0x7D4D37, 0x7036D8,
        0xA5664F, 0x10E410, 0x7F9458, 0xEAF7AE, 0xF1586D, 0xC91B8E,
        0x909374, 0xB80192, 0x4BBA82, 0x746487, 0x3F877A, 0xC72C4A,
        0x69CFBA, 0x208D7D, 0x4BAED1, 0x213A67, 0x1C09AD, 0x17DF90,
        0x4E6475, 0x8E60D4, 0xCE7D27, 0x2117E2, 0xEF7E4A, 0x0EC7FE,
        0x25FFF7, 0x816603, 0xFBCBC4, 0x62D682, 0x9B47DB, 0x4D9FB3,
        0xC9F2C2, 0x6DD3D1, 0x8FD9A7, 0x97FA8B, 0x5D49EE, 0xB1FAF9,
        0x7C5ECF, 0x41CE7D, 0xE294A4, 0xBA9AFE, 0xD7EC47};

    /*
     * Constants for 2*PI. Only the 23 most significant bits of each mantissa are used.
     * 2*PI approximation = sum of TWOPI_TAB<i>.
     */
    private static final double TWOPI_TAB0 = Double.longBitsToDouble(0x401921FB40000000L);
    private static final double TWOPI_TAB1 = Double.longBitsToDouble(0x3E94442D00000000L);
    private static final double TWOPI_TAB2 = Double.longBitsToDouble(0x3D18469880000000L);
    private static final double TWOPI_TAB3 = Double.longBitsToDouble(0x3B98CC5160000000L);
    private static final double TWOPI_TAB4 = Double.longBitsToDouble(0x3A101B8380000000L);

    private static final double INVPIO2 = Double.longBitsToDouble(0x3FE45F306DC9C883L); // 6.36619772367581382433e-01 53 bits of 2/pi
    private static final double PIO2_HI = Double.longBitsToDouble(0x3FF921FB54400000L); // 1.57079632673412561417e+00 first 33 bits of pi/2
    private static final double PIO2_LO = Double.longBitsToDouble(0x3DD0B4611A626331L); // 6.07710050650619224932e-11 pi/2 - PIO2_HI
    private static final double INVTWOPI = INVPIO2/4;
    private static final double TWOPI_HI = 4*PIO2_HI;
    private static final double TWOPI_LO = 4*PIO2_LO;

    // fdlibm uses 2^19*PI/2 here, but we normalize with % 2*PI instead of % PI/2,
    // and we can bear some more error.
    private static final double NORMALIZE_ANGLE_MAX_MEDIUM_DOUBLE = StrictMath.pow(2,20)*(2*Math.PI);

    /**
     * 2*Math.PI, normalized into [-PI,PI].
     * Computed using normalizeMinusPiPi(double).
     */
    private static final double TWO_MATH_PI_IN_MINUS_PI_PI = -2.449293598153844E-16;

    //--------------------------------------------------------------------------
    // CONSTANTS AND TABLES FOR SIN AND COS
    //--------------------------------------------------------------------------

    private static final int SIN_COS_TABS_SIZE = (1<<getTabSizePower(11)) + 1;
    private static final double SIN_COS_DELTA_HI = TWOPI_HI/(SIN_COS_TABS_SIZE-1);
    private static final double SIN_COS_DELTA_LO = TWOPI_LO/(SIN_COS_TABS_SIZE-1);
    private static final double SIN_COS_INDEXER = 1/(SIN_COS_DELTA_HI+SIN_COS_DELTA_LO);
    private static final double[] sinTab = new double[SIN_COS_TABS_SIZE];
    private static final double[] cosTab = new double[SIN_COS_TABS_SIZE];

    // Max abs value for fast modulo, above which we use regular angle normalization.
    // This value must be < (Integer.MAX_VALUE / SIN_COS_INDEXER), to stay in range of int type.
    // The higher it is, the higher the error, but also the faster it is for lower values.
    // If you set it to ((Integer.MAX_VALUE / SIN_COS_INDEXER) * 0.99), worse accuracy on double range is about 1e-10.
    private static final double SIN_COS_MAX_VALUE_FOR_INT_MODULO = ((Integer.MAX_VALUE>>9) / SIN_COS_INDEXER) * 0.99;

    //--------------------------------------------------------------------------
    // CONSTANTS AND TABLES FOR TAN
    //--------------------------------------------------------------------------

    // We use the following formula:
    // 1) tan(-x) = -tan(x)
    // 2) tan(x) = 1/tan(PI/2-x)
    // ---> we only have to compute tan(x) on [0,A] with PI/4<=A<PI/2.

    // We use indexing past look-up tables, so that indexing information
    // allows for fast recomputation of angle in [0,PI/2] range.
    private static final int TAN_VIRTUAL_TABS_SIZE = (1<<getTabSizePower(12)) + 1;

    // Must be >= 45deg, and supposed to be >= 51.4deg, as fdlibm code is not
    // supposed to work with values inferior to that (51.4deg is about
    // (PI/2-Double.longBitsToDouble(0x3FE5942800000000L))).
    private static final double TAN_MAX_VALUE_FOR_TABS = Math.toRadians(77.0);

    private static final int TAN_TABS_SIZE = (int)((TAN_MAX_VALUE_FOR_TABS/(Math.PI/2)) * (TAN_VIRTUAL_TABS_SIZE-1)) + 1;
    private static final double TAN_DELTA_HI = PIO2_HI/(TAN_VIRTUAL_TABS_SIZE-1);
    private static final double TAN_DELTA_LO = PIO2_LO/(TAN_VIRTUAL_TABS_SIZE-1);
    private static final double TAN_INDEXER = 1/(TAN_DELTA_HI+TAN_DELTA_LO);
    private static final double[] tanTab = new double[TAN_TABS_SIZE];
    private static final double[] tanDer1DivF1Tab = new double[TAN_TABS_SIZE];
    private static final double[] tanDer2DivF2Tab = new double[TAN_TABS_SIZE];
    private static final double[] tanDer3DivF3Tab = new double[TAN_TABS_SIZE];
    private static final double[] tanDer4DivF4Tab = new double[TAN_TABS_SIZE];

    // Max abs value for fast modulo, above which we use regular angle normalization.
    // This value must be < (Integer.MAX_VALUE / TAN_INDEXER), to stay in range of int type.
    // The higher it is, the higher the error, but also the faster it is for lower values.
    private static final double TAN_MAX_VALUE_FOR_INT_MODULO = (((Integer.MAX_VALUE>>9) / TAN_INDEXER) * 0.99);

    //--------------------------------------------------------------------------
    // CONSTANTS AND TABLES FOR ACOS, ASIN
    //--------------------------------------------------------------------------

    // We use the following formula:
    // 1) acos(x) = PI/2 - asin(x)
    // 2) asin(-x) = -asin(x)
    // ---> we only have to compute asin(x) on [0,1].
    // For values not close to +-1, we use look-up tables;
    // for values near +-1, we use code derived from fdlibm.

    // Supposed to be >= sin(77.2deg), as fdlibm code is supposed to work with values > 0.975,
    // but seems to work well enough as long as value >= sin(25deg).
    private static final double ASIN_MAX_VALUE_FOR_TABS = StrictMath.sin(Math.toRadians(73.0));

    private static final int ASIN_TABS_SIZE = (1<<getTabSizePower(13)) + 1;
    private static final double ASIN_DELTA = ASIN_MAX_VALUE_FOR_TABS/(ASIN_TABS_SIZE - 1);
    private static final double ASIN_INDEXER = 1/ASIN_DELTA;
    private static final double[] asinTab = new double[ASIN_TABS_SIZE];
    private static final double[] asinDer1DivF1Tab = new double[ASIN_TABS_SIZE];
    private static final double[] asinDer2DivF2Tab = new double[ASIN_TABS_SIZE];
    private static final double[] asinDer3DivF3Tab = new double[ASIN_TABS_SIZE];
    private static final double[] asinDer4DivF4Tab = new double[ASIN_TABS_SIZE];

    private static final double ASIN_MAX_VALUE_FOR_POWTABS = StrictMath.sin(Math.toRadians(88.6));
    private static final int ASIN_POWTABS_POWER = 84;

    private static final double ASIN_POWTABS_ONE_DIV_MAX_VALUE = 1/ASIN_MAX_VALUE_FOR_POWTABS;
    private static final int ASIN_POWTABS_SIZE = USE_POWTABS_FOR_ASIN ? (1<<getTabSizePower(12)) + 1 : 0;
    private static final int ASIN_POWTABS_SIZE_MINUS_ONE = ASIN_POWTABS_SIZE - 1;
    private static final double[] asinParamPowTab = new double[ASIN_POWTABS_SIZE];
    private static final double[] asinPowTab = new double[ASIN_POWTABS_SIZE];
    private static final double[] asinDer1DivF1PowTab = new double[ASIN_POWTABS_SIZE];
    private static final double[] asinDer2DivF2PowTab = new double[ASIN_POWTABS_SIZE];
    private static final double[] asinDer3DivF3PowTab = new double[ASIN_POWTABS_SIZE];
    private static final double[] asinDer4DivF4PowTab = new double[ASIN_POWTABS_SIZE];

    private static final double ASIN_PIO2_HI = Double.longBitsToDouble(0x3FF921FB54442D18L); // 1.57079632679489655800e+00
    private static final double ASIN_PIO2_LO = Double.longBitsToDouble(0x3C91A62633145C07L); // 6.12323399573676603587e-17
    private static final double ASIN_PS0 = Double.longBitsToDouble(0x3fc5555555555555L); //  1.66666666666666657415e-01
    private static final double ASIN_PS1 = Double.longBitsToDouble(0xbfd4d61203eb6f7dL); // -3.25565818622400915405e-01
    private static final double ASIN_PS2 = Double.longBitsToDouble(0x3fc9c1550e884455L); //  2.01212532134862925881e-01
    private static final double ASIN_PS3 = Double.longBitsToDouble(0xbfa48228b5688f3bL); // -4.00555345006794114027e-02
    private static final double ASIN_PS4 = Double.longBitsToDouble(0x3f49efe07501b288L); //  7.91534994289814532176e-04
    private static final double ASIN_PS5 = Double.longBitsToDouble(0x3f023de10dfdf709L); //  3.47933107596021167570e-05
    private static final double ASIN_QS1 = Double.longBitsToDouble(0xc0033a271c8a2d4bL); // -2.40339491173441421878e+00
    private static final double ASIN_QS2 = Double.longBitsToDouble(0x40002ae59c598ac8L); //  2.02094576023350569471e+00
    private static final double ASIN_QS3 = Double.longBitsToDouble(0xbfe6066c1b8d0159L); // -6.88283971605453293030e-01
    private static final double ASIN_QS4 = Double.longBitsToDouble(0x3fb3b8c5b12e9282L); //  7.70381505559019352791e-02

    //--------------------------------------------------------------------------
    // CONSTANTS AND TABLES FOR ATAN
    //--------------------------------------------------------------------------

    // We use the formula atan(-x) = -atan(x)
    // ---> we only have to compute atan(x) on [0,+infinity[.
    // For values corresponding to angles not close to +-PI/2, we use look-up tables;
    // for values corresponding to angles near +-PI/2, we use code derived from fdlibm.

    // Supposed to be >= tan(67.7deg), as fdlibm code is supposed to work with values > 2.4375.
    private static final double ATAN_MAX_VALUE_FOR_TABS = StrictMath.tan(Math.toRadians(74.0));

    private static final int ATAN_TABS_SIZE = (1<<getTabSizePower(12)) + 1;
    private static final double ATAN_DELTA = ATAN_MAX_VALUE_FOR_TABS/(ATAN_TABS_SIZE - 1);
    private static final double ATAN_INDEXER = 1/ATAN_DELTA;
    private static final double[] atanTab = new double[ATAN_TABS_SIZE];
    private static final double[] atanDer1DivF1Tab = new double[ATAN_TABS_SIZE];
    private static final double[] atanDer2DivF2Tab = new double[ATAN_TABS_SIZE];
    private static final double[] atanDer3DivF3Tab = new double[ATAN_TABS_SIZE];
    private static final double[] atanDer4DivF4Tab = new double[ATAN_TABS_SIZE];

    private static final double ATAN_HI3 = Double.longBitsToDouble(0x3ff921fb54442d18L); // 1.57079632679489655800e+00 atan(inf)hi
    private static final double ATAN_LO3 = Double.longBitsToDouble(0x3c91a62633145c07L); // 6.12323399573676603587e-17 atan(inf)lo
    private static final double ATAN_AT0 = Double.longBitsToDouble(0x3fd555555555550dL); //  3.33333333333329318027e-01
    private static final double ATAN_AT1 = Double.longBitsToDouble(0xbfc999999998ebc4L); // -1.99999999998764832476e-01
    private static final double ATAN_AT2 = Double.longBitsToDouble(0x3fc24924920083ffL); //  1.42857142725034663711e-01
    private static final double ATAN_AT3 = Double.longBitsToDouble(0xbfbc71c6fe231671L); // -1.11111104054623557880e-01
    private static final double ATAN_AT4 = Double.longBitsToDouble(0x3fb745cdc54c206eL); //  9.09088713343650656196e-02
    private static final double ATAN_AT5 = Double.longBitsToDouble(0xbfb3b0f2af749a6dL); // -7.69187620504482999495e-02
    private static final double ATAN_AT6 = Double.longBitsToDouble(0x3fb10d66a0d03d51L); //  6.66107313738753120669e-02
    private static final double ATAN_AT7 = Double.longBitsToDouble(0xbfadde2d52defd9aL); // -5.83357013379057348645e-02
    private static final double ATAN_AT8 = Double.longBitsToDouble(0x3fa97b4b24760debL); //  4.97687799461593236017e-02
    private static final double ATAN_AT9 = Double.longBitsToDouble(0xbfa2b4442c6a6c2fL); // -3.65315727442169155270e-02
    private static final double ATAN_AT10 = Double.longBitsToDouble(0x3f90ad3ae322da11L); // 1.62858201153657823623e-02 

    //--------------------------------------------------------------------------
    // CONSTANTS AND TABLES FOR TANH
    //--------------------------------------------------------------------------
    
    /**
     * Constant found experimentally:
     * StrictMath.tanh(TANH_1_THRESHOLD) = 1,
     * StrictMath.tanh(nextDown(TANH_1_THRESHOLD)) = FastMath.tanh(nextDown(TANH_1_THRESHOLD)) < 1.
     */
    private static final double TANH_1_THRESHOLD = 19.061547465398498;
            
    //--------------------------------------------------------------------------
    // CONSTANTS AND TABLES FOR ASINH AND ACOSH
    //--------------------------------------------------------------------------

    private static final double ASINH_LOG1P_THRESHOLD = 0.04;
    
    /**
     * sqrt(x*x+-1) should yield higher threshold, but it's enough due to
     * subsequent log.
     */
    private static final double ASINH_ACOSH_SQRT_ELISION_THRESHOLD = (1<<24);
    
    //--------------------------------------------------------------------------
    // CONSTANTS AND TABLES FOR EXP AND EXPM1
    //--------------------------------------------------------------------------

    private static final double EXP_OVERFLOW_LIMIT = Double.longBitsToDouble(0x40862E42FEFA39EFL); // 7.09782712893383973096e+02
    private static final double EXP_UNDERFLOW_LIMIT = Double.longBitsToDouble(0xC0874910D52D3051L); // -7.45133219101941108420e+02
    private static final int EXP_LO_DISTANCE_TO_ZERO_POT = 0;
    private static final int EXP_LO_DISTANCE_TO_ZERO = (1<<EXP_LO_DISTANCE_TO_ZERO_POT);
    private static final int EXP_LO_TAB_SIZE_POT = getTabSizePower(11);
    private static final int EXP_LO_TAB_SIZE = (1<<EXP_LO_TAB_SIZE_POT)+1;
    private static final int EXP_LO_TAB_MID_INDEX = ((EXP_LO_TAB_SIZE-1)/2);
    private static final int EXP_LO_INDEXING = EXP_LO_TAB_MID_INDEX/EXP_LO_DISTANCE_TO_ZERO;
    private static final int EXP_LO_INDEXING_DIV_SHIFT = EXP_LO_TAB_SIZE_POT-1-EXP_LO_DISTANCE_TO_ZERO_POT;
    private static final double[] expHiTab = new double[1+(int)EXP_OVERFLOW_LIMIT-(int)EXP_UNDERFLOW_LIMIT];
    private static final double[] expLoPosTab = new double[EXP_LO_TAB_SIZE];
    private static final double[] expLoNegTab = new double[EXP_LO_TAB_SIZE];

    //--------------------------------------------------------------------------
    // CONSTANTS AND TABLES FOR LOG AND LOG1P
    //--------------------------------------------------------------------------

    private static final int LOG_BITS = getTabSizePower(12);
    private static final int LOG_TAB_SIZE = (1<<LOG_BITS);
    private static final double[] logXLogTab = new double[LOG_TAB_SIZE];
    private static final double[] logXTab = new double[LOG_TAB_SIZE];
    private static final double[] logXInvTab = new double[LOG_TAB_SIZE];

    //--------------------------------------------------------------------------
    // TABLE FOR POWERS OF TWO
    //--------------------------------------------------------------------------

    private static final double[] twoPowTab = (USE_TWO_POW_TAB ? new double[(MAX_DOUBLE_EXPONENT-MIN_DOUBLE_EXPONENT)+1] : null);

    //--------------------------------------------------------------------------
    // CONSTANTS AND TABLES FOR SQRT
    //--------------------------------------------------------------------------

    private static final int SQRT_LO_BITS = getTabSizePower(12);
    private static final int SQRT_LO_TAB_SIZE = (1<<SQRT_LO_BITS);
    private static final double[] sqrtXSqrtHiTab = new double[MAX_DOUBLE_EXPONENT-MIN_DOUBLE_EXPONENT+1];
    private static final double[] sqrtXSqrtLoTab = new double[SQRT_LO_TAB_SIZE];
    private static final double[] sqrtSlopeHiTab = new double[MAX_DOUBLE_EXPONENT-MIN_DOUBLE_EXPONENT+1];
    private static final double[] sqrtSlopeLoTab = new double[SQRT_LO_TAB_SIZE];

    //--------------------------------------------------------------------------
    // CONSTANTS AND TABLES FOR CBRT
    //--------------------------------------------------------------------------

    private static final int CBRT_LO_BITS = getTabSizePower(12);
    private static final int CBRT_LO_TAB_SIZE = (1<<CBRT_LO_BITS);
    // For CBRT_LO_BITS = 12:
    // cbrtXCbrtLoTab[0] = 1.0.
    // cbrtXCbrtLoTab[1] = cbrt(1. 000000000000 1111111111111111111111111111111111111111b)
    // cbrtXCbrtLoTab[2] = cbrt(1. 000000000001 1111111111111111111111111111111111111111b)
    // cbrtXCbrtLoTab[3] = cbrt(1. 000000000010 1111111111111111111111111111111111111111b)
    // cbrtXCbrtLoTab[4] = cbrt(1. 000000000011 1111111111111111111111111111111111111111b)
    // etc.
    private static final double[] cbrtXCbrtHiTab = new double[MAX_DOUBLE_EXPONENT-MIN_DOUBLE_EXPONENT+1];
    private static final double[] cbrtXCbrtLoTab = new double[CBRT_LO_TAB_SIZE];
    private static final double[] cbrtSlopeHiTab = new double[MAX_DOUBLE_EXPONENT-MIN_DOUBLE_EXPONENT+1];
    private static final double[] cbrtSlopeLoTab = new double[CBRT_LO_TAB_SIZE];

    //--------------------------------------------------------------------------
    // PUBLIC TREATMENTS
    //--------------------------------------------------------------------------

    /*
     * trigonometry
     */
    
    /**
     * @param angle Angle in radians.
     * @return Angle sine.
     */
    public static double sin(double angle) {
        if (USE_JDK_MATH) {
            return Math.sin(angle);
        }
        boolean negateResult;
        if (angle < 0.0) {
            angle = -angle;
            negateResult = true;
        } else {
            negateResult = false;
        }
        if (angle > SIN_COS_MAX_VALUE_FOR_INT_MODULO) {
            // Faster than using normalizeZeroTwoPi.
            angle = remainderTwoPi(angle);
            if (angle < 0.0) {
                angle += 2*Math.PI;
            }
        }
        int index = (int)(angle * SIN_COS_INDEXER + 0.5);
        double delta = (angle - index * SIN_COS_DELTA_HI) - index * SIN_COS_DELTA_LO;
        index &= (SIN_COS_TABS_SIZE-2); // index % (SIN_COS_TABS_SIZE-1)
        double indexSin = sinTab[index];
        double indexCos = cosTab[index];
        double result = indexSin + delta * (indexCos + delta * (-indexSin * ONE_DIV_F2 + delta * (-indexCos * ONE_DIV_F3 + delta * indexSin * ONE_DIV_F4)));
        return negateResult ? -result : result;
    }

    /**
     * Quick sin, with accuracy of about 1.6e-3 (PI/<look-up tabs size>)
     * for |angle| < 6588397.0 (Integer.MAX_VALUE * (2*PI/<look-up tabs size>)),
     * and no accuracy at all for larger values.
     * 
     * @param angle Angle in radians.
     * @return Angle sine.
     */
    public static double sinQuick(double angle) {
        if (USE_JDK_MATH) {
            return Math.sin(angle);
        }
        return cosTab[((int)(Math.abs(angle-Math.PI/2) * SIN_COS_INDEXER + 0.5)) & (SIN_COS_TABS_SIZE-2)];
    }

    /**
     * @param angle Angle in radians.
     * @return Angle cosine.
     */
    public static double cos(double angle) {
        if (USE_JDK_MATH) {
            return Math.cos(angle);
        }
        angle = Math.abs(angle);
        if (angle > SIN_COS_MAX_VALUE_FOR_INT_MODULO) {
            // Faster than using normalizeZeroTwoPi.
            angle = remainderTwoPi(angle);
            if (angle < 0.0) {
                angle += 2*Math.PI;
            }
        }
        // index: possibly outside tables range.
        int index = (int)(angle * SIN_COS_INDEXER + 0.5);
        double delta = (angle - index * SIN_COS_DELTA_HI) - index * SIN_COS_DELTA_LO;
        // Making sure index is within tables range.
        // Last value of each table is the same than first, so we ignore it (tabs size minus one) for modulo.
        index &= (SIN_COS_TABS_SIZE-2); // index % (SIN_COS_TABS_SIZE-1)
        double indexCos = cosTab[index];
        double indexSin = sinTab[index];
        return indexCos + delta * (-indexSin + delta * (-indexCos * ONE_DIV_F2 + delta * (indexSin * ONE_DIV_F3 + delta * indexCos * ONE_DIV_F4)));
    }

    /**
     * Quick cos, with accuracy of about 1.6e-3 (PI/<look-up tabs size>)
     * for |angle| < 6588397.0 (Integer.MAX_VALUE * (2*PI/<look-up tabs size>)),
     * and no accuracy at all for larger values.
     * 
     * @param angle Angle in radians.
     * @return Angle cosine.
     */
    public static double cosQuick(double angle) {
        if (USE_JDK_MATH) {
            return Math.cos(angle);
        }
        return cosTab[((int)(Math.abs(angle) * SIN_COS_INDEXER + 0.5)) & (SIN_COS_TABS_SIZE-2)];
    }

    /**
     * Computes sine and cosine together.
     * 
     * @param angle Angle in radians.
     * @param cosine (out) Angle cosine.
     * @return Angle sine.
     */
    public static double sinAndCos(double angle, DoubleWrapper cosine) {
        if (USE_JDK_MATH) {
            cosine.value = Math.cos(angle);
            return Math.sin(angle);
        }
        // Using the same algorithm than sin(double) method, and computing also cosine at the end.
        boolean negateResult;
        if (angle < 0.0) {
            angle = -angle;
            negateResult = true;
        } else {
            negateResult = false;
        }
        if (angle > SIN_COS_MAX_VALUE_FOR_INT_MODULO) {
            // Faster than using normalizeZeroTwoPi.
            angle = remainderTwoPi(angle);
            if (angle < 0.0) {
                angle += 2*Math.PI;
            }
        }
        int index = (int)(angle * SIN_COS_INDEXER + 0.5);
        double delta = (angle - index * SIN_COS_DELTA_HI) - index * SIN_COS_DELTA_LO;
        index &= (SIN_COS_TABS_SIZE-2); // index % (SIN_COS_TABS_SIZE-1)
        double indexSin = sinTab[index];
        double indexCos = cosTab[index];
        cosine.value = indexCos + delta * (-indexSin + delta * (-indexCos * ONE_DIV_F2 + delta * (indexSin * ONE_DIV_F3 + delta * indexCos * ONE_DIV_F4)));
        double result = indexSin + delta * (indexCos + delta * (-indexSin * ONE_DIV_F2 + delta * (-indexCos * ONE_DIV_F3 + delta * indexSin * ONE_DIV_F4)));
        return negateResult ? -result : result;
    }

    /**
     * Can have very bad relative error near +-PI/2,
     * but of the same magnitude than the relative delta between
     * StrictMath.tan(PI/2) and StrictMath.tan(nextDown(PI/2)).
     * 
     * @param angle Angle in radians.
     * @return Angle tangent.
     */
    public static double tan(double angle) {
        if (USE_JDK_MATH) {
            return Math.tan(angle);
        }
        if (Math.abs(angle) > TAN_MAX_VALUE_FOR_INT_MODULO) {
            // Faster than using normalizeMinusHalfPiHalfPi.
            angle = remainderTwoPi(angle);
            if (angle < -Math.PI/2) {
                angle += Math.PI;
            } else if (angle > Math.PI/2) {
                angle -= Math.PI;
            }
        }
        boolean negateResult;
        if (angle < 0.0) {
            angle = -angle;
            negateResult = true;
        } else {
            negateResult = false;
        }
        int index = (int)(angle * TAN_INDEXER + 0.5);
        double delta = (angle - index * TAN_DELTA_HI) - index * TAN_DELTA_LO;
        // index modulo PI, i.e. 2*(virtual tab size minus one). 
        index &= (2*(TAN_VIRTUAL_TABS_SIZE-1)-1); // index % (2*(TAN_VIRTUAL_TABS_SIZE-1))
        // Here, index is in [0,2*(TAN_VIRTUAL_TABS_SIZE-1)-1], i.e. indicates an angle in [0,PI[.
        if (index > (TAN_VIRTUAL_TABS_SIZE-1)) {
            index = (2*(TAN_VIRTUAL_TABS_SIZE-1)) - index;
            delta = -delta;
            negateResult = !negateResult;
        }
        double result;
        if (index < TAN_TABS_SIZE) {
            result = tanTab[index] + delta * (tanDer1DivF1Tab[index] + delta * (tanDer2DivF2Tab[index] + delta * (tanDer3DivF3Tab[index] + delta * tanDer4DivF4Tab[index])));
        } else { // angle in ]TAN_MAX_VALUE_FOR_TABS,TAN_MAX_VALUE_FOR_INT_MODULO], or angle is NaN
            // Using tan(angle) == 1/tan(PI/2-angle) formula: changing angle (index and delta), and inverting.
            index = (TAN_VIRTUAL_TABS_SIZE-1) - index;
            result = 1/(tanTab[index] - delta * (tanDer1DivF1Tab[index] - delta * (tanDer2DivF2Tab[index] - delta * (tanDer3DivF3Tab[index] - delta * tanDer4DivF4Tab[index]))));
        }
        return negateResult ? -result : result;
    }

    /**
     * @param value Value in [-1,1].
     * @return Value arcsine, in radians, in [-PI/2,PI/2].
     */
    public static double asin(double value) {
        if (USE_JDK_MATH) {
            return Math.asin(value);
        }
        boolean negateResult;
        if (value < 0.0) {
            value = -value;
            negateResult = true;
        } else {
            negateResult = false;
        }
        if (value <= ASIN_MAX_VALUE_FOR_TABS) {
            int index = (int)(value * ASIN_INDEXER + 0.5);
            double delta = value - index * ASIN_DELTA;
            double result = asinTab[index] + delta * (asinDer1DivF1Tab[index] + delta * (asinDer2DivF2Tab[index] + delta * (asinDer3DivF3Tab[index] + delta * asinDer4DivF4Tab[index])));
            return negateResult ? -result : result;
        } else if (USE_POWTABS_FOR_ASIN && (value <= ASIN_MAX_VALUE_FOR_POWTABS)) {
            int index = (int)(FastMath.powFast(value * ASIN_POWTABS_ONE_DIV_MAX_VALUE, ASIN_POWTABS_POWER) * ASIN_POWTABS_SIZE_MINUS_ONE + 0.5);
            double delta = value - asinParamPowTab[index];
            double result = asinPowTab[index] + delta * (asinDer1DivF1PowTab[index] + delta * (asinDer2DivF2PowTab[index] + delta * (asinDer3DivF3PowTab[index] + delta * asinDer4DivF4PowTab[index])));
            return negateResult ? -result : result;
        } else { // value > ASIN_MAX_VALUE_FOR_TABS, or value is NaN
            // This part is derived from fdlibm.
            if (value < 1.0) {
                double t = (1.0 - value)*0.5;
                double p = t*(ASIN_PS0+t*(ASIN_PS1+t*(ASIN_PS2+t*(ASIN_PS3+t*(ASIN_PS4+t*ASIN_PS5)))));
                double q = 1.0+t*(ASIN_QS1+t*(ASIN_QS2+t*(ASIN_QS3+t*ASIN_QS4)));
                double s = FastMath.sqrt(t);
                double z = s+s*(p/q);
                double result = ASIN_PIO2_HI-((z+z)-ASIN_PIO2_LO);
                return negateResult ? -result : result;
            } else { // value >= 1.0, or value is NaN
                if (value == 1.0) {
                    return negateResult ? -Math.PI/2 : Math.PI/2;
                } else {
                    return Double.NaN;
                }
            }
        }
    }

    /**
     * If value is not NaN and is outside [-1,1] range, closest value in this range is used.
     * 
     * @param value Value in [-1,1].
     * @return Value arcsine, in radians, in [-PI/2,PI/2].
     */
    public static double asinInRange(double value) {
        if (value <= -1) {
            return -Math.PI/2;
        } else if (value >= 1) {
            return Math.PI/2;
        } else {
            return FastMath.asin(value);
        }
    }

    /**
     * @param value Value in [-1,1].
     * @return Value arccosine, in radians, in [0,PI].
     */
    public static double acos(double value) {
        if (USE_JDK_MATH) {
            return Math.acos(value);
        }
        return Math.PI/2 - FastMath.asin(value);
    }

    /**
     * If value is not NaN and is outside [-1,1] range, closest value in this range is used.
     * 
     * @param value Value in [-1,1].
     * @return Value arccosine, in radians, in [0,PI].
     */
    public static double acosInRange(double value) {
        if (value <= -1) {
            return Math.PI;
        } else if (value >= 1) {
            return 0.0;
        } else {
            return FastMath.acos(value);
        }
    }

    /**
     * @param value A double value.
     * @return Value arctangent, in radians, in [-PI/2,PI/2].
     */
    public static double atan(double value) {
        if (USE_JDK_MATH) {
            return Math.atan(value);
        }
        boolean negateResult;
        if (value < 0.0) {
            value = -value;
            negateResult = true;
        } else {
            negateResult = false;
        }
        if (value == 1.0) {
            // We want "exact" result for 1.0.
            return negateResult ? -Math.PI/4 : Math.PI/4;
        } else if (value <= ATAN_MAX_VALUE_FOR_TABS) {
            int index = (int)(value * ATAN_INDEXER + 0.5);
            double delta = value - index * ATAN_DELTA;
            double result = atanTab[index] + delta * (atanDer1DivF1Tab[index] + delta * (atanDer2DivF2Tab[index] + delta * (atanDer3DivF3Tab[index] + delta * atanDer4DivF4Tab[index])));
            return negateResult ? -result : result;
        } else { // value > ATAN_MAX_VALUE_FOR_TABS, or value is NaN
            // This part is derived from fdlibm.
            if (value < TWO_POW_66) {
                double x = -1/value;
                double x2 = x*x;
                double x4 = x2*x2;
                double s1 = x2*(ATAN_AT0+x4*(ATAN_AT2+x4*(ATAN_AT4+x4*(ATAN_AT6+x4*(ATAN_AT8+x4*ATAN_AT10)))));
                double s2 = x4*(ATAN_AT1+x4*(ATAN_AT3+x4*(ATAN_AT5+x4*(ATAN_AT7+x4*ATAN_AT9))));
                double result = ATAN_HI3-((x*(s1+s2)-ATAN_LO3)-x);
                return negateResult ? -result : result;
            } else { // value >= 2^66, or value is NaN
                if (value != value) {
                    return Double.NaN;
                } else {
                    return negateResult ? -Math.PI/2 : Math.PI/2;
                }
            }
        }
    }

    /**
     * For special values for which multiple conventions could be adopted,
     * behaves like Math.atan2(double,double).
     * 
     * @param y Coordinate on y axis.
     * @param x Coordinate on x axis.
     * @return Angle from x axis positive side to (x,y) position, in radians, in [-PI,PI].
     *         Angle measure is positive when going from x axis to y axis (positive sides).
     */
    public static double atan2(double y, double x) {
        if (USE_JDK_MATH) {
            return Math.atan2(y,x);
        }
        /*
         * Using sub-methods, to make method lighter for general case,
         * and to avoid JIT-optimization crash on NaN.
         */
        if (x > 0.0) {
            if (y == 0.0) {
                // +-0.0
                return y;
            }
            if (x == Double.POSITIVE_INFINITY) {
                return atan2_pinf_yyy(y);
            } else {
                return FastMath.atan(y/x);
            }
        } else if (x < 0.0) {
            if (y == 0.0) {
                return FastMath.signFromBit(y) * Math.PI;
            }
            if (x == Double.NEGATIVE_INFINITY) {
                return atan2_ninf_yyy(y);
            } else if (y > 0.0) {
                return Math.PI/2 + FastMath.atan(-x/y);
            } else if (y < 0.0) {
                return -Math.PI/2 - FastMath.atan(x/y);
            } else {
                return Double.NaN;
            }
        } else {
            return atan2_zeroOrNaN_yyy(x, y);
        }
    }

    /**
     * Gives same result as Math.toRadians for some particular values
     * like 90.0, 180.0 or 360.0, but is faster (no division).
     * 
     * @param angdeg Angle value in degrees.
     * @return Angle value in radians.
     */
    public static double toRadians(double angdeg) {
        if (USE_JDK_MATH) {
            return Math.toRadians(angdeg);
        }
        return angdeg * (Math.PI/180);
    }

    /**
     * Gives same result as Math.toDegrees for some particular values
     * like Math.PI/2, Math.PI or 2*Math.PI, but is faster (no division).
     * 
     * @param angrad Angle value in radians.
     * @return Angle value in degrees.
     */
    public static double toDegrees(double angrad) {
        if (USE_JDK_MATH) {
            return Math.toDegrees(angrad);
        }
        return angrad * (180/Math.PI);
    }

    /**
     * @param sign Sign of the angle: true for positive, false for negative.
     * @param degrees Degrees, in [0,180].
     * @param minutes Minutes, in [0,59].
     * @param seconds Seconds, in [0.0,60.0[.
     * @return Angle in radians.
     */
    public static double toRadians(boolean sign, int degrees, int minutes, double seconds) {
        return FastMath.toRadians(FastMath.toDegrees(sign, degrees, minutes, seconds));
    }

    /**
     * @param sign Sign of the angle: true for positive, false for negative.
     * @param degrees Degrees, in [0,180].
     * @param minutes Minutes, in [0,59].
     * @param seconds Seconds, in [0.0,60.0[.
     * @return Angle in degrees.
     */
    public static double toDegrees(boolean sign, int degrees, int minutes, double seconds) {
        double signFactor = sign ? 1.0 : -1.0;
        return signFactor * (degrees + (1.0/60)*(minutes + (1.0/60)*seconds));
    }

    /**
     * @param angrad Angle in radians.
     * @param degrees (out) Degrees, in [0,180].
     * @param minutes (out) Minutes, in [0,59].
     * @param seconds (out) Seconds, in [0.0,60.0[.
     * @return True if the resulting angle in [-180deg,180deg] is positive, false if it is negative.
     */
    public static boolean toDMS(double angrad, IntWrapper degrees, IntWrapper minutes, DoubleWrapper seconds) {
        // Computing longitude DMS.
        double tmp = FastMath.toDegrees(FastMath.normalizeMinusPiPi(angrad));
        boolean isNeg = (tmp < 0.0);
        if (isNeg) {
            tmp = -tmp;
        }
        degrees.value = (int)tmp;
        tmp = (tmp-degrees.value)*60.0;
        minutes.value = (int)tmp;
        seconds.value = Math.min((tmp-minutes.value)*60.0,DOUBLE_BEFORE_60);
        return !isNeg;
    }

    /**
     * NB: Since 2*Math.PI < 2*PI, a span of 2*Math.PI does not mean full angular range.
     * ex.: isInClockwiseDomain(0.0, 2*Math.PI, -1e-20) returns false.
     * ---> For full angular range, use a span > 2*Math.PI, like 2*PI_SUP constant of this class.
     * 
     * @param startAngRad An angle, in radians.
     * @param angSpanRad An angular span, >= 0.0, in radians.
     * @param angRad An angle, in radians.
     * @return True if angRad is in the clockwise angular domain going from startAngRad, over angSpanRad,
     *         extremities included, false otherwise.
     */
    public static boolean isInClockwiseDomain(double startAngRad, double angSpanRad, double angRad) {
        if (Math.abs(angRad) < -TWO_MATH_PI_IN_MINUS_PI_PI) {
            // special case for angular values of small magnitude
            if (angSpanRad <= 2*Math.PI) {
                if (angSpanRad < 0.0) {
                    // empty domain
                    return false;
                }
                // angSpanRad is in [0.0,2*Math.PI]
                startAngRad = FastMath.normalizeMinusPiPi(startAngRad);
                double endAngRad = FastMath.normalizeMinusPiPi(startAngRad + angSpanRad);
                if (startAngRad <= endAngRad) {
                    return (angRad >= startAngRad) && (angRad <= endAngRad);
                } else {
                    return (angRad >= startAngRad) || (angRad <= endAngRad);
                }
            } else { // angSpanRad > 2*Math.PI, or is NaN
                return (angSpanRad == angSpanRad);
            }
        } else {
            // general case
            return (FastMath.normalizeZeroTwoPi(angRad - startAngRad) <= angSpanRad);
        }
    }
    
    /*
     * hyperbolic trigonometry
     */
    
    /**
     * Some properties of sinh(x) = (exp(x)-exp(-x))/2:
     * 1) defined on ]-Infinity,+Infinity[
     * 2) result in ]-Infinity,+Infinity[
     * 3) sinh(x) = -sinh(-x) (implies sinh(0) = 0)
     * 4) sinh(epsilon) ~= epsilon
     * 5) lim(sinh(x),x->+Infinity) = +Infinity
     *    (y increasing exponentially faster than x)
     * 6) reaches +Infinity (double overflow) for x >= 710.475860073944,
     *    i.e. a bit further than exp(x)
     * 
     * @param value A double value.
     * @return Value hyperbolic sine.
     */
    public static double sinh(double value) {
        if (USE_JDK_MATH) {
            return Math.sinh(value);
        }
        // sinh(x) = (exp(x)-exp(-x))/2
        double h;
        if (value < 0.0) {
            value = -value;
            h = -0.5;
        } else {
            h = 0.5;
        }
        if (value < 22.0) {
            if (value < TWO_POW_N28) {
                return (h < 0.0) ? -value : value;
            } else {
                // sinh(x)
                // = (exp(x)-exp(-x))/2
                // = (exp(x)-1/exp(x))/2
                // = (expm1(x) + 1 - 1/(expm1(x)+1))/2
                // = (expm1(x) + (expm1(x)+1)/(expm1(x)+1) - 1/(expm1(x)+1))/2
                // = (expm1(x) + expm1(x)/(expm1(x)+1))/2
                double t = FastMath.expm1(value);
                // Might be more accurate, if value < 1: return h*((t+t)-t*t/(t+1.0)).
                return h * (t + t/(t+1.0));
            }
        } else if (value < LOG_DOUBLE_MAX_VALUE) {
            return h * FastMath.exp(value);
        } else {
            double t = FastMath.exp(value*0.5);
            return (h*t)*t;
        }
    }

    /**
     * Some properties of cosh(x) = (exp(x)+exp(-x))/2:
     * 1) defined on ]-Infinity,+Infinity[
     * 2) result in ]1,+Infinity[
     * 3) cosh(0) = 1 (by continuity)
     * 4) cosh(x) = cosh(-x)
     * 5) lim(cosh(x),x->+Infinity) = +Infinity
     *    (y increasing exponentially faster than x)
     * 6) reaches +Infinity (double overflow) for x >= 710.475860073944,
     *    i.e. a bit further than exp(x)
     * 
     * @param value A double value.
     * @return Value hyperbolic cosine.
     */
    public static double cosh(double value) {
        if (USE_JDK_MATH) {
            return Math.cosh(value);
        }
        // cosh(x) = (exp(x)+exp(-x))/2
        if (value < 0.0) {
            value = -value;
        }
        if (value < LOG_TWO_POW_27) {
            if (value < TWO_POW_N27) {
                // cosh(x)
                // = (exp(x)+exp(-x))/2
                // = ((1+x+x^2/2!+...) + (1-x+x^2/2!-...))/2
                // = 1+x^2/2!+x^4/4!+...
                // For value of x small in magnitude, the sum of the terms does not add to 1.
                return 1;
            } else {
                // cosh(x)
                // = (exp(x)+exp(-x))/2
                // = (exp(x)+1/exp(x))/2
                double t = FastMath.exp(value);
                return 0.5 * (t+1/t);
            }
        } else if (value < LOG_DOUBLE_MAX_VALUE) {
            return 0.5 * FastMath.exp(value);
        } else {
            double t = FastMath.exp(value*0.5);
            return (0.5*t)*t;
        }
    }

    /**
     * Much more accurate than cosh(value)-1,
     * for arguments (and results) close to zero.
     * 
     * coshm1(-0.0) = -0.0, for homogeneity with
     * acosh1p(-0.0) = -0.0.
     * 
     * @param value A double value.
     * @return Value hyperbolic cosine, minus 1.
     */
    public static double coshm1(double value) {
        // cosh(x)-1 = (exp(x)+exp(-x))/2 - 1
        if (value < 0.0) {
            value = -value;
        }
        if (value < LOG_TWO_POW_27) {
            if (value < TWO_POW_N27) {
                if (value == 0.0) {
                    // +-0.0
                    return value;
                }
                // Using (expm1(x)+expm1(-x))/2
                // is not accurate for tiny values,
                // for expm1 results are of higher
                // magnitude than the result and
                // of different signs, such as their
                // sum is not accurate.
                // cosh(x) - 1
                // = (exp(x)+exp(-x))/2 - 1
                // = ((1+x+x^2/2!+...) + (1-x+x^2/2!-...))/2 - 1
                // = x^2/2!+x^4/4!+...
                // ~= x^2 * (1/2 + x^2 * 1/24)
                //  = x^2 * 0.5 (since x < 2^-27)
                return 0.5 * value*value;
            } else {
                // cosh(x) - 1
                // = (exp(x)+exp(-x))/2 - 1
                // = (exp(x)-1+exp(-x)-1)/2
                // = (expm1(x)+expm1(-x))/2
                return 0.5 * (FastMath.expm1(value)+FastMath.expm1(-value));
            }
        } else if (value < LOG_DOUBLE_MAX_VALUE) {
            return 0.5 * FastMath.exp(value) - 1.0;
        } else {
            // No need to subtract 1 from result.
            double t = FastMath.exp(value*0.5);
            return (0.5*t)*t;
        }
    }

    /**
     * Computes hyperbolic sine and hyperbolic cosine together.
     * 
     * @param value A double value.
     * @param hcosine (out) Value hyperbolic cosine.
     * @return Value hyperbolic sine.
     */
    public static double sinhAndCosh(double value, DoubleWrapper hcosine) {
        if (USE_JDK_MATH) {
            hcosine.value = Math.cosh(value);
            return Math.sinh(value);
        }
        // Mixup of sinh and cosh treatments: if you modify them,
        // you might want to also modify this.
        double h;
        if (value < 0.0) {
            value = -value;
            h = -0.5;
        } else {
            h = 0.5;
        }
        final double hsine;
        // LOG_TWO_POW_27 = 18.714973875118524
        if (value < LOG_TWO_POW_27) { // test from cosh
            // sinh
            if (value < TWO_POW_N28) {
                hsine = (h < 0.0) ? -value : value;
            } else {
                double t = FastMath.expm1(value);
                hsine = h * (t + t/(t+1.0));
            }
            // cosh
            if (value < TWO_POW_N27) {
                hcosine.value = 1;
            } else {
                double t = FastMath.exp(value);
                hcosine.value = 0.5 * (t+1/t);
            }
        } else if (value < 22.0) { // test from sinh
            // Here, value is in [18.714973875118524,22.0[.
            double t = FastMath.expm1(value);
            hsine = h * (t + t/(t+1.0));
            hcosine.value = 0.5 * (t+1.0);
        } else {
            if (value < LOG_DOUBLE_MAX_VALUE) {
                hsine = h * FastMath.exp(value);
            } else {
                double t = FastMath.exp(value*0.5);
                hsine = (h*t)*t;
            }
            hcosine.value = Math.abs(hsine);
        }
        return hsine;
    }

    /**
     * Some properties of tanh(x) = sinh(x)/cosh(x) = (exp(2*x)-1)/(exp(2*x)+1):
     * 1) defined on ]-Infinity,+Infinity[
     * 2) result in ]-1,1[
     * 3) tanh(x) = -tanh(-x) (implies tanh(0) = 0)
     * 4) tanh(epsilon) ~= epsilon
     * 5) lim(tanh(x),x->+Infinity) = 1
     * 6) reaches 1 (double loss of precision) for x = 19.061547465398498
     * 
     * @param value A double value.
     * @return Value hyperbolic tangent.
     */
    public static double tanh(double value) {
        if (USE_JDK_MATH) {
            return Math.tanh(value);
        }
        // tanh(x) = sinh(x)/cosh(x)
        //         = (exp(x)-exp(-x))/(exp(x)+exp(-x))
        //         = (exp(2*x)-1)/(exp(2*x)+1)
        boolean negateResult;
        if (value < 0.0) {
            value = -value;
            negateResult = true;
        } else {
            negateResult = false;
        }
        double z;
        if (value < TANH_1_THRESHOLD) {
            if (value < TWO_POW_N55) {
                return negateResult ? -value*(1.0-value) : value*(1.0+value);
            } else if (value >= 1) {
                z = 1.0-2.0/(FastMath.expm1(value+value)+2.0);
            } else {
                double t = FastMath.expm1(-(value+value));
                z = -t/(t+2.0);
            }
        } else {
            z = (value != value) ? Double.NaN : 1.0;
        }
        return negateResult ? -z : z;
    }

    /**
     * Some properties of asinh(x) = log(x + sqrt(x^2 + 1))
     * 1) defined on ]-Infinity,+Infinity[
     * 2) result in ]-Infinity,+Infinity[
     * 3) asinh(x) = -asinh(-x) (implies asinh(0) = 0)
     * 4) asinh(epsilon) ~= epsilon
     * 5) lim(asinh(x),x->+Infinity) = +Infinity
     *    (y increasing logarithmically slower than x)
     * 
     * @param value A double value.
     * @return Value hyperbolic arcsine.
     */
    public static double asinh(double value) {
        // asinh(x) = log(x + sqrt(x^2 + 1))
        boolean negateResult;
        if (value < 0.0) {
            value = -value;
            negateResult = true;
        } else {
            negateResult = false;
        }
        double result;
        // (about) smallest possible for
        // non-log1p case to be accurate.
        if (value < ASINH_LOG1P_THRESHOLD) {
            // Around this range, FDLIBM uses
            // log1p(value+value*value/(1+sqrt(value*value+1))),
            // but it's slower, so we don't use it.
            /*
             * If x is close to zero, log argument is close to 1,
             * so to avoid precision loss we use log1p(double),
             * with
             * (1+x)^p = 1 + p * x + (p*(p-1))/2! * x^2 + (p*(p-1)*(p-2))/3! * x^3 + ...
             * (1+x)^p = 1 + p * x * (1 + (p-1)/2 * x * (1 + (p-2)/3 * x + ...)
             * (1+x)^0.5 = 1 + 0.5 * x * (1 + (0.5-1)/2 * x * (1 + (0.5-2)/3 * x + ...)
             * (1+x^2)^0.5 = 1 + 0.5 * x^2 * (1 + (0.5-1)/2 * x^2 * (1 + (0.5-2)/3 * x^2 + ...)
             * x + (1+x^2)^0.5 = 1 + x * (1 + 0.5 * x * (1 + (0.5-1)/2 * x^2 * (1 + (0.5-2)/3 * x^2 + ...))
             * so
             * asinh(x) = log1p(x * (1 + 0.5 * x * (1 + (0.5-1)/2 * x^2 * (1 + (0.5-2)/3 * x^2 + ...)))
             */
            final double x = value;
            final double x2 = x*x;
            // Enough terms for good accuracy,
            // given our threshold.
            final double argLog1p = (x *
                    (1 + 0.5 * x
                            * (1 + ((0.5-1)/2 * x2
                                    * (1 + (0.5-2)/3 * x2
                                            * (1 + (0.5-3)/4 * x2
                                                    * (1 + (0.5-4)/5 * x2
                                                            )))))));
            result = FastMath.log1p(argLog1p);
        } else if (value < ASINH_ACOSH_SQRT_ELISION_THRESHOLD) {
            // Around this range, FDLIBM uses
            // log(2*value+1/(value+sqrt(value*value+1))),
            // but it involves a additional division
            // so we don't use it.
            result = FastMath.log(value + FastMath.sqrt(value*value + 1.0));
        } else {
            // log(2*value) would overflow for value > Double.MAX_VALUE/2,
            // so we compute otherwise.
            result = LOG_2 + FastMath.log(value);
        }
        return negateResult ? -result : result;
    }
    
    /**
     * Some properties of acosh(x) = log(x + sqrt(x^2 - 1)):
     * 1) defined on ]1,+Infinity[
     * 2) result in ]0,+Infinity[ (by convention, since cosh(x) = cosh(-x))
     * 3) acosh(1) = 0 (by continuity)
     * 4) acosh(1+epsilon) ~= log(1 + sqrt(2*epsilon)) ~= sqrt(2*epsilon)
     * 5) lim(acosh(x),x->+Infinity) = +Infinity
     *    (y increasing logarithmically slower than x)
     * 
     * @param value A double value.
     * @return Value hyperbolic arccosine.
     */
    public static double acosh(double value) {
        if (!(value > 1.0)) {
            // NaN, or value <= 1
            if (ANTI_JIT_OPTIM_CRASH_ON_NAN) {
                return (value < 1.0) ? Double.NaN : value - 1.0;
            } else {
                return (value == 1.0) ? 0.0 : Double.NaN;
            }
        }
        double result;
        if (value < ASINH_ACOSH_SQRT_ELISION_THRESHOLD) {
            // Around this range, FDLIBM uses
            // log(2*value-1/(value+sqrt(value*value-1))),
            // but it involves a additional division
            // so we don't use it.
            result = FastMath.log(value + FastMath.sqrt(value*value - 1.0));
        } else {
            // log(2*value) would overflow for value > Double.MAX_VALUE/2,
            // so we compute otherwise.
            result = LOG_2 + FastMath.log(value);
        }
        return result;
    }
    
    /**
     * Much more accurate than acosh(1+value),
     * for arguments (and results) close to zero.
     * 
     * acosh1p(-0.0) = -0.0, for homogeneity with
     * sqrt(-0.0) = -0.0, which looks about the same
     * near 0.
     * 
     * @param value A double value.
     * @return Hyperbolic arccosine of (1+value).
     */
    public static double acosh1p(double value) {
        if (!(value > 0.0)) {
            // NaN, or value <= 0.
            // If value is -0.0, returning -0.0.
            if (ANTI_JIT_OPTIM_CRASH_ON_NAN) {
                return (value < 0.0) ? Double.NaN : value;
            } else {
                return (value == 0.0) ? value : Double.NaN;
            }
        }
        double result;
        if (value < (ASINH_ACOSH_SQRT_ELISION_THRESHOLD-1)) {
            // acosh(1+x)
            // = log((1+x) + sqrt((1+x)^2 - 1))
            // = log(1 + x + sqrt(1 + 2*x + x^2 - 1))
            // = log1p(x + sqrt(2*x + x^2))
            // = log1p(x + sqrt(x * (2 + x))
            result = FastMath.log1p(value + FastMath.sqrt(value * (2 + value)));
        } else {
            result = LOG_2 + FastMath.log(1+value);
        }
        return result;
    }

    /**
     * Some properties of atanh(x) = log((1+x)/(1-x))/2:
     * 1) defined on ]-1,1[
     * 2) result in ]-Infinity,+Infinity[
     * 3) atanh(-1) = -Infinity (by continuity)
     * 4) atanh(1) = +Infinity (by continuity)
     * 5) atanh(epsilon) ~= epsilon
     * 6) lim(atanh(x),x->1) = +Infinity
     * 
     * @param value A double value.
     * @return Value hyperbolic arctangent.
     */
    public static double atanh(double value) {
        boolean negateResult;
        if (value < 0.0) {
            value = -value;
            negateResult = true;
        } else {
            negateResult = false;
        }
        double result;
        if (!(value < 1.0)) {
            // NaN, or value >= 1
            if (ANTI_JIT_OPTIM_CRASH_ON_NAN) {
                result = (value > 1.0) ? Double.NaN : Double.POSITIVE_INFINITY + value;
            } else {
                result = (value == 1.0) ? Double.POSITIVE_INFINITY : Double.NaN;
            }
        } else {
            // For value < 0.5, FDLIBM uses
            // 0.5 * log1p((value+value) + (value+value)*value/(1-value)),
            // instead, but this is good enough for us.
            // atanh(x)
            // = log((1+x)/(1-x))/2
            // = log((1-x+2x)/(1-x))/2
            // = log1p(2x/(1-x))/2
            result = 0.5 * FastMath.log1p((value+value)/(1.0-value));
        }
        return negateResult ? -result : result;
    }
    
    /*
     * exponentials
     */
    
    /**
     * @param value A double value.
     * @return e^value.
     */
    public static double exp(double value) {
        if (USE_JDK_MATH) {
            return Math.exp(value);
        }
        // exp(x) = exp([x])*exp(y)
        // with [x] the integer part of x, and y = x-[x]
        // ===>
        // We find an approximation of y, called z.
        // ===>
        // exp(x) = exp([x])*(exp(z)*exp(epsilon))
        // with epsilon = y - z
        // ===>
        // We have exp([x]) and exp(z) pre-computed in tables, we "just" have to compute exp(epsilon).
        //
        // We use the same indexing (cast to int) to compute x integer part and the
        // table index corresponding to z, to avoid two int casts.
        // Also, to optimize index multiplication and division, we use powers of two,
        // so that we can do it with bits shifts.
        
        if (value > EXP_OVERFLOW_LIMIT) {
            return Double.POSITIVE_INFINITY;
        } else if (!(value >= EXP_UNDERFLOW_LIMIT)) {
            return (value != value) ? Double.NaN : 0.0;
        }
        
        final int indexes = (int)(value*EXP_LO_INDEXING);
        
        final int valueInt;
        if (indexes >= 0) {
            valueInt = (indexes>>EXP_LO_INDEXING_DIV_SHIFT);
        } else {
            valueInt = -((-indexes)>>EXP_LO_INDEXING_DIV_SHIFT);
        }
        final double hiTerm = expHiTab[valueInt-(int)EXP_UNDERFLOW_LIMIT];
        
        final int zIndex = indexes - (valueInt<<EXP_LO_INDEXING_DIV_SHIFT);
        final double y = (value-valueInt);
        final double z = zIndex*(1.0/EXP_LO_INDEXING);
        final double eps = y-z;
        final double expZ = expLoPosTab[zIndex+EXP_LO_TAB_MID_INDEX];
        final double expEps = (1+eps*(1+eps*(1.0/2+eps*(1.0/6+eps*(1.0/24)))));
        final double loTerm = expZ * expEps;
        
        return hiTerm * loTerm;
    }

    /**
     * Quick exp, with a max relative error of about 2.94e-2 for |value| < 700.0 or so,
     * and no accuracy at all outside this range.
     * Derived from a note by Nicol N. Schraudolph, IDSIA, 1998.
     * 
     * @param value A double value.
     * @return e^value.
     */
    public static double expQuick(double value) {
        if (USE_JDK_MATH) {
            return Math.exp(value);
        }
        /*
         * Cast of double values, even in long range, into long, is slower than
         * from double to int for values in int range, and then from int to long.
         * For that reason, we only work with integer values in int range
         * (corresponding to the 32 first bits of the long, containing sign,
         * exponent, and highest significant bits of double's mantissa),
         * and cast twice.
         * 
         * Constants determined empirically, using a random-based metaheuristic.
         * Should be possible to find better ones.
         */
        return Double.longBitsToDouble(((long)(int)(1512775.3952 * value + 1.0726481222E9))<<32);
    }

    /**
     * Much more accurate than exp(value)-1,
     * for arguments (and results) close to zero.
     * 
     * @param value A double value.
     * @return e^value-1.
     */
    public static double expm1(double value) {
        if (USE_JDK_MATH) {
            return Math.expm1(value);
        }
        // If value is far from zero, we use exp(value)-1.
        //
        // If value is close to zero, we use the following formula:
        // exp(value)-1
        // = exp(valueApprox)*exp(epsilon)-1
        // = exp(valueApprox)*(exp(epsilon)-exp(-valueApprox))
        // = exp(valueApprox)*(1+epsilon+epsilon^2/2!+...-exp(-valueApprox))
        // = exp(valueApprox)*((1-exp(-valueApprox))+epsilon+epsilon^2/2!+...)
        // exp(valueApprox) and exp(-valueApprox) being stored in tables.

        if (Math.abs(value) < EXP_LO_DISTANCE_TO_ZERO) {
            // Taking int part instead of rounding, which takes too long.
            int i = (int)(value*EXP_LO_INDEXING);
            double delta = value-i*(1.0/EXP_LO_INDEXING);
            return expLoPosTab[i+EXP_LO_TAB_MID_INDEX]*(expLoNegTab[i+EXP_LO_TAB_MID_INDEX]+delta*(1+delta*(1.0/2+delta*(1.0/6+delta*(1.0/24+delta*(1.0/120))))));
        } else {
            return FastMath.exp(value)-1;
        }
    }
    
    /*
     * logarithms
     */

    /**
     * @param value A double value.
     * @return Value logarithm (base e).
     */
    public static double log(double value) {
        if (USE_JDK_MATH || (!USE_REDEFINED_LOG)) {
            return Math.log(value);
        }
        if (value > 0.0) {
            if (value == Double.POSITIVE_INFINITY) {
                return Double.POSITIVE_INFINITY;
            }

            // For normal values not close to 1.0, we use the following formula:
            // log(value)
            // = log(2^exponent*1.mantissa)
            // = log(2^exponent) + log(1.mantissa)
            // = exponent * log(2) + log(1.mantissa)
            // = exponent * log(2) + log(1.mantissaApprox) + log(1.mantissa/1.mantissaApprox)
            // = exponent * log(2) + log(1.mantissaApprox) + log(1+epsilon)
            // = exponent * log(2) + log(1.mantissaApprox) + epsilon-epsilon^2/2+epsilon^3/3-epsilon^4/4+...
            // with:
            // 1.mantissaApprox <= 1.mantissa,
            // log(1.mantissaApprox) in table,
            // epsilon = (1.mantissa/1.mantissaApprox)-1
            //
            // To avoid bad relative error for small results,
            // values close to 1.0 are treated aside, with the formula:
            // log(x) = z*(2+z^2*((2.0/3)+z^2*((2.0/5))+z^2*((2.0/7))+...)))
            // with z=(x-1)/(x+1)

            double h;
            if (value > 0.95) {
                if (value < 1.14) {
                    double z = (value-1.0)/(value+1.0);
                    double z2 = z*z;
                    return z*(2+z2*((2.0/3)+z2*((2.0/5)+z2*((2.0/7)+z2*((2.0/9)+z2*((2.0/11)))))));
                }
                h = 0.0;
            } else if (value < DOUBLE_MIN_NORMAL) {
                // Ensuring value is normal.
                value *= TWO_POW_52;
                // log(x*2^52)
                // = log(x)-ln(2^52)
                // = log(x)-52*ln(2)
                h = -52*LOG_2;
            } else {
                h = 0.0;
            }

            int valueBitsHi = (int)(Double.doubleToRawLongBits(value)>>32);
            int valueExp = (valueBitsHi>>20)-MAX_DOUBLE_EXPONENT;
            // Getting the first LOG_BITS bits of the mantissa.
            int xIndex = ((valueBitsHi<<12)>>>(32-LOG_BITS));

            // 1.mantissa/1.mantissaApprox - 1
            double z = (value * twoPowNormalOrSubnormal(-valueExp)) * logXInvTab[xIndex] - 1;

            z *= (1-z*((1.0/2)-z*((1.0/3))));

            return h + valueExp * LOG_2 + (logXLogTab[xIndex] + z);

        } else if (value == 0.0) {
            return Double.NEGATIVE_INFINITY;
        } else { // value < 0.0, or value is NaN
            return Double.NaN;
        }
    }

    /**
     * Quick log, with a max relative error of about 1.9e-3
     * for values in ]Double.MIN_NORMAL,+infinity[, and
     * worse accuracy outside this range.
     * 
     * @param value A double value, in ]0,+infinity[ (strictly positive and finite).
     * @return Value logarithm (base e).
     */
    public static double logQuick(double value) {
        if (USE_JDK_MATH) {
            return Math.log(value);
        }
        /*
         * Inverse of Schraudolph's method for exp, is very inaccurate near 1,
         * and not that fast (even using floats), especially with added if's
         * to deal with values near 1, so we don't use it, and use a simplified
         * version of our log's redefined algorithm.
         */

        // Simplified version of log's redefined algorithm:
        // log(value) ~= exponent * log(2) + log(1.mantissaApprox)

        double h;
        if (value > 0.87) {
            if (value < 1.16) {
                return 2.0 * (value-1.0)/(value+1.0);
            }
            h = 0.0;
        } else if (value < DOUBLE_MIN_NORMAL) {
            value *= TWO_POW_52;
            h = -52*LOG_2;
        } else {
            h = 0.0;
        }

        int valueBitsHi = (int)(Double.doubleToRawLongBits(value)>>32);
        int valueExp = (valueBitsHi>>20)-MAX_DOUBLE_EXPONENT;
        int xIndex = ((valueBitsHi<<12)>>>(32-LOG_BITS));

        return h + valueExp * LOG_2 + logXLogTab[xIndex];
    }

    /**
     * @param value A double value.
     * @return Value logarithm (base 10).
     */
    public static double log10(double value) {
        if (USE_JDK_MATH || (!USE_REDEFINED_LOG)) {
            return Math.log10(value);
        }
        // INV_LOG_10 is < 1, but there is no risk of log(double)
        // overflow (positive or negative) while the end result shouldn't,
        // since log(Double.MIN_VALUE) and log(Double.MAX_VALUE) have
        // magnitudes of just a few hundreds.
        return FastMath.log(value) * INV_LOG_10;
    }

    /**
     * Much more accurate than log(1+value),
     * for arguments (and results) close to zero.
     * 
     * @param value A double value.
     * @return Logarithm (base e) of (1+value).
     */
    public static double log1p(double value) {
        if (USE_JDK_MATH) {
            return Math.log1p(value);
        }
        if (false) {
            // This also works. Simpler but a bit slower.
            if (value == Double.POSITIVE_INFINITY) {
                return Double.POSITIVE_INFINITY;
            }
            double valuePlusOne = 1+value;
            if (valuePlusOne == 1.0) {
                return value;
            } else {
                return FastMath.log(valuePlusOne)*(value/(valuePlusOne-1.0));
            }
        }
        if (value > -1.0) {
            if (value == Double.POSITIVE_INFINITY) {
                return Double.POSITIVE_INFINITY;
            }

            // ln'(x) = 1/x
            // so
            // log(x+epsilon) ~= log(x) + epsilon/x
            // 
            // Let u be 1+value rounded:
            // 1+value = u+epsilon
            //
            // log(1+value)
            // = log(u+epsilon)
            // ~= log(u) + epsilon/value
            // We compute log(u) as done in log(double), and then add the corrective term.

            double valuePlusOne = 1.0+value;
            if (valuePlusOne == 1.0) {
                return value;
            } else if (Math.abs(value) < 0.15) {
                double z = value/(value+2.0);
                double z2 = z*z;
                return z*(2+z2*((2.0/3)+z2*((2.0/5)+z2*((2.0/7)+z2*((2.0/9)+z2*((2.0/11)))))));
            }

            int valuePlusOneBitsHi = (int)(Double.doubleToRawLongBits(valuePlusOne)>>32) & 0x7FFFFFFF;
            int valuePlusOneExp = (valuePlusOneBitsHi>>20)-MAX_DOUBLE_EXPONENT;
            // Getting the first LOG_BITS bits of the mantissa.
            int xIndex = ((valuePlusOneBitsHi<<12)>>>(32-LOG_BITS));

            // 1.mantissa/1.mantissaApprox - 1
            double z = (valuePlusOne * twoPowNormalOrSubnormal(-valuePlusOneExp)) * logXInvTab[xIndex] - 1;

            z *= (1-z*((1.0/2)-z*(1.0/3)));

            // Adding epsilon/valuePlusOne to z,
            // with
            // epsilon = value - (valuePlusOne-1)
            // (valuePlusOne + epsilon ~= 1+value (not rounded))

            return valuePlusOneExp * LOG_2 + logXLogTab[xIndex] + (z + (value - (valuePlusOne-1))/valuePlusOne);
        } else if (value == -1.0) {
            return Double.NEGATIVE_INFINITY;
        } else { // value < -1.0, or value is NaN
            return Double.NaN;
        }
    }

    /**
     * @param value An integer value in [1,Integer.MAX_VALUE].
     * @return The integer part of the logarithm, in base 2, of the specified value,
     *         i.e. a result in [0,30]
     * @throws IllegalArgumentException if the specified value is <= 0.
     */
    public static int log2(int value) {
        return NumbersUtils.log2(value);
    }

    /**
     * @param value An integer value in [1,Long.MAX_VALUE].
     * @return The integer part of the logarithm, in base 2, of the specified value,
     *         i.e. a result in [0,62]
     * @throws IllegalArgumentException if the specified value is <= 0.
     */
    public static int log2(long value) {
        return NumbersUtils.log2(value);
    }
    
    /*
     * powers
     */

    /**
     * 1e-13ish accuracy (or better) on whole double range.
     * 
     * @param value A double value.
     * @param power A power.
     * @return value^power.
     */
    public static double pow(double value, double power) {
        if (USE_JDK_MATH) {
            return Math.pow(value,power);
        }
        if (power == 0.0) {
            return 1.0;
        } else if (power == 1.0) {
            return value;
        }
        if (value <= 0.0) {
            // powerInfo: 0 if not integer, 1 if even integer, -1 if odd integer
            int powerInfo;
            if (Math.abs(power) >= (TWO_POW_52*2)) {
                // The binary digit just before comma is outside mantissa,
                // thus it is always 0: power is an even integer.
                powerInfo = 1;
            } else {
                // If power's magnitude permits, we cast into int instead of into long,
                // as it is faster.
                if (Math.abs(power) <= (double)Integer.MAX_VALUE) {
                    int powerAsInt = (int)power;
                    if (power == (double)powerAsInt) {
                        powerInfo = ((powerAsInt & 1) == 0) ? 1 : -1;
                    } else { // power is not an integer (and not NaN, due to test against Integer.MAX_VALUE)
                        powerInfo = 0;
                    }
                } else {
                    long powerAsLong = (long)power;
                    if (power == (double)powerAsLong) {
                        powerInfo = ((powerAsLong & 1) == 0) ? 1 : -1;
                    } else { // power is not an integer, or is NaN
                        if (power != power) {
                            return Double.NaN;
                        }
                        powerInfo = 0;
                    }
                }
            }

            if (value == 0.0) {
                if (power < 0.0) {
                    return (powerInfo < 0) ? 1/value : Double.POSITIVE_INFINITY;
                } else { // power > 0.0 (0 and NaN cases already treated)
                    return (powerInfo < 0) ? value : 0.0;
                }
            } else { // value < 0.0
                if (value == Double.NEGATIVE_INFINITY) {
                    if (powerInfo < 0) { // power odd integer
                        return (power < 0.0) ? -0.0 : Double.NEGATIVE_INFINITY;
                    } else { // power even integer, or not an integer
                        return (power < 0.0) ? 0.0 : Double.POSITIVE_INFINITY;
                    }
                } else {
                    return (powerInfo == 0) ? Double.NaN : powerInfo * FastMath.exp(power*FastMath.log(-value));
                }
            }
        } else { // value > 0.0, or value is NaN
            return FastMath.exp(power*FastMath.log(value));
        }
    }

    /**
     * Quick pow, with a max relative error of about 4e-3
     * for value >= Double.MIN_NORMAL and 1e-10 < |value^power| < 1e10,
     * of about 4e-2 for value >= Double.MIN_NORMAL and 1e-50 < |value^power| < 1e50,
     * and worse accuracy otherwise.
     * 
     * @param value A double value, in ]0,+infinity[ (strictly positive and finite).
     * @param power A double value.
     * @return value^power.
     */
    public static double powQuick(double value, double power) {
        if (USE_JDK_MATH) {
            return Math.pow(value,power);
        }
        return FastMath.exp(power*FastMath.logQuick(value));
    }

    /**
     * This treatment is somehow accurate for low values of |power|,
     * and for |power*getExponent(value)| < 1023 or so (to stay away
     * from double extreme magnitudes (large and small)).
     * 
     * @param value A double value.
     * @param power A power.
     * @return value^power.
     */
    public static double powFast(double value, int power) {
        if (USE_JDK_MATH) {
            return Math.pow(value,power);
        }
        if (power > 5) { // Most common case first.
            double oddRemains = 1.0;
            do {
                // Test if power is odd.
                if ((power & 1) != 0) {
                    oddRemains *= value;
                }
                value *= value;
                power >>= 1; // power = power / 2
            } while (power > 5);
            // Here, power is in [3,5]: faster to finish outside the loop.
            if (power == 3) {
                return oddRemains * value * value * value;
            } else {
                double v2 = value * value;
                if (power == 4) {
                    return oddRemains * v2 * v2;
                } else { // power == 5
                    return oddRemains * v2 * v2 * value;
                }
            }
        } else if (power >= 0) { // power in [0,5]
            if (power < 3) { // power in [0,2]
                if (power == 2) { // Most common case first.
                    return value * value;
                } else if (power != 0) { // faster than == 1
                    return value;
                } else { // power == 0
                    return 1.0;
                }
            } else { // power in [3,5]
                if (power == 3) {
                    return value * value * value;
                } else { // power in [4,5]
                    double v2 = value * value;
                    if (power == 4) {
                        return v2 * v2;
                    } else { // power == 5
                        return v2 * v2 * value;
                    }
                }
            }
        } else { // power < 0
            // Opposite of Integer.MIN_VALUE does not exist as int.
            if (power == Integer.MIN_VALUE) {
                // Integer.MAX_VALUE = -(power+1)
                return 1.0/(FastMath.powFast(value,Integer.MAX_VALUE) * value);
            } else {
                return 1.0/FastMath.powFast(value,-power);
            }
        }
    }

    /**
     * Returns the exact result, provided it's in double range.
     * 
     * @param power A power.
     * @return 2^power.
     */
    public static double twoPow(int power) {
        if (USE_TWO_POW_TAB) {
            if (power >= MIN_DOUBLE_EXPONENT) {
                if (power <= MAX_DOUBLE_EXPONENT) { // Normal or subnormal.
                    return twoPowTab[power-MIN_DOUBLE_EXPONENT];
                } else { // Overflow.
                    return Double.POSITIVE_INFINITY;
                }
            } else { // Underflow.
                return 0.0;
            }
        } else {
            return NumbersUtils.twoPow(power);
        }
    }

    /**
     * @param value An int value.
     * @return value*value.
     */
    public static int pow2(int value) {
        return value*value;
    }

    /**
     * @param value A long value.
     * @return value*value.
     */
    public static long pow2(long value) {
        return value*value;
    }

    /**
     * @param value A float value.
     * @return value*value.
     */
    public static float pow2(float value) {
        return value*value;
    }

    /**
     * @param value A double value.
     * @return value*value.
     */
    public static double pow2(double value) {
        return value*value;
    }

    /**
     * @param value An int value.
     * @return value*value*value.
     */
    public static int pow3(int value) {
        return value*value*value;
    }

    /**
     * @param value A long value.
     * @return value*value*value.
     */
    public static long pow3(long value) {
        return value*value*value;
    }

    /**
     * @param value A float value.
     * @return value*value*value.
     */
    public static float pow3(float value) {
        return value*value*value;
    }

    /**
     * @param value A double value.
     * @return value*value*value.
     */
    public static double pow3(double value) {
        return value*value*value;
    }

    /*
     * roots
     */
    
    /**
     * @param value A double value.
     * @return Value square root.
     */
    public static double sqrt(double value) {
        if (USE_JDK_MATH || (!USE_REDEFINED_SQRT)) {
            return Math.sqrt(value);
        }
        // See cbrt for comments, sqrt uses the same ideas.
        
        if (!(value > 0.0)) { // value <= 0.0, or value is NaN
            if (ANTI_JIT_OPTIM_CRASH_ON_NAN) {
                return (value < 0.0) ? Double.NaN : value;
            } else {
                return (value == 0.0) ? value : Double.NaN;
            }
        } else if (value == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }

        double h;
        if (value < DOUBLE_MIN_NORMAL) {
            value *= TWO_POW_52;
            h = 2*TWO_POW_N26;
        } else {
            h = 2.0;
        }

        int valueBitsHi = (int)(Double.doubleToRawLongBits(value)>>32);
        int valueExponentIndex = (valueBitsHi>>20)+(-MAX_DOUBLE_EXPONENT-MIN_DOUBLE_EXPONENT);
        int xIndex = ((valueBitsHi<<12)>>>(32-SQRT_LO_BITS));

        double result = sqrtXSqrtHiTab[valueExponentIndex] * sqrtXSqrtLoTab[xIndex];
        double slope = sqrtSlopeHiTab[valueExponentIndex] * sqrtSlopeLoTab[xIndex];
        value *= 0.25;

        result += (value - result * result) * slope;
        result += (value - result * result) * slope;
        return h*(result + (value - result * result) * slope);
    }

    /**
     * Quick sqrt, with with a max relative error of about 3.41e-2
     * for values in [Double.MIN_NORMAL,Double.MAX_VALUE], and worse
     * accuracy outside this range.
     * 
     * @param value A double value.
     * @return Value square root.
     */
    public static double sqrtQuick(double value) {
        if (USE_JDK_MATH) {
            return Math.sqrt(value);
        }
        final long bits = Double.doubleToRawLongBits(value);
        /*
         * Constant determined empirically, using a random-based metaheuristic.
         * Should be possible to find a better one.
         */
        return Double.longBitsToDouble((bits+4606859074900000000L)>>>1);
    }

    /**
     * Quick inverse of square root, with a max relative error of about 3.44e-2
     * for values in [Double.MIN_NORMAL,Double.MAX_VALUE], and worse accuracy
     * outside this range.
     * 
     * This implementation uses zero step of Newton's method.
     * Here are the max relative errors in [Double.MIN_NORMAL,Double.MAX_VALUE]
     * depending on number of steps, if you want to copy-paste this code
     * and use your own number:
     * n=0: about 3.44e-2
     * n=1: about 1.75e-3
     * n=2: about 4.6e-6
     * n=3: about 3.17e-11
     * n=4: about 3.92e-16
     * n=5: about 3.03e-16
     * 
     * @param value A double value.
     * @return Inverse of value square root.
     */
    public static double invSqrtQuick(double value) {
        if (USE_JDK_MATH) {
            return 1/Math.sqrt(value);
        }
        /*
         * http://en.wikipedia.org/wiki/Fast_inverse_square_root
         */
        if (false) {
            // With one Newton step (much slower than
            // 1/Math.sqrt(double) if not optimized).
            final double halfInitial = value * 0.5;
            long bits = Double.doubleToRawLongBits(value);
            // If n=0, 6910474759270000000L might be better (3.38e-2 max relative error).
            bits = 0x5FE6EB50C7B537A9L - (bits>>1);
            value = Double.longBitsToDouble(bits);
            value = value * (1.5 - halfInitial * value * value); // Newton step, can repeat.
            return value;
        } else {
            return Double.longBitsToDouble(0x5FE6EB50C7B537A9L - (Double.doubleToRawLongBits(value)>>1));
        }
    }

    /**
     * @param value A double value.
     * @return Value cubic root.
     */
    public static double cbrt(double value) {
        if (USE_JDK_MATH) {
            return Math.cbrt(value);
        }
        double h;
        if (value < 0.0) {
            if (value == Double.NEGATIVE_INFINITY) {
                return Double.NEGATIVE_INFINITY;
            }
            value = -value;
            // Making sure value is normal.
            if (value < DOUBLE_MIN_NORMAL) {
                value *= (TWO_POW_52*TWO_POW_26);
                // h = <result_sign> * <result_multiplicator_to_avoid_overflow> / <cbrt(value_multiplicator_to_avoid_subnormal)>
                h = -2*TWO_POW_N26;
            } else {
                h = -2.0;
            }
        } else {
            if (!(value < Double.POSITIVE_INFINITY)) { // value is +infinity, or value is NaN
                return value;
            }
            // Making sure value is normal.
            if (value < DOUBLE_MIN_NORMAL) {
                if (value == 0.0) {
                    // cbrt(0.0) = 0.0, cbrt(-0.0) = -0.0
                    return value;
                }
                value *= (TWO_POW_52*TWO_POW_26);
                h = 2*TWO_POW_N26;
            } else {
                h = 2.0;
            }
        }

        // Normal value is (2^<value exponent> * <a value in [1,2[>).
        // First member cubic root is computed, and multiplied with an approximation
        // of the cubic root of the second member, to end up with a good guess of
        // the result before using Newton's (or Archimedes's) method.
        // To compute the cubic root approximation, we use the formula "cbrt(value) = cbrt(x) * cbrt(value/x)",
        // choosing x as close to value as possible but inferior to it, so that cbrt(value/x) is close to 1
        // (we could iterate on this method, using value/x as new value for each iteration,
        // but finishing with Newton's method is faster).

        // Shift and cast into an int, which overall is faster than working with a long.
        int valueBitsHi = (int)(Double.doubleToRawLongBits(value)>>32);
        int valueExponentIndex = (valueBitsHi>>20)+(-MAX_DOUBLE_EXPONENT-MIN_DOUBLE_EXPONENT);
        // Getting the first CBRT_LO_BITS bits of the mantissa.
        int xIndex = ((valueBitsHi<<12)>>>(32-CBRT_LO_BITS));
        double result = cbrtXCbrtHiTab[valueExponentIndex] * cbrtXCbrtLoTab[xIndex];
        double slope = cbrtSlopeHiTab[valueExponentIndex] * cbrtSlopeLoTab[xIndex];

        // Lowering values to avoid overflows when using Newton's method
        // (we will then just have to return twice the result).
        // result^3 = value
        // (result/2)^3 = value/8
        value *= 0.125;
        // No need to divide result here, as division is factorized in result computation tables.
        // result *= 0.5;

        // Newton's method, looking for y = x^(1/p):
        // y(n) = y(n-1) + (x-y(n-1)^p) * slope(y(n-1))
        // y(n) = y(n-1) + (x-y(n-1)^p) * (1/p)*(x(n-1)^(1/p-1))
        // y(n) = y(n-1) + (x-y(n-1)^p) * (1/p)*(x(n-1)^((1-p)/p))
        // with x(n-1)=y(n-1)^p, i.e.:
        // y(n) = y(n-1) + (x-y(n-1)^p) * (1/p)*(y(n-1)^(1-p))
        //
        // For p=3:
        // y(n) = y(n-1) + (x-y(n-1)^3) * (1/(3*y(n-1)^2))

        // To save time, we don't recompute the slope between Newton's method steps,
        // as initial slope is good enough for a few iterations.
        //
        // NB: slope = 1/(3*trueResult*trueResult)
        //     As we have result = trueResult/2 (to avoid overflows), we have:
        //     slope = 4/(3*result*result)
        //           = (4/3)*resultInv*resultInv
        //     with newResultInv = 1/newResult
        //                       = 1/(oldResult+resultDelta)
        //                       = (oldResultInv)*1/(1+resultDelta/oldResult)
        //                       = (oldResultInv)*1/(1+resultDelta*oldResultInv)
        //                      ~= (oldResultInv)*(1-resultDelta*oldResultInv)
        //     ===> Successive slopes could be computed without division, if needed,
        //          by computing resultInv (instead of slope right away) and retrieving
        //          slopes from it.

        result += (value - result * result * result) * slope;
        result += (value - result * result * result) * slope;
        return h*(result + (value - result * result * result) * slope);
    }

    /**
     * Returns sqrt(x^2+y^2) without intermediate overflow or underflow.
     */
    public static double hypot(double x, double y) {
        if (USE_JDK_MATH) {
            return Math.hypot(x,y);
        }
        x = Math.abs(x);
        y = Math.abs(y);
        if (y < x) {
            double a = x;
            x = y;
            y = a;
        } else if (!(y >= x)) { // Testing if we have some NaN.
            if ((x == Double.POSITIVE_INFINITY) || (y == Double.POSITIVE_INFINITY)) {
                return Double.POSITIVE_INFINITY;
            } else {
                return Double.NaN;
            }
        }
        if (y-x == y) { // x too small to substract from y
            return y;
        } else {
            double factor;
            if (x > TWO_POW_450) { // 2^450 < x < y
                x *= TWO_POW_N750;
                y *= TWO_POW_N750;
                factor = TWO_POW_750;
            } else if (y < TWO_POW_N450) { // x < y < 2^-450
                x *= TWO_POW_750;
                y *= TWO_POW_750;
                factor = TWO_POW_N750;
            } else {
                factor = 1.0;
            }
            return factor * FastMath.sqrt(x*x+y*y);
        }
    }

    /*
     * absolute values
     */
    
    /**
     * @param value An int value.
     * @return The absolute value, except if value is Integer.MIN_VALUE, for which it returns Integer.MIN_VALUE.
     */
    public static int abs(int value) {
        if (USE_JDK_MATH) {
            return Math.abs(value);
        }
        return NumbersUtils.abs(value);
    }

    /**
     * @param value A long value.
     * @return The absolute value, except if value is Long.MIN_VALUE, for which it returns Long.MIN_VALUE.
     */
    public static long abs(long value) {
        if (USE_JDK_MATH) {
            return Math.abs(value);
        }
        return NumbersUtils.abs(value);
    }

    /*
     * close values
     */

    /**
     * @param value A long value.
     * @return The specified value as int.
     * @throws ArithmeticException if the specified value is not in [Integer.MIN_VALUE,Integer.MAX_VALUE] range.
     */
    public static int toIntExact(long value) {
        return NumbersUtils.asInt(value);
    }

    /**
     * @param value A long value.
     * @return The closest int value in [Integer.MIN_VALUE,Integer.MAX_VALUE] range.
     */
    public static int toInt(long value) {
        return NumbersUtils.toInt(value);
    }

    /**
     * @param value A float value.
     * @return Floor of value.
     */
    public static float floor(float value) {
        final int exponent = FastMath.getExponent(value);
        if (exponent < 0) {
            // abs(value) < 1.
            if (value < 0.0f) {
                return -1.0f;
            } else {
                // 0.0f, or -0.0f if value is -0.0f
                return 0.0f * value;
            }
        } else if (exponent < 23) {
            // A bit faster than using casts.
            final int bits = Float.floatToRawIntBits(value);
            final int anteCommaBits = bits & (0xFF800000>>exponent);
            if ((value < 0.0f) && (anteCommaBits != bits)) {
                return Float.intBitsToFloat(anteCommaBits) - 1.0f;
            } else {
                return Float.intBitsToFloat(anteCommaBits);
            }
        } else {
            // +-Infinity, NaN, or a mathematical integer.
            return value;
        }
    }

    /**
     * @param value A double value.
     * @return Floor of value.
     */
    public static double floor(double value) {
        if (USE_JDK_MATH) {
            return Math.floor(value);
        }
        if (ANTI_SLOW_CASTS) {
            double valueAbs = Math.abs(value);
            if (valueAbs <= (double)Integer.MAX_VALUE) {
                if (value > 0.0) {
                    return (double)(int)value;
                } else if (value < 0.0) {
                    double anteCommaDigits = (double)(int)value;
                    if (value != anteCommaDigits) {
                        return anteCommaDigits - 1.0;
                    } else {
                        return anteCommaDigits;
                    }
                } else { // value is +-0.0 (not NaN due to test against Integer.MAX_VALUE)
                    return value;
                }
            } else if (valueAbs < TWO_POW_52) {
                // We split the value in two:
                // high part, which is a mathematical integer,
                // and the rest, for which we can get rid of the
                // post comma digits by casting into an int.
                double highPart = ((int)(value * TWO_POW_N26)) * TWO_POW_26;
                if (value > 0.0) {
                    return highPart + (double)((int)(value - highPart));
                } else {
                    double anteCommaDigits = highPart + (double)((int)(value - highPart));
                    if (value != anteCommaDigits) {
                        return anteCommaDigits - 1.0;
                    } else {
                        return anteCommaDigits;
                    }
                }
            } else { // abs(value) >= 2^52, or value is NaN
                return value;
            }
        } else {
            final int exponent = FastMath.getExponent(value);
            if (exponent < 0) {
                // abs(value) < 1.
                if (value < 0.0) {
                    return -1.0;
                } else {
                    // 0.0, or -0.0 if value is -0.0
                    return 0.0 * value;
                }
            } else if (exponent < 52) {
                // A bit faster than working on bits.
                final long matIntPart = (long)value;
                final double matIntToValue = value-(double)matIntPart;
                if (matIntToValue >= 0.0) {
                    return (double)matIntPart;
                } else {
                    return (double)(matIntPart - 1);
                }
            } else {
                // +-Infinity, NaN, or a mathematical integer.
                return value;
            }
        }
    }

    /**
     * @param value A float value.
     * @return Ceiling of value.
     */
    public static float ceil(float value) {
        return -FastMath.floor(-value);
    }

    /**
     * @param value A double value.
     * @return Ceiling of value.
     */
    public static double ceil(double value) {
        if (USE_JDK_MATH) {
            return Math.ceil(value);
        }
        return -FastMath.floor(-value);
    }

    /**
     * Might not behave like Math.round(float), see bug 6430675 and
     * similar bug for odd values of ulp 1.
     * 
     * @param value A double value.
     * @return Value rounded to nearest int, choosing superior int in case two
     *         are equally close (i.e. rounding-up).
     */
    public static int round(float value) {
        final int exponent = FastMath.getExponent(value);
        if (exponent < 0) {
            // abs(value) < 1.
            if (value < -0.5) {
                return -1;
            } else if (value < 0.5) {
                return 0;
            } else {
                return 1;
            }
        } else if (exponent < 23) {
            // A bit faster than working on bits.
            final int matIntPart = (int)value;
            final float intToValue = value-(float)matIntPart;
            if (intToValue < -0.5f) {
                return matIntPart - 1;
            } else if (intToValue < 0.5f) {
                return matIntPart;
            } else {
                return matIntPart + 1;
            }
        } else {
            // +-Infinity, NaN, or a mathematical integer.
            if (false && ANTI_SLOW_CASTS) { // not worth it
                if (Math.abs(value) >= -(float)Integer.MIN_VALUE) {
                    // +-Infinity or a mathematical integer (mostly) out of int range.
                    return (value < 0.0) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
                }
                // NaN or a mathematical integer (mostly) in int range.
            }
            return (int)value;
        }
    }

    /**
     * Might not behave like Math.round(double), see bug 6430675 and
     * similar bug for odd values of ulp 1.
     * 
     * @param value A double value.
     * @return Value rounded to nearest long, choosing superior long in case two
     *         are equally close (i.e. rounding-up).
     */
    public static long round(double value) {
        if (ANTI_SLOW_CASTS) {
            double valueAbs = Math.abs(value);
            if (valueAbs <= (double)Integer.MAX_VALUE) {
                final int matIntPart = (int)value;
                final double intToValue = value-(double)matIntPart;
                if (intToValue < -0.5) {
                    return matIntPart - 1;
                } else if (intToValue < 0.5) {
                    return matIntPart;
                } else {
                    return matIntPart + 1;
                }
            } else if (valueAbs < TWO_POW_52) {
                final long matIntPart = (long)value;
                final double intToValue = value-(double)matIntPart;
                if (intToValue < -0.5) {
                    return matIntPart - 1;
                } else if (intToValue < 0.5) {
                    return matIntPart;
                } else {
                    return matIntPart + 1;
                }
            } else if (valueAbs >= -(double)Long.MIN_VALUE) {
                // +-Infinity or a mathematical integer (mostly) out of long range.
                return (value < 0.0) ? Long.MIN_VALUE : Long.MAX_VALUE;
            } else {
                // NaN or a mathematical integer (mostly) in long range.
                return (long)value;
            }
        } else {
            final int exponent = FastMath.getExponent(value);
            if (exponent < 0) {
                // abs(value) < 1.
                if (value < -0.5) {
                    return -1;
                } else if (value < 0.5) {
                    return 0;
                } else {
                    return 1;
                }
            } else if (exponent < 52) {
                final long matIntPart = (long)value;
                final double intToValue = value-(double)matIntPart;
                if (intToValue < -0.5) {
                    return matIntPart - 1;
                } else if (intToValue < 0.5) {
                    return matIntPart;
                } else {
                    return matIntPart + 1;
                }
            } else {
                // +-Infinity, NaN, or a mathematical integer.
                return (long)value;
            }
        }
    }

    /**
     * Faster than round(float).
     * 
     * @param value A float value.
     * @return Value rounded to nearest int, choosing even int in case two
     *         are equally close.
     */
    public static int roundEven(float value) {
        final int sign = FastMath.signFromBit(value);
        value = Math.abs(value);
        if (ANTI_SLOW_CASTS) {
            if (value < TWO_POW_23_F) {
                // Getting rid of post-comma bits.
                value = ((value + TWO_POW_23_F) - TWO_POW_23_F);
                return sign * (int)value;
            } else if (value < (float)Integer.MAX_VALUE) { // <= doesn't work because of float precision
                // value is in [-Integer.MAX_VALUE,Integer.MAX_VALUE]
                return sign * (int)value;
            }
        } else {
            if (value < TWO_POW_23_F) {
                // Getting rid of post-comma bits.
                value = ((value + TWO_POW_23_F) - TWO_POW_23_F);
            }
        }
        return (int)(sign * value);
    }

    /**
     * Faster than round(double).
     * 
     * @param value A double value.
     * @return Value rounded to nearest long, choosing even long in case two
     *         are equally close.
     */
    public static long roundEven(double value) {
        final int sign = (int)FastMath.signFromBit(value);
        value = Math.abs(value);
        if (value < TWO_POW_52) {
            // Getting rid of post-comma bits.
            value = ((value + TWO_POW_52) - TWO_POW_52);
        }
        if (ANTI_SLOW_CASTS) {
            if (value <= (double)Integer.MAX_VALUE) {
                // value is in [-Integer.MAX_VALUE,Integer.MAX_VALUE]
                return sign * (int)value;
            }
        }
        return (long)(sign * value);
    }

    /**
     * @param value A float value.
     * @return The float mathematical integer closest to the specified value,
     *         choosing even one if two are equally close, or respectively
     *         NaN, +-Infinity or +-0.0f if the value is any of these.
     */
    public static float rint(float value) {
        final int sign = FastMath.signFromBit(value);
        value = Math.abs(value);
        if (value < TWO_POW_23_F) {
            // Getting rid of post-comma bits.
            value = ((TWO_POW_23_F + value ) - TWO_POW_23_F);
        }
        // Restoring original sign.
        return sign * value;
    }

    /**
     * @param value A double value.
     * @return The double mathematical integer closest to the specified value,
     *         choosing even one if two are equally close, or respectively
     *         NaN, +-Infinity or +-0.0 if the value is any of these.
     */
    public static double rint(double value) {
        if (USE_JDK_MATH) {
            return Math.rint(value);
        }
        final int sign = (int)FastMath.signFromBit(value);
        value = Math.abs(value);
        if (value < TWO_POW_52) {
            // Getting rid of post-comma bits.
            value = ((TWO_POW_52 + value ) - TWO_POW_52);
        }
        // Restoring original sign.
        return sign * value;
    }
    
    /*
     * ranges
     */
    
    /**
     * @param min An int value.
     * @param max An int value.
     * @param value An int value.
     * @return minValue if value < minValue, maxValue if value > maxValue, value otherwise.
     */
    public static int toRange(int min, int max, int value) {
        return NumbersUtils.toRange(min, max, value);
    }

    /**
     * @param min A long value.
     * @param max A long value.
     * @param value A long value.
     * @return min if value < min, max if value > max, value otherwise.
     */
    public static long toRange(long min, long max, long value) {
        return NumbersUtils.toRange(min, max, value);
    }

    /**
     * @param min A float value.
     * @param max A float value.
     * @param value A float value.
     * @return min if value < min, max if value > max, value otherwise.
     */
    public static float toRange(float min, float max, float value) {
        return NumbersUtils.toRange(min, max, value);
    }

    /**
     * @param min A double value.
     * @param max A double value.
     * @param value A double value.
     * @return min if value < min, max if value > max, value otherwise.
     */
    public static double toRange(double min, double max, double value) {
        return NumbersUtils.toRange(min, max, value);
    }

    /*
     * binary operators (+,-,*)
     */

    /**
     * @param a An int value.
     * @param b An int value.
     * @return The mathematical result of a+b.
     * @throws ArithmeticException if the mathematical result of a+b is not in [Integer.MIN_VALUE,Integer.MAX_VALUE] range.
     */
    public static int addExact(int a, int b) {
        return NumbersUtils.plusExact(a, b);
    }

    /**
     * @param a A long value.
     * @param b A long value.
     * @return The mathematical result of a+b.
     * @throws ArithmeticException if the mathematical result of a+b is not in [Long.MIN_VALUE,Long.MAX_VALUE] range.
     */
    public static long addExact(long a, long b) {
        return NumbersUtils.plusExact(a, b);
    }

    /**
     * @param a An int value.
     * @param b An int value.
     * @return The int value of [Integer.MIN_VALUE,Integer.MAX_VALUE] range which is the closest to mathematical result of a+b.
     */
    public static int addBounded(int a, int b) {
        return NumbersUtils.plusBounded(a, b);
    }

    /**
     * @param a A long value.
     * @param b A long value.
     * @return The long value of [Long.MIN_VALUE,Long.MAX_VALUE] range which is the closest to mathematical result of a+b.
     */
    public static long addBounded(long a, long b) {
        return NumbersUtils.plusBounded(a, b);
    }

    /**
     * @param a An int value.
     * @param b An int value.
     * @return The mathematical result of a-b.
     * @throws ArithmeticException if the mathematical result of a-b is not in [Integer.MIN_VALUE,Integer.MAX_VALUE] range.
     */
    public static int subtractExact(int a, int b) {
        return NumbersUtils.minusExact(a, b);
    }

    /**
     * @param a A long value.
     * @param b A long value.
     * @return The mathematical result of a-b.
     * @throws ArithmeticException if the mathematical result of a-b is not in [Long.MIN_VALUE,Long.MAX_VALUE] range.
     */
    public static long subtractExact(long a, long b) {
        return NumbersUtils.minusExact(a, b);
    }

    /**
     * @param a An int value.
     * @param b An int value.
     * @return The int value of [Integer.MIN_VALUE,Integer.MAX_VALUE] range which is the closest to mathematical result of a-b.
     */
    public static int subtractBounded(int a, int b) {
        return NumbersUtils.minusBounded(a, b);
    }

    /**
     * @param a A long value.
     * @param b A long value.
     * @return The long value of [Long.MIN_VALUE,Long.MAX_VALUE] range which is the closest to mathematical result of a-b.
     */
    public static long subtractBounded(long a, long b) {
        return NumbersUtils.minusBounded(a, b);
    }

    /**
     * @param a An int value.
     * @param b An int value.
     * @return The mathematical result of a*b.
     * @throws ArithmeticException if the mathematical result of a*b is not in [Integer.MIN_VALUE,Integer.MAX_VALUE] range.
     */
    public static int multiplyExact(int a, int b) {
        return NumbersUtils.timesExact(a, b);
    }

    /**
     * @param a A long value.
     * @param b A long value.
     * @return The mathematical result of a*b.
     * @throws ArithmeticException if the mathematical result of a*b is not in [Long.MIN_VALUE,Long.MAX_VALUE] range.
     */
    public static long multiplyExact(long a, long b) {
        return NumbersUtils.timesExact(a, b);
    }

    /**
     * @param a An int value.
     * @param b An int value.
     * @return The int value of [Integer.MIN_VALUE,Integer.MAX_VALUE] range which is the closest to mathematical result of a*b.
     */
    public static int multiplyBounded(int a, int b) {
        return NumbersUtils.timesBounded(a, b);
    }

    /**
     * @param a A long value.
     * @param b A long value.
     * @return The long value of [Long.MIN_VALUE,Long.MAX_VALUE] range which is the closest to mathematical result of a*b.
     */
    public static long multiplyBounded(long a, long b) {
        return NumbersUtils.timesBounded(a, b);
    }

    /*
     * binary operators (/,%)
     */
    
    /**
     * Returns the largest int <= dividend/divisor.
     * 
     * Unlike "/" operator, which rounds towards 0, this division
     * rounds towards -Infinity (which give different result
     * when the exact result is negative).
     * 
     * @param x The dividend.
     * @param y The divisor.
     * @return The largest int <= dividend/divisor, unless dividend is
     *         Integer.MIN_VALUE and divisor is -1, in which case
     *         Integer.MIN_VALUE is returned.
     * @throws ArithmeticException if the divisor is zero.
     */
    public static int floorDiv(int x, int y) {
        int r = x / y;
        // If the signs are different and modulo not zero, rounding down.
        if (((x ^ y) < 0) && ((r * y) != x)) {
            r--;
        }
        return r;
    }

    /**
     * Returns the largest long <= dividend/divisor.
     * 
     * Unlike "/" operator, which rounds towards 0, this division
     * rounds towards -Infinity (which give different result
     * when the exact result is negative).
     * 
     * @param x The dividend.
     * @param y The divisor.
     * @return The largest long <= dividend/divisor, unless dividend is
     *         Long.MIN_VALUE and divisor is -1, in which case
     *         Long.MIN_VALUE is returned.
     * @throws ArithmeticException if the divisor is zero.
     */
    public static long floorDiv(long x, long y) {
        long r = x / y;
        // If the signs are different and modulo not zero, rounding down.
        if (((x ^ y) < 0) && ((r * y) != x)) {
            r--;
        }
        return r;
    }

    /**
     * Returns the floor modulus, which is "x - floorDiv(x,y) * y",
     * has the same sign as y, and is in ]-abs(y),abs(y)[.
     *
     * The relationship between floorMod and floorDiv is the same
     * than between "%" and "/".
     * 
     * @param x The dividend.
     * @param y The divisor.
     * @return The floor modulus, i.e. "x - (floorDiv(x, y) * y)".
     * @throws ArithmeticException if the divisor is zero.
     */
    public static int floorMod(int x, int y) {
        return x - floorDiv(x, y) * y;
    }

    /**
     * Returns the floor modulus, which is "x - floorDiv(x,y) * y",
     * has the same sign as y, and is in ]-abs(y),abs(y)[.
     *
     * The relationship between floorMod and floorDiv is the same
     * than between "%" and "/".
     * 
     * @param x The dividend.
     * @param y The divisor.
     * @return The floor modulus, i.e. "x - (floorDiv(x, y) * y)".
     * @throws ArithmeticException if the divisor is zero.
     */
    public static long floorMod(long x, long y) {
        return x - floorDiv(x, y) * y;
    }
    
    /**
     * Returns dividend - divisor * n, where n is the mathematical integer
     * closest to dividend/divisor.
     * If dividend/divisor is equally close to surrounding integers,
     * we choose n to be the integer of smallest magnitude, which makes
     * this treatment differ from Math.IEEEremainder(double,double),
     * where n is chosen to be the even integer.
     * Note that the choice of n is not done considering the double
     * approximation of dividend/divisor, because it could cause
     * result to be outside [-|divisor|/2,|divisor|/2] range.
     * The practical effect is that if multiple results would be possible,
     * we always choose the result that is the closest to (and has the same
     * sign as) the dividend.
     * Ex. :
     * - for (-3.0,2.0), this method returns -1.0,
     *   whereas Math.IEEEremainder returns 1.0.
     * - for (-5.0,2.0), both this method and Math.IEEEremainder return -1.0.
     * 
     * If the remainder is zero, its sign is the same as the sign of the first argument.
     * If either argument is NaN, or the first argument is infinite,
     * or the second argument is positive zero or negative zero,
     * then the result is NaN.
     * If the first argument is finite and the second argument is
     * infinite, then the result is the same as the first argument.
     * 
     * NB:
     * - Modulo operator (%) returns a value in ]-|divisor|,|divisor|[,
     *   which sign is the same as dividend.
     * - As for modulo operator, the sign of the divisor has no effect on the result.
     * - On some architecture, % operator has been oserved to return NaN
     *   for some subnormal values of divisor, when dividend exponent is 1023,
     *   which impacts the correctness of this method.
     * 
     * @param dividend Dividend.
     * @param divisor Divisor.
     * @return Remainder of dividend/divisor, i.e. a value in [-|divisor|/2,|divisor|/2].
     */
    public static double remainder(double dividend, double divisor) {
        if (Double.isInfinite(divisor)) {
            if (Double.isInfinite(dividend)) {
                return Double.NaN;
            } else {
                return dividend;
            }
        }
        double value = dividend % divisor;
        if (Math.abs(value+value) > Math.abs(divisor)) {
            return value + ((value > 0.0) ? -Math.abs(divisor) : Math.abs(divisor));
        } else {
            return value;
        }
    }

    /**
     * @param angle Angle in radians.
     * @return The same angle, in radians, but in [-Math.PI,Math.PI].
     */
    public static double normalizeMinusPiPi(double angle) {
        // Not modifying values in output range.
        if ((angle >= -Math.PI) && (angle <= Math.PI)) {
            return angle;
        }
        double angleMinusPiPiOrSo = remainderTwoPi(angle);
        if (angleMinusPiPiOrSo < -Math.PI) {
            return -Math.PI;
        } else if (angleMinusPiPiOrSo > Math.PI) {
            return Math.PI;
        } else {
            return angleMinusPiPiOrSo;
        }
    }

    /**
     * Not accurate for large values.
     * 
     * @param angle Angle in radians.
     * @return The same angle, in radians, but in [-Math.PI,Math.PI].
     */
    public static double normalizeMinusPiPiFast(double angle) {
        // Not modifying values in output range.
        if ((angle >= -Math.PI) && (angle <= Math.PI)) {
            return angle;
        }
        double angleMinusPiPiOrSo = remainderTwoPiFast(angle);
        if (angleMinusPiPiOrSo < -Math.PI) {
            return -Math.PI;
        } else if (angleMinusPiPiOrSo > Math.PI) {
            return Math.PI;
        } else {
            return angleMinusPiPiOrSo;
        }
    }

    /**
     * @param angle Angle in radians.
     * @return The same angle, in radians, but in [0,2*Math.PI].
     */
    public static double normalizeZeroTwoPi(double angle) {
        // Not modifying values in output range.
        if ((angle >= 0.0) && (angle <= 2*Math.PI)) {
            return angle;
        }
        double angleMinusPiPiOrSo = remainderTwoPi(angle);
        if (angleMinusPiPiOrSo < 0.0) {
            // Not a problem if angle is slightly < -Math.PI,
            // since result ends up around PI, which is not near output range borders.
            return angleMinusPiPiOrSo + 2*Math.PI;
        } else {
            // Not a problem if angle is slightly > Math.PI,
            // since result ends up around PI, which is not near output range borders.
            return angleMinusPiPiOrSo;
        }
    }

    /**
     * Not accurate for large values.
     * 
     * @param angle Angle in radians.
     * @return The same angle, in radians, but in [0,2*Math.PI].
     */
    public static double normalizeZeroTwoPiFast(double angle) {
        // Not modifying values in output range.
        if ((angle >= 0.0) && (angle <= 2*Math.PI)) {
            return angle;
        }
        double angleMinusPiPiOrSo = remainderTwoPiFast(angle);
        if (angleMinusPiPiOrSo < 0.0) {
            // Not a problem if angle is slightly < -Math.PI,
            // since result ends up around PI, which is not near output range borders.
            return angleMinusPiPiOrSo + 2*Math.PI;
        } else {
            // Not a problem if angle is slightly > Math.PI,
            // since result ends up around PI, which is not near output range borders.
            return angleMinusPiPiOrSo;
        }
    }

    /**
     * @param angle Angle in radians.
     * @return Angle value modulo PI, in radians, in [-Math.PI/2,Math.PI/2].
     */
    public static double normalizeMinusHalfPiHalfPi(double angle) {
        // Not modifying values in output range.
        if ((angle >= -Math.PI/2) && (angle <= Math.PI/2)) {
            return angle;
        }
        double angleMinusPiPiOrSo = remainderTwoPi(angle);
        if (angleMinusPiPiOrSo < -Math.PI/2) {
            // Not a problem if angle is slightly < -Math.PI,
            // since result ends up around zero, which is not near output range borders.
            return angleMinusPiPiOrSo + Math.PI;
        } else if (angleMinusPiPiOrSo > Math.PI/2) {
            // Not a problem if angle is slightly > Math.PI,
            // since result ends up around zero, which is not near output range borders.
            return angleMinusPiPiOrSo - Math.PI;
        } else {
            return angleMinusPiPiOrSo;
        }
    }

    /**
     * Not accurate for large values.
     * 
     * @param angle Angle in radians.
     * @return Angle value modulo PI, in radians, in [-Math.PI/2,Math.PI/2].
     */
    public static double normalizeMinusHalfPiHalfPiFast(double angle) {
        // Not modifying values in output range.
        if ((angle >= -Math.PI/2) && (angle <= Math.PI/2)) {
            return angle;
        }
        double angleMinusPiPiOrSo = remainderTwoPiFast(angle);
        if (angleMinusPiPiOrSo < -Math.PI/2) {
            // Not a problem if angle is slightly < -Math.PI,
            // since result ends up around zero, which is not near output range borders.
            return angleMinusPiPiOrSo + Math.PI;
        } else if (angleMinusPiPiOrSo > Math.PI/2) {
            // Not a problem if angle is slightly > Math.PI,
            // since result ends up around zero, which is not near output range borders.
            return angleMinusPiPiOrSo - Math.PI;
        } else {
            return angleMinusPiPiOrSo;
        }
    }
    
    /*
     * floating points utils
     */
    
    /**
     * @param value A float value.
     * @return True if the specified value is NaN or +-Infinity, false otherwise.
     */
    public static boolean isNaNOrInfinite(float value) {
        return NumbersUtils.isNaNOrInfinite(value);
    }

    /**
     * @param value A double value.
     * @return True if the specified value is NaN or +-Infinity, false otherwise.
     */
    public static boolean isNaNOrInfinite(double value) {
        return NumbersUtils.isNaNOrInfinite(value);
    }

    /**
     * @param value A float value.
     * @return Value unbiased exponent.
     */
    public static int getExponent(float value) {
        return ((Float.floatToRawIntBits(value)>>23)&0xFF)-MAX_FLOAT_EXPONENT;
    }

    /**
     * @param value A double value.
     * @return Value unbiased exponent.
     */
    public static int getExponent(double value) {
        return (((int)(Double.doubleToRawLongBits(value)>>52))&0x7FF)-MAX_DOUBLE_EXPONENT;
    }

    /**
     * @param value A float value.
     * @return -1.0f if the specified value is < 0, 1.0f if it is > 0,
     *         and the value itself if it is NaN or +-0.0f.
     */
    public static float signum(float value) {
        if (USE_JDK_MATH) {
            return Math.signum(value);
        }
        if ((value == 0.0f) || (value != value)) {
            return value;
        }
        return (float)signFromBit(value);
    }
    
    /**
     * @param value A double value.
     * @return -1.0 if the specified value is < 0, 1.0 if it is > 0,
     *         and the value itself if it is NaN or +-0.0.
     */
    public static double signum(double value) {
        if (USE_JDK_MATH) {
            return Math.signum(value);
        }
        if ((value == 0.0) || (value != value)) {
            return value;
        }
        if (ANTI_SLOW_CASTS) {
            return (double)(int)signFromBit(value);
        } else {
            return (double)signFromBit(value);
        }
    }
    
    /**
     * @param value A float value.
     * @return -1 if sign bit if 1, 1 if sign bit if 0.
     */
    public static int signFromBit(float value) {
        return ((Float.floatToRawIntBits(value)>>30)|1);
    }

    /**
     * @param value A double value.
     * @return -1 if sign bit if 1, 1 if sign bit if 0.
     */
    public static long signFromBit(double value) {
        // Returning a long, to avoid useless cast into int.
        return ((Double.doubleToRawLongBits(value)>>62)|1);
    }

    /**
     * A sign of NaN can be interpreted as positive or negative.
     *
     * @param magnitude A float value.
     * @param sign A float value.
     * @return A value with the magnitude of the first argument, and the sign
     *         of the second argument.
     */
    public static float copySign(float magnitude, float sign) {
        return Float.intBitsToFloat(
                (Float.floatToRawIntBits(sign) & Integer.MIN_VALUE)
                | (Float.floatToRawIntBits(magnitude) & Integer.MAX_VALUE));
    }
    
    /**
     * A sign of NaN can be interpreted as positive or negative.
     *
     * @param magnitude A double value.
     * @param sign A double value.
     * @return A value with the magnitude of the first argument, and the sign
     *         of the second argument.
     */
    public static double copySign(double magnitude, double sign) {
        return Double.longBitsToDouble(
                (Double.doubleToRawLongBits(sign) & Long.MIN_VALUE)
                | (Double.doubleToRawLongBits(magnitude) & Long.MAX_VALUE));
    }

    /**
     * The ULP (Unit in the Last Place) is the distance to the next value larger
     * in magnitude.
     *
     * @param value A float value.
     * @return The size of an ulp of the specified value, or Float.MIN_VALUE
     *         if it is +-0.0f, or +Infinity if it is +-Infinity, or NaN
     *         if it is NaN.
     */
    public static float ulp(float value) {
        if (USE_JDK_MATH) {
            return Math.ulp(value);
        }
        /*
         * Look-up table not really worth it in micro-benchmark,
         * so should be worse with cache-misses.
         */
        final int exponent = FastMath.getExponent(value);
        if (exponent >= (MIN_FLOAT_NORMAL_EXPONENT+23)) {
            if (exponent == MAX_FLOAT_EXPONENT+1) {
                // NaN or +-Infinity
                return Math.abs(value);
            }
            // normal: returning 2^(exponent-23)
            return Float.intBitsToFloat((exponent+(MAX_FLOAT_EXPONENT-23))<<23);
        } else {
            if (exponent == MIN_FLOAT_NORMAL_EXPONENT-1) {
                // +-0.0f or subnormal
                return Float.MIN_VALUE;
            }
            // subnormal result
            return Float.intBitsToFloat(1<<(exponent-MIN_FLOAT_NORMAL_EXPONENT));
        }
    }

    /**
     * The ULP (Unit in the Last Place) is the distance to the next value larger
     * in magnitude.
     *
     * @param value A double value.
     * @return The size of an ulp of the specified value, or Double.MIN_VALUE
     *         if it is +-0.0, or +Infinity if it is +-Infinity, or NaN
     *         if it is NaN.
     */
    public static double ulp(double value) {
        if (USE_JDK_MATH) {
            return Math.ulp(value);
        }
        /*
         * Look-up table not really worth it in micro-benchmark,
         * so should be worse with cache-misses.
         */
        final int exponent = FastMath.getExponent(value);
        if (exponent >= (MIN_DOUBLE_NORMAL_EXPONENT+52)) {
            if (exponent == MAX_DOUBLE_EXPONENT+1) {
                // NaN or +-Infinity
                return Math.abs(value);
            }
            // normal: returning 2^(exponent-52)
            return Double.longBitsToDouble((exponent+(MAX_DOUBLE_EXPONENT-52L))<<52);
        } else {
            if (exponent == MIN_DOUBLE_NORMAL_EXPONENT-1) {
                // +-0.0f or subnormal
                return Double.MIN_VALUE;
            }
            // subnormal result
            return Double.longBitsToDouble(1L<<(exponent-MIN_DOUBLE_NORMAL_EXPONENT));
        }
    }
    
    /**
     * If both arguments are +-0.0(f), (float)direction is returned.
     * 
     * If both arguments are +Infinity or -Infinity,
     * respectively +Infinity or -Infinity is returned.
     *
     * @param start A float value.
     * @param direction A double value.
     * @return The float adjacent to start towards direction, considering that
     *         +(-)Float.MIN_VALUE is adjacent to +(-)0.0f, and that
     *         +(-)Float.MAX_VALUE is adjacent to +(-)Infinity,
     *         or NaN if any argument is NaN.
     */
    public static float nextAfter(float start, double direction) {
        if (direction > start) {
            // Going towards +Infinity.
            // +0.0f to get rid of eventual -0.0f
            final int bits = Float.floatToRawIntBits(start + 0.0f);
            return Float.intBitsToFloat(bits + (bits >= 0 ? 1 : -1));
        } else if (direction < start) {
            // Going towards -Infinity.
            if (start == 0.0f) {
                // +-0.0f
                return -Float.MIN_VALUE;
            }
            final int bits = Float.floatToRawIntBits(start);
            return Float.intBitsToFloat(bits + ((bits > 0) ? -1 : 1));
        } else if (start == direction) {
            return (float)direction;
        } else {
            // Returning a NaN derived from the input NaN(s).
            return start + (float)direction;
        }
    }
    
    /**
     * If both arguments are +-0.0, direction is returned.
     * 
     * If both arguments are +Infinity or -Infinity,
     * respectively +Infinity or -Infinity is returned.
     *
     * @param start A double value.
     * @param direction A double value.
     * @return The double adjacent to start towards direction, considering that
     *         +(-)Double.MIN_VALUE is adjacent to +(-)0.0, and that
     *         +(-)Double.MAX_VALUE is adjacent to +(-)Infinity,
     *         or NaN if any argument is NaN.
     */
    public static double nextAfter(double start, double direction) {
        if (direction > start) {
            // Going towards +Infinity.
            // +0.0 to get rid of eventual -0.0
            final long bits = Double.doubleToRawLongBits(start + 0.0f);
            return Double.longBitsToDouble(bits + (bits >= 0 ? 1 : -1));
        } else if (direction < start) {
            // Going towards -Infinity.
            if (start == 0.0) {
                // +-0.0
                return -Double.MIN_VALUE;
            }
            final long bits = Double.doubleToRawLongBits(start);
            return Double.longBitsToDouble(bits + ((bits > 0) ? -1 : 1));
        } else if (start == direction) {
            return direction;
        } else {
            // Returning a NaN derived from the input NaN(s).
            return start + direction;
        }
    }
    
    /**
     * Semantically equivalent to nextAfter(start,Double.NEGATIVE_INFINITY).
     */
    public static float nextDown(float start) {
        if (start > Float.NEGATIVE_INFINITY) {
            if (start == 0.0f) {
                // +-0.0f
                return -Float.MIN_VALUE;
            }
            final int bits = Float.floatToRawIntBits(start);
            return Float.intBitsToFloat(bits + ((bits > 0) ? -1 : 1));
        } else if (start == Float.NEGATIVE_INFINITY) {
            return Float.NEGATIVE_INFINITY;
        } else {
            // NaN
            return start;
        }
    }
    
    /**
     * Semantically equivalent to nextAfter(start,Double.NEGATIVE_INFINITY).
     */
    public static double nextDown(double start) {
        if (start > Double.NEGATIVE_INFINITY) {
            if (start == 0.0) {
                // +-0.0
                return -Double.MIN_VALUE;
            }
            final long bits = Double.doubleToRawLongBits(start);
            return Double.longBitsToDouble(bits + ((bits > 0) ? -1 : 1));
        } else if (start == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        } else {
            // NaN
            return start;
        }
    }
    
    /**
     * Semantically equivalent to nextAfter(start,Double.POSITIVE_INFINITY).
     */
    public static float nextUp(float start) {
        if (start < Float.POSITIVE_INFINITY) {
            // +0.0f to get rid of eventual -0.0f
            final int bits = Float.floatToRawIntBits(start + 0.0f);
            return Float.intBitsToFloat(bits + (bits >= 0 ? 1 : -1));
        } else if (start == Float.POSITIVE_INFINITY) {
            return Float.POSITIVE_INFINITY;
        } else {
            // NaN
            return start;
        }
    }
    
    /**
     * Semantically equivalent to nextAfter(start,Double.POSITIVE_INFINITY).
     */
    public static double nextUp(double start) {
        if (start < Double.POSITIVE_INFINITY) {
            // +0.0 to get rid of eventual -0.0
            final long bits = Double.doubleToRawLongBits(start + 0.0);
            return Double.longBitsToDouble(bits + (bits >= 0 ? 1 : -1));
        } else if (start == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        } else {
            // NaN
            return start;
        }
    }
    
    /**
     * Precision may be lost if the result is subnormal.
     *
     * @param value A float value.
     * @param scaleFactor An int value.
     * @return value * 2^scaleFactor, or a value equivalent to the specified
     *         one if it is NaN, +-Infinity or +-0.0f.
     */
    public static float scalb(float value, int scaleFactor) {
        // Large enough to imply overflow or underflow for
        // a finite non-zero value.
        final int MAX_SCALE = 2*MAX_FLOAT_EXPONENT+23+1;

        // Making sure scaling factor is in a reasonable range.
        scaleFactor = Math.max(Math.min(scaleFactor, MAX_SCALE), -MAX_SCALE);
        
        return (float)(((double)value) * twoPowNormal(scaleFactor));
    }
    
    /**
     * Precision may be lost if the result is subnormal.
     *
     * @param value A double value.
     * @param scaleFactor An int value.
     * @return value * 2^scaleFactor, or a value equivalent to the specified
     *         one if it is NaN, +-Infinity or +-0.0.
     */
    public static double scalb(double value, int scaleFactor) {
        // Large enough to imply overflow or underflow for
        // a finite non-zero value.
        final int MAX_SCALE = 2*MAX_DOUBLE_EXPONENT+52+1;

        // Making sure scaling factor is in a reasonable range.
        final int exponentAdjust;
        final int scaleIncrement;
        final double exponentDelta;
        if (scaleFactor < 0) {
            scaleFactor = Math.max(scaleFactor, -MAX_SCALE);
            scaleIncrement = -512;
            exponentDelta = TWO_POW_N512;
        } else {
            scaleFactor = Math.min(scaleFactor, MAX_SCALE);
            scaleIncrement = 512;
            exponentDelta = TWO_POW_512;
        }

        // Calculating (scaleFactor % +-512), 512 = 2^9, using
        // technique from "Hacker's Delight" section 10-2.
        final int t = (scaleFactor >> (9-1)) >>> (32-9);
        exponentAdjust = ((scaleFactor + t) & (512-1)) - t;

        value *= twoPowNormal(exponentAdjust);
        scaleFactor -= exponentAdjust;

        while (scaleFactor != 0) {
            value *= exponentDelta;
            scaleFactor -= scaleIncrement;
        }
        
        return value;
    }
    
    /*
     * non-redefined java.lang.Math public values and treatments
     */

    public static final double E = Math.E;
    public static final double PI = Math.PI;
    public static float abs(float a) {
        return Math.abs(a);
    }
    public static double abs(double a) {
        return Math.abs(a);
    }
    public static int min(int a, int b) {
        return Math.min(a,b);
    }
    public static long min(long a, long b) {
        return Math.min(a,b);
    }
    public static float min(float a, float b) {
        return Math.min(a,b);
    }
    public static double min(double a, double b) {
        return Math.min(a,b);
    }
    public static int max(int a, int b) {
        return Math.max(a,b);
    }
    public static long max(long a, long b) {
        return Math.max(a,b);
    }
    public static float max(float a, float b) {
        return Math.max(a,b);
    }
    public static double max(double a, double b) {
        return Math.max(a,b);
    }
    public static double IEEEremainder(double f1, double f2) {
        return Math.IEEEremainder(f1,f2);
    }
    public static double random() {
        return Math.random();
    }

    //--------------------------------------------------------------------------
    //  PRIVATE TREATMENTS
    //--------------------------------------------------------------------------

    /**
     * FastMath is non-instantiable.
     */
    private FastMath() {
    }

    private static boolean getBooleanProperty(
            final String key,
            boolean defaultValue) {
        final String tmp = System.getProperty(key);
        if (tmp != null) {
            return Boolean.parseBoolean(tmp);
        } else {
            return defaultValue;
        }
    }
    
    /**
     * Use look-up tables size power through this method,
     * to make sure is it small in case java.lang.Math
     * is directly used.
     */
    private static int getTabSizePower(int tabSizePower) {
        return USE_JDK_MATH ? Math.min(2, tabSizePower) : tabSizePower;
    }

    /**
     * @param power Must be in normal values range.
     */
    private static double twoPowNormal(int power) {
        if (USE_TWO_POW_TAB) {
            return twoPowTab[power-MIN_DOUBLE_EXPONENT];
        } else {
            return Double.longBitsToDouble(((long)(power+MAX_DOUBLE_EXPONENT))<<52);
        }
    }

    /**
     * @param power Must be in normal or subnormal values range.
     */
    private static double twoPowNormalOrSubnormal(int power) {
        if (USE_TWO_POW_TAB) {
            return twoPowTab[power-MIN_DOUBLE_EXPONENT];
        } else {
            if (power <= -MAX_DOUBLE_EXPONENT) { // Not normal.
                return Double.longBitsToDouble(0x0008000000000000L>>(-(power+MAX_DOUBLE_EXPONENT)));
            } else { // Normal.
                return Double.longBitsToDouble(((long)(power+MAX_DOUBLE_EXPONENT))<<52);
            }
        }
    }

    private static double atan2_pinf_yyy(double y) {
        if (y == Double.POSITIVE_INFINITY) {
            return Math.PI/4;
        } else if (y == Double.NEGATIVE_INFINITY) {
            return -Math.PI/4;
        } else if (y > 0.0) {
            return 0.0;
        } else if (y < 0.0) {
            return -0.0;
        } else {
            return Double.NaN;
        }
    }
    
    private static double atan2_ninf_yyy(double y) {
        if (y == Double.POSITIVE_INFINITY) {
            return 3*Math.PI/4;
        } else if (y == Double.NEGATIVE_INFINITY) {
            return -3*Math.PI/4;
        } else if (y > 0.0) {
            return Math.PI;
        } else if (y < 0.0) {
            return -Math.PI;
        } else {
            return Double.NaN;
        }
    }
    
    private static double atan2_zeroOrNaN_yyy(double x, double y) {
        if (x == 0.0) {
            if (y == 0.0) {
                if (FastMath.signFromBit(x) < 0) {
                    // x is -0.0
                    return FastMath.signFromBit(y) * Math.PI;
                } else {
                    // +-0.0
                    return y;
                }
            }
            if (y > 0.0) {
                return Math.PI/2;
            } else if (y < 0.0) {
                return -Math.PI/2;
            } else {
                return Double.NaN;
            }
        } else {
            return Double.NaN;
        }
    }

    /**
     * Remainder using an accurate definition of PI.
     * Derived from a fdlibm treatment called __ieee754_rem_pio2.
     * 
     * This method can return values slightly (like one ULP or so) outside [-Math.PI,Math.PI] range.
     * 
     * @param angle Angle in radians.
     * @return Remainder of (angle % (2*PI)), which is in [-PI,PI] range.
     */
    private static double remainderTwoPi(double angle) {
        if (USE_JDK_MATH) {
            double y = Math.sin(angle);
            double x = Math.cos(angle);
            return Math.atan2(y,x);
        }
        boolean negateResult;
        if (angle < 0.0) {
            negateResult = true;
            angle = -angle;
        } else {
            negateResult = false;
        }
        if (angle <= NORMALIZE_ANGLE_MAX_MEDIUM_DOUBLE) {
            double fn = (double)(int)(angle*INVTWOPI+0.5);
            double result = (angle - fn*TWOPI_HI) - fn*TWOPI_LO;
            return negateResult ? -result : result;
        } else if (angle < Double.POSITIVE_INFINITY) {
            // Reworking exponent to have a value < 2^24.
            long lx = Double.doubleToRawLongBits(angle);
            long exp = ((lx>>52)&0x7FF) - 1046;
            double z = Double.longBitsToDouble(lx - (exp<<52));

            double x0 = (double)((int)z);
            z = (z-x0)*TWO_POW_24;
            double x1 = (double)((int)z);
            double x2 = (z-x1)*TWO_POW_24;

            double result = subRemainderTwoPi(x0, x1, x2, (int)exp, (x2 == 0) ? 2 : 3);
            return negateResult ? -result : result;
        } else { // angle is +infinity or NaN
            return Double.NaN;
        }
    }

    /** 
     * Not accurate for large values.
     * 
     * This method can return values slightly (like one ULP or so) outside [-Math.PI,Math.PI] range.
     * 
     * @param angle Angle in radians.
     * @return Remainder of (angle % (2*PI)), which is in [-PI,PI] range.
     */
    private static double remainderTwoPiFast(double angle) {
        if (USE_JDK_MATH) {
            return remainderTwoPi(angle);
        }
        boolean negateResult;
        if (angle < 0.0) {
            negateResult = true;
            angle = -angle;
        } else {
            negateResult = false;
        }
        // - We don't bother with values higher than (2*PI*(2^52)),
        //   since they are spaced by 2*PI or more from each other.
        // - For large values, we don't use % because it might be very slow,
        //   and we split computation in two, because cast from double to int
        //   with large numbers might be very slow also.
        if (angle <= TWO_POW_26*(2*Math.PI)) {
            double fn = (double)(int)(angle*INVTWOPI+0.5);
            double result = (angle - fn*TWOPI_HI) - fn*TWOPI_LO;
            return negateResult ? -result : result;
        } else if (angle <= TWO_POW_52*(2*Math.PI)) {
            // 1) Computing remainder of angle modulo TWO_POW_26*(2*PI).
            double fn = (double)(int)(angle*(INVTWOPI/TWO_POW_26)+0.5);
            double result = (angle - fn*(TWOPI_HI*TWO_POW_26)) - fn*(TWOPI_LO*TWO_POW_26);
            // Here, result is in [-TWO_POW_26*Math.PI,TWO_POW_26*Math.PI].
            if (result < 0.0) {
                result = -result;
                negateResult = !negateResult;
            }
            // 2) Computing remainder of angle modulo 2*PI.
            fn = (double)(int)(result*INVTWOPI+0.5);
            result = (result - fn*TWOPI_HI) - fn*TWOPI_LO;
            return negateResult ? -result : result;
        } else if (angle < Double.POSITIVE_INFINITY) {
            return 0.0;
        } else { // angle is +infinity or NaN
            return Double.NaN;
        }
    }

    /**
     * Remainder using an accurate definition of PI.
     * Derived from a fdlibm treatment called __kernel_rem_pio2.
     * 
     * @param x0 Most significant part of the value, as an integer < 2^24, in double precision format. Must be >= 0.
     * @param x1 Following significant part of the value, as an integer < 2^24, in double precision format.
     * @param x2 Least significant part of the value, as an integer < 2^24, in double precision format.
     * @param e0 Exponent of x0 (value is (2^e0)*(x0+(2^-24)*(x1+(2^-24)*x2))). Must be >= -20.
     * @param nx Number of significant parts to take into account. Must be 2 or 3.
     * @return Remainder of (value % (2*PI)), which is in [-PI,PI] range.
     */
    private static double subRemainderTwoPi(double x0, double x1, double x2, int e0, int nx) {
        int ih;
        double z,fw;
        double f0,f1,f2,f3,f4,f5,f6 = 0.0,f7;
        double q0,q1,q2,q3,q4,q5;
        int iq0,iq1,iq2,iq3,iq4;

        final int jx = nx - 1; // jx in [1,2] (nx in [2,3])
        // Could use a table to avoid division, but the gain isn't worth it most likely...
        final int jv = (e0-3)/24; // We do not handle the case (e0-3 < -23).
        int q = e0-((jv<<4)+(jv<<3))-24; // e0-24*(jv+1)

        final int j = jv + 4;
        if (jx == 1) {
            f5 = (j >= 0) ? ONE_OVER_TWOPI_TAB[j]: 0.0;
            f4 = (j >= 1) ? ONE_OVER_TWOPI_TAB[j-1]: 0.0;
            f3 = (j >= 2) ? ONE_OVER_TWOPI_TAB[j-2]: 0.0;
            f2 = (j >= 3) ? ONE_OVER_TWOPI_TAB[j-3]: 0.0;
            f1 = (j >= 4) ? ONE_OVER_TWOPI_TAB[j-4]: 0.0;
            f0 = (j >= 5) ? ONE_OVER_TWOPI_TAB[j-5]: 0.0;

            q0 = x0*f1 + x1*f0;
            q1 = x0*f2 + x1*f1;
            q2 = x0*f3 + x1*f2;
            q3 = x0*f4 + x1*f3;
            q4 = x0*f5 + x1*f4;
        } else { // jx == 2
            f6 = (j >= 0) ? ONE_OVER_TWOPI_TAB[j]: 0.0;
            f5 = (j >= 1) ? ONE_OVER_TWOPI_TAB[j-1]: 0.0;
            f4 = (j >= 2) ? ONE_OVER_TWOPI_TAB[j-2]: 0.0;
            f3 = (j >= 3) ? ONE_OVER_TWOPI_TAB[j-3]: 0.0;
            f2 = (j >= 4) ? ONE_OVER_TWOPI_TAB[j-4]: 0.0;
            f1 = (j >= 5) ? ONE_OVER_TWOPI_TAB[j-5]: 0.0;
            f0 = (j >= 6) ? ONE_OVER_TWOPI_TAB[j-6]: 0.0;

            q0 = x0*f2 + x1*f1 + x2*f0;
            q1 = x0*f3 + x1*f2 + x2*f1;
            q2 = x0*f4 + x1*f3 + x2*f2;
            q3 = x0*f5 + x1*f4 + x2*f3;
            q4 = x0*f6 + x1*f5 + x2*f4;
        }

        z = q4;
        fw  = (double)((int)(TWO_POW_N24*z));
        iq0 = (int)(z-TWO_POW_24*fw);
        z   = q3+fw;
        fw  = (double)((int)(TWO_POW_N24*z));
        iq1 = (int)(z-TWO_POW_24*fw);
        z   = q2+fw;
        fw  = (double)((int)(TWO_POW_N24*z));
        iq2 = (int)(z-TWO_POW_24*fw);
        z   = q1+fw;
        fw  = (double)((int)(TWO_POW_N24*z));
        iq3 = (int)(z-TWO_POW_24*fw);
        z   = q0+fw;

        // Here, q is in [-25,2] range or so,
        // so in normal exponents range.
        double twoPowQ = twoPowNormal(q);

        z = (z*twoPowQ) % 8.0;
        z -= (double)((int)z);
        if (q > 0) {
            iq3 &= 0xFFFFFF>>q;
            ih = iq3>>(23-q);
        } else if (q == 0) {
            ih = iq3>>23;
        } else if (z >= 0.5) {
            ih = 2;
        } else {
            ih = 0;
        }
        if (ih > 0) {
            int carry;
            if (iq0 != 0) {
                carry = 1;
                iq0 = 0x1000000 - iq0;
                iq1 = 0x0FFFFFF - iq1;
                iq2 = 0x0FFFFFF - iq2;
                iq3 = 0x0FFFFFF - iq3;
            } else {
                if (iq1 != 0) {
                    carry = 1;
                    iq1 = 0x1000000 - iq1;
                    iq2 = 0x0FFFFFF - iq2;
                    iq3 = 0x0FFFFFF - iq3;
                } else {
                    if (iq2 != 0) {
                        carry = 1;
                        iq2 = 0x1000000 - iq2;
                        iq3 = 0x0FFFFFF - iq3;
                    } else {
                        if (iq3 != 0) {
                            carry = 1;
                            iq3 = 0x1000000 - iq3;
                        } else {
                            carry = 0;
                        }
                    }
                }
            }
            if (q > 0) {
                switch (q) {
                case 1:
                    iq3 &= 0x7FFFFF;
                    break;
                case 2:
                    iq3 &= 0x3FFFFF;
                    break;
                }
            }
            if (ih == 2) {
                z = 1.0 - z;
                if (carry != 0) {
                    z -= twoPowQ;
                }
            }
        }

        if (z == 0.0) {
            if (jx == 1) {
                f6 = ONE_OVER_TWOPI_TAB[jv+5];
                q5 = x0*f6 + x1*f5;
            } else { // jx == 2
                f7 = ONE_OVER_TWOPI_TAB[jv+5];
                q5 = x0*f7 + x1*f6 + x2*f5;
            }

            z = q5;
            fw  = (double)((int)(TWO_POW_N24*z));
            iq0 = (int)(z-TWO_POW_24*fw);
            z   = q4+fw;
            fw  = (double)((int)(TWO_POW_N24*z));
            iq1 = (int)(z-TWO_POW_24*fw);
            z   = q3+fw;
            fw  = (double)((int)(TWO_POW_N24*z));
            iq2 = (int)(z-TWO_POW_24*fw);
            z   = q2+fw;
            fw  = (double)((int)(TWO_POW_N24*z));
            iq3 = (int)(z-TWO_POW_24*fw);
            z   = q1+fw;
            fw  = (double)((int)(TWO_POW_N24*z));
            iq4 = (int)(z-TWO_POW_24*fw);
            z   = q0+fw;

            z = (z*twoPowQ) % 8.0;
            z -= (double)((int)z);
            if (q > 0) {
                // some parentheses for Eclipse formatter's weaknesses with bits shifts
                iq4 &= (0xFFFFFF>>q);
                ih = (iq4>>(23-q));
            } else if (q == 0) {
                ih = iq4>>23;
            } else if (z >= 0.5) {
                ih = 2;
            } else {
                ih = 0;
            }
            if (ih > 0) {
                if (iq0 != 0) {
                    iq0 = 0x1000000 - iq0;
                    iq1 = 0x0FFFFFF - iq1;
                    iq2 = 0x0FFFFFF - iq2;
                    iq3 = 0x0FFFFFF - iq3;
                    iq4 = 0x0FFFFFF - iq4;
                } else {
                    if (iq1 != 0) {
                        iq1 = 0x1000000 - iq1;
                        iq2 = 0x0FFFFFF - iq2;
                        iq3 = 0x0FFFFFF - iq3;
                        iq4 = 0x0FFFFFF - iq4;
                    } else {
                        if (iq2 != 0) {
                            iq2 = 0x1000000 - iq2;
                            iq3 = 0x0FFFFFF - iq3;
                            iq4 = 0x0FFFFFF - iq4;
                        } else {
                            if (iq3 != 0) {
                                iq3 = 0x1000000 - iq3;
                                iq4 = 0x0FFFFFF - iq4;
                            } else {
                                if (iq4 != 0) {
                                    iq4 = 0x1000000 - iq4;
                                }
                            }
                        }
                    }
                }
                if (q > 0) {
                    switch (q) {
                    case 1:
                        iq4 &= 0x7FFFFF;
                        break;
                    case 2:
                        iq4 &= 0x3FFFFF;
                        break;
                    }
                }
            }
            fw = twoPowQ * TWO_POW_N24; // q -= 24, so initializing fw with ((2^q)*(2^-24)=2^(q-24))
        } else {
            if (false) {
                // Here, q is in [-25,-2] range or so,
                // so -q is in normal exponents range,
                // and we could use that,
                // but tests show using division is faster.
                iq4 = (int)(z*twoPowNormal(-q));
            } else {
                iq4 = (int)(z/twoPowQ);
            }
            fw = twoPowQ;
        }

        q4 = fw*(double)iq4;
        fw *= TWO_POW_N24;
        q3 = fw*(double)iq3;
        fw *= TWO_POW_N24;
        q2 = fw*(double)iq2;
        fw *= TWO_POW_N24;
        q1 = fw*(double)iq1;
        fw *= TWO_POW_N24;
        q0 = fw*(double)iq0;
        fw *= TWO_POW_N24;

        fw = TWOPI_TAB0*q4;
        fw += TWOPI_TAB0*q3 + TWOPI_TAB1*q4;
        fw += TWOPI_TAB0*q2 + TWOPI_TAB1*q3 + TWOPI_TAB2*q4;
        fw += TWOPI_TAB0*q1 + TWOPI_TAB1*q2 + TWOPI_TAB2*q3 + TWOPI_TAB3*q4;
        fw += TWOPI_TAB0*q0 + TWOPI_TAB1*q1 + TWOPI_TAB2*q2 + TWOPI_TAB3*q3 + TWOPI_TAB4*q4;

        return (ih == 0) ? fw : -fw;
    }

    //--------------------------------------------------------------------------
    // STATIC INITIALIZATIONS
    //--------------------------------------------------------------------------

    static {
        init();
    }
    
    /**
     * Initializes look-up tables.
     * 
     * Using redefined pure Java treatments in this method, instead of Math
     * or StrictMath ones (even asin(double)), can make this class load much
     * slower, because class loading is likely not to be optimized.
     * 
     * Could use strictfp and StrictMath here, to always en up with the same tables,
     * but this class doesn't guarantee identical results across various architectures,
     * so to make it simple (less keywords/dependencies), we don't use strictfp,
     * and use Math - which could also help if used StrictMath methods are slow.
     */
    private static void init() {
        
        /*
         * sin and cos
         */

        final int SIN_COS_PI_INDEX = (SIN_COS_TABS_SIZE-1)/2;
        final int SIN_COS_PI_MUL_2_INDEX = 2*SIN_COS_PI_INDEX;
        final int SIN_COS_PI_MUL_0_5_INDEX = SIN_COS_PI_INDEX/2;
        final int SIN_COS_PI_MUL_1_5_INDEX = 3*SIN_COS_PI_INDEX/2;
        for (int i=0;i<SIN_COS_TABS_SIZE;i++) {
            // angle: in [0,2*PI].
            double angle = i * SIN_COS_DELTA_HI + i * SIN_COS_DELTA_LO;
            double sinAngle = Math.sin(angle);
            double cosAngle = Math.cos(angle);
            // For indexes corresponding to zero cosine or sine, we make sure the value is zero
            // and not an epsilon. This allows for a much better accuracy for results close to zero.
            if (i == SIN_COS_PI_INDEX) {
                sinAngle = 0.0;
            } else if (i == SIN_COS_PI_MUL_2_INDEX) {
                sinAngle = 0.0;
            } else if (i == SIN_COS_PI_MUL_0_5_INDEX) {
                cosAngle = 0.0;
            } else if (i == SIN_COS_PI_MUL_1_5_INDEX) {
                cosAngle = 0.0;
            }
            sinTab[i] = sinAngle;
            cosTab[i] = cosAngle;
        }

        /*
         * tan
         */

        for (int i=0;i<TAN_TABS_SIZE;i++) {
            // angle: in [0,TAN_MAX_VALUE_FOR_TABS].
            double angle = i * TAN_DELTA_HI + i * TAN_DELTA_LO;
            double sinAngle = Math.sin(angle);
            double cosAngle = Math.cos(angle);
            double cosAngleInv = 1/cosAngle;
            double cosAngleInv2 = cosAngleInv*cosAngleInv;
            double cosAngleInv3 = cosAngleInv2*cosAngleInv;
            double cosAngleInv4 = cosAngleInv2*cosAngleInv2;
            double cosAngleInv5 = cosAngleInv3*cosAngleInv2;
            tanTab[i] = sinAngle * cosAngleInv;
            tanDer1DivF1Tab[i] = cosAngleInv2;
            tanDer2DivF2Tab[i] = ((2*sinAngle)*cosAngleInv3) * ONE_DIV_F2;
            tanDer3DivF3Tab[i] = ((2*(1+2*sinAngle*sinAngle))*cosAngleInv4) * ONE_DIV_F3;
            tanDer4DivF4Tab[i] = ((8*sinAngle*(2+sinAngle*sinAngle))*cosAngleInv5) * ONE_DIV_F4;
        }
        
        /*
         * asin
         */

        for (int i=0;i<ASIN_TABS_SIZE;i++) {
            // x: in [0,ASIN_MAX_VALUE_FOR_TABS].
            double x = i * ASIN_DELTA;
            double oneMinusXSqInv = 1/(1-x*x);
            double oneMinusXSqInv0_5 = Math.sqrt(oneMinusXSqInv);
            double oneMinusXSqInv1_5 = oneMinusXSqInv0_5*oneMinusXSqInv;
            double oneMinusXSqInv2_5 = oneMinusXSqInv1_5*oneMinusXSqInv;
            double oneMinusXSqInv3_5 = oneMinusXSqInv2_5*oneMinusXSqInv;
            asinTab[i] = Math.asin(x);
            asinDer1DivF1Tab[i] = oneMinusXSqInv0_5;
            asinDer2DivF2Tab[i] = (x*oneMinusXSqInv1_5) * ONE_DIV_F2;
            asinDer3DivF3Tab[i] = ((1+2*x*x)*oneMinusXSqInv2_5) * ONE_DIV_F3;
            asinDer4DivF4Tab[i] = ((5+2*x*(2+x*(5-2*x)))*oneMinusXSqInv3_5) * ONE_DIV_F4;
        }

        if (USE_POWTABS_FOR_ASIN) {
            for (int i=0;i<ASIN_POWTABS_SIZE;i++) {
                // x: in [0,ASIN_MAX_VALUE_FOR_POWTABS].
                double x = Math.pow(i*(1.0/ASIN_POWTABS_SIZE_MINUS_ONE), 1.0/ASIN_POWTABS_POWER) * ASIN_MAX_VALUE_FOR_POWTABS;
                double oneMinusXSqInv = 1/(1-x*x);
                double oneMinusXSqInv0_5 = Math.sqrt(oneMinusXSqInv);
                double oneMinusXSqInv1_5 = oneMinusXSqInv0_5*oneMinusXSqInv;
                double oneMinusXSqInv2_5 = oneMinusXSqInv1_5*oneMinusXSqInv;
                double oneMinusXSqInv3_5 = oneMinusXSqInv2_5*oneMinusXSqInv;
                asinParamPowTab[i] = x;
                asinPowTab[i] = Math.asin(x);
                asinDer1DivF1PowTab[i] = oneMinusXSqInv0_5;
                asinDer2DivF2PowTab[i] = (x*oneMinusXSqInv1_5) * ONE_DIV_F2;
                asinDer3DivF3PowTab[i] = ((1+2*x*x)*oneMinusXSqInv2_5) * ONE_DIV_F3;
                asinDer4DivF4PowTab[i] = ((5+2*x*(2+x*(5-2*x)))*oneMinusXSqInv3_5) * ONE_DIV_F4;
            }
        }
        
        /*
         * atan
         */

        for (int i=0;i<ATAN_TABS_SIZE;i++) {
            // x: in [0,ATAN_MAX_VALUE_FOR_TABS].
            double x = i * ATAN_DELTA;
            double onePlusXSqInv = 1/(1+x*x);
            double onePlusXSqInv2 = onePlusXSqInv*onePlusXSqInv;
            double onePlusXSqInv3 = onePlusXSqInv2*onePlusXSqInv;
            double onePlusXSqInv4 = onePlusXSqInv2*onePlusXSqInv2;
            atanTab[i] = Math.atan(x);
            atanDer1DivF1Tab[i] = onePlusXSqInv;
            atanDer2DivF2Tab[i] = (-2*x*onePlusXSqInv2) * ONE_DIV_F2;
            atanDer3DivF3Tab[i] = ((-2+6*x*x)*onePlusXSqInv3) * ONE_DIV_F3;
            atanDer4DivF4Tab[i] = ((24*x*(1-x*x))*onePlusXSqInv4) * ONE_DIV_F4;
        }
        
        /*
         * exp
         */

        for (int i=(int)EXP_UNDERFLOW_LIMIT;i<=(int)EXP_OVERFLOW_LIMIT;i++) {
            expHiTab[i-(int)EXP_UNDERFLOW_LIMIT] = Math.exp(i);
        }
        for (int i=0;i<EXP_LO_TAB_SIZE;i++) {
            // x: in [-EXPM1_DISTANCE_TO_ZERO,EXPM1_DISTANCE_TO_ZERO].
            double x = -EXP_LO_DISTANCE_TO_ZERO + i/(double)EXP_LO_INDEXING;
            // exp(x)
            expLoPosTab[i] = Math.exp(x);
            // 1-exp(-x), accurately computed
            expLoNegTab[i] = -Math.expm1(-x);
        }

        /*
         * log
         */

        for (int i=0;i<LOG_TAB_SIZE;i++) {
            // Exact to use inverse of tab size, since it is a power of two.
            double x = 1+i*(1.0/LOG_TAB_SIZE);
            logXLogTab[i] = Math.log(x);
            logXTab[i] = x;
            logXInvTab[i] = 1/x;
        }

        /*
         * twoPowTab
         */

        if (USE_TWO_POW_TAB) {
            for (int i=MIN_DOUBLE_EXPONENT;i<=MAX_DOUBLE_EXPONENT;i++) {
                twoPowTab[i-MIN_DOUBLE_EXPONENT] = NumbersUtils.twoPow(i);
            }
        }
        
        /*
         * sqrt
         */

        for (int i=MIN_DOUBLE_EXPONENT;i<=MAX_DOUBLE_EXPONENT;i++) {
            double twoPowExpDiv2 = Math.pow(2.0,i*0.5);
            sqrtXSqrtHiTab[i-MIN_DOUBLE_EXPONENT] = twoPowExpDiv2 * 0.5; // Half sqrt, to avoid overflows.
            sqrtSlopeHiTab[i-MIN_DOUBLE_EXPONENT] = 1/twoPowExpDiv2;
        }
        sqrtXSqrtLoTab[0] = 1.0;
        sqrtSlopeLoTab[0] = 1.0;
        final long SQRT_LO_MASK = (0x3FF0000000000000L | (0x000FFFFFFFFFFFFFL>>SQRT_LO_BITS));
        for (int i=1;i<SQRT_LO_TAB_SIZE;i++) {
            long xBits = SQRT_LO_MASK | (((long)(i-1))<<(52-SQRT_LO_BITS));
            double sqrtX = Math.sqrt(Double.longBitsToDouble(xBits));
            sqrtXSqrtLoTab[i] = sqrtX;
            sqrtSlopeLoTab[i] = 1/sqrtX;
        }

        /*
         * cbrt
         */

        for (int i=MIN_DOUBLE_EXPONENT;i<=MAX_DOUBLE_EXPONENT;i++) {
            double twoPowExpDiv3 = Math.pow(2.0,i*(1.0/3));
            cbrtXCbrtHiTab[i-MIN_DOUBLE_EXPONENT] = twoPowExpDiv3 * 0.5; // Half cbrt, to avoid overflows.
            cbrtSlopeHiTab[i-MIN_DOUBLE_EXPONENT] = (4.0/3)/(twoPowExpDiv3*twoPowExpDiv3);
        }
        cbrtXCbrtLoTab[0] = 1.0;
        cbrtSlopeLoTab[0] = 1.0;
        final long CBRT_LO_MASK = (0x3FF0000000000000L | (0x000FFFFFFFFFFFFFL>>CBRT_LO_BITS));
        for (int i=1;i<CBRT_LO_TAB_SIZE;i++) {
            long xBits = CBRT_LO_MASK | (((long)(i-1))<<(52-CBRT_LO_BITS));
            double cbrtX = Math.cbrt(Double.longBitsToDouble(xBits));
            cbrtXCbrtLoTab[i] = cbrtX;
            cbrtSlopeLoTab[i] = 1/(cbrtX*cbrtX);
        }
    }
}
