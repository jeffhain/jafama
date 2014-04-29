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
 * StrictFastMath micro benchmarks.
 */
public class StrictFastMathPerf extends AbstractFastMathPerf {

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
        new StrictFastMathPerf().run(args);
    }

    public StrictFastMathPerf() {
    }

    //--------------------------------------------------------------------------
    // PRIVATE METHODS
    //--------------------------------------------------------------------------

    private static void settle() {
        TestUtils.settleAndNewLine();
    }

    private void run(String[] args) {
        System.out.println("--- "+StrictFastMathPerf.class.getSimpleName()+"... ---");
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

        settle();
        test_log2_int();
        settle();
        test_log2_long();

        /*
         * powers
         */

        settle();
        test_pow_2double();
        settle();
        test_powQuick_2double();
        settle();
        test_powFast_double_int();

        settle();
        test_twoPow_int();

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
         * absolute values
         */

        settle();
        test_abs_int();
        settle();
        test_abs_long();

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
         * binary operators (+,-,*)
         */

        settle();
        test_addExact_2int();
        settle();
        test_addExact_2long();
        settle();
        test_addBounded_2int();
        settle();
        test_addBounded_2long();
        settle();
        test_subtractExact_2int();
        settle();
        test_subtractExact_2long();
        settle();
        test_subtractBounded_2int();
        settle();
        test_subtractBounded_2long();
        settle();
        test_multiplyExact_2int();
        settle();
        test_multiplyExact_2long();
        settle();
        test_multiplyBounded_2int();
        settle();
        test_multiplyBounded_2long();

        /*
         * binary operators (/,%)
         */

        settle();
        test_floorDiv_2int();
        settle();
        test_floorDiv_2long();
        settle();
        test_floorMod_2int();
        settle();
        test_floorMod_2long();

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

        System.out.println("--- ..."+StrictFastMathPerf.class.getSimpleName()+" ---");
    }

    /*
     * 
     */

    private void testClassLoad() {
        int dummy = 0;

        System.out.println("--- testing StrictFastMath class load (if tables not loaded already) ---");

        // Making sure Math and StrictMath are loaded already.
        if (Math.sin(0.0) != StrictMath.sin(0.0)) {
            System.out.println("can't happen");
        }

        startTimer();
        dummy += StrictFastMath.abs(0);
        System.out.println("StrictFastMath class load took "+getElapsedSeconds()+" s");

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
            dummy += StrictMath.sin(values[j]);
            }
            System.out.println("Loop on     StrictMath.sin(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.sin(values[j]);
            }
            System.out.println("Loop on StrictFastMath.sin(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.sin(values[j]);
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
            dummy += StrictMath.sin(values[j]);
            }
            System.out.println("Loop on          StrictMath.sin(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.sinQuick(values[j]);
            }
            System.out.println("Loop on StrictFastMath.sinQuick(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.sinQuick(values[j]);
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
            dummy += StrictMath.cos(values[j]);
            }
            System.out.println("Loop on     StrictMath.cos(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.cos(values[j]);
            }
            System.out.println("Loop on StrictFastMath.cos(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.cos(values[j]);
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
            dummy += StrictMath.cos(values[j]);
            }
            System.out.println("Loop on          StrictMath.cos(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.cosQuick(values[j]);
            }
            System.out.println("Loop on StrictFastMath.cosQuick(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.cosQuick(values[j]);
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
            double sine = StrictFastMath.sinAndCos(values[j],cosine);
            dummy += sine + cosine.value;
            }
            System.out.println("Loop on StrictFastMath.sinAndCos(double,DoubleWrapper), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.sinAndCos(values[j],cosine);
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
            dummy += StrictMath.tan(values[j]);
            }
            System.out.println("Loop on     StrictMath.tan(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.tan(values[j]);
            }
            System.out.println("Loop on StrictFastMath.tan(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.tan(values[j]);
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
            dummy += StrictMath.asin(values[j]);
            }
            System.out.println("Loop on     StrictMath.asin(double) took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.asin(values[j]);
            }
            System.out.println("Loop on StrictFastMath.asin(double) took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.asin(values[j]);
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
            dummy += StrictMath.acos(values[j]);
            }
            System.out.println("Loop on     StrictMath.acos(double) took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.acos(values[j]);
            }
            System.out.println("Loop on StrictFastMath.acos(double) took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.acos(values[j]);
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
            dummy += StrictMath.atan(values[j]);
            }
            System.out.println("Loop on     StrictMath.atan(double) took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.atan(values[j]);
            }
            System.out.println("Loop on StrictFastMath.atan(double) took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.atan(values[j]);
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
            dummy += StrictMath.atan2(values[j],values[MASK-j]);
            }
            System.out.println("Loop on     StrictMath.atan2(double,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.atan2(values[j],values[MASK-j]);
            }
            System.out.println("Loop on StrictFastMath.atan2(double,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.atan2(values[j],values[MASK-j]);
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
            dummy += StrictMath.toRadians(values[j]);
            }
            System.out.println("Loop on     StrictMath.toRadians(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.toRadians(values[j]);
            }
            System.out.println("Loop on StrictFastMath.toRadians(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s... ");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.toRadians(values[j]);
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
            dummy += StrictMath.toDegrees(values[j]);
            }
            System.out.println("Loop on     StrictMath.toDegrees(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.toDegrees(values[j]);
            }
            System.out.println("Loop on StrictFastMath.toDegrees(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s... ");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.toDegrees(values[j]);
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
            dummy &= StrictFastMath.isInClockwiseDomain(
                    values1[j],
                    values2[j],
                    values3[j]);
            }
            System.out.println("Loop on StrictFastMath.isInClockwiseDomain(double,double,double), args in "+toStringSmart(args1,args2,args3)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values1 = randomDoubleTabSmart(new double[]{});
            final double[] values2 = randomDoubleTabSmart(new double[]{});
            final double[] values3 = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy &= StrictFastMath.isInClockwiseDomain(
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
            dummy += StrictMath.sinh(values[j]);
            }
            System.out.println("Loop on     StrictMath.sinh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.sinh(values[j]);
            }
            System.out.println("Loop on StrictFastMath.sinh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.sinh(values[j]);
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
            dummy += StrictMath.cosh(values[j]);
            }
            System.out.println("Loop on     StrictMath.cosh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.cosh(values[j]);
            }
            System.out.println("Loop on StrictFastMath.cosh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.cosh(values[j]);
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
            dummy += StrictFastMath.coshm1(values[j]);
            }
            System.out.println("Loop on StrictFastMath.coshm1(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.coshm1(values[j]);
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
            double hsine = StrictFastMath.sinhAndCosh(values[j],hcosine);
            dummy += hsine + hcosine.value;
            }
            System.out.println("Loop on StrictFastMath.sinhAndCosh(double,DoubleWrapper), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.sinhAndCosh(values[j],hcosine);
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
            dummy += StrictMath.tanh(values[j]);
            }
            System.out.println("Loop on     StrictMath.tanh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.tanh(values[j]);
            }
            System.out.println("Loop on StrictFastMath.tanh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.tanh(values[j]);
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
            dummy += StrictFastMath.asinh(values[j]);
            }
            System.out.println("Loop on StrictFastMath.asinh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.asinh(values[j]);
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
            dummy += StrictFastMath.acosh(values[j]);
            }
            System.out.println("Loop on StrictFastMath.acosh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.acosh(values[j]);
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
            dummy += StrictFastMath.acosh1p(values[j]);
            }
            System.out.println("Loop on StrictFastMath.acosh1p(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.acosh1p(values[j]);
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
            dummy += StrictFastMath.atanh(values[j]);
            }
            System.out.println("Loop on StrictFastMath.atanh(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.atanh(values[j]);
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
            dummy += StrictMath.exp(values[j]);
            }
            System.out.println("Loop on     StrictMath.exp(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.exp(values[j]);
            }
            System.out.println("Loop on StrictFastMath.exp(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.exp(values[j]);
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
            dummy += StrictMath.exp(values[j]);
            }
            System.out.println("Loop on          StrictMath.exp(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.expQuick(values[j]);
            }
            System.out.println("Loop on StrictFastMath.expQuick(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.expQuick(values[j]);
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
            dummy += StrictMath.expm1(values[j]);
            }
            System.out.println("Loop on     StrictMath.expm1(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.expm1(values[j]);
            }
            System.out.println("Loop on StrictFastMath.expm1(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.expm1(values[j]);
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
            dummy += StrictMath.log(values[j]);
            }
            System.out.println("Loop on     StrictMath.log(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.log(values[j]);
            }
            System.out.println("Loop on StrictFastMath.log(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.log(values[j]);
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
            dummy += StrictMath.log(values[j]);
            }
            System.out.println("Loop on          StrictMath.log(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.logQuick(values[j]);
            }
            System.out.println("Loop on StrictFastMath.logQuick(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.logQuick(values[j]);
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
            dummy += StrictMath.log10(values[j]);
            }
            System.out.println("Loop on     StrictMath.log10(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.log10(values[j]);
            }
            System.out.println("Loop on StrictFastMath.log10(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.log10(values[j]);
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
            dummy += StrictMath.log1p(values[j]);
            }
            System.out.println("Loop on     StrictMath.log1p(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.log1p(values[j]);
            }
            System.out.println("Loop on StrictFastMath.log1p(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.log1p(values[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_log2_int() {
        double dummy = 0.0;

        System.out.println("--- testing log2(int) ---");

        for (int[] args : new int[][]{
                new int[]{1,Integer.MAX_VALUE}}) {

            final int[] values = randomIntTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.log2(values[j]);
            }
            System.out.println("Loop on StrictFastMath.log2(int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_log2_long() {
        double dummy = 0.0;

        System.out.println("--- testing log2(long) ---");

        for (long[] args : new long[][]{
                new long[]{1,Long.MAX_VALUE}}) {

            final long[] values = randomLongTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.log2(values[j]);
            }
            System.out.println("Loop on StrictFastMath.log2(long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
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
            dummy += StrictMath.pow(values1[j],values2[j]);
            }
            System.out.println("Loop on     StrictMath.pow(double,double), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.pow(values1[j],values2[j]);
            }
            System.out.println("Loop on StrictFastMath.pow(double,double), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.pow(values[j],values[MASK-j]);
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
            dummy += StrictMath.pow(values1[j],values2[j]);
            }
            System.out.println("Loop on          StrictMath.pow(double,double), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.powQuick(values1[j],values2[j]);
            }
            System.out.println("Loop on StrictFastMath.powQuick(double,double), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.powQuick(values[j],values[MASK-j]);
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
            dummy += StrictMath.pow(values1[j],(double)values2[j]);
            }
            System.out.println("Loop on      StrictMath.pow(double,double), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.powFast(values1[j],values2[j]);
            }
            System.out.println("Loop on StrictFastMath.powFast(double,int), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values1 = randomDoubleTabSmart(new double[]{});
            final int[] values2 = randomIntTabSmart(new int[]{0});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.powFast(values1[j],values2[j]);
            }
        }

        useDummy(dummy);
    }

    private void test_twoPow_int() {
        double dummy = 0.0;

        System.out.println("--- testing twoPow(int) ---");

        for (int[] args : new int[][]{
                new int[]{-1074,1023},
                new int[]{0}}) {

            final int[] values = randomIntTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictMath.pow(2.0,(double)values[j]);
            }
            System.out.println("Loop on Math.pow(2.0,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.twoPow(values[j]);
            }
            System.out.println("Loop on StrictFastMath.twoPow(int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
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
            dummy += StrictMath.sqrt(values[j]);
            }
            System.out.println("Loop on     StrictMath.sqrt(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.sqrt(values[j]);
            }
            System.out.println("Loop on StrictFastMath.sqrt(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.sqrt(values[j]);
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
            dummy += StrictMath.sqrt(values[j]);
            }
            System.out.println("Loop on          StrictMath.sqrt(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.sqrtQuick(values[j]);
            }
            System.out.println("Loop on StrictFastMath.sqrtQuick(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.sqrtQuick(values[j]);
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
            dummy += StrictFastMath.invSqrtQuick(values[j]);
            }
            System.out.println("Loop on StrictFastMath.invSqrtQuick(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.invSqrtQuick(values[j]);
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
            dummy += StrictMath.cbrt(values[j]);
            }
            System.out.println("Loop on     StrictMath.cbrt(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.cbrt(values[j]);
            }
            System.out.println("Loop on StrictFastMath.cbrt(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.cbrt(values[j]);
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
                dummy += StrictMath.hypot(valuesX[j],valuesY[j]);
                }
                System.out.println("Loop on     StrictMath.hypot(double,double), args in "+toStringSmart(args)+bonus+", took "+getElapsedSeconds()+" s");

                startTimer();
                for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
                dummy += StrictFastMath.hypot(valuesX[j],valuesY[j]);
                }
                System.out.println("Loop on StrictFastMath.hypot(double,double), args in "+toStringSmart(args)+bonus+", took "+getElapsedSeconds()+" s");
            }
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.hypot(values[j],values[MASK-j]);
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
                dummy += StrictFastMath.hypot(valuesX[j],valuesY[j],valuesZ[j]);
                }
                System.out.println("Loop on StrictFastMath.hypot(double,double,double), args in "+toStringSmart(args)+bonus+", took "+getElapsedSeconds()+" s");
            }
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.hypot(values[j],values[MASK-j],values[MASK-(j/2)]);
            }
        }

        useDummy(dummy);
    }

    /*
     * absolute values
     */

    private void test_abs_int() {
        int dummy = 0;

        System.out.println("--- testing abs(int) ---");

        for (int[] args : new int[][]{
                new int[]{0}}) {

            final int[] values = randomIntTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictMath.abs(values[j]);
            }
            System.out.println("Loop on     StrictMath.abs(int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.abs(values[j]);
            }
            System.out.println("Loop on StrictFastMath.abs(int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_abs_long() {
        long dummy = 0;

        System.out.println("--- testing abs(long) ---");

        for (long[] args : new long[][]{
                new long[]{0}}) {

            final long[] values = randomLongTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictMath.abs(values[j]);
            }
            System.out.println("Loop on     StrictMath.abs(long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.abs(values[j]);
            }
            System.out.println("Loop on StrictFastMath.abs(long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
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
            dummy += StrictFastMath.floor(values[j]);
            }
            System.out.println("Loop on StrictFastMath.floor(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.floor(values[j]);
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
            dummy += StrictMath.floor(values[j]);
            }
            System.out.println("Loop on     StrictMath.floor(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.floor(values[j]);
            }
            System.out.println("Loop on StrictFastMath.floor(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.floor(values[j]);
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
            dummy += StrictFastMath.ceil(values[j]);
            }
            System.out.println("Loop on StrictFastMath.ceil(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.ceil(values[j]);
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
            dummy += StrictMath.ceil(values[j]);
            }
            System.out.println("Loop on     StrictMath.ceil(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.ceil(values[j]);
            }
            System.out.println("Loop on StrictFastMath.ceil(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.ceil(values[j]);
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
            dummy += StrictMath.round(values[j]);
            }
            System.out.println("Loop on     StrictMath.round(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.round(values[j]);
            }
            System.out.println("Loop on StrictFastMath.round(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.round(values[j]);
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
            dummy += StrictMath.round(values[j]);
            }
            System.out.println("Loop on     StrictMath.round(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.round(values[j]);
            }
            System.out.println("Loop on StrictFastMath.round(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.round(values[j]);
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
            dummy += StrictMath.round(values[j]);
            }
            System.out.println("Loop on         StrictMath.round(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.roundEven(values[j]);
            }
            System.out.println("Loop on StrictFastMath.roundEven(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.roundEven(values[j]);
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
            dummy += StrictMath.round(values[j]);
            }
            System.out.println("Loop on         StrictMath.round(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.roundEven(values[j]);
            }
            System.out.println("Loop on StrictFastMath.roundEven(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.roundEven(values[j]);
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
            dummy += StrictFastMath.rint(values[j]);
            }
            System.out.println("Loop on StrictFastMath.rint(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.rint(values[j]);
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
            dummy += StrictMath.rint(values[j]);
            }
            System.out.println("Loop on     StrictMath.rint(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.rint(values[j]);
            }
            System.out.println("Loop on StrictFastMath.rint(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.rint(values[j]);
            }
        }

        useDummy(dummy);
    }

    /*
     * binary operators (+,-,*)
     */

    private void test_addExact_2int() {
        int dummy = 0;

        System.out.println("--- testing addExact(int,int) ---");

        for (int[] args : new int[][]{
                new int[]{Integer.MIN_VALUE/2,Integer.MAX_VALUE/2}}) {

            final int[] values = randomIntTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.addExact(values[j],values[MASK-j]);
            }
            System.out.println("Loop on StrictFastMath.addExact(int,int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_addExact_2long() {
        long dummy = 0;

        System.out.println("--- testing addExact(long,long) ---");

        for (long[] args : new long[][]{
                new long[]{Long.MIN_VALUE/2,Long.MAX_VALUE/2}}) {

            final long[] values = randomLongTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.addExact(values[j],values[MASK-j]);
            }
            System.out.println("Loop on StrictFastMath.addExact(long,long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_addBounded_2int() {
        int dummy = 0;

        System.out.println("--- testing addBounded(int,int) ---");

        for (int[] args : new int[][]{
                new int[]{Integer.MIN_VALUE/2,Integer.MAX_VALUE/2}}) {

            final int[] values = randomIntTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.addBounded(values[j],values[MASK-j]);
            }
            System.out.println("Loop on StrictFastMath.addBounded(int,int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_addBounded_2long() {
        long dummy = 0;

        System.out.println("--- testing addBounded(long,long) ---");

        for (long[] args : new long[][]{
                new long[]{Long.MIN_VALUE/2,Long.MAX_VALUE/2}}) {

            final long[] values = randomLongTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.addBounded(values[j],values[MASK-j]);
            }
            System.out.println("Loop on StrictFastMath.addBounded(long,long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_subtractExact_2int() {
        int dummy = 0;

        System.out.println("--- testing subtractExact(int,int) ---");

        for (int[] args : new int[][]{
                new int[]{Integer.MIN_VALUE/2,Integer.MAX_VALUE/2}}) {

            final int[] values = randomIntTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.subtractExact(values[j],values[MASK-j]);
            }
            System.out.println("Loop on StrictFastMath.subtractExact(int,int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_subtractExact_2long() {
        long dummy = 0;

        System.out.println("--- testing subtractExact(long,long) ---");

        for (long[] args : new long[][]{
                new long[]{Long.MIN_VALUE/2,Long.MAX_VALUE/2}}) {

            final long[] values = randomLongTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.subtractExact(values[j],values[MASK-j]);
            }
            System.out.println("Loop on StrictFastMath.subtractExact(long,long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_subtractBounded_2int() {
        int dummy = 0;

        System.out.println("--- testing subtractBounded(int,int) ---");

        for (int[] args : new int[][]{
                new int[]{Integer.MIN_VALUE/2,Integer.MAX_VALUE/2}}) {

            final int[] values = randomIntTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.subtractBounded(values[j],values[MASK-j]);
            }
            System.out.println("Loop on StrictFastMath.subtractBounded(int,int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_subtractBounded_2long() {
        long dummy = 0;

        System.out.println("--- testing subtractBounded(long,long) ---");

        for (long[] args : new long[][]{
                new long[]{Long.MIN_VALUE/2,Long.MAX_VALUE/2}}) {

            final long[] values = randomLongTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.subtractBounded(values[j],values[MASK-j]);
            }
            System.out.println("Loop on StrictFastMath.subtractBounded(long,long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_multiplyExact_2int() {
        int dummy = 0;

        System.out.println("--- testing multiplyExact(int,int) ---");

        for (int[] args : new int[][]{
                new int[]{-(int)Math.sqrt(Integer.MAX_VALUE),(int)Math.sqrt(Integer.MAX_VALUE)}}) {

            final int[] values = randomIntTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.multiplyExact(values[j],values[MASK-j]);
            }
            System.out.println("Loop on StrictFastMath.multiplyExact(int,int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_multiplyExact_2long() {
        long dummy = 0;

        System.out.println("--- testing multiplyExact(long,long) ---");

        for (long[] args : new long[][]{
                new long[]{-(long)Math.sqrt(Long.MAX_VALUE),(long)Math.sqrt(Long.MAX_VALUE)}}) {

            final long[] values = randomLongTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.multiplyExact(values[j],values[MASK-j]);
            }
            System.out.println("Loop on StrictFastMath.multiplyExact(long,long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_multiplyBounded_2int() {
        int dummy = 0;

        System.out.println("--- testing multiplyBounded(int,int) ---");

        for (int[] args : new int[][]{
                new int[]{-(int)Math.sqrt(Integer.MAX_VALUE),(int)Math.sqrt(Integer.MAX_VALUE)}}) {

            final int[] values = randomIntTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.multiplyBounded(values[j],values[MASK-j]);
            }
            System.out.println("Loop on StrictFastMath.multiplyBounded(int,int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_multiplyBounded_2long() {
        long dummy = 0;

        System.out.println("--- testing multiplyBounded(long,long) ---");

        for (long[] args : new long[][]{
                new long[]{-(long)Math.sqrt(Long.MAX_VALUE),(long)Math.sqrt(Long.MAX_VALUE)}}) {

            final long[] values = randomLongTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.multiplyBounded(values[j],values[MASK-j]);
            }
            System.out.println("Loop on StrictFastMath.multiplyBounded(long,long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    /*
     * binary operators (/,%)
     */

    public void test_floorDiv_2int() {
        int dummy = 0;

        System.out.println("--- testing floorDiv(int,int) ---");

        for (int[][] args12 : new int[][][]{
                new int[][]{new int[]{1},new int[]{2}},
                new int[][]{new int[]{-1},new int[]{-2}},
                new int[][]{new int[]{0},new int[]{2}},
                new int[][]{new int[]{0},new int[]{-2}}}) {

            final int[] args1 = args12[0];
            final int[] args2 = args12[1];

            final int[] values1 = randomIntTabSmart(args1);
            final int[] values2 = randomIntTabSmart(args2);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.floorDiv(values1[j],values2[j]);
            }
            System.out.println("Loop on StrictFastMath.floorDiv(int,int), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_floorDiv_2long() {
        int dummy = 0;

        System.out.println("--- testing floorDiv(long,long) ---");

        for (long[][] args12 : new long[][][]{
                new long[][]{new long[]{1},new long[]{2}},
                new long[][]{new long[]{-1},new long[]{-2}},
                new long[][]{new long[]{0},new long[]{2}},
                new long[][]{new long[]{0},new long[]{-2}}}) {

            final long[] args1 = args12[0];
            final long[] args2 = args12[1];

            final long[] values1 = randomLongTabSmart(args1);
            final long[] values2 = randomLongTabSmart(args2);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.floorDiv(values1[j],values2[j]);
            }
            System.out.println("Loop on StrictFastMath.floorDiv(long,long), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_floorMod_2int() {
        int dummy = 0;

        System.out.println("--- testing floorMod(int,int) ---");

        for (int[][] args12 : new int[][][]{
                new int[][]{new int[]{1},new int[]{2}},
                new int[][]{new int[]{-1},new int[]{-2}},
                new int[][]{new int[]{0},new int[]{2}},
                new int[][]{new int[]{0},new int[]{-2}}}) {

            final int[] args1 = args12[0];
            final int[] args2 = args12[1];

            final int[] values1 = randomIntTabSmart(args1);
            final int[] values2 = randomIntTabSmart(args2);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.floorMod(values1[j],values2[j]);
            }
            System.out.println("Loop on StrictFastMath.floorMod(int,int), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_floorMod_2long() {
        int dummy = 0;

        System.out.println("--- testing floorMod(long,long) ---");

        for (long[][] args12 : new long[][][]{
                new long[][]{new long[]{1},new long[]{2}},
                new long[][]{new long[]{-1},new long[]{-2}},
                new long[][]{new long[]{0},new long[]{2}},
                new long[][]{new long[]{0},new long[]{-2}}}) {

            final long[] args1 = args12[0];
            final long[] args2 = args12[1];

            final long[] values1 = randomLongTabSmart(args1);
            final long[] values2 = randomLongTabSmart(args2);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.floorMod(values1[j],values2[j]);
            }
            System.out.println("Loop on StrictFastMath.floorMod(long,long), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

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
            dummy += StrictMath.IEEEremainder(values[j],values[MASK-j]);
            }
            System.out.println("Loop on Math.IEEEremainder(double,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.remainder(values[j],values[MASK-j]);
            }
            System.out.println("Loop on StrictFastMath.remainder(double,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.remainder(values[j],values[MASK-j]);
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
            dummy += StrictFastMath.normalizeMinusPiPi(values[j]);
            }
            System.out.println("Loop on StrictFastMath.normalizeMinusPiPi(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.normalizeMinusPiPi(values[j]);
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
            dummy += StrictFastMath.normalizeMinusPiPiFast(values[j]);
            }
            System.out.println("Loop on StrictFastMath.normalizeMinusPiPiFast(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.normalizeMinusPiPiFast(values[j]);
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
            dummy += StrictFastMath.normalizeZeroTwoPi(values[j]);
            }
            System.out.println("Loop on StrictFastMath.normalizeZeroTwoPi(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.normalizeZeroTwoPi(values[j]);
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
            dummy += StrictFastMath.normalizeZeroTwoPiFast(values[j]);
            }
            System.out.println("Loop on StrictFastMath.normalizeZeroTwoPiFast(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.normalizeZeroTwoPiFast(values[j]);
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
            dummy += StrictFastMath.normalizeMinusHalfPiHalfPi(values[j]);
            }
            System.out.println("Loop on StrictFastMath.normalizeMinusHalfPiHalfPi(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.normalizeMinusHalfPiHalfPi(values[j]);
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
            dummy += StrictFastMath.normalizeMinusHalfPiHalfPiFast(values[j]);
            }
            System.out.println("Loop on StrictFastMath.normalizeMinusHalfPiHalfPiFast(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.normalizeMinusHalfPiHalfPiFast(values[j]);
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
            dummy += StrictMath.getExponent(values[j]);
            }
            System.out.println("Loop on     StrictMath.getExponent(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.getExponent(values[j]);
            }
            System.out.println("Loop on StrictFastMath.getExponent(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s... ");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.getExponent(values[j]);
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
            dummy += StrictMath.getExponent(values[j]);
            }
            System.out.println("Loop on     StrictMath.getExponent(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.getExponent(values[j]);
            }
            System.out.println("Loop on StrictFastMath.getExponent(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s... ");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.getExponent(values[j]);
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
            dummy += StrictMath.signum(values[j]);
            }
            System.out.println("Loop on     StrictMath.signum(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.signum(values[j]);
            }
            System.out.println("Loop on StrictFastMath.signum(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.signum(values[j]);
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
            dummy += StrictMath.signum(values[j]);
            }
            System.out.println("Loop on     StrictMath.signum(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.signum(values[j]);
            }
            System.out.println("Loop on StrictFastMath.signum(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.signum(values[j]);
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
            dummy += StrictFastMath.signFromBit(values[j]);
            }
            System.out.println("Loop on StrictFastMath.signFromBit(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.signFromBit(values[j]);
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
            dummy += StrictFastMath.signFromBit(values[j]);
            }
            System.out.println("Loop on StrictFastMath.signFromBit(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.signFromBit(values[j]);
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
            dummy += StrictMath.copySign(values[j],values[MASK-j]);
            }
            System.out.println("Loop on     StrictMath.copySign(float,float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.copySign(values[j],values[MASK-j]);
            }
            System.out.println("Loop on StrictFastMath.copySign(float,float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.copySign(values[j],values[MASK-j]);
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
            dummy += StrictMath.copySign(values[j],values[MASK-j]);
            }
            System.out.println("Loop on     StrictMath.copySign(double,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.copySign(values[j],values[MASK-j]);
            }
            System.out.println("Loop on StrictFastMath.copySign(double,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.copySign(values[j],values[MASK-j]);
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
            dummy += StrictMath.ulp(values[j]);
            }
            System.out.println("Loop on     StrictMath.ulp(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.ulp(values[j]);
            }
            System.out.println("Loop on StrictFastMath.ulp(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.ulp(values[j]);
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
            dummy += StrictMath.ulp(values[j]);
            }
            System.out.println("Loop on     StrictMath.ulp(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.ulp(values[j]);
            }
            System.out.println("Loop on StrictFastMath.ulp(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.ulp(values[j]);
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
            dummy += StrictMath.nextAfter(values1[j],values2[j]);
            }
            System.out.println("Loop on     StrictMath.nextAfter(float,double), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.nextAfter(values1[j],values2[j]);
            }
            System.out.println("Loop on StrictFastMath.nextAfter(float,double), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values1 = randomFloatTabSmart(new float[]{});
            final double[] values2 = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.nextAfter(values1[j],values2[j]);
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
            dummy += StrictMath.nextAfter(values[j],values[MASK-j]);
            }
            System.out.println("Loop on     StrictMath.nextAfter(double,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.nextAfter(values[j],values[MASK-j]);
            }
            System.out.println("Loop on StrictFastMath.nextAfter(double,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values1 = randomDoubleTabSmart(new double[]{});
            final double[] values2 = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.nextAfter(values1[j],values2[j]);
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
            dummy += StrictFastMath.nextDown(values[j]);
            }
            System.out.println("Loop on StrictFastMath.nextDown(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.nextDown(values[j]);
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
            dummy += StrictFastMath.nextDown(values[j]);
            }
            System.out.println("Loop on StrictFastMath.nextDown(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.nextDown(values[j]);
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
            dummy += StrictMath.nextUp(values[j]);
            }
            System.out.println("Loop on     StrictMath.nextUp(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.nextUp(values[j]);
            }
            System.out.println("Loop on StrictFastMath.nextUp(float), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final float[] values = randomFloatTabSmart(new float[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.nextUp(values[j]);
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
            dummy += StrictMath.nextUp(values[j]);
            }
            System.out.println("Loop on     StrictMath.nextUp(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.nextUp(values[j]);
            }
            System.out.println("Loop on StrictFastMath.nextUp(double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        // NaN-crash test.
        {
            final double[] values = randomDoubleTabSmart(new double[]{});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.nextUp(values[j]);
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
                dummy += StrictMath.scalb(values1[j],values2[j]);
                }
                System.out.println("Loop on     StrictMath.scalb(float,int), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");

                startTimer();
                for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
                dummy += StrictFastMath.scalb(values1[j],values2[j]);
                }
                System.out.println("Loop on StrictFastMath.scalb(float,int), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
            }
        }

        // NaN-crash test.
        {
            final float[] values1 = randomFloatTabSmart(new float[]{});
            final int[] values2 = randomIntTabSmart(new int[]{0});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.scalb(values1[j],values2[j]);
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
                dummy += StrictMath.scalb(values1[j],values2[j]);
                }
                System.out.println("Loop on     StrictMath.scalb(double,int), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");

                startTimer();
                for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
                dummy += StrictFastMath.scalb(values1[j],values2[j]);
                }
                System.out.println("Loop on StrictFastMath.scalb(double,int), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
            }
        }

        // NaN-crash test.
        {
            final double[] values1 = randomDoubleTabSmart(new double[]{});
            final int[] values2 = randomIntTabSmart(new int[]{0});
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += StrictFastMath.scalb(values1[j],values2[j]);
            }
        }

        useDummy(dummy);
    }
}
