package com.catalisa.contabancaria2.repository;

import com.catalisa.contabancaria2.model.ContaBancariaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancariaModel,Long> {


}
