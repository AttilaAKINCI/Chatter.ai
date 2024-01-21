package com.akinci.chatter.data.repository

import com.akinci.chatter.data.room.AppDatabase
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val database: AppDatabase,
) {
    private val messageDao by lazy { database.getMessageDao() }

    fun getMessages(chatWindowId: Long) = messageDao.getHistory(chatWindowId)

}
