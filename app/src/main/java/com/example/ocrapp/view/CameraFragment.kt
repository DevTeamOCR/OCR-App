package com.example.ocrapp.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ocrapp.R
import com.example.ocrapp.api.DetectService
import com.example.ocrapp.api.UtilsDetection
import com.example.ocrapp.databinding.FragmentCameraBinding
import com.example.ocrapp.util.getFileName
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding

    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    // Coroutine variables to run asynchronously request without stop UI
    private var job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGallery.setOnClickListener {
            selectImage()
        }

        binding.btnScan.setOnClickListener { takePhoto() }

        cameraExecutor = Executors.newSingleThreadExecutor()

        outputDirectory = getOutputDirectory()

        setupCamera()

    }



    // Contract for: Open gallery and send image
    private val sendImageFromGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                // image selected
                val image = result.data?.data

                // Getting image as file

                val parcel = activity?.contentResolver?.openFileDescriptor(image!!, "r", null)
                val inputStream = FileInputStream(parcel?.fileDescriptor)
                val file =
                    File(activity?.cacheDir, activity?.contentResolver?.getFileName(image!!)!!)
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)

                showPhoto(file)

                // Sending request...
                requestDetection(file)

            } else {
                Log.e("Error", result.toString())
            }
        }

    // Contract for: Check external permission
    private val externalPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                Snackbar.make(
                    binding.btnGallery,
                    "No se tienen permisos para acceder a la galeria",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    // Contract for: Check camera permission
    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

            if (isGranted) {
                startCamera()
            } else {
                Snackbar.make(
                    binding.btnGallery,
                    "No se tienen permisos para usar la cámara",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    // Setup the camera asking permissions or starting if the permission are already granted
    private fun setupCamera() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            cameraPermission.launch(Manifest.permission.CAMERA)
        }

    }

    // Start camera taking care the lifecycle
    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            preview = Preview.Builder().build()

            // Capture for quality
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            } catch (ex: Exception) {
                Log.e("Camera", "Camera fail", ex)
            }

        }, ContextCompat.getMainExecutor(requireContext()))

    }

    // Take photo and save in storage
    private fun takePhoto(){

        val imageCapture = imageCapture ?: return

        val fileName = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis())+".jpg"
        val photoFile = File(outputDirectory, fileName)


        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
            .build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()),
        object : ImageCapture.OnImageSavedCallback{

            override fun onError(exception: ImageCaptureException) {
                Log.e("Picture took Error", "Failed", exception)
                Snackbar.make(binding.viewFinder, "Error taking picture", Snackbar.LENGTH_SHORT).show()
            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                showPhoto(photoFile)

                requestDetection(photoFile)

            }

        })

    }

    private fun showPhoto(photoFile: File){

        // Saves image in outputDirectory variable
        val savedUri = Uri.fromFile(photoFile)

        binding.apply {
            imageView.visibility = View.VISIBLE
            imageView.setImageURI(savedUri)
            viewFinder.visibility = View.GONE
            btnScan.visibility = View.GONE
            btnGallery.visibility = View.GONE
            progressBar.isIndeterminate = true
            progressBar.visibility = View.VISIBLE
        }

    }

    private fun getOutputDirectory(): File {
        // Here save on file explorer in a private folder for the app
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    // Intent to open gallery or internal content provider.
    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        sendImageFromGallery.launch(gallery)
    }

    // Open a gallery and the selected image will send it to server.
    private fun selectImage() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        } else {
            externalPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    // Send image to server and get the inference.
    private fun requestDetection(image: File) {

        val requestImage = image.asRequestBody("multipart/form-data".toMediaType())
        val requestBody = MultipartBody.Part.createFormData("image", image.name, requestImage)

        coroutineScope.launch {
            val detectionResponse = DetectService.api.detect(requestBody)
            try {
                // Getting the response
                var result = detectionResponse.await()

                // Converting response to a readable data
                val detection = UtilsDetection(result).consumptionDetected(0.5)

                // Stop loading...
                binding.progressBar.isIndeterminate = false
                binding.progressBar.visibility = View.GONE

                // Show info
                Snackbar.make(binding.viewFinder,"Consumption: $detection",Snackbar.LENGTH_SHORT).show()


            } catch (e: Exception) {
                Log.e("Error", "Something fails")
            }
        }
    }

}