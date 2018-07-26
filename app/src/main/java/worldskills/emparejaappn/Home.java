package worldskills.emparejaappn;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Home extends AppCompatActivity {


    TextView t1, t2;
    String modo, dificultad;
    Dialog settings, names, dificult, scores;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //INIT
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);

        // SETTINGS
        settings = new Dialog(this);
        settings.setContentView(R.layout.dialog_settings);


        // NAMES
        names = new Dialog(this);
        names.setContentView(R.layout.dialog_names);
        names.setCanceledOnTouchOutside(false);
        names.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // DIFICULTAD

        dificult = new Dialog(this);
        dificult.setContentView(R.layout.dialog_dificultad);
        dificult.setCanceledOnTouchOutside(false);

        // PUNTAJES

        scores = new Dialog(this);
        scores.setContentView(R.layout.dialog_scores);

        ingresarNombres();
    }

    String nick1, nick2;
    int ingreso = 0;

    public void ingresarNombres() {
        TextView titulo = names.findViewById(R.id.textV);
        final EditText edit = names.findViewById(R.id.editT);
        final Button confirmar = names.findViewById(R.id.confirmar);

        if (ingreso == 1) {
            titulo.setText("JUGADOR 2");
            edit.setText("JUGADOR 2");
        }

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingreso == 0) {
                    nick1 = edit.getText().toString();

                    ingreso = 1;
                    if (nick1.equalsIgnoreCase("")) nick1 = "JUGADOR 1";
                    ingresarNombres();
                    t1.setText(nick1);
                } else {
                    nick2 = edit.getText().toString();
                    t2.setText(nick2);
                    names.dismiss();
                }
            }
        });

        names.show();
    }

    String puntos [];
    public void rellenar (){
        puntos= new String[4];

        RegistroBBDD db = new RegistroBBDD(this);
        Cursor cursor = db.cargarDatos(dificultad,modo);
        int i =0;
        try {
            if (cursor.moveToFirst()){
                do {
                    puntos[i]= cursor.getString(0)+ "\n" + cursor.getString(1);
                    i++;
                }while (cursor.moveToNext());
            }
        } catch (Exception e){
            for (int a=0; a<puntos.length; a++){
                puntos[i] =" VACIO  \n NO ONE";
            }
        }

    }

    public void mostrarPuntajes (View v){

        final ImageButton salir = scores.findViewById(R.id.salir);
        TextView p1, p2, p3, p4;
        p1= scores.findViewById(R.id.p1);
        p2= scores.findViewById(R.id.p2);
        p3= scores.findViewById(R.id.p3);
        p4= scores.findViewById(R.id.p4);

        Button botonp;


        switch (v.getId()){
            case R.id.bintentos:

                modo= "1";
                break;
            case R.id.btiempo:
                modo="2";
                break;
            case R.id.bfacil:
                dificultad = "8";
                break;
            case R.id.bmedio:
                dificultad= "12";
                break;
            case R.id.bdificil:
                dificultad="16";
                break;
        }

        for (int i =0; i<puntos.length; i++){
            puntos[i] = "VACIO \n NO ONE";
        }

        rellenar();

        p1.setText(puntos[0]);
        p2.setText(puntos[1]);
        p3.setText(puntos[2]);
        p4.setText(puntos[3]);

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scores.dismiss();
            }
        });

        scores.show();
    }
    String modalidad;
    public void configurar(View v){
        final Button tiempo, intentos;
        tiempo = settings.findViewById(R.id.tiempo);
        intentos = settings.findViewById(R.id.intento);

        tiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tiempo.setBackgroundResource(R.drawable.activado_boton);
                intentos.setBackgroundResource(R.drawable.boton_desactivado);
                modalidad = "2";
                retrasar();
                settings.dismiss();
            }
        });

        intentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiempo.setBackgroundResource(R.drawable.boton_desactivado);
                intentos.setBackgroundResource(R.drawable.activado_boton);
                modalidad = "1";
                retrasar();
                settings.dismiss();
            }
        });
    }

    public void retrasar (){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 300);
    }

    public void partida (View v){
        dificult.show();
        int juego=8;
        switch (v.getId()){
            case R.id.play:

                View view = findViewById(R.id.view);
                view.setVisibility(View.VISIBLE);
                break;
            case R.id.easy:
                juego =8;
                break;
            case R.id.medio:
                juego = 12;
                break;
            case R.id.duro:
                juego = 16;
                break;
        }

      Intent  i = new Intent(Home.this, Partida.class);
        i.putExtra("dificultad", juego);
        startActivity(i);
        dificult.dismiss();
        finish();
    }

    @Override
    protected void onPause() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("nick1", nick1);
        editor.putString("nick2", nick2);
        editor.putString("estado", modalidad);

        editor.apply();
        super.onPause();
    }

    @Override
    protected void onResume() {

        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);

        nick1= datos.getString("nick1", "JUGADOR 1");
        nick2 = datos.getString("nick2", "JUGADOR 2");
        modalidad = datos.getString("estado", modalidad );

        t1.setText(nick1);
        t2.setText(nick2);
        super.onResume();
    }
}
