package com.example.condorapp.data.injection

import com.example.condorapp.data.datasource.ArticuloDataSource
import com.example.condorapp.data.datasource.ArticuloFirestoreDataSource
import com.example.condorapp.data.datasource.ReviewDataSource
import com.example.condorapp.data.datasource.ReviewFirestoreDataSource
import com.example.condorapp.data.datasource.UsuarioDataSource
import com.example.condorapp.data.datasource.UsuarioFirestoreDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de inyección que vincula las interfaces DataSource a sus implementaciones.
 *
 * ╔═══════════════════════════════════════════════════════════════════════════╗
 * ║  PARA CAMBIAR DE FIRESTORE A RETROFIT:                                  ║
 * ║  Solo cambia las clases de implementación en cada @Binds:               ║
 * ║    ArticuloFirestoreDataSource → ArticuloRemoteDataSource               ║
 * ║    UsuarioFirestoreDataSource  → UsuarioRemoteDataSource                ║
 * ║    ReviewFirestoreDataSource   → ReviewRemoteDataSource                 ║
 * ╚═══════════════════════════════════════════════════════════════════════════╝
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindArticuloDataSource(
        impl: ArticuloFirestoreDataSource
    ): ArticuloDataSource

    @Binds
    @Singleton
    abstract fun bindUsuarioDataSource(
        impl: UsuarioFirestoreDataSource
    ): UsuarioDataSource

    @Binds
    @Singleton
    abstract fun bindReviewDataSource(
        impl: ReviewFirestoreDataSource
    ): ReviewDataSource
}
