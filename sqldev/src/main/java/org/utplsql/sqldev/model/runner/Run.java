/*
 * Copyright 2019 Philipp Salvisberg <philipp.salvisberg@trivadis.com>
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
package org.utplsql.sqldev.model.runner

import java.util.LinkedHashMap
import java.util.List
import org.eclipse.xtend.lib.annotations.Accessors

@Accessors
class Run {
	String reporterId
	String connectionName
	List<String> pathList
	Integer currentTestNumber
	Test currentTest
	Integer totalNumberOfTests
	String startTime
	String endTime
	Double executionTime
	Counter counter
	Integer infoCount
	String errorStack
	String serverOutput
	LinkedHashMap<String, Test> tests
	String status
	Long start
	
	new(String reporterId, String connectionName, List<String> pathList) {
		this.reporterId = reporterId
		this.connectionName = connectionName
		this.pathList = pathList
		this.counter = new Counter
		this.tests = new LinkedHashMap<String, Test>
	}
	
	def void setStartTime(String startTime) {
		this.startTime = startTime
		start = System.currentTimeMillis
	}
		
	def getName() {
		val time = startTime.substring(11,19)
		val conn = connectionName?.substring(15)
		return '''«time» («conn»)'''
	}
	
	def void put(List<Item> items) {
		for (item : items) {
			if (item instanceof Test) {
				this.tests.put(item.id, item)
			}
			if (item instanceof Suite) {
				item.items.put
			}
		}
	}
	
	def getTest(String id) {
		return tests.get(id)
	}

	def getTotalNumberOfCompletedTests() {
		return counter.disabled + counter.success + counter.failure + counter.error
	}
	
}
