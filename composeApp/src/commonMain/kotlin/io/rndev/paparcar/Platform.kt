package io.rndev.paparcar

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform