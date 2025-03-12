package chirag.jethva.gallery.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chirag.jethva.gallery.Models.MediaItem
import chirag.jethva.gallery.databinding.ItemMediaBinding
import com.bumptech.glide.Glide

class MediaAdapter(
    private val mediaItems: List<MediaItem>,
    private val onItemClick: (MediaItem) -> Unit
) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemMediaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(mediaItems[position])
    }

    override fun getItemCount(): Int = mediaItems.size

    inner class MediaViewHolder(private val binding: ItemMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mediaItem: MediaItem) {
            Glide.with(binding.root.context)
                .load(mediaItem.path)
                .centerCrop()
                .into(binding.ivMedia)

            binding.ivVideoIcon.visibility = if (mediaItem.isVideo) View.VISIBLE else View.GONE

            binding.root.setOnClickListener {
                onItemClick(mediaItem)
            }
        }
    }
}