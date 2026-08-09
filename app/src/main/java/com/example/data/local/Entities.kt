package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "campaigns")
data class VideoCampaignEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val videoTitle: String,
    val videoUrl: String,
    val thumbnailUrl: String = "",
    val targetViews: Int,
    val currentViews: Int = 0,
    val targetSubs: Int = 0,
    val currentSubs: Int = 0,
    val rewardCoins: Int,
    val durationSeconds: Int = 60,
    val category: String = "Video View", // "Video View", "Shorts", "Subscribe"
    val isFeatured: Boolean = false,
    val createdTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "community_posts")
data class CommunityPostEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val authorName: String = "Premi Sehjal Official",
    val isVerified: Boolean = true,
    val contentText: String,
    val videoUrl: String = "",
    val imageDrawableName: String = "img_premi_sehjal_hero_1786299977012",
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val isLikedByUser: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val userEmail: String = "kishanaifact@gmail.com",
    val displayName: String = "Premi Sehjal (Creator)",
    val isCreatorAdmin: Boolean = true,
    val coins: Int = 1500,
    val isSubscribedToChannel: Boolean = false,
    val totalVideosWatched: Int = 12,
    val channelSubscribersCount: Int = 85400,
    val channelGoalSubscribers: Int = 100000,
    val totalViewsCount: Int = 1240500
)

@Entity(tableName = "task_history")
data class TaskHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val campaignId: Int,
    val completedAt: Long = System.currentTimeMillis(),
    val coinsEarned: Int
)
