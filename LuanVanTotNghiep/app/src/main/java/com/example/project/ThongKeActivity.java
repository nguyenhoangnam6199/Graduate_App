package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.GetChars;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ThongKeActivity extends AppCompatActivity {

    ConnectDB connectDB;
    Spinner spnLanThi;
    BarChart barChart;
    ImageView btnThoatTK;

    ArrayList<String> dsmonhoc = new ArrayList<>();
    List<Float> soluongqua = new ArrayList<>();
    List<Float> soluongrot = new ArrayList<>();

    BarDataSet barDataSet1, barDataSet2, barDataSet3;

    ArrayList barEntries;

    String solan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        //Tạo database
        connectDB = new ConnectDB(this,"QuanLyDiem.db",null,1);

        //Tạo bảng sinh viên
        connectDB.QueryData("CREATE TABLE IF NOT EXISTS DiemThi(MaSV VARCHAR(10), MaMon VARCHAR(20), LanThi VARCHAR(5), Diem INTEGER, " +
                "PRIMARY KEY(MaSV,MaMon,LanThi), FOREIGN KEY(MaSV) REFERENCES SinhVien(MaSV), FOREIGN KEY(MaMon) REFERENCES MonHoc(MaMon))");

        //Ánh xạ
        spnLanThi = findViewById(R.id.spnLanThi);
        barChart = findViewById(R.id.mbarChart);
        btnThoatTK = findViewById(R.id.btnExit);

        btnThoatTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ThongKeActivity.this,FunctionActivity.class);
                startActivity(i);
            }
        });

        //Thiết lập giá trị ô lần thi
        ArrayList<String> a = new ArrayList<>();
        a.clear();
        a.add("1");
        a.add("2");
        ArrayAdapter<String> a1 = new ArrayAdapter<String>(this,R.layout.spiner_layout,R.id.txt,a);
        spnLanThi.setAdapter(a1);

        String lanthi = spnLanThi.getSelectedItem().toString().trim();

        ArrayList<String> arr1 = a;
        spnLanThi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                solan = arr1.get(position);
                DrawChart(solan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(solan.equals("")==false){
            lanthi = solan;
        }

        DrawChart(lanthi);


    }

    public void DrawChart(String solan){
        ArrayList<String> dsmon1 = new ArrayList<>();
        dsmon1.clear();
        dsmon1 = LayMonHoc();
        ArrayList<Float> dsqua = new ArrayList<>();
        dsqua.clear();
        dsqua = (ArrayList<Float>)LaySoLuongQua(solan);
        ArrayList<Float> dsrot = new ArrayList<>();
        dsrot.clear();
        dsrot = (ArrayList<Float>) LaySoLuongRot(solan);


        //Thiết lập dữ liệu cho biểu đồ
        barDataSet1 = new BarDataSet(getBarEntriesOne(),"Số lượng qua");
        barDataSet1.setColor(Color.GREEN);
        barDataSet2 = new BarDataSet(getBarEntriesTwo(),"Số lượng rớt");
        barDataSet2.setColor(Color.BLUE);

        BarData data = new BarData(barDataSet1, barDataSet2);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dsmonhoc));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(3);
        float barSpace = 0.1f;
        float groupSpace = 0.5f;
        data.setBarWidth(0.15f);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.animate();
        barChart.groupBars(0, groupSpace, barSpace);
        barChart.animateY(2000);
        barChart.invalidate();
    }
    //Lấy danh sách tên môn học
    public ArrayList<String> LayMonHoc(){
        dsmonhoc.clear();
        Cursor data = connectDB.GetData("SELECT * FROM MonHoc ORDER BY MaMon ASC");
        while(data.moveToNext()){
            dsmonhoc.add(data.getString(1));
        }
        return  dsmonhoc;
    }

    //Lấy số lượng qua của từng môn
    public List<Float> LaySoLuongQua(String lanthi){
        ArrayList<String> dsmamon = new ArrayList<>();
        Cursor data = connectDB.GetData("SELECT * FROM MonHoc ORDER BY MaMon ASC");
        while(data.moveToNext()){
            dsmamon.add(data.getString(0));
        }
        soluongqua.clear();
        for(int i=0; i<dsmamon.size(); i++){
            //Cursor data1 = connectDB.GetData("SELECT COUNT(MaSV) FROM DiemThi WHERE LanThi='"+lanthi+"' AND Diem >= 5 AND MaMon='"+dsmamon.get(i)+"' GROUP BY MaMon");
            Cursor data1 = connectDB.GetData("SELECT COUNT(MaSV) FROM DiemThi WHERE LanThi='"+lanthi+"' AND Diem >= 5 AND MaMon='"+dsmamon.get(i)+"' ORDER BY MaMon ASC");
            while (data1.moveToNext()){
                if(data1.getFloat(0)== (float)0){
                    soluongqua.add((float) 0);
                }
                else{
                    soluongqua.add(data1.getFloat(0));
                }

            }
        }
        return soluongqua;
    }

    //Lấy số lượng rớt của từng môn
    public List<Float> LaySoLuongRot(String lanthi){
        ArrayList<String> dsmamon = new ArrayList<>();
        Cursor data = connectDB.GetData("SELECT * FROM MonHoc ORDER BY MaMon ASC");
        while(data.moveToNext()){
            dsmamon.add(data.getString(0));
        }
        soluongrot.clear();
        for(int i=0; i<dsmamon.size(); i++){
            //Cursor data1 = connectDB.GetData("SELECT COUNT(MaSV) FROM DiemThi WHERE LanThi='"+lanthi+"' AND Diem < 5 AND MaMon='"+dsmamon.get(i)+"' GROUP BY MaMon");
            Cursor data1 = connectDB.GetData("SELECT COUNT(MaSV) FROM DiemThi WHERE LanThi='"+lanthi+"' AND Diem < 5 AND MaMon='"+dsmamon.get(i)+"' ORDER BY MaMon ASC");
            while (data1.moveToNext()){
                if(data1.getFloat(0)== (float)0){
                    soluongrot.add((float) 0);
                }
                else{
                    soluongrot.add(data1.getFloat(0));
                }
            }
        }
        return soluongrot;
    }

    private ArrayList<BarEntry> getBarEntriesOne() {

        barEntries = new ArrayList<>();
        for(int i=1; i<=soluongqua.size(); i++){
            barEntries.add(new BarEntry((float)i,soluongqua.get(i-1)));
        }
        return barEntries;
    }

    private ArrayList<BarEntry> getBarEntriesTwo() {
        barEntries = new ArrayList<>();
        for(int i=1; i<=soluongrot.size(); i++){
            barEntries.add(new BarEntry((float)i,soluongrot.get(i-1)));
        }
        return barEntries;
    }


}