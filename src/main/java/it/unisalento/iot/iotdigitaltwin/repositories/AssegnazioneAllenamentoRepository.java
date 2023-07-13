package it.unisalento.iot.iotdigitaltwin.repositories;

import it.unisalento.iot.iotdigitaltwin.domain.Allenamento;
import it.unisalento.iot.iotdigitaltwin.domain.AssegnazioneAllenamento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AssegnazioneAllenamentoRepository extends MongoRepository<AssegnazioneAllenamento, String> {

    List<AssegnazioneAllenamento> findAllByIdAtleta(String idAtleta);

    boolean existsByIdRisultatoPrecedente(String idRisultatoPrecedente);

    Optional<AssegnazioneAllenamento> findByIdRisultatoPrecedente(String idRisultatoPrecedente);
}
