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

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * To create the jar.
 */
public class JfmJarBuilder {
    
    //--------------------------------------------------------------------------
    // CONFIGURATION
    //--------------------------------------------------------------------------
    
    private static final String MODULE_NAME = "net.jafama";
    
    private static final int BUFFER_SIZE = 4096;

    /**
     * We want not to do it, because software often have issues
     * with empty stuffs, cf. JDK 8182377.
     */
    private static final boolean MUST_ADD_EMPTY_DIRECTORIES = false;

    //--------------------------------------------------------------------------
    // PUBLIC METHODS
    //--------------------------------------------------------------------------

    public static void createJarFile(
            File filesRootDir,
            String implementationVersion,
            String jarFilePath) {
        final Manifest manifest = new Manifest();
        {
            final Attributes attributes = manifest.getMainAttributes();
            
            attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
            
            attributes.put(Attributes.Name.IMPLEMENTATION_VERSION, implementationVersion);
            
            attributes.put(new Name("Automatic-Module-Name"), MODULE_NAME);
            
            /*
             * We don't add "X-Compile-Source-JDK" and "X-Compile-Target-JDK"
             * attributes, because a jar could deliberately contain classes
             * with different source and target versions, to provide advanced
             * features without forcing a JDK version for other features.
             * For the same reason we don't add "Created-By" attribute, for
             * JDKs have restrictions on what versions they can be used for.
             */
        }
        
        /*
         * NB: About JarOutputStream, and due to zip specification:
         * - Directory names must end with a slash.
         * - Paths must use slashes, not anti slashes.
         * - Entries may not begin with a slash.
         */
        
        final FileOutputStream fos;
        try {
            fos = new FileOutputStream(jarFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            final JarOutputStream jos;
            try {
                jos = new JarOutputStream(fos, manifest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                // +1 for '/'.
                final int rootDirPathLengthToIgnore = filesRootDir.getPath().length() + 1;
                final byte[] tmpBuff = new byte[BUFFER_SIZE];
                addFilesInSubtree(
                        filesRootDir,
                        rootDirPathLengthToIgnore,
                        jos,
                        tmpBuff);
            } finally {
                closeQuietly(jos);
            }
        } finally {
            closeQuietly(fos);
        }
    }

    //--------------------------------------------------------------------------
    // PRIVATE METHODS
    //--------------------------------------------------------------------------
    
    private static void addFilesInSubtree(
            File dir,
            int rootDirPathLengthToIgnore,
            JarOutputStream jos,
            byte[] tmpBuff) {
        if (MUST_ADD_EMPTY_DIRECTORIES) {
            String dirEntryName = dir.getPath().replace("\\", "/");
            if (!dirEntryName.endsWith("/")) {
                dirEntryName += "/";
            }
            dirEntryName = dirEntryName.substring(rootDirPathLengthToIgnore);
            // For some reason, when adding an entry with empty name,
            // it gets renamed as the jar name minus the extension,
            // but we don't want to add this entry anyway.
            if (!dirEntryName.isEmpty()) {
                final JarEntry dirEntry = new JarEntry(dirEntryName);
                dirEntry.setTime(dir.lastModified());
                try {
                    jos.putNextEntry(dirEntry);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                closeEntryQuietly(jos);
            }
        }
        final File[] fileArr = dir.listFiles();
        if (fileArr != null) {
            for (File nestedFile : dir.listFiles()) {
                if (nestedFile.isDirectory()) {
                    addFilesInSubtree(
                            nestedFile,
                            rootDirPathLengthToIgnore,
                            jos,
                            tmpBuff);
                } else {
                    addFile(
                            nestedFile,
                            rootDirPathLengthToIgnore,
                            jos,
                            tmpBuff);
                }
            }
        }
    }

    private static void addFile(
            File file,
            int rootDirPathLengthToIgnore,
            JarOutputStream jos,
            byte[] tmpBuff) {
        String fileEntryName = file.getPath().replace("\\", "/");
        fileEntryName = fileEntryName.substring(rootDirPathLengthToIgnore);

        final JarEntry fileEntry = new JarEntry(fileEntryName);
        fileEntry.setTime(file.lastModified());
        try {
            jos.putNextEntry(fileEntry);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            final FileInputStream fis;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            try {
                final BufferedInputStream bis = new BufferedInputStream(fis);
                try {
                    int n;
                    while ((n = bis.read(tmpBuff)) != -1) {
                        jos.write(tmpBuff, 0, n);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    closeQuietly(bis);
                }
            } finally {
                closeQuietly(fis);
            }
        } finally {
            closeEntryQuietly(jos);
        }
    }

    private static void closeEntryQuietly(JarOutputStream jos) {
        try {
            jos.closeEntry();
        } catch (IOException ignore) {
        }
    }
    
    private static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException ignore) {
        }
    }
}
