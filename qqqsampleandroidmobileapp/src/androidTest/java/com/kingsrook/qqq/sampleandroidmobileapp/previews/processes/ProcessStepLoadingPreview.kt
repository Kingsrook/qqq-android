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

package com.kingsrook.qqq.sampleandroidmobileapp.previews.processes

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kingsrook.qqq.sampleandroidmobileapp.SampleAppMockQQQRepository
import com.kingsrook.qqq.frontend.android.mobileapp.ui.processes.ProcessStepLoading
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.ProcessViewModel

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ProcessStepLoadingPreview()
{
   val processViewModel = ProcessViewModel()
   processViewModel.qqqRepository = SampleAppMockQQQRepository()
   ProcessStepLoading(processViewModel)
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ProcessStepLoadingPreviewWithMessageWithoutCurrentAndTotal()
{
   val processViewModel = ProcessViewModel()
   processViewModel.qqqRepository = SampleAppMockQQQRepository()
   processViewModel.jobRunningMessage = "Working..."
   ProcessStepLoading(processViewModel)
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ProcessStepLoadingPreviewSuperLongMessage()
{
   val processViewModel = ProcessViewModel()
   processViewModel.qqqRepository = SampleAppMockQQQRepository()
   processViewModel.jobRunningMessage = "Doing a thing and stuff and words and more and and on and on."
   ProcessStepLoading(processViewModel)
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ProcessStepLoadingPreviewWithMessageWithCurrentAndTotal()
{
   val processViewModel = ProcessViewModel()
   processViewModel.qqqRepository = SampleAppMockQQQRepository()
   processViewModel.jobRunningMessage = "Processing Orders"
   processViewModel.currentOfTotalMessage = "3 of 2"
   ProcessStepLoading(processViewModel)
}

