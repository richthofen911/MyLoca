package net.callofdroidy.myloc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ActivityMain extends AppCompatActivity {
    private static final String TAG = "ActivityMain";

    MockLocationProvider mock;
    LocationManager locMgr;
    LocationListener lis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mock = new MockLocationProvider(LocationManager.NETWORK_PROVIDER, this);

        //Set test location
        mock.pushLocation(-12.34, 23.45);

        locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        lis = new LocationListener() {
            public void onLocationChanged(Location location) {
                //You will get the mock location
                Log.e(TAG, "onLocationChanged: " + location.getLatitude() + "/" + location.getLongitude());
            }

            public void onStatusChanged(String str, int number, Bundle info){

            }

            public void onProviderEnabled(String str){

            }

            public void onProviderDisabled(String str){

            }
            //...
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }else {
            locMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, lis);
        }

    }

    protected void onDestroy() {
        mock.shutdown();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        locMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, lis);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
