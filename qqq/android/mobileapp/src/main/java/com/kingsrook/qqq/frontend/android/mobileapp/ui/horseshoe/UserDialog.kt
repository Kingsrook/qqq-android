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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.QViewModel

/***************************************************************************
 **
 ***************************************************************************/
@Composable
fun UserDialog(qViewModel: QViewModel, isUserDialogOpen: MutableState<Boolean>)
{
   AlertDialog(
      icon = { Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "User Information") },
      title = { Text("User Information") },
      text = {
         Text("Current User:  ${qViewModel.sessionUserFullName}")
      },
      onDismissRequest = { isUserDialogOpen.value = false },
      dismissButton = { TextButton(onClick = { isUserDialogOpen.value = false }) { Text("Dismiss") } },
      confirmButton = {
         TextButton(onClick = {
            qViewModel.requestLogout();
            isUserDialogOpen.value = false
         }) { Text("Log Out") }
      }
   )
}