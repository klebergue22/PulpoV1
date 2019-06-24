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
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.entities.AdminPerfil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CargarImagenesActivity extends AppCompatActivity {
    /*
    Declaracion de atributos para pantalla
     */
    private ImageView imagenSeleccionada;
    private Button btnGuardar;
    private Button btnCargarImagen;
    private String pathPintar;
    private String nombreImagen;//nombre con el que se guarda la imagen
    private String pathAtributo;//atributo del objeto donde guardo el nombre de la imagen
    private String pathClave; //atributo clave del objeto, por ejemplo para Jugador es cedula
    private String valorClave; //valor del atributo clave, por ejemplo el valor de la cedula
    private ImageView ivImg;


    /*
    Declaracion de atributos del manejo de imágenes
     */
    private static final byte GALLERY_REQUEST_CODE = 1;
    private Uri imagenSelec;
    private StorageReference storageReferencia;



    /*
    Declaracion de atributos para recuperacion del jugador
     */
    private AdminPerfil adminPerfil;

    /*
    Metodo onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_imagenes);
        /*
        Recupero el Jugador
         */

        /*
        Se recupera los valores del intent
         */
        Intent intent = getIntent();
        pathPintar = intent.getStringExtra("paramPathPintar");
        nombreImagen = intent.getStringExtra("paramNombreImagen");
        pathAtributo = intent.getStringExtra("paramPathAtributo");
        pathClave = intent.getStringExtra("paramPathClave");
        valorClave = intent.getStringExtra("paramValorClave");

        //IMPMRIMIR TODOS LOS PARAMS QUE LLEGAN!!!



        atarcomponentes();

        /*
        Instancia de referencia para firebase
         */
        storageReferencia = FirebaseStorage.getInstance().getReference(pathPintar);
        /*
        Boton que llama al metodo que realiza la carga de las imagenes (carga explorador)
         */
        btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarExplorador();
                btnCargarImagen.setVisibility(View.INVISIBLE);

            }
        });
        /*
            LLama al metodo que realiza el guardado de la informacion en el Storage depende de la informacion que se envie
         */
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarImagen();

            }
        });

        recuperarImg();

    }


    /*
    Mètodo que realiza la carga del Explorador
     */
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
        btnGuardar.setVisibility(View.VISIBLE);
    }

    /*
    Metodo que realiza la relacion entre la pagina y java
     */
    public void atarcomponentes() {
        imagenSeleccionada = findViewById(R.id.ivImg);
        btnCargarImagen = findViewById(R.id.btnCargarImg);
        btnGuardar = findViewById(R.id.btnGuardarImg);
        btnGuardar.setVisibility(View.INVISIBLE);

        ivImg=findViewById(R.id.ivImg);
    }

    /*
    Metodo que realiza la insercion de los gráficos
     */
    private void insertarImagen() {

        nombreImagen = System.currentTimeMillis() + "";

        // StorageReference imagenReference = storageReference.child(jugador.getCedula()).child(nombreImagen);
        StorageReference imagenReference = storageReferencia.child(nombreImagen);

        //actualizarImagenPerfil();
        UploadTask uploadTask = imagenReference.putFile(imagenSelec);
        Log.w(LogPulpo.TAG, "SMO El valor de la referencia  es " + imagenReference.getPath());
        uploadTask.addOnFailureListener(new OnFailureListener() {
            /*
            Implementacion de los metodos para saber si tuvo exito o no la accion insertar
             */
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("LogPulpo.TAG ", "error al cagar la imagen " + "EquipoActivity", e);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("LogPulpo.TAG ", "imagen cargada");
                // put the String to pass back into an Intent and close this activity
                actualizarImagenPerfil();

                Intent intent = new Intent();
                intent.putExtra("cargoImagen", true);
                intent.putExtra("nombreImagen", nombreImagen);

                setResult(RESULT_OK, intent);
                finish();

            }
        });


    }

    /*****************************************METODOS SOBREESCRITOS**************************************************************************/
    /*
    Metodo que realiza el repintado de la imagen en el Imagen view en esta clase
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    imagenSelec = data.getData();
                    imagenSeleccionada.setImageURI(imagenSelec);
                    break;
            }
    }


    public void actualizarImagenPerfil() {
            FirebaseDatabase.getInstance().getReference(pathAtributo)
                    .setValue(nombreImagen);
        FirebaseDatabase.getInstance().getReference(pathClave)
                .setValue(valorClave);
    }

    private void recuperarImg() {
        Log.d(LogPulpo.TAG,"Recuperar Imagen en Cargar Imagen nombreImagen>>>"+nombreImagen);


        if (nombreImagen != null) {
            StorageReference imagenReference = storageReferencia.child(nombreImagen);
            Log.d(LogPulpo.TAG,"Recuperar Imagen en Cargar Imagen imagenReference>>>"+imagenReference);
            GlideApp.with(getApplicationContext())
                    .load(imagenReference)
                    .error(R.drawable.ic_person_outline_24px)
                    .into(ivImg);
        }
    }

}
