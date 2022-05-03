import java.util.*;

public class Labirinto {
    private final int width;
    private final int height;
    List<Node> listaNodes;


    public Labirinto(int width, int height) {
        this.width = width;
        this.height = height;
        listaNodes = new ArrayList<>();
        conectarNodes();
    }

    private void conectarNodes() {
        for (int i = 0; i < width * height; i++) {
            int x = width - i % width - 1;
            int y = height - (i / width) - 1;

            double distanciaDestino = Math.sqrt((x * x) + (y * y));

            listaNodes.add(new Node(i, distanciaDestino));

            System.out.println(i + 1 + " " + distanciaDestino);

        }

        for (int i = 0; i < width * height; i++) {
            if (i == 5)
                System.out.println();
            Node norte = getNode(i, -width);
            Node sul = getNode(i, +width);
            Node leste = getNode(i, +1);
            Node oeste = getNode(i, -1);

            listaNodes.get(i).setVizinhos(norte, sul, leste, oeste);
        }

        listaNodes.get(0).setOrigem(true);
        listaNodes.get(listaNodes.size() - 1).setDestino(true);
    }

    private Node getNode(int index, int mudanca) {
        if (index + mudanca < 0 || index + mudanca > listaNodes.size() - 1) return null;
        if (index % width == 0 && mudanca == -1) return null;
        if (index % width == width - 1 && mudanca == 1) return null;
        return listaNodes.get(index + mudanca);
    }

    public void criarParede(int n1, int n2) {
        Node node1 = listaNodes.get(n1);
        Node node2 = listaNodes.get(n2);

        if (node1.getNorte() != null && node1.getNorte().equals(node2)) {
            node1.setNorte(null);
            node2.setSul(null);
        } else if (node1.getSul() != null && node1.getSul().equals(node2)) {
            node1.setSul(null);
            node2.setNorte(null);
        } else if (node1.getLeste() != null && node1.getLeste().equals(node2)) {
            node1.setLeste(null);
            node2.setOeste(null);
        } else if (node1.getOeste() != null && node1.getOeste().equals(node2)) {
            node1.setOeste(null);
            node2.setLeste(null);
        }
    }

    public void desenharLabirinto() {
        int i = 1;
        for (Node node : listaNodes) {

            if (node.getOeste() == null) System.out.print("|");
            else System.out.print(" ");

            if (node.getNorte() == null) System.out.print("¯");
            else System.out.print(" ");

            if (i == width) {
                i = 0;
                System.out.println("|");
            }
            i++;
        }
        for (int j = 0; j < width; j++) {
            System.out.print("¯¯");
        }
    }

    // Buscas

    public void buscaLargura() {
        List<Integer> indexList = new ArrayList<>();
        boolean foundDestino = false;
        List<Node> marcados = new ArrayList<>();
        List<Node> fila = new ArrayList<>();
        Node atual = listaNodes.get(0);
        marcados.add(atual);
        indexList.add(listaNodes.indexOf(atual) + 1);
        fila.add(atual);
        while (fila.size() > 0 && !foundDestino) {
            Node visitado = fila.get(0);
            List<Node> directions = visitado.getDirections();
            for (Node i : directions) {
                if (i != null && !marcados.contains(i)) {
                    marcados.add(i);
                    indexList.add(listaNodes.indexOf(i) + 1);
                    fila.add(i);
                    foundDestino = i.isDestino();
                    break;
                }
            }
            fila.remove(0);


        }
        for (int i : indexList) {
            if (i == 0) {
                System.out.print(i + "->");
            } else if (i != indexList.get(indexList.size() - 1)) {
                System.out.print(i + "->");
            } else {
                System.out.println(i);
            }

        }
    }

    public void buscaProfundidade() {
        List<Node> pilha = new ArrayList<>();
        Node atual = listaNodes.get(0);
        List<Integer> indexPath = new ArrayList<>();
        List<Node> visitado = new ArrayList<>();
        indexPath.add(listaNodes.indexOf(atual) + 1);
        visitado.add(atual);
        pilha.add(atual);
        List<Integer> indexList = buscaProfundidadeAux(indexPath, visitado, pilha);
        for (int i : indexList) {
            if (i == 0) {
                System.out.print(i + "->");
            } else if (i != indexList.get(indexList.size() - 1)) {
                System.out.print(i + "->");
            } else {
                System.out.println(i);
            }
        }
    }

    public List<Integer> buscaProfundidadeAux(List<Integer> indexPath, List<Node> visitado, List<Node> pilha) {
        Node proximo = pilha.get(pilha.size() - 1);
        if (proximo.isDestino()) {
            return indexPath;
        } else {
            while (pilha.size() > 0) {
                List<Node> directions = proximo.getDirections();
                for (Node i : directions) {
                    if (!visitado.contains(i) && i != null) {
                        visitado.add(i);
                        pilha.add(i);
                        indexPath.add(listaNodes.indexOf(i) + 1);
                        buscaProfundidadeAux(indexPath, visitado, pilha);
                        if (listaNodes.get(indexPath.get(indexPath.size() - 1) - 1).isDestino()) {
                            return indexPath;
                        }
                    }
                }
                pilha.remove(pilha.size() - 1);
            }
            return indexPath;
        }
    }

    public void buscaProfundidadeLimitada(int limite) {
        Node atual = listaNodes.get(0);
        List<Node> pilha = new ArrayList<>();
        List<Integer> indexPath = new ArrayList<>();
        List<Node> visitado = new ArrayList<>();
        indexPath.add(listaNodes.indexOf(atual) + 1);
        visitado.add(atual);
        int counter = 1;
        List<Integer> indexList = buscaProfundidadeLimitadaAux(indexPath, visitado, limite, counter, pilha);
        for (int i : indexList) {
            if (i == 0) {
                System.out.print(i + "->");
            } else if (i != indexList.get(indexList.size() - 1)) {
                System.out.print(i + "->");
            } else {
                System.out.println(i);
            }
        }
    }

    public List<Integer> buscaProfundidadeLimitadaAux(List<Integer> indexPath, List<Node> visitados, int limite, int counter, List<Node> pilha) {
        Node proximo = pilha.get(pilha.size() - 1);
        if (proximo.isDestino()) {
            return indexPath;
        } else {
            while (pilha.size() > 0) {
                if (counter != limite) {
                    List<Node> directions = proximo.getDirections();
                    for (Node i : directions) {
                        if (i != null & !visitados.contains(i)) {
                            visitados.add(i);
                            pilha.add(i);
                            indexPath.add(listaNodes.indexOf(i) + 1);
                            counter++;
                            buscaProfundidadeLimitadaAux(indexPath, visitados, limite, counter, pilha);
                            break;
                        }

                    }
                    pilha.remove(pilha.size() - 1);
                }
            }
            return indexPath;
        }

    }

    public void buscaAprofundamentoIterativo() {
        Node atual = listaNodes.get(0);
        int i = 1;
        while(true) {
            atual = listaNodes.get(0);
            List<Node> visitado = new ArrayList<>();
            visitado.add(atual);
            List<Integer> indexPath = new ArrayList<>();
            indexPath.add(atual.index + 1);
            for(int j =0; j<i;j++){
                List<Node> directions = atual.getDirections();
                for (Node n : directions){
                    if(n!=null && !visitado.contains(n)){
                        visitado.add(n);
                        indexPath.add(n.index + 1);
                        atual = n;
                        break;
                    }
                }
            }
            for (int j : indexPath) {
                if (j == 0) {
                    System.out.print(j + "->");
                } else if (j != indexPath.get(indexPath.size() - 1)) {
                    System.out.print(j + "->");
                } else {
                    System.out.println(j);
                }
            }
            if(atual.isDestino()){
                break;
            }
            i++;


//            List<Integer> indexList = buscaAprofundamentoIterativoAux(atual, i + 1, visitado, i, indexPath);


        }
    }



}