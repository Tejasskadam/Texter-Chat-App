package com.coretech.duck;

public class User {
    String name;
    String email;
    String phone;
    String password;
    String image;
    String uid;
    String stat;




    public User(){

    }

    public User(String name, String email, String phone, String password,String image, String uid,String stat) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.image =image;
        this.uid = uid;
        this.stat=stat;


    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage(){
        return image;
    }
    public  void setImage(){
        this.image=image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}
