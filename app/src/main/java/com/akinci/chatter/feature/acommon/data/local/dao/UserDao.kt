package com.akinci.chatter.feature.acommon.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akinci.chatter.feature.acommon.data.local.entities.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(userEntity : UserEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllUsers(users : List<UserEntity>)

    @Query("SELECT * FROM userTable WHERE nickname= :userName")
    suspend fun getUser(userName: String): UserEntity?

    @Query("SELECT * FROM userTable WHERE nickname= :userName AND loggedIdUser = :loggedInUser")
    suspend fun getLoggedInUser(userName: String, loggedInUser: Boolean = true): UserEntity?

}