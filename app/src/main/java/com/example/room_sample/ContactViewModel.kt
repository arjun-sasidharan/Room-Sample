package com.example.room_sample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.room_sample.db.Contact
import com.example.room_sample.db.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    val contacts = repository.contacts

    val inputName = MutableLiveData<String>()
    val inputPhoneNo = MutableLiveData<String>()

    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {
        val name = inputName.value!!
        val phoneNo = inputPhoneNo.value!!
        insert(Contact(0, name, phoneNo))
        inputName.value = ""
        inputPhoneNo.value = ""
    }

    fun clearAllOrDelete() {
        clearAll()
    }

    fun insert(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(contact)
    }

    fun update(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(contact)
    }

    fun delete(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(contact)
    }

    fun clearAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

}