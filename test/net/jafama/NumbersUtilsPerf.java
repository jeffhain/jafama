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
    
    //--------------------------------------------------------------------------
    // PRIVATE CLASSES
    //--------------------------------------------------------------------------

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
        @Override
        public int toStringLength(int bits) {
            return NumbersUtils.toStringBits((byte)bits).length();
        }
    }

    private static class MyTSLP_toStringBits_short extends MyAbstractTSLP_1 {
        public MyTSLP_toStringBits_short() {
            super("short");
        }
        @Override
        public int toStringLength(int bits) {
            return NumbersUtils.toStringBits((short)bits).length();
        }
    }

    private static class MyTSLP_toStringBits_int extends MyAbstractTSLP_1 {
        public MyTSLP_toStringBits_int() {
            super("int");
        }
        @Override
        public int toStringLength(int bits) {
            return NumbersUtils.toStringBits(bits).length();
        }
    }

    private static class MyTSLP_toStringBits_long extends MyAbstractTSLP_1 {
        public MyTSLP_toStringBits_long() {
            super("long");
        }
        @Override
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
        @Override
        public int toStringLength(int bits) {
            return NumbersUtils.toStringBits((byte)bits,first,lastExcl,bigEndian,padding).length();
        }
    }

    private static class MyTSLP_toStringBits_short_2int_2boolean extends MyAbstractTSLP_5 {
        public MyTSLP_toStringBits_short_2int_2boolean(int first, int lastExcl, boolean bigEndian, boolean padding) {
            super("short",first, lastExcl, bigEndian, padding);
        }
        @Override
        public int toStringLength(int bits) {
            return NumbersUtils.toStringBits((short)bits,first,lastExcl,bigEndian,padding).length();
        }
    }

    private static class MyTSLP_toStringBits_int_2int_2boolean extends MyAbstractTSLP_5 {
        public MyTSLP_toStringBits_int_2int_2boolean(int first, int lastExcl, boolean bigEndian, boolean padding) {
            super("int",first, lastExcl, bigEndian, padding);
        }
        @Override
        public int toStringLength(int bits) {
            return NumbersUtils.toStringBits(bits,first,lastExcl,bigEndian,padding).length();
        }
    }

    private static class MyTSLP_toStringBits_long_2int_2boolean extends MyAbstractTSLP_5 {
        public MyTSLP_toStringBits_long_2int_2boolean(int first, int lastExcl, boolean bigEndian, boolean padding) {
            super("long",first, lastExcl, bigEndian, padding);
        }
        @Override
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

        System.out.println("--- ..."+NumbersUtilsPerf.class.getSimpleName()+" ---");
    }
    
    /*
     * 
     */
    
    private void bench_isMathematicalInteger_float() {
        final int nbrOfRuns = NBR_OF_RUNS;
        final int nbrOfCalls = NBR_OF_CALLS;

        final Random random = new Random(123456789L);
        
        System.out.println("");
        
        int dummy = Integer.MIN_VALUE;

        {
            final int n = Integer.highestOneBit(NBR_OF_CALLS);
            final int mask = n-1;
            final float[] values = new float[n];
            for (int i=0;i<n;i++) {
                values[i] = (1-2*random.nextFloat()) * 100.0f;
            }
            for (int k=0;k<nbrOfRuns;k++) {
                long a = System.nanoTime();
                for (int i=0;i<nbrOfCalls;i++) {
                    dummy += NumbersUtils.isMathematicalInteger(values[i&mask]) ? 1 : -1;
                }
                long b = System.nanoTime();
                System.out.println("NumbersUtils.isMathematicalInteger(float), values in [-100,100], took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        {
            final int n = Integer.highestOneBit(NBR_OF_CALLS);
            final int mask = n-1;
            final float[] values = new float[n];
            for (int i=0;i<n;i++) {
                // possibly NaN or +-Infinity
                values[i] = Float.intBitsToFloat(random.nextInt());
            }
            for (int k=0;k<nbrOfRuns;k++) {
                long a = System.nanoTime();
                for (int i=0;i<nbrOfCalls;i++) {
                    dummy += NumbersUtils.isMathematicalInteger(values[i&mask]) ? 1 : -1;
                }
                long b = System.nanoTime();
                System.out.println("NumbersUtils.isMathematicalInteger(float), values all magnitudes, took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        if (dummy == 0) {
            System.out.println("rare");
        }
    }
    
    private void bench_isMathematicalInteger_double() {
        final int nbrOfRuns = NBR_OF_RUNS;
        final int nbrOfCalls = NBR_OF_CALLS;

        final Random random = new Random(123456789L);
        
        System.out.println("");
        
        int dummy = Integer.MIN_VALUE;

        {
            final int n = Integer.highestOneBit(NBR_OF_CALLS);
            final int mask = n-1;
            final double[] values = new double[n];
            for (int i=0;i<n;i++) {
                values[i] = (1-2*random.nextDouble()) * 100.0;
            }
            for (int k=0;k<nbrOfRuns;k++) {
                long a = System.nanoTime();
                for (int i=0;i<nbrOfCalls;i++) {
                    dummy += NumbersUtils.isMathematicalInteger(values[i&mask]) ? 1 : -1;
                }
                long b = System.nanoTime();
                System.out.println("NumbersUtils.isMathematicalInteger(double), values in [-100,100], took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        {
            final int n = Integer.highestOneBit(NBR_OF_CALLS);
            final int mask = n-1;
            final double[] values = new double[n];
            for (int i=0;i<n;i++) {
                // possibly NaN or +-Infinity
                values[i] = Double.longBitsToDouble(random.nextLong());
            }
            for (int k=0;k<nbrOfRuns;k++) {
                long a = System.nanoTime();
                for (int i=0;i<nbrOfCalls;i++) {
                    dummy += NumbersUtils.isMathematicalInteger(values[i&mask]) ? 1 : -1;
                }
                long b = System.nanoTime();
                System.out.println("NumbersUtils.isMathematicalInteger(double), values all magnitudes, took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        if (dummy == 0) {
            System.out.println("rare");
        }
    }
    
    private void bench_isEquidistant_float() {
        final int nbrOfRuns = NBR_OF_RUNS;
        final int nbrOfCalls = NBR_OF_CALLS;

        final Random random = new Random(123456789L);
        
        System.out.println("");
        
        int dummy = Integer.MIN_VALUE;

        {
            final int n = Integer.highestOneBit(NBR_OF_CALLS);
            final int mask = n-1;
            final float[] values = new float[n];
            for (int i=0;i<n;i++) {
                values[i] = (1-2*random.nextFloat()) * 100.0f;
            }
            for (int k=0;k<nbrOfRuns;k++) {
                long a = System.nanoTime();
                for (int i=0;i<nbrOfCalls;i++) {
                    dummy += NumbersUtils.isEquidistant(values[i&mask]) ? 1 : -1;
                }
                long b = System.nanoTime();
                System.out.println("NumbersUtils.isEquidistant(float), values in [-100,100], took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        {
            final int n = Integer.highestOneBit(NBR_OF_CALLS);
            final int mask = n-1;
            final float[] values = new float[n];
            for (int i=0;i<n;i++) {
                // possibly NaN or +-Infinity
                values[i] = Float.intBitsToFloat(random.nextInt());
            }
            for (int k=0;k<nbrOfRuns;k++) {
                long a = System.nanoTime();
                for (int i=0;i<nbrOfCalls;i++) {
                    dummy += NumbersUtils.isEquidistant(values[i&mask]) ? 1 : -1;
                }
                long b = System.nanoTime();
                System.out.println("NumbersUtils.isEquidistant(float), values all magnitudes, took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        if (dummy == 0) {
            System.out.println("rare");
        }
    }

    private void bench_isEquidistant_double() {
        final int nbrOfRuns = NBR_OF_RUNS;
        final int nbrOfCalls = NBR_OF_CALLS;

        final Random random = new Random(123456789L);
        
        System.out.println("");
        
        int dummy = Integer.MIN_VALUE;

        {
            final int n = Integer.highestOneBit(NBR_OF_CALLS);
            final int mask = n-1;
            final double[] values = new double[n];
            for (int i=0;i<n;i++) {
                values[i] = (1-2*random.nextDouble()) * 100.0;
            }
            for (int k=0;k<nbrOfRuns;k++) {
                long a = System.nanoTime();
                for (int i=0;i<nbrOfCalls;i++) {
                    dummy += NumbersUtils.isEquidistant(values[i&mask]) ? 1 : -1;
                }
                long b = System.nanoTime();
                System.out.println("NumbersUtils.isEquidistant(double), values in [-100,100], took "+TestUtils.nsToSRounded(b-a)+" s");
            }
        }

        {
            final int n = Integer.highestOneBit(NBR_OF_CALLS);
            final int mask = n-1;
            final double[] values = new double[n];
            for (int i=0;i<n;i++) {
                // possibly NaN or +-Infinity
                values[i] = Double.longBitsToDouble(random.nextLong());
            }
            for (int k=0;k<nbrOfRuns;k++) {
                long a = System.nanoTime();
                for (int i=0;i<nbrOfCalls;i++) {
                    dummy += NumbersUtils.isEquidistant(values[i&mask]) ? 1 : -1;
                }
                long b = System.nanoTime();
                System.out.println("NumbersUtils.isEquidistant(double), values all magnitudes, took "+TestUtils.nsToSRounded(b-a)+" s");
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
        final int nbrOfRuns = NBR_OF_RUNS;
        final int nbrOfCalls = NBR_OF_CALLS;

        final String header = "Loop on ";
        
        int dummy = 0;

        for (int k=0;k<nbrOfRuns;k++) {
            long a = System.nanoTime();
            for (int i=0;i<nbrOfCalls;i++) {
                dummy += provider.toStringLength(i);
            }
            long b = System.nanoTime();
            System.out.println(header+provider.toString()+" took "+TestUtils.nsToSRounded(b-a)+" s");
        }

        if (dummy == 0) {
            System.out.println("rare");
        }
    }

    /*
     * 
     */

    private void bench_toString_int_int() {
        final int nbrOfRuns = NBR_OF_RUNS;
        final int nbrOfCalls = NBR_OF_CALLS;
        
        final String header = "Loop on ";

        for (int radix : new int[]{2,10,11,16}) {
            int dummy = 0;

            System.out.println("");
            System.out.println("radix = "+radix);

            for (int k=0;k<nbrOfRuns;k++) {
                long a = System.nanoTime();
                for (int i=0;i<nbrOfCalls;i++) {
                    dummy += Integer.toString(i, radix).length();
                }
                long b = System.nanoTime();
                System.out.println(header+"Integer.toString(int,int) took "+TestUtils.nsToSRounded(b-a)+" s");
            }

            for (int k=0;k<nbrOfRuns;k++) {
                long a = System.nanoTime();
                for (int i=0;i<nbrOfCalls;i++) {
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
        final int nbrOfRuns = NBR_OF_RUNS;
        final int nbrOfCalls = NBR_OF_CALLS;

        final String header = "Loop on ";

        for (int radix : new int[]{2,10,11,16}) {
            int dummy = 0;

            System.out.println("");
            System.out.println("radix = "+radix);

            for (int k=0;k<nbrOfRuns;k++) {
                long a = System.nanoTime();
                for (int i=0;i<nbrOfCalls;i++) {
                    dummy += Long.toString((long)i, radix).length();
                }
                long b = System.nanoTime();
                System.out.println(header+"Long.toString(long,int) took "+TestUtils.nsToSRounded(b-a)+" s");
            }

            for (int k=0;k<nbrOfRuns;k++) {
                long a = System.nanoTime();
                for (int i=0;i<nbrOfCalls;i++) {
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
        System.out.println("");
        bench(new MyTSLP_toStringBits_byte());
    }

    private void bench_toStringBits_short() {
        System.out.println("");
        bench(new MyTSLP_toStringBits_short());
    }

    private void bench_toStringBits_int() {
        System.out.println("");
        bench(new MyTSLP_toStringBits_int());
    }

    private void bench_toStringBits_long() {
        System.out.println("");
        bench(new MyTSLP_toStringBits_long());
    }
    
    private void bench_toStringBits_byte_2int_2boolean() {
        System.out.println("");
        bench(new MyTSLP_toStringBits_byte_2int_2boolean(0,6,true,false));
        bench(new MyTSLP_toStringBits_byte_2int_2boolean(2,6,true,true));
        bench(new MyTSLP_toStringBits_byte_2int_2boolean(2,6,true,false));
    }
    
    private void bench_toStringBits_short_2int_2boolean() {
        System.out.println("");
        bench(new MyTSLP_toStringBits_short_2int_2boolean(0,14,true,false));
        bench(new MyTSLP_toStringBits_short_2int_2boolean(2,14,true,true));
        bench(new MyTSLP_toStringBits_short_2int_2boolean(2,14,true,false));
    }
    
    private void bench_toStringBits_int_2int_2boolean() {
        System.out.println("");
        bench(new MyTSLP_toStringBits_int_2int_2boolean(0,30,true,false));
        bench(new MyTSLP_toStringBits_int_2int_2boolean(2,30,true,true));
        bench(new MyTSLP_toStringBits_int_2int_2boolean(2,30,true,false));
    }
    
    private void bench_toStringBits_long_2int_2boolean() {
        System.out.println("");
        bench(new MyTSLP_toStringBits_long_2int_2boolean(0,62,true,false));
        bench(new MyTSLP_toStringBits_long_2int_2boolean(2,62,true,true));
        bench(new MyTSLP_toStringBits_long_2int_2boolean(2,62,true,false));
    }
}
