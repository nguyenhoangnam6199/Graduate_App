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

import com.example.project.DiemThiActivity;
import com.example.project.Model.Diem;
import com.example.project.Model.SinhVien;
import com.example.project.R;

import java.util.List;

public class DiemAdapter extends BaseAdapter {
    private DiemThiActivity context;
    private int layout;
    private List<Diem> diemList;

    public DiemAdapter(DiemThiActivity context, int layout, List<Diem> diemList) {
        this.context = context;
        this.layout = layout;
        this.diemList = diemList;
    }

    @Override
    public int getCount() {
        return diemList.size();
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
        TextView tvMaSV, tvTenSV, tvLop, tvDiem, tvLanThi;
        ImageView imgSua;
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
            holder.tvLop = convertView.findViewById(R.id.tvLop);
            holder.tvDiem = convertView.findViewById(R.id.tvDiemThi);
            holder.tvLanThi = convertView.findViewById(R.id.tvLanThi);
            holder.imgSua = convertView.findViewById(R.id.imgSua);


            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        Diem diem = diemList.get(position);
        holder.tvMaSV.setText("Mã SV: "+diem.getMaSV().toString().trim().toUpperCase());
        holder.tvTenSV.setText("Họ tên: "+context.LayTenSV(diem.getMaSV().toString().trim()));
        holder.tvLop.setText("Lớp: "+context.LayMaLop(diem.getMaSV().toString().trim()));
        holder.tvDiem.setText("Điểm: "+diem.getDiemThi());
        holder.tvLanThi.setText("Lần thi: "+diem.getLanThi());

        holder.imgSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chen ham sua
                context.DialogUpdate(diem.getMaSV(), diem.getMaMH(), diem.getLanThi());
            }
        });

        // Tạo animation
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.scale_list);
        convertView.startAnimation(animation);

        return convertView;
    }
}
