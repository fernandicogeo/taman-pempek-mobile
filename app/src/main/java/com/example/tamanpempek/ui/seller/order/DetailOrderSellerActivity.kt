package com.example.tamanpempek.ui.seller.order

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.example.tamanpempek.request.ProductUpdateRequest
import com.example.tamanpempek.request.ProductUpdateStockRequest
import com.example.tamanpempek.ui.user.history.HistoryUserActivity
import com.example.tamanpempek.viewmodel.CartViewModel
import com.example.tamanpempek.viewmodel.PaymentViewModel
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.UserViewModel
import com.example.tamanpempek.viewmodel.factory.CartViewModelFactory
import com.example.tamanpempek.viewmodel.factory.PaymentViewModelFactory
import com.example.tamanpempek.viewmodel.factory.ProductViewModelFactory
import com.example.tamanpempek.viewmodel.factory.UserViewModelFactory

class DetailOrderSellerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailOrderSellerBinding
    private val cartViewModel: CartViewModel by viewModels { cartFactory }
    private val paymentViewModel: PaymentViewModel by viewModels { paymentFactory }
    private val userViewModel: UserViewModel by viewModels { userFactory }
    private val productViewModel: ProductViewModel by viewModels { productFactory }
    private lateinit var cartFactory: CartViewModelFactory
    private lateinit var paymentFactory: PaymentViewModelFactory
    private lateinit var userFactory: UserViewModelFactory
    private lateinit var productFactory: ProductViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartFactory = CartViewModelFactory.getInstanceCart(binding.root.context)
        paymentFactory = PaymentViewModelFactory.getInstancePayment(binding.root.context)
        userFactory = UserViewModelFactory.getInstanceAuth(binding.root.context)
        productFactory = ProductViewModelFactory.getInstanceProduct(binding.root.context)

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

                    getProductName(cart.product_id)
                    getPaymentById(cart.payment_id)
                    getUserName(cart.user_id)
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun getProductName(productId: Int) {
        productViewModel.getProductById(productId).observe(this) { product ->
            Log.d("GETPRODUCTNAME", product.toString())
            when (product) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.ErrorState -> {
                }
                is ResultCondition.SuccessState -> {
                    binding.tvProduct.text = getString(R.string.product_template, product.data.data.name)
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
                    binding.tvDeliveryName.text = getString(R.string.delivery_template, payment.delivery_name)
                    binding.tvResi.text = getString(R.string.resi_template, payment.resi)

                    when (payment.payment_status) {
                        "waiting for sent" -> {
                            binding.tvPaymentStatus.text =
                                getString(R.string.payment_status_template, "Menunggu pesanan dikirim")
                            binding.tvDelivery.visibility = View.VISIBLE
                            binding.etDelivery.visibility = View.VISIBLE
                            binding.etlDelivery.visibility = View.VISIBLE
                            binding.tvResiNumber.visibility = View.VISIBLE
                            binding.etResiNumber.visibility = View.VISIBLE
                            binding.etlResiNumber.visibility = View.VISIBLE
                            binding.btnSend.visibility = View.VISIBLE
                            binding.tvDeliveryName.visibility = View.GONE
                            binding.tvResi.visibility = View.GONE

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
                            binding.tvResiNumber.visibility = View.GONE
                            binding.etResiNumber.visibility = View.GONE
                            binding.etlResiNumber.visibility = View.GONE
                            binding.btnSend.visibility = View.GONE
                            binding.tvDeliveryName.visibility = View.VISIBLE
                            binding.tvResi.visibility = View.VISIBLE
                        }
                        "finished" -> {
                            binding.tvPaymentStatus.text =
                                getString(R.string.payment_status_template, "Pesanan selesai")
                            binding.tvDelivery.visibility = View.GONE
                            binding.etDelivery.visibility = View.GONE
                            binding.etlDelivery.visibility = View.GONE
                            binding.tvResiNumber.visibility = View.GONE
                            binding.etResiNumber.visibility = View.GONE
                            binding.etlResiNumber.visibility = View.GONE
                            binding.btnSend.visibility = View.GONE
                            binding.tvDeliveryName.visibility = View.VISIBLE
                            binding.tvResi.visibility = View.VISIBLE
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
            val resi = binding.etResiNumber.text.toString()
            when {
                delivery.isEmpty() -> {
                    binding.etlDelivery.error = "Masukkan jasa pengiriman"
                }
                resi.isEmpty() -> {
                    binding.etlDelivery.error = "Masukkan nomor resi"
                }
                else -> {
                    updatePayment(paymentId, paymentStatus, delivery, resi)
                }
            }
        }
    }

    private fun updatePayment(id: Int, paymentStatus: String, deliveryName: String, resi: String) {
        val request = PaymentUpdateStatusAndDeliveryRequest(paymentStatus, deliveryName, resi)

        paymentViewModel.updatePaymentStatusAndDelivery(id, request).observe(this) {
            when (it) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.ErrorState -> {
                }
                is ResultCondition.SuccessState -> {
                    val cartId = intent.getIntExtra("CART_ID", -1)
                    updateProductStock(cartId)
                }
            }
        }
    }

    private fun updateProductStock(cartId: Int) {
        cartViewModel.getCartById(cartId).observe(this) { result ->
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    val cart = result.data.data
                    val productId = cart.product_id
                    val quantity = cart.quantity

                    productViewModel.getProductById(productId).observe(this) { product ->
                        when (product) {
                            is ResultCondition.LoadingState -> {
                            }
                            is ResultCondition.ErrorState -> {
                            }
                            is ResultCondition.SuccessState -> {
                                val stock = product.data.data.stock - quantity
                                val request = ProductUpdateStockRequest(stock)

                                productViewModel.updateProductStock(productId, request).observe(this) { result ->
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
                        }
                    }
                }
                is ResultCondition.ErrorState -> {
                    showDialog(false)
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