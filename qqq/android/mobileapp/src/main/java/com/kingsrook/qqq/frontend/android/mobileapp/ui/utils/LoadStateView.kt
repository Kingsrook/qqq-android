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

package com.kingsrook.qqq.frontend.android.mobileapp.ui.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kingsrook.qqq.frontend.android.mobileapp.R
import com.kingsrook.qqq.frontend.android.mobileapp.ui.common.FullScreenLoading
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.LoadState

/***************************************************************************
 ** Composable that
 ***************************************************************************/
@Composable
fun LoadStateView(
   loadStates: List<LoadState<*>>,
   modifier: Modifier = Modifier,
   reloader: Reloader? = null,
   loadingText: String = "Loading...",
   errorHeadline: String = "An Error Has Occurred",
   content: @Composable ColumnScope.() -> Unit
)
{

   if(LoadState.areAnyErrors(loadStates))
   {
      ///////////////////////////////////////////////////////
      // show errors, with reload button, if one was given //
      ///////////////////////////////////////////////////////
      LazyColumn(Modifier.padding(8.dp))
      {
         item()
         {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth())
            {
               Icon(
                  painter = painterResource(id = R.drawable.error_outline),
                  tint = Color.Red,
                  contentDescription = "Error",
                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(bottom = 40.dp, top = 80.dp)
                     .scale(4f)
               )
               Text(errorHeadline, fontWeight = FontWeight.SemiBold, fontSize = 24.sp, color = Color.Red, modifier = Modifier.padding(vertical = 16.dp))
            }
         }

         items(LoadState.getErrorMessages(loadStates))
         {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth())
            {
               Text(it, fontSize = 16.sp, modifier = Modifier.padding(bottom = 16.dp))
            }
         }

         if(reloader != null)
         {
            item()
            {
               Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth())
               {
                  Button(onClick = reloader.callback)
                  {
                     Text(reloader.buttonLabel)
                  }
               }
            }
         }
      }

   }
   else if(LoadState.areAnyLoading(loadStates))
   {
      //////////////////
      // show loading //
      //////////////////
      FullScreenLoading(loadingText)
   }
   else
   {
      ///////////////////////
      // show the content! //
      ///////////////////////
      Column(modifier = modifier, content = content)
   }
}

