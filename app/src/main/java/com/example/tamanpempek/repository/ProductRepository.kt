package com.example.tamanpempek.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.tamanpempek.api.ApiService
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.request.ProductCreateRequest
import com.example.tamanpempek.request.ProductUpdateRequest
import com.example.tamanpempek.response.ProductResponse
import com.example.tamanpempek.response.ProductsResponse
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.awaitResponse
import java.io.File
import java.io.InputStream

class ProductRepository(private val apiService: ApiService) {
    fun getProducts() : LiveData<ResultCondition<ProductsResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val response = apiService.getProducts()
            if (response.error) {
                emit(ResultCondition.ErrorState(response.msg))
            } else {
                emit(ResultCondition.SuccessState(response))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message.toString()))
        }
    }

    fun getUserProductsByCategory(userId: Int, categoryId: Int): LiveData<ResultCondition<ProductsResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultCondition.LoadingState)
        try {
            val response =
                apiService.getUserProductsByCategory(userId, categoryId).awaitResponse()

            if (response.isSuccessful) {
                val productResponse = response.body()
                if (productResponse != null) {
                    emit(ResultCondition.SuccessState(productResponse))
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

    fun getProductById(userId: Int): LiveData<ResultCondition<ProductResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultCondition.LoadingState)
        try {
            val response =
                apiService.getProductById(userId).awaitResponse()

            if (response.isSuccessful) {
                val productResponse = response.body()
                if (productResponse != null) {
                    emit(ResultCondition.SuccessState(productResponse))
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

    fun createProduct(productCreateRequest: ProductCreateRequest, context: Context): LiveData<ResultCondition<ProductResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val requestMap = createProductRequest(productCreateRequest)
            val imagePart = createMultipartBodyPart(productCreateRequest.image, context)

            val response = apiService.createProduct(
                userId = requestMap["user_id"]!!,
                categoryId = requestMap["category_id"]!!,
                name = requestMap["name"]!!,
                image = imagePart,
                description = requestMap["description"]!!,
                price = requestMap["price"]!!,
                stock = requestMap["stock"]!!
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

    fun updateProduct(id: Int, productUpdateRequest: ProductUpdateRequest, context: Context): LiveData<ResultCondition<ProductResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val requestMap = updateProductRequest(productUpdateRequest)
            val imagePart = productUpdateRequest.image?.let { createMultipartBodyPart(it, context) }

            val response = apiService.updateProduct(
                id = id,
                userId = requestMap["user_id"]!!,
                categoryId = requestMap["category_id"]!!,
                name = requestMap["name"]!!,
                image = imagePart,
                description = requestMap["description"]!!,
                price = requestMap["price"]!!,
                stock = requestMap["stock"]!!
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

    fun deleteProduct(userId: Int): LiveData<ResultCondition<ProductResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val response = apiService.deleteProduct(userId)
            if (response.error) {
                emit(ResultCondition.ErrorState(response.msg))
            } else {
                emit(ResultCondition.SuccessState(response))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message.toString()))
        }
    }

    private fun createProductRequest(productCreateRequest: ProductCreateRequest): Map<String, RequestBody> {
        val requestMap = mutableMapOf<String, RequestBody>()
        requestMap["user_id"] = RequestBody.create("text/plain".toMediaTypeOrNull(), productCreateRequest.user_id.toString())
        requestMap["category_id"] = RequestBody.create("text/plain".toMediaTypeOrNull(), productCreateRequest.category_id.toString())
        requestMap["name"] = RequestBody.create("text/plain".toMediaTypeOrNull(), productCreateRequest.name)
        requestMap["description"] = RequestBody.create("text/plain".toMediaTypeOrNull(), productCreateRequest.description)
        requestMap["price"] = RequestBody.create("text/plain".toMediaTypeOrNull(), productCreateRequest.price.toString())
        requestMap["stock"] = RequestBody.create("text/plain".toMediaTypeOrNull(), productCreateRequest.stock.toString())

        return requestMap
    }

    private fun updateProductRequest(productUpdateRequest: ProductUpdateRequest): Map<String, RequestBody> {
        val requestMap = mutableMapOf<String, RequestBody>()
        requestMap["user_id"] = RequestBody.create("text/plain".toMediaTypeOrNull(), productUpdateRequest.user_id.toString())
        requestMap["category_id"] = RequestBody.create("text/plain".toMediaTypeOrNull(), productUpdateRequest.category_id.toString())
        requestMap["name"] = RequestBody.create("text/plain".toMediaTypeOrNull(), productUpdateRequest.name)
        requestMap["description"] = RequestBody.create("text/plain".toMediaTypeOrNull(), productUpdateRequest.description)
        requestMap["price"] = RequestBody.create("text/plain".toMediaTypeOrNull(), productUpdateRequest.price.toString())
        requestMap["stock"] = RequestBody.create("text/plain".toMediaTypeOrNull(), productUpdateRequest.stock.toString())

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
        private var instanceRepo: ProductRepository? = null
        fun getInstance(api: ApiService): ProductRepository =
            instanceRepo ?: synchronized(this) {
                instanceRepo ?: ProductRepository(api)
            }.also {
                instanceRepo = it
            }
    }
}