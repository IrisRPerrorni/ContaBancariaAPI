package com.catalisa.contabancaria2.service;

import com.catalisa.contabancaria2.model.ContaBancariaModel;
import com.catalisa.contabancaria2.repository.ContaBancariaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ContaBancariaService {

    final
    ContaBancariaRepository contaBancariaRepository;

    public ContaBancariaService(ContaBancariaRepository contaBancariaRepository) {
        this.contaBancariaRepository = contaBancariaRepository;
    }

    public List<ContaBancariaModel> exibirTodos() {
        return contaBancariaRepository.findAll();

    }

    public ContaBancariaModel cadastrar(ContaBancariaModel contaBancariaModel) {
        return contaBancariaRepository.save(contaBancariaModel);
    }

    public Optional<ContaBancariaModel> exibirEspecifico(Long id) {
        return contaBancariaRepository.findById(id);

    }

    public void deletar(ContaBancariaModel contaBancariaModel) {

        contaBancariaRepository.delete(contaBancariaModel);
    }

    public ContaBancariaModel atualizacaoValor(Long id, ContaBancariaModel contaBancariaModel) {
        ContaBancariaModel conta = exibirEspecifico(id).get();
        if (contaBancariaModel.getTipoOperacao().equalsIgnoreCase("deposito")) {
            conta.setSaldo(conta.getSaldo() + contaBancariaModel.getValorOperacao());
            conta.setValorOperacao(contaBancariaModel.getValorOperacao());
            conta.setTipoOperacao("deposito");
        } else if (contaBancariaModel.getTipoOperacao().equalsIgnoreCase("saque")) {
            if (conta.getSaldo() >= conta.getValorOperacao()) {
                conta.setSaldo(conta.getSaldo() - contaBancariaModel.getValorOperacao());
                conta.setValorOperacao(contaBancariaModel.getValorOperacao());
                conta.setTipoOperacao("saque");

            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente, não é possivel sacar!");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operacao Invalida!");
        }
        conta.setSaldo(conta.getSaldo());
        return contaBancariaRepository.save(conta);
    }



}
