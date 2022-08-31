package com.jatpack.socialmediahub.ui.socialmediadownloader.services


import kotlinx.serialization.Serializable


@Serializable
data class MediaUrl(
    var mediaDomain: String? = null,
    var mediaType: String? = null,
    var mediaUrl: String? = null,
    var mediaSize: String? = null,
    var mediaSelected: Boolean? = false,
    var mediaTitle: String? = null
)
