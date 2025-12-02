package il.soulSalttrader.retro.shabbatApp.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import il.soulSalttrader.retro.shabbatApp.model.SolarTimes

@Composable
fun ShabbatContent(
    modifier: Modifier = Modifier,
    result: SolarTimes,
) {
    Column(modifier = modifier.padding(24.dp)) {

        ShabbatCard(
            modifier = Modifier.padding(bottom = 12.dp).weight(1f),
            text = "Jerusalem",
        )

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ShabbatCard(
                modifier = Modifier.weight(1.5f),
                text = "${result.sunset}\nFri ${result.date}",
            )

            ShabbatCard(
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                text = "${result.sunset}\nSat ${result.date}",
            )
        }
    }
}