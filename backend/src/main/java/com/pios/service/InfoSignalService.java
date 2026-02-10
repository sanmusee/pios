package com.pios.service;

import com.pios.entity.InfoSignal;
import java.util.List;
import java.util.Optional;

public interface InfoSignalService {
    InfoSignal save(InfoSignal infoSignal);
    Optional<InfoSignal> findById(Long id);
    List<InfoSignal> findAll();
    void deleteById(Long id);
    List<InfoSignal> findByCategory(String category);
    List<InfoSignal> findByLevel(Integer level);
}
