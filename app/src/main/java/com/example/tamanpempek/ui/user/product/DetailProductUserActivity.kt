package com.example.tamanpempek.ui.user.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityDetailProductSellerBinding
import com.example.tamanpempek.databinding.ActivityDetailProductUserBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.ProductModel
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.factory.ProductViewModelFactory

class DetailProductUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProductUserBinding
    private val productViewModel: ProductViewModel by viewModels { factory }
    private lateinit var factory: ProductViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductUserBinding.inflate(layoutInflater)
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

            Glide.with(this@DetailProductUserActivity)
                .load(product.image)
                .into(headerImage)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}