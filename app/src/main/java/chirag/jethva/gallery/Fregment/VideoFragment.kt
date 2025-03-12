package chirag.jethva.gallery.Fregment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import chirag.jethva.gallery.Adapter.MediaAdapter
import chirag.jethva.gallery.Models.MediaItem
import chirag.jethva.gallery.R
import chirag.jethva.gallery.Utils.MediaLoader
import chirag.jethva.gallery.databinding.FragmentVideoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoFragment : Fragment() {
    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    private lateinit var mediaLoader: MediaLoader
    private lateinit var mediaAdapter: MediaAdapter
    private val videos = mutableListOf<MediaItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaLoader = MediaLoader(requireContext())

        setupRecyclerView()
        loadVideos()
    }

    private fun setupRecyclerView() {
        binding.rvVideos.layoutManager = GridLayoutManager(requireContext(), 4)
        mediaAdapter = MediaAdapter(videos) { mediaItem ->
            // Handle item click
            Toast.makeText(requireContext(), "Video: ${mediaItem.name}", Toast.LENGTH_SHORT).show()
        }
        binding.rvVideos.adapter = mediaAdapter
    }

    private fun loadVideos() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.rvVideos.visibility = View.GONE

            val result = withContext(Dispatchers.IO) {
                mediaLoader.loadVideos()
            }

            videos.clear()
            videos.addAll(result)
            mediaAdapter.notifyDataSetChanged()

            if (videos.isEmpty()) {
                binding.tvNoVideos.visibility = View.VISIBLE
                binding.rvVideos.visibility = View.GONE
            } else {
                binding.tvNoVideos.visibility = View.GONE
                binding.rvVideos.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}