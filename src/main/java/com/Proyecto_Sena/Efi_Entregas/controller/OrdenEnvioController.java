package com.Proyecto_Sena.Efi_Entregas.controller;

import com.Proyecto_Sena.Efi_Entregas.model.OrdenEnvio;
import com.Proyecto_Sena.Efi_Entregas.service.OrdenEnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/ordenes")
public class OrdenEnvioController {

    @Autowired
    private OrdenEnvioService ordenEnvioService;

    @PostMapping
    public ResponseEntity<OrdenEnvio> crearOrden(@RequestBody OrdenEnvio orden) {
        return ResponseEntity.ok(ordenEnvioService.save(orden));
    }

    @GetMapping
    public List<OrdenEnvio> obtenerTodas() {
        return ordenEnvioService.getAll();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrden(@PathVariable Long id) {
    ordenEnvioService.eliminar(id);
    return ResponseEntity.noContent().build();
}
}
