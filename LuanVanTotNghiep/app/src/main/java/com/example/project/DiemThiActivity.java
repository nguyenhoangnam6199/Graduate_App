package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adapter.DiemAdapter;
import com.example.project.Model.Diem;
import com.example.project.Model.MonHoc;
import com.example.project.Model.SinhVien;

import java.util.ArrayList;

public class DiemThiActivity extends AppCompatActivity {
    ConnectDB connectDB;
    ListView listView;
    ArrayList<Diem> arrayList;
    DiemAdapter adapter;
    Spinner spnMon, spnLanThi;
    ImageView btnThemDiem, btnThoatDiem;

    ArrayList<String> dsMonHoc = new ArrayList<>();
    ArrayList<String> dsLop = new ArrayList<>();
    ArrayList<String> dsSV = new ArrayList<>();

    String diemthi = "";

    String mamondemo = "";
    String lanthi1 = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diem_thi);

        //Tạo database
        connectDB = new ConnectDB(this,"QuanLyDiem.db",null,1);

        //Tạo bảng điểm thi
        connectDB.QueryData("CREATE TABLE IF NOT EXISTS DiemThi(MaSV VARCHAR(10), MaMon VARCHAR(20), LanThi VARCHAR(5), Diem INTEGER, " +
                "PRIMARY KEY(MaSV,MaMon,LanThi), FOREIGN KEY(MaSV) REFERENCES SinhVien(MaSV), FOREIGN KEY(MaMon) REFERENCES MonHoc(MaMon))");


        //Ánh xạ
        spnMon = findViewById(R.id.spnMon);
        spnLanThi = findViewById(R.id.spnLanThi);
        btnThemDiem = findViewById(R.id.btnAdd);
        btnThoatDiem = findViewById(R.id.btnExit);

        listView = findViewById(R.id.lvDSDiemThi);
        arrayList = new ArrayList<>();
        adapter = new DiemAdapter(this,R.layout.item_diemthi,arrayList);
        listView.setAdapter(adapter);

        //Thiết lập giá trị ô lần thi
        ArrayList<String> a = new ArrayList<>();
        a.clear();
        a.add("1");
        a.add("2");
        ArrayAdapter<String> a1 = new ArrayAdapter<String>(this,R.layout.spiner_layout,R.id.txt,a);
        spnLanThi.setAdapter(a1);

        //Thiết lập giá trị ô môn học
        ArrayList<String> b = new ArrayList<>();
        b.clear();
        b = getDSMonHoc();
        ArrayAdapter<String> a2 = new ArrayAdapter<String>(this,R.layout.spiner_layout,R.id.txt,b);
        spnMon.setAdapter(a2);

        String lanthi = spnLanThi.getSelectedItem().toString().trim();
        String mamon = LayMaTuTenMon(spnMon.getSelectedItem().toString().trim());

        mamondemo = mamon;
        lanthi1 = lanthi;

        //GetData(mamondemo,lanthi);

        ArrayList<String> arr1 = a;
        ArrayList<String> arr2 = b;

        spnLanThi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //hàm lấy dữ liệu
                String solan = arr1.get(position);
                lanthi1 = solan;
                GetData(mamondemo,solan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnMon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String mamon1 = LayMaTuTenMon(arr2.get(position));
                mamondemo = mamon1;
                GetData(mamon1,lanthi1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        GetData(mamondemo,lanthi1);

        //Xử lý sự kiện
        btnThoatDiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DiemThiActivity.this,FunctionActivity.class);
                startActivity(i);
            }
        });

        btnThemDiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInsert();
            }
        });


    }

    public void GetData(String mamon, String lanthi){
        Cursor data = connectDB.GetData("SELECT * FROM DiemThi WHERE MaMon = '"+mamon+"' AND LanThi='"+lanthi+"' ORDER BY MaSV ASC");
        arrayList.clear();
        while (data.moveToNext()){
           String masv = data.getString(0);
           int diem = data.getInt(3);

           Diem diemthi = new Diem(masv,lanthi,diem,LayTenSV(masv),LayMaLop(masv));
           arrayList.add(diemthi);
        }

        adapter.notifyDataSetChanged();
    }

    public String LayTenSV(String masv){
        String tenSV = null;
        Cursor data = connectDB.GetData("SELECT * FROM SinhVien WHERE MaSV='"+masv+"'");
        while (data.moveToNext()){
            tenSV = data.getString(1);
        }
        return tenSV;
    }

    public String LayMaLop(String masv){
        String malop = null;
        Cursor data = connectDB.GetData("SELECT * FROM SinhVien WHERE MaSV='"+masv+"'");
        while (data.moveToNext()){
            malop = data.getString(3);
        }
        return malop;
    }

    public ArrayList<String> getDSMonHoc(){
        dsMonHoc.clear();
        Cursor data = connectDB.GetData("SELECT * FROM MonHoc");
        while (data.moveToNext()){
            dsMonHoc.add(data.getString(1));
        }
        return dsMonHoc;
    }

    public ArrayList<String> getDSLop(){
        dsLop.clear();
        Cursor data = connectDB.GetData("SELECT * FROM Lop");
        while (data.moveToNext()){
            dsLop.add(data.getString(0));
        }
        return dsLop;
    }

    public ArrayList<String> getDSSVtheoLop(String malop){
        dsSV.clear();
        Cursor data = connectDB.GetData("SELECT * FROM SinhVien WHERE MaLop='"+malop+"'");
        while (data.moveToNext()){
            dsSV.add(data.getString(0)+"-"+data.getString(1));
        }
        return dsSV;
    }


    public String LayMaTuTenMon(String tenmon){
        String mamon = null;
        Cursor data = connectDB.GetData("SELECT * FROM MonHoc WHERE TenMon='"+tenmon+"'");
        while (data.moveToNext()){
            mamon = data.getString(0);
        }
        return mamon;
    }

    public String LayTenTuMaMon(String mamon){
        String tenmon = null;
        Cursor data = connectDB.GetData("SELECT * FROM MonHoc WHERE MaMon='"+mamon+"'");
        while (data.moveToNext()){
            tenmon = data.getString(1);
        }
        return tenmon;
    }

    public void DialogUpdate(String masv, String mamon,String lanthi){
        Dialog dialog = new Dialog(DiemThiActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_diemthi);

        //mamon = mamondemo;

        TextView tvTieuDe;
        LinearLayout lnLop, lnSV ,lnMH, lnLanThi;
        Button btnLuu, btnHuy;
        //Spinner spnMon1 = dialog.findViewById(R.id.spnMonHoc);

        //ánh xạ
        tvTieuDe = dialog.findViewById(R.id.tvTieuDe);
        lnLop  = dialog.findViewById(R.id.lnLop);
        lnSV = dialog.findViewById(R.id.lnSV);
        lnMH = dialog.findViewById(R.id.lnMH);
        lnLanThi = dialog.findViewById(R.id.lnLanThi);

        btnLuu = dialog.findViewById(R.id.btnLuu);
        btnHuy = dialog.findViewById(R.id.btnHuy);

        tvTieuDe.setText("CẬP NHẬT ĐIỂM");
        lnLop.setVisibility(View.GONE);
        lnSV.setVisibility(View.GONE);
        lnMH.setVisibility(View.GONE);
        lnLanThi.setVisibility(View.GONE);

        mamon = spnMon.getSelectedItem().toString().trim();

//        String tenmon = LayTenTuMaMon(mamon);
//        spnMon1.setSelection(getIndex(spnMon,tenmon));

        //Phần nhận dạng kí tự số
        DrawingView drawView;
        ImageButton drawBtn, eraseBtn, newBtn, saveBtn;
        float smallBrush, mediumBrush, largeBrush;
        Bitmap grayBitmap = null;
        LinearLayout inferencePreview;
        TextView mResultText;

        int PIXEL_WIDTH = 28;
        DigitsDetector mnistClassifier;

        drawView = dialog.findViewById(R.id.drawing);
        mnistClassifier = new DigitsDetector(this);
        mResultText = dialog.findViewById(R.id.detect);

        mediumBrush=20;
        largeBrush=30;

        drawView.setBrushSize(mediumBrush);

        drawBtn= dialog.findViewById(R.id.draw_btn);
        eraseBtn = dialog.findViewById(R.id.erase_btn);
        newBtn = dialog.findViewById(R.id.new_btn);
        saveBtn = dialog.findViewById(R.id.save_btn);

        drawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setColor("#FFFFFFFF");
                drawView.setBrushSize(mediumBrush);
                drawView.setLastBrushSize(largeBrush);
                drawView.setErase(false);
            }
        });

        eraseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setErase(false);
                drawView.setBrushSize(largeBrush);
                drawView.setLastBrushSize(largeBrush);

                drawView.setErase(true);
            }
        });

        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.startNew();
                mResultText.setText("Điểm = ? \n Độ chính xác = ?");
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setDrawingCacheEnabled(true);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(drawView.getDrawingCache(), PIXEL_WIDTH, PIXEL_WIDTH, true);


                String digit = mnistClassifier.classify((scaledBitmap));

                char[] ch = digit.toCharArray();

                char x = ch[7];

                diemthi = String.valueOf(x);

                //previewImage.setImageBitmap(scaledBitmap );
                if (digit != "") {
                    Toast.makeText(getApplicationContext(),
                            "" + digit, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),"Điểm"+diemthi,Toast.LENGTH_SHORT).show();
                    mResultText.setText(getString(R.string.found_digits, String.valueOf(digit)));
                    //mResultText.setText("Điểm = "+diemthi+"\nĐộ chính xác = "+ch[6]);
                } else {
                    mResultText.setText(getString(R.string.not_detected));
                }
                drawView.destroyDrawingCache();
            }
        });

        //Bắt sự kiện
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        String finalMamon = LayMaTuTenMon(mamon);
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String tenmon1 = spnMon1.getSelectedItem().toString().trim()
                int diem = Integer.valueOf(diemthi);
                if(diemthi.equals("")){
                    Toast.makeText(DiemThiActivity.this,"Bạn chưa nhập điểm !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    connectDB.QueryData("UPDATE DiemThi SET Diem = "+diem+" WHERE MaSV='"+masv+"' AND MaMon='"+ finalMamon +"' AND LanThi ='"+lanthi+"'");
                    Toast.makeText(DiemThiActivity.this,"Sửa thành công!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetData(finalMamon,lanthi);
                }
            }
        });
        dialog.show();
    }

    public void DialogInsert(){
        Dialog dialog = new Dialog(DiemThiActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_diemthi);

        Spinner spnLop, spnSV, spnMon1, spnLanThi1;
        Button btnLuu, btnHuy;

        btnLuu = dialog.findViewById(R.id.btnLuu);
        btnHuy = dialog.findViewById(R.id.btnHuy);

        //final int[] diemthi = new int[1];

        spnLop = dialog.findViewById(R.id.spnLop);
        spnSV = dialog.findViewById(R.id.spnSinhVien);
        spnMon1 = dialog.findViewById(R.id.spnMonHoc);
        spnLanThi1 = dialog.findViewById(R.id.spnLanThi);

        //spnSV.setEnabled(false);
        String malop;

        //Thả danh sách vào spinner

        //danh sách lớp
        ArrayList<String> a = new ArrayList<>();
        a.clear();
        a = getDSLop();
        ArrayAdapter<String> a1 = new ArrayAdapter<String>(this,R.layout.spiner_layout,R.id.txt,a);
        spnLop.setAdapter(a1);

        malop = spnLop.getSelectedItem().toString().trim();

        //danh sách sinh viên theo mã lớp
        ArrayList<String> b = new ArrayList<>();
        b.clear();
        b = getDSSVtheoLop(malop);
        ArrayAdapter<String> b1 = new ArrayAdapter<String>(this,R.layout.spiner_layout,R.id.txt,b);
        spnSV.setAdapter(b1);

        ArrayList<String> a2 = a;
        spnLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String malop1 = a2.get(position).toString().trim();
                ArrayList<String> b = new ArrayList<>();
                b.clear();
                b = getDSSVtheoLop(malop1);
                ArrayAdapter<String> b1 = new ArrayAdapter<String>(DiemThiActivity.this,R.layout.spiner_layout,R.id.txt,b);
                spnSV.setAdapter(b1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //Danh sách môn học
        ArrayList<String> c = new ArrayList<>();
        c.clear();
        c = getDSMonHoc();
        ArrayAdapter<String> c1 = new ArrayAdapter<String>(this,R.layout.spiner_layout,R.id.txt,c);
        spnMon1.setAdapter(c1);


        //Danh sách lần thi
        ArrayList<String> d = new ArrayList<>();
        d.clear();
        d.add("1");
        d.add("2");
        ArrayAdapter<String> d1 = new ArrayAdapter<String>(this,R.layout.spiner_layout,R.id.txt,d);
        spnLanThi1.setAdapter(d1);


        //Phần nhận dạng kí tự số
        DrawingView drawView;
        ImageButton drawBtn, eraseBtn, newBtn, saveBtn;
        float smallBrush, mediumBrush, largeBrush;
        Bitmap grayBitmap = null;
        LinearLayout inferencePreview;
        TextView mResultText;

        int PIXEL_WIDTH = 28;
        DigitsDetector mnistClassifier;

        drawView = dialog.findViewById(R.id.drawing);
        mnistClassifier = new DigitsDetector(this);
        mResultText = dialog.findViewById(R.id.detect);

        mediumBrush=20;
        largeBrush=30;

        drawView.setBrushSize(mediumBrush);

        drawBtn= dialog.findViewById(R.id.draw_btn);
        eraseBtn = dialog.findViewById(R.id.erase_btn);
        newBtn = dialog.findViewById(R.id.new_btn);
        saveBtn = dialog.findViewById(R.id.save_btn);

        drawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setColor("#FFFFFFFF");
                drawView.setBrushSize(mediumBrush);
                drawView.setLastBrushSize(largeBrush);
                drawView.setErase(false);
            }
        });

        eraseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setErase(false);
                drawView.setBrushSize(largeBrush);
                drawView.setLastBrushSize(largeBrush);

                drawView.setErase(true);
            }
        });

        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.startNew();
                mResultText.setText("Điểm = ? \n Độ chính xác = ?");
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setDrawingCacheEnabled(true);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(drawView.getDrawingCache(), PIXEL_WIDTH, PIXEL_WIDTH, true);


                String digit = mnistClassifier.classify((scaledBitmap));

                char[] ch = digit.toCharArray();

                char x = ch[7];

                diemthi = String.valueOf(x);

                //previewImage.setImageBitmap(scaledBitmap );
                if (digit != "") {
                    Toast.makeText(getApplicationContext(),
                            "" + digit, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),"Điểm"+diemthi,Toast.LENGTH_SHORT).show();
                    mResultText.setText(getString(R.string.found_digits, String.valueOf(digit)));
                    //mResultText.setText("Điểm = "+diemthi+"\nĐộ chính xác = "+ch[6]);
                } else {
                    mResultText.setText(getString(R.string.not_detected));
                }
                drawView.destroyDrawingCache();
            }
        });

        //Bắt sự kiện
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String masv = spnSV.getSelectedItem().toString().trim().substring(0,5);
                String tenmonhoc = spnMon1.getSelectedItem().toString().trim();
                String mamonhoc = LayMaTuTenMon(spnMon1.getSelectedItem().toString().trim());

                String lanthi = spnLanThi1.getSelectedItem().toString().trim();

                //Toast.makeText(DiemThiActivity.this,"Điểm"+diem,Toast.LENGTH_SHORT).show();
                if(diemthi.equals("")){
                    Toast.makeText(DiemThiActivity.this,"Bạn chưa nhập điểm !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(KtraDuLieu(masv,mamonhoc,lanthi)==true){
                    Toast.makeText(DiemThiActivity.this,"Đã có dữ liệu !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Integer diem = Integer.valueOf(diemthi);
                    connectDB.QueryData("INSERT INTO DiemThi VALUES('"+masv+"','"+mamonhoc+"','"+lanthi+"',"+diem+")");
                    Toast.makeText(DiemThiActivity.this,"Thêm thành công!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    spnMon.setSelection(getIndex(spnMon1,tenmonhoc));
                    spnLanThi.setSelection(getIndex(spnLanThi1,lanthi));
                    GetData(mamonhoc,lanthi);
                    diemthi = "";
                }
            }
        });
        dialog.show();
    }

    //Hàm thiết lập giá trị mong muốn cho Spiner
    public int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    //Hàm kiểm tr dữ liệu
    public boolean KtraDuLieu(String masv, String mamon, String lanthi){
        boolean kt = false;
        Cursor data = connectDB.GetData("SELECT * FROM DiemThi WHERE MaSV='"+masv+"'AND MaMon='"+mamon+"'AND LanThi='"+lanthi+"'");
        while (data.moveToNext()){
            kt = true;
        }
        return kt;
    }
}