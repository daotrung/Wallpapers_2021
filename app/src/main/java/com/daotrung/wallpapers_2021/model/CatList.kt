package com.daotrung.wallpapers_2021.model

import java.io.Serializable

data class CatList(
    val images:String ,
    val is_premium : String ,
    val cat_name : String ,
    val cid : String
):Serializable
