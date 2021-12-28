package com.example.project.Model;

public class SinhVien {
    private String maSV, tenSV, ngaySinh;
    private Lop maLop;

    public SinhVien(String maSV, String tenSV, String ngaySinh, Lop maLop) {
        this.maSV = maSV;
        this.tenSV = tenSV;
        this.ngaySinh = ngaySinh;
        this.maLop = maLop;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getTenSV() {
        return tenSV;
    }

    public void setTenSV(String tenSV) {
        this.tenSV = tenSV;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public Lop getMaLop() {
        return maLop;
    }

    public void setMaLop(Lop maLop) {
        this.maLop = maLop;
    }
}
