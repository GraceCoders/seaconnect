package com.sea.seaconnect.view.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sea.seaconnect.R
import com.sea.seaconnect.controller.Utills.AppPreferences
import com.sea.seaconnect.controller.Utills.BottomSheetDialog
import com.sea.seaconnect.controller.Utills.Gps
import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.controller.utills.SnackbarUtil
import com.sea.seaconnect.model.ResponseModel.HomeApiRespone.HomeApiResponse
import com.sea.seaconnect.model.ResponseModel.HomeApiRespone.InteractionData
import com.sea.seaconnect.viewModel.HomeViewModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.custom_navigation_bar_user.*
import java.lang.reflect.Type


class MainActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {
    private var mMap: GoogleMap? = null
    var mName: String? = null
    var mProfilePic: String? = null
    var getName: ArrayList<String> = ArrayList()
    var getId: ArrayList<String> = ArrayList()
    var homeApiData: ArrayList<InteractionData> = ArrayList()
var showUserID=""
    var interestText: String? = ""
    var statusCode: Int? = null
    private var viewModel: HomeViewModel? = null
    var gps: Gps? = null
    var current_lat: Double? = null
    var current_long: Double? = null
    var auth_token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        auth_token = AppPreferences.init(this).getString(Constants.TOKEN)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            val mFcmToken = it.token
            viewModel?.savFcmToken(auth_token, mFcmToken)
            Log.e("TAG", ": " + mFcmToken)
        }
        setData()
        getDataForInterestName()
        getDataForInterestId()
        setUpMapIfNeeded()

        getLocations()
        getHomeApiData()
        getHomeApiData()
        iv_nav_bar.setOnClickListener(this)
        iv_filter.setOnClickListener(this)
        nav_close.setOnClickListener(this)
        ll_edit_profile.setOnClickListener(this)
        ll_notifications.setOnClickListener(this)
        icon_notification.setOnClickListener(this)
        ll_connection.setOnClickListener(this)
        ll_reviews.setOnClickListener(this)
        ll_logout.setOnClickListener(this)
        ll_subscription.setOnClickListener(this)
        ll_privacy.setOnClickListener(this)
        ll_blocked_users.setOnClickListener(this)
        ll_terms.setOnClickListener(this)

    }

    //...................................get home api............................................


    private fun getHomeApiData() {
        cl_loader_main.visibility = View.VISIBLE
        viewModel?.homeApi(auth_token, getId, current_lat.toString(), current_long.toString())
            ?.observe(
                this,
                object : Observer<HomeApiResponse> {
                    override fun onChanged(@Nullable loginResponse: HomeApiResponse?) {
                        cl_loader_main.visibility = View.GONE
                        statusCode = loginResponse?.code

                        if (statusCode == 200) {
                            homeApiData =
                                loginResponse?.data?.interactionData as ArrayList<InteractionData>
                            if (loginResponse.data.notificationCount == 0) {
                                rlCount.visibility = View.GONE
                            }
                            tvCount.text = loginResponse.data.notificationCount.toString()
                            for (i in 0 until homeApiData.size) {
                                val sydney = LatLng(
                                    homeApiData[i].userDetail.latitude.toDouble(),
                                    homeApiData[i].userDetail.longitude.toDouble()
                                )
                                mMap?.addMarker(
                                    MarkerOptions().position(sydney).icon(
                                        BitmapDescriptorFactory.fromBitmap(
                                            createCustomMarker(
                                                applicationContext,
                                                Constants.PROFILE_URL_API + homeApiData.get(i).userDetail.profileImage
                                            )
                                        )
                                    ).title(homeApiData.get(i).userDetail.firstName)
                                )

                                mMap?.addMarker(
                                    MarkerOptions().position(sydney).icon(
                                        BitmapDescriptorFactory.fromBitmap(
                                            createCustomMarker(
                                                applicationContext,
                                                Constants.PROFILE_URL_API + homeApiData[i].userDetail.profileImage
                                            )
                                        )
                                    ).title(homeApiData[i].userDetail.firstName)
                                )

                            }

                            //mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17f))
                            mMap?.setOnMarkerClickListener(this@MainActivity)
                        } else {
                            showSnackBar(loginResponse?.message)
                        }

                    }
                })


    }

    private fun showSnackBar(message: String?) {
        SnackbarUtil.showWarningShortSnackbar(this, message)
    }

    //.......................get current locations.......................................

    private fun getLocations() {
        gps = Gps(this)

        if (gps!!.canGetLocation()) {
            current_lat = gps!!.getLatitude()
            current_long = gps!!.getLongitude()
            Log.e("current_lat", current_lat.toString() + "")
            Log.e("current_long", current_long.toString() + "")

        } else {
            gps!!.showSettingsAlert()
        }
        if (mMap != null) {
            if(current_lat!=null)
            {
            val center = CameraUpdateFactory.newLatLng(
                LatLng(
                    current_lat!!.toDouble(),
                    current_long!!.toDouble()
                )
            )

            val zoom = CameraUpdateFactory.zoomTo(11f)
            mMap!!.moveCamera(center)
            mMap!!.animateCamera(zoom)
        }}
    }


//.........................................map code.....................................................

    private fun setUpMapIfNeeded() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        if(current_lat!=null)
        {
        val center = CameraUpdateFactory.newLatLng(
            LatLng(
                current_lat!!.toDouble(),
                current_long!!.toDouble()
            )
        )

        val zoom = CameraUpdateFactory.zoomTo(11f)
        mMap!!.moveCamera(center)
        mMap!!.animateCamera(zoom)}
        mMap!!.isMyLocationEnabled = true
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) { // TODO: Consider calling
            return
        }
        getLocations()


    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        val clickCount = marker?.tag as? Int
        Log.e("marker", marker?.id)
        val latitude = marker!!.position.latitude
        var longitude: Double? = marker.position.longitude
        showUserID=""
        // Show Bottom Sheet
        for (i in homeApiData.indices) {
            if (homeApiData[i].userDetail.latitude == latitude.toString() && homeApiData[i].userDetail.longitude == longitude.toString()) {
                var bottomSheet = BottomSheetDialog()

                if(showUserID=="" &&showUserID!=homeApiData[i].userDetail.id){
                    showUserID=homeApiData[i].userDetail.id
                val bundle = Bundle()
                bundle.putParcelable("UserData", homeApiData[i].userDetail)
                bottomSheet.arguments = bundle
                bottomSheet.show(
                    supportFragmentManager,
                    "ModalBottomSheet"
                )
            }}
        }



        return false
    }

    //...........................................customized marker code...............................................
    fun createCustomMarker(
        context: Context, resource: String
    ): Bitmap? {

        val marker: View =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.custom_marker_layout,
                null
            )
        val markerImage =
            marker.findViewById<View>(R.id.user_dp) as CircleImageView
//        Glide.with(this).load(resource).into(markerImage)
        Picasso.get().load(resource).into(markerImage)


        val displayMetrics = DisplayMetrics()
        (this).windowManager.defaultDisplay.getMetrics(displayMetrics)
        marker.layoutParams = ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT)
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(
            marker.measuredWidth,
            marker.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        marker.draw(canvas)
        return bitmap
    }


    //.................................get interest..........................................

    private fun getDataForInterestName() {
        val sharedPreferences =
            getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("nameList", null)
        val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        getName = gson.fromJson<Any>(json, type) as ArrayList<String>
        if (getName == null) {
            getName = ArrayList()
        }

        for (i in 0 until getName.size) {
            interestText += getName.get(i) + ","
        }
        tv_interest.text = interestText


    }


    private fun getDataForInterestId() {
        val sharedPreferences =
            getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("idList", null)
        val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        getId = gson.fromJson<Any>(json, type) as ArrayList<String>
        if (getId == null) {
            getId = ArrayList()
        }

        Log.e("id", getId.size.toString() + "sizeee")
        // hit api
        getHomeApiData()
    }

    //...................................set data...............................................


    private fun setData() {
        mName = AppPreferences.init(this).getString(Constants.F_NAME)
        mProfilePic = AppPreferences.init(this).getString(Constants.PROFILE_IMAGE)
        tv_user_name.text = mName
        if (mProfilePic.equals("null")) {
            img_profile_pic?.setImageResource(R.drawable.user)


        } else {
            this.let {
                Glide.with(it)
                    .load(mProfilePic)
                    .into(img_profile_pic)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        getHomeApiData()
        setData()
    }

    //...........................click listener of nav bar..........................................//
    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.iv_nav_bar -> {
                drawer_layout.openDrawer(GravityCompat.START)
            }
            R.id.iv_filter -> {
                val i = Intent(this, ChooseInterestActivity::class.java)
                startActivity(i)
            }
            R.id.nav_close -> {
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.ll_edit_profile -> {
                val i = Intent(this, EditProfileActivity::class.java)
                startActivity(i)
            }
            R.id.ll_notifications -> {
                val i = Intent(this, NotificationActivity::class.java)
                startActivity(i)
            }
            R.id.icon_notification -> {
                val i = Intent(this, NotificationActivity::class.java)
                startActivity(i)
            }
            R.id.ll_reviews -> {
                val i = Intent(this, ReviewQueaueActivity::class.java)
                startActivity(i)
            }
            R.id.ll_connection -> {
                val i = Intent(this, ConnectionActivity::class.java)
                startActivity(i)
            }
            R.id.ll_subscription -> {
                Toast.makeText(this@MainActivity, "under development!", Toast.LENGTH_SHORT).show()
            }
            R.id.ll_privacy -> {
                Toast.makeText(this@MainActivity, "under development!", Toast.LENGTH_SHORT).show()
            }
            R.id.ll_blocked_users -> {
                val i = Intent(this, BLockedUserListActivity::class.java)
                startActivity(i)
            }
            R.id.ll_terms -> {
                Toast.makeText(this@MainActivity, "under development!", Toast.LENGTH_SHORT).show()
            }
            R.id.ll_logout -> {
                logoutdialog()
            }
        }

    }

    private fun logoutdialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.app_name))
            .setMessage("Are you sure?You want to logout from the Sea Connect!!")
            .setPositiveButton(
                "Yes"
            ) { dialog, which ->
                dialog.dismiss()
                AppPreferences.init(this).clearUserPrefs()
                val logout = Intent(this, LoginActivity::class.java)
                startActivity(logout)
                finishAffinity()
            }
            .setNegativeButton(
                "No"
            ) { dialog, which -> dialog.dismiss() }
            .setIcon(R.drawable.logo)
            .show()
    }


    //................................hide keyboard on touch.................................................
    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            activity.currentFocus!!.windowToken, 0
        )
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        val ret = super.dispatchTouchEvent(ev)
        if (view is EditText) {
            val w = currentFocus
            val scrcoords = IntArray(2)
            w!!.getLocationOnScreen(scrcoords)
            val x = ev.rawX + w.left - scrcoords[0]
            val y = ev.rawY + w.top - scrcoords[1]
            if (ev.action == MotionEvent.ACTION_UP
                && (x < w.left || x >= w.right || y < w.top || y > w.bottom)
            ) {
                hideSoftKeyboard(this)
            }
        }
        return ret
    }


}
