package com.akinci.chatter.data.room.message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Query("SELECT distinct(receiver) FROM db_table_message WHERE sender = :sender")
    suspend fun getReceiverIds(sender: Long): List<Long>

    @Query("SELECT * FROM db_table_message WHERE (sender = :sender AND receiver = :receiver) OR (sender = :receiver AND receiver = :sender) ORDER BY id ASC")
    fun getChatHistory(sender: Long, receiver: Long): Flow<MessageEntity>
}
