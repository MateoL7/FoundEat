package com.example.foundeat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.foundeat.R;

public class SplashScreen extends AppCompatActivity {

    public final static int PERMISSION_CALLBACK=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        requestPermissions(new String[]{
                Manifest.permission.CAMERA
                ,Manifest.permission.READ_EXTERNAL_STORAGE

        }, PERMISSION_CALLBACK);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode== PERMISSION_CALLBACK){
            boolean allGranted = true;
            for (int r : grantResults){
                if(r== PackageManager.PERMISSION_DENIED){
                    allGranted=false;
                    break;
                }
            }
            if(!allGranted){
                Toast.makeText(this,"No todos los permisos fueron concedidos",Toast.LENGTH_LONG).show();
            }else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}