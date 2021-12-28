package com.example.project.Model;

public class MonHoc {
    private String MaMon, TenMon;

    public MonHoc(String maMon, String tenMon) {
        MaMon = maMon;
        TenMon = tenMon;
    }

    public String getMaMon() {
        return MaMon;
    }

    public void setMaMon(String maMon) {
        MaMon = maMon;
    }

    public String getTenMon() {
        return TenMon;
    }

    public void setTenMon(String tenMon) {
        TenMon = tenMon;
    }
}
