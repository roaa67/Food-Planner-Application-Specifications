package com.example.foodplanner.ui.mealdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.foodplanner.R
import com.example.foodplanner.databinding.FragmentMealVideoBinding

class VideoFragment : Fragment() {

    private var _binding: FragmentMealVideoBinding? = null
    private val binding get() = _binding!!

    companion object {

        private const val ARG_YOUTUBE_URL = "YOUTUBE_URL"

        fun newInstance(youtubeUrl: String) =
            VideoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_YOUTUBE_URL, youtubeUrl)
                }
            }

        fun extractVideoId(url: String): String? {
            return try {
                when {
                    url.contains("v=") ->
                        url.substringAfter("v=")
                            .substringBefore("&")

                    url.contains("youtu.be/") ->
                        url.substringAfter("youtu.be/")
                            .substringBefore("?")

                    url.contains("youtube.com/embed/") ->
                        url.substringAfter("youtube.com/embed/")
                            .substringBefore("?")

                    else -> null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMealVideoBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val youtubeUrl =
            arguments?.getString(ARG_YOUTUBE_URL).orEmpty()

        val videoId = extractVideoId(youtubeUrl)

        if (youtubeUrl.isBlank() || videoId.isNullOrBlank()) {
            showNoVideo()
            return
        }

        showVideoThumbnail(videoId)

        val openVideoListener = View.OnClickListener {
            openYoutubeVideo(youtubeUrl)
        }

        binding.btnPlayVideo.setOnClickListener(openVideoListener)

        binding.ivVideoThumbnail.setOnClickListener(openVideoListener)
    }

    private fun showVideoThumbnail(videoId: String) {

        binding.layoutNoVideo.visibility = View.GONE
        binding.wvRecipeVideo.visibility = View.GONE
        binding.pbVideoLoading.visibility = View.GONE

        binding.ivVideoThumbnail.visibility = View.VISIBLE
        binding.viewVideoOverlay.visibility = View.VISIBLE
        binding.btnPlayVideo.visibility = View.VISIBLE

        val thumbnailUrl =
            "https://img.youtube.com/vi/$videoId/hqdefault.jpg"

        Glide.with(this)
            .load(thumbnailUrl)
            .placeholder(R.drawable.ic_chef_logo)
            .centerCrop()
            .into(binding.ivVideoThumbnail)
    }

    private fun openYoutubeVideo(youtubeUrl: String) {

        try {

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(youtubeUrl)
            )

            startActivity(intent)

        } catch (e: Exception) {

            showNoVideo()
        }
    }

    private fun showNoVideo() {

        binding.ivVideoThumbnail.visibility = View.GONE
        binding.viewVideoOverlay.visibility = View.GONE
        binding.btnPlayVideo.visibility = View.GONE
        binding.wvRecipeVideo.visibility = View.GONE
        binding.pbVideoLoading.visibility = View.GONE

        binding.layoutNoVideo.visibility = View.VISIBLE
    }

    override fun onDestroyView() {

        binding.wvRecipeVideo.apply {
            stopLoading()
            loadUrl("about:blank")
            clearHistory()
            removeAllViews()
            destroy()
        }

        _binding = null

        super.onDestroyView()
    }
}