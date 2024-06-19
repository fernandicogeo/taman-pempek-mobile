package com.example.tamanpempek.ui.adapter.user.cart

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ItemPaymentBinding
import com.example.tamanpempek.helper.GetData
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.CartModel
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.UserViewModel

class PaymentAdapter(
    private val carts: List<CartModel>,
    private val productViewModel: ProductViewModel,
    private val userViewModel: UserViewModel,
    private val lifecycleOwner: LifecycleOwner,
) : RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val binding = ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentViewHolder(binding, productViewModel, userViewModel, lifecycleOwner)
    }

    override fun getItemCount(): Int = carts.size

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.bind(carts[position])
    }

    class PaymentViewHolder(
        private val binding: ItemPaymentBinding,
        private val productViewModel: ProductViewModel,
        private val userViewModel: UserViewModel,
        private val lifecycleOwner: LifecycleOwner,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: CartModel) {
            binding.apply {
                val context = binding.root.context
                tvPaymentQuantity.text = context.getString(R.string.quantity_template, cart.quantity.toString())
                tvPaymentPrice.text = context.getString(R.string.price_template, cart.total_price.toString())

                productViewModel.getProductById(cart.product_id).observe(lifecycleOwner) { result ->
                    when (result) {
                        is ResultCondition.LoadingState -> {
                        }
                        is ResultCondition.SuccessState -> {
                            tvPaymentName.text = result.data.data.name
                            GetData.getSellerName(userViewModel, lifecycleOwner, result.data.data.user_id) {
                                tvSeller.text = context.getString(R.string.seller_template, it)
                            }
                        }
                        is ResultCondition.ErrorState -> {
                        }
                    }
                }
            }
        }
    }
}