package gbrstd.clinica.service;

import gbrstd.clinica.exceptions.ResourceNotFoundException;
import gbrstd.clinica.model.Clinica;
import gbrstd.clinica.model.Especialidade;
import gbrstd.clinica.repository.ClinicaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicaService {

    private final ClinicaRepository clinicaRepository;

    private final EspecialidadeService especialidadeService;

    public ClinicaService(ClinicaRepository clinicaRepository, EspecialidadeService especialidadeService) {
        this.clinicaRepository = clinicaRepository;
        this.especialidadeService = especialidadeService;
    }

    public List<Clinica> findAll() {
        return clinicaRepository.findAll();
    }

    public Clinica findById(Long id) throws ResourceNotFoundException {
        return clinicaRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public Clinica save(Clinica clinica) {
        Especialidade especialidade;
        try {
            especialidade = especialidadeService.findByDescricao(clinica.getEspecialidade().getDescricao());
        } catch (ResourceNotFoundException e) {
            especialidade = especialidadeService.save(clinica.getEspecialidade());
        }
        clinica.setEspecialidade(especialidade);
        return clinicaRepository.save(clinica);
    }

    public void delete(Long id) throws ResourceNotFoundException {
        Clinica clinica = findById(id);
        clinicaRepository.delete(clinica);
    }

}
