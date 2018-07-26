package worldskills.emparejaappn;

public class ItemCarta {

    private int numero;
    private int fondoTapar;

    public ItemCarta(int numero, int fondoTapar) {
        this.numero = numero;
        this.fondoTapar = fondoTapar;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getFondoTapar() {
        return fondoTapar;
    }

    public void setFondoTapar(int fondoTapar) {
        this.fondoTapar = fondoTapar;
    }
}
