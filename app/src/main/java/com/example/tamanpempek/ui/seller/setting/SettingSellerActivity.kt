package com.example.tamanpempek.ui.seller.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityEditProfileSellerBinding
import com.example.tamanpempek.databinding.ActivitySettingSellerBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.ui.LoginActivity
import com.example.tamanpempek.ui.seller.bank.BankSellerActivity
import com.example.tamanpempek.ui.seller.order.OrderSellerActivity
import com.example.tamanpempek.ui.seller.product.DashboardSellerActivity
import com.example.tamanpempek.ui.seller.profile.ProfileSellerActivity
import com.example.tamanpempek.viewmodel.SettingViewModel
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.SettingViewModelFactory
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory
import com.google.android.gms.location.LocationServices

class SettingSellerActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingSellerBinding
    private val userViewModel: UserViewModel by viewModels { factory }
    private lateinit var factory: UserViewModelFactory
    private val settingViewModel: SettingViewModel by viewModels { settingFactory }
    private lateinit var settingFactory: SettingViewModelFactory
    private lateinit var preference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = UserViewModelFactory.getInstanceAuth(binding.root.context)
        settingFactory = SettingViewModelFactory.getInstanceSetting(binding.root.context)
        preference = UserPreference(this)

        getSetting()

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Apakah anda yakin?")
                setPositiveButton("Ya") { _, _ ->
                    logout()
                }
                create()
                show()
            }
        }
        bottomNav()
    }

    private fun getSetting() {
        settingViewModel.getSettingById(1).observe(this) {
            showLoading(true)
            when (it) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    binding.apply {
                        tvAbout.text = it.data.data.description
                        tvContact.text = it.data.data.contact
                    }
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun logout() {
        userViewModel.logout().observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    preference.clearLoginSession()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun bottomNav() {
        binding.bottomNavigationView.setOnNavigationItemReselectedListener { item ->
            when(item.itemId) {
                R.id.dashboard -> {
                    startActivity(Intent(this, DashboardSellerActivity::class.java))
                }
                R.id.order -> {
                    startActivity(Intent(this, OrderSellerActivity::class.java))
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
        binding.bottomNavigationView.setSelectedItemId(R.id.setting)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}