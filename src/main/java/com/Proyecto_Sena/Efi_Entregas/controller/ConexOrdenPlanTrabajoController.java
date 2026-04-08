package com.Proyecto_Sena.Efi_Entregas.controller;

import com.Proyecto_Sena.Efi_Entregas.model.ConexOrdenPlanTrabajo;
import com.Proyecto_Sena.Efi_Entregas.service.ConexOrdenPlanTrabajoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ordenes-plan-trabajo")
public class ConexOrdenPlanTrabajoController {

    @Autowired
    private ConexOrdenPlanTrabajoService conexOrdenPlanTrabajoService;

    @GetMapping
    public List<ConexOrdenPlanTrabajo> getAll() {
        return conexOrdenPlanTrabajoService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConexOrdenPlanTrabajo> getById(@PathVariable @NonNull Long id) {
        return conexOrdenPlanTrabajoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ConexOrdenPlanTrabajo create(@RequestBody @NonNull ConexOrdenPlanTrabajo conexOrdenPlanTrabajo) {
        return conexOrdenPlanTrabajoService.save(conexOrdenPlanTrabajo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConexOrdenPlanTrabajo> update(@PathVariable @NonNull Long id,
            @RequestBody @NonNull ConexOrdenPlanTrabajo conexOrdenPlanTrabajo) {
        return conexOrdenPlanTrabajoService.getById(id)
                .map(existing -> {
                    conexOrdenPlanTrabajo.setIdConexOrdenPlanTrabajo(id);
                    return ResponseEntity.ok(conexOrdenPlanTrabajoService.save(conexOrdenPlanTrabajo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NonNull Long id) {
        Optional<ConexOrdenPlanTrabajo> conex = conexOrdenPlanTrabajoService.getById(id);
        if (conex.isPresent()) {
            conexOrdenPlanTrabajoService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
