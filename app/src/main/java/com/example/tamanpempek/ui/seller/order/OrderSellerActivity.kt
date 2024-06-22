package com.example.tamanpempek.ui.seller.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityOrderSellerBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.CartModel
import com.example.tamanpempek.model.PaymentModel
import com.example.tamanpempek.model.ProductModel
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.ui.adapter.seller.order.SectionPagerOrderAdapter
import com.example.tamanpempek.ui.adapter.user.history.SectionPagerHistoryAdapterUser
import com.example.tamanpempek.ui.seller.bank.BankSellerActivity
import com.example.tamanpempek.ui.seller.product.DashboardSellerActivity
import com.example.tamanpempek.ui.seller.profile.ProfileSellerActivity
import com.example.tamanpempek.ui.seller.setting.SettingSellerActivity
import com.example.tamanpempek.viewmodel.BankViewModel
import com.example.tamanpempek.viewmodel.CartViewModel
import com.example.tamanpempek.viewmodel.PaymentViewModel
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.BankViewModelFactory
import com.example.tamanpempek.viewmodel.factory.CartViewModelFactory
import com.example.tamanpempek.viewmodel.factory.PaymentViewModelFactory
import com.example.tamanpempek.viewmodel.factory.ProductViewModelFactory
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OrderSellerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderSellerBinding

    private val cartViewModel: CartViewModel by viewModels { cartFactory }
    private val productViewModel: ProductViewModel by viewModels { productFactory }
    private val paymentViewModel: PaymentViewModel by viewModels { paymentFactory }
    private lateinit var cartFactory: CartViewModelFactory
    private lateinit var productFactory: ProductViewModelFactory
    private lateinit var paymentFactory: PaymentViewModelFactory

    private lateinit var preference: UserPreference

    private var productsData: List<ProductModel> = emptyList()
    private var cartsData: List<CartModel> = emptyList()
    private var paymentsData: List<PaymentModel> = emptyList()

    private var orderWaiting: List<CartModel> = emptyList()
    private var orderSent: List<CartModel> = emptyList()
    private var orderFinished: List<CartModel> = emptyList()

    private val sectionsPagerAdapter = SectionPagerOrderAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartFactory = CartViewModelFactory.getInstanceCart(binding.root.context)
        productFactory = ProductViewModelFactory.getInstanceProduct(binding.root.context)
        paymentFactory = PaymentViewModelFactory.getInstancePayment(binding.root.context)
        preference = UserPreference(this)

        val userId = preference.getLoginSession().id

        setupRecyclerView()
        getOrdersData(userId)
        bottomNav()
    }

    private fun setupRecyclerView() {
        binding.rvOrder.apply {
            layoutManager = GridLayoutManager(this@OrderSellerActivity, 1)
            setHasFixedSize(true)
        }
    }

    private fun getOrdersData(userId: Int) {
        productViewModel.getProductsByUser(userId).observe(this) { result ->
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    productsData = result.data.data
                    productsData.forEach { product ->
                        getCartsData(product.id)
                    }
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun getCartsData(productId: Int) {
        cartViewModel.getCartsByProductId(productId).observe(this) { result ->
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    cartsData = result.data.data
                    cartsData.forEach { cart ->
                        getPaymentsData(cart.payment_id, cart)
                    }
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun getPaymentsData(paymentId: Int, cart: CartModel) {
        paymentViewModel.getPaymentById(paymentId).observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    paymentsData = paymentsData.toMutableList().apply {
                        add(result.data.data)
                    }
                    paymentsData.forEach {payment ->
                        when (payment.payment_status) {
                            "waiting for sent" -> {
                                orderWaiting = orderWaiting.toMutableList().apply {
                                    add(cart)
                                }
                                waitingDataReceived()
                            }
                            "sent" -> {
                                orderSent = orderSent.toMutableList().apply {
                                    add(cart)
                                }
                                sentDataReceived()
                            }
                            "finished" -> {
                                orderFinished = orderFinished.toMutableList().apply {
                                    add(cart)
                                }
                                finishedDataReceived()
                            }
                        }
                    }
                }
                is ResultCondition.ErrorState -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun waitingDataReceived() {
        sectionsPagerAdapter.orderWaiting = orderWaiting
        sectionPage()
    }

    private fun sentDataReceived() {
        sectionsPagerAdapter.orderSent = orderSent
        sectionPage()
    }

    private fun finishedDataReceived() {
        sectionsPagerAdapter.orderFinished = orderFinished
        sectionPage()
    }
    private fun sectionPage() {
        val viewPager: ViewPager2 = findViewById(R.id.view_pager_order)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs_order)
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
                R.id.dashboard -> {
                    startActivity(Intent(this, DashboardSellerActivity::class.java))
                }
                R.id.order -> {
                    startActivity(Intent(this, OrderSellerActivity::class.java))
                }
                R.id.rekening -> {
                    startActivity(Intent(this, BankSellerActivity::class.java))
                }
                R.id.profil -> {
                    startActivity(Intent(this, ProfileSellerActivity::class.java))
                }
                R.id.setting -> {
                    startActivity(Intent(this, SettingSellerActivity::class.java))
                }
            }
        }
        binding.bottomNavigationView.setSelectedItemId(R.id.order)
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.history2,
            R.string.history3,
            R.string.history4,
        )
    }
}