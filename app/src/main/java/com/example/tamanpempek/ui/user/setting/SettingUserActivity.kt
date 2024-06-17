package com.example.tamanpempek.ui.user.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivitySettingUserBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.ui.LoginActivity
import com.example.tamanpempek.ui.seller.bank.BankSellerActivity
import com.example.tamanpempek.ui.seller.product.DashboardSellerActivity
import com.example.tamanpempek.ui.seller.profile.ProfileSellerActivity
import com.example.tamanpempek.ui.seller.setting.SettingSellerActivity
import com.example.tamanpempek.ui.user.product.DashboardUserActivity
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory

class SettingUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingUserBinding
    private val userViewModel: UserViewModel by viewModels { factory }
    private lateinit var factory: UserViewModelFactory
    private lateinit var preference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = UserViewModelFactory.getInstanceAuth(binding.root.context)
        preference = UserPreference(this)

        showLoading(false)

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
                    startActivity(Intent(this, DashboardUserActivity::class.java))
                }
                R.id.setting -> {
                    startActivity(Intent(this, SettingUserActivity::class.java))
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