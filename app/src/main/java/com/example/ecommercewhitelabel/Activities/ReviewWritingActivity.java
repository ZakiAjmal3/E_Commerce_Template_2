package com.example.ecommercewhitelabel.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.ecommercewhitelabel.Adapter.SingleProductPageAdapters.ReviewImagesListAdapter;
import com.example.ecommercewhitelabel.R;
import com.example.ecommercewhitelabel.Utils.Constant;
import com.example.ecommercewhitelabel.Utils.MultipartRequest;
import com.example.ecommercewhitelabel.Utils.MySingleton;
import com.example.ecommercewhitelabel.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewWritingActivity extends AppCompatActivity {
    ImageView backBtn;
    CardView uploadImage;
    Button submitBtn;
    RatingBar ratingBar;
    EditText edtHeading, edtReviewLong;
    TextView allReviewCountTxt;
    private RecyclerView imagesRV;
    private ReviewImagesListAdapter imageAdapter;
    SessionManager sessionManager;
    String authToken;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    String ratingStr = "",headingStr,reviewLongStr;
    String reviewURL = Constant.BASE_URL + "review";
    String product_id = "";
    private ArrayList<File> imageFiles = new ArrayList<>();
    Dialog progressDialog;
    private ActivityResultLauncher<String[]> permissionsLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
                Boolean cameraPermissionGranted = result.get(Manifest.permission.CAMERA);
                Boolean readMediaImagesPermissionGranted = result.get(Manifest.permission.READ_MEDIA_IMAGES);
                Boolean readExternalStoragePermissionGranted = result.get(Manifest.permission.READ_EXTERNAL_STORAGE);

                if (cameraPermissionGranted != null && readMediaImagesPermissionGranted != null
                        && cameraPermissionGranted && (readMediaImagesPermissionGranted || readExternalStoragePermissionGranted)) {
                    openGallery(); // Permissions granted, open gallery or camera
                } else {
                    Toast.makeText(ReviewWritingActivity.this, "Permissions required", Toast.LENGTH_SHORT).show();
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_writing);

        sessionManager = new SessionManager(ReviewWritingActivity.this);
        authToken = sessionManager.getUserData().get("authToken");

        imagesRV = findViewById(R.id.selectedImagesRV);
        backBtn = findViewById(R.id.backBtn);
        uploadImage = findViewById(R.id.productImgCardView);
        ratingBar = findViewById(R.id.ratingBar);
        edtHeading = findViewById(R.id.edtHeading);
        edtReviewLong = findViewById(R.id.edtReviewLong);
        submitBtn = findViewById(R.id.submitBtn);

        setupActivityResultLaunchers();

        imagesRV.setLayoutManager(new LinearLayoutManager(ReviewWritingActivity.this, LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new ReviewImagesListAdapter( ReviewWritingActivity.this,imageFiles,null);
        imagesRV.setAdapter(imageAdapter);

        product_id = getIntent().getStringExtra("productId");

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openGallery();
                checkPermissionsAndOpenGalleryOrCamera();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingStr = String.valueOf(ratingBar.getRating());
                headingStr = edtHeading.getText().toString();
                reviewLongStr = edtReviewLong.getText().toString();
                progressDialog = new Dialog(ReviewWritingActivity.this);
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.setContentView(R.layout.progress_bar_dialog);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.getWindow().setGravity(Gravity.CENTER); // Center the dialog
                progressDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT); // Adjust the size
                progressDialog.show();
                checkFields();
            }
        });
    }
    private void checkFields() {
        if (ratingStr.isEmpty()) {
            Toast.makeText(ReviewWritingActivity.this, "Please Select Rating", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        if (headingStr.isEmpty()) {
            edtHeading.setError("Please provide a heading");
            progressDialog.dismiss();
            return;
        }
        if (reviewLongStr.isEmpty()) {
            edtReviewLong.setError("Please write something");
            progressDialog.dismiss();
            return;
        }
        if (imageFiles.isEmpty()) {
            Toast.makeText(ReviewWritingActivity.this, "Please Select an Image", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        createReview();
    }
    public void createReview() {
        // Prepare form data
        Map<String, String> params = new HashMap<>();
        params.put("rating", ratingStr);
        params.put("productId", product_id);
        params.put("storeId", sessionManager.getStoreId());
        params.put("headline", headingStr);
        params.put("review",reviewLongStr);
        // Create a Map for files
        Map<String, File> files = new HashMap<>();
        for (int i = 0; i < imageFiles.size(); i++) {
            files.put("images" + i, imageFiles.get(i)); // Add each image to the request map
        }
        // Create and send the multipart request
        MultipartRequest multipartRequest = new MultipartRequest(reviewURL, params, files,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            boolean status = responseObject.getBoolean("success");
                            if (status) {
                                String message = responseObject.getString("message");
                                Toast.makeText(ReviewWritingActivity.this, message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                onBackPressed();
                            }
                        } catch (JSONException e) {
                            Log.e("JSON_ERROR", "Error parsing JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        String errorMessage = "Error: " + error.toString();
                        if (error.networkResponse != null) {
                            try {
                                String responseData = new String(error.networkResponse.data, "UTF-8");
                                errorMessage += "\nStatus Code: " + error.networkResponse.statusCode;
                                errorMessage += "\nResponse Data: " + responseData;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(ReviewWritingActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        Log.e("BlogFetchError", errorMessage);
                    }
                }, authToken);

        // Add the request to the queue
        MySingleton.getInstance(this).addToRequestQueue(multipartRequest);
    }

    private void setupActivityResultLaunchers() {
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    // Check if multiple images are selected
                    if (data.getClipData() != null) {
                        // Multiple images selected
                        ClipData clipData = data.getClipData();
                        ArrayList<Uri> imageUris = new ArrayList<>();
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            imageUris.add(clipData.getItemAt(i).getUri());
                        }
                        handleMultipleImages(imageUris);
                    } else {
                        // Single image selected
                        Uri imageUri = data.getData();
                        handleImageUri(imageUri);
                    }
                }
            }
        });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                if (bitmap != null) {
                    handleBitmap(bitmap);
                }
            }
        });
    }
    private void handleMultipleImages(ArrayList<Uri> imageUris) {
        // Clear the current image files list if necessary
        imageFiles.clear();  // Clear any previously selected images

        for (Uri uri : imageUris) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                bitmap = getResizedBitmap(bitmap, 400);  // Resize image if needed

                // Convert Bitmap to File and add to the list
                File imageFile = bitmapToFile(bitmap);
                imageFiles.add(imageFile);

                // Log the file path for each image
                Log.d("ImageFiles", "Image added: " + imageFile.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Log the list size to ensure multiple images are added
        Log.d("ImageFiles", "Number of images selected: " + imageFiles.size());

        // Now that all images are processed, notify the adapter
        imageAdapter.notifyDataSetChanged();
    }

    private void handleImageUri(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(ReviewWritingActivity.this.getContentResolver(), uri);
            bitmap = getResizedBitmap(bitmap, 400);  // Resize image if needed

            // Convert Bitmap to File and add to the imageFiles list
            File imageFile = bitmapToFile(bitmap);
            imageFiles.add(imageFile);

            // Notify adapter to update RecyclerView
            imageAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void handleBitmap(Bitmap bitmap) {
        // Resize image
        bitmap = getResizedBitmap(bitmap, 400);

        // Convert Bitmap to File and add to the imageFiles list
        File imageFile = bitmapToFile(bitmap);
        imageFiles.add(imageFile);

        // Notify adapter that new image has been added
        imageAdapter.notifyDataSetChanged();
    }
    private File bitmapToFile(Bitmap bitmap) {
        // Generate a unique filename based on the current time
        String fileName = "uploaded_image_" + System.currentTimeMillis() + ".jpg";
        File file = new File(ReviewWritingActivity.this.getCacheDir(), fileName);

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private void openGallery() {
        final CharSequence[] options = {"Open Camera", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ReviewWritingActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Open Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraLauncher.launch(intent); // Use cameraLauncher
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    galleryLauncher.launch(Intent.createChooser(intent, "Select Image")); // Use galleryLauncher
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                    Toast.makeText(ReviewWritingActivity.this, "Image Uploading Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    private void checkPermissionsAndOpenGalleryOrCamera() {
        // Check if we have both permissions: Camera and Storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsLauncher.launch(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES
            });
        } else {
            permissionsLauncher.launch(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            });
        }
    }
}