package med.voll.api.domain.paciente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.endereco.DadosEndereco;

public record DadosCadastrosPaciente(@NotBlank String nome,
                                     @NotBlank String cpf,
                                     @NotBlank @Email String email,
                                     @NotBlank String telefone,
                                     @NotNull @Valid DadosEndereco endereco) {
}
