package com.example.fotografpaylasma

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.example.fotografpaylasma.databinding.FragmentFeedBinding
import com.example.fotografpaylasma.databinding.FragmentYuklemeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.UUID


class YuklemeFragment : Fragment() {
    private var _binding: FragmentYuklemeBinding? = null
    private val binding get() = _binding!!
    private lateinit var  activityResultLauncher : ActivityResultLauncher<Intent>
    private  lateinit var  permissionLauncher: ActivityResultLauncher<String>

    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var storeage : FirebaseStorage
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()
        auth=Firebase.auth
        storeage=Firebase.storage
        db=Firebase.firestore

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentYuklemeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.yukleButton.setOnClickListener{YukleTiklandi(it)}
        binding.imageView.setOnClickListener { GorselSec(it) }
    }

    fun YukleTiklandi(view: View){
        val uuid = UUID.randomUUID()
        val gorselAdi = "${uuid}.jpg"
        val reference = storeage.reference
        val gorselReferans=reference.child("images").child(gorselAdi)
        if (secilenGorsel != null){
            gorselReferans.putFile(secilenGorsel!!).addOnSuccessListener { uploadTask ->
                gorselReferans.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    val postMap = hashMapOf<String,Any>()
                    postMap.put("downloadUrl",downloadUrl)
                    postMap.put("email",auth.currentUser!!.email.toString())
                    postMap.put("comment",binding.commenText.text.toString())
                    postMap.put("date",Timestamp.now())

                    db.collection("Posts").add(postMap).addOnSuccessListener { documentReference ->
                        val action = YuklemeFragmentDirections.actionYuklemeFragmentToFeedFragment()
                        Navigation.findNavController(view).navigate(action)
                        Toast.makeText(requireContext(),"Post Paylaşıldı",Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { exception ->
                        Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_SHORT).show()
                    }
                }

            }.addOnFailureListener{ exception ->
                Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_SHORT).show()

            }
        }


    }
    fun GorselSec(view: View){


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.READ_MEDIA_IMAGES)) {
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",
                        View.OnClickListener {
                            permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                        }).show()
                } else {
                    permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                }
            } else {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",
                        View.OnClickListener {
                            permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()
                } else {
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            } else {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            }
        }

    }
    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if (result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if (intentFromResult != null){
                    secilenGorsel = intentFromResult.data
                    try {
                        if (Build.VERSION.SDK_INT >= 28){
                            val source = ImageDecoder.createSource(requireActivity().contentResolver,secilenGorsel!!)
                            secilenBitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageView.setImageBitmap(secilenBitmap)

                        }else{
                            secilenBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,secilenGorsel)
                            binding.imageView.setImageBitmap(secilenBitmap)
                        }

                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }

        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->
            if (result){
                val intentToGalery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGalery)
            }else{
                Toast.makeText(requireContext(),"İzini reddettiniz izine ihtiyacımız var.",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}