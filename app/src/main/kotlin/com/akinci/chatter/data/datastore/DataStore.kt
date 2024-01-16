package com.akinci.chatter.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.akinci.chatter.core.coroutine.ContextProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "dataStorage",
)

class DataStorage @Inject constructor(
    private val contextProvider: ContextProvider,
    @ApplicationContext private val context: Context,
) {
    suspend fun getLoggedInUsersName() = withContext(contextProvider.io) {
        context.dataStore.data
            .map { it[DataStorageKeys.LOGGED_IN_USER] }
            .firstOrNull()
    }

    suspend fun setLoggedInUsersName(name: String) {
        withContext(contextProvider.io) {
            context.dataStore.edit {
                it[DataStorageKeys.LOGGED_IN_USER] = name
            }
        }
    }
}
