package com.example.tamanpempek.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.tamanpempek.api.ApiService
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.request.SettingRequest
import com.example.tamanpempek.response.SettingResponse
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.awaitResponse

class SettingRepository(private val apiService: ApiService) {
    fun getSettingById(id: Int): LiveData<ResultCondition<SettingResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultCondition.LoadingState)
        try {
            val response =
                apiService.getSettings(id).awaitResponse()

            if (response.isSuccessful) {
                val settingResponse = response.body()
                if (settingResponse != null) {
                    emit(ResultCondition.SuccessState(settingResponse))
                } else {
                    emit(ResultCondition.ErrorState("Empty response body"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMsg = errorBody ?: "Unknown error occurred"
                emit(ResultCondition.ErrorState(errorMsg))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message ?: "Error occurred"))
        }
    }
    
    fun updateSetting(id: Int, settingUpdateRequest: SettingRequest, context: Context): LiveData<ResultCondition<SettingResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val requestMap = updateSettingRequest(settingUpdateRequest)
            val imagePart = settingUpdateRequest.image?.let { createMultipartBodyPart(it, context) }

            val response = apiService.updateSetting(
                id = id,
                image = imagePart,
                description = requestMap["description"]!!,
                contact = requestMap["contact"]!!,
            )

            if (response.error) {
                emit(ResultCondition.ErrorState(response.msg))
            } else {
                emit(ResultCondition.SuccessState(response))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message.toString()))
        }
    }

    private fun updateSettingRequest(settingUpdateRequest: SettingRequest): Map<String, RequestBody> {
        val requestMap = mutableMapOf<String, RequestBody>()
        requestMap["description"] = RequestBody.create("text/plain".toMediaTypeOrNull(), settingUpdateRequest.description)
        requestMap["contact"] = RequestBody.create("text/plain".toMediaTypeOrNull(), settingUpdateRequest.contact)

        return requestMap
    }

    private fun createMultipartBodyPart(imageUri: Uri, context: Context): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(imageUri)
        val fileName = getFileName(imageUri, context)

        val requestFile = inputStream!!.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", fileName, requestFile)
    }

    private fun getFileName(uri: Uri, context: Context): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex != -1) {
                        result = cursor.getString(columnIndex)
                    }
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result ?: "unknown_file"
    }

    companion object {
        @Volatile
        private var instanceRepo: SettingRepository? = null
        fun getInstance(api: ApiService): SettingRepository =
            instanceRepo ?: synchronized(this) {
                instanceRepo ?: SettingRepository(api)
            }.also {
                instanceRepo = it
            }
    }
}