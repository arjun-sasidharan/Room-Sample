package com.example.room_sample.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Contact::class], version = 1)
abstract class ContactDatabase : RoomDatabase(){

    abstract val contactDAO : ContactDAO

    companion object {
        // @Volatile annotation will mark the JVM backing field of the annotated property as volatile.
        // Thus, the writes to this field are immediately made visible to other threads.
        @Volatile
        private var INSTANCE : ContactDatabase? = null
            fun getInstance(context: Context) : ContactDatabase {
                synchronized(this) {
                    var instance = INSTANCE
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            ContactDatabase::class.java,
                            "contact_data_database"
                        ).build()
                        INSTANCE = instance
                    }
                    return instance
                }
            }
    }
}