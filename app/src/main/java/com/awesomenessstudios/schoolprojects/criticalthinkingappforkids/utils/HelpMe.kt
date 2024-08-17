package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids.utils

import kotlin.random.Random
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import java.util.Locale


object HelpMe {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    val TAG = "HelpMe"
    fun initialize(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission") // Ensure permissions are checked before calling this
    fun getCurrentAddress(context: Context, callback: (String?) -> Unit) {

        fusedLocationClient.lastLocation.addOnCompleteListener {
            Log.d(TAG, "getCurrentAddress: ${it}")
        }
        fusedLocationClient.lastLocation.addOnCompleteListener(OnCompleteListener<Location> { task ->
            if (task.isSuccessful && task.result != null) {
                val location = task.result
               // Log.d(TAG, "getCurrentAddress: $location")
                getAddressFromLocation(context, location, callback)
            } else {
                callback(null) // Failed to get location
            }
        })
    }

    private fun getAddressFromLocation(
        context: Context,
        location: Location,
        callback: (String?) -> Unit
    ) {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses: List<Address>? =
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            Log.d(TAG, "getAddressFromLocation: $addresses")
            if (addresses != null && addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                val addressLine = address.getAddressLine(0) ?: "Unknown Location"
                callback(addressLine)
            } else {
                callback(null) // No address found
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callback(null) // Error occurred
        }
    }

    /*  private fun requestLocationPermissions(context: Context, activity: Activity) {
          //Request permissions
          Dexter.withActivity(activity) //Dexter makes runtime permission easier to implement
              .withPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
              .withListener(object : PermissionListener {
                  override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                      getCurrentLocation(context, activity, callBack = {})
                  }

                  override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                      activity.toast("Accept Permission")
                  }

                  override fun onPermissionRationaleShouldBeShown(
                      permission: com.karumi.dexter.listener.PermissionRequest?,
                      token: PermissionToken?
                  ) {
                      TODO("Not yet implemented")
                  }
              }).check()
          //Request permissions
          Dexter.withActivity(activity) //Dexter makes runtime permission easier to implement
              .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
              .withListener(object : PermissionListener {
                  override fun onPermissionGranted(response: PermissionGrantedResponse?) {

                  }

                  override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                      context.toast("Accept Permission")
                  }

                  override fun onPermissionRationaleShouldBeShown(
                      permission: com.karumi.dexter.listener.PermissionRequest?,
                      token: PermissionToken?
                  ) {
                      TODO("Not yet implemented")
                  }
              }).check()

      }

      fun getCurrentLocation(context: Context, activity: Activity, callBack: (location: String) -> Unit) {
          val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

          var currentLocation = "No Address Found!"

          if (ActivityCompat.checkSelfPermission(
                  context,
                  Manifest.permission.ACCESS_FINE_LOCATION
              ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                  context,
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
              requestLocationPermissions(context, activity )
          }
          fusedLocationClient.lastLocation.addOnCompleteListener(activity) { task ->
              val location: Location? = task.result


              val geocoder = Geocoder(context, Locale.getDefault())
              val list: List<Address> =
                  geocoder.getFromLocation(location!!.latitude, location.longitude, 1)!!

              //mUsageLocality = "Locality\n${list[0].locality}"
              currentLocation = list[0].subLocality ?: "No Address Found!"// .getAddressLine(0)
              callBack(currentLocation)

          }
      }

  */
}