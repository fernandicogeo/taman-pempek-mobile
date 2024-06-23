package com.example.tamanpempek.ui.adapter.admin.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ItemPaymentBinding
import com.example.tamanpempek.helper.GetData
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.CartModel
import com.example.tamanpempek.ui.adapter.user.history.HistoryDetailAdapter
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.UserViewModel

class PaymentDetailAdapterAdmin(
    private val carts: List<CartModel>,
    private val productViewModel: ProductViewModel,
    private val userViewModel: UserViewModel,
    private val lifecycleOwner: LifecycleOwner,
) : RecyclerView.Adapter<PaymentDetailAdapterAdmin.HistoryDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDetailViewHolder {
        val binding = ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryDetailViewHolder(binding, productViewModel, userViewModel, lifecycleOwner)
    }

    override fun getItemCount(): Int = carts.size

    override fun onBindViewHolder(holder: HistoryDetailViewHolder, position: Int) {
        holder.bind(carts[position])
    }

    class HistoryDetailViewHolder(
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