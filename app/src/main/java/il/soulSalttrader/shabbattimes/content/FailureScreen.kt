package il.soulSalttrader.shabbattimes.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.content.uiIcon.IconTextButton
import il.soulSalttrader.shabbattimes.content.uiIcon.UiIcon
import il.soulSalttrader.shabbattimes.content.uiIcon.UiIconImage

@Composable
fun FailureScreen(
    modifier: Modifier = Modifier,
    message: String,
    onRetry: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            UiIconImage(
                icon = UiIcon.Resource(R.drawable.sentiment_very_dissatisfied_outlined_192),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Oops! Something went wrong",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.tertiary,
            )

            Spacer(modifier = Modifier.height(48.dp))

            IconTextButton(
                modifier = Modifier,
                icon = UiIcon.Resource(R.drawable.replay_filled_24),
                onClick = onRetry,
            )
        }
    }
}

@Preview
@Composable
fun Preview(modifier: Modifier = Modifier) {
    FailureScreen(
        modifier = Modifier,
        message = "Unable to load data. Check your connection and retry.",
        onRetry = { /* no op */ }
    )
}