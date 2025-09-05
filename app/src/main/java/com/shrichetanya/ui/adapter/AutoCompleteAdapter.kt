package com.shrichetanya.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.shrichetanya.R
import com.shrichetanya.model.Client

class AutoCompleteAdapter(
    context: Context,
    private val list: List<Client>
) : ArrayAdapter<Client>(context, 0, list), Filterable {

    private var filteredList: List<Client> = list

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_customer, parent, false)
        view.findViewById<TextView>(R.id.text_view).text = filteredList[position].name
        return view
    }

    override fun getCount(): Int = filteredList.size

    override fun getItem(position: Int): Client = filteredList[position]

     override  fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val query = constraint?.toString()?.lowercase()?.trim()

                filteredList = if (query.isNullOrEmpty()) {
                    list
                } else {
                    list.filter {
                        it.name.lowercase().contains(query)
                    }
                }

                results.values = filteredList
                results.count = filteredList.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as? List<Client> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}
