package com.kingsrook.qqq.frontend.android.mobileapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.kingsrook.qqq.frontend.android.mobileapp.ui.horseshoe.AppRoute
import com.kingsrook.qqq.frontend.android.mobileapp.ui.horseshoe.HomeRoute
import com.kingsrook.qqq.frontend.android.mobileapp.ui.horseshoe.ProcessRoute

private const val TAG = "QNavigator";

/***************************************************************************
 **
 ***************************************************************************/
class QNavigator(val navController: NavHostController) : ViewModel()
{
   var navDepth: Int by mutableStateOf(0)
      private set

   var atHome: Boolean by mutableStateOf(true)
      private set

   /***************************************************************************
    **
    ***************************************************************************/
   fun navigateToApp(name: String)
   {
      navigateToRoute(AppRoute(name))
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun navigateToProcess(name: String)
   {
      navigateToRoute(ProcessRoute(name))
   }

   /***************************************************************************
    **
    ***************************************************************************/
   private fun navigateToRoute(route: Any)
   {
      navDepth++
      atHome = false;
      navController.navigate(route)
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun popStack()
   {
      navDepth--
      if(!navController.popBackStack())
      {
         navController.navigate(HomeRoute)
         navDepth = 0;
      }

      if(navDepth <= 0)
      {
         atHome = true;
      }
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun goHome()
   {
      while(!atHome)
      {
         popStack()
      }
   }

}