package com.example.domain.di

import com.example.domain.repository.MessagesRepository
import com.example.domain.repository.MessagesRepositoryImpl
import com.example.domain.repository.generate.IdGenerator
import com.example.domain.repository.generate.IdGeneratorImpl
import org.koin.dsl.module

val domainModule = module{

    single<MessagesRepository> {
        MessagesRepositoryImpl(
            tcp = get(),
            localRepository = get(),
            idGenerator = get()
        )
    }

    factory<IdGenerator> {
        IdGeneratorImpl()
    }

}