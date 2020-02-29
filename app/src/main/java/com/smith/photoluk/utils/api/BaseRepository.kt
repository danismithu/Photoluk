package com.smith.photoluk.utils.api

import android.util.Log
import retrofit2.Response
import com.smith.photoluk.utils.models.Result
import java.io.IOException

open class BaseRepository {
    suspend fun <T: Any> safeApiCall(call: suspend () -> Result<T>, errorMessage: String): T? {
        val result: Result<T> = call.invoke()
        var data: T? = null

        when(result) {
            is Result.Success ->{
                data = result.data
                Log.d("BASEREPOSITORY", "Result data success")
            }
            is Result.Error -> {
                Log.e("BASEREPOSITORY", "Ocurrió un error: $errorMessage & Exception - ${result.exception}")
            }
        }
        return data
    }

    suspend fun <T: Any> safeApiResponseCall(call: suspend () -> Response<T>, errorMessage: String): Response<T>? {
        val result: Result<Response<T>> = safeApiResponseResult(call, errorMessage)
        var data: Response<T>? = null

        when(result) {
            is Result.Success ->{
                data = result.data
                Log.d("BASEREPOSITORY", "Result data success")
            }
            is Result.Error -> {
                Log.e("BASEREPOSITORY", "Ocurrió un error: $errorMessage & Exception - ${result.exception}")
            }
        }
        return data
    }

    private suspend fun <T: Any> safeApiResult(call: suspend ()-> Response<T>, errorMessage: String): Result<T>{
        val response = call.invoke()
        if(response.isSuccessful) return Result.Success(response.body()!!)
        Log.e("BASEREPOSITORY", "Ocurrió un error obteniendo safeApiResult")
        return Result.Error(IOException("Ocurrió un error obteniendo safeApiResult, ERROR - $errorMessage"))
    }

    private suspend fun <T: Any> safeApiResponseResult(call: suspend ()-> Response<T>, errorMessage: String): Result<Response<T>> {
        val response = call.invoke()
        if(response.isSuccessful) return Result.Success(response)
        Log.e("BASEREPOSITORY", "Ocurrió un error obteniendo safeApiResult")
        return Result.Error(IOException("Ocurrió un error obteniendo safeApiResult, ERROR - $errorMessage"))
    }
}