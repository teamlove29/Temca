package com.alw.temca.Model.AmountInRails

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultRailsToReportModel(
    var typeCable:String,
    var sizeCable:String,
    var sizeRails:String,
    var amountCable:String): Parcelable