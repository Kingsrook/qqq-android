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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import com.kingsrook.qqq.frontend.android.mobileapp.ui.QNavigator
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.QViewModel

/***************************************************************************
 **
 ***************************************************************************/
@OptIn(ExperimentalMaterial3Api::class) // needed for TopAppBar
@Composable
fun QTopAppBar(qViewModel: QViewModel, title: String = "A QQQ Android Application", navMenuCallback: (() -> Unit)? = null, qNavigator: QNavigator? = null)
{
   val isUserDialogOpen = remember { mutableStateOf(false) }
   val isEnvironmentDialogOpen = remember { mutableStateOf(false) }

   TopAppBar(
      colors = TopAppBarDefaults.topAppBarColors(
         containerColor = MaterialTheme.colorScheme.primaryContainer,
         titleContentColor = MaterialTheme.colorScheme.primary,
      ),
      title = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
      navigationIcon =
      {
         /////////////////////////////////////////////////////////
         // this was for the left-menu - but - not using today! //
         /////////////////////////////////////////////////////////
         // navMenuCallback?.let()
         // {
         //    IconButton(onClick = { it() })
         //    {
         //       Icon(imageVector = Icons.Filled.Menu, contentDescription = "Open Menu")
         //    }
         // }

         key(qNavigator?.navDepth ?: 0)
         {
            qNavigator?.let()
            {
               IconButton(onClick = { qNavigator.popStack() }, enabled = !qNavigator.atHome)
               {
                  if(qNavigator.atHome)
                  {
                     Icon(imageVector = Icons.Filled.Home, contentDescription = "At Home", tint = Color.Black)
                  }
                  else
                  {
                     Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
                  }
               }
            }
         }
      },
      actions =
      {
         IconButton(onClick = { isUserDialogOpen.value = true }, Modifier.testTag("topBar.userIcon"))
         {
            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "User Information")
         }

         IconButton(onClick = { isEnvironmentDialogOpen.value = true }, Modifier.testTag("topBar.environmentIcon"))
         {
            Icon(imageVector = Icons.Default.Place, contentDescription = "App Environment")
         }
      }
   )

   if(isUserDialogOpen.value)
   {
      UserDialog(qViewModel, isUserDialogOpen)
   }

   if(isEnvironmentDialogOpen.value)
   {
      EnvironmentDialog(qViewModel, isEnvironmentDialogOpen)
   }

}

