package com.jatpack.socialmediahub.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MediaData implements Parcelable {
    private int key_id;
    private String id;
    private String Name;
    private String path;
    private String bucket_id;
    private String bucket_name;
    private String date;
    private int effect_id;
    private String effect_name;
    private String effect_description = "NA";
    private int effect_sub_id;
    private boolean isChecked;
    private String mTag;
    private Boolean isSelected=false;

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }


    public void setTag(String mTag) {
        this.mTag = mTag;
    }

    public String getTag() {
        return this.mTag;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }


    public boolean getChecked() {
        return this.isChecked;
    }

    public int getEffect_sub_id() {
        return effect_sub_id;
    }

    public void setEffect_sub_id(int effect_sub_id) {
        this.effect_sub_id = effect_sub_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBucket_id() {
        return bucket_id;
    }

    public void setBucket_id(String bucket_id) {
        this.bucket_id = bucket_id;
    }

    public String getBucket_name() {
        return bucket_name;
    }

    public void setBucket_name(String bucket_name) {
        this.bucket_name = bucket_name;
    }


    public void setName(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getEffect_name() {
        return effect_name;
    }

    public void setEffect_name(String effect_name) {
        this.effect_name = effect_name;
    }

    public String getEffect_description() {
        return effect_description;
    }

    public void setEffect_description(String effect_description) {
        this.effect_description = effect_description;
    }

    public int getEffect_id() {
        return effect_id;
    }

    public void setEffect_id(int effect_id) {
        this.effect_id = effect_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.key_id);
        dest.writeString(this.id);
        dest.writeString(this.Name);
        dest.writeString(this.path);
        dest.writeString(this.bucket_id);
        dest.writeString(this.bucket_name);
        dest.writeString(this.date);
    }

    public MediaData() {

    }

    public MediaData(Parcel source) {
        this.key_id = source.readInt();
        this.id = source.readString();
        this.Name = source.readString();
        this.path = source.readString();
        this.bucket_id = source.readString();
        this.bucket_name = source.readString();
        this.date = source.readString();
    }

    public MediaData(int key_id, String id, String Name, String path, String bucket_id, String bucket_name, String date) {
        this.key_id = key_id;
        this.id = id;
        this.Name = Name;
        this.path = path;
        this.bucket_id = bucket_id;
        this.bucket_name = bucket_name;
        this.date = date;
    }

    public static final Creator<MediaData> CREATOR = new Creator<MediaData>() {

        public MediaData createFromParcel(Parcel source) {
            return new MediaData(source);
        }

        public MediaData[] newArray(int size) {
            return new MediaData[size];
        }

    };
}
