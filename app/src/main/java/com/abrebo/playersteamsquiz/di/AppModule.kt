package com.abrebo.playersteamsquiz.di

import android.content.Context
import com.abrebo.playersteamsquiz.data.datasource.DataSource
import com.abrebo.playersteamsquiz.data.datasource.GameDataSource
import com.abrebo.playersteamsquiz.data.repo.Repository
import com.abrebo.playersteamsquiz.retrofit.ApiUtils
import com.abrebo.playersteamsquiz.retrofit.QuestionDao
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UsersCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game1EasyCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game1MediumCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game1HardCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game2EasyCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game2MediumCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game2HardCollection

    @Provides
    @Singleton
    fun provideDataSource(@UsersCollection collectionReference: CollectionReference):DataSource{
        return DataSource(collectionReference)
    }
    @Provides
    @Singleton
    fun provideGameDataSource(dao:QuestionDao,
                              @Game1EasyCollection collectionReferenceGame1Easy: CollectionReference,
                              @Game1MediumCollection collectionReferenceGame1Medium: CollectionReference,
                              @Game1HardCollection collectionReferenceGame1Hard: CollectionReference,
                              @Game2EasyCollection collectionReferenceGame2Easy: CollectionReference,
                              @Game2MediumCollection collectionReferenceGame2Medium: CollectionReference,
                              @Game2HardCollection collectionReferenceGame2Hard: CollectionReference,
                              ):GameDataSource{
        return GameDataSource(dao,collectionReferenceGame1Easy,collectionReferenceGame1Medium,collectionReferenceGame1Hard,
            collectionReferenceGame2Easy,collectionReferenceGame2Medium, collectionReferenceGame2Hard)
    }

    @Provides
    @Singleton
    fun provideRepository(dataSource: DataSource,
                          gameDataSource: GameDataSource,
                          @ApplicationContext context: Context):Repository{
        return Repository(dataSource,gameDataSource,context)
    }

    @Provides
    @Singleton
    fun provideTeamDao():QuestionDao{
        return ApiUtils.getTeamDao()
    }

    @Provides
    @Singleton
    @UsersCollection
    fun provideCollectionReference():CollectionReference{
        return Firebase.firestore.collection("Users")
    }
    @Provides
    @Singleton
    @Game1EasyCollection
    fun provideCollectionReferenceGame1():CollectionReference{
        return Firebase.firestore.collection("Game1_Easy")
    }
    @Provides
    @Singleton
    @Game1MediumCollection
    fun provideCollectionReferenceGame2():CollectionReference{
        return Firebase.firestore.collection("Game1_Medium")
    }
    @Provides
    @Singleton
    @Game1HardCollection
    fun provideCollectionReferenceGame3():CollectionReference{
        return Firebase.firestore.collection("Game1_Hard")
    }
    @Provides
    @Singleton
    @Game2EasyCollection
    fun provideCollectionReferenceGame4():CollectionReference{
        return Firebase.firestore.collection("Game2_Easy")
    }
    @Provides
    @Singleton
    @Game2MediumCollection
    fun provideCollectionReferenceGame6():CollectionReference{
        return Firebase.firestore.collection("Game2_Medium")
    }
    @Provides
    @Singleton
    @Game2HardCollection
    fun provideCollectionReferenceGame7():CollectionReference{
        return Firebase.firestore.collection("Game2_Hard")
    }


}