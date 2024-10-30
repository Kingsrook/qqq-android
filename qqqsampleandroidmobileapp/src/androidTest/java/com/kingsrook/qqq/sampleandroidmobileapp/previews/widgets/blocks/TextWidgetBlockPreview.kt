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

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kingsrook.qqq.frontend.android.core.model.MutableMapOfStringAny
import com.kingsrook.qqq.frontend.android.core.model.widgets.WidgetBlock
import com.kingsrook.qqq.frontend.android.mobileapp.ui.widgets.blocks.TextWidgetBlock
import com.kingsrook.qqq.frontend.android.mobileapp.ui.widgets.blocks.WidgetBlockParameters

@Composable
fun TextBlock(
   styles: MutableMapOfStringAny = emptyMap<String, Any?>().toMutableMap(),
   values: MutableMapOfStringAny = emptyMap<String, Any?>().toMutableMap()
) = TextWidgetBlock(WidgetBlockParameters(WidgetBlock("", "TEXT", styles = styles, values = values)))

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockDefault()
{
   TextBlock(values = mutableMapOf("text" to "Default color"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockAlert()
{
   TextBlock(mutableMapOf("format" to "alert"), mutableMapOf("text" to "Default color"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockUnknownColor()
{
   TextBlock(mutableMapOf("color" to "un known"), mutableMapOf("text" to "Unknown color"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockSuccess()
{
   TextBlock(mutableMapOf("color" to "SUCCESS"), mutableMapOf("text" to "Great Success"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockSuccessAlert()
{
   TextBlock(mutableMapOf("color" to "SUCCESS", "format" to "alert"), mutableMapOf("text" to "Success alert"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockDefaultColorBanner()
{
   TextBlock(mutableMapOf("format" to "banner"), mutableMapOf("text" to "Default banner!"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockMutedBanner()
{
   TextBlock(mutableMapOf("color" to "MUTED", "format" to "banner"), mutableMapOf("text" to "Muted banner!"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockErrorBanner()
{
   TextBlock(mutableMapOf("color" to "ERROR", "format" to "banner"), mutableMapOf("text" to "Error banner!"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockSuccessBanner()
{
   TextBlock(mutableMapOf("color" to "SUCCESS", "format" to "banner"), mutableMapOf("text" to "Success banner!"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockWarning()
{
   TextBlock(mutableMapOf("color" to "WARNING"), mutableMapOf("text" to "Warning Will"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockError()
{
   TextBlock(mutableMapOf("color" to "ERROR"), mutableMapOf("text" to "Error Jordan"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockInfo()
{
   TextBlock(mutableMapOf("color" to "INFO"), mutableMapOf("text" to "Info matic"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockMuted()
{
   TextBlock(mutableMapOf("color" to "MUTED"), mutableMapOf("text" to "Shhh!"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockInterpolated()
{
   TextBlock(values = mutableMapOf("text" to "\${someVar}", "interpolatedText" to "Some Val"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockSemiBold()
{
   TextBlock(mutableMapOf("weight" to "semibold"), mutableMapOf("text" to "boldly"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockWeight100()
{
   TextBlock(mutableMapOf("weight" to "100"), mutableMapOf("text" to "thin 100"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockWeight900()
{
   TextBlock(mutableMapOf("weight" to "900"), mutableMapOf("text" to "thick 900"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockSizeLargest()
{
   TextBlock(mutableMapOf("size" to "largest"), mutableMapOf("text" to "big"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockSize6()
{
   TextBlock(mutableMapOf("size" to "6"), mutableMapOf("text" to "so 6"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBlockSize26()
{
   TextBlock(mutableMapOf("size" to "26"), mutableMapOf("text" to "so 26"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetRGBColor()
{
   TextBlock(mutableMapOf("color" to "00FF80"), mutableMapOf("text" to "some teal"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetRGBAColor()
{
   TextBlock(mutableMapOf("color" to "00FF8080"), mutableMapOf("text" to "some teal"))
}

/***************************************************************************
 **
 ***************************************************************************/
@Preview
@Composable
fun TextWidgetBannerAndIcon()
{
   TextBlock(mutableMapOf("format" to "banner"), mutableMapOf("startIcon" to "verified_user", "text" to "Hello there"))
}