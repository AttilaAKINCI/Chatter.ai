package com.akinci.chatter.data.room.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun createUser(userEntity: UserEntity)

    @Query("SELECT * FROM db_table_user WHERE name= :name")
    suspend fun getUser(name: String): UserEntity?

    // TODO refactor
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllUsers(users: List<UserEntity>)


}
