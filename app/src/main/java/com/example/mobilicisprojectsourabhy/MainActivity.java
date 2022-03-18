package com.example.mobilicisprojectsourabhy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    LocationManager locationManager;
    LocationListener locationListener;
    SensorManager sensorManager;
    Sensor pressureSensor, lightSensor, acceleroSensor, gyroSensor;
    SensorEventListener sensorEventListener;
    TextView tvLat, tvLong, tvPress, tvIP, tvLight, tvAccelero, tvGyro, tvDown, tvUp, tvPower;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locationListener);
            }
            else{
                tvLat.setText("Enable");
                tvLong.setText("Enable");
            }
        }

    }

    public void updateLocationInfo(Location location) {
        tvLat.setText("Latitude : " + location.getLatitude());
        tvLong.setText("Longitude : " + location.getLongitude());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLat = findViewById(R.id.tvLat);
        tvLong = findViewById(R.id.tvLong);
        tvPress = findViewById(R.id.tvPress);
        tvIP = findViewById(R.id.tvIP);
        tvUp = findViewById(R.id.tvUp);
        tvDown = findViewById(R.id.tvDown);
        tvPower = findViewById(R.id.tvPower);
        tvLight = findViewById(R.id.tvLight);
        tvAccelero = findViewById(R.id.tvAccelero);
        tvGyro = findViewById(R.id.tvGyro);

        GetIpAddress();

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Retrieving Data");
        progressDialog.show();
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);


        if (sensorManager != null) {
            pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            acceleroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

            if (lightSensor != null) {
                sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
            if (pressureSensor != null) {
                sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_UI);
            }
            if (acceleroSensor != null) {
                sensorManager.registerListener(this, acceleroSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
            if (gyroSensor != null) {
                sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
                progressDialog.dismiss();
            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locationListener);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locationListener);
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastLocation != null) {
                    updateLocationInfo(lastLocation);
                }
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            tvDown.setText("Volume Down Working : True");
            return true;
        } else return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            tvUp.setText("Volume Up Working : True");
            return true;
        } else return false;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_POWER) {
            tvPower.setText("Power Button Working : True");
            return true;
        } else return false;
    }


    private void GetIpAddress() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ipAddress = String.valueOf(wifiManager.getConnectionInfo().getIpAddress());
        try {
            if (ipAddress != null) {
                tvIP.setText("Ip Address : " + ipAddress);
            } else
                tvIP.setText("Connect to Wi-Fi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            tvLight.setText("Ambient Light Sensor : True");
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            tvAccelero.setText("Accelerometer: \nX: " + event.values[0] + "\nY: " + event.values[1] + "\nZ: " + event.values[2]);
        }

        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            Toast.makeText(this, "working", Toast.LENGTH_SHORT).show();
            tvPress.setText(event.values[0] + "");
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            tvGyro.setText("Gyroscope: \nX: " + event.values[0] + "\nY: " + event.values[1] + "\nZ: " + event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}