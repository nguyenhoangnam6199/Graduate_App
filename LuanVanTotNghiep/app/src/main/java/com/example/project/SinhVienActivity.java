package com.example.project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adapter.SinhVienAdapter;
import com.example.project.Model.Khoa;
import com.example.project.Model.Lop;
import com.example.project.Model.SinhVien;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class SinhVienActivity extends AppCompatActivity {

    ConnectDB connectDB;
    ListView listView;
    ArrayList<SinhVien> arrayList;
    SinhVienAdapter adapter;
    ImageView btnThemSV, btnThoatSV;
    Spinner spnLop;

    ArrayList<String> dsLop = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinh_vien);

        //Tạo database
        connectDB = new ConnectDB(this,"QuanLyDiem.db",null,1);

        //Tạo bảng sinh viên
        connectDB.QueryData("CREATE TABLE IF NOT EXISTS SinhVien(MaSV VARCHAR(10) PRIMARY KEY, HoTen VARCHAR(200), NgaySinh VARCHAR(200), MaLop VARCHAR(15), FOREIGN KEY(MaLop) REFERENCES Lop(MaLop))");

        btnThemSV = findViewById(R.id.btnAdd);
        btnThoatSV = findViewById(R.id.btnExit);
        spnLop = findViewById(R.id.spnLop);
        listView = findViewById(R.id.lvDSSinhVien);
        arrayList = new ArrayList<>();
        adapter = new SinhVienAdapter(this,R.layout.item_sv,arrayList);
        listView.setAdapter(adapter);

        ArrayList<String> a = new ArrayList<>();
        a.clear();
        a = getDSLop();
        ArrayAdapter<String> a1 = new ArrayAdapter<String>(this,R.layout.spiner_layout,R.id.txt,a);
        spnLop.setAdapter(a1);

        //Xử lý sự kiện
        btnThoatSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SinhVienActivity.this,FunctionActivity.class);
                startActivity(i);
            }
        });

        btnThemSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Insert();
            }
        });

        String malop = spnLop.getSelectedItem().toString().trim();
        ArrayList<String> arr1 = a;
        spnLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ma1 = arr1.get(position);
                GetData(ma1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        GetData(malop);
    }

    //Hàm thêm sinh viên
    public void Dialog_Insert(){
        Dialog dialog = new Dialog(SinhVienActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sv);

        //Ánh xạ
        Spinner spnLop1;
        EditText edtMaSV, edtTenSV, edtNS;
        Button btnLuu, btnHuy;

       spnLop1 = dialog.findViewById(R.id.spnLop);
       edtMaSV=dialog.findViewById(R.id.edtMaSV);
       edtTenSV = dialog.findViewById(R.id.edtTenSV);
       edtNS = dialog.findViewById(R.id.edtNS);
        btnLuu = dialog.findViewById(R.id.btnLuu);
        btnHuy = dialog.findViewById(R.id.btnHuy);

        ArrayList<String> a = new ArrayList<>();
        a.clear();
        a = getDSLop();
        ArrayAdapter<String> a1 = new ArrayAdapter<String>(this,R.layout.spiner_layout,R.id.txt,a);
        spnLop1.setAdapter(a1);

        int selectedYear = 2000;
        int selectedMonth = 5;
        int selectedDayOfMonth = 10;

        edtNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if(month<10 && dayOfMonth<10){
                            edtNS.setText("0"+dayOfMonth + "/0" + (month + 1) + "/" + year);
                        }
                        else if(month<10){
                            edtNS.setText(+dayOfMonth + "/0" + (month + 1) + "/" + year);
                        }
                        else if(dayOfMonth<10){
                            edtNS.setText("0"+dayOfMonth + "/" + (month + 1) + "/" + year);
                        }
                        else{
                            edtNS.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        }

                    }
                };
                // Create DatePickerDialog (Spinner Mode):
                DatePickerDialog datePickerDialog = new DatePickerDialog(SinhVienActivity.this,
                        dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);

                // Show
                datePickerDialog.show();
            }
        });

        // 06/01/1999
        edtMaSV.setText(TaoMaSV());
        edtMaSV.setEnabled(false);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String ma1 = edtMaSV.getText().toString().trim();
                String ten1 = ChuanHoa(edtTenSV.getText().toString().trim().toLowerCase());
                String ns1 = edtNS.getText().toString().trim();
                String lop1 = spnLop1.getSelectedItem().toString().trim();
                if(ten1.isEmpty()){
                    edtTenSV.setError("Tên sinh viên không được trống !");
                    edtTenSV.requestFocus();
                    return;
                }
                else if(ns1.isEmpty()){
                    edtNS.setError("Ngày sinh không được trống !");
                    edtNS.requestFocus();
                    return;
                }
                else if((LocalDate.now().getYear()-Integer.parseInt(ns1.substring(6)))<18){
                    edtNS.setError("Sinh viên phải từ 18 tuổi trở lên!");
                    edtNS.requestFocus();
                    return;
                }
                else{
                    connectDB.QueryData("INSERT INTO SinhVien VALUES('"+ma1+"','"+ten1+"','"+ns1+"', '"+lop1+"')");
                    Toast.makeText(SinhVienActivity.this,"Thêm thành công!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    spnLop.setSelection(getIndex(spnLop1,lop1));
                    GetData(lop1);
                }
            }
        });
        dialog.show();
    }


    //Hàm sửa
    public void DialogUpdate(String masv, String tensv, String ns, String lop){
        Dialog dialog = new Dialog(SinhVienActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sv);

        //Ánh xạ
        Spinner spnLop1;
        EditText edtMaSV, edtTenSV, edtNS;
        Button btnLuu, btnHuy;
        TextView tvTieuDe;

        tvTieuDe = dialog.findViewById(R.id.tvTieuDe);
        spnLop1 = dialog.findViewById(R.id.spnLop);
        edtMaSV=dialog.findViewById(R.id.edtMaSV);
        edtTenSV = dialog.findViewById(R.id.edtTenSV);
        edtNS = dialog.findViewById(R.id.edtNS);
        btnLuu = dialog.findViewById(R.id.btnLuu);
        btnHuy = dialog.findViewById(R.id.btnHuy);

        ArrayList<String> a = new ArrayList<>();
        a.clear();
        a = getDSLop();
        ArrayAdapter<String> a1 = new ArrayAdapter<String>(this,R.layout.spiner_layout,R.id.txt,a);
        spnLop1.setAdapter(a1);

        int selectedYear = 2000;
        int selectedMonth = 5;
        int selectedDayOfMonth = 10;

        edtNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if(month<10 && dayOfMonth<10){
                            edtNS.setText("0"+dayOfMonth + "/0" + (month + 1) + "/" + year);
                        }
                        else if(month<10){
                            edtNS.setText(+dayOfMonth + "/0" + (month + 1) + "/" + year);
                        }
                        else if(dayOfMonth<10){
                            edtNS.setText("0"+dayOfMonth + "/" + (month + 1) + "/" + year);
                        }
                        else{
                            edtNS.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        }

                    }
                };
                // Create DatePickerDialog (Spinner Mode):
                DatePickerDialog datePickerDialog = new DatePickerDialog(SinhVienActivity.this,
                        dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);

                // Show
                datePickerDialog.show();
            }
        });

        // 06/01/1999
        tvTieuDe.setText("CẬP NHẬT THÔNG TIN");
        edtMaSV.setText(TaoMaSV());
        edtMaSV.setEnabled(false);
        edtTenSV.setText(tensv);
        edtNS.setText(ns);
        spnLop1.setSelection(getIndex(spnLop,lop));

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String ma1 = edtMaSV.getText().toString().trim();
                String ten1 = ChuanHoa(edtTenSV.getText().toString().trim().toLowerCase());
                String ns1 = edtNS.getText().toString().trim();
                String lop1 = spnLop1.getSelectedItem().toString().trim();
                if(ten1.isEmpty()){
                    edtTenSV.setError("Tên sinh viên không được trống !");
                    edtTenSV.requestFocus();
                    return;
                }
                else if(ns1.isEmpty()){
                    edtNS.setError("Ngày sinh không được trống !");
                    edtNS.requestFocus();
                    return;
                }
                else if((LocalDate.now().getYear()-Integer.parseInt(ns1.substring(6)))<18){
                    edtNS.setError("Sinh viên phải từ 18 tuổi trở lên!");
                    edtNS.requestFocus();
                    return;
                }
                else{
                    connectDB.QueryData("UPDATE SinhVien SET HoTen='"+ten1+"', NgaySinh='"+ns1+"', MaLop='"+lop1+"' WHERE MaSV='"+masv+"'");
                    Toast.makeText(SinhVienActivity.this,"Cập nhật thành công!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    spnLop.setSelection(getIndex(spnLop1,lop1));
                    GetData(lop1);
                }
            }
        });
        dialog.show();
    }
    //Hàm xóa
    public void DialogDelete(String masv, String tensv,String malop){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc muốn xóa : "+tensv+" hay không ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(checkXoa(masv)){
                    Toast.makeText(SinhVienActivity.this,"Không thể xóa do có bảng tham chiếu tới!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    connectDB.QueryData("DELETE FROM SinhVien WHERE MaSV='"+masv+"'");
                    GetData(malop);
                    Toast.makeText(SinhVienActivity.this,"Xóa thành công!",Toast.LENGTH_SHORT).show();
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

    public boolean checkXoa(String masv){
        boolean kt = false;
        Cursor data = connectDB.GetData("SELECT * FROM DiemThi WHERE MaSV = '"+masv+"' ");
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

    //Hàm tạo mã sinh viên random
    public String TaoMaSV(){
        String masv = "HS";
        Random rand = new Random();
        int ranNum = rand.nextInt(999)+1;
        if(ranNum<10){
            masv="HS00"+String.valueOf(ranNum);
        }
        else if(ranNum>=10 && ranNum<=99){
            masv="HS0"+String.valueOf(ranNum);
        }
        else{
            masv="HS"+String.valueOf(ranNum);
        }
        return masv;
    }
    public void GetData(String ma){
        Cursor data = connectDB.GetData("SELECT * FROM SinhVien WHERE MaLop = '"+ma+"' ORDER BY MaSV ASC");
        arrayList.clear();
        while (data.moveToNext()){
           String masv = data.getString(0);
           String tensv = data.getString(1);
           String ns = data.getString(2);
           String malop = data.getString(3);

           SinhVien sv = new SinhVien(masv,tensv,ns,LayLopTuMa(malop));
           arrayList.add(sv);
        }

        adapter.notifyDataSetChanged();
    }

    public Lop LayLopTuMa(String malop){
        Cursor data = connectDB.GetData("SELECT * FROM Lop WHERE MaLop='"+malop+"'");
        String tenlop = null;
        String khoa =  null;
        while (data.moveToNext()){
            tenlop = data.getString(1);
            khoa = data.getString(2);
        }
        Lop lop = new Lop(malop,tenlop,LayKhoaTuMa(khoa));
        return lop;
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
    public ArrayList<String> getDSLop(){
        dsLop.clear();
        Cursor data = connectDB.GetData("SELECT * FROM Lop");
        while (data.moveToNext()){
           dsLop.add(data.getString(0));
        }
        return dsLop;
    }
}