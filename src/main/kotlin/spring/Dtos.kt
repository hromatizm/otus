package spring

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val nic: String,
)

@Serializable
data class UserListDto(
    val users: List<UserDto>
)
