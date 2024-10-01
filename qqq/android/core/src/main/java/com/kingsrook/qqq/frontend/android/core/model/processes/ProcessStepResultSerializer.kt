package com.kingsrook.qqq.frontend.android.core.model.processes

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

/***************************************************************************
 **
 ***************************************************************************/
class ProcessStepResultSerializer : JsonContentPolymorphicSerializer<ProcessStepResult>(ProcessStepResult::class)
{
   /***************************************************************************
    **
    ***************************************************************************/
   override fun selectDeserializer(element: JsonElement) =
      if(element.jsonObject["jobUUID"] != null)
      {
         QJobStarted.serializer()
      }
      else if(element.jsonObject["values"] != null || element.jsonObject["nextStep"] != null)
      {
         QJobComplete.serializer()
      }
      else if(element.jsonObject["error"] != null)
      {
         QJobError.serializer()
      }
      else if(element.jsonObject["jobStatus"] != null)
      {
         QJobRunning.serializer()
      }
      else
      {
         throw SerializationException("Could not ascertain ProcessStepResult type from JSON response.")
      }

}
