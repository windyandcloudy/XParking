package com.example.qlguixe.Admin;

import static android.Manifest.permission.CAMERA;

import static com.example.qlguixe.Admin.AdminMainActivity.GUIXE;
import static com.example.qlguixe.Admin.AdminMainActivity.XUATXE;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlguixe.ApiController.ApiController;
import com.example.qlguixe.Capture;

import com.example.qlguixe.Models.Statistical;
import com.example.qlguixe.R;
import com.example.qlguixe.databinding.FragmentParkingBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ParkingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ParkingFragment() {
        // Required empty public constructor
    }


    public static ParkingFragment newInstance(String param1, String param2) {
        ParkingFragment fragment = new ParkingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    public static FragmentParkingBinding binding;
    public static XeTrongBaiAdapter adapter;
    public static List<Statistical> statisticals;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_parking, container, false);

        //getAllXeDangGui
        statisticals= new ArrayList<>();
        getAllXeDangGui();
        binding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermissions()){
                    requestPermission();
                }else {
                    GUIXE= true;
                    guiXe();
                }

            }
        });

        binding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XUATXE= true;
                xuatXe();
            }
        });

        return binding.getRoot();
    }

    private void xuatXe() {
        //init intent integrator
        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
        //set promt text
        intentIntegrator.setPrompt("Bấm nút tăng âm lượng để bật đèn pin");
        //set beep
        intentIntegrator.setBeepEnabled(true);
        //locked orientation
        intentIntegrator.setOrientationLocked(true);
        //set capture activity
        intentIntegrator.setCaptureActivity(Capture.class);
        //initial scan
        intentIntegrator.initiateScan();
        getAllXeDangGui();
    }


    private void getAllXeDangGui() {
        ApiController.apiService.getAllIn("0").enqueue(new Callback<List<Statistical>>() {
            @Override
            public void onResponse(Call<List<Statistical>> call, Response<List<Statistical>> response) {
                if (response.isSuccessful()){
                    statisticals= response.body();
                    binding.tvSoXeDangGui.setText("SỐ XE ĐANG GỬI: "+ statisticals.size()+"/1000");
                    adapter= new XeTrongBaiAdapter(statisticals, getContext());
                    RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                    binding.revCacXeTrongBai.setLayoutManager(layoutManager);
                    binding.revCacXeTrongBai.setAdapter(adapter);
                }else {
                    Toast.makeText(getActivity().getBaseContext(), "Lỗi Server API", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Statistical>> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "Lỗi đường truyền: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean checkPermissions(){
        int camerPermission= ContextCompat.checkSelfPermission(getContext(), CAMERA);
        return camerPermission== PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        int PERMISSION_CODE= 200;
        ActivityCompat.requestPermissions(getActivity(), new String[]{CAMERA}, PERMISSION_CODE);
    }


    private void guiXe() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
        }
    }
}