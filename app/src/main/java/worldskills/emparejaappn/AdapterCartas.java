package worldskills.emparejaappn;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class AdapterCartas extends BaseAdapter {
    private ArrayList<ItemCarta> cartas;
    private int layoutItem;
    private Context context;
    private final int[] FIGURAS={
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,
            R.drawable.a6,
            R.drawable.a7,
            R.drawable.a8

    };

    public AdapterCartas(ArrayList<ItemCarta> cartas, int layoutItem, Context context) {
        this.cartas = cartas;
        this.layoutItem = layoutItem;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cartas.size();
    }

    @Override
    public Object getItem(int position) {
        return cartas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row=convertView;
        Holder holder=new Holder();

        if(row==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(layoutItem,null);

            holder.figura=row.findViewById(R.id.figura);
            holder.tapar=row.findViewById(R.id.tapar);

            row.setTag(holder);

        }else{
            holder=(Holder)row.getTag();

        }
        ItemCarta carta=cartas.get(position);

        holder.figura.setImageResource(FIGURAS[carta.getNumero()]);

        holder.tapar.setBackgroundResource(carta.getFondoTapar());

        return row;
    }
    public class Holder{
        ImageView figura, tapar;
    }
}
