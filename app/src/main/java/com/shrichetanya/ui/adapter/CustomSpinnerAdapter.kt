package com.shrichetanya.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.shrichetanya.R
import com.shrichetanya.model.Client

class CustomSpinnerAdapter( context: Context,
                            private val items: List<Client>
) : ArrayAdapter<Client>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.spinner_item, parent, false)

        val textView = view.findViewById<TextView>(R.id.text1)
        textView.text = items[position].name

        return view
    }
}