package com.example.ocrapp.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding

    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null

    private lateinit var safeContext: Context

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    // Coroutine variables to run asynchronously request without stop UI
    private var job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    // Contract for: Open gallery and send image
    private val sendImageFromGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                // image selected
                val image = result.data?.data

                // Loading...
                binding.progressBar.visibility = View.VISIBLE
                binding.progressBar.isIndeterminate = true

                // Getting image as file

                val parcel = activity?.contentResolver?.openFileDescriptor(image!!, "r", null)
                val inputStream = FileInputStream(parcel?.fileDescriptor)
                val file =
                    File(activity?.cacheDir, activity?.contentResolver?.getFileName(image!!)!!)
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)

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
            detect()
        }

        binding.btnScan.setOnClickListener { }

        cameraExecutor = Executors.newSingleThreadExecutor()

        setupCamera()

    }

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

    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            preview = Preview.Builder().build()

            imageCapture = ImageCapture.Builder().build()

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


    // Intent to open gallery or internal content provider.
    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        sendImageFromGallery.launch(gallery)
    }

    // Open a gallery and the selected image will send it to server.
    private fun detect() {
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
                val detection = UtilsDetection(result).consumptionDetected(0.8)

                // Stop loading...
                binding.progressBar.isIndeterminate = false
                binding.progressBar.visibility = View.GONE

                // Show info


            } catch (e: Exception) {
                Log.e("Error", "Something fails")
            }
        }
    }

}