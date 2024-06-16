package com.example.tamanpempek.ui.seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityDashboardSellerBinding
import com.example.tamanpempek.databinding.ActivityDetailProductSellerBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.ProductModel
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.factory.ProductViewModelFactory

class DetailProductSellerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProductSellerBinding
    private val productViewModel: ProductViewModel by viewModels { factory }
    private lateinit var factory: ProductViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ProductViewModelFactory.getInstanceProduct(binding.root.context)

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
            val intent = Intent(this, EditProductSellerActivity::class.java).apply {
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
            val categoryName = if (product.category_id in categoryArray.indices) {
                categoryArray[product.category_id]
            } else {
                getString(R.string.category)
            }

            tvCategory.text = getString(R.string.category_template, categoryName)
            tvPrice.text = getString(R.string.price_template, product.price.toString())
            tvStock.text = getString(R.string.stock_template, product.stock.toString())
            description.text = product.description

            Glide.with(this@DetailProductSellerActivity)
                .load(product.image)
                .into(headerImage)
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
                    val intent = Intent(this@DetailProductSellerActivity, DashboardSellerActivity::class.java)
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