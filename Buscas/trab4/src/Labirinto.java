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

    public List<Integer> buscaAprofundamentoIterativoAux(Node proximo, int limite, List<Node> visitado, int counter, List<Integer> indexPath) {
        visitado = new ArrayList<>();
        visitado.add(proximo);
        indexPath = new ArrayList<>();
        indexPath.add(proximo.index + 1);

        if (proximo.isDestino()) {
            return indexPath;
        } else {
            List<Node> directions = proximo.getDirections();
            for (Node i : directions) {
                if (i != null & !visitado.contains(i)) {
                    visitado.add(i);
                    indexPath.add(i.index + 1);
                    counter++;
                    if (counter == limite) {
                        buscaAprofundamentoIterativoAux(proximo, limite+1, visitado, 0, indexPath);
                        return indexPath;
                    }


                }


            }

        }
        return indexPath;
    }

    public void buscaGulosa() {
        List<Integer> indexPath = new ArrayList<>();
        boolean destino = false;
        List<Node> fila = new ArrayList<>();
        List<Node> visitados = new ArrayList<>();
        fila.add(listaNodes.get(0));
        visitados.add(listaNodes.get(0));
        indexPath.add(listaNodes.indexOf(listaNodes.get(0)));
        while (fila.size() > 0 && !destino) {
            Node atual = fila.get(0);
            Node proximo = fila.get(0);
            double distancia = Double.MAX_VALUE;
            List<Node> directions = atual.getDirections();
            for (Node i : directions) {
                if (i != null && !visitados.contains(i)) {
                    if (i.getDistanciaDestino() < distancia) {
                        distancia = i.getDistanciaDestino();
                        proximo = i;
                    }

                }
            }

            visitados.add(proximo);
            indexPath.add(listaNodes.indexOf(proximo));
            fila.add(proximo);
            fila.remove(0);
            destino = proximo.isDestino();

        }
        for (int i : indexPath) {
            if (i == 0) {
                System.out.print(i + 1 + "->");
            } else if (i != indexPath.get(indexPath.size() - 1)) {
                System.out.print(i + 1 + "->");
            } else {
                System.out.println(i + 1);
            }

        }


    }

    public void buscaAestrela() {
        List<Integer> visitados = new ArrayList<>();
        List<Double[]> possibilidades = new ArrayList<>();
        int destino = 0;

        Double[] lista = new Double[3]; // guarda passos dados, distacia euleriana e index do pai
        lista[0] = 0.0;
        lista[1] = listaNodes.get(0).getDistanciaDestino();
        lista[2] = -1.0;
        possibilidades.add(lista);
        //inicializa
        for (double i = 1.0; i < width * height; i++) {
            lista = new Double[3];
            lista[0] = Double.MAX_VALUE;
            lista[1] = Double.MAX_VALUE;
            lista[2] = Double.MAX_VALUE;
            possibilidades.add(lista);
        }

        while (true) {
            int menor = -1; // olha
            for (int i = 0; i < width * height; i++) {
                if (possibilidades.get(i)[1] != Double.MAX_VALUE && !visitados.contains(i)) {
                    if (menor == -1) {
                        menor = i;
                        continue;
                    }
                    double soma_1 = possibilidades.get(menor)[0] + possibilidades.get(menor)[1];
                    double soma_2 = possibilidades.get(i)[0] + possibilidades.get(i)[1];
                    if (soma_2 < soma_1 || (soma_1 == soma_2 && possibilidades.get(i)[1] < possibilidades.get(menor)[1])) {
                        menor = i;
                    }
                }
            }

            visitados.add(menor);

            Node atual = listaNodes.get(menor);
            if (atual.isDestino()) {
                destino = atual.index;
                break;
            }
            List<Node> directions = atual.getDirections();

            for (Node n : directions) {
                if (n != null && !visitados.contains(n.index)) {
                    possibilidades.get(n.index)[0] = possibilidades.get(menor)[0] + 1;
                    possibilidades.get(n.index)[1] = n.getDistanciaDestino();
                    possibilidades.get(n.index)[2] = Double.valueOf(atual.index);
                }
            }
        }

        List<Integer> caminho = new ArrayList<>();
        int pai = width * height - 1;
        caminho.add(width * height);
        while (true) {
            pai = possibilidades.get(pai)[2].intValue();
            caminho.add(pai + 1);
            if (possibilidades.get(pai)[0] == 0.0) {
                break;
            }
        }

        Collections.reverse(caminho);
        for (int i : caminho) {
            if (i == 0) {
                System.out.print(i + "->");
            } else if (i != caminho.get(caminho.size() - 1)) {
                System.out.print(i + "->");
            } else {
                System.out.println(i);
            }
        }
    }

    public void buscaAestrelaIterativo() {
        for(double x = 1.0; x < height * width; x++){
            List<Integer> visitados = new ArrayList<>();
            List<Double[]> possibilidades = new ArrayList<>();
            int destino = 0;

            Double[] lista = new Double[3]; // guarda passos dados, distacia euleriana e index do pai
            lista[0] = 0.0;
            lista[1] = listaNodes.get(0).getDistanciaDestino();
            lista[2] = -1.0;
            possibilidades.add(lista);
            //inicializa
            for (double i = 1.0; i < width * height; i++) {
                lista = new Double[3];
                lista[0] = Double.MAX_VALUE;
                lista[1] = Double.MAX_VALUE;
                lista[2] = Double.MAX_VALUE;
                possibilidades.add(lista);
            }

            int menor;
            while (true) {
                menor = -1;
                for (int i = 0; i < width * height; i++) {
                    if (possibilidades.get(i)[1] != Double.MAX_VALUE && !visitados.contains(i)) {
                        if (menor == -1) {
                            menor = i;
                            continue;
                        }
                        double soma_1 = possibilidades.get(menor)[0] + possibilidades.get(menor)[1];
                        double soma_2 = possibilidades.get(i)[0] + possibilidades.get(i)[1];
                        if (soma_2 < soma_1 || (soma_1 == soma_2 && possibilidades.get(i)[1] < possibilidades.get(menor)[1])) {
                            menor = i;
                        }
                    }
                }

                visitados.add(menor);

                Node atual = listaNodes.get(menor);
                if (atual.isDestino() || possibilidades.get(menor)[0] == x) {
                    destino = atual.index;
                    break;
                }
                List<Node> directions = atual.getDirections();

                for (Node n : directions) {
                    if (n != null && !visitados.contains(n.index)) {
                        possibilidades.get(n.index)[0] = possibilidades.get(menor)[0] + 1;
                        possibilidades.get(n.index)[1] = n.getDistanciaDestino();
                        possibilidades.get(n.index)[2] = Double.valueOf(atual.index);
                    }
                }
            }

            List<Integer> caminho = new ArrayList<>();
            int pai = menor;
            caminho.add(pai+1);
            while (true) {
                pai = possibilidades.get(pai)[2].intValue();
                caminho.add(pai + 1);
                if (possibilidades.get(pai)[0] == 0.0) {
                    break;
                }
            }

            Collections.reverse(caminho);
            for (int i : caminho) {
                if (i == 0) {
                    System.out.print(i + "->");
                } else if (i != caminho.get(caminho.size() - 1)) {
                    System.out.print(i + "->");
                } else {
                    System.out.println(i);
                }
            }
            if(listaNodes.get(menor).isDestino()){
                return;
            }
        }
    }


}