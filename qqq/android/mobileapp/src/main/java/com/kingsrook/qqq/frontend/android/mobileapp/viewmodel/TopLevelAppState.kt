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

package com.kingsrook.qqq.frontend.android.mobileapp.viewmodel

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/***************************************************************************
 **
 ***************************************************************************/
class TopLevelAppState(
   private val coroutineScope: CoroutineScope,
   private val qViewModel: QViewModel
)
{
   var navDrawerState: DrawerState by mutableStateOf(DrawerState(DrawerValue.Closed))
      private set

   /***************************************************************************
    **
    ***************************************************************************/
   fun openNavDrawer()
   {
      coroutineScope.launch()
      {
         navDrawerState.open()
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun closeNavDrawer()
   {
      coroutineScope.launch()
      {
         navDrawerState.close()
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun logOut()
   {
      this.qViewModel.requestLogout()
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun isLoggedIn(): Boolean
   {
      return this.qViewModel.isAuthenticated
   }

}