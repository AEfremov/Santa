package com.efremov.santa.data

import java.io.Serializable

data class UserDto(
    val id: String,
    val first_name: String,
    val last_name: String
) : Serializable

data class UserResponse(
    val user: UserDto
) : Serializable

data class SignUpParams(
    /*@Json(name = "first_name") */val first_name: String = "",
    /*@Json(name = "last_name") */val last_name: String = ""
)

data class JoinGroupParams(
    val code: String = "",
    val user_id: String = ""
)

data class CreateGroupParams(
    val name: String = "",
    val user_id: String = ""
)

data class SantaDrawParams(
    val user_id: String = "",
    val group_id: String = ""
)

data class GroupResponse(
    val group: GroupDto?
)

data class GroupDataResponse(
    val groups: List<GroupDto>?
)

data class GroupDto(
    val id: String,
    val is_draw: Boolean,
    val code: String,
    val name: String,
    val users: List<GroupUserDto>,
    val owner: Boolean
) : Serializable

data class GroupUserDto(
    val id: String,
    val first_name: String,
    val last_name: String,
    val is_owner: Boolean,
    var avatar: AvatarObj
) : Serializable

data class AvatarObj(
    val avatarResId: Int,
    val avatarId: Int
) : Serializable