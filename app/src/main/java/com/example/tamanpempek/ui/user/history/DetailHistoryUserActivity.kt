package com.example.tamanpempek.ui.user.history

import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityDetailHistoryUserBinding
import com.example.tamanpempek.databinding.ActivityHistoryUserBinding
import com.example.tamanpempek.helper.GetData
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.model.ProductModel
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.request.PaymentUpdateStatusRequest
import com.example.tamanpempek.request.ProductUpdateRequest
import com.example.tamanpempek.ui.adapter.user.cart.PaymentAdapter
import com.example.tamanpempek.ui.adapter.user.history.HistoryDetailAdapter
import com.example.tamanpempek.ui.seller.product.DashboardSellerActivity
import com.example.tamanpempek.viewmodel.BankViewModel
import com.example.tamanpempek.viewmodel.CartViewModel
import com.example.tamanpempek.viewmodel.PaymentViewModel
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.BankViewModelFactory
import com.example.tamanpempek.viewmodel.factory.CartViewModelFactory
import com.example.tamanpempek.viewmodel.factory.PaymentViewModelFactory
import com.example.tamanpempek.viewmodel.factory.ProductViewModelFactory
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory

class DetailHistoryUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHistoryUserBinding
    private val cartViewModel: CartViewModel by viewModels { cartFactory }
    private val productViewModel: ProductViewModel by viewModels { productFactory }
    private val userViewModel: UserViewModel by viewModels { userFactory }
    private val bankViewModel: BankViewModel by viewModels { bankFactory }
    private val paymentViewModel: PaymentViewModel by viewModels { paymentFactory }
    private lateinit var cartFactory: CartViewModelFactory
    private lateinit var productFactory: ProductViewModelFactory
    private lateinit var userFactory: UserViewModelFactory
    private lateinit var bankFactory: BankViewModelFactory
    private lateinit var paymentFactory: PaymentViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartFactory = CartViewModelFactory.getInstanceCart(binding.root.context)
        productFactory = ProductViewModelFactory.getInstanceProduct(binding.root.context)
        userFactory = UserViewModelFactory.getInstanceAuth(binding.root.context)
        bankFactory = BankViewModelFactory.getInstanceBank(binding.root.context)
        paymentFactory = PaymentViewModelFactory.getInstancePayment(binding.root.context)

        setupRecyclerView()
        val paymentId = intent.getIntExtra("PAYMENT_ID", -1)

        getDetailPayment(paymentId)
    }

    private fun setupRecyclerView() {
        binding.rvPayment.apply {
            layoutManager = GridLayoutManager(this@DetailHistoryUserActivity, 1)
            setHasFixedSize(true)
        }
    }

    private fun getDetailPayment(paymentId: Int) {
        paymentViewModel.getPaymentById(paymentId).observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    val payment = result.data.data

                    getCartsByPaymentId(paymentId)

                    binding.tvPrice.text = getString(R.string.price_template, payment.total_price.toString())
                    binding.tvAddress.text = getString(R.string.address_template, payment.address)
                    binding.tvWhatsapp.text = getString(R.string.whatsapp_template, payment.whatsapp)

                    when (payment.payment_status) {
                        "reviewed" -> {
                            binding.tvPaymentStatus.text =
                                getString(R.string.payment_status_template, "Menunggu review")
                            binding.btnFinish.visibility = View.GONE
                            binding.btnCancel.visibility = View.VISIBLE
                        }
                        "waiting for sent" -> {
                            binding.tvPaymentStatus.text =
                                getString(R.string.payment_status_template, "Menunggu pesanan dikirim")
                            binding.btnFinish.visibility = View.GONE
                            binding.btnCancel.visibility = View.VISIBLE
                        }
                        "sent" -> {
                            binding.tvPaymentStatus.text = getString(
                                R.string.payment_status_template,
                                "Pesanan sedang diantar"
                            )
                            binding.btnFinish.visibility = View.VISIBLE
                            binding.btnCancel.visibility = View.GONE
                        }
                        "finished" -> {
                            binding.tvPaymentStatus.text =
                                getString(R.string.payment_status_template, "Pesanan selesai")
                            binding.btnFinish.visibility = View.GONE
                            binding.btnCancel.visibility = View.GONE
                        }
                        "canceled" -> {
                            binding.tvPaymentStatus.text =
                                getString(R.string.payment_status_template, "Pesanan dibatalkan pembeli")
                            binding.btnFinish.visibility = View.GONE
                            binding.btnCancel.visibility = View.GONE
                        }
                        "rejected" -> {
                            binding.tvPaymentStatus.text =
                                getString(R.string.payment_status_template, "Pesanan ditolak")
                            binding.btnFinish.visibility = View.GONE
                            binding.btnCancel.visibility = View.GONE
                        }
                    }

                    binding.btnCancel.setOnClickListener {
                        AlertDialog.Builder(this).apply {
                            setTitle("Apakah anda yakin?")
                            setPositiveButton("Ya") { _, _ ->
                                updatePayment(result.data.data.id, "canceled")
                            }
                            create()
                            show()
                        }
                    }

                    binding.btnFinish.setOnClickListener {
                        AlertDialog.Builder(this).apply {
                            setTitle("Apakah anda yakin?")
                            setPositiveButton("Ya") { _, _ ->
                                updatePayment(result.data.data.id, "finished")
                            }
                            create()
                            show()
                        }
                    }
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun getCartsByPaymentId(paymentId: Int) {
        cartViewModel.getCartsByPaymentId(paymentId).observe(this) {
            showLoading(true)
            when (it) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    val adapter = HistoryDetailAdapter(it.data.data, productViewModel, userViewModel, this)
                    binding.rvPayment.adapter = adapter
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun updatePayment(id: Int, paymentStatus: String) {
        val request = PaymentUpdateStatusRequest(paymentStatus)

        paymentViewModel.updatePaymentStatus(id, request).observe(this) {
            when (it) {
                is ResultCondition.LoadingState -> {
                    showLoading(true)
                }
                is ResultCondition.ErrorState -> {
                    showLoading(false)
                    showDialog(false, paymentStatus)
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    showDialog(true, paymentStatus)
                }
            }
        }
    }

    private fun showDialog(isSuccess: Boolean, paymentStatus: String) {
        if (paymentStatus == "canceled") {
            if (isSuccess) {
                AlertDialog.Builder(this).apply {
                    setTitle("Pembatalan transaksi berhasil!!")
                    setPositiveButton("Lanjut") { _, _ ->
                        val intent = Intent(this@DetailHistoryUserActivity, HistoryUserActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("Pembatalan transaksi gagal!")
                    setMessage("Silakan coba lagi.")
                    create()
                    show()
                }
            }
        } else if (paymentStatus == "finished") {
            if (isSuccess) {
                AlertDialog.Builder(this).apply {
                    setTitle("Transaksi selesai!!")
                    setPositiveButton("Lanjut") { _, _ ->
                        val intent = Intent(this@DetailHistoryUserActivity, HistoryUserActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("Transaksi selesai gagal!")
                    setMessage("Silakan coba lagi.")
                    create()
                    show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}