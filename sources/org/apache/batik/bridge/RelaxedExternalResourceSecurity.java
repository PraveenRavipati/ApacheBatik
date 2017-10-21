/*

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.apache.batik.bridge;

import org.apache.batik.util.ParsedURL;

/**
 * This implementation of <tt>ExternalResourceSecurity</tt> allows any 
 * external references.
 *
 * @author <a href="mailto:vhardy@apache.org">Vincent Hardy</a>
 * @version $Id: RelaxedExternalResourceSecurity.java 475477 2006-11-15 22:44:28Z cam $
 */
public class RelaxedExternalResourceSecurity implements ExternalResourceSecurity {
     /**
     * Controls whether the externalResource should be loaded or not.
     *
     * @throws SecurityException if the externalResource should not be loaded.
     */
    public void checkLoadExternalResource(){
    }

    /**
     * @param externalResourceURL url for the externalResource, as defined in
     *        the externalResource's xlink:href attribute. If that
     *        attribute was empty, then this parameter should
     *        be null
     * @param docURL url for the document into which the 
     *        externalResource was found.
     */
    public RelaxedExternalResourceSecurity(ParsedURL externalResourceURL,
                                           ParsedURL docURL){
        /* do nothing */
    }
}


    
