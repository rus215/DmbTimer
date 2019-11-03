package army.org.dmbtimer.room.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData


class MyRepository(private val soldierDao: SoldierDao) {
    val allSoldiers: LiveData<List<Soldier>> = soldierDao.getAllSoldiers()

    @WorkerThread
    suspend fun insertSoldier(soldier: Soldier) = soldierDao.insertSoldier(soldier)

}