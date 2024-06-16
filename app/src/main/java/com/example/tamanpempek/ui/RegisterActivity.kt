package com.example.tamanpempek.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityRegisterBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.ui.customview.SubmitButton
import com.example.tamanpempek.viewmodel.RegisterViewModel
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels { factory }
    private lateinit var factory: UserViewModelFactory

    private lateinit var registerTitleTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var passwordTextView: TextView
    private lateinit var whatsappTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var roleTextView: TextView
    private lateinit var linkLoginTextView: TextView

    private lateinit var registerButton: SubmitButton
    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var whatsappEditText: TextInputEditText
    private lateinit var genderSpinner: Spinner
    private lateinit var roleSpinner: Spinner
    private lateinit var nameEditTextLayout: TextInputLayout
    private lateinit var emailEditTextLayout: TextInputLayout
    private lateinit var passwordEditTextLayout: TextInputLayout
    private lateinit var whatsappEditTextLayout: TextInputLayout
    private lateinit var progressBar: ProgressBar

    private lateinit var selectedGender: String
    private lateinit var selectedRole: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        factory = UserViewModelFactory.getInstanceAuth(binding.root.context)

        setupView()
        setupAction()

        textChangedListener()
        setTextViewEnable()

        linkLoginTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
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

        registerTitleTextView = findViewById(R.id.tv_register_title)
        nameTextView = findViewById(R.id.tv_name)
        emailTextView = findViewById(R.id.tv_email)
        passwordTextView = findViewById(R.id.tv_password)
        whatsappTextView = findViewById(R.id.tv_whatsapp)
        genderTextView = findViewById(R.id.tv_gender)
        roleTextView = findViewById(R.id.tv_role)
        linkLoginTextView = findViewById(R.id.link_login)

        registerButton = findViewById(R.id.btn_register)
        nameEditText = findViewById(R.id.et_name)
        emailEditText = findViewById(R.id.et_email)
        passwordEditText = findViewById(R.id.et_password)
        whatsappEditText = findViewById(R.id.et_whatsapp)
        genderSpinner = findViewById(R.id.spinner_gender)
        roleSpinner = findViewById(R.id.spinner_role)
        nameEditTextLayout = findViewById(R.id.etl_name)
        emailEditTextLayout = findViewById(R.id.etl_email)
        passwordEditTextLayout = findViewById(R.id.etl_password)
        whatsappEditTextLayout = findViewById(R.id.etl_whatsapp)
        progressBar = findViewById(R.id.progressBar)

        val genders = arrayOf("Laki-laki", "Perempuan")
        val adapterGender = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genders)
        genderSpinner.adapter = adapterGender

        val roles = arrayOf("Pembeli", "Penjual")
        val adapterRole = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        roleSpinner.adapter = adapterRole

        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedGender = genders[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedRole = roles[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun setupAction() {
        showLoading(false)
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val whatsapp = whatsappEditText.text.toString()
            val gender = selectedGender
            val role = selectedRole
            when {
                name.isEmpty() -> {
                    emailEditTextLayout.error = "Masukkan nama"
                }
                email.isEmpty() -> {
                    emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    passwordEditTextLayout.error = "Masukkan password"
                }
                whatsapp.isEmpty() -> {
                    whatsappEditTextLayout.error = "Masukkan Nomor Whatsapp"
                }
                else -> {
                    register(name, email, password, whatsapp, gender, role)
                }
            }
        }
    }

    private fun textChangedListener() {
        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setTextViewEnable()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setTextViewEnable()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setTextViewEnable()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        whatsappEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setTextViewEnable()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun register(userName: String, userEmail: String, userPassword: String, userWhatsapp: String, userGender: String, userRole: String) {
        registerViewModel.register(userName, userEmail, userPassword, userWhatsapp, userGender, userRole).observe(this) {
            if (it != null) {
                when (it) {
                    is ResultCondition.LoadingState -> {
                        showLoading(true)
                    } is ResultCondition.ErrorState -> {
                    showLoading(false)
                    showDialog(false)
                    } is ResultCondition.SuccessState -> {
                        showLoading(false)
                        showDialog(true)
                    }
                }
            }
        }
    }

    private fun showDialog(isSuccess: Boolean) {
        if (isSuccess) {
            AlertDialog.Builder(this).apply {
                setTitle("Register berhasil!!")
                setMessage("Anda sudah terdaftar di TamanPempek! Silakan login.")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Register gagal!")
                setMessage("Format email anda salah/sudah terdaftar!")
                create()
                show()
            }
        }
    }

    private fun setTextViewEnable() {
        val resultName = nameEditText.text
        val resultEmail = emailEditText.text
        val resultPassword = passwordEditText.text
        val resultWhatsapp = whatsappEditText.text
        registerButton.isEnabled = resultName.toString().length >= 3 && resultEmail.toString().length >= 3 && resultPassword.toString().length >= 3 && resultWhatsapp.toString().length >= 3
}

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }
}