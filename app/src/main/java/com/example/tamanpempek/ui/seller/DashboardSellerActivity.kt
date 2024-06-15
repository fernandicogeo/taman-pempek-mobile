package com.example.tamanpempek.ui.seller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityDashboardSellerBinding
import com.example.tamanpempek.databinding.ActivityLoginBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.ProductModel
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.response.ProductResponse
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.RegisterViewModel
import com.example.tamanpempek.viewmodel.factory.ProductViewModelFactory
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DashboardSellerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardSellerBinding
    private val productViewModel: ProductViewModel by viewModels { factory }
    private lateinit var factory: ProductViewModelFactory
    private lateinit var preference: UserPreference
    private val sectionsPagerAdapter = SectionPagerAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ProductViewModelFactory.getInstanceProduct(binding.root.context)
        preference = UserPreference(this)

        setupRecyclerView()

        val userId = preference.getLoginSession().id
        var category1: List<ProductModel>
        var category2: List<ProductModel>
        var category3: List<ProductModel>

        productViewModel.getProducts().observe(this) { result ->
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    category1 = result.data.data
                    onCategory1DataReceived(category1)
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
        productViewModel.getUserProductsByCategory(userId, 2).observe(this) { result ->
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
        productViewModel.getUserProductsByCategory(userId, 3).observe(this) { result ->
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
    }

    private fun onCategory2DataReceived(category2: List<ProductModel>) {
        sectionsPagerAdapter.category2 = category2
        sectionPage()
    }

    private fun onCategory3DataReceived(category3: List<ProductModel>) {
        sectionsPagerAdapter.category3 = category3
        sectionPage()
    }

    private fun setupRecyclerView() {
        binding.rvProduct.apply {
            layoutManager = GridLayoutManager(this@DashboardSellerActivity, 2)
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

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.category1,
            R.string.category2,
            R.string.category3,
        )
    }
}