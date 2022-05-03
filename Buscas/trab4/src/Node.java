
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node {

    public final int index;
    private boolean origem = false;
    private boolean destino = false;
    private boolean visitado = false;
    private Node norte;
    private Node sul;
    private Node leste;
    private Node oeste;
    private final double distanciaDestino;

    public Node(int i, double distanciaDestino) {
        this.index = i;
        this.distanciaDestino = distanciaDestino;
    }

    public double getDistanciaDestino() {
        return distanciaDestino;
    }

    public void setVizinhos(Node norte, Node sul, Node leste, Node oeste) {

        this.norte = norte;
        this.sul = sul;
        this.leste = leste;
        this.oeste = oeste;
    }

    public List<Node> getDirections(){
        return Arrays.asList(norte,sul,leste,oeste);
    }

    public void setOrigem(boolean origem) {
        this.origem = origem;
    }

    public boolean isOrigem() {
        return origem;
    }

    public boolean isDestino() {
        return destino;
    }

    public void setDestino(boolean destino) {
        this.destino = destino;
    }

    public boolean isVisitado() {
        return visitado;
    }

    public void setVisitado(boolean visitado) {
        this.visitado = visitado;
    }

    public Node getNorte() {
        return norte;
    }

    public void setNorte(Node norte) {
        this.norte = norte;
    }

    public Node getSul() {
        return sul;
    }

    public void setSul(Node sul) {
        this.sul = sul;
    }

    public Node getLeste() {
        return leste;
    }

    public void setLeste(Node leste) {
        this.leste = leste;
    }

    public Node getOeste() {
        return oeste;
    }

    public void setOeste(Node oeste) {
        this.oeste = oeste;
    }
}