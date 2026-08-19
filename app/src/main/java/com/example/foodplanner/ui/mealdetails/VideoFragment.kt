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
import com.example.foodplanner.databinding.FragmentMealVideoBinding

/**
 * VideoFragment — Engineer 1 (Lead UI/UX & Design Specialist)
 *
 * Tab 3 of MealDetailsActivity — embeds YouTube recipe video using WebView.
 * Engineer 1 responsibility: "Embedded Video Player (not a raw URL)" — course p450-454 WebView
 *
 * The YouTube URL (strYoutube from API) is converted into an iframe embed inside WebView:
 *   https://www.youtube.com/watch?v=VIDEO_ID  →  <iframe src="https://www.youtube.com/embed/VIDEO_ID">
 *
 * This avoids raw URL display and provides a proper embedded video player experience.
 *
 * Course ref: WebView p450-454, JavaScript settings p452
 */
class VideoFragment : Fragment() {
    private var _binding: FragmentMealVideoBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(youtubeUrl: String) = VideoFragment().apply {
            arguments = Bundle().also { it.putString("YOUTUBE_URL", youtubeUrl) }
        }

        /** Extract YouTube video ID from standard YouTube URL formats */
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

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val youtubeUrl = arguments?.getString("YOUTUBE_URL") ?: ""
        val videoId = extractVideoId(youtubeUrl)

        if (videoId != null) {
            setupWebView(videoId)
        } else {
            // No video URL — show fallback (visibility toggle — course p232)
            binding.wvRecipeVideo.visibility = View.GONE
            binding.pbVideoLoading.visibility = View.GONE
            binding.layoutNoVideo.visibility = View.VISIBLE
        }
    }

    /**
     * Configure WebView to display YouTube embed iframe
     * Course ref: WebView p450, WebSettings.setJavaScriptEnabled() p452
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(videoId: String) {
        binding.wvRecipeVideo.apply {
            settings.apply {
                javaScriptEnabled = true                          // Required for YouTube (course p452)
                mediaPlaybackRequiresUserGesture = false          // Allows autoplay controls
                loadWithOverviewMode = true
                useWideViewPort = true
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK  // Course p454 — cache strategy
            }
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.pbVideoLoading.visibility = View.GONE
                }
            }
        }

        // YouTube iframe embed HTML
        val html = """
            <html>
            <body style="margin:0;padding:0;background:#000;">
                <iframe 
                    width="100%" height="100%"
                    src="https://www.youtube.com/embed/$videoId?rel=0&autoplay=0"
                    frameborder="0"
                    allowfullscreen>
                </iframe>
            </body>
            </html>
        """.trimIndent()

        binding.wvRecipeVideo.loadData(html, "text/html", "utf-8")
    }

    override fun onDestroyView() {
        binding.wvRecipeVideo.destroy()   // Proper WebView cleanup (course p454)
        super.onDestroyView()
        _binding = null
    }
}
