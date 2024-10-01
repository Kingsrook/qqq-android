package com.kingsrook.qqq.frontend.android.mobileapp.ui.apps

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kingsrook.qqq.frontend.android.core.model.metadata.QAppMetaData
import com.kingsrook.qqq.frontend.android.core.model.metadata.QAppNodeType
import com.kingsrook.qqq.frontend.android.core.model.metadata.QInstance
import com.kingsrook.qqq.frontend.android.mobileapp.ui.QNavigator

/***************************************************************************
 **
 ***************************************************************************/
@Composable
fun QAppHome(qInstance: QInstance, modifier: Modifier = Modifier, app: QAppMetaData? = null, qNavigator: QNavigator? = null)
{
   val title = app?.let { app.label } ?: "Home"
   val childList = app?.let { app.children } ?: qInstance.appTree
   val widgetList = app?.let { app.widgets } ?: emptyList()

   val childApps = childList.filter { child -> child.type == QAppNodeType.APP }
   val childTables = childList.filter { child -> child.type == QAppNodeType.TABLE }
   val childProcesses = childList.filter { child -> child.type == QAppNodeType.PROCESS }
   val childReports = childList.filter { child -> child.type == QAppNodeType.REPORT }

   val density = LocalDensity.current
   val rowWidth = remember { mutableStateOf(300.dp) }
   val rowWithinSectionWidth = remember { mutableStateOf(300.dp) }

   LazyColumn()
   {
      ////////////////////////////////
      // start with the app's label //
      ////////////////////////////////
      item()
      {
         Text(
            title,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
               .padding(bottom = 8.dp)
               .testTag("qInstance.appLabel")
         )
      }

      ///////////////////////////
      // first show child-apps //
      ///////////////////////////
      items(childApps)
      { childApp ->
         Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
               .fillMaxWidth()
               .padding(8.dp)
               .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
               .onGloballyPositioned()
               { coordinates ->
                  rowWidth.value = with(density) { coordinates.size.width.toDp() }
               }
         )
         {
            Text(
               childApp.label,
               softWrap = true,
               lineHeight = 20.sp,
               modifier = Modifier
                  .requiredWidth(rowWidth.value - 100.dp)
                  .padding(24.dp)
            )

            Button(
               onClick = { qNavigator?.navigateToApp(childApp.name) },
               modifier = Modifier
                  .width(100.dp)
                  .padding(12.dp)
            )
            {
               Text("Go")
            }
         }
      }

      ///////////////////////////////////////////////////////////
      // todo actually implement widgets here (or above apps?) //
      ///////////////////////////////////////////////////////////
      items(widgetList)
      { widget ->
         Text(
            widget,
            softWrap = true,
            lineHeight = 20.sp,
            modifier = Modifier
               .fillMaxWidth()
               .padding(8.dp)
               .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
               .padding(24.dp)
         )
      }

      ////////////////////////////////////////////////////
      // app sections (with tables, processes, reports) //
      ////////////////////////////////////////////////////
      items(app?.sections ?: emptyList())
      { section ->
         Column(
            modifier = Modifier
               .fillMaxWidth()
               .padding(8.dp)
               .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
               .padding(16.dp)
         )
         {
            Text(section.label, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(8.dp))

            ///////////////
            // processes //
            ///////////////
            section.processes?.forEach()
            { processName ->
               val process = qInstance.processes?.get(processName)
               process?.let()
               {
                  Row(
                     horizontalArrangement = Arrangement.SpaceBetween,
                     modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                        .onGloballyPositioned()
                        { coordinates ->
                           rowWithinSectionWidth.value = with(density) { coordinates.size.width.toDp() }
                        }
                  )
                  {
                     Text(
                        process.label,
                        softWrap = true,
                        lineHeight = 20.sp,
                        modifier = Modifier
                           .requiredWidth(rowWithinSectionWidth.value - 100.dp)
                           // .requiredWidth(200.dp)
                           .padding(24.dp)
                     )

                     Button(
                        onClick = { qNavigator?.navigateToProcess(process.name) },
                        modifier = Modifier
                           .requiredWidth(100.dp)
                           .padding(12.dp)
                     )
                     {
                        Text("Go", softWrap = false)
                     }
                  }
               }
            }
         }
      }

   }
}