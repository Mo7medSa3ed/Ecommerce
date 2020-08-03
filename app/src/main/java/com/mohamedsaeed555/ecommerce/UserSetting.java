package com.mohamedsaeed555.ecommerce;


import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.mohamedsaeed555.MyDataBase.Database;
import com.mohamedsaeed555.MyDataBase.Users;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserSetting extends Fragment {

    TextInputLayout namelayout, citylayout, emaillayout, mobilelayout, addresslayout;
    AutoCompleteTextView nametext, citytext, emailtext, mobiletext, addresstext;
    CircleImageView img;
    BottomSheetDialog bottomSheetDialog;
    Uri path;
    String Image_path = "";
    Button update;
    Database db;
    Users users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = new Database(getActivity());
        return inflater.inflate(R.layout.usersetting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("Profile");

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 8);
        }

        namelayout = view.findViewById(R.id.inputlayout2);
        citylayout = view.findViewById(R.id.inputlayout4);
        emaillayout = view.findViewById(R.id.inputlayout5);
        mobilelayout = view.findViewById(R.id.inputlayout3);
        addresslayout = view.findViewById(R.id.inputlayout);
        nametext = view.findViewById(R.id.filled_exposed_dropdown2);
        citytext = view.findViewById(R.id.filled_exposed_dropdown4);
        emailtext = view.findViewById(R.id.filled_exposed_dropdown5);
        mobiletext = view.findViewById(R.id.filled_exposed_dropdown3);
        addresstext = view.findViewById(R.id.filled_exposed_dropdown);
        update = view.findViewById(R.id.button4);
        img = view.findViewById(R.id.profile_img);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        View v = getLayoutInflater().inflate(R.layout.bottom_sheat, null);
        bottomSheetDialog.setContentView(v);
        ImageView cam = v.findViewById(R.id.imageView);
        ImageView gal = v.findViewById(R.id.imageView4);
        TextView cancel = v.findViewById(R.id.cancel);

        if (db.getAllusers().size() > 0) {
            try {
                users = db.getAllusers().get(0);
                Picasso.get().load(users.getImage()).placeholder(R.drawable.pick).into(img);
                nametext.setText(users.getName());
                mobiletext.setText(users.getTel());
                citytext.setText(users.getCity());
                addresstext.setText(users.getAdress());
                emailtext.setText(users.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (db.getAllusers().get(0).getGo_id() != null || db.getAllusers().get(0).getFb_id() != null) {
            img.setClickable(false);
            img.setEnabled(false);
            emaillayout.setEnabled(false);
            emaillayout.setClickable(false);
            namelayout.setClickable(false);
            namelayout.setEnabled(false);
        }


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database db = new Database(getActivity());
                if (nametext.getText().toString().trim().isEmpty()) {
                    namelayout.setErrorEnabled(true);
                    namelayout.setError("Please enter your name");
                    return;
                } else if (mobiletext.getText().toString().trim().isEmpty()) {
                    mobilelayout.setErrorEnabled(true);
                    mobilelayout.setError("Please enter your mobile phone");
                    return;
                } else if (citytext.getText().toString().trim().isEmpty()) {
                    citylayout.setErrorEnabled(true);
                    citylayout.setError("Please enter your city");
                    return;
                } else if (addresstext.getText().toString().trim().isEmpty()) {
                    addresslayout.setErrorEnabled(true);
                    addresslayout.setError("Please enter your full address");
                    return;
                } else if (emailtext.getText().toString().trim().isEmpty()) {
                    emaillayout.setErrorEnabled(true);
                    emaillayout.setError("Please enter your email");
                    return;
                } else {

                    String image = "";
                    if (users.getImage() == null || users.getImage().equals("")) {
                        if (Image_path.isEmpty() && path != null) {
                            image = getRealPathFromURI(path);
                        } else {
                            image = Image_path;
                        }
                        if (image.isEmpty() || image == null) {
                            Toast.makeText(getActivity(), "Please Choose photo", Toast.LENGTH_LONG).show();
                            return;
                        }


                        File file1 = null;
                        try {
                            File file = new File(image);
                            file1 = new Compressor(getActivity()).compressToFile(file);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        final RequestBody img2 = RequestBody.create(MediaType.parse("image/*"), file1);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file1.getName(), img2);
                        String name = nametext.getText().toString().trim();
                        String email = emailtext.getText().toString().trim();
                        //String pass = db.getAllusers().get(0).getPassword();
                        String city = citytext.getText().toString().trim();
                        String address = addresstext.getText().toString().trim();
                        String tel = mobiletext.getText().toString().trim();
                        String _id = db.getAllusers().get(0).get_id();

                        RequestBody nam = RequestBody.create(MediaType.parse("text/plain"), name);
                        RequestBody emai = RequestBody.create(MediaType.parse("text/plain"), email);
                        RequestBody cit = RequestBody.create(MediaType.parse("text/plain"), city);
                        RequestBody adress = RequestBody.create(MediaType.parse("text/plain"), address);
                        RequestBody te = RequestBody.create(MediaType.parse("text/plain"), tel);
                        RetrofitClient.getInstance().UpdateUser(users.getToken(), _id, nam, te, adress, cit, emai, part, false, false, null, null).enqueue(new Callback<Users>() {
                            @Override
                            public void onResponse(Call<Users> call, Response<Users> response) {
                                if (response.isSuccessful()) {
                                    db.Delete_All("Users");
                                    if (response.body().getToken() != null) {
                                        db.insert_user(response.body().getUser(), response.body().getToken());
                                    } else {
                                        db.insert_user(response.body(), null);
                                    }
                                    db.insert_user(response.body(), null);
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Update Profile")
                                            .setContentText("Profile Updated successfully")
                                            .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();
                                                    getActivity().getSupportFragmentManager().beginTransaction()
                                                            .replace(R.id.cotainers, new HomeFragment()).commit();

                                                }
                                            })
                                            .show();
                                } else {
                                    // Toast.makeText(getActivity(),String.valueOf(response.code()),Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Users> call, Throwable t) {
                                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {

                        image = users.getImage();
                        String name = nametext.getText().toString().trim();
                        String email = emailtext.getText().toString().trim();
                        //String pass = db.getAllusers().get(0).getPassword();
                        String city = citytext.getText().toString().trim();
                        String address = addresstext.getText().toString().trim();
                        String tel = mobiletext.getText().toString().trim();
                        Users u = db.getAllusers().get(0);
                        String _id = u.get_id();
                        Toast.makeText(getActivity(), u.getAdmin().toString(), Toast.LENGTH_LONG).show();
                        Users users = new Users(name, tel, address, image, email, u.getPassword(), city, u.getFbid(), u.getGoid(), u.getAdmin(), u.getSuperAdmin(), _id);
                        RetrofitClient.getInstance().UpdateUser2(users.getToken(), _id, users).enqueue(new Callback<Users>() {
                            @Override
                            public void onResponse(Call<Users> call, Response<Users> response) {
                                if (response.isSuccessful()) {
                                    //Toast.makeText(getActivity(),String.valueOf(response.code()),Toast.LENGTH_LONG).show();
                                    db.Delete_All("Users");
                                    if (response.body().getToken() != null) {
                                        db.insert_user(response.body().getUser(), response.body().getToken());
                                    } else {
                                        db.insert_user(response.body().getUser(), null);
                                    }
                                    //db.insert_user(users,null);
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Update Profile")
                                            .setContentText("Profile Updated successfully")
                                            .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();
                                                    getActivity().getSupportFragmentManager().beginTransaction()
                                                            .replace(R.id.cotainers, new HomeFragment()).commit();

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
                            public void onFailure(Call<Users> call, Throwable t) {
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
                        startActivityForResult(Intent.createChooser(cameraIntent, "Select Camera"), 3);
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
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                bottomSheetDialog.dismiss();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == getActivity().RESULT_OK && data != null) {
            path = data.getData();
            img.setImageURI(path);
        } else if (requestCode == 3 && resultCode == getActivity().RESULT_OK) {
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.updatepassword,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.updatepass){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.cotainers,new ChangePassword()).addToBackStack(null).commit();
        }
        return super.onOptionsItemSelected(item);
    }
}
