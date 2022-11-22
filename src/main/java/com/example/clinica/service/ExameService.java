package com.example.clinica.service;

import com.example.clinica.exceptions.ResourceNotFoundException;
import com.example.clinica.model.Exame;
import com.example.clinica.repository.ExameRepository;
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
