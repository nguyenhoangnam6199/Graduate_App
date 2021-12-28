package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    ConnectDB connectDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Tạo database
        connectDB = new ConnectDB(this,"QuanLyDiem.db",null,1);

        //Tạo bảng khoa
        connectDB.QueryData("CREATE TABLE IF NOT EXISTS Khoa(MaKhoa VARCHAR(10) PRIMARY KEY, " +
                "TenKhoa VARCHAR(200) )");

        //Tạo bảng lớp
        connectDB.QueryData("CREATE TABLE IF NOT EXISTS Lop(MaLop VARCHAR(15) PRIMARY KEY, TenLop VARCHAR(200), MaKhoa VARCHAR(10), FOREIGN KEY(MaKhoa) REFERENCES Khoa(MaKhoa))");

        //Tạo bảng sinh viên
        connectDB.QueryData("CREATE TABLE IF NOT EXISTS SinhVien(MaSV VARCHAR(10) PRIMARY KEY, HoTen VARCHAR(200), NgaySinh VARCHAR(200), MaLop VARCHAR(15), FOREIGN KEY(MaLop) REFERENCES Lop(MaLop))");

        //Tạo bảng môn học
        connectDB.QueryData("CREATE TABLE IF NOT EXISTS MonHoc(MaMon VARCHAR(20) PRIMARY KEY, " +
                "TenMon VARCHAR(200) )");

        //Tạo bảng điểm thi
        connectDB.QueryData("CREATE TABLE IF NOT EXISTS DiemThi(MaSV VARCHAR(10), MaMon VARCHAR(20), LanThi VARCHAR(5), Diem INTEGER, " +
                "PRIMARY KEY(MaSV,MaMon,LanThi), FOREIGN KEY(MaSV) REFERENCES SinhVien(MaSV), FOREIGN KEY(MaMon) REFERENCES MonHoc(MaMon))");

        TextView username = findViewById(R.id.username);
        TextView password = findViewById(R.id.password);

        MaterialButton loginbtn = findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("")){
                    username.setError("Please enter your username !");
                    return;
                }
                else if(password.getText().toString().equals("")){
                    password.setError("Please enter your password !");
                    return;
                }
                else if(username.getText().toString().equals("hoangnam")&&password.getText().toString().equals("123456"))
                {
                    //Successfull Login
                    //Toast.makeText(MainActivity.this,"LOGIN",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,FunctionActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this,"Incorrect Login",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}