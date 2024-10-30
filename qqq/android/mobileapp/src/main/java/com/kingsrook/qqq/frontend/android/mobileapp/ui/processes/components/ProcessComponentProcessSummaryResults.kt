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

package com.kingsrook.qqq.frontend.android.mobileapp.ui.processes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.kingsrook.qqq.frontend.android.core.model.metadata.QFrontendComponent
import com.kingsrook.qqq.frontend.android.mobileapp.ui.utils.Colors
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.ProcessViewModel
import java.lang.String.format

/***************************************************************************
 ** TODO WIP - very not ready...
 ***************************************************************************/
@OptIn(ExperimentalLayoutApi::class) // for FlowRow
@Composable
fun ProcessComponentProcessSummaryResults(processViewModel: ProcessViewModel, component: QFrontendComponent)
{
   val icon = processViewModel.processMetaData?.icon
   val processResults = processViewModel.processValues["processResults"]
   val recordCount = processViewModel.processValues["recordCount"]

   Column()
   {
      // Text(
      //    "${processViewModel.processValues}",
      //    modifier = Modifier
      //       .fillMaxWidth()
      //       .padding(vertical = 12.dp)
      // )

      Row(Modifier.background(color = Colors.SUCCESS, RoundedCornerShape(4.dp)))
      {
         if(icon != null)
         {
            Text("Icon [${icon.name}]", color = Color.White)
         }

         Text("Process Summary", color = Color.White)
      }

      if(recordCount != null && recordCount is Int)
      {
         Text("${format("%,d", recordCount)} ${if(recordCount == 1) "record was" else "records were"} processed")
      }

      if(processResults != null && processResults is List<*>)
      {
         processResults.forEach()
         { line ->
            if(line is Map<*, *>)
            {
               FlowRow()
               {
                  when (line["status"])
                  {
                     "OK" -> Text("Icon [OK or →]", color = Colors.SUCCESS)
                     "INFO" -> Text("Icon [i]", color = Colors.INFO)
                     "WARNING" -> Text("Icon [Warn]", color = Colors.WARNING)
                     "ERROR" -> Text("Icon [Err]", color = Colors.ERROR)
                     else -> Box {}
                  }

                  Text("${format("%,d", line["count"])} ${line["message"]}")
               }
            }
         }
      }
   }
}