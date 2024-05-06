package sintatico;

public enum Categoria {
	FUNCAO("Função"),
	VARIAVEL("Variável"),
	PARAMETRO("Parâmetro"),
	PROCEDIMENTO("Procedimento"),
	TIPO("Tipo"),
	INDEFINIDA("Indefinida"),
    PROGRAMAPRINCIPAL("Programa Principal");
	
	private String descricao;
	
	private Categoria(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}
