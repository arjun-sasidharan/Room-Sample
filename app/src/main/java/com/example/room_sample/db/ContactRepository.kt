package com.example.room_sample.db

class ContactRepository(private val dao: ContactDAO) {

    val contacts = dao.getAllContacts()

    suspend fun insert(contact: Contact) {
        dao.insertContact(contact)
    }

    suspend fun update(contact: Contact) {
        dao.updateContact(contact)
    }

    suspend fun delete(contact: Contact) {
        dao.deleteContact(contact)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}