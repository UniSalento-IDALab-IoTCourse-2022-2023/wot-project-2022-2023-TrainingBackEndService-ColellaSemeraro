package it.unisalento.iot.iotdigitaltwin.restcontrollers;

import it.unisalento.iot.iotdigitaltwin.domain.AssegnazioneAllenamento;
import it.unisalento.iot.iotdigitaltwin.dto.AssegnazioneAllenamentoDTO;
import it.unisalento.iot.iotdigitaltwin.repositories.AssegnazioneAllenamentoRepository;
import it.unisalento.iot.iotdigitaltwin.service.APICalls;
import it.unisalento.iot.iotdigitaltwin.service.RoleResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/api/assegnazioni")
public class AssegnazioneAllenamentoRestController {

    @Autowired
    AssegnazioneAllenamentoRepository assegnazioneAllenamentoRepository;

    @Autowired
    APICalls apiCalls;

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AssegnazioneAllenamentoDTO assegnaAllenamento(HttpServletRequest request, @RequestBody AssegnazioneAllenamentoDTO assegnazioneAllenamentoDTO) {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            RoleResponse roleResponse = getRoleResponse(jwt);
            System.out.println(roleResponse);

            // Verifica se l'autenticazione è valida
            if (roleResponse != null && !Objects.equals(roleResponse.getRole(), "Authentication failed")) {
                // Esegui le operazioni del metodo solo se l'autenticazione è valida

                // Esempio: Controlla se l'utente ha il ruolo necessario per eseguire questa operazione
                if (roleResponse.getRole().equals("COACH")) {

                    AssegnazioneAllenamento assegnazioneAllenamento = new AssegnazioneAllenamento();

                    assegnazioneAllenamento.setIdAllenamento(assegnazioneAllenamentoDTO.getIdAllenamento());
                    assegnazioneAllenamento.setIdAtleta(assegnazioneAllenamentoDTO.getIdAtleta());
                    assegnazioneAllenamento.setDurataInMinuti(assegnazioneAllenamentoDTO.getDurataInMinuti());
                    assegnazioneAllenamento.setNumeroCircuiti(assegnazioneAllenamentoDTO.getNumeroCircuiti());
                    assegnazioneAllenamento.setDataAssegnazione(assegnazioneAllenamentoDTO.getDataAssegnazione());
                    assegnazioneAllenamento.setIdRisultatoPrecedente(assegnazioneAllenamentoDTO.getIdRisultatoPrecedente());

                    assegnazioneAllenamentoRepository.save(assegnazioneAllenamento);

                    assegnazioneAllenamentoDTO.setId(assegnazioneAllenamento.getId());

                    return assegnazioneAllenamentoDTO;
                } else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
                }
            }else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticazione fallita");
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
    }

    @GetMapping(value = "/existsByIdRisultatoPrecedente/{idRisultatoPrecedente}")
    public boolean existsByRisultatoPrecedente(HttpServletRequest request, @PathVariable String idRisultatoPrecedente) {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            RoleResponse roleResponse = getRoleResponse(jwt);
            System.out.println(roleResponse);

            // Verifica se l'autenticazione è valida
            if (roleResponse != null && !Objects.equals(roleResponse.getRole(), "Authentication failed")) {
                // Esegui le operazioni del metodo solo se l'autenticazione è valida

                // Esempio: Controlla se l'utente ha il ruolo necessario per eseguire questa operazione
                if (roleResponse.getRole().equals("COACH")) {

                    return assegnazioneAllenamentoRepository.existsByIdRisultatoPrecedente(idRisultatoPrecedente);

                } else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
                }
            }else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticazione fallita");
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");

    }

    private RoleResponse getRoleResponse(String jwt) {
        return apiCalls.checkRole(jwt);
    }
}
