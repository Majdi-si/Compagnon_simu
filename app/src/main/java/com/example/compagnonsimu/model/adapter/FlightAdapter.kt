package com.example.compagnonsimu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.compagnonsimu.R
import com.example.compagnonsimu.model.Flight
import com.example.compagnonsimu.model.Taxibot

class FlightAdapter(private val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Flight -> if ((items[position] as Flight).Dep == "LFPG") TYPE_DEPARTURE else TYPE_ARRIVAL
            is Taxibot -> TYPE_TAXIBOT
            else -> throw IllegalArgumentException("Invalid type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DEPARTURE -> DepartureViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_departure, parent, false)
            )
            TYPE_ARRIVAL -> ArrivalViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_arrival, parent, false)
            )
            else -> TaxibotViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_taxibot, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DepartureViewHolder -> holder.bind(items[position] as Flight)
            is ArrivalViewHolder -> holder.bind(items[position] as Flight)
            is TaxibotViewHolder -> holder.bind(items[position] as Taxibot)
        }
    }

    override fun getItemCount() = items.size

    class DepartureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(flight: Flight) {
            itemView.findViewById<TextView>(R.id.callsign).text = flight.CallSign
            itemView.findViewById<TextView>(R.id.type).text = flight.AircraftType
            itemView.findViewById<TextView>(R.id.departure).text = flight.Dep
            itemView.findViewById<TextView>(R.id.arrival).text = flight.Arr
        }
    }

    class ArrivalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(flight: Flight) {
            itemView.findViewById<TextView>(R.id.callsign).text = flight.CallSign
            itemView.findViewById<TextView>(R.id.type).text = flight.AircraftType
            itemView.findViewById<TextView>(R.id.eta).text = flight.Tobt
        }
    }

    class TaxibotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(taxibot: Taxibot) {
            itemView.findViewById<TextView>(R.id.tb_callsign).text = "Taxibot ${taxibot.CallSign}"
            itemView.findViewById<TextView>(R.id.tb_missions).text =
                "Missions: ${taxibot.Missions.joinToString(", ")}"
        }
    }

    companion object {
        private const val TYPE_DEPARTURE = 0
        private const val TYPE_ARRIVAL = 1
        private const val TYPE_TAXIBOT = 2
    }
}