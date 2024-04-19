package com.example.sajidaliandroidassignment.network.response

/**
 * Data class for image API response Item
 */

data class ImagesResponseItem(
    val backupDetails: BackupDetails,
    val coverageURL: String,
    val id: String,
    val language: String,
    val mediaType: Int,
    val publishedAt: String,
    val publishedBy: String,
    val thumbnail: Thumbnail,
    val title: String
)