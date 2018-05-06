package com.iuh.tranthang.myshool

import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.iuh.tranthang.myshool.ViewApdater.DataAdapter
import com.iuh.tranthang.myshool.ViewApdater.RecycleViewUserAdapter
import com.iuh.tranthang.myshool.ViewApdater.SimpleAdapter_UpdateSalary
import com.iuh.tranthang.myshool.model.Parameter
import com.iuh.tranthang.myshool.model.mUser

class ListUserForUpdateSalary : AppCompatActivity() {
    private var mAuth: FirebaseUser? = null
    private var recyclerView: RecyclerView? = null
    private var viewHolder: RecyclerView.ViewHolder? = null
    private var info: AdapterView.AdapterContextMenuInfo? = null
    val listUser = ArrayList<mUser>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user_updatesalary)
        mAuth = FirebaseAuth.getInstance().currentUser
        recyclerView = findViewById<RecyclerView>(R.id.recycle_listUserUpdateSalary)

        firebaseListenerInit()
        registerForContextMenu(recyclerView)
    }

    private fun firebaseListenerInit() {
        if (mAuth != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection(Parameter.root_User)
                    .get()
                    .addOnCompleteListener({ task ->
                        if (task.isSuccessful) {
                            listUser.clear()
                            for (document in task.result) {
                                Log.e("tmt", document.toString())
                                var mUser = mUser(document.data[Parameter.comp_UId] as String,

                                        document.data[Parameter.comp_fullname] as String,
                                        document.data[Parameter.comp_Permission] as String,
                                        document.data[Parameter.comp_numberphone] as String,
                                        document.data[Parameter.comp_address] as String,
                                        document.data[Parameter.comp_email] as String,
                                        document.data[Parameter.comp_birthday] as String,
                                        document.data[Parameter.comp_toCongTac] as String,
                                        document.data[Parameter.comp_chucVu] as String,
                                        document.data[Parameter.comp_url] as String,
                                        document.data[Parameter.comp_action] as Boolean,
                                        document.data[Parameter.comp_baseSalary] as String,
                                        document.data[Parameter.comp_uidDevice] as String)
                                if (document.data[Parameter.comp_action].toString() == "true")
                                    listUser.add(mUser)
                            }
                            callAdapter(listUser)
                        } else {
                            Log.d("tmt", "Error getting documents: ", task.exception)
                            // Lỗi trả về
                        }
                    })
        }

    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        var inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.context_menu_updatesalary, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        info = item!!.menuInfo as AdapterView.AdapterContextMenuInfo?
        when (item!!.itemId) {
            R.id.updateHeSoluong -> updateHeSoLuong()
        }
        return super.onContextItemSelected(item)

    }


    private fun callAdapter(listUser: ArrayList<mUser>) {

        recyclerView!!.layoutManager = LinearLayoutManager(this)
        var adapter = DataAdapter(listUser)
        val simpleAdapter = SimpleAdapter_UpdateSalary(listUser)
        recyclerView!!.adapter = simpleAdapter
        adapter!!.notifyDataSetChanged()

    }

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
                var dUser: mUser = listUser.get(viewHolder.adapterPosition)
                var dbFireStore = FirebaseFirestore.getInstance()
                dbFireStore.collection(Parameter.root_User)
                Log.e("tmt id", dUser.getUid())
                // Thực hiện hiện update
                var washingtonRef: DocumentReference =
                        dbFireStore.collection(Parameter.root_User).document(dUser.getUid())
                washingtonRef.update(Parameter.comp_action, false).addOnSuccessListener { void ->
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

    private fun updateHeSoLuong() {

    }

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
}

