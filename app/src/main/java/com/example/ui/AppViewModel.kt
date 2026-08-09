package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.*
import com.example.data.repository.AppRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class WatchUiState {
    object Idle : WatchUiState()
    data class Watching(
        val campaign: VideoCampaignEntity,
        val secondsRemaining: Int,
        val totalSeconds: Int,
        val isCompleted: Boolean
    ) : WatchUiState()
}

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository
    private var timerJob: Job? = null

    val campaigns: StateFlow<List<VideoCampaignEntity>>
    val posts: StateFlow<List<CommunityPostEntity>>
    val userProfile: StateFlow<UserProfileEntity?>
    val completedCampaignIds: StateFlow<List<Int>>

    private val _watchState = MutableStateFlow<WatchUiState>(WatchUiState.Idle)
    val watchState: StateFlow<WatchUiState> = _watchState.asStateFlow()

    private val _snackMessage = MutableStateFlow<String?>(null)
    val snackMessage: StateFlow<String?> = _snackMessage.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = AppRepository(database.appDao())

        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }

        campaigns = repository.campaigns.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        posts = repository.posts.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        userProfile = repository.userProfile.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

        completedCampaignIds = repository.completedCampaignIds.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

    fun clearSnackMessage() {
        _snackMessage.value = null
    }

    fun toggleRoleMode(isCreatorAdmin: Boolean) {
        viewModelScope.launch {
            val current = userProfile.value ?: return@launch
            val newRole = if (isCreatorAdmin) "Premi Sehjal (Creator)" else "Normal Viewer"
            repository.updateProfile(
                current.copy(
                    isCreatorAdmin = isCreatorAdmin,
                    displayName = newRole,
                    userEmail = if (isCreatorAdmin) "kishanaifact@gmail.com" else "viewer@gmail.com"
                )
            )
            val modeName = if (isCreatorAdmin) "Creator / Admin Mode Active" else "Viewer Mode Active"
            _snackMessage.value = "Switched to $modeName"
        }
    }

    fun startWatchingCampaign(campaign: VideoCampaignEntity) {
        timerJob?.cancel()
        val duration = campaign.durationSeconds.coerceAtLeast(5)
        _watchState.value = WatchUiState.Watching(
            campaign = campaign,
            secondsRemaining = duration,
            totalSeconds = duration,
            isCompleted = false
        )

        timerJob = viewModelScope.launch {
            var remaining = duration
            while (remaining > 0) {
                delay(1000)
                remaining--
                val currentState = _watchState.value
                if (currentState is WatchUiState.Watching) {
                    _watchState.value = currentState.copy(secondsRemaining = remaining)
                }
            }

            // Task Completed!
            _watchState.value = WatchUiState.Watching(
                campaign = campaign,
                secondsRemaining = 0,
                totalSeconds = duration,
                isCompleted = true
            )

            // Claim rewards automatically
            repository.completeVideoTask(campaign.id, campaign.rewardCoins)
            _snackMessage.value = "🎉 View counted! You earned +${campaign.rewardCoins} Coins!"
        }
    }

    fun closeWatchPlayer() {
        timerJob?.cancel()
        _watchState.value = WatchUiState.Idle
    }

    fun toggleSubscribe() {
        viewModelScope.launch {
            val profile = userProfile.value ?: return@launch
            val subbed = !profile.isSubscribedToChannel
            repository.toggleSubscribeChannel(profile)
            if (subbed) {
                _snackMessage.value = "❤️ Subscribed to Premi Sehjal Official! +250 Coins earned!"
            } else {
                _snackMessage.value = "Unsubscribed from Premi Sehjal Official"
            }
        }
    }

    fun addCampaign(
        title: String,
        url: String,
        targetViews: Int,
        targetSubs: Int,
        rewardCoins: Int,
        duration: Int,
        category: String
    ) {
        viewModelScope.launch {
            val newCampaign = VideoCampaignEntity(
                videoTitle = title,
                videoUrl = url,
                targetViews = targetViews,
                targetSubs = targetSubs,
                rewardCoins = rewardCoins,
                durationSeconds = duration,
                category = category,
                isFeatured = true
            )
            repository.addCampaign(newCampaign)
            _snackMessage.value = "🚀 New Campaign Published by Creator!"
        }
    }

    fun deleteCampaign(campaignId: Int) {
        viewModelScope.launch {
            repository.deleteCampaign(campaignId)
            _snackMessage.value = "Campaign removed"
        }
    }

    fun addCommunityPost(content: String) {
        viewModelScope.launch {
            val newPost = CommunityPostEntity(
                authorName = "Premi Sehjal Official",
                isVerified = true,
                contentText = content,
                likesCount = 0,
                commentsCount = 0
            )
            repository.addCommunityPost(newPost)
            _snackMessage.value = "📢 Update Posted to Community!"
        }
    }

    fun toggleLikePost(post: CommunityPostEntity) {
        viewModelScope.launch {
            val newLiked = !post.isLikedByUser
            val newLikesCount = if (newLiked) post.likesCount + 1 else post.likesCount - 1
            repository.updatePost(
                post.copy(
                    isLikedByUser = newLiked,
                    likesCount = newLikesCount
                )
            )
        }
    }

    fun updateChannelStats(subscribersCount: Int, goalCount: Int) {
        viewModelScope.launch {
            val profile = userProfile.value ?: return@launch
            repository.updateProfile(
                profile.copy(
                    channelSubscribersCount = subscribersCount,
                    channelGoalSubscribers = goalCount
                )
            )
            _snackMessage.value = "Channel Stats Updated!"
        }
    }

    fun processUpiPayment(
        packageTitle: String,
        amountRs: Int,
        coinCredit: Int,
        subscriberCredit: Int,
        utrReference: String
    ) {
        viewModelScope.launch {
            val current = userProfile.value ?: return@launch
            repository.updateProfile(
                current.copy(
                    coins = current.coins + coinCredit,
                    channelSubscribersCount = current.channelSubscribersCount + subscriberCredit
                )
            )
            _snackMessage.value = "✅ UPI Payment Verified! +$subscriberCredit Subscribers & +$coinCredit Coins Added!"
        }
    }
}
