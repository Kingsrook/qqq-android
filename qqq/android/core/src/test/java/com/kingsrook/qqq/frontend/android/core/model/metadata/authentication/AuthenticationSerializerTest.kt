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

package com.kingsrook.qqq.frontend.android.core.model.metadata.authentication

import kotlinx.serialization.json.Json
import org.junit.Test

import org.junit.Assert.*

/***************************************************************************
 **
 ***************************************************************************/
class AuthenticationSerializerTest
{
   /***************************************************************************
    **
    ***************************************************************************/
   @Test
   fun testDecodeAuth0()
   {
      val json = """
        {
           "name":"auth0",
           "type":"AUTH_0",
           "values": 
           {
              "baseUrl":"https://auth.coldtrack-dev.com/",
              "clientId":"Q7hjPeDrfq11EgZyFxpoXQRTPhTn3KzN",
              "audience":"https://www.nutrifresh-one-dev.com"
           }
        }
        """

      val withUnknownKeys = Json { ignoreUnknownKeys = true }
      val parsedItem = withUnknownKeys.decodeFromString<BaseAuthenticationMetaData>(json)
      assertTrue(parsedItem is Auth0AuthenticationMetaData)
      assertEquals("Q7hjPeDrfq11EgZyFxpoXQRTPhTn3KzN", (parsedItem as Auth0AuthenticationMetaData).values.clientId)
   }
}