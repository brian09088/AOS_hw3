package com.example.weatherapp

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weatherapp.databinding.FragmentMemoBinding


class MemoFragment : Fragment(){
    private var _binding:FragmentMemoBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentMemoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPreferences = activity?.getSharedPreferences("Memo", Context.MODE_PRIVATE)
        // Load the text using the same key "Memo"
        val savedInput = sharedPreferences?.getString("Memo", "")
        if(savedInput == null) {
            Toast.makeText(context, "saveInput is null", Toast.LENGTH_SHORT).show()
        }
        // Set the retrieved text to EditText
        binding.edittextMemo.setText(savedInput, TextView.BufferType.EDITABLE)
        binding.edittextMemo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            // put editText content into memory on every keystroke
            override fun afterTextChanged(s: Editable?) {
                val userInput = binding.edittextMemo.text.toString()
                val sharedPref = activity?.getSharedPreferences("Memo", Context.MODE_PRIVATE)
                with (sharedPref?.edit()) {
                    //binding.edittextMemo.toString()
                    //Toast.makeText(context,"Editor Works" , Toast.LENGTH_SHORT).show()
                    this?.putString("Memo", userInput)
                    this?.apply()
                }

            }
        })

        return root
    }
}