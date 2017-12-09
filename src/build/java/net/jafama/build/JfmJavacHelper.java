/*
 * Copyright 2015 Jeff Hain
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
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * Helper to compile Java sources into some directory.
 */
public class JfmJavacHelper {

    //--------------------------------------------------------------------------
    // CONFIGURATION
    //--------------------------------------------------------------------------

    private static final boolean DEBUG = false;
    
    //--------------------------------------------------------------------------
    // FIELDS
    //--------------------------------------------------------------------------
    
    /**
     * We don't like system-dependent default values.
     */
    private static final Locale LOCALE = Locale.US;
    
    /**
     * We don't like system-dependent default values.
     */
    private static final String CHARSET_NAME = "UTF-8";
    private static final Charset CHARSET = Charset.forName(CHARSET_NAME);

    private final List<String> optionList;
    
    private final List<String> classpathElementList;
    
    private final PrintStream errStream;
    
    /**
     * "out" argument for JavaCompiler.getTask(...).
     */
    private final Writer out;

    //--------------------------------------------------------------------------
    // PUBLIC METHODS
    //--------------------------------------------------------------------------

    /**
     * @param optionList Must not contain "-d" option, which is specified
     *        for each compilation, nor classpath options, which are specified
     *        aside. Can be empty.
     * @param classpathElementList Can be empty.
     * @param errStream Stream for error logs.
     */
    public JfmJavacHelper(
            List<String> optionList,
            List<String> classpathElementList,
            PrintStream errStream) {
        if (errStream == null) {
            throw new NullPointerException();
        }
        
        // Defensive copies.
        this.optionList = new ArrayList<String>(optionList);
        this.classpathElementList = new ArrayList<String>(classpathElementList);
        
        this.errStream = errStream;
        try {
            this.out = new OutputStreamWriter(errStream, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convenience constructor.
     * 
     * Uses "-g:vars -Xlint:-options -source sourceVersion -target targetVersion" option list,
     * and System.err as error stream.
     * 
     * @param sourceVersion String for the -source option.
     * @param targetVersion String for the -target option.
     * @param classpathElementList Can be empty.
     */
    public JfmJavacHelper(
            String sourceVersion,
            String targetVersion,
            List<String> classpathElementList) {
        this(
                computeDefaultOptionList(sourceVersion, targetVersion),
                classpathElementList,
                System.err);
    }

    /*
     * 
     */
    
    /**
     * @param outputDirPath Path of the directory where class files and their
     *        packages must be generated.
     * @param srcDirPathArr Paths of the root directories containing sources
     *        to compile.
     */
    public void compile(
            String compDirPath,
            String... srcDirPathArr) {
        for (String srcDirPath : srcDirPathArr) {
            this.compileInto(
                    srcDirPath,
                    compDirPath);
        }
    }

    //--------------------------------------------------------------------------
    // PRIVATE METHODS
    //--------------------------------------------------------------------------

    private static List<String> computeDefaultOptionList(String sourceVersion, String targetVersion) {
        final ArrayList<String> optionList = new ArrayList<String>();

        // To have arguments names in class files, instead of arg0 etc.
        optionList.add("-g:vars");

        // To avoid warnings with obsolete options.
        optionList.add("-Xlint:-options");
        optionList.add("-source");
        optionList.add(sourceVersion);
        optionList.add("-target");
        optionList.add(targetVersion);
        
        return optionList;
    }
    
    private void compileInto(
            String srcDirPath,
            String compDirPath) {
        
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("no JavaCompiler provided for this platform");
        }

        final List<String> javaFilePathList = new ArrayList<String>();
        final File mainSrcDir = new File(srcDirPath);
        addAllJavaFilesFromDirInto(
                mainSrcDir,
                javaFilePathList);

        {
            final File compDir = new File(compDirPath);
            JfmFsUtils.ensureDir(compDir);
        }

        final List<String> options = new ArrayList<String>(this.optionList);
        {
            options.add("-d");
            options.add(compDirPath);
            
            {
                options.add("-cp");
                
                final StringBuilder sb = new StringBuilder();
                
                // For visibility of compiled classes, when compiling
                // in multiple passes into a same directory.
                sb.append(compDirPath);
                
                for (String classpathElement : this.classpathElementList) {
                    sb.append(";");
                    sb.append(classpathElement);
                }
                
                options.add(sb.toString());
            }
        }

        final Writer out = this.out;
        // For annotation processing.
        // We don't delve into so language-specific things.
        final List<String> classes = null;
        final DiagnosticCollector<JavaFileObject> diagnosticListener =
                new DiagnosticCollector<JavaFileObject>();
        final StandardJavaFileManager fileManager = compiler.getStandardFileManager(
                diagnosticListener,
                LOCALE,
                CHARSET);
        final Iterable<? extends JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjectsFromStrings(
                        javaFilePathList);
        
        if (DEBUG) {
            System.out.println("javaFilePathList = " + javaFilePathList);
            System.out.println("options = " + options);
        }
        
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
        } catch (IOException ignore) {
        }
    }
    
    private void onCompilationError(DiagnosticCollector<JavaFileObject> diagnosticListener) {
        final PrintStream errStream = this.errStream;
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnosticListener.getDiagnostics()) {
            errStream.println();
            errStream.println("code = " + diagnostic.getCode());
            errStream.println("kind = " + diagnostic.getKind());
            errStream.println("position = " + diagnostic.getPosition());
            errStream.println("start position = " + diagnostic.getStartPosition());
            errStream.println("end position = " + diagnostic.getEndPosition());
            errStream.println("source = " + diagnostic.getSource());
            errStream.println("message = " + diagnostic.getMessage(LOCALE));
        }
        final RuntimeException exception = new RuntimeException("compilation failed");
        exception.printStackTrace(errStream);
        throw exception;
    }
    
    /*
     * 
     */
    
    /**
     * This method is recursive.
     */
    private static void addAllJavaFilesFromDirInto(
            File dir,
            List<String> javaFilePathList) {
        final File[] childArr = dir.listFiles();
        if (childArr == null) {
            return;
        }
        for (File child : childArr) {
            if (child.isDirectory()) {
                addAllJavaFilesFromDirInto(
                        child,
                        javaFilePathList);
            } else {
                final boolean isJavaFile = child.getName().endsWith(".java");
                if (isJavaFile) {
                    final String javaFilePath = child.getPath();
                    if (DEBUG) {
                        System.out.println("javaFilePath = " + javaFilePath);
                    }
                    javaFilePathList.add(javaFilePath);
                }
            }
        }
    }
}
