package com.akinci.chatter.common.room

class RoomConfig {
    companion object {
        /** DB Related **/
        const val DB_NAME = "chatterDB"

        /** Tables **/
        const val MESSAGE_TABLE_NAME = "messageTable"
        const val USER_TABLE_NAME = "userTable"

        // Indices
        const val ID = "id"

    }
}