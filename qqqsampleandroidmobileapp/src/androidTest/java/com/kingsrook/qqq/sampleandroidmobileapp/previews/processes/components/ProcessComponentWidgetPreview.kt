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

package com.kingsrook.qqq.sampleandroidmobileapp.previews.processes.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kingsrook.qqq.sampleandroidmobileapp.SampleAppMockQQQRepository
import com.kingsrook.qqq.frontend.android.core.model.metadata.QComponentType
import com.kingsrook.qqq.frontend.android.core.model.metadata.QFrontendComponent
import com.kingsrook.qqq.frontend.android.core.model.metadata.QInstance
import com.kingsrook.qqq.frontend.android.mobileapp.ui.processes.components.ProcessComponentWidget
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.ProcessViewModel

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ProcessComponentWidgetPreview()
{
   val processViewModel = ProcessViewModel()
   processViewModel.qqqRepository = SampleAppMockQQQRepository()
   val component = QFrontendComponent(type = QComponentType.WIDGET, values = mapOf(
      "isAdHocWidget" to true,
      "blocks" to listOf(
         mapOf(
            "blockType" to "TEXT",
            "styles" to mapOf(
               "foo" to "bar"
            ),
            "values" to mapOf(
               "text" to "this is my text, what do you want from me?"
            ),
         )
      )
   ))
   ProcessComponentWidget(processViewModel, QInstance(),  component)
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ProcessComponentWidgetPreviewMultipleAdHoc()
{
   val processViewModel = ProcessViewModel()
   processViewModel.qqqRepository = SampleAppMockQQQRepository()
   val component = QFrontendComponent(type = QComponentType.WIDGET, values = mapOf(
      "isAdHocWidget" to true,
      "blocks" to listOf(
         mapOf(
            "blockType" to "TEXT",
            "styles" to mapOf(
               "standardColor" to "SUCCESS"
            ),
            "values" to mapOf(
               "text" to "this is my text, what do you want from me?"
            ),
         ),
         mapOf(
            "blockType" to "TEXT",
            "values" to mapOf(
               "text" to "and this is mine."
            ),
         )
      )
   ))
   ProcessComponentWidget(processViewModel, QInstance(), component)
}


/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ProcessComponentWidgetAdHocUnknownType()
{
   val processViewModel = ProcessViewModel()
   processViewModel.qqqRepository = SampleAppMockQQQRepository()
   val component = QFrontendComponent(type = QComponentType.WIDGET, values = mapOf(
      "isAdHocWidget" to true,
      "blocks" to listOf(
         mapOf(
            "blockType" to "FOOBAR"
         )
      )
   ))
   ProcessComponentWidget(processViewModel, QInstance(), component)
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ProcessComponentWidgetPreviewNonAdHoc()
{
   val processViewModel = ProcessViewModel()
   processViewModel.qqqRepository = SampleAppMockQQQRepository()
   val component = QFrontendComponent(type = QComponentType.WIDGET, values = mapOf(
      "widgetName" to "testWidget"
   ))
   ProcessComponentWidget(processViewModel, QInstance(), component)
}

