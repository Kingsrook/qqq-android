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

package com.kingsrook.qqq.sampleandroidmobileapp.previews.widgets.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kingsrook.qqq.frontend.android.core.model.MutableMapOfStringAny
import com.kingsrook.qqq.frontend.android.core.model.widgets.WidgetBlock
import com.kingsrook.qqq.frontend.android.mobileapp.ui.widgets.blocks.ButtonWidgetBlock
import com.kingsrook.qqq.frontend.android.mobileapp.ui.widgets.blocks.WidgetBlockParameters

@Composable
fun ActionButton(
   styles: MutableMapOfStringAny = emptyMap<String, Any?>().toMutableMap(),
   values: MutableMapOfStringAny = emptyMap<String, Any?>().toMutableMap()
) = ButtonWidgetBlock(WidgetBlockParameters(WidgetBlock("", "BUTTON", styles = styles, values = values)))

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ActionButtonWidgetBlockDefault()
{
   ActionButton(values = mutableMapOf("label" to "Default"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ActionButtonWidgetBlockSuccess()
{
   ActionButton(styles = mutableMapOf("color" to "SUCCESS"), values = mutableMapOf("label" to "Default"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ActionButtonWidgetBlockOutlined()
{
   Surface(modifier = Modifier.background(Color.Gray).padding(20.dp))
   {
      ActionButton(styles = mutableMapOf("format" to "outlined"), values = mutableMapOf("label" to "Outlined"))
   }
}


/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ActionButtonWidgetBlockOutlinedError()
{
   ActionButton(styles = mutableMapOf("color" to "ERROR", "format" to "outlined"), values = mutableMapOf("label" to "Outlined"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ActionButtonWidgetBlockFilled()
{
   ActionButton(styles = mutableMapOf("format" to "filled"), values = mutableMapOf("label" to "Filled"))
}


/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ActionButtonWidgetBlockFilledError()
{
   ActionButton(styles = mutableMapOf("color" to "SUCCESS", "format" to "filled"), values = mutableMapOf("label" to "Filled"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ActionButtonWidgetBlockText()
{
   ActionButton(styles = mutableMapOf("format" to "text"), values = mutableMapOf("label" to "Text"))
}


/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ActionButtonWidgetBlockTextError()
{
   ActionButton(styles = mutableMapOf("color" to "ERROR", "format" to "text"), values = mutableMapOf("label" to "Text"))
}


/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ActionButtonWidgetBlockStartIcon()
{
   ActionButton(styles = mutableMapOf("color" to "INFO", "format" to "outlined"), values = mutableMapOf("label" to "Text", "startIcon" to mapOf("name" to "check")))
}



/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun ActionButtonWidgetBlockEndIcon()
{
   ActionButton(styles = mutableMapOf("color" to "WARNING", "format" to "filled"), values = mutableMapOf("label" to "Text", "endIcon" to mapOf("name" to "warning")))
}

