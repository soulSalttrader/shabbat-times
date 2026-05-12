package il.soulSalttrader.shabbattimes.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SavedLocationEntity::class, CurrentLocationEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedLocationDao(): SavedLocationDao
    abstract fun currentLocationDao(): CurrentLocationDao
}