package com.mohamedsaeed555.ecommerce;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.zxing.Result;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Product_class;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannerViewForSearch extends Fragment implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.scannerview,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Database db = new Database(getActivity());
        scannerView=(ZXingScannerView)view.findViewById(R.id.scanner);

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{ Manifest.permission.CAMERA}, 1);
        }


    }

    @Override
    public void handleResult(Result result) {
        final Database db = new Database(getActivity());
        Product_class productClass ;
        if (db.Search_product2("AllData",result.getText()).size() > 0){
            productClass=db.Search_product2("AllData",result.getText()).get(0);
            Bundle bundle = new Bundle();
            bundle.putString("collection_name",productClass.getCollection());
            bundle.putString("barcode",productClass.getBarcode());
            bundle.putString("name",productClass.getName());
            bundle.putString("date",productClass.getDate());
            bundle.putDouble("price",productClass.getPrice());
            bundle.putString("brand",productClass.getBrand());
            bundle.putString("image",productClass.getImage());
            bundle.putInt("amount",productClass.getAmount());
            DetailsProductActivity myFragment = new DetailsProductActivity();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, myFragment).addToBackStack(null).commit();
    }else {
            Toast.makeText(getActivity(),"Product not exist",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}

