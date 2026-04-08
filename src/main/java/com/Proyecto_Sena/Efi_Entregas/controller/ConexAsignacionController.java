package com.Proyecto_Sena.Efi_Entregas.controller;

import com.Proyecto_Sena.Efi_Entregas.model.ConexAsignacion;
import com.Proyecto_Sena.Efi_Entregas.service.ConexAsignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/asignaciones")
public class ConexAsignacionController {

    @Autowired
    private ConexAsignacionService conexAsignacionService;

    @GetMapping
    public List<ConexAsignacion> getAll() {
        return conexAsignacionService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConexAsignacion> getById(@PathVariable @NonNull Long id) {
        return conexAsignacionService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ConexAsignacion create(@RequestBody @NonNull ConexAsignacion conexAsignacion) {
        return conexAsignacionService.save(conexAsignacion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConexAsignacion> update(@PathVariable @NonNull Long id, @RequestBody @NonNull ConexAsignacion conexAsignacion) {
        return conexAsignacionService.getById(id)
                .map(existing -> {
                    conexAsignacion.setIdConexAsignacion(id);
                    return ResponseEntity.ok(conexAsignacionService.save(conexAsignacion));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NonNull Long id) {
        Optional<ConexAsignacion> asignacion = conexAsignacionService.getById(id);
        if (asignacion.isPresent()) {
            conexAsignacionService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
