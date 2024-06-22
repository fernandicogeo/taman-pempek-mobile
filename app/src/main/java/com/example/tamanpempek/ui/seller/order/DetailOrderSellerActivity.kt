package com.example.tamanpempek.ui.seller.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityDetailHistoryUserBinding
import com.example.tamanpempek.databinding.ActivityDetailOrderSellerBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.request.PaymentUpdateStatusAndDeliveryRequest
import com.example.tamanpempek.request.PaymentUpdateStatusRequest
import com.example.tamanpempek.ui.user.history.HistoryUserActivity
import com.example.tamanpempek.viewmodel.CartViewModel
import com.example.tamanpempek.viewmodel.PaymentViewModel
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.CartViewModelFactory
import com.example.tamanpempek.viewmodel.factory.PaymentViewModelFactory
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory

class DetailOrderSellerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailOrderSellerBinding
    private val cartViewModel: CartViewModel by viewModels { cartFactory }
    private val paymentViewModel: PaymentViewModel by viewModels { paymentFactory }
    private val userViewModel: UserViewModel by viewModels { userFactory }
    private lateinit var cartFactory: CartViewModelFactory
    private lateinit var paymentFactory: PaymentViewModelFactory
    private lateinit var userFactory: UserViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartFactory = CartViewModelFactory.getInstanceCart(binding.root.context)
        paymentFactory = PaymentViewModelFactory.getInstancePayment(binding.root.context)
        userFactory = UserViewModelFactory.getInstanceAuth(binding.root.context)

        val cartId = intent.getIntExtra("CART_ID", -1)

        getDetailCart(cartId)
    }

    private fun getDetailCart(id: Int) {
        cartViewModel.getCartById(id).observe(this) { result ->
            showLoading(true)
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    val cart = result.data.data

                    binding.tvQuantity.text = getString(R.string.quantity_template, cart.quantity.toString())
                    binding.tvPrice.text = getString(R.string.price_template, cart.total_price.toString())

                    getPaymentById(cart.payment_id)
                    getUserName(cart.user_id)
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun getPaymentById(paymentId: Int) {
        paymentViewModel.getPaymentById(paymentId).observe(this) {result ->
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    val payment = result.data.data

                    binding.tvAddress.text = getString(R.string.address_template, payment.address)
                    binding.tvWhatsapp.text = getString(R.string.whatsapp_template, payment.whatsapp)
                    binding.tvPaymentStatus.text = getString(R.string.payment_status_template, payment.payment_status)

                    when (payment.payment_status) {
                        "waiting for sent" -> {
                            binding.tvPaymentStatus.text =
                                getString(R.string.payment_status_template, "Menunggu pesanan dikirim")
                            binding.tvDelivery.visibility = View.VISIBLE
                            binding.etDelivery.visibility = View.VISIBLE
                            binding.etlDelivery.visibility = View.VISIBLE
                            binding.btnSend.visibility = View.VISIBLE

                            btnAction(payment.id, "sent")
                        }
                        "sent" -> {
                            binding.tvPaymentStatus.text = getString(
                                R.string.payment_status_template,
                                "Pesanan sedang diantar"
                            )
                            binding.tvDelivery.visibility = View.GONE
                            binding.etDelivery.visibility = View.GONE
                            binding.etlDelivery.visibility = View.GONE
                            binding.btnSend.visibility = View.GONE
                        }
                        "finished" -> {
                            binding.tvPaymentStatus.text =
                                getString(R.string.payment_status_template, "Pesanan selesai")
                            binding.tvDelivery.visibility = View.GONE
                            binding.etDelivery.visibility = View.GONE
                            binding.etlDelivery.visibility = View.GONE
                            binding.btnSend.visibility = View.GONE
                        }
                    }
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun getUserName(userId: Int) {
        userViewModel.getUserById(userId).observe(this) {result ->
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    val user = result.data.data
                    binding.tvUser.text = getString(R.string.user_template, user.name)
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun btnAction(paymentId: Int, paymentStatus: String) {
        binding.btnSend.setOnClickListener {
            val delivery = binding.etDelivery.text.toString()
            when {
                delivery.isEmpty() -> {
                    binding.etlDelivery.error = "Masukkan jasa pengiriman"
                }
                else -> {
                    updatePayment(paymentId, paymentStatus, delivery)
                }
            }
        }
    }

    private fun updatePayment(id: Int, paymentStatus: String, deliveryName: String) {
        val request = PaymentUpdateStatusAndDeliveryRequest(paymentStatus, deliveryName)

        paymentViewModel.updatePaymentStatusAndDelivery(id, request).observe(this) {
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

    private fun showDialog(isSuccess: Boolean) {
        if (isSuccess) {
            AlertDialog.Builder(this).apply {
                setTitle("Produk berhasil dikirim!!")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(this@DetailOrderSellerActivity, OrderSellerActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Produk gagal dikirim!")
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