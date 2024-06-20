package com.example.tamanpempek.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.tamanpempek.api.ApiService
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.request.PaymentCreateRequest
import com.example.tamanpempek.response.PaymentResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class PaymentRepository(private val apiService: ApiService) {
    fun createPayment(paymentCreateRequest: PaymentCreateRequest, context: Context): LiveData<ResultCondition<PaymentResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val requestMap = createPaymentRequest(paymentCreateRequest)
            val imagePart = createMultipartBodyPart(paymentCreateRequest.image, context)

            val response = apiService.createPayment(
                userId = requestMap["user_id"]!!,
                deliveryId = requestMap["delivery_id"]!!,
                totalPrice = requestMap["total_price"]!!,
                image = imagePart,
                paymentStatus = requestMap["payment_status"]!!,
                deliveryStatus = requestMap["delivery_status"]!!
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

    private fun createPaymentRequest(paymentCreateRequest: PaymentCreateRequest): Map<String, RequestBody> {
        val requestMap = mutableMapOf<String, RequestBody>()
        requestMap["user_id"] = RequestBody.create("text/plain".toMediaTypeOrNull(), paymentCreateRequest.user_id.toString())
        requestMap["delivery_id"] = RequestBody.create("text/plain".toMediaTypeOrNull(), paymentCreateRequest.delivery_id.toString())
        requestMap["total_price"] = RequestBody.create("text/plain".toMediaTypeOrNull(), paymentCreateRequest.total_price.toString())
        requestMap["payment_status"] = RequestBody.create("text/plain".toMediaTypeOrNull(), paymentCreateRequest.payment_status)
        requestMap["delivery_status"] = RequestBody.create("text/plain".toMediaTypeOrNull(), paymentCreateRequest.delivery_status.toString())

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
        private var instanceRepo: PaymentRepository? = null
        fun getInstance(api: ApiService): PaymentRepository =
            instanceRepo ?: synchronized(this) {
                instanceRepo ?: PaymentRepository(api)
            }.also {
                instanceRepo = it
            }
    }
}