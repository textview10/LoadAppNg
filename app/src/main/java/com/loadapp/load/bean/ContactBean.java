package com.loadapp.load.bean;

/**
 * @author xu.wang
 * @date 2020/2/3 17:24
 * @desc
 */
public class ContactBean {

    public String id;

    public String number;

    public String contactName;

    public String photoUri;

    public String ringTonePath;

    public ContactBean(String id, String number, String contactName, String photoUri, String ringTone) {
        this.id = id;
        this.number = number;
        this.contactName = contactName;
        this.photoUri = photoUri;
        this.ringTonePath = ringTone;
    }
}
