package com.ace.homework2.model.members.data

import com.google.gson.annotations.SerializedName

data class Member(
    val fullName: String,
    val id: String,
    val initials: String,
    @SerializedName("avatarHash")
    var avatar: String? = null,
    val username: String
)