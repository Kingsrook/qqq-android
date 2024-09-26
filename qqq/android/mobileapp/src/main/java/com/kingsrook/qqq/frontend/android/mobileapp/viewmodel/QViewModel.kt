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

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kingsrook.qqq.frontend.android.core.controllers.QQQRepository
import com.kingsrook.qqq.frontend.android.core.model.Environment
import com.kingsrook.qqq.frontend.android.core.model.metadata.QInstance
import com.kingsrook.qqq.frontend.android.core.model.metadata.QProcessMetaData
import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.BaseAuthenticationMetaData
import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.ManageSessionRequest
import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.ManageSessionResponse
import com.kingsrook.qqq.frontend.android.core.utils.firstN
import com.kingsrook.qqq.frontend.android.mobileapp.QMobileApplication
import com.kingsrook.qqq.frontend.android.mobileapp.container.QAppContainer
import com.kingsrook.qqq.frontend.android.mobileapp.data.SettingsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


private const val TAG = "QViewModel"

/***************************************************************************
 **
 ***************************************************************************/
open class QViewModel(
   private val qqqRepository: QQQRepository,
   private val dataStore: DataStore<Preferences>
) : ViewModel()
{
   private var settingsRepository: SettingsRepository = SettingsRepository(dataStore)

   var initialLoadFromDataStoreLoadState: LoadState<Unit> by mutableStateOf(LoadState.Loading(Unit))
      private set

   var isAuthenticated: Boolean by mutableStateOf(false)
      private set

   var isLogoutRequested: Boolean by mutableStateOf(false)
      private set

   var sessionUUID: String by mutableStateOf("")
      private set

   private var storedSessionUUIDLoadState: LoadState<String> by mutableStateOf(LoadState.Loading(Unit))
      private set

   var sessionUserFullName: String by mutableStateOf("")

   private var storedSessionUserFullName: LoadState<String> by mutableStateOf(LoadState.Loading(Unit))

   private var storedEnvironmentLoadState: LoadState<Environment> by mutableStateOf(LoadState.Loading(Unit))

   private var defaultEnvironment = QAppContainer.availableEnvironments[0];

   var environment: Environment by mutableStateOf(defaultEnvironment)
      private set

   var authenticationMetaDataLoadState: LoadState<BaseAuthenticationMetaData> by mutableStateOf(LoadState.Loading(Unit))
      private set

   var manageSessionLoadState: LoadState<ManageSessionResponse> by mutableStateOf(LoadState.Loading(Unit))
      private set

   var qInstanceLoadState: LoadState<QInstance> by mutableStateOf(LoadState.Loading(Unit))
      private set

   var qProcessMetaDataLoadState: Map<String, LoadState<QProcessMetaData>> by mutableStateOf(emptyMap())
      private set

   init
   {
      viewModelScope.launch()
      {
         doInitialLoadFromDataStoreV2().join()
         loadAuthenticationMetaData().join()
         tryLoginFromDataStore().join()
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun reInitAfterFailure()
   {
      clearAll()
      loadAuthenticationMetaData()
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun clearAll()
   {
      ////////////////////////////////////////////
      // reset all the things to initial values //
      ////////////////////////////////////////////
      // storedSessionDance = LoadState.Loading(Unit) // well, not this one - it's only for our very first load.
      isAuthenticated = false
      isLogoutRequested = false
      sessionUUID = ""
      storedSessionUUIDLoadState = LoadState.Loading(Unit)
      sessionUserFullName = ""
      storedSessionUserFullName = LoadState.Loading(Unit)
      storedEnvironmentLoadState = LoadState.Loading(Unit)
      authenticationMetaDataLoadState = LoadState.Loading(Unit)
      manageSessionLoadState = LoadState.Loading(Unit)
      qInstanceLoadState = LoadState.Loading(Unit)
      qProcessMetaDataLoadState = emptyMap()
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun loadAuthenticationMetaData(): Job
   {
      return viewModelScope.launch()
      {
         authenticationMetaDataLoadState = try
         {
            LoadState.Success(qqqRepository.getAuthenticationMetaData())
         }
         catch(e: Exception)
         {
            Log.w(TAG, "Error loading Authentication MetaData", e)
            LoadState.Error(e.message ?: "Error loading Authentication MetaData")
         }
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   open fun manageSession(accessToken: String)
   {
      val outerThis = this
      viewModelScope.launch()
      {
         try
         {
            Log.d(TAG, "managing session with an access token from login flow: ${accessToken.firstN(8)}")

            val manageSessionLoadState = LoadState.Success(qqqRepository.manageSession(ManageSessionRequest(accessToken = accessToken, uuid = null)))
            sessionUUID = manageSessionLoadState.value.uuid
            qqqRepository.setSessionUUID(sessionUUID)
            loadQInstance()
            logInSuccessful()

            outerThis.manageSessionLoadState = manageSessionLoadState
         }
         catch(e: Exception)
         {
            Log.w(TAG, "Error managing session", e)
            outerThis.manageSessionLoadState = LoadState.Error(e.message ?: "Error managing session")
         }
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun logInFailed()
   {
      isAuthenticated = false;

      ///////////////////////////////////////////////////
      // make sure we don't have a sessionUUID stored. //
      ///////////////////////////////////////////////////
      storeSessionUUID("")
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun logInSuccessful()
   {
      isAuthenticated = true;
      storeSessionUUID(sessionUUID)
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun requestLogout()
   {
      isLogoutRequested = true
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun requestLogin()
   {
      isLogoutRequested = false
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun doInitialLoadFromDataStore()
   {
      viewModelScope.launch()
      {
         ////////////////////////////////////////
         // load the session UUID from storage //
         ////////////////////////////////////////
         Log.d(TAG, "starting initialLoadFromDataStore; waiting for loadStoredSessionUUID()")

         viewModelScope.launch()
         {
            loadStoredSessionUUID()
            loadStoredSessionUserFullName()
         }.join()

         when (storedSessionUUIDLoadState)
         {
            is LoadState.Success<String> ->
            {
               Log.d(TAG, "loadStoredSessionUUID was success")
               sessionUUID = (storedSessionUUIDLoadState as LoadState.Success<String>).value

               if(sessionUUID == "")
               {
                  Log.d(TAG, "loadStoredSessionUUID was empty - no more dance")
               }
               else
               {
                  Log.d(TAG, "loadStoredSessionUUID was set - trying to load instance now")
                  qqqRepository.setSessionUUID(sessionUUID);
                  loadQInstance().join()

                  when (qInstanceLoadState)
                  {
                     is LoadState.Success<QInstance> ->
                     {
                        loadStoredSessionUserFullName().join()

                        sessionUserFullName = when (val it = storedSessionUserFullName)
                        {
                           is LoadState.Success<String> -> it.value
                           else -> "Unknown"
                        }

                        Log.d(TAG, "qInstanceLoadState was success (${qInstanceLoadState}).  marking login success!")
                        logInSuccessful()
                     }

                     else ->
                     {
                        qInstanceLoadState = LoadState.Loading(Unit)
                        Log.d(TAG, "qInstanceLoadState was not success (${qInstanceLoadState}).  marking login failed.")
                        logInFailed()
                     }
                  }
               }
            }

            else ->
            {
               ///////////
               // noop. //
               ///////////
               Log.d(TAG, "loadStoredSessionUUID was not success (${storedSessionUUIDLoadState}).  ending dance")
            }
         }

         initialLoadFromDataStoreLoadState = LoadState.Success(Unit)
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun doInitialLoadFromDataStoreV2(): Job
   {
      return viewModelScope.launch()
      {
         ////////////////////////////////////////
         // load the session UUID from storage //
         ////////////////////////////////////////
         Log.d(TAG, "starting initialLoadFromDataStore;")

         val job1 = loadStoredSessionUUID();
         val job2 = loadStoredSessionUserFullName();
         val job3 = loadStoredEnvironmentOrDefault();

         job1.join()
         job2.join()
         job3.join()

         Log.d(TAG, "past initialLoadFromDataStore;")
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun tryLoginFromDataStore(): Job
   {
      return viewModelScope.launch()
      {
         when (storedSessionUUIDLoadState)
         {
            is LoadState.Success<String> ->
            {
               Log.d(TAG, "loadStoredSessionUUID was success")
               sessionUUID = (storedSessionUUIDLoadState as LoadState.Success<String>).value

               if(sessionUUID == "")
               {
                  Log.d(TAG, "loadStoredSessionUUID was empty - no more dance")
                  logInFailed()
               }
               else
               {
                  Log.d(TAG, "loadStoredSessionUUID was set - trying to load instance now")
                  qqqRepository.setSessionUUID(sessionUUID);
                  loadQInstance().join()

                  when (qInstanceLoadState)
                  {
                     is LoadState.Success<QInstance> ->
                     {
                        loadStoredSessionUserFullName().join()

                        sessionUserFullName = when (val it = storedSessionUserFullName)
                        {
                           is LoadState.Success<String> -> it.value
                           else -> "Unknown"
                        }

                        Log.d(TAG, "qInstanceLoadState was success (${qInstanceLoadState}).  marking login success!")
                        logInSuccessful()
                     }

                     else ->
                     {
                        qInstanceLoadState = LoadState.Loading(Unit)
                        Log.d(TAG, "qInstanceLoadState was not success (${qInstanceLoadState}).  marking login failed.")
                        logInFailed()
                     }
                  }
               }
            }

            else ->
            {
               ///////////
               // noop. //
               ///////////
               Log.d(TAG, "loadStoredSessionUUID was not success (${storedSessionUUIDLoadState}).  ending dance")
            }
         }

         initialLoadFromDataStoreLoadState = LoadState.Success(Unit)
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun loadStoredSessionUUID(): Job
   {
      return viewModelScope.launch()
      {
         storedSessionUUIDLoadState = try
         {
            LoadState.Success(settingsRepository.getSessionUUID())
         }
         catch(e: Exception)
         {
            Log.w(TAG, "Error loading stored session UUID", e)
            LoadState.Error(e.message ?: "Error loading stored session UUID")
         }
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun loadStoredEnvironmentOrDefault(): Job
   {
      return viewModelScope.launch()
      {
         try
         {
            var env: Environment? = settingsRepository.getEnvironment()

            if(env == null)
            {
               Log.d(TAG, "got null environment from settings repo.  Using default.")
               env = defaultEnvironment
            }
            else
            {
               Log.d(TAG, "got environment ${env} from settings repo.")
            }

            activateEnvironment(env).join()
         }
         catch(e: Exception)
         {
            Log.w(TAG, "Error loading stored environment", e)
            storedEnvironmentLoadState = LoadState.Error(e.message ?: "Error loading stored environment")
         }
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun activateEnvironment(environment: Environment): Job
   {
      return viewModelScope.launch()
      {
         this@QViewModel.environment = environment
         qqqRepository.setEnvironment(environment)
         settingsRepository.setEnvironment(environment)
         storedEnvironmentLoadState = LoadState.Success(environment)
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun switchEnvironment(environment: Environment, postSwitchCallback: () -> Unit)
   {
      viewModelScope.launch()
      {
         activateEnvironment(environment).join()
         logOut().join()
         loadAuthenticationMetaData().join()
         postSwitchCallback.invoke()
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun loadStoredSessionUserFullName(): Job
   {
      return viewModelScope.launch()
      {
         storedSessionUserFullName = try
         {
            LoadState.Success(settingsRepository.getSessionUserFullName())
         }
         catch(e: Exception)
         {
            Log.w(TAG, "Error loading stored session user full name", e)
            LoadState.Error(e.message ?: "Error loading stored session user full name")
         }
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun storeSessionUUID(value: String)
   {
      viewModelScope.launch()
      {
         Log.d(TAG, "Storing session UUID: [${value}]")
         settingsRepository.setSessionUUID(value)
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun doSetSessionUserFullName(value: String)
   {
      sessionUserFullName = value;
      viewModelScope.launch()
      {
         settingsRepository.setSessionUserFullName(value)
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun logOut(): Job
   {
      return viewModelScope.launch()
      {
         val capturedIsLogoutRequested = isLogoutRequested
         val capturedAuthLoadState = authenticationMetaDataLoadState

         viewModelScope.launch()
         {
            isAuthenticated = false
            sessionUUID = ""
            qqqRepository.setSessionUUID(sessionUUID)
            settingsRepository.setSessionUUID(sessionUUID)
         }.join()

         clearAll();

         ////////////////////////////////////////
         // restore these 2 after the clear... //
         ////////////////////////////////////////
         isLogoutRequested = capturedIsLogoutRequested
         authenticationMetaDataLoadState = capturedAuthLoadState
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun loadQInstance(): Job
   {
      return viewModelScope.launch()
      {
         qInstanceLoadState = try
         {
            val value = qqqRepository.getMetaData()
            LoadState.Success(value)
         }
         catch(e: Exception)
         {
            Log.w(TAG, "Error loading QInstance", e)
            LoadState.Error(e.message ?: "Error loading QInstance")
         }
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun loadQProcessMetaData(processName: String)
   {
      if(qProcessMetaDataLoadState.containsKey(processName))
      {
         // todo - try again if failed, or if old??
         return;
      }

      qProcessMetaDataLoadState = qProcessMetaDataLoadState.plus(Pair(processName, LoadState.Loading(Unit)))

      viewModelScope.launch()
      {
         val processLoadState = try
         {
            LoadState.Success(qqqRepository.getProcessMetaData(processName))
         }
         catch(e: Exception)
         {
            Log.w(TAG, "Error loading Process [${processName}]", e)
            LoadState.Error(e.message ?: "Error loading Process")
         }

         qProcessMetaDataLoadState = qProcessMetaDataLoadState.plus(Pair(processName, processLoadState))
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   companion object
   {
      lateinit var dataStore: DataStore<Preferences>

      val factory: ViewModelProvider.Factory = viewModelFactory()
      {
         initializer()
         {
            val application = (this[APPLICATION_KEY] as QMobileApplication)
            QViewModel(qqqRepository = application.container.qqqRepository, dataStore = dataStore)
         }
      }
   }
}
