package com.example.aaron.sos;

import android.text.Editable;

/**
 * Created by Aaron on 2014/9/5.
 */
public class Model {
    private String phone;
    private String message;

    public Model(String p,String m) {
        this.phone = p;
        this.message = m;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



}
