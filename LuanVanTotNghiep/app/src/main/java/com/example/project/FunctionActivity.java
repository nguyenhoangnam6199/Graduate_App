package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FunctionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        CardView qlkhoa = findViewById(R.id.qlkhoa);
        CardView qllop = findViewById(R.id.qllop);
        CardView qlsinhvien = findViewById(R.id.qlsinhvien);
        CardView qlmonhoc = findViewById(R.id.qlmonhoc);
        CardView qldiem = findViewById(R.id.qldiem);
        CardView qlthongke = findViewById(R.id.thongke);

        //Quản Lý Khoa
        qlkhoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FunctionActivity.this,KhoaActivity.class);
                startActivity(intent);
            }
        });

        //Quản lý lớp
        qllop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FunctionActivity.this,LopActivity.class);
                startActivity(intent);
            }
        });

        //Quản lý sinh viên
        qlsinhvien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FunctionActivity.this,SinhVienActivity.class);
                startActivity(intent);
            }
        });

        //Quản lý môn học
        qlmonhoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FunctionActivity.this,MonHocActivity.class);
                startActivity(intent);
            }
        });
        //Quản lý điểm
        qldiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FunctionActivity.this,DiemThiActivity.class);
                startActivity(intent);
            }
        });
        //Quản lý thống kê
        qlthongke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FunctionActivity.this,ThongKeActivity.class);
                startActivity(intent);
            }
        });

    }
}