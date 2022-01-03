package com.example.cryptocurrencytrackingapprxjava

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class RecyclerViewAdapter(private var list: ArrayList<CryptoModel>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(),Filterable {


     var filteredList = ArrayList<CryptoModel>()

     init {
         filteredList = list
     }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nameText : TextView = itemView.findViewById(R.id.nameText)
        val priceText : TextView = itemView.findViewById(R.id.priceText)
        //val logoImg : ImageView = itemView.findViewById(R.id.logoImageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent,false)

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = filteredList[position]

        holder.nameText.text = itemsViewModel.currency
        holder.priceText.text = itemsViewModel.price

      //  Picasso.get().load(itemsViewModel.logo_url).into(holder.logoImg)
    }

     override fun getFilter(): Filter {
         return object : Filter() {
             override fun performFiltering(constraint: CharSequence?): FilterResults {
                 val charSearch = constraint.toString()
                 filteredList = if (charSearch.isEmpty()) {
                     list
                 } else {
                     val resultList = ArrayList<CryptoModel>()
                     for (row in list) {
                         if (row.currency.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                             resultList.add(row)
                         }
                     }
                     resultList
                 }
                 val filterResults = FilterResults()
                 filterResults.values = filteredList
                 return filterResults
             }

             @SuppressLint("NotifyDataSetChanged")
             @Suppress("UNCHECKED_CAST")
             override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                 filteredList = results?.values as ArrayList<CryptoModel>
                 notifyDataSetChanged()
             }

         }
     }

}