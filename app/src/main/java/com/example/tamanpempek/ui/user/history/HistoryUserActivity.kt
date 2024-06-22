package com.example.tamanpempek.ui.user.history

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityHistoryUserBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.PaymentModel
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.ui.adapter.user.history.SectionPagerHistoryAdapterUser
import com.example.tamanpempek.ui.user.cart.CartUserActivity
import com.example.tamanpempek.ui.user.product.DashboardUserActivity
import com.example.tamanpempek.ui.user.setting.SettingUserActivity
import com.example.tamanpempek.viewmodel.PaymentViewModel
import com.example.tamanpempek.viewmodel.factory.PaymentViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HistoryUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryUserBinding
    private val paymentViewModel: PaymentViewModel by viewModels { paymentFactory }
    private lateinit var paymentFactory: PaymentViewModelFactory

    private lateinit var preference: UserPreference

    private var historyReviewed: List<PaymentModel> = emptyList()
    private var historySent: List<PaymentModel> = emptyList()
    private var historyFinished: List<PaymentModel> = emptyList()
    private var historyCanceled: List<PaymentModel> = emptyList()
    private var historyRejected: List<PaymentModel> = emptyList()

    private val sectionsPagerAdapter = SectionPagerHistoryAdapterUser(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentFactory = PaymentViewModelFactory.getInstancePayment(binding.root.context)
        preference = UserPreference(this)

        val userId = preference.getLoginSession().id

        setupRecyclerView()
        bottomNav()

        getPaymentData(userId)
    }

    private fun setupRecyclerView() {
        binding.rvHistory.apply {
            layoutManager = GridLayoutManager(this@HistoryUserActivity, 1)
            setHasFixedSize(true)
        }
    }

    private fun getPaymentData(userId: Int) {
        paymentViewModel.getPaymentsByUserAndPaymentStatus(userId, "reviewed").observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    historyReviewed = result.data.data
                    reviewedDataReceived()
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }

        paymentViewModel.getPaymentsByUserAndPaymentStatus(userId, "sent").observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    historySent = result.data.data
                    sentDataReceived()
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }

        paymentViewModel.getPaymentsByUserAndPaymentStatus(userId, "finished").observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    historyFinished = result.data.data
                    finishedDataReceived()
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }

        paymentViewModel.getPaymentsByUserAndPaymentStatus(userId, "canceled").observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    historyCanceled = result.data.data
                    canceledDataReceived()
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }

        paymentViewModel.getPaymentsByUserAndPaymentStatus(userId, "rejected").observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    historyRejected = result.data.data
                    rejectedDataReceived()
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun reviewedDataReceived() {
        sectionsPagerAdapter.historyReviewed = historyReviewed
        sectionPage()
    }

    private fun sentDataReceived() {
        sectionsPagerAdapter.historySent = historySent
        sectionPage()
    }

    private fun finishedDataReceived() {
        sectionsPagerAdapter.historyFinished = historyFinished
        sectionPage()
    }

    private fun canceledDataReceived() {
        sectionsPagerAdapter.historyCanceled = historyCanceled
        sectionPage()
    }

    private fun rejectedDataReceived() {
        sectionsPagerAdapter.historyRejected = historyRejected
        sectionPage()
    }

    private fun sectionPage() {
        val viewPager: ViewPager2 = findViewById(R.id.view_pager_history)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs_history)
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
                R.id.setting_user -> {
                    startActivity(Intent(this, SettingUserActivity::class.java))
                }
            }
        }
        binding.bottomNavigationView.setSelectedItemId(R.id.history_user)
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.history1,
            R.string.history2,
            R.string.history3,
            R.string.history4,
            R.string.history5,
        )
    }
}