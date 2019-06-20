package com.cmc.pulpov1.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.entities.Jugador;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CargarImagenActivity extends AppCompatActivity {
    private static final byte GALLERY_REQUEST_CODE = 1;
    private Uri selectedImage;
    private  StorageReference storageReference;
    private ImageView imagenE;
    private Button btnCargarImagen;
    private Button btnGuardar;
    private Jugador jugador;
    private String tipoImagen;//cedulas,fotoPerfil
    private String nombreImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_imagen);
        Intent intent = getIntent();
        tipoImagen = intent.getStringExtra("paramTipoImagen");
        nombreImagen = intent.getStringExtra("paramNombreImagen");
        PulpoSingleton.getInstance().setTipo(tipoImagen);
        Log.d(Rutas.TAG, "el valor recuperado del tipoImagen es " + tipoImagen);


        atarcomponentes();
        storageReference = FirebaseStorage.getInstance().getReference(tipoImagen);
        btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarExplorador();

            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarImagen();
            }
        });

        jugador = PulpoSingleton.getInstance().getJugador();

        recuperarImg();
    }

    public void atarcomponentes() {
        imagenE = findViewById(R.id.ivCedula);
        btnCargarImagen = findViewById(R.id.btnCargarCedula);
        btnGuardar = findViewById(R.id.btnGuardarImg);

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
        btnCargarImagen.setVisibility(View.INVISIBLE);
    }

    private void insertarImagen() {
       /* StorageReference imagenReference = storageReference
                .child(tipoImagen);*/
     /*  String imagenPerfil=System.currentTimeMillis()+"";
       if(jugador.getImagenPerfil()!=null){
           imagenPerfil=jugador.getImagenPerfil();
       }else{*/
     if(Rutas.PERFIL.equals(tipoImagen)) {
         jugador.setImagenPerfil(System.currentTimeMillis() + "");
         nombreImagen = jugador.getImagenPerfil();
     }else if(Rutas.CEDULAS.equals(tipoImagen)){
            jugador.setImagenCedula(System.currentTimeMillis() + "");
            nombreImagen = jugador.getImagenCedula();
     }

        StorageReference imagenReference=storageReference.child(jugador.getCedula()).child(nombreImagen);

        actualizarImagenPerfil();

        UploadTask uploadTask =imagenReference.putFile(selectedImage);
        Log.w(Rutas.TAG, "SMO El valor de la referencia es " + imagenReference.getPath());

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("PULPOLOG ", "error al cagar la imagen " + "EquipoActivity", e);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("PULPOLOG ", "imagen cargada" + "EquipoActivity");
                // put the String to pass back into an Intent and close this activity
                Intent intent = new Intent();
                intent.putExtra("cargoImagen", true);
                setResult(RESULT_OK, intent);
                finish();

            }
        });


    }

    public void actualizarImagenPerfil(){
        if(Rutas.PERFIL.equals(tipoImagen)) {
            FirebaseDatabase.getInstance().getReference(Rutas.JUGADORES).child(PulpoSingleton.getInstance().getMail())
                    .child("imagenPerfil").setValue(jugador.getImagenPerfil());
        }else{
            if(Rutas.CEDULAS.equals(tipoImagen)) {
                FirebaseDatabase.getInstance().getReference(Rutas.JUGADORES).child(PulpoSingleton.getInstance().getMail())
                        .child("imagenCedula").setValue(jugador.getImagenCedula());
            }
        }
    }

    public void navIrRegistroJugador(){
        Intent intent=new Intent(this,RegistroJugadorActivity.class);
        intent.putExtra("tipoImagen", tipoImagen);
        startActivity(intent);
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
                    //recargarImg();
                  /*  GlideApp.with(getApplicationContext())
                            .load(selectedImage)

                            .circleCrop().
                            error(R.drawable.ic_person_outline_24px)
                            .into(imagenE);*/
                    //    Log.d("PULPOLOG", "Toma el valor del URI" + imagenE.toString());
                    break;


            }
    }

    private void recuperarImg() {

        if(nombreImagen!=null&&jugador.getCedula()!=null) {
            StorageReference imagenReference = storageReference.child(jugador.getCedula()).child(nombreImagen);
            Log.d(Rutas.TAG, "el tipoImagen es  ++++" + tipoImagen);
            Log.d(Rutas.TAG, "path imagen  ++++" + imagenReference.getPath());

            GlideApp.with(getApplicationContext())
                    .load(imagenReference)


                    .error(R.drawable.ic_person_outline_24px)
                    .into(imagenE);


        }
    }

   /* private void recargarImg() {

        StorageReference imagenReference = storageReference.child(jugador.getCedula()+"xx");
        Log.d(Rutas.TAG, "el tipoImagen es  ++++" + tipoImagen);
        Log.d(Rutas.TAG, "path imagen  ++++" + imagenReference.getPath());

        GlideApp.with(getApplicationContext())

                .load(imagenReference).
                skipMemoryCache(true)
                 .signature(new ObjectKey(jugador.getCedula()+String.valueOf(System.currentTimeMillis())))
                .circleCrop().error(R.drawable.ic_person_outline_24px)
                .into(imagenE);

    }*/

}
