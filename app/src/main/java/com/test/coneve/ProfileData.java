package com.test.coneve;

public class ProfileData {
    public String uid;
    public String name;
    public String emailid;
    public String photourl;
    public TagWord interests;
    public Boolean organiser;

    public ProfileData()
    {
        organiser = false;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public TagWord getInterests() {
        return interests;
    }

    public void setInterests(TagWord interests) {
        this.interests = interests;
    }

    public Boolean getOrganiser() {
        return organiser;
    }

    public void setOrganiser(Boolean organiser) {
        this.organiser = organiser;
    }
}
