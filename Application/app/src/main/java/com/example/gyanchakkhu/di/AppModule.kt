package com.example.gyanchakkhu.di

import com.example.gyanchakkhu.viewmodels.AuthViewModel
import com.example.gyanchakkhu.viewmodels.BooksViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { AuthViewModel(get(), androidContext()) }
    single { BooksViewModel() }
}