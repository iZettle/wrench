package com.izettle.wrench.dialogs.scope

import android.database.sqlite.SQLiteException
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.izettle.wrench.database.WrenchScope
import com.izettle.wrench.database.WrenchScopeDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ScopeFragmentViewModel @Inject
constructor(private val scopeDao: WrenchScopeDao) : ViewModel() {
    private var applicationId: Long = 0

    internal val selectedScopeLiveData: LiveData<WrenchScope> by lazy {
        scopeDao.getSelectedScopeLiveData(applicationId)
    }

    internal var selectedScope: WrenchScope? = null

    internal val scopes: LiveData<List<WrenchScope>>
        get() = scopeDao.getScopes(applicationId)

    fun init(applicationId: Long) {
        this.applicationId = applicationId
    }

    internal fun selectScope(wrenchScope: WrenchScope) {
        GlobalScope.launch {
            wrenchScope.timeStamp = Date()
            scopeDao.update(wrenchScope)
        }
    }

    @WorkerThread
    @Throws(SQLiteException::class)
    internal fun createScope(scopeName: String) {
        GlobalScope.launch {
            val wrenchScope = WrenchScope()
            wrenchScope.name = scopeName
            wrenchScope.applicationId = applicationId
            wrenchScope.id = scopeDao.insert(wrenchScope)
        }
    }

    @WorkerThread
    internal fun removeScope(scope: WrenchScope) {
        GlobalScope.launch {
            scopeDao.delete(scope)
        }
    }
}
