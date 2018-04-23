package com.iuh.tranthang.myshool


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.Firebase.dbConnect
import com.iuh.tranthang.myshool.ViewApdater.ActivityFragment
import com.iuh.tranthang.myshool.ViewApdater.PageAdapter
import com.iuh.tranthang.myshool.ViewApdater.ProfileFragment
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.User
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileActivity : ProfileFragment.OnSelectedListener, AppCompatActivity() {
    override fun onSelected(dUser: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val frm_birthday: String = "birthday"
    val frm_address: String = "address"
    val frm_email: String = "email"
    val frm_phone: String = "phone"
    val frm_name: String="name"
    var viewPager: ViewPager? = null
    var tabLayout: TabLayout? = null
    var pageAdapter: PageAdapter? = null
    private var drawerLayout: DrawerLayout? = null
    private var abdt: ActionBarDrawerToggle? = null
    private var navigationView: NavigationView? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        mAuth = FirebaseAuth.getInstance()

        val intent = Intent(this, InsideActivity::class.java)
        val intent_profile = Intent(this, ProfileActivity::class.java)

        var token = getSharedPreferences("usename", Context.MODE_PRIVATE)
//        Log.e("tmt check", mAuth!!.uid)

        var db = dbConnect()
        if (db.isAuthentication()) {
            txt_nickname.setText("Thắng đẹp trai hihi")
            var dbFireStore = FirebaseFirestore.getInstance()
            dbFireStore!!.collection(Parameter().root_User).document(mAuth!!.uid!!)
                    .get().addOnCompleteListener({ task ->
                        if (task.isSuccessful) {
                            Log.e("Tmt inside", "mmmmmmmmmmmmmm")
                            var result: DocumentSnapshot = task.result
                            if (result.exists()) {
                                var tUser = User()
                                tUser.setAddress(result.data[Parameter().comp_address].toString())
                                tUser.setFullname(result.data[Parameter().comp_fullname].toString())
                                var tChv = ""
                                val listStringPermission = applicationContext.resources.getStringArray(R.array.select_permission)
                                when (result.data[Parameter().comp_Permission].toString()) {
                                    "0" -> tChv = listStringPermission.get(1)
                                    "1" -> tChv = listStringPermission.get(2)
                                    "2" -> tChv = listStringPermission.get(3)
                                    "3" -> tChv = listStringPermission.get(4)
                                }
                                tUser.setChucVu(tChv)
                                Log.e("Tmt inside", "nnnnnnnnnnn")
                                updateUI(tUser)
                            } else {
                                Log.e("tmt false", "false")
                            }
                        }
                    })
//            MyTask().execute();
            Log.e("tmtgetfata:", db.getUser());
        } else {
            // Xử lý quay trở về đăng nhập
            Log.e("tmt", "false")
        }

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.menuNavigation)

        abdt = ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close)

        drawerLayout!!.addDrawerListener(abdt!!)
        abdt!!.syncState()

        navigationView!!.setNavigationItemSelectedListener(
                object : NavigationView.OnNavigationItemSelectedListener {
                    override fun onNavigationItemSelected(item: MenuItem): Boolean {
                        var boolean: Boolean?
                        if (item!!.itemId == R.id.DangXuat) {
                            var editor = token.edit()
                            editor.putString("loginusername", " ")
                            editor.commit()
                            startActivity(intent)
                            finish()
                            boolean = true
                        } else if (item!!.itemId == R.id.itemTrangCaNhan) {
                            startActivity(intent_profile)
                            boolean = true
                        } else {
                            boolean = false
                        }
                        return boolean!!
                    }
                }
        )
    }

    /**
     * Cập nhật UI chính
     */
    private fun updateUI(tUser: User) {
        Log.e("tmt UI", "oke")
        txt_nickname.setText(tUser.getFullname())
        txt_chucvu.setText(tUser.getChucVu())

        val bundle = Bundle()
        bundle.putString(frm_address, tUser.getAddress())
        bundle.putString(frm_birthday, tUser.getBirthday())
        bundle.putString(frm_email, tUser.getEmail())
        bundle.putString(frm_phone, tUser.getNumberphone())
        bundle.putString(frm_name,tUser.getFullname())
         var fragment_profile = ProfileFragment()
        fragment_profile.arguments = bundle
        pageAdapter = PageAdapter(supportFragmentManager)

        pageAdapter!!.addFragment(fragment_profile, resources.getString(R.string.frmInfo))
        pageAdapter!!.addFragment(ActivityFragment(), resources.getString(R.string.frmAction))

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        viewPager!!.adapter = pageAdapter
        tabLayout!!.setupWithViewPager(viewPager)

        fab_changeInfo.setOnClickListener { view ->
            var intent = Intent(this, UpdateProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var boolean: Boolean?
        if (item!!.itemId == R.id.DangXuat) {
            var token = getSharedPreferences("username", Context.MODE_PRIVATE)
            var editor = token.edit()
            editor.putString("loginusername", " ")
            editor.commit()
            var intent = Intent(this, InsideActivity::class.java)
            startActivity(intent)
            finish()
            boolean = true
        } else if (item!!.itemId == R.id.itemTrangCaNhan) {
            var intent_profile = Intent(this, ProfileActivity::class.java)
            startActivity(intent_profile)
            boolean = true
        } else {
            boolean = super.onOptionsItemSelected(item)
        }
        return boolean!!
    }

}
