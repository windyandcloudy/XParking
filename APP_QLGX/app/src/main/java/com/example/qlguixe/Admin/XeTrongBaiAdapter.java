package com.example.qlguixe.Admin;

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

public class XeTrongBaiAdapter extends RecyclerView.Adapter<XeTrongBaiAdapter.ViewHolder> {
    List<Statistical> statisticals;
    Context context;



    public XeTrongBaiAdapter(List<Statistical> list, Context context) {
        this.statisticals = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_xe_trong_bai, parent, false);
        XeTrongBaiAdapter.ViewHolder viewHolder = new XeTrongBaiAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull XeTrongBaiAdapter.ViewHolder holder, int position) {
        Statistical xeGui= statisticals.get(position);
        holder.tvLoaiXe.setText("Loại xe: "+ xeGui.getTransport().getTrans_type());
        holder.tvTenXe.setText("Tên xe: "+ xeGui.getTransport().getTrans_name());
        holder.tvChuXe.setText("Chủ xe: "+ xeGui.getTransport().getOwn().getUsername());
        holder.tvBienSo.setText("Biển số: "+ xeGui.getTransport().getTrans_license());
        holder.tvGioGui.setText("Giờ gửi: "+ xeGui.getTimeCome().split(" ")[1]);
        holder.tvNgayGui.setText("Ngày gửi: "+ xeGui.getTimeCome().split(" ")[0]);
    }

    @Override
    public int getItemCount() {
        if (statisticals!=null)
            return statisticals.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLoaiXe, tvTenXe, tvChuXe, tvBienSo, tvGioGui, tvNgayGui;
        LinearLayout lnItemXeTrongBai;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLoaiXe= itemView.findViewById(R.id.tvLoaiXe);
            tvTenXe= itemView.findViewById(R.id.tvTenXe);
            tvChuXe= itemView.findViewById(R.id.tvChuXe);
            tvBienSo= itemView.findViewById(R.id.tvBienSo);
            tvGioGui= itemView.findViewById(R.id.tvGioGui);
            tvNgayGui= itemView.findViewById(R.id.tvNgayGui);
            lnItemXeTrongBai= itemView.findViewById(R.id.lnIemXeTrongBai);
        }
    }
    public void setStatisticals(List<Statistical> l){
        statisticals= l;
        notifyDataSetChanged();
    }
}