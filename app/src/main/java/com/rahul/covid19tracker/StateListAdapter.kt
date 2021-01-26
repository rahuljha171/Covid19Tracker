package com.rahul.covid19tracker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView



class StateListAdapter(val list: List<StatewiseItem>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list, parent, false)
        val item = list[position]
        val confirmedTv : TextView = view.findViewById(R.id.confirmedTv)

        val activeTv : TextView = view.findViewById(R.id.activeTv)

        val recoveredTv : TextView =view.findViewById(R.id.recoveredTv)
        val deceasedTv : TextView = view.findViewById(R.id.deceasedTv)
        val stateTv : TextView = view.findViewById(R.id.stateTv)




       confirmedTv.text = SpannableDelta(
                    "${item.confirmed}\n ↑ ${item.deltaconfirmed ?: "0"}",
                    "#D32F2F",
                    item.confirmed?.length ?: 0
             )

        activeTv.text = SpannableDelta(
                "${item.active}\n ↑ ${item.active?: "0"}",
                "#1976D2",
                item.confirmed?.length ?: 0
        )
        recoveredTv.text = SpannableDelta(
                "${item.recovered}\n ↑ ${item.deltarecovered ?: "0"}",
                "#388E3C",
                item.recovered?.length ?: 0
        )
        deceasedTv.text = SpannableDelta(
                "${item.deaths}\n ↑ ${item.deltadeaths ?: "0"}",
                "#FBC02D",
                item.deaths?.length ?: 0
        )
        stateTv.text = item.state
        return view
    }

    override fun getItem(position: Int) = list[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount(): Int = list.size

}