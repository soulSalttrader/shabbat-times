package il.soulSalttrader.shabbattimes.permission

import android.Manifest
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import il.soulSalttrader.shabbattimes.MainActivity
import il.soulSalttrader.shabbattimes.TestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PermissionGrantedFlowTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val permissionRule: GrantPermissionRule? = GrantPermissionRule.grant(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    @get:Rule(order = 2)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun `app launches without crash`() {
        composeRule.onRoot().assertExists()
    }

    @Test
    fun `PERM_RESTART_S1 - GPS card visible when permission already granted`() {
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule
                .onAllNodesWithTag(TestTags.GPS_CARD)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
        composeRule
            .onNodeWithTag(TestTags.GPS_CARD)
            .assertExists()
    }
}