package com.pios.service.impl;

import com.pios.entity.InfoSignal;
import com.pios.repository.InfoSignalRepository;
import com.pios.service.InfoSignalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InfoSignalServiceImpl implements InfoSignalService {
    
    @Autowired
    private InfoSignalRepository infoSignalRepository;
    
    @Override
    public InfoSignal save(InfoSignal infoSignal) {
        return infoSignalRepository.save(infoSignal);
    }
    
    @Override
    public Optional<InfoSignal> findById(Long id) {
        return infoSignalRepository.findById(id);
    }
    
    @Override
    public List<InfoSignal> findAll() {
        return infoSignalRepository.findAll();
    }
    
    @Override
    public void deleteById(Long id) {
        infoSignalRepository.deleteById(id);
    }
    
    @Override
    public List<InfoSignal> findByCategory(String category) {
        return infoSignalRepository.findByCategory(category);
    }
    
    @Override
    public List<InfoSignal> findByLevel(Integer level) {
        return infoSignalRepository.findByLevel(level);
    }
}
