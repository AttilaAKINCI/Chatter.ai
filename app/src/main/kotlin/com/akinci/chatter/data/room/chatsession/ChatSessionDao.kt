package com.akinci.chatter.data.room.chatsession

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatSessionDao {

    @Insert
    suspend fun create(chatWindow: ChatSessionEntity)

    @Query("SELECT * FROM db_table_chat_sessions WHERE membersIds like '%,' || :memberId || ',%'")
    fun getSessions(memberId: Long): Flow<List<ChatSessionEntity>>
}
