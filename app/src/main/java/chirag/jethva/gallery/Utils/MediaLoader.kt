package chirag.jethva.gallery.Utils

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import chirag.jethva.gallery.Models.FolderItem
import chirag.jethva.gallery.Models.MediaItem
import java.io.File

class MediaLoader(private val context: Context) {

    fun loadPhotos(): List<MediaItem> {
        val photos = mutableListOf<MediaItem>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_MODIFIED
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateModifiedColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val path = cursor.getString(dataColumn)
                val name = cursor.getString(nameColumn)
                val dateModified = cursor.getLong(dateModifiedColumn)

                photos.add(MediaItem(id, path, name, dateModified))
            }
        }

        return photos
    }

    fun loadVideos(): List<MediaItem> {
        val videos = mutableListOf<MediaItem>()

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_MODIFIED
        )

        val sortOrder = "${MediaStore.Video.Media.DATE_MODIFIED} DESC"

        context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val dateModifiedColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val path = cursor.getString(dataColumn)
                val name = cursor.getString(nameColumn)
                val dateModified = cursor.getLong(dateModifiedColumn)

                videos.add(MediaItem(id, path, name, dateModified, isVideo = true))
            }
        }

        return videos
    }

    fun loadFolders(): List<FolderItem> {
        val folderMap = mutableMapOf<String, MutableList<MediaItem>>()

        val allMedia = loadPhotos() + loadVideos()

        for (media in allMedia) {
            val file = File(media.path)
            val folderPath = file.parent ?: continue
            val folderName = File(folderPath).name

            if (!folderMap.containsKey(folderPath)) {
                folderMap[folderPath] = mutableListOf()
            }

            folderMap[folderPath]?.add(media)
        }

        return folderMap.map { (path, mediaItems) ->
            FolderItem(
                path = path,
                name = File(path).name,
                mediaCount = mediaItems.size,
                thumbnailPath = mediaItems.firstOrNull()?.path ?: ""
            )
        }.sortedByDescending { it.mediaCount }
    }

    fun getUriFromMediaId(id: Long, isVideo: Boolean): Uri {
        val contentUri = if (isVideo) {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        return ContentUris.withAppendedId(contentUri, id)
    }
}