package com.example.log_up;

import java.io.Serializable;
import java.util.HashMap;

public class ProductObject implements Serializable {
    private int id;
    private String danhmucSanPham;
    private String tenSanPham;
    private String giaSanPham;
    private String tieudeSanPham;
    private String motaSanPham;
    private String imageUrl;
    private String verify;
    private String diachiSanPham;
    private String sodienthoaiUser;
    private String checkHeart;

    public ProductObject() {
    }

    public ProductObject(int id, String danhmucSanPham, String tenSanPham, String giaSanPham, String tieudeSanPham, String motaSanPham,String diachiSanPham ,String checkHeart,String verify) {
        this.id = id;
        this.danhmucSanPham = danhmucSanPham;
        this.tenSanPham = tenSanPham;
        this.giaSanPham = giaSanPham;
        this.tieudeSanPham = tieudeSanPham;
        this.motaSanPham = motaSanPham;
        this.diachiSanPham = diachiSanPham;
        this.checkHeart = checkHeart;
        this.verify = verify;
    }

    public int getId() {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }

    public String getDanhmucSanPham() {
        return danhmucSanPham;
    }


    public String getTenSanPham() {
        return tenSanPham;
    }



    public String getGiaSanPham() {
        return giaSanPham;
    }



    public String getTieudeSanPham() {
        return tieudeSanPham;
    }

    public String getDiachiSanPham(){ return diachiSanPham; }

    public String getMotaSanPham() {
        return motaSanPham;
    }

    public String getCheckHeart()
    {
        return checkHeart;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVerify() {return verify;}

    public String getSodienthoaiUser() {
        return sodienthoaiUser;
    }

    public void setDanhmucSanPham(String danhmucSanPham) {
        this.danhmucSanPham = danhmucSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public void setGiaSanPham(String giaSanPham) {
        this.giaSanPham = giaSanPham;
    }

    public void setTieudeSanPham(String tieudeSanPham) {
        this.tieudeSanPham = tieudeSanPham;
    }

    public void setMotaSanPham(String motaSanPham) {
        this.motaSanPham = motaSanPham;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public void setDiachiSanPham(String diachiSanPham) {
        this.diachiSanPham = diachiSanPham;
    }

    public void setSodienthoaiUser(String sodienthoaiUser) {
        this.sodienthoaiUser = sodienthoaiUser;
    }

    public void setCheckHeart(String checkHeart) {
        this.checkHeart = checkHeart;
    }
}
