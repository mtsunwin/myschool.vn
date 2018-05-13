package com.iuh.tranthang.myshool

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.Firebase.dbConnect
import com.iuh.tranthang.myshool.ViewApdater.*
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.mUser
import kotlinx.android.synthetic.main.activity_list_user.*


class ListUserActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
        AdapterDialogAsk.sendReponse, dbConnect.sendListUser {

    private var permissionForLogIn: String? = null
    private var mAuth: FirebaseUser? = null
    private lateinit var db: dbConnect
    private var recyclerView: RecyclerView? = null
    private lateinit var type_list: String
    private lateinit var listUser: ArrayList<mUser>

    /**
     * Swipe Refresh
     */
    override fun onRefresh() {
        firebaseListenerInit(type_list)
        swipe_container.setRefreshing(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var token_ps = getSharedPreferences("permission", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)
        db = dbConnect(this)
        permissionForLogIn = token_ps!!.getString("permission", " ")
        mAuth = FirebaseAuth.getInstance().currentUser
        swipe_container.setOnRefreshListener { onRefresh() }
        type_list = intent.getStringExtra(Parameter.intent_user)
        firebaseListenerInit(type_list)
    }

    /**
     * Lấy danh sách mUser từ Firebase
     * Thực hiện cập nhật lại listview
     */
    private fun firebaseListenerInit(type: String) {
        if (mAuth != null) {
            when (type) {
                Parameter.intent_all_user -> {
                    db.getAllListUser("4")
                }
                Parameter.intent_teacher_user -> {
                    db.getAllListUser("1")
                }
                Parameter.intent_staff_user -> {
                    db.getAllListUser("2")
                }
                Parameter.intent_ketoan_user -> {
                    db.getAllListUser("0")
                }
                Parameter.intent_manager_user -> {
                    db.getAllListUser("3")
                }
                else -> {
                    db.getAllListUser("4")
                }
            }
        }
    }

    /*
    * Khơi tạo Adapter và danh sách
    * */
    private fun callAdapter(listMUser: ArrayList<mUser>) {
        recyclerView = findViewById<RecyclerView>(R.id.recycle) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        var adapter = AdapterDataListUser(listMUser)
        val simpleAdapter = RecycleViewUserAdapter(listMUser, { mU: mUser -> callNow(mU) }, { mU: mUser -> cardView(mU) })
        recyclerView!!.adapter = simpleAdapter
        adapter!!.notifyDataSetChanged()
        if (permissionForLogIn.equals("3")) {
            val swipeHandler = object : SwipeToDeleteCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = recyclerView!!.adapter as RecycleViewUserAdapter
                    showDialog(adapter, viewHolder)
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(recyclerView)
            fab.setOnClickListener { view ->
                var intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        } else fab.hide()
    }

    /**
     * Sử lý sự kiện khi người dùng click vào CardView
     */
    private fun cardView(mU: mUser) {
        val fm: FragmentManager = supportFragmentManager
        val userInfoDialogAsk: AdapterDialogProfile = AdapterDialogProfile().newInstance(mU)
        userInfoDialogAsk.show(fm, null)
    }

    /**
     * Callback khi tương tác với CardView
     */
    @SuppressLint("MissingPermission")
    private fun callNow(m: mUser) {
        if (m.getNumberphone().length > 0) {
            var intent_call = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + m.getNumberphone()))
            startActivity(intent_call)
        }
    }

    /**
     * Hiển thị Dialog hỏi người dùng có muốn xóa hay không
     */
    private fun showDialog(adapter: RecycleViewUserAdapter, viewHolder: RecyclerView.ViewHolder) {
        var builder: AlertDialog.Builder = AlertDialog.Builder(this)
        var inflater: LayoutInflater = layoutInflater
        var view: View = inflater.inflate(R.layout.layout_dialog, null)
        var content: TextView = view.findViewById<View>(R.id.txtDialog_content) as TextView
        content.setText("Bạn có muốn xóa?")
        builder.setView(view)
        builder.setNegativeButton(R.string.dialogAsk_no, object : DialogInterface.OnClickListener { // cancel
            override fun onClick(p0: DialogInterface?, p1: Int) {
                p0!!.dismiss()
                adapter!!.notifyDataSetChanged()
            }
        })
        builder.setPositiveButton(R.string.dialogAsk_yes, object : DialogInterface.OnClickListener { // apply
            override fun onClick(p0: DialogInterface?, p1: Int) {
                var dMUser: mUser = listUser.get(viewHolder.adapterPosition)
                var dbFireStore = FirebaseFirestore.getInstance()
                dbFireStore.collection(Parameter.root_User)
                Log.e("tmt id", dMUser.getUid())
                // Thực hiện hiện update
                var washingtonRef: DocumentReference =
                        dbFireStore.collection(Parameter.root_User).document(dMUser.getUid())
                washingtonRef.update(Parameter.comp_action, false).addOnSuccessListener { void ->
                    firebaseListenerInit(type_list)
                }.addOnFailureListener { exception ->
                    Log.e("tmt", "that bai")
                }
//                adapter.removeAt(viewHolder.adapterPosition)
            }

        })
        var dialog: Dialog = builder.create()
        dialog.show()
    }

    /**
     * Menu Search
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)
        val searchItem = menu!!.findItem(R.id.action_search)
        if (searchItem != null) {
            var searchView = MenuItemCompat.getActionView(searchItem) as SearchView
            searchView.setOnCloseListener(object : SearchView.OnCloseListener {
                override fun onClose(): Boolean {
                    return false
                }
            })
            searchView.setOnSearchClickListener(View.OnClickListener {
                //some operation
            })
            val searchPlate = searchView.findViewById<View>(android.support.v7.appcompat.R.id.search_src_text) as EditText
            searchPlate.hint = resources.getString(R.string.menu_search)
            val searchPlateView = searchView.findViewById<View>(android.support.v7.appcompat.R.id.search_plate)
            searchPlateView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
            // use this method for search process
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    var tempList: ArrayList<mUser> = ArrayList<mUser>()
                    for (mUser in listUser) {
                        var fCheck = mUser.getFullname().toLowerCase()
                        var fCompare = newText!!.toLowerCase()
                        if (fCheck.contains(fCompare)) {
                            tempList.add(mUser)
                            callAdapter(tempList)
                        }
                    }
                    return true
                }
            })
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun list(list: ArrayList<mUser>) {
        listUser = list
    }

    override fun listUserDB(listUser: ArrayList<mUser>) {
        list(listUser)
        callAdapter(listUser)
    }

    /**
     * Nhận giá trị trả về từ hộp thoại Dialog
     */
    override fun completed(input: Boolean) {
        if (input) {

        } else {

        }
    }
}