package com.akinci.chatter.data.room.chat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChatSessionDao {

    @Insert
    suspend fun create(chatWindow: ChatSessionEntity)

    @Query("SELECT * FROM db_table_chat_sessions WHERE membersIds like '%,' || :memberId || ',%'")
    suspend fun getSessions(memberId: Long): List<ChatSessionEntity>
}
