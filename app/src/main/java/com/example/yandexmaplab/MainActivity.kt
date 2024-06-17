package com.example.yandexmaplab

import android.os.Bundle
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import android.Manifest
import android.content.pm.PackageManager
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.user_location.UserLocationLayer

var key = "d93ad095-e575-437d-b542-1d384a150555"

private lateinit var mapView: MapView
class MainActivity : AppCompatActivity() {
    private lateinit var mapObjectCollection: MapObjectCollection
    lateinit var userLocationLayer: UserLocationLayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey(key)
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapview)
        val mapKit: MapKit = MapKitFactory.getInstance()
        val jams = mapKit.createTrafficLayer(mapView.mapWindow)

        val btnCity = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val btnPost = findViewById<FloatingActionButton>(R.id.floatingActionButtonPochta)
        val toggleBtn = findViewById<ToggleButton>(R.id.toggleButton)

        btnCity.setOnClickListener {
            mapView.map.move(CameraPosition(Point(findKemerovo()[0], findKemerovo()[1]), 11.0f, 0.0f, 0.0f))
        }

        btnPost.setOnClickListener {
            mapView.map.move(CameraPosition(Point(findGlavPochta()[0], findGlavPochta()[1]), 18.0f, 0.0f, 0.0f))
        }

        toggleBtn.setOnClickListener {
            if(toggleBtn.isChecked){
                jams.isTrafficVisible = true
            } else {
                jams.isTrafficVisible = false
            }
        }

        requestPermissions()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.isVisible = true

        mapObjectCollection = mapView.map.mapObjects.addCollection()

        fun createMark(point: Point ) {
            mapObjectCollection.addPlacemark(point)
        }

        val listener = object : InputListener {
            override fun onMapTap(p0: Map, p1: Point) {
                createMark(p1)
            }

            override fun onMapLongTap(p0: Map, p1: Point) {
                createMark(p1)
            }
        }

        mapView.map.addInputListener(listener)
    }

    private fun requestPermissions(){
        if((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 0)
        }
    }

    fun findKemerovo(): DoubleArray{
        val kemerovo = doubleArrayOf(55.3333, 86.0833)
        return kemerovo
    }

    fun findGlavPochta(): DoubleArray{
        val kemerovo = doubleArrayOf(55.354993, 86.0858053)
        return kemerovo
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}