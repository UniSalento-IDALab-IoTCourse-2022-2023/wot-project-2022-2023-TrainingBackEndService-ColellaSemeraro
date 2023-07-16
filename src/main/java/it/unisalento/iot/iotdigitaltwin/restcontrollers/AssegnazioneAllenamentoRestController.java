package it.unisalento.iot.iotdigitaltwin.restcontrollers;

import it.unisalento.iot.iotdigitaltwin.domain.Allenamento;
import it.unisalento.iot.iotdigitaltwin.domain.AssegnazioneAllenamento;
import it.unisalento.iot.iotdigitaltwin.dto.AllenamentoDTO;
import it.unisalento.iot.iotdigitaltwin.dto.AssegnazioneAllenamentoDTO;
import it.unisalento.iot.iotdigitaltwin.repositories.AssegnazioneAllenamentoRepository;
import it.unisalento.iot.iotdigitaltwin.service.APICalls;
import it.unisalento.iot.iotdigitaltwin.service.RoleResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/assegnazioni")
public class AssegnazioneAllenamentoRestController {

    @Autowired
    AssegnazioneAllenamentoRepository assegnazioneAllenamentoRepository;

    @Autowired
    APICalls apiCalls;

    @RequestMapping(value = "/{idCoach}/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AssegnazioneAllenamentoDTO assegnaAllenamento(HttpServletRequest request, @RequestBody AssegnazioneAllenamentoDTO assegnazioneAllenamentoDTO, @PathVariable String idCoach) {

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

                    String url = "https://x7oeqezkzi.execute-api.us-east-1.amazonaws.com/dev/api/users/atleta/findIdCoachByIdAtleta/" + assegnazioneAllenamentoDTO.getIdAtleta();
                    RestTemplate restTemplate = new RestTemplate();

                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer "+jwt);

                    HttpEntity<Object> entity = new HttpEntity<>(headers);

                    ResponseEntity<String> result = restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<>() {
                            }
                    );

                    String coach = result.getBody();

                    if (coach.equals(idCoach)){
                        AssegnazioneAllenamento assegnazioneAllenamento = new AssegnazioneAllenamento();

                        assegnazioneAllenamento.setIdAllenamento(assegnazioneAllenamentoDTO.getIdAllenamento());
                        assegnazioneAllenamento.setIdAtleta(assegnazioneAllenamentoDTO.getIdAtleta());
                        assegnazioneAllenamento.setDurataInMinuti(assegnazioneAllenamentoDTO.getDurataInMinuti());
                        assegnazioneAllenamento.setNumeroCircuiti(assegnazioneAllenamentoDTO.getNumeroCircuiti());
                        assegnazioneAllenamento.setDataAssegnazione(assegnazioneAllenamentoDTO.getDataAssegnazione());
                        assegnazioneAllenamento.setIdRisultatoPrecedente(assegnazioneAllenamentoDTO.getIdRisultatoPrecedente());
                        assegnazioneAllenamento.setDurataCircuiti(assegnazioneAllenamentoDTO.getDurataCircuiti());

                        assegnazioneAllenamentoRepository.save(assegnazioneAllenamento);

                        assegnazioneAllenamentoDTO.setId(assegnazioneAllenamento.getId());

                        return assegnazioneAllenamentoDTO;
                    }else{
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ID Coach non corrisponde");
                    }
                }else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
                }
            }else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticazione fallita");
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
    }

    @GetMapping(value = "/findById/{id}")
    public AssegnazioneAllenamentoDTO trovaAssegnazionePerId(HttpServletRequest request, @PathVariable String id){
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            RoleResponse roleResponse = getRoleResponse(jwt);

            // Verifica se l'autenticazione è valida
            if (roleResponse != null && !Objects.equals(roleResponse.getRole(), "Authentication failed")) {
                // Esegui le operazioni del metodo solo se l'autenticazione è valida

                // Esempio: Controlla se l'utente ha il ruolo necessario per eseguire questa operazione
                if (roleResponse.getRole().equals("COACH") || roleResponse.getRole().equals("ATLETA")) {

                    AssegnazioneAllenamentoDTO assegnazioneAllenamentoDTO = new AssegnazioneAllenamentoDTO();
                    Optional<AssegnazioneAllenamento> optionalAssegnazioneAllenamento = assegnazioneAllenamentoRepository.findById(id);

                    if (optionalAssegnazioneAllenamento.isPresent()){
                        AssegnazioneAllenamento assegnazioneAllenamento = optionalAssegnazioneAllenamento.get();

                        assegnazioneAllenamentoDTO.setId(assegnazioneAllenamento.getId());
                        assegnazioneAllenamentoDTO.setDataAssegnazione(assegnazioneAllenamento.getDataAssegnazione());
                        assegnazioneAllenamentoDTO.setIdAllenamento(assegnazioneAllenamento.getIdAllenamento());
                        assegnazioneAllenamentoDTO.setIdAtleta(assegnazioneAllenamento.getIdAtleta());
                        assegnazioneAllenamentoDTO.setIdRisultatoPrecedente(assegnazioneAllenamento.getIdRisultatoPrecedente());
                        assegnazioneAllenamentoDTO.setNumeroCircuiti(assegnazioneAllenamento.getNumeroCircuiti());
                        assegnazioneAllenamentoDTO.setDurataCircuiti(assegnazioneAllenamento.getDurataCircuiti());
                        assegnazioneAllenamentoDTO.setDurataInMinuti(assegnazioneAllenamento.getDurataInMinuti());
                    }

                    return assegnazioneAllenamentoDTO;
                }else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
                }
            }else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticazione fallita");
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
    }

    @GetMapping(value = "/findByMostRecentId/{idRisultatoPrecedente}")
    public AssegnazioneAllenamentoDTO trovaAssegnazionePerIdPiuRecente(HttpServletRequest request, @PathVariable String idRisultatoPrecedente){
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            RoleResponse roleResponse = getRoleResponse(jwt);

            // Verifica se l'autenticazione è valida
            if (roleResponse != null && !Objects.equals(roleResponse.getRole(), "Authentication failed")) {
                // Esegui le operazioni del metodo solo se l'autenticazione è valida

                // Esempio: Controlla se l'utente ha il ruolo necessario per eseguire questa operazione
                if (roleResponse.getRole().equals("COACH") || roleResponse.getRole().equals("ATLETA")) {

                    AssegnazioneAllenamentoDTO assegnazioneAllenamentoDTO = new AssegnazioneAllenamentoDTO();
                    Optional<AssegnazioneAllenamento> optionalAssegnazioneAllenamento = assegnazioneAllenamentoRepository.findByIdRisultatoPrecedente(idRisultatoPrecedente);

                    if (optionalAssegnazioneAllenamento.isPresent()){
                        AssegnazioneAllenamento assegnazioneAllenamento = optionalAssegnazioneAllenamento.get();

                        assegnazioneAllenamentoDTO.setId(assegnazioneAllenamento.getId());
                        assegnazioneAllenamentoDTO.setDataAssegnazione(assegnazioneAllenamento.getDataAssegnazione());
                        assegnazioneAllenamentoDTO.setIdAllenamento(assegnazioneAllenamento.getIdAllenamento());
                        assegnazioneAllenamentoDTO.setIdAtleta(assegnazioneAllenamento.getIdAtleta());
                        assegnazioneAllenamentoDTO.setIdRisultatoPrecedente(assegnazioneAllenamento.getIdRisultatoPrecedente());
                        assegnazioneAllenamentoDTO.setNumeroCircuiti(assegnazioneAllenamento.getNumeroCircuiti());
                        assegnazioneAllenamentoDTO.setDurataCircuiti(assegnazioneAllenamento.getDurataCircuiti());
                        assegnazioneAllenamentoDTO.setDurataInMinuti(assegnazioneAllenamento.getDurataInMinuti());
                    }

                    return assegnazioneAllenamentoDTO;
                }else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
                }
            }else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticazione fallita");
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
    }

    @RequestMapping(value = "/findAllByAtletaId/{idAtleta}", method = RequestMethod.GET)
    public List<AssegnazioneAllenamentoDTO> trovaTutteAssegnazioniDiUnAtleta(HttpServletRequest request, @PathVariable String idAtleta){

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            RoleResponse roleResponse = getRoleResponse(jwt);

            // Verifica se l'autenticazione è valida
            if (roleResponse != null && !Objects.equals(roleResponse.getRole(), "Authentication failed")) {
                // Esegui le operazioni del metodo solo se l'autenticazione è valida

                // Esempio: Controlla se l'utente ha il ruolo necessario per eseguire questa operazione
                if (roleResponse.getRole().equals("ATLETA") || roleResponse.getRole().equals("COACH")) {

                    List<AssegnazioneAllenamentoDTO> assegnazioni = new ArrayList<>();

                    for (AssegnazioneAllenamento assegnazioneAllenamento : assegnazioneAllenamentoRepository.findAllByIdAtleta(idAtleta)){

                        AssegnazioneAllenamentoDTO assegnazioneAllenamentoDTO = new AssegnazioneAllenamentoDTO();

                        assegnazioneAllenamentoDTO.setId(assegnazioneAllenamento.getId());
                        assegnazioneAllenamentoDTO.setDataAssegnazione(assegnazioneAllenamento.getDataAssegnazione());
                        assegnazioneAllenamentoDTO.setIdAllenamento(assegnazioneAllenamento.getIdAllenamento());
                        assegnazioneAllenamentoDTO.setIdAtleta(assegnazioneAllenamento.getIdAtleta());
                        assegnazioneAllenamentoDTO.setIdRisultatoPrecedente(assegnazioneAllenamento.getIdRisultatoPrecedente());
                        assegnazioneAllenamentoDTO.setNumeroCircuiti(assegnazioneAllenamento.getNumeroCircuiti());
                        assegnazioneAllenamentoDTO.setDurataCircuiti(assegnazioneAllenamento.getDurataCircuiti());
                        assegnazioneAllenamentoDTO.setDurataInMinuti(assegnazioneAllenamento.getDurataInMinuti());

                        assegnazioni.add(assegnazioneAllenamentoDTO);
                    }

                    return assegnazioni;
                }else {
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


    @RequestMapping(value = "/findMostRecentByIdAtleta/{idAtleta}/", method = RequestMethod.GET)
    public AssegnazioneAllenamentoDTO trovaAssegnazionePiuRecentePerAtleta(HttpServletRequest request, @PathVariable String idAtleta) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            RoleResponse roleResponse = getRoleResponse(jwt);

            // Verifica se l'autenticazione è valida
            if (roleResponse != null && !Objects.equals(roleResponse.getRole(), "Authentication failed")) {
                // Esegui le operazioni del metodo solo se l'autenticazione è valida

                // Esempio: Controlla se l'utente ha il ruolo necessario per eseguire questa operazione
                if (roleResponse.getRole().equals("ATLETA") || roleResponse.getRole().equals("COACH")) {

                    List<AssegnazioneAllenamentoDTO> assegnazioni = trovaTutteAssegnazioniDiUnAtleta(request, idAtleta);
                    Collections.sort(assegnazioni, Comparator.comparing(AssegnazioneAllenamentoDTO::getDataAssegnazione).reversed());

                    if (!assegnazioni.isEmpty()) {
                        AssegnazioneAllenamentoDTO assegnazioneAllenamentoDTO = new AssegnazioneAllenamentoDTO();

                        assegnazioneAllenamentoDTO.setId(assegnazioni.get(0).getId());
                        assegnazioneAllenamentoDTO.setDataAssegnazione(assegnazioni.get(0).getDataAssegnazione());
                        assegnazioneAllenamentoDTO.setIdAllenamento(assegnazioni.get(0).getIdAllenamento());
                        assegnazioneAllenamentoDTO.setIdAtleta(assegnazioni.get(0).getIdAtleta());
                        assegnazioneAllenamentoDTO.setIdRisultatoPrecedente(assegnazioni.get(0).getIdRisultatoPrecedente());
                        assegnazioneAllenamentoDTO.setNumeroCircuiti(assegnazioni.get(0).getNumeroCircuiti());
                        assegnazioneAllenamentoDTO.setDurataCircuiti(assegnazioni.get(0).getDurataCircuiti());
                        assegnazioneAllenamentoDTO.setDurataInMinuti(assegnazioni.get(0).getDurataInMinuti());


                        return assegnazioneAllenamentoDTO;
                    }
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
