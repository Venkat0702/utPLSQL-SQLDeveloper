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
package org.utplsql.sqldev.test.preference

import org.junit.Assert
import org.junit.Test
import org.utplsql.sqldev.model.preference.PreferenceModel

class PreferenceModelTest {

	@Test
	def defaultValues() {
		val PreferenceModel model = PreferenceModel.getInstance(null)
		Assert.assertTrue(model.useRealtimeReporter)
		Assert.assertTrue(model.unsharedWorksheet)
		Assert.assertFalse(model.resetPackage)
		Assert.assertFalse(model.clearScreen)
		Assert.assertTrue(model.autoExecute)
		Assert.assertFalse(model.checkRunUtplsqlTest)
		Assert.assertFalse(model.useSmartTimes)
		Assert.assertEquals(model.numberOfRunsInHistory, 10)
		Assert.assertFalse(model.showDisabledCounter)
		Assert.assertFalse(model.showWarningsCounter)
		Assert.assertFalse(model.showInfoCounter)
		Assert.assertFalse(model.showWarningIndicator)
		Assert.assertFalse(model.showInfoIndicator)
		Assert.assertTrue(model.showSuccessfulTests)
		Assert.assertTrue(model.showDisabledTests)
		Assert.assertFalse(model.isShowTestDescription)
		Assert.assertTrue(model.syncDetailTab)
		Assert.assertEquals("test_", model.testPackagePrefix)
		Assert.assertEquals("", model.testPackageSuffix)
		Assert.assertEquals("", model.testUnitPrefix)
		Assert.assertEquals("", model.testUnitSuffix)
		Assert.assertEquals(1, model.numberOfTestsPerUnit)
		Assert.assertFalse(model.checkGenerateUtplsqlTest)
		Assert.assertTrue(model.generateComments)
		Assert.assertFalse(model.disableTests)
		Assert.assertEquals("alltests", model.suitePath)
		Assert.assertEquals(3, model.indentSpaces)
		Assert.assertTrue(model.generateFiles)
		Assert.assertEquals(PreferenceModel.DEFAULT_OUTPUT_DIRECTORY, model.outputDirectory)
		Assert.assertEquals(false, model.deleteExistingFiles)
		Assert.assertEquals("utPLSQL", model.rootFolderInOddgenView)
	}
}
