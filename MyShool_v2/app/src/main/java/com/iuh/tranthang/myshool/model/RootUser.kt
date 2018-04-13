package com.iuh.tranthang.myshool.model

/**
 * Created by ThinkPad on 4/12/2018.
 */
class RootUser {
    private var rootUser: String = ""
    private var branchUser: ArrayList<User>? = null

    constructor(root: String, branch: ArrayList<User>) {
        this.rootUser = root
        this.branchUser = branch
    }

    public fun getRoot(): String {
        return rootUser
    }

    public fun getBranch(): ArrayList<User>? {
        return branchUser
    }

    public fun setRoot(root: String) {
        this.rootUser = root;
    }

    public fun setBranch(branch: ArrayList<User>) {
        this.branchUser = branch
    }
}