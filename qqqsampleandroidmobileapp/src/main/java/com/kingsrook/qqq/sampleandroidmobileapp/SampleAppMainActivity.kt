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

package com.kingsrook.qqq.sampleandroidmobileapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kingsrook.qqq.frontend.android.core.model.Environment
import com.kingsrook.qqq.frontend.android.mobileapp.container.QAppContainer
import com.kingsrook.qqq.frontend.android.mobileapp.ui.authentication.QAuth0Service
import com.kingsrook.qqq.frontend.android.mobileapp.ui.authentication.QAuthenticationWrapper
import com.kingsrook.qqq.frontend.android.mobileapp.ui.horseshoe.QNavigationDrawer
import com.kingsrook.qqq.frontend.android.mobileapp.ui.horseshoe.QTopAppBar
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.QViewModel
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.TopLevelAppState
import com.kingsrook.qqq.sampleandroidmobileapp.ui.theme.AndroidDevProjectTheme


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/***************************************************************************
 **
 ***************************************************************************/
class SampleAppMainActivity : ComponentActivity()
{
   /***************************************************************************
    **
    ***************************************************************************/
   override fun onCreate(savedInstanceState: Bundle?)
   {
      super.onCreate(savedInstanceState);

      QAuth0Service.scheme = "qqqsampleapp";
      QAppContainer.availableEnvironments = listOf(
         Environment("Production", "https://live.coldtrack.com/"),
         Environment("Staging", "https://live.coldtrack-staging.com/"),
         Environment("Development", "https://live.coldtrack-dev.com/"),
         Environment("Local Dev", "http://192.168.4.70:8000/"),
         Environment("Bad URL", "http://no.such.domain.dot.dot/"),
      )

      enableEdgeToEdge()
      setContent()
      {
         // todo - this should come from the Container / DI (i think?)
         QViewModel.dataStore = LocalContext.current.dataStore;
         val qViewModel: QViewModel = viewModel(factory = QViewModel.factory)

         MainComposable(qViewModel)
      }
   }
}

/***************************************************************************
 **
 ***************************************************************************/
@Composable
fun MainComposable(qViewModel: QViewModel)
{
   AndroidDevProjectTheme()
   {
      val topLevelAppState = TopLevelAppState(coroutineScope = rememberCoroutineScope(), qViewModel = qViewModel)

      QNavigationDrawer(topLevelAppState)
      {
         Scaffold(
            Modifier.fillMaxSize(),
            topBar = { QTopAppBar(qViewModel, navMenuCallback = topLevelAppState::openNavDrawer) }
         ) { innerPadding ->
            Column(
               Modifier
                  .padding(innerPadding)
                  .fillMaxSize()
            )
            {
               QAuthenticationWrapper(
                  qViewModel,
                  Modifier
                     .padding(8.dp)
                     .fillMaxSize()
               )
            }
         }
      }

   }
}


