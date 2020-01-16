package com.kenyadevelopers.uadmin;

import com.google.firebase.database.Exclude;

public class Chick
{

   String downloadUrl;
   String name;
   String key;

    public Chick()
    {

    }

    public Chick(String downloadUrl, String name)
    {
        if(name==null||name.trim().equals(""))
        {
            name="Age 23 Nairobi";
        }
        else
            {
                this.downloadUrl = downloadUrl;
                this.name = name;
            }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
@Exclude
    public String getKey() {
        return key;
    }
@Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
