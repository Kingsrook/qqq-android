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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.kingsrook.qqq.frontend.android.mobileapp.container.QAppContainer
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.QViewModel

/***************************************************************************
 **
 ***************************************************************************/
@Composable
fun MenuDialog(qViewModel: QViewModel, appTitle: String, isMenuDialogOpen: MutableState<Boolean>)
{
   val dropdownExpanded = remember { mutableStateOf(false) }
   val textFieldSize = remember { mutableStateOf(IntSize.Zero) }
   val options = QAppContainer.availableEnvironments;
   val selectedValue = remember { mutableStateOf(qViewModel.environment) }
   val isSwitching = remember { mutableStateOf(false) }
   val icon = if(dropdownExpanded.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

   AlertDialog(
      title = { Text(appTitle, modifier = Modifier.testTag("menuDialog.title")) },
      text = {
         LazyColumn()
         {
            ///////////////////////
            // current user name //
            ///////////////////////
            item()
            {
               Column(Modifier.padding(bottom = 16.dp))
               {
                  if(qViewModel.isAuthenticated)
                  {
                     Text("Current User:  ${qViewModel.sessionUserFullName}")
                  }
                  else
                  {
                     Text("Not currently logged in")
                  }

                  Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth())
                  {
                     TextButton(
                        onClick =
                        {
                           qViewModel.requestLogout()
                           isMenuDialogOpen.value = false
                        }, enabled = qViewModel.isAuthenticated
                     ) { Text("Log Out") }
                  }

                  HorizontalDivider()
               }
            }

            //////////////////////////
            // environment dropdown //
            //////////////////////////
            item()
            {
               Column(Modifier.padding(bottom = 16.dp))
               {
                  Text("Environment:", modifier = Modifier.padding(bottom = 8.dp))

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

                  Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth())
                  {
                     TextButton(
                        onClick = {
                           if(selectedValue.value != qViewModel.environment)
                           {
                              isSwitching.value = true
                              qViewModel.switchEnvironment(selectedValue.value)
                              {
                                 isMenuDialogOpen.value = false
                              };
                           }
                           else
                           {
                              isMenuDialogOpen.value = false
                           }
                        },
                        enabled = (selectedValue.value != qViewModel.environment && !isSwitching.value)
                     ) { Text(if(isSwitching.value) "Switching..." else "Switch") }
                  }

                  HorizontalDivider()
               }
            }

            item()
            {
               Column(Modifier.padding(bottom = 16.dp))
               {
                  Text("App version:  0.0.1", modifier = Modifier.padding(bottom = 16.dp)) // todo wip
                  HorizontalDivider()
               }
            }
         }
      },
      onDismissRequest = { isMenuDialogOpen.value = false },
      dismissButton = { TextButton(onClick = { isMenuDialogOpen.value = false }) { Text("Dismiss") } },
      confirmButton = { }
   )
}