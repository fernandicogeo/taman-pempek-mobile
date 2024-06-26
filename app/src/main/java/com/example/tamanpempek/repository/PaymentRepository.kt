package com.example.tamanpempek.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.tamanpempek.api.ApiService
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.request.PaymentCreateRequest
import com.example.tamanpempek.request.PaymentUpdateStatusAndDeliveryRequest
import com.example.tamanpempek.request.PaymentUpdateStatusRequest
import com.example.tamanpempek.response.CartsResponse
import com.example.tamanpempek.response.PaymentResponse
import com.example.tamanpempek.response.PaymentsResponse
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.awaitResponse

class PaymentRepository(private val apiService: ApiService) {

    fun getPaymentById(id: Int): LiveData<ResultCondition<PaymentResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultCondition.LoadingState)
        try {
            val response =
                apiService.getPaymentById(id).awaitResponse()

            if (response.isSuccessful) {
                val cartResponse = response.body()
                if (cartResponse != null) {
                    emit(ResultCondition.SuccessState(cartResponse))
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

    fun getPaymentsByUserAndPaymentStatus(userId: Int, paymentStatus: String): LiveData<ResultCondition<PaymentsResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultCondition.LoadingState)
        try {
            val response =
                apiService.getPaymentsByUserAndPaymentStatus(userId, paymentStatus).awaitResponse()

            if (response.isSuccessful) {
                val cartResponse = response.body()
                if (cartResponse != null) {
                    emit(ResultCondition.SuccessState(cartResponse))
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

    fun getPaymentsByPaymentStatus(paymentStatus: String): LiveData<ResultCondition<PaymentsResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultCondition.LoadingState)
        try {
            val response =
                apiService.getPaymentsByPaymentStatus(paymentStatus).awaitResponse()

            if (response.isSuccessful) {
                val cartResponse = response.body()
                if (cartResponse != null) {
                    emit(ResultCondition.SuccessState(cartResponse))
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
                address = requestMap["address"]!!,
                whatsapp = requestMap["whatsapp"]!!,
                paymentStatus = requestMap["payment_status"]!!,
                deliveryName = requestMap["delivery_name"]!!,
                resi = requestMap["resi"]!!
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

    fun updatePaymentStatus(id: Int, paymentUpdateStatusRequest: PaymentUpdateStatusRequest): LiveData<ResultCondition<PaymentResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val requestMap = updatePaymentStatusRequest(paymentUpdateStatusRequest)

            val response = apiService.updatePaymentStatus(
                id = id,
                paymentStatus = requestMap["payment_status"]!!,
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

    fun updatePaymentStatusAndDelivery(id: Int, paymentUpdateStatusAndDeliveryRequest: PaymentUpdateStatusAndDeliveryRequest): LiveData<ResultCondition<PaymentResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val requestMap = paymentUpdateStatusAndDeliveryRequest(paymentUpdateStatusAndDeliveryRequest)

            val response = apiService.updatePaymentStatusAndDelivery(
                id = id,
                paymentStatus = requestMap["payment_status"]!!,
                deliveryName = requestMap["delivery_name"]!!,
                resi = requestMap["resi"]!!
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
        requestMap["address"] = RequestBody.create("text/plain".toMediaTypeOrNull(), paymentCreateRequest.address)
        requestMap["whatsapp"] = RequestBody.create("text/plain".toMediaTypeOrNull(), paymentCreateRequest.whatsapp)
        requestMap["payment_status"] = RequestBody.create("text/plain".toMediaTypeOrNull(), paymentCreateRequest.payment_status)
        requestMap["delivery_name"] = RequestBody.create("text/plain".toMediaTypeOrNull(), paymentCreateRequest.delivery_name.toString())
        requestMap["resi"] = RequestBody.create("text/plain".toMediaTypeOrNull(), paymentCreateRequest.resi.toString())

        return requestMap
    }

    private fun paymentUpdateStatusAndDeliveryRequest(paymentUpdateStatusAndDeliveryRequest: PaymentUpdateStatusAndDeliveryRequest): Map<String, RequestBody> {
        val requestMap = mutableMapOf<String, RequestBody>()
        requestMap["payment_status"] = RequestBody.create("text/plain".toMediaTypeOrNull(), paymentUpdateStatusAndDeliveryRequest.payment_status)
        requestMap["delivery_name"] = RequestBody.create("text/plain".toMediaTypeOrNull(), paymentUpdateStatusAndDeliveryRequest.delivery_name)
        requestMap["resi"] = RequestBody.create("text/plain".toMediaTypeOrNull(), paymentUpdateStatusAndDeliveryRequest.resi)

        return requestMap
    }

    private fun updatePaymentStatusRequest(paymentUpdateStatusRequest: PaymentUpdateStatusRequest): Map<String, RequestBody> {
        val requestMap = mutableMapOf<String, RequestBody>()
        requestMap["payment_status"] = RequestBody.create("text/plain".toMediaTypeOrNull(), paymentUpdateStatusRequest.payment_status)

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