package com.example.room_sample.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContactDAO {

    @Insert
    suspend fun insertContact(contact: Contact)

    @Update
    suspend fun updateContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("DELETE FROM contact_data_table")
    suspend fun deleteAll()

    // since this method return LiveData, and Room library will automatically call this fun in
    // a background thread, we don't have to manually make it suspend fun and call it in a coroutine
    @Query("SELECT * FROM contact_data_table")
    fun getAllContacts(): LiveData<List<Contact>>

}