package it.unisalento.iot.iotdigitaltwin.restcontrollers;

import it.unisalento.iot.iotdigitaltwin.domain.Allenamento;
import it.unisalento.iot.iotdigitaltwin.dto.AllenamentoDTO;
import it.unisalento.iot.iotdigitaltwin.repositories.AllenamentoRepository;
import it.unisalento.iot.iotdigitaltwin.service.APICalls;
import it.unisalento.iot.iotdigitaltwin.service.RoleResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/allenamenti")
public class AllenamentoRestController {

    @Autowired
    AllenamentoRepository allenamentoRepository;

    @Autowired
    APICalls apiCalls;

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AllenamentoDTO creaAllenamento(HttpServletRequest request, @RequestBody AllenamentoDTO allenamentoDTO){

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            RoleResponse roleResponse = getRoleResponse(jwt);
            System.out.println(roleResponse);

            // Verifica se l'autenticazione è valida
            if (roleResponse != null && !Objects.equals(roleResponse.getRole(), "Authentication failed")) {
                // Esegui le operazioni del metodo solo se l'autenticazione è valida

                // Esempio: Controlla se l'utente ha il ruolo necessario per eseguire questa operazione
                if (roleResponse.getRole().equals("AMMINISTRATORE")) {
                    Allenamento allenamento = new Allenamento();

                    allenamento.setNome(allenamentoDTO.getNome());
                    allenamento.setTipologia(allenamentoDTO.getTipologia());

                    allenamentoRepository.save(allenamento);

                    allenamentoDTO.setId(allenamento.getId());

                    return allenamentoDTO;
                }else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
                }
            }else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticazione fallita");
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
    }


    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public List<AllenamentoDTO> trovaTutti(){

        List<AllenamentoDTO> allenamenti = new ArrayList<>();

        for (Allenamento allenamento : allenamentoRepository.findAll()){

            AllenamentoDTO allenamentoDTO = new AllenamentoDTO();
            allenamentoDTO.setId(allenamento.getId());
            allenamentoDTO.setNome(allenamentoDTO.getNome());
            allenamentoDTO.setTipologia(allenamento.getTipologia());

            allenamenti.add(allenamentoDTO);
        }

        return allenamenti;
    }

    @RequestMapping(value = "/findById/{idAllenamento}", method = RequestMethod.GET)
    public AllenamentoDTO trovaPerId(@PathVariable String idAllenamento){

        AllenamentoDTO allenamentoDTO = new AllenamentoDTO();
        Optional<Allenamento> optAllenamento = allenamentoRepository.findById(idAllenamento);

        if (optAllenamento.isPresent()){
            Allenamento allenamento = optAllenamento.get();

            allenamentoDTO.setId(allenamento.getId());
            allenamentoDTO.setNome(allenamento.getNome());
            allenamentoDTO.setTipologia(allenamento.getTipologia());
        }

        return allenamentoDTO;
    }

    @RequestMapping(value = "/findByName/{nome}", method = RequestMethod.GET)
    public AllenamentoDTO trovaPerNome(@PathVariable String nome){

        AllenamentoDTO allenamentoDTO = new AllenamentoDTO();
        Optional<Allenamento> optAllenamento = allenamentoRepository.findByNome(nome);

        if (optAllenamento.isPresent()){
            Allenamento allenamento = optAllenamento.get();

            allenamentoDTO.setId(allenamento.getId());
            allenamentoDTO.setNome(allenamento.getNome());
            allenamentoDTO.setTipologia(allenamento.getTipologia());
        }

        return allenamentoDTO;
    }


    @RequestMapping(value = "/findAllByType/{tipologia}", method = RequestMethod.GET)
    public List<AllenamentoDTO> trovaPerTipologia(@PathVariable String tipologia){

        List<AllenamentoDTO> allenamenti = new ArrayList<>();

        for (Allenamento allenamento : allenamentoRepository.findAllByTipologia(tipologia)){
            AllenamentoDTO allenamentoDTO = new AllenamentoDTO();

            allenamentoDTO.setId(allenamento.getId());
            allenamentoDTO.setNome(allenamento.getNome());
            allenamentoDTO.setTipologia(allenamento.getTipologia());

            allenamenti.add(allenamentoDTO);
        }

        return allenamenti;
    }


    @PatchMapping(value = "/changeTrainName/{nome}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AllenamentoDTO cambiaNomeAllenamento(@PathVariable String nome, @RequestBody String newName){

        AllenamentoDTO allenamentoDTO = new AllenamentoDTO();
        Optional<Allenamento> optionalAllenamento = allenamentoRepository.findByNome(nome);

        if (optionalAllenamento.isPresent()){

            Allenamento allenamento = optionalAllenamento.get();
            allenamento.setNome(newName);
            allenamentoRepository.save(allenamento);

            allenamentoDTO.setId(allenamento.getId());
            allenamentoDTO.setNome(allenamento.getNome());
            allenamentoDTO.setTipologia(allenamento.getTipologia());
        }

        return allenamentoDTO;
    }


    @PatchMapping(value = "/changeTypeByName/{nome}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AllenamentoDTO cambiaTipologiaAllenamento(@PathVariable String nome, @RequestBody String tipologia){

        AllenamentoDTO allenamentoDTO = new AllenamentoDTO();
        Optional<Allenamento> optionalAllenamento = allenamentoRepository.findByNome(nome);

        if (optionalAllenamento.isPresent()){

            Allenamento allenamento = optionalAllenamento.get();
            allenamento.setTipologia(tipologia);
            allenamentoRepository.save(allenamento);

            allenamentoDTO.setId(allenamento.getId());
            allenamentoDTO.setNome(allenamento.getNome());
            allenamentoDTO.setTipologia(allenamento.getTipologia());
        }

        return allenamentoDTO;
    }

    private RoleResponse getRoleResponse(String jwt) {
        return apiCalls.checkRole(jwt);
    }

}
