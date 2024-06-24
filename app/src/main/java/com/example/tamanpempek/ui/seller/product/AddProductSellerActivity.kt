package com.example.tamanpempek.ui.seller.product

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityAddProductSellerBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.helper.getFileSize
import com.example.tamanpempek.preference.UserPreference
import com.example.tamanpempek.request.ProductCreateRequest
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.factory.ProductViewModelFactory

class AddProductSellerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductSellerBinding
    private val productViewModel: ProductViewModel by viewModels { factory }
    private lateinit var factory: ProductViewModelFactory
    private lateinit var preference: UserPreference

    var selectedCategory: Int = 0
    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ProductViewModelFactory.getInstanceProduct(binding.root.context)
        preference = UserPreference(this)

        setupView()
        setupAction()
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

    private fun setupAction() {
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
                selectedImageUri == null -> {
                    Toast.makeText(this, "Masukkan gambar", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    createProduct(userId, category, name, selectedImageUri!!, description, price, stock)
                }
            }
        }
    }

    private fun createProduct(userId: Int, categoryId: Int, name: String, imageUri: Uri?, description: String, price: Int, stock: Int) {
        if (imageUri == null) {
            Toast.makeText(this, "Masukkan gambar", Toast.LENGTH_SHORT).show()
            return
        }

        val fileSize = getFileSize(imageUri, this)
        if (fileSize > 500 * 1024) {
            Toast.makeText(this, "Ukuran gambar tidak boleh lebih dari 500KB", Toast.LENGTH_SHORT).show()
            return
        }

        productViewModel.createProduct(ProductCreateRequest(userId, categoryId, name, imageUri, description, price, stock), this).observe(this) {
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

    private fun showDialog(isSuccess: Boolean) {
        if (isSuccess) {
            AlertDialog.Builder(this).apply {
                setTitle("Tambah produk berhasil!!")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(this@AddProductSellerActivity, DashboardSellerActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Tambah produk gagal!")
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
