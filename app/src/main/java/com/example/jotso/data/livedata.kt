package com.example.jotso.data

sealed class livedata{
    object MainMenu : livedata()

    data class Yongd(
        val username: String
    ) : livedata()

    data class People(
        val username: String
    ) : livedata()
}