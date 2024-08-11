package med.voll.api.domain.consulta.service;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.cancelamento.DadosCancelamentoConsulta;
import med.voll.api.repository.MedicoRepository;
import med.voll.api.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CancelamentoDeConsultas {
    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;



    public void cancelarConsulta(DadosCancelamentoConsulta dados){
        var consulta = dados.consulta();

        if (!consultaRepository.existsById(consulta.id())) {
            throw new ValidacaoException("Id da consulta informada não existe");
        }

        if (!pacienteRepository.existsById(consulta.idPaciente())) {
            throw new ValidacaoException("Id do paciente informado não existe");
        }

        if (!medicoRepository.existsById(consulta.idMedico())) {
            throw new ValidacaoException("Id do medico informado não existe");
        }

        if(dados.motivo() == null){
            throw new ValidacaoException("O motivo não pode ser nulo");
        }

        salvarCancelamento(dados);
    }

    private void salvarCancelamento(DadosCancelamentoConsulta dados){
        try{
            var consulta = consultaRepository.getReferenceById(dados.consulta().id());
            if(consulta.getStatus_consulta().equals("C")){
                throw new ValidacaoException("A consulta já foi cancelada");
            }

            consulta.setStatus_consulta("A");
            consulta.setData_cancelamento(LocalDateTime.now());
            consulta.setMotivo_cancelamento(dados.motivo().toString());
        }catch (Exception e){
            throw new ValidacaoException("Aconteceu um erro ao excluir consulta: " + e.getMessage());
        }
    }
}
