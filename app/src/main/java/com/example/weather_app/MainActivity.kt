package com.example.weather_app

import android.net.wifi.rtt.CivicLocationKeys.CITY
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    val our_city: String = "Jeddah,sa"
    val ourAPI: String = "49d97d9bcbf94e07de74d6373b00d2a1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherCity().execute()

    }

    inner class weatherCity() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainCol).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE


        }//----------------

        override fun doInBackground(vararg params: String?): String? {

            var response: String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$our_city&units=metric&appid=$ourAPI").readText(Charsets.UTF_8)
            } catch (e: Exception) {

                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {

            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "تم التحيث في : "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                val tem = main.getString("temp")+"°C"
                val temmin = "الصغرى : " + main.getString("temp_min")+"°C"
                val temmax = "العضمى : " + main.getString("temp_max")+"°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")

                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val weatherDes = weather.getString("description")
                val address = jsonObj.getString("name") + ", " + sys.getString("country")
//---------------
                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updateat).text = updatedAtText

             //   findViewById<TextView>(R.id.states).text = weatherDes.capitalize()
                findViewById<TextView>(R.id.tem).text = tem
                findViewById<TextView>(R.id.temmin).text = temmin
                findViewById<TextView>(R.id.temmax).text = temmax

                findViewById<TextView>(R.id.TV1_shoroq).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
                findViewById<TextView>(R.id.TV2_ghorob).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))
                findViewById<TextView>(R.id.TV3).text = windSpeed
                findViewById<TextView>(R.id.TV4).text = pressure
                findViewById<TextView>(R.id.TV5).text = humidity

                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainCol).visibility = View.VISIBLE
            } catch (e: Exception) {

                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE

            }
        }
    }
}