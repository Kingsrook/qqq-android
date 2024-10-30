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

package com.kingsrook.qqq.frontend.android.core.model.widgets

import com.kingsrook.qqq.frontend.android.core.model.MutableMapOfStringAny
import kotlinx.serialization.Serializable

@Serializable
data class WidgetBlock(
   val blockId: String,
   val blockType: String,
   val conditional: String? = null,
   val layout: String? = null,
   var modalMode: String? = null,
   val styles: MutableMapOfStringAny = mutableMapOf(),
   val values: MutableMapOfStringAny = mutableMapOf(),
   var subBlocks: List<WidgetBlock> = emptyList(),
)
{
   companion object
   {
      fun fromMap(param: Any?): WidgetBlock?
      {
         if(param is Map<*, *>)
         {
            fun mapGetOrElse(map: Map<String, Any?>, key: String, elseValue: String? = null): String?
            {
               if(map[key] != null)
               {
                  return map[key].toString()
               }

               return elseValue
            }

            val map: Map<String, Any?> = param as Map<String, Any?>
            val blockId = if(map["blockId"] != null) "${map["blockId"]}" else ""
            val blockType = if(map["blockType"] != null) "${map["blockType"]}" else if(map["blockTypeName"] != null) "${map["blockTypeName"]}" else "" // todo - not great, looking under 2 keys...
            val conditional = if(map["conditional"] != null) "${map["conditional"]}" else null
            val layout = if(map["layout"] != null) "${map["layout"]}" else null
            val modalMode = mapGetOrElse(map, "modalMode")

            val mapStyles = map["styles"]
            val styles = if(mapStyles is Map<*, *>) mapStyles else mutableMapOf<String, Any>()

            val mapValues = map["values"]
            val values = if(mapValues is Map<*, *>) mapValues else mutableMapOf<String, Any>()

            val mapSubBlocks = map["subBlocks"] ?: map["blocks"] // todo - not great, looking under 2 keys...

            var subBlocks = emptyList<WidgetBlock>()
            if(mapSubBlocks is List<*>)
            {
               subBlocks = mutableListOf()
               mapSubBlocks.forEach()
               { subBlock ->
                  val subWidgetBlock = fromMap(subBlock)
                  if(subWidgetBlock != null)
                  {
                     subBlocks.add(subWidgetBlock)
                  }
               }
            }

            return WidgetBlock(blockId, blockType, conditional, layout, modalMode, styles as MutableMapOfStringAny, values as MutableMapOfStringAny, subBlocks)
         }

         return (null)
      }
   }
}
