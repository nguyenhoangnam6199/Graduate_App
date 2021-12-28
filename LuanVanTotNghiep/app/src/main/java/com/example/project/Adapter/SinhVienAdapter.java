package com.example.project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.Model.Lop;
import com.example.project.Model.SinhVien;
import com.example.project.R;
import com.example.project.SinhVienActivity;

import java.util.List;

public class SinhVienAdapter extends BaseAdapter {

    private SinhVienActivity context;
    private int layout;
    private List<SinhVien> sinhVienList;

    public SinhVienAdapter(SinhVienActivity context, int layout, List<SinhVien> sinhVienList) {
        this.context = context;
        this.layout = layout;
        this.sinhVienList = sinhVienList;
    }

    @Override
    public int getCount() {
        return sinhVienList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView tvMaSV, tvTenSV, tvNS, tvLop;
        ImageView imgSua, imgXoa;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);

            holder.tvMaSV = convertView.findViewById(R.id.tvMaSV);
            holder.tvTenSV = convertView.findViewById(R.id.tvTenSV);
            holder.tvNS = convertView.findViewById(R.id.tvNS);
            holder.tvLop = convertView.findViewById(R.id.tvLop);
            holder.imgSua = convertView.findViewById(R.id.imgSua);
            holder.imgXoa = convertView.findViewById(R.id.imgXoa);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        SinhVien sv = sinhVienList.get(position);
        holder.tvMaSV.setText("Mã SV: "+sv.getMaSV().toString().trim().toUpperCase());
        holder.tvTenSV.setText("Họ tên: "+sv.getTenSV().toString().trim());
        holder.tvNS.setText("Ngày sinh: "+sv.getNgaySinh().toString().trim());
        holder.tvLop.setText("Lớp: "+sv.getMaLop().getMaLop().toString().trim());
        holder.imgSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"update",Toast.LENGTH_SHORT).show();
                context.DialogUpdate(sv.getMaSV(),sv.getTenSV(),sv.getNgaySinh(),sv.getMaLop().getMaLop());
            }
        });

        holder.imgXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"delete",Toast.LENGTH_SHORT).show();
                context.DialogDelete(sv.getMaSV(),sv.getTenSV(),sv.getMaLop().getMaLop());
            }
        });

        // Tạo animation
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.scale_list);
        convertView.startAnimation(animation);
        return convertView;
    }
}
