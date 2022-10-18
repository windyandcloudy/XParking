package com.example.qlguixe.User;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlguixe.Models.Transport;
import com.example.qlguixe.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.List;

public class TransportAdapter extends RecyclerView.Adapter<TransportAdapter.ViewHolder> {
    List<Transport> list;
    Context context;

    IOnClickItemTransport iOnClickItemTransport;

    public IOnClickItemTransport getiOnClickItemTransport() {
        return iOnClickItemTransport;
    }

    public void setiOnClickItemTransport(IOnClickItemTransport iOnClickItemTransport) {
        this.iOnClickItemTransport = iOnClickItemTransport;
    }

    public List<Transport> getList() {
        return list;
    }

    public void setList(List<Transport> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public TransportAdapter(List<Transport> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_item_transport, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Transport transport= list.get(position);

        holder.tvLoaiXe.setText(transport.getTrans_type());
        holder.tvTenXe.setText(transport.getTrans_name());
        holder.tvBienSo.setText(transport.getTrans_license());

        if (transport.getQr()!=null){
            //initial multi format write
            MultiFormatWriter writer = new MultiFormatWriter();
            try {
                //initial bitmatrix
                BitMatrix matrix = writer.encode(transport.getQr(), BarcodeFormat.QR_CODE, 500, 500);
                //initial barcode encoder
                BarcodeEncoder encoder = new BarcodeEncoder();
                //init bitmap
                Bitmap bitmap = encoder.createBitmap(matrix);
                //set bitmap on image view
                holder.imgQr.setImageBitmap(bitmap);
                //init input manager
//            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //hide soft keyboard
//                        manager.hideSoftInputFromWindow(edtBienSo.getApplicationWindowToken(), 0);
            } catch (WriterException e) {
                System.out.println("Lỗi tạo qr");
                e.printStackTrace();
            }
        }

        holder.lnItemTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOnClickItemTransport.iOnClick(transport, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list!=null?list.size(): 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLoaiXe, tvTenXe, tvBienSo;
        ImageView imgQr;
        LinearLayout lnItemTransport;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLoaiXe= itemView.findViewById(R.id.tvLoaiXe);
            tvTenXe= itemView.findViewById(R.id.tvTenXe);
            tvBienSo= itemView.findViewById(R.id.tvBienSo);

            imgQr= itemView.findViewById(R.id.imgQr);

            lnItemTransport= itemView.findViewById(R.id.lnItemTransport);
        }
    }
}
