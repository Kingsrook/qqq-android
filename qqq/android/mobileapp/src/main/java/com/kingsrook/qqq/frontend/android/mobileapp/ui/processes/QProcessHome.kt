package com.kingsrook.qqq.frontend.android.mobileapp.ui.processes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kingsrook.qqq.frontend.android.core.model.metadata.QComponentType
import com.kingsrook.qqq.frontend.android.core.model.metadata.QInstance
import com.kingsrook.qqq.frontend.android.core.model.metadata.QProcessMetaData
import com.kingsrook.qqq.frontend.android.core.model.processes.QJobComplete
import com.kingsrook.qqq.frontend.android.core.model.processes.QJobError
import com.kingsrook.qqq.frontend.android.core.model.processes.QJobRunning
import com.kingsrook.qqq.frontend.android.core.model.processes.QJobStarted
import com.kingsrook.qqq.frontend.android.mobileapp.ui.QNavigator
import com.kingsrook.qqq.frontend.android.mobileapp.ui.processes.components.ProcessComponentHTML
import com.kingsrook.qqq.frontend.android.mobileapp.ui.processes.components.ProcessComponentHelpText
import com.kingsrook.qqq.frontend.android.mobileapp.ui.processes.components.UnsupportedProcessComponent
import com.kingsrook.qqq.frontend.android.mobileapp.ui.utils.LoadStateView
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.ProcessViewModel

/***************************************************************************
 **
 ***************************************************************************/
@Composable
fun QProcessHome(qInstance: QInstance, modifier: Modifier = Modifier, lightProcess: QProcessMetaData, qNavigator: QNavigator? = null)
{
   val processViewModel: ProcessViewModel = viewModel(factory = ProcessViewModel.factory)
   processViewModel.qInstance = qInstance
   processViewModel.processName = lightProcess.name

   processViewModel.closeProcessCallback =
      {
         qNavigator?.popStack()
      }

   val processMetaDataLoadState = processViewModel.processMetaDataLoadState;
   val initLoadState = processViewModel.initLoadState;
   val topLevelErrorState = processViewModel.topLevelErrorState;
   val loadStates = listOf(processMetaDataLoadState, initLoadState, topLevelErrorState);

   LaunchedEffect(key1 = lightProcess.name)
   {
      processViewModel.doInit();
   }

   Column()
   {
      Text(
         lightProcess.label,
         fontSize = 24.sp,
         fontWeight = FontWeight.SemiBold,
         modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .testTag("process.title")
      )

      HorizontalDivider(thickness = Dp.Hairline)

      LoadStateView(
         loadStates,
         modifier = Modifier,
         loadingText = "Loading Process"
      )
      {
         val lastStepResult = processViewModel.lastStepResult

         LazyColumn()
         {
            item()
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
                     ProcessFrontendStepView(processViewModel, Modifier.padding(top = 8.dp))
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
 **
 ***************************************************************************/
@Composable
fun ProcessFrontendStepView(processViewModel: ProcessViewModel, modifier: Modifier = Modifier)
{
   Column(modifier = modifier)
   {
      Text(processViewModel.activeStep?.label ?: "?", fontWeight = FontWeight.Bold, fontSize = 20.sp)

      processViewModel.activeStep?.components?.forEach()
      { component ->
         when (component.type)
         {
            QComponentType.HELP_TEXT -> ProcessComponentHelpText(processViewModel, component)
            QComponentType.HTML -> ProcessComponentHTML(processViewModel, component)
            else -> UnsupportedProcessComponent(processViewModel, component)
         }
      }


      ProcessBottomButtons(processViewModel)
   }
}
