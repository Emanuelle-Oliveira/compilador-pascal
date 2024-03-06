import lexico.Classe;
import lexico.Lexico;
import lexico.Token;

public class Compilador {

  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("Modo de usar: java -jar NomePrograma NomeArquivoCodigo");
      return;
    }

    Lexico lexico = new Lexico(args[0]);
    Token token;

    do {
      token = lexico.nextToken();
      System.out.println(token);
    } while (token.getClasse() != Classe.EOF);
  }

}
