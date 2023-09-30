package com.example.room_sample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.room_sample.db.Contact
import com.example.room_sample.db.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    val contacts = repository.contacts

    private var isUpdateOrDelete = false
    private lateinit var contactToUpdateOrDelete: Contact

    val inputName = MutableLiveData<String>()
    val inputPhoneNo = MutableLiveData<String>()

    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {
        if (isUpdateOrDelete) {
            contactToUpdateOrDelete.name = inputName.value!!
            contactToUpdateOrDelete.phoneNo = inputPhoneNo.value!!
            update(contactToUpdateOrDelete)
        } else {
            val name = inputName.value!!
            val phoneNo = inputPhoneNo.value!!
            insert(Contact(0, name, phoneNo))
            inputName.value = ""
            inputPhoneNo.value = ""
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            delete(contactToUpdateOrDelete)
        } else {
            clearAll()
        }
    }

    fun initUpdateAndDelete(contact: Contact) {
        inputName.value = contact.name
        inputPhoneNo.value = contact.phoneNo
        isUpdateOrDelete = true
        contactToUpdateOrDelete = contact
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    private fun insert(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(contact)
    }

    private fun update(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(contact)
        withContext(Dispatchers.Main) {
            inputName.value = ""
            inputPhoneNo.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
        }
    }

    private fun delete(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(contact)
        withContext(Dispatchers.Main) {
            inputName.value = ""
            inputPhoneNo.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
        }
    }

    private fun clearAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}