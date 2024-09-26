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

package com.kingsrook.qqq.frontend.android.core.model.metadata

import com.kingsrook.qqq.frontend.android.core.model.QPossibleValue
import kotlinx.serialization.Serializable

/***************************************************************************
 **
 ***************************************************************************/
@Serializable
data class QFieldMetaData(
   val name: String,
   val label: String,
   val defaultValue: String? = null,
   val type: QFieldType,
   val isRequired: Boolean = false,
   val isEditable: Boolean = false,
   val isHeavy: Boolean = false,
   val possibleValueSourceName: String? = null,
   val inlinePossibleValueSource: List<com.kingsrook.qqq.frontend.android.core.model.QPossibleValue>? = null,
   val displayFormat: String? = null,
   val adornments: List<FieldAdornment>? = null,
   val helpContents: List<QHelpContent>? = null,
   // todo - not Any... val behaviors: List<Any>?,
)
{

}
