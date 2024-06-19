package com.example.tamanpempek.ui.user.cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityCartUserBinding
import com.example.tamanpempek.databinding.ActivityPaymentUserBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.CartModel
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.ui.adapter.seller.bank.BankAdapter
import com.example.tamanpempek.ui.adapter.user.cart.CartAdapter
import com.example.tamanpempek.ui.adapter.user.cart.PaymentAdapter
import com.example.tamanpempek.ui.user.product.DashboardUserActivity
import com.example.tamanpempek.ui.user.setting.SettingUserActivity
import com.example.tamanpempek.viewmodel.BankViewModel
import com.example.tamanpempek.viewmodel.CartViewModel
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.BankViewModelFactory
import com.example.tamanpempek.viewmodel.factory.CartViewModelFactory
import com.example.tamanpempek.viewmodel.factory.ProductViewModelFactory
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory

class PaymentUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentUserBinding
    private val cartViewModel: CartViewModel by viewModels { cartFactory }
    private lateinit var cartFactory: CartViewModelFactory
    private val productViewModel: ProductViewModel by viewModels { productFactory }
    private lateinit var productFactory: ProductViewModelFactory
    private val userViewModel: UserViewModel by viewModels { userFactory }
    private lateinit var userFactory: UserViewModelFactory
    private val bankViewModel: BankViewModel by viewModels { bankFactory }
    private lateinit var bankFactory: BankViewModelFactory
    private lateinit var preference: UserPreference

    private var checkoutedCarts: List<CartModel> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartFactory = CartViewModelFactory.getInstanceCart(binding.root.context)
        productFactory = ProductViewModelFactory.getInstanceProduct(binding.root.context)
        userFactory = UserViewModelFactory.getInstanceAuth(binding.root.context)
        bankFactory = BankViewModelFactory.getInstanceBank(binding.root.context)

        preference = UserPreference(this)

        val userId = preference.getLoginSession().id

        setupRecyclerView()
        getCheckoutedCartsByUser(userId)
        getAdminBanks()

        getTotalPrice(userId)
        bottomNav()
    }

    private fun setupRecyclerView() {
        binding.rvPayment.apply {
            layoutManager = GridLayoutManager(this@PaymentUserActivity, 1)
            setHasFixedSize(true)
        }
        binding.rvRekening.apply {
            layoutManager = GridLayoutManager(this@PaymentUserActivity, 1)
            setHasFixedSize(true)
        }
    }

    private fun getCheckoutedCartsByUser(userId: Int) {
        cartViewModel.FindStatusCardByUser("checkouted", userId).observe(this) {
            showLoading(true)
            when (it) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    checkoutedCarts = it.data.data
                    val adapter = PaymentAdapter(it.data.data, productViewModel, userViewModel, this)
                    binding.rvPayment.adapter = adapter
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun getAdminBanks() {
        bankViewModel.getAdminBanks().observe(this) {
            when (it) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    val adapter = BankAdapter(it.data.data)
                    binding.rvRekening.adapter = adapter
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun getTotalPrice(userId: Int) {
        cartViewModel.getCartsTotalPriceByUser("checkouted", userId).observe(this) {
            Log.d("ITTOTALPRICE", it.toString())
            when (it) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    binding.tvPrice.text = getString(R.string.price_template, it.data.data.toString())
                }
                is ResultCondition.ErrorState -> {
                }
            }
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
                R.id.setting_user -> {
                    startActivity(Intent(this, SettingUserActivity::class.java))
                }
            }
        }
        binding.bottomNavigationView.setSelectedItemId(R.id.cart_user)
    }
}