package com.azimmermannrosenthal.myapplication

data class Comment (
    val postId: Int = 0,
    val id: Int = 0,
    val name: String? = null,
    val email: String? = null,
    val body: String? = null
)