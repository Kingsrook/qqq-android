/*
 * QQQ - Low-code Application Framework for Engineers.
 * Copyright (C) 2024-2024.  Kingsrook, LLC
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
 */

package com.kingsrook.qqq.frontend.android.mobileapp.ui.horseshoe

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kingsrook.qqq.frontend.android.core.model.metadata.QInstance
import com.kingsrook.qqq.frontend.android.mobileapp.ui.utils.LoadStateView
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.LoadState
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.QViewModel

/***************************************************************************
 ** Composable that wraps our entire application - providing a login form
 ** when needed, else showing the app.
 ***************************************************************************/
@Composable
fun QApplicationWrapper(qViewModel: QViewModel, modifier: Modifier = Modifier)
{
   val qInstanceLoadState = qViewModel.qInstanceLoadState;
   val loadStates = listOf(qInstanceLoadState);

   LoadStateView(loadStates,
      modifier,
      loadingText = "Loading apps for environment: ${qViewModel.environment.label}")
   {
      val qInstance = (qInstanceLoadState as LoadState.Success<QInstance>).value
      Column(modifier = Modifier)
      {
         Text(
            "${qInstance.branding?.appName}",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp).testTag("qInstance.branding.appName")
         )

         // Column(modifier = modifier, content = content)

         LazyColumn()
         {
            items(qInstance.appTree) { app ->
               Text(
                  app.label,
                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(8.dp)
                     .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                     .padding(24.dp)
               )
            }
         }
      }
   }
}

