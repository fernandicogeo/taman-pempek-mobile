package com.example.tamanpempek.request

data class PaymentUpdateStatusAndDeliveryRequest (
    val payment_status: String,
    val delivery_name: String,
    val resi: String
)