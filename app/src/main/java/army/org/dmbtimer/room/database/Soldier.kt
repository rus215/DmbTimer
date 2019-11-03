package army.org.dmbtimer.room.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "soldiers")
class Soldier(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, val name: String, @ColumnInfo(name = "start_date") val startDate: Long,
    val userImage: String?
)