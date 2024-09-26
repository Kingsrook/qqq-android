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

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.kingsrook.qqq.frontend.android.mobileapp.container.QAppContainer
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.QViewModel

const val TAG = "EnvironmentDialog"

/***************************************************************************
 **
 ***************************************************************************/
@Composable
fun EnvironmentDialog(qViewModel: QViewModel, isEnvironmentDialogOpen: MutableState<Boolean>)
{
   val dropdownExpanded = remember { mutableStateOf(false) }
   val textFieldSize = remember { mutableStateOf(IntSize.Zero) }

   val options = QAppContainer.availableEnvironments;
   val selectedValue = remember { mutableStateOf(qViewModel.environment) }
   val isSwitching = remember { mutableStateOf(false) }

   val icon = if(dropdownExpanded.value)
      Icons.Filled.KeyboardArrowUp
   else
      Icons.Filled.KeyboardArrowDown

   AlertDialog(
      icon = { Icon(imageVector = Icons.Default.Place, contentDescription = "App Environment") },
      title = { Text("App Environment") },
      text = {

         Column()
         {
            Box(
               Modifier
                  .fillMaxWidth()
                  .clickable { dropdownExpanded.value = !dropdownExpanded.value }
            ) {
               Row(
                  horizontalArrangement = Arrangement.SpaceBetween,
                  modifier = Modifier
                     .fillMaxWidth()
                     .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(4.dp))
                     .padding(16.dp)
                     .onGloballyPositioned { coordinates -> textFieldSize.value = coordinates.size },
               )
               {
                  Text(text = selectedValue.value.label)
                  Icon(icon, if(dropdownExpanded.value) "close" else "open")
               }

               DropdownMenu(
                  expanded = dropdownExpanded.value,
                  onDismissRequest = { dropdownExpanded.value = false },
                  Modifier.width((textFieldSize.value.width.toFloat() / LocalDensity.current.density).dp)
               )
               {
                  options.forEach()
                  { environment ->
                     DropdownMenuItem(
                        onClick =
                        {
                           selectedValue.value = environment
                           dropdownExpanded.value = false
                        },
                        text = { Text(text = environment.label) }
                     )
                  }
               }
            }
         }

      },
      onDismissRequest = {
         if(isSwitching.value)
         {
            Log.d(TAG, "Request to dismiss, but we are currently switching, so noop")
            return@AlertDialog;
         }

         isEnvironmentDialogOpen.value = false
      },
      dismissButton = { TextButton(onClick = { isEnvironmentDialogOpen.value = false }) { Text("Dismiss") } },
      confirmButton = {
         TextButton(
            onClick = {
               if(selectedValue.value != qViewModel.environment)
               {
                  isSwitching.value = true
                  qViewModel.switchEnvironment(selectedValue.value)
                  {
                     isEnvironmentDialogOpen.value = false
                  };
               }
               else
               {
                  isEnvironmentDialogOpen.value = false
               }
            },
            enabled = (selectedValue.value != qViewModel.environment && !isSwitching.value)
         ) { Text(if(isSwitching.value) "Switching..." else "Switch") }
      }
   )
}