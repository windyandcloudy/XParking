package com.example.qlguixe.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlguixe.Admin.AdminMainActivity;
import com.example.qlguixe.ApiController.ApiController;
import com.example.qlguixe.Login;
import com.example.qlguixe.Models.Account;
import com.example.qlguixe.Models.Statistical;
import com.example.qlguixe.Models.Transport;
import com.example.qlguixe.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserMainActivity extends AppCompatActivity {
    RecyclerView revTransportOfUser;
    FloatingActionButton btnAddTransport;
    TransportAdapter adapter;
    List<Transport> transports;
    ActionBar actionBar;
    TextView tvSoXeTrongBai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        anhXa();

        actionBar = getSupportActionBar();
        actionBar.setTitle("User: " + Login.currentAccount.getUsername() + " == $: " + Login.currentAccount.getMoney() + "VNĐ");

        ApiController.apiService.getTransportOfUser(Login.currentAccount.get_id()).enqueue(new Callback<List<Transport>>() {
            @Override
            public void onResponse(Call<List<Transport>> call, Response<List<Transport>> response) {
                if (response.isSuccessful()) {
                    transports = response.body();
                    Collections.sort(transports, new Comparator<Transport>() {
                        @Override
                        public int compare(Transport transport, Transport t1) {
                            return -(transport.getQr() == null ? "" : transport.getQr()).compareTo(t1.getQr() == null ? "" : t1.getQr());
                        }
                    });
                    adapter = new TransportAdapter(transports, UserMainActivity.this);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserMainActivity.this, RecyclerView.VERTICAL, false);
                    revTransportOfUser.setLayoutManager(layoutManager);
                    revTransportOfUser.setAdapter(adapter);

                    adapter.setiOnClickItemTransport(new IOnClickItemTransport() {
                        @Override
                        public void iOnClick(Transport transport, int pos) {
                            Toast.makeText(UserMainActivity.this, "Bạn vừa click vào xe", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(UserMainActivity.this, "Lỗi server. Hãy thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Transport>> call, Throwable t) {
                Toast.makeText(UserMainActivity.this, "Lỗi đường truyền. Hãy thử lại", Toast.LENGTH_SHORT).show();
            }
        });

        ApiController.apiService.getAllIn("0").enqueue(new Callback<List<Statistical>>() {
            @Override
            public void onResponse(Call<List<Statistical>> call, Response<List<Statistical>> response) {
                if (response.isSuccessful()) {
                    tvSoXeTrongBai.setText("Số xe trong bãi: " + response.body().size() + "/1000");
                } else {
                    tvSoXeTrongBai.setVisibility(View.GONE);
                    Toast.makeText(UserMainActivity.this, "Không đếm được số xe trong bãi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Statistical>> call, Throwable t) {

            }
        });


        btnAddTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTransport();
            }
        });

    }

    private void anhXa() {
        revTransportOfUser = findViewById(R.id.revTransportOfUser);
        btnAddTransport = findViewById(R.id.btnAddTransport);
        tvSoXeTrongBai = findViewById(R.id.tvSoXeTrongBai);
    }

    private void addTransport() {
        final Dialog dialog = new Dialog(UserMainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_add_transport);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtributes = window.getAttributes();
        windowAtributes.gravity = Gravity.CENTER_VERTICAL;
        window.setAttributes(windowAtributes);

        dialog.setCancelable(true);

        TextView tvLoaiXe, tvTenXe, tvBienSo;
        Button btnSend;

        //anh xa
        tvLoaiXe = dialog.findViewById(R.id.edtTransType);
        tvTenXe = dialog.findViewById(R.id.edtTransName);
        tvBienSo = dialog.findViewById(R.id.edtTransLicense);
        btnSend = dialog.findViewById(R.id.btnAddToServer);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loaiXe = tvLoaiXe.getText().toString().trim();
                String tenXe = tvTenXe.getText().toString().trim();
                String bienSo = tvBienSo.getText().toString().trim();
                if (loaiXe.equals("") || tenXe.equals("") || bienSo.equals("")) {
                    Toast.makeText(UserMainActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    Transport transport = new Transport(loaiXe, tenXe, bienSo);
                    ApiController.apiService.createTransport(Login.currentAccount.get_id(), transport).enqueue(new Callback<Transport>() {
                        @Override
                        public void onResponse(Call<Transport> call, Response<Transport> response) {
                            if (response.isSuccessful()) {
                                transports.add(response.body());
                                adapter.setList(transports);
                                Toast.makeText(UserMainActivity.this, "Thêm xe thành công", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            } else {
                                Toast.makeText(UserMainActivity.this, "Lỗi Server: " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Transport> call, Throwable t) {
                            Toast.makeText(UserMainActivity.this, "Lỗi đường truyền: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        dialog.show();
    }

    //doi mat khau
    private void showChangePasswordDialog() {
        final Dialog dialog = new Dialog(UserMainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_change_password);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtributes = window.getAttributes();
        windowAtributes.gravity = Gravity.CENTER_VERTICAL;
        window.setAttributes(windowAtributes);

        dialog.setCancelable(false);

        //anh xa
        Button btnUpdatePass, btnCancle;
        ImageView imgBack;
        EditText etOldPass, etNewPass, etReNewPass;
        TextView tvError;

        imgBack = dialog.findViewById(R.id.imgBack);
        btnCancle = dialog.findViewById(R.id.btnCancle);
        btnUpdatePass = dialog.findViewById(R.id.btnUpdatePass);
        etNewPass = dialog.findViewById(R.id.etNewPass);
        etReNewPass = dialog.findViewById(R.id.etReNewPass);
        etOldPass = dialog.findViewById(R.id.etOldPass);
        tvError = dialog.findViewById(R.id.tvError);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = etOldPass.getText().toString().trim();
                String newPass = etNewPass.getText().toString().trim();
                String reNewPass = etReNewPass.getText().toString().trim();
                if (!oldPass.equals(Login.currentAccount.getPassword())){
                    tvError.setText("Mật khẩu cũ không đúng");
                }else if (newPass.equals(oldPass)){
                    tvError.setText("Mật khẩu mới không được phép trùng mật khẩu cũ");
                }else if (oldPass.equals("") ||
                        newPass.equals("") ||
                        reNewPass.equals("")) {
                    tvError.setText("Xin vui lòng điền đầy đủ thông tin");
                } else if (!reNewPass.equals(newPass)) {
                    tvError.setText("Xác nhận mật khẩu mới không trùng khớp");
                } else if (newPass.length()<8){
                    tvError.setText("Mật khẩu phải trên 8 ký tự");
                }else {
                    Login.currentAccount.setPassword(newPass);
                    ApiController.apiService.updateUserPass(Login.currentAccount.get_id(), Login.currentAccount).enqueue(new Callback<Account>() {
                        @Override
                        public void onResponse(Call<Account> call, Response<Account> response) {
                            Toast.makeText(UserMainActivity.this, "Cập nhật mật khẩu thành công", Toast.LENGTH_LONG).show();
                            dialog.cancel();
                            Intent intent= new Intent(UserMainActivity.this, Login.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Account> call, Throwable t) {
                            Toast.makeText(UserMainActivity.this, "Lỗi Server API", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }

        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(getBaseContext());
        menuInflater.inflate(R.menu.menu_user, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLogOut:
                onBackPressed();
                Toast.makeText(getBaseContext(), "Đã đăng xuất", Toast.LENGTH_LONG).show();
                break;

            case R.id.menuChangPass:
                showChangePasswordDialog();
                break;
            case R.id.menuNapTien:
                showNapTienDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNapTienDialog() {
        final Dialog dialog = new Dialog(UserMainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_nap_tien);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtributes = window.getAttributes();
        windowAtributes.gravity = Gravity.CENTER_VERTICAL;
        window.setAttributes(windowAtributes);

        dialog.setCancelable(false);

        ImageView imgBack = dialog.findViewById(R.id.imgBack);
        EditText edtSoTien = dialog.findViewById(R.id.edtSoTien);
        Button btnNap = dialog.findViewById(R.id.btnNap);
        Button btnCancle = dialog.findViewById(R.id.btnCancle);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnNap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    long tien = Long.parseLong(edtSoTien.getText().toString().trim());
                    if (tien <= 0) {
                        throw new NumberFormatException("Số tiền phải lớn hơn 0");
                    }
                    Login.currentAccount.setMoney(Login.currentAccount.getMoney() + tien);
                    ApiController.apiService.updateUserMoney(Login.currentAccount.get_id(), Login.currentAccount).enqueue(new Callback<Account>() {
                        @Override
                        public void onResponse(Call<Account> call, Response<Account> response) {
                            if (response.isSuccessful()) {
                                actionBar.setTitle("User: " + Login.currentAccount.getUsername() + " == $: " + Login.currentAccount.getMoney() + "VNĐ");
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserMainActivity.this);
                                builder.setTitle("Thông báo hệ thống");
                                builder.setMessage("Bạn đã nạp thành công " + tien + "VNĐ");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                builder.show();
                                dialog.dismiss();
                            } else {
                                String err = "";
                                try {
                                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                    err = jsonObject.getString("message");
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserMainActivity.this);
                                builder.setTitle("Thông báo hệ thống");
                                builder.setMessage(err);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                builder.show();
//                                Toast.makeText(UserMainActivity.this, "Có lỗi xảy ra: "+ err, Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<Account> call, Throwable t) {
                            Toast.makeText(UserMainActivity.this, "Lỗi đường truyền", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (NumberFormatException e) {
                    Toast.makeText(UserMainActivity.this, "Số tiền không hợp lệ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        dialog.show();
    }
}