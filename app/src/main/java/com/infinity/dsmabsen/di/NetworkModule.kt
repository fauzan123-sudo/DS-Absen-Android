package com.infinity.dsmabsen.di

import com.google.gson.GsonBuilder
import com.infinity.dsmabsen.helper.AuthInterceptor
import com.infinity.dsmabsen.helper.Constans
import com.infinity.dsmabsen.network.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit.Builder {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(Constans.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): AuthApi {
        return retrofitBuilder.client(okHttpClient).build().create(AuthApi::class.java)
//        return retrofitBuilder.build()
//            .create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun providesHomeAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): HomeApi {
        return retrofitBuilder.client(okHttpClient).build().create(HomeApi::class.java)
    }

    @Singleton
    @Provides
    fun attendanceHistory(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): DataAttendanceApi {
        return retrofitBuilder.client(okHttpClient).build().create(DataAttendanceApi::class.java)
    }

    @Singleton
    @Provides
    fun userProfile(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): UserProfileApi {
        return retrofitBuilder.client(okHttpClient).build().create(UserProfileApi::class.java)
    }

    @Singleton
    @Provides
    fun providesReimbursement(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): ReimbursementApi {
        return retrofitBuilder.client(okHttpClient).build().create(ReimbursementApi::class.java)
    }
    @Singleton
    @Provides
    fun providesPerizinan(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): PerizinanApi {
        return retrofitBuilder.client(okHttpClient).build().create(PerizinanApi::class.java)
    }

    @Singleton
    @Provides
    fun providesShift(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): ShiftApi {
        return retrofitBuilder.client(okHttpClient).build().create(ShiftApi::class.java)
    }

    @Singleton
    @Provides
    fun providesPengumuman(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): PengumumanApi {
        return retrofitBuilder.client(okHttpClient).build().create(PengumumanApi::class.java)
    }

    @Singleton
    @Provides
    fun providesVisit(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): VisitApi {
        return retrofitBuilder.client(okHttpClient).build().create(VisitApi::class.java)
    }

    @Singleton
    @Provides
    fun providesPassword(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): PasswordApi {
        return retrofitBuilder.client(okHttpClient).build().create(PasswordApi::class.java)
    }

    @Singleton
    @Provides
    fun providesAktivitas(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): AktivitasApi {
        return retrofitBuilder.client(okHttpClient).build().create(AktivitasApi::class.java)
    }

}