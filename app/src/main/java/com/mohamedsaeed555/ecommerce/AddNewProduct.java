package com.mohamedsaeed555.ecommerce;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Product_class;
import com.mohamedsaeed555.Notification.Notification_Class;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewProduct extends Fragment {


    ImageView img;
    Button btn_add, a;
    AutoCompleteTextView id, name, collection, brand, date, price, amount;
    TextInputLayout code, pname, pcollecion, pbrand, pprice, pamount, pdate;


    String[] list;
    ArrayList<String> list2 = new ArrayList<>();
    DatePickerDialog.OnDateSetListener mDateSetListener;
    Uri path;
    String Image_path = "";
    String collection_name = "";
    String brand_name = "";
    BottomSheetDialog bottomSheetDialog;
    String barcode = "";
    String Datepick = "";
    LottieAlertDialog alertDialog;
    Database db;
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("https://newaccsys.herokuapp.com");
        } catch (URISyntaxException e) {
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = new Database(getActivity());
        return inflater.inflate(R.layout.activity_add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("Add Product");
        barcode = getArguments().getString("id", "");
        btn_add = view.findViewById(R.id.button);
        img = view.findViewById(R.id.img);
        id = view.findViewById(R.id.filled_exposed_dropdown);
        name = view.findViewById(R.id.filled_exposed_dropdown2);
        collection = view.findViewById(R.id.filled_exposed_dropdown4);
        brand = view.findViewById(R.id.filled_exposed_dropdown3);
        date = view.findViewById(R.id.filled_exposed_dropdown5);
        price = view.findViewById(R.id.filled_exposed_dropdown6);
        amount = view.findViewById(R.id.filled_exposed_dropdown7);
        a = view.findViewById(R.id.vvv);
        code = view.findViewById(R.id.inputlayout);
        pname = view.findViewById(R.id.inputlayout2);
        pcollecion = view.findViewById(R.id.inputlayout4);
        pbrand = view.findViewById(R.id.inputlayout3);
        pprice = view.findViewById(R.id.inputlayout6);
        pamount = view.findViewById(R.id.inputlayout7);
        pdate = view.findViewById(R.id.inputlayout5);


        if (!(barcode == null || barcode.isEmpty()))
            id.setText(barcode);

        bottomSheetDialog = new BottomSheetDialog(getActivity());
        View v = getLayoutInflater().inflate(R.layout.bottom_sheat, null);
        bottomSheetDialog.setContentView(v);
        ImageView cam = v.findViewById(R.id.imageView);
        ImageView gal = v.findViewById(R.id.imageView4);
        TextView cancel = v.findViewById(R.id.cancel);


        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 2);
        }

        if (barcode != null) {
            id.setText(barcode);
        }

        list = getActivity().getResources().getStringArray(R.array.picker_items);
        list2 = db.GETALLBRAND();

        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEditText(s, code);
            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEditText(s, pname);
            }
        });
        collection.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEditText(s, pcollecion);
            }
        });
        brand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEditText(s, pbrand);
            }
        });
        date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEditText(s, pdate);
            }
        });
        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEditText(s, pprice);
            }
        });
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEditText(s, pamount);
            }
        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                DatePicker dp = findDatePicker((ViewGroup) dialog.getWindow().getDecorView());
                if (dp != null) {
                    ((ViewGroup) dialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);

                }
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month++;
                Datepick = year + "-" + month + "-" + day;
                String date2 = month + " / " + year;
                date.setText(date2);
            }
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
        collection.setAdapter(adapter);

        collection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                collection_name = list[position].toLowerCase();
            }
        });


        final ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list2);
        brand.setAdapter(adapter2);

        brand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                brand_name = list2.get(position);
            }
        });

        brand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int ss = brand.getAdapter().getCount();
                if (ss == 0) {
                    a.setVisibility(View.VISIBLE);
                } else {
                    a.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = brand.getText().toString().trim();
                if (!(s.isEmpty() || s == null)) {
                    Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                    db.insert_brand(s);
                    list2.add(s);
                    adapter2.add(s);
                    adapter2.notifyDataSetChanged();
                }
            }
        });


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();

            }
        });


        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    File photo_file = null;
                    photo_file = createphotoFile();
                    if (photo_file != null) {

                        Uri path2 = FileProvider.getUriForFile(getActivity(), "com.example.android.fileprovider", photo_file);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, path2);
                        startActivityForResult(Intent.createChooser(cameraIntent, "Select Camera"), 1);
                    }
                }
                bottomSheetDialog.dismiss();
            }
        });

        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
                bottomSheetDialog.dismiss();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nam = name.getText().toString().trim();
                String pric = price.getText().toString().trim();
                String barcod = id.getText().toString().trim();
                String dat = date.getText().toString().trim();
                String amoun = amount.getText().toString().trim();
                String bran = brand.getText().toString().trim();

                if (barcod == null || barcod.isEmpty()) {
                    code.setError("Please enter product barcode");
                    code.requestFocus();
                    return;
                } else if (nam == null || nam.isEmpty()) {
                    pname.setError("Please enter product name");
                    pname.requestFocus();
                    return;
                } else if (collection_name == null || collection_name.isEmpty()) {
                    pcollecion.setError("Please choose product collection");
                    pcollecion.requestFocus();
                    return;
                } else if (bran == null || bran.isEmpty()) {
                    pbrand.setError("Please choose product brand");
                    pbrand.requestFocus();
                    return;
                } else if (dat == null || dat.isEmpty()) {
                    pdate.setError("Please enter Expire date");
                    pdate.requestFocus();
                    return;
                } else if (pric == null || pric.isEmpty()) {
                    pprice.setError("Please enter product Price");
                    pprice.requestFocus();
                    return;
                } else if (amoun == null || amoun.isEmpty()) {
                    pamount.setError("Please enter available amount");
                    pamount.requestFocus();
                    return;
                } else {
                    String image = "";
                    if (Image_path.isEmpty() && path != null) {
                        image = getRealPathFromURI(path);
                    } else {
                        image = Image_path;
                    }
                    if (image.isEmpty() || image == null) {
                        Toast.makeText(getActivity(), "Please Choose photo", Toast.LENGTH_LONG).show();
                        return;
                    } else {

                        btn_add.setEnabled(false);
                        btn_add.setClickable(false);
                        btn_add.setBackground(getResources().getDrawable(R.drawable.btn_shape_enable));

                        alertDialog = new LottieAlertDialog.Builder(getActivity(), DialogTypes.TYPE_LOADING)
                                .setTitle("Loading")
                                .setDescription("Please wait until product added")
                                .build();
                        alertDialog.setCancelable(false);
                        alertDialog.show();

                        POST_NEW_PRODUCT(collection_name, Datepick, Integer.parseInt(amoun), barcod, nam, Double.parseDouble(pric), bran, image);
                        // Toast.makeText(getApplicationContext(),collection_name,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private File createphotoFile() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image_path = image.getAbsolutePath();
        return image;
    }


    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == getActivity().RESULT_OK && data != null) {
            path = data.getData();
            img.setImageURI(path);
        } else if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            Bitmap bitmapImage = BitmapFactory.decodeFile(Image_path);
            int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
            img.setImageBitmap(scaled);
        }

    }


    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    public void POST_NEW_PRODUCT(String coll_name, final String date1, int amount1, String barcode, String name1, double price1, String brand1, String image_path) {

        try {
            Database db = new Database(getActivity());

            File file = new File(image_path);

            // File file1 = Compressor.getDefault(getActivity()).compressToFile(file);

            File file1 = null;
            try {
                file1 = new Compressor(getActivity()).compressToFile(file);

            } catch (IOException e) {
                e.printStackTrace();
            }
            final RequestBody img2 = RequestBody.create(MediaType.parse("image/*"), file1);
            MultipartBody.Part part = MultipartBody.Part.createFormData("image", file1.getName(), img2);
            RequestBody nam = RequestBody.create(MediaType.parse("text/plain"), name1);
            RequestBody code = RequestBody.create(MediaType.parse("text/plain"), barcode);
            RequestBody bran = RequestBody.create(MediaType.parse("text/plain"), brand1);


            //Product_class productClass = new Product_class(date1,amount1,barcode,name1,price1,brand1,image_path);
            //Product_class productClass2 = new Product_class(date1,amount1,barcode,name1,price1,brand1,image_path,coll_name);

            RetrofitClient.getInstance().ADDPRODUCT(db.getAllusers().get(0).getToken(), coll_name, date1, amount1, code, nam, price1, bran, part)
                    .enqueue(new Callback<Product_class>() {
                        @Override
                        public void onResponse(Call<Product_class> call, Response<Product_class> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_LONG).show();
                                btn_add.setEnabled(true);
                                btn_add.setClickable(true);
                                btn_add.setBackground(getResources().getDrawable(R.drawable.btn_click));

                                return;
                            }

                            Notification_Class notification_class = new Notification_Class(
                                    db.getAllusers().get(0).getAdmin(), "Admin Added New Product",
                                    "details", coll_name, response.body(),response.body().getImage(),
                                    db.getAllusers().get(0).get_id(),new Random().nextInt());
                            Gson gson = new Gson();

                            mSocket.emit("dbchanged", gson.toJson(notification_class));

                            amount.setText("");
                            brand.setText("");
                            collection.setText("");
                            id.setText("");
                            name.setText("");
                            price.setText("");
                            date.setText("");
                            img.setImageResource(R.drawable.pick);
                            Product_class productClass = new Product_class(response.body().getDate(), response.body().getAmount(),
                                    response.body().getBarcode(), response.body().getName(), response.body().getPrice(), response.body().getBrand(),
                                    response.body().getImage(), coll_name);
                            db.insert_product_toAlldata("AllData", productClass);
                            alertDialog.dismiss();

                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Add Product")
                                    .setContentText("product added successfully")
                                    .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                            btn_add.setEnabled(true);
                                            btn_add.setClickable(true);
                                            btn_add.setBackground(getResources().getDrawable(R.drawable.btn_click));
                                        }
                                    })
                                    .show();
                        }

                        @Override
                        public void onFailure(Call<Product_class> call, Throwable t) {
                            alertDialog.dismiss();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Something went wrong!")
                                    .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                            btn_add.setEnabled(true);
                            btn_add.setClickable(true);
                            btn_add.setBackground(getResources().getDrawable(R.drawable.btn_click));

                        }
                    });

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.barcode, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.bar) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction().replace(R.id.cotainers, new ScannerView()).addToBackStack(null).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void validateEditText(Editable s, TextInputLayout layout) {
        if (!TextUtils.isEmpty(s)) {
            layout.setError(null);
        }

    }
}
