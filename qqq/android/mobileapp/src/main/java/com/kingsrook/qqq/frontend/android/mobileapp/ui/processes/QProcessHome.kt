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

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.DisposableEffectResult
import androidx.compose.runtime.DisposableEffectScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kingsrook.qqq.frontend.android.core.model.metadata.QComponentType
import com.kingsrook.qqq.frontend.android.core.model.metadata.QInstance
import com.kingsrook.qqq.frontend.android.core.model.metadata.QProcessMetaData
import com.kingsrook.qqq.frontend.android.core.model.processes.QJobComplete
import com.kingsrook.qqq.frontend.android.core.model.processes.QJobError
import com.kingsrook.qqq.frontend.android.core.model.processes.QJobRunning
import com.kingsrook.qqq.frontend.android.core.model.processes.QJobStarted
import com.kingsrook.qqq.frontend.android.mobileapp.R
import com.kingsrook.qqq.frontend.android.mobileapp.ui.QNavigator
import com.kingsrook.qqq.frontend.android.mobileapp.ui.processes.components.ProcessComponentEditForm
import com.kingsrook.qqq.frontend.android.mobileapp.ui.processes.components.ProcessComponentHTML
import com.kingsrook.qqq.frontend.android.mobileapp.ui.processes.components.ProcessComponentHelpText
import com.kingsrook.qqq.frontend.android.mobileapp.ui.processes.components.ProcessComponentProcessSummaryResults
import com.kingsrook.qqq.frontend.android.mobileapp.ui.processes.components.ProcessComponentWidget
import com.kingsrook.qqq.frontend.android.mobileapp.ui.processes.components.UnsupportedProcessComponent
import com.kingsrook.qqq.frontend.android.mobileapp.ui.utils.LoadStateView
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.LoadState
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.ProcessViewModel
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.QViewModel
import kotlinx.coroutines.delay

/***************************************************************************
 **
 ***************************************************************************/
@Composable
fun QProcessHome(qViewModel: QViewModel, qInstance: QInstance, modifier: Modifier = Modifier, lightProcess: QProcessMetaData, qNavigator: QNavigator? = null)
{
   val processViewModel: ProcessViewModel = viewModel(factory = ProcessViewModel.factory)
   processViewModel.qqqRepository = qViewModel.qqqRepository
   processViewModel.qInstance = qInstance
   processViewModel.processName = lightProcess.name
   processViewModel.qViewModel = qViewModel

   processViewModel.closeProcessCallback =
      {
         qNavigator?.popStack()
      }

   val processMetaDataLoadState = processViewModel.processMetaDataLoadState
   val initLoadState = processViewModel.initLoadState
   val topLevelErrorState = processViewModel.topLevelErrorState
   val loadStates = listOf(processMetaDataLoadState, initLoadState, topLevelErrorState)

   val context = LocalContext.current
   var mediaPlayer: MediaPlayer? = null
   processViewModel.onSubmitCallback =
      {
         val format = FrontendStepFormat.of(processViewModel.activeStep?.format)
         if(format.playOnSubmitSound())
         {
            if(mediaPlayer == null)
            {
               mediaPlayer = createMediaPlayer(context, R.raw.boop1)
            }
            mediaPlayer?.start()
         }
      }

   //////////////////////////////////////////////////////////////
   // try to remove the media player when the screen goes away //
   //////////////////////////////////////////////////////////////
   val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
   DisposableEffect(lifecycleOwner, disposeMediaPlayer(mediaPlayer, lifecycleOwner))

   /////////////////////////////////
   // do the initial process init //
   /////////////////////////////////
   LaunchedEffect(key1 = lightProcess.name)
   {
      processViewModel.doInit()
   }

   ///////////////////////////////////////////////////////////////////////////////////////////
   // in case the init job has gone async - this LaunchedEffect will make us check in on it //
   ///////////////////////////////////////////////////////////////////////////////////////////
   LaunchedEffect(key1 = processViewModel.jobRunningLastUpdated)
   {
      if(processViewModel.initLoadState is LoadState.Loading && processViewModel.processUUID != null)
      {
         delay(1000)
         processViewModel.checkIn()
      }
   }

   Column()
   {
      LoadStateView(
         loadStates,
         modifier = Modifier,
         loadingText = "Loading Process"
      )
      {
         val lastStepResult = processViewModel.lastStepResult

         Column()
         {
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////
            // use the step-instance-counter as key here - to help drive re-composition when advancing through steps //
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////
            key(processViewModel.stepInstanceCounter)
            {
               when (lastStepResult)
               {
                  is QJobRunning,
                  is QJobStarted ->
                  {
                     ProcessStepLoading(processViewModel)
                  }

                  is QJobComplete ->
                  {
                     ProcessFrontendStepView(processViewModel, qInstance)
                  }

                  is QJobError ->
                  {
                     Text("Error: ${lastStepResult.userFacingError ?: lastStepResult.error}")
                  }
               }
            }
         }

      }
   }
}

/***************************************************************************
 ** composable that outputs a frontend process step - its components basically
 ***************************************************************************/
@Composable
fun ProcessFrontendStepView(processViewModel: ProcessViewModel, qInstance: QInstance, modifier: Modifier = Modifier)
{
   val format = FrontendStepFormat.of(processViewModel.activeStep?.format)

   Box()
   {
      Column(
         modifier = modifier
            .padding(if(format.doesScreenGetPadding()) 8.dp else 0.dp)
            .zIndex(1f)
      )
      {
         if(format.isStepLabelDisplayed())
         {
            Text(processViewModel.activeStep?.label ?: "?", fontWeight = FontWeight.Bold, fontSize = 20.sp)
         }

         processViewModel.activeStep?.components?.forEach()
         { component ->
            when (component.type)
            {
               QComponentType.EDIT_FORM -> ProcessComponentEditForm(processViewModel, component)
               QComponentType.HELP_TEXT -> ProcessComponentHelpText(processViewModel, component)
               QComponentType.HTML -> ProcessComponentHTML(processViewModel, component)
               QComponentType.PROCESS_SUMMARY_RESULTS -> ProcessComponentProcessSummaryResults(processViewModel, component)
               QComponentType.WIDGET -> ProcessComponentWidget(processViewModel, qInstance, component, disableControls = processViewModel.waitingOnBackend)
               else -> UnsupportedProcessComponent(processViewModel, component)
            }
         }

         if(format.areStandardFooterButtonsDisplayed())
         {
            ProcessBottomButtons(processViewModel)
         }
      }
   }
}

/***************************************************************************
 **
 ***************************************************************************/
fun createMediaPlayer(context: Context, resourceId: Int): MediaPlayer?
{
   val player: MediaPlayer? = MediaPlayer.create(context, resourceId)
   player?.setVolume(1f, 1f)
   return player
}

/***************************************************************************
 **
 ***************************************************************************/
@Composable
fun disposeMediaPlayer(mediaPlayer: MediaPlayer?, lifecycleOwner: LifecycleOwner): DisposableEffectScope.() -> DisposableEffectResult
{
   var mediaPlayer1 = mediaPlayer
   return {
      val observer = LifecycleEventObserver()
      { _, event ->
         if(event == Lifecycle.Event.ON_STOP)
         {
            mediaPlayer1?.release()
            mediaPlayer1 = null
         }
      }

      lifecycleOwner.lifecycle.addObserver(observer)

      onDispose()
      {
         lifecycleOwner.lifecycle.removeObserver(observer)
         mediaPlayer1?.release()
         mediaPlayer1 = null
      }
   }
}

