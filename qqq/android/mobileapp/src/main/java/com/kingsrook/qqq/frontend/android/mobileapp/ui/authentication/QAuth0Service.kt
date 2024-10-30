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

package com.kingsrook.qqq.frontend.android.mobileapp.ui.authentication

import android.util.Log
import com.auth0.android.Auth0
import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.Auth0AuthenticationMetaData
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.LoadState
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.QViewModel


/***************************************************************************
 ** class to work with auth0
 ***************************************************************************/
object QAuth0Service
{
   lateinit var scheme: String

   private const val TAG = "QAuth0Service"

   /***************************************************************************
    **
    ***************************************************************************/
   fun makeAuth0Instance(qViewModel: QViewModel): Auth0
   {
      if(!::scheme.isInitialized)
      {
         throw (IllegalStateException("QAuth0Service.scheme was not initialized!"))
      }

      when (val loadState = qViewModel.authenticationMetaDataLoadState)
      {
         is LoadState.Success ->
         {
            val authenticationMetaData = loadState.value as Auth0AuthenticationMetaData
            val auth0Instance: Auth0 = Auth0.getInstance(authenticationMetaData.values.clientId, authenticationMetaData.values.baseUrl)
            Log.d(TAG, "clientId: ${auth0Instance.clientId}, domain: ${auth0Instance.domain}")
            return auth0Instance
         }

         else ->
         {
            throw (IllegalStateException("authenticationMetaData is not loaded."))
         }
      }

   }


}