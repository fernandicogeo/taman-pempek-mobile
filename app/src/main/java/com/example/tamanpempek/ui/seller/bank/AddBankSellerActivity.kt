package com.example.tamanpempek.ui.seller.bank

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityAddBankSellerBinding
import com.example.tamanpempek.databinding.ActivityBankSellerBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.request.ProductCreateRequest
import com.example.tamanpempek.ui.seller.product.DashboardSellerActivity
import com.example.tamanpempek.viewmodel.BankViewModel
import com.example.tamanpempek.viewmodel.factory.BankViewModelFactory

class AddBankSellerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBankSellerBinding
    private val bankViewModel: BankViewModel by viewModels { factory }
    private lateinit var factory: BankViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBankSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = BankViewModelFactory.getInstanceBank(binding.root.context)

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
                    createBank(name, type, number)
                }
            }
        }
    }

    private fun createBank(name: String, type: String, number: String) {
        bankViewModel.createBank(name, type, number).observe(this) {
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
                    val intent = Intent(this@AddBankSellerActivity, BankSellerActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Tambah banj gagal!")
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