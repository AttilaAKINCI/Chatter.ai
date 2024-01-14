package com.akinci.chatter.data.room.message

import androidx.lifecycle.LiveData
import androidx.room.*
import com.akinci.chatter.data.room.relations.MessageWithUser

@Dao
interface MessageDao {

    // TODO refactor

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllMessages(messages: List<MessageEntity>)

    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Transaction
    @Query("SELECT * FROM db_table_message WHERE dataOwnerId = :dataOwnerId ORDER BY timestamp ASC")
    fun getAllMessages(dataOwnerId: Long): LiveData<List<MessageWithUser>>
}