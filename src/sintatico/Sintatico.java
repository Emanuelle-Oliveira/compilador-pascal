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

    public Boolean ehDeterminadaPalavraReservada(String palavra) {
        return token.getClasse() == Classe.palavraReservada && token.getValor().getValorTexto().equals(palavra);
    }

    // <programa> ::= program <id> {A01} ; <corpo> • {A45}
    private void programa() {
        if (ehDeterminadaPalavraReservada("program")) {
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

        if (ehDeterminadaPalavraReservada("begin")) {
            token = lexico.nextToken();
            sentencas();
            if(ehDeterminadaPalavraReservada("end")) {
                token = lexico.nextToken();
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() + "Palavra reservada 'end' esperada no final.");
            }
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() + "Palavra reservada 'begin' esperada no início.");
        }
    }

    // <declara> ::= var <dvar> <mais_dc> | ε
    private void declara() {
        if (ehDeterminadaPalavraReservada("var")) {
            token = lexico.nextToken();
            dvar();
            mais_dc();
        }   
    }

    // <dvar> ::= <variaveis> : <tipo_var> {A02}
    private void dvar() {
        variaveis();
        if(token.getClasse() == Classe.doisPontos) {
            token = lexico.nextToken();
            tipo_var();
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() + "Ponto e vírgula esperado.");
        }
    }

    // <variaveis> ::= <id> {A03} <mais_var>
    private void variaveis() {
        if(token.getClasse() == Classe.identificador) {
            token = lexico.nextToken(); 
            mais_var();
        }
    }

    // <mais_var> ::=  ,  <variaveis> | ε
    private void mais_var() {
        if(token.getClasse() == Classe.virgula) {
            token = lexico.nextToken(); 
            variaveis();
        }
    }

    // <tipo_var> ::= integer
    private void tipo_var() {
        if(ehDeterminadaPalavraReservada("integer")) {
            token = lexico.nextToken(); 
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() + "Tipo integer esperado.");
        }
    }

    //<mais_dc> ::=  ; <cont_dc>
    private void mais_dc() {
        if(token.getClasse() == Classe.pontoEVirgula) {
            token = lexico.nextToken(); 
            cont_dc();
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() + "Ponto e vírgula esperado.");
        }
    }

    // <cont_dc> ::= <dvar> <mais_dc> | ε
    private void cont_dc() {
        if(token.getClasse() == Classe.identificador) {
            dvar();
            mais_dc();
        }
    }

    // <sentencas> ::= <comando> <mais_sentencas> 
    private void sentencas() {
        //comando()
        mais_sentencas();
    }

    // <mais_sentencas> ::= ; <cont_sentencas>
    private void mais_sentencas() {
        if(token.getClasse() == Classe.pontoEVirgula) {
            token = lexico.nextToken(); 
            cont_sentencas();
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() + "Ponto e vírgula esperado.");
        }
    }

    // <cont_sentencas> ::= <sentencas> | ε
    private void cont_sentencas() {
        sentencas();
    }

}
