package lexico;

public enum Classe {
  identificador,
  palavraReservada,
  numeroInteiro,
  EOF,
  //operadorSoma, // +
  //operadorSubtracao, // -
  //operadorMultiplicacao, // *
  //operadorDivisao, // /
  operadorMaior, // >
  operadorMenor, // <
  operadorMenorIgual, // <=
  operadorDiferente, // <>
  operadorMaiorIgual, // >=
  //operadorIgual,  // =
  atribuicao,  // :=
  //pontoEVirgula,  // ;
  //virgula, // ,
  //ponto,
  //doisPontos, // :
  //parentesesEsquerdo, // (
  //parentesesDireito, // )
  string
}