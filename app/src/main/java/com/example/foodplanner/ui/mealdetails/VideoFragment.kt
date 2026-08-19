package com.example.foodplanner.ui.mealdetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.databinding.FragmentMealVideoBinding

/**
 * VideoFragment — Engineer 1 (Lead UI/UX & Design Specialist)
 *
 * Tab 3 of MealDetailsActivity — shows Video thumbnail with central Play Button overlay matching mockup image 3.
 * Tapping the play button ("لما العميل يضغط علي كلمة video يطلع الفيديو") activates the embedded YouTube player.
 * Includes Related Meals horizontal list.
 *
 * Course ref: WebView p450-454, Glide p633-637
 */
class VideoFragment : Fragment() {
    private var _binding: FragmentMealVideoBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(youtubeUrl: String) = VideoFragment().apply {
            arguments = Bundle().also { it.putString("YOUTUBE_URL", youtubeUrl) }
        }

        fun extractVideoId(url: String): String? {
            return try {
                when {
                    url.contains("v=") -> url.substringAfter("v=").substringBefore("&")
                    url.contains("youtu.be/") -> url.substringAfter("youtu.be/").substringBefore("?")
                    else -> null
                }
            } catch (e: Exception) { null }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMealVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val youtubeUrl = arguments?.getString("YOUTUBE_URL") ?: "https://www.youtube.com/watch?v=1IszT_guI08"
        val videoId = extractVideoId(youtubeUrl) ?: "1IszT_guI08"

        // Load YouTube video thumbnail image (hqdefault.jpg)
        val thumbnailUrl = "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
        Glide.with(requireContext())
            .load(thumbnailUrl)
            .placeholder(R.drawable.ic_chef_logo)
            .centerCrop()
            .into(binding.ivVideoThumbnail)

        // When user taps Play button or Thumbnail -> Hide thumbnail & load/play video player
        val startVideoPlayback = View.OnClickListener {
            binding.ivVideoThumbnail.visibility = View.GONE
            binding.viewVideoOverlay.visibility = View.GONE
            binding.btnPlayVideo.visibility = View.GONE
            binding.pbVideoLoading.visibility = View.VISIBLE
            binding.wvRecipeVideo.visibility = View.VISIBLE

            setupWebView(videoId)
        }

        binding.btnPlayVideo.setOnClickListener(startVideoPlayback)
        binding.ivVideoThumbnail.setOnClickListener(startVideoPlayback)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(videoId: String) {
        binding.wvRecipeVideo.apply {
            settings.apply {
                javaScriptEnabled = true
                mediaPlaybackRequiresUserGesture = false
                loadWithOverviewMode = true
                useWideViewPort = true
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            }
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.pbVideoLoading.visibility = View.GONE
                }
            }
        }

        val html = """
            <html>
            <body style="margin:0;padding:0;background:#000;">
                <iframe 
                    width="100%" height="100%"
                    src="https://www.youtube.com/embed/$videoId?autoplay=1&rel=0"
                    frameborder="0"
                    allow="autoplay; encrypted-media"
                    allowfullscreen>
                </iframe>
            </body>
            </html>
        """.trimIndent()

        binding.wvRecipeVideo.loadData(html, "text/html", "utf-8")
    }

    override fun onDestroyView() {
        binding.wvRecipeVideo.destroy()
        super.onDestroyView()
        _binding = null
    }
}
