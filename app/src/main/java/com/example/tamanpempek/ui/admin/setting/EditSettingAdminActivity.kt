package com.example.tamanpempek.ui.admin.setting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.tamanpempek.R
import com.example.tamanpempek.databinding.ActivityEditSettingAdminBinding
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.helper.getFileSize
import com.example.tamanpempek.request.SettingRequest
import com.example.tamanpempek.response.SettingResponse
import com.example.tamanpempek.viewmodel.SettingViewModel
import com.example.tamanpempek.viewmodel.factory.SettingViewModelFactory

class EditSettingAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditSettingAdminBinding
    private val settingViewModel: SettingViewModel by viewModels { settingFactory }
    private lateinit var settingFactory: SettingViewModelFactory
    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSettingAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingFactory = SettingViewModelFactory.getInstanceSetting(binding.root.context)

        getSettingDetail()
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
    }

    private fun setupAction() {
        showLoading(false)

        binding.btnUploadImage.setOnClickListener {
            selectImage()
        }

        binding.btnSubmit.setOnClickListener {
            val description = binding.etDescription.text.toString()
            val contact = binding.etContact.text.toString()
            when {
                description.isEmpty() -> {
                    binding.etlDescription.error = "Masukkan deskripsi"
                }
                contact.isEmpty() -> {
                    binding.etlContact.error = "Masukkan kontak"
                }
                else -> {
                    updateSetting(1, selectedImageUri, description, contact)
                }
            }
        }
    }

    private fun updateSetting(id: Int, imageUri: Uri?, description: String, contact: String) {
        if (imageUri != null) {
            val fileSize = getFileSize(imageUri, this)
            if (fileSize > 500 * 1024) {
                Toast.makeText(this, "Ukuran gambar tidak boleh lebih dari 500KB", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val imageUriToUse = imageUri?.toString()

        val request = SettingRequest(imageUriToUse?.let { Uri.parse(it) }, description, contact)

        settingViewModel.updateSetting(id, request, this).observe(this) {
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

    private fun getSettingDetail() {
        settingViewModel.getSettingById(1).observe(this) {
            when (it) {
                is ResultCondition.LoadingState -> {
                    showLoading(true)
                }
                is ResultCondition.ErrorState -> {
                    showLoading(false)
                }
                is ResultCondition.SuccessState -> {
                    showLoading(false)
                    populateSettingDetails(it.data)
                }
            }
        }
    }

    private fun populateSettingDetails(setting: SettingResponse) {
        binding.etDescription.setText(setting.data.description)
        binding.etContact.setText(setting.data.contact)
        binding.btnUploadImage.text = getString(R.string.success_upload_image)
    }

    private fun showDialog(isSuccess: Boolean) {
        if (isSuccess) {
            AlertDialog.Builder(this).apply {
                setTitle("Edit setting berhasil!!")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(this@EditSettingAdminActivity, SettingAdminActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Edit setting gagal!")
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