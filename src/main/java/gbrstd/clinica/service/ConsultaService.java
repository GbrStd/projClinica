package gbrstd.clinica.service;

import gbrstd.clinica.exceptions.ResourceNotFoundException;
import gbrstd.clinica.model.Consulta;
import gbrstd.clinica.repository.ConsultaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;

    public ConsultaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public List<Consulta> findAll() {
        return consultaRepository.findAll();
    }

    public Consulta findById(Long id) throws ResourceNotFoundException {
        return consultaRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public Consulta save(Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    public Consulta delete(Long id) throws ResourceNotFoundException {
        Consulta consulta = findById(id);
        consultaRepository.delete(consulta);
        return consulta;
    }

}
