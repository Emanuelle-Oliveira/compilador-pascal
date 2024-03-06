package lexico;

public class Token {
  private int linha;
  private int coluna;
  private Classe classe;
  private Valor valor;

  public Token (int linha, int coluna, Classe classe, Valor valor) {
    this.linha = linha;
    this.coluna = coluna;
    this.classe = classe;
    this.valor = valor;
  }

  public Token (int linha, int coluna, Classe classe) {
    this.linha = linha;
    this.coluna = coluna;
    this.classe = classe;
  }

  public Token (int linha, int coluna) {
    this.linha = linha;
    this.coluna = coluna;
  }

  public int getLinha() {
    return linha;
  }

  public void setLinha(int linha) {
    this.linha = linha;
  }

  public int getColuna() {
    return coluna;
  }

  public void setColuna(int coluna) {
    this.coluna = coluna;
  }

  public Classe getClasse() {
    return classe;
  }

  public void setClasse(Classe classe) {
    this.classe = classe;
  }

  public Valor getValor() {
    return valor;
  }

  public void setValor(Valor valor) {
    this.valor = valor;
  }

  @Override
  public String toString() {
    return "Token [linha=" + linha + ", coluna=" + coluna + ", classe=" + classe + ", valor=" + valor + "]";
  }
  
}
