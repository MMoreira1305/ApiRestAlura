package med.voll.api.domain.medico;

import jakarta.persistence.*;
import lombok.*;
import med.voll.api.domain.endereco.Endereco;

@Table(name = "medicos")
@Entity(name = "Medico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Medico {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String telefone;
    private String crm;
    private Boolean ativo;

    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    @Embedded
    private Endereco endereco;

    public Medico(DadosCadastrosMedicos dadosCadastrosMedicos) {
        this.ativo = true;
        this.nome = dadosCadastrosMedicos.nome();
        this.email = dadosCadastrosMedicos.email();
        this.telefone = dadosCadastrosMedicos.telefone();
        this.crm = dadosCadastrosMedicos.crm();
        this.especialidade = dadosCadastrosMedicos.especialidade();
        this.endereco = new Endereco(dadosCadastrosMedicos.endereco());
    }

    public void atualizarInformacoes(DadosAtualizaMedico dados) {
        if(dados.nome()!=null){
            System.out.println("Chegou aqui: " + dados.nome());
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
