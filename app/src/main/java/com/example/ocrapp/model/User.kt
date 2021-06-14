package com.example.ocrapp.model

class User {

    var type: String? = null
    var email: String
        get() {
            return email
        }
        set(value) {
            email = value
        }
    var password: String
        get() {
            return password
        }
        set(value) {
            password = value
        }

    var uid: String
        get() {
            return uid
        }
        set(value) {
            uid = value
        }


    constructor() {}

    constructor(type: String?) {

        this.type = type

    }


}