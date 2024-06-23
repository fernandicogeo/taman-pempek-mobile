package com.example.tamanpempek.ui.admin.product

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityDashboardAdminBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.ProductModel
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.ui.adapter.admin.product.SectionPagerAdapterAdmin
import com.example.tamanpempek.ui.admin.users.UserAdminActivity
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.factory.ProductViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DashboardAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardAdminBinding
    private val productViewModel: ProductViewModel by viewModels { factory }
    private lateinit var factory: ProductViewModelFactory
    private lateinit var preference: UserPreference
    private val sectionsPagerAdapter = SectionPagerAdapterAdmin(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ProductViewModelFactory.getInstanceProduct(binding.root.context)
        preference = UserPreference(this)

        setupRecyclerView()
        bottomNav()

        val userId = preference.getLoginSession().id
        var category1: List<ProductModel>
        var category2: List<ProductModel>
        var category3: List<ProductModel>

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
            layoutManager = GridLayoutManager(this@DashboardAdminActivity, 2)
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
                R.id.dashboard_admin -> {
                    startActivity(Intent(this, DashboardAdminActivity::class.java))
                }
                R.id.user_admin -> {
                    startActivity(Intent(this, UserAdminActivity::class.java))
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