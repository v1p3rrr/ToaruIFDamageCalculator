package com.example.toaruifdamagecalculator.data.di

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped


@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {

    @FragmentScoped
    @Provides
    fun providePicasso(@ApplicationContext context: Context): Picasso {
        val picasso = Picasso.Builder(context)
            .downloader(OkHttp3Downloader(context, Long.MAX_VALUE))
            .build()
        picasso.isLoggingEnabled = true
        return picasso
    }


}