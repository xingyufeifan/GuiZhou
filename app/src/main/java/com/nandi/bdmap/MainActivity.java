package com.nandi.bdmap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.MobileBasemapLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Item;
import com.esri.arcgisruntime.mapping.MobileMapPackage;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.portal.PortalItem;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MMPK";
    private static final String FILE_EXTENSION = ".tpk";
    private static File extStorDir;
    private static String extSDCardDirName;
    private static String filename;
    private static String mmpkFilePath;
    private MapView mMapView;
    private MobileMapPackage mapPackage;

    // define permission to request
    String[] reqPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int requestCode = 2;

    /**
     * Create the mobile map package file location and name structure
     */
    private static String createMobileMapPackageFilePath(){
        return extStorDir.getAbsolutePath() + File.separator + extSDCardDirName + File.separator + filename + FILE_EXTENSION;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=  new Intent(this,BNMainActivity.class);
        startActivity(intent);
        // get sdcard resource name
        extStorDir = Environment.getExternalStorageDirectory();
        // get the directory
        extSDCardDirName = this.getResources().getString(R.string.config_data_sdcard_offline_dir);
        // get mobile map package filename
        filename = this.getResources().getString(R.string.config_mmpk_name);
        // create the full path to the mobile map package file
        mmpkFilePath = createMobileMapPackageFilePath();

        // retrieve the MapView from layout
        mMapView = (MapView) findViewById(R.id.mapview);

        // For API level 23+ request permission at runtime
        if(ContextCompat.checkSelfPermission(MainActivity.this, reqPermission[0]) == PackageManager.PERMISSION_GRANTED){
            loadMobileMapPackage(mmpkFilePath);
        }else{
            // request permission
            ActivityCompat.requestPermissions(MainActivity.this, reqPermission, requestCode);
        }

    }

    /**
     * Handle the permissions request response
     *
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            loadMobileMapPackage(mmpkFilePath);
        }else{
            // report to user that permission was denied
            Toast.makeText(MainActivity.this, getResources().getString(R.string.location_permission_denied),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Load a mobile map package into a MapView
     *
     * @param mmpkFile Full path to mmpk file
     */
    private void loadMobileMapPackage(String mmpkFile){
        TileCache tileCache=new TileCache(mmpkFile);
        ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(tileCache);
        ArcGISMap arcGISMap = new ArcGISMap();
        arcGISMap.getOperationalLayers().add(tiledLayer);
        mMapView.setMap(arcGISMap);
        arcGISMap.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                Point p = new Point(-117.162040, 32.718260, SpatialReference.create(4326));
                Viewpoint vp = new Viewpoint(p, 10000);
                mMapView.setViewpointAsync(vp, 3);
            }
        });

    }

    @Override
    protected void onPause(){
        super.onPause();
        mMapView.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mMapView.resume();
    }
}
