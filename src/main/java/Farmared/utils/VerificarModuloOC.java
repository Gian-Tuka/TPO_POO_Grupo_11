package Farmared.utils;

import Farmared.controller.ordenes.ControladorDeOrdenDeCompra;
import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.controller.usuariosYSeguridad.ControladorUsuariosYSeguridad;
import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.dto.ordenes.OrdenDeCompraDTO;
import Farmared.dto.ordenes.DetalleItemDTO;
import Farmared.dto.ordenes.DetalleOCDTO;
import Farmared.dto.proveedor.ProveedorDTO;
import Farmared.dto.rubro.RubroDTO;
import Farmared.dto.item.ItemDTO;
import Farmared.dto.item.UnidadDeMedidaDTO;
import Farmared.dto.user.UsuarioDTO;
import Farmared.model.ordenCompra.EstadoOC;

import java.util.ArrayList;
import java.util.List;

public class VerificarModuloOC {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO VERIFICACIÓN DEL MÓDULO ORDEN DE COMPRA ===");

        ControladorUsuariosYSeguridad ctrlUser = ControladorUsuariosYSeguridad.getInstance();
        ControladorProveedores ctrlProv = ControladorProveedores.getInstance();
        ControladorProductosYServicios ctrlProdServ = ControladorProductosYServicios.getInstance();
        ControladorDeOrdenDeCompra ctrlOC = ControladorDeOrdenDeCompra.getInstance();

        try {
            // 1. Simular carga de Rubros y Proveedores
            System.out.println("\n--- 1. Inicializando datos de prueba (Rubros y Proveedores) ---");
            ctrlProv.altaRubro(new RubroDTO("Medicamentos", "BIENES"));
            
            ProveedorDTO provAlfa = new ProveedorDTO(
                "30-12345678-1", "Proveedor Alfa S.A.", "Alfa", 
                "Calle Falsa", "123", "1000", "CABA", "Argentina", 
                "4555-1234", "alfa@test.com", "RESPONSABLE_INSCRIPTO", 
                "12345", "01/01/2020", 1000f, new ArrayList<>(List.of("Medicamentos"))
            );
            ctrlProv.registrarProveedor(provAlfa);
            System.out.println("Proveedor Alfa registrado con tope de deuda: $1000.00");

            // 2. Simular carga de Unidad de Medida e Ítems
            System.out.println("\n--- 2. Inicializando Items y Precios de Proveedor ---");
            UnidadDeMedidaDTO udmDTO = ctrlProdServ.altaUnidadDeMedida(new UnidadDeMedidaDTO("U01", "Unidad", "UNIDAD"));
            String codigoUdm = udmDTO.getCodigoUnidad();
            
            ItemDTO itemIbuprofeno = new ItemDTO(
                "ART001", "Ibuprofeno 400mg", "Unidad", codigoUdm, 
                "IVA_21", "Medicamentos", "0.0", true
            );
            itemIbuprofeno.setTipoItem("PRODUCTO");
            ItemDTO itemRegistrado = ctrlProdServ.registrarItem(itemIbuprofeno);
            String codigoItemReal = itemRegistrado.getCodigo();
            System.out.println("Producto 'Ibuprofeno 400mg' registrado con codigo: " + codigoItemReal);

            // Registrar precio del ítem para el proveedor Alfa
            ctrlProv.registrarPrecioProveedor("30-12345678-1", codigoItemReal, 10f);
            System.out.println("Precio de 'Ibuprofeno 400mg' asignado para Proveedor Alfa: $10.00 por unidad.");

            // 3. Simular login de usuario
            System.out.println("\n--- 3. Autenticación de Usuario ---");
            // Ana Martinez (LU-2000, clave "1315") es Empleada de Compras
            boolean loginOk = ctrlUser.login("LU-2000", "1315");
            if (loginOk) {
                UsuarioDTO userDto = ctrlUser.getUsuarioActual();
                System.out.println("Usuario logueado correctamente: " + userDto.getNombre() + " " + userDto.getApellido() + " (Legajo: " + userDto.getLegajo() + ")");
            } else {
                throw new RuntimeException("Fallo al loguear usuario de prueba.");
            }

            // 4. Testear el filtrado de items con precio proveedor
            System.out.println("\n--- 4. Consultando Ítems con Precio del Proveedor ---");
            ArrayList<ItemDTO> itemsConPrecio = ctrlOC.obtenerItemsConPrecioPorProveedor("30-12345678-1");
            System.out.println("Ítems disponibles para Proveedor Alfa (deben mostrarse solo con precio):");
            for (ItemDTO i : itemsConPrecio) {
                System.out.println("  - [" + i.getCodigo() + "] " + i.getDescripcionDeItem() + " (Precio vigente: $" + i.getPrecioVigente() + ")");
            }

            // 5. Test Caso Feliz (Emisión de OC dentro del límite de deuda)
            System.out.println("\n--- 5. Emitiendo OC dentro del límite de deuda ($1000.00) ---");
            List<DetalleItemDTO> itemsCompra1 = new ArrayList<>();
            itemsCompra1.add(new DetalleItemDTO(codigoItemReal, 50)); // 50 unidades * $10.00 = $500.00 (Dentro del límite de $1000.00)
            
            OrdenDeCompraDTO inputOc1 = new OrdenDeCompraDTO("30-12345678-1", itemsCompra1);
            OrdenDeCompraDTO outputOc1 = ctrlOC.emitirOC(inputOc1);
            
            System.out.println("OC emitida exitosamente:");
            System.out.println("  - Nro OC: " + outputOc1.getNroOC());
            System.out.println("  - Proveedor: " + outputOc1.getRazonSocialProveedor());
            System.out.println("  - Creador Legajo: " + outputOc1.getCreadorLegajo());
            System.out.println("  - Total: $" + outputOc1.getTotal());
            System.out.println("  - Estado: " + outputOc1.getEstado() + " (Esperado: APROBADA)");

            // 6. Test Caso Excedido (Emisión de OC que supera el límite de deuda)
            System.out.println("\n--- 6. Emitiendo OC que excede el límite de deuda ---");
            List<DetalleItemDTO> itemsCompra2 = new ArrayList<>();
            itemsCompra2.add(new DetalleItemDTO(codigoItemReal, 120)); // 120 unidades * $10.00 = $1200.00 > $1000.00 (Excede el límite de $1000.00)
            
            OrdenDeCompraDTO inputOc2 = new OrdenDeCompraDTO("30-12345678-1", itemsCompra2);
            OrdenDeCompraDTO outputOc2 = ctrlOC.emitirOC(inputOc2);
            
            System.out.println("OC emitida exitosamente:");
            System.out.println("  - Nro OC: " + outputOc2.getNroOC());
            System.out.println("  - Total: $" + outputOc2.getTotal());
            System.out.println("  - Estado: " + outputOc2.getEstado() + " (Esperado: PENDIENTE_AUTORIZACION)");

            // 7. Test de Autorización de la OC excedida por un Supervisor
            System.out.println("\n--- 7. Autorizando la OC pendiente por un Supervisor ---");
            // Carlos Gomez (LU-1000) es Supervisor de Sistemas
            ctrlOC.autorizarOC(outputOc2.getNroOC(), "LU-1000", "Aprobado por el supervisor del area.");
            
            OrdenDeCompraDTO ocConsultada = ctrlOC.consultarOC(outputOc2.getNroOC());
            System.out.println("OC Consultada post-autorización:");
            System.out.println("  - Nro OC: " + ocConsultada.getNroOC());
            System.out.println("  - Estado: " + ocConsultada.getEstado() + " (Esperado: APROBADA_AUTORIZACION)");

            // 8. Listar todas las OCs del sistema
            System.out.println("\n--- 8. Listando todas las OCs del sistema ---");
            ArrayList<OrdenDeCompraDTO> listado = ctrlOC.obtenerOrdenesDeCompraDTO();
            for (OrdenDeCompraDTO oc : listado) {
                System.out.println("  - OC: " + oc.getNroOC() + " | Proveedor: " + oc.getRazonSocialProveedor() + " | Total: $" + oc.getTotal() + " | Estado: " + oc.getEstado());
                for (DetalleOCDTO d : oc.getDetalles()) {
                    System.out.println("    * [" + d.getCodigoItem() + "] " + d.getDescripcionItem() + " | Cantidad: " + d.getCantidad() + " | Unitario: $" + d.getPrecioUnitario() + " | Subtotal: $" + d.getSubtotal());
                }
            }

            System.out.println("\n=== VERIFICACIÓN COMPLETADA EXITOSAMENTE Y SIN ERRORES ===");

        } catch (Exception e) {
            System.err.println("\n!!! ERROR EN LA VERIFICACIÓN !!!");
            e.printStackTrace();
        }
    }
}
