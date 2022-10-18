package com.example.qlguixe.Admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlguixe.Models.Statistical;
import com.example.qlguixe.R;

import java.util.List;

public class ThongKeXeAdapter extends RecyclerView.Adapter<ThongKeXeAdapter.ViewHolder> {
    List<Statistical> listXe;
    Context context;
//    IOnClickItemStatistical iOnClickItemStatistical;
//
//    public IOnClickItemStatistical getiOnClickItemXe() {
//        return iOnClickItemStatistical;
//    }
//
//    public void setiOnClickItemXe(IOnClickItemStatistical IOnClickItemStatistical) {
//        this.iOnClickItemStatistical = IOnClickItemStatistical;
//    }

    public ThongKeXeAdapter(List<Statistical> list, Context context) {
        this.listXe = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_xe_da_xuat, parent, false);
        ThongKeXeAdapter.ViewHolder viewHolder = new ThongKeXeAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ThongKeXeAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Statistical xe= listXe.get(position);
        holder.tvLoaiXe.setText("Loại xe: "+ xe.getTransport().getTrans_type());
        holder.tvTenXe.setText("Tên xe: "+ xe.getTransport().getTrans_name());
        holder.tvChuXe.setText("Chủ xe: "+ xe.getTransport().getOwn().getUsername());
        holder.tvBienSo.setText("Biển số: "+ xe.getTransport().getTrans_license());
        holder.tvGioGui.setText("Giờ gửi: "+ xe.getTimeCome().split(" ")[1]);
        holder.tvNgayGui.setText("Ngày gửi: "+ xe.getTimeCome().split(" ")[0]);
        holder.tvGioNhan.setText("Giờ nhận: "+ xe.getTimeOut().split(" ")[1]);
        holder.tvNgayNhan.setText("Ngày nhận: "+ xe.getTimeOut().split(" ")[0]);

        holder.lnItemXeTrongBai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                iOnClickItemStatistical.iOnClickItemStatistical(xe, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listXe!=null)
            return listXe.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLoaiXe, tvTenXe, tvChuXe, tvBienSo, tvGioGui, tvNgayGui, tvGioNhan, tvNgayNhan;
        LinearLayout lnItemXeTrongBai;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLoaiXe= itemView.findViewById(R.id.tvLoaiXeX);
            tvTenXe= itemView.findViewById(R.id.tvTenXeX);
            tvChuXe= itemView.findViewById(R.id.tvChuXeX);
            tvBienSo= itemView.findViewById(R.id.tvBienSoX);
            tvGioGui= itemView.findViewById(R.id.tvGioGuiX);
            tvNgayGui= itemView.findViewById(R.id.tvNgayGuiX);
            lnItemXeTrongBai= itemView.findViewById(R.id.lnIemXeTrongBaiX);
            tvGioNhan= itemView.findViewById(R.id.tvGioNhanX);
            tvNgayNhan= itemView.findViewById(R.id.tvNgayNhanX);
        }
    }
    public void updateList(List<Statistical> l){
        listXe= l;
        notifyDataSetChanged();
    }
}