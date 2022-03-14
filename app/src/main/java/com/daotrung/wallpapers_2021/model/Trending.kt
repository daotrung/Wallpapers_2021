package com.daotrung.wallpapers_2021.model

import java.io.Serializable

data class Trending(
     var cid : String ,
     var category_name : String ,
     var category_image : String ,
     var order_no : String ,
     var id : String  ,
     var cat_id : String ,
     var color_id : String ,
     var image_date : String? ,
     var image : String ,
     var is_popular : String ,
     var is_featured : String ,
     var is_premium : String
):Serializable
