package com.Proyecto_Sena.Efi_Entregas.controller;

import com.Proyecto_Sena.Efi_Entregas.model.PlanTrabajoDTO;
import com.Proyecto_Sena.Efi_Entregas.service.PlanTrabajoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/planes")
public class PlanTrabajoController {

    @Autowired
    private PlanTrabajoService planTrabajoService;

    @GetMapping
    public List<PlanTrabajoDTO> obtenerPlanes() {
        return planTrabajoService.obtenerTodos();
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<PlanTrabajoDTO> obtenerPlanesPorUsuario(@PathVariable Long idUsuario) {
        return planTrabajoService.obtenerPorUsuario(idUsuario);
    }

@PutMapping("/{idPlan}/estado")
public PlanTrabajoDTO actualizarEstado(@PathVariable Long idPlan, @RequestBody PlanTrabajoDTO dto) {
    return planTrabajoService.actualizarEstado(idPlan, dto.getEstado());
}
}
