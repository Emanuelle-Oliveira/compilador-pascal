package sintatico;

import java.util.HashMap;

public class TabelaSimbolos {
	
	private HashMap<String, Registro> simbolos = new HashMap<String, Registro>();
	
	public boolean isPresent(String lexema) {
		return simbolos.containsKey(lexema);
	}
	
	public Registro add(String lexema) {
		if (simbolos.containsKey(lexema)) {
			return simbolos.get(lexema);
		}
		Registro novo = new Registro();
		novo.setLexema(lexema);
		simbolos.put(lexema, novo);
		return novo;
	}
	
	public Registro get(String lexema) {
		return simbolos.get(lexema);
	}
	
	public void delete(String lexema) {
		simbolos.remove(lexema);
	}

	@Override
	public String toString() {
		String result = "";
		for (String chave : simbolos.keySet()) {
			result += chave + "-> " + simbolos.get(chave) + "\n";
		}
		return result;
	}
	
}