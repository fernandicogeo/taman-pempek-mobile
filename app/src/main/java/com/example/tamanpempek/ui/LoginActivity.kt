package com.example.tamanpempek.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.tamanpempek.databinding.ActivityLoginBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.UserModel
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.response.LoginResponse
import com.example.tamanpempek.ui.seller.product.DashboardSellerActivity
import com.example.tamanpempek.ui.user.DashboardUserActivity
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels { factory }
    private lateinit var factory: UserViewModelFactory
    private lateinit var preference: UserPreference
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = UserViewModelFactory.getInstanceAuth(binding.root.context)
        preference = UserPreference(this)

        setupView()
        setupAction()
        isLoginSession()

        binding.linkRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishAffinity()
            exitProcess(0)
        }
        return super.onKeyDown(keyCode, event)
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
    }

    private fun login(userEmail: String, userPassword: String) {
        userViewModel.login(userEmail, userPassword).observe(this) {
            if (it != null) {
                when (it) {
                    is ResultCondition.LoadingState -> {
                        showLoading(true)
                    } is ResultCondition.ErrorState -> {
                        showLoading(false)
                        showDialog(false)
                    } is ResultCondition.SuccessState -> {
                        showLoading(false)
                        saveLoginSession(it.data)
                        showDialog(true)
                    }
                }
            }
        }
    }

    private fun isLoginSession() {
        if (preference.getLoginSession().name != null && preference.getLoginSession().email != null && preference.getLoginSession().accessToken != null)
            if (preference.getLoginSession().role == "Pembeli") startActivity(Intent(this, DashboardUserActivity::class.java))
            else if (preference.getLoginSession().role == "Penjual") startActivity(Intent(this, DashboardSellerActivity::class.java))
    }

    private fun showDialog(isSuccess: Boolean) {
        if (isSuccess) {
            AlertDialog.Builder(this).apply {
                setTitle("Login berhasil!!")
                setMessage("Selamat datang di Taman Pempek!.")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(context, DashboardSellerActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Login gagal!")
                setMessage("Email atau password anda salah!")
                create()
                show()
            }
        }
    }

    private fun saveLoginSession(login: LoginResponse) {
        val loginPref = UserPreference(this)
        val loginResult = login.data
        val user = UserModel(id = loginResult.id, name = loginResult.name, email = loginResult.email, password = loginResult.password, whatsapp = loginResult.whatsapp, gender = loginResult.gender, role = loginResult.role, accessToken = login.token)
        loginPref.saveLoginSession(user)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    private fun setupAction() {
        showLoading(false)
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.etlEmail.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.etlPassword.error = "Masukkan password"
                }
                else -> {
                    login(email, password)
                }
            }
        }
    }
}