package med.voll.api.repository;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.medico.DadosCadastrosMedicos;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.paciente.DadosCadastrosPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;

// Sempre quando um teste rodar após o término irá limpar o banco de dados novamente
// Assim cada método não irá interferir em um e em outro
// Anotação para testar uma interface repository
@DataJpaTest
// Anotação para usar as configurações de base de dados padrão
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// Anotação para usar o application-test.properties da aplicação
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    // Simula o os métodos dos repositório como o "Save"
    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Deveria devolver null quando medico cadastrado não está disponível na data")
    void escolherMedicoAleatorioLivreNaDataCenario1() {
        // Given or arrange
        var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);


        var medico = cadastrarMedico("Medico", "medico@voll.med", "123456", "91999998888", Especialidade.CARDIOLOGIA);
        var paciente = cadastrarPaciente("Paciente", "paciente@voll.med", "12345678910");
        cadastrarConsulta(medico, paciente, proximaSegundaAs10);

        // When or Act
        var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAs10);

        // Then or assert
        assertThat(medicoLivre).isNull();
    }

    @Test
    @DisplayName("Deveria devolver medico quando ele estiver disponivel na data")
    void escolherMedicoAleatorioLivreNaDataCenario2() {
        // Given or arrange
        var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);

        var medico = cadastrarMedico("Medico", "medico@voll.med", "123456", "91999998888", Especialidade.CARDIOLOGIA);

        // When or Act
        var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAs10);

        // Then or assert
        assertThat(medicoLivre).isEqualTo(medico);
    }

    // Lógica de criação de DTO e cadastro de informações

    private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data){
        em.persist(new Consulta(null, medico, paciente, data));
    }

    private Medico cadastrarMedico(String nome, String email, String crm, String telefone, Especialidade especialidade){
        var medico = new Medico(dadosMedico(nome, email, crm, telefone, especialidade));
        em.persist(medico);
        return medico;
    }

    private Paciente cadastrarPaciente(String nome, String email, String cpf){
        var paciente = new Paciente(dadosPaciente(nome, email, cpf));
        em.persist(paciente);
        return paciente;
    }

    private DadosCadastrosMedicos dadosMedico(String nome, String email, String crm, String telefone, Especialidade especialidade){
        return new DadosCadastrosMedicos(
                nome,
                email,
                telefone,
                crm,
                especialidade,
                dadosEndereco()
        );
    }

    private DadosCadastrosPaciente dadosPaciente(String nome, String email, String cpf){
        return new DadosCadastrosPaciente(
                nome,
                cpf,
                email,
                "12345678910",
                dadosEndereco()
        );
    }

    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "Rua xto",
                "bairro",
                "00000000",
                "recife",
                "PE",
                null,
                null
        );
    }
}