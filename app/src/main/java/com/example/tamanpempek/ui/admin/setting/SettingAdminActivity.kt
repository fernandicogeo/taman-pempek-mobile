package com.example.tamanpempek.ui.admin.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivitySettingAdminBinding
import com.example.tamanpempek.databinding.ActivitySettingUserBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.ui.LoginActivity
import com.example.tamanpempek.ui.admin.payment.PaymentAdminActivity
import com.example.tamanpempek.ui.admin.product.DashboardAdminActivity
import com.example.tamanpempek.ui.admin.profile.ProfileAdminActivity
import com.example.tamanpempek.ui.admin.users.UserAdminActivity
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory

class SettingAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingAdminBinding
    private val userViewModel: UserViewModel by viewModels { factory }
    private lateinit var factory: UserViewModelFactory
    private lateinit var preference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingAdminBinding.inflate(layoutInflater)
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    private fun bottomNav() {
        binding.bottomNavigationView.setOnNavigationItemReselectedListener { item ->
            when(item.itemId) {
                R.id.dashboard_admin -> {
                    startActivity(Intent(this, DashboardAdminActivity::class.java))
                }
                R.id.user_admin -> {
                    startActivity(Intent(this, UserAdminActivity::class.java))
                }
                R.id.payment_admin -> {
                    startActivity(Intent(this, PaymentAdminActivity::class.java))
                }
                R.id.profile_admin -> {
                    startActivity(Intent(this, ProfileAdminActivity::class.java))
                }
                R.id.setting_admin -> {
                    startActivity(Intent(this, SettingAdminActivity::class.java))
                }
            }
        }
        binding.bottomNavigationView.setSelectedItemId(R.id.setting_admin)
    }
}