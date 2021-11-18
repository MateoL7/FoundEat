package com.example.foundeat.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foundeat.R;
import com.example.foundeat.model.Client;
import com.example.foundeat.util.UtilDomi;

import java.io.File;

public class ClientPhoto extends AppCompatActivity implements ChoiceDialog.OnChoiceListener{

    private TextView skipPicTV, greetingTV;
    private ImageView profilePic;
    private Button continueBtn;

    private Client client;

    private File file;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_photo);

        client = (Client) getIntent().getExtras().get("client");

        greetingTV = findViewById(R.id.greetingTV);

        greetingTV.setText("Hola "+ client.getName()+",\n¿Deseas agregar una foto de perfil?");

        profilePic = findViewById(R.id.profilePic);
        continueBtn = findViewById(R.id.continueBtn);
        skipPicTV = findViewById(R.id.skipPicTV);

        skipPicTV.setOnClickListener(v->{
            Intent intent = new Intent(this,ClientHome.class);
            intent.putExtra("client", client);
            startActivity(intent);
        });
        continueBtn.setOnClickListener(v->{
            Intent intent = new Intent(this,ClientHome.class);
            intent.putExtra("client", client);
            startActivity(intent);
        });

        continueBtn.setEnabled(false);
        profilePic.setOnClickListener(this::openChoice);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onCameraResult);
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onGalleryResult);


    }

    public void openChoice(View view){
        ChoiceDialog dialog = ChoiceDialog.newInstance();
        dialog.setListener(this);
        dialog.show(this.getSupportFragmentManager(),"dialog");
    }
    public void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(this.getExternalFilesDir(null)+"/photo.png");
        Uri uri = FileProvider.getUriForFile(this,this.getPackageName(),file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        cameraLauncher.launch(intent);
    }
    public void onCameraResult(ActivityResult result){
        if(result.getResultCode()==this.RESULT_OK){

            //Foto completa
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            Bitmap thumbnail = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/4,bitmap.getHeight()/4,true);

            profilePic.setImageBitmap(thumbnail);
            Uri uri = FileProvider.getUriForFile(this,this.getPackageName(),file);
            client.setProfilePic(file.getPath());
            continueBtn.setEnabled(true);
            profilePic.setBackground(null);

        }else if(result.getResultCode()==this.RESULT_CANCELED){
            Toast.makeText(this,"Operación cancelada", Toast.LENGTH_LONG).show();
        }
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }
    public void onGalleryResult(ActivityResult result){
        if(result.getResultCode()==this.RESULT_OK){
            Uri uri = result.getData().getData();
            profilePic.setImageURI(uri);
            client.setProfilePic(UtilDomi.getPath(this,uri));
            continueBtn.setEnabled(true);
            profilePic.setBackground(null);
        }
    }

    @Override
    public void onChoice(int choice) {
        if(choice == 0){
            openCamera();
        }else if(choice==1){
            openGallery();
        }
    }
}