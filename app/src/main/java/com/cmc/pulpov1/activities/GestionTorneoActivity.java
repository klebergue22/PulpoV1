package com.cmc.pulpov1.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.cmc.pulpov1.IdentificadorUtils;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.entities.Rol;
import com.cmc.pulpov1.entities.Torneo;
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class GestionTorneoActivity extends AppCompatActivity {
    private static final byte GALLERY_REQUEST_CODE = 1;
    private Button btnCargarImagen;
    private Button btnCrearTorneo;
    //  private EditText etId;
    private EditText etNombre;
    private EditText etAnio;
    private EditText etNombreOrg;
    private EditText etApellidoOrg;
    private EditText etCorreoOrg;
    private EditText etTelefonoOrg;
    private int etAnios;
    private EditText etInicia;
    private ImageView imagen;
    private Date etFechIni;
    private int anio;
    private int mes;
    private int dia;
    private String fecha;
    private Torneo torneo;
    private ConstraintLayout constraintLayout;
    private String id;
    private Uri selectedImage;
    private StorageReference storageReference;
    private String mail;
    private Rol rol;
    private UsuarioRol usuarioRol;
    private DatabaseReference refUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_torneo);
        rol = new Rol();

        atarComponentes();
        desplazarPantalla();
        storageReference = FirebaseStorage.getInstance().getReference();
        //Boton para cargar imagenes
        btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();

            }
        });

        btnCrearTorneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Log.w("LogPulpo.TAG", "INGRESA AL BOTON CREAR TORNEO");
                ingresar();
               // Log.w("LogPulpo.TAG", "INGRESA AL BOTON CREAR TORNEO");


            }
        });

        etInicia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                anio = mcurrentDate.get(Calendar.YEAR);
                mes = mcurrentDate.get(Calendar.MONTH);
                dia = mcurrentDate.get(Calendar.DAY_OF_MONTH);

//CORREGIR IGUAL A JUGADOR
                final DatePickerDialog mDatePicker = new DatePickerDialog

                        (GestionTorneoActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                Log.d("LogPulpo.TAG", "selectedday" + selectedday);
                                etInicia.setText(new StringBuilder().append(selectedyear).append("/").append(selectedmonth + 1).append("/").append(selectedday));
                                Log.d("LogPulpo.TAG", "etinicia" + etInicia.getText().toString());
                                int month_k = selectedmonth + 1;

                                //armar la fecha con los valores de anio mes y dia que selecciona

                                final Calendar c = Calendar.getInstance();
                                c.set(anio, mes, dia);
                                fecha = DateFormat.getInstance().format(c.getTime());
                                etFechIni = c.getTime();
                                //fecha=(String)"  "+selectedyear+selectedmonth+selectedday;
                                Log.d("LogPulpo.TAG", "Fecha armada" + fecha);
                                //fechai.parse(fecha);


                                Log.d("LogPulpo.TAG", "Fecha inicio" + etFechIni.toString());

                            }
                        }, anio, mes, dia);

                mDatePicker.setTitle("Please select date");
                // TODO Hide Future Date Here
                mDatePicker.getDatePicker();

                // TODO Hide Past Date Here
                //  mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                mDatePicker.show();
            }

        });

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
                    constraintSet.setGuidelinePercent(R.id.guideline, 0.2f); // 7% // range: 0 <-> 1
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.applyTo(constraintLayout);
                }
            }
        });
    }

    public void insertarTorneo() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refTorneos = database.getReference(Rutas.ROOT_TORNEOS);
        mail = IdentificadorUtils.crearIdentificacionMail(etCorreoOrg.getText().toString());
        Log.d("LogPulpo.TAG", "El correo del organizador es el siguiente" + mail);

        Log.w("LogPulpo.TAG", "EL VALOR DE etAnios ANTES DE PARSEAR" + etAnios);
        if ("" != etAnio.getText().toString()) {
            try {
                etAnios = Integer.parseInt(etAnio.getText().toString().trim());
                Log.w("LogPulpo.TAG", "EL VALOR DE etAnios" + etAnios);
            } catch (NumberFormatException nf) {
                nf.printStackTrace();
            }
        }
        //etAnios = Integer.parseInt(etAnio.getText().toString().trim());
        id = etNombre.getText().toString() + "_" + etAnio.getText().toString();
        Log.d("LogPulpo.TAG", "EL VALOR DEL ID ES " + id);

        //  etId.setText(id);

        torneo = new Torneo();
        torneo.setId(id);
        torneo.setNombreTorneo(etNombre.getText().toString());
        torneo.setAnio(etAnios);
        torneo.setFechaInicio(etFechIni);
        torneo.setCorreoOrganizador(etCorreoOrg.getText().toString());
        torneo.setNombreOrganizador(etNombreOrg.getText().toString());
        torneo.setApellidoOrganizador(etApellidoOrg.getText().toString());
        torneo.setTelefonoOrganizador(etTelefonoOrg.getText().toString());


        refUsuario = database.getReference(Rutas.USUARIOS).child(mail);
        rol.setIdRol("2");
        rol.setIdTorneo(id);

        refUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UsuarioRol usuarioRol=dataSnapshot.getValue(UsuarioRol.class);
                    if(usuarioRol==null){
                        usuarioRol=new UsuarioRol();
                        usuarioRol.setNombre(torneo.getNombreOrganizador());
                        Log.d("LogPulpo.TAG","Nombre Organizador"+ usuarioRol.getNombre());
                        usuarioRol.setApellido(torneo.getApellidoOrganizador());
                        Log.d("LogPulpo.TAG","Apellido Organizador"+ usuarioRol.getApellido());
                        refUsuario.setValue(usuarioRol);
                        refUsuario.child(Rutas.ROLES).push().setValue(rol);
                    }else{
                        refUsuario.child(Rutas.ROLES).push().setValue(rol);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        refTorneos.child(torneo.getId()).setValue(torneo);
        Log.d("LogPulpo.TAG", "Se inserto el torneo");


    }

    private void validaRol(){


    }

    private void insertarImagen() {
        StorageReference imagenReference = storageReference.child("torneos/" + torneo.getId());
        UploadTask uploadTask = imagenReference.putFile(selectedImage);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("LogPulpo.TAG ", "error al cagar la imagen del torneo ", e);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("LogPulpo.TAG ", "imagen cargada");
            }
        });
    }

    public void atarComponentes() {
        // etId = findViewById(R.id.etIdTorneo);
        etNombre = findViewById(R.id.etNombreTorneo);
        etAnio = findViewById(R.id.etAnio);
        etInicia = (EditText) findViewById(R.id.etFechInicio);
        etNombreOrg = findViewById(R.id.etNombreOrganizador);
        etApellidoOrg = findViewById(R.id.etApellidoOrganizador);
        etCorreoOrg = findViewById(R.id.etCorreoOrganizador);
        etTelefonoOrg = findViewById(R.id.etTelefonoOrganizador);
        btnCrearTorneo = findViewById(R.id.btnCrearT);
        constraintLayout = findViewById(R.id.rootview);
        btnCargarImagen = findViewById(R.id.btnCargarImagen);
        imagen = findViewById(R.id.iVTorneo);

        //etId.setEnabled(false);
    }

    public void limpiarComponentes() {
        //  etId.setText("");
        etNombre.setText("");
        etAnio.setText("");
        etInicia.setText("");
        etNombreOrg.setText("");
        etCorreoOrg.setText("");
        etTelefonoOrg.setText("");


    }

    public void ingresar() {
        boolean resultadoValidacion;
        Log.w("LogPulpo.TAG", "Ingresa al Metodo ingresar()");
        // String salida = "Mail:" + etMail.getText().toString() + "Password:" + etPassword.getText().toString();
      /* Toast.makeText(this,
             "Valor:" + salida, Toast.LENGTH_LONG).show();*/
        resultadoValidacion = validarCampos();
        Log.w("LogPulpo.TAG", "RESULTADO VALIDACION>>" + resultadoValidacion);
        if (resultadoValidacion) {
            insertarTorneo();
            insertarImagen();
            limpiarComponentes();
            finish();
        }
    }

    public boolean validarCampos() {
        boolean correcto = true;

        Log.w("LogPulpo.TAG", "ingreso al Metodo validarCampos() ");
       /* if (etId.getText() != null && etId.getText().toString().isEmpty()) {
            etId.requestFocus();
            etId.setError("El Id es obligatorio");
            correcto = false;
        }*/
        if (etNombre.getText() != null && etNombre.getText().toString().isEmpty()) {

            etNombre.requestFocus();
            etNombre.setError("El nombre del Torneo es obligatorio");
            correcto = false;
        }
        if (etAnio.getText() != null && etAnio.getText().toString().isEmpty()) {
            Log.w("LogPulpo.TAG", "ingreso al Metodo Valicadion de año ");

            etAnio.requestFocus();
            etAnio.setError("El año del Torneo es obligatorio");
            correcto = false;

        }


        if (etInicia.getText() != null && etInicia.getText().toString().isEmpty()) {
            etInicia.requestFocus();
            etInicia.setError("El nombre del Torneo es obligatorio");
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
        Log.d("LogPulpo.TAG", "Ingresa al onActivityResult");
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    selectedImage = data.getData();

                    imagen.setImageURI(selectedImage);
                    Log.d("LogPulpo.TAG", "Toma el valor del URI" + imagen.toString());
                    break;
            }
    }
}