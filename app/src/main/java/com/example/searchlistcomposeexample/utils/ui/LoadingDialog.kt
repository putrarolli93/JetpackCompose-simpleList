package com.example.searchlistcomposeexample.utils.ui

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import com.example.searchlistcomposeexample.R

class LoadingDialog(
    activity: Activity?
) : Dialog(activity!!) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_loading)
        setCancelable(true)

        this.window?.setBackgroundDrawableResource(R.color.transparent)
    }

}