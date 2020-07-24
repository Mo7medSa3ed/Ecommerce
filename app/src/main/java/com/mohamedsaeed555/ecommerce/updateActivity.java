package com.mohamedsaeed555.ecommerce;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Product_class;
import com.mohamedsaeed555.MyDataBase.Users;
import com.mohamedsaeed555.Notification.Notification_Class;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class updateActivity extends Fragment {


    ImageView img;
    Button btn_add, btn_delete;
    AutoCompleteTextView id, name, collection, brand, date, price, amount;

    Users users;
    Database db;

    String[] list;
    String[] list2;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    TextInputLayout code, pname, pcollecion, pbrand, pprice, pamount, pdate;
    Uri path;
    String Image_path = "";
    Retrofit_Interface retrofit_interface;
    String brand_name = "";
    ProgressDialog progressDialog;
    BottomSheetDialog bottomSheetDialog;
    String DatePick = "";
    String DateUpload = "";
    LottieAlertDialog alertDialog;
    private String item_Selected = "";
    private String collection_name = "";
    private String barcode;
    private String image1 = "";
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
        users = db.getAllusers().get(0);
        return inflater.inflate(R.layout.activity_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("Update/Delete");
        collection_name = getArguments().getString("collection_name");
        barcode = getArguments().getString("barcode");
        String date1 = getArguments().getString("date");
        String name1 = getArguments().getString("name");
        String price1 = getArguments().getString("price");
        String amount1 = getArguments().getString("amount");
        String brand1 = getArguments().getString("brand");
        image1 = getArguments().getString("image");

        btn_add = view.findViewById(R.id.button2);
        btn_delete = view.findViewById(R.id.button);
        img = view.findViewById(R.id.img);
        id = view.findViewById(R.id.filled_exposed_dropdown);
        name = view.findViewById(R.id.filled_exposed_dropdown2);
        collection = view.findViewById(R.id.filled_exposed_dropdown4);
        brand = view.findViewById(R.id.filled_exposed_dropdown3);
        date = view.findViewById(R.id.filled_exposed_dropdown5);
        price = view.findViewById(R.id.filled_exposed_dropdown6);
        amount = view.findViewById(R.id.filled_exposed_dropdown7);
        code = view.findViewById(R.id.inputlayout);
        pname = view.findViewById(R.id.inputlayout2);
        pcollecion = view.findViewById(R.id.inputlayout4);
        pbrand = view.findViewById(R.id.inputlayout3);
        pprice = view.findViewById(R.id.inputlayout6);
        pamount = view.findViewById(R.id.inputlayout7);
        pdate = view.findViewById(R.id.inputlayout5);

        bottomSheetDialog = new BottomSheetDialog(getActivity());
        View v = getLayoutInflater().inflate(R.layout.bottom_sheat, null);
        bottomSheetDialog.setContentView(v);
        ImageView cam = v.findViewById(R.id.imageView);
        ImageView gal = v.findViewById(R.id.imageView4);
        TextView cancel = v.findViewById(R.id.cancel);


        Picasso.get().load(image1).placeholder(R.drawable.haircode).into(img);
        id.setText(barcode);
        String d = date1.substring(0, 10);
        date.setText(d);
        name.setText(name1);
        price.setText(price1);
        amount.setText(amount1);
        brand.setText(brand1);
        collection.setText(collection_name);


        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 2);
        }

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
                DatePick = year + "-" + month + "-" + day;
                String date2 = month + " / " + year;
                date.setText(date2);
            }
        };

        list = getActivity().getResources().getStringArray(R.array.picker_items);
        list2 = getActivity().getResources().getStringArray(R.array.brand_items);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
        collection.setAdapter(adapter);

        collection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                collection_name = list[position].toLowerCase();
            }
        });


        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list2);
        brand.setAdapter(adapter2);

        brand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                brand_name = list2[position];
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
                        startActivityForResult(cameraIntent, 1);
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
                startActivityForResult(intent, 0);
                bottomSheetDialog.dismiss();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

       /* img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    File photo_file = null;
                    photo_file = createphotoFile();
                    if (photo_file != null) {
                         Uri path2 = FileProvider.getUriForFile(getActivity(), "com.example.android.fileprovider", photo_file);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, path2);
                        startActivityForResult(cameraIntent, 1);
                    }
                }
            }
        });
*/

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog = new LottieAlertDialog.Builder(getActivity(), DialogTypes.TYPE_LOADING)
                        .setTitle("Loading")
                        .setDescription("Please wait until product updated")
                        .build();
                alertDialog.setCancelable(true);
                alertDialog.show();


                String name2 = name.getText().toString().trim();
                String price2 = price.getText().toString().trim();
                String barcode = id.getText().toString().trim();
                String date2 = date.getText().toString().trim();
                String amount2 = amount.getText().toString().trim();
                String brand2 = brand.getText().toString().trim();
                if (barcode == null || barcode.isEmpty()) {
                    code.setError("Please enter product barcode");
                    code.requestFocus();
                    return;
                } else if (name2 == null || name2.isEmpty()) {
                    pname.setError("Please enter product name");
                    pname.requestFocus();
                    return;
                } else if (collection_name == null || collection_name.isEmpty()) {
                    pcollecion.setError("Please choose product collection");
                    pcollecion.requestFocus();
                    return;
                } else if (brand2 == null || brand2.isEmpty()) {
                    pbrand.setError("Please choose product brand");
                    pbrand.requestFocus();
                    return;
                } else if (date2 == null || date2.isEmpty()) {
                    pdate.setError("Please enter Expire date");
                    pdate.requestFocus();
                    return;
                } else if (price2 == null || price2.isEmpty()) {
                    pprice.setError("Please enter product Price");
                    pprice.requestFocus();
                    return;
                } else if (amount2 == null || amount2.isEmpty()) {
                    pamount.setError("Please enter available amount");
                    pamount.requestFocus();
                    return;
                } else {

                    String image = "";
                    if (Image_path.isEmpty() && path != null) {
                        image = getRealPathFromURI(path);
                        // Toast.makeText(getApplicationContext(),image,Toast.LENGTH_LONG).show();
                    } else {
                        image = Image_path;
                        //Toast.makeText(getApplicationContext(),image,Toast.LENGTH_LONG).show();
                    }
                    //image=getRealPathFromURI(path);

                    if (DatePick == null || DatePick.isEmpty()) {
                        DateUpload = date2;
                    } else {
                        DateUpload = DatePick;
                    }


                    Update_product(collection_name, DateUpload, Integer.parseInt(amount2), barcode, name2, Double.parseDouble(price2), brand2, image);


                    // Toast.makeText(getApplicationContext(),image,Toast.LENGTH_LONG).show();
                }
            }
        });


        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database db = new Database(getActivity());
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to delete this product!")
                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog
                                        .setTitleText("Deleted!")
                                        .setContentText("Your imaginary product has been deleted!")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                RetrofitClient.getInstance().DELETEPRODUCT(users.getToken(), collection_name, barcode).enqueue(new Callback<Void>() {
                                                    @Override
                                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                                        if (response.isSuccessful()) {

                                                            amount.setText("");
                                                            brand.setText("");
                                                            collection.setText("");
                                                            id.setText("");
                                                            name.setText("");
                                                            price.setText("");
                                                            date.setText("");
                                                            db.Delete_product2(collection_name, barcode);
                                                            db.Delete_product2("AllData", barcode);
                                                            db.Delete_product2("Cart", barcode);
                                                            img.setImageResource(R.drawable.pick);
                                                            img.setImageResource(R.drawable.haircode);
                                                            sDialog.dismissWithAnimation();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Void> call, Throwable t) {

                                                    }
                                                });
                                            }
                                        })
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .show();
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
            Bitmap bitmap = BitmapFactory.decodeFile(Image_path);
            img.setImageBitmap(bitmap);
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


    public void Update_product(String collection_name, String date3, int amount3, String barcode, String name3, double price3, String brand3, String image_path) {
        Database db = new Database(getActivity());
        MultipartBody.Part part = null;
        if (image_path != "") {
            File file = new File(image_path);
            File file1 = null;
            try {
                file1 = new Compressor(getActivity()).compressToFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            final RequestBody img2 = RequestBody.create(MediaType.parse("image/*"), file1);
            part = MultipartBody.Part.createFormData("image", file1.getName(), img2);
        }

        RequestBody nam = RequestBody.create(MediaType.parse("text/plain"), name3);
        RequestBody code = RequestBody.create(MediaType.parse("text/plain"), barcode);
        RequestBody bran = RequestBody.create(MediaType.parse("text/plain"), brand3);


        RetrofitClient.getInstance().UPDATEPRODUCT(users.getToken(), collection_name, date3, amount3, code, nam, price3, bran, part)
                .enqueue(new Callback<Product_class>() {
                    @Override
                    public void onResponse(Call<Product_class> call, Response<Product_class> response) {
                        if (!response.isSuccessful()) {
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
                            return;
                        }

                        Notification_Class notification_class = new Notification_Class(
                                db.getAllusers().get(0).getAdmin(), "Admin Updated Product",
                                "details", collection_name, response.body()
                        );

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
                                response.body().getImage(), collection_name);

                        db.update_product2("AllData", productClass, barcode);

                        img.setImageResource(R.drawable.haircode);
                        alertDialog.dismiss();

                        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Update Product")
                                .setContentText("product updated successfully")
                                .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.cotainers, new HomeFragment()).commit();
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
                    }
                });

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.delete, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Database db = new Database(getActivity());
        int id2 = item.getItemId();
        if (id2 == R.id.delete) {

            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("You want to delete this product!")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            RetrofitClient.getInstance().DELETEPRODUCT(users.getToken(), collection_name, barcode)
                                    .enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if (response.isSuccessful()) {
                                                amount.setText("");
                                                brand.setText("");
                                                collection.setText("");
                                                id.setText("");
                                                name.setText("");
                                                price.setText("");
                                                date.setText("");
                                                db.Delete_product2("AllData", barcode);
                                                db.Delete_product2("Cart", barcode);
                                                db.Delete_Fav(barcode);
                                                img.setImageResource(R.drawable.pick);
                                                img.setImageResource(R.drawable.haircode);
                                                sweetAlertDialog.dismissWithAnimation();

                                                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                                        .setTitleText("Delete Product")
                                                        .setContentText("product deleted successfully")
                                                        .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                sweetAlertDialog.dismissWithAnimation();
                                                            }
                                                        })
                                                        .show();

                                            } else {
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
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(getActivity(), DialogTypes.TYPE_ERROR)
                                                    .setTitle("Error")
                                                    .setDescription("Some error has happened.")
                                                    .setPositiveText("Okay")
                                                    .setPositiveListener(new ClickListener() {
                                                        @Override
                                                        public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                                                            lottieAlertDialog.dismiss();
                                                        }
                                                    })
                                                    .build();
                                            alertDialog.setCancelable(false);
                                            alertDialog.show();
                                        }
                                    });
                        }
                    })
                    .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .show();

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

/*


 */
