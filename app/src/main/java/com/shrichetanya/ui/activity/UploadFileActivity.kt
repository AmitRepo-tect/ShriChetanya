package com.shrichetanya.ui.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider

import com.shrichetanya.R
import com.shrichetanya.databinding.ActUploadFileLayoutBinding
import com.shrichetanya.utils.Cutil
import com.shrichetanya.utils.IntentConstant
import com.shrichetanya.utils.NetworkResult
import com.shrichetanya.utils.SharePreferenceConstant
import com.shrichetanya.viewmodel.HomeViewModel
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class UploadFileActivity : BaseActivity(), OnClickListener {

    lateinit var binding: ActUploadFileLayoutBinding
    val viewModel by viewModels<HomeViewModel>()
    private lateinit var cameraImageUri: Uri
val CAMERA_PERMISSION_REQUEST=123
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) startCrop(cameraImageUri)
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { startCrop(it) }
        }

    private val cropLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val croppedUri = UCrop.getOutput(result.data!!)
                croppedUri?.let { uploadImage(it) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActUploadFileLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        addObservers()
        setStatusBarColor(R.color.white)
    }

    private fun setListeners() {
        binding.cameraBtn.setOnClickListener(this@UploadFileActivity)
        binding.galleryBtn.setOnClickListener(this@UploadFileActivity)
    }

   /* private fun takePhoto() {
        val imageFile = File(externalCacheDir, "camera_image_${System.currentTimeMillis()}.jpg")
        cameraImageUri = FileProvider.getUriForFile(this, "$packageName.provider", imageFile)
        cameraLauncher.launch(cameraImageUri)
    }*/
    private fun takePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST
            )
        } else {
            openCamera()
        }
    }
    private fun openCamera() {
        val imageFile = File(externalCacheDir, "camera_image_${System.currentTimeMillis()}.jpg")
        cameraImageUri = FileProvider.getUriForFile(this, "$packageName.provider", imageFile)
        cameraLauncher.launch(cameraImageUri)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pickFromGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun startCrop(sourceUri: Uri) {
        val destinationFile = File(cacheDir, "cropped_image_${System.currentTimeMillis()}.jpg")
        val destinationUri = Uri.fromFile(destinationFile) // Ensures file:// URI

        val crop = UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(800, 800)

        cropLauncher.launch(crop.getIntent(this))
    }

    private fun uploadImage(uri: Uri) {
        val bookingId = intent.getIntExtra(IntentConstant.BOOKING_ID, 0).toString()
        val userId = Cutil.getStringFromSP(this, SharePreferenceConstant.USER_ID)

        val filePath = getPathFromUri(this, uri) ?: return
        val file = File(filePath)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val bookingIdPart = bookingId.toRequestBody("text/plain".toMediaTypeOrNull())
        val userIdPart = userId.toRequestBody("text/plain".toMediaTypeOrNull())

        viewModel.uploadBookingFile(bookingIdPart, userIdPart, body)
    }

    /**
     * Works for both content:// and file:// URIs on all Android versions.
     * Copies the file into cache if needed and returns its path.
     */
    private fun getPathFromUri(context: Context, uri: Uri): String? {
        return try {
            if ("file" == uri.scheme) {
                uri.path
            } else {
                val inputStream = context.contentResolver.openInputStream(uri) ?: return null
                val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
                tempFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                tempFile.absolutePath
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun addObservers() {
        viewModel.uploadFileResponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideProgressbar()
                    response.data?.let {
                        if (it.result.code == 1) {
                            showSuccesfulDialog(true)
                        }
                    }
                }

                is NetworkResult.Error -> {
                    hideProgressbar()
                    response.data?.result?.let { result ->
                        Toast.makeText(
                            this@UploadFileActivity,
                            "Error ${result.code} ${result.msg}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is NetworkResult.Loading -> {
                    showProgressbar()
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.gallery_btn -> pickFromGallery()
            R.id.camera_btn -> takePhoto()
        }
    }
}