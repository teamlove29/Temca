package com.alw.temca.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataToTransformerReportModel(var pressure:String,
                                        var installation:String,
                                        var typecable:String,
                                        var sizetransformer:String,
                                        var resultpowerrating:String,
                                        var resultsizecable:String,
                                        var resultrailsize:String ): Parcelable