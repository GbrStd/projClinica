package gbrstd.clinica.service;

import gbrstd.clinica.exceptions.ResourceNotFoundException;
import gbrstd.clinica.model.Especialidade;
import gbrstd.clinica.repository.EspecialidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadeService {

    private final EspecialidadeRepository especialidadeRepository;

    public EspecialidadeService(EspecialidadeRepository especialidadeRepository) {
        this.especialidadeRepository = especialidadeRepository;
    }

    public List<Especialidade> findAll() {
        return especialidadeRepository.findAll();
    }

    public Especialidade save(Especialidade especialidade) {
        return especialidadeRepository.save(especialidade);
    }

    public Especialidade findByDescricao(String descricao) throws ResourceNotFoundException {
        return especialidadeRepository.findByDescricao(descricao).orElseThrow(ResourceNotFoundException::new);
    }

    public boolean existsByDescricao(String descricao) {
        return especialidadeRepository.existsByDescricao(descricao);
    }


}
