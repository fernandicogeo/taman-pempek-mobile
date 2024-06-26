package com.example.tamanpempek.ui.user.cart

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityPaymentUserBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.helper.getFileSize
import com.example.tamanpempek.model.CartModel
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.request.PaymentCreateRequest
import com.example.tamanpempek.ui.adapter.user.bank.BankAdapterUser
import com.example.tamanpempek.ui.adapter.user.cart.PaymentAdapter
import com.example.tamanpempek.ui.user.history.HistoryUserActivity
import com.example.tamanpempek.ui.user.product.DashboardUserActivity
import com.example.tamanpempek.ui.user.profile.ProfileUserActivity
import com.example.tamanpempek.ui.user.setting.SettingUserActivity
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

class PaymentUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentUserBinding
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

    private lateinit var preference: UserPreference
    private var totalPrice: Int = 0
    private var selectedImageUri: Uri? = null
    private var checkoutedCarts: List<CartModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartFactory = CartViewModelFactory.getInstanceCart(binding.root.context)
        productFactory = ProductViewModelFactory.getInstanceProduct(binding.root.context)
        userFactory = UserViewModelFactory.getInstanceAuth(binding.root.context)
        bankFactory = BankViewModelFactory.getInstanceBank(binding.root.context)
        paymentFactory = PaymentViewModelFactory.getInstancePayment(binding.root.context)

        preference = UserPreference(this)

        val userId = preference.getLoginSession().id

        setupRecyclerView()
        getCheckoutedCartsByUser(userId)
        getAdminBanks()

        getTotalPrice(userId)
        btnAction(userId)
        bottomNav()
    }

    private fun setupRecyclerView() {
        binding.rvPayment.apply {
            layoutManager = GridLayoutManager(this@PaymentUserActivity, 1)
            setHasFixedSize(true)
        }
        binding.rvRekeningUser.apply {
            layoutManager = GridLayoutManager(this@PaymentUserActivity, 1)
            setHasFixedSize(true)
        }
    }

    private fun getCheckoutedCartsByUser(userId: Int) {
        cartViewModel.getStatusCartByUser("checkouted", userId).observe(this) {
            showLoading(true)
            when (it) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    checkoutedCarts = it.data.data
                    val adapter = PaymentAdapter(it.data.data, productViewModel, userViewModel, this)
                    binding.rvPayment.adapter = adapter
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun getAdminBanks() {
        bankViewModel.getAdminBanks().observe(this) {
            when (it) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    val adapter = BankAdapterUser(it.data.data)
                    binding.rvRekeningUser.adapter = adapter
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun getTotalPrice(userId: Int) {
        cartViewModel.getCartsTotalPriceByUser("checkouted", userId).observe(this) {
            when (it) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    binding.tvPrice.text = getString(R.string.price_template, it.data.data.toString())
                    totalPrice = it.data.data
                }
                is ResultCondition.ErrorState -> {
                }
            }
        }
    }

    private fun btnAction(userId: Int) {
        binding.etWhatsapp.setText(preference.getLoginSession().whatsapp)

        binding.btnUploadImage.setOnClickListener {
            selectImage()
        }
        binding.btnPayment.setOnClickListener {
            val address = binding.etAddress.text.toString()
            val whatsapp = binding.etWhatsapp.text.toString()
            when {
                address.isEmpty() -> {
                    binding.etlAddress.error = "Masukkan alamat pengiriman"
                }
                whatsapp.isEmpty() -> {
                    binding.etlWhatsapp.error = "Masukkan nomor whatsapp"
                }
                selectedImageUri == null -> {
                    Toast.makeText(this, "Masukkan bukti transfer", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    createProduct(
                        userId,
                        -1,
                        totalPrice,
                        selectedImageUri!!,
                        address,
                        whatsapp,
                        "reviewed",
                        "null",
                        "null",
                    )
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Apakah anda yakin?")
                setPositiveButton("Ya") { _, _ ->
                    checkoutedCarts.forEach { cart ->
                        updateStatusCarts(cart.id, "canceled")
                    }
                }
                create()
                show()
            }
        }
    }

    private fun createProduct(userId: Int, deliveryId: Int?, totalPrice: Int, imageUri: Uri?, address: String, whatsapp: String, paymentStatus: String, deliveryStatus: String?, resi: String?) {
        if (imageUri == null) {
            Toast.makeText(this, "Masukkan gambar", Toast.LENGTH_SHORT).show()
            return
        }

        val fileSize = getFileSize(imageUri, this)
        if (fileSize > 500 * 1024) {
            Toast.makeText(this, "Ukuran gambar tidak boleh lebih dari 500KB", Toast.LENGTH_SHORT).show()
            return
        }

        paymentViewModel.createPayment(PaymentCreateRequest(userId, deliveryId, totalPrice, imageUri, address, whatsapp, paymentStatus, deliveryStatus, resi), this).observe(this) {
            Log.d("ITCREATEPAYMENT", it.toString())
            when (it) {
                is ResultCondition.LoadingState -> {
                    showLoading(true)
                }
                is ResultCondition.ErrorState -> {
                    showLoading(false)
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    checkoutedCarts.forEach { cart ->
                        updatePaymentIdCarts(cart.id, it.data.data.id)
                        updateStatusCarts(cart.id, "paid")
                    }
                }
            }
        }
    }

    private fun updateStatusCarts(cartId: Int, isActived: String) {
        cartViewModel.updateStatusCart(cartId, isActived).observe(this) {
            when (it) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.ErrorState -> {
                    showDialog(false, isActived)
                }
                is ResultCondition.SuccessState -> {
                    showDialog(true, isActived)
                }
            }
        }
    }

    private fun updatePaymentIdCarts(cartId: Int, paymentId: Int) {
        cartViewModel.updatePaymentIdCarts(cartId, paymentId).observe(this) {
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

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        selectImageLauncher.launch(intent)
    }

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            selectedImageUri = data?.data
            binding.btnUploadImage.text = getString(R.string.success_upload_image)
        }
    }

    private fun showDialog(isSuccess: Boolean, isActived: String) {
        if (isActived == "paid") {
            if (isSuccess) {
                AlertDialog.Builder(this).apply {
                    setTitle("Pembayaran berhasil!! Silakan menunggu verifikasi dari admin.")
                    setPositiveButton("Lanjut") { _, _ ->
                        val intent = Intent(this@PaymentUserActivity, HistoryUserActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("Pembayaran gagal!")
                    setMessage("Format inputan anda salah, coba lagi.")
                    create()
                    show()
                }
            }
        } else {
            if (isSuccess) {
                AlertDialog.Builder(this).apply {
                    setTitle("Pembatalan transaksi berhasil!!")
                    setPositiveButton("Lanjut") { _, _ ->
                        val intent = Intent(this@PaymentUserActivity, DashboardUserActivity::class.java)
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
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    private fun bottomNav() {
        binding.bottomNavigationView.setOnNavigationItemReselectedListener { item ->
            when(item.itemId) {
                R.id.dashboard_user -> {
                    startActivity(Intent(this, DashboardUserActivity::class.java))
                }
                R.id.cart_user -> {
                    startActivity(Intent(this, CartUserActivity::class.java))
                }
                R.id.history_user -> {
                    startActivity(Intent(this, HistoryUserActivity::class.java))
                }
                R.id.profile_user -> {
                    startActivity(Intent(this, ProfileUserActivity::class.java))
                }
                R.id.setting_user -> {
                    startActivity(Intent(this, SettingUserActivity::class.java))
                }
            }
        }
        binding.bottomNavigationView.setSelectedItemId(R.id.cart_user)
    }
}