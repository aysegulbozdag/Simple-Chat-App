package com.example.chatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: ChatRecycAdapter
    private var chats = arrayListOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = Firebase.firestore
        auth = Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ChatRecycAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.btnSend.setOnClickListener {
            auth.currentUser?.let { it ->
                val user = it.email
                val chatText = binding.editChat.text.toString()
                val date = FieldValue.serverTimestamp()

                val dataMap = HashMap<String, Any>()
                dataMap["chatText"] = chatText
                user?.let { it1 -> dataMap.put("user", it1) }
                dataMap["date"] = date

                firestore.collection("Chats").add(dataMap).addOnSuccessListener {
                    binding.editChat.setText("")
                }.addOnFailureListener { ex ->
                    Toast.makeText(requireContext(), ex.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                    binding.editChat.setText("")
                }
            }
        }

        firestore.collection("Chats").orderBy("date", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                } else if (value != null) {
                    if (value.isEmpty) {
                        Toast.makeText(requireContext(), "Mesaj yok!", Toast.LENGTH_SHORT).show()
                    } else {
                        val documents = value.documents
                        chats.clear()
                        for (document in documents) {
                            val text = document.get("text") as String
                            val user = document.get("user") as String
                            val chat = Chat(text, user)
                            chats.add(chat)
                            adapter.chats = chats
                        }

                        adapter.notifyDataSetChanged()
                    }

                }
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}