package com.cherryyeti.nativen.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class HomeDoujin(
    val id: Int, val thumbnail: String, val title: String, val width: Int, val height: Int
) : Parcelable


@Parcelize
data class Doujin(
    val id: Int,
    val media_id: String,
    val title: Title,
    val images: Images,
    val tags: List<Tag>,
    val num_pages: Int,
    val num_favorites: Int,
    val upload_date: Long

) : Parcelable


@Parcelize
data class Tag(
    val id: Int, val type: String, val name: String, val url: String, val count: Int
) : Parcelable

@Parcelize
data class Title(
    val english: String, val japanese: String, val pretty: String
) : Parcelable

@Parcelize
data class Images(
    val pages: List<Image>,
    val cover: Image,
    val thumbnail: Image,
    val scanlator: String,
) : Parcelable

@Parcelize
data class Image(
    val t: String, val w: Int, val h: Int
) : Parcelable

val dummyDoujin = Doujin(
    id = 0, media_id = "",

    title = Title(
        english = "", japanese = "", pretty = ""
    ), upload_date = 0L, images = Images(
        pages = listOf(),
        cover = Image(t = "", w = 0, h = 0),
        thumbnail = Image(t = "", w = 0, h = 0),
        scanlator = "",
    ), tags = listOf(), num_pages = 0, num_favorites = 0
)