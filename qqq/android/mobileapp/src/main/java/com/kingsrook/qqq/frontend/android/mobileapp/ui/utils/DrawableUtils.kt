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

package com.kingsrook.qqq.frontend.android.mobileapp.ui.utils

import android.annotation.SuppressLint
import android.content.Context

/***************************************************************************
 ** utilities for working with drawable resources
 ***************************************************************************/
object DrawableUtils
{

   /***************************************************************************
    ** given a name (e.g., a material icon name), get the corresponding
    ** resource identifier.  Null (instead of 0...) if it's not found.
    ***************************************************************************/
   @SuppressLint("DiscouragedApi") // for getIdentifier...
   fun loadDrawableId(context: Context, name: String?): Int?
   {
      if(name == null)
      {
         return (null)
      }

      try
      {
         val rs = context.resources.getIdentifier(
            name,
            "drawable",
            context.packageName
         )
         return if(rs == 0) null else rs
      }
      catch(e: Exception)
      {
         return null
      }
   }

}