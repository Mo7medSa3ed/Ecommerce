package com.mohamedsaeed555.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Product_class;
import java.util.ArrayList;


public class SplashActivity extends AppCompatActivity {

    private ArrayList<Product_class> array =new ArrayList<>();
    Database db = new Database(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);




   /*     GET("cosmatics");
        GET("medical");
        GET("papers");
        GET("makeup");
        GET("others");


        Toast.makeText(getApplicationContext(),String.valueOf(db.getAllProducts("AllData").size()),Toast.LENGTH_LONG).show();


*/

        if (db.getAllusers().size() != 0 || db.getAllusers().size()>0){
            //second

            Intent intent = new Intent(SplashActivity.this,testAcivity.class);
            startActivity(intent);
            finish();
        }else {
            //main
            Intent intent = new Intent(SplashActivity.this,testAcivity.class);
            startActivity(intent);
            finish();
        }





        /*GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null || AccessToken.getCurrentAccessToken()!=null ){
            Intent intent = new Intent(SplashActivity.this,SecondActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }*/

    }


/*
    public void GET (String collection_name ){
        RetrofitClient.getInstance().GETALLPRODUCTS(collection_name).enqueue(new Callback<List<Product_class>>() {
            @Override
            public void onResponse(Call<List<Product_class>> call, Response<List<Product_class>> response) {
                if (!(response.isSuccessful())){
                    Toast.makeText(getApplicationContext(),String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                }

                for (Product_class p : response.body()){
                    array.add(p);
                    db.insert_product2("AllData",p);
                }

            }
            @Override
            public void onFailure(Call<List<Product_class>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
*/

}
