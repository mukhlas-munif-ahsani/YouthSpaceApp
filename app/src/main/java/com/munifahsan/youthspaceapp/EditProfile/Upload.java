package com.munifahsan.youthspaceapp.EditProfile;

public class Upload {
    private String mImageName;
    private String mImageUrl;
    public Upload() {
        //empty constructor needed
    }
    public Upload(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        mImageName = name;
        mImageUrl = imageUrl;
    }
    public String getName() {
        return mImageName;
    }
    public void setName(String name) {
        mImageName = name;
    }
    public String getImageUrl() {
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}
