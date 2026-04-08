package com.Proyecto_Sena.Efi_Entregas.service;

import com.Proyecto_Sena.Efi_Entregas.model.ConexOrdenPlanTrabajo;
import com.Proyecto_Sena.Efi_Entregas.repository.ConexOrdenPlanTrabajoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConexOrdenPlanTrabajoService {

    @Autowired
    private ConexOrdenPlanTrabajoRepository conexOrdenPlanTrabajoRepository;

    public List<ConexOrdenPlanTrabajo> getAll() {
        return conexOrdenPlanTrabajoRepository.findAll();
    }

    public Optional<ConexOrdenPlanTrabajo> getById(@NonNull Long id) {
        return conexOrdenPlanTrabajoRepository.findById(id);
    }

    public ConexOrdenPlanTrabajo save(@NonNull ConexOrdenPlanTrabajo conexOrdenPlanTrabajo) {
        return conexOrdenPlanTrabajoRepository.save(conexOrdenPlanTrabajo);
    }

    public void delete(@NonNull Long id) {
        conexOrdenPlanTrabajoRepository.deleteById(id);
    }
}
