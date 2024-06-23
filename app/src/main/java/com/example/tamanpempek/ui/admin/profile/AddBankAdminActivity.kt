package com.example.tamanpempek.ui.admin.profile

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityAddBankAdminBinding
import com.example.tamanpempek.databinding.ActivityAddBankSellerBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.ui.seller.bank.BankSellerActivity
import com.example.tamanpempek.viewmodel.BankViewModel
import com.example.tamanpempek.viewmodel.factory.BankViewModelFactory

class AddBankAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBankAdminBinding
    private val bankViewModel: BankViewModel by viewModels { factory }
    private lateinit var factory: BankViewModelFactory
    private lateinit var preference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBankAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = BankViewModelFactory.getInstanceBank(binding.root.context)
        preference = UserPreference(this)

        setupView()
        setupAction()
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

    private fun setupAction() {
        val userId = preference.getLoginSession().id

        showLoading(false)

        binding.btnSubmit.setOnClickListener {
            val name = binding.etName.text.toString()
            val type = binding.etType.text.toString()
            val number = binding.etNumber.text.toString()
            when {
                name.isEmpty() -> {
                    binding.etlName.error = "Masukkan nama"
                }
                type.isEmpty() -> {
                    binding.etlType.error = "Masukkan bank"
                }
                number.isEmpty() -> {
                    binding.etlNumber.error = "Masukkan nomor rekening"
                }
                else -> {
                    createBank(userId, name, type, number)
                }
            }
        }
    }

    private fun createBank(userId: Int, name: String, type: String, number: String) {
        bankViewModel.createBank(userId, name, type, number).observe(this) {
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

    private fun showDialog(isSuccess: Boolean) {
        if (isSuccess) {
            AlertDialog.Builder(this).apply {
                setTitle("Tambah bank berhasil!!")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(this@AddBankAdminActivity, ProfileAdminActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Tambah bank gagal!")
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