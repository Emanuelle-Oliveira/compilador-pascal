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
    private Registro registro;
    private String rotuloElse;

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
                    System.err.println(token.getLinha() + ", " + token.getColuna() + "(; programa) Ponto e vírgula esperado no nome do programa.");
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
            System.err.println(token.getLinha() + ", " + token.getColuna() + "(dvar) Ponto e vírgula esperado.");
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
            System.err.println(token.getLinha() + ", " + token.getColuna() + "(mais dc) Ponto e vírgula esperado.");
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
            System.err.println(token.getLinha() + ", " + token.getColuna() + "(mais sentencas) Ponto e vírgula esperado.");
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
                    // {A61}
                    String novaLinha = "rotuloStringLN: db '', 10, 0";
                    if (!sectionData.contains(novaLinha)) {
                        sectionData.add(novaLinha);
                    }
                    escreverCodigo("\tpush rotuloStringLN");
                    escreverCodigo("\tcall printf");
                    escreverCodigo("\tadd esp, 4");
                    // {A61}
                } else {
                    System.err.println(token.getLinha() + ", " + token.getColuna() + "Parênteses direito esperado no comando writeln.");
                }
            } else {
                System.err.println(token.getLinha() + ", " + token.getColuna() + "Parênteses esquerdo esperado no comando writeln.");
            }
        } else if(ehDeterminadaPalavraReservada("for")) {
            token = lexico.nextToken();

            if(token.getClasse() == Classe.identificador) {
                // {A57}
                String variavel = token.getValor().getValorTexto();
                if (!tabela.isPresent(variavel)) {
                    System.err.println("A57: Variável " + variavel + " não foi declarada");
                    System.exit(-1);
                } else {
                    registro = tabela.get(variavel);
                    if (registro.getCategoria() != Categoria.VARIAVEL) {
                        System.err.println("A57: Identificador " + variavel + " não é uma variável");
                        System.exit(-1);
                    } 
                }
                // {A57}

                token = lexico.nextToken();

                if(token.getClasse() == Classe.atribuicao) {
                    token = lexico.nextToken();

                    expressao();

                    // {A11}
                    escreverCodigo("\tpop dword[ebp - " + registro.getOffset() + "]");
                    
                    String rotuloEntrada  = criarRotulo("FOR");
                    String rotuloSaida  = criarRotulo("FIMFOR");

                    rotulo = rotuloEntrada;
                    // {A11}

                    if(ehDeterminadaPalavraReservada("to")) {
                        token = lexico.nextToken();
                        expressao();

                        // {A12}
                        escreverCodigo("\tpush ecx\n"
                                     + "\tmov ecx, dword[ebp - " + registro.getOffset() + "]\n"
                                     + "\tcmp ecx, dword[esp+4]\n"  //+4 por causa do ecx
                                     + "\tjg " + rotuloSaida + "\n"
                                     + "\tpop ecx");
                        // {A12}

                        if(ehDeterminadaPalavraReservada("do")) {
                            token = lexico.nextToken();

                            if(ehDeterminadaPalavraReservada("begin")) {
                                token = lexico.nextToken();
                                sentencas();

                                if(ehDeterminadaPalavraReservada("end")) {
                                    token = lexico.nextToken();

                                    // {A13}
                                    escreverCodigo("\tadd dword[ebp - " + registro.getOffset() + "], 1");
                                    escreverCodigo("\tjmp " + rotuloEntrada);
                                    rotulo = rotuloSaida;
                                    // {A13}

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
            // {A14}
            String rotRepeat = criarRotulo("Repeat");
            rotulo = rotRepeat;
            // {A14}
            
            token = lexico.nextToken();
            sentencas();

            if(ehDeterminadaPalavraReservada("until")) {
                token = lexico.nextToken();
                if(token.getClasse() == Classe.parentesesEsquerdo) {
                    token = lexico.nextToken();
                    expressao_logica();
                    if(token.getClasse() == Classe.parentesesDireito) {
                        token = lexico.nextToken();

                        // {A15}
                        escreverCodigo("\tcmp dword[esp], 0\n");
                        escreverCodigo("\tje " + rotRepeat);
                        // {A15}

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
            // {A16}
            String rotuloWhile = criarRotulo("While");
            String rotuloFim = criarRotulo("FimWhile");

            rotulo = rotuloWhile;
            // {A16}

            token = lexico.nextToken();
            if(token.getClasse() == Classe.parentesesEsquerdo) {
                token = lexico.nextToken();
                expressao_logica();
                if(token.getClasse() == Classe.parentesesDireito) {
                    // {A17}
                    escreverCodigo("\tcmp dword[esp], 0\n");
                    escreverCodigo("\tje " + rotuloFim);
                    // {A17}

                    token = lexico.nextToken();
                    
                    if(ehDeterminadaPalavraReservada("do")) {
                        token = lexico.nextToken();

                        if(ehDeterminadaPalavraReservada("begin")) {
                            token = lexico.nextToken();
                            sentencas();

                            if(ehDeterminadaPalavraReservada("end")) {
                                // {A18}
                                escreverCodigo("\tjmp " + rotuloWhile);

                                rotulo = rotuloFim;
                                // {A18}
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
                    // {A19}
                    rotuloElse = criarRotulo("Else");
                    String rotuloFim = criarRotulo("FimIf");

                    escreverCodigo("\tcmp dword [esp], 0\n");
                    escreverCodigo("\tje " + rotuloElse);
                    // {A19}
                    token = lexico.nextToken();
                    if(ehDeterminadaPalavraReservada("then")) {
                        token = lexico.nextToken();

                        if(ehDeterminadaPalavraReservada("begin")) {
                            token = lexico.nextToken();
                            sentencas();

                            if(ehDeterminadaPalavraReservada("end")) {
                                // {A20}
                                escreverCodigo("\tjmp " + rotuloFim);
                                // {A20}

                                token = lexico.nextToken();
                                pfalsa();

                                // {A21}
                                rotulo = rotuloFim;
                                // {A21}
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
            // {A49}
            String variavel = token.getValor().getValorTexto();
            if (!tabela.isPresent(variavel)) {
                System.err.println("A49: Variável " + variavel + " não foi declarada");
                System.exit(-1);
            } else {
                registro = tabela.get(variavel);
                if (registro.getCategoria() != Categoria.VARIAVEL) {
                    System.err.println("A49: Identificador " + variavel + " não é uma variável");
                    System.exit(-1);
                } 
            }
            // {A49}

            token = lexico.nextToken();

            if(token.getClasse() == Classe.atribuicao) {
                token = lexico.nextToken();
                expressao();

                // {A22}
                escreverCodigo("\tpop eax");
                escreverCodigo("\tmov dword[ebp - " + registro.getOffset() + "], eax");
                // {A22}
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
                System.err.println("A09: Variável " + variavel + " não foi declarada");
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
        // {A25}
        escreverCodigo(rotuloElse + ":");
        // {A25}

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

            // {A26}
            // Empilhar 1, caso o valor de expressao_logica ou termo_logico seja 1, e 0
            // (falso), caso seja diferente. Isto pode ser feito da seguinte forma:
            // Crie um novo rótulo, digamos rotSaida
            String rotSaida = criarRotulo("SaidaMEL");
            // Crie um novo rótulo, digamos rotVerdade
            String rotVerdade = criarRotulo("VerdadeMEL");
            // Gere a instrução: cmp dword [ESP + 4], 1
            escreverCodigo("\tcmp dword [ESP + 4], 1");
            // Gere a instrução je para rotVerdade
            escreverCodigo("\tje " + rotVerdade);
            // Gere a instrução: cmp dword [ESP], 1
            escreverCodigo("\tcmp dword [ESP], 1");
            // Gere a instrução je para rotVerdade
            escreverCodigo("\tje " + rotVerdade);
            // Gere a instrução: mov dword [ESP + 4], 0
            escreverCodigo("\tmov dword [ESP + 4], 0");
            // Gere a instrução jmp para rotSaida
            escreverCodigo("\tjmp " + rotSaida);
            // Gere o rótulo rotVerdade
            rotulo = rotVerdade;
            // Gere a instrução: mov dword [ESP + 4], 1
            escreverCodigo("\tmov dword [ESP + 4], 1");
            // Gere o rótulo rotSaida
            rotulo = rotSaida;
            // Gere a instrução: add esp, 4
            escreverCodigo("\tadd esp, 4");
            // {A26}
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

            // {A27}
            // Empilhar 1 (verdadeiro), caso o valor de termo_logico e fator_logico seja 1,
            // e 0 (falso), caso seja diferente. Proceda de forma semelhante a ação 26.
            // Crie um novo rótulo, digamos rotSaida
            String rotSaida = criarRotulo("SaidaMTL");
            // Crie um novo rótulo, digamos rotFalso
            String rotFalso = criarRotulo("FalsoMTL");
            // Gere a instrução: cmp dword [ESP + 4], 1
            escreverCodigo("\tcmp dword [ESP + 4], 1");
            escreverCodigo("\tjne " + rotFalso);
            // Comparar os 2 valores
            // Gere a instrução: pop eax
            escreverCodigo("\tpop eax");
            // Gere a instrução: cmp dword [ESP], eax
            escreverCodigo("\tcmp dword [ESP], eax");
            // Gere a instrução je para rotVerdade
            escreverCodigo("\tjne " + rotFalso);
            // Gere a instrução: mov dword [ESP + 4], 1
            escreverCodigo("\tmov dword [ESP], 1");
            // Gere a instrução jmp para rotSaida
            escreverCodigo("\tjmp " + rotSaida);
            // Gere o rótulo rotFalso
            rotulo = rotFalso;
            // Gere a instrução: mov dword [ESP], 0
            escreverCodigo("\tmov dword [ESP], 0");
            // Gere o rótulo rotSaida
            rotulo = rotSaida;
            // Gere a instrução: add esp, 4
            // escreverCodigo("\tadd esp, 4");
            // {A27}
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

            // {A28}
            // Empilhar 1 (verdadeiro), caso o valor de fator_logico seja 0, e 0 (falso),
            // caso seja diferente. Proceda da seguinte forma:
            // Crie um rótulo Falso e outro Saida.
            String rotFalso = criarRotulo("FalsoFL");
            String rotSaida = criarRotulo("SaidaFL");
            // Gere a instrução: cmp dword [ESP], 1
            escreverCodigo("\tcmp dword [ESP], 1");
            // Gere a instrução: jne Falso
            escreverCodigo("\tjne " + rotFalso);
            // Gere a instrução: mov dword [ESP], 0
            escreverCodigo("\tmov dword [ESP], 0");
            // Gere a instrução: jmp Fim
            escreverCodigo("\tjmp " + rotSaida);
            // Gere o rótulo Falso
            rotulo = rotFalso;
            // Gere a instrução: mov dword [ESP], 1
            escreverCodigo("\tmov dword [ESP], 1");
            // Gere o rótulo Fim
            rotulo = rotSaida;
            // {A28}

        } else if (ehDeterminadaPalavraReservada("true")) {
            token = lexico.nextToken();

            // {A29}
            escreverCodigo("\tpush 1");
            // {A29}

        } else if (ehDeterminadaPalavraReservada("false")) {
            token = lexico.nextToken();

            // {A30}
            escreverCodigo("\tpush 0");
            // {A30}

        } else {
            relacional();     
        }
    }

    private void relacional() {
        if (token.getClasse() == Classe.identificador 
            || token.getClasse() == Classe.numeroInteiro 
            || token.getClasse() == Classe.parentesesEsquerdo) {
            
            expressao();
                
            if (token.getClasse() == Classe.operadorIgual) {
                token = lexico.nextToken();
                if (token.getClasse() == Classe.identificador 
                || token.getClasse() == Classe.numeroInteiro 
                || token.getClasse() == Classe.parentesesEsquerdo) {
                    expressao();

                    // {A31}
                    // Empilhar 1 (verdadeiro), caso a primeira expressão expressao seja igual a
                    // segunda, ou 0 (falso), caso contrário. Isto pode ser feito da seguinte forma:
                    // Crie um rótulo Falso e outro Saida.
                    String rotFalso = criarRotulo("FalsoREL");
                    String rotSaida = criarRotulo("SaidaREL");
                    // COMPARA 2 VALORES
                    // Gere a instrução: pop eax
                    escreverCodigo("\tpop eax");
                    // Gere a instrução: cmp dword [ESP], eax
                    escreverCodigo("\tcmp dword [ESP], eax");
                    // Gere a instrução: jne Falso
                    escreverCodigo("\tjne " + rotFalso);
                    // Gere a instrução: mov dword [ESP], 1
                    escreverCodigo("\tmov dword [ESP], 1");
                    // Gere a instrução: jmp Fim
                    escreverCodigo("\tjmp " + rotSaida);
                    // Gere o rótulo Falso
                    rotulo = rotFalso;
                    // Gere a instrução: mov dword [ESP], 0
                    escreverCodigo("\tmov dword [ESP], 0");
                    // Gere o rótulo Fim
                    rotulo = rotSaida;
                    // {A31}
                } else {
                    System.err.println(token.getLinha() + ", " + token.getColuna() + "Expressão inválida no relacional.");
                }
            } else if (token.getClasse() == Classe.operadorMaior) {
                token = lexico.nextToken();
                if (token.getClasse() == Classe.identificador 
                || token.getClasse() == Classe.numeroInteiro 
                || token.getClasse() == Classe.parentesesEsquerdo) {
                    expressao();
                    // {A32}
                    // Empilhar 1 (verdadeiro), caso a primeira expressão expressao seja maior que a
                    // segunda, ou 0 (falso), caso contrário. Proceda como o exemplo da ação 31.
                    // Crie um rótulo Falso e outro Saida.
                    String rotFalso = criarRotulo("FalsoREL");
                    String rotSaida = criarRotulo("SaidaREL");
                    // Gere a instrução: pop eax
                    escreverCodigo("\tpop eax");
                    // Gere a instrução: cmp dword [ESP], eax
                    escreverCodigo("\tcmp dword [ESP], eax");
                    // Gere a instrução: jle Falso
                    escreverCodigo("\tjle " + rotFalso);
                    // Gere a instrução: mov dword [ESP], 1
                    escreverCodigo("\tmov dword [ESP], 1");
                    // Gere a instrução: jmp Fim
                    escreverCodigo("\tjmp " + rotSaida);
                    // Gere o rótulo Falso
                    rotulo = rotFalso;
                    // Gere a instrução: mov dword [ESP], 0
                    escreverCodigo("\tmov dword [ESP], 0");
                    // Gere o rótulo Fim
                    rotulo = rotSaida;
                    // {A32}
                } else {
                    System.err.println(token.getLinha() + ", " + token.getColuna() + "Expressão inválida no relacional.");
                }
            } else if (token.getClasse() == Classe.operadorMaiorIgual) {
                token = lexico.nextToken();
                if (token.getClasse() == Classe.identificador 
                || token.getClasse() == Classe.numeroInteiro 
                || token.getClasse() == Classe.parentesesEsquerdo) {
                    expressao();
                    // {A33}
                    // Empilhar 1 (verdadeiro), caso a primeira expressão expressao seja maior ou
                    // igual a segunda, ou 0 (falso), caso contrário. Proceda como o exemplo da ação
                    // 31.
                    // Crie um rótulo Falso e outro Saida.
                    String rotFalso = criarRotulo("FalsoREL");
                    String rotSaida = criarRotulo("SaidaREL");
                    // Gere a instrução: pop eax
                    escreverCodigo("\tpop eax");
                    // Gere a instrução: cmp dword [ESP], eax
                    escreverCodigo("\tcmp dword [ESP], eax");
                    // Gere a instrução: jl Falso
                    escreverCodigo("\tjl " + rotFalso);
                    // Gere a instrução: mov dword [ESP], 1
                    escreverCodigo("\tmov dword [ESP], 1");
                    // Gere a instrução: jmp Fim
                    escreverCodigo("\tjmp " + rotSaida);
                    // Gere o rótulo Falso
                    rotulo = rotFalso;
                    // Gere a instrução: mov dword [ESP], 0
                    escreverCodigo("\tmov dword [ESP], 0");
                    // Gere o rótulo Fim
                    rotulo = rotSaida;
                    // {A33}
                } else {
                    System.err.println(token.getLinha() + ", " + token.getColuna() + "Expressão inválida no relacional.");
                }
            } else if (token.getClasse() == Classe.operadorMenor) {
                token = lexico.nextToken();
                if (token.getClasse() == Classe.identificador 
                || token.getClasse() == Classe.numeroInteiro 
                || token.getClasse() == Classe.parentesesEsquerdo) {
                    expressao();
                    // {A34}
                    // Empilhar 1 (verdadeiro), caso a primeira expressão expressao seja menor que a
                    // segunda, ou 0 (falso), caso contrário. Proceda como o exemplo da ação 31.
                    // Crie um rótulo Falso e outro Saida.
                    String rotFalso = criarRotulo("FalsoREL");
                    String rotSaida = criarRotulo("SaidaREL");
                    // Gere a instrução: pop eax
                    escreverCodigo("\tpop eax");
                    // Gere a instrução: cmp dword [ESP], eax
                    escreverCodigo("\tcmp dword [ESP], eax");
                    // Gere a instrução: jge Falso
                    escreverCodigo("\tjge " + rotFalso);
                    // Gere a instrução: mov dword [ESP], 1
                    escreverCodigo("\tmov dword [ESP], 1");
                    // Gere a instrução: jmp Fim
                    escreverCodigo("\tjmp " + rotSaida);
                    // Gere o rótulo Falso
                    rotulo = rotFalso;
                    // Gere a instrução: mov dword [ESP], 0
                    escreverCodigo("\tmov dword [ESP], 0");
                    // Gere o rótulo Fim
                    rotulo = rotSaida;
                    // {A34}
                } else {
                    System.err.println(token.getLinha() + ", " + token.getColuna() + "Expressão inválida no relacional.");
                }
            } else if (token.getClasse() == Classe.operadorMenorIgual) {
                token = lexico.nextToken();
                if (token.getClasse() == Classe.identificador 
                || token.getClasse() == Classe.numeroInteiro 
                || token.getClasse() == Classe.parentesesEsquerdo) {
                    expressao();
                    // {A35}
                    // Empilhar 1 (verdadeiro), caso a primeira expressão expressao seja menor ou
                    // igual a segunda, ou 0 (falso), caso contrário. Proceda como o exemplo da ação
                    // 31.
                    // Crie um rótulo Falso e outro Saida.
                    String rotFalso = criarRotulo("FalsoREL");
                    String rotSaida = criarRotulo("SaidaREL");
                    // Gere a instrução: pop eax
                    escreverCodigo("\tpop eax");
                    // Gere a instrução: cmp dword [ESP], eax
                    escreverCodigo("\tcmp dword [ESP], eax");
                    // Gere a instrução: jg Falso
                    escreverCodigo("\tjg " + rotFalso);
                    // Gere a instrução: mov dword [ESP], 1
                    escreverCodigo("\tmov dword [ESP], 1");
                    // Gere a instrução: jmp Fim
                    escreverCodigo("\tjmp " + rotSaida);
                    // Gere o rótulo Falso
                    rotulo = rotFalso;
                    // Gere a instrução: mov dword [ESP], 0
                    escreverCodigo("\tmov dword [ESP], 0");
                    // Gere o rótulo Fim
                    rotulo = rotSaida;
                    // {A35}
                } else {
                    System.err.println(token.getLinha() + ", " + token.getColuna() + "Expressão inválida no relacional.");
                }
            } else if (token.getClasse() == Classe.operadorDiferente) {
                token = lexico.nextToken();
                if (token.getClasse() == Classe.identificador 
                || token.getClasse() == Classe.numeroInteiro 
                || token.getClasse() == Classe.parentesesEsquerdo) {
                    expressao();
                    // {A36}
                    // Empilhar 1 (verdadeiro), caso a primeira expressão expressao seja diferente
                    // da segunda, ou 0 (falso), caso contrário. Proceda como o exemplo da ação 31.
                    // Crie um rótulo Falso e outro Saida.
                    String rotFalso = criarRotulo("FalsoREL");
                    String rotSaida = criarRotulo("SaidaREL");
                    // Gere a instrução: pop eax
                    escreverCodigo("\tpop eax");
                    // Gere a instrução: cmp dword [ESP], eax
                    escreverCodigo("\tcmp dword [ESP], eax");
                    // Gere a instrução: je Falso
                    escreverCodigo("\tje " + rotFalso);
                    // Gere a instrução: mov dword [ESP], 1
                    escreverCodigo("\tmov dword [ESP], 1");
                    // Gere a instrução: jmp Fim
                    escreverCodigo("\tjmp " + rotSaida);
                    // Gere o rótulo Falso
                    rotulo = rotFalso;
                    // Gere a instrução: mov dword [ESP], 0
                    escreverCodigo("\tmov dword [ESP], 0");
                    // Gere o rótulo Fim
                    rotulo = rotSaida;
                    // {A36}
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
        if (token.getClasse() == Classe.operadorSoma) {
            token = lexico.nextToken();
            termo();
            mais_expressao();

            // {A37}
            escreverCodigo("\tpop eax");
            escreverCodigo("\tadd dword[ESP], eax");
            // {A37}
        } else if (token.getClasse() == Classe.operadorSubtracao) {
            token = lexico.nextToken();
            termo();
            mais_expressao();

            // {A38}
            escreverCodigo("\tpop eax");
            escreverCodigo("\tsub dword[ESP], eax");
            // {A38}
        }
    }

    // <mais_termo> ::= * <fator> <mais_termo> {A39} |
    //                  / <fator> <mais_termo> {A40} | ε
    private void mais_termo() {
        if (token.getClasse() == Classe.operadorMultiplicacao) {
            token = lexico.nextToken();
            fator();
            mais_termo();

            // {A39}
            escreverCodigo("\tpop eax");
            escreverCodigo("\timul eax, dword [ESP]");
            escreverCodigo("\tmov dword [ESP], eax");
            // {A39}

        } else if (token.getClasse() == Classe.operadorDivisao) {
            token = lexico.nextToken();
            fator();
            mais_termo();

            // {A40}
            escreverCodigo("\tpop ecx");
            escreverCodigo("\tpop eax");
            escreverCodigo("\tidiv ecx");
            escreverCodigo("\tpush eax");
            // {A40}
        }
    }

    // <fator> ::= <id> {A55} | <intnum> {A41} | ( <expressao> ) | <id> {A60} <argumentos> {A42}
    private void fator() {
        if (token.getClasse() == Classe.identificador) {
            // {A55}
            // Se a categoria do identificador id, reconhecido em fator, for variável ou
            // parâmetro, então empilhar o valor armazenado no endereço de memória de id.
            // Lembre-se, que o endereço de memória de id é calculado em função da base da
            // pilha (EBP) e do deslocamento contido em display.
            String variavel = token.getValor().getValorTexto();
            if (!tabela.isPresent(variavel)) {
                System.err.println("Variável " + variavel + " não foi declarada");
                System.exit(-1);
            } else {
                registro = tabela.get(variavel);
                if (registro.getCategoria() != Categoria.VARIAVEL) {
                    System.err.println("O identificador " + variavel + "não é uma variável. A55");
                    System.exit(-1);
                }
            }
            escreverCodigo("\tpush dword[ebp - " + registro.getOffset() + "]");
            // {A55}
            
            token = lexico.nextToken();

        } else if (token.getClasse() == Classe.numeroInteiro) {
            // {A41}
            escreverCodigo("\npush " + token.getValor().getValorInteiro());
            // {A41}

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
