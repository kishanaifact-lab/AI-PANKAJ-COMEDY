package com.example.data.repository

import com.example.data.local.*
import kotlinx.coroutines.flow.Flow

class AppRepository(private val dao: AppDao) {

    val campaigns: Flow<List<VideoCampaignEntity>> = dao.getAllCampaigns()
    val posts: Flow<List<CommunityPostEntity>> = dao.getAllPosts()
    val userProfile: Flow<UserProfileEntity?> = dao.getUserProfile()
    val completedCampaignIds: Flow<List<Int>> = dao.getCompletedCampaignIds()

    suspend fun seedInitialDataIfNeeded() {
        // Ensure User Profile exists
        val currentProfile = dao.getUserProfile()
        if (currentProfile == null) {
            val defaultProfile = UserProfileEntity(
                id = 1,
                userEmail = "kishanaifact@gmail.com",
                displayName = "Premi Sehjal (Creator)",
                isCreatorAdmin = true,
                coins = 500,
                isSubscribedToChannel = false,
                totalVideosWatched = 0,
                channelSubscribersCount = 85400,
                channelGoalSubscribers = 100000,
                totalViewsCount = 1250000
            )
            dao.insertOrUpdateProfile(defaultProfile)
        }

        // Seed Campaigns if empty
        // We will seed default initial campaigns
        val initialCampaigns = listOf(
            VideoCampaignEntity(
                videoTitle = "🔥 Official Song Release - Premi Sehjal | Full HD Video",
                videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                targetViews = 5000,
                currentViews = 3200,
                targetSubs = 1000,
                currentSubs = 650,
                rewardCoins = 120,
                durationSeconds = 60,
                category = "Video View",
                isFeatured = true
            ),
            VideoCampaignEntity(
                videoTitle = "⚡ Subscribe Premi Sehjal Official Channel & Claim Reward",
                videoUrl = "https://www.youtube.com/@PremiSehjalOfficial",
                targetViews = 10000,
                currentViews = 8400,
                targetSubs = 2000,
                currentSubs = 1850,
                rewardCoins = 250,
                durationSeconds = 10,
                category = "Subscribe",
                isFeatured = true
            ),
            VideoCampaignEntity(
                videoTitle = "🎬 Behind The Scenes Vlog #12 - Shooting New Music Video",
                videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                targetViews = 2500,
                currentViews = 1100,
                targetSubs = 500,
                currentSubs = 320,
                rewardCoins = 90,
                durationSeconds = 45,
                category = "Shorts",
                isFeatured = false
            ),
            VideoCampaignEntity(
                videoTitle = "🎵 Acoustic Unplugged Session | Premi Sehjal Live",
                videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                targetViews = 1500,
                currentViews = 980,
                targetSubs = 300,
                currentSubs = 210,
                rewardCoins = 80,
                durationSeconds = 30,
                category = "Video View",
                isFeatured = false
            )
        )

        initialCampaigns.forEach { campaign ->
            dao.insertCampaign(campaign)
        }

        // Seed Initial Community Posts
        val initialPosts = listOf(
            CommunityPostEntity(
                authorName = "Premi Sehjal Official",
                isVerified = true,
                contentText = "Namaste dosto! 🙏 Welcome to my official channel hub app! Press SUBSCRIBE below to support our target of 100,000 subscribers! Special surprises coming soon 🔥❤️",
                likesCount = 1420,
                commentsCount = 230,
                isLikedByUser = false
            ),
            CommunityPostEntity(
                authorName = "Premi Sehjal Official",
                isVerified = true,
                contentText = "New Music Video teaser is OUT NOW! Watch the video in the Watch section below to boost views and earn free coins 🚀",
                likesCount = 980,
                commentsCount = 115,
                isLikedByUser = true
            )
        )

        initialPosts.forEach { post ->
            dao.insertPost(post)
        }
    }

    suspend fun addCampaign(campaign: VideoCampaignEntity) {
        dao.insertCampaign(campaign)
    }

    suspend fun updateCampaign(campaign: VideoCampaignEntity) {
        dao.updateCampaign(campaign)
    }

    suspend fun deleteCampaign(campaignId: Int) {
        dao.deleteCampaignById(campaignId)
    }

    suspend fun addCommunityPost(post: CommunityPostEntity) {
        dao.insertPost(post)
    }

    suspend fun updatePost(post: CommunityPostEntity) {
        dao.updatePost(post)
    }

    suspend fun updateProfile(profile: UserProfileEntity) {
        dao.insertOrUpdateProfile(profile)
    }

    suspend fun completeVideoTask(campaignId: Int, rewardCoins: Int) {
        // Record task
        dao.insertTaskHistory(TaskHistoryEntity(campaignId = campaignId, coinsEarned = rewardCoins))

        // Update campaign views
        val campaign = dao.getCampaignById(campaignId)
        if (campaign != null) {
            dao.updateCampaign(
                campaign.copy(currentViews = campaign.currentViews + 1)
            )
        }

        // Update user profile coins and total watched
        // Note: we fetch current profile via query or default
        val currentProfile = UserProfileEntity(
            id = 1,
            userEmail = "kishanaifact@gmail.com",
            displayName = "Premi Sehjal (Creator)",
            isCreatorAdmin = true,
            coins = 500,
            isSubscribedToChannel = false,
            totalVideosWatched = 0,
            channelSubscribersCount = 85400,
            channelGoalSubscribers = 100000,
            totalViewsCount = 1250000
        )
        // Update user profile stats
        dao.insertOrUpdateProfile(
            currentProfile.copy(
                coins = currentProfile.coins + rewardCoins,
                totalVideosWatched = currentProfile.totalVideosWatched + 1
            )
        )
    }

    suspend fun toggleSubscribeChannel(profile: UserProfileEntity) {
        val updatedSubState = !profile.isSubscribedToChannel
        val newSubCount = if (updatedSubState) profile.channelSubscribersCount + 1 else profile.channelSubscribersCount - 1
        val newCoins = if (updatedSubState) profile.coins + 250 else profile.coins
        dao.insertOrUpdateProfile(
            profile.copy(
                isSubscribedToChannel = updatedSubState,
                channelSubscribersCount = newSubCount,
                coins = newCoins
            )
        )
    }
}
