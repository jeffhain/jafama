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

import java.util.Random;

/**
 * Stuffs to bench FastMath and StrictFastMath.
 */
abstract class AbstractFastMathPerf {

    //--------------------------------------------------------------------------
    // CONFIGURATION
    //--------------------------------------------------------------------------

    static final int NBR_OF_CALLS = 10 * 1000 * 1000;

    static final int NBR_OF_VALUES = NumbersUtils.ceilingPowerOfTwo(10 * 1000);
    static final int MASK = NBR_OF_VALUES-1;

    //--------------------------------------------------------------------------
    // PACKAGE-PRIVATE CLASSES
    //--------------------------------------------------------------------------

    interface InterfaceIntGenerator {
        public int newValue();
    }

    interface InterfaceLongGenerator {
        public long newValue();
    }

    interface InterfaceFloatGenerator {
        public float newValue();
    }

    interface InterfaceDoubleGenerator {
        public double newValue();
    }

    //--------------------------------------------------------------------------
    // MEMBERS
    //--------------------------------------------------------------------------

    static final double DOUBLE_1E6 = 1e6;
    static final double DOUBLE_1E12 = 1e12;
    /**
     * For float values near limit where they can't have digits after comma.
     */
    static final float FLOAT_COMMA_LIMIT = 2e7f; // > 2*Math.pow(2,23);
    /**
     * For double values near limit where they can't have digits after comma.
     */
    static final double DOUBLE_COMMA_LIMIT = 1e16; // > 2*Math.pow(2,52);

    final Random random = new Random(123456789L);
    
    final NumbersTestUtils utils = new NumbersTestUtils(this.random);

    long timerRef;
    
    //--------------------------------------------------------------------------
    // PACKAGE-PRIVATE METHODS
    //--------------------------------------------------------------------------

    static void dummyLog() {
        System.out.println("anti optimization log - discard it");
    }

    static void useDummy(int dummy) {
        if (dummy == Integer.MIN_VALUE+1) {
            dummyLog();
        }
    }

    static void useDummy(long dummy) {
        if (dummy == Long.MIN_VALUE+1) {
            dummyLog();
        }
    }

    static void useDummy(float dummy) {
        if (dummy == Math.PI+Math.E) {
            dummyLog();
        }
    }

    static void useDummy(double dummy) {
        if (dummy == Math.PI+Math.E) {
            dummyLog();
        }
    }

    static void useDummy(boolean dummy) {
        // Either case shouldn't be optimized away
        // (uses Math.sqrt, which is a native function).
        if (dummy) {
            useDummy(Math.sqrt(Math.PI));
        } else {
            useDummy(Math.sqrt(Math.E));
        }
    }

    void startTimer() {
        timerRef = System.nanoTime();
    }

    double getElapsedSeconds() {
        return TestUtils.nsToSRounded(System.nanoTime() - timerRef);
    }

    /*
     * tabs conversions
     */

    static int[] toIntTab(double[] args) {
        final int[] res = new int[args.length];
        for (int i=0;i<args.length;i++) {
            res[i] = (int)args[i];
        }
        return res;
    }

    /*
     * tabs
     */

    int[] newIntTab(InterfaceIntGenerator generator) {
        final int[] tab = new int[NBR_OF_VALUES];
        for (int i=0;i<tab.length;i++) {
            tab[i] = generator.newValue();
        }
        return tab;
    }

    long[] newLongTab(InterfaceLongGenerator generator) {
        final long[] tab = new long[NBR_OF_VALUES];
        for (int i=0;i<tab.length;i++) {
            tab[i] = generator.newValue();
        }
        return tab;
    }

    float[] newFloatTab(InterfaceFloatGenerator generator) {
        final float[] tab = new float[NBR_OF_VALUES];
        for (int i=0;i<tab.length;i++) {
            tab[i] = generator.newValue();
        }
        return tab;
    }

    double[] newDoubleTab(InterfaceDoubleGenerator generator) {
        final double[] tab = new double[NBR_OF_VALUES];
        for (int i=0;i<tab.length;i++) {
            tab[i] = generator.newValue();
        }
        return tab;
    }

    /*
     * smart tabs
     */

    /**
     * If one argument, uses values of all magnitudes,
     * only <= 0 if argument is < 0, only >= 0 if argument is > 0,
     * and both >= 0 or <= 0 if argument is 0. If argument is -2
     * or 2, only uses values != 0.
     * If two arguments, considers them as (min,max) and uses uniform values.
     */
    int[] randomIntTabSmart(final int[] args) {
        if (args.length == 1) {
            final int sign = args[0];
            return newIntTab(new InterfaceIntGenerator() {
                public int newValue() {
                    int value = randomIntUniMag();
                    if (sign < 0) {
                        value = -Math.abs(value);
                        if (sign == -2) {
                            if (value == 0) {
                                value = -1;
                            }
                        }
                    } else if (sign > 0) {
                        value = Math.abs(value);
                        if (value == Integer.MIN_VALUE) {
                            value = Integer.MAX_VALUE;
                        }
                        if (sign == 2) {
                            if (value == 0) {
                                value = 1;
                            }
                        }
                    }
                    return value;
                }
            });
        } else if (args.length == 2) {
            final int min = args[0];
            final int max = args[1];
            return newIntTab(new InterfaceIntGenerator() {
                public int newValue() {
                    return randomIntUniform(min, max);
                }
            });
        } else {
            throw new AssertionError();
        }
    }

    /**
     * If one argument, uses values of all magnitudes,
     * only <= 0 if argument is < 0, only >= 0 if argument is > 0,
     * and both >= 0 or <= 0 if argument is 0. If argument is -2
     * or 2, only uses values != 0.
     * If two arguments, considers them as (min,max) and uses uniform values.
     */
    long[] randomLongTabSmart(final long[] args) {
        if (args.length == 1) {
            final long sign = args[0];
            return newLongTab(new InterfaceLongGenerator() {
                public long newValue() {
                    long value = randomLongUniMag();
                    if (sign < 0) {
                        value = -Math.abs(value);
                        if (sign == -2) {
                            if (value == 0) {
                                value = -1;
                            }
                        }
                    } else if (sign > 0) {
                        value = Math.abs(value);
                        if (value == Long.MIN_VALUE) {
                            value = Long.MAX_VALUE;
                        }
                        if (sign == 2) {
                            if (value == 0) {
                                value = 1;
                            }
                        }
                    }
                    return value;
                }
            });
        } else if (args.length == 2) {
            final long min = args[0];
            final long max = args[1];
            return newLongTab(new InterfaceLongGenerator() {
                public long newValue() {
                    return randomLongUniform(min, max);
                }
            });
        } else {
            throw new AssertionError();
        }
    }

    /**
     * If no argument, uses values of whatever type.
     * If one argument, uses values of all magnitudes, not NaN nor +-Infinity,
     * only <= 0 if argument is < 0, only >= 0 if argument is > 0,
     * and both <= 0 or >= 0 if argument is +-0.0f. If argument is -2
     * or 2, only uses normal values.
     * If two arguments, considers them as (min,max) and uses uniform values.
     * If three arguments, considers them as (minExponent,maxExponent,sign).
     */
    float[] randomFloatTabSmart(final float[] args) {
        if (args.length == 0) {
            return newFloatTab(new InterfaceFloatGenerator() {
                public float newValue() {
                    return utils.randomFloatWhatever();
                }
            });
        } else if (args.length == 1) {
            final float sign = args[0];
            return newFloatTab(new InterfaceFloatGenerator() {
                public float newValue() {
                    float value = randomFloatUniMag();
                    if (sign < 0) {
                        value = -Math.abs(value);
                        if (sign == -2) {
                            value -= NumbersUtils.FLOAT_MIN_NORMAL;
                        }
                    } else if (sign > 0) {
                        value = Math.abs(value);
                        if (sign == 2) {
                            value += NumbersUtils.FLOAT_MIN_NORMAL;
                        }
                    }
                    return value;
                }
            });
        } else if (args.length == 2) {
            final float min = args[0];
            final float max = args[1];
            return newFloatTab(new InterfaceFloatGenerator() {
                public float newValue() {
                    return (float)randomDoubleUniform((double)min, (double)max);
                }
            });
        } else if (args.length == 3) {
            final float minExponent = args[0];
            final float maxExponent = args[1];
            final float sign = args[2];
            return newFloatTab(new InterfaceFloatGenerator() {
                public float newValue() {
                    float value = (float)Math.pow(2.0, randomDoubleUniform(minExponent, maxExponent));
                    if (sign < 0) {
                        value = -value;
                    } else if (sign == 0) {
                        value *= (random.nextBoolean() ? 1 : -1);
                    }
                    return value;
                }
            });
        } else {
            throw new AssertionError();
        }
    }

    /**
     * If no argument, uses values of whatever type.
     * If one argument, uses values of all magnitudes, not NaN nor +-Infinity,
     * only <= 0 if argument is < 0, only >= 0 if argument is > 0,
     * and both >= 0 or <= 0 if argument is +-0.0. If argument is -2
     * or 2, only uses normal values.
     * If two arguments, considers them as (min,max) and uses uniform values.
     * If three arguments, considers them as (minExponent,maxExponent,sign).
     */
    double[] randomDoubleTabSmart(final double[] args) {
        if (args.length == 0) {
            return newDoubleTab(new InterfaceDoubleGenerator() {
                public double newValue() {
                    return utils.randomDoubleWhatever();
                }
            });
        } else if (args.length == 1) {
            final double sign = args[0];
            return newDoubleTab(new InterfaceDoubleGenerator() {
                public double newValue() {
                    double value = randomDoubleUniMag();
                    if (sign < 0) {
                        value = -Math.abs(value);
                        if (sign == -2) {
                            value -= NumbersUtils.DOUBLE_MIN_NORMAL;
                        }
                    } else if (sign > 0) {
                        value = Math.abs(value);
                        if (sign == 2) {
                            value += NumbersUtils.DOUBLE_MIN_NORMAL;
                        }
                    }
                    return value;
                }
            });
        } else if (args.length == 2) {
            final double min = args[0];
            final double max = args[1];
            return newDoubleTab(new InterfaceDoubleGenerator() {
                public double newValue() {
                    return randomDoubleUniform(min, max);
                }
            });
        } else if (args.length == 3) {
            final double minExponent = args[0];
            final double maxExponent = args[1];
            final double sign = args[2];
            return newDoubleTab(new InterfaceDoubleGenerator() {
                public double newValue() {
                    double value = Math.pow(2.0, randomDoubleUniform(minExponent, maxExponent));
                    if (sign < 0) {
                        value = -value;
                    } else if (sign == 0) {
                        value *= (random.nextBoolean() ? 1 : -1);
                    }
                    return value;
                }
            });
        } else {
            throw new AssertionError();
        }
    }

    /*
     * args toString
     */

    static String toStringSmart(int[] args) {
        if (args.length == 1) {
            final int sign = args[0];
            String bonus = "";
            if (sign < 0) {
                if (sign == -1) {
                    bonus = " (<=0)";
                } else if (sign == -2) {
                    bonus = " (<0)";
                } else {
                    throw new AssertionError();
                }
            } else if (sign > 0) {
                if (sign == 1) {
                    bonus = " (>=0)";
                } else if (sign == 2) {
                    bonus = " (>0)";
                } else {
                    throw new AssertionError();
                }
            }
            return "all magnitudes"+bonus;
        } else if (args.length == 2) {
            final int min = args[0];
            final int max = args[1];
            return "["+toString(min)+","+toString(max)+"]";
        } else {
            throw new AssertionError();
        }
    }

    static String toStringSmart(long[] args) {
        if (args.length == 1) {
            final long sign = args[0];
            String bonus = "";
            if (sign < 0) {
                if (sign == -1) {
                    bonus = " (<=0)";
                } else if (sign == -2) {
                    bonus = " (<0)";
                } else {
                    throw new AssertionError();
                }
            } else if (sign > 0) {
                if (sign == 1) {
                    bonus = " (>=0)";
                } else if (sign == 2) {
                    bonus = " (>0)";
                } else {
                    throw new AssertionError();
                }
            }
            return "all magnitudes"+bonus;
        } else if (args.length == 2) {
            final long min = args[0];
            final long max = args[1];
            return "["+toString(min)+","+toString(max)+"]";
        } else {
            throw new AssertionError();
        }
    }

    static String toStringSmart(float[] args) {
        if (args.length == 0) {
            return "whatever";
        } else if (args.length == 1) {
            final float sign = args[0];
            String bonus = "";
            if (sign < 0) {
                if (sign == -1) {
                    bonus = " (<=0)";
                } else if (sign == -2) {
                    bonus = " (<0,normal)";
                } else {
                    throw new AssertionError();
                }
            } else if (sign > 0) {
                if (sign == 1) {
                    bonus = " (>=0)";
                } else if (sign == 2) {
                    bonus = " (>0,normal)";
                } else {
                    throw new AssertionError();
                }
            }
            return "all magnitudes"+bonus;
        } else if (args.length == 2) {
            final float min = args[0];
            final float max = args[1];
            return "["+toString(min)+","+toString(max)+"]";
        } else if (args.length == 3) {
            final float minExponent = args[0];
            final float maxExponent = args[1];
            final float sign = args[2];
            if (sign < 0) {
                return "[-2^"+toString(maxExponent)+",-2^"+toString(minExponent)+"]";
            } else if (sign > 0) {
                return "[2^"+toString(minExponent)+",2^"+toString(maxExponent)+"]";
            } else {
                return "{[-2^"+toString(maxExponent)+",-2^"+toString(minExponent)+"],[2^"+toString(minExponent)+",2^"+toString(maxExponent)+"]}";
            }
        } else {
            throw new AssertionError();
        }
    }

    static String toStringSmart(double[] args) {
        if (args.length == 0) {
            return "whatever";
        } else if (args.length == 1) {
            final double sign = args[0];
            String bonus = "";
            if (sign < 0) {
                if (sign == -1) {
                    bonus = " (<=0)";
                } else if (sign == -2) {
                    bonus = " (<0,normal)";
                } else {
                    throw new AssertionError();
                }
            } else if (sign > 0) {
                if (sign == 1) {
                    bonus = " (>=0)";
                } else if (sign == 2) {
                    bonus = " (>0,normal)";
                } else {
                    throw new AssertionError();
                }
            }
            return "all magnitudes"+bonus;
        } else if (args.length == 2) {
            final double min = args[0];
            final double max = args[1];
            return "["+toString(min)+","+toString(max)+"]";
        } else if (args.length == 3) {
            final double minExponent = args[0];
            final double maxExponent = args[1];
            final double sign = args[2];
            if (sign < 0) {
                return "[-2^"+toString(maxExponent)+",-2^"+toString(minExponent)+"]";
            } else if (sign > 0) {
                return "[2^"+toString(minExponent)+",2^"+toString(maxExponent)+"]";
            } else {
                return "{[-2^"+toString(maxExponent)+",-2^"+toString(minExponent)+"],[2^"+toString(minExponent)+",2^"+toString(maxExponent)+"]}";
            }
        } else {
            throw new AssertionError();
        }
    }

    /*
     * multi tabs
     */

    static String toStringSmart(int[] args1, int[] args2) {
        return "{"+toStringSmart(args1)+","+toStringSmart(args2)+"}";
    }

    static String toStringSmart(long[] args1, long[] args2) {
        return "{"+toStringSmart(args1)+","+toStringSmart(args2)+"}";
    }

    static String toStringSmart(float[] args1, int[] args2) {
        return "{"+toStringSmart(args1)+","+toStringSmart(args2)+"}";
    }

    static String toStringSmart(float[] args1, double[] args2) {
        return "{"+toStringSmart(args1)+","+toStringSmart(args2)+"}";
    }

    static String toStringSmart(double[] args1, int[] args2) {
        return "{"+toStringSmart(args1)+","+toStringSmart(args2)+"}";
    }

    static String toStringSmart(double[] args1, double[] args2) {
        return "{"+toStringSmart(args1)+","+toStringSmart(args2)+"}";
    }

    static String toStringSmart(double[] args1, double[] args2, double[] args3) {
        return "{"+toStringSmart(args1)+","+toStringSmart(args2)+","+toStringSmart(args3)+"}";
    }

    /*
     * arg toString
     */

    static String toString(int value) {
        return Integer.toString(value);
    }

    static String toString(long value) {
        return Long.toString(value);
    }

    static String toString(float value) {
        return Float.toString(value);
    }

    static String toString(double value) {
        if (value != 0.0) {
            if (isMultipleOf(value,Math.PI)) {
                return toStringMultiple(Math.round(value/Math.PI), "PI");
            }
            if (isMultipleOf(value,Math.PI/2)) {
                return toStringMultiple(Math.round(value/(Math.PI/2)), "PI/2");
            }
        }
        return Double.toString(value);
    }

    static boolean isMultipleOf(double value, double ref) {
        final double tolerance = 1e-15;
        if (Math.abs(value) < tolerance) {
            return false;
        }
        final double maxFactor = 10.0;
        return (Math.abs(value % ref) < tolerance) && (Math.abs(Math.rint(value/ref)) < maxFactor);
    }

    static String toStringMultiple(long factor, String ref) {
        if (factor == 0) {
            throw new AssertionError();
        } else if (factor == -1) {
            return "-"+ref;
        } else if (factor == 1) {
            return ref;
        } else {
            return factor+"*"+ref;
        }
    }

    /*
     * uniform
     */

    int randomIntUniform(int min, int max) {
        return this.utils.randomIntUniform(min, max);
    }

    long randomLongUniform(long min, long max) {
        return this.utils.randomLongUniform(min, max);
    }

    double randomDoubleUniform(double min, double max) {
        return this.utils.randomDoubleUniform(min, max);
    }

    /*
     * magnitudes
     */

    int randomIntUniMag() {
        return this.utils.randomIntUniMag();
    }

    long randomLongUniMag() {
        return this.utils.randomLongUniMag();
    }

    float randomFloatUniMag() {
        return this.utils.randomFloatUniMag();
    }

    double randomDoubleUniMag() {
        return this.utils.randomDoubleUniMag();
    }

    /*
     * 
     */

    void printLoopOverhead() {
        final double[] values = randomDoubleTabSmart(new double[]{0.0,1.0});
        double dummy = 0.0;
        
        startTimer();
        for (int i=0;i<NBR_OF_CALLS;i++) { int j=(i&MASK);
        dummy += values[j];
        }
        System.out.println("Loop overhead: "+getElapsedSeconds()+" s");

        useDummy(dummy);
    }
}
