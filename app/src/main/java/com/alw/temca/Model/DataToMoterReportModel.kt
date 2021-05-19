package com.alw.temca.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataToMoterReportModel(var sizemoter:String,
                                  var unit:String,
                                  var phase:String,
                                  var panternstart:String,
                                  var cabletype:String,
                                  var amountdistance:String,

                                  var resultpowerrate:String,
                                  var resultrefpower:String,
                                  var resultsizecable:String,
                                  var resultgroudcable:String,
                                  var resultconduit:String,
                                  var resultbreaker:String,
                                  var resultpressure:String): Parcelable
