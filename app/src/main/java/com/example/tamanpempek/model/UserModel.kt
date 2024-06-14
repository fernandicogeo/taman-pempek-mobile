package com.example.tamanpempek.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var whatsapp: String? = null,
    var gender: String? = null,
    var role: String? = null
) : Parcelable