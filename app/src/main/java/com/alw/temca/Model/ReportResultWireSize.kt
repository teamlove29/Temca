package com.alw.temca.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ReportResultWireSize(
        val phase:String,
        val installation:String,
        val cableType:String,
        val breaker:String,
        val distance:String,
        val cableSize:String,
        val wireGround:String,
        val condutiSize: String,
        val pressure:String, ) : Parcelable
