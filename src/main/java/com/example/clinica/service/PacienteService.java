package com.example.clinica.service;

import com.example.clinica.exceptions.ResourceNotFoundException;
import com.example.clinica.model.Paciente;
import com.example.clinica.repository.PacienteRepository;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente findById(Long id) throws ResourceNotFoundException {
        return pacienteRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public Paciente save(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public Paciente delete(Long id) throws ResourceNotFoundException {
        Paciente paciente = findById(id);
        pacienteRepository.delete(paciente);
        return paciente;
    }

}
