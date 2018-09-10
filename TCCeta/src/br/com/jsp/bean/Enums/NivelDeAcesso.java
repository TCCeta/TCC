package br.com.jsp.bean.Enums;

public enum NivelDeAcesso {

	Funcionario(0), Empresa(1), Usuario(2), Admin(3);
	
	//Serve somente para identificar melhor qual numerador é de qual número
	@SuppressWarnings("unused")
	private int value;
	
	private NivelDeAcesso(int value) {
		this.value = value;
	}
	
	public static NivelDeAcesso fromInt(int x) {
        switch(x) {
        case 0:
            return Funcionario;
        case 1:
            return Empresa;
        case 2:
        	return Usuario;
        case 3: 
        	return Admin;
        }
        return null;
    }
	
	
}
