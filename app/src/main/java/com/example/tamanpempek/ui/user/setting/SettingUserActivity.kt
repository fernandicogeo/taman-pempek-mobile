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
import com.example.tamanpempek.ui.user.cart.CartUserActivity
import com.example.tamanpempek.ui.user.history.HistoryUserActivity
import com.example.tamanpempek.ui.user.product.DashboardUserActivity
import com.example.tamanpempek.ui.user.profile.ProfileUserActivity
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
                R.id.dashboard_user -> {
                    startActivity(Intent(this, DashboardUserActivity::class.java))
                }
                R.id.cart_user -> {
                    startActivity(Intent(this, CartUserActivity::class.java))
                }
                R.id.history_user -> {
                    startActivity(Intent(this, HistoryUserActivity::class.java))
                }
                R.id.profile_user -> {
                    startActivity(Intent(this, ProfileUserActivity::class.java))
                }
                R.id.setting_user -> {
                    startActivity(Intent(this, SettingUserActivity::class.java))
                }
            }
        }
        binding.bottomNavigationView.setSelectedItemId(R.id.setting_user)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}