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

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kingsrook.qqq.frontend.android.core.model.Environment
import com.kingsrook.qqq.frontend.android.core.model.metadata.QInstance
import com.kingsrook.qqq.frontend.android.core.model.metadata.QProcessMetaData
import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.BaseAuthenticationMetaData
import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.ManageSessionRequest
import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.ManageSessionResponse
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import java.io.IOException


/***************************************************************************
 **
 ***************************************************************************/
class NetworkQQQRepository(
   private var baseUrl: String
) : QQQRepository
{
   private val retroJson = Json { ignoreUnknownKeys = true }

   private val qqqApiServiceMap: MutableMap<String, QQQApiService> = mutableMapOf()

   /***************************************************************************
    **
    ***************************************************************************/
   override fun setEnvironment(environment: Environment)
   {
      baseUrl = environment.baseUrl

      if(qqqApiServiceMap[baseUrl] == null)
      {
         val thisRetrofit = buildRetrofit(baseUrl);
         val thisQQQApiService = thisRetrofit.create(QQQApiService::class.java)
         qqqApiServiceMap[baseUrl] = thisQQQApiService;
      }

      // retrofit = buildRetrofit();
      // retrofit.create(QQQApiService::class.java)
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun buildRetrofit(baseUrl: String): Retrofit
   {
      return Retrofit.Builder()
         .addConverterFactory(retroJson.asConverterFactory("application/json".toMediaType()))
         .client(
            OkHttpClient.Builder()
               .addInterceptor(SessionCookieInterceptor)
               .build()
         )
         .baseUrl(baseUrl)
         .build()
   }

   /***************************************************************************
    **
    ***************************************************************************/
   override fun setSessionUUID(sessionUUID: String)
   {
      SessionCookieInterceptor.sessionUUID = sessionUUID;
   }

   /***************************************************************************
    **
    ***************************************************************************/
   override suspend fun getAuthenticationMetaData(): BaseAuthenticationMetaData
   {
      // return qqqApiService.getAuthenticationMetaData()
      return qqqApiServiceMap[baseUrl]!!.getAuthenticationMetaData()
   }

   /***************************************************************************
    **
    ***************************************************************************/
   override suspend fun manageSession(body: ManageSessionRequest): ManageSessionResponse
   {
      return qqqApiServiceMap[baseUrl]!!.manageSession(body)
   }

   /***************************************************************************
    **
    ***************************************************************************/
   override suspend fun getMetaData(): QInstance
   {
      return qqqApiServiceMap[baseUrl]!!.getMetaData()
   }

   /***************************************************************************
    **
    ***************************************************************************/
   override suspend fun getProcessMetaData(processName: String): QProcessMetaData
   {
      return qqqApiServiceMap[baseUrl]!!.getProcessMetaData(processName).process
   }

   /***************************************************************************
    **
    ***************************************************************************/
   companion object SessionCookieInterceptor : Interceptor
   {
      private var sessionUUID: String? = null;

      @Throws(IOException::class)
      override fun intercept(chain: Interceptor.Chain): Response
      {
         val requestBuilder = chain.request().newBuilder()

         sessionUUID?.let()
         {
            requestBuilder.addHeader("Cookie", "sessionUUID=${it}")
         }

         val response: Response = chain.proceed(requestBuilder.build())

         return response
      }
   }

}
