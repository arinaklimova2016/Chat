package com.example.chat.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM Message WHERE `from` = :id OR `to` = :id")
    fun getById(id: String): Flow<List<Message>>

    @Insert
    fun addMessage(message: Message)

    @Query("DELETE FROM Message")
    fun deleteAllChats()
}