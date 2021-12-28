package com.example.project.Model;

public class Diem {
    private String maSV, maMH, lanThi;
    private Integer diemThi;
    private String tenSV, tenLop;

    public Diem(String maSV, String lanThi, Integer diemThi, String tenSV, String tenLop) {
        this.maSV = maSV;
        this.lanThi = lanThi;
        this.diemThi = diemThi;
        this.tenSV = tenSV;
        this.tenLop = tenLop;
    }

    public Diem(String maSV, String maMH, String lanThi, Integer diemThi) {
        this.maSV = maSV;
        this.maMH = maMH;
        this.lanThi = lanThi;
        this.diemThi = diemThi;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getLanThi() {
        return lanThi;
    }

    public void setLanThi(String lanThi) {
        this.lanThi = lanThi;
    }

    public Integer getDiemThi() {
        return diemThi;
    }

    public void setDiemThi(Integer diemThi) {
        this.diemThi = diemThi;
    }
}
