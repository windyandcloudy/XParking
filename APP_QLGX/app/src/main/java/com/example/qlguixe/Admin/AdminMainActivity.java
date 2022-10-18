package com.example.qlguixe.Admin;

import static com.example.qlguixe.Admin.ParkingFragment.binding;
import static com.example.qlguixe.Admin.ParkingFragment.statisticals;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlguixe.ApiController.ApiController;
import com.example.qlguixe.Models.Statistical;
import com.example.qlguixe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import kotlin.text.Regex;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminMainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CUPTURE = 1;
    ChipNavigationBar chipNavigationBar;
    public static Bitmap imageBitmap;
    public static boolean XUATXE = false;
    public static boolean GUIXE = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        chipNavigationBar = findViewById(R.id.bottomNav);
        chipNavigationBar.setItemSelected(R.id.home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ParkingFragment()).commit();
        bottomMenu();
    }

    @SuppressLint("NonConstantResourceId")
    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(i -> {
            Fragment fragment = null;
            switch (i) {
                case R.id.home:
                    fragment = new ParkingFragment();
                    break;
                case R.id.statistical:
                    fragment = new StatisticalFragment();
                    break;
                case R.id.user:
                    fragment = new UserFragment();
                    break;
            }
            assert fragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (cameraPermission) {
                Toast.makeText(AdminMainActivity.this, "Permissions Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AdminMainActivity.this, "Permissions Dinied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //gui xe
        if (GUIXE) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            // below line is to set the
            // image bitmap to our image.
//            img.setImageBitmap(imageBitmap);
            Dialog dialog = new Dialog(AdminMainActivity.this);
            dialog.setContentView(R.layout.dialog_guixe);

            TextView tvBienSo = dialog.findViewById(R.id.tvBienSo);

            Button btnOK = dialog.findViewById(R.id.btnOK);
            ImageView imgBienSo = dialog.findViewById(R.id.imgBienSo);

            imgBienSo.setImageBitmap(imageBitmap);

            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);

                    // below line is to create a variable for detector and we
                    // are getting vision text detector from our firebase vision.
                    FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();

                    // adding on success listener method to detect the text from image.
                    detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText text) {

                            List<FirebaseVisionText.Block> blocks = text.getBlocks();

                            // checking if the size of the
                            // block is not equal to zero.
                            if (blocks.size() == 0) {
                                // if the size of blocks is zero then we are displaying
                                // a toast message as no text detected.
                                Toast.makeText(AdminMainActivity.this, "No Text ", Toast.LENGTH_LONG).show();
                                return;
                            }
                            // extracting data from each block using a for loop.
                            for (FirebaseVisionText.Block block : text.getBlocks()) {
                                // below line is to get text
                                // from each block.
                                String txt = block.getText();

                                // below line is to set our
                                // string to our text view.

                                Log.d("bs", "onSuccess: "+ txt);
                                Log.d("bs", "length: "+ txt.length());

                                for (int i=0; i<txt.length(); i++){
                                    Log.d("bs", "char:"+ txt.charAt(i)+"==="+(int) txt.charAt(i)+"===");
                                    if (((int) txt.charAt(i)<48 || (int) txt.charAt(i)>58) && ((int) txt.charAt(i)<65||(int) txt.charAt(i)>90)){
                                        txt= txt.substring(0, i)+txt.substring(i+1);
                                    }
                                }
                                Log.d("bs", "onSuccess: "+ txt);
                                Log.d("bs", "length: "+ txt.length());
                                tvBienSo.setText(txt);
                                if (txt!=null && !txt.equals("")){
                                    parking(txt);
                                    dialog.dismiss();
                                }else {
                                    Toast.makeText(AdminMainActivity.this, "Không đọc được biển số xe", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }


                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // handling an error listener.
                            Toast.makeText(AdminMainActivity.this, "Fail to detect the text from image..", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            dialog.show();

            GUIXE = false;
        }

        //initial intent result
        if (XUATXE) {
            XUATXE = false;
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            //check condition
            if (intentResult != null && intentResult.getContents() != null) {
                if (intentResult.getContents().length() < 10) {
                    Toast.makeText(AdminMainActivity.this, "QR KHÔNG HỢP LỆ", Toast.LENGTH_SHORT).show();
                } else {
                    ApiController.apiService.deparking(intentResult.getContents()).enqueue(new Callback<Statistical>() {
                        @Override
                        public void onResponse(Call<Statistical> call, Response<Statistical> response) {
                            if (response.isSuccessful()) {
                                Statistical statistical = response.body();
                                final Dialog dialog = new Dialog(AdminMainActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.item_xe_da_xuat);

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

                                TextView tvLoaiXe = dialog.findViewById(R.id.tvLoaiXeX);
                                TextView tvTenXe = dialog.findViewById(R.id.tvTenXeX);
                                TextView tvChuXe = dialog.findViewById(R.id.tvChuXeX);
                                TextView tvBienSo = dialog.findViewById(R.id.tvBienSoX);
                                TextView tvGioGui = dialog.findViewById(R.id.tvGioGuiX);
                                TextView tvNgayGui = dialog.findViewById(R.id.tvNgayGuiX);
                                TextView tvGioNhan = dialog.findViewById(R.id.tvGioNhanX);
                                TextView tvNgayNhan = dialog.findViewById(R.id.tvNgayNhanX);


                                tvLoaiXe.setText("XUẤT XE=========\nLoại xe: " + statistical.getTransport().getTrans_type());
                                tvTenXe.setText("Tên xe: " + statistical.getTransport().getTrans_name());
                                tvChuXe.setText("Chủ xe: " + statistical.getTransport().getOwn().getUsername());
                                tvBienSo.setText("Biển số: " + statistical.getTransport().getTrans_license());
                                tvGioGui.setText("Giờ gửi: " + statistical.getTimeCome().split(" ")[1]);
                                tvNgayGui.setText("Ngày gửi: " + statistical.getTimeCome().split(" ")[0]);
                                tvGioNhan.setText("Giờ nhận: " + statistical.getTimeOut().split(" ")[1]);
                                tvNgayNhan.setText("Ngày nhận: " + statistical.getTimeOut().split(" ")[0]);

//                            for (int i=0; i<statisticals.size(); i++){
//                                if (statisticals.get(i).getTransport().getTrans_license().equals(statistical.getTransport().getTrans_license())){
//                                    statisticals.remove(i);
//                                    adapter.setStatisticals(statisticals);
//                                }
//                            }

                                dialog.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);
                                builder.setTitle("OPPS......");
                                builder.setMessage("Không tìm thấy xe trong bãi. Hãy liên hệ với bảo vệ");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                builder.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Statistical> call, Throwable t) {
                            Toast.makeText(AdminMainActivity.this, "Lỗi đường truyền", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            } else {
                Toast.makeText(AdminMainActivity.this, "OPPPS.... Không có gì được tìm thấy", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void parking(String bienSo) {
        if (bienSo.equals("")){
            Toast.makeText(AdminMainActivity.this, "Biển số không được để trống", Toast.LENGTH_SHORT).show();
        }else {
            ApiController.apiService.parking(bienSo).enqueue(new Callback<Statistical>() {
                @Override
                public void onResponse(Call<Statistical> call, Response<Statistical> response) {
                    if (response.isSuccessful()){
                        Statistical statistical= response.body();
                        statisticals.add(statistical);
                        ParkingFragment.adapter.setStatisticals(statisticals);
                        binding.tvSoXeDangGui.setText("SỐ XE ĐANG GỬI: "+ statisticals.size()+ "/1000");
                        binding.edtBienSo.setText("");
                        Toast.makeText(AdminMainActivity.this, "Gửi xe "+bienSo+" thành công", Toast.LENGTH_SHORT).show();
                    }else {
                        String err="";
                        try {
                            JSONObject jsonObject= new JSONObject(response.errorBody().string());
                            err= jsonObject.getString("message");
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                        AlertDialog.Builder builder= new AlertDialog.Builder(AdminMainActivity.this);
                        builder.setTitle("Thông báo hệ thống");
                        builder.setMessage(err);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();
//                        Toast.makeText(AdminMainActivity.this, "Server thông báo: "+ err, Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Statistical> call, Throwable t) {
                    Toast.makeText(AdminMainActivity.this, "Lỗi đường truyền", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}