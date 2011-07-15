/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.enterprise.security.jmac.config;

import com.sun.enterprise.security.SecurityServicesUtil;
import com.sun.enterprise.security.jmac.WebServicesDelegate;
import com.sun.jaspic.config.factory.BaseAuthConfigFactory;
import com.sun.jaspic.config.factory.RegStoreFileParser;
import com.sun.jaspic.config.factory.EntryInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
    
/**
 * This class implements methods in the abstract class AuthConfigFactory.
 * @author  Shing Wai Chan
 */
public class GFAuthConfigFactory extends BaseAuthConfigFactory {

 // MUST "hide" regStore in derived class.
    protected static RegStoreFileParser regStore = null;

    /**
     * to specialize the defaultEntries passed to the RegStoreFileParser
     * constructor, create another subclass of BaseAuthconfigFactory, that is
     * basically a copy of this class, with a change to the third argument
     * of the call to new ResSToreFileParser.
     * to ensure runtime use of the the associated regStore, make sure that
     * the new subclass also contains an implementation of the getRegStore method.
     * As done within this class, use the locks defined in
     * BaseAuthConfigFactory to serialize access to the regStore (both within
     * the class constructor, and within getRegStore)
     *
     * All EentyInfo OBJECTS PASSED as deualtEntries MUST HAVE BEEN
     * CONSTRCTED USING THE FOLLOWING CONSTRUCTOR:
     *
     * EntryInfo(String className, Map<String, String> properties);
     *
     */
    public GFAuthConfigFactory() {
        rLock.lock();
        try {
            if (regStore != null) {
                return;
            }
        } finally {
            rLock.unlock();
        }
        String userDir = System.getProperty("user.dir");
        wLock.lock();
        try {
            if (regStore == null) {
                regStore = new RegStoreFileParser(userDir,
                        BaseAuthConfigFactory.CONF_FILE_NAME,
                        getDefaultProviders());
                _loadFactory();
            }
        } finally {
            wLock.unlock();
        }
    }

    @Override
    protected RegStoreFileParser getRegStore() {
        rLock.lock();
        try {
            return regStore;
        } finally {
            rLock.unlock();
        }
    }

    /*
     * Contains the default providers used when none are
     * configured in a factory configuration file.
     */
    static List<EntryInfo> getDefaultProviders() {
        WebServicesDelegate delegate = null;
        SecurityServicesUtil svcUtil = SecurityServicesUtil.getInstance();
        if ( svcUtil != null) {
            delegate = svcUtil.getHabitat().getComponent(WebServicesDelegate.class);
        }
        if (delegate != null) {
            List<EntryInfo> entries = new ArrayList<EntryInfo>(2);
            entries.add(new EntryInfo(
                    delegate.getDefaultWebServicesProvider(), null));
            entries.add(new EntryInfo(
                    GFServerConfigProvider.class.getName(), null));
            return entries;
        }
        List<EntryInfo> entries = new ArrayList<EntryInfo>(1);
        entries.add(new EntryInfo(
            GFServerConfigProvider.class.getName(), null));
        return entries;
    }

}
