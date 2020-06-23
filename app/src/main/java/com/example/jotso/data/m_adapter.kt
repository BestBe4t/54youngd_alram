package com.example.jotso.data

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.jotso.R

class m_adapter(context: Context, data:ArrayList<list_item>): BaseAdapter() {

    private var mContext:Context? = null
    private var list:ArrayList<list_item>
    private var mLayoutInflater:LayoutInflater? = null

    init {
        mContext = context
        list = data
        mLayoutInflater = LayoutInflater.from(mContext)
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = mLayoutInflater!!.inflate(R.layout.list_item, null)

        val num = view.findViewById<TextView>(R.id.Num)
        val title = view.findViewById<TextView>(R.id.Title)

        try {
        num.setText(list.get(position).get_num()!!.toString())
        title.setText(list.get(position).get_title()!!)

        return view
        }catch (e: Resources.NotFoundException){
            return view
        }
    }
}