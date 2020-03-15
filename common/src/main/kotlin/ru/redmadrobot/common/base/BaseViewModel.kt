package ru.redmadrobot.common.base

import android.content.Context
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.redmadrobot.common.R
import ru.redmadrobot.core.network.entities.ErrorResponse
import ru.redmadrobot.core.network.entities.HttpException
import timber.log.Timber
import javax.inject.Inject

open class BaseViewModel @Inject constructor(context: Context) : ViewModel() {

    private val getContextString: (resId: Int) -> String = context::getString

    protected val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    fun Disposable.disposeOnCleared() {
        compositeDisposable.add(this)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun Throwable.toUiString(): String {
        return when (this) {
            is HttpException.BadRequest -> {
                when (errorResponse.statusCode) {
                    ErrorResponse.StatusCode.INVALID_API_KEY -> getContextString(R.string.error_invalid_api_key)
                    ErrorResponse.StatusCode.INVALID_CREDENTIALS -> getContextString(R.string.error_invalid_api_key)
                    else -> getContextString(R.string.error_invalid_api_key)
                }
            }
            is HttpException.NoNetworkConnection -> getContextString(R.string.error_no_internet)
            else -> {
                Timber.e(this)
                getContextString(R.string.error_unknown)
            }
        }
    }

}

