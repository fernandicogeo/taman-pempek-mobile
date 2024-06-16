package com.example.tamanpempek.ui.seller

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityAddProductSellerBinding
import com.example.tamanpempek.databinding.ActivityEditProductSellerBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.request.ProductCreateRequest
import com.example.tamanpempek.request.ProductUpdateRequest
import com.example.tamanpempek.response.ProductResponse
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.factory.ProductViewModelFactory

class EditProductSellerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProductSellerBinding
    private val productViewModel: ProductViewModel by viewModels { factory }
    private lateinit var factory: ProductViewModelFactory
    private lateinit var preference: UserPreference

    var selectedCategory: Int = 0
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProductSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ProductViewModelFactory.getInstanceProduct(binding.root.context)
        preference = UserPreference(this)

        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        if (productId != -1) {
            getProductDetail(productId)
        }

        setupView()
        setupAction(productId)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        val categories = arrayOf("Pempek", "Lainnya")
        val adapterCategory = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        binding.spinnerCategory.adapter = adapterCategory

        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedCategory = (position + 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun setupAction(id: Int) {
        showLoading(false)

        binding.btnUploadImage.setOnClickListener {
            selectImage()
        }

        val userId = preference.getLoginSession().id
        binding.btnSubmit.setOnClickListener {
            val name = binding.etName.text.toString()
            val description = binding.etDescription.text.toString()
            val price = binding.etPrice.text.toString().toIntOrNull()
            val stock = binding.etStock.text.toString().toIntOrNull()
            val category = selectedCategory
            when {
                name.isEmpty() -> {
                    binding.etlName.error = "Masukkan nama"
                }
                description.isEmpty() -> {
                    binding.etlDescription.error = "Masukkan deskripsi"
                }
                price == null -> {
                    binding.etlPrice.error = "Masukkan harga yang valid"
                }
                stock == null -> {
                    binding.etlStock.error = "Masukkan stok yang valid"
                }
                else -> {
                    updateProduct(id, userId, category, name, selectedImageUri, description, price, stock)
                }
            }
        }
    }

    private fun updateProduct(id: Int, userId: Int, categoryId: Int, name: String, imageUri: Uri?, description: String, price: Int, stock: Int) {
        val imageUriToUse = imageUri?.toString()

        val request = ProductUpdateRequest(userId, categoryId, name, imageUriToUse?.let { Uri.parse(it) }, description, price, stock)

        productViewModel.updateProduct(id, request, this).observe(this) {
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

    private fun getProductDetail(productId: Int) {
        productViewModel.getProductById(productId).observe(this) {
            when (it) {
                is ResultCondition.LoadingState -> {
                    showLoading(true)
                }
                is ResultCondition.ErrorState -> {
                    showLoading(false)
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    populateProductDetails(it.data)
                }
            }
        }
    }

    private fun populateProductDetails(product: ProductResponse) {
        binding.etName.setText(product.data.name)
        binding.etDescription.setText(product.data.description)
        binding.etPrice.setText(product.data.price.toString())
        binding.etStock.setText(product.data.stock.toString())
        binding.spinnerCategory.setSelection(product.data.category_id - 1)
        binding.btnUploadImage.text = getString(R.string.success_upload_image)
    }

    private fun showDialog(isSuccess: Boolean) {
        if (isSuccess) {
            AlertDialog.Builder(this).apply {
                setTitle("Edit produk berhasil!!")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(this@EditProductSellerActivity, DashboardSellerActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Edit produk gagal!")
                setMessage("Format inputan anda salah, coba lagi.")
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
