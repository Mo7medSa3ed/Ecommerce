package com.mohamedsaeed555.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.zxing.Result;

import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerView extends Fragment implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.scannerview,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scannerView=(ZXingScannerView)view.findViewById(R.id.scanner);
    }

    @Override
    public void handleResult(Result result) {
        if (!(result.toString().isEmpty())){
        Bundle bundle = new Bundle();
        bundle.putString("id",result.getText().toString());
        AddNewProduct frag = new AddNewProduct();
        frag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.cotainers,frag).addToBackStack(null).commit();
    }
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();

    }
}
/*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);
        View inflater = getLayoutInflater().inflate(R.layout.activity_second,null);
        continer = inflater.findViewById(R.id.cotainers);
    }

    @Override
    public void handleResult(Result result) {
      Intent intent = new Intent(this , SecondActivity.class);
      intent.putExtra("id",result.getText().toString());
      intent.putExtra("open_add","open_add2");
      startActivity(intent);

     // getSupportFragmentManager().beginTransaction().replace(continer.getId(),new AddNewProduct()).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}
*/
