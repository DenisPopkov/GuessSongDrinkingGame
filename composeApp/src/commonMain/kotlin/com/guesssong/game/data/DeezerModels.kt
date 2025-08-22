package com.guesssong.game.data

import kotlinx.serialization.Serializable

@Serializable
data class DeezerSearchResponse(
    val data: List<DeezerTrack>,
    val total: Int,
    val next: String? = null
)

@Serializable
data class DeezerTrack(
    val id: Long,
    val title: String,
    val title_short: String? = null,
    val title_version: String? = null,
    val link: String,
    val duration: Int,
    val rank: Int,
    val explicit_lyrics: Boolean,
    val explicit_content_lyrics: Int,
    val explicit_content_cover: Int,
    val preview: String,
    val md5_image: String,
    val artist: DeezerArtist,
    val album: DeezerAlbum,
    val type: String
)

@Serializable
data class DeezerArtist(
    val id: Long,
    val name: String,
    val link: String,
    val picture: String,
    val picture_small: String,
    val picture_medium: String,
    val picture_big: String,
    val picture_xl: String,
    val tracklist: String,
    val type: String
)

@Serializable
data class DeezerAlbum(
    val id: Long,
    val title: String,
    val cover: String,
    val cover_small: String,
    val cover_medium: String,
    val cover_big: String,
    val cover_xl: String,
    val md5_image: String,
    val tracklist: String,
    val type: String
)

@Serializable
data class DeezerPlaylistResponse(
    val id: Long,
    val title: String,
    val description: String,
    val duration: Int,
    val public: Boolean,
    val is_loved_track: Boolean,
    val collaborative: Boolean,
    val nb_tracks: Int,
    val fans: Int,
    val link: String,
    val share: String,
    val picture: String,
    val picture_small: String,
    val picture_medium: String,
    val picture_big: String,
    val picture_xl: String,
    val checksum: String,
    val tracklist: String,
    val creation_date: String,
    val md5_image: String,
    val picture_type: String,
    val creator: DeezerUser,
    val type: String,
    val tracks: DeezerPlaylistTracks
)

@Serializable
data class DeezerUser(
    val id: Long,
    val name: String,
    val tracklist: String,
    val type: String
)

@Serializable
data class DeezerPlaylistTracks(
    val data: List<DeezerTrack>,
    val checksum: String
) 