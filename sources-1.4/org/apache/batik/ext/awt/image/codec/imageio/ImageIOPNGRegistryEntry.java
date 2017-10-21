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
package org.apache.batik.ext.awt.image.codec.imageio;

/**
 * RegistryEntry implementation for loading PNG images through Image I/O.
 *
 * @version $Id: ImageIOPNGRegistryEntry.java 502538 2007-02-02 08:52:56Z dvholten $
 */
public class ImageIOPNGRegistryEntry 
    extends AbstractImageIORegistryEntry {


    static final byte [] signature = {(byte)0x89, 80, 78, 71, 13, 10, 26, 10};

    public ImageIOPNGRegistryEntry() {
        super("PNG", "png", "image/png", 0, signature);
    }
    
    @Override
	public void scaleTo(long toHeight, long toWidth, int intepolationType) {
		// TODO Auto-generated method stub
		
	}
}
