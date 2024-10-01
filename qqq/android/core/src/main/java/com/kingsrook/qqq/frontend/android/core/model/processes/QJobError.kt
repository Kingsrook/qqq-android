package com.kingsrook.qqq.frontend.android.core.model.processes

import kotlinx.serialization.Serializable

/***************************************************************************
 **
 ***************************************************************************/
@Serializable
data class QJobError(
   override val processUUID: String,
   val error: String,
   val userFacingError: String? = null
) : ProcessStepResult()
