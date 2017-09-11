/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phantomdeveloper.qrscannermaven;

/**
 *
 * @author Inspiron
 */
class peserta {
    String nama;
    String email;
    String code;
    Boolean sign_in;
    Boolean sign_out;

    public peserta(String nama, String email, String code, Boolean sign_in, Boolean sign_out) {
        this.nama = nama;
        this.email = email;
        this.code = code;
        this.sign_in = sign_in;
        this.sign_out = sign_out;
    }

    

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }

    public Boolean getSign_in() {
        return sign_in;
    }

    public Boolean getSign_out() {
        return sign_out;
    }
}
