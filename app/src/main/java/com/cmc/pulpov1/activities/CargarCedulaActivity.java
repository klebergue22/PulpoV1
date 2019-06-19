package com.cmc.pulpov1.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.entities.Jugador;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CargarCedulaActivity extends AppCompatActivity {
    private static final byte GALLERY_REQUEST_CODE = 1;
    private Uri selectedImage;
    private StorageReference storageReference;
    private ImageView imagenE;
    private Button btnCargarImagen;
    private Jugador jugador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_cedula);
        atarcomponentes();
        storageReference = FirebaseStorage.getInstance().getReference();
        btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarExplorador();

            }
        });

        jugador=PulpoSingleton.getInstance().getJugador();
        if (jugador==null){
            jugador=new Jugador();

        }

    }

    public void atarcomponentes() {
        imagenE = findViewById(R.id.ivCedula);
        btnCargarImagen = findViewById(R.id.btnCargarCedula);

    }

    private void cargarExplorador() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void insertarImagen() {
        StorageReference imagenReference = storageReference
                .child(Rutas.CEDULAS + "/" + PulpoSingleton.getInstance().getJugador().getCedula());
      UploadTask uploadTask = imagenReference.putFile(selectedImage);
        Log.w(Rutas.TAG,"El valor de la referencia es "+imagenReference.getPath());

      uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("PULPOLOG ", "error al cagar la cedula " + "EquipoActivity", e);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("PULPOLOG ", "cedula cargada" + "EquipoActivity");
            }
        });
    }

    public void guardar() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  Log.d("PULPOLOG", "Ingresa al onActivityResult");
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    selectedImage = data.getData();

                    imagenE.setImageURI(selectedImage);
                    //    Log.d("PULPOLOG", "Toma el valor del URI" + imagenE.toString());
                    break;


            }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu2, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        boolean valor = true;
        if (id == R.id.btnCargarCedulaJugador) {
            insertarImagen();

            finish();

            return valor;
        }
        return valor;

        //return super.onOptionsItemSelected(item);
    }
}
