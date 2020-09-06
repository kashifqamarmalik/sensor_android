package com.example.sensor

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity(), SensorEventListener {

    lateinit var sm: SensorManager
    lateinit var stepSensor: Sensor
    private var stepCount: Int = 0
    private var isAvalaible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission

            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION), 1)
        }


        sm = getSystemService(Context.SENSOR_SERVICE) as SensorManager



        if(sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
             // There is a magnetometer.
             // You can register listener to get data and use them.
            isAvalaible = true
            stepSensor = sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
            sm.registerListener(this as SensorEventListener, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
            Log.d("A", "${stepSensor}")
            Log.d("BAC", "${sm.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)}")


        } else {
             // No magnetometer.
             // Disable related features or provide an alternative if possible?
            Log.d("C", "Sensor is not present")
             }



    }

    @Override
    override fun onResume() {
        super.onResume()

        if (isAvalaible)
            sm.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
        Log.d("X", "${sm.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)}")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }

    @Override
    override fun onPause() {
        super.onPause()

        if (isAvalaible)
            sm.unregisterListener(this, stepSensor)
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        if (sensorEvent.sensor == stepSensor) {
            stepCount = (stepCount + sensorEvent.values[0]).toInt()
            Log.d("E", "${stepCount}")
            txt.text = stepCount.toString()
        }
    }


}