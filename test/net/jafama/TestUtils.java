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

import java.lang.management.ManagementFactory;

/**
 * Utility treatments for tests or benches.
 */
public class TestUtils {

    //--------------------------------------------------------------------------
    // MEMBERS
    //--------------------------------------------------------------------------

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    private static final String JVM_INFO = readJVMInfo();
    
    //--------------------------------------------------------------------------
    // PUBLIC METHODS
    //--------------------------------------------------------------------------
    
    /**
     * @return JVM info to serve as header for tests logs.
     */
    public static String getJVMInfo() {
        return JVM_INFO;
    }
    
    /**
     * @param ns A duration in nanoseconds.
     * @return The specified duration in seconds, rounded to 3 digits past comma.
     */
    public static double nsToSRounded(long ns) {
        return Math.round(ns/1e6)/1e3;
    }
    
    //--------------------------------------------------------------------------
    // PRIVATE METHODS
    //--------------------------------------------------------------------------
    
    private static String readJVMInfo() {
        StringBuilder sb = new StringBuilder();
        final String[] SYSTEM_PROPERTIES = new String[] {
                "java.vm.name",
                "java.runtime.version",
                "java.class.version",
                "os.name",
                "os.arch",
                "os.version",
                "sun.arch.data.model"
        };
        for (int i=0;i<SYSTEM_PROPERTIES.length;i++) {
            sb.append(SYSTEM_PROPERTIES[i]+"="+System.getProperty(SYSTEM_PROPERTIES[i]));
            sb.append(LINE_SEPARATOR);
        }
        sb.append("availableProcessors: "+Runtime.getRuntime().availableProcessors());
        sb.append(LINE_SEPARATOR);
        sb.append("JVM input arguments: "+ManagementFactory.getRuntimeMXBean().getInputArguments());
        sb.append(LINE_SEPARATOR);
        return sb.toString();
    }
}
