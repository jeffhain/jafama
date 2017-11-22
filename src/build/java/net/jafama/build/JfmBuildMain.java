/*
 * Copyright 2017 Jeff Hain
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
package net.jafama.build;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * Compiles sources and creates the jar.
 */
public class JfmBuildMain {

    //--------------------------------------------------------------------------
    // CONFIGURATION
    //--------------------------------------------------------------------------
    
    private static final String JAFAMA_VERSION = "2.3";
    
    private static final SourceVersion SOURCE_VERSION_ENUM = SourceVersion.RELEASE_5;
    private static final String SOURCE_VERSION = "1.5";
    private static final String TARGET_VERSION = "1.5";
    
    /**
     * We don't like system-dependent default values.
     */
    private static final Locale LOCALE = Locale.US;
    
    /**
     * We don't like system-dependent default values.
     */
    private static final Charset CHARSET = Charset.forName("UTF-8");

    private static final String MAIN_SRC_PATH = "src/main/java";

    private static final String COMP_DIR_PATH = "build_comp";

    private static final String JAR_FILE_NAME = "jafama.jar";

    private static final String PACKAGE_NAME_SLASH = "net/jafama";
    private static final String[] CLASS_SIMPLE_NAME_ARR = new String[]{
        "CmnFastMath",
        "DoubleWrapper",
        "FastMath",
        "IntWrapper",
        "NumbersUtils",
        "StrictFastMath",
    };

    //--------------------------------------------------------------------------
    // PUBLIC METHODS
    //--------------------------------------------------------------------------

    public static void main(String[] args) {
        
        final File compDir = new File(COMP_DIR_PATH);
        
        JfmFsUtils.ensureEmptyDir(compDir);
        
        compileInto(COMP_DIR_PATH);

        final String jarFilePath = "dist/" + JAR_FILE_NAME;
        JfmJarBuilder.createJarFile(
                compDir,
                JAFAMA_VERSION,
                jarFilePath);
        
        System.out.println("generated " + jarFilePath);
    }
    
    //--------------------------------------------------------------------------
    // PRIVATE METHODS
    //--------------------------------------------------------------------------
    
    private static void compileInto(String compDirPath) {
        
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        final Set<SourceVersion> svSet = compiler.getSourceVersions();
        if (!svSet.contains(SOURCE_VERSION_ENUM)) {
            throw new RuntimeException("source version " + SOURCE_VERSION_ENUM + " not available");
        }

        final List<String> srcFilePathList = new ArrayList<String>();
        for (String classSimpleName : CLASS_SIMPLE_NAME_ARR) {
            srcFilePathList.add(MAIN_SRC_PATH + "/" + PACKAGE_NAME_SLASH + "/" + classSimpleName + ".java");
        }

        final List<String> options = new ArrayList<String>();
        {
            // To have arguments names in class files, instead of arg0 etc.
            options.add("-g:vars");

            // To avoid warnings with obsolete options.
            options.add("-Xlint:-options");
            options.add("-source");
            options.add(SOURCE_VERSION);
            options.add("-target");
            options.add(TARGET_VERSION);

            options.add("-d");
            options.add(compDirPath);
        }

        final Writer out = null; // System.err
        final List<String> classes = null;
        final DiagnosticCollector<JavaFileObject> diagnosticListener =
                new DiagnosticCollector<JavaFileObject>();
        final StandardJavaFileManager fileManager = compiler.getStandardFileManager(
                diagnosticListener,
                LOCALE,
                CHARSET);
        final Iterable<? extends JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjectsFromStrings(
                        srcFilePathList);
        final CompilationTask task = compiler.getTask(
                out,
                fileManager,
                diagnosticListener,
                options,
                classes,
                compilationUnits);

        final boolean success = task.call();
        if (!success) {
            onCompilationError(diagnosticListener);
        }

        try {
            fileManager.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static void onCompilationError(DiagnosticCollector<JavaFileObject> diagnosticListener) {
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnosticListener.getDiagnostics()) {
            System.out.println();
            System.out.println("code = " + diagnostic.getCode());
            System.out.println("kind = " + diagnostic.getKind());
            System.out.println("position = " + diagnostic.getPosition());
            System.out.println("start position = " + diagnostic.getStartPosition());
            System.out.println("end position = " + diagnostic.getEndPosition());
            System.out.println("source = " + diagnostic.getSource());
            System.out.println("message = " + diagnostic.getMessage(LOCALE));
        }
        throw new RuntimeException("compilation failed");
    }
}
