package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.brnoEntry
import il.soulSalttrader.shabbattimes.brnoLocation
import il.soulSalttrader.shabbattimes.gpsEntry
import il.soulSalttrader.shabbattimes.gpsLocation
import il.soulSalttrader.shabbattimes.jerusalemEntry
import il.soulSalttrader.shabbattimes.jerusalemLocation
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import il.soulSalttrader.shabbattimes.telAvivEntry
import il.soulSalttrader.shabbattimes.telAvivLocation
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.test.runTest

class ReorderLocationsUseCaseTest : DescribeSpec({
    lateinit var savedLocationsRepository: SavedLocationsRepository
    lateinit var useCase: ReorderLocationsUseCase

    beforeTest {
        savedLocationsRepository = mockk(relaxed = true)
        useCase = ReorderLocationsUseCase(savedLocationsRepository)
    }

    describe("CARD_REORDER - SCENARIO: Reorder cards via drag handle") {
        it("USECASE_REORDER_S1 - should move first item to the end") {
            runTest {
                useCase(listOf(jerusalemEntry, telAvivEntry, brnoEntry).toImmutableList(), from = 0, to = 2)
                coVerify {
                    savedLocationsRepository.reorder(
                        listOf(
                            telAvivLocation(),
                            brnoLocation(),
                            jerusalemLocation(),
                        ),
                    )
                }
            }
        }

        it("USECASE_REORDER_S2 - should move item from last to first") {
            runTest {
                useCase(listOf(jerusalemEntry, telAvivEntry, brnoEntry).toImmutableList(), from = 2, to = 0)
                coVerify {
                    savedLocationsRepository.reorder(
                        listOf(
                            brnoLocation(),
                            jerusalemLocation(),
                            telAvivLocation(),
                        ),
                    )
                }
            }
        }

        it("USECASE_REORDER_S3 - should move item in middle of list") {
            runTest {
                useCase(listOf(jerusalemEntry, telAvivEntry, brnoEntry).toImmutableList(), from = 2, to = 0)
                coVerify {
                    savedLocationsRepository.reorder(
                        listOf(
                            brnoLocation(),
                            jerusalemLocation(),
                            telAvivLocation(),
                        ),
                    )
                }
            }
        }

        it("USECASE_REORDER_S4 - should move item to same position") {
            runTest {
                useCase(listOf(jerusalemEntry, telAvivEntry, brnoEntry).toImmutableList(), from = 0, to = 0)
                coVerify {
                    savedLocationsRepository.reorder(
                        listOf(
                            jerusalemLocation(),
                            telAvivLocation(),
                            brnoLocation(),
                        ),
                    )
                }
            }
        }

        it("USECASE_REORDER_S5 - should reorder GPS card") {
            runTest {
                useCase(listOf(gpsEntry, telAvivEntry, brnoEntry).toImmutableList(), from = 0, to = 2)
                coVerify {
                    savedLocationsRepository.reorder(
                        listOf(
                            telAvivLocation(),
                            brnoLocation(),
                            gpsLocation(),
                        ),
                    )
                }
            }
        }
    }
})
