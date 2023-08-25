package com.catalisa.contabancaria2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "Conta_bancaria2")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContaBancariaModel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 8)
    private String numeroConta;
    @Column(nullable = false, length = 4)
    private String agencia;
    @Column(nullable = false, length = 100)
    private String nomeUsuario;
    @Column
    private double saldo ;
    @Column
    private double valorOperacao;
    @Column
    private String tipoOperacao;



}
