package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.repository.PacienteRepository;

public class ValidadorPacienteAtivo implements ValidadorAgendamentoDeConsulta{
    private PacienteRepository pacienteRepository;

    public void validar(DadosAgendamentoConsulta dados){
        var pacienteEstaAtivo = pacienteRepository.findAtivoById(dados.idPaciente());
        if(!pacienteEstaAtivo){
            throw new ValidacaoException("Consulta não pode ser agendada com paciente excluído");
        }
    }
}
