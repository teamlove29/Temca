package com.alw.temca.Common

import android.content.Context
import android.os.Environment
import com.alw.temca.R
import java.io.File

object Common {
    fun getAppPath(context:Context) : String{
        val dir = File(Environment.getExternalStorageDirectory().toString()
//        val dir = File(context.filesDir.toString()
        + File.separator
        + context.resources.getString(R.string.app_name)
        + File.separator
        )
        if(!dir.exists()){
            dir.mkdir()
        }
        return dir.path

    }
}