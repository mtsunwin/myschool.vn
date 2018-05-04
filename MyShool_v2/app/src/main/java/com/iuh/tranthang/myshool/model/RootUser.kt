package com.iuh.tranthang.myshool.model

/**
 * Created by ThinkPad on 4/12/2018.
 */
class RootUser {
    private var rootUser: String = ""
    private var branchMUser: ArrayList<mUser>? = null

    constructor(root: String, branches: ArrayList<mUser>) {
        this.rootUser = root
        this.branchMUser = branches
    }

    public fun getRoot(): String {
        return rootUser
    }

    public fun getBranch(): ArrayList<mUser>? {
        return branchMUser
    }

    public fun setRoot(root: String) {
        this.rootUser = root;
    }

    public fun setBranch(branches: ArrayList<mUser>) {
        this.branchMUser = branches
    }
}