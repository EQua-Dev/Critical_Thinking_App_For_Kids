package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.screens.video

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.ui.theme.Typography
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.ActivityTypeViewModel
import com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.viewmodels.VideoViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun VideoScreen(videoViewModel: VideoViewModel = hiltViewModel(), childStage: String, category: String) {
    val videoData by videoViewModel.videoData.observeAsState()

    LaunchedEffect(Unit) {
        videoViewModel.fetchVideoData(childStage, category)
    }

    if (videoData != null) {
        Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            // YouTube video player
            YouTubePlayerView(
                youtubeVideoId = videoData!!.videoId,
                lifecycleOwner = LocalLifecycleOwner.current
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Video title
            Text(
                text = videoData!!.title,
                style = Typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Video description
            Text(
                text = videoData!!.description,
                style = Typography.bodyMedium
            )
        }
    } else {
        Text("Loading video...")
    }
}
