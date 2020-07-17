package com.mohamedsaeed555.ecommerce;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.Result;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Product_class;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerViewForAmount extends Fragment implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    String t="";


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
        t= getArguments().getString("sale","");



    }

    @Override
    public void handleResult(Result result) {
        final Database db = new Database(getActivity());
        Product_class productClass ;
        if (db.Search_product2("AllData",result.getText()).size() > 0){

            if (t.equals("sale")){
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
                SaleFragment myFragment = new SaleFragment();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, myFragment).addToBackStack(null).commit();
            }else {
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
                AmountFragment myFragment = new AmountFragment();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, myFragment).addToBackStack(null).commit();

            }


        }else {
            if (t.equals("sale")){
            Bundle bundle = new Bundle();
            bundle.putString("collection_name","");
            bundle.putString("barcode","");
            bundle.putString("name","");
            bundle.putString("date","");
            bundle.putDouble("price",0);
            bundle.putString("brand","");
            bundle.putString("image","");
            bundle.putInt("amount",0);
            SaleFragment myFragment = new SaleFragment();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, myFragment).addToBackStack(null).commit();
            }else {
                Bundle bundle = new Bundle();
                bundle.putString("collection_name","");
                bundle.putString("barcode","");
                bundle.putString("name","");
                bundle.putString("date","");
                bundle.putDouble("price",0);
                bundle.putString("brand","");
                bundle.putString("image","");
                bundle.putInt("amount",0);
                AmountFragment myFragment = new AmountFragment();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.cotainers, myFragment).addToBackStack(null).commit();

            }
    }       }

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

