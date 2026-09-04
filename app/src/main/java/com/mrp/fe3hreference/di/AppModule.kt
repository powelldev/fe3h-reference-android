package com.mrp.fe3hreference.di

import com.mrp.fe3hreference.data.repository.FE3HRepositoryImpl
import com.mrp.fe3hreference.data.source.AndroidFE3HRawDataSource
import com.mrp.fe3hreference.data.source.FE3HJsonParser
import com.mrp.fe3hreference.data.source.FE3HRawDataSource
import com.mrp.fe3hreference.domain.repository.FE3HRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { FE3HJsonParser() }
    single<FE3HRawDataSource> { AndroidFE3HRawDataSource(androidContext()) }
    single<FE3HRepository> { FE3HRepositoryImpl(get(), get()) }
}
