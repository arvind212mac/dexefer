/*
   Copyright 2012 Luis Román Gutiérrez

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.dexefer.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field as a DXF point property, in which each point coordinate goes to a given code.
 * @author luisro
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)	
@Target(ElementType.FIELD)
public @interface DXFPointProperty {
	/**
	 * The code of the X component of the point, as speficied in the DXF file format.
	 * @return
	 */
	public int xCode();
	/**
	 * The code of the Y component of the point, as speficied in the DXF file format.
	 * @return
	 */
	public int yCode();
	/**
	 * The code of the Z component of the point, as speficied in the DXF file format.
	 * @return
	 */
	public int zCode();
}
