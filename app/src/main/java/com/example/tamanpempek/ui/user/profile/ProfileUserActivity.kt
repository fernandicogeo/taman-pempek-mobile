package com.example.tamanpempek.ui.user.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityProfileSellerBinding
import com.example.tamanpempek.databinding.ActivityProfileUserBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.UserModel
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.ui.seller.profile.EditProfileSellerActivity
import com.example.tamanpempek.ui.user.cart.CartUserActivity
import com.example.tamanpempek.ui.user.history.HistoryUserActivity
import com.example.tamanpempek.ui.user.product.DashboardUserActivity
import com.example.tamanpempek.ui.user.setting.SettingUserActivity
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory

class ProfileUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileUserBinding
    private val userViewModel: UserViewModel by viewModels { factory }
    private lateinit var factory: UserViewModelFactory
    private lateinit var preference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = UserViewModelFactory.getInstanceAuth(binding.root.context)
        preference = UserPreference(this)
        val userId = preference.getLoginSession().id

        userViewModel.getUserById(userId).observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    detailUser(result.data.data)
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileUserActivity::class.java)
            startActivity(intent)
        }

        bottomNav()
    }

    private fun detailUser(user: UserModel) {
        binding.apply {
            tvName.text = getString(R.string.name_template, user.name)
            tvEmail.text = getString(R.string.email_template, user.email)
            tvPassword.text = getString(R.string.password_template, user.password)
            tvWhatsapp.text = getString(R.string.whatsapp_template, user.whatsapp)
            tvGender.text = getString(R.string.gender_template, user.gender)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
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
        binding.bottomNavigationView.setSelectedItemId(R.id.profile_user)
    }
}