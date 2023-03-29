package gbrstd.clinica.service;

import gbrstd.clinica.exceptions.ResourceNotFoundException;
import gbrstd.clinica.model.Exame;
import gbrstd.clinica.repository.ExameRepository;
import org.springframework.stereotype.Service;

@Service
public class ExameService {

    private final ExameRepository exameRepository;

    public ExameService(ExameRepository exameRepository) {
        this.exameRepository = exameRepository;
    }

    public Exame findById(Long id) throws ResourceNotFoundException {
        return exameRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

}
