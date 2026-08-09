package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Campaign queries
    @Query("SELECT * FROM campaigns ORDER BY isFeatured DESC, createdTimestamp DESC")
    fun getAllCampaigns(): Flow<List<VideoCampaignEntity>>

    @Query("SELECT * FROM campaigns WHERE id = :id")
    suspend fun getCampaignById(id: Int): VideoCampaignEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaign(campaign: VideoCampaignEntity): Long

    @Update
    suspend fun updateCampaign(campaign: VideoCampaignEntity)

    @Query("DELETE FROM campaigns WHERE id = :id")
    suspend fun deleteCampaignById(id: Int)

    // Community Post queries
    @Query("SELECT * FROM community_posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<CommunityPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: CommunityPostEntity)

    @Update
    suspend fun updatePost(post: CommunityPostEntity)

    // User Profile queries
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    // Task History
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskHistory(history: TaskHistoryEntity)

    @Query("SELECT campaignId FROM task_history")
    fun getCompletedCampaignIds(): Flow<List<Int>>
}
