package chirag.jethva.gallery.Fregment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import chirag.jethva.gallery.Adapter.FolderAdapter
import chirag.jethva.gallery.Models.FolderItem
import chirag.jethva.gallery.Utils.MediaLoader
import chirag.jethva.gallery.databinding.FragmentFolderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FolderFragment : Fragment() {
    private var _binding: FragmentFolderBinding? = null
    private val binding get() = _binding!!

    private lateinit var mediaLoader: MediaLoader
    private lateinit var folderAdapter: FolderAdapter
    private val folders = mutableListOf<FolderItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaLoader = MediaLoader(requireContext())

        setupRecyclerView()
        loadFolders()
    }

    private fun setupRecyclerView() {
        binding.rvFolders.layoutManager = GridLayoutManager(requireContext(), 3)
        folderAdapter = FolderAdapter(folders) { folderItem ->
            // Handle folder click
            Toast.makeText(
                requireContext(),
                "Folder: ${folderItem.name} (${folderItem.mediaCount} items)",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.rvFolders.adapter = folderAdapter
    }

    private fun loadFolders() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.rvFolders.visibility = View.GONE

            val result = withContext(Dispatchers.IO) {
                mediaLoader.loadFolders()
            }

            folders.clear()
            folders.addAll(result)
            folderAdapter.notifyDataSetChanged()

            if (folders.isEmpty()) {
                binding.tvNoFolders.visibility = View.VISIBLE
                binding.rvFolders.visibility = View.GONE
            } else {
                binding.tvNoFolders.visibility = View.GONE
                binding.rvFolders.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}