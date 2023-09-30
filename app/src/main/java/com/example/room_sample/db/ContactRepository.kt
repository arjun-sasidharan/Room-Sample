package com.example.room_sample.db

class ContactRepository(private val dao: ContactDAO) {

    val contacts = dao.getAllContacts()

    suspend fun insert(contact: Contact) : Long{
       return dao.insertContact(contact)
    }

    suspend fun update(contact: Contact) : Int {
        return dao.updateContact(contact)
    }

    suspend fun delete(contact: Contact): Int {
        return dao.deleteContact(contact)
    }

    suspend fun deleteAll() : Int{
        return dao.deleteAll()
    }
}