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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adapter.LopAdapter;
import com.example.project.Model.Khoa;
import com.example.project.Model.Lop;

import java.util.ArrayList;

public class LopActivity extends AppCompatActivity {

    ConnectDB connectDB;
    ListView listView;
    ArrayList<Lop> arrayList;
    LopAdapter adapter;
    ImageView btnThemLop, btnThoatLop;
    Spinner spnKhoa;

    ArrayList<String> dsKhoa = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lop);

        //Tạo database
        connectDB = new ConnectDB(this,"QuanLyDiem.db",null,1);

        //Tạo bảng lớp
        connectDB.QueryData("CREATE TABLE IF NOT EXISTS Lop(MaLop VARCHAR(15) PRIMARY KEY, TenLop VARCHAR(200), MaKhoa VARCHAR(10), FOREIGN KEY(MaKhoa) REFERENCES Khoa(MaKhoa))");

        //Tạo bản ghi
//        connectDB.QueryData("INSERT INTO Lop VALUES ('D17CQCP01-N','Lớp phần mềm 1 khóa 2017','CNTT')");
//        connectDB.QueryData("INSERT INTO Lop VALUES ('D17CQQT01-N','Lớp quản trị 1 khóa 2017','QTKD')");


        btnThemLop = findViewById(R.id.btnAdd);
        btnThoatLop = findViewById(R.id.btnExit);
        spnKhoa = findViewById(R.id.spnKhoa);
        listView = findViewById(R.id.lvDSLop);
        arrayList = new ArrayList<>();
        adapter = new LopAdapter(this,R.layout.item_lop,arrayList);
        listView.setAdapter(adapter);

        ArrayList<String> a = new ArrayList<>();
        a.clear();
        a = getDSKhoa();
        ArrayAdapter<String> a1 = new ArrayAdapter<String>(this,R.layout.spiner_layout,R.id.txt,a);
        spnKhoa.setAdapter(a1);
//      listView.setVisibility(View.GONE);

        //Xử lý sự kiện
        btnThoatLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LopActivity.this,FunctionActivity.class);
                startActivity(i);
            }
        });

        btnThemLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInsert();
            }
        });

        String tenkhoa = spnKhoa.getSelectedItem().toString().trim();
        String makhoa = LayMaTuten(tenkhoa);

        ArrayList<String> arr1 = a;
        spnKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String t1 = arr1.get(position);
                GetData(LayMaTuten(t1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        GetData(makhoa);
    }

    public void DialogInsert(){
        Dialog dialog = new Dialog(LopActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_lop);

        //Ánh xạ
        Spinner spnKhoa1;
        EditText edtMaLop, edtTenLop;
        Button btnLuu, btnHuy;

        spnKhoa1 = dialog.findViewById(R.id.spnKhoa);
        edtMaLop = dialog.findViewById(R.id.edtMaLop);
        edtTenLop = dialog.findViewById(R.id.edtTenLop);
        btnLuu = dialog.findViewById(R.id.btnLuu);
        btnHuy = dialog.findViewById(R.id.btnHuy);

        ArrayList<String> a = new ArrayList<>();
        a.clear();
        a = getDSKhoa();
        ArrayAdapter<String> a1 = new ArrayAdapter<String>(this,R.layout.spiner_layout,R.id.txt,a);
        spnKhoa1.setAdapter(a1);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenkhoa = spnKhoa1.getSelectedItem().toString().trim();
                String makhoa = LayMaTuten(tenkhoa);
                String malop = edtMaLop.getText().toString().trim().toUpperCase();
                String tenlop = ChuanHoa(edtTenLop.getText().toString().trim().toLowerCase());

                if(malop.isEmpty()){
                    edtMaLop.setError("Mã lớp không được trống !");
                    edtMaLop.requestFocus();
                    return;
                }
                else if(malop.length()>15){
                    edtMaLop.setError("Mã lớp không được quá 15 kí tự !");
                    edtMaLop.requestFocus();
                    return;
                }
                else if(tenlop.isEmpty()){
                    edtTenLop.setError("Tên lớp không được trống !");
                    edtTenLop.requestFocus();
                    return;
                }
                else if(CheckMaLop(malop)){
                    edtMaLop.setError("Mã lớp đã tồn tại !");
                    edtMaLop.requestFocus();
                    return;
                }
                else if(CheckTenLop(tenlop)){
                    edtTenLop.setError("Tên lớp đã tồn tại !");
                    edtTenLop.requestFocus();
                    return;
                }
                else{
                    connectDB.QueryData("INSERT INTO Lop VALUES('"+malop+"','"+tenlop+"','"+makhoa+"')");
                    Toast.makeText(LopActivity.this,"Thêm thành công!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    spnKhoa.setSelection(getIndex(spnKhoa1,tenkhoa));
                    GetData(makhoa);
                }
            }
        });
        dialog.show();
    }

    //Hàm kiểm tra mã lớp
    public boolean CheckMaLop(String malop){
        boolean kt = false;
        Cursor data = connectDB.GetData("SELECT * FROM Lop WHERE MaLop = '"+malop.toUpperCase()+"' ");
        while(data.moveToNext()){
            kt = true;
        }
        return kt;
    }

    public boolean CheckTenLop(String tenlop){
        boolean kt = false;
        Cursor data = connectDB.GetData("SELECT * FROM Lop WHERE TenLop = '"+tenlop+"' ");
        while(data.moveToNext()){
            kt = true;
        }
        return kt;
    }

    //Hàm sửa thông tin lớp
    public void DialogUpdate(String malop, String tenlop, String makhoa){
        Dialog dialog = new Dialog(LopActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_lop);

        //Ánh xạ
        Spinner spnKhoa1;
        EditText edtMaLop, edtTenLop;
        Button btnLuu, btnHuy;
        TextView tvTieuDe;

        tvTieuDe = dialog.findViewById(R.id.tvTieuDe);
        spnKhoa1 = dialog.findViewById(R.id.spnKhoa);
        edtMaLop = dialog.findViewById(R.id.edtMaLop);
        edtTenLop = dialog.findViewById(R.id.edtTenLop);
        btnLuu = dialog.findViewById(R.id.btnLuu);
        btnHuy = dialog.findViewById(R.id.btnHuy);

        ArrayList<String> a = new ArrayList<>();
        a.clear();
        a = getDSKhoa();
        ArrayAdapter<String> a1 = new ArrayAdapter<String>(this,R.layout.spiner_layout,R.id.txt,a);
        spnKhoa1.setAdapter(a1);

        tvTieuDe.setText("CẬP NHẬT THÔNG TIN");
        edtMaLop.setEnabled(false);
        edtMaLop.setText(malop);
        edtTenLop.setText(tenlop);
        spnKhoa1.setSelection(getIndex(spnKhoa,LayTenTuMa(makhoa)));

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenkhoa = spnKhoa1.getSelectedItem().toString().trim();
                String makhoa = LayMaTuten(tenkhoa);
                String malop = edtMaLop.getText().toString().trim().toUpperCase();
                String tenlop = ChuanHoa(edtTenLop.getText().toString().trim().toLowerCase());
                if(malop.length()>15){
                    edtMaLop.setError("Mã lớp không được quá 10 kí tự !");
                    edtMaLop.requestFocus();
                    return;
                }
                else if(tenlop.isEmpty()){
                    edtTenLop.setError("Tên lớp không được trống !");
                    edtTenLop.requestFocus();
                    return;
                }
                else if(CheckTenLop(tenlop)){
                    edtTenLop.setError("Tên lớp đã tồn tại !");
                    edtTenLop.requestFocus();
                    return;
                }
                else{
                    connectDB.QueryData("UPDATE Lop SET TenLop ='"+tenlop+"', MaKhoa ='"+makhoa+"' WHERE MaLop ='"+malop+"'");
                    Toast.makeText(LopActivity.this,"Sửa thành công!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    spnKhoa.setSelection(getIndex(spnKhoa1,tenkhoa));
                    GetData(makhoa);
                }
            }
        });
        dialog.show();
    }

    //Hàm xóa
    public void DialogDelete(String malop, String tenlop, String makhoa){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc muốn xóa : "+tenlop+" hay không ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(checkXoa(malop)){
                    Toast.makeText(LopActivity.this,"Không thể xóa do có bảng tham chiếu tới!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    connectDB.QueryData("DELETE FROM Lop WHERE MaLop='"+malop+"'");
                    GetData(makhoa);
                    Toast.makeText(LopActivity.this,"Xóa thành công!",Toast.LENGTH_SHORT).show();
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

    public boolean checkXoa(String malop){
        boolean kt = false;
        Cursor data = connectDB.GetData("SELECT * FROM SinhVien WHERE MaLop = '"+malop+"' ");
        while(data.moveToNext()){
            kt = true;
        }
        return kt;
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

    public String LayTenTuMa(String makhoa){
        String tenkhoa = null;
        Cursor data = connectDB.GetData("SELECT * FROM Khoa WHERE MaKhoa='"+makhoa+"'");
        while (data.moveToNext()){
            tenkhoa = data.getString(1);
        }
        return tenkhoa;
    }

    public String LayMaTuten(String tenkhoa){
        String makhoa = null;
        Cursor data = connectDB.GetData("SELECT * FROM Khoa WHERE TenKhoa='"+tenkhoa+"'");
        while (data.moveToNext()){
            makhoa = data.getString(0);
        }
        return makhoa;
    }
    public void GetData(String ma){
        Cursor data = connectDB.GetData("SELECT * FROM Lop WHERE MaKhoa = '"+ma+"' ORDER BY MaLop ASC");
        arrayList.clear();
        while (data.moveToNext()){
           String malop = data.getString(0);
           String tenlop = data.getString(1);
           String makhoa = data.getString(2);

           Lop lop = new Lop(malop,tenlop,LayKhoaTuMa(makhoa));
           arrayList.add(lop);
        }

        adapter.notifyDataSetChanged();
    }

    public Khoa LayKhoaTuMa(String makhoa){
        Cursor data = connectDB.GetData("SELECT * FROM Khoa WHERE MaKhoa='"+makhoa+"'");
        String tenkhoa = null;
        while (data.moveToNext()){
            tenkhoa = data.getString(1);
        }
        Khoa khoa = new Khoa(makhoa,tenkhoa);
        return khoa;
    }
    public ArrayList<String> getDSKhoa(){
        dsKhoa.clear();
        Cursor data = connectDB.GetData("SELECT * FROM Khoa");
        while (data.moveToNext()){
            String tenkhoa = data.getString(1);
            dsKhoa.add(tenkhoa);
        }
        return dsKhoa;
    }
}