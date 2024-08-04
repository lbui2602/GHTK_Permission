package com.example.ghtk_permission

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ghtk_permission.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.btnStart.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            }
        }
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    binding.ll2.visibility = View.VISIBLE
                    val latitude = location.latitude
                    val longitude = location.longitude
                    binding.tvLat.text="Lat: "+latitude.toString()
                    binding.tvLong.text="Long: "+longitude.toString()
                    binding.tvStatus.text = "Granted"
                    binding.tvStatus.setTextColor(Color.GREEN)
                    binding.btnStart.setBackgroundColor(Color.GREEN)
                } else {
                    Log.d("MainActivity", "Location is null")
                }
            }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation()
            } else {
                // Quyền truy cập bị từ chối
                Log.d("MainActivity", "Permission denied")
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}