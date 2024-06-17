package com.example.tamanpempek.ui.seller.profile

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityEditProfileSellerBinding
import com.example.tamanpempek.databinding.ActivityProfileSellerBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.UserModel
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.request.ProductUpdateRequest
import com.example.tamanpempek.request.UserUpdateRequest
import com.example.tamanpempek.response.LoginResponse
import com.example.tamanpempek.response.ProductResponse
import com.example.tamanpempek.response.UserResponse
import com.example.tamanpempek.ui.seller.product.DashboardSellerActivity
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory

class EditProfileSellerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileSellerBinding
    private val userViewModel: UserViewModel by viewModels { factory }
    private lateinit var factory: UserViewModelFactory
    private lateinit var preference: UserPreference
    private lateinit var selectedGender: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = UserViewModelFactory.getInstanceAuth(binding.root.context)
        preference = UserPreference(this)
        val userId = preference.getLoginSession().id
        if (userId != -1) {
            getUserDetail(userId)
        }

        setupView()
        setupAction(userId)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        val genders = arrayOf("Laki-laki", "Perempuan")
        val adapterGender = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genders)
        binding.spinnerGender.adapter = adapterGender

        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedGender = genders[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun setupAction(id: Int) {
        showLoading(false)

        binding.btnEditProfile.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val whatsapp = binding.etWhatsapp.text.toString()
            val gender = selectedGender
            when {
                name.isEmpty() -> {
                    binding.etlName.error = "Masukkan nama"
                }
                email.isEmpty() -> {
                    binding.etlEmail.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.etlPassword.error = "Masukkan password"
                }
                whatsapp.isEmpty() -> {
                    binding.etlWhatsapp.error = "Masukkan nomor whatsapp"
                }
                else -> {
                    updateUser(id, name, email, password, whatsapp, gender)
                }
            }
        }
    }

    private fun updateUser(id: Int, userName: String, userEmail: String, userPassword: String, userWhatsapp: String, userGender: String) {
        userViewModel.updateUser(id, userName, userEmail, userPassword, userWhatsapp, userGender).observe(this) {
            when (it) {
                is ResultCondition.LoadingState -> {
                    showLoading(true)
                }
                is ResultCondition.ErrorState -> {
                    showLoading(false)
                    showDialog(false)
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    showDialog(true)
                    saveLoginSession(it.data)
                }
            }
        }
    }

    private fun getUserDetail(userId: Int) {
        userViewModel.getUserById(userId).observe(this) {
            when (it) {
                is ResultCondition.LoadingState -> {
                    showLoading(true)
                }
                is ResultCondition.ErrorState -> {
                    showLoading(false)
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    populateUserDetails(it.data)
                }
            }
        }
    }

    private fun populateUserDetails(user: UserResponse) {
        binding.etName.setText(user.data.name)
        binding.etEmail.setText(user.data.email)
        binding.etPassword.setText(user.data.password)
        binding.etWhatsapp.setText(user.data.whatsapp)

        val genders = arrayOf("Laki-laki", "Perempuan")
        val genderIndex = genders.indexOf(user.data.gender)
        if (genderIndex != -1) {
            binding.spinnerGender.setSelection(genderIndex)
        } else {
            binding.spinnerGender.setSelection(0)
        }
    }

    private fun showDialog(isSuccess: Boolean) {
        if (isSuccess) {
            AlertDialog.Builder(this).apply {
                setTitle("Edit profil berhasil!!")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(this@EditProfileSellerActivity, ProfileSellerActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Edit profil gagal!")
                setMessage("Format inputan anda salah, coba lagi.")
                create()
                show()
            }
        }
    }

    private fun saveLoginSession(data: UserResponse) {
        val loginPref = UserPreference(this)
        val loginResult = data.data
        val user = UserModel(id = loginResult.id, name = loginResult.name, email = loginResult.email, password = loginResult.password, whatsapp = loginResult.whatsapp, gender = loginResult.gender)
        loginPref.saveEditProfile(user)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}