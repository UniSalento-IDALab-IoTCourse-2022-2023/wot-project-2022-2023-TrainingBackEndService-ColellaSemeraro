package it.unisalento.iot.iotdigitaltwin.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("assegnazione")

public class AssegnazioneAllenamento {

    @Id
    String id;

    String idAllenamento;

    String idAtleta;

    int indiceSforzo;

    int numeroCircuiti;

    int durataInMinuti;

    int calorieConsumate;

    public enum Intensita{
        Bassa, Media, Elevata
    }

    Intensita intensita;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate dataAssegnazione;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdAllenamento() {
        return idAllenamento;
    }

    public void setIdAllenamento(String idAllenamento) {
        this.idAllenamento = idAllenamento;
    }

    public String getIdAtleta() {
        return idAtleta;
    }

    public void setIdAtleta(String idAtleta) {
        this.idAtleta = idAtleta;
    }

    public int getIndiceSforzo() {
        return indiceSforzo;
    }

    public void setIndiceSforzo(int indiceSforzo) {
        this.indiceSforzo = indiceSforzo;
    }

    public int getNumeroCircuiti() {
        return numeroCircuiti;
    }

    public void setNumeroCircuiti(int numeroCircuiti) {
        this.numeroCircuiti = numeroCircuiti;
    }

    public int getDurataInMinuti() {
        return durataInMinuti;
    }

    public void setDurataInMinuti(int durataInMinuti) {
        this.durataInMinuti = durataInMinuti;
    }

    public int getCalorieConsumate() {
        return calorieConsumate;
    }

    public void setCalorieConsumate(int calorieConsumate) {
        this.calorieConsumate = calorieConsumate;
    }

    public LocalDate getDataAssegnazione() {
        return dataAssegnazione;
    }

    public void setDataAssegnazione(LocalDate dataAssegnazione) {
        this.dataAssegnazione = dataAssegnazione;
    }

    public Intensita getIntensita() {
        return intensita;
    }

    public void setIntensita(Intensita intensita) {
        this.intensita = intensita;
    }
}
