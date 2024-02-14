package com.telemedicine.myclinic.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import com.telemedicine.myclinic.networks.Error;
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

open class BaseViewModel : ViewModel() {
    val compositeDisposable = CompositeDisposable()

    private val schedulersTransformer: SingleTransformer<Objects, Objects> = SingleTransformer { observable ->

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun <T> applySchedulers(): SingleTransformer<T, T> {
        return schedulersTransformer as SingleTransformer<T, T>
    }
    protected val _errorMessage: MutableLiveData<Error> by lazy {
        MutableLiveData<Error>()
    }

    val errorMessage: LiveData<Error> = _errorMessage

    protected val _loading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isLoading: LiveData<Boolean> = _loading


    fun onHandleError(error: Throwable): Error {

        when (error) {
            is HttpException -> {

                return when {
                    error.code() == 404 -> {
                        Error(description = "Url not found.")
                    }
                    error.code() == 500 -> {

                        Error(description = "Internal server error")

                    }
                    else -> {
                        Error(description = error.message())
                    }
                }
            }
            is SocketTimeoutException -> return Error(description = "Problem occur with connecting server!")
            is IOException -> return Error(description = "You are offline. Please connect to internet connection.")
        }

        return Error(description = "Unknown error while processing request.")
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        // compositeDisposable.dispose()
    }
}