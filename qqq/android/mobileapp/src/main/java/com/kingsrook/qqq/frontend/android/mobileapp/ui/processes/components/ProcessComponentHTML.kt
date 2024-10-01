package com.kingsrook.qqq.frontend.android.mobileapp.ui.processes.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import com.kingsrook.qqq.frontend.android.core.model.metadata.QFrontendComponent
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.ProcessViewModel

/***************************************************************************
 **
 ***************************************************************************/
@Composable
fun ProcessComponentHTML(processViewModel: ProcessViewModel, component: QFrontendComponent)
{
   val processValue = processViewModel.processValues["${processViewModel.activeStepName}.html"]
   val html: String = if(processValue is String)
   {
      processValue
   }
   else
   {
      processValue.toString()
   }

   Text(
      AnnotatedString.fromHtml(html),
      modifier = Modifier
         .fillMaxWidth()
         .padding(vertical = 12.dp)
   )
}