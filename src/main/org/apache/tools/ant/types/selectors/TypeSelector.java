/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "Ant" and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.tools.ant.types.selectors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Parameter;

/**
 * Selector that selects a certain kind of file: directory or regular.
 *
 * @author <a href="mailto:jefft@apache.org">Jeff Turner</a>
 * @since 1.6
 */
public class TypeSelector extends BaseExtendSelector {

    private String type = null;

    public final static String TYPE_KEY = "type";

    public TypeSelector() {
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("{typeselector type: ");
        buf.append(type);
        buf.append("}");
        return buf.toString();
    }

    /**
     * Set the type of file to require.
     */
    public void setType(FileType fileTypes) {
        this.type = fileTypes.getValue();
    }

    /**
     * When using this as a custom selector, this method will be called.
     * It translates each parameter into the appropriate setXXX() call.
     *
     * @param parameters the complete set of parameters for this selector
     */
    public void setParameters(Parameter[] parameters) {
        super.setParameters(parameters);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                String paramname = parameters[i].getName();
                if (TYPE_KEY.equalsIgnoreCase(paramname)) {
                    FileType type = new FileType();
                    type.setValue(parameters[i].getValue());
                    setType(type);
                }
                else {
                    setError("Invalid parameter " + paramname);
                }
            }
        }
    }

    /**
     * Checks to make sure all settings are kosher. In this case, it
     * means that the pattern attribute has been set.
     *
     */
    public void verifySettings() {
        if (type == null) {
            setError("The type attribute is required");
        }
    }

    /**
     * The heart of the matter. This is where the selector gets to decide
     * on the inclusion of a file in a particular fileset.
     *
     * @param basedir the base directory the scan is being done from
     * @param filename is the name of the file to check
     * @param file is a java.io.File object the selector can use
     * @return whether the file should be selected or not
     */
    public boolean isSelected(File basedir, String filename, File file) {

        // throw BuildException on error
        validate();

        if (file.isDirectory()) {
            return type.equals(FileType.DIR);
        } else return type.equals(FileType.FILE);
    }

    /**
     * Enumerated attribute with the values for types of file
     */
    public static class FileType extends EnumeratedAttribute {
        public static final String FILE="file";
        public static final String DIR="dir";
        public String[] getValues() {
            return new String[] {FILE, DIR};
        }
    }


}
