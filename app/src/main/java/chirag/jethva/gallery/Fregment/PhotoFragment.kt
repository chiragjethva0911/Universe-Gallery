package chirag.jethva.gallery.Fregment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import chirag.jethva.gallery.Adapter.MediaAdapter
import chirag.jethva.gallery.Models.MediaItem
import chirag.jethva.gallery.Utils.MediaLoader
import chirag.jethva.gallery.databinding.FragmentPhotoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoFragment : Fragment() {

    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!

    private lateinit var mediaLoader: MediaLoader
    private lateinit var mediaAdapter: MediaAdapter
    private val photos = mutableListOf<MediaItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaLoader = MediaLoader(requireContext())

        setupRecyclerView()
        loadPhotos()
    }

    private fun setupRecyclerView() {
        binding.rvPhotos.layoutManager = GridLayoutManager(requireContext(), 4)
        mediaAdapter = MediaAdapter(photos) { mediaItem ->
            // Handle item click
            Toast.makeText(requireContext(), "Photo: ${mediaItem.name}", Toast.LENGTH_SHORT).show()
        }
        binding.rvPhotos.adapter = mediaAdapter
    }

    private fun loadPhotos() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.rvPhotos.visibility = View.GONE

            val result = withContext(Dispatchers.IO) {
                mediaLoader.loadPhotos()
            }

            photos.clear()
            photos.addAll(result)
            mediaAdapter.notifyDataSetChanged()

            if (photos.isEmpty()) {
                binding.tvNoPhotos.visibility = View.VISIBLE
                binding.rvPhotos.visibility = View.GONE
            } else {
                binding.tvNoPhotos.visibility = View.GONE
                binding.rvPhotos.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}