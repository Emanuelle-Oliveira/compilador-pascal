package sintatico;

import lexico.Classe;
import lexico.Lexico;
import lexico.Token;

public class Sintatico {

    private String nomeArquivo;
    private Lexico lexico;
    private Token token;

    public Sintatico(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        lexico = new Lexico(nomeArquivo);
    }

    public void analisar() {
        System.out.println("Analisando " + nomeArquivo);
        token = lexico.nextToken();
        programa();
    }

    // <programa> ::= program <id> {A01} ; <corpo> • {A45}
    private void programa() {
        if (token.getClasse() == Classe.palavraReservada && 
            token.getValor().getValorTexto().equals("program")) {
            token = lexico.nextToken();

            if(token.getClasse() == Classe.identificador) {
                token = lexico.nextToken(); 
                //{A01}
                if (token.getClasse() == Classe.pontoEVirgula) {
                    token = lexico.nextToken();
                    corpo();

                    if (token.getClasse() == Classe.ponto) {
                        token = lexico.nextToken();
                        //{A45}
                    } else {
                        System.err.println(token.getLinha() + ", " + token.getColuna() + "(.) Ponto final esperado no final do programa.");
                    }
                } else {
                    System.err.println(token.getLinha() + ", " + token.getColuna() + "(;) Ponto e vírgula esperado no nome do programa.");
                }
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() + "Nome do program principal esperado.");
            }
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() + "Palavra reservada 'program' esperada no início do programa principal.");
        }
    }

    // <corpo> ::= <declara> <rotina> {A44} begin <sentencas> end {A46}
    private void corpo() {
        declara();
        // rotina();
        // {44}

        if (token.getClasse() == Classe.palavraReservada && token.getValor().getValorTexto().equals("begin")) {
            token = lexico.nextToken();
            //sentencas();
            if(token.getClasse() == Classe.palavraReservada && token.getValor().getValorTexto().equals("end")) {
                token = lexico.nextToken();
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() + "Palavra reservada 'end' esperada no final.");
            }
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() + "Palavra reservada 'begin' esperada no início.");
        }
    }

    private void declara() {

    }
}
