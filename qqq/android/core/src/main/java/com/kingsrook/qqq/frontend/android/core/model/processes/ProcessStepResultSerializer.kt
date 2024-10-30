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

package com.kingsrook.qqq.frontend.android.core.model.processes

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/***************************************************************************
 **
 ***************************************************************************/
class ProcessStepResultSerializer : JsonContentPolymorphicSerializer<ProcessStepResult>(ProcessStepResult::class)
{
   /***************************************************************************
    **
    ***************************************************************************/
   override fun selectDeserializer(element: JsonElement) = when (element.jsonObject["type"]?.jsonPrimitive?.content)
   {
      "COMPLETE" -> QJobComplete.serializer()
      "JOB_STARTED" -> QJobStarted.serializer()
      "RUNNING" -> QJobRunning.serializer()
      "ERROR" -> QJobError.serializer()
      else -> throw SerializationException("Could not ascertain ProcessStepResult type from JSON response.")
   }

}
