package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adapter.KhoaAdapter;
import com.example.project.Model.Khoa;

import java.util.ArrayList;

public class KhoaActivity extends AppCompatActivity {

    ConnectDB connectDB;
    ListView listView;
    ArrayList<Khoa> arrayList;
    KhoaAdapter adapter;
    ImageView btnThemKhoa, btnThoatKhoa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khoa);

        //Tạo database
        connectDB = new ConnectDB(this,"QuanLyDiem.db",null,1);

        //Tạo bảng khoa
        connectDB.QueryData("CREATE TABLE IF NOT EXISTS Khoa(MaKhoa VARCHAR(10) PRIMARY KEY, " +
                "TenKhoa VARCHAR(200) )");

        //Tạo dữ liệu
//        connectDB.QueryData("INSERT INTO Khoa VALUES ('CNTT','Công Nghệ Thông Tin')");
//        connectDB.QueryData("INSERT INTO Khoa VALUES ('QTKD','Quản Trị Kinh Doanh')");

        //Ánh xạ
        listView = findViewById(R.id.lvDSKhoa);
        btnThemKhoa = findViewById(R.id.btnAdd);
        btnThoatKhoa = findViewById(R.id.btnExit);
        arrayList = new ArrayList<>();
        adapter = new KhoaAdapter(this,R.layout.item_khoa,arrayList);
        listView.setAdapter(adapter);

        //Xử lý sự kiện
        btnThoatKhoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(KhoaActivity.this,FunctionActivity.class);
                startActivity(i);
            }
        });

        btnThemKhoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInsert();
            }
        });

        //Hiện dữ liệu
        GetData();


    }

    //Hàm thêm mới
    public void DialogInsert(){
        Dialog dialog = new Dialog(KhoaActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_khoa);

        //Ánh xạ
        EditText edtMaKhoa, edtTenKhoa;
        Button btnLuu, btnHuy;

        edtMaKhoa = dialog.findViewById(R.id.edtMaKhoa);
        edtTenKhoa = dialog.findViewById(R.id.edtTenKhoa);
        btnLuu = dialog.findViewById(R.id.btnLuu);
        btnHuy = dialog.findViewById(R.id.btnHuy);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String makhoa = edtMaKhoa.getText().toString().trim().toUpperCase();
                String tenkhoa = ChuanHoa(edtTenKhoa.getText().toString().trim().toLowerCase());

                if(makhoa.isEmpty()){
                    edtMaKhoa.setError("Mã khoa không được trống !");
                    edtMaKhoa.requestFocus();
                    return;
                }
                else if(tenkhoa.isEmpty()){
                    edtTenKhoa.setError("Tên khoa không được trống !");
                    edtTenKhoa.requestFocus();
                    return;
                }
                else if(CheckMaKhoa(makhoa)){
                    Toast.makeText(KhoaActivity.this,"Mã khoa đã tồn tại !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(CheckTenKhoa(tenkhoa)){
                    Toast.makeText(KhoaActivity.this,"Tên khoa đã tồn tại !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    connectDB.QueryData("INSERT INTO Khoa VALUES('"+makhoa+"','"+tenkhoa+"')");
                    Toast.makeText(KhoaActivity.this,"Thêm thành công!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetData();
                }
            }
        });
    dialog.show();
    }

    //Hàm sửa thông tin
    public void DialogUpdate(String makhoa, String tenkhoa){
        Dialog dialog = new Dialog(KhoaActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_khoa);

        //Ánh xạ
        EditText edtMaKhoa, edtTenKhoa;
        Button btnLuu, btnHuy;
        TextView tvTieuDe;

        edtMaKhoa = dialog.findViewById(R.id.edtMaKhoa);
        edtTenKhoa = dialog.findViewById(R.id.edtTenKhoa);
        btnLuu = dialog.findViewById(R.id.btnLuu);
        btnHuy = dialog.findViewById(R.id.btnHuy);
        tvTieuDe = dialog.findViewById(R.id.tvTieuDe);

        tvTieuDe.setText("CẬP NHẬT THÔNG TIN");
        edtMaKhoa.setText(makhoa);
        edtMaKhoa.setEnabled(false);
        edtTenKhoa.setText(tenkhoa);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String makhoa1 = edtMaKhoa.getText().toString().trim().toUpperCase();
                String tenkhoa1 = ChuanHoa(edtTenKhoa.getText().toString().trim());

                if(makhoa1.isEmpty()){
                    edtMaKhoa.setError("Mã khoa không được trống !");
                    edtMaKhoa.requestFocus();
                    return;
                }
                else if(tenkhoa1.isEmpty()){
                    edtTenKhoa.setError("Tên khoa không được trống !");
                    edtTenKhoa.requestFocus();
                    return;
                }
                else if(CheckTenKhoa(tenkhoa1)){
                    Toast.makeText(KhoaActivity.this,"Tên khoa đã tồn tại !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    connectDB.QueryData("UPDATE Khoa SET TenKhoa = '"+tenkhoa1+"' WHERE MaKhoa = '"+makhoa+"'");
                    Toast.makeText(KhoaActivity.this,"Cập nhật thành công!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetData();
                }
            }
        });
        dialog.show();
    }

    //Hàm xóa
    public void DialogDelete(String makhoa, String tenkhoa){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc muốn xóa khoa: "+tenkhoa+" hay không ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(CheckXoa(makhoa)){
                    Toast.makeText(KhoaActivity.this,"Không thể xóa do có bảng tham chiếu tới!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    connectDB.QueryData("DELETE FROM Khoa WHERE MaKhoa='"+makhoa+"'");
                    GetData();
                    Toast.makeText(KhoaActivity.this,"Xóa thành công!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    //Hàm check trước khi xóa
    public boolean CheckXoa(String makhoa){
        boolean kt = false;
        Cursor data = connectDB.GetData("SELECT * FROM Lop WHERE MaKhoa='"+makhoa+"'");
        while (data.moveToNext()){
            kt = true;
        }
        return kt;
    }
    //Hàm format tên
    public String ChuanHoa(String chuoi){
        char[] a = chuoi.toCharArray();
        boolean kt = true;
        for(int i=0; i<a.length;i++){
            //Nếu phần tử là 1 chữ cái
            if(Character.isLetter(a[i])){
                //kiểm tra khoảng trắng trc chữ cái
                if(kt){
                    a[i] = Character.toUpperCase(a[i]);
                    kt = false;
                }
            }
            else{
                kt = true;
            }
        }
        String ketqua = String.valueOf(a);
        return ketqua;
    }

    //Kiểm tra mã khoa, tên khoa
    public boolean CheckKhoa(String makhoa, String tenkhoa){
        boolean kt = false;
        Cursor data = connectDB.GetData("SELECT * FROM Khoa WHERE MaKhoa ='"+makhoa+"' OR TenKhoa ='"+tenkhoa+"'");
        while(data.moveToNext()){
            kt = true;
        }
        return kt;
    }

    public boolean CheckMaKhoa(String makhoa){
        boolean kt = false;
        Cursor data = connectDB.GetData("SELECT * FROM Khoa WHERE MaKhoa = '"+makhoa.toUpperCase()+"' ");
        while(data.moveToNext()){
            kt = true;
        }
        return kt;
    }

    public boolean CheckTenKhoa(String tenkhoa){
        boolean kt = false;
        Cursor data = connectDB.GetData("SELECT * FROM Khoa WHERE TenKhoa = '"+ChuanHoa(tenkhoa)+"' ");
        while(data.moveToNext()){
            kt = true;
        }
        return kt;
    }

    public void GetData(){
        Cursor data = connectDB.GetData("SELECT * FROM Khoa ORDER BY MaKhoa ASC");
        arrayList.clear();
        while(data.moveToNext()){
            arrayList.add(new Khoa(data.getString(0),data.getString(1)));
        }
        adapter.notifyDataSetChanged();
    }
}