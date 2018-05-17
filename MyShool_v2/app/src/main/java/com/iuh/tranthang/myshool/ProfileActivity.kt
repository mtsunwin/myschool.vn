package com.iuh.tranthang.myshool


import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.iuh.tranthang.myshool.Firebase.dbConnect
import com.iuh.tranthang.myshool.ViewApdater.PageAdapter
import com.iuh.tranthang.myshool.ViewApdater.ProfileFragment
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.mUser
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File


class ProfileActivity : ProfileFragment.OnSelectedListener, AppCompatActivity() {

    override fun onSelected(dMUser: mUser) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var dbFireStore: FirebaseFirestore? = null
    val frm_birthday: String = "birthday"
    val frm_address: String = "address"
    val frm_email: String = "email"
    val frm_phone: String = "phone"
    val frm_fullname: String = "fullname"
    val frm_luong: String = "luong"
    var frm_baseSalary: String = ""
    var permission: String = ""
    var viewPager: ViewPager? = null
    var tabLayout: TabLayout? = null
    var pageAdapter: PageAdapter? = null
    private var drawerLayout: DrawerLayout? = null
    private var abdt: ActionBarDrawerToggle? = null
    private var navigationView: NavigationView? = null
    private var mAuth: FirebaseAuth? = null
    internal var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var txtURLImage: String? = ""
    private var txtBirthDay: String? = ""
    private var imgAvatar: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        // Tắt menu
        val actionBar = supportActionBar
        actionBar!!.hide()

        mAuth = FirebaseAuth.getInstance()
        val intent = Intent(this, InsideActivity::class.java)
        val intent_profile = Intent(this, ProfileActivity::class.java)
        imgAvatar = findViewById(R.id.imgAvatar)
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        var token = getSharedPreferences("usename", Context.MODE_PRIVATE)
//        Log.e("tmt check", mAuth!!.uid)

        var db = dbConnect(this)
        if (db.isAuthentication()) {
            txt_nickname.setText("LOADING....")
            var dbFireStore = FirebaseFirestore.getInstance()
            dbFireStore!!.collection(Parameter.root_User).document(mAuth!!.uid!!)
                    .get().addOnCompleteListener({ task ->
                        if (task.isSuccessful) {
                            Log.e("Tmt inside", "mmmmmmmmmmmmmm")
                            var result: DocumentSnapshot = task.result
                            if (result.exists()) {
                                var tUser = mUser()
                                tUser.setAddress(result.data[Parameter.comp_address].toString())
                                tUser.setFullname(result.data[Parameter.comp_fullname].toString())
                                tUser.setPermission(result.data[Parameter.comp_Permission].toString())
                                txtBirthDay = result.data[Parameter.comp_birthday].toString()
                                if (txtBirthDay!!.length > 0)
                                    tUser.setBirthday(result.data[Parameter.comp_birthday].toString())
                                else tUser.setBirthday("Chưa cập nhật")
                                tUser.setNumberphone(result.data[Parameter.comp_numberphone].toString())
                                tUser.setEmail(result.data[Parameter.comp_email].toString())
                                tUser.setCoefficient(result.data[Parameter.comp_baseSalary].toString())
                                txtURLImage = result.data[Parameter.comp_url].toString()
                                Log.e("URL:", txtURLImage.toString())
                                if (txtURLImage!!.length > 0) {
                                    try {
                                        val tmpFile = File.createTempFile("img", "png")
                                        val reference = FirebaseStorage.getInstance().getReference("images/")
                                        //  "id" is name of the image file....
                                        reference.child(txtURLImage.toString()).getFile(tmpFile).addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot> {
                                            val image = BitmapFactory.decodeFile(tmpFile.getAbsolutePath())
                                            imgAvatar!!.setImageBitmap(image)
                                        })
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                                Log.e("Tmt inside abcd", tUser.getFullname() + "-" + tUser.getAddress() + "-" + tUser.getBirthday() + "-" +
                                        tUser.getNumberphone() + "-" + tUser.getHeSoLuong())
                                dbFireStore!!.collection(Parameter.collection_hesoluongcoban).document(Parameter.salary)
                                        .get().addOnCompleteListener({ task ->
                                            if (task.isSuccessful) {
                                                var temp = task.result.data[Parameter.luongCoBan].toString()
                                                updateUI(tUser, temp.toInt())
                                            }
                                        })
                            } else {
                                Log.e("tmt false", "false")
                            }
                        }
                    })
        } else {
            // Xử lý quay trở về đăng nhập
            Log.e("tmt", "false")
        }

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = this.findViewById(R.id.menuNavigation)

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
    private fun updateUI(tMUser: mUser, luong: Int = 0) {
        Log.e("tmt UI", "oke")
        txt_nickname.setText(tMUser.getFullname())
        permission = tMUser.getPermission().toString()
        Log.e("Permission profile", permission.toString())
        when (permission) {
            "0" -> {
                txt_chucvu.setText("Kế toán")
            }
            "1" -> {
                txt_chucvu.setText("Giáo viên")
            }
            "2" -> {
                txt_chucvu.setText("Nhân viên")
            }
            "3" -> {
                txt_chucvu.setText("Admin")
            }
            else -> {
                Log.e("tmt-123123", permission + " - Lỗi")
            } // THONG BAO LOI !!!
        }

        val bundle = Bundle()
        Log.e("thong tin address", tMUser.getAddress())
        bundle.putString(frm_fullname, tMUser.getFullname().toString())
        bundle.putString(frm_address, tMUser.getAddress().toString())
        bundle.putString(frm_birthday, tMUser.getBirthday().toString())
        bundle.putString(frm_email, tMUser.getEmail().toString())
        bundle.putString(frm_phone, tMUser.getNumberphone().toString())
        Log.e("tmttttt", luong.toString())
        Log.e("tmttttt", tMUser.getHeSoLuong())
        Log.e("tmttttt", tMUser.getHeSoLuong().length.toString())
        Log.e("tmttttt", tMUser.getHeSoLuong().toDouble().toString())
        var tem: Double = 0.0
        if (tMUser.getHeSoLuong().length > 0) {
            tem = tMUser.getHeSoLuong().toDouble() * luong
        }
        bundle.putDouble(frm_luong, tem)
        var fragment_profile = ProfileFragment()
        fragment_profile.arguments = bundle
        pageAdapter = PageAdapter(supportFragmentManager)

        pageAdapter!!.addFragment(fragment_profile, resources.getString(R.string.frmInfo))
//        pageAdapter!!.addFragment(ActivityFragment(), resources.getString(R.string.frmAction))

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        viewPager!!.adapter = pageAdapter
        tabLayout!!.setupWithViewPager(viewPager)

        fab_changeInfo.setOnClickListener { view ->
            var intent = Intent(this, UpdateProfileActivity::class.java)
            startActivity(intent)
        }
    }


}
