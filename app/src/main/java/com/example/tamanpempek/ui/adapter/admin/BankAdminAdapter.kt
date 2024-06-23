package com.example.tamanpempek.ui.adapter.admin

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tamanpempek.databinding.ItemRekeningAdminBinding
import com.example.tamanpempek.model.BankModel
import com.example.tamanpempek.ui.admin.profile.DetailBankAdminActivity

class BankAdminAdapter(private val banks: List<BankModel>) : RecyclerView.Adapter<BankAdminAdapter.BankViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
        val binding = ItemRekeningAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BankViewHolder(binding)
    }

    override fun getItemCount(): Int = banks.size

    override fun onBindViewHolder(holder: BankViewHolder, position: Int) {
        holder.bind(banks[position])
    }

    class BankViewHolder(private val binding: ItemRekeningAdminBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bank: BankModel) {
            binding.apply {
                tvRekeningName.text = bank.name
                tvRekeningBank.text = bank.type
                tvRekeningNumber.text = bank.number

                root.setOnClickListener {
                    val context = itemView.context
                    val intent = Intent(context, DetailBankAdminActivity::class.java).apply {
                        putExtra("BANK_ID", bank.id)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

}