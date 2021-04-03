package com.alw.temca.Function

fun FindCableSizeInTable(size:String):String{
    val result:String = when(size){
        "1/2" -> "15"
        "3/4" -> "20"
        "1" -> "25"
        "1-1/4" -> "32"
        "1-1/2" -> "40"
        "2" -> "50"
        "2-1/2" -> "65"
        "3" -> "80"
        "3-1/2" -> "90"
        "4" -> "100"
        "5" -> "125"
        "6" -> "150"
        else -> "-"
    }
    return result
}