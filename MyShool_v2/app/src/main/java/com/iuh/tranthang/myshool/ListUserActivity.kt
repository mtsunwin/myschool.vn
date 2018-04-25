package com.iuh.tranthang.myshool

import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.ViewApdater.DataAdapter
import com.iuh.tranthang.myshool.ViewApdater.SimpleAdapter
import com.iuh.tranthang.myshool.ViewApdater.SwipeToDeleteCallback
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.User
import kotlinx.android.synthetic.main.activity_list_user.*


class ListUserActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {


    private var mAuth: FirebaseUser? = null
    private var recyclerView: RecyclerView? = null

    val listUser = ArrayList<User>()

    /**
     * Swipe Refresh
     */
    override fun onRefresh() {
        Toast.makeText(this, "Showw", Toast.LENGTH_LONG).show()
        firebaseListenerInit()
        swipe_container.setRefreshing(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)
        mAuth = FirebaseAuth.getInstance().currentUser
        swipe_container.setOnRefreshListener { onRefresh() }
        swipe_container.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
        firebaseListenerInit()
    }

    /**
     * Lấy danh sách User từ Firebase
     */
    private fun firebaseListenerInit() {
        if (mAuth != null) {
            Log.e("tmt data", "it will run this")
            val db = FirebaseFirestore.getInstance()
            db.collection(Parameter().root_User)
                    .get()
                    .addOnCompleteListener({ task ->
                        if (task.isSuccessful) {
                            Log.e("tmt data", "it will run this")
                            for (document in task.result) {
                                Log.e("tmt data", document.data.toString())
                                var mUser = User(document.data[Parameter().comp_UId] as String,
                                        document.data[Parameter().comp_fullname] as String,
                                        document.data[Parameter().comp_Permission] as String,
                                        document.data[Parameter().comp_numberphone] as String,
                                        document.data[Parameter().comp_address] as String,
                                        document.data[Parameter().comp_email] as String,
                                        document.data[Parameter().comp_birthday] as String,
                                        document.data[Parameter().comp_toCongTac] as String,
                                        document.data[Parameter().comp_chucVu] as String,
                                        document.data[Parameter().comp_url] as String,
                                        document.data[Parameter().comp_action] as Boolean
                                )
                                var temp: Boolean = false
                                for (cUser in listUser) {
                                    if (cUser.getUid() == mUser.getUid()) {
//                                        cUser.setAddress(mUser.getAddress())
//                                        cUser.setBirthday(mUser.getBirthday())
//                                        cUser.setEmail(mUser.getEmail())
//                                        cUser.setFullname(mUser.getFullname())
                                        temp = true
                                    }
                                }
                                if (!temp && mUser.isAction()) {
                                    listUser.add(mUser)
                                }
                            }
                            callAdapter(listUser)
                        } else {
                            Log.d("tmt", "Error getting documents: ", task.exception)
                            // Lỗi trả về
                        }
                    })
        }
    }

    /*
    * Khơi tạo Adapter và danh sách
    * */
    private fun callAdapter(listUser: ArrayList<User>) {
        recyclerView = findViewById<RecyclerView>(R.id.recycle) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        var adapter = DataAdapter(listUser)
        val simpleAdapter = SimpleAdapter(listUser)
        recyclerView!!.adapter = simpleAdapter
        adapter!!.notifyDataSetChanged()
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView!!.adapter as SimpleAdapter
                Log.e("tmt deleted", direction.toString())
                showDialog(adapter, viewHolder)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        fab.setOnClickListener { view ->
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showDialog(adapter: SimpleAdapter, viewHolder: RecyclerView.ViewHolder) {
        var builder: AlertDialog.Builder = AlertDialog.Builder(this)
        var inflater: LayoutInflater = layoutInflater
        var view: View = inflater.inflate(R.layout.layout_dialog, null)
        var content: TextView = view.findViewById<View>(R.id.content) as TextView
        content.setText("Bạn có muốn xóa?")
        builder.setView(view)
        builder.setNegativeButton(R.string.dialog_no, object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                p0!!.dismiss()
                adapter!!.notifyDataSetChanged()
            }
        })
        builder.setPositiveButton(R.string.dialog_yes, object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                Log.e("tmt onClick", p1.toString())
                var dUser: User = listUser.get(viewHolder.adapterPosition)
                var dbFireStore = FirebaseFirestore.getInstance()
                dbFireStore.collection(Parameter().root_User)
                Log.e("tmt id", dUser.getUid())
                var washingtonRef: DocumentReference =
                        dbFireStore.collection(Parameter().root_User).document(dUser.getUid())
                washingtonRef.update(Parameter().comp_action, false).addOnSuccessListener { void ->
                    firebaseListenerInit()
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
                    var tempList: ArrayList<User> = ArrayList<User>()
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
}