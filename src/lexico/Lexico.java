package lexico;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class Lexico {
  private String nomeArquivo;
  private Token token;
  private BufferedReader br;
  private char caractere;
  private StringBuilder lexema = new StringBuilder();
  private int linha;
  private int coluna;

  public Lexico(String nomeArquivo) {
    this.nomeArquivo = nomeArquivo;

    String caminhoArquivo = Paths.get(nomeArquivo).toAbsolutePath().toString();

    try {
      BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo, StandardCharsets.UTF_8));
      this.br = br;
      linha = 1;
      coluna = 1;

      caractere = proximoChar();
    } catch (IOException e) {
      System.err.println("Não foi abrir o arquivo: " + nomeArquivo);
      e.printStackTrace();
      System.exit(-1);
    }
  }

  private char proximoChar() {
    try {
      return (char) br.read();
    } catch (IOException e) {
      System.err.println("Não foi ler do arquivo: " + nomeArquivo);
      e.printStackTrace();
      System.exit(-1);
    }
    return 0;
  }

  public Token nextToken() {
    lexema.setLength(0);

    do {
      if (Character.isLetter(caractere)) {
        token = new Token(linha, coluna);

        lexema.append(caractere);
        caractere = proximoChar();
        coluna++;

        while (Character.isLetterOrDigit(caractere)) {
          lexema.append(caractere);
          caractere = proximoChar();
          coluna++;
        }

        token.setClasse(Classe.identificador);
        token.setValor(new Valor(lexema.toString()));
        return token;

      } else if (Character.isDigit(caractere)) {
        token = new Token(linha, coluna);

        lexema.append(caractere);
        caractere = proximoChar();
        coluna++;

        while (Character.isDigit(caractere)) {
          lexema.append(caractere);
          caractere = proximoChar();
          coluna++;
        }

        token.setClasse(Classe.identificador);
        token.setValor(new Valor(Integer.parseInt(lexema.toString())));
        return token;

      } else if (caractere == ' ') {
        caractere = proximoChar();
        coluna++;

      } else if (caractere == '\n') {
        caractere = proximoChar();
        linha++;
        coluna = 1;

      } else if (caractere == '\t') {
        caractere = proximoChar();
        coluna += 4;

      } else if (caractere == '+') {
        token = new Token(linha, coluna, Classe.operadorSoma);
        caractere = proximoChar();
        linha++;
        return token;

      } else if (caractere == '-') {
        token = new Token(linha, coluna, Classe.operadorSubtracao);
        caractere = proximoChar();
        linha++;
        return token;

      } else if (caractere == '*') {
        token = new Token(linha, coluna, Classe.operadorMultiplicacao);
        caractere = proximoChar();
        linha++;
        return token;

      } else if (caractere == '/') {
        token = new Token(linha, coluna, Classe.operadorDiferente);
        caractere = proximoChar();
        linha++;
        return token;

      } else if (caractere == '=') {
        token = new Token(linha, coluna, Classe.operadorIgual);
        caractere = proximoChar();
        linha++;
        return token;

      } else if (caractere == ';') {
        token = new Token(linha, coluna, Classe.pontoEVirgula);
        caractere = proximoChar();
        linha++;
        return token;

      } else if (caractere == ',') {
        token = new Token(linha, coluna, Classe.virgula);
        caractere = proximoChar();
        linha++;
        return token;
 
      } else if (caractere == '.') {
        token = new Token(linha, coluna, Classe.ponto);
        caractere = proximoChar();
        linha++;
        return token;

      } else if (caractere == '(') {
        token = new Token(linha, coluna, Classe.parentesesEsquerdo);
        caractere = proximoChar();
        linha++;
        return token;

      } else if (caractere == ')') {
        token = new Token(linha, coluna, Classe.parentesesDireito);
        caractere = proximoChar();
        linha++;
        return token;
 
      } else if (caractere == ':') {
        token = new Token(linha, coluna, Classe.doisPontos);
        caractere = proximoChar();
        linha++;
        return token;

      } else if (caractere == '>') {
        token = new Token(linha, coluna, Classe.doisPontos);
        caractere = proximoChar();
        linha++;
        return token;

      } else if (caractere == '<') {
        token = new Token(linha, coluna, Classe.doisPontos);
        caractere = proximoChar();
        linha++;
        return token;

      }
 
 
      System.out.println("Caractere: " + caractere);
    } while (caractere != 65535);

    return new Token(linha, coluna, Classe.EOF);
  }
}