package il.soulSalttrader.shabbattimes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_location")
data class CurrentLocationEntity(
    @PrimaryKey val rowId: Int = 0, // enforces single row
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val timeZoneId: String,
)