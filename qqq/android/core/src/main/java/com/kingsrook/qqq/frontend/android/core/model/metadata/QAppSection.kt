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

package com.kingsrook.qqq.frontend.android.core.model.metadata

import kotlinx.serialization.Serializable

/***************************************************************************
 **
 ***************************************************************************/
@Serializable
data class QAppSection(
   val name: String,
   val label: String,
   val icon: QIcon? = null,
   val tables: List<String>? = emptyList(),
   val processes: List<String>? = emptyList(),
   val reports: List<String>? = emptyList(),
)
{

   /***************************************************************************
    **
    ***************************************************************************/
   fun hasContent(): Boolean
   {
      if(tables?.isEmpty() != false && processes?.isEmpty() != false && reports?.isEmpty() != false)
      {
         return (false)
      }

      return (true)
   }
}
