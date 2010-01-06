/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

/* 
 * JavaCompilerException.java
 *
 * Created on June 22, 2002, 9:03 PM
 * 
 * @author  bnevins
 * @version $Revision: 1.3 $
 * <BR> <I>$Source: /cvs/glassfish/appserv-core/src/java/com/sun/ejb/codegen/JavaCompilerException.java,v $
 *
 */

package com.sun.ejb.codegen;

import com.sun.enterprise.util.i18n.StringManager;

/* This Exception should ONLY be created and thrown by JavaCompiler
 */
public class JavaCompilerException extends Exception 
{
	JavaCompilerException(Throwable t) 
	{
		// t is already localized!
		super(t);
	}

	JavaCompilerException(String key, String defalt) 
	{
		super(localStrings.getStringWithDefault(key, defalt));
	}

	JavaCompilerException(String key, String defalt, String oneObj) 
	{
		this(key, defalt, new Object[] { oneObj });
	}

	JavaCompilerException(String key, String defalt, Throwable t) 
	{
		super(localStrings.getStringWithDefault(key, defalt), t);
	}

	JavaCompilerException(String key, String defalt, Object[] objs) 
	{
		super(localStrings.getStringWithDefault(key, defalt, objs));
	}

	JavaCompilerException(String key, String defalt, Object[] objs, Throwable t) 
	{
		super(localStrings.getStringWithDefault(key, defalt, objs) 
				+ " -- " + t.getMessage(), t);
	}
	
	private static final StringManager localStrings = 
				StringManager.getManager(JavaCompilerException.class);
}


