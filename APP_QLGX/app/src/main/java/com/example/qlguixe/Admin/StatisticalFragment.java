package com.example.qlguixe.Admin;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.qlguixe.ApiController.ApiController;
import com.example.qlguixe.Models.Statistical;
import com.example.qlguixe.R;
import com.example.qlguixe.databinding.FragmentStatisticalBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StatisticalFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatisticalFragment() {
        // Required empty public constructor
    }


    public static StatisticalFragment newInstance(String param1, String param2) {
        StatisticalFragment fragment = new StatisticalFragment();
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

    FragmentStatisticalBinding binding;
    ThongKeXeAdapter adapter;
    List<Statistical> statisticals;

    String[] ngay = {"1", "2", "3", "4", "5", "6", "7"} ;
    String[] thang = {"1", "2", "3"} ;
    String ngayS="";
    String thangS="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_statistical, container, false);
        //render các xe đã gửi
        getAllXeParked();
        generateDate();
        selectTimeParked();
        choiceOptionFilter();

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.cbNgay.isChecked()){
                    filterByTimeCome(ngayS);
                }else if(binding.cbThang.isChecked()) {
                    filterByTimeCome(thangS);
                }else {
                    Toast.makeText(getActivity().getBaseContext(), "HÃY CHỌN TÌM THEO NGÀY HOẶC THÁNG", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.edtSearchTenChuXe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str= editable.toString();
                List<Statistical> list= new ArrayList<>();
                for(Statistical st: statisticals){
                    if (st.getTransport().getTrans_name().toLowerCase().contains(str.toLowerCase())){
                        list.add(st);
                    }
                }
                adapter.updateList(list);
            }
        });

        binding.btnAllXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllXeParked();
            }
        });

        return binding.getRoot();
    }

    private void filterByTimeCome(String timeCome) {
        List<Statistical> list= new ArrayList<>();
        for(Statistical st: statisticals){
            if (st.getTimeCome().contains(timeCome)){
                list.add(st);
            }
        }
        adapter.updateList(list);
    }

    private void choiceOptionFilter() {
        binding.cbThang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.cbThang.isChecked()){
                    binding.cbNgay.setChecked(false);
                }
            }
        });
        binding.cbNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.cbNgay.isChecked()) {
                    binding.cbThang.setChecked(false);
                }
            }
        });
    }

    private void selectTimeParked() {
        //spn ngay
        binding.spnNgay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ngayS= ngay[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapterNgay = new ArrayAdapter(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, ngay);
        adapterNgay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnNgay.setAdapter(adapterNgay);

        //spn tháng
        binding.spnThang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                thangS= thang[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapterthang = new ArrayAdapter(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, thang);
        adapterthang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnThang.setAdapter(adapterthang);
    }

    private void generateDate() {
        Date myDate= new Date();
        for (int i=1; i<=7; i++){
            ngay[i-1]= new SimpleDateFormat("dd-MM-yyyy",
                    Locale.getDefault()).format(new Date(myDate.getTime()-i*(24 * 3600000)));
        }
        for (int i=0; i<3; i++){
            String []str= new SimpleDateFormat("dd-MM-yyyy",
                    Locale.getDefault()).format(new Date()).substring(3).split("-");
            int m= Integer.parseInt(str[0])-i;
            if (m==0){
                m=12;
            }else if (m==-1){
                m=11;
            }
            thang[i]= m+"-"+str[1];
        }
    }

    private void getAllXeParked() {
        ApiController.apiService.getAllIn("1").enqueue(new Callback<List<Statistical>>() {
            @Override
            public void onResponse(Call<List<Statistical>> call, Response<List<Statistical>> response) {
                if (response.isSuccessful()){
                    statisticals= response.body();
                    adapter= new ThongKeXeAdapter(statisticals, getActivity().getBaseContext());
                    RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity().getBaseContext(), RecyclerView.VERTICAL, false);
                    binding.revThongKe.setLayoutManager(layoutManager);
                    binding.revThongKe.setAdapter(adapter);
//                    adapter.setiOnClickItemXe(new IOnClickItemStatistical() {
//                        @Override
//                        public void iOnClickItemStatistical(Statistical statistical, int position) {
//                            AlertDialog.Builder builder= new AlertDialog.Builder(getActivity().getBaseContext());
//                            builder.setTitle("========XÓA XE=======");
//                            builder.setMessage("Bạn có muốn xóa lịch sử này vĩnh viễn");
//                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    ApiController.apiService.deleteStatistical(statistical.get_id()).enqueue(new Callback<String>() {
//                                        @Override
//                                        public void onResponse(Call<String> call, Response<String> response) {
//                                            if (response.isSuccessful()){
//                                                statisticals.remove(position);
//                                                adapter.updateList(statisticals);
//                                                Toast.makeText(getActivity().getBaseContext(), "Xóa lịch sử thành công", Toast.LENGTH_SHORT).show();
//                                            }else {
//                                                Toast.makeText(getActivity().getBaseContext(), "Server bảo trì: không xóa được", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<String> call, Throwable t) {
//                                            Toast.makeText(getActivity().getBaseContext(), "Lỗi đường truyền", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//
//                                    dialogInterface.dismiss();
//                                }
//                            });
//                            builder.setNegativeButton("HỦY", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
//                                }
//                            });
//                            builder.show();
//                        }
//                    });
                }else {
                    String err="";
                    try {
                        JSONObject jsonObject= new JSONObject(response.errorBody().string());
                        err= jsonObject.getString("message");
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity().getBaseContext(), "Có lỗi xảy ra: "+ err, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Statistical>> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), "Lỗi đường truyền", Toast.LENGTH_SHORT).show();
            }
        });


    }

}