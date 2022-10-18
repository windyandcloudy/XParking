package com.example.qlguixe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlguixe.ApiController.ApiController;
import com.example.qlguixe.Models.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    List<Account> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnUserRegister;
        btnUserRegister = findViewById(R.id.btnUserRegister);
        EditText etUser, etPass,etPhoneNumber,etAddress,etNgaySinh,etRePass, etEmail;
        TextView tvError;


        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        etNgaySinh =findViewById(R.id.etNgaySinh);
        etRePass = findViewById(R.id.etRePass);
        tvError = findViewById(R.id.tvError);
        etEmail= findViewById(R.id.etEmail);

        accounts = new ArrayList<>();
        btnUserRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUser.getText().toString().trim();
                String pass = etPass.getText().toString().trim();
                String phone = etPhoneNumber.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                String dob = etNgaySinh.getText().toString().trim();
                String repass = etRePass.getText().toString().trim();
                String email= etEmail.getText().toString().trim();

                Pattern pusername = Pattern.compile("[^A-Za-z]");
                Matcher musername = pusername.matcher(username);

                Pattern pphone = Pattern.compile("[^0-9]");
                Matcher mphone = pphone.matcher(phone);

                Pattern pemail = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$");
                Matcher memail = pemail.matcher(email);

                if (username.length()<3 || musername.find()){
                    tvError.setText("Tên đăng nhập không được chứa số và phải trên 3 kí tự!");
                }else if (pass.length()<8){
                    tvError.setText("Mật khẩu phải trên 8 kí tự");
                }else if (mphone.find() || phone.length()<10){
                    tvError.setText("Số điện thoại không hợp lệ");
                }else if(memail.find()){
                    tvError.setText("Email không hợp lệ");
                } else if( username.equals("") ||
                        pass.equals("") ||
                        phone.equals("") ||
                        address.equals("") ||
                        dob.equals("")|| email.equals("")) {
                    tvError.setText("Xin vui lòng điền đủ thông tin!");
                }else {

                    if(!repass.equals(pass)){
                        tvError.setText("Mật khẩu không trùng khớp!");
                    }else {
                        Account account = new Account(username,pass,phone,address,dob, email);
                        ApiController.apiService.createAccount(account).enqueue(new Callback<Account>() {
                            @Override
                            public void onResponse(Call<Account> call, Response<Account> response) {
                                if(response.isSuccessful()){
                                    accounts.add(response.body());
                                    Toast.makeText(Register.this,"Thêm tài khoản thành công",Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(Register.this, "Lỗi Server", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<Account> call, Throwable t) {
                                Toast.makeText(Register.this, "Lỗi đường truyền: "+ t.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                }
            }
        });
    }
}