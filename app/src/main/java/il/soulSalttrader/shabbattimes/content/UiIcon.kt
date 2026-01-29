package il.soulSalttrader.shabbattimes.content

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface UiIcon {
    data class Vector(val image: ImageVector) : UiIcon
    data class Resource(@param:DrawableRes val resId: Int) : UiIcon
}