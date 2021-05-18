package com.alw.temca.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReportReslutPipeSizeModel(val cabletype:String,
                                     val sizecable: String,
                                     val amount:String,
                                     val pipesize:String,
                                     val conduitsize:String ,
                                     val conduitsizechoose: String,
                                     val amountcablemax : String) : Parcelable