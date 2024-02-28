package com.example.adventureawaits.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventureawaits.MyDatabase;
import com.example.adventureawaits.R;
import com.example.adventureawaits.TripDao;
import com.example.adventureawaits.TripEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ReadOnlyActivity extends AppCompatActivity {
    private TextView tripNameTextView;
    private TextView tripLocationTextView;
    private TextView startDayTextView;
    private TextView endDayTextView;
    private TextView startMonthYearTextView;
    private TextView endMonthYearTextView;
    private RadioButton cityBreakRadioButton;
    private RadioButton seaSideRadioButton;
    private RadioButton mountainsRadioButton;
    private RatingBar tripRatingBar;
    private SeekBar tripPriceSeekBar;

    private ImageView tripImageView;

    private int idTrip;

    private TextView temperatureTextView;
    private TextView minMaxTemperatureTextView;
    private TextView windSpeedTextView;
    private TextView humidityPercentageTextView;
    private TextView sunriseTextView;
    private TextView sunsetTextView;
    private TextView weatherDescriptionTextView;
    private ImageView weatherImageView;
    private OpenWeatherApiRepository openWeatherApiRepository;

    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_read_only);

        tripNameTextView = findViewById(R.id.textViewTripName);
        tripLocationTextView = findViewById(R.id.textViewTripDestination);
        startDayTextView = findViewById(R.id.textViewStartDay);
        endDayTextView = findViewById(R.id.textViewEndDay);
        startMonthYearTextView = findViewById(R.id.textViewStartMonthYear);
        endMonthYearTextView = findViewById(R.id.textViewEndMonthYear);
        cityBreakRadioButton = findViewById(R.id.radioButtonCityBreak);
        seaSideRadioButton = findViewById(R.id.radioButtonSeaSide);
        mountainsRadioButton = findViewById(R.id.radioButtonMountains);
        tripRatingBar = findViewById(R.id.ratingBar);
        tripPriceSeekBar = findViewById(R.id.seekBarPrice);
        tripImageView = findViewById(R.id.imageViewTripPhoto);
        temperatureTextView = findViewById(R.id.textViewTemperature);
        minMaxTemperatureTextView = findViewById(R.id.textViewMinMaxTemperature);
        windSpeedTextView = findViewById(R.id.textViewWindSpeed);
        humidityPercentageTextView = findViewById(R.id.textViewHumidity);
        sunriseTextView = findViewById(R.id.textViewSunrise);
        sunsetTextView = findViewById(R.id.textViewSunset);
        weatherDescriptionTextView = findViewById(R.id.textViewDescription);
        weatherImageView = findViewById(R.id.imageViewWeather);

        tripPriceSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        tripPriceSeekBar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        cityBreakRadioButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        seaSideRadioButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        mountainsRadioButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("ID")) {
            Log.e("ID", "Intent has extra id");
            idTrip = intent.getIntExtra("ID", -1);
            MyDatabase tripDatabase = MyDatabase.getTripDatabase(ReadOnlyActivity.this);
            TripDao tripDao = tripDatabase.tripDao();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    TripEntity tripEntity = tripDao.findTripById(intent.getIntExtra("ID", -1));
                    city = tripEntity.getDestination().toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (tripEntity.getName() != null)
                                tripNameTextView.setText(tripEntity.getName());
                            if (tripEntity.getDestination() != null)
                                tripLocationTextView.setText(tripEntity.getDestination());
                            if (tripEntity.getStartDate() != null) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String[] date = simpleDateFormat.format(tripEntity.getStartDate()).split("/");
                                startDayTextView.setText(date[0]);
                                startMonthYearTextView.setText(date[1] + " " + date[2]);
                            }
                            if (tripEntity.getEndDate() != null) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String[] date = simpleDateFormat.format(tripEntity.getEndDate()).split("/");
                                endDayTextView.setText(date[0]);
                                endMonthYearTextView.setText(date[1] + " " + date[2]);
                            }
                            if (tripEntity.getType() != null) {
                                if (tripEntity.getType().matches("City Break"))
                                    cityBreakRadioButton.setChecked(true);
                                else if (tripEntity.getType().matches("Sea Side"))
                                    seaSideRadioButton.setChecked(true);
                                else if (tripEntity.getType().matches("Mountains"))
                                    mountainsRadioButton.setChecked(true);
                            }
                            if (tripEntity.getRating() != null)
                                tripRatingBar.setRating(tripEntity.getRating());
                            if (tripEntity.getPrice() != null)
                                tripPriceSeekBar.setProgress(tripEntity.getPrice());
                            if (tripEntity.getPhotoData() != null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(tripEntity.getPhotoData(), 0, tripEntity.getPhotoData().length);
                                tripImageView.setImageBitmap(bitmap);
                            }       //        String city = tripLocationTextView.getText().toString();
                            String apiId = "dc6194ad6006b60aa02600597f69b3c9";
                            if (city == null || city.isEmpty()) {
                                Log.e("NO CITY", "City is null");
                                city = tripLocationTextView.getText().toString();
                                Log.e("CITY NAME", city);
                            }

                            openWeatherApiRepository = OpenWeatherApiRepository.getInstance();
                            openWeatherApiRepository.getWeather(new OnGetWeatherCallback() {
                                @Override
                                public void onSuccess(OpenWeatherApiResponse weather) {
                                    Log.e("Weather=", weather.toString());
                                    Log.e("CITY FROM REQUEST", city);
                                    if (weather.getWeather().get(0).getShortDescription().matches("Clear")) {
                                        if (weather.getWeather().get(0).getIcon().substring(2).matches("d"))
                                            weatherImageView.setImageResource(R.drawable.clear_day);
                                        else
                                            weatherImageView.setImageResource(R.drawable.clear_night);
                                    } else if (weather.getWeather().get(0).getShortDescription().matches("Clouds")){
                                        if (weather.getWeather().get(0).getIcon().substring(2).matches("d"))
                                            weatherImageView.setImageResource(R.drawable.cloudy_day);
                                        else
                                            weatherImageView.setImageResource(R.drawable.cloudy_night);
                                    }

                                    else if (weather.getWeather().get(0).getShortDescription().matches("Mist") ||
                                            weather.getWeather().get(0).getShortDescription().matches("Smoke") ||
                                            weather.getWeather().get(0).getShortDescription().matches("Haze") ||
                                            weather.getWeather().get(0).getShortDescription().matches("Dust") ||
                                            weather.getWeather().get(0).getShortDescription().matches("Fog") ||
                                            weather.getWeather().get(0).getShortDescription().matches("Sand") ||
                                            weather.getWeather().get(0).getShortDescription().matches("Dust") ||
                                            weather.getWeather().get(0).getShortDescription().matches("Ash") ||
                                            weather.getWeather().get(0).getShortDescription().matches("Squall") ||
                                            weather.getWeather().get(0).getShortDescription().matches("Tornado"))
                                        weatherImageView.setImageResource(R.drawable.atmosphere);
                                    else if (weather.getWeather().get(0).getShortDescription().matches("Snow") ||
                                            weather.getWeather().get(0).getLongDescription().matches("freezing rain"))
                                        weatherImageView.setImageResource(R.drawable.snow);
                                    else if (weather.getWeather().get(0).getShortDescription().matches("Rain") ||
                                            weather.getWeather().get(0).getShortDescription().matches("Drizzle"))
                                        weatherImageView.setImageResource(R.drawable.rain);
                                    else if (weather.getWeather().get(0).getShortDescription().matches("Thunderstorm"))
                                        weatherImageView.setImageResource(R.drawable.storm);
                                    double kelvin = weather.getMain().getTemperature();
                                    double celsius = kelvinToCelsius(kelvin);
                                    celsius = (int) Math.round(celsius);
                                    temperatureTextView.setText(Integer.toString((int) celsius) + "°C");
                                    weatherDescriptionTextView.setText(weather.getWeather().get(0).getShortDescription());
                                    double kelvinMin = weather.getMain().getMinTemperature();
                                    double celsiusMin = kelvinToCelsius(kelvinMin);
                                    celsiusMin = (int) Math.round(celsiusMin);
                                    double kelvinMax = weather.getMain().getMaxTemperature();
                                    double celsiusMax = kelvinToCelsius(kelvinMax);
                                    celsiusMax = (int) Math.round(celsiusMax);
                                    minMaxTemperatureTextView.setText(Integer.toString((int) celsiusMin) + "°C/" + Integer.toString((int) celsiusMax) + "°C");
                                    int windSpeed = (int) Math.round(weather.getWind().getWindSpeed() * 3.6);
                                    windSpeedTextView.setText(Integer.toString(windSpeed) + "km/h");
                                    humidityPercentageTextView.setText(weather.getMain().getHumidity() + "%");

                                    long unixTimestampSunrise = weather.getSys().getSunrise();
                                    Log.e("TIME ZONE", String.valueOf(TimeZone.getDefault()));
                                    Date sunriseDate = new Date((unixTimestampSunrise + weather.getTimezone() - 10800) * 1000);
//                Date sunriseDate = new Date((unixTimestampSunrise) * 1000);
                                    Log.e("TIMEZONE SUNRISE", weather.getTimezone().toString());
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
                                    String formattedDateTimeSunrise = dateFormat.format(sunriseDate);
                                    Log.e("SUNRISE", formattedDateTimeSunrise);
                                    String[] splitDateTimeSunrise = formattedDateTimeSunrise.split(" ");
                                    String timePartSunrise = splitDateTimeSunrise[1];
                                    String[] splitTimeSunrise = timePartSunrise.split(":");
                                    sunriseTextView.setText(splitTimeSunrise[0] + ":" + splitTimeSunrise[1]);

                                    long unixTimestampSunset = weather.getSys().getSunset();
                                    Date sunsetDate = new Date((unixTimestampSunset + weather.getTimezone() - 10800) * 1000);
                                    Log.e("TIMEZONE SUNSET", weather.getTimezone().toString());
                                    String formattedDateTimeSunset = dateFormat.format(sunsetDate);
                                    Log.e("SUNSET", formattedDateTimeSunset);
                                    String[] splitDateTimeSunset = formattedDateTimeSunset.split(" ");
                                    String timePartSunset = splitDateTimeSunset[1];
                                    String[] splitTimeSunset = timePartSunset.split(":");
                                    sunsetTextView.setText(splitTimeSunset[0] + ":" + splitTimeSunset[1]);
                                }

                                @Override
                                public void onError() {
                                    Toast.makeText(ReadOnlyActivity.this, "An error has occured. Please try again later", Toast.LENGTH_LONG).show();
                                }
                            }, city, apiId);
                        }
                    });
                }
            }).start();

        }
    }

    private double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }
}