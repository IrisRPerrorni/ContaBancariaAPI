package com.catalisa.contabancaria2.controller;

import com.catalisa.contabancaria2.dto.ContaBancariaDTO;
import com.catalisa.contabancaria2.model.ContaBancariaModel;
import com.catalisa.contabancaria2.service.ContaBancariaService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class ContaBancariaController {
    final ContaBancariaService contaBancariaService;

    public ContaBancariaController(ContaBancariaService contaBancariaService) {
        this.contaBancariaService = contaBancariaService;
    }
    //testado
    @PostMapping(path = "/contabancaria2")
    public ResponseEntity<Object> cadastrarConta(@RequestBody ContaBancariaDTO contaBancariaDTO){
        ContaBancariaModel contaBancariaModel = new ContaBancariaModel();
        BeanUtils.copyProperties(contaBancariaDTO,contaBancariaModel);
        contaBancariaModel.setSaldo(0);
        contaBancariaModel.setValorOperacao(0);
        contaBancariaModel.setTipoOperacao(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(contaBancariaService.cadastrar(contaBancariaModel));

    }
//testado
    @GetMapping(path = "/contabancaria2")
    public ResponseEntity<List<ContaBancariaModel>>exibirTodasContas(){
        return ResponseEntity.status(HttpStatus.OK).body(contaBancariaService.exibirTodos());
    }
//testado
    @GetMapping(path = "/contabancaria2/{id}")
    public ResponseEntity<Object>verificarContaEspecifica(@PathVariable(value = "id")Long id){
        Optional<ContaBancariaModel>contaBancariaModel = contaBancariaService.exibirEspecifico(id);
        if (!contaBancariaModel.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(contaBancariaModel.get());
    }
    //testado
    @DeleteMapping (path = "/contabancaria2/{id}")
    public ResponseEntity<Object>deletarConta(@PathVariable (value = "id") Long id){
        Optional<ContaBancariaModel>contaBancariaModelOptional= contaBancariaService.exibirEspecifico(id);
        if (!contaBancariaModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada");

        }
        contaBancariaService.deletar(contaBancariaModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Conta deletada com sucesso");
    }
    @PutMapping("/contabancaria2/{id}")
    public ResponseEntity<ContaBancariaModel> alterandoValorSaldo(@PathVariable Long id ,
                                                                  @RequestBody ContaBancariaDTO contaBancariaDTO) {
        ContaBancariaModel contaBancariaModel = new ContaBancariaModel();
        BeanUtils.copyProperties(contaBancariaDTO,contaBancariaModel);
        contaBancariaModel.setValorOperacao(contaBancariaDTO.getValorOperacao());
        contaBancariaModel.setTipoOperacao(contaBancariaDTO.getTipoOperacao());
        contaBancariaModel.setSaldo(contaBancariaModel.getSaldo());
        return ResponseEntity.status(HttpStatus.OK).body(contaBancariaService.atualizacaoValor(id,contaBancariaModel));


    }







}
