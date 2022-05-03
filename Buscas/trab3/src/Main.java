public class Main {
    public static void main(String[] args) {

        Labirinto labirinto = new Labirinto(5, 5);

//        labirinto.criarParede(0,1);
        labirinto.criarParede(5,6);
        labirinto.criarParede(10,11);
        labirinto.criarParede(15,16);
        labirinto.criarParede(6,7);
        labirinto.criarParede(11,12);
        labirinto.criarParede(16,17);
        labirinto.criarParede(21,22);
        labirinto.criarParede(2,3);
        labirinto.criarParede(7,8);
        labirinto.criarParede(12,13);
        labirinto.criarParede(17,18);
        labirinto.criarParede(18,23);

        labirinto.desenharLabirinto();
        System.out.println("");
//        labirinto.buscaLargura();
//        labirinto.buscaProfundidade();
//        labirinto.buscaProfundidadeLimitada(5);
        //labirinto.buscaAprofundamentoIterativo();
    }

}
