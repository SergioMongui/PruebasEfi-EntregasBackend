package com.Proyecto_Sena.Efi_Entregas.controller;

import com.Proyecto_Sena.Efi_Entregas.model.OrdenEnvio;
import com.Proyecto_Sena.Efi_Entregas.service.OrdenEnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.util.Objects;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/ordenes")
public class OrdenEnvioController {

    @Autowired
    private OrdenEnvioService ordenEnvioService;

    @PostMapping
    public ResponseEntity<OrdenEnvio> crearOrden(@RequestBody @NonNull OrdenEnvio orden) {
        return ResponseEntity.ok(ordenEnvioService.save(orden));
    }

    @GetMapping
    public List<OrdenEnvio> obtenerTodas() {
        return ordenEnvioService.getAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrden(@PathVariable @NonNull Long id) {
        ordenEnvioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<OrdenEnvio> cancelarOrden(
            @PathVariable @NonNull Long id,
            @RequestBody @NonNull OrdenEnvio datosCancelacion) {
        OrdenEnvio ordenActualizada = ordenEnvioService.cancelarOrden(id, datosCancelacion.getMotivoCancelacion());
        return ResponseEntity.ok(ordenActualizada);
    }

    @PutMapping("/asignar")
    @SuppressWarnings("null")
    public ResponseEntity<String> asignarOrdenes(@RequestBody @NonNull Map<String, Object> data) {

        Long idUsuario = Long.valueOf(Objects.requireNonNull(data.get("idUsuario")).toString());

        List<?> lista = (List<?>) Objects.requireNonNull(data.get("ordenes"));

        List<Integer> ordenesIds = lista.stream()
                .map(obj -> Integer.parseInt(Objects.requireNonNull(obj).toString()))
                .toList();

        ordenEnvioService.asignarOrdenes(idUsuario, ordenesIds);

        return ResponseEntity.ok("Ordenes asignadas");
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<OrdenEnvio> actualizarEstado(
            @PathVariable @NonNull Long id,
            @RequestBody @NonNull Map<String, String> datos) {

        String nuevoEstado = Objects.requireNonNull(datos.get("estado"));
        String comentario = datos.get("comentario");

        OrdenEnvio actualizada = ordenEnvioService.actualizarEstado(id, nuevoEstado, comentario);

        return ResponseEntity.ok(actualizada);
    }
}
