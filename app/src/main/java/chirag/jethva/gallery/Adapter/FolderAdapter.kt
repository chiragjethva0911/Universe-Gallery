package chirag.jethva.gallery.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chirag.jethva.gallery.Models.FolderItem
import chirag.jethva.gallery.databinding.ItemFolderBinding
import com.bumptech.glide.Glide

class FolderAdapter(
    private val folders: List<FolderItem>,
    private val onItemClick: (FolderItem) -> Unit
) : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val binding = ItemFolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        holder.bind(folders[position])
    }

    override fun getItemCount(): Int = folders.size

    inner class FolderViewHolder(private val binding: ItemFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
            
        fun bind(folder: FolderItem) {
            Glide.with(binding.root.context)
                .load(folder.thumbnailPath)
                .centerCrop()
                .into(binding.ivFolderThumbnail)
                
            binding.tvFolderName.text = folder.name
            binding.tvItemCount.text = "${folder.mediaCount} items"
            
            binding.root.setOnClickListener {
                onItemClick(folder)
            }
        }
    }
}