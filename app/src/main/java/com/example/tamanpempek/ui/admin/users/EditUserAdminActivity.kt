package com.example.tamanpempek.ui.admin.users

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityEditProfileUserBinding
import com.example.tamanpempek.databinding.ActivityEditUserAdminBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.response.UserResponse
import com.example.tamanpempek.ui.user.profile.ProfileUserActivity
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory

class EditUserAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditUserAdminBinding
    private val userViewModel: UserViewModel by viewModels { factory }
    private lateinit var factory: UserViewModelFactory
    private lateinit var preference: UserPreference
    private lateinit var selectedGender: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = UserViewModelFactory.getInstanceAuth(binding.root.context)
        val userId = intent.getIntExtra("USER_ID", -1)

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
                setTitle("Edit pengguna berhasil!!")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(this@EditUserAdminActivity, UserAdminActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Edit pengguna gagal!")
                setMessage("Format inputan anda salah, coba lagi.")
                create()
                show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}