package com.example.qlguixe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlguixe.Admin.AdminMainActivity;
import com.example.qlguixe.ApiController.ApiController;
import com.example.qlguixe.Models.Account;
import com.example.qlguixe.User.UserMainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    static  final  String SHARE_PRE_NAME="User"; //hello Phong
    TextView btnRegister, btnForgetPassword, tvError;
    Button btnLogin;
    CheckBox btnSavePassword;
    EditText edtUsername, edtPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static Account currentAccount=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhXa();
        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();
        //hiện sẵn tên đăng nhập và mật khẩu
        sharedPreferences = getSharedPreferences(SHARE_PRE_NAME,MODE_PRIVATE);
        String username =sharedPreferences.getString("username","");
        String password = sharedPreferences.getString("password","");
        edtUsername.setText(username);
        edtPassword.setText(password);

        btnSavePassword.setChecked(true);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username= edtUsername.getText().toString().trim();
                String password= edtPassword.getText().toString().trim();
                if (btnSavePassword.isChecked()){
                    savePassword(username, password);
                }
                Account account= new Account(username, password);
                ApiController.apiService.login(account).enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        if (response.isSuccessful()){
                            currentAccount= response.body();
                            if (currentAccount.getRole().equals("user")){
                                startActivity(new Intent(Login.this, UserMainActivity.class));
                            }else if (currentAccount.getRole().equals("admin")) {
                                startActivity(new Intent(Login.this, AdminMainActivity.class));
                            }else {
                                Toast.makeText(Login.this, "Tài khoản không có quyền truy cập", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(Login.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        Toast.makeText(Login.this, "Lỗi đường truyền. Hãy thử lại", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

    }
    private void savePassword(String username, String password) {
        sharedPreferences = getSharedPreferences(SHARE_PRE_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }

    private void anhXa() {
        btnRegister= findViewById(R.id.btnRegister);
        btnForgetPassword= findViewById(R.id.btnForgetPassword);
        tvError= findViewById(R.id.tvError);
        btnLogin= findViewById(R.id.btnLogin);
        btnSavePassword= findViewById(R.id.btnSavepassword);
        edtUsername= findViewById(R.id.edtUsername);
        edtPassword= findViewById(R.id.edtPassword);

    }
}