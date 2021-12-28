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
import android.widget.Toast;

import com.example.project.KhoaActivity;
import com.example.project.Model.Khoa;
import com.example.project.R;

import java.util.List;

public class KhoaAdapter extends BaseAdapter {

    private KhoaActivity context;
    private int layout;
    private List<Khoa> khoaList;

    public KhoaAdapter(KhoaActivity context, int layout, List<Khoa> khoaList) {
        this.context = context;
        this.layout = layout;
        this.khoaList = khoaList;
    }

    @Override
    public int getCount() {
        return khoaList.size();
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
        TextView tvMaKhoa, tvTenKhoa;
        ImageView imgSua, imgXoa;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);

            holder.tvMaKhoa = convertView.findViewById(R.id.tvMaKhoa);
            holder.tvTenKhoa = convertView.findViewById(R.id.tvTenKhoa);
            holder.imgSua = convertView.findViewById(R.id.imgSua);
            holder.imgXoa = convertView.findViewById(R.id.imgXoa);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        Khoa khoa = khoaList.get(position);
        holder.tvMaKhoa.setText("Mã khoa: "+khoa.getMaKhoa().toString().trim().toUpperCase());
        holder.tvTenKhoa.setText("Tên khoa: "+khoa.getTenKhoa().toString().trim());

        holder.imgSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"update",Toast.LENGTH_SHORT).show();
                context.DialogUpdate(khoa.getMaKhoa(),khoa.getTenKhoa());
            }
        });

        holder.imgXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"delete",Toast.LENGTH_SHORT).show();
                context.DialogDelete(khoa.getMaKhoa(),khoa.getTenKhoa());
            }
        });

        // Tạo animation
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.scale_list);
        convertView.startAnimation(animation);
        return convertView;
    }
}
