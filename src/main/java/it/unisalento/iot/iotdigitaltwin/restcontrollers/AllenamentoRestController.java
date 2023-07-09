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

            // Verifica se l'autenticazione è valida
            if (roleResponse != null && !Objects.equals(roleResponse.getRole(), "Authentication failed")) {
                // Esegui le operazioni del metodo solo se l'autenticazione è valida

                // Esempio: Controlla se l'utente ha il ruolo necessario per eseguire questa operazione
                if (roleResponse.getRole().equals("AMMINISTRATORE")) {

                    if (!allenamentoRepository.existsByNomeAndTipologia(allenamentoDTO.getNome(), allenamentoDTO.getTipologia())){

                        Allenamento allenamento = new Allenamento();

                        allenamento.setNome(allenamentoDTO.getNome());
                        allenamento.setTipologia(allenamentoDTO.getTipologia());

                        allenamentoRepository.save(allenamento);

                        allenamentoDTO.setId(allenamento.getId());

                        return allenamentoDTO;
                    }else{
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Allenamento già esistente");
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


    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public List<AllenamentoDTO> trovaTutti(HttpServletRequest request){

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            RoleResponse roleResponse = getRoleResponse(jwt);

            // Verifica se l'autenticazione è valida
            if (roleResponse != null && !Objects.equals(roleResponse.getRole(), "Authentication failed")) {
                // Esegui le operazioni del metodo solo se l'autenticazione è valida

                // Esempio: Controlla se l'utente ha il ruolo necessario per eseguire questa operazione
                if (roleResponse.getRole().equals("AMMINISTRATORE") || roleResponse.getRole().equals("COACH")) {

                    List<AllenamentoDTO> allenamenti = new ArrayList<>();

                    for (Allenamento allenamento : allenamentoRepository.findAll()){

                        AllenamentoDTO allenamentoDTO = new AllenamentoDTO();

                        allenamentoDTO.setId(allenamento.getId());
                        allenamentoDTO.setNome(allenamento.getNome());
                        allenamentoDTO.setTipologia(allenamento.getTipologia());

                        allenamenti.add(allenamentoDTO);
                    }

                    return allenamenti;
                }else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
                }
            }else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticazione fallita");
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
    }


    @RequestMapping(value = "/findById/{idAllenamento}", method = RequestMethod.GET)
    public AllenamentoDTO trovaPerId(HttpServletRequest request, @PathVariable String idAllenamento){

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            RoleResponse roleResponse = getRoleResponse(jwt);

            // Verifica se l'autenticazione è valida
            if (roleResponse != null && !Objects.equals(roleResponse.getRole(), "Authentication failed")) {
                // Esegui le operazioni del metodo solo se l'autenticazione è valida

                // Esempio: Controlla se l'utente ha il ruolo necessario per eseguire questa operazione
                if (roleResponse.getRole().equals("AMMINISTRATORE") || roleResponse.getRole().equals("COACH") || roleResponse.getRole().equals("ATLETA")) {

                    AllenamentoDTO allenamentoDTO = new AllenamentoDTO();
                    Optional<Allenamento> optAllenamento = allenamentoRepository.findById(idAllenamento);

                    if (optAllenamento.isPresent()){
                        Allenamento allenamento = optAllenamento.get();

                        allenamentoDTO.setId(allenamento.getId());
                        allenamentoDTO.setNome(allenamento.getNome());
                        allenamentoDTO.setTipologia(allenamento.getTipologia());
                    }

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


    @RequestMapping(value = "/findByName/{nome}", method = RequestMethod.GET)
    public AllenamentoDTO trovaPerNome(HttpServletRequest request, @PathVariable String nome){
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            RoleResponse roleResponse = getRoleResponse(jwt);

            // Verifica se l'autenticazione è valida
            if (roleResponse != null && !Objects.equals(roleResponse.getRole(), "Authentication failed")) {
                // Esegui le operazioni del metodo solo se l'autenticazione è valida

                // Esempio: Controlla se l'utente ha il ruolo necessario per eseguire questa operazione
                if (roleResponse.getRole().equals("AMMINISTRATORE") || roleResponse.getRole().equals("COACH")) {

                    AllenamentoDTO allenamentoDTO = new AllenamentoDTO();
                    Optional<Allenamento> optAllenamento = allenamentoRepository.findByNome(nome);

                    if (optAllenamento.isPresent()){
                        Allenamento allenamento = optAllenamento.get();

                        allenamentoDTO.setId(allenamento.getId());
                        allenamentoDTO.setNome(allenamento.getNome());
                        allenamentoDTO.setTipologia(allenamento.getTipologia());
                    }

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


    @RequestMapping(value = "/findAllByType/{tipologia}", method = RequestMethod.GET)
    public List<AllenamentoDTO> trovaPerTipologia(HttpServletRequest request, @PathVariable String tipologia){

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            RoleResponse roleResponse = getRoleResponse(jwt);

            // Verifica se l'autenticazione è valida
            if (roleResponse != null && !Objects.equals(roleResponse.getRole(), "Authentication failed")) {
                // Esegui le operazioni del metodo solo se l'autenticazione è valida

                // Esempio: Controlla se l'utente ha il ruolo necessario per eseguire questa operazione
                if (roleResponse.getRole().equals("AMMINISTRATORE") || (roleResponse.getRole().equals("COACH"))) {

                    List<AllenamentoDTO> allenamenti = new ArrayList<>();

                    for (Allenamento allenamento : allenamentoRepository.findAllByTipologia(tipologia)){
                        AllenamentoDTO allenamentoDTO = new AllenamentoDTO();

                        allenamentoDTO.setId(allenamento.getId());
                        allenamentoDTO.setNome(allenamento.getNome());
                        allenamentoDTO.setTipologia(allenamento.getTipologia());

                        allenamenti.add(allenamentoDTO);
                    }

                    return allenamenti;
                }else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
                }
            }else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticazione fallita");
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
    }


    @PatchMapping(value = "/changeTrainName/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AllenamentoDTO cambiaNomeAllenamento(HttpServletRequest request, @PathVariable String id, @RequestBody AllenamentoDTO allenamentoDTO){

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            RoleResponse roleResponse = getRoleResponse(jwt);

            // Verifica se l'autenticazione è valida
            if (roleResponse != null && !Objects.equals(roleResponse.getRole(), "Authentication failed")) {
                // Esegui le operazioni del metodo solo se l'autenticazione è valida

                // Esempio: Controlla se l'utente ha il ruolo necessario per eseguire questa operazione
                if (roleResponse.getRole().equals("AMMINISTRATORE")) {

                    Optional<Allenamento> optionalAllenamento = allenamentoRepository.findById(id);

                    if (optionalAllenamento.isPresent()){

                        Allenamento allenamento = optionalAllenamento.get();

                        if(allenamentoDTO.getNome() != null) {
                            String nome = allenamentoDTO.getNome();
                            if(!allenamentoRepository.existsByNomeAndTipologia(nome, allenamento.getTipologia()))
                                allenamento.setNome(allenamentoDTO.getNome());
                            else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Allenamento già esistente");

                            allenamentoRepository.save(allenamento);

                            allenamentoDTO.setId(allenamento.getId());
                            allenamentoDTO.setNome(allenamento.getNome());
                            allenamentoDTO.setTipologia(allenamento.getTipologia());

                            return allenamentoDTO;

                        } throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inserire nome da aggiornaare");

                    } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Allenamento non esistente");


                }else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
                }
            }else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticazione fallita");
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
    }


    @PatchMapping(value = "/changeTypeByName/{nome}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AllenamentoDTO cambiaTipologiaAllenamento(HttpServletRequest request, @PathVariable String nome, @RequestBody AllenamentoDTO allenamentoDTO){

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            RoleResponse roleResponse = getRoleResponse(jwt);

            // Verifica se l'autenticazione è valida
            if (roleResponse != null && !Objects.equals(roleResponse.getRole(), "Authentication failed")) {
                // Esegui le operazioni del metodo solo se l'autenticazione è valida

                // Esempio: Controlla se l'utente ha il ruolo necessario per eseguire questa operazione
                if (roleResponse.getRole().equals("AMMINISTRATORE")) {

                    Optional<Allenamento> optionalAllenamento = allenamentoRepository.findByNome(nome);

                    if (optionalAllenamento.isPresent()){

                        Allenamento allenamento = optionalAllenamento.get();
                        allenamento.setTipologia(allenamentoDTO.getTipologia());
                        allenamentoRepository.save(allenamento);

                        allenamentoDTO.setId(allenamento.getId());
                        allenamentoDTO.setNome(allenamento.getNome());
                        allenamentoDTO.setTipologia(allenamento.getTipologia());
                    }

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


    @RequestMapping(value = "/checkIfTrainAlreadyExists", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean verificaEsistenzaAllenamento(HttpServletRequest request, @RequestBody AllenamentoDTO allenamentoDTO){

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            RoleResponse roleResponse = getRoleResponse(jwt);

            // Verifica se l'autenticazione è valida
            if (roleResponse != null && !Objects.equals(roleResponse.getRole(), "Authentication failed")) {
                // Esegui le operazioni del metodo solo se l'autenticazione è valida

                // Esempio: Controlla se l'utente ha il ruolo necessario per eseguire questa operazione
                if (roleResponse.getRole().equals("AMMINISTRATORE")) {

                    return allenamentoRepository.existsByNomeAndTipologia(allenamentoDTO.getNome(), allenamentoDTO.getTipologia());

                }else {
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
