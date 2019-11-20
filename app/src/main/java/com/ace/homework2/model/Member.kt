package com.ace.homework2.model

data class Member(
    val avatarHash: Any,
    val avatarUrl: Any,
    val fullName: String,
    val id: String,
    val idMemberReferrer: Any,
    val initials: String,
    val memberType: String,
    val nonPublic: NonPublic,
    val nonPublicAvailable: Boolean,
    val username: String
)