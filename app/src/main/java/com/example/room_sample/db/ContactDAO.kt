package com.example.room_sample.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContactDAO {

    // return row id
    @Insert
    suspend fun insertContact(contact: Contact) : Long

    // return no of rows updated
    @Update
    suspend fun updateContact(contact: Contact): Int

    // return no of rows deleted
    @Delete
    suspend fun deleteContact(contact: Contact) : Int

    @Query("DELETE FROM contact_data_table")
    suspend fun deleteAll() : Int

    // since this method return LiveData, and Room library will automatically call this fun in
    // a background thread, we don't have to manually make it suspend fun and call it in a coroutine
    @Query("SELECT * FROM contact_data_table")
    fun getAllContacts(): LiveData<List<Contact>>

}