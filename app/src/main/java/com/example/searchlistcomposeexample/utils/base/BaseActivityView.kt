package com.example.searchlistcomposeexample.utils.base

import android.os.Bundle
import com.example.testapp.model.Resource
import com.example.testapp.model.Status

interface BaseActivityView {

    fun initData()

    fun initView(savedInstanceState: Bundle?)

    fun initEvent()

    fun initActivityResultLauncher()

    fun loadingData()

    fun observeData()

    /**
     * Group function negative scenario
     */

    fun onError(throwable: Throwable? = null)

    fun onErrorWithMessage(message: String? = null)

    fun onInternetError()

    fun <T> parseObserveData(
        resource: Resource<T>,
        resultLoading: (T?) -> Unit = { showLoadingDialog() },
        resultSuccess: (T) -> Unit = { _: T -> },
        resultNetworkFailed: (Throwable?) -> Unit = { onError(it) },
        resultError: (Throwable?) -> Unit = { onError(it) },
        resultErrorWithMessage: (String?) -> Unit = { onErrorWithMessage(it) }
    ) {
        when (resource.status) {
            Status.LOADING -> {
                resultLoading(resource.data)
            }
            Status.SUCCESS -> {
                dismissLoadingDialog()
                resource.data?.let { resultSuccess(it) }
            }
            Status.ERROR -> {
                dismissLoadingDialog()
                resultError(resource.throwable)
                resultErrorWithMessage(resource.message)
            }
            Status.NETWORK_FAILED -> {
                dismissLoadingDialog()
                resultNetworkFailed(resource.throwable)
            }
            else -> {}
        }
    }

    /**
     * Group function show and gone UI view progress bar
     */

    fun showLoadingDialog(){}

    fun hideLoadingDialog(){}

    fun dismissLoadingDialog(){}

    fun showLoadingDialogAbsolute(){}

    fun hideLoadingDialogAbsolute(){}

    fun dismissLoadingDialogAbsolute(){}
}