package com.example.team16_medassist.fragment

import HistoryFragment
import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.team16_medassist.R
import com.example.team16_medassist.activity.MapAPI
import com.example.team16_medassist.adaptor.RecentCasesRecyclerAdaptor
import com.example.team16_medassist.model.MapMatricesModel
import com.example.team16_medassist.viewmodel.LoginViewModel
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import com.example.team16_medassist.activity.MainActivity
import com.google.android.gms.location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY


class HomeFragment : Fragment(){

    val TAG = "HomeFragment"
    private lateinit var viewModel : LoginViewModel
    lateinit var ETA: TextView

    val REQUEST_PERMISSIONS_REQUEST_CODE = 1234


    /**
     * Location services variables
     * `locationCallback` is the callback for location request
     * `fusedLocationClient` is for getting current location
     * `location` to store the location data
     * TODO: !TURN ON LOCATION. Location of the device *must be on or Location service will not work.
     */
    private lateinit var locationCallback: LocationCallback
    lateinit var fusedLocationClient: FusedLocationProviderClient
    var location: Location? = null

    /**
     * API call variables
     * `_response` is the model class to put the json data into
     */
    lateinit var _response: MapMatricesModel

    companion object {
        private val ARG_CAUGHT = "homeFragment_caught"

        fun newInstance(inputArgs: Bundle?, context: Context): HomeFragment {
            val args = inputArgs
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }

    }


    @NonNull
    @Override
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        val homeView : View = inflater.inflate(R.layout.fragment_homepage,container,false)
        // declare recyclerView
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        val recentActivityRecycler = homeView.findViewById<RecyclerView>(R.id.caseRecyclerView)

        viewModel.getCaseLiveData().observe(viewLifecycleOwner, Observer { cases ->

            // update the number of caseHistory
            val getCaseCount = viewModel.getCaseReportCount().value.toString()
            homeView.findViewById<TextView>(R.id.textViewHistoryNum).text = getCaseCount
            var numPendingReports = 0
            for (i in cases.indices){
                if (cases[i].getCaseStatus().toString()=="Pending"){
                    numPendingReports+= 1
                }
            }
            homeView.findViewById<TextView>(R.id.textViewReportNum).text = numPendingReports.toString()
            // update the latest case assigned
            val caseId = viewModel.getCases().value!!.getCaseId()
            val caseStatus = viewModel.getCases().value!!.getCaseStatus()
            homeView.findViewById<TextView>(R.id.textViewCaseID).text = caseId.toString()
            homeView.findViewById<TextView>(R.id.textViewStatus).text = caseStatus
            //ETA codes
            ETA = homeView.findViewById(R.id.ETATextview)
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            /**
             *  Start getting location data, ask for location permission.
             *  locationCallback to constantly get location data.
             *  getTimeToMedicalCentre takes in the latitude and longitude
             *  and pass to the api call to get ETA.
             *  Gets new location data every 1 min.
             *
             *  `getCurrentLocation()` needs to run first to kick start the `onLocationResult`
             */
            getCurrentLocation()
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    for (location in locationResult.locations) {
                        //Toast.makeText(context, "${location}", Toast.LENGTH_SHORT).show()
                        getTimeToMedicalCentre(location.latitude, location.longitude)
                        Log.d(TAG, "onLocationResult")
                    }
                }
            }

            val drawable = homeView.findViewById<ImageView>(R.id.imageViewStatus)
            // logic for the changing of status
            if (caseStatus.toString()=="Pending"){
                homeView.findViewById<TextView>(R.id.textViewStatus).setTextColor(Color.parseColor("#FFA000"))
                drawable.setColorFilter(ContextCompat.getColor(homeView.context,R.color.orange))
            } else if (caseStatus.toString()=="Closed"){
                homeView.findViewById<TextView>(R.id.textViewStatus).setTextColor(Color.parseColor("#FF1700"))
                drawable.setColorFilter(ContextCompat.getColor(homeView.context,R.color.red))
            } else {
                homeView.findViewById<TextView>(R.id.textViewStatus).setTextColor(Color.parseColor("#4BCC89"))
                drawable.setColorFilter(ContextCompat.getColor(homeView.context,R.color.green))
            }

            // update the recyclerview
            val recyclerViewAdapter = RecentCasesRecyclerAdaptor(cases, homeView.context)
            recentActivityRecycler.layoutManager = LinearLayoutManager(activity)
            recentActivityRecycler.adapter = recyclerViewAdapter
            recyclerViewAdapter.notifyDataSetChanged()
        })


        // buttonViewHistory onClick logic
        homeView.findViewById<Button>(R.id.buttonViewHistory).setOnClickListener(){
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.contentFrame, HistoryFragment())
            transaction.disallowAddToBackStack()
            transaction.commit()
        }


        homeView.findViewById<Button>(R.id.buttonViewReport).setOnClickListener(){
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.contentFrame, ReportFragment())
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction.commit()
        }
        return homeView


    }

    /**
     *  Function to do api call to google distance matrix api to get
     *  estimated time from one location(origin) to another(destination).
     *  get the params from locationCallback.
     *  @param latitude
     *  @param longitude
     */
    private fun getTimeToMedicalCentre(latitude: Double, longitude: Double) {
        val units = "imperial" // units of measurement for the map data
        val key =
                "AIzaSyA9G3NuKvhxjDwWAtIohG1UUhFGp0nIQKw"// Only 300 request. Free account, if suddenly error... probably no more request

        //Toast.makeText(applicationContext, "BEFORE", Toast.LENGTH_SHORT).show()
        //val origins = "${location!!.latitude},${location!!.longitude}"
        /**
         * origins is the current location of the phone.
         */
        val origins = "$latitude,$longitude"
        //Toast.makeText(context, "${location?.latitude}", Toast.LENGTH_LONG).show()

        /**
         * destinations is the location of the medical centre.
         */
        // TODO REPLACE WITH THE REAL DESTINATION
        val destinations =
                "1.3342207508811255,103.89363338370808" // latitude , longitude of the destination

        /**
         * Calls the interface which contains the api request
         */
        // TODO: PUT THIS INTO REPOSITORY, REPO WILL CALL THE API AND PASS UP TEH VIEW MODEL.
        MapAPI.retrofitService.getMapMatrices(units, origins, destinations, key).enqueue(
                object : Callback<MapMatricesModel> {
                    override fun onResponse(
                            call: Call<MapMatricesModel>?,
                            response: Response<MapMatricesModel>?
                    ) {
                        if (response != null) {
                            /**
                             * get response and parse the json response.
                             * Update UI to show the time.
                             */
                            _response = response.body()
                            //Toast.makeText(applicationContext, "OK", Toast.LENGTH_SHORT).show()

                            /**
                             * Paramedic activity will push the ETA time to database and the doctor
                             * activity will get from database.
                             * TODO push `_response.rows[0].elements[0].duration.text` into DB
                             */
                            ETA.text = _response.rows[0].elements[0].duration.text // Get just the time
                            Toast.makeText(context, "ETA Updated.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    /**
                     * if api call fail, update UI with the error message.
                     */
                    override fun onFailure(call: Call<MapMatricesModel>?, t: Throwable?) {
                        ETA.text = t.toString()
                        //Toast.makeText(context, "$t", Toast.LENGTH_SHORT).show()

                    }
                })
    }

    /**
     * Function to get the current location of the phone.
     * Checks for location permissions. if location permissions not
     * allowed prompt user to allow.
     * This function only called once at onCreate, after that
     * startLocationUpdates() will get the location every 1 min
     */
    private fun getCurrentLocation() {

        if (context?.let {
                    ActivityCompat.checkSelfPermission(
                            it,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    )
                } != PackageManager.PERMISSION_GRANTED && context?.let {
                    ActivityCompat.checkSelfPermission(
                            it,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                } != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSIONS_REQUEST_CODE
            )
            return
        }
        /**
         * get the current location with high accuracy.
         * when the location is available and is not null, log the location coordinates
         * can get the location data from `location` and do anything with it.
         *
         */
        fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)!!
                .addOnCompleteListener(MainActivity()) { task ->
                    if (task.result != null) {


                        location = task.result
                        Log.d(
                                TAG,
                                "latitude: ${location!!.latitude}, longitude: ${location!!.longitude}"
                        )
                        //getTimeToMedicalCentre()
                        //mGpsText!!.text = "LAT: " + (location)!!.latitude.toString() + " LONG: " +  (location)!!.longitude.toString()
                    } else {
                        Toast.makeText(context, "Location Service Error", Toast.LENGTH_SHORT).show()
                    }
                }
        return
    }

    /**
     * function to check if location permission is allowed
     * @return boolean if permission is allowed
     */
    fun checkLocationPermission(): Boolean {
        val permission = context?.let {
            ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        return permission == PackageManager.PERMISSION_GRANTED
    }

    /**
     * a function to get location data at every interval
     */
    private fun startLocationUpdates() {
        /**
         * checks permissions, if no permissions prompt user to allow
         */
        if (context?.let {
                    ActivityCompat.checkSelfPermission(
                            it,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    )
                } != PackageManager.PERMISSION_GRANTED && context?.let {
                    ActivityCompat.checkSelfPermission(
                            it,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                } != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSIONS_REQUEST_CODE
            )
            return
        }

        /**
         * The location request to request a location data at every interval(ms)
         * at high location accuracy.
         * fastestInterval is the fastest the app can get a location data(for when app needs more
         * regular location updates than normal intervals)
         */
        val locationRequest = LocationRequest.create().apply {
            interval = 60000
            //fastestInterval = 10000
            priority = PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
        )
    }

    /**
     * stop the location update request
     */
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d(TAG, "stopLocationUpdates")
    }

    /**
     * onResume of the activity start the location requests
     */
    override fun onResume() {
        super.onResume()
        if (checkLocationPermission()) startLocationUpdates()
        Toast.makeText(context, "Location updates started", Toast.LENGTH_SHORT).show()
    }

    /**
     * onPause of the activity stop the location request
     */
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        Log.d(TAG, "onPause")
    }

}