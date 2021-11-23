package com.example.chatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.chatapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSign.setOnClickListener {

            auth.createUserWithEmailAndPassword(binding.emailText.text.toString(), binding.passwordText.text.toString())
                .addOnCompleteListener(requireActivity()){
                    if (it.isSuccessful){
                       val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
                        findNavController().navigate(action)
                    }else{
                        Toast.makeText(requireContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }
        binding.btnLogin.setOnClickListener {
            auth.signInWithEmailAndPassword(binding.emailText.text.toString(), binding.passwordText.text.toString())
                .addOnCompleteListener(requireActivity()){
                    if (it.isSuccessful){
                        val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
                        findNavController().navigate(action)
                    }else{
                        Toast.makeText(context,"Email veya şifre yanlış!", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}