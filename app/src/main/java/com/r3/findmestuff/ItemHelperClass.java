package com.r3.findmestuff;

public class ItemHelperClass {
    String Iuid,Iname, Idescription;


    public ItemHelperClass() {

    }



    public ItemHelperClass(String Iname, String Idescription,String Iuid) {
        this.Iname = Iname;
        this.Idescription = Idescription;


    }

    public String getIuid() {
        return Iuid;
    }

    public void setIuid(String iuid) {
        Iuid = iuid;
    }

    public String getIname() {
        return Iname;
    }

    public void setIname(String Iname) {
        this.Iname = Iname;
    }

    public String getIdescription() {
        return Idescription;
    }

    public void setIdescription(String Idescription) {
        this.Idescription = Idescription;
    }



}