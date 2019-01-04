package com.example.hoong.coiple;

/**
 * Created by hun on 2017-11-15.
 */

    public class user {
        private String CopleId;
    private String ID;
    private String PASSWORD;
    private String uid;
    private String photo;



    public user(){

    }
    public user(String CoupleId, String Id, String Pw, String uid,String photo){
        this.CopleId = CoupleId;
        this.photo = photo;
        this.ID = ID;

        this.PASSWORD =PASSWORD;
        this.uid=uid;

        }

    public String getCopleId() {
        return CopleId;
    }

    public void setCopleId(String birth) {
        this.CopleId = birth;
    }

    public String getID() {
        return ID;
    }

    public void setID(String name) {
        this.ID = name;
    }



    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String pw) {
        this.PASSWORD = pw;
    }

    public String getuid() {
        return uid;
    }


    public void setuid(String uid) {
        this.uid = uid;
    }

    public String getPhoto() {
        return photo;
    }


    public void setPhoto(String uid) {
        this.photo = photo;
    }

}


