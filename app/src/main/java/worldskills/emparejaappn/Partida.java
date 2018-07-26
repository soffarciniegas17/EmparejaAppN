package worldskills.emparejaappn;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

        findViews();
        capacidad=8;
        puntos1=0;
        puntos2=0;

        numeros=new int[capacidad];
        for (int i=0; i<numeros.length;i++){
            numeros[i]=-1;
        }
        rellenarAzarNumeros(capacidad/2);


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
    }
    public void clickItems(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!cambiaCarta) {

                    position1=position;
                    view1=view;

                    cartas.get(position1).setFondoTapar(android.R.color.transparent);

                    cambiaCarta=true;
                    adapter.notifyDataSetChanged();

                }else{
                    position2=position;
                    view2=view;

                    cartas.get(position1).setFondoTapar(android.R.color.transparent);

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

                    if(!turnoJugador){
                        puntos1+=100;


                    }else{
                        puntos2+=100;

                    }
                    finPartida();
                }else{

                    cartas.get(position1).setFondoTapar(R.drawable.fondo_tapar_carta);
                    cartas.get(position2).setFondoTapar(R.drawable.fondo_tapar_carta);

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
            view2.setBackgroundResource(R.drawable.fondo_turno_verde);

            viewJugador1.setBackgroundResource(R.drawable.fondo_turno_gris);
            view1.setBackgroundResource(R.drawable.fondo_turno_gris);

            turnoJugador=true;
        }else{
            viewScore2.setText(puntos2+"");

            viewJugador1.setBackgroundResource(R.drawable.fondo_turno_verde);
            view1.setBackgroundResource(R.drawable.fondo_turno_verde);

            viewJugador2.setBackgroundResource(R.drawable.fondo_turno_gris);
            view2.setBackgroundResource(R.drawable.fondo_turno_gris);

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
        if(ve==0)finish();
    }
    public void findViews(){
        viewJugador1=findViewById(R.id.view_jugador1);
        viewJugador2=findViewById(R.id.view_jugador2);
        viewScore1=findViewById(R.id.view_score1);
        viewScore2=findViewById(R.id.view_score2);
        gridView=findViewById(R.id.gridview);
    }

    public void onResume() {
        super.onResume();
        SharedPreferences guarda = PreferenceManager.getDefaultSharedPreferences(this);

        nom1 = guarda.getString("JUGADOR1", "JUGADOR1");
        nom2 = guarda.getString("JUGADOR2", "JUGADOR2");

        Random randomB=new Random();

        turnoJugador=randomB.nextBoolean();
        estadoJugador();
    }
    public void onPause(){
        super.onPause();
    }
}
