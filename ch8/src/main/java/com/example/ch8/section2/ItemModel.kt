package com.example.ch8.section2

data class ItemModel(
    var id: Long,
    var author: String? = null,
    var title: String? = null,
    var description: String? = null,
    var urlToImage: String? = null,
    var publishedAt: String? = null
)
