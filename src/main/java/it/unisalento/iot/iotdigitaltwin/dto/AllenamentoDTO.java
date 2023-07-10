package it.unisalento.iot.iotdigitaltwin.dto;

public class AllenamentoDTO {
    String id;

    String nome;

    String tipologia;

    String intensita;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public String getIntensita() {
        return intensita;
    }

    public void setIntensita(String intensita) {
        this.intensita = intensita;
    }
}
