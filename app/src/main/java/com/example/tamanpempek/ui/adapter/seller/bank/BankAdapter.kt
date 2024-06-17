package com.example.tamanpempek.ui.adapter.seller.bank

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tamanpempek.databinding.ItemRekeningBinding
import com.example.tamanpempek.model.BankModel
import com.example.tamanpempek.ui.seller.bank.DetailBankSellerActivity

class BankAdapter(private val banks: List<BankModel>) : RecyclerView.Adapter<BankAdapter.BankViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
        val binding = ItemRekeningBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BankViewHolder(binding)
    }

    override fun getItemCount(): Int = banks.size

    override fun onBindViewHolder(holder: BankViewHolder, position: Int) {
        holder.bind(banks[position])
    }

    class BankViewHolder(private val binding: ItemRekeningBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bank: BankModel) {
            binding.apply {
                tvRekeningName.text = bank.name
                tvRekeningBank.text = bank.type
                tvRekeningNumber.text = bank.number

                root.setOnClickListener {
                    val context = itemView.context
                    val intent = Intent(context, DetailBankSellerActivity::class.java).apply {
                        putExtra("BANK_ID", bank.id)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

}