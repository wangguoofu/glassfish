/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author bhavanishankar@dev.java.net
 */
public abstract class AbstractDeployMojo extends AbstractServerMojo {
    /**
     * @parameter expression="${name}" default-value="myapp"
     */
    protected String name;

    /**
     * @parameter expression="${contextRoot}"
     */
    protected String contextRoot;
    /**
     * @parameter expression="${precompileJsp}"
     */
    protected Boolean precompileJsp;

    /**
     * @parameter expression="${dbVendorName}"
     */
    protected String dbVendorName;

    /**
     * @parameter expression="${createTables}"
     */
    protected Boolean createTables;

    /**
     * @parameter expression="${libraries}"
     */
    protected String libraries;
    /**
     * @parameter expression="${project.build.directory}"
     */
    String buildDirectory;

    /**
     * @parameter expression="${project.build.finalName}"
     */
    String fileName;

    /**
     * @parameter expression="${app}"
     */
    protected String app;

    public abstract void execute() throws MojoExecutionException, MojoFailureException;

    protected Map<String,String> getDeploymentParameters() {
        Map<String, String> deployParams = new HashMap();
        set(deployParams, "name", name);
        set(deployParams, "force", "true");
        set(deployParams, "contextroot", contextRoot);
        set(deployParams, "precompilejsp", precompileJsp);
        set(deployParams, "dbvendorname", dbVendorName);
        set(deployParams, "createtables", createTables);
        set(deployParams, "libraries", libraries);
        return deployParams;
    }

    /**
     * Add the paramName:paramValue key-value pair into params, if both
     * paramName and paramValue are non null.
     *
     * @param params Map where the paramName:Value to be added
     * @param paramName Name of the parameter
     * @param paramValue Value of the parameter.
     */
    void set(Map<String, String> params, String paramName, Object paramValue) {
        if(paramValue != null && paramName != null) {
            params.put(paramName, paramValue.toString());
        }
    }

    protected String getApp() {
        return app != null ? app : buildDirectory + File.separator + fileName + ".war";
    }

    protected void doDeploy(String serverId, ClassLoader cl, Properties bootstrapProps,
                         File archive, Map<String, String> deploymentParams) throws Exception {
        Class clazz = cl.loadClass(PluginUtil.class.getName());
        Method m = clazz.getMethod("doDeploy", new Class[]{String.class, Properties.class,
                File.class, Map.class});
        m.invoke(null, new Object[]{serverId, bootstrapProps, archive, deploymentParams});
    }

    protected void doUndeploy(String serverId, ClassLoader cl, Properties bootstrapProps,
                              String appName, Map<String, String> deploymentParams) throws Exception {
        Class clazz = cl.loadClass(PluginUtil.class.getName());
        Method m = clazz.getMethod("doUndeploy", new Class[]{String.class, Properties.class,
                String.class, Map.class});
        m.invoke(null, new Object[]{serverId, bootstrapProps, appName, deploymentParams});
    }
    
}
