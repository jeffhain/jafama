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

/**
 * CmnFastMath micro benchmarks.
 */
public class CmnFastMathPerf extends AbstractFastMathPerf {

    //--------------------------------------------------------------------------
    // PUBLIC METHODS
    //--------------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println(TestUtils.getJVMInfo());
        newRun(args);
    }

    public static void newRun(String[] args) {
        new CmnFastMathPerf().run(args);
    }

    public CmnFastMathPerf() {
    }

    //--------------------------------------------------------------------------
    // PRIVATE METHODS
    //--------------------------------------------------------------------------

    private static void settle() {
        TestUtils.settleAndNewLine();
    }

    private void run(String[] args) {
        System.out.println("--- "+CmnFastMathPerf.class.getSimpleName()+"... ---");
        System.out.println("number of calls = "+NBR_OF_CALLS);
        System.out.println("number of random values = "+NBR_OF_VALUES);
        printLoopOverhead();

        /*
         * logarithms
         */

        settle();
        test_log2_int();
        settle();
        test_log2_long();

        /*
         * powers
         */

        settle();
        test_twoPow_int();

        /*
         * absolute values
         */

        settle();
        test_abs_int();
        settle();
        test_abs_long();
        
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
        test_multiplyExact_long_int();
        settle();
        test_multiplyExact_2long();
        settle();
        test_multiplyBounded_2int();
        settle();
        test_multiplyBounded_long_int();
        settle();
        test_multiplyBounded_2long();

        /*
         * binary operators (/,%)
         */

        settle();
        test_floorDiv_2int();
        settle();
        test_floorDiv_long_int();
        settle();
        test_floorDiv_2long();
        settle();
        test_floorMod_2int();
        settle();
        test_floorMod_long_int();
        settle();
        test_floorMod_2long();

        System.out.println("--- ..."+CmnFastMathPerf.class.getSimpleName()+" ---");
    }
    
    /*
     * logarithms
     */

    private void test_log2_int() {
        double dummy = 0.0;

        System.out.println("--- testing log2(int) ---");

        for (int[] args : new int[][]{
                new int[]{1,Integer.MAX_VALUE}}) {

            final int[] values = randomIntTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += CmnFastMath.log2(values[j]);
            }
            System.out.println("Loop on CmnFastMath.log2(int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
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
            dummy += CmnFastMath.log2(values[j]);
            }
            System.out.println("Loop on CmnFastMath.log2(long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    /*
     * powers
     */
    
    private void test_twoPow_int() {
        double dummy = 0.0;

        System.out.println("--- testing twoPow(int) ---");

        for (int[] args : new int[][]{
                new int[]{-1074,1023},
                new int[]{0}}) {

            final int[] values = randomIntTabSmart(args);

            // StrictMath should not be faster than Math here.
            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.pow(2.0,(double)values[j]);
            }
            System.out.println("Loop on    Math.pow(2.0,double), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += CmnFastMath.twoPow(values[j]);
            }
            System.out.println("Loop on CmnFastMath.twoPow(int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
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

            // StrictMath should not be faster than Math here.
            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.abs(values[j]);
            }
            System.out.println("Loop on        Math.abs(int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += CmnFastMath.abs(values[j]);
            }
            System.out.println("Loop on CmnFastMath.abs(int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_abs_long() {
        long dummy = 0;

        System.out.println("--- testing abs(long) ---");

        for (long[] args : new long[][]{
                new long[]{0}}) {

            final long[] values = randomLongTabSmart(args);

            // StrictMath should not be faster than Math here.
            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += Math.abs(values[j]);
            }
            System.out.println("Loop on        Math.abs(long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += CmnFastMath.abs(values[j]);
            }
            System.out.println("Loop on CmnFastMath.abs(long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
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
            dummy += CmnFastMath.addExact(values[j],values[MASK-j]);
            }
            System.out.println("Loop on CmnFastMath.addExact(int,int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
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
            dummy += CmnFastMath.addExact(values[j],values[MASK-j]);
            }
            System.out.println("Loop on CmnFastMath.addExact(long,long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
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
            dummy += CmnFastMath.addBounded(values[j],values[MASK-j]);
            }
            System.out.println("Loop on CmnFastMath.addBounded(int,int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
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
            dummy += CmnFastMath.addBounded(values[j],values[MASK-j]);
            }
            System.out.println("Loop on CmnFastMath.addBounded(long,long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
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
            dummy += CmnFastMath.subtractExact(values[j],values[MASK-j]);
            }
            System.out.println("Loop on CmnFastMath.subtractExact(int,int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
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
            dummy += CmnFastMath.subtractExact(values[j],values[MASK-j]);
            }
            System.out.println("Loop on CmnFastMath.subtractExact(long,long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
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
            dummy += CmnFastMath.subtractBounded(values[j],values[MASK-j]);
            }
            System.out.println("Loop on CmnFastMath.subtractBounded(int,int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
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
            dummy += CmnFastMath.subtractBounded(values[j],values[MASK-j]);
            }
            System.out.println("Loop on CmnFastMath.subtractBounded(long,long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_multiplyExact_2int() {
        int dummy = 0;

        System.out.println("--- testing multiplyExact(int,int) ---");

        for (int[] args : new int[][]{
                INT_ARGS_SQRT_NEG_POS}) {

            final int[] values = randomIntTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += CmnFastMath.multiplyExact(values[j],values[MASK-j]);
            }
            System.out.println("Loop on CmnFastMath.multiplyExact(int,int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_multiplyExact_long_int() {
        int dummy = 0;

        System.out.println("--- testing multiplyExact(long,int) ---");

        {
            final long[] longArgs = LONG_ARGS_SQRT_NEG_POS;
            final int[] intArgs = INT_ARGS_SQRT_NEG_POS;
            
            final long[] longValues = randomLongTabSmart(longArgs);
            final int[] intValues = randomIntTabSmart(intArgs);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += CmnFastMath.multiplyExact(longValues[j],intValues[j]);
            }
            System.out.println("Loop on CmnFastMath.multiplyExact(long,int), args in "+toStringSmart(longArgs,intArgs)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_multiplyExact_2long() {
        long dummy = 0;

        System.out.println("--- testing multiplyExact(long,long) ---");

        for (long[] args : new long[][]{
                LONG_ARGS_SQRT_NEG_POS}) {

            final long[] values = randomLongTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += CmnFastMath.multiplyExact(values[j],values[MASK-j]);
            }
            System.out.println("Loop on CmnFastMath.multiplyExact(long,long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_multiplyBounded_2int() {
        int dummy = 0;

        System.out.println("--- testing multiplyBounded(int,int) ---");

        for (int[] args : new int[][]{
                INT_ARGS_SQRT_NEG_POS}) {

            final int[] values = randomIntTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += CmnFastMath.multiplyBounded(values[j],values[MASK-j]);
            }
            System.out.println("Loop on CmnFastMath.multiplyBounded(int,int), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_multiplyBounded_long_int() {
        int dummy = 0;

        System.out.println("--- testing multiplyBounded(long,int) ---");

        {
            final long[] longArgs = LONG_ARGS_SQRT_NEG_POS;
            final int[] intArgs = INT_ARGS_SQRT_NEG_POS;
            
            final long[] longValues = randomLongTabSmart(longArgs);
            final int[] intValues = randomIntTabSmart(intArgs);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += CmnFastMath.multiplyBounded(longValues[j],intValues[j]);
            }
            System.out.println("Loop on CmnFastMath.multiplyBounded(long,int), args in "+toStringSmart(longArgs,intArgs)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_multiplyBounded_2long() {
        long dummy = 0;

        System.out.println("--- testing multiplyBounded(long,long) ---");

        for (long[] args : new long[][]{
                LONG_ARGS_SQRT_NEG_POS}) {

            final long[] values = randomLongTabSmart(args);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += CmnFastMath.multiplyBounded(values[j],values[MASK-j]);
            }
            System.out.println("Loop on CmnFastMath.multiplyBounded(long,long), args in "+toStringSmart(args)+", took "+getElapsedSeconds()+" s");
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
            dummy += CmnFastMath.floorDiv(values1[j],values2[j]);
            }
            System.out.println("Loop on CmnFastMath.floorDiv(int,int), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_floorDiv_long_int() {
        int dummy = 0;

        System.out.println("--- testing floorDiv(long,int) ---");

        for (long[][] args12 : new long[][][]{
                new long[][]{new long[]{1},new long[]{2}},
                new long[][]{new long[]{-1},new long[]{-2}},
                new long[][]{new long[]{0},new long[]{2}},
                new long[][]{new long[]{0},new long[]{-2}}}) {

            final long[] args1 = args12[0];
            final int[] args2 = new int[]{(int) args12[1][0]};
            
            final long[] values1 = randomLongTabSmart(args1);
            final int[] values2 = randomIntTabSmart(args2);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += CmnFastMath.floorDiv(values1[j],values2[j]);
            }
            System.out.println("Loop on CmnFastMath.floorDiv(long,int), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
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
            dummy += CmnFastMath.floorDiv(values1[j],values2[j]);
            }
            System.out.println("Loop on CmnFastMath.floorDiv(long,long), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
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
            dummy += CmnFastMath.floorMod(values1[j],values2[j]);
            }
            System.out.println("Loop on CmnFastMath.floorMod(int,int), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }

    private void test_floorMod_long_int() {
        int dummy = 0;

        System.out.println("--- testing floorMod(long,int) ---");

        for (long[][] args12 : new long[][][]{
                new long[][]{new long[]{1},new long[]{2}},
                new long[][]{new long[]{-1},new long[]{-2}},
                new long[][]{new long[]{0},new long[]{2}},
                new long[][]{new long[]{0},new long[]{-2}}}) {

            final long[] args1 = args12[0];
            final int[] args2 = new int[]{(int) args12[1][0]};

            final long[] values1 = randomLongTabSmart(args1);
            final int[] values2 = randomIntTabSmart(args2);

            startTimer();
            for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
            dummy += CmnFastMath.floorMod(values1[j],values2[j]);
            }
            System.out.println("Loop on CmnFastMath.floorMod(long,int), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
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
            dummy += CmnFastMath.floorMod(values1[j],values2[j]);
            }
            System.out.println("Loop on CmnFastMath.floorMod(long,long), args in "+toStringSmart(args1,args2)+", took "+getElapsedSeconds()+" s");
        }

        useDummy(dummy);
    }
}
