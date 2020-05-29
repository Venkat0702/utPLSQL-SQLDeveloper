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
import org.junit.Test
import org.utplsql.sqldev.ui.runner.SmartTime

class SmartTimeTest {

	@Test
	def null_default() {
		val effective = (new SmartTime(null, false)).toString
		Assert.assertEquals(null, effective);
	}

	@Test
	def null_smart() {
		val effective = (new SmartTime(null, true)).toString
		Assert.assertEquals(null, effective);
	}

	@Test
	def ms_0_default() {
		val effective = (new SmartTime(0.0, false)).toString
		Assert.assertEquals("0.000", effective);
	}

	@Test
	def ms_0_smart() {
		val effective = (new SmartTime(0.0, true)).toString
		Assert.assertEquals("0 ms", effective);
	}

	@Test
	def ms_999_default() {
		val effective = (new SmartTime(0.999, false)).toString
		Assert.assertEquals("0.999", effective);
	}

	@Test
	def ms_999_smart() {
		val effective = (new SmartTime(0.999, true)).toString
		Assert.assertEquals("999 ms", effective);
	}

	@Test
	def s_1_default() {
		val effective = (new SmartTime(1.0, false)).toString
		Assert.assertEquals("1.000", effective);
	}

	@Test
	def s_1_smart() {
		val effective = (new SmartTime(1.0, true)).toString
		Assert.assertEquals("1.000 s", effective);
	}

	@Test
	def s_59_default() {
		val effective = (new SmartTime(59.999, false)).toString
		Assert.assertEquals("59.999", effective);
	}

	@Test
	def s_59_smart() {
		val effective = (new SmartTime(59.999, true)).toString
		Assert.assertEquals("59.999 s", effective);
	}

	@Test
	def min_1_default() {
		val effective = (new SmartTime(60.0, false)).toString
		Assert.assertEquals("60.000", effective);
	}

	@Test
	def min_1_smart() {
		val effective = (new SmartTime(60.0, true)).toString
		Assert.assertEquals("1.00 min", effective);
	}

	@Test
	def min_59_default() {
		val effective = (new SmartTime(3599.999, false)).toString
		Assert.assertEquals("3,599.999", effective);
	}

	@Test
	def min_59_smart_and_rounded() {
		val effective = (new SmartTime(3599.999, true)).toString
		Assert.assertEquals("60.00 min", effective);
	}

	@Test
	def h_1_default() {
		val effective = (new SmartTime(3600.0, false)).toString
		Assert.assertEquals("3,600.000", effective);
	}

	@Test
	def h_1_smart() {
		val effective = (new SmartTime(3600.0, true)).toString
		Assert.assertEquals("1.00 h", effective);
	}

	@Test
	def h_max_default() {
		val effective = (new SmartTime(99999.999, false)).toString
		Assert.assertEquals("99,999.999", effective);
	}

	@Test
	def h_max_smart() {
		val effective = (new SmartTime(99999.999, true)).toString
		Assert.assertEquals("27.78 h", effective);
	}

	@Test
	def h_higher_than_max_default() {
		// larger than format mask
		// grouping separator applied, even if not specified in format mask
		val effective = (new SmartTime(100000000.0, false)).toString
		Assert.assertEquals("100,000,000.000", effective);
	}

	@Test
	def h_higher_than_max_smart() {
		// larger than format mask
		// no grouping separator applied (that's ok)
		val effective = (new SmartTime(100000000.0, true)).toString
		Assert.assertEquals("27777.78 h", effective);
	}
}