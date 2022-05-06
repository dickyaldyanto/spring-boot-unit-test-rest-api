package com.learn.unittest.controller;

import com.learn.unittest.model.PatientRecord;
import com.learn.unittest.repository.PatientRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/patient")
public class PatientRecordController {
    @Autowired
    PatientRecordRepository patientRecordRepository;

    @GetMapping
    public List<PatientRecord> getAllRecords() {
        return patientRecordRepository.findAll();
    }

    @GetMapping(value = "{patientId}")
    public PatientRecord getPatientById(@PathVariable(value="patientId") Long patientId) {
        return patientRecordRepository.findById(patientId).get();
    }

    @PostMapping
    public PatientRecord createRecord(@RequestBody PatientRecord patientRecord) {
        return patientRecordRepository.save(patientRecord);
    }

    @PutMapping
    public PatientRecord updatePatientRecord(@RequestBody PatientRecord patientRecord){
        if (patientRecord == null || patientRecord.getPatientId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "PatientRecord or ID must not be null!");
        }
        Optional<PatientRecord> optionalRecord = patientRecordRepository.findById(patientRecord.getPatientId());
        if (optionalRecord == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Patient with ID " + patientRecord.getPatientId() + " does not exist.");
        }
        PatientRecord existingPatientRecord = optionalRecord.get();

        existingPatientRecord.setName(patientRecord.getName());
        existingPatientRecord.setAge(patientRecord.getAge());
        existingPatientRecord.setAddress(patientRecord.getAddress());

        return patientRecordRepository.save(existingPatientRecord);
    }

    @DeleteMapping(value = "{patientId}")
    public void deletePatientById(@PathVariable(value = "patientId") Long patientId){
        if (patientRecordRepository.findById(patientId) == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Patient with ID " + patientId + " does not exist.");
        }
        patientRecordRepository.deleteById(patientId);
    }
}