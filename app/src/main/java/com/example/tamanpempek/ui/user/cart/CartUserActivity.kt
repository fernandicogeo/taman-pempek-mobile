package com.example.tamanpempek.ui.user.cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityCartUserBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.CartModel
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.ui.adapter.user.cart.CartAdapter
import com.example.tamanpempek.ui.user.history.HistoryUserActivity
import com.example.tamanpempek.ui.user.product.DashboardUserActivity
import com.example.tamanpempek.ui.user.profile.ProfileUserActivity
import com.example.tamanpempek.ui.user.setting.SettingUserActivity
import com.example.tamanpempek.viewmodel.CartViewModel
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.CartViewModelFactory
import com.example.tamanpempek.viewmodel.factory.ProductViewModelFactory
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory

class CartUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartUserBinding
    private val cartViewModel: CartViewModel by viewModels { cartFactory }
    private lateinit var cartFactory: CartViewModelFactory
    private val productViewModel: ProductViewModel by viewModels { productFactory }
    private lateinit var productFactory: ProductViewModelFactory
    private val userViewModel: UserViewModel by viewModels { userFactory }
    private lateinit var userFactory: UserViewModelFactory
    private lateinit var preference: UserPreference

    private var activeCarts: List<CartModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartFactory = CartViewModelFactory.getInstanceCart(binding.root.context)
        productFactory = ProductViewModelFactory.getInstanceProduct(binding.root.context)
        userFactory = UserViewModelFactory.getInstanceAuth(binding.root.context)

        preference = UserPreference(this)

        val userId = preference.getLoginSession().id

        isCheckouted(userId)

        setupRecyclerView()
        bottomNav()
    }

    private fun setupRecyclerView() {
        binding.rvCart.apply {
            layoutManager = GridLayoutManager(this@CartUserActivity, 1)
            setHasFixedSize(true)
        }
    }

    private fun isCheckouted(userId: Int) {
        cartViewModel.getStatusCartByUser("checkouted", userId).observe(this) {
            showLoading(true)
            when (it) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    if (it.data.data != null && it.data.data.isNotEmpty()) {
                        startActivity(
                            Intent(
                                this,
                                PaymentUserActivity::class.java
                            )
                        )
                    } else {
                        getActivedCartsByUser(userId)

                        binding.btnPayment.setOnClickListener {
                            AlertDialog.Builder(this).apply {
                                setTitle("Apakah anda yakin?")
                                setPositiveButton("Ya") { _, _ ->
                                    activeCarts.forEach { cart ->
                                        postPayment(cart.id)
                                    }
                                }
                                create()
                                show()
                            }
                        }
                    }
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun getActivedCartsByUser(userId: Int) {
        cartViewModel.getStatusCartByUser("actived", userId).observe(this) {
            showLoading(true)
            when (it) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    if (it.data.data != null && it.data.data.isNotEmpty()) {
                        activeCarts = it.data.data
                        val adapter = CartAdapter(it.data.data, productViewModel, cartViewModel, userViewModel, this, binding.progressBar)
                        binding.rvCart.adapter = adapter
                        getTotalPrice(userId)
                        showNoCartsMessage(false)
                    } else {
                        showNoCartsMessage(true)
                    }
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun getTotalPrice(userId: Int) {
        cartViewModel.getCartsTotalPriceByUser("actived", userId).observe(this) {
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

    private fun postPayment(cartId: Int) {
        cartViewModel.updateStatusCart(cartId, "checkouted").observe(this) {
            when (it) {
                is ResultCondition.LoadingState -> {
                    showLoading(true)
                }
                is ResultCondition.ErrorState -> {
                    showLoading(false)
                    showDialog(false)
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    showDialog(true)
                }
            }
        }
    }

    private fun showDialog(isSuccess: Boolean) {
        if (isSuccess) {
            AlertDialog.Builder(this).apply {
                setTitle("Checkout berhasil!!")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(this@CartUserActivity, PaymentUserActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Checkout gagal!")
                setMessage("Silakan coba lagi.")
                create()
                show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    private fun showNoCartsMessage(show: Boolean) {
        binding.tvNoCart.visibility = if (show) View.VISIBLE else View.GONE
        binding.tvPrice.visibility = if (show) View.GONE else View.VISIBLE
        binding.btnPayment.visibility = if (show) View.GONE else View.VISIBLE
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
        binding.bottomNavigationView.setSelectedItemId(R.id.cart_user)
    }
}