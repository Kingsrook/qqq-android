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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kingsrook.qqq.frontend.android.mobileapp.R
import com.kingsrook.qqq.frontend.android.mobileapp.container.QAppContainer
import com.kingsrook.qqq.frontend.android.mobileapp.ui.utils.Dropdown
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.QViewModel

/***************************************************************************
 **
 ***************************************************************************/
@Composable
fun MenuDialog(qViewModel: QViewModel, appTitle: String, isMenuDialogOpen: MutableState<Boolean>)
{
   val isSwitchingEnvironment = remember { mutableStateOf(false) }
   val selectedEnvironment = remember { mutableStateOf(qViewModel.environment) }

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

                  Dropdown(value = qViewModel.environment, options = QAppContainer.availableEnvironments, labelExtractor = { it.label }, onClick = { selectedEnvironment.value = it })

                  Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth())
                  {
                     TextButton(
                        onClick = {
                           if(selectedEnvironment.value != qViewModel.environment)
                           {
                              isSwitchingEnvironment.value = true
                              qViewModel.switchEnvironment(selectedEnvironment.value)
                              {
                                 isMenuDialogOpen.value = false
                              }
                           }
                           else
                           {
                              isMenuDialogOpen.value = false
                           }
                        },
                        enabled = (selectedEnvironment.value != qViewModel.environment && !isSwitchingEnvironment.value)
                     ) { Text(if(isSwitchingEnvironment.value) "Switching..." else "Switch") }
                  }

                  HorizontalDivider()
               }
            }

            //////////////////
            // Reset button //
            //////////////////
            item()
            {
               Column(Modifier.padding(bottom = 16.dp))
               {
                  Text("In case of issues, you may try:")

                  Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth())
                  {
                     TextButton(
                        onClick =
                        {
                           qViewModel.resetApp()
                           isMenuDialogOpen.value = false
                        }
                     ) { Text("Reset App") }
                  }

                  HorizontalDivider()
               }
            }

            /////////////////
            // app version //
            /////////////////
            item()
            {
               Column(Modifier.padding(bottom = 16.dp))
               {
                  Text("Application version:  ${QViewModel.applicationVersion}")
                  if(QViewModel.applicationBuildTimestamp != null)
                  {
                     Text("Built at:  ${QViewModel.applicationBuildTimestamp}")
                  }
                  Text("QQQ Android version:  ${stringResource(R.string.qqq_android_version)}")
                  HorizontalDivider(Modifier.padding(top = 16.dp))
               }
            }
         }
      },
      onDismissRequest = { isMenuDialogOpen.value = false },
      dismissButton = { TextButton(onClick = { isMenuDialogOpen.value = false }) { Text("Dismiss") } },
      confirmButton = { }
   )
}