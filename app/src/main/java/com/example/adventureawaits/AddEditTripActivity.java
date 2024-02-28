package com.example.adventureawaits;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SeekBar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEditTripActivity extends AppCompatActivity {
    private Button saveButton;
    private Button galleryButton;
    private EditText tripNameEditText;
    private EditText destinationEditText;
    private RadioButton cityBreakRadioButton;
    private RadioButton seaSideRadioButton;
    private RadioButton mountainsRadioButton;
    private SeekBar priceSeekBar;
    private RatingBar ratingRatingBar;
    private EditText startDateEditText;
    private EditText endDateEditText;
    private boolean isEditActivity = false;
    private int idTrip;
    private ActivityResultLauncher<String> galleryLauncher;

    private byte[] photoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_edit_trip);
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    photoData = getPhotoFromUri(result);
                }
            }
        });
        saveButton = findViewById(R.id.buttonSave);
        tripNameEditText = findViewById(R.id.editTextTripName);
        destinationEditText = findViewById(R.id.editTextDestination);
        cityBreakRadioButton = findViewById(R.id.radioButtonCityBreak);
        seaSideRadioButton = findViewById(R.id.radioButtonSeaSide);
        mountainsRadioButton = findViewById(R.id.radioButtonMountains);
        priceSeekBar = findViewById(R.id.seekBarPrice);
        ratingRatingBar = findViewById(R.id.ratingBar);
        startDateEditText = findViewById(R.id.editTextStartDate);
        endDateEditText = findViewById(R.id.editTextEndDate);
        galleryButton = findViewById(R.id.buttonGalleryPhoto);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryLauncher.launch("image/*");
            }
        });
        Intent intent = getIntent();
        if (intent.hasExtra("ID")) {
            idTrip = intent.getIntExtra("ID", -1);
            isEditActivity = true;
            MyDatabase tripsDatabase = MyDatabase.getTripDatabase(AddEditTripActivity.this);
            TripDao tripDao = tripsDatabase.tripDao();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    TripEntity tripEntity = tripDao.findTripById(intent.getIntExtra("ID", -1));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (tripEntity.getName() != null)
                                tripNameEditText.setText(tripEntity.getName());
                            if (tripEntity.getDestination() != null)
                                destinationEditText.setText(tripEntity.getDestination());
                            if (tripEntity.getType() != null) {
                                if (tripEntity.getType().matches("City Break"))
                                    cityBreakRadioButton.setChecked(true);
                                else if (tripEntity.getType().matches("Sea Side"))
                                    seaSideRadioButton.setChecked(true);
                                else if (tripEntity.getType().matches("Mountains"))
                                    mountainsRadioButton.setChecked(true);
                            }
                            if (tripEntity.getPrice() != null)
                                priceSeekBar.setProgress(tripEntity.getPrice());
                            if (tripEntity.getRating() != null)
                                ratingRatingBar.setRating(tripEntity.getRating());
                            if (tripEntity.getStartDate() != null) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                startDateEditText.setText(simpleDateFormat.format(tripEntity.getStartDate()));
                            }
                            if (tripEntity.getEndDate() != null) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                endDateEditText.setText(simpleDateFormat.format(tripEntity.getEndDate()));
                            }
                            if(tripEntity.getPhotoData() != null)
                                photoData = tripEntity.getPhotoData();
                        }
                    });
                }
            }).start();
        }
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEditTripActivity.this, R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month += 1;
                        String date = day + "/" + month + "/" + year;
                        startDateEditText.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEditTripActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month += 1;
                        String date = day + "/" + month + "/" + year;
                        endDateEditText.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tripNameEditText.getText().toString();
                String destination = destinationEditText.getText().toString();
                String type;
                if (cityBreakRadioButton.isChecked())
                    type = "City Break";
                else if (seaSideRadioButton.isChecked())
                    type = "Sea Side";
                else
                    type = "Mountains";
                Integer price = priceSeekBar.getProgress();
                Float rating = ratingRatingBar.getRating();
                String startDateString = startDateEditText.getText().toString();
                String endDateString = endDateEditText.getText().toString();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date startDate;
                Date endDate;
                try {
                    startDate = format.parse(startDateString);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                try {
                    endDate = format.parse(endDateString);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                MyDatabase tripDatabase = MyDatabase.getTripDatabase(AddEditTripActivity.this);
                TripDao tripDao = tripDatabase.tripDao();
                TripEntity tripEntity = new TripEntity();
                tripEntity.setName(name);
                tripEntity.setDestination(destination);
                tripEntity.setType(type);
                tripEntity.setPrice(price);
                tripEntity.setStartDate(startDate);
                tripEntity.setEndDate(endDate);
                tripEntity.setRating(rating);
                tripEntity.setPhotoData(photoData);
                tripEntity.setFavourite(false);

                if (isEditActivity == false) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            TripEntity sameNameTripEntity = tripDao.findTripByName(name);
                            if (sameNameTripEntity != null)
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tripNameEditText.setError("This trip name already exists");
                                    }
                                });
                            else new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    tripDao.insertTrip(tripEntity);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(AddEditTripActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }).start();
                        }
                    }).start();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            TripEntity sameNameTripEntity = tripDao.findTripByName(name);
                            if (sameNameTripEntity != null && sameNameTripEntity.getId() != idTrip)
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tripNameEditText.setError("This trip name already exists");
                                    }
                                });
                            else new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    tripEntity.setId(idTrip);
                                    tripDao.updateTrip(tripEntity);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(AddEditTripActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }).start();
                        }
                    }).start();
                }
            }
        });
    }

    private byte[] getPhotoFromUri(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] photoData = getBytesFromInputStream(inputStream);
            return photoData;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}