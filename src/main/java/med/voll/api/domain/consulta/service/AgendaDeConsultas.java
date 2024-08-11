package med.voll.api.domain.consulta.service;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.consulta.validacoes.ValidadorAgendamentoDeConsulta;
import med.voll.api.domain.consulta.validacoes.ValidadorHorarioFuncionamentoClinica;
import med.voll.api.domain.medico.Medico;
import med.voll.api.repository.MedicoRepository;
import med.voll.api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultas {
    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private List<ValidadorAgendamentoDeConsulta> validacoes;

    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados){

        if (!pacienteRepository.existsById(dados.idPaciente())) {
            throw new ValidacaoException("Id do paciente informado não existe");
        }

        if (dados.idMedico()!= null && !medicoRepository.existsById(dados.idMedico())) {
            throw new ValidacaoException("Id do medico informado não existe");
        }

        validacoes.forEach(v -> v.validar(dados));

        // Irá entrar aqui se todas as validações passarem
        var consultaRetorno = salvarConsulta(dados);
        return new DadosDetalhamentoConsulta(consultaRetorno.getId(), dados.idMedico(), dados.idPaciente(), dados.data());
    }

    private Consulta salvarConsulta(DadosAgendamentoConsulta dados) {
        var medico = escolherMedico(dados);
        if(medico == null){
            throw new ValidacaoException("Não existe médico disponível para a data escolhida");
        }
        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
        var consulta = new Consulta(medico, paciente, dados.data());
        consulta.setStatus_consulta("A");
        return consultaRepository.save(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if(dados.idMedico()!= null){
            return medicoRepository.getReferenceById(dados.idMedico());
        }

        if (dados.especialidade() == null){
            throw new ValidacaoException("Especialidade é obrigatória quando médico não é escolhido");
        }

        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
    }
}
