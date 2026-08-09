package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.GoldStar
import com.example.ui.theme.RedPrimary

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val coins: Int,
    val videosWatched: Int,
    val badgeTitle: String
)

@Composable
fun LeaderboardScreen(
    modifier: Modifier = Modifier
) {
    val topUsers = listOf(
        LeaderboardUser(1, "Rahul Verma", 4500, 48, "🔥 Top Fan Champion"),
        LeaderboardUser(2, "Ananya Sharma", 3800, 39, "⚡ Super Supporter"),
        LeaderboardUser(3, "Kishan Kumar", 3200, 31, "🌟 Loyal Viewer"),
        LeaderboardUser(4, "Amit Patel", 2700, 26, "🎬 Active Booster"),
        LeaderboardUser(5, "Priya Singh", 2400, 22, "❤️ Channel Fan"),
        LeaderboardUser(6, "Vikas Gupta", 2100, 19, "⭐ Member"),
        LeaderboardUser(7, "Suresh Yadav", 1850, 16, "⭐ Member")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("leaderboard_screen_lazy_column"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Top Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = GoldStar,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Premi Sehjal Top Fans Leaderboard",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Rankings based on views boosted & coins earned",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Leaderboard List
        itemsIndexed(topUsers) { index, user ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (user.rank == 1) GoldStar.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rank Badge
                    Surface(
                        color = when (user.rank) {
                            1 -> GoldStar
                            2 -> Color(0xFFC0C0C0)
                            3 -> Color(0xFFCD7F32)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = CircleShape,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "#${user.rank}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (user.rank <= 3) Color.Black else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = user.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${user.badgeTitle} • ${user.videosWatched} videos watched",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = GoldStar,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "%,d".format(user.coins),
                            fontWeight = FontWeight.Bold,
                            color = GoldStar,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
