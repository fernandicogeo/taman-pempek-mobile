package com.example.tamanpempek.ui.admin.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityHistoryUserBinding
import com.example.tamanpempek.databinding.ActivityPaymentAdminBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.PaymentModel
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.ui.adapter.admin.payment.SectionPagerAdapterAdminPayment
import com.example.tamanpempek.ui.admin.product.DashboardAdminActivity
import com.example.tamanpempek.ui.admin.users.UserAdminActivity
import com.example.tamanpempek.viewmodel.PaymentViewModel
import com.example.tamanpempek.viewmodel.factory.PaymentViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PaymentAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentAdminBinding
    private val paymentViewModel: PaymentViewModel by viewModels { paymentFactory }
    private lateinit var paymentFactory: PaymentViewModelFactory

    private var paymentReviewed: List<PaymentModel> = emptyList()
    private var paymentWaiting: List<PaymentModel> = emptyList()
    private var paymentSent: List<PaymentModel> = emptyList()
    private var paymentFinished: List<PaymentModel> = emptyList()
    private var paymentCanceled: List<PaymentModel> = emptyList()
    private var paymentRejected: List<PaymentModel> = emptyList()

    private val sectionsPagerAdapter = SectionPagerAdapterAdminPayment(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentFactory = PaymentViewModelFactory.getInstancePayment(binding.root.context)

        setupRecyclerView()
        bottomNav()

        getPaymentData()
    }

    private fun setupRecyclerView() {
        binding.rvPayment.apply {
            layoutManager = GridLayoutManager(this@PaymentAdminActivity, 1)
            setHasFixedSize(true)
        }
    }

    private fun getPaymentData() {
        paymentViewModel.getPaymentsByPaymentStatus("reviewed").observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    paymentReviewed = result.data.data
                    reviewedDataReceived()
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }

        paymentViewModel.getPaymentsByPaymentStatus("waiting for sent").observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    paymentWaiting = result.data.data
                    waitingDataReceived()
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }

        paymentViewModel.getPaymentsByPaymentStatus("sent").observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    paymentSent = result.data.data
                    sentDataReceived()
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }

        paymentViewModel.getPaymentsByPaymentStatus("finished").observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    paymentFinished = result.data.data
                    finishedDataReceived()
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }

        paymentViewModel.getPaymentsByPaymentStatus("canceled").observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    paymentCanceled = result.data.data
                    canceledDataReceived()
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }

        paymentViewModel.getPaymentsByPaymentStatus("rejected").observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    paymentRejected = result.data.data
                    rejectedDataReceived()
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun reviewedDataReceived() {
        sectionsPagerAdapter.paymentReviewed = paymentReviewed
        sectionPage()
    }

    private fun waitingDataReceived() {
        sectionsPagerAdapter.paymentWaiting = paymentWaiting
        sectionPage()
    }

    private fun sentDataReceived() {
        sectionsPagerAdapter.paymentSent = paymentSent
        sectionPage()
    }

    private fun finishedDataReceived() {
        sectionsPagerAdapter.paymentFinished = paymentFinished
        sectionPage()
    }

    private fun canceledDataReceived() {
        sectionsPagerAdapter.paymentCanceled = paymentCanceled
        sectionPage()
    }

    private fun rejectedDataReceived() {
        sectionsPagerAdapter.paymentRejected = paymentRejected
        sectionPage()
    }
    
    private fun sectionPage() {
        val viewPager: ViewPager2 = findViewById(R.id.view_pager_payment)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs_payment)
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
            }
        }
        binding.bottomNavigationView.setSelectedItemId(R.id.payment_admin)
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.history1,
            R.string.history2,
            R.string.history3,
            R.string.history4,
            R.string.history5,
            R.string.history6,
        )
    }
}