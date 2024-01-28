package modelos;

public class Crianca {

    private Responsavel responsavel;
    private String nome;
    private int idade;
    private String sexo;

    public Crianca(Responsavel responsavel, String nome, int idade, String sexo) throws Exception {
        if (idade > 10) throw new Exception("A criança deve ter no máximo 10 anos.");

        if (nome.isEmpty() || sexo.isEmpty()) {
            throw new Exception("Todos os campos são obrigatórios");
        }
       
        this.responsavel = responsavel;
        this.nome = nome;
        this.idade = idade;
        this.sexo = sexo;
    }

    public Responsavel getResponsavel() {
        return responsavel;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public String getSexo() {
        return sexo;
    }
}