package com.geeks.pixabay

data class PixaBayModel(
    var hits: ArrayList<ImageModel>
)

data class ImageModel(
    var largeImageURL: String
)
