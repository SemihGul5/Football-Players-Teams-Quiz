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

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game3EasyCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game3MediumCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game3HardCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game4EasyCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game4MediumCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game4HardCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game5EasyCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game5MediumCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game5HardCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game6EasyCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game6MediumCollection

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Game6HardCollection


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
                              @Game3EasyCollection collectionReferenceGame3Easy: CollectionReference,
                              @Game3MediumCollection collectionReferenceGame3Medium: CollectionReference,
                              @Game3HardCollection collectionReferenceGame3Hard: CollectionReference,
                              @Game4EasyCollection collectionReferenceGame4Easy: CollectionReference,
                              @Game4MediumCollection collectionReferenceGame4Medium: CollectionReference,
                              @Game4HardCollection collectionReferenceGame4Hard: CollectionReference,
                              @Game5EasyCollection collectionReferenceGame5Easy: CollectionReference,
                              @Game5MediumCollection collectionReferenceGame5Medium: CollectionReference,
                              @Game5HardCollection collectionReferenceGame5Hard: CollectionReference,
                              @Game6EasyCollection collectionReferenceGame6Easy: CollectionReference,
                              @Game6MediumCollection collectionReferenceGame6Medium: CollectionReference,
                              @Game6HardCollection collectionReferenceGame6Hard: CollectionReference,
                              ):GameDataSource{
        return GameDataSource(dao,collectionReferenceGame1Easy,collectionReferenceGame1Medium,collectionReferenceGame1Hard,
            collectionReferenceGame2Easy,collectionReferenceGame2Medium, collectionReferenceGame2Hard,
            collectionReferenceGame3Easy,collectionReferenceGame3Medium,collectionReferenceGame3Hard,
            collectionReferenceGame4Easy,collectionReferenceGame4Medium, collectionReferenceGame4Hard,
            collectionReferenceGame5Easy,collectionReferenceGame5Medium,collectionReferenceGame5Hard,
            collectionReferenceGame6Easy,collectionReferenceGame6Medium, collectionReferenceGame6Hard)
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
    fun provideCollectionReferenceGame1Easy():CollectionReference{
        return Firebase.firestore.collection("Game1_Easy")
    }
    @Provides
    @Singleton
    @Game1MediumCollection
    fun provideCollectionReferenceGame1Medium():CollectionReference{
        return Firebase.firestore.collection("Game1_Medium")
    }
    @Provides
    @Singleton
    @Game1HardCollection
    fun provideCollectionReferenceGame1Hard():CollectionReference{
        return Firebase.firestore.collection("Game1_Hard")
    }
    @Provides
    @Singleton
    @Game2EasyCollection
    fun provideCollectionReferenceGame2Easy():CollectionReference{
        return Firebase.firestore.collection("Game2_Easy")
    }
    @Provides
    @Singleton
    @Game2MediumCollection
    fun provideCollectionReferenceGame2Medium():CollectionReference{
        return Firebase.firestore.collection("Game2_Medium")
    }
    @Provides
    @Singleton
    @Game2HardCollection
    fun provideCollectionReferenceGame2Hard():CollectionReference{
        return Firebase.firestore.collection("Game2_Hard")
    }
    //
    @Provides
    @Singleton
    @Game3EasyCollection
    fun provideCollectionReferenceGame3Easy():CollectionReference{
        return Firebase.firestore.collection("Game3_Easy")
    }
    @Provides
    @Singleton
    @Game3MediumCollection
    fun provideCollectionReferenceGame3Medium():CollectionReference{
        return Firebase.firestore.collection("Game3_Medium")
    }
    @Provides
    @Singleton
    @Game3HardCollection
    fun provideCollectionReferenceGame3Hard():CollectionReference{
        return Firebase.firestore.collection("Game3_Hard")
    }
    //
    @Provides
    @Singleton
    @Game4EasyCollection
    fun provideCollectionReferenceGame4Easy():CollectionReference{
        return Firebase.firestore.collection("Game4_Easy")
    }
    @Provides
    @Singleton
    @Game4MediumCollection
    fun provideCollectionReferenceGame4Medium():CollectionReference{
        return Firebase.firestore.collection("Game4_Medium")
    }
    @Provides
    @Singleton
    @Game4HardCollection
    fun provideCollectionReferenceGame4Hard():CollectionReference{
        return Firebase.firestore.collection("Game4_Hard")
    }
    //
    @Provides
    @Singleton
    @Game5EasyCollection
    fun provideCollectionReferenceGame5Easy():CollectionReference{
        return Firebase.firestore.collection("Game5_Easy")
    }
    @Provides
    @Singleton
    @Game5MediumCollection
    fun provideCollectionReferenceGame5Medium():CollectionReference{
        return Firebase.firestore.collection("Game5_Medium")
    }
    @Provides
    @Singleton
    @Game5HardCollection
    fun provideCollectionReferenceGame5Hard():CollectionReference{
        return Firebase.firestore.collection("Game5_Hard")
    }
    //
    @Provides
    @Singleton
    @Game6EasyCollection
    fun provideCollectionReferenceGame6Easy():CollectionReference{
        return Firebase.firestore.collection("Game6_Easy")
    }
    @Provides
    @Singleton
    @Game6MediumCollection
    fun provideCollectionReferenceGame6Medium():CollectionReference{
        return Firebase.firestore.collection("Game6_Medium")
    }
    @Provides
    @Singleton
    @Game6HardCollection
    fun provideCollectionReferenceGame6Hard():CollectionReference{
        return Firebase.firestore.collection("Game6_Hard")
    }

}