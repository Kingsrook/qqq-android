/*
 * QQQ - Low-code Application Framework for Engineers.
 * Copyright (C) 2004-2024.  Kingsrook, LLC
 * 651 N Broad St Ste 205 # 6917 | Middletown DE 19709 | United States
 * contact@kingsrook.com
 * https://github.com/Kingsrook/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.kingsrook.qqq.sampleandroidmobileapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.kingsrook.qqq.frontend.android.core.model.metadata.QAppNodeType
import com.kingsrook.qqq.sampleandroidmobileapp.utils.TestUtils
import com.kingsrook.qqq.sampleandroidmobileapp.utils.clickAppScreenOption
import org.junit.Rule
import org.junit.Test

/***************************************************************************
 **
 ***************************************************************************/
class ProcessTests : BaseTest()
{
   @get:Rule
   val composeTestRule = createComposeRule()

   /***************************************************************************
    **
    ***************************************************************************/
   private fun atEndLocalDebugSleep()
   {
      TestUtils.localDebugSleep(composeTestRule, 5)
   }

   /***************************************************************************
    **
    ***************************************************************************/
   @Test
   fun testLaunchProcess()
   {
      TestUtils.startStandardTest(composeTestRule)
      TestUtils.login(composeTestRule)

      composeTestRule.clickAppScreenOption(QAppNodeType.APP, "app1")

      composeTestRule
         .onNodeWithTag("topAppBar.title")
         .assertTextContains("App 1")
         .assertIsDisplayed()
         .performClick()

      composeTestRule.clickAppScreenOption(QAppNodeType.PROCESS, "process1")

      composeTestRule
         .onNodeWithTag("topAppBar.title")
         .assertTextContains("Process 1")
         .assertIsDisplayed()
         .performClick()

      atEndLocalDebugSleep()
   }

}