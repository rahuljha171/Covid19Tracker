package com.rahul.covid19tracker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.AbsListView
import android.widget.ListView
import android.widget.TextView
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var lastupdateTV: TextView
    private lateinit var confirmedTv: TextView
    private lateinit var activeTv: TextView
    private lateinit var deceasedTv: TextView
    private lateinit var recoveredTv: TextView
    private lateinit var list: ListView
    lateinit var stateListAdapter: StateListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list = findViewById(R.id.list)
        lastupdateTV =findViewById(R.id.lastupdateTV)
        confirmedTv = findViewById(R.id.confirmedTv )
        activeTv = findViewById(R.id.activeTv)
        deceasedTv = findViewById(R.id.deceasedTv)
        recoveredTv = findViewById(R.id.recoveredTv)

        list.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_header, list, false))
        fetchResult()


    }




    private fun fetchResult() {
        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) { Client.api.execute() }
            if (response.isSuccessful)
            {
                val data = Gson().fromJson(response.body?.string(),Response::class.java)
                launch (Dispatchers.Main){
                    bindCombinedData(data.statewise[0])
                    bindStateWiseData(data.statewise.subList(0, data.statewise.size))
                }
            }
        }
    }

    private fun bindStateWiseData(subList: List<StatewiseItem>) {
        stateListAdapter = StateListAdapter(subList)
        list.adapter = stateListAdapter

    }
    private fun bindCombinedData(data: StatewiseItem) {
        val lastupdatedtime = data.lastupdatedtime
        val simpleDateFormat = SimpleDateFormat("dd/mm/yyyy hh:mm:ss")
        lastupdateTV.text = "Last Updated\n ${getTimeAgo(simpleDateFormat.parse(lastupdatedtime))}"
        confirmedTv.text = data.confirmed
        activeTv.text = data.active
        recoveredTv.text = data.recovered
        deceasedTv.text = data.deaths
    }
    }

    fun getTimeAgo(past: Date): String {
        val now = Date()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)

        return when {
            seconds < 60 -> {
                "Few seconds ago"
            }
            minutes < 60 -> {
                "$minutes minutes ago"
            }
            hours < 24 -> {
                "$hours hour ${minutes % 60} min ago"
            }
            else -> {
                SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(past).toString()
            }
        }
}
