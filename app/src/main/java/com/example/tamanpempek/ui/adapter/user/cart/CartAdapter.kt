package com.example.tamanpempek.ui.adapter.user.cart

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.content.contentValuesOf
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ItemCartBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.CartModel
import com.example.tamanpempek.ui.seller.bank.BankSellerActivity
import com.example.tamanpempek.ui.seller.bank.DetailBankSellerActivity
import com.example.tamanpempek.ui.user.cart.CartUserActivity
import com.example.tamanpempek.viewmodel.CartViewModel
import com.example.tamanpempek.viewmodel.ProductViewModel

class CartAdapter(
    private val carts: List<CartModel>,
    private val productViewModel: ProductViewModel,
    private val cartViewModel: CartViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val progressBar: ProgressBar
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding, productViewModel, cartViewModel, lifecycleOwner, progressBar)
    }

    override fun getItemCount(): Int = carts.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(carts[position])
    }

    class CartViewHolder(
        private val binding: ItemCartBinding,
        private val productViewModel: ProductViewModel,
        private val cartViewModel: CartViewModel,
        private val lifecycleOwner: LifecycleOwner,
        private val progressBar: ProgressBar
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

                binding.btnDelete.setOnClickListener {
                    AlertDialog.Builder(context).apply {
                        setTitle("Apakah anda yakin?")
                        setPositiveButton("Ya") { _, _ ->
                            cartViewModel.deleteCart(cart.id).observe(lifecycleOwner) {
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
                        create()
                        show()
                    }
                }
            }
        }

        private fun showDialog(isSuccess: Boolean) {
            val context = binding.root.context
            if (isSuccess) {
                AlertDialog.Builder(context).apply {
                    setTitle("Hapus keranjang produk berhasil!!")
                    setPositiveButton("Lanjut") { _, _ ->
                        val intent = Intent(context, CartUserActivity::class.java)
                        context.startActivity(intent)
                    }
                    create()
                    show()
                }
            } else {
                AlertDialog.Builder(context).apply {
                    setTitle("Hapus keranjang produk gagal!")
                    setMessage("Silakan coba lagi.")
                    create()
                    show()
                }
            }
        }

        private fun showLoading(isLoading: Boolean) {
            if (isLoading) progressBar.visibility = View.VISIBLE
            else progressBar.visibility = View.GONE
        }
    }
}