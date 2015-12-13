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

import java.util.Random;

public class NumbersUtilsPerf {

    //--------------------------------------------------------------------------
    // CONFIGURATION
    //--------------------------------------------------------------------------

    private static final int NBR_OF_RUNS = 2;
    
    private static final int NBR_OF_CALLS = 10 * 1000 * 1000;

    private static final int NBR_OF_VALUES = NumbersUtils.floorPowerOfTwo(10 * 1000);

    //--------------------------------------------------------------------------
    // PRIVATE CLASSES
    //--------------------------------------------------------------------------

    /**
     * To bench toStringXXX methods on ints or longs.
     * Provides length to avoid code being optimized away.
     */
    private interface InterfaceToStringLengthProvider {
        public int toStringLength(int value);
    }

    private static abstract class MyAbstractTSLP_1 implements InterfaceToStringLengthProvider {
        final String primitive;
        public MyAbstractTSLP_1(final String primitive) {
            this.primitive = primitive;
        }
        @Override
        public String toString() {
            return "NumbersUtils.toStringBits("+primitive+")";
        }
    }

    private static abstract class MyAbstractTSLP_5 implements InterfaceToStringLengthProvider {
        final String primitive;
        final int first;
        final int lastExcl;
        final boolean bigEndian;
        final boolean padding;
        public MyAbstractTSLP_5(
                final String primitive,
                int first,
                int lastExcl,
                boolean bigEndian,
                boolean padding) {
            this.primitive = primitive;
            this.first = first;
            this.lastExcl = lastExcl;
            this.bigEndian = bigEndian;
            this.padding = padding;
        }
        @Override
        public String toString() {
            return "NumbersUtils.toStringBits("+primitive+","+first+","+lastExcl+","+bigEndian+","+padding+")";
        }
    }
    
    /*
     * 
     */

    private static class MyTSLP_toStringBits_byte extends MyAbstractTSLP_1 {
        public MyTSLP_toStringBits_byte() {
            super("byte");
        }
        //@Override
        public int toStringLength(int bits) {
            return NumbersUtils.toStringBits((byte)bits).length();
        }
    }

    private static class MyTSLP_toStringBits_short extends MyAbstractTSLP_1 {
        public MyTSLP_toStringBits_short() {
            super("short");
        }
        //@Override
        public int toStringLength(int bits) {
            return NumbersUtils.toStringBits((short)bits).length();
        }
    }

    private static class MyTSLP_toStringBits_int extends MyAbstractTSLP_1 {
        public MyTSLP_toStringBits_int() {
            super("int");
        }
        //@Override
        public int toStringLength(int bits) {
            return NumbersUtils.toStringBits(bits).length();
        }
    }

    private static class MyTSLP_toStringBits_long extends MyAbstractTSLP_1 {
        public MyTSLP_toStringBits_long() {
            super("long");
        }
        //@Override
        public int toStringLength(int bits) {
            return NumbersUtils.toStringBits((long)bits).length();
        }
    }
    
    /*
     * 
     */

    private static class MyTSLP_toStringBits_byte_2int_2boolean extends MyAbstractTSLP_5 {
        public MyTSLP_toStringBits_byte_2int_2boolean(int first, int lastExcl, boolean bigEndian, boolean padding) {
            super("byte",first, lastExcl, bigEndian, padding);
        }
        //@Override
        public int toStringLength(int bits) {
            return NumbersUtils.toStringBits((byte)bits,first,lastExcl,bigEndian,padding).length();
        }
    }

    private static class MyTSLP_toStringBits_short_2int_2boolean extends MyAbstractTSLP_5 {
        public MyTSLP_toStringBits_short_2int_2boolean(int first, int lastExcl, boolean bigEndian, boolean padding) {
            super("short",first, lastExcl, bigEndian, padding);
        }
        //@Override
        public int toStringLength(int bits) {
            return NumbersUtils.toStringBits((short)bits,first,lastExcl,bigEndian,padding).length();
        }
    }

    private static class MyTSLP_toStringBits_int_2int_2boolean extends MyAbstractTSLP_5 {
        public MyTSLP_toStringBits_int_2int_2boolean(int first, int lastExcl, boolean bigEndian, boolean padding) {
            super("int",first, lastExcl, bigEndian, padding);
        }
        //@Override
        public int toStringLength(int bits) {
            return NumbersUtils.toStringBits(bits,first,lastExcl,bigEndian,padding).length();
        }
    }

    private static class MyTSLP_toStringBits_long_2int_2boolean extends MyAbstractTSLP_5 {
        public MyTSLP_toStringBits_long_2int_2boolean(int first, int lastExcl, boolean bigEndian, boolean padding) {
            super("long",first, lastExcl, bigEndian, padding);
        }
        //@Override
        public int toStringLength(int bits) {
            return NumbersUtils.toStringBits((long)bits,first,lastExcl,bigEndian,padding).length();
        }
    }

    //--------------------------------------------------------------------------
    // PUBLIC METHODS
    //--------------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println(TestUtils.getJVMInfo());
        newRun(args);
    }

    public static void newRun(String[] args) {
        new NumbersUtilsPerf().run(args);
    }
    
    public NumbersUtilsPerf() {
    }

    //--------------------------------------------------------------------------
    // PRIVATE METHODS
    //--------------------------------------------------------------------------

    private void run(String[] args) {
        // XXX
        System.out.println("--- "+NumbersUtilsPerf.class.getSimpleName()+"... ---");
        System.out.println("number of calls = "+NBR_OF_CALLS);
        
        bench_isMathematicalInteger_float();
        
        bench_isMathematicalInteger_double();
        
        bench_isEquidistant_float();

        bench_isEquidistant_double();
        
        bench_floorPowerOfTwo_int();

        bench_floorPowerOfTwo_long();

        bench_ceilingPowerOfTwo_int();

        bench_ceilingPowerOfTwo_long();

        bench_toString_int_int();

        bench_toString_long_int();

        bench_toStringBits_byte();

        bench_toStringBits_short();

        bench_toStringBits_int();

        bench_toStringBits_long();

        bench_toStringBits_byte_2int_2boolean();
        
        bench_toStringBits_short_2int_2boolean();
        
        bench_toStringBits_int_2int_2boolean();
        
        bench_toStringBits_long_2int_2boolean();

        bench_toStringCSN_double();

        bench_toStringNoCSN_double();

        System.out.println("--- ..."+NumbersUtilsPerf.class.getSimpleName()+" ---");
    }
    
    /*
     * 
     */
    
    private void bench_isMathematicalInteger_float() {
        final Random random = new Random(123456789L);
        
        System.out.println();
        
        int dummy = Integer.MIN_VALUE;

        {
            final float[] values = new float[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                values[i] = (1-2*random.nextFloat()) * 100.0f;
            }
            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS;i++) {
                    dummy += NumbersUtils.isMathematicalInteger(values[i&(NBR_OF_VALUES-1)]) ? 1 : -1;
                }
                long b = System.nanoTime();
                System.out.println("Loop on NumbersUtils.isMathematicalInteger(float), values in [-100,100], took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        {
            final float[] values = new float[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                // possibly NaN or +-Infinity
                values[i] = Float.intBitsToFloat(random.nextInt());
            }
            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS;i++) {
                    dummy += NumbersUtils.isMathematicalInteger(values[i&(NBR_OF_VALUES-1)]) ? 1 : -1;
                }
                long b = System.nanoTime();
                System.out.println("Loop on NumbersUtils.isMathematicalInteger(float), values of all magnitudes, took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        if (dummy == 0) {
            System.out.println("rare");
        }
    }
    
    private void bench_isMathematicalInteger_double() {
        final Random random = new Random(123456789L);
        
        System.out.println();
        
        int dummy = Integer.MIN_VALUE;

        {
            final double[] values = new double[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                values[i] = (1-2*random.nextDouble()) * 100.0;
            }
            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS;i++) {
                    dummy += NumbersUtils.isMathematicalInteger(values[i&(NBR_OF_VALUES-1)]) ? 1 : -1;
                }
                long b = System.nanoTime();
                System.out.println("Loop on NumbersUtils.isMathematicalInteger(double), values in [-100,100], took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        {
            final double[] values = new double[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                // possibly NaN or +-Infinity
                values[i] = Double.longBitsToDouble(random.nextLong());
            }
            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS;i++) {
                    dummy += NumbersUtils.isMathematicalInteger(values[i&(NBR_OF_VALUES-1)]) ? 1 : -1;
                }
                long b = System.nanoTime();
                System.out.println("Loop on NumbersUtils.isMathematicalInteger(double), values of all magnitudes, took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        if (dummy == 0) {
            System.out.println("rare");
        }
    }
    
    private void bench_isEquidistant_float() {
        final Random random = new Random(123456789L);
        
        System.out.println();
        
        int dummy = Integer.MIN_VALUE;

        {
            final float[] values = new float[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                values[i] = (1-2*random.nextFloat()) * 100.0f;
            }
            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS;i++) {
                    dummy += NumbersUtils.isEquidistant(values[i&(NBR_OF_VALUES-1)]) ? 1 : -1;
                }
                long b = System.nanoTime();
                System.out.println("Loop on NumbersUtils.isEquidistant(float), values in [-100,100], took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        {
            final float[] values = new float[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                // possibly NaN or +-Infinity
                values[i] = Float.intBitsToFloat(random.nextInt());
            }
            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS;i++) {
                    dummy += NumbersUtils.isEquidistant(values[i&(NBR_OF_VALUES-1)]) ? 1 : -1;
                }
                long b = System.nanoTime();
                System.out.println("Loop on NumbersUtils.isEquidistant(float), values of all magnitudes, took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        if (dummy == 0) {
            System.out.println("rare");
        }
    }

    private void bench_isEquidistant_double() {
        final Random random = new Random(123456789L);
        
        System.out.println();
        
        int dummy = Integer.MIN_VALUE;

        {
            final double[] values = new double[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                values[i] = (1-2*random.nextDouble()) * 100.0;
            }
            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS;i++) {
                    dummy += NumbersUtils.isEquidistant(values[i&(NBR_OF_VALUES-1)]) ? 1 : -1;
                }
                long b = System.nanoTime();
                System.out.println("Loop on NumbersUtils.isEquidistant(double), values in [-100,100], took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        {
            final double[] values = new double[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                // possibly NaN or +-Infinity
                values[i] = Double.longBitsToDouble(random.nextLong());
            }
            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS;i++) {
                    dummy += NumbersUtils.isEquidistant(values[i&(NBR_OF_VALUES-1)]) ? 1 : -1;
                }
                long b = System.nanoTime();
                System.out.println("Loop on NumbersUtils.isEquidistant(double), values of all magnitudes, took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        if (dummy == 0) {
            System.out.println("rare");
        }
    }
    
    /*
     * 
     */

    private void bench_floorPowerOfTwo_int() {
        final Random random = new Random(123456789L);
        
        System.out.println();
        
        int dummy = Integer.MIN_VALUE;

        {
            final int[] values = new int[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                values[i] = (int)(1.0 + random.nextDouble() * Integer.MAX_VALUE);
            }
            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS;i++) {
                    dummy += NumbersUtils.floorPowerOfTwo(values[i&(NBR_OF_VALUES-1)]);
                }
                long b = System.nanoTime();
                System.out.println("Loop on NumbersUtils.floorPowerOfTwo(int) took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        if (dummy == 0) {
            System.out.println("rare");
        }
    }

    private void bench_floorPowerOfTwo_long() {
        final Random random = new Random(123456789L);
        
        System.out.println();
        
        long dummy = Integer.MIN_VALUE;

        {
            final long[] values = new long[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                values[i] = (long)(1.0 + random.nextDouble() * Long.MAX_VALUE);
            }
            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS;i++) {
                    dummy += NumbersUtils.floorPowerOfTwo(values[i&(NBR_OF_VALUES-1)]);
                }
                long b = System.nanoTime();
                System.out.println("Loop on NumbersUtils.floorPowerOfTwo(long) took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        if (dummy == 0) {
            System.out.println("rare");
        }
    }
    
    private void bench_ceilingPowerOfTwo_int() {
        final Random random = new Random(123456789L);
        
        System.out.println();
        
        int dummy = Integer.MIN_VALUE;

        {
            final int[] values = new int[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                values[i] = (int)(random.nextDouble() * (1<<30));
            }
            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS;i++) {
                    dummy += NumbersUtils.ceilingPowerOfTwo(values[i&(NBR_OF_VALUES-1)]);
                }
                long b = System.nanoTime();
                System.out.println("Loop on NumbersUtils.ceilingPowerOfTwo(int) took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        if (dummy == 0) {
            System.out.println("rare");
        }
    }

    private void bench_ceilingPowerOfTwo_long() {
        final Random random = new Random(123456789L);
        
        System.out.println();
        
        long dummy = Integer.MIN_VALUE;

        {
            final long[] values = new long[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                values[i] = (long)(random.nextDouble() * (1L<<62));
            }
            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS;i++) {
                    dummy += NumbersUtils.ceilingPowerOfTwo(values[i&(NBR_OF_VALUES-1)]);
                }
                long b = System.nanoTime();
                System.out.println("Loop on NumbersUtils.ceilingPowerOfTwo(long) took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        if (dummy == 0) {
            System.out.println("rare");
        }
    }

    /*
     * 
     */
    
    private static void bench(InterfaceToStringLengthProvider provider) {
        final String header = "Loop on ";
        
        int dummy = 0;

        for (int k=0;k<NBR_OF_RUNS;k++) {
            long a = System.nanoTime();
            for (int i=0;i<NBR_OF_CALLS;i++) {
                dummy += provider.toStringLength(i);
            }
            long b = System.nanoTime();
            System.out.println(header+provider+" took "+TestUtils.nsToSRounded(b-a)+" s");
        }

        if (dummy == 0) {
            System.out.println("rare");
        }
    }

    /*
     * 
     */

    private void bench_toString_int_int() {
        final String header = "Loop on ";

        for (int radix : new int[]{2,10,11,16}) {
            int dummy = 0;

            System.out.println();
            System.out.println("radix = "+radix);

            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS;i++) {
                    dummy += Integer.toString(i, radix).length();
                }
                long b = System.nanoTime();
                System.out.println(header+"Integer.toString(int,int) took "+TestUtils.nsToSRounded(b-a)+" s");
            }

            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS;i++) {
                    dummy += NumbersUtils.toString(i, radix).length();
                }
                long b = System.nanoTime();
                System.out.println(header+"NumbersUtils.toString(int,int) took "+TestUtils.nsToSRounded(b-a)+" s");
            }

            if (dummy == 0) {
                System.out.println("rare");
            }
        }
    }

    private void bench_toString_long_int() {
        final String header = "Loop on ";

        for (int radix : new int[]{2,10,11,16}) {
            int dummy = 0;

            System.out.println();
            System.out.println("radix = "+radix);

            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS;i++) {
                    dummy += Long.toString((long)i, radix).length();
                }
                long b = System.nanoTime();
                System.out.println(header+"Long.toString(long,int) took "+TestUtils.nsToSRounded(b-a)+" s");
            }

            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS;i++) {
                    dummy += NumbersUtils.toString((long)i, radix).length();
                }
                long b = System.nanoTime();
                System.out.println(header+"NumbersUtils.toString(long,int) took "+TestUtils.nsToSRounded(b-a)+" s");
            }

            if (dummy == 0) {
                System.out.println("rare");
            }
        }
    }

    private void bench_toStringBits_byte() {
        System.out.println();
        bench(new MyTSLP_toStringBits_byte());
    }

    private void bench_toStringBits_short() {
        System.out.println();
        bench(new MyTSLP_toStringBits_short());
    }

    private void bench_toStringBits_int() {
        System.out.println();
        bench(new MyTSLP_toStringBits_int());
    }

    private void bench_toStringBits_long() {
        System.out.println();
        bench(new MyTSLP_toStringBits_long());
    }
    
    private void bench_toStringBits_byte_2int_2boolean() {
        System.out.println();
        bench(new MyTSLP_toStringBits_byte_2int_2boolean(0,6,true,false));
        bench(new MyTSLP_toStringBits_byte_2int_2boolean(2,6,true,true));
        bench(new MyTSLP_toStringBits_byte_2int_2boolean(2,6,true,false));
    }
    
    private void bench_toStringBits_short_2int_2boolean() {
        System.out.println();
        bench(new MyTSLP_toStringBits_short_2int_2boolean(0,14,true,false));
        bench(new MyTSLP_toStringBits_short_2int_2boolean(2,14,true,true));
        bench(new MyTSLP_toStringBits_short_2int_2boolean(2,14,true,false));
    }
    
    private void bench_toStringBits_int_2int_2boolean() {
        System.out.println();
        bench(new MyTSLP_toStringBits_int_2int_2boolean(0,30,true,false));
        bench(new MyTSLP_toStringBits_int_2int_2boolean(2,30,true,true));
        bench(new MyTSLP_toStringBits_int_2int_2boolean(2,30,true,false));
    }
    
    private void bench_toStringBits_long_2int_2boolean() {
        System.out.println();
        bench(new MyTSLP_toStringBits_long_2int_2boolean(0,62,true,false));
        bench(new MyTSLP_toStringBits_long_2int_2boolean(2,62,true,true));
        bench(new MyTSLP_toStringBits_long_2int_2boolean(2,62,true,false));
    }
    
    /*
     * 
     */
    
    private void bench_toStringCSN_double() {
        final Random random = new Random(123456789L);
        
        System.out.println();
        
        int dummy = Integer.MIN_VALUE;

        /*
         * 
         */
        
        {
            final double[] values = new double[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                values[i] = (random.nextInt()>>random.nextInt(32));
            }

            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS/100;i++) {
                    dummy += Double.toString(values[i&(NBR_OF_VALUES-1)]).length();
                }
                long b = System.nanoTime();
                System.out.println("Loop(/100) on          Double.toString(double), values integers in int range, took "+TestUtils.nsToSRounded(b-a)+" s");
            }

            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS/100;i++) {
                    dummy += NumbersUtils.toStringCSN(values[i&(NBR_OF_VALUES-1)]).length();
                }
                long b = System.nanoTime();
                System.out.println("Loop(/100) on NumbersUtils.toStringCSN(double), values integers in int range, took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        /*
         * 
         */
        
        {
            final double[] values = new double[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                values[i] = (1.0 - 2.0 * random.nextDouble()) * 1E20;
            }

            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS/100;i++) {
                    dummy += Double.toString(values[i&(NBR_OF_VALUES-1)]).length();
                }
                long b = System.nanoTime();
                System.out.println("Loop(/100) on          Double.toString(double), values in [-1E20,1E20], took "+TestUtils.nsToSRounded(b-a)+" s");
            }

            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS/100;i++) {
                    dummy += NumbersUtils.toStringCSN(values[i&(NBR_OF_VALUES-1)]).length();
                }
                long b = System.nanoTime();
                System.out.println("Loop(/100) on NumbersUtils.toStringCSN(double), values in [-1E20,1E20], took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }
        
        /*
         * 
         */
        
        {
            final double[] values = new double[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                values[i] = Double.longBitsToDouble(random.nextLong());
            }

            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS/100;i++) {
                    dummy += Double.toString(values[i&(NBR_OF_VALUES-1)]).length();
                }
                long b = System.nanoTime();
                System.out.println("Loop(/100) on          Double.toString(double), values of all magnitudes, took "+TestUtils.nsToSRounded(b-a)+" s");
            }

            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS/100;i++) {
                    dummy += NumbersUtils.toStringCSN(values[i&(NBR_OF_VALUES-1)]).length();
                }
                long b = System.nanoTime();
                System.out.println("Loop(/100) on NumbersUtils.toStringCSN(double), values of all magnitudes, took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }
        
        /*
         * 
         */

        if (dummy == 0) {
            System.out.println("rare");
        }
    }
    
    private void bench_toStringNoCSN_double() {
        final Random random = new Random(123456789L);
        
        System.out.println();
        
        int dummy = Integer.MIN_VALUE;

        /*
         * 
         */
        
        {
            final double[] values = new double[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                values[i] = (random.nextInt()>>random.nextInt(32));
            }

            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS/100;i++) {
                    dummy += Double.toString(values[i&(NBR_OF_VALUES-1)]).length();
                }
                long b = System.nanoTime();
                System.out.println("Loop(/100) on            Double.toString(double), values integers in int range, took "+TestUtils.nsToSRounded(b-a)+" s");
            }

            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS/100;i++) {
                    dummy += NumbersUtils.toStringNoCSN(values[i&(NBR_OF_VALUES-1)]).length();
                }
                long b = System.nanoTime();
                System.out.println("Loop(/100) on NumbersUtils.toStringNoCSN(double), values integers in int range, took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        /*
         * 
         */
        
        {
            final double[] values = new double[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                values[i] = (1.0 - 2.0 * random.nextDouble()) * 1E20;
            }

            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS/100;i++) {
                    dummy += Double.toString(values[i&(NBR_OF_VALUES-1)]).length();
                }
                long b = System.nanoTime();
                System.out.println("Loop(/100) on            Double.toString(double), values in [-1E20,1E20], took "+TestUtils.nsToSRounded(b-a)+" s");
            }

            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS/100;i++) {
                    dummy += NumbersUtils.toStringNoCSN(values[i&(NBR_OF_VALUES-1)]).length();
                }
                long b = System.nanoTime();
                System.out.println("Loop(/100) on NumbersUtils.toStringNoCSN(double), values in [-1E20,1E20], took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }
        
        /*
         * 
         */

        {
            final double[] values = new double[NBR_OF_VALUES];
            for (int i=0;i<NBR_OF_VALUES;i++) {
                values[i] = Double.longBitsToDouble(random.nextLong());
            }

            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS/100;i++) {
                    dummy += Double.toString(values[i&(NBR_OF_VALUES-1)]).length();
                }
                long b = System.nanoTime();
                System.out.println("Loop(/100) on            Double.toString(double), values of all magnitudes, took "+TestUtils.nsToSRounded(b-a)+" s");
            }

            for (int k=0;k<NBR_OF_RUNS;k++) {
                long a = System.nanoTime();
                for (int i=0;i<NBR_OF_CALLS/100;i++) {
                    dummy += NumbersUtils.toStringNoCSN(values[i&(NBR_OF_VALUES-1)]).length();
                }
                long b = System.nanoTime();
                System.out.println("Loop(/100) on NumbersUtils.toStringNoCSN(double), values of all magnitudes, took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        if (dummy == 0) {
            System.out.println("rare");
        }
    }
}
