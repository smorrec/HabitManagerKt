package com.example.habitmanager.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.data.category.model.Category
import com.example.habitmanagerkt.R

class CategoryAdapter(
    context: Context,
    resource: Int,
    list: List<Category>
): ArrayAdapter<Category?>(context, resource, list) {
    private val list: ArrayList<Category>

    init {
        this.list = list as ArrayList<Category>
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder
        if (view == null) {
            viewHolder = ViewHolder()
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, null)
            viewHolder.textView = view.findViewById(R.id.categoryTextView)
            viewHolder.imageView = view.findViewById(R.id.categoryImageView)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.imageView!!.setImageResource(list[position].picture!!)
        viewHolder.textView!!.text = HabitManagerApplication.applicationContext().getString(list[position].name!!)
        return view!!
    }

    class ViewHolder {
        var textView: TextView? = null
        var imageView: ImageView? = null
    }
}