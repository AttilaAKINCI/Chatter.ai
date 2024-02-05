package com.akinci.chatter.data.room.message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert
    suspend fun save(message: MessageEntity)

    @Transaction
    @Query("SELECT * FROM db_table_message WHERE chatSessionId = :chatSessionId ORDER BY id DESC")
    fun get(chatSessionId: Long): Flow<List<MessageWithUser>>
}
