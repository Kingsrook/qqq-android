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

package com.kingsrook.qqq.frontend.android.mobileapp.ui.processes

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase

/***************************************************************************
 ** when a frontend step specifies a "format", that implies a number of
 ** different settings - which this class defines and its subclasses clarify.
 ***************************************************************************/
open class FrontendStepFormat
{
   open fun isStepLabelDisplayed() = true
   open fun doesScreenGetPadding() = true
   open fun areStandardFooterButtonsDisplayed() = true
   open fun playOnSubmitSound() = false
   open fun playOnLoadingSound() = false

   companion object
   {
      /***************************************************************************
       **
       ***************************************************************************/
      fun of(format: String?): FrontendStepFormat
      {
         return when ((format ?: "").toLowerCase(Locale.current))
         {
            "scanner" -> ScannerFormat()
            else -> DefaultFormat()
         }
      }

   }
}

/***************************************************************************
 **
 ***************************************************************************/
class DefaultFormat : FrontendStepFormat()

/***************************************************************************
 **
 ***************************************************************************/
class ScannerFormat : FrontendStepFormat()
{
   /***************************************************************************
    **
    ***************************************************************************/
   override fun isStepLabelDisplayed() = false

   /***************************************************************************
    **
    ***************************************************************************/
   override fun doesScreenGetPadding() = false

   /***************************************************************************
    **
    ***************************************************************************/
   override fun areStandardFooterButtonsDisplayed() = false

   /***************************************************************************
    **
    ***************************************************************************/
   override fun playOnSubmitSound() = true

   /***************************************************************************
    **
    ***************************************************************************/
   override fun playOnLoadingSound() = true
}