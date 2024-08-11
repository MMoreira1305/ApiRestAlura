package med.voll.api.controller;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.consulta.service.AgendaDeConsultas;
import med.voll.api.domain.medico.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

// Anotações para configuração do mock e também para utilizar o JacksonTester
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {

    // Classe para simular requisições http do mockito
    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;

    @MockBean
    private AgendaDeConsultas agendaDeConsultas;

    // Utilização para testar cenário 1 com o
    // WithMockUser para não cair no Forbidden quando estiver testando
    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informações estão inválidas")
    @WithMockUser
    void agendar_cenario1() throws Exception{
        var response = mvc.perform(post("/consultas"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    // Cenário 2
    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informações estão inválidas")
    @WithMockUser
    void agendar_cenario2() throws Exception{
        var data = LocalDateTime.now().plusHours(1);
        var especialidade = Especialidade.CARDIOLOGIA;
        var dadosDetalhamento = new DadosDetalhamentoConsulta(null, 1l, 1l, data);
        // Aqui utilizamos o when, pois como o "Mock Bean" acima do agendaDeConsultas
        // precisamos dizer qual vai ser o retorno dela
        when(agendaDeConsultas.agendar(any())).thenReturn(dadosDetalhamento);

        // Pegando o retorno, utilizando o mock mvc
        var response = mvc
                .perform(
                        // Porta
                        post("/consultas")
                                // Qual o tipo de retorno
                                .contentType(MediaType.APPLICATION_JSON)
                                // Conteúdo enviado no body
                                .content(dadosAgendamentoConsultaJson.write(
                                        new DadosAgendamentoConsulta(1l, 1l, data, especialidade)
                                ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        // Verificando se está retornando corretamente o json correto
        var jsonEsperado = dadosDetalhamentoConsultaJson.write(
                dadosDetalhamento
        ).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
}