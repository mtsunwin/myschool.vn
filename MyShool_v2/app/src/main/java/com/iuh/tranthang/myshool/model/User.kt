package com.iuh.tranthang.myshool.model

/**
 * Created by ThinkPad on 4/11/2018.
 */
class User {
    private var uid: String = ""
    private var fullname: String = ""
    private var permission: String = ""
    private var numberphone: String = ""
    private var address: String = ""
    private var birthday: String = ""
    private var email: String = ""

    private var toCongTac:String =""
    private var chucVu:String=""
    private var url:String=""
    constructor(id: String, fullname: String, permission: String, numberphone: String,
                address: String, email: String, birthday: String,toCongTac:String,chucVu:String,url:String) {
        this.address = address
        this.email = email
        this.fullname = fullname
        this.numberphone = numberphone
        this.permission = permission
        this.uid = id
        this.birthday = birthday

        this.toCongTac= toCongTac
        this.chucVu=chucVu
        this.url=url
    }

    constructor()

    public fun getChucVu(): String {
        return chucVu
    }

    public fun getUid(): String {
        return uid
    }

    public fun getBirthday(): String {
        return birthday
    }

    public fun getEmail(): String {
        return email
    }

    public fun getAddress(): String {
        return address
    }

    public fun getFullname(): String {
        return fullname
    }

    public fun getPermission(): String {
        return permission
    }
    public fun setPermission(permission: String){
        this.permission=permission
    }
    public fun getNumberphone(): String {
        return numberphone
    }
    public fun getUrl() :String{
        return url
    }
    public fun setUid(id: String) {
        this.uid = id
    }

    public fun setAddress(address: String) {
        this.address = address
    }

    public fun setFullname(fullname: String) {
        this.fullname = fullname
    }

    public fun setNumberphone(numberphone: String) {
        this.numberphone = numberphone
    }

    public fun setEmail(email: String) {
        this.email = email
    }

    public fun setBirthday(birthday: String) {
        this.birthday = birthday
    }

    public fun settoCongTac(toCongTac: String) {
        this.toCongTac = toCongTac
    }

    public fun setChucVu(chucVu: String) {
        this.chucVu = chucVu
    }
    public fun setUrl(url:String){
        this.url=url
    }

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result.put("fullname", fullname)
        result.put("address", address)
        result.put("birthday", birthday)
        result.put("numberphone", numberphone)
        result.put("permission", permission)
        result.put("email", email)
        result.put("toCongTac",toCongTac)
        result.put("chucVu",chucVu)
        result.put("url",url)
        return result
    }

}