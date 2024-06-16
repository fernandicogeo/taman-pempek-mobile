package com.example.tamanpempek.ui.seller.bank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
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
    }

    private fun setDetail(bank: BankModel) {
        binding.apply {
            tvName.text = bank.name
            tvType.text = getString(R.string.type_template, bank.type)
            tvNumber.text = getString(R.string.number_template, bank.number)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}