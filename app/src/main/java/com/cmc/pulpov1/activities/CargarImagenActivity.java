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

import com.cmc.pulpov1.LogPulpo;
import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.entities.AdminPerfil;
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
    private StorageReference storageReference;
    private ImageView imagenE;
    private Button btnCargarImagen;
    private Button btnGuardar;
    private Jugador jugador;
    private String tipoImagen;//cedulas,fotoPerfil
    private String nombreImagen;//valor de milisegundos
    private AdminPerfil adminPerfil;
    private String tipoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_imagen);
        Intent intent = getIntent();
        tipoImagen = intent.getStringExtra("paramTipoImagen");
        tipoPerfil = intent.getStringExtra("paramTipoPerfil");
        recuperarJugador();

        Log.d(LogPulpo.TAG, "KDGGel valor recuperado del tipoImagen es " + tipoImagen);
        Log.d(LogPulpo.TAG, "KDGGel valor recuperado del nombreImagen es " + nombreImagen);
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
        recuperarImg();
    }

    public void atarcomponentes() {
        imagenE = findViewById(R.id.ivCedula);
        btnCargarImagen = findViewById(R.id.btnCargarCedula);
        btnGuardar = findViewById(R.id.btnGuardarImg);
        btnGuardar.setVisibility(View.INVISIBLE);
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
        btnGuardar.setVisibility(View.VISIBLE);
    }

    private void insertarImagen() {
        Log.d(LogPulpo.TAG,"INSERTANDO IMAGEN DE TIPO "+tipoImagen);
        if (Rutas.FOTOPERFIL.equals(tipoImagen)) {
            jugador.setImagenPerfil(System.currentTimeMillis() + "");
            nombreImagen = jugador.getImagenPerfil();
        } else if (Rutas.CEDULAS.equals(tipoImagen)) {
            jugador.setImagenCedula(System.currentTimeMillis() + "");
            nombreImagen = jugador.getImagenCedula();
        }
        Log.d(LogPulpo.TAG, "eL VALOR DEL JUGADOR ES " + jugador);
        StorageReference imagenReference = storageReference.child(jugador.getCedula()).child(nombreImagen);

        actualizarImagenPerfil();
        UploadTask uploadTask = imagenReference.putFile(selectedImage);
        Log.w(LogPulpo.TAG, "SMO El valor de la referencia x es " + imagenReference.getPath());
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("LogPulpo.TAG ", "error al cagar la imagen " + "EquipoActivity", e);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("LogPulpo.TAG ", "imagen cargada" + "EquipoActivity");
                // put the String to pass back into an Intent and close this activity
                Intent intent = new Intent();
                intent.putExtra("cargoImagen", true);
                setResult(RESULT_OK, intent);
                finish();

            }
        });


    }

    public void actualizarImagenPerfil() {
        if (Rutas.FOTOPERFIL.equals(tipoImagen)) {
            FirebaseDatabase.getInstance().getReference(Rutas.JUGADORES)
                            .child(PulpoSingleton.getInstance()
                            .getMail()).child(tipoPerfil)
                            .child(Rutas.IMGPERFIL)
                            .setValue(jugador.getImagenPerfil());

            FirebaseDatabase.getInstance().getReference(Rutas.JUGADORES)
                    .child(PulpoSingleton.getInstance()
                            .getMail()).child(tipoPerfil)
                    .child("cedula")
                    .setValue(jugador.getCedula());
        } else {
            if (Rutas.CEDULAS.equals(tipoImagen)) {
                FirebaseDatabase.getInstance().getReference(Rutas.JUGADORES)
                        .child(PulpoSingleton.getInstance().getMail())
                        .child(tipoPerfil)
                        .child(Rutas.IMGCEDULA)
                        .setValue(jugador.getImagenCedula());

                FirebaseDatabase.getInstance().getReference(Rutas.JUGADORES)
                        .child(PulpoSingleton.getInstance()
                                .getMail()).child(tipoPerfil)
                        .child("cedula")
                        .setValue(jugador.getCedula());
            }
        }
    }

    public void recuperarJugador() {
        adminPerfil = PulpoSingleton.getInstance().getAdminPerfil();
        switch (tipoPerfil) {
            case Rutas.PRINCIPAL:
                jugador = adminPerfil.getPrincipal();
                break;
            case Rutas.ADICIONAL1:
                jugador = adminPerfil.getAdicional1();
                break;
            case Rutas.ADICIONAL2:
                jugador = adminPerfil.getAdicional2();
                break;
        }
        Log.d(LogPulpo.TAG,"jugador recuperado "+jugador);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    selectedImage = data.getData();
                    imagenE.setImageURI(selectedImage);
                    break;
            }
    }

    private void recuperarImg() {

        if (Rutas.FOTOPERFIL.equals(tipoImagen)) {
            nombreImagen = jugador.getImagenPerfil();
        } else if (Rutas.CEDULAS.equals(tipoImagen)) {
            nombreImagen = jugador.getImagenCedula();
        }

        if (jugador.getCedula() != null && nombreImagen!=null ) {
            StorageReference imagenReference = storageReference.child(jugador.getCedula()).child(nombreImagen);
            GlideApp.with(getApplicationContext())
                    .load(imagenReference)
                    .error(R.drawable.ic_person_outline_24px)
                    .into(imagenE);
        }
    }
}
