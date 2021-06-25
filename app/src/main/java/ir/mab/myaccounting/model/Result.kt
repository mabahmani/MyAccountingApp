package ir.mab.myaccounting.model

sealed class Result<out T> {
    data class Loading<out T>(val data: T) : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val msg: String) : Result<Nothing>()
}
