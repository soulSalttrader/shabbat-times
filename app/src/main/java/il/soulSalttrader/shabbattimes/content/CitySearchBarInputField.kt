package il.soulSalttrader.shabbattimes.content

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.R
import kotlinx.coroutines.FlowPreview
@Composable
@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
private fun SearchBarInputField(
    state: TextFieldState,
    expanded: Boolean,
    onSearch: (String) -> Unit,
    onExpandedChange: (Boolean) -> Unit,

    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)?,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    shape: Shape = RoundedCornerShape(16.dp),
) {
    SearchBarDefaults.InputField(
        state = state,
        expanded = expanded,
        onSearch = { onSearch(state.text.toString()) },
        onExpandedChange = { onExpandedChange(!expanded) },
        modifier = modifier.fillMaxWidth(),
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        shape = shape,
    )
}

@Composable
private fun TrailingSearchIconButton(
    onClick: () -> Unit,
    resId: Int = R.drawable.cancel_24px,
    contentDescription: String? = "search_trailing",
) {
    IconButton(onClick = onClick) {
        UiIconImage(
            icon = UiIcon.Resource(resId = resId),
            contentDescription = contentDescription,
        )
    }
}

@Composable
private fun LeadingSearchIcon(
    resId: Int = R.drawable.search_24px,
    contentDescription: String? = "search_leading",
) {
    UiIconImage(
        icon = UiIcon.Resource(resId = resId),
        contentDescription = contentDescription,
    )
}