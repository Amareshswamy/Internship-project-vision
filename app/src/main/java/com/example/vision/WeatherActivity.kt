package com.example.vision


import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_weather.*
import org.json.JSONObject
import java.util.*

class WeatherActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    var api_id1 = "030314b750cc43e7b39e503dfe37150c"

    // weather url to get JSON
    var weather_url1 =
        "https://api.weatherbit.io/v2.0/current?” + “lat=” + location?.latitude +”&lon=”+ location?.longitude + “&key=”+ $api_id1";

    // api id for url

    private lateinit var textView: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        tts = TextToSpeech(this, this)
        // link the textView in which the
        // temperature will be displayed
        textView = findViewById(R.id.textView)

        // create an instance of the Fused
        // Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.e("lat", weather_url1)

        // on clicking this button function to
        // get the coordinates will be called
        btVar1.setOnClickListener {
            Log.e("lat", "onClick")
            // function to find the coordinates
            // of the last location
            obtainLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun obtainLocation() {
        val s = "getting weather info..."
        speak(s)
        textView.text = s
        Log.e("lat", "function")
        // get the last location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // get the latitude and longitude
                // and create the http URL
                weather_url1 =
                    "https://api.weatherbit.io/v2.0/current?" + "lat=" + location?.latitude + "&lon=" + location?.longitude + "&key=" + api_id1
                Log.e("lat", weather_url1.toString())
                // this function will
                // fetch data from URL
                getTemp()
            }
    }

    @SuppressLint("SetTextI18n")
    fun getTemp() {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url: String = weather_url1
        Log.e("lat", url)

        // Request a string response
        // from the provided URL.
        val stringReq = StringRequest(Request.Method.GET, url,
            { response ->
                Log.e("lat", response.toString())

                // get the JSON object
                val obj = JSONObject(response)

                // get the Array from obj of name - "data"
                val arr = obj.getJSONArray("data")
                Log.e("lat obj1", arr.toString())

                // get the JSON object from the
                // array at index position 0
                val obj2 = arr.getJSONObject(0)
                Log.e("lat obj2", obj2.toString())

                // set the temperature and the city
                // name using getString() function
                val s = obj2.getString("temp") + " deg Celsius in " + obj2.getString("city_name")
                textView.text =
                    s
                speak(s)
            },
            // In case of any error
            { textView.text = "That didn't work!"
            speak("Something went wrong!")}
        )
        queue.add(stringReq)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun speak(text: String) {

        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)

    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts!!.language = Locale.US
            tts?.speak(
                "Phone manager opened.",
                TextToSpeech.QUEUE_FLUSH, null, null
            )
        }
    }

}
