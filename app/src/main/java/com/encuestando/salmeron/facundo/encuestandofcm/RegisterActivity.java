package com.encuestando.salmeron.facundo.encuestandofcm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Facundo Salmerón on 14/6/2018.
 */


public class RegisterActivity extends AppCompatActivity implements HttpAsyncTaskInterface {

    private TextInputLayout fechaNacimiento;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private CardView boton_volver;
    private CardView boton_guardar;
    private CheckBox tipoUsuario_checkbox;
    private TextInputLayout nombre_usuario;
    private TextInputLayout password;
    private TextInputLayout password2;
    private TextInputLayout email;
    private TextInputLayout codigoValidacion;
    private TextInputLayout name;
    private TextInputLayout surname;
    private TextInputLayout dni;
    private TextView errores;
    private RadioGroup sexo_radioGroup;
    private RadioButton sexo_radioButton;
    private Map<Integer, String> erroresRegister = null;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        toolbar = findViewById(R.id.registro_titulo);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(view -> {
            RegisterActivity.this.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        errores = findViewById(R.id.errores_registro);
        errores.setVisibility(View.GONE);
        tipoUsuario_checkbox = findViewById(R.id.checkbox_tipo_usuario);
        fechaNacimiento = findViewById(R.id.fecha_nacimiento_text);
        nombre_usuario = findViewById(R.id.nombre_register_text);
        name = findViewById(R.id.name_register_text);
        surname = findViewById(R.id.apellido_register_text);
        dni = findViewById(R.id.dni_register_text);
        password = findViewById(R.id.password1_register_text);
        password2 = findViewById(R.id.password2_register_text);
        email = findViewById(R.id.mail_register_text);
        codigoValidacion = findViewById(R.id.tipo_usuario_validator);
        codigoValidacion.setVisibility(View.GONE);
        sexo_radioGroup = findViewById(R.id.sexo_radio_group);
        sexo_radioGroup.setOnCheckedChangeListener((radioGroup, i) -> sexo_radioButton = findViewById(i));
        fechaNacimiento.getEditText().setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                Calendar cal = Calendar.getInstance();
                int year, month, day;
                if (fechaNacimiento.getEditText().getText().toString().isEmpty()){
                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    day = cal.get(Calendar.DAY_OF_MONTH);
                } else {
                    year = Integer.parseInt(fechaNacimiento.getEditText().getText().toString().substring(6,10));
                    month = Integer.parseInt(fechaNacimiento.getEditText().getText().toString().substring(3,5)) -1;
                    day = Integer.parseInt(fechaNacimiento.getEditText().getText().toString().substring(0,2));
                }
                DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        fechaNacimiento.getEditText().setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year, month, day;
            if (fechaNacimiento.getEditText().getText().toString().isEmpty()){
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
            } else {
                year = Integer.parseInt(fechaNacimiento.getEditText().getText().toString().substring(6,10));
                month = Integer.parseInt(fechaNacimiento.getEditText().getText().toString().substring(3,5)) - 1;
                day = Integer.parseInt(fechaNacimiento.getEditText().getText().toString().substring(0,2));
            }
            DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this,
                    android.R.style.Theme_Holo_Dialog_MinWidth,
                    mDateSetListener,
                    year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        mDateSetListener = (datePicker, year, month, day) -> {
            month += 1;
            String date = "";

            if (day < 10)
                date += "0" + day + "/";
            else
                date += day + "/";

            if (month < 10)
                date += "0" + month;
            else
                date += month;
            date += "/" + year;
            fechaNacimiento.getEditText().setText(date);
        };

        tipoUsuario_checkbox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                codigoValidacion.setVisibility(View.VISIBLE);
            } else {
                codigoValidacion.setVisibility(View.GONE);
            }
        });

        boton_volver = findViewById(R.id.volver_button);
        boton_volver.setOnClickListener(view -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
            alertDialog.setTitle("Atención");
            alertDialog.setIcon(R.drawable.ic_action_error);
            alertDialog.setMessage("Si sale de la pantalla se perderán todos los datos ingresados!\n¿Desea salir?");
            alertDialog.setNegativeButton("Aceptar", (dialog, which) -> {
                dialog.cancel();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            });
            alertDialog.setPositiveButton("Cancelar", (dialog, which) -> dialog.cancel());
            alertDialog.show();
        });

        boton_guardar = findViewById(R.id.guardar_button);
        boton_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre_usuario.setError(null);
                password.setError(null);
                codigoValidacion.setError(null);
                email.setError(null);
                dni.setError(null);
                final String nombre = nombre_usuario.getEditText().getText().toString();
                final String doc = dni.getEditText().getText().toString();
                final String nameUser = name.getEditText().getText().toString();
                final String surnameUser = surname.getEditText().getText().toString();
                final String contrasenia1 = password.getEditText().getText().toString();
                final String contrasenia2 = password2.getEditText().getText().toString();
                final String mail = email.getEditText().getText().toString();
                final String nacimiento = fechaNacimiento.getEditText().getText().toString();
                final String codigo = codigoValidacion.getEditText().getText().toString();
                errores.setTextColor(getResources().getColor(R.color.colorAccent));
                if (doc.isEmpty() || nameUser.isEmpty() || surnameUser.isEmpty() || nombre.isEmpty() || contrasenia1.isEmpty() || contrasenia2.isEmpty() || mail.isEmpty() || nacimiento.isEmpty() || (tipoUsuario_checkbox.isChecked() && codigo.isEmpty()) || (sexo_radioButton == null)) {
                    errores.setText("Debe completar todos los campos!");
                    errores.setVisibility(View.VISIBLE);
                } else if (!contrasenia1.equals(contrasenia2)) {
                    errores.setText("Las contraseñas no coinciden. Revisar!");
                    errores.setVisibility(View.VISIBLE);
                } else {
                    new Thread(() -> runOnUiThread(() -> {
                        errores.setVisibility(View.GONE);
                        String sexo = sexo_radioButton.getHint().toString().equals("Masculino") ? "1" : "2";
                        String tipoUsuario = tipoUsuario_checkbox.isChecked() ? "1" : "2";

                        ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                        progressDialog.setTitle("Creando usuario");
                        progressDialog.setMessage("Espere por favor...");
                        progressDialog.setIndeterminate(false);
                        progressDialog.setCancelable(false);
                        progressDialog.setInverseBackgroundForced(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setCancelable(true);
                        progressDialog.show();

                        HttpCall httpCall = new HttpCall();
                        httpCall.setMethodType(HttpCall.GET);
                        String url = getResources().getString(R.string.urlWS) + "/usuarios/saveUser";
                        httpCall.setUrl(url);
                        HashMap<String,String> params = new HashMap<>();
                        params.put("nombre", nameUser);
                        params.put("apellido", surnameUser);
                        params.put("dni", doc);
                        params.put("nombreUsuario", nombre);
                        params.put("password", contrasenia1);
                        params.put("fechaNacimiento", nacimiento);
                        params.put("mail", mail);
                        params.put("activo", "1");
                        params.put("sexo", sexo);
                        params.put("tipoUsuario", tipoUsuario);
                        params.put("validar", codigo);
                        httpCall.setParams(params);
                        new HttpRequest(WebServiceEnum.REGISTER_USER.getCodigo(), RegisterActivity.this){
                            @Override
                            public void onResponse(String response) {
                                super.onResponse(response);
                                progressDialog.dismiss();
                                if (erroresRegister != null && erroresRegister.size() < 1){
                                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this, AlertDialog.THEME_HOLO_DARK);
                                    alertDialog.setTitle("Éxito");
                                    alertDialog.setCancelable(false);
                                    alertDialog.setIcon(R.drawable.ic_validar_usuario);
                                    alertDialog.setMessage("El usuario ha sido registrado correctamente!");
                                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            RegisterActivity.this.finish();
                                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                        }
                                    });
                                    errores.setVisibility(View.GONE);
                                    alertDialog.show();

                                } else if (erroresRegister == null){
                                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this, AlertDialog.THEME_HOLO_DARK);
                                    alertDialog.setTitle("Error al Registrar");
                                    alertDialog.setMessage("Verifique su conexión a Internet! \n\nSi el problema persiste se trata de un error interno en la base de datos.");
                                    alertDialog.setIcon(R.drawable.ic_action_error);
                                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    errores.setVisibility(View.GONE);
                                    alertDialog.show();
                                } else{
                                    insertarErrores(erroresRegister);
                                }
                            }
                        }.execute(httpCall);
                    })).start();

                }

            }
        });

        if (savedInstanceState != null){
            String nombreInstance = savedInstanceState.getString("nombreInstance");
            String apellidoInstance = savedInstanceState.getString("apellidoInstance");
            String dniInstance = savedInstanceState.getString("dniInstance");
            String nombreUsuarioInstance = savedInstanceState.getString("nombreUsuario");
            String contraseniaUsuarioInstance = savedInstanceState.getString("contraseniaUsuario");
            String contraseniaUsuario2Instance = savedInstanceState.getString("contraseniaUsuario2");
            String emailInstance = savedInstanceState.getString("email");
            String nacimientoInstance = savedInstanceState.getString("nacimiento");
            String codigoInstance = savedInstanceState.getString("codigo");
            if (nombreInstance != null){
                name.getEditText().setText(nombreInstance);
            }
            if (apellidoInstance != null){
                surname.getEditText().setText(apellidoInstance);
            }
            if (dniInstance != null){
                dni.getEditText().setText(dniInstance);
            }
            if (nombreUsuarioInstance != null){
                nombre_usuario.getEditText().setText(nombreUsuarioInstance);
            }
            if (contraseniaUsuarioInstance != null){
                password.getEditText().setText(contraseniaUsuarioInstance);
            }
            if (contraseniaUsuario2Instance != null){
                password2.getEditText().setText(contraseniaUsuario2Instance);
            }
            if (emailInstance != null){
                email.getEditText().setText(emailInstance);
            }
            if (nacimientoInstance != null){
                fechaNacimiento.getEditText().setText(nacimientoInstance);
            }
            if (codigoInstance != null){
                codigoValidacion.getEditText().setText(codigoInstance);
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("nombreInstance", name.getEditText().getText().toString());
        outState.putString("apellidoInstance", surname.getEditText().getText().toString());
        outState.putString("dniInstance", dni.getEditText().getText().toString());
        outState.putString("nombreUsuario", nombre_usuario.getEditText().getText().toString());
        outState.putString("contraseniaUsuario", password.getEditText().getText().toString());
        outState.putString("contraseniaUsuario2", password2.getEditText().getText().toString());
        outState.putString("email", email.getEditText().getText().toString());
        outState.putString("nacimiento", fechaNacimiento.getEditText().getText().toString());
        outState.putString("codigo", codigoValidacion.getEditText().getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
        alertDialog.setTitle("Atención");
        alertDialog.setIcon(R.drawable.ic_action_error);
        alertDialog.setMessage("Si sale de la pantalla se perderán todos los datos ingresados!\n ¿Desea salir?");
        alertDialog.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        alertDialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void insertarErrores(Map<Integer, String> mapa){
        Integer numError = 0;
        for (Integer key : mapa.keySet()){
            numError = key;
        }

        if (numError == ErrorRegisterEnum.ERROR_CODIGO_VALIDACION.getCodigo()){
            codigoValidacion.setError(mapa.get(numError));
        } else if (numError == ErrorRegisterEnum.ERROR_MAIL_REPETIDO.getCodigo()){
            email.setError(mapa.get(numError));
        } else if (numError == ErrorRegisterEnum.ERROR_PASSWORD_MIN.getCodigo() || numError == ErrorRegisterEnum.ERROR_PASSWORD_MAX.getCodigo()){
            password.setError(mapa.get(numError));
        } else if (numError == ErrorRegisterEnum.ERROR_DNI_REPETIDO.getCodigo()){
            dni.setError(mapa.get(numError));
        } else {
            nombre_usuario.setError(mapa.get(numError));
        }
    }

    @Override
    public void registerUsuario(String result) {
        String registerUsuarioJSON = result;
        if (registerUsuarioJSON != null && !registerUsuarioJSON.isEmpty()) {
            erroresRegister = JSONConverterUtils.JSONUsuarioRegisterConverter(result);
        }
    }

}
