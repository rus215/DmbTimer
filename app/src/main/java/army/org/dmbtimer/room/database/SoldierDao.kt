package army.org.dmbtimer.room.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SoldierDao {

    @Insert
    suspend fun insertSoldier(soldier: Soldier)

    @Query("SELECT * FROM soldiers")
    fun getAllSoldiers(): LiveData<List<Soldier>>

    @Query("DELETE FROM soldiers")
    fun deleteAll()

}