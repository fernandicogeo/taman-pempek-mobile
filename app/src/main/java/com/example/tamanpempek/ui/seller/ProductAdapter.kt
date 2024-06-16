package com.example.tamanpempek.ui.seller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tamanpempek.databinding.ItemProductBinding
import com.example.tamanpempek.model.ProductModel

class ProductAdapter(private val products: List<ProductModel>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

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
                stockProduct.text = "Stock: " + product.stock.toString()

                Glide.with(itemView.context)
                    .load(product.image)
                    .into(headerImage)
            }
        }
    }
}