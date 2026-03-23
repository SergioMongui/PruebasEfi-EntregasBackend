package com.Proyecto_Sena.Efi_Entregas.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Proyecto_Sena.Efi_Entregas.model.ConexOrdenPlanTrabajo;
import com.Proyecto_Sena.Efi_Entregas.model.PlanTrabajo;

public interface ConexOrdenPlanTrabajoRepository extends JpaRepository<ConexOrdenPlanTrabajo, Long> {
List<ConexOrdenPlanTrabajo> findByPlanTrabajo(PlanTrabajo planTrabajo);
}
