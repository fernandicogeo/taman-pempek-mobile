package com.example.tamanpempek.preference

import android.content.Context
import com.example.tamanpempek.model.UserModel

class UserPreference(context: Context) {

    private val pref = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)

    fun saveEditProfile(user: UserModel) {
        val prefEdit = pref.edit()
        prefEdit.putInt(USER_ID, user.id)
        prefEdit.putString(USER_NAME, user.name)
        prefEdit.putString(USER_EMAIL, user.email)
        prefEdit.putString(USER_PASSWORD, user.password)
        prefEdit.putString(USER_WHATSAPP, user.whatsapp)
        prefEdit.putString(USER_GENDER, user.gender)
        prefEdit.apply()
    }

    fun saveLoginSession(user: UserModel) {
        val prefEdit = pref.edit()
        prefEdit.putInt(USER_ID, user.id)
        prefEdit.putString(USER_NAME, user.name)
        prefEdit.putString(USER_EMAIL, user.email)
        prefEdit.putString(USER_PASSWORD, user.password)
        prefEdit.putString(USER_WHATSAPP, user.whatsapp)
        prefEdit.putString(USER_GENDER, user.gender)
        prefEdit.putString(USER_ROLE, user.role)
        prefEdit.putString(USER_TOKEN, user.accessToken)
        prefEdit.apply()
    }

    fun getLoginSession() : UserModel {
        val id = pref.getInt(USER_ID, -1)
        val name = pref.getString(USER_NAME, null)
        val email = pref.getString(USER_EMAIL, null)
        val password = pref.getString(USER_PASSWORD, null)
        val whatsapp = pref.getString(USER_WHATSAPP, null)
        val gender = pref.getString(USER_GENDER, null)
        val role = pref.getString(USER_ROLE, null)
        val accessToken = pref.getString(USER_TOKEN, null)

        return UserModel(id, name, email, password, whatsapp, gender, role, accessToken)
    }

    fun clearLoginSession() {
        val prefClear = pref.edit().clear()
        prefClear.apply()
    }



    companion object {
        private const val USER_ID = "user_id"
        private const val USER_NAME = "user_name"
        private const val USER_EMAIL = "user_email"
        private const val USER_PASSWORD = "user_password"
        private const val USER_WHATSAPP = "user_whatsapp"
        private const val USER_GENDER = "user_gender"
        private const val USER_ROLE = "user_role"
        private const val USER_TOKEN = "user_token"

        private const val USER_PREF = "user_pref"
    }
}