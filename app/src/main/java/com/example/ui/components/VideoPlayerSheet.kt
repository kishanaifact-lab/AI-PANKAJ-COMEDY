package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.VideoCampaignEntity
import com.example.ui.theme.BlueVerified
import com.example.ui.theme.GoldStar
import com.example.ui.theme.RedPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerSheet(
    campaign: VideoCampaignEntity,
    secondsRemaining: Int,
    totalSeconds: Int,
    isCompleted: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timerProgress = (1f - (secondsRemaining.toFloat() / totalSeconds.coerceAtLeast(1).toFloat()))
        .coerceIn(0f, 1f)

    ModalBottomSheet(
        onDismissRequest = onClose,
        modifier = modifier.testTag("video_player_bottom_sheet"),
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = null,
                        tint = RedPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Premi Sehjal Video Boost",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Player",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Video Title
            Text(
                text = campaign.videoTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Video Player Simulation Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0F172A),
                                Color(0xFF1E1B4B)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                            Surface(
                                color = GoldStar,
                                shape = CircleShape,
                                modifier = Modifier.size(64.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Complete",
                                    tint = Color.Black,
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "🎉 View Counted!",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldStar
                            )
                            Text(
                                text = "+${campaign.rewardCoins} Coins Added to Wallet",
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Countdown Ring / Timer
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                progress = { timerProgress },
                                modifier = Modifier.size(80.dp),
                                color = RedPrimary,
                                strokeWidth = 6.dp,
                                trackColor = RedPrimary.copy(alpha = 0.2f)
                            )
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${secondsRemaining}s",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "watching",
                                    fontSize = 10.sp,
                                    color = Color.LightGray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = BlueVerified,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Playing Official Premi Sehjal Stream",
                                fontSize = 12.sp,
                                color = Color.LightGray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Button
            if (isCompleted) {
                Button(
                    onClick = onClose,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldStar, contentColor = Color.Black),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "Claim Reward & Continue (+${campaign.rewardCoins} 🪙)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            } else {
                OutlinedButton(
                    onClick = onClose,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Cancel Watching")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
