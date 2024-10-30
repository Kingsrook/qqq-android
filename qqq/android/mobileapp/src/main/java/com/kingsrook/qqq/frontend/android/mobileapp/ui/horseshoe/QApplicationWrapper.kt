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

package com.kingsrook.qqq.frontend.android.mobileapp.ui.horseshoe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kingsrook.qqq.frontend.android.core.model.metadata.QInstance
import com.kingsrook.qqq.frontend.android.mobileapp.ui.QNavigator
import com.kingsrook.qqq.frontend.android.mobileapp.ui.apps.QAppHome
import com.kingsrook.qqq.frontend.android.mobileapp.ui.common.FullSizedAllCenteredColumn
import com.kingsrook.qqq.frontend.android.mobileapp.ui.processes.QProcessHome
import com.kingsrook.qqq.frontend.android.mobileapp.ui.utils.LoadStateView
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.LoadState
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.QViewModel
import kotlinx.serialization.Serializable

/***************************************************************************
 ** Composable that wraps our entire application - providing a login form
 ** when needed, else showing the app.
 ***************************************************************************/
@Composable
fun QApplicationWrapper(qViewModel: QViewModel, modifier: Modifier = Modifier, qNavigator: QNavigator? = null)
{
   val qInstanceLoadState = qViewModel.qInstanceLoadState
   val loadStates = listOf(qInstanceLoadState)

   val navController = qNavigator?.navController ?: rememberNavController()

   LoadStateView(
      loadStates,
      modifier,
      loadingText = "Loading apps for environment: ${qViewModel.environment.label}"
   )
   {
      val qInstance = (qInstanceLoadState as LoadState.Success<QInstance>).value
      Column(modifier = Modifier)
      {
         NavHost(navController, startDestination = HomeRoute, modifier = modifier) {

            composable<HomeRoute>()
            {
               QAppHome(qInstance, modifier, qNavigator = qNavigator)
            }

            composable<AppRoute>()
            { backStackEntry ->
               val appRoute: AppRoute = backStackEntry.toRoute()
               val app = qInstance.apps?.get(appRoute.name)
               if(app != null)
               {
                  QAppHome(qInstance, modifier, app = app, qNavigator = qNavigator)
               }
               else
               {
                  NotFound(qNavigator, "App not found (name=${appRoute.name})")
               }
            }

            composable<ProcessRoute>()
            { backStackEntry ->
               val processRoute: ProcessRoute = backStackEntry.toRoute()
               val process = qInstance.processes?.get(processRoute.name)
               if(process != null)
               {
                  QProcessHome(qViewModel, qInstance, modifier, lightProcess = process, qNavigator = qNavigator)
               }
               else
               {
                  NotFound(qNavigator, "Process not found (name=${processRoute.name})")
               }
            }

         }
      }
   }
}

/***************************************************************************
 **
 ***************************************************************************/
@Composable
private fun NotFound(qNavigator: QNavigator?, message: String)
{
   FullSizedAllCenteredColumn()
   {
      Text(message, modifier = Modifier.padding(bottom = 16.dp))
      Button(onClick = { qNavigator?.goHome() })
      {
         Text("Return Home")
      }
   }
}

/***************************************************************************
 ** route/data (singleton) to represent the home screen
 ***************************************************************************/
@Serializable
object HomeRoute

/***************************************************************************
 ** route/data to represent an app
 ***************************************************************************/
@Serializable
data class AppRoute(val name: String)

/***************************************************************************
 ** route/data to represent a process
 ***************************************************************************/
@Serializable
data class ProcessRoute(val name: String)

