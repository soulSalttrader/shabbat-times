package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.di.InMemory
import il.soulSalttrader.shabbattimes.model.ShabbatEntry
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import jakarta.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class ReorderLocationsUseCase @Inject constructor(
    @param:InMemory private val savedLocationsRepository: SavedLocationsRepository,
) {
    suspend operator fun invoke(entries: ImmutableList<ShabbatEntry>, from: Int, to: Int) {
        val reordered = entries.toMutableList()
            .apply { add(to, removeAt(from)) }
            .toImmutableList()
        savedLocationsRepository.reorder(reordered.map { it.location })
    }
}