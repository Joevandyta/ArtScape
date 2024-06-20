package com.jovan.artscape.ui.upload

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.databinding.ActivityUploadBinding
import com.jovan.artscape.remote.response.ApiResponse
import com.jovan.artscape.ui.main.MainActivity
import com.jovan.artscape.utils.DialogUtils
import com.jovan.artscape.utils.NetworkUtils
import com.jovan.artscape.utils.reduceFileImage
import com.jovan.artscape.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.DecimalFormat

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private var previewImageUri: Uri? = null
    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (NetworkUtils.isNetworkAvailable(this))
            {
                bindingView()
                genrePrediction()
                topActionBar()
                actionSetup()
                enableButton()
                getUploadResponse()
            } else {
            showLoading(false)
            Log.d("ERROR", "Network Not Available")
            DialogUtils.showNetworkSettingsDialog(this)
        }
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showExitConfirmationDialog()
                }
            },
        )
    }

    private fun showExitConfirmationDialog() {
        MaterialAlertDialogBuilder(this).apply {
            setMessage("Are you sure you want to go back? Any unsaved changes will be lost.")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    finish()
                }.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            create()
            show()
        }
    }

    private fun topActionBar() {
        supportActionBar?.show()
        supportActionBar?.title = "Upload Art"
        val toolbar: Toolbar = binding.uploadToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun genrePrediction() {
        previewImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this@UploadActivity).reduceFileImage()
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

            val photo =
                MultipartBody.Part.createFormData(
                    "file",
                    imageFile.name,
                    requestImageFile,
                )
            viewModel.setGenreClasification(photo)
        } ?: showToast("Cant Predict Image")

        viewModel.getGenreClasification().observe(this) {
            when (it) {
                is ApiResponse.Success -> {
                    val data = it.data.predictions!!.first()
                    val decimalFormat = DecimalFormat("#.##")
                    val genre = data.className
                    val confidence = data.probability
                    val confidencePercentage = decimalFormat.format(confidence * 100) + "%"
                    binding.tvGenrePrediction.text = genre.toString()
                    binding.tvConfidance.text = confidencePercentage
                    setMyButtonEnable()
                }

                is ApiResponse.Error -> {
                    Log.d("ERROR", "${it.error} ${it.details}")
                    MaterialAlertDialogBuilder(this).apply {
                        setTitle("Failed Clasify Painting")
                        setMessage("Try Again Later")
                        setPositiveButton("Continue") { _, _ ->
                            startActivity(Intent(this@UploadActivity, MainActivity::class.java))
                            finish()
                        }
                        setCancelable(false)
                        create()
                        show()
                    }
                }
            }
        }
    }

    private fun bindingView() {
        previewImageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE))

        binding.apply {
            Glide
                .with(this@UploadActivity)
                .load(previewImageUri)
                .into(previewImageView)
        }
    }

    private fun enableButton() {
        binding.apply {
            edTitlePainting.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        setMyButtonEnable()
                    }
                },
            )

            edPrice.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        setMyButtonEnable()
                    }
                },
            )

            edPaintingMedia.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        setMyButtonEnable()
                    }
                },
            )

            edYearCreated.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        setMyButtonEnable()
                    }
                },
            )

            edDescription.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        setMyButtonEnable()
                    }
                },
            )
        }
    }

    private fun isValid(editText: TextInputEditText): Boolean = editText.text.toString().isNotEmpty()

    fun setMyButtonEnable() {
        binding.apply {
            val isTitleValid = isValid(edTitlePainting)
            val isPriceValid = isValid(edPrice)
            val isMediaValid = isValid(edPaintingMedia)
            val isYearCreatedValid = isValid(edYearCreated)
            val isDescriptionValid = isValid(edDescription)
            val isGenreValid = binding.tvConfidance.text.isNotEmpty()

            buttonAdd.isEnabled =
                isTitleValid &&
                isPriceValid &&
                isMediaValid &&
                isYearCreatedValid &&
                isDescriptionValid &&
                isGenreValid
        }
    }

    private fun actionSetup() {
        binding.buttonAdd.setOnClickListener {
            binding.apply {
                previewImageUri?.let { uri ->
                    showLoading(true)
                    val imageFile = uriToFile(uri, this@UploadActivity).reduceFileImage()
                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

                    val photo =
                        MultipartBody.Part.createFormData(
                            "photo",
                            imageFile.name,
                            requestImageFile,
                        )

                    val title = edTitlePainting.text.toString().toRequestBody()
                    val description = edDescription.text.toString().toRequestBody()
                    val media = edPaintingMedia.text.toString().toRequestBody()
                    val genre = tvGenrePrediction.text.toString().toRequestBody()
                    val price = edPrice.text.toString().toRequestBody()
                    val yearCreated = edYearCreated.text.toString().toRequestBody()
                    Log.d("PARAM", "addUser: \"${edYearCreated.text}\"")

                    viewModel.getSession().observe(this@UploadActivity) {
                        viewModel.setUploadPainting(
                            photo,
                            title,
                            description,
                            media,
                            genre,
                            price,
                            yearCreated,
                            it.uid.toRequestBody(),
                        )
                    }
                } ?: showToast("Image not found")
            }
        }
    }

    private fun getUploadResponse() {
        viewModel.getUploadResponse().observe(this) {
            when (it) {
                is ApiResponse.Success -> {
                    Log.d("UPLOAD SUCCESS", "${it.data.id} ${it.data.message}")
                    showLoading(false)
                    MaterialAlertDialogBuilder(this).apply {
                        setTitle("Upload Success")
                        setMessage("New painting successfully uploaded. Return to the home")
                        setPositiveButton("Continue") { _, _ ->
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        setCancelable(false)
                        create()
                        show()
                    }
                }

                is ApiResponse.Error -> {
                    showLoading(false)
                    Log.d("UPLOAD FAILED", "${it.error} ${it.details}")
                    MaterialAlertDialogBuilder(this).apply {
                        setTitle("Upload Failed")
                        setMessage("Try Again!")
                        setPositiveButton("Continue") { _, _ ->
                        }
                        setCancelable(false)
                        create()
                        show()
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
