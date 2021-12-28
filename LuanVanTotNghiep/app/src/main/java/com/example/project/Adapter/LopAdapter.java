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

import com.example.project.LopActivity;
import com.example.project.Model.Khoa;
import com.example.project.Model.Lop;
import com.example.project.R;

import java.util.List;

public class LopAdapter extends BaseAdapter {
    private LopActivity context;
    private int layout;
    private List<Lop> lopList;

    public LopAdapter(LopActivity context, int layout, List<Lop> lopList) {
        this.context = context;
        this.layout = layout;
        this.lopList = lopList;
    }

    @Override
    public int getCount() {
        return lopList.size();
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
        TextView tvMaLop, tvTenLop, tvKhoa;
        ImageView imgSua, imgXoa;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);

            holder.tvMaLop = convertView.findViewById(R.id.tvMaLop);
            holder.tvTenLop = convertView.findViewById(R.id.tvTenLop);
            holder.tvKhoa = convertView.findViewById(R.id.tvKhoa);
            holder.imgSua = convertView.findViewById(R.id.imgSua);
            holder.imgXoa = convertView.findViewById(R.id.imgXoa);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        Lop lop = lopList.get(position);
        holder.tvMaLop.setText("Mã lớp: "+lop.getMaLop().toString().trim().toUpperCase());
        holder.tvTenLop.setText("Tên lớp: "+lop.getTenLop().toString().trim());
        holder.tvKhoa.setText("Khoa: "+lop.getKhoa().getTenKhoa().toString().trim());
        holder.imgSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"update",Toast.LENGTH_SHORT).show();
                context.DialogUpdate(lop.getMaLop(),lop.getTenLop(),lop.getKhoa().getMaKhoa());
            }
        });

        holder.imgXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"delete",Toast.LENGTH_SHORT).show();
                context.DialogDelete(lop.getMaLop(),lop.getTenLop(),lop.getKhoa().getMaKhoa());
            }
        });

        // Tạo animation
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.scale_list);
        convertView.startAnimation(animation);
        return convertView;
    }
}
