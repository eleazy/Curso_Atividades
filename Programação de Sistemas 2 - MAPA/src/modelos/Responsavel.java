package modelos;

public class Responsavel {

    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String endereco;
    private int idade;

    public Responsavel(String nome, String cpf, String telefone, String email, String endereco, int idade) throws Exception {
        if (idade < 18) throw new Exception("O responsável deve ser maior de idade.");      

        if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || email.isEmpty() || endereco.isEmpty()) {
            throw new Exception("Todos os campos são obrigatórios");
        }

        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.idade = idade;
    }
    

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getEndereco() {
        return endereco;
    }

    public int getIdade() {
        return idade;
    }

}