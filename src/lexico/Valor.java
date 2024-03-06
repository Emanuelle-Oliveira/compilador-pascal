package lexico;

public class Valor {
  private int valorInteiro;
  private String valorTexto;

  public Valor(int valorInteiro) {
    this.valorInteiro = valorInteiro;
  }

  public Valor(String valorTexto) {
    this.valorTexto = valorTexto;
  }

  public int getValorInteiro() {
    return valorInteiro;
  }

  public void setValorInteiro(int valorInteiro) {
    this.valorInteiro = valorInteiro;
  }

  public String getValorTexto() {
    return valorTexto;
  }

  public void setValorTexto(String valorTexto) {
    this.valorTexto = valorTexto;
  }

  @Override
  public String toString() {
    return (valorTexto != null) ? "Valor [valorInteiro=" + valorInteiro + ", valorTexto=" + valorTexto + "]" : "Valor [valorInteiro=" + valorInteiro + "]";
  }
  
}
