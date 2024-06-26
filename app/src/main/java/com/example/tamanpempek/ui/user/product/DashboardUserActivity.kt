package com.example.tamanpempek.ui.user.product

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityDashboardUserBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.ProductModel
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.ui.adapter.user.product.SectionPagerAdapterUser
import com.example.tamanpempek.ui.user.cart.CartUserActivity
import com.example.tamanpempek.ui.user.history.HistoryUserActivity
import com.example.tamanpempek.ui.user.profile.ProfileUserActivity
import com.example.tamanpempek.ui.user.setting.SettingUserActivity
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.SettingViewModel
import com.example.tamanpempek.viewmodel.factory.ProductViewModelFactory
import com.example.tamanpempek.viewmodel.factory.SettingViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DashboardUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardUserBinding
    private val productViewModel: ProductViewModel by viewModels { factory }
    private lateinit var factory: ProductViewModelFactory
    private val settingViewModel: SettingViewModel by viewModels { settingFactory }
    private lateinit var settingFactory: SettingViewModelFactory
    private lateinit var preference: UserPreference
    private val sectionsPagerAdapter = SectionPagerAdapterUser(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ProductViewModelFactory.getInstanceProduct(binding.root.context)
        settingFactory = SettingViewModelFactory.getInstanceSetting(binding.root.context)
        preference = UserPreference(this)

        setupRecyclerView()
        bottomNav()

        val userId = preference.getLoginSession().id
        var category1: List<ProductModel>
        var category2: List<ProductModel>
        var category3: List<ProductModel>

        settingViewModel.getSettingById(1).observe(this) {
            showLoading(true)
            when (it) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    binding.apply {
                        Glide.with(this@DashboardUserActivity)
                            .load(it.data.data.image)
                            .into(headerImage)
                    }
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }

        productViewModel.getProducts().observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    category1 = result.data.data
                    onCategory1DataReceived(category1)
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
        productViewModel.getProductsByCategory(1).observe(this) { result ->
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    category2 = result.data.data
                    onCategory2DataReceived(category2)
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
        productViewModel.getProductsByCategory(2).observe(this) { result ->
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    category3 = result.data.data
                    onCategory3DataReceived(category3)
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }
    private fun onCategory1DataReceived(category1: List<ProductModel>) {
        sectionsPagerAdapter.category1 = category1
        sectionPage()
        showNoProductsMessage(category1.isEmpty())
    }

    private fun onCategory2DataReceived(category2: List<ProductModel>) {
        sectionsPagerAdapter.category2 = category2
        sectionPage()
    }

    private fun onCategory3DataReceived(category3: List<ProductModel>) {
        sectionsPagerAdapter.category3 = category3
        sectionPage()
    }

    private fun showNoProductsMessage(show: Boolean) {
        binding.tvNoProduct.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun setupRecyclerView() {
        binding.rvProduct.apply {
            layoutManager = GridLayoutManager(this@DashboardUserActivity, 2)
            setHasFixedSize(true)
        }
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
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.category1,
            R.string.category2,
            R.string.category3,
        )
    }
}