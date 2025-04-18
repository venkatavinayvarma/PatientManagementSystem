package com.vinay.pm.patientservice.service;

import com.vinay.pm.patientservice.dto.PatientRequestDTO;
import com.vinay.pm.patientservice.dto.PatientResponseDTO;
import com.vinay.pm.patientservice.exception.EmailAlreadyExistsException;
import com.vinay.pm.patientservice.exception.PatientNotFoundException;
import com.vinay.pm.patientservice.mapper.PatientMapper;
import com.vinay.pm.patientservice.model.Patient;
import com.vinay.pm.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public PatientResponseDTO updatePatient(UUID id,
                                            PatientRequestDTO patientRequestDTO){
        Patient patient = patientRepository.findById(id).orElseThrow(
                ()->new PatientNotFoundException("Patient not found with id: " + id));

        if (patientRepository.existsByEmailAndIdNot(
                patientRequestDTO.getEmail(), id)) {
            throw  new EmailAlreadyExistsException("A patient with this email " +
                    "already exists"+ patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient  updatedPatient = patientRepository.save(patient);


        return PatientMapper.toDTO(updatedPatient);


    }


}
