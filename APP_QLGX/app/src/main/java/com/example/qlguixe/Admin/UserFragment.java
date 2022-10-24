package com.example.qlguixe.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlguixe.ApiController.ApiController;
import com.example.qlguixe.Models.Account;
import com.example.qlguixe.R;
import com.example.qlguixe.User.UserMainActivity;
import com.example.qlguixe.databinding.FragmentUserBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    FragmentUserBinding binding;
    AccountListAdapter adapter;
    List<Account> accounts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user,container,false);
        accounts = new ArrayList<>();
        getAllUser();
        binding.btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegister();
            }
        });
        return binding.getRoot();
    }


    //hien thi danh sach tai khoan
    public void getAllUser(){
        ApiController.apiService.getAllUser().enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if(response.isSuccessful()){
                    accounts = response.body();
                    adapter = new AccountListAdapter(accounts,getContext());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                    binding.revTaiKhoan.setLayoutManager(layoutManager);
                    binding.revTaiKhoan.setAdapter(adapter);

                    //hien thi chi tiet tai khoan da chon
                    adapter.setListener(new AccountListAdapter.IOnItemAccountClickListener() {
                        @Override
                        public void onItemClick(Account account) {
                            showDetailDialog(account);
                        }
                    });
                }else {
                    Toast.makeText(getActivity().getBaseContext(), "Lỗi Server API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "Lỗi đường truyền: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //hien thi hop thoai chi tiet tai khoan
    public void showDetailDialog(Account account){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_detail_account);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtributes = window.getAttributes();
        windowAtributes.gravity = Gravity.CENTER_VERTICAL;
        window.setAttributes(windowAtributes);

        dialog.setCancelable(true);

        TextView tvDetaiId, tvDetailUserName, tvDetailPhone, tvDetailAddress, tvDetailDOB, tvDetailMoney;
        Button btnCharge,btnDeleteAccount;

        //anh xa
        tvDetaiId = dialog.findViewById(R.id.tvDetailId);
        tvDetailUserName = dialog.findViewById(R.id.tvDetailUsername);
        tvDetailPhone = dialog.findViewById(R.id.tvDetailPhone);
        tvDetailAddress = dialog.findViewById(R.id.tvDetailAddress);
        tvDetailDOB = dialog.findViewById(R.id.tvDetailDOB);
        tvDetailMoney = dialog.findViewById(R.id.tvDetailMoney);
        btnCharge = dialog.findViewById(R.id.btnCharge);
        btnDeleteAccount = dialog.findViewById(R.id.btnDeleteAccount);

        tvDetaiId.setText("ID: "+account.get_id());
        tvDetailUserName.setText("Tài khoản: "+account.getUsername());
        tvDetailPhone.setText("Số điện thoại: "+account.getPhone());
        tvDetailAddress.setText("Địa chỉ: "+account.getAddress());
        tvDetailDOB.setText("Ngày sinh: "+account.getDob());
        tvDetailMoney.setText("Số dư: "+account.getMoney());

        //nap tien
        btnCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChargeForm(account);
                dialog.cancel();
            }
        });

        //xoa tai khoan
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccount(account);
                dialog.cancel();
            }

        });


        dialog.show();
    }

    //nap tien
    public void showChargeForm(Account account){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_charge);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtributes = window.getAttributes();
        windowAtributes.gravity = Gravity.CENTER_VERTICAL;
        window.setAttributes(windowAtributes);

        dialog.setCancelable(true);

        Button btnFormCharge;
        EditText etMoney;

        //anh xa
        btnFormCharge = dialog.findViewById(R.id.btnFormCharge);
        etMoney = dialog.findViewById(R.id.etMoney);



        btnFormCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tien = etMoney.getText().toString().trim();
                if(tien == ""){
                    Toast.makeText(getActivity().getBaseContext(),"Vui lòng nhập số tiền",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        long money = Long.parseLong(tien);
                        if (money<=0){
                            throw new NumberFormatException("Số tiền phải là 1 số dương");
                        }

                        account.setMoney(account.getMoney()+money);
                        ApiController.apiService.updateUserMoney(account.get_id(),account).enqueue(new Callback<Account>() {
                            @Override
                            public void onResponse(Call<Account> call, Response<Account> response) {
                                if(response.isSuccessful()){
                                    accounts.set(accounts.indexOf(account),account);
                                    adapter.setAccounts(accounts);
                                    Toast.makeText(getActivity().getBaseContext(),"Nạp tiền thành công",Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                                else {
                                    Toast.makeText(getActivity().getBaseContext(), "Lỗi Server API", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Account> call, Throwable t) {
                                Toast.makeText(getActivity().getBaseContext(), "Lỗi đường truyền: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }catch (NumberFormatException e){
                        Toast.makeText(getActivity().getBaseContext(),"Số tiền phải là 1 số dương",Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });



        dialog.show();
    }

    //hien thi hop thoai them tai khoan
    public void showRegister(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_add_user);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtributes = window.getAttributes();
        windowAtributes.gravity = Gravity.CENTER_VERTICAL;
        window.setAttributes(windowAtributes);

        dialog.setCancelable(true);

        //anh xa
        Button btnThemTaiKhoan;
        EditText edtUser, edtPass,edtPhoneNumber,edtAddress,edtNgaySinh,edtRePass, edtEmail;
        TextView tvErrDK;

        btnThemTaiKhoan = dialog.findViewById(R.id.btnThemTaiKhoan);
        edtUser = dialog.findViewById(R.id.edtUser);
        edtPass = dialog.findViewById(R.id.edtPass);
        edtPhoneNumber = dialog.findViewById(R.id.edtPhoneNumber);
        edtAddress = dialog.findViewById(R.id.edtAddress);
        edtNgaySinh = dialog.findViewById(R.id.edtNgaySinh);
        edtRePass = dialog.findViewById(R.id.edtRePass);
        edtEmail= dialog.findViewById(R.id.edtEmail);
        tvErrDK = dialog.findViewById(R.id.tvErrDK);


        btnThemTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUser.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                String phone = edtPhoneNumber.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();
                String dob = edtNgaySinh.getText().toString().trim();
                String repass = edtRePass.getText().toString().trim();
                String email= edtEmail.getText().toString().trim();

                Pattern pusername = Pattern.compile("^[A-Za-z]*$");
                Matcher musername = pusername.matcher(username);

                Pattern pphone = Pattern.compile("^[0-9]*$");
                Matcher mphone = pphone.matcher(phone);

                Pattern pemail = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
                Matcher memail = pemail.matcher(email);

                if (username.length()<3 || !musername.find()){
                    tvErrDK.setText("Tên đăng nhập không được chứa số và phải trên 3 kí tự!");
                }else if (pass.length()<8){
                    tvErrDK.setText("Mật khẩu phải trên 8 kí tự");
                }else if (mphone.find() || phone.length()<10){
                    tvErrDK.setText("Số điện thoại không hợp lệ");
                }else if(!memail.find()){
                    tvErrDK.setText("Email không hợp lệ");
                } else
                if( username.equals("") ||
                        pass.equals("") ||
                        phone.equals("") ||
                        address.equals("") ||
                        dob.equals("")){
                    tvErrDK.setText("Xin vui lòng điền đủ thông tin!");
                }else {

                    if(!repass.equals(pass)){
                        tvErrDK.setText("Mật khẩu không trùng khớp!");
                    }else {
                        Account account = new Account(username,pass,phone,address,dob, email);
                        ApiController.apiService.createAccount(account).enqueue(new Callback<Account>() {
                            @Override
                            public void onResponse(Call<Account> call, Response<Account> response) {
                                if(response.isSuccessful()){
                                    accounts.add(response.body());
                                    adapter.setAccounts(accounts);
                                    Toast.makeText(getActivity().getBaseContext(),"Thêm tài khoản thành công",Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }else {
                                    Toast.makeText(getActivity().getBaseContext(), "Lỗi Server", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<Account> call, Throwable t) {
                                Toast.makeText(getActivity().getBaseContext(), "Lỗi đường truyền: "+ t.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                }
            }
        });



        dialog.show();
    }

    //xoa tai khoan
    public void deleteAccount(Account account){
        AlertDialog alertDialog =new AlertDialog.Builder(getContext()).setTitle("Thông báo")
                .setMessage("Bạn có muốn xóa tài khoản "+account.getUsername()+" không?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ApiController.apiService.deleteAccount(account.get_id()).enqueue(new Callback<Account>() {
                            @Override
                            public void onResponse(Call<Account> call, Response<Account> response) {
                                if(response.isSuccessful()){
                                    accounts.remove(account);
                                    adapter.setAccounts(accounts);
                                    Toast.makeText(getActivity().getBaseContext(),"Xóa tài khoản thành công",Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(getActivity().getBaseContext(), "Lỗi Server API", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Account> call, Throwable t) {
                                Toast.makeText(getActivity().getBaseContext(), "Lỗi đường truyền: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }



}