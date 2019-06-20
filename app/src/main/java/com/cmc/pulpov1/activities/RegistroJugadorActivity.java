package com.cmc.pulpov1.activities;

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
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cmc.pulpov1.PulpoSingleton;
import com.cmc.pulpov1.R;
import com.cmc.pulpov1.Rutas;
import com.cmc.pulpov1.adapters.JugadorAdapter;
import com.cmc.pulpov1.entities.Jugador;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RegistroJugadorActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private EditText etCedula;
    private EditText etPrimerNombre;
    private EditText etPrimerApellido;
    private EditText etSegundoNombre;
    private EditText etSegundoApellido;
    private EditText etFechaNacimiento;
    private TextInputLayout tilCedulaRecuperar;
    private TextInputLayout tilPrimerNombreJug;
    private TextInputLayout tilSegundoNombre;
    private TextInputLayout tilPrimerApellido;
    private TextInputLayout tilSegundoApellido;
    private TextInputLayout tilFechaNacimeinto;
    private EditText etMailJ;
    private ConstraintLayout constraintLayout;
    private Jugador jugador;
    private List<Jugador> jugadores;
    private String mailc;
    private Date fechaN;
    private int anio;
    private int mes;
    private int dia;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat sdfCompleto = new SimpleDateFormat("dd MMM yyyy");
    private Button btnAprobar;
    private ImageButton btnCargarCedula;
    private Button btnCargarImgPerfil;
    private Uri selectedImage;
    private StorageReference storageReference;
    private ImageView imagenE;
    private static final byte GALLERY_REQUEST_CODE = 1;
    private static final byte GALLERY_REQUEST_ID = 2;
    private JugadorAdapter jadp;
    private String tipoJugador;
    private String tipoPerfil;
    private StorageReference imageReference;
    private static final int ACTIVITY_CARGAR_IMAGEN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_jugador);
        Intent intent = getIntent();
        tipoJugador = intent.getStringExtra("paramTipoJugador");
        tipoPerfil=intent.getStringExtra("paramTipoPerfil");
        atarComponentes();


        database = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference(Rutas.PERFIL);
        //etMailJ.setText(PulpoSingleton.getInstance().getMailN());
        //mailc=PulpoSingleton.getInstance().getMail();
        //etMailJ.setEnabled(false);
        jugadores = new ArrayList<Jugador>();
        jugadores = PulpoSingleton.getInstance().getJugadores();

        jugador = PulpoSingleton.getInstance().getJugador();
        if(jugador==null){
            jugador = new Jugador();
            PulpoSingleton.getInstance().setJugador(jugador);
        }
        Log.d(Rutas.TAG, "el valor del jugador en el onCreate es " + jugador.toString());
        recuperarImg();


        if (jugador != null && jugador.getCedula()!=null) {

            mailc = PulpoSingleton.getInstance().getMail();
            etCedula.setText(jugador.getCedula());
            etCedula.setEnabled(false);
            etPrimerNombre.setText(jugador.getPrimerNombre());
            etPrimerApellido.setText(jugador.getPrimerApellido());
            etSegundoNombre.setText(jugador.getSegundoNombre());
            etSegundoApellido.setText(jugador.getSegundoApellido());

            if (jugador.getFechaNacimiento() != null) {
                etFechaNacimiento.setText(sdfCompleto.format(jugador.getFechaNacimiento()));

            }
        } else {
            limpiarComponentes();
            // btnCargarCedula.setVisibility(View.INVISIBLE);
        }

        desplazarPantalla();

        etFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                anio = mcurrentDate.get(Calendar.YEAR);
                mes = mcurrentDate.get(Calendar.MONTH);
                dia = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                final DatePickerDialog mDatePicker = new DatePickerDialog

                        (RegistroJugadorActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                int month_k = selectedmonth + 1;
                                //armar la fecha con los valores de anio mes y dia que selecciona
                                final Calendar c = Calendar.getInstance();
                                c.set(selectedyear, selectedmonth, selectedday);
                                fechaN = c.getTime();
                                etFechaNacimiento.setText(sdfCompleto.format(fechaN));
                            }
                        }, anio, mes, dia);

                mDatePicker.setTitle("Seleccione una fecha");
                mDatePicker.getDatePicker();
                mDatePicker.show();
            }

        });

        btnCargarImgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navIrCargarFotoPerfil();
            }
        });

        btnCargarCedula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((etCedula.getText().toString() == null)) {
                    tilCedulaRecuperar.setError("Debe guardar la cedula para poder cargar la imagen");
                } else
                    navIrCargarCedula();

            }
        });

    }

    public void limpiarComponentes() {
        etCedula.setText("");
        etPrimerNombre.setText("");
        etPrimerApellido.setText("");
        etSegundoNombre.setText("");
        etSegundoApellido.setText("");
        etFechaNacimiento.setText("");


    }

    public void insertarJugador() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mailc = PulpoSingleton.getInstance().getMail();
        Log.d(Rutas.TAG, "El valor dell mail es el siguiente***MAILC " + mailc);
        DatabaseReference refJugador = database.getReference(Rutas.JUGADORES).child(mailc);

        jugador.setMailJugador(PulpoSingleton.getInstance().getMail());
        jugador.setCedula(etCedula.getText().toString());
        jugador.setPrimerNombre(etPrimerNombre.getText().toString());
        jugador.setPrimerApellido(etPrimerApellido.getText().toString());
        jugador.setSegundoNombre(etSegundoNombre.getText().toString());
        jugador.setSegundoApellido(etSegundoApellido.getText().toString());
        if (fechaN == null) {
            try {
                fechaN = sdfCompleto.parse(etFechaNacimiento.getText().toString());
            } catch (Exception e) {
                Log.e("PULPOLOG", "Error al convertir la fehca");
            }
        }
        jugador.setFechaNacimiento(fechaN);
        refJugador.setValue(jugador);//si es exitoso o no
        PulpoSingleton.getInstance().setJugador(jugador);
        finish();
    }

    private void desplazarPantalla() {
        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                constraintLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = constraintLayout.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.setGuidelinePercent(R.id.guideline, 0.1f); // 7% // range: 0 <-> 1
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.applyTo(constraintLayout);
                } else {
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    //Regresar la línea de guía a la posición original
                    constraintSet.setGuidelinePercent(R.id.guideline, 0.4f); // 7% // range: 0 <-> 1
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.applyTo(constraintLayout);
                }
            }
        });
    }

    public void atarComponentes() {
        etCedula = findViewById(R.id.etCedula);
        etPrimerNombre = findViewById(R.id.etPrimerNombre);
        etPrimerApellido = findViewById(R.id.etPrimerApellido);
        etSegundoNombre = findViewById(R.id.etSegundoNombre);
        etSegundoApellido = findViewById(R.id.etSegundoApellido);
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento);
        constraintLayout = findViewById(R.id.rootview);
        btnAprobar = findViewById(R.id.btnANuevoJugador);
        btnCargarImgPerfil = findViewById(R.id.btnCargarImagenJugador);
        btnCargarCedula = findViewById(R.id.btnCargarCedula);
        imagenE = findViewById(R.id.ivFotoPerfil);
        tilCedulaRecuperar = findViewById(R.id.tilCedulaRecuperar);
        tilPrimerNombreJug = findViewById(R.id.tilPrimerNombreJug);
        tilSegundoNombre = findViewById(R.id.tilSegundoNombre);
        tilPrimerApellido = findViewById(R.id.tilPrimerApellido);
        tilSegundoApellido = findViewById(R.id.tilSegundoApellido);
        tilFechaNacimeinto = findViewById(R.id.tilFechaNacimeinto);
    }

    public void crearJugador() {
        boolean resultadoValidacion;
        resultadoValidacion = validarCampos();
        if (resultadoValidacion) {
            insertarJugador();
         //   insertarImagen();
        }
    }

    public boolean validarCampos() {
        boolean correcto = true;
        Log.w("PULPOLOG", "ingreso al Metodo validarCampos() ");
        if (etCedula.getText() != null && etCedula.getText().toString().isEmpty()) {
            etCedula.requestFocus();
            //etCedula.setError("La cédula es obligatorio");
            tilCedulaRecuperar.setError("La cédula es obligatorio");
            correcto = false;
        }
        if (etPrimerNombre.getText() != null && etPrimerNombre.getText().toString().isEmpty()) {

            etPrimerNombre.requestFocus();
            //etPrimerNombre.setError("El nombre  es obligatorio");
            tilPrimerNombreJug.setError("El nombre  es obligatorio");
            correcto = false;
        }
        if (etPrimerApellido.getText() != null && etPrimerApellido.getText().toString().isEmpty()) {
            Log.w("PULPOLOG", "ingreso al Metodo Valicadion de año ");

            etPrimerApellido.requestFocus();
            tilPrimerApellido.setError("El apellido es obligatorio");
            //etPrimerApellido.setError("El apellido es obligatorio");
            correcto = false;

        }
        if (etFechaNacimiento.getText() != null && etFechaNacimiento.getText().toString().isEmpty()) {
            etFechaNacimiento.requestFocus();
            //etFechaNacimiento.setError("La fecha de nacimiento es obligatorio");
            tilFechaNacimeinto.setError("La fecha de nacimiento es obligatorio");
            correcto = false;
        }

        return correcto;
    }

  /*  private void insertarImagen() {

        StorageReference imagenReference = storageReference.child(Rutas.PERFIL + "/" + jugador.getCedula());
        Log.d(Rutas.TAG, "El valor de la cedula es ****************** " + imagenReference.getPath());
        if (selectedImage==null) {
            tilCedulaRecuperar.setError("La imagen no se encuentra cargada ");
        } else {
            UploadTask uploadTask = imagenReference.putFile(selectedImage);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("PULPOLOG ", "error al cagar la imagen del torneo " + "EquipoActivity", e);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("PULPOLOG ", "imagen cargada" + "EquipoActivity");
                }
            });

        }

    }*/

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

    private void navIrCargarCedula() {
      //  tipo = Rutas.CEDULAS;
        Log.d(Rutas.TAG, "El valor de la cedula es=== " + etCedula.getText().toString());
        if (etCedula.getText() != null && etCedula.getText().toString().isEmpty()) {
            tilCedulaRecuperar.setError("Debe guardar previamente una cedula");
        } else {
            Intent intent = new Intent(this, CargarImagenActivity.class);
            intent.putExtra("paramTipoImagen", Rutas.CEDULAS);
            intent.putExtra("paramNombreImagen", jugador.getImagenCedula());
            startActivity(intent);
        }


    }

    private void navIrCargarFotoPerfil() {
        Log.d(Rutas.TAG, "El valor de la cedula es=== " + etCedula.getText().toString());
        if (etCedula.getText() != null && etCedula.getText().toString().isEmpty()) {
            tilCedulaRecuperar.setError("Debe guardar previamente una cedula");
        } else {
            Intent intent = new Intent(this, CargarImagenActivity.class);
            intent.putExtra("paramTipoImagen", Rutas.PERFIL);
            intent.putExtra("paramNombreImagen", jugador.getImagenPerfil());


          //  startActivity(intent);
            startActivityForResult(intent, ACTIVITY_CARGAR_IMAGEN);
        }
    }





    private void recuperarImg() {
        if (jugador!=null){
            if(jugador.getImagenPerfil()!=null&&jugador.getCedula()!=null) {
                imageReference = storageReference.child(jugador.getCedula()).child(jugador.getImagenPerfil());
                Log.d(Rutas.TAG, "el jugador  es  ++++" + jugador.toString());

                GlideApp.with(getApplicationContext())
                    .load(imageReference)
                    .circleCrop().error(R.drawable.ic_person_outline_24px)
                    .into(imagenE);

            }
        }

    }


  /*  @Override
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

                case GALLERY_REQUEST_ID:
                    //data.getData returns the content URI for the selected Image
                    selectedImage = data.getData();

                    btnCargarCedula.setImageURI(selectedImage);
                    //    Log.d("PULPOLOG", "Toma el valor del URI" + imagenE.toString());
                    break;
            }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == ACTIVITY_CARGAR_IMAGEN) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK

                // get String data from Intent
                boolean cargoImagen = data.getBooleanExtra("cargoImagen",false);

                if(cargoImagen){
                  //  recargarImg();
                    recuperarImg();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu1, menu);

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
        if (id == R.id.btnANuevoJugador) {
            crearJugador();
            return valor;
        } else if (id == R.id.btnAprobar) {
            Toast.makeText(this, "Se presiono el boton de Aprobar ", Toast.LENGTH_LONG).show();
            return valor;

        }

        if (id == R.id.mnuAdicional) {
            limpiarComponentes();
        }
        return valor;

        //return super.onOptionsItemSelected(item);
    }


    private void escucharJugador() {
        //1.-Referencia al arbol
        DatabaseReference refJugador = database.getReference(Rutas.CEDULAS);
        //2.-Crear el listener
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("PULPOLOG", "Se agrega Jugador RegistroJugadorActivity" + dataSnapshot.getKey());
                boolean repetido = false;
                for (Jugador jugador : jugadores) {
                    //COMPARAR CONTRA EL ID Para ver si es repetido
                    if (jugador.getCedula().equals(dataSnapshot.getKey())) {
                        repetido = true;
                    }
                }
                if (!repetido) {
                    //Se recupera el objeto completo
                    Jugador jugador = dataSnapshot.getValue(Jugador.class);
                    jugadores.add(jugador);

                    jadp.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d("PULPOLOG", "Se borra torneo ListaTorneosActivity " + dataSnapshot.getKey());
                int posicionRepetido = -1;
                for (int i = 0; i < jugadores.size(); i++) {
                    //COMPARAR POR EL ID
                    if (jugadores.get(i).getCedula().equals(dataSnapshot.getKey())) {
                        posicionRepetido = i;
                    }
                }
                if (posicionRepetido != -1) {
                    jugadores.remove(posicionRepetido);
                    jadp.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        //Se descomenta el retorno del paso 3.-del listener
        refJugador.addChildEventListener(childEventListener);
    }


}
