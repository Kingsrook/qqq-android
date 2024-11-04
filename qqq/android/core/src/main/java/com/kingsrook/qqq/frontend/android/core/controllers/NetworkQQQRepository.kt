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

package com.kingsrook.qqq.frontend.android.core.controllers

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kingsrook.qqq.frontend.android.core.model.Environment
import com.kingsrook.qqq.frontend.android.core.model.metadata.QInstance
import com.kingsrook.qqq.frontend.android.core.model.metadata.QProcessMetaData
import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.BaseAuthenticationMetaData
import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.ManageSessionRequest
import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.ManageSessionResponse
import com.kingsrook.qqq.frontend.android.core.model.processes.ProcessStepResult
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import retrofit2.Retrofit
import java.io.IOException
import java.util.TimeZone


/***************************************************************************
 **
 ***************************************************************************/
class NetworkQQQRepository(private var baseUrl: String) : QQQRepository
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
         val thisRetrofit = buildRetrofit(baseUrl)
         val thisQQQApiService = thisRetrofit.create(QQQApiService::class.java)
         qqqApiServiceMap[baseUrl] = thisQQQApiService
      }

      // retrofit = buildRetrofit()
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
               .addInterceptor(QQQInterceptor)
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
      QQQInterceptor.sessionUUID = sessionUUID
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
   override suspend fun getMetaData(
      frontendName: String, frontendVersion: String, applicationName: String, applicationVersion: String
   ): QInstance
   {
      return qqqApiServiceMap[baseUrl]!!.getMetaData(frontendName, frontendVersion, applicationName, applicationVersion)
   }

   /***************************************************************************
    **
    ***************************************************************************/
   override suspend fun getProcessMetaData(processName: String): QProcessMetaData
   {
      return qqqApiServiceMap[baseUrl]!!.getProcessMetaData(processName)
   }

   /***************************************************************************
    **
    ***************************************************************************/
   override suspend fun processInit(processName: String, values: Map<String, Any?>, recordsParam: String?, recordIds: String?, filterJSON: String?, stepTimeoutMillis: Int?): ProcessStepResult
   {
      return qqqApiServiceMap[baseUrl]!!.processInit(processName, buildProcessInitOrStepRequestBodyV1(values, recordsParam, recordIds, filterJSON, stepTimeoutMillis))
   }

   /***************************************************************************
    **
    ***************************************************************************/
   override suspend fun processStep(processName: String, processUUID: String, stepName: String, values: Map<String, Any?>, stepTimeoutMillis: Int?): ProcessStepResult
   {
      return qqqApiServiceMap[baseUrl]!!.processStep(processName, processUUID, stepName, buildProcessInitOrStepRequestBodyV1(values, stepTimeoutMillis = stepTimeoutMillis))
   }

   /***************************************************************************
    **
    ***************************************************************************/
   override suspend fun processJobStatus(processName: String, processUUID: String, jobUUID: String): ProcessStepResult
   {
      return qqqApiServiceMap[baseUrl]!!.processJobStatus(processName, processUUID, jobUUID)
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun buildProcessInitOrStepRequestBodyV1(values: Map<String, Any?>, recordsParam: String? = null, recordIds: String? = null, filterJSON: String? = null, stepTimeoutMillis: Int? = null): RequestBody
   {
      val bodyBuilder = MultipartBody.Builder()
         .setType(MultipartBody.FORM)

      if(!values.isEmpty())
      {
         val valuesJson = JSONObject()
         for(entry in values.entries)
         {
            valuesJson.put(entry.key, entry.value)
         }
         bodyBuilder.addFormDataPart("values", valuesJson.toString())
      }

      if(recordsParam != null)
      {
         bodyBuilder.addFormDataPart("recordsParam", recordsParam)
      }

      if(recordIds != null)
      {
         bodyBuilder.addFormDataPart("recordIds", recordIds)
      }

      if(filterJSON != null)
      {
         bodyBuilder.addFormDataPart("filterJSON", filterJSON)
      }

      if(stepTimeoutMillis != null)
      {
         bodyBuilder.addFormDataPart("stepTimeoutMillis", stepTimeoutMillis.toString())
      }

      val requestBody: RequestBody = bodyBuilder.build()
      return requestBody
   }

   /***************************************************************************
    **
    ***************************************************************************/
   override suspend fun resource(path: String): ByteArray?
   {
      return qqqApiServiceMap[baseUrl]!!.resource(path)
   }

   /***************************************************************************
    **
    ***************************************************************************/
   override fun getURI(path: String): String
   {
      return "${baseUrl.replaceFirst(Regex("/*$"), "/")}${path.replaceFirst(Regex("^/+"), "")}"
   }

   /***************************************************************************
    **
    ***************************************************************************/
   companion object QQQInterceptor : Interceptor
   {
      private var sessionUUID: String? = null

      @Throws(IOException::class)
      override fun intercept(chain: Interceptor.Chain): Response
      {
         val requestBuilder = chain.request().newBuilder()

         sessionUUID?.let()
         {
            requestBuilder.addHeader("Cookie", "sessionUUID=${it}")
         }

         val timeZone = TimeZone.getDefault()
         requestBuilder.addHeader("X-QQQ-UserTimezone", timeZone.toZoneId().toString())

         val response: Response = chain.proceed(requestBuilder.build())

         return response
      }
   }

}
