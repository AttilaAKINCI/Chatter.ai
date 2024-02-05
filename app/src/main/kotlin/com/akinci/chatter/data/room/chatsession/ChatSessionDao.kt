package com.akinci.chatter.data.room.chatsession

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatSessionDao {

    @Insert
    suspend fun create(chatWindow: ChatSessionEntity)

    @Transaction
    @Query("SELECT * FROM db_table_chat_sessions WHERE primaryUserId = :primaryUserId ORDER BY id ASC")
    fun get(primaryUserId: Long): Flow<List<ChatSessionWithUser>>
}
