package med.voll.api.domain.paciente;

import jakarta.persistence.*;
import lombok.*;
import med.voll.api.domain.endereco.Endereco;

@Table(name = "pacientes")
@Entity(name = "Paciente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private Boolean ativo;

    @Embedded
    private Endereco endereco;

    public Paciente(DadosCadastrosPaciente dadosCadastrosPaciente) {
        this.ativo = true;
        this.nome = dadosCadastrosPaciente.nome();
        this.cpf = dadosCadastrosPaciente.cpf();
        this.email = dadosCadastrosPaciente.email();
        this.telefone = dadosCadastrosPaciente.telefone();
        this.endereco = new Endereco(dadosCadastrosPaciente.endereco());
    }

    public void atualizarInformacoes(DadosAtualizaPaciente dados) {
        if(dados.nome()!=null){
            this.nome = dados.nome();
        }
        if(dados.telefone()!=null){
            this.telefone = dados.telefone();
        }
        if(dados.endereco()!=null){
            this.endereco.atualizarEndereco(dados.endereco());
        }
    }

    public void excluir(Long id) {
        this.ativo = false;
    }
}
