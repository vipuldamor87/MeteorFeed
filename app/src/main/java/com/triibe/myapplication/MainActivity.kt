package com.triibe.myapplication

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.triibe.myapplication.databinding.ActivityMainBinding
import com.triibe.myapplication.network.ApiService
import com.triibe.myapplication.network.Repository
import com.triibe.myapplication.utils.TimeStamp
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityMainBinding
    private lateinit var  viewModel :MainViewModel
    private val apiService = ApiService.getInstance()
    var barEntriesArrayList = arrayListOf<BarEntry>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
//        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, MyViewModelFactory(Repository(apiService))).get(
            MainViewModel::class.java)

        binding.tvstart.setOnClickListener{
            openCalender(binding.tvstart)
        }
        binding.tvEnd.setOnClickListener{
            openCalender(binding.tvEnd)
        }
        binding.btnSearch.setOnClickListener{
            if(binding.tvstart.text.toString().isNullOrEmpty()&&binding.tvEnd.text.toString().isNullOrEmpty()){
                Toast.makeText(this,"Please Select Date",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            viewModel.getFeed(binding.tvstart.text.toString(),binding.tvEnd.text.toString())
        }

        viewModel.errorMessage.observe(this){
            Toast.makeText(this,"$it",Toast.LENGTH_LONG).show()
        }
        viewModel.newsList.observe(this){
            var fastestkmHR:Double = 0.0
            var closestKm:Double = Double.POSITIVE_INFINITY
            var SizeSum:Double = 0.0
            var  count = 0
           var fastestString = ""
            var closestString = ""
            var average = ""
            var barCount = 1
            it.near_earth_objects.forEach {
                barEntriesArrayList.add(BarEntry(barCount.toFloat(),it.value.size.toFloat(),it.key))
                barCount++
                it.value.forEach {
                    if(fastestkmHR<it.close_approach_data[0].relative_velocity.kilometers_per_hour.toDouble()){
                        fastestkmHR = it.close_approach_data[0].relative_velocity.kilometers_per_hour.toDouble()
                        fastestString = "Fasted Id: ${it.id} \n speed: $fastestkmHR "
                    }
                    if(closestKm>it.close_approach_data[0].miss_distance.kilometers.toDouble()){
                        closestKm = it.close_approach_data[0].miss_distance.kilometers.toDouble()
                        closestString = "Closest Id: ${it.id} \n speed: $closestKm "
                    }
                    SizeSum = SizeSum+((it.estimated_diameter.kilometers.estimated_diameter_max.toDouble()+it.estimated_diameter.kilometers.estimated_diameter_min.toDouble())/2)
                    count++
                }
            }
            binding.tvFastest.text = fastestString
            binding.tvClosest.text = closestString
            binding.averageSize.text = "Average Size: " + (SizeSum/count.toDouble()).toString()

            var barDataSet = BarDataSet(barEntriesArrayList, "Vipul Damor")
            var barData =  BarData(barDataSet);

//            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

            // setting text color.
            barDataSet.setValueTextColor(Color.BLACK);
            val xLabel: ArrayList<String> = ArrayList()
            barEntriesArrayList.forEach {
                xLabel.add(it.data.toString())
            }


            // setting text size
            barDataSet.setValueTextSize(16f);
            val xAxis: XAxis = binding.idBarChart.getXAxis()
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            val xAxisFormatter: ValueFormatter = DayAxisValueFormatter(binding.idBarChart,xLabel)

            xAxis.setValueFormatter( xAxisFormatter)
            binding.idBarChart.data = barData
            binding.idBarChart.getDescription().setEnabled(false);
            binding.idBarChart.invalidate()


        }
    }
    class DayAxisValueFormatter(private val chart: BarLineChartBase<*>, val xLabel: ArrayList<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            if(value<xLabel.size&&xLabel[value.toInt()]!=null){
                return xLabel[value.toInt()]
            }else{
                return value.toString()
            }
        }
    }

    private fun openCalender(textView: TextView) {
        val calc = Calendar.getInstance()
        calc.timeInMillis = System.currentTimeMillis()

//        if (textView.getValue().isNotEmpty()) {
//            val millis =
//                TimeStamp.formatToSeconds(
//                    textView.text.toString(),
//                    TimeStamp.DATE_FORMAT_YYYYMMDD
//                )
//            calc.timeInMillis = millis * 1000
//        }

        val dialog = DatePickerDialog(
            this,
            { view, year, month, dayOfMonth ->
//                textView.text = "$year-${String.format("%02d",month+1)}-${String.format("%02d",dayOfMonth)}"
                calc.set(Calendar.YEAR, year)
                calc.set(Calendar.MONTH, month)
                calc.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val millis = calc.timeInMillis
                textView.tag = millis
                textView.text = TimeStamp.millisToFormat(
                    millis,
                    TimeStamp.DATE_FORMAT
                )
            },
            calc.get(Calendar.YEAR),
            calc.get(Calendar.MONTH),
            calc.get(Calendar.DAY_OF_MONTH)
        )

//        dialog.datePicker.maxDate = System.currentTimeMillis()
//        dialog.datePicker.minDate = System.currentTimeMillis()
        dialog.show()
    }

}