package com.akinci.chatter.data.room.message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Query("SELECT * FROM db_table_message WHERE chatSessionId = :chatSessionId ORDER BY id DESC")
    fun getHistory(chatSessionId: Long): Flow<List<MessageEntity>>
}
