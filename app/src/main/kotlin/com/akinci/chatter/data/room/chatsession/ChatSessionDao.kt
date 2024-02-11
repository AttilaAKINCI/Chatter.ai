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
    @Query("SELECT * FROM db_table_chat_sessions WHERE primaryUserId =:sessionMemberId OR secondaryUserId =:sessionMemberId ORDER BY id DESC")
    fun getStream(sessionMemberId: Long): Flow<List<ChatSessionWithUser>>
}
