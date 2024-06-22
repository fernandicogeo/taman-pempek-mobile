package com.example.tamanpempek.ui.adapter.user.bank

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tamanpempek.databinding.ItemRekeningBinding
import com.example.tamanpempek.model.BankModel
import com.example.tamanpempek.ui.adapter.seller.bank.BankAdapter
import com.example.tamanpempek.ui.seller.bank.DetailBankSellerActivity

class BankAdapterUser(private val banks: List<BankModel>) : RecyclerView.Adapter<BankAdapterUser.BankUserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankUserViewHolder {
        val binding = ItemRekeningBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BankUserViewHolder(binding)
    }

    override fun getItemCount(): Int = banks.size

    override fun onBindViewHolder(holder: BankUserViewHolder, position: Int) {
        holder.bind(banks[position])
    }

    class BankUserViewHolder(private val binding: ItemRekeningBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bank: BankModel) {
            binding.apply {
                tvRekeningName.text = bank.name
                tvRekeningBank.text = bank.type
                tvRekeningNumber.text = bank.number
            }
        }
    }

}