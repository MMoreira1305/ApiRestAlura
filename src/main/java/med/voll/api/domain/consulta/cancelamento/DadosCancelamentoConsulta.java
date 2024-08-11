package med.voll.api.domain.consulta.cancelamento;

import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;

public record DadosCancelamentoConsulta(@NotNull DadosDetalhamentoConsulta consulta,
                                        @NotNull MotivoCancelamento motivo) {

}
