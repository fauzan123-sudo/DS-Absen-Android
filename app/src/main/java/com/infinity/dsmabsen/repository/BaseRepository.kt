package com.infinity.dsmabsen.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

abstract class BaseRepository {
    suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> Response<T>): NetworkResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<T> = apiToBeCalled()
                if (response.isSuccessful) {
                    NetworkResult.Success(data = response.body()!!)
                } else {
                    val errorResponse =
                        JSONObject(response.errorBody()!!.charStream().readText())
                    NetworkResult.Error(errorResponse.toString())

                }
            } catch (e: HttpException) {
                Log.d("HttpException", "safeApiCall: $e ")
                NetworkResult.Error(e.message ?: "terjadi kesalahan")
            } catch (e: IOException) {
                Log.d("IOException", "safeApiCall: $e ")
//                No Connection or URL error or bad connection
                when (e) {
                    is ConnectException -> {
                        val errorMessage =
                            "Gagal terhubung ke server. Mohon periksa koneksi internet Anda dan coba lagi."
                        Log.d("ConnectException", "safeApiCall: $e ")
                        NetworkResult.Error(errorMessage)
                    }
                    is SocketTimeoutException -> {
                        val errorMessage =
                            "Waktu permintaan habis. Mohon periksa koneksi internet Anda dan coba lagi."
                        Log.d("SocketTimeoutException", "safeApiCall: $e ")
                        NetworkResult.Error(errorMessage)
                    }
                    else -> {
                        val errorMessage =
                            "Terjadi kesalahan saat berkomunikasi dengan server. Mohon coba lagi nanti."
                        Log.d("IOException", "safeApiCall: $e")
                        NetworkResult.Error(errorMessage)
                    }
                }
//                NetworkResult.Error("Request Time Out")
            } catch (e: Exception) {
//                NetworkResult.Error(e.toString() + "Url not found error code 404" )
                Log.d("Exception", "safeApiCall: $e")
                NetworkResult.Error("alamat URL tidak ditemukan $e")
            } catch (e: Throwable) {
                Log.d("Throwable", "safeApiCall: $e")
                NetworkResult.Error(e.toString() + "terjadi kesalahan")
            } catch (e: SocketTimeoutException) {
                Log.d("SocketTimeoutException", "safeApiCall: $e")
                NetworkResult.Error("Sesi waktu habis, periksa kembali jaringan anda")
            }
        }
    }
}