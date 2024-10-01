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

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kingsrook.qqq.frontend.android.core.controllers.QQQRepository
import com.kingsrook.qqq.frontend.android.core.model.metadata.QFrontendStepMetaData
import com.kingsrook.qqq.frontend.android.core.model.metadata.QInstance
import com.kingsrook.qqq.frontend.android.core.model.metadata.QProcessMetaData
import com.kingsrook.qqq.frontend.android.core.model.processes.ProcessStepResult
import com.kingsrook.qqq.frontend.android.core.model.processes.QJobComplete
import com.kingsrook.qqq.frontend.android.core.model.processes.QJobError
import com.kingsrook.qqq.frontend.android.core.model.processes.QJobRunning
import com.kingsrook.qqq.frontend.android.core.model.processes.QJobStarted
import com.kingsrook.qqq.frontend.android.mobileapp.QMobileApplication
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDateTime


private const val TAG = "QViewModel"

/***************************************************************************
 **
 ***************************************************************************/
open class ProcessViewModel(
   private val qqqRepository: QQQRepository
) : ViewModel()
{
   lateinit var qInstance: QInstance
   lateinit var processName: String
   var closeProcessCallback: (() -> Unit)? = null

   var processMetaDataLoadState: LoadState<QProcessMetaData> by mutableStateOf(LoadState.Loading(Unit))
      private set

   var initLoadState: LoadState<ProcessStepResult> by mutableStateOf(LoadState.Loading(Unit))
      private set

   var topLevelErrorState: LoadState<Unit> by mutableStateOf(LoadState.Success(Unit))
      private set

   //////////////////////////////////
   // state of the running process //
   //////////////////////////////////
   private var processUUID: String? = null

   var waitingOnBackend: Boolean by mutableStateOf(true) // true when, we're waiting on the backend - so e.g., next buttons are disabled.
      private set

   var activeJobUUID: String by mutableStateOf("")
      private set

   var jobRunningMessage: String by mutableStateOf("")

   var currentOfTotalMessage: String by mutableStateOf("")

   var jobRunningLastUpdated: LocalDateTime by mutableStateOf(LocalDateTime.now())
      private set

   var lastStepResult: ProcessStepResult by mutableStateOf(QJobComplete("", nextStep = ""))
      private set

   var frontendSteps: List<QFrontendStepMetaData> = emptyList()
      private set

   var activeStepName: String by mutableStateOf("")

   var activeStepIndex: Int by mutableIntStateOf(0)
      private set

   var activeStep: QFrontendStepMetaData? by mutableStateOf(null)
      private set

   var processValues: MutableMap<String, Any?> = mutableMapOf()

   /***************************************************************************
    **
    ***************************************************************************/
   fun doInit()
   {
      viewModelScope.launch()
      {
         loadProcessMetaData().join()
         initProcess()
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun loadProcessMetaData(): Job
   {
      return viewModelScope.launch()
      {
         processMetaDataLoadState = try
         {
            val processMetaData = qqqRepository.getProcessMetaData(processName)
            frontendSteps = processMetaData.frontendSteps ?: emptyList()

            LoadState.Success(processMetaData)
         }
         catch(e: Exception)
         {
            Log.w(TAG, "Error loading ProcessMetaData", e)
            LoadState.Error(e.message ?: "Error loading ProcessMetaData")
         }
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun initProcess(): Job
   {
      return viewModelScope.launch()
      {
         initLoadState = try
         {
            waitingOnBackend = true
            val value = qqqRepository.processInit(processName, emptyMap())
            processUUID = value.processUUID
            waitingOnBackend = false

            when (value)
            {
               is QJobComplete ->
               {
                  processJobCompleteResult(value)
               }

               is QJobError ->
               {
                  setTopLevelError(value.userFacingError ?: value.error)
               }

               else ->
               {
                  setTopLevelError("Unexpected response state on a process init call (of type: ${value.javaClass.simpleName})")
               }
            }

            lastStepResult = value
            LoadState.Success(value)
         }
         catch(e: Exception)
         {
            Log.w(TAG, "Error initing process", e)
            LoadState.Error(e.message ?: "Error initing process")
         }
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun setTopLevelError(message: String)
   {
      topLevelErrorState = LoadState.Error(message)
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun runStep()
   {
      val localProcessUUID = processUUID;
      val localActiveStepName = activeStepName;

      if(localProcessUUID == null)
      {
         setTopLevelError("Missing process UUID.  Unable to continue running process.")
         return;
      }

      if(localActiveStepName == "")
      {
         setTopLevelError("Missing current step name.  Unable to continue running process.")
         return;
      }

      // todo all the work with the form and form values

      val formParams: MutableMap<String, Any?> = mutableMapOf()
      formParams["_qStepTimeoutMillis"] = 1000

      /////////////////////////////////////////
      // set state to show we're loading now //
      /////////////////////////////////////////
      waitingOnBackend = true
      jobRunningLastUpdated = LocalDateTime.now()
      jobRunningMessage = "Working..."

      viewModelScope.launch()
      {
         try
         {
            val value = qqqRepository.processStep(processName, localProcessUUID, localActiveStepName, formParams)
            evaluateProcessResult(value)
         }
         catch(e: Exception)
         {
            Log.w(TAG, "Error running process step", e)
            setTopLevelError("Error running process step:  ${e.message}")
         }
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun evaluateProcessResult(value: ProcessStepResult)
   {
      lastStepResult = value

      when (value)
      {
         is QJobComplete ->
         {
            waitingOnBackend = false
            processJobCompleteResult(value)
         }

         is QJobError ->
         {
            waitingOnBackend = false
            setTopLevelError(value.userFacingError ?: value.error)
         }

         is QJobStarted ->
         {
            waitingOnBackend = true
            activeJobUUID = value.jobUUID
         }

         is QJobRunning ->
         {
            waitingOnBackend = true
            jobRunningMessage = value.message
            jobRunningLastUpdated = LocalDateTime.now()
            if(jobRunningMessage == "")
            {
               jobRunningMessage = "Working..."
            }

            val localCurrent = value.current
            val localTotal = value.total
            currentOfTotalMessage = if(localCurrent != null && localTotal != null)
            {
               "${localCurrent} of ${localTotal}"
            }
            else
            {
               ""
            }
         }

         else ->
         {
            setTopLevelError("Unexpected response state on a process step call (of type: ${value.javaClass.simpleName})")
         }
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun processJobCompleteResult(value: QJobComplete)
   {
      updateActiveStepName(value.nextStep)

      processValues = value.values.toMutableMap()
      activeJobUUID = ""

      value.processMetaDataAdjustment?.let()
      { adjustment ->
         adjustment.updatedFields // todo tododo
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun updateActiveStepName(newActiveStepName: String)
   {
      activeStepName = newActiveStepName

      for((index, qFrontendStepMetaData) in frontendSteps.withIndex())
      {
         if(qFrontendStepMetaData.name == activeStepName)
         {
            activeStepIndex = index
            activeStep = qFrontendStepMetaData
            return
         }
      }

      Log.w(TAG, "Got through findActiveStepIndex and didn't find one...")
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun checkIn()
   {
      val localProcessUUID = processUUID;
      val localActiveStepName = activeStepName;
      val localJobUUID = activeJobUUID;

      if(localProcessUUID == null)
      {
         setTopLevelError("Missing process UUID.  Unable to continue running process.")
         return;
      }

      if(localActiveStepName == "")
      {
         setTopLevelError("Missing current step name.  Unable to continue running process.")
         return;
      }

      if(localJobUUID == null)
      {
         setTopLevelError("Missing job UUID.  Unable to continue running process.")
         return;
      }

      viewModelScope.launch()
      {
         try
         {
            val value = qqqRepository.processJobStatus(processName, localProcessUUID, localJobUUID)
            evaluateProcessResult(value)
         }
         catch(e: Exception)
         {
            Log.w(TAG, "Error checking running process step status", e)
            setTopLevelError("Error checking running process step status:  ${e.message}")
         }
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun cancel()
   {
      // todo (maybe) an 'are you sure' dialog
      // todo - call backend if there's a cancel step on the process
      closeProcessCallback?.invoke()
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun getNextButtonLabel(): String
   {
      when (val local = processMetaDataLoadState)
      {
         is LoadState.Success ->
         {
            if(local.value.stepFlow == "LINEAR" && activeStepIndex == frontendSteps.size - 2)
            {
               return ("Submit");
            }
         }

         else ->
         {
            // noop
         }
      }

      // todo - a special case for VALIDATION_REVIEW_SCREEN, based on the value of the radio, may return Submit

      return ("Next")
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun getNoMoreSteps(): Boolean
   {
      when (val local = processMetaDataLoadState)
      {
         is LoadState.Success ->
         {
            if(local.value.stepFlow == "LINEAR" && activeStepIndex == frontendSteps.size - 1)
            {
               return (true);
            }
         }

         else ->
         {
            // noop
         }
      }

      if(processValues["noMoreSteps"] != null)
      {
         return (true);
      }

      return (false);
   }

   /***************************************************************************
    **
    ***************************************************************************/
   companion object
   {
      val factory: ViewModelProvider.Factory = viewModelFactory()
      {
         initializer()
         {
            val application = (this[APPLICATION_KEY] as QMobileApplication)
            ProcessViewModel(qqqRepository = application.container.qqqRepository)
         }
      }
   }
}
