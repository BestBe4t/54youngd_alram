package com.example.jotso.data

class list_item(
    num:Int,
    title:String
){
    private var Num:Int
    private var Title:String

    init {
        Num = num
        Title = title
    }

    fun get_num():Int{
        return if(Num==null) 1 else Num
    }

    fun get_title():String{
        return if(Title==null) "Test Title" else Title
    }
}