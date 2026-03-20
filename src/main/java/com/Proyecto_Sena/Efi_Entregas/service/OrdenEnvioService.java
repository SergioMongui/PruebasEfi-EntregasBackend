package com.Proyecto_Sena.Efi_Entregas.service;

import com.Proyecto_Sena.Efi_Entregas.model.OrdenEnvio;
import com.Proyecto_Sena.Efi_Entregas.repository.OrdenEnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdenEnvioService {

    @Autowired
    private OrdenEnvioRepository ordenEnvioRepository;

    public OrdenEnvio save(OrdenEnvio orden) {
        return ordenEnvioRepository.save(orden);
    }

    public List<OrdenEnvio> getAll() {
        return ordenEnvioRepository.findAll();
    }

    public void eliminar(Long id) {
    ordenEnvioRepository.deleteById(id);
}
}
