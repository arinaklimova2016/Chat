package com.example.chat.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Entity
data class Message(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val from: String,
    val to: String,
    val message: String
)

@Dao
interface MessageDao {
    @Query("SELECT * FROM Message WHERE `from` = :id OR `to` = :id")
    fun getById(id: String): Flow<List<Message>>

    @Insert
    fun addMessage(message: Message)

    @Query("DELETE FROM Message")
    fun deleteAllChats()
}