package com.alw.temca.Model.AmountInPipe

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultToReportModel(
    var typeCable:String,
    var sizeCable:String,
    var amountCable:String,
    var maxConduit:String,
    var maxRails:String): Parcelable