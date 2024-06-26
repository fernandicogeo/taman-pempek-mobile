package com.example.tamanpempek.ui.admin.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityDetailPaymentAdminBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.request.PaymentUpdateStatusRequest
import com.example.tamanpempek.ui.adapter.admin.payment.PaymentDetailAdapterAdmin
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

class DetailPaymentAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPaymentAdminBinding
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
        binding = ActivityDetailPaymentAdminBinding.inflate(layoutInflater)
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
            layoutManager = GridLayoutManager(this@DetailPaymentAdminActivity, 1)
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

                    getUserName(payment.user_id)

                    binding.tvPrice.text = getString(R.string.price_template, payment.total_price.toString())
                    binding.tvAddress.text = getString(R.string.address_template, payment.address)
                    binding.tvWhatsapp.text = getString(R.string.whatsapp_template, payment.whatsapp)
                    binding.tvDeliveryName.text = getString(R.string.delivery_template, payment.delivery_name)
                    binding.tvResi.text = getString(R.string.resi_template, payment.resi)

                    Glide.with(this@DetailPaymentAdminActivity)
                        .load(payment.image)
                        .into(binding.headerImage)

                    when (payment.payment_status) {
                        "reviewed" -> {
                            binding.tvPaymentStatus.text =
                                getString(R.string.payment_status_template, "Menunggu review")
                            binding.btnConfirm.visibility = View.VISIBLE
                            binding.btnReject.visibility = View.VISIBLE
                            binding.tvDeliveryName.visibility = View.GONE
                            binding.tvResi.visibility = View.GONE
                        }
                        "waiting for sent" -> {
                            binding.tvPaymentStatus.text =
                                getString(R.string.payment_status_template, "Menunggu pesanan dikirim")
                            binding.btnConfirm.visibility = View.GONE
                            binding.btnReject.visibility = View.GONE
                            binding.tvDeliveryName.visibility = View.GONE
                            binding.tvResi.visibility = View.GONE
                        }
                        "sent" -> {
                            binding.tvPaymentStatus.text = getString(
                                R.string.payment_status_template,
                                "Pesanan sedang diantar"
                            )
                            binding.btnConfirm.visibility = View.GONE
                            binding.btnReject.visibility = View.GONE
                            binding.tvDeliveryName.visibility = View.VISIBLE
                            binding.tvResi.visibility = View.VISIBLE
                        }
                        "finished" -> {
                            binding.tvPaymentStatus.text =
                                getString(R.string.payment_status_template, "Pesanan selesai")
                            binding.btnConfirm.visibility = View.GONE
                            binding.btnReject.visibility = View.GONE
                            binding.tvDeliveryName.visibility = View.VISIBLE
                            binding.tvResi.visibility = View.VISIBLE
                        }
                        "canceled" -> {
                            binding.tvPaymentStatus.text =
                                getString(R.string.payment_status_template, "Pesanan dibatalkan pembeli")
                            binding.btnConfirm.visibility = View.GONE
                            binding.btnReject.visibility = View.GONE
                            binding.tvDeliveryName.visibility = View.GONE
                            binding.tvResi.visibility = View.GONE
                        }
                        "rejected" -> {
                            binding.tvPaymentStatus.text =
                                getString(R.string.payment_status_template, "Pesanan ditolak")
                            binding.btnConfirm.visibility = View.GONE
                            binding.btnReject.visibility = View.GONE
                            binding.tvDeliveryName.visibility = View.GONE
                            binding.tvResi.visibility = View.GONE
                        }
                    }

                    binding.btnReject.setOnClickListener {
                        AlertDialog.Builder(this).apply {
                            setTitle("Apakah anda yakin?")
                            setPositiveButton("Ya") { _, _ ->
                                updatePayment(result.data.data.id, "rejected")
                            }
                            create()
                            show()
                        }
                    }

                    binding.btnConfirm.setOnClickListener {
                        AlertDialog.Builder(this).apply {
                            setTitle("Apakah anda yakin?")
                            setPositiveButton("Ya") { _, _ ->
                                updatePayment(result.data.data.id, "waiting for sent")
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

    private fun getUserName(userId: Int) {
        userViewModel.getUserById(userId).observe(this) {
            when (it) {
                is ResultCondition.LoadingState -> {
                }

                is ResultCondition.SuccessState -> {
                    binding.tvUser.text = getString(R.string.user_template, it.data.data.name)
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
                    val adapter = PaymentDetailAdapterAdmin(it.data.data, productViewModel, userViewModel, this)
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
        if (paymentStatus == "rejected") {
            if (isSuccess) {
                AlertDialog.Builder(this).apply {
                    setTitle("Penolakan transaksi berhasil!!")
                    setPositiveButton("Lanjut") { _, _ ->
                        val intent = Intent(this@DetailPaymentAdminActivity, PaymentAdminActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("Penolakan transaksi gagal!")
                    setMessage("Silakan coba lagi.")
                    create()
                    show()
                }
            }
        } else if (paymentStatus == "waiting for sent") {
            if (isSuccess) {
                AlertDialog.Builder(this).apply {
                    setTitle("Konfirmasi transaksi berhasil!!")
                    setPositiveButton("Lanjut") { _, _ ->
                        val intent = Intent(this@DetailPaymentAdminActivity, PaymentAdminActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("Konfirmasi transaksi gagal!")
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