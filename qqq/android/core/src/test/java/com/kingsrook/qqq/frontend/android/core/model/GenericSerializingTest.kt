package com.kingsrook.qqq.frontend.android.core.model


import com.kingsrook.qqq.frontend.android.core.model.metadata.QComponentType
import com.kingsrook.qqq.frontend.android.core.model.metadata.QFrontendComponent
import com.kingsrook.qqq.frontend.android.core.model.processes.QJobComplete
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/***************************************************************************
 **
 ***************************************************************************/
class GenericSerializingTest
{
   /***************************************************************************
    **
    ***************************************************************************/
   @Test
   fun testThatCameWithCode()
   {
      /***************************************************************************
       **
       ***************************************************************************/
      @Serializable
      data class MySerializableData(
         val mapValues: Map<String, @Serializable(with = AnyValueSerializer::class) Any?>
      )

      /***************************************************************************
       **
       ***************************************************************************/
      @Serializable
      @ExperimentalSerializationApi
      data class OtherDataClass(
         val intVal: Int,
         val strVal: String,
         @Serializable(with = AnyValueSerializer::class)
         val anyVal: Any? = null,
      )


      val map = mapOf<String, Any?>(
         "Foo" to "bar",
         "One" to 1L, // note, Int comes through as Long, so, Long...
         "Two dot zero" to 2.0,
         "True" to true,
         "None" to null,
         "Data" to OtherDataClass(5, "Cinque")
      )
      val mapData = MySerializableData(map)

      val encoded = Json.encodeToString(mapData)
      println(encoded)

      val decoded = Json.decodeFromString<MySerializableData>(encoded)
      println(decoded)

      assertEquals(mapData, decoded)

      val mapStringAny = MapStringAny(map)
      val encoded2 = Json.encodeToString(mapStringAny)
      println(encoded2)

      val decoded2 = Json.decodeFromString<MapStringAny>(encoded2)
      println(decoded2)

      assertEquals(mapStringAny, decoded2)
   }

   /***************************************************************************
    **
    ***************************************************************************/
   @Test
   fun testQFrontendComponent()
   {
      val component = Json.decodeFromString<QFrontendComponent>(
         """
         {
            "type":  "HELP_TEXT",
            "values": 
            {
               "helpText": "Lorem ipsum"
            }
         }"""
      )

      assertEquals(QComponentType.HELP_TEXT, component.type)
      assertEquals(mapOf("helpText" to "Lorem ipsum"), component.values)
      assertEquals(1, component.values.size)
   }

   /***************************************************************************
    **
    ***************************************************************************/
   @Test
   fun testQJobCompleteWithArray()
   {
      val jobComplete = Json.decodeFromString<QJobComplete>(
         """
         {
            "processUUID":  "123-123-123-123",
            "nextStep": "Underpants",
            "values": 
            {
               "someString": "Nylon",
               "someArray": 
               [
                  {"foo": "bar", "one": 1}
               ],
               "someObject": {"foo": "bar", "one": 1},
               "someNull": null
            }
         }"""
      )

      assertEquals("123-123-123-123", jobComplete.processUUID)
      assertEquals("Underpants", jobComplete.nextStep)
      assertEquals("Nylon", jobComplete.values["someString"])
      assertEquals(listOf(mapOf("foo" to "bar", "one" to 1L)), jobComplete.values["someArray"])
      assertEquals(mapOf("foo" to "bar", "one" to 1L), jobComplete.values["someObject"])
      assertNull(jobComplete.values["someNull"])

   }
}