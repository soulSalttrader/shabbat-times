package il.soulSalttrader.shabbattimes.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedLocationDao {
    @Query("SELECT * FROM saved_locations ORDER BY sortOrder ASC")
    fun observeAll(): Flow<List<SavedLocationEntity>>

    @Upsert
    suspend fun upsertAll(entities: List<SavedLocationEntity>)

    @Upsert
    suspend fun upsert(entity: SavedLocationEntity)

    @Query("DELETE FROM saved_locations WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT MAX(sortOrder) FROM saved_locations")
    suspend fun getMaxSortOrder(): Int?

    @Transaction
    suspend fun insertWithOrder(entity: SavedLocationEntity) {
        val currentMax = getMaxSortOrder() ?: -1
        upsert(entity.copy(sortOrder = currentMax + 1))
    }
}