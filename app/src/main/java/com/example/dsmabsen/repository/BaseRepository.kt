package com.example.dsmabsen.repository

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
                NetworkResult.Error(e.message ?: "terjadi kesalahan")
            } catch (e: IOException) {
//                No Connection or URL error or bad connection
                if (e is ConnectException || e is SocketTimeoutException) {
                    NetworkResult.Error("koneksi internet buruk harap periksa kembali koneksi anda")
                } else {
                    NetworkResult.Error("tidak ada koneksi internet")
                }
//                NetworkResult.Error("Request Time Out")
            } catch (e: Exception) {
//                NetworkResult.Error(e.toString() + "Url not found error code 404" )
                NetworkResult.Error("alamat URL tidak ditemukan")
            } catch (e: Throwable) {
                NetworkResult.Error(e.toString() + "terjadi kesalahan")
            } catch (e: SocketTimeoutException) {
                NetworkResult.Error("Sesi waktu habis, periksa kembali jaringan anda")
            }
        }
    }
}