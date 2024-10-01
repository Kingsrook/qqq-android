package com.kingsrook.qqq.frontend.android.core.model.processes

import kotlinx.serialization.Serializable

/***************************************************************************
 **
 ***************************************************************************/
@Serializable
data class QJobRunning(
   override val processUUID: String,
   val message: String = "",
   val current: Int? = null,
   val total: Int? = null,
) : ProcessStepResult()
