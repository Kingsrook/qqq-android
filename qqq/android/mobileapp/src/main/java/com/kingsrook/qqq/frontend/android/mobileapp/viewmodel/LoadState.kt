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

package com.kingsrook.qqq.frontend.android.mobileapp.viewmodel

/***************************************************************************
 **
 ***************************************************************************/
sealed interface LoadState<T>
{
   data class Success<T>(val value: T) : LoadState<T>
   data class Error<T>(val message: String) : LoadState<T>
   {
      constructor(e: Exception, m: String) : this("${m}: ${e.message ?: "Error details not available"}")
   }

   data class Loading<T>(val unit: Unit) : LoadState<T>
   {
      val start = System.currentTimeMillis()
   }

   companion object
   {
      /***************************************************************************
       **
       ***************************************************************************/
      fun areAnyErrors(loadStates: List<LoadState<*>>): Boolean
      {
         for(loadState in loadStates)
         {
            if(loadState is Error)
            {
               return (true);
            }
         }

         return (false);
      }

      /***************************************************************************
       **
       ***************************************************************************/
      fun getErrorMessages(loadStates: List<LoadState<*>>): List<String>
      {
         val rs: MutableList<String> = mutableListOf();

         for(loadState in loadStates)
         {
            if(loadState is Error)
            {
               rs.add(loadState.message)
            }
         }

         return (rs)
      }

      /***************************************************************************
       **
       ***************************************************************************/
      fun areAnyLoading(loadStates: List<LoadState<*>>): Boolean
      {
         for(loadState in loadStates)
         {
            if(loadState is Loading)
            {
               return (true);
            }
         }

         return (false);
      }
   }
}
