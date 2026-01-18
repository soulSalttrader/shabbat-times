package il.soulSalttrader.retro.shabbatApp.model

import il.soulSalttrader.retro.core.model.State

sealed interface ShabbatDataState : State {
    data object Idle : ShabbatDataState
    data object Loading : ShabbatDataState
    data class Success(val data: HalachicTimesDisplay?) : ShabbatDataState
    data class Failure(val message: String, val cause: Throwable? = null) : ShabbatDataState
}