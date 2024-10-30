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

package com.kingsrook.qqq.frontend.android.mobileapp.ui.widgets.blocks

import android.view.KeyEvent.ACTION_MULTIPLE
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.zIndex
import com.kingsrook.qqq.frontend.android.mobileapp.ui.utils.Colors
import kotlinx.coroutines.awaitCancellation

/***************************************************************************
 **
 ***************************************************************************/
@Composable
fun InputFieldWidgetBlock(params: WidgetBlockParameters, modifier: Modifier = Modifier, disableControls: Boolean = false)
{
   val widgetBlock = params.widgetBlock
   val actionCallback = params.actionCallback

   var text: String by remember { mutableStateOf("") }
   var error: String by remember { mutableStateOf("") }
   val focusRequester = remember { FocusRequester() }

   var disableSoftKeyboard: Boolean by remember { mutableStateOf(true) }
   val keyboard = LocalSoftwareKeyboardController.current

   /////////////////////////////////
   // auto-focus, if so requested //
   /////////////////////////////////
   LaunchedEffect(Unit)
   {
      if(widgetBlock.values["autoFocus"] == true)
      {
         focusRequester.requestFocus()
      }
   }

   /////////////////////////////////////
   // read field meta data, if we can //
   /////////////////////////////////////
   val fieldMetaDataValue = widgetBlock.values["fieldMetaData"]
   val fieldMetaDataMap: Map<String, Any?> = if(fieldMetaDataValue is Map<*, *>) fieldMetaDataValue as Map<String, Any?> else emptyMap()
   val label = fieldMetaDataMap["label"]?.toString() ?: ""
   val fieldName = fieldMetaDataMap["name"]?.toString() ?: ""

   // todo - should probably be a specific value to trigger this.
   val scannerMode = widgetBlock.values["autoFocus"] == true

   /***************************************************************************
    **
    ***************************************************************************/
   fun keyEventHandler(keyEvent: KeyEvent): Boolean
   {
      if(scannerMode)
      {
         ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
         // if scanner mode, and we received an action-multiple, read its chars, and return true to indicate that we've handled the input //
         ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
         if(keyEvent.nativeKeyEvent.action == ACTION_MULTIPLE)
         {
            val chars = keyEvent.nativeKeyEvent.characters
            text += chars
            return (true)
         }
      }

      ///////////////////////////////////////////////////
      // else, if we didn't handle input, return false //
      ///////////////////////////////////////////////////
      return (false)
   }

   /***************************************************************************
    **
    ***************************************************************************/
   fun submit()
   {
      // todo - separate config for "supportsActionCodes"?
      if(scannerMode && text.startsWith("->"))
      {
         if(text.length > 2) // todo - validate that the action code exists on this screen?
         {
            actionCallback?.invoke(mapOf("actionCode" to text.substring(2)))
         }
         else
         {
            error = "Invalid action: ${text}"
         }
         return
      }

      if(fieldMetaDataMap["isRequired"] == true && text == "")
      {
         /////////////////////////////////////////////////////
         // can't submit if value is required, but no value //
         /////////////////////////////////////////////////////
         error = "${if(label == "") "This field" else label} is required"
         return
      }

      ///////////////////////////////////////////////
      // if proceeding to submit, then reset error //
      ///////////////////////////////////////////////
      error = ""

      ////////////////////////////////////////////////////////
      // call the actionCallback - to submit the value, etc //
      ////////////////////////////////////////////////////////
      actionCallback?.invoke(mapOf(fieldName to text))
   }

   /***************************************************************************
    ** if user clicks the input field, then try to show the soft keyboard.
    ***************************************************************************/
   fun handleInputClick()
   {
      disableSoftKeyboard = false
      keyboard?.show()
   }

   val inputBackground = Color(247, 247, 247)

   Column(modifier.onKeyEvent(::keyEventHandler))
   {
      Box(modifier = Modifier
         .zIndex(2f)
         .clickable { handleInputClick() }
      )
      {
         Box(modifier = Modifier
            .zIndex(1f)
            .clickable { handleInputClick() })
         {
            DisableSoftKeyboard(disable = disableSoftKeyboard)
            {
               TextField(
                  value = text,
                  enabled = !disableControls,
                  placeholder = {
                     Text("${widgetBlock.values["placeholder"] ?: ""}",
                        Modifier
                           .fillMaxWidth()
                           .clickable { handleInputClick() })
                  },
                  onValueChange =
                  {
                     if(it.contains('\n') && widgetBlock.values["submitOnEnter"] == true)
                     {
                        submit()
                     }
                     else
                     {
                        text = it
                     }
                  },
                  label = if(label != "")
                  {
                     { Text(label, Modifier.clickable { handleInputClick() }) }
                  }
                  else null,

                  colors = TextFieldDefaults.colors(unfocusedContainerColor = inputBackground, focusedContainerColor = inputBackground),

                  modifier = Modifier
                     .zIndex(0f)
                     .clickable()
                     {
                        disableSoftKeyboard = false
                     }

                     .fillMaxWidth()
                     .focusRequester(focusRequester)
               )
            }
         }
      }

      if(error != "")
      {
         Text(error, color = Colors.ERROR)
      }
   }

}

/***************************************************************************
 ** from internet, a thing to make on-screen keyboard go away.
 ***************************************************************************/
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DisableSoftKeyboard(disable: Boolean = true, content: @Composable () -> Unit)
{
   CompositionLocalProvider(LocalSoftwareKeyboardController provides null)
   {
      InterceptPlatformTextInput(
         interceptor = { request, nextHandler ->
            if(!disable)
            {
               nextHandler.startInputMethod(request)
            }
            else
            {
               awaitCancellation()
            }
         },
         content = content,
      )
   }
}