package com.mrp.fe3hreference.data.source

import java.io.File

/**
 * Reads the production JSON straight off disk from `src/main/res/raw`, so
 * repository tests exercise the real FE3H data without needing an Android
 * runtime. Gradle's `Test` task runs with the module directory as its
 * working directory, so the path below is relative to `app/`.
 */
class TestFE3HRawDataSource : FE3HRawDataSource {
    override fun charactersJson(): String = readRaw("characters.json")

    override fun itemsJson(): String = readRaw("items.json")

    override fun crestsJson(): String = readRaw("crests.json")

    override fun teasJson(): String = readRaw("teas.json")

    private fun readRaw(fileName: String): String {
        val file = File("src/main/res/raw/$fileName")
        require(file.exists()) { "Could not find $file" }
        return file.readText()
    }
}
