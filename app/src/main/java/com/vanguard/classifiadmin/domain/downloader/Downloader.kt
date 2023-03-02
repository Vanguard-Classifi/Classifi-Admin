package com.vanguard.classifiadmin.domain.downloader

interface Downloader {
    fun downloadFile(url: String): Long

    fun downloadAvatar(url: String)
}