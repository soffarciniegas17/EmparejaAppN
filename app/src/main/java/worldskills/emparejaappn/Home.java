package worldskills.emparejaappn;

import android.app.Dialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        Cursor cursor = db.cargarDatos();

    }
}
