package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.VideoCampaignEntity
import com.example.data.local.UserProfileEntity
import com.example.ui.components.CampaignCard
import com.example.ui.theme.BlueVerified
import com.example.ui.theme.GoldStar
import com.example.ui.theme.RedPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorAdminScreen(
    userProfile: UserProfileEntity?,
    campaigns: List<VideoCampaignEntity>,
    onAddCampaign: (
        title: String,
        url: String,
        targetViews: Int,
        targetSubs: Int,
        rewardCoins: Int,
        duration: Int,
        category: String
    ) -> Unit,
    onDeleteCampaign: (Int) -> Unit,
    onUpdateChannelStats: (subscribersCount: Int, goalCount: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var videoTitle by remember { mutableStateOf("") }
    var videoUrl by remember { mutableStateOf("") }
    var targetViewsText by remember { mutableStateOf("1000") }
    var rewardCoinsText by remember { mutableStateOf("100") }
    var durationText by remember { mutableStateOf("60") }
    var selectedCategory by remember { mutableStateOf("Video View") }

    var currentSubsText by remember { mutableStateOf(userProfile?.channelSubscribersCount?.toString() ?: "85400") }
    var targetSubsText by remember { mutableStateOf(userProfile?.channelGoalSubscribers?.toString() ?: "100000") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("creator_admin_screen_lazy_column"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Admin Control Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = RedPrimary.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = RedPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Premi Sehjal Channel Management",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Self-Service Creator Dashboard (Gmail: ${userProfile?.userEmail})",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Total Campaigns", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${campaigns.size}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = RedPrimary)
                            }
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Total Delivered Views", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                val totalDelivered = campaigns.sumOf { it.currentViews }
                                Text("${"%,d".format(totalDelivered)}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BlueVerified)
                            }
                        }
                    }
                }
            }
        }

        // Form 1: Publish New Boost Campaign
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🚀 Launch New Video Boost Campaign",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = videoTitle,
                        onValueChange = { videoTitle = it },
                        label = { Text("Video Title") },
                        placeholder = { Text("e.g., Premi Sehjal - New Vlog Full HD") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = videoUrl,
                        onValueChange = { videoUrl = it },
                        label = { Text("YouTube Video or Channel Link") },
                        placeholder = { Text("https://www.youtube.com/watch?v=...") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Category Radio Chips
                    Text("Select Campaign Type:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Video View", "Shorts", "Subscribe").forEach { cat ->
                            FilterChip(
                                selected = selectedCategory == cat,
                                onClick = { selectedCategory = cat },
                                label = { Text(cat, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = RedPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = targetViewsText,
                            onValueChange = { targetViewsText = it },
                            label = { Text("Target Views") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = rewardCoinsText,
                            onValueChange = { rewardCoinsText = it },
                            label = { Text("Reward Coins") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val targetViews = targetViewsText.toIntOrNull() ?: 1000
                            val rewardCoins = rewardCoinsText.toIntOrNull() ?: 100
                            val duration = durationText.toIntOrNull() ?: 60
                            if (videoTitle.isNotBlank()) {
                                onAddCampaign(
                                    videoTitle,
                                    videoUrl,
                                    targetViews,
                                    0,
                                    rewardCoins,
                                    duration,
                                    selectedCategory
                                )
                                videoTitle = ""
                                videoUrl = ""
                            }
                        },
                        enabled = videoTitle.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Publish Campaign to All Mobile Viewers", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Form 2: Channel Subscribers & Goal Config
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "⚙️ Update Channel Live Subscribers Goal",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = currentSubsText,
                            onValueChange = { currentSubsText = it },
                            label = { Text("Current Subscribers") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = targetSubsText,
                            onValueChange = { targetSubsText = it },
                            label = { Text("Goal Target (e.g. 100K)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = {
                            val cur = currentSubsText.toIntOrNull() ?: 85400
                            val goal = targetSubsText.toIntOrNull() ?: 100000
                            onUpdateChannelStats(cur, goal)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Update Live Counter")
                    }
                }
            }
        }

        // Active Campaigns Management Header
        item {
            Text(
                text = "📋 Active Campaigns Management (${campaigns.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Campaigns List
        items(
            items = campaigns,
            key = { it.id }
        ) { campaign ->
            CampaignCard(
                campaign = campaign,
                isCompleted = false,
                onWatchClick = {},
                onDeleteClick = { onDeleteCampaign(campaign.id) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
    }
}
