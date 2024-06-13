package com.example.tamanpempek.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.tamanpempek.R


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val dropdownGender = findViewById<Spinner>(R.id.spinner_gender)
        val genders = arrayOf("Laki-laki", "Perempuan")
        val adapterGender = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genders)
        dropdownGender.adapter = adapterGender

        val dropdownRole = findViewById<Spinner>(R.id.spinner_role)
        val roles = arrayOf("Pembeli", "Penjual")
        val adapterRole = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        dropdownRole.adapter = adapterRole

    }
}