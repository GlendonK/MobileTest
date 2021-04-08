package com.example.team16_medassist.activity

import HistoryFragment
import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.example.team16_medassist.R
import com.example.team16_medassist.database.FirebaseApp
import com.example.team16_medassist.databinding.ActivityMainBinding
import com.example.team16_medassist.fragment.DoctorFragment
import com.example.team16_medassist.fragment.HomeFragment
import com.example.team16_medassist.fragment.ReportFragment
import com.example.team16_medassist.viewmodel.LoginViewModel
import com.example.team16_medassist.viewmodel.ViewModelFactory
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.android.material.navigation.NavigationView
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() , AuthListener {
    private val ARG_CAUGHT = "Main_caught"
    // view binding to access view elements

    private lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory((application as FirebaseApp).userRepository,(application as FirebaseApp).caseRepository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // side - navigation
        val sideNavigationView: NavigationView = findViewById(R.id.sideNav_view)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_menu)
        val toolbarExt: View = findViewById(R.id.toolbar_ext)
        val toolbar: androidx.appcompat.widget.Toolbar = toolbarExt.findViewById(R.id.toolbar)
        val toggleSideNav: ActionBarDrawerToggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.open, R.string.close
        )
        drawerLayout.addDrawerListener(toggleSideNav)
        toggleSideNav.syncState()

        supportActionBar?.setDisplayShowHomeEnabled(true)
        sideNavigationView.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.sideNav_home -> nav_home()
                R.id.sideNav_reports -> nav_reports()
                R.id.sideNav_history -> nav_history()
                R.id.sideNav_signout -> logout()
            }
            true
        }

        // retrieve dynamic data



        // implementation logic: get the values from LoginActivity through intents
        // 1. retrieve first name and last name for side-nav
        val firstName = loginViewModel.getUsers()!!.value!!.getFirstName()
        val lastName = loginViewModel.getUsers()!!.value!!.getLastName()
        val position = loginViewModel.getUsers()!!.value!!.getPosition()
        Log.d("ASDSA","position: ${position.toString()}")
        val navigationView : NavigationView  = findViewById(R.id.sideNav_view)
        val headerView : View = navigationView.getHeaderView(0)
        val navUsername : TextView = headerView.findViewById(R.id.userName)
        navUsername.text = lastName + " " + firstName

        // 2. retrieve display photo for side-nav
        val image = loginViewModel.getUsers()!!.value!!.getImageURL()
        val profileImage : CircleImageView = headerView.findViewById(R.id.imageViewProfile)
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(image.toString())
        Glide.with(this)
                .using(FirebaseImageLoader())
                .load(storageRef)
                .into(profileImage)

        // 3. load home fragment

        val bundle = Bundle()
        // FIXME: I DONNO WHAT THESE STRINGS ARE FOR AND WHERE IT FROM. -GLENDON.
        //bundle.putString("caseID", caseID.toString())
        //bundle.putString("caseStatus", caseStatus)
        if(position.toString() == "paramedic"){
            val fragInfo = HomeFragment()
            fragInfo.setArguments(bundle)
            loadFragment(HomeFragment.newInstance(bundle, this))
        }
        if(position.toString() == "doctor"){
            val fragInfo = DoctorFragment()
            fragInfo.setArguments(bundle)
            loadFragment(DoctorFragment.newInstance(bundle))
        }

    }

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val message = intent.extras?.getString("message")
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Bio")
//            builder.setMessage(userBio)
            Log.d("hello",message.toString())

        }
    }

    private fun logout(){
        val logoutIntent = Intent(this, LoginActivity::class.java)
        val closePrompt = AlertDialog.Builder(this)
        closePrompt.setTitle("Confirm Logout?")
        closePrompt.setMessage("You will be returned to the login screen.")
        closePrompt.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> }
        closePrompt.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int -> startActivity(logoutIntent)}
        closePrompt.show()

    }

    private fun nav_history(){
        loadFragment(HistoryFragment())
    }

    private fun nav_home(){
        loadFragment(HomeFragment())
    }

    private fun nav_reports(){
        loadFragment(ReportFragment())
    }

    // side - navigation methods

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentFrame,fragment,"dashboard_fragment")
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, IntentFilter("MyData"))
    }

    override fun onStarted() {
        TODO("Not yet implemented")
    }

    override fun onSuccess() {
        TODO("Not yet implemented")
    }

    override fun onFailure(message: String) {
        TODO("Not yet implemented")
    }

//    override fun onStop() {
//        super.onStop()
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
//    }

}
