package gbrstd.clinica.service;

import gbrstd.clinica.exceptions.ResourceNotFoundException;
import gbrstd.clinica.model.Especialista;
import gbrstd.clinica.repository.EspecialistaRepository;
import org.springframework.stereotype.Service;

@Service
public class EspecialistaService {

    private final EspecialistaRepository especialistaRepository;

    public EspecialistaService(EspecialistaRepository especialistaRepository) {
        this.especialistaRepository = especialistaRepository;
    }

    public Especialista findById(Long id) throws ResourceNotFoundException {
        return especialistaRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public Especialista save(Especialista especialista) {
        return especialistaRepository.save(especialista);
    }

    /*public void delete(Long id) throws ResourceNotFoundException {
        Especialista especialista = findById(id);
        especialistaRepository.delete(especialista);
    }*/

    public Especialista delete(Long id) throws ResourceNotFoundException {
        Especialista especialista = findById(id);
        especialistaRepository.delete(especialista);
        return especialista;
    }

}
