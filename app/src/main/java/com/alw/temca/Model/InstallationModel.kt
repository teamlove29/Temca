package com.alw.temca.Model

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class InstallationModel(val image:Drawable,var title:String, var des : String)

@Parcelize
data class InstallationModelInTransformer(var title:String, var des :String): Parcelable