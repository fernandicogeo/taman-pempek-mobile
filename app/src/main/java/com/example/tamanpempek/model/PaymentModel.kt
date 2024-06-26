package com.example.tamanpempek.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentModel(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val user_id: Int,

    @SerializedName("bank_id")
    val bank_id: Int,

    @SerializedName("delivery_id")
    val delivery_id: Int?,

    @SerializedName("total_price")
    val total_price: Int,

    @SerializedName("image")
    val image: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("whatsapp")
    val whatsapp: String,

    @SerializedName("payment_status")
    val payment_status: String, // reviewed, rejected, waiting for sent, canceled, sent, finished

    @SerializedName("delivery_name")
    val delivery_name: String?,

    @SerializedName("resi")
    val resi: String?,
) : Parcelable
