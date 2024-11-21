package com.example.fotografpaylasma

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.fotografpaylasma.databinding.FragmentKullaniciBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class KullaniciFragment : Fragment() {

    private var _binding: FragmentKullaniciBinding? = null
    private val binding get() = _binding!!

    private  lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKullaniciBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.kayitButton.setOnClickListener { KayitOl(it) }
        binding.girisButton.setOnClickListener { GirisYap(it) }
        val guncelKullanici = auth.currentUser
        if (guncelKullanici != null){
            val action = KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
            Navigation.findNavController(view).navigate(action)
        }

    }

    fun KayitOl(view: View){
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task ->
                if (task.isSuccessful){
                    //kullanıcı olusturuldu
                    val action = KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }.addOnFailureListener{exception ->
                Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun GirisYap(view:View){
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val action = KullaniciFragmentDirections.actionKullaniciFragmentToFeedFragment()
                Navigation.findNavController(view).navigate(action)
            }.addOnFailureListener{exception ->
                Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}