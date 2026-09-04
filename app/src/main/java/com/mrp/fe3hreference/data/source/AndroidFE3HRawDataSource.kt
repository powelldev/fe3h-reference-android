package com.mrp.fe3hreference.data.source

import android.content.Context
import androidx.annotation.RawRes
import com.mrp.fe3hreference.R

class AndroidFE3HRawDataSource(
    private val context: Context,
) : FE3HRawDataSource {
    override fun charactersJson(): String = readRaw(R.raw.characters)

    override fun itemsJson(): String = readRaw(R.raw.items)

    override fun crestsJson(): String = readRaw(R.raw.crests)

    override fun teasJson(): String = readRaw(R.raw.teas)

    private fun readRaw(
        @RawRes resId: Int,
    ): String = context.resources.openRawResource(resId).bufferedReader().use { it.readText() }
}
