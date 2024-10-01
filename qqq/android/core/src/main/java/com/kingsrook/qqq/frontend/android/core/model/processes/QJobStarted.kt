package com.kingsrook.qqq.frontend.android.core.model.processes

import kotlinx.serialization.Serializable

/***************************************************************************
 **
 ***************************************************************************/
@Serializable
data class QJobStarted(
   override val processUUID: String,
   val jobUUID: String,
) : ProcessStepResult()
