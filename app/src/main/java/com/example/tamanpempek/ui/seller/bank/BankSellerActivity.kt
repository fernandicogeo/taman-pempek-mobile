package com.example.tamanpempek.ui.seller.bank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityBankSellerBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.ui.adapter.seller.bank.BankAdapter
import com.example.tamanpempek.ui.seller.product.DashboardSellerActivity
import com.example.tamanpempek.ui.seller.profile.ProfileSellerActivity
import com.example.tamanpempek.ui.seller.setting.SettingSellerActivity
import com.example.tamanpempek.viewmodel.BankViewModel
import com.example.tamanpempek.viewmodel.factory.BankViewModelFactory

class BankSellerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBankSellerBinding
    private val bankViewModel: BankViewModel by viewModels { factory }
    private lateinit var factory: BankViewModelFactory
    private lateinit var preference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBankSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = BankViewModelFactory.getInstanceBank(binding.root.context)
        preference = UserPreference(this)

        setupRecyclerView()
        bottomNav()

        binding.btnAddRekening.setOnClickListener {
            startActivity(Intent(this, AddBankSellerActivity::class.java))
        }

        val userId = preference.getLoginSession().id

        bankViewModel.getBanksByUser(userId).observe(this) {
            showLoading(true)
            when (it) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    val adapter = BankAdapter(it.data.data)
                    binding.rvRekening.adapter = adapter
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvRekening.apply {
            layoutManager = GridLayoutManager(this@BankSellerActivity, 1)
            setHasFixedSize(true)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    private fun bottomNav() {
        binding.bottomNavigationView.setOnNavigationItemReselectedListener { item ->
            when(item.itemId) {
                R.id.dashboard -> {
                    startActivity(Intent(this, DashboardSellerActivity::class.java))
                }
                R.id.rekening -> {
                    startActivity(Intent(this, BankSellerActivity::class.java))
                }
                R.id.profil -> {
                    startActivity(Intent(this, ProfileSellerActivity::class.java))
                }
                R.id.setting -> {
                    startActivity(Intent(this, SettingSellerActivity::class.java))
                }
            }
        }
        binding.bottomNavigationView.setSelectedItemId(R.id.rekening)
    }
}