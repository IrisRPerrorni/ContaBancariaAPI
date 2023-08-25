package com.catalisa.contabancaria2.dto;

import com.catalisa.contabancaria2.model.ContaBancariaModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
@Getter
@Setter
public class ContaBancariaDTO {
    private String numeroConta;

    private String agencia;

    private String nomeUsuario;

    private double valorOperacao;

    private String tipoOperacao;




}
