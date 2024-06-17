package com.example.tamanpempek.ui.seller.bank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityDetailBankSellerBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.BankModel
import com.example.tamanpempek.viewmodel.BankViewModel
import com.example.tamanpempek.viewmodel.factory.BankViewModelFactory

class DetailBankSellerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBankSellerBinding
    private val bankViewModel: BankViewModel by viewModels { factory }
    private lateinit var factory: BankViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBankSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = BankViewModelFactory.getInstanceBank(binding.root.context)

        val bankId = intent.getIntExtra("BANK_ID", -1)

        bankViewModel.getBankById(bankId).observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    setDetail(result.data.data)
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }

        binding.btnEditBank.setOnClickListener {
            val intent = Intent(this, EditBankSellerActivity::class.java).apply {
                putExtra("BANK_ID", bankId)
            }
            startActivity(intent)
        }

        binding.btnDeleteBank.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Apakah anda yakin?")
                setPositiveButton("Ya") { _, _ ->
                    deleteBank(bankId)
                }
                create()
                show()
            }
        }
    }

    private fun setDetail(bank: BankModel) {
        binding.apply {
            tvName.text = bank.name
            tvType.text = getString(R.string.type_template, bank.type)
            tvNumber.text = getString(R.string.number_template, bank.number)
        }
    }

    private fun deleteBank(id: Int) {
        bankViewModel.deleteBank(id).observe(this) {
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
                setTitle("Hapus bank berhasil!!")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(this@DetailBankSellerActivity, BankSellerActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Hapus bank gagal!")
                setMessage("Silakan coba lagi.")
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