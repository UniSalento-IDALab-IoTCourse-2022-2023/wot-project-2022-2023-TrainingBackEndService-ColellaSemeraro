package it.unisalento.iot.iotdigitaltwin.repositories;

import it.unisalento.iot.iotdigitaltwin.domain.Allenamento;
import it.unisalento.iot.iotdigitaltwin.domain.AssegnazioneAllenamento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface AssegnazioneAllenamentoRepository extends MongoRepository<AssegnazioneAllenamento, String> {

    List<AssegnazioneAllenamento> findAllByIdAllenamento(String idAllenamento);

    List<AssegnazioneAllenamento> findAllByIdAtleta(String idAtleta);

    List<AssegnazioneAllenamento> findAllByIdAllenamentoAndIdAtleta(String idAllenamento, String idAtleta);

    List<AssegnazioneAllenamento> findAllByIdAtletaAndDataAssegnazione(String idAtleta, LocalDate dataAssegnazione);
}
