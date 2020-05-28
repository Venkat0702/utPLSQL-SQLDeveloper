package org.utplsql.sqldev.test

import org.junit.Assert
import org.junit.Test
import org.utplsql.sqldev.model.PrefixTools

class PrefixToolsTest {
	@Test
	def void two() {
		val actual = PrefixTools.commonPrefix(#["junit.test.a", "junit.test.b"])
		val expected = "junit.test."
		Assert.assertEquals(expected, actual)
	}

	@Test
	def void oneWithDot() {
		val actual = PrefixTools.commonPrefix(#["junit.test.a"])
		val expected = "junit.test."
		Assert.assertEquals(expected, actual)
	}

	@Test
	def void oneWithoutDot() {
		val actual = PrefixTools.commonPrefix(#["junit-test-a"])
		val expected = ""
		Assert.assertEquals(expected, actual)
	}
	
	@Test
	def void twoOverlapLeft() {
		val actual = PrefixTools.commonPrefix(#["a.b.c", "a.b.c.d"])
		val expected = ""
		Assert.assertEquals(expected, actual)
	}

	@Test
	def void twoOverlapRight() {
		val actual = PrefixTools.commonPrefix(#["a.b.c.d", "a.b.c"])
		val expected = ""
		Assert.assertEquals(expected, actual)
	}

}