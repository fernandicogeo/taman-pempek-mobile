package com.example.tamanpempek.ui.admin.product

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityDetailProductAdminBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.ProductModel
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.ProductViewModelFactory
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory

class DetailProductAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProductAdminBinding
    private val productViewModel: ProductViewModel by viewModels { productFactory }
    private lateinit var productFactory: ProductViewModelFactory
    private val userViewModel: UserViewModel by viewModels { userFactory }
    private lateinit var userFactory: UserViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productFactory = ProductViewModelFactory.getInstanceProduct(binding.root.context)
        userFactory = UserViewModelFactory.getInstanceAuth(binding.root.context)

        val productId = intent.getIntExtra("PRODUCT_ID", -1)

        productViewModel.getProductById(productId).observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    setDetail(result.data.data)
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }

        binding.btnEditProduct.setOnClickListener {
            val intent = Intent(this, EditProductAdminActivity::class.java).apply {
                putExtra("PRODUCT_ID", productId)
            }
            startActivity(intent)
        }

        binding.btnDeleteProduct.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Apakah anda yakin?")
                setPositiveButton("Ya") { _, _ ->
                    deleteProduct(productId)
                }
                create()
                show()
            }
        }
    }

    private fun setDetail(product: ProductModel) {
        binding.apply {
            tvName.text = product.name
            val categoryArray = resources.getStringArray(R.array.category_array)
            val categoryName = if (product.category_id - 1 in (categoryArray.indices)) {
                categoryArray[product.category_id - 1]
            } else {
                getString(R.string.category)
            }

            tvCategory.text = getString(R.string.category_template, categoryName)
            tvPrice.text = getString(R.string.price_template, product.price.toString())
            tvStock.text = getString(R.string.stock_template, product.stock.toString())
            description.text = product.description

            Glide.with(this@DetailProductAdminActivity)
                .load(product.image)
                .into(headerImage)
        }
        userViewModel.getUserById(product.user_id).observe(this) { result ->
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    binding.tvSeller.text = getString(R.string.seller_template, result.data.data.name)
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun deleteProduct(userId: Int) {
        productViewModel.deleteProduct(userId).observe(this) {
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
                setTitle("Hapus produk berhasil!!")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(this@DetailProductAdminActivity, DashboardAdminActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Hapus produk gagal!")
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
}