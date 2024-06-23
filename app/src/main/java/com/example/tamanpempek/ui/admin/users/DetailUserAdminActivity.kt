package com.example.tamanpempek.ui.admin.users

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityDetailUserAdminBinding
import com.example.tamanpempek.databinding.ActivityProfileUserBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.PaymentModel
import com.example.tamanpempek.model.ProductModel
import com.example.tamanpempek.model.UserModel
import com.example.tamanpempek.ui.seller.product.DashboardSellerActivity
import com.example.tamanpempek.ui.user.profile.EditProfileUserActivity
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.ProductViewModelFactory
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory

class DetailUserAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserAdminBinding
    private val userViewModel: UserViewModel by viewModels { userFactory }
    private lateinit var userFactory: UserViewModelFactory
    private val productViewModel: ProductViewModel by viewModels { productFactory }
    private lateinit var productFactory: ProductViewModelFactory
    private var userProducts: List<ProductModel> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userFactory = UserViewModelFactory.getInstanceAuth(binding.root.context)
        productFactory = ProductViewModelFactory.getInstanceProduct(binding.root.context)

        val userId = intent.getIntExtra("USER_ID", -1)

        getProducts(userId)

        userViewModel.getUserById(userId).observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    detailUser(result.data.data)
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }

        binding.btnEditUser.setOnClickListener {
            val intent = Intent(this, EditUserAdminActivity::class.java).apply {
                putExtra("USER_ID", userId)
            }
            startActivity(intent)
        }

        binding.btnDeleteUser.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Apakah anda yakin?")
                setPositiveButton("Ya") { _, _ ->
                    deleteUser(userId)
                    userProducts.forEach() { product ->
                        deleteProduct(product.id)
                    }
                }
                create()
                show()
            }
        }
    }

    private fun detailUser(user: UserModel) {
        binding.apply {
            tvName.text = getString(R.string.name_template, user.name)
            tvEmail.text = getString(R.string.email_template, user.email)
            tvPassword.text = getString(R.string.password_template, user.password)
            tvWhatsapp.text = getString(R.string.whatsapp_template, user.whatsapp)
            tvGender.text = getString(R.string.gender_template, user.gender)
            tvRole.text = getString(R.string.role_template, user.role)
        }
    }

    private fun deleteUser(userId: Int) {
        userViewModel.deleteUser(userId).observe(this) {
            when (it) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.ErrorState -> {
                }
                is ResultCondition.SuccessState -> {
                }
            }
        }
    }

    private fun getProducts(userId: Int) {
        productViewModel.getProductsByUser(userId).observe(this) { result ->
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    userProducts = result.data.data
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun deleteProduct(productId: Int) {
        productViewModel.deleteProduct(productId).observe(this) { result ->
            when (result) {
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

    private fun showDialog(isSuccess: Boolean) {
        if (isSuccess) {
            AlertDialog.Builder(this).apply {
                setTitle("Hapus pengguna berhasil!!")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(this@DetailUserAdminActivity, UserAdminActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Hapus pengguna gagal!")
                setMessage("Silakan coba lagi.")
                create()
                show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}