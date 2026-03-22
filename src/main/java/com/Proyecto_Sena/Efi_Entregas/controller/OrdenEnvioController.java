package com.Proyecto_Sena.Efi_Entregas.controller;

import com.Proyecto_Sena.Efi_Entregas.model.OrdenEnvio;
import com.Proyecto_Sena.Efi_Entregas.service.OrdenEnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

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

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<OrdenEnvio> cancelarOrden(
            @PathVariable Long id,
            @RequestBody OrdenEnvio datosCancelacion) {
        OrdenEnvio ordenActualizada = ordenEnvioService.cancelarOrden(id, datosCancelacion.getMotivoCancelacion());
        return ResponseEntity.ok(ordenActualizada);
    }

    @PutMapping("/asignar")
    public ResponseEntity<String> asignarOrdenes(@RequestBody Map<String, Object> data) {

        Long idUsuario = Long.valueOf(data.get("idUsuario").toString());

        List<?> lista = (List<?>) data.get("ordenes");

        List<Integer> ordenesIds = lista.stream()
                .map(obj -> Integer.parseInt(obj.toString()))
                .toList();

        ordenEnvioService.asignarOrdenes(idUsuario, ordenesIds);

        return ResponseEntity.ok("Ordenes asignadas");
    }
}
