package Model;

public class Usuario {
	private String nome, sobrenome, sexo, data_nascimento,
			email, cidade, estado, pais, pergunta_secreta,
			resposta_secreta, login, senha;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getData_nascimento() {
		return data_nascimento;
	}

	public void setData_nascimento(String dataNascimento) {
		data_nascimento = dataNascimento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getPergunta_secreta() {
		return pergunta_secreta;
	}

	public void setPergunta_secreta(String perguntaSecreta) {
		pergunta_secreta = perguntaSecreta;
	}

	public String getResposta_secreta() {
		return resposta_secreta;
	}

	public void setResposta_secreta(String respostaSecreta) {
		resposta_secreta = respostaSecreta;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
