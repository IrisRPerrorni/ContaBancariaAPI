package com.catalisa.contabancaria2.controllerTest;

import com.catalisa.contabancaria2.controller.ContaBancariaController;
import com.catalisa.contabancaria2.dto.ContaBancariaDTO;
import com.catalisa.contabancaria2.model.ContaBancariaModel;
import com.catalisa.contabancaria2.service.ContaBancariaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ContaBancariaController.class)
public class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContaBancariaService contaBancariaService;


    @Test
    public void listarContaBancaria() throws Exception {

        //criando uma lista para retornar
        ContaBancariaModel conta1 = new ContaBancariaModel(1L, "54876", "0001",
                "Antonio", 0, 0, "deposito");
        ContaBancariaModel conta2 = new ContaBancariaModel(2L, "7845", "0001",
                "Bianca", 0, 50, "deposito");
        when(contaBancariaService.exibirTodos()).thenReturn(List.of(conta1, conta2));

        //configurando o mockMVC

        mockMvc.perform(get("/contabancaria2").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(Arrays.asList(conta1, conta2)))).andDo(print());

        //verifica se o metodo foi chamado uma vez
        verify(contaBancariaService, times(1)).exibirTodos();

    }

    @Test
    public void cadastrarContaTest() throws Exception {
        ContaBancariaDTO criarConta = new ContaBancariaDTO();
        criarConta.setNumeroConta("54876");
        criarConta.setAgencia("0001");
        criarConta.setNomeUsuario("Antonio");
        criarConta.setValorOperacao(50);
        criarConta.setTipoOperacao("deposito");

        ContaBancariaModel criarContaModel = new ContaBancariaModel();
        BeanUtils.copyProperties(criarConta, criarContaModel);
        criarContaModel.setSaldo(0);
        criarContaModel.setValorOperacao(0);
        criarContaModel.setTipoOperacao(null);

        Mockito.when(contaBancariaService.cadastrar(any(ContaBancariaModel.class))).thenReturn(criarContaModel);

        mockMvc.perform(post("/contabancaria2").contentType("application/json")
                        .content(objectMapper.writeValueAsString(criarConta)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(criarContaModel)));
    }

    @Test
    public void testDeletarContaExistente() throws Exception {
        Long contaId = 1L;
        ContaBancariaModel conta1 = new ContaBancariaModel(1L, "54876", "0001",
                "Antonio", 0, 0, "deposito");
        Optional<ContaBancariaModel> contaOptional = Optional.of(conta1);
        when(contaBancariaService.exibirEspecifico(contaId)).thenReturn(contaOptional);
        mockMvc.perform(delete("/contabancaria2/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Conta deletada com sucesso"));
        verify(contaBancariaService, times(1)).deletar(contaOptional.get());

    }

    @Test
    public void testDeletarContaNaoExistente() throws Exception {
        Long id = 1L;
        Optional<ContaBancariaModel> contaOptional = Optional.empty();
        when(contaBancariaService.exibirEspecifico(id)).thenReturn(contaOptional);

        mockMvc.perform(delete("/contabancaria2/{id}", id).
                        contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Conta não encontrada"));
        verify(contaBancariaService, never()).deletar(any(ContaBancariaModel.class));
    }

    @Test
    public void exibirContaExistente() throws Exception {
        Long contaId = 1L;
        ContaBancariaModel conta1 = new ContaBancariaModel(1L, "54876", "0001",
                "Antonio", 0, 0, "deposito");
        Optional<ContaBancariaModel> contaOptional = Optional.of(conta1);
        when(contaBancariaService.exibirEspecifico(contaId)).thenReturn(contaOptional);
        mockMvc.perform(get("/contabancaria2/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());

        verify(contaBancariaService, times(1)).exibirEspecifico(contaId);

    }
    @Test
    public void testAlterandoValorSaldo() throws Exception {
        Long contaId = 1L;
        ContaBancariaDTO contaBancariaDTO = new ContaBancariaDTO();
        contaBancariaDTO.setValorOperacao(100);
        contaBancariaDTO.setTipoOperacao("deposito");

        ContaBancariaModel contaAntiga = new ContaBancariaModel(1L, "54876", "0001",
                "Antonio", 0, 0, "deposito");


        when(contaBancariaService.exibirEspecifico(contaId)).thenReturn(Optional.of(contaAntiga));
        when(contaBancariaService.atualizacaoValor(eq(contaId), any(ContaBancariaModel.class))).thenReturn(contaAntiga);

        mockMvc.perform(MockMvcRequestBuilders.put("/contabancaria2/{id}", contaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contaBancariaDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.saldo").value(contaAntiga.getSaldo()));

        verify(contaBancariaService, times(1)).atualizacaoValor(eq(contaId), any(ContaBancariaModel.class));
    }
}
