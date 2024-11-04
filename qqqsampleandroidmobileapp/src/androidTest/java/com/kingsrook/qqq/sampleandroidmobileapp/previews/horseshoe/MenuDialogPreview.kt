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

package com.kingsrook.qqq.sampleandroidmobileapp.previews.horseshoe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.kingsrook.qqq.frontend.android.mobileapp.ui.horseshoe.MenuDialog
import com.kingsrook.qqq.sampleandroidmobileapp.previews.utils.PreviewUtils

/***************************************************************************
 **
 ***************************************************************************/
@Preview(widthDp = 400)
@Composable
fun MenuDialogPreviewNormal()
{
   val qViewModel = PreviewUtils.createQViewModel().also()
   {
      it.logInSuccessful()
      it.sessionUserFullName = "John Doekhoff"
   }
   val isOpen = remember { mutableStateOf(true) }

   MenuDialog(qViewModel, "A Preview", isOpen)
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview(widthDp = 400)
@Composable
fun MenuDialogPreviewSuperLongName()
{
   val qViewModel = PreviewUtils.createQViewModel().also()
   {
      it.logInSuccessful()
      it.sessionUserFullName = "Testasuperlongnameandwheredoesitwraandwheredoesitwrappandwheredoesitwrap?"
   }
   val isOpen = remember { mutableStateOf(true) }

   MenuDialog(qViewModel, "A Preview", isOpen)
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview(widthDp = 400)
@Composable
fun MenuDialogPreviewNotLoggedIn()
{
   val qViewModel = PreviewUtils.createQViewModel()
   val isOpen = remember { mutableStateOf(true) }

   MenuDialog(qViewModel, "A Preview", isOpen)
}