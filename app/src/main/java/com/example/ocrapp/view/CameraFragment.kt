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
import com.google.android.material.snackbar.Snackbar
import com.squareup.okhttp.MediaType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding

    private val resultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

        if(result.resultCode == Activity.RESULT_OK){

            binding.photo.setImageURI(result.data?.data)



        }else{

            Log.e("Error",result.toString())

        }

    }

    private val externalPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->

        if(isGranted){
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            resultContract.launch(gallery)
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

    private fun detect() {

        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            resultContract.launch(gallery)
        }else{
            externalPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    }

    private fun requestDetection(image: File){

        val requestImage = image.asRequestBody("multipart/form-data".toMediaType())
        val body = MultipartBody.Part.createFormData("image", image.name, requestImage)

        CoroutineScope(Dispatchers.IO).launch {

            val requestDetection = DetectService.api.detect(body)

            if(requestDetection.isSuccessful){
                Log.d("API", requestDetection.body()?.boxes.toString())
                Log.d("API", requestDetection.body()?.scores.toString())
                Log.d("API", requestDetection.body()?.classes.toString())
            }else{
                Log.d("API", "Ha ocurrido un error con la peticion")
            }

        }

    }

}