package com.example.tamanpempek.ui.adapter.user.cart

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ItemCartBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.CartModel
import com.example.tamanpempek.viewmodel.ProductViewModel

class CartAdapter(
    private val carts: List<CartModel>,
    private val productViewModel: ProductViewModel,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding, productViewModel, lifecycleOwner)
    }

    override fun getItemCount(): Int = carts.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(carts[position])
    }

    class CartViewHolder(
        private val binding: ItemCartBinding,
        private val productViewModel: ProductViewModel,
        private val lifecycleOwner: LifecycleOwner
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: CartModel) {
            binding.apply {
                val context = binding.root.context
                tvCartQuantity.text = context.getString(R.string.quantity_template, cart.quantity.toString())
                tvCartPrice.text = context.getString(R.string.price_template, cart.total_price.toString())

                productViewModel.getProductById(cart.product_id).observe(lifecycleOwner) { result ->
                    when (result) {
                        is ResultCondition.LoadingState -> {
                        }
                        is ResultCondition.SuccessState -> {
                            tvCartName.text = result.data.data.name
                        }
                        is ResultCondition.ErrorState -> {
                        }
                    }
                }
            }
        }
    }
}