package com.kingsrook.qqq.frontend.android.mobileapp.ui.processes.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kingsrook.qqq.frontend.android.core.model.metadata.QFrontendComponent
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.ProcessViewModel

/***************************************************************************
 **
 ***************************************************************************/
@Composable
fun ProcessComponentHelpText(processViewModel: ProcessViewModel, component: QFrontendComponent)
{
   Text(
      "Help Text? : ${processViewModel.processValues}",
      color = Color.Gray,
      modifier = Modifier
         .fillMaxWidth()
         .padding(12.dp)
   )
}