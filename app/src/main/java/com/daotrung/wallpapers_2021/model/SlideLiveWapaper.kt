package com.daotrung.wallpapers_2021.model

import java.io.Serializable
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

data class SlideLiveWapaper(
    var id: Int,
    var kind: String,
    var title: String,
    var description: String?,
    var review: Boolean,
    var premium: Boolean,
    var color: String,
    var size: String,
    var resolution: String,
    var comment: Boolean,
    var comments: Int,
    var downloads: Int,
    var views: Int,
    var shares: Int,
    var sets: Int,
    var trusted: Boolean,
    var user: String,
    var userid: Int,
    var userimage: String,
    var type: String,
    var extension: String,
    var thumbnail: String,
    var image: String,
    var original: String,
    var created: String,
    var tags: String
) : Serializable
