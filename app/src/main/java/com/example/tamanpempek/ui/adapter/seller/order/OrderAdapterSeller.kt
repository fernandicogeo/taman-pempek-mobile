package com.example.tamanpempek.ui.adapter.seller.order

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ItemOrderBinding
import com.example.tamanpempek.helper.GetData
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.CartModel
import com.example.tamanpempek.viewmodel.UserViewModel

class OrderAdapterSeller(
    private val payments: List<CartModel>,
    private val userViewModel: UserViewModel,
    private val lifecycleOwner: LifecycleOwner,
) : RecyclerView.Adapter<OrderAdapterSeller.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding, userViewModel, lifecycleOwner)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(payments[position])
    }

    override fun getItemCount(): Int = payments.size

    class OrderViewHolder(
        private val binding: ItemOrderBinding,
        private val userViewModel: UserViewModel,
        private val lifecycleOwner: LifecycleOwner,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: CartModel) {
            binding.apply {
                val context = binding.root.context
                tvOrderQuantity.text = itemView.context.getString(R.string.quantity_template, cart.quantity.toString())
                tvOrderPrice.text = itemView.context.getString(R.string.price_template, cart.total_price.toString())
                userViewModel.getUserById(cart.user_id).observe(lifecycleOwner) {
                    when (it) {
                        is ResultCondition.LoadingState -> {
                        }
                        is ResultCondition.SuccessState -> {
                            tvUser.text = context.getString(R.string.user_template, it.data.data.name)
                        }
                        is ResultCondition.ErrorState -> {
                        }
                    }
                }
//                root.setOnClickListener {
//                    val context = itemView.context
//                    val intent = Intent(context, DetailOrderUserActivity::class.java).apply {
//                        putExtra("PAYMENT_ID", payment.id)
//                    }
//                    context.startActivity(intent)
//                }
            }
        }
    }
}