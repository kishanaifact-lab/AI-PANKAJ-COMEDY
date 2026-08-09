package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppViewModel
import com.example.ui.WatchUiState
import com.example.ui.components.VideoPlayerSheet
import com.example.ui.screens.*
import com.example.ui.theme.BlueVerified
import com.example.ui.theme.GoldStar
import com.example.ui.theme.PremiSehjalTheme
import com.example.ui.theme.RedPrimary

enum class AppTab(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    HOME("Home", Icons.Default.Home),
    WATCH("Watch & Boost", Icons.Default.PlayCircle),
    BUY_SUBS("Buy Subs (UPI)", Icons.Default.ShoppingCart),
    COMMUNITY("Community", Icons.Default.RssFeed),
    LEADERBOARD("Leaderboard", Icons.Default.EmojiEvents),
    ADMIN("Control Center", Icons.Default.AdminPanelSettings)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PremiSehjalTheme {
                PremiSehjalMainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiSehjalMainApp(
    viewModel: AppViewModel = viewModel()
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val campaigns by viewModel.campaigns.collectAsStateWithLifecycle()
    val posts by viewModel.posts.collectAsStateWithLifecycle()
    val completedCampaignIds by viewModel.completedCampaignIds.collectAsStateWithLifecycle()
    val watchState by viewModel.watchState.collectAsStateWithLifecycle()
    val snackMessage by viewModel.snackMessage.collectAsStateWithLifecycle()

    var currentTab by remember { mutableStateOf(AppTab.HOME) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackMessage) {
        snackMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_premi_sehjal_hero_1786299977012),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Premi Sehjal Official",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = BlueVerified,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Text(
                                text = if (userProfile?.isCreatorAdmin == true) "Creator Admin Mode" else "Subscriber & Views Booster App",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    // Coins Badge
                    Surface(
                        color = GoldStar.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .testTag("topbar_coins_badge")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = "Coins",
                                tint = GoldStar,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${userProfile?.coins ?: 0}",
                                fontWeight = FontWeight.Bold,
                                color = GoldStar,
                                fontSize = 13.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("main_navigation_bar")
            ) {
                val availableTabs = mutableListOf(
                    AppTab.HOME,
                    AppTab.WATCH,
                    AppTab.BUY_SUBS,
                    AppTab.COMMUNITY,
                    AppTab.LEADERBOARD
                )

                if (userProfile?.isCreatorAdmin == true) {
                    availableTabs.add(AppTab.ADMIN)
                }

                availableTabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentTab == tab,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 10.sp,
                                fontWeight = if (currentTab == tab) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = RedPrimary,
                            selectedTextColor = RedPrimary,
                            indicatorColor = RedPrimary.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.HOME -> {
                    HomeScreen(
                        userProfile = userProfile,
                        campaigns = campaigns,
                        completedCampaignIds = completedCampaignIds,
                        onSubscribeToggle = { viewModel.toggleSubscribe() },
                        onRoleToggle = { viewModel.toggleRoleMode(it) },
                        onWatchCampaign = { viewModel.startWatchingCampaign(it) },
                        onDeleteCampaign = { viewModel.deleteCampaign(it) },
                        onNavigateToAdmin = { currentTab = AppTab.ADMIN }
                    )
                }
                AppTab.WATCH -> {
                    WatchScreen(
                        userProfile = userProfile,
                        campaigns = campaigns,
                        completedCampaignIds = completedCampaignIds,
                        onWatchCampaign = { viewModel.startWatchingCampaign(it) }
                    )
                }
                AppTab.BUY_SUBS -> {
                    BuySubscribersScreen(
                        userProfile = userProfile,
                        onProcessUpiPayment = { title, amount, coins, subs, ref ->
                            viewModel.processUpiPayment(title, amount, coins, subs, ref)
                        }
                    )
                }
                AppTab.COMMUNITY -> {
                    CommunityScreen(
                        userProfile = userProfile,
                        posts = posts,
                        onLikeToggle = { viewModel.toggleLikePost(it) },
                        onAddPost = { viewModel.addCommunityPost(it) }
                    )
                }
                AppTab.LEADERBOARD -> {
                    LeaderboardScreen()
                }
                AppTab.ADMIN -> {
                    CreatorAdminScreen(
                        userProfile = userProfile,
                        campaigns = campaigns,
                        onAddCampaign = { title, url, targetViews, targetSubs, rewardCoins, duration, category ->
                            viewModel.addCampaign(title, url, targetViews, targetSubs, rewardCoins, duration, category)
                        },
                        onDeleteCampaign = { viewModel.deleteCampaign(it) },
                        onUpdateChannelStats = { subCount, goalCount ->
                            viewModel.updateChannelStats(subCount, goalCount)
                        }
                    )
                }
            }

            // Video Player Sheet Overlay
            val currentWatchState = watchState
            if (currentWatchState is WatchUiState.Watching) {
                VideoPlayerSheet(
                    campaign = currentWatchState.campaign,
                    secondsRemaining = currentWatchState.secondsRemaining,
                    totalSeconds = currentWatchState.totalSeconds,
                    isCompleted = currentWatchState.isCompleted,
                    onClose = { viewModel.closeWatchPlayer() }
                )
            }
        }
    }
}
