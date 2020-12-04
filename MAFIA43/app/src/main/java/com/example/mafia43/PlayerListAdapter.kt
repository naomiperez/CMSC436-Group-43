package com.example.mafia43

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TextView

class PlayerListAdapter(context: Context, resource: Int, objects: Array<Player>, themeDark: Boolean = false) :
    ArrayAdapter<Player>(context, resource, objects) {

    private var mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var mDark = themeDark

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val newView: View

        // Check for recycled View
        if (null == convertView) {

            // Not recycled. Create the View
            // Mode False = light | True = dark
            if (!mDark) {
                newView = mLayoutInflater.inflate(R.layout.player_list_item, parent, false)
            } else {
                newView = mLayoutInflater.inflate(R.layout.player_list_item_dark, parent, false)
            }

            // Cache View information in ViewHolder Object
            val viewHolder = ViewHolder()
            newView.tag = viewHolder
            viewHolder.textView = newView.findViewById(R.id.text)

        } else {
            newView = convertView
        }

        // Set the View's data


        // Retrieve the viewHolder Object
        val storedViewHolder = newView.tag as ViewHolder

        //Set the data in the data View
        storedViewHolder.textView.text = getItem(position)?.name()
        Log.i("tag", getItem(position).toString())

        return newView
    }

    // The ViewHolder class. See:
    // http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
    internal class ViewHolder {
        lateinit var textView: TextView
    }
}

