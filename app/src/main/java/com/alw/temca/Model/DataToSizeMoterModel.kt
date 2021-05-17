package com.alw.temca.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataToSizeMoterModel(var phase:String,var panternStart:String, var unit:String): Parcelable
