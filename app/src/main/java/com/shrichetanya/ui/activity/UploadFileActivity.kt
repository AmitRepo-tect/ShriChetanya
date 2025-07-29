package com.shrichetanya.ui.activity

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.shrichetanya.R
import com.shrichetanya.databinding.ActDeliveryLayoutBinding
import com.shrichetanya.databinding.ActHomeLayoutBinding
import com.shrichetanya.databinding.ActUploadFileLayoutBinding
import com.shrichetanya.model.Client
import com.shrichetanya.utils.Constants
import com.shrichetanya.utils.Cutil
import com.shrichetanya.utils.IntentConstant
import com.shrichetanya.utils.NetworkResult
import com.shrichetanya.utils.SharePreferenceConstant
import com.shrichetanya.viewmodel.HomeViewModel
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull

@AndroidEntryPoint
class UploadFileActivity : BaseActivity(), OnClickListener {
    lateinit var binding: ActUploadFileLayoutBinding
    val viewModel by viewModels<HomeViewModel>()
    private lateinit var selectedImageUri: Uri
    private lateinit var cameraImageUri: Uri

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

    private fun takePhoto() {
        val imageFile = File(externalCacheDir, "camera_image.jpg")
        cameraImageUri = FileProvider.getUriForFile(this, "$packageName.provider", imageFile)
        cameraLauncher.launch(cameraImageUri)
    }

    private fun pickFromGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun startCrop(sourceUri: Uri) {
        val destinationFile = File(cacheDir, "cropped_image.jpg")
        val destinationUri = Uri.fromFile(destinationFile)

        val crop = UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(800, 800)

        cropLauncher.launch(crop.getIntent(this))
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


    private fun uploadImage(uri: Uri) {
        val bookingId = intent.getIntExtra(IntentConstant.BOOKING_ID,0).toString() // replace with dynamic value
        val userId = Cutil.getStringFromSP(
            this,
            SharePreferenceConstant.USER_ID
        )

        val filePath = getPathFromUri(this, uri) ?: return
        val file = File(filePath)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val bookingIdPart = bookingId.toRequestBody("text/plain".toMediaTypeOrNull())
        val userIdPart = userId.toRequestBody("text/plain".toMediaTypeOrNull())
        viewModel.uploadBookingFile(bookingIdPart, userIdPart, body)
    }


    /*  private fun getPathFromUri(context: Context, uri: Uri): String? {
          val projection = arrayOf(MediaStore.Images.Media.DATA)
          val cursor = context.contentResolver.query(uri, projection, null, null, null)
          return cursor?.use {
              it.moveToFirst()
              val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
              it.getString(columnIndex)
          }
      }*/
    private fun getPathFromUri(context: Context, uri: Uri): String? {
        return when (uri.scheme) {
            "file" -> uri.path // direct file path
            "content" -> {
                val projection = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = context.contentResolver.query(uri, projection, null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        return it.getString(columnIndex)
                    }
                }
                null
            }
            else -> null
        }
    }

    private fun addObservers() {

        viewModel.uploadFileResponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideProgressbar()
                    response.data?.let {
                        if (it.result.code == 1) {
                            showSuccesfulDialog()
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
            R.id.gallery_btn -> {
                pickFromGallery()
            }

            R.id.camera_btn -> {
                takePhoto()
            }
        }
    }
}
