package com.cmc.pulpov1.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cmc.pulpov1.IdentificadorUtils;
import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.entities.Equipo;
import com.cmc.pulpov1.entities.Rol;
import com.cmc.pulpov1.entities.UsuarioRol;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrearEquipoActivity extends AppCompatActivity {

    private static final byte GALLERY_REQUEST_CODE = 1;
    private Button btnCargarImagen;
    private EditText etNombreE;
    private EditText etCategoria;
    private EditText etNombreRepresentanteE;
    private EditText etApellidoRepresentanteE;
    private EditText etMailE;
    private EditText etTelefonoE;
    private ImageView imagenE;
    private Button btnCrearE;
    private ConstraintLayout constraintLayout;
    private Equipo equipo;
    private String codigoTorneo;
    private Uri selectedImage;
    private StorageReference storageReference;
    private UsuarioRol usuarioRol;
    private String mailC;
    private Rol rol;
    private List<Rol> roles;
    private UsuarioRol representanteEquipoRol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_equipo);
        atarComponentes();

        desplazarPantalla();

        storageReference = FirebaseStorage.getInstance().getReference();
        btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();

            }
        });
        btnCrearE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearEquipo();
            }
        });


    }

    public void insertarEquipo() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refEquipo = database.
                getReference(Rutas.ROOT_TORNEOS).
                child(PulpoSingleton.getInstance().getCodigoTorneo()).
                child(Rutas.CATEGORIAS).
                child(PulpoSingleton.getInstance().getCodigoCategoria()).
                child(Rutas.EQUIPOS);
        //etAnios = Integer.parseInt(etAnio.getText().toString().trim());

        equipo = new Equipo();
        usuarioRol = new UsuarioRol();
        rol = new Rol();
        representanteEquipoRol = new UsuarioRol();
        roles = new ArrayList<Rol>();


        equipo.setNombreEquipo(etNombreE.getText().toString());
        equipo.setNombreRepresentante(etNombreRepresentanteE.getText().toString());
        equipo.setApellidoRepresentante(etApellidoRepresentanteE.getText().toString());
        equipo.setMail(etMailE.getText().toString());
        Log.d("PULPOLOG", "el MAIL  SETEADO ES " + equipo.getMail()+"CrearEquipoActivity");
        mailC = IdentificadorUtils.crearIdentificacionMail(equipo.getMail());
        Log.d("PULPOLOG", "EL ID DEL REPRESENTANTE ES " + mailC+"CrearEquipoActiviy");
        equipo.setTelefono(etTelefonoE.getText().toString());
        equipo.setCategoria(etCategoria.getText().toString());
        equipo.setId(equipo.getNombreEquipo() + "_" + equipo.getCategoria());
        PulpoSingleton.getInstance().setCodigoEquipo(equipo.getId());
        refEquipo.child(equipo.getId()).setValue(equipo);



        rol.setIdRol("3");
        rol.setIdTorneo(PulpoSingleton.getInstance().getCodigoTorneo());
        rol.setIdEquipo(equipo.getId());


        roles = PulpoSingleton.getInstance().getRoles();
        final DatabaseReference refUsuarioRol = database.getReference(Rutas.USUARIOS).child(mailC);

        refUsuarioRol.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuarioRol=dataSnapshot.getValue(UsuarioRol.class);
                if(usuarioRol==null){
                    usuarioRol=new UsuarioRol();
                    usuarioRol.setNombre(equipo.getNombreRepresentante());
                    usuarioRol.setApellido(equipo.getApellidoRepresentante());
                    refUsuarioRol.setValue(usuarioRol);
                    refUsuarioRol.child(Rutas.ROLES).push().setValue(rol);

                }else{
                    refUsuarioRol.child(Rutas.ROLES).push().setValue(rol);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(this,
                "Se inserto el equipo:" + equipo.getNombreEquipo().toString(), Toast.LENGTH_LONG).show();
        Log.d("PULPOLOG", "Se inserto el EQUIPO"+equipo.getNombreEquipo().toString()+"EquipoActivity");

        //TODO:Listeners de exito o error

        //  finish();
    }

    private void insertarImagen() {
        StorageReference imagenReference = storageReference.child("equipos/" + equipo.getId());
        UploadTask uploadTask = imagenReference.putFile(selectedImage);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("PULPOLOG ", "error al cagar la imagen del torneo "+"EquipoActivity", e);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("PULPOLOG ", "imagen cargada"+"EquipoActivity");
            }
        });
    }


    public void crearEquipo() {
        boolean resultadoValidacion;
        Log.w("PULPOLOG", "Ingresa al Metodo crearEquipo()"+"EquipoActivity");
        // String salida = "Mail:" + etMail.getText().toString() + "Password:" + etPassword.getText().toString();
      /* Toast.makeText(this,
             "Valor:" + salida, Toast.LENGTH_LONG).show();*/
        resultadoValidacion = validarCampos();
        Log.w("PULPOLOG", "RESULTADO VALIDACION>>" + resultadoValidacion+"EquipoActivity");
        if (resultadoValidacion) {
            insertarEquipo();
            insertarImagen();
            limpiarComponentes();
            finish();

        }
    }


    //Mueve la pantalla hacia arriba cuando se abre el teclado, regresa a la posición original cuando se cierra el teclado
    private void desplazarPantalla() {
        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                constraintLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = constraintLayout.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    //Toast.makeText(MainActivity.this,"Keyboard is showing",Toast.LENGTH_LONG).show();
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    //Subir la línea de guía
                    constraintSet.setGuidelinePercent(R.id.guideline, 0.001f); // 7% // range: 0 <-> 1
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.applyTo(constraintLayout);
                } else {
                    //Toast.makeText(MainActivity.this,"keyboard closed",Toast.LENGTH_LONG).show();
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    //Regresar la línea de guía a la posición original
                    constraintSet.setGuidelinePercent(R.id.guideline, 0.20f); // 7% // range: 0 <-> 1
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.applyTo(constraintLayout);
                }
            }
        });
    }

    private void atarComponentes() {

        etCategoria = findViewById(R.id.etCategoriaE);
        etNombreE = findViewById(R.id.etNombreE);
        etNombreRepresentanteE = findViewById(R.id.etNombreRepresentanteE);
        etApellidoRepresentanteE = findViewById(R.id.etApellidoRepresentanteE);
        etMailE = findViewById(R.id.etMailE);
        etTelefonoE = findViewById(R.id.etTelefonoE);
        btnCrearE = findViewById(R.id.btnIngresarE);
        constraintLayout = findViewById(R.id.rootview);
        imagenE = findViewById(R.id.iVEquipo);
        btnCargarImagen = findViewById(R.id.btnCargarImg);
    }

    private void limpiarComponentes() {
        etCategoria.setText("");
        etNombreE.setText("");
        etNombreRepresentanteE.setText("");
        etApellidoRepresentanteE.setText("");
        etMailE.setText("");
        etTelefonoE.setText("");


    }

    public boolean validarCampos() {
        boolean correcto = true;
        Log.w("PULPOLOG", "ingreso al Metodo validarCampos() "+"EquipoActivity");
        if (etNombreE.getText() != null && etNombreE.getText().toString().isEmpty()) {
            etNombreE.requestFocus();
            etNombreE.setError("El Nombre del equipo es obligatorio");
            correcto = false;
        }
        if (etNombreRepresentanteE.getText() != null && etNombreRepresentanteE.getText().toString().isEmpty()) {
            etNombreRepresentanteE.requestFocus();
            etNombreRepresentanteE.setError("El Nombre del representante del equipo es obligatorio");
            correcto = false;
        }
        if (etApellidoRepresentanteE.getText() != null && etApellidoRepresentanteE.getText().toString().isEmpty()) {
            etApellidoRepresentanteE.requestFocus();
            etApellidoRepresentanteE.setError("El Apellido del representante del equipo es obligatorio");
            correcto = false;
        }


        if (etMailE.getText() != null && etMailE.getText().toString().isEmpty()) {
            etMailE.requestFocus();
            etMailE.setError("El Mail del equipo es obligatorio");
            correcto = false;
        }
        if (etTelefonoE.getText() != null && etTelefonoE.getText().toString().isEmpty()) {
            etTelefonoE.requestFocus();
            etTelefonoE.setError("El Teléfono del equipo es obligatorio");
            correcto = false;
        }

        return correcto;
    }




    //Metodo para capturar

    private void pickFromGallery() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("PULPOLOG", "Ingresa al onActivityResult");
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    selectedImage = data.getData();

                    imagenE.setImageURI(selectedImage);
                    Log.d("PULPOLOG", "Toma el valor del URI" + imagenE.toString());
                    break;
            }
    }
}
