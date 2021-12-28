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

import com.example.project.Model.Khoa;
import com.example.project.Model.MonHoc;
import com.example.project.MonHocActivity;
import com.example.project.R;

import java.util.List;

public class MonHocAdapter extends BaseAdapter {
    private MonHocActivity context;
    private int layout;
    private List<MonHoc> monHocList;

    public MonHocAdapter(MonHocActivity context, int layout, List<MonHoc> monHocList) {
        this.context = context;
        this.layout = layout;
        this.monHocList = monHocList;
    }

    @Override
    public int getCount() {
        return monHocList.size();
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
        TextView tvMaMH, tvTenMH;
        ImageView imgSua, imgXoa;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);

            holder.tvMaMH = convertView.findViewById(R.id.tvMaMonHoc);
            holder.tvTenMH = convertView.findViewById(R.id.tvTenMonHoc);
            holder.imgSua = convertView.findViewById(R.id.imgSua);
            holder.imgXoa = convertView.findViewById(R.id.imgXoa);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        MonHoc monhoc = monHocList.get(position);
        holder.tvMaMH.setText("Mã môn học: "+monhoc.getMaMon().toString().trim().toUpperCase());
        holder.tvTenMH.setText("Tên môn học: "+monhoc.getTenMon().toString().trim());

        holder.imgSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"update",Toast.LENGTH_SHORT).show();
                context.DialogUpdate(monhoc.getMaMon(),monhoc.getTenMon());
            }
        });

        holder.imgXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"delete",Toast.LENGTH_SHORT).show();
                context.DialogDelete(monhoc.getMaMon(),monhoc.getTenMon());
            }
        });

        // Tạo animation
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.scale_list);
        convertView.startAnimation(animation);
        return convertView;
    }
}
