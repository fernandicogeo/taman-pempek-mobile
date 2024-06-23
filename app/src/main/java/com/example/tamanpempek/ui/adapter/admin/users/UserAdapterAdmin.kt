package com.example.tamanpempek.ui.adapter.admin.users

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ItemUserBinding
import com.example.tamanpempek.model.UserModel
import com.example.tamanpempek.ui.admin.users.DetailUserAdminActivity

class UserAdapterAdmin(private val users: List<UserModel>) : RecyclerView.Adapter<UserAdapterAdmin.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserModel) {
            binding.apply {
                tvUserName.text = user.name
                tvEmail.text = itemView.context.getString(R.string.email_template, user.email)
                tvWhatsapp.text = itemView.context.getString(R.string.whatsapp_template, user.whatsapp)

                root.setOnClickListener {
                    val context = itemView.context
                    val intent = Intent(context, DetailUserAdminActivity::class.java).apply {
                        putExtra("USER_ID", user.id)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

}