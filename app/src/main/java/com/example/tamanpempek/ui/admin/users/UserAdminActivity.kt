package com.example.tamanpempek.ui.admin.users

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityProfileUserBinding
import com.example.tamanpempek.databinding.ActivityUserAdminBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.PaymentModel
import com.example.tamanpempek.model.UserModel
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.ui.adapter.admin.users.SectionPagerAdapterUserAdmin
import com.example.tamanpempek.ui.admin.payment.PaymentAdminActivity
import com.example.tamanpempek.ui.admin.product.DashboardAdminActivity
import com.example.tamanpempek.ui.admin.profile.ProfileAdminActivity
import com.example.tamanpempek.ui.admin.setting.SettingAdminActivity
import com.example.tamanpempek.ui.user.history.HistoryUserActivity
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserAdminBinding
    private val userViewModel: UserViewModel by viewModels { factory }
    private lateinit var factory: UserViewModelFactory

    private var seller: List<UserModel> = emptyList()
    private var user: List<UserModel> = emptyList()

    private val sectionsPagerAdapter = SectionPagerAdapterUserAdmin(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = UserViewModelFactory.getInstanceAuth(binding.root.context)

        setupRecyclerView()
        bottomNav()

        getUserData()

    }

    private fun setupRecyclerView() {
        binding.rvUser.apply {
            layoutManager = GridLayoutManager(this@UserAdminActivity, 1)
            setHasFixedSize(true)
        }
    }

    private fun getUserData() {
        userViewModel.getUsersByRole("Penjual").observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    seller = result.data.data
                    sellerDataReceived()
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }

        userViewModel.getUsersByRole("Pembeli").observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    user = result.data.data
                    buyerDataReceived()
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun sellerDataReceived() {
        sectionsPagerAdapter.category1 = seller
        sectionPage()
    }

    private fun buyerDataReceived() {
        sectionsPagerAdapter.category2 = user
        sectionPage()
    }

    private fun sectionPage() {
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
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
        binding.bottomNavigationView.setSelectedItemId(R.id.user_admin)
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.seller,
            R.string.buyer,
        )
    }
}