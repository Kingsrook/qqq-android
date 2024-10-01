package com.kingsrook.qqq.frontend.android.mobileapp.ui.processes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kingsrook.qqq.frontend.android.mobileapp.viewmodel.ProcessViewModel
import kotlinx.coroutines.delay
import java.time.format.DateTimeFormatter

/***************************************************************************
 **
 ***************************************************************************/
@Composable
fun ProcessStepLoading(processViewModel: ProcessViewModel)
{
   LaunchedEffect(key1 = processViewModel.jobRunningLastUpdated)
   {
      delay(1000)
      if(processViewModel.waitingOnBackend)
      {
         processViewModel.checkIn()
      }
   }

   val indicatorSize = 64.dp

   Column()
   {
      Row(
         horizontalArrangement = Arrangement.Center, modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .height(indicatorSize)
      )
      {
         CircularProgressIndicator(
            modifier = Modifier.width(indicatorSize).height(indicatorSize),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
         )
      }

      Column(modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(.75f))
      {
         val commonModifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 4.dp)

         if(processViewModel.jobRunningMessage != "")
         {
            Text(processViewModel.jobRunningMessage, modifier = commonModifier)
         }
         else
         {
            Text("Working...", modifier = commonModifier)
         }

         if(processViewModel.currentOfTotalMessage != "")
         {
            Text(processViewModel.currentOfTotalMessage, modifier = commonModifier)
         }

         Text("Updated at: ${processViewModel.jobRunningLastUpdated.format(DateTimeFormatter.ofPattern("h:mm:ss"))}", modifier = commonModifier)
      }

      /////////////////////////////////////////////////////////////////
      // todo - maybe turn this back on for if the auto-checks fail? //
      /////////////////////////////////////////////////////////////////
      // if(processViewModel.activeJobUUID != "")
      // {
      //    Row(
      //       horizontalArrangement = Arrangement.SpaceEvenly,
      //       modifier = Modifier.fillMaxWidth()
      //    )
      //    {
      //       Button(onClick = processViewModel::checkIn, modifier = Modifier.requiredWidth(200.dp))
      //       {
      //          Text("Check status")
      //       }
      //    }
      // }
   }
}

