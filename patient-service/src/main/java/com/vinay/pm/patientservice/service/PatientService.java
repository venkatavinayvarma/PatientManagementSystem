package com.vinay.pm.patientservice.service;

import com.vinay.pm.patientservice.dto.PatientRequestDTO;
import com.vinay.pm.patientservice.dto.PatientResponseDTO;
import com.vinay.pm.patientservice.exception.EmailAlreadyExistsException;
import com.vinay.pm.patientservice.mapper.PatientMapper;
import com.vinay.pm.patientservice.model.Patient;
import com.vinay.pm.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {
    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        List<PatientResponseDTO> patientResponseDTOs = patients.stream()
                .map(patient -> PatientMapper.toDTO(patient)).toList();


        return patientResponseDTOs;
    }


    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw  new EmailAlreadyExistsException("A patient with this email " +
                    "already exists"+ patientRequestDTO.getEmail());
        }

        Patient newpatient = patientRepository.save(
                PatientMapper.toModel(patientRequestDTO));

        return PatientMapper.toDTO(newpatient);
    }


}
