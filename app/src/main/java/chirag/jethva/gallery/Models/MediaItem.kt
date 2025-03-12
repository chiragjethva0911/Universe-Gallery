package chirag.jethva.gallery.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MediaItem(
    val id: Long,
    val path: String,
    val name: String,
    val dateModified: Long,
    val isVideo: Boolean = false
) : Parcelable