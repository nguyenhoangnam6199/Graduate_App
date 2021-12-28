package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adapter.MonHocAdapter;
import com.example.project.Model.Khoa;
import com.example.project.Model.MonHoc;

import java.util.ArrayList;

public class MonHocActivity extends AppCompatActivity {

    ConnectDB connectDB;
    ListView listView;
    ArrayList<MonHoc> arrayList;
    MonHocAdapter adapter;
    ImageView btnThemMH, btnThoatMH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_hoc);

        //Tạo database
        connectDB = new ConnectDB(this,"QuanLyDiem.db",null,1);

        //Tạo bảng môn học
        connectDB.QueryData("CREATE TABLE IF NOT EXISTS MonHoc(MaMon VARCHAR(20) PRIMARY KEY, " +
                "TenMon VARCHAR(200) )");

        //Ánh xạ
        listView = findViewById(R.id.lvDSMon);
        btnThemMH = findViewById(R.id.btnAdd);
        btnThoatMH = findViewById(R.id.btnExit);
        arrayList = new ArrayList<>();
        adapter = new MonHocAdapter(this,R.layout.item_monhoc,arrayList);
        listView.setAdapter(adapter);

        //Xử lý sự kiện
        btnThoatMH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MonHocActivity.this,FunctionActivity.class);
                startActivity(i);
            }
        });

        btnThemMH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInsert();
            }
        });

        GetData();
    }

    //Hàm thêm mới
    public void DialogInsert(){
        Dialog dialog = new Dialog(MonHocActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_monhoc);

        //Ánh xạ
        EditText edtMaMon, edtTenMon;
        Button btnLuu, btnHuy;

        edtMaMon = dialog.findViewById(R.id.edtMaMon);
        edtTenMon = dialog.findViewById(R.id.edtTenMon);
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
                String mamon = edtMaMon.getText().toString().trim().toUpperCase();
                String tenmon = ChuanHoa(edtTenMon.getText().toString().trim().toLowerCase());

                if(mamon.isEmpty()){
                    edtMaMon.setError("Mã môn không được trống !");
                    edtMaMon.requestFocus();
                    return;
                }
                else if(mamon.length()>15){
                    edtMaMon.setError("Mã môn không được quá 15 kí tự !");
                    edtMaMon.requestFocus();
                    return;
                }
                else if(tenmon.isEmpty()){
                    edtTenMon.setError("Tên môn không được trống !");
                    edtTenMon.requestFocus();
                    return;
                }
               else if(CheckMaMon(mamon)){
                    edtMaMon.setError("Mã môn đã tồn tại !");
                    edtMaMon.requestFocus();
                    return;
                }
               else if(CheckTenMon(tenmon)){
                    edtTenMon.setError("Tên môn đã tồn tại !");
                    edtTenMon.requestFocus();
                    return;
                }
                else{
                    connectDB.QueryData("INSERT INTO MonHoc VALUES('"+mamon+"','"+tenmon+"')");
                    Toast.makeText(MonHocActivity.this,"Thêm thành công!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetData();
                }
            }
        });
        dialog.show();
    }

    //Hàm sửa thông tin
    public void DialogUpdate(String mamon, String tenmon){
        Dialog dialog = new Dialog(MonHocActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_monhoc);

        //Ánh xạ
        EditText edtMaMon, edtTenMon;
        Button btnLuu, btnHuy;
        TextView tvTieuDe;

        edtMaMon = dialog.findViewById(R.id.edtMaMon);
        edtTenMon = dialog.findViewById(R.id.edtTenMon);
        btnLuu = dialog.findViewById(R.id.btnLuu);
        btnHuy = dialog.findViewById(R.id.btnHuy);
        tvTieuDe = dialog.findViewById(R.id.tvTieuDe);

        tvTieuDe.setText("CẬP NHẬT THÔNG TIN");
        edtMaMon.setText(mamon);
        edtMaMon.setEnabled(false);
        edtTenMon.setText(tenmon);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mamon1 = edtMaMon.getText().toString().trim().toUpperCase();
                String tenmon1 = ChuanHoa(edtTenMon.getText().toString().trim().toLowerCase());
                if(mamon1.isEmpty()){
                    edtMaMon.setError("Mã môn không được trống !");
                    edtMaMon.requestFocus();
                    return;
                }
                else if(tenmon1.isEmpty()){
                    edtTenMon.setError("Tên môn không được trống !");
                    edtTenMon.requestFocus();
                    return;
                }
                else if(CheckTenMon(tenmon1)){
                    edtTenMon.setError("Tên môn đã tồn tại !");
                    edtTenMon.requestFocus();
                    return;
                }
                else{
                    connectDB.QueryData("UPDATE MonHoc SET TenMon = '"+tenmon1+"' WHERE MaMon = '"+mamon+"'");
                    Toast.makeText(MonHocActivity.this,"Cập nhật thành công!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetData();
                }
            }
        });
        dialog.show();
    }

    //Hàm xóa
    public void DialogDelete(String mamon, String tenmon){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc muốn xóa môn: "+tenmon+" hay không ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(checkXoa(mamon)){
                    Toast.makeText(MonHocActivity.this,"Không thể xóa do có bảng tham chiếu tới!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    connectDB.QueryData("DELETE FROM MonHoc WHERE MaMon='"+mamon+"'");
                    GetData();
                    Toast.makeText(MonHocActivity.this,"Xóa thành công!",Toast.LENGTH_SHORT).show();
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

    public boolean checkXoa(String mamon){
        boolean kt = false;
        Cursor data = connectDB.GetData("SELECT * FROM DiemThi WHERE MaMon = '"+mamon+"' ");
        while(data.moveToNext()){
            kt = true;
        }
        return kt;
    }
    public boolean CheckTenMon(String tenmon){
        boolean kt = false;
        Cursor data = connectDB.GetData("SELECT * FROM MonHoc WHERE TenMon = '"+tenmon+"' ");
        while(data.moveToNext()){
            kt = true;
        }
        return kt;
    }

    //Kiểm tra mã môn, tên môn
    public boolean CheckMon(String mamon, String tenmon){
        boolean kt = false;
        Cursor data = connectDB.GetData("SELECT * FROM MonHoc WHERE MaMon ='"+mamon+"' OR TenMon ='"+tenmon+"'");
        while(data.moveToNext()){
            kt = true;
        }
        return kt;
    }

    //Kiểm tra mã môn, tên môn
    public boolean CheckMaMon(String mamon){
        boolean kt = false;
        Cursor data = connectDB.GetData("SELECT * FROM MonHoc WHERE MaMon ='"+mamon+"'");
        while(data.moveToNext()){
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

    public void GetData(){
        Cursor data = connectDB.GetData("SELECT * FROM MonHoc ORDER BY MaMon ASC");
        arrayList.clear();
        while(data.moveToNext()){
            arrayList.add(new MonHoc(data.getString(0),data.getString(1)));
        }
        adapter.notifyDataSetChanged();
    }
}