package army.org.dmbtimer.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Soldier::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun soldierDao(): SoldierDao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "Dmb_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }
}