package com.example.qlguixe.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlguixe.Models.Account;
import com.example.qlguixe.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> {

    public interface IOnItemAccountClickListener{
        void onItemClick(Account account);
    }

    List<Account> accounts;
    Context context;

    IOnItemAccountClickListener listener;

    public IOnItemAccountClickListener getListener() {
        return listener;
    }

    public void setListener(IOnItemAccountClickListener listener) {
        this.listener = listener;
    }

    public AccountListAdapter(List<Account> list, Context context){
        this.accounts = list;
        this.context = context;
    }




    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_tai_khoan,parent,false);
        AccountListAdapter.ViewHolder viewHolder = new AccountListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AccountListAdapter.ViewHolder holder, int position) {
        Account account = accounts.get(position);
        holder.tvUsername.setText("Tài khoản: "+account.getUsername());
        holder.tvPhone.setText("Số điện thoại: "+account.getPhone());
        holder.tvAddress.setText("Địa chỉ: "+account.getAddress());

        holder.lnItemAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(account);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvPhone, tvAddress;
        LinearLayout lnItemAccount;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            lnItemAccount = itemView.findViewById(R.id.lnItemAccount);


        }
    }

    public  void setAccounts(List<Account> list){
        accounts = list;
        notifyDataSetChanged();
    }
}
