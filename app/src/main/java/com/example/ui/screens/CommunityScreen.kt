package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.RssFeed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CommunityPostEntity
import com.example.data.local.UserProfileEntity
import com.example.ui.components.CommunityPostCard
import com.example.ui.theme.RedPrimary

@Composable
fun CommunityScreen(
    userProfile: UserProfileEntity?,
    posts: List<CommunityPostEntity>,
    onLikeToggle: (CommunityPostEntity) -> Unit,
    onAddPost: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var newPostText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("community_screen_lazy_column"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Creator Broadcast Card (Only shown if Creator Admin Mode)
        if (userProfile?.isCreatorAdmin == true) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.RssFeed,
                                contentDescription = null,
                                tint = RedPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Broadcast Update to Community",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = newPostText,
                            onValueChange = { newPostText = it },
                            placeholder = { Text("What's on your mind, Premi Sehjal? Write announcement...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (newPostText.isNotBlank()) {
                                    onAddPost(newPostText)
                                    newPostText = ""
                                }
                            },
                            enabled = newPostText.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Post Broadcast", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "📢 Official Creator Broadcasts",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Posts List
        items(
            items = posts,
            key = { it.id }
        ) { post ->
            CommunityPostCard(
                post = post,
                onLikeToggle = { onLikeToggle(post) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
    }
}
