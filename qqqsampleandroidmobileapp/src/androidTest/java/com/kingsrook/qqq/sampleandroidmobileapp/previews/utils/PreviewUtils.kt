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

package com.kingsrook.qqq.sampleandroidmobileapp.previews.utils

import com.kingsrook.qqq.sampleandroidmobileapp.SampleAppMockQQQRepository
import com.kingsrook.qqq.frontend.android.core.model.Environment
import com.kingsrook.qqq.frontend.android.mobileapp.container.QAppContainer
import com.kingsrook.qqq.frontend.android.mobileapp.data.MockPreferencesDataStore
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.QViewModel

/***************************************************************************
 **
 ***************************************************************************/
object PreviewUtils
{
   /***************************************************************************
    **
    ***************************************************************************/
   fun createQViewModel(): QViewModel
   {
      QAppContainer.availableEnvironments = listOf(
         Environment("Production", "https://live.coldtrack.com/"),
         Environment("Staging", "https://live.coldtrack-staging.com/"),
         Environment("Production", "https://live.coldtrack-dev.com/"),
         Environment("Local Test", "http://192.168.4.67:8000/"),
      )

      val dataStore = MockPreferencesDataStore()
      val qViewModel = QViewModel(SampleAppMockQQQRepository(), dataStore)
      return (qViewModel)
   }
}