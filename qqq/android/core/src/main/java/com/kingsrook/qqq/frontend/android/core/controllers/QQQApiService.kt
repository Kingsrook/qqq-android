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

package com.kingsrook.qqq.frontend.android.core.controllers

import com.kingsrook.qqq.frontend.android.core.model.metadata.QInstance
import com.kingsrook.qqq.frontend.android.core.model.metadata.QProcessMetaDataWrapped
import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.BaseAuthenticationMetaData
import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.ManageSessionRequest
import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.ManageSessionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


/***************************************************************************
 ** Class (interface?) that instructs retrofit how to make HTTP requests
 ** to a QQQ middleware server.
 ***************************************************************************/
interface QQQApiService
{
   //////////////////////////////
   // authentication endpoints //
   //////////////////////////////
   @GET("metaData/authentication")
   suspend fun getAuthenticationMetaData(): BaseAuthenticationMetaData

   @POST("manageSession")
   suspend fun manageSession(@Body body: ManageSessionRequest): ManageSessionResponse

   //////////////////
   // qqq meta dta //
   //////////////////
   @GET("metaData")
   suspend fun getMetaData(): QInstance

   @GET("/metaData/process/{processName}")
   suspend fun getProcessMetaData(@Path(value = "processName") processName: String): QProcessMetaDataWrapped

   ///////////////////////////////////////////////////////////////////////////////
   // todo - maybe this is supposed to use some kind of interceptor in okhttp?? //
   // but, if this works, just go with it??                                     //
   ///////////////////////////////////////////////////////////////////////////////
   /*
   object Creddy
   {
      // Create credentials
      val username = "darin+devcoldtrack@coldtrack.com"
      val password = "bgz3pnx@KDP1zmx_jdw"

      // create correct Base64 encoded Basic Auth credentials
      val credentials = Credentials.basic(username, password)
   }
   */

}

