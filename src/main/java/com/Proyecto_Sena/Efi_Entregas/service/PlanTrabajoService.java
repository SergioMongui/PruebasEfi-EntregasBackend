package com.Proyecto_Sena.Efi_Entregas.service;

import com.Proyecto_Sena.Efi_Entregas.model.ConexOrdenPlanTrabajo;
import com.Proyecto_Sena.Efi_Entregas.model.OrdenEnvio;
import com.Proyecto_Sena.Efi_Entregas.model.PlanTrabajo;
import com.Proyecto_Sena.Efi_Entregas.model.PlanTrabajoDTO;
import com.Proyecto_Sena.Efi_Entregas.repository.PlanTrabajoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import com.Proyecto_Sena.Efi_Entregas.repository.ConexOrdenPlanTrabajoRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanTrabajoService {

    @Autowired
    private PlanTrabajoRepository planTrabajoRepository;

    @Autowired
    private ConexOrdenPlanTrabajoRepository conexionRepository;

    public List<PlanTrabajoDTO> obtenerTodos() {

        List<PlanTrabajo> planes = planTrabajoRepository.findAll();

        return planes.stream().map(plan -> {

            PlanTrabajoDTO dto = new PlanTrabajoDTO();

            dto.setIdPlanTrabajo(plan.getIdPlanTrabajo());
            dto.setEstado(plan.getEstadoPT());

            List<ConexOrdenPlanTrabajo> conexiones = conexionRepository.findByPlanTrabajo(plan);

            ConexOrdenPlanTrabajo conexion = conexiones.stream().findFirst().orElse(null);

            if (conexion != null) {
                dto.setFecha(conexion.getFechaCreacion());
            }

            // toma el id usuario de la orden
            OrdenEnvio orden = conexion != null ? conexion.getOrdenEnvio() : null;

            if (orden != null && orden.getUsuario() != null) {
                dto.setNombre(orden.getUsuario().getNombre());
                dto.setTelefono(orden.getUsuario().getTelefono());
                dto.setEmail(orden.getUsuario().getEmail());
            }

            return dto;

        }).toList();
    }

    public List<PlanTrabajoDTO> obtenerPorUsuario(@NonNull Long idUsuario) {

        List<ConexOrdenPlanTrabajo> conexiones = conexionRepository.findDistinctByOrdenEnvioUsuarioIdUsuario(idUsuario);

        return conexiones.stream()
                .collect(Collectors.toMap(
                        conexion -> conexion.getPlanTrabajo().getIdPlanTrabajo(), // clave unica
                        conexion -> {
                            PlanTrabajo plan = conexion.getPlanTrabajo();

                            PlanTrabajoDTO dto = new PlanTrabajoDTO();

                            dto.setIdPlanTrabajo(plan.getIdPlanTrabajo());
                            dto.setEstado(plan.getEstadoPT());
                            dto.setFecha(conexion.getFechaCreacion());

                            if (conexion.getOrdenEnvio() != null &&
                                    conexion.getOrdenEnvio().getUsuario() != null) {

                                dto.setNombre(conexion.getOrdenEnvio().getUsuario().getNombre());
                                dto.setTelefono(conexion.getOrdenEnvio().getUsuario().getTelefono());
                                dto.setEmail(conexion.getOrdenEnvio().getUsuario().getEmail());
                            }

                            return dto;
                        },
                        (existente, repetido) -> existente // evita duplicados
                ))
                .values()
                .stream()
                .toList();
    }

public PlanTrabajoDTO actualizarEstado(@NonNull Long idPlan, @NonNull String nuevoEstado) {
    PlanTrabajo plan = planTrabajoRepository.findById(idPlan)
            .orElseThrow(() -> new RuntimeException("Plan no encontrado"));

    plan.setEstadoPT(nuevoEstado);
    PlanTrabajo planActualizado = planTrabajoRepository.save(plan);

    return convertirADTO(planActualizado);
}

private PlanTrabajoDTO convertirADTO(PlanTrabajo plan) {
    PlanTrabajoDTO dto = new PlanTrabajoDTO();
    dto.setIdPlanTrabajo(plan.getIdPlanTrabajo());
    dto.setEstado(plan.getEstadoPT());

    List<ConexOrdenPlanTrabajo> conexiones = conexionRepository.findByPlanTrabajo(plan);
    ConexOrdenPlanTrabajo conexion = conexiones.stream().findFirst().orElse(null);

    if (conexion != null) {
        dto.setFecha(conexion.getFechaCreacion());
        
        OrdenEnvio orden = conexion.getOrdenEnvio();
        if (orden != null && orden.getUsuario() != null) {
            dto.setNombre(orden.getUsuario().getNombre());
            dto.setTelefono(orden.getUsuario().getTelefono());
            dto.setEmail(orden.getUsuario().getEmail());
        }
    }

    return dto;
}
}
