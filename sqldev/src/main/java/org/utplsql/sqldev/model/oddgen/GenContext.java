/*
 * Copyright 2018 Philipp Salvisberg <philipp.salvisberg@trivadis.com>
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
package org.utplsql.sqldev.model.oddgen

import java.sql.Connection
import org.eclipse.xtend.lib.annotations.Accessors
import org.utplsql.sqldev.model.AbstractModel

@Accessors
class GenContext extends AbstractModel {
	Connection conn
	String objectType
	String objectName
	String testPackagePrefix
	String testPackageSuffix
	String testUnitPrefix
	String testUnitSuffix
	int numberOfTestsPerUnit
	boolean generateComments
	boolean disableTests
	String suitePath
	int indentSpaces
}
