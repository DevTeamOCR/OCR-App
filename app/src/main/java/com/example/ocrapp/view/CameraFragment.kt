package com.example.ocrapp.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ocrapp.api.DetectService
import com.example.ocrapp.databinding.FragmentCameraBinding
import com.example.ocrapp.util.getFileName
import com.google.android.material.snackbar.Snackbar
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.RequestBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding

    // Contract for: Open gallery and send image
    private val sendImageFromGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK){

            val image = result.data?.data
            binding.photo.setImageURI(image)

            // Getting image as file
            val parcel = activity?.contentResolver?.openFileDescriptor(image!!,"r", null)
            val inputStream = FileInputStream(parcel?.fileDescriptor)
            val file = File(activity?.cacheDir, activity?.contentResolver?.getFileName(image!!)!!)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)

            // Sending request...
            requestDetection(file)

        }else{
            Log.e("Error",result.toString())
        }
    }

    // Contract for: Check external permission
    private val externalPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
        if(isGranted){
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            sendImageFromGallery.launch(gallery)
        }else{
            Snackbar.make(binding.photo, "No se tienen permisos para acceder a la galeria", Snackbar.LENGTH_SHORT).show()
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

        binding.button.setOnClickListener {
            detect()
        }

    }

    // Open a gallery and the selected image will send it to server.
    private fun detect() {

        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            sendImageFromGallery.launch(gallery)
        }else{
            externalPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    }

    // Send image to server and get the inference.
    private fun requestDetection(image: File){

        val requestImage = image.asRequestBody("multipart/form-data".toMediaType())
        val requestBody = MultipartBody.Part.createFormData("image", image.name, requestImage)

        CoroutineScope(Dispatchers.IO).launch {

            // Sending image to server...
            val requestDetection = DetectService.api.detect(requestBody)

            // If everything is ok
            if(requestDetection.isSuccessful){

                // Do something with the response
                binding.response.text = ""
                binding.response.append("${requestDetection.body()?.boxes.toString()} \n")
                binding.response.append("${requestDetection.body()?.classes.toString()} \n")
                binding.response.append("${requestDetection.body()?.scores.toString()} \n")

                val boxes = requestDetection.body()?.boxes?.get(0)
                val classes = requestDetection.body()?.classes?.get(0)
                val scores = requestDetection.body()?.scores?.get(0)

            // If something goes wrong
            }else{
                Log.d("API", "Ha ocurrido un error con la peticion")
            }

        }

    }

}