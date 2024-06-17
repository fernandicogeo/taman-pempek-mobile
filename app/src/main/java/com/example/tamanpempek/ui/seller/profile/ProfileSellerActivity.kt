package com.example.tamanpempek.ui.seller.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityAddProductSellerBinding
import com.example.tamanpempek.databinding.ActivityProfileSellerBinding
import com.example.tamanpempek.databinding.ActivityRegisterBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.BankModel
import com.example.tamanpempek.model.UserModel
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.ui.seller.bank.BankSellerActivity
import com.example.tamanpempek.ui.seller.product.DashboardSellerActivity
import com.example.tamanpempek.ui.seller.product.EditProductSellerActivity
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.ProductViewModelFactory
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory

class ProfileSellerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileSellerBinding
    private val userViewModel: UserViewModel by viewModels { factory }
    private lateinit var factory: UserViewModelFactory
    private lateinit var preference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSellerBinding.inflate(layoutInflater)
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
            val intent = Intent(this, EditProfileSellerActivity::class.java)
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
            }
        }
        binding.bottomNavigationView.setSelectedItemId(R.id.profil)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}