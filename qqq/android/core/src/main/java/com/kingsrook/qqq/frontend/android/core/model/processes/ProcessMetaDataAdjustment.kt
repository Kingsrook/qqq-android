package com.kingsrook.qqq.frontend.android.core.model.processes

import com.kingsrook.qqq.frontend.android.core.model.metadata.QFieldMetaData
import com.kingsrook.qqq.frontend.android.core.model.metadata.QFrontendStepMetaData
import kotlinx.serialization.Serializable

/***************************************************************************
 **
 ***************************************************************************/
@Serializable
data class ProcessMetaDataAdjustment(
   val updatedFrontendStepList: List<QFrontendStepMetaData>? = emptyList(),
   val updatedFields: Map<String, QFieldMetaData>? = emptyMap(),
)
