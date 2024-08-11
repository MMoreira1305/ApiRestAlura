package med.voll.api.domain.consulta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    Boolean existsByMedicoIdAndData(Long id_medico, LocalDateTime data);

    Boolean existsByPacienteIdAndDataBetween(Long id_paciente, LocalDateTime primeiroHorario, LocalDateTime ultimoHorario);
}
