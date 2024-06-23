package com.example.tamanpempek.ui.adapter.admin.product

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ItemProductBinding
import com.example.tamanpempek.model.ProductModel
import com.example.tamanpempek.ui.adapter.user.product.ProductAdapterUser
import com.example.tamanpempek.ui.admin.product.DetailProductAdminActivity
import com.example.tamanpempek.ui.user.product.DetailProductUserActivity

class ProductAdapterAdmin(private val products: List<ProductModel>) : RecyclerView.Adapter<ProductAdapterAdmin.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductModel) {
            binding.apply {
                titleProduct.text = product.name
                val stockText = itemView.context.getString(R.string.stock_template, product.stock.toString())
                stockProduct.text = stockText

                Glide.with(itemView.context)
                    .load(product.image)
                    .into(headerImage)

                root.setOnClickListener {
                    val context = itemView.context
                    val intent = Intent(context, DetailProductAdminActivity::class.java).apply {
                        putExtra("PRODUCT_ID", product.id)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

}