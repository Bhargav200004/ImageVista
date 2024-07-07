package com.example.imagevista.data.repository

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.example.imagevista.domain.repository.Downloader
import java.io.File

class AndroidImageDownloader(
    context: Context
) : Downloader {

private val downloaderManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    override fun downloadFile(url: String, fileName: String?) {
        try {
            val tile = fileName ?: "New Image"
            val request = DownloadManager.Request(url.toUri())
                .setMimeType("image/*")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(tile)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + tile + ".jpg"
                )
            downloaderManager.enqueue(request)
        }
        catch (e : Exception){
            e.printStackTrace()
        }
    }
}