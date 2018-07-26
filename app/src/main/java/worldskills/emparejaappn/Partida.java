package worldskills.emparejaappn;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Partida extends AppCompatActivity {

    private ArrayList<ItemCarta> cartas;
    private GridView gridView;
    private int[] numeros;
    private AdapterCartas adapter;

    private int puntos1, puntos2, position1, position2, capacidad;
    private TextView viewJugador1, viewJugador2, viewScore1, viewScore2;
    private View view1, view2;
    private String nom1, nom2;
    private boolean cambiaCarta, turnoJugador;
    private Animation voltear, girarDesaparecer;
    private Dialog mensajeFinal, redes;
    private Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

        findViews();
        Bundle datos=getIntent().getExtras();
        try{
            capacidad=Integer.parseInt(datos.getString("dificultad"));

        }catch (Exception e){}
        capacidad=8;
        puntos1=0;
        puntos2=0;


        numeros=new int[capacidad];
        for (int i=0; i<numeros.length;i++){
            numeros[i]=-1;
        }
        animaciones();
        rellenarAzarNumeros(capacidad/2);
        iniciaJuego();

        mensajeFinal=new Dialog(this);
        mensajeFinal.setContentView(R.layout.mensaje_final);
        mensajeFinal.setCanceledOnTouchOutside(false);
        mensajeFinal.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }
    public void animaciones(){
        voltear= AnimationUtils.loadAnimation(this, R.anim.voltear);
        voltear.setFillAfter(true);

        girarDesaparecer=AnimationUtils.loadAnimation(this, R.anim.girar_desaparecer);
        girarDesaparecer.setFillAfter(true);
    }
    private int base=0;
    public int rellenarAzarNumeros(int parejas){
        int ve=0;

        for(int i=0; i<numeros.length;i++){
            if(numeros[i]==-1){
                ve=1;
            }
        }
        if (ve==0)return 0;

        Random a=new Random();
        int b=a.nextInt(numeros.length);

        if(numeros[b]==-1){
            numeros[b]=base;
            base++;
            if(base==parejas)base=0;
        }
        rellenarAzarNumeros(parejas);

        return 1;

    }
    public void iniciaJuego(){
        try{
            cartas=new ArrayList<>();
            adapter=new AdapterCartas(cartas,R.layout.item_layout,this);
            gridView.setAdapter(adapter);


            for(int i=0; i<capacidad; i++){
                cartas.add(new ItemCarta(numeros[i],R.drawable.fondo_tapar_carta));
            }
        }catch (Exception e){}
        adapter.notifyDataSetChanged();
        clickItems();
    }
    public void clickItems(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!cambiaCarta) {

                    if(view1 != null && view2!=null){
                        view1.clearAnimation();
                        view2.clearAnimation();
                    }

                    position1=position;
                    view1=view;

                    cartas.get(position1).setFondoTapar(android.R.color.transparent);

                    view1.startAnimation(voltear);
                    cambiaCarta=true;
                    adapter.notifyDataSetChanged();



                }else{
                    position2=position;
                    view2=view;

                    cartas.get(position2).setFondoTapar(android.R.color.transparent);
                    view1.clearAnimation();
                    view2.startAnimation(voltear);
                    cambiaCarta=false;
                    adapter.notifyDataSetChanged();
                    comprueba();


                }
            }
        });
    }
    public void comprueba(){
        gridView.setOnItemClickListener(null);
        new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if(cartas.get(position1).getNumero()==cartas.get(position2).getNumero()){

                    view1.setVisibility(View.INVISIBLE);
                    view2.setVisibility(View.INVISIBLE);

                    view1.startAnimation(girarDesaparecer);
                    view2.startAnimation(girarDesaparecer);

                    if(!turnoJugador){
                        puntos1+=100;


                    }else{
                        puntos2+=100;

                    }
                    finPartida();
                }else{


                    cartas.get(position1).setFondoTapar(R.drawable.fondo_tapar_carta);
                    cartas.get(position2).setFondoTapar(R.drawable.fondo_tapar_carta);

                    view1.startAnimation(voltear);

                    view2.startAnimation(voltear);
                    if(!turnoJugador){
                        puntos1-=1;
                    }else{
                        puntos2-=2;
                    }



                }


                adapter.notifyDataSetChanged();
                clickItems();
                estadoJugador();
            }
        }.start();
    }
    public void estadoJugador(){
        if(!turnoJugador){
            viewScore1.setText(puntos1+"");

            viewJugador2.setBackgroundResource(R.drawable.fondo_turno_verde);
            viewScore2.setBackgroundResource(R.drawable.fondo_turno_verde);

            viewJugador1.setBackgroundResource(R.drawable.fondo_turno_gris);
            viewScore1.setBackgroundResource(R.drawable.fondo_turno_gris);

            turnoJugador=true;
        }else{
            viewScore2.setText(puntos2+"");

            viewJugador1.setBackgroundResource(R.drawable.fondo_turno_verde);
            viewScore1.setBackgroundResource(R.drawable.fondo_turno_verde);

            viewJugador2.setBackgroundResource(R.drawable.fondo_turno_gris);
            viewScore2.setBackgroundResource(R.drawable.fondo_turno_gris);

            turnoJugador=false;
        }
    }
    public void finPartida(){
        int ve=0;

        for(int i=0; i<capacidad;i++){
            if(cartas.get(i).getFondoTapar()==R.drawable.fondo_tapar_carta){
                ve=1;
            }
        }
        if(ve==0){
            abrirDialogFinJuego();
            guardarPuntaje();
        }
    }
    public void abrirDialogFinJuego(){
        TextView viewGanador, viewScoreGanador, viewTiempoPartida;
        viewGanador=mensajeFinal.findViewById(R.id.view_ganador);
        viewScoreGanador=mensajeFinal.findViewById(R.id.view_score_ganador);
        viewTiempoPartida=mensajeFinal.findViewById(R.id.view_tiempo);

        if(modo.equalsIgnoreCase("2")){
            chronometer.stop();
             tiempo=(int)((SystemClock.elapsedRealtime()-chronometer.getBase())/1000);
            viewTiempoPartida.setText(tiempo+"");
        }else{
            viewScoreGanador.setText("no time");
        }

        if(puntos2<puntos1){
            viewGanador.setText(nom1);
            viewScoreGanador.setText(puntos1+"");
        }else{
            viewGanador.setText(nom2);
            viewScoreGanador.setText(puntos2+"");

        }
        ImageView btonHome, btonReplay, btonShared;

        mensajeFinal.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent intent=new Intent(getApplicationContext(), Partida.class);
                startActivity(intent);
            }
        });


        mensajeFinal.show();
    }
    public void botonesFinal(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.bton_inicio:
                intent=new Intent(this, Home.class);
                startActivity(intent);
                break;
            case R.id.bton_replay:
                intent=new Intent(this, Partida.class);
                startActivity(intent);
                break;
            case R.id.bton_compartir:

                break;
        }
    }


    public void findViews(){
        viewJugador1=findViewById(R.id.view_jugador1);
        viewJugador2=findViewById(R.id.view_jugador2);
        viewScore1=findViewById(R.id.view_score1);
        viewScore2=findViewById(R.id.view_score2);
        gridView=findViewById(R.id.gridview);
        chronometer=findViewById(R.id.view_cronometro);
    }
    private int tiempo=0;
    private String modo;
    public void guardarPuntaje(){
        RegistroBBDD db=new RegistroBBDD(this);

        db.guardarDatos(nom1,puntos1,tiempo,capacidad+"",modo);
        db.guardarDatos(nom2,puntos2,tiempo,capacidad+"",modo);
        Toast.makeText(getApplicationContext(),"EmparejaApp",Toast.LENGTH_SHORT).show();

    }


    public void onResume() {
        super.onResume();
        SharedPreferences guarda = PreferenceManager.getDefaultSharedPreferences(this);

        nom1 = guarda.getString("nick1", "JUGADOR1");
        nom2 = guarda.getString("nick2", "JUGADOR2");
        modo=guarda.getString("estado","1");

        Random randomB=new Random();

        turnoJugador=randomB.nextBoolean();
        estadoJugador();

        if(modo.equalsIgnoreCase("2")){
            chronometer.start();
        }
    }
    public void onPause(){
        super.onPause();
    }
}
