package sintatico;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import lexico.Classe;
import lexico.Lexico;
import lexico.Token;

public class Sintatico {

    private String nomeArquivo;
    private Lexico lexico;
    private Token token;

    private TabelaSimbolos tabela = new TabelaSimbolos();
    private String rotulo = "";
    private int contRotulo = 1;
    private int offsetVariavel = 0;
    private String nomeArquivoSaida;
    private String caminhoArquivoSaida;
    private BufferedWriter bw;
    private FileWriter fw;
    private static final int TAMANHO_INTEIRO = 4;
    private List<String> variaveis = new ArrayList<>();
    private List<String> sectionData = new ArrayList<>();

    public Sintatico(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        lexico = new Lexico(nomeArquivo);

        nomeArquivoSaida = "queronemver.asm";
        caminhoArquivoSaida = Paths.get(nomeArquivoSaida).toAbsolutePath().toString();
        bw = null;
        fw = null;
        try {
            fw = new FileWriter(caminhoArquivoSaida, Charset.forName("UTF-8"));
            bw = new BufferedWriter(fw);
        } catch (Exception e) {
            System.err.println("Erro ao criar arquivo de saída");
        }
    }

    private void escreverCodigo(String instrucoes) {
        try {
            if (rotulo.isEmpty()) {
                bw.write(instrucoes + "\n");
            } else {
                bw.write(rotulo + ": " + instrucoes + "\n");
                rotulo = "";
            }
        } catch (IOException e) {
            System.err.println("Erro escrevendo no arquivo de saída");
        }
    }

    private String criarRotulo(String texto) {
        String retorno = "rotulo" + texto + contRotulo;
        contRotulo++;
        return retorno;
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
                //{A01}
                Registro registro = tabela.add(token.getValor().getValorTexto());
                offsetVariavel = 0;
                registro.setCategoria(Categoria.PROGRAMAPRINCIPAL);
                escreverCodigo("global main");
                escreverCodigo("extern printf");
                escreverCodigo("extern scanf\n");
                escreverCodigo("section .text ");
                rotulo = "main";
                escreverCodigo("\t; Entrada do programa");
                escreverCodigo("\tpush ebp");
                escreverCodigo("\tmov ebp, esp");
                System.out.println(tabela);
                //{A01}

                token = lexico.nextToken(); 

                if (token.getClasse() == Classe.pontoEVirgula) {
                    token = lexico.nextToken();
                    corpo();

                    if (token.getClasse() == Classe.ponto) {
                        token = lexico.nextToken();

                        //{A45}
                        escreverCodigo("\tleave");
                        escreverCodigo("\tret");
                        if (!sectionData.isEmpty()) {
                            escreverCodigo("\nsection .data\n");
                            for (String mensagem : sectionData) {
                                escreverCodigo(mensagem);
                            }
                        }

                        try {
                            bw.close();
                            fw.close();
                        } catch (IOException e) {
                            System.err.println("Erro ao fechar arquivo de saída");
                        }
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

            //{A02}
            int tamanho = 0;
            for (String var : variaveis) {
                tabela.get(var).setTipo(Tipo.INTEGER);
                tamanho += TAMANHO_INTEIRO;
            }
            escreverCodigo("\tsub esp, " + tamanho);
            variaveis.clear();
            //{A02}
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() + "Ponto e vírgula esperado.");
        }
    }

    // <variaveis> ::= <id> {A03} <mais_var>
    private void variaveis() {
        if(token.getClasse() == Classe.identificador) {
            //{A03}
            String variavel = token.getValor().getValorTexto();
            if (tabela.isPresent(variavel)) {
                System.out.println("Variável " + variavel + " já foi declarada anteriormente");
                System.exit(-1);
            } else {
                tabela.add(variavel);
                tabela.get(variavel).setCategoria(Categoria.VARIAVEL);
                tabela.get(variavel).setOffset(offsetVariavel);
                offsetVariavel += TAMANHO_INTEIRO;
                variaveis.add(variavel);
            }
            System.out.println(tabela);
            //{A03}
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
        comando();
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
        if(ehDeterminadaPalavraReservada("read") || 
           ehDeterminadaPalavraReservada("write") || 
           ehDeterminadaPalavraReservada("writeln") ||
           ehDeterminadaPalavraReservada("for") ||
           ehDeterminadaPalavraReservada("repeat") ||
           ehDeterminadaPalavraReservada("while") ||
           ehDeterminadaPalavraReservada("if") ||
           token.getClasse() == Classe.identificador) {
            sentencas();
        }
    }

    private void comando() {
        if(ehDeterminadaPalavraReservada("read")) {
            token = lexico.nextToken();
            if(token.getClasse() == Classe.parentesesEsquerdo) {
                token = lexico.nextToken();
                var_read();
                if(token.getClasse() == Classe.parentesesDireito) {
                    token = lexico.nextToken();
                    
                } else {
                    System.err.println(token.getLinha() + ", " + token.getColuna() + "Parênteses direito esperado no comando read.");
                }
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() + "Parênteses esquerdo esperado no comando read.");
            }
        } else if(ehDeterminadaPalavraReservada("write")) {
            token = lexico.nextToken();
            if(token.getClasse() == Classe.parentesesEsquerdo) {
                token = lexico.nextToken();
                exp_write();
                if(token.getClasse() == Classe.parentesesDireito) {
                    token = lexico.nextToken();
                } else {
                    System.err.println(token.getLinha() + ", " + token.getColuna() + "Parênteses direito esperado no comando write.");
                }
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() + "Parênteses esquerdo esperado no comando write.");
            }
        } else if(ehDeterminadaPalavraReservada("writeln")) {
            token = lexico.nextToken();
            if(token.getClasse() == Classe.parentesesEsquerdo) {
                token = lexico.nextToken();
                exp_write();
                if(token.getClasse() == Classe.parentesesDireito) {
                    token = lexico.nextToken();
                } else {
                    System.err.println(token.getLinha() + ", " + token.getColuna() + "Parênteses direito esperado no comando writeln.");
                }
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() + "Parênteses esquerdo esperado no comando writeln.");
            }
        } else if(ehDeterminadaPalavraReservada("for")) {
            token = lexico.nextToken();

            if(token.getClasse() == Classe.identificador) {
                token = lexico.nextToken();

                if(token.getClasse() == Classe.atribuicao) {
                    token = lexico.nextToken();

                    expressao();

                    if(ehDeterminadaPalavraReservada("to")) {
                        token = lexico.nextToken();

                        expressao();
                        if(ehDeterminadaPalavraReservada("do")) {
                            token = lexico.nextToken();

                            if(ehDeterminadaPalavraReservada("begin")) {
                                token = lexico.nextToken();
                                sentencas();

                                if(ehDeterminadaPalavraReservada("end")) {
                                    token = lexico.nextToken();
                                } else {
                                    System.err.println(token.getLinha() + ", " + token.getColuna() + "(end) esperado no comando for.");
                                }
                            } else {
                                System.err.println(token.getLinha() + ", " + token.getColuna() + "(begin) esperado no comando for.");
                            }
                        } else {
                            System.err.println(token.getLinha() + ", " + token.getColuna() + "(do) esperado no comando for.");
                        }
                    } else {
                        System.err.println(token.getLinha() + ", " + token.getColuna() + "(to) esperado no comando for.");
                    }
                } else {
                    System.err.println(token.getLinha() + ", " + token.getColuna() + "Atribuição esperado no comando for.");
                }
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() + "Identificador esperado no comando for.");
            }
        } else if (ehDeterminadaPalavraReservada("repeat")) {
            token = lexico.nextToken();
            sentencas();

            if(ehDeterminadaPalavraReservada("until")) {
                token = lexico.nextToken();
                if(token.getClasse() == Classe.parentesesEsquerdo) {
                    token = lexico.nextToken();
                    expressao_logica();
                    if(token.getClasse() == Classe.parentesesDireito) {
                        token = lexico.nextToken();
                    } else {
                        System.err.println(token.getLinha() + ", " + token.getColuna() + "Parênteses direito esperado no comando until.");
                    }
                } else {
                    System.err.println(token.getLinha() + ", " + token.getColuna() + "Parênteses esquerdo esperado no comando until.");
                }
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() + "(until) esperado no comando repeat.");
            }
        } else if (ehDeterminadaPalavraReservada("while")) {
            token = lexico.nextToken();
            if(token.getClasse() == Classe.parentesesEsquerdo) {
                token = lexico.nextToken();
                expressao_logica();
                if(token.getClasse() == Classe.parentesesDireito) {
                    token = lexico.nextToken();
                    
                    if(ehDeterminadaPalavraReservada("do")) {
                        token = lexico.nextToken();

                        if(ehDeterminadaPalavraReservada("begin")) {
                            token = lexico.nextToken();
                            sentencas();

                            if(ehDeterminadaPalavraReservada("end")) {
                                token = lexico.nextToken();
                            } else {
                                System.err.println(token.getLinha() + ", " + token.getColuna() + "(end) esperado no comando while.");
                            }
                        } else {
                            System.err.println(token.getLinha() + ", " + token.getColuna() + "(begin) esperado no comando while.");
                        }
                    } else {
                        System.err.println(token.getLinha() + ", " + token.getColuna() + "(do) esperado no comando while.");
                    }
                } else {
                    System.err.println(token.getLinha() + ", " + token.getColuna() + "Parênteses direito esperado no comando while.");
                }
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() + "Parênteses esquerdo esperado no comando while.");
            }
        } else if (ehDeterminadaPalavraReservada("if")) {
            token = lexico.nextToken();
            if(token.getClasse() == Classe.parentesesEsquerdo) {
                token = lexico.nextToken();
                expressao_logica();
                if(token.getClasse() == Classe.parentesesDireito) {
                    token = lexico.nextToken();
                    if(ehDeterminadaPalavraReservada("then")) {
                        token = lexico.nextToken();

                        if(ehDeterminadaPalavraReservada("begin")) {
                            token = lexico.nextToken();
                            sentencas();

                            if(ehDeterminadaPalavraReservada("end")) {
                                token = lexico.nextToken();
                                pfalsa();
                            } else {
                                System.err.println(token.getLinha() + ", " + token.getColuna() + "(end) esperado no comando if.");
                            }
                        } else {
                            System.err.println(token.getLinha() + ", " + token.getColuna() + "(begin) esperado no comando if.");
                        }
                    } else {
                        System.err.println(token.getLinha() + ", " + token.getColuna() + "(then) esperado no comando if.");
                    }
                } else {
                    System.err.println(token.getLinha() + ", " + token.getColuna() + "Parênteses direito esperado no comando if.");
                }
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() + "Parênteses esquerdo esperado no comando if.");
            }
        } else if (token.getClasse() == Classe.identificador) {
            token = lexico.nextToken();

            if(token.getClasse() == Classe.atribuicao) {
                token = lexico.nextToken();
                expressao();
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() + "Atribuição esperado após o identificador.");
            }
        }
    }

    // <var_read> ::= <id> {A08} <mais_var_read>
    private void var_read() {
        if (token.getClasse() == Classe.identificador) {

            //{A08}
            String variavel = token.getValor().getValorTexto();
            if (!tabela.isPresent(variavel)) {
                System.err.println("Variável " + variavel + " não foi declarada");
                System.exit(-1);
            } else {
                Registro registro = tabela.get(variavel);
                if (registro.getCategoria() != Categoria.VARIAVEL) {
                    System.err.println("Identificador " + variavel + " não é uma variável");
                    System.exit(-1);
                } else {
                    escreverCodigo("\tmov edx, ebp");
                    escreverCodigo("\tlea eax, [edx - " + registro.getOffset() + "]");
                    escreverCodigo("\tpush eax");
                    escreverCodigo("\tpush @Integer");
                    escreverCodigo("\tcall scanf");
                    escreverCodigo("\tadd esp, 8");
                    if (!sectionData.contains("@Integer: db '%d',0")) {
                        sectionData.add("@Integer: db '%d',0");
                    }
                }
            }
            //{A08}

            token = lexico.nextToken();
            mais_var_read();
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() + "Identificador esperado na regra var read.");
        }  
    }

    // <mais_var_read> ::= , <var_read> | ε
    private void mais_var_read() {
        if (token.getClasse() == Classe.virgula) {
            token = lexico.nextToken();
            var_read();
        }
    }

    // <exp_write> ::= <id> {A09} <mais_exp_write> |
    // <string> {A59} <mais_exp_write> |
    // <intnum> {A43} <mais_exp_write>
    private void exp_write() {
        if (token.getClasse() == Classe.identificador) {
            //{A09}
            String variavel = token.getValor().getValorTexto();
            if (!tabela.isPresent(variavel)) {
                System.err.println("Variável " + variavel + " não foi declarada");
                System.exit(-1);
            } else {
                Registro registro = tabela.get(variavel);
                if (registro.getCategoria() != Categoria.VARIAVEL) {
                    System.err.println("Identificador " + variavel + " não é uma variável");
                    System.exit(-1);
                } else {
                    escreverCodigo("\tpush dword[ebp - " + registro.getOffset() + "]");
                    escreverCodigo("\tpush @Integer");
                    escreverCodigo("\tcall printf");
                    escreverCodigo("\tadd esp, 8");
                    if (!sectionData.contains("@Integer: db '%d',0")) {
                        sectionData.add("@Integer: db '%d',0");
                    }
                }
            }
            //{A09}

            token = lexico.nextToken();
            mais_exp_write();
        } else if (token.getClasse() == Classe.string) {
            //{A59}
            String string = token.getValor().getValorTexto();
            String rotulo = criarRotulo("String");
            sectionData.add(rotulo + ": db '" + string + "', 0 ");
            escreverCodigo("\tpush " + rotulo);
            escreverCodigo("\tcall printf");
            escreverCodigo("\tadd esp, 4");
            //{A59}

            token = lexico.nextToken();
            mais_exp_write();
        } else if (token.getClasse() == Classe.numeroInteiro) {
            //{A43}
            int numero = token.getValor().getValorInteiro();
            escreverCodigo("\tpush " + numero);
            escreverCodigo("\tpush @Integer");
            escreverCodigo("\tcall printf");
            escreverCodigo("\tadd esp, 8");
            if (!sectionData.contains("@Integer: db '%d',0")) {
                sectionData.add("@Integer: db '%d',0");
            }
            //{A43}

            token = lexico.nextToken();
            mais_exp_write();
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() + "Valor esperado no write.");
        }
    }

    // <mais_exp_write> ::=  ,  <exp_write> | ε
    private void mais_exp_write() {
        if (token.getClasse() == Classe.virgula) {
            token = lexico.nextToken();
            exp_write();
        }
    }

    // <pfalsa> ::= else {A25} begin <sentencas> end | ε
    private void pfalsa() {
        if (ehDeterminadaPalavraReservada("else")) {
            token = lexico.nextToken();
            if(ehDeterminadaPalavraReservada("begin")) {
                token = lexico.nextToken();
                sentencas();

                if(ehDeterminadaPalavraReservada("end")) {
                    token = lexico.nextToken();
                }
            } 
        }
    }

    // <expressao_logica> ::= <termo_logico> <mais_expr_logica>
    private void expressao_logica() {
        termo_logico();
        mais_expr_logica();
    }

    // <mais_expr_logica> ::= or <termo_logico> <mais_expr_logica> {A26} | ε
    private void mais_expr_logica() {
        if(ehDeterminadaPalavraReservada("or")) {
            token = lexico.nextToken();
            termo_logico();
            mais_expr_logica();
        }
    }

    // <termo_logico> ::= <fator_logico> <mais_termo_logico>
    private void termo_logico() {
        fator_logico();
        mais_termo_logico();
    }

    // <mais_termo_logico> ::= and <fator_logico> <mais_termo_logico> {A27} | ε
    private void mais_termo_logico() {
        if(ehDeterminadaPalavraReservada("and")) {
            token = lexico.nextToken();
            fator_logico();
            mais_termo_logico();
        }
    }

    private void fator_logico() {
        if (token.getClasse() == Classe.parentesesEsquerdo) { // xxxxxxxxxxx
            token = lexico.nextToken();
            expressao_logica();
            if (token.getClasse() == Classe.parentesesDireito) {
                token = lexico.nextToken();
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() + "Parênteses direito esperado na função fator logico.");
            }
        } else if (ehDeterminadaPalavraReservada("not")) {
            token = lexico.nextToken();
            fator_logico();
        } else if (ehDeterminadaPalavraReservada("true")) {
            token = lexico.nextToken();
        } else if (ehDeterminadaPalavraReservada("false")) {
            token = lexico.nextToken();
        } else {
            relacional();     
        }
    }

    private void relacional() {
        if (token.getClasse() == Classe.identificador 
            || token.getClasse() == Classe.numeroInteiro 
            || token.getClasse() == Classe.parentesesEsquerdo) {
            
            expressao();
                
            if (token.getClasse() == Classe.operadorIgual
                || token.getClasse() == Classe.operadorMaior
                || token.getClasse() == Classe.operadorMaiorIgual
                || token.getClasse() == Classe.operadorMenor
                || token.getClasse() == Classe.operadorMenorIgual
                || token.getClasse() == Classe.operadorDiferente) {
              
                token = lexico.nextToken();
                if (token.getClasse() == Classe.identificador 
                || token.getClasse() == Classe.numeroInteiro 
                || token.getClasse() == Classe.parentesesEsquerdo) {
                    expressao();
                } else {
                    System.err.println(token.getLinha() + ", " + token.getColuna() + "Expressão inválida no relacional.");
                }
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() + "Operador inválido no relacional.");
            }
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() + "Expressão inválida no relacional.");
        }
    }

    // <expressao> ::= <termo> <mais_expressao>
    private void expressao() {
        termo();
        mais_expressao();
    }

    // <termo> ::= <fator> <mais_termo>
    private void termo() {
        fator();
        mais_termo();
    }

    // <mais_expressao> ::= + <termo> <mais_expressao> {A37} |
    //                      - <termo> <mais_expressao> {A38} | ε
    private void mais_expressao() {
        if (token.getClasse() == Classe.operadorSoma || token.getClasse() == Classe.operadorSubtracao) {
            token = lexico.nextToken();
            termo();
            mais_expressao();
        }
    }

    // <mais_termo> ::= * <fator> <mais_termo> {A39} |
    //                  / <fator> <mais_termo> {A40} | ε
    private void mais_termo() {
        if (token.getClasse() == Classe.operadorMultiplicacao || token.getClasse() == Classe.operadorDivisao) {
            token = lexico.nextToken();
            fator();
            mais_termo();
        }
    }

    // <fator> ::= <id> {A55} | <intnum> {A41} | ( <expressao> ) | <id> {A60} <argumentos> {A42}
    private void fator() {
        if (token.getClasse() == Classe.identificador) {
            token = lexico.nextToken();
        } else if (token.getClasse() == Classe.numeroInteiro) {
            token = lexico.nextToken();
        } else if (token.getClasse() == Classe.parentesesEsquerdo) {
            token = lexico.nextToken();
            expressao();
            if (token.getClasse() == Classe.parentesesDireito) {
                token = lexico.nextToken();
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() + "Parênteses direito esperado na função fator logico.");
            }
        } else {
            System.err.println(token.getLinha() + ", " + token.getColuna() + "Fator invalido.");
        }
    }
}
