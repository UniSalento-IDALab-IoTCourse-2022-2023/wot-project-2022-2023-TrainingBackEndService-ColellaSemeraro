package it.unisalento.iot.iotdigitaltwin.repositories;

import it.unisalento.iot.iotdigitaltwin.domain.Allenamento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AllenamentoRepository extends MongoRepository<Allenamento, String> {
    Optional<Allenamento> findByNome(String nome);

    List<Allenamento> findAllByTipologia(String tipologia);
}
