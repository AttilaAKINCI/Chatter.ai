package com.akinci.chatter.feature.acommon.data.local.dao

import androidx.room.*
import com.akinci.chatter.feature.acommon.data.local.entities.Message
import com.akinci.chatter.feature.acommon.data.local.entities.relations.MessageWithUser

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllMessages(messages : List<Message>)

    @Transaction
    @Query("SELECT * FROM messageTable WHERE dataOwnerId = :dataOwnerId")
    suspend fun getMessage(dataOwnerId: Long): List<MessageWithUser>

}