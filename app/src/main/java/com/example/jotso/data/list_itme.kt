package com.example.jotso.data

class list_item(
    num:Int,
    title:String,
    content:String
){
    private var Num:Int
    private var Title:String
    private var Content:String

    init {
        Num = num
        Title = title
        Content = content
    }

    fun get_num():Int{
        return if(Num==null) 1 else Num
    }

    fun get_title():String{
        return if(Title==null) "Test Title" else Title
    }

    fun get_Content():String{
        return if(Content==null) "Test Content" else Content
    }
}