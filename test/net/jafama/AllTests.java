/*
 * Copyright 2014-2015 Jeff Hain
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

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class AllTests {

    //--------------------------------------------------------------------------
    // PUBLIC METHODS
    //--------------------------------------------------------------------------
    
    public static void main(String [] args) {
        TestRunner.main(new String[]{"-c",AllTests.class.getName()});
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite("Test suite for "+AllTests.class.getPackage());

        suite.addTestSuite(CmnFastMathTest.class);
        suite.addTestSuite(FastMathTest.class);
        suite.addTestSuite(NumbersUtilsTest.class);
        suite.addTestSuite(StrictFastMathTest.class);
        
        return suite;
    }
}
