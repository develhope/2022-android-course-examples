package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val cityName: TextView

    init {
        cityName = view.findViewById(R.id.city_name)
    }
}

class CityAdapter(val cities: List<City>) : RecyclerView.Adapter<CityViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val cityView =
            LayoutInflater.from(parent.context).inflate(R.layout.citylistitem, parent, false)
        return CityViewHolder(cityView)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.cityName.text = cities[position].name
    }

    override fun getItemCount(): Int {
        return cities.size
    }
}