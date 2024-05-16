package com.allobank.allobackendtest.repository;

import com.allobank.allobackendtest.model.Partai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PartaiRepo extends JpaRepository<Partai, UUID> {
    Partai findByNamaPartai(String nama);
}

