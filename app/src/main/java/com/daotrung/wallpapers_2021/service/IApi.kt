package com.daotrung.wallpapers_2021.service


import com.daotrung.wallpapers_2021.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "http://admin.apphicsamoled.xyz/"
private const val BASE_URL_2 = "http://hdwalls.wallzapps.com/"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit =
    Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(BASE_URL)
        .build()
private val retrofit_2 =
    Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(BASE_URL_2)
        .build()

interface ApiInterface {
    @GET("api/category/all/p2pqlsnjHgdxX21GFRAYyLvNBe3zcsSz/16edd7cf-2525-485e-b11a-3dd35f382457/")
    fun getALLData(): Call<List<WallPaper>>

    @GET("api/wallpaper/category/0/{id}/p2pqlsnjHgdxX21GFRAYyLvNBe3zcsSz/16edd7cf-2525-485e-b11a-3dd35f382457/")
    fun getSlideDataById(@Path("id") id: Int): Call<List<SlideLiveWapaper>>

    @GET("api.php?popular&page=1")
    fun getAllDataPopularTrending(): Call<MaterialWapaper>

    @GET("api.php?featured&page=1")
    fun getAllDataFeaturedTrending(): Call<MaterialWapaper>

    @GET("api.php?latest&page=1")
    fun getAllDataNewTrending(): Call<MaterialWapaper>

    @GET("api.php")
    fun getAllDataCategoriesMainWallpaper(): Call<MaterialWallpaperCateMain>

    @GET("api.php")
    fun getAllDataCategoriesListWallpaper(
        @Query("cat_id") id: Int,
        @Query("page") page: Int = 1
    ): Call<MaterialWallpaperCatList>

    @GET("api.php?color=Space")
    fun getColorModel(): Call<MaterialWallpaperColorMain>

    @GET("api.php")
    fun getAllSliderListColor(
        @Query("color_id") id: Int,
        @Query("page") page: Int = 1
    ): Call<MaterialWallpaperCatList>

    object Api {
        val retrofitService: ApiInterface by lazy {
            retrofit.create(ApiInterface::class.java)
        }
    }

    object Api2 {
        val retrofitService2: ApiInterface by lazy {
            retrofit_2.create(ApiInterface::class.java)
        }
    }

}