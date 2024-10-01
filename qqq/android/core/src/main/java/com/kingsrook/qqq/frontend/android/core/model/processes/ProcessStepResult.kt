package com.kingsrook.qqq.frontend.android.core.model.processes

import com.kingsrook.qqq.frontend.android.core.model.metadata.authentication.AuthenticationSerializer
import kotlinx.serialization.Serializable

/***************************************************************************
 **
 ***************************************************************************/
@Serializable(with = ProcessStepResultSerializer::class)
sealed class ProcessStepResult()
{
   abstract val processUUID: String;
}