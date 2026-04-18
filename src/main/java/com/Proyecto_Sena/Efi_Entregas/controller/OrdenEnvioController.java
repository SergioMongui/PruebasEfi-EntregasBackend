package com.Proyecto_Sena.Efi_Entregas.controller;

import com.Proyecto_Sena.Efi_Entregas.model.OrdenEnvio;
import com.Proyecto_Sena.Efi_Entregas.service.OrdenEnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.util.List;
import java.util.Objects;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.ArrayList;

import com.Proyecto_Sena.Efi_Entregas.model.OrdenCargaDTO;
import com.Proyecto_Sena.Efi_Entregas.model.ErrorCargaDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;

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

    @PostMapping("/cargar")
    public ResponseEntity<?> cargarOrdenes(@RequestParam("archivo") MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Archivo no válido");
            return ResponseEntity.badRequest().body(error);
        }

        List<ErrorCargaDTO> listaErrores = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (InputStream is = archivo.getInputStream();
                Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Validar encabezados (Fila 0)
            Row headerRow = sheet.getRow(0);
            if (headerRow == null ||
                    !formatter.formatCellValue(headerRow.getCell(0)).trim().equals("NOMBRE CLIENTE") ||
                    !formatter.formatCellValue(headerRow.getCell(1)).trim().equals("DIRECCION") ||
                    !formatter.formatCellValue(headerRow.getCell(2)).trim().equals("TELEFONO") ||
                    !formatter.formatCellValue(headerRow.getCell(3)).trim().equals("LISTA PRODUCTOS") ||
                    !formatter.formatCellValue(headerRow.getCell(4)).trim().equals("VALOR")) {

                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Estructura del archivo incorrecta");
                error.put("detalle", "Los encabezados no coinciden con el formato esperado");
                return ResponseEntity.badRequest().body(error);
            }

            int rowNumber = 0;
            int totalFilas = 0;
            List<OrdenCargaDTO> listaValidas = new ArrayList<>();
            List<ErrorCargaDTO> listaErroresGlobal = new ArrayList<>();

            for (Row row : sheet) {
                rowNumber++;

                // Saltar encabezados
                if (rowNumber == 1) {
                    continue;
                }

                totalFilas++;
                List<ErrorCargaDTO> erroresFila = new ArrayList<>();

                // NOMBRE CLIENTE
                String nombre = formatter.formatCellValue(row.getCell(0)).trim();
                if (nombre.isEmpty()) {
                    erroresFila.add(new ErrorCargaDTO(rowNumber, "nombreCliente", "No puede estar vacío"));
                } else if (nombre.length() < 3) {
                    erroresFila.add(new ErrorCargaDTO(rowNumber, "nombreCliente", "Mínimo 3 caracteres"));
                }

                // DIRECCION
                String direccion = formatter.formatCellValue(row.getCell(1)).trim();
                if (direccion.isEmpty()) {
                    erroresFila.add(new ErrorCargaDTO(rowNumber, "direccion", "No puede estar vacío"));
                } else if (direccion.length() < 5) {
                    erroresFila.add(new ErrorCargaDTO(rowNumber, "direccion", "Mínimo 5 caracteres"));
                }

                // TELEFONO
                String telefono = formatter.formatCellValue(row.getCell(2)).trim();
                if (telefono.isEmpty()) {
                    erroresFila.add(new ErrorCargaDTO(rowNumber, "telefono", "No puede estar vacío"));
                } else if (!telefono.matches("\\d+")) {
                    erroresFila.add(new ErrorCargaDTO(rowNumber, "telefono", "Debe contener solo números"));
                } else if (telefono.length() != 10) {
                    erroresFila.add(new ErrorCargaDTO(rowNumber, "telefono", "Debe tener 10 dígitos"));
                }

                // LISTA PRODUCTOS
                String productos = formatter.formatCellValue(row.getCell(3)).trim();
                if (productos.isEmpty()) {
                    erroresFila.add(new ErrorCargaDTO(rowNumber, "listaProductos", "No puede estar vacío"));
                }

                // VALOR
                double valorFinal = 0;
                String valorStr = formatter.formatCellValue(row.getCell(4)).trim();
                if (valorStr.isEmpty()) {
                    erroresFila.add(new ErrorCargaDTO(rowNumber, "valor", "No puede estar vacío"));
                } else {
                    try {
                        valorFinal = Double.parseDouble(valorStr.replace(",", "."));
                        if (valorFinal <= 0) {
                            erroresFila.add(new ErrorCargaDTO(rowNumber, "valor", "Debe ser mayor a 0"));
                        }
                    } catch (NumberFormatException e) {
                        erroresFila.add(new ErrorCargaDTO(rowNumber, "valor", "Debe ser un número válido"));
                    }
                }

                // Separar válidas de inválidas
                if (erroresFila.isEmpty()) {
                    OrdenCargaDTO dto = new OrdenCargaDTO();
                    dto.setNombreCliente(nombre);
                    dto.setDireccion(direccion);
                    dto.setTelefono(telefono);
                    dto.setListaProductos(productos);
                    dto.setValor(valorFinal);
                    listaValidas.add(dto);
                } else {
                    listaErroresGlobal.addAll(erroresFila);
                }
            }

            // Guardar registros válidos en base de datos
            for (OrdenCargaDTO dto : listaValidas) {
                OrdenEnvio nuevaOrden = new OrdenEnvio();
                nuevaOrden.setNombreCliente(dto.getNombreCliente());
                nuevaOrden.setDireccion(dto.getDireccion());
                nuevaOrden.setTelefono(dto.getTelefono());
                nuevaOrden.setListaProductos(dto.getListaProductos());
                nuevaOrden.setValor(dto.getValor());
                nuevaOrden.setEstado("ACTIVA"); // Estado por defecto para nuevas órdenes

                ordenEnvioService.save(nuevaOrden);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("total", totalFilas);
            response.put("exitosas", listaValidas.size());
            response.put("fallidas", totalFilas - listaValidas.size());
            response.put("errores", listaErroresGlobal);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al leer el archivo: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
