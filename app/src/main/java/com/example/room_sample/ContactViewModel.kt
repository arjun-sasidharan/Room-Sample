package com.example.room_sample

import android.util.Patterns
import androidx.lifecycle.LiveData
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

    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {
        // validation
        if (inputName.value == null || inputName.value!!.isEmpty()) {
            statusMessage.value = Event("Please enter contact name")
        }
        else if (inputPhoneNo.value == null || inputPhoneNo.value!!.isEmpty()) {
            statusMessage.value = Event("Please enter contact phone no")
        } else if (!Patterns.PHONE.matcher(inputPhoneNo.value!!).matches()) {
            statusMessage.value = Event("Please enter a valid phone no")
        } else {
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
        val newRawId = repository.insert(contact)
        withContext(Dispatchers.Main) {
            if (newRawId > -1) {
                statusMessage.value = Event("Contact saved successfully, $newRawId")
            } else {
                statusMessage.value = Event("Error occurred")
            }
        }
    }

    private fun update(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        val noOfRows = repository.update(contact)
        withContext(Dispatchers.Main) {
            if (noOfRows > 0) {
                inputName.value = ""
                inputPhoneNo.value = ""
                isUpdateOrDelete = false
                saveOrUpdateButtonText.value = "Save"
                clearAllOrDeleteButtonText.value = "Clear All"
                statusMessage.value = Event("$noOfRows rows updated successfully")
            } else {
                statusMessage.value = Event("Error occurred")
            }

        }
    }

    private fun delete(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        val noOfRowsDeleted = repository.delete(contact)
        withContext(Dispatchers.Main) {
            if (noOfRowsDeleted > 0) {
                inputName.value = ""
                inputPhoneNo.value = ""
                isUpdateOrDelete = false
                saveOrUpdateButtonText.value = "Save"
                clearAllOrDeleteButtonText.value = "Clear All"
                statusMessage.value = Event("$noOfRowsDeleted rows deleted successfully")
            } else {
                statusMessage.value = Event("Error occurred")
            }
        }
    }

    private fun clearAll() = viewModelScope.launch(Dispatchers.IO) {
        val noOfRowsDeleted = repository.deleteAll()
        withContext(Dispatchers.Main) {
            if (noOfRowsDeleted > 0) {
                statusMessage.value = Event("$noOfRowsDeleted rows deleted successfully")
            } else {
                statusMessage.value = Event("Error occurred")
            }
        }
    }
}