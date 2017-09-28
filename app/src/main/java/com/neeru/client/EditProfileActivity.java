package com.neeru.client;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;

import com.dd.CircularProgressButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.neeru.client.adapter.ArrayAdapterWithIcon;
import com.neeru.client.models.User;
import com.neeru.client.network.ApiStatus;
import com.neeru.client.network.NetworkHelper;
import com.neeru.client.network.PostListener;
import com.neeru.client.prefs.AuthPreference;
import com.neeru.client.util.DialogHelper;
import com.neeru.client.util.ImageUtil;
import com.neeru.client.util.Util;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Request;
import retrofit2.Response;


public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener, PostListener {

    public static String INTENT_EXTRA_FROM_REGISTER = "intent_register";
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public int AVATAR_WIDTH = 300, AVATAR_HEIGHT = 300;
    public static final int PERMISSION_REQUEST_CAMERA = 2;
    private CircleImageView ivIcon;
    private TextInputLayout inputFName;
    private EditText etFname;
    private TextInputLayout inputLName;
    private TextInputLayout inputEmail;
    private EditText etLname;
    private EditText etEmail;
    private CircularProgressButton btnSave;
    private NetworkHelper networkHelper;
    private DialogHelper dialogHelper;
    private View mRoot;
    boolean isFromRegister = false;
    private FirebaseStorage storage;

    private File mCurrentPhotoPath;


    User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        isFromRegister = getIntent().getBooleanExtra(INTENT_EXTRA_FROM_REGISTER, false);


        mUser = new AuthPreference(getApplicationContext()).getUser();
        storage = FirebaseStorage.getInstance();

        if (!isFromRegister) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        mRoot = findViewById(R.id.root_view);
        ivIcon = (CircleImageView) findViewById(R.id.imageview);
        inputFName = (TextInputLayout) findViewById(R.id.input_layout_fname);
        inputLName = (TextInputLayout) findViewById(R.id.input_layout_lname);
        inputEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        etFname = (EditText) findViewById(R.id.input_fname);
        etLname = (EditText) findViewById(R.id.input_lname);
        etEmail = (EditText) findViewById(R.id.input_email);
        btnSave = (CircularProgressButton) findViewById(R.id.button_save);
        btnSave.setOnClickListener(this);

        etFname.setText(mUser.firstName == null ? "" : mUser.firstName);
        etLname.setText(mUser.lastName == null ? "" : mUser.lastName);
        etEmail.setText(mUser.email == null ? "" : mUser.email);


        if (mUser.avatar != null) {
            Picasso.with(getApplicationContext()).load(mUser.avatar).placeholder(R.drawable.ic_avatar_default).error(R.drawable.ic_avatar_default).into(ivIcon);
        }


        networkHelper = new NetworkHelper(this);
        dialogHelper = new DialogHelper();
        ivIcon.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_save:
                attemptRegister();
                break;
            case R.id.imageview:

                boolean needsRead = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED;

                boolean needsWrite = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED;
                boolean needsCamera = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED;
                String[] PERMISSIONS_STORAGE = {
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA
                };


                if (needsRead || needsWrite || needsCamera) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(
                            this,
                            PERMISSIONS_STORAGE,
                            PERMISSION_REQUEST_CAMERA
                    );

                } else {
                    selectImage();
                }


                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        boolean needsRead = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;

        boolean needsWrite = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;

        boolean needsCamera = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED;

        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA:
                if (!needsRead && !needsWrite && !needsCamera) {
                    selectImage();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(mRoot, "Camera/Read Permission Required for accessing images. Go to Settings->App->Arkraiders to enable permission", Snackbar.LENGTH_LONG)
                            .setAction("GO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startInstalledAppDetailsActivity(EditProfileActivity.this);
                                }
                            });

                    snackbar.show();
                }

                break;
        }

    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_skip, menu);

        if (isFromRegister) {
            menu.findItem(R.id.action_skip).setVisible(true);
        } else {
            menu.findItem(R.id.action_skip).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
        } else if (id == R.id.action_skip) {
            Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


    void attemptRegister() {

        inputFName.setError(null);
        inputLName.setError(null);
        inputEmail.setError(null);


        String firstName = etFname.getText().toString();
        String lastName = etLname.getText().toString();
        String email = etEmail.getText().toString();

        if (validate(firstName, email)) {

            User user = new User();

            user.firstName = firstName;


            if (email != null && !TextUtils.isEmpty(email)) {
                user.email = email;
            }
            user.email = email;

            if (lastName != null && !TextUtils.isEmpty(lastName)) {
                user.lastName = lastName;
            }


            networkHelper.updateUser(new AuthPreference(getApplicationContext()).getAccessTocken(), this, user);
            btnSave.setProgress(50);
        }

    }


    boolean validate(String firstName, String email) {

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(firstName)) {
            inputFName.setError(getString(R.string.error_empty_name));
            focusView = etFname;
            cancel = true;
        } else if (!isValidEmail(email)) {
            inputEmail.setError(getString(R.string.error_invalid_email));
            focusView = etEmail;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
            return false;
        } else {

            return true;
        }

    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null || TextUtils.isEmpty(target)) {
            return true;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public void onResult(Integer position, Request request, Response response, boolean isError, Throwable throwable, ApiStatus status) {

        if (status == ApiStatus.IMAGE_UPLOAD) {
            dialogHelper.hideProgressDialog();
        }

        if (isError) {
            if (status != ApiStatus.IMAGE_UPLOAD) {
                btnSave.setProgress(-1);
                dialogHelper.showSnackBar("Something went wrong. Please try after some time.", mRoot);
            } else {
                dialogHelper.showSnackBar("Error in image uploading", mRoot);
            }

        } else if (!response.isSuccessful()) {
            if (status != ApiStatus.IMAGE_UPLOAD) {
                btnSave.setProgress(-1);
                dialogHelper.showSnackBar("Unable to process this request. Please try after some time.", mRoot);
            } else {
                dialogHelper.showSnackBar("Error in image uploading", mRoot);
            }
        } else {


            User user = ((List<User>) response.body()).get(0);
            AuthPreference mAuth = new AuthPreference(getApplicationContext());
            if (status == ApiStatus.IMAGE_UPLOAD) {
                mAuth.saveAvatar(user.avatar);
                dialogHelper.showSnackBar("Image Uploaded", mRoot);
                displayImage(user.avatar);

            } else {
                btnSave.setProgress(0);
                mAuth.saveUser(user);
                dialogHelper.showSnackBar("User information saved successfully.", mRoot);
            }

        }
    }


    private void selectImage() {
        final String[] items = {"Use the Camera", "Pick from Gallery"};

        final Integer[] icons = new Integer[]{R.drawable.ic_menu_camera, R.drawable.ic_menu_gallery};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);


        AlertDialog dialog = builder.create();

        String message = "Select Profile Picture";

        builder.setTitle(message);


        dialog.setCanceledOnTouchOutside(false);


        ListAdapter adapter = new ArrayAdapterWithIcon(this, items, icons);

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Use the Camera")) {

                    dispatchTakePictureIntent();

                } else if (items[item].equals("Pick from Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                }
            }
        });

        builder.show();

    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = null;

                photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        BuildConfig.APPLICATION_ID,
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );


        mCurrentPhotoPath = image;
        return image;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                Uri uri = data.getData();

                File file = compress(Util.getFile(getApplicationContext(), uri));

                if (file != null) {
                    uploadFile(Uri.fromFile(file));
                } else {
                    file = Util.getFile(getApplicationContext(), uri);
                    uploadFile(uri);
                }


                dialogHelper.showProgressDialog(EditProfileActivity.this, "Uploading");

            } else if (requestCode == REQUEST_CAMERA) {
                File file = compress(mCurrentPhotoPath);
                if (file == null) {
                    file = mCurrentPhotoPath;
                }

                uploadFile(Uri.fromFile(file));

                dialogHelper.showProgressDialog(EditProfileActivity.this, "Uploading");
            }
        }

    }


    File compress(File selectedFile) {

        ImageUtil imageUtil = new ImageUtil();
        File imageFIle = imageUtil.initCompress(selectedFile, AVATAR_WIDTH, AVATAR_HEIGHT, 60, getApplicationContext());

        return imageFIle;
    }


    void uploadFile(Uri uri) {

        StorageReference storageRef = storage.getReference();
        StorageReference riversRef = storageRef.child("images/avatar/" + mUser.contact);
        UploadTask uploadTask = riversRef.putFile(uri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
                dialogHelper.showSnackBar("Error in image uploading", mRoot);

                dialogHelper.hideProgressDialog();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String mImageUrl = downloadUrl.toString();


                Log.v("URL-------->", downloadUrl.toString());


                User user = new User();
                user.avatar = mImageUrl;

                networkHelper.updateAvater(new AuthPreference(getApplicationContext()).getAccessTocken(), EditProfileActivity.this, user);

            }
        });
    }

    public void displayImage(String mImageUrl) {

        try {

            Picasso.with(getApplicationContext()).load(mImageUrl).placeholder(R.drawable.ic_avatar_default).fit().centerCrop().into(ivIcon);


        } catch (Throwable th) {
        }

    }



}
