package com.akinci.chatter.data.room.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun createUser(userEntity: UserEntity)

    @Query("SELECT * FROM db_table_user WHERE name= :name")
    suspend fun getUser(name: String): UserEntity?

    @Query("SELECT * FROM db_table_user WHERE id= :id")
    suspend fun getUser(id: Long): UserEntity?
}
