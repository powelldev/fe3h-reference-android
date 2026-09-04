package com.mrp.fe3hreference.data.source

interface FE3HRawDataSource {
    fun charactersJson(): String

    fun itemsJson(): String

    fun crestsJson(): String

    fun teasJson(): String
}
