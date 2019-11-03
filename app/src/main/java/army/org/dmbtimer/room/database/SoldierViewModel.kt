package army.org.dmbtimer.room.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SoldierViewModel(application: Application):AndroidViewModel(application) {
    private val repository: MyRepository

    val allSoldiers: LiveData<List<Soldier>>

    init {
        val soldierDao: SoldierDao = MyDatabase.getDatabase(application).soldierDao()
        repository = MyRepository(soldierDao)

        allSoldiers = repository.allSoldiers
    }

    fun insert(soldier: Soldier) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertSoldier(soldier)
    }
}