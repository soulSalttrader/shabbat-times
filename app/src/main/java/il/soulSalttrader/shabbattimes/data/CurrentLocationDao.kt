package il.soulSalttrader.shabbattimes.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentLocationDao {
    @Query("SELECT * FROM current_location WHERE rowId = 0")
    fun observe(): Flow<CurrentLocationEntity?>

    @Upsert
    suspend fun upsert(entity: CurrentLocationEntity)

    @Query("DELETE FROM current_location WHERE rowId = 0")
    suspend fun clear()
}