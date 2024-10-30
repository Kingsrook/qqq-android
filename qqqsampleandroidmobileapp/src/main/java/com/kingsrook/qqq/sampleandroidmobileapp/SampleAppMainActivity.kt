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

package com.kingsrook.qqq.sampleandroidmobileapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kingsrook.qqq.frontend.android.core.model.Environment
import com.kingsrook.qqq.frontend.android.mobileapp.QMobileAppMain
import com.kingsrook.qqq.frontend.android.mobileapp.container.QAppContainer
import com.kingsrook.qqq.frontend.android.mobileapp.ui.authentication.QAuth0Service
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.QViewModel
import com.kingsrook.qqq.sampleandroidmobileapp.ui.theme.AppTheme

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
      /////////////////////////////////////////////////////////////////////////////////////////////
      // configure your application:                                                             //
      // title: text displayed in topAppBar                                                      //
      // scheme: unique identifier for your app, used by URL-scheme-based callbacks              //
      // availableEnvironments: list of Environments - labels & Urls of your application servers //
      // applicationName: name sent to your backend server to identify this frontend application //
      // applicationVersion: version that accompanies applicationName.                           //
      /////////////////////////////////////////////////////////////////////////////////////////////
      val title = "Sample App"
      QAuth0Service.scheme = "qqqsampleapp"
      QAppContainer.availableEnvironments = listOf(
         Environment("Production", "https://live.coldtrack.com/"),
         Environment("Staging", "https://live.coldtrack-staging.com/"),
         Environment("Development", "https://live.coldtrack-dev.com/"),
         Environment("Local Dev", "http://192.168.4.122:8000/"),
         Environment("Bad URL", "http://no.such.domain.dot.dot/"),
      )
      QViewModel.applicationName = "qqqSampleApp"
      QViewModel.applicationVersion = "0.1"
      val splashResourceId: Int? = null // R.drawable.coldtrack_logo_icon_gradient_800

      ///////////////////////////////////////////////////////////
      // boilerplate - paste in below your app's configuration //
      ///////////////////////////////////////////////////////////
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContent()
      {
         QViewModel.dataStore = LocalContext.current.dataStore
         val qViewModel: QViewModel = viewModel(factory = QViewModel.factory)

         AppTheme()
         {
            QMobileAppMain(qViewModel, title = title, if(splashResourceId == null) null else painterResource(splashResourceId))
         }
      }
   }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
