package com.example.tamanpempek.ui.seller.bank

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
import com.example.tamanpempek.databinding.ActivityAddBankSellerBinding
import com.example.tamanpempek.databinding.ActivityEditBankSellerBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.response.BankResponse
import com.example.tamanpempek.response.ProductResponse
import com.example.tamanpempek.viewmodel.BankViewModel
import com.example.tamanpempek.viewmodel.factory.BankViewModelFactory

class EditBankSellerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBankSellerBinding
    private val bankViewModel: BankViewModel by viewModels { factory }
    private lateinit var factory: BankViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBankSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = BankViewModelFactory.getInstanceBank(binding.root.context)
        val bankId = intent.getIntExtra("BANK_ID", -1)
        if (bankId != -1) {
            getBankDetail(bankId)
        }
        setupView()
        setupAction(bankId)
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

    private fun setupAction(bankId: Int) {
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
                    updateBank(bankId, name, type, number)
                }
            }
        }
    }

    private fun updateBank(id: Int, name: String, type: String, number: String) {
        bankViewModel.updateBank(id, name, type, number).observe(this) {
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

    private fun getBankDetail(id: Int) {
        bankViewModel.getBankById(id).observe(this) {
            when (it) {
                is ResultCondition.LoadingState -> {
                    showLoading(true)
                }
                is ResultCondition.ErrorState -> {
                    showLoading(false)
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    populateBankDetails(it.data)
                }
            }
        }
    }

    private fun populateBankDetails(bank: BankResponse) {
        binding.etName.setText(bank.data.name)
        binding.etType.setText(bank.data.type)
        binding.etNumber.setText(bank.data.number)
    }

    private fun showDialog(isSuccess: Boolean) {
        if (isSuccess) {
            AlertDialog.Builder(this).apply {
                setTitle("Edit bank berhasil!!")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(this@EditBankSellerActivity, BankSellerActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Edit bank gagal!")
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