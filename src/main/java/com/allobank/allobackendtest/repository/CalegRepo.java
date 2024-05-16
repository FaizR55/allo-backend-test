package com.allobank.allobackendtest.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allobank.allobackendtest.model.Caleg;

@Repository
public interface CalegRepo extends JpaRepository<Caleg, UUID>  {
    List<Caleg> findByDapilId(UUID dapilId);
    List<Caleg> findByPartaiId(UUID partaiId);
    List<Caleg> findByPartaiNamaPartai(String nama_partai);
    List<Caleg> findByDapilNamaDapil(String nama_dapil);
    Caleg findByNama(String nama);
}
