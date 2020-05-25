/* Copyright 2018 Philipp Salvisberg <philipp.salvisberg@trivadis.com>
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
package org.utplsql.sqldev.model

import java.net.URL
import java.util.regex.Pattern

class URLTools {
	def replaceHexChars(String input) {
		var String output = input;
		val p = Pattern.compile("%([0-9A-F]{2})")
		val m = p.matcher(input)
		while (m.find) {
			val what = m.group(0);
			val decimal = Integer.parseInt(m.group(1), 16)
			val with = String.valueOf(decimal as char)
			output = output.replace(what, with)
		}
		return output
	}
	
	def getConnectionName(URL url) {
		val p = Pattern.compile("(sqldev.nav:)([^/]+)(//)?")
		val m = p.matcher(url.toString)
		if (m.find) {
			return m.group(2).replace("IdeConnections%2523", "IdeConnections%23").replaceHexChars
		} else {
			return ""
		}		
	}
	
	def getSchema(URL url) {
		val p = Pattern.compile("(//)([^/]+)")
		val m = p.matcher(url.toString)
		if (m.find) {
			return m.group(2)
		} else {
			return ""
		}
	}
	
	def getObjectType(URL url) {
		val p = Pattern.compile("(//)([^/]+)(/)([^/]+)")
		val m = p.matcher(url.toString)
		if (m.find) {
			return m.group(4)
		} else {
			return ""
		}
	}
	
	def getMemberObject(URL url) {
		val p = Pattern.compile("(/)([^/]+)(#MEMBER)")
		val m = p.matcher(url.toString)
		
		if (m.find) {
			return m.group(2)
		} else {
			return ""
		}		
	}	
}