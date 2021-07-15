package com.alw.temca.Model.CurrentRating

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReportResultCurrent(
    val phase:String,
    val installation:String,
    val cableType:String,
    val cableSize:String,
    val resultCurrentMax:String, ) : Parcelable