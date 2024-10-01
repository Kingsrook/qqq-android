package com.kingsrook.qqq.frontend.android.mobileapp.ui.processes.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.kingsrook.qqq.frontend.android.core.model.metadata.QFrontendComponent
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.ProcessViewModel

/***************************************************************************
 **
 ***************************************************************************/
@Composable
fun UnsupportedProcessComponent(processViewModel: ProcessViewModel, component: QFrontendComponent)
{
   Text(
      "Unsupported component type: ${component.type}",
      fontStyle = FontStyle.Italic,
      color = Color.Gray,
      modifier = Modifier
         .fillMaxWidth()
         .padding(8.dp)
         .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
         .padding(12.dp)
   )
}