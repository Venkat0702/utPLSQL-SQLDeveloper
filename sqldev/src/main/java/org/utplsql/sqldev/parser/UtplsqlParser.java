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
package org.utplsql.sqldev.parser

import java.sql.Connection
import java.util.ArrayList
import java.util.Arrays
import java.util.regex.Pattern
import javax.swing.text.JTextComponent
import org.utplsql.sqldev.dal.UtplsqlDao
import org.utplsql.sqldev.model.parser.PlsqlObject
import org.utplsql.sqldev.model.parser.Unit

class UtplsqlParser {
	String owner
	String plsql
	String plsqlReduced
	ArrayList<PlsqlObject> objects = new ArrayList<PlsqlObject>
	ArrayList<Unit> units = new ArrayList<Unit>
	
	new(String plsql, Connection conn, String owner) {
		setPlsql(plsql)
		setPlsqlReduced
		populateObjects
		populateUnits
		processAnnotations(conn, owner)
	}
	
	new(String plsql) {
		this(plsql, null, null)
	}
	
	/**
	 * JTextComponents uses one position for EOL (end-of-line),
	 * even on Windows platforms were it is two characters (CR/LF).
	 * To simplify position calculations and subsequent regular expressions
	 * all new lines are replaced with LF on Windows platforms.
	 */
	private def setPlsql(String plsql) {
		val lineSep = System.getProperty("line.separator")
		if (lineSep.length > 0) {
			// replace CR/LF with LF on Windows platforms
			this.plsql = plsql.replace(lineSep, "\n")
		} else {
			this.plsql = plsql
		}
	}
	
	/**
	 * replace the following expressions with space to simplify 
	 * and improve performance of subsequent regular expressions:
	 * - multi-line PL/SQL comments
	 * - single-line PL/SQL comments
	 * - string literals
	 * the result is not valid PL/SQL anymore, but good enough
	 * to find PL/SQL objects and units
	 */
	private def setPlsqlReduced() {
		val sb = new StringBuffer
		val p = Pattern.compile("(/\\*(.|[\\n])*?\\*/)|(--[^\\n]*\\n)|('([^']|[\\n])*?')")
		val m = p.matcher(plsql)
		var pos = 0
		while (m.find) {
			if (pos < m.start) {
				sb.append(plsql.substring(pos, m.start))
			}
			for (var i=m.start; i<m.end; i++) {
				val c = plsql.substring(i, i+1)
				if (c == "\n" || c == "\r") {
					sb.append(c)
				} else {
					sb.append(" ")
				}
			}
			pos = m.end
		}
		if (plsql.length > pos) {
			sb.append(plsql.substring(pos, plsql.length))
		}
		plsqlReduced=sb.toString
	}
	
	private def populateObjects() {
		val p = Pattern.compile("(?i)(\\s*)(create(\\s+or\\s+replace)?\\s+(package|type|function|procedure)\\s+(body\\s+)?)([^\\s]+)(\\s+)")
		val m = p.matcher(plsqlReduced)
		while (m.find) {
			val o = new PlsqlObject
			o.type = m.group(4).toUpperCase
			o.name = m.group(6)
			o.position = m.start
			objects.add(o)
		}
	}
	private def populateUnits() {
		val p = Pattern.compile("(?i)(\\s*)(procedure)(\\s+)([^\\s\\(;]+)")
		val m = p.matcher(plsqlReduced)
		while (m.find) {
			val u = new Unit
			u.name = m.group(4)
			u.position = m.start
			u.positionOfName = m.start(4)
			units.add(u)
		}
	}
	
	private def processAnnotations(Connection conn, String owner) {
		this.owner = owner
		if (conn !== null) {
			val dao = new UtplsqlDao(conn)
			if (dao.utAnnotationManagerInstalled) {
				for (o : objects) {
					val segments = Arrays.asList(o.name.fixName.split("\\."))	
					val annotations = dao.annotations(if (owner !== null) {owner} else {conn.schema}, segments.last.toUpperCase)
					if (annotations.findFirst[it.name == "suite"] !== null) {
						o.annotations = annotations
					}
				}
				val fixedUnits = new ArrayList<Unit>
				for (u : units) {
					val o = getObjectAt(u.position)
					if (o?.annotations !== null && o.annotations.findFirst [
						it.name == "test" && it.subobjectName.equalsIgnoreCase(u.name.fixName)
					] !== null) {
						fixedUnits.add(u)
					}
				}
				units = fixedUnits
				val fixedObjects = new ArrayList<PlsqlObject>
				for (o : objects) {
					if (o.annotations !== null) {
						fixedObjects.add(o)
					}
				}
				objects = fixedObjects
			}
		}
	}

	/**
	 * gets the PL/SQL object based on the current editor position
	 * 
	 * @param position the absolute position as used in {@link JTextComponent#getCaretPosition()}
	 * @return the PL/SQL object
	 */
	def getObjectAt(int position) {
		var PlsqlObject obj
		for (o : objects) {
			if (o.position <= position) {
				obj = o
			}
		}
		return obj
	}
	
	/**
	 * converts a line and column to a postion as used in as used in {@link JTextComponent#getCaretPosition()}
	 * used for testing purposes only
	 *
	 * @param line the line as used in SQL Developer, starting with 1
	 * @param column the column as used in SQL Developer, starting with 1
	 * @return the position
	 */	
	def toPosition(int line, int column) {
		var lines=0
		for (var i=0; i<plsql.length; i++) {
			if (plsql.substring(i,i+1) == "\n") {
				lines++
				if (lines == line - 1) {
					return (i + column)
				}
			}
		}
		throw new RuntimeException('''Line «line» not found.''')
	}	
	
	private def getUnitNameAt(int position) {
		var name = ""
		for (u : units) {
			if (u.position <= position) {
				name = u.name
			}
		}
		return name
	}
	
	private def fixName(String name) {
		return name.replace("\"", "")
	}
	
	def getObjects() {
		return objects
	}
	
	def getUnits() {
		return units
	}

	/**
	 * gets the utPLSQL path based on the current editor position
	 * 
	 * @param position the absolute position as used in {@link JTextComponent#getCaretPosition()}
	 * @return the utPLSQL path
	 */
	def getPathAt(int position) {
		var object = getObjectAt(position)
		if (object !== null && object.type == "PACKAGE") {
			var unitName = getUnitNameAt(position)
			val path = '''«IF owner !== null»«owner».«ENDIF»«object.name.fixName»«IF !unitName.empty».«unitName.fixName»«ENDIF»'''
			return path
		}
		return ""
	}
	
	private def getStartLine(int position) {
		var int line = 1
		for (var i = 0; i < plsql.length; i++) {
			val c = plsql.substring(i, i+1)
			if (i > position) {
				return line
			} else if (c == '\n') {
				line = line + 1
			}
		}
		return line
	}
	
	/**
	 * get the line of a PL/SQL package unit
	 * 
	 * @param unitName name of the unit. Only procedures are supported
	 * @return the line where the procedure is defined
	 */
	def getLineOf(String unitName) {
		for (u : units) {
			if (u.name.equalsIgnoreCase(unitName)) {
				return u.positionOfName.startLine
			}
		}
		return 1
	}
}