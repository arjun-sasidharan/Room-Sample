package com.example.room_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.room_sample.databinding.ActivityMainBinding
import com.example.room_sample.db.ContactDatabase
import com.example.room_sample.db.ContactRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var contactViewModel: ContactViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val dao = ContactDatabase.getInstance(application).contactDAO
        val repository = ContactRepository(dao)
        val factory = ContactViewModelFactory(repository)
        contactViewModel = ViewModelProvider(this, factory)[ContactViewModel::class.java]

        binding.myViewModel = contactViewModel
        // since we are using live data with data binding, need to specify the lifecycle owner
        binding.lifecycleOwner = this
        displayContactsList()
    }

    private fun displayContactsList() {
        contactViewModel.contacts.observe(this) {
            Log.i("TAG", it.toString())
        }
    }
}