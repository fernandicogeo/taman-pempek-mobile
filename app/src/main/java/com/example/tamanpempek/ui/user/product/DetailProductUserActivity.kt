package com.example.tamanpempek.ui.user.product

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityDetailProductSellerBinding
import com.example.tamanpempek.databinding.ActivityDetailProductUserBinding
import com.example.tamanpempek.helper.GetData
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.ProductModel
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.ui.seller.bank.BankSellerActivity
import com.example.tamanpempek.ui.user.cart.CartUserActivity
import com.example.tamanpempek.viewmodel.CartViewModel
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.CartViewModelFactory
import com.example.tamanpempek.viewmodel.factory.ProductViewModelFactory
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory

class DetailProductUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProductUserBinding
    private val productViewModel: ProductViewModel by viewModels { productFactory }
    private lateinit var productFactory: ProductViewModelFactory
    private val cartViewModel: CartViewModel by viewModels { cartFactory }
    private lateinit var cartFactory: CartViewModelFactory
    private val userViewModel: UserViewModel by viewModels { userFactory }
    private lateinit var userFactory: UserViewModelFactory

    private lateinit var preference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productFactory = ProductViewModelFactory.getInstanceProduct(binding.root.context)
        cartFactory = CartViewModelFactory.getInstanceCart(binding.root.context)
        userFactory = UserViewModelFactory.getInstanceAuth(binding.root.context)

        preference = UserPreference(this)

        val userId = preference.getLoginSession().id
        val productId = intent.getIntExtra("PRODUCT_ID", -1)

        productViewModel.getProductById(productId).observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    setDetail(result.data.data, userId, productId)
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun setDetail(product: ProductModel, userId: Int, productId: Int) {
        GetData.getSellerName(userViewModel, this, product.user_id) { sellerName ->
            binding.apply {
                tvName.text = product.name
                val categoryArray = resources.getStringArray(R.array.category_array)
                val categoryName = if (product.category_id - 1 in (categoryArray.indices)) {
                    categoryArray[product.category_id - 1]
                } else {
                    getString(R.string.category)
                }

                tvSeller.text = getString(R.string.seller_template, sellerName)
                tvCategory.text = getString(R.string.category_template, categoryName)
                tvPrice.text = getString(R.string.price_template, product.price.toString())
                tvStock.text = getString(R.string.stock_template, product.stock.toString())
                description.text = product.description

                Glide.with(this@DetailProductUserActivity)
                    .load(product.image)
                    .into(headerImage)
            }

            setupAction(product, userId, productId)
        }
    }


    private fun setupAction(product: ProductModel, userId: Int, productId: Int) {
        showLoading(false)

        binding.btnCart.setOnClickListener {
            val quantity = binding.etQuantity.text.toString()
            when {
                quantity.isEmpty() -> {
                    binding.etlQuantity.error = "Masukkan kuantitas produk"
                }
                quantity.toInt() > product.stock -> {
                    binding.etlQuantity.error = "Jumlah kuantitas melebihi stok produk"
                }
                else -> {
                    val totalPrice = quantity.toInt() * product.price
                    createCart(userId, productId, quantity.toInt(), totalPrice)
                }
            }
        }
    }
    private fun createCart(userId: Int, productId: Int, quantity: Int, totalPrice: Int) {
        cartViewModel.createCart(userId, productId, null, quantity, totalPrice, "actived").observe(this) {
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
                setTitle("Produk berhasil dimasukkan ke keranjang!!")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(this@DetailProductUserActivity, CartUserActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Produk gagal dimasukkan ke keranjang!")
                setMessage("Coba lagi.")
                create()
                show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}