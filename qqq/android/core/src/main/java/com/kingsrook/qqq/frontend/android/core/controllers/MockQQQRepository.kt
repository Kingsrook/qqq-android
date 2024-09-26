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

import com.kingsrook.qqq.frontend.android.core.model.Environment
import com.kingsrook.qqq.frontend.android.core.model.metadata.QAppMetaData
import com.kingsrook.qqq.frontend.android.core.model.metadata.QBrandingMetaData
import com.kingsrook.qqq.frontend.android.core.model.metadata.QInstance
import com.kingsrook.qqq.frontend.android.core.model.metadata.QProcessMetaData
import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.BaseAuthenticationMetaData
import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.FullyAnonymousAuthenticationMetaData
import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.ManageSessionRequest
import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.ManageSessionResponse
import java.util.UUID

/***************************************************************************
 **
 ***************************************************************************/
class MockQQQRepository : QQQRepository
{
   private var sessionUUID: String? = null;
   private var environment: Environment = Environment("Mock Env", "https://mock/");

   /***************************************************************************
    **
    ***************************************************************************/
   override fun setEnvironment(environment: Environment)
   {
      this.environment = environment;
   }

   /***************************************************************************
    **
    ***************************************************************************/
   override fun setSessionUUID(sessionUUID: String)
   {
      this.sessionUUID = sessionUUID;
   }

   /***************************************************************************
    **
    ***************************************************************************/
   override suspend fun getAuthenticationMetaData(): BaseAuthenticationMetaData
   {
      return (FullyAnonymousAuthenticationMetaData("FULLY_ANONYMOUS"))
   }

   /***************************************************************************
    **
    ***************************************************************************/
   override suspend fun manageSession(body: ManageSessionRequest): ManageSessionResponse
   {
      return ManageSessionResponse(UUID.randomUUID().toString())
   }

   /***************************************************************************
    **
    ***************************************************************************/
   override suspend fun getMetaData(): QInstance
   {
      if((sessionUUID ?: "").lowercase().contains("fail"))
      {
         throw RuntimeException("sessionUUID indicates that we should fail")
      }

      var appName = "Mock Application"
      if(environment.label == "Test Env 2")
      {
         appName += " 2"
      }

      return QInstance(
         branding = QBrandingMetaData(appName = appName),
         appTree = listOf(
            QAppMetaData(name = "app1", label = "App 1"),
            QAppMetaData(name = "app2", label = "App 2")
         ),
         processes = emptyMap(),
      )
   }

   /***************************************************************************
    **
    ***************************************************************************/
   override suspend fun getProcessMetaData(processName: String): QProcessMetaData
   {
      return QProcessMetaData(name = processName, label = processName)
   }
}