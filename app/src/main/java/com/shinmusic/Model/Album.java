package com.shinmusic.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Album implements Serializable {

@SerializedName("IdAlbum")
@Expose
private String idAlbum;
@SerializedName("TenAlbum")
@Expose
private String tenAlbum;
@SerializedName("TenCaSy")
@Expose
private String tenCaSy;
@SerializedName("ImageAlbum")
@Expose
private String imageAlbum;

public String getIdAlbum() {
return idAlbum;
}

public void setIdAlbum(String idAlbum) {
this.idAlbum = idAlbum;
}

public String getTenAlbum() {
return tenAlbum;
}

public void setTenAlbum(String tenAlbum) {
this.tenAlbum = tenAlbum;
}

public String getTenCaSy() {
return tenCaSy;
}

public void setTenCaSy(String tenCaSy) {
this.tenCaSy = tenCaSy;
}

public String getImageAlbum() {
return imageAlbum;
}

public void setImageAlbum(String imageAlbum) {
this.imageAlbum = imageAlbum;
}

}