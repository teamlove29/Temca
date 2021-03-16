package com.alw.temca.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ReportResultWireSize(
        val cableSize:String,
        val condutiSize: String,
        val pressure:String, ) : Parcelable