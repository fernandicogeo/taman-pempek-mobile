package com.example.tamanpempek.ui.adapter.user.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ItemHistoryBinding
import com.example.tamanpempek.model.PaymentModel
import com.example.tamanpempek.ui.user.history.DetailHistoryUserActivity
import com.example.tamanpempek.ui.user.history.HistoryUserActivity

class HistoryAdapterUser(private val payments: List<PaymentModel>) : RecyclerView.Adapter<HistoryAdapterUser.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(payments[position])
    }

    override fun getItemCount(): Int = payments.size

    class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(payment: PaymentModel) {
            binding.apply {
                val priceText = itemView.context.getString(R.string.price_template, payment.total_price.toString())
                tvHistoryPrice.text = priceText

                root.setOnClickListener {
                    val context = itemView.context
                    val intent = Intent(context, DetailHistoryUserActivity::class.java).apply {
                        putExtra("PAYMENT_ID", payment.id)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }
}