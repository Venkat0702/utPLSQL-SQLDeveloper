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
package org.utplsql.sqldev.test.runner

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.utplsql.sqldev.model.runner.Expectation

class ExpectationTest {
	var Expectation exceptionWithDescription
	var Expectation exceptionWithoutDescription
	
	@Before
	def void setup() {
		exceptionWithDescription = new Expectation
		exceptionWithDescription.description = '''This assert must fail'''
		exceptionWithDescription.message = '''at: 1 (number) was expected to equal: 2 (number)'''
		exceptionWithDescription.caller  = '''"SCOTT.JUNIT_UTPLSQL_TEST1_PKG.TEST_2_NOK", line 14 ut.expect(1, 'This assert must fail').to_equal(2);'''
		exceptionWithoutDescription = new Expectation
		exceptionWithoutDescription.message = exceptionWithDescription.message
		exceptionWithoutDescription.caller = exceptionWithDescription.caller
		exceptionWithoutDescription.message = '''at: 1 (number) was expected to equal: 2 (number)'''
		exceptionWithoutDescription.caller  = '''"SCOTT.JUNIT_UTPLSQL_TEST1_PKG.TEST_3_NOK", line 42 ut.expect(1).to_equal(2);'''
	}
	
	@Test
	def void failedExpectationCallerLine() {
		val actual = exceptionWithDescription.callerLine
		val expected = new Integer(14)
		Assert.assertEquals(expected, actual)
	}

	@Test
	def void shortFailureTextWithDescription() {
		val actual = exceptionWithDescription.shortFailureText
		val expected = 'This assert must fail (line 14)'
		Assert.assertEquals(expected, actual)
	}

	@Test
	def void shortFailureTextWithoutDescription() {
		val actual = exceptionWithoutDescription.shortFailureText
		val expected = 'Line 42'
		Assert.assertEquals(expected, actual)
	}

}