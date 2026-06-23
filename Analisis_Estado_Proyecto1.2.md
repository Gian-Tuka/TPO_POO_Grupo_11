# Análisis de Estado del Proyecto (TPO POO)

A continuación se detalla el relevamiento del estado actual del sistema, validando los puntos reportados. Todo lo expuesto a continuación es **correcto** de acuerdo al código y arquitectura encontrados en el proyecto.

---

## 1. Lo que está CORRECTAMENTE hecho

**Arquitectura general:**
* **Patrón Singleton sincronizado:** En los cuatro controladores está bien implementado el uso de `synchronized` junto con el null check.
* **Patrón DTO:** Está bien aplicado, asegurando que la Vista nunca toque el modelo directamente y que todo el intercambio de datos pase a través de DTOs. *(Matiz: `ProveedorDTO` y `UsuarioDTO` importan clases del modelo, violando parcialmente la independencia de capas — ver Bugs 25 y 37.)*
* **Comunicación inter-controladores:** Está bien planteada. Por ejemplo, `ControladorProveedores` llama a `ControladorProductosYServicios.buscarItemModeloPorCodigo()` y viceversa de manera correcta.
* **Asociación bidireccional:** En `registrarPrecioProveedor()` es correcta; el mismo objeto `PrecioProveedor` se agrega tanto a `prov.getPrecioPorItem()` como a `item.getPrecioItem()`. *(Matiz: utiliza `getPrecioPorItem().add()` directamente en lugar de `agregarPrecioItem()`, exponiendo la lista interna y violando encapsulamiento — ver Bug 38.)*

**Modelo:**
* **Validación del CUIT:** `Validations.validCuit()` implementa el algoritmo real de verificación con los coeficientes `[5,4,3,2,7,6,5,4,3,2]`, lo cual es correcto.
* **GeneradorDeCodigos:** El uso de un `HashSet` para evitar duplicados es una solución acertada.
* **Herencia y Polimorfismo:** La herencia `Item` <- `Producto` / `Servicio` y el control de tipo en `generarCod()` son válidos.
* **Control de tope de deuda:** En `ControladorDeOrdenDeCompra.validarLimite()` la validación (`deudaActual + total <= topeDeuda`) está bien planteada conceptualmente.
* **Seguridad y Accesos:** El control de acceso por rol/área en `tieneAccesoAModulo()` es correcto, así como `Autorizacion.setSupervisor()` que valida el rol correctamente antes de asignar.

---

## 2. Lo que está MAL (Errores concretos en el código)

> [!WARNING]
> Estos son bugs críticos que impiden el funcionamiento normal del sistema o generan estados inconsistentes.

* **Bug 1 — `modificarProveedor()` silenciosamente ignora el nuevo tope de deuda**
  En `ControladorProveedores.java` (~línea 80) se llama a `proveedor.getCuentaCorriente().getTopeDeuda()`, lo cual solo *lee* el valor pero no lo actualiza. El tope ingresado desde la vista es descartado, por lo que nunca se modifica.
* **Bug 2 — `registrarItem()` SIEMPRE falla en producción**
  En `ControladorProductosYServicios.java`, al llamar a `buscarUnidadModelo(codigoUnidad)` siempre devuelve `null` porque no existe un método público para cargar la lista `unidadesDeMedida`. El `requireNotNull` posterior siempre explota.
* **Bug 3 — `CuentaCorriente` nunca actualiza `deudaActual`**
  En `CuentaCorriente.java`, el método `agregarComprobante` añade el elemento a la lista pero no recalcula ni incrementa `deudaActual`. Como resultado, `calcularDeuda()` siempre devuelve `0f` y las validaciones de tope de deuda fallan silenciosamente permitiendo superar el límite.
* **Bug 4 — `ProveedorDialog.java` está en el paquete equivocado**
  Existe un archivo huérfano en `src/main/java/ProveedorDialog.java` que declara `package view;` (sin el prefijo `Farmared.`) pero está ubicado directamente en `src/main/java/` en vez de `src/main/java/view/`. No pertenece al paquete `Farmared.view` y no compila en el contexto del resto del proyecto. Es una versión vieja/descartada del diálogo de proveedores que usa datos mock hardcodeados. !!
* **Bug 5 — `MenuPrincipal` tiene campos muertos declarados pero nunca usados**
  Posee `DefaultTableModel` para Proveedores, Productos y Servicios que siempre son `null` porque las tablas reales son manejadas en `GUIProveedor` y `GUIItem`.
* **Bug 6 — `ImpuestoRetenible` no tiene getters**
  Esta clase carece de getters, por lo que los datos del impuesto (y especialmente `minimoNoImponible` que está declarado como `String` en lugar de `float`) son inaccesibles, rompiendo futuros cálculos numéricos.
* **Bug 7 — `emitirOC()` siempre crea OCs vacías**
  En `ControladorDeOrdenDeCompra.java` la llamada a `crearDetalle` está comentada: `// oc.crearDetalle(item, cantidad);`. Esto significa que todas las OC se generan sin ítems, con total $0. Adicionalmente, la asignación del `creador` (líneas 35-37) está comentada, causando que el creador de la OC sea `null` (Bug 23).
* **Bug 8 — `ImpuestoRetenible.minimoNoImponible` es inasignable**
  El campo existe pero no está en el constructor ni tiene setter. Al no poder asignarse, toda la lógica fiscal y el cálculo de retenciones queda bloqueado desde la raíz.
* **Bug 9 — `RangoDeRetencion.estaEnRango()` y `calcularRetencion()` retornan null**
  Ambos métodos tienen `return null;` hardcodeado en el modelo. Cuando el módulo de retenciones intente usarlos, causarán un `NullPointerException` garantizado.
* **Bug 10 — `CertificadoNoRetencion.validarVigencia()` retorna null**
  Idéntico problema al Bug 9. La lógica para comparar si un certificado está vigente no existe.
* **Bug 11 — `CuentaCorriente` no tiene `setTopeDeuda()`**
  Imposibilita arreglar el Bug 1. La clase `CuentaCorriente` expone `getTopeDeuda()` pero carece del método para mutar el valor, por lo que no hay forma de guardar la actualización enviada desde la vista.
* **Bug 12 — `DetalleOC.obtenerPrecioProveedor()` usa igualdad por referencia**
  La validación `if (pp.getItem().equals(this.item))` utiliza la implementación de `Object.equals()` ya que la clase `Item` no sobreescribe `equals()` ni `hashCode()`. Esto causa que compare posiciones de memoria y falle silenciosamente si el ítem llega desde otra instancia equivalente pero no idéntica.
* **Bug 13 — `toModel()` en `ControladorProveedores` ignora la fecha del DTO**
  En `ControladorProveedores.toModel()`, siempre crea un `new Date()` para `fechaInicioActividades`, ignorando el valor del DTO.
* **Bug 14 — `ChequeDialog.java` es un archivo vacío**
  El archivo existe en el proyecto pero tiene 0 bytes. No tiene paquete ni clase, causando error de compilación.
* **Bug 15 — `VistaAltaRubro` tiene valor de enum inválido `"SERVICIO"`**
  En el combo de la vista se hardcodeó `"SERVICIO"`, pero el enum `TipoRubro` define `"SERVICIOS"` (con S al final). Al seleccionar la primera lanzará `IllegalArgumentException`.
* **Bug 16 — `UnidadDialog` pide un "Código de Unidad" que no existe en el modelo + combos incompatibles con enum**
  Se pide al usuario que ingrese este código, pero en la clase modelo `UnidadDeMedida` el código se auto-genera con `GeneradorDeCodigos`. Además, los valores del combo `TipoDeUnidad` en `UnidadDialog` son `{"Peso", "Volumen", "Cantidad"}` pero el enum `TipoDeUnidad` define `{PESO, DISTANCIA, UNIDAD, VOLUMEN, TIEMPO}`. Problemas concretos: (1) `"Cantidad"` no existe en el enum, debería ser `"UNIDAD"`; (2) la capitalización `"Peso"` no coincide con `"PESO"`, causando `IllegalArgumentException` en un futuro `valueOf()`; (3) faltan las opciones `DISTANCIA` y `TIEMPO`.
* **Bug 17 — `altaUsuario()` hardcodea la contraseña `"1415"`**
  `ControladorUsuariosYSeguridad` siempre crea los nuevos usuarios con la password `"1415"` ignorando la posible entrada segura de contraseñas. !!
* **Bug 18 y Bug 19 — Diálogos de guardado "Mockeados" que no invocan controladores**
  `ProductoDialog`, `ServicioDialog` y `UnidadDialog` sólo muestran un `JOptionPane` con mensajes de éxito ("¡Guardado!") pero no invocan los controladores correspondientes, perdiendo los datos ingresados. !!
* **Bug 20 — `OrdenDePagoDialog` no invoca ningún controlador**
  Diálogo 100% cosmético. Muestra un alert de éxito pero nunca impacta en el sistema.
* **Bug 21 — `GeneradorDeCodigos` se instancia como objeto descartable**
  Se hace un `new GeneradorDeCodigos()` cada vez que se requiere usarlo, a pesar de usar internamente un Set estático. Es un diseño ineficiente y no obedece al Singleton.
* **Bug 22 — `Proveedor` no tiene `setFechaInicioActividades()`**
  Impide actualizar este campo una vez que la entidad ha sido instanciada.
* **Bug 24 — `estadosDeOC` es código muerto en el controlador**
  Declaración vacía `List<EstadoOC> estadosDeOC` en `ControladorDeOrdenDeCompra` que nunca se emplea.
* **Bug 25 — `ProveedorDTO` importa `Proveedor` del modelo**
  Viola el patrón DTO al hacer que la capa de transferencia conozca a la entidad del dominio subyacente. El import de `Farmared.model.proveedor.Proveedor` existe en la línea 3 de `ProveedorDTO.java` pero nunca se usa en la clase (import muerto que además viola la arquitectura).
* **Bug 26 — Imports muertos en `Producto.java` y `Servicio.java`**
  Importan `PrecioProveedor`, `Rubro` y `ArrayList` que nunca utilizan (se manejan en su clase padre `Item`).
* **Error de diseño — `txtPrecio` en `ProductoDialog` y `ServicioDialog` es conceptualmente incorrecto**
  Ambos diálogos tienen un campo "Precio" que no existe en el modelo `Item`. El precio en este sistema no es un atributo del ítem: es un `PrecioProveedor` (entidad que asocia un Item con un Proveedor específico y un valor). Registrar el precio directamente en el alta del producto rompe el modelo. Este campo debe ser eliminado y reemplazado por la pantalla de `registrarPrecioProveedor()`.
* **Vista incompleta — `VistaAltaProveedor` no tiene campo para `fechaInicioActividades`**
  La vista directamente no tiene ningún `JTextField` para que el usuario ingrese esa fecha. El DTO recibe un string vacío (`""`), por lo que nunca se construye el valor adecuadamente. Para resolver los bugs asociados en el controlador, primero debe existir el input en la vista.

### Bugs adicionales detectados en revisión posterior

* **Bug 27 — Combos de IVA en `ProductoDialog` y `ServicioDialog` no coinciden con el enum `TipoDeIVA`**
  Los combos ofrecen `{"21%", "10.5%", "Exento"}` pero el enum `TipoDeIVA` define `{IVA_27, IVA_21, IVA_10_5, EXENTO}`. Si alguna vez se conectan estos diálogos al controlador, `TipoDeIVA.valueOf("21%")` lanzará `IllegalArgumentException`. Además, falta la opción `IVA_27` (27%).
* **Bug 28 — Combos de rubros en `ProductoDialog` y `ServicioDialog` son hardcodeados**
  `ProductoDialog` tiene `{"Medicamentos", "Higiene", "Cosmética"}` y `ServicioDialog` tiene `{"Mantenimiento", "Limpieza", "Logística"}`. Estos valores están hardcodeados y no se cargan desde el controlador como sí lo hace `VistaAltaProveedor.actualizarListaRubros()`. Nunca coincidirán con los rubros reales del sistema. !!
* **Bug 29 — Combos de unidades en `ProductoDialog` y `ServicioDialog` son hardcodeados**
  Las unidades de medida en los combos (`"Kilogramo", "Litro", "Unidad"` y `"Hora", "Unidad", "Mensual"`) no se cargan dinámicamente del controlador. Nunca apuntarán a una `UnidadDeMedida` real del sistema. !!
* **Bug 30 — `OrdenDePagoDialog` tiene proveedores y comprobantes mock hardcodeados**
  Los proveedores son un array fijo `{"Proveedor Alfa S.A.", "Distribuidora Beta SRL"}` y la tabla de comprobantes muestra datos ficticios (`FC-0001`, `FC-0002`, `FC-0003`). No se cargan desde ningún controlador. !!
* **Bug 31 — Panel de Órdenes de Pago en `MenuPrincipal` tiene tabla con datos simulados**
  La tabla de OPs en `crearPanelOrdenesPago()` muestra datos ficticios hardcodeados (`"OP-001"`, `"OP-002"`) sin vínculo con ningún controlador ni modelo. !!
* **Bug 32 — `Usuario.setPassword()` es `private`**
  El setter de contraseña es `private`, lo que impide que un usuario cambie su contraseña desde fuera de la clase. Combinado con el Bug 17 (password hardcodeada `"1415"`), todos losz usuarios creados por el sistema quedan con contraseña permanente e inmodificable. !!
* **Bug 33 — `Proveedor` no sobreescribe `equals()` ni `hashCode()`**
  El método `existeProveedor()` en `ControladorDeOrdenDeCompra` usa `proveedores.contains(proveedor)`, que depende de `Object.equals()` (comparación por referencia). Si el objeto `Proveedor` llega desde una ruta diferente, fallará silenciosamente. Es el mismo tipo de error que el Bug 12 pero aplicado a la clase `Proveedor`.
* **Bug 34 — `registrarProveedor()` bypasea `asociarRubro()` accediendo directo al ArrayList**
  En `ControladorProveedores.registrarProveedor()` línea 51, se hace `nuevo.getRubroProveedor().add(r)` accediendo directamente al `ArrayList` interno en vez de usar `nuevo.asociarRubro(r)`. Esto bypasea la validación de duplicados que tiene `asociarRubro()` (que hace un check con `.contains()` antes de agregar). Es una violación de encapsulamiento.
* **Bug 35 — `Item` no tiene setters para ningún atributo**
  La clase `Item` carece completamente de setters para `descripcionDeItem`, `unidadMedida`, `tipoDeIVA` y `rubro`. Si se quisiera implementar `modificarItem()` en el controlador, sería imposible sin refactorear el modelo primero.
* **Bug 36 — `ControladorProductosYServicios` no tiene método `eliminarItem()`**
  Solo es posible agregar y listar ítems. No se puede dar de baja un producto o servicio ya registrado.
* **Bug 37 — `UsuarioDTO` importa clases del modelo (misma violación que Bug 25)**
  `UsuarioDTO.java` tiene imports de `Farmared.model.user.Rol` y `Farmared.model.user.Usuario` que nunca se usan en la clase. Es la misma violación del patrón DTO que el Bug 25 pero en otro archivo.
* **Bug 38 — `registrarPrecioProveedor()` viola encapsulamiento accediendo directo a listas**
  En `ControladorProveedores.registrarPrecioProveedor()` líneas 141-142, se hace `prov.getPrecioPorItem().add(nuevoPrecio)` y `item.getPrecioItem().add(nuevoPrecio)` en vez de usar los métodos propios `prov.agregarPrecioItem(nuevoPrecio)` y `item.agregarPrecio(nuevoPrecio)`. Expone las listas internas y bypasea cualquier validación futura.
* **Bug 39 — `eliminarProveedor()` no verifica referencias activas**
  Se puede eliminar un proveedor sin verificar si tiene OCs activas, comprobantes pendientes, o precios asociados. Esto dejaría `PrecioProveedor`, `OrdenDeCompra` y potenciales `Comprobante` apuntando a un `Proveedor` fantasma (referencia colgante / integridad referencial rota).
* **Bug 40 — `Domicilio.toString()` está incompleto**
  El `toString()` de `Domicilio` solo incluye `calle`, `numero` y `codigoPostal`, omitiendo `ciudad` y `pais`. Genera información parcial en cualquier log, debug o representación textual.

---

## 3. Lo que FALTA (Carencias críticas)

> [!IMPORTANT]
> El proyecto actual funciona como un "esqueleto". Las siguientes entidades, controladores y vistas son obligatorias para cumplir con los Requerimientos Funcionales de la Segunda Fase.

### Módulos Faltantes
1. **Recepción y Validación de Comprobantes (Facturación)**
   * **Qué falta:** La clase `Comprobante` está vacía. Faltan las subclases `Factura`, `NotaCredito` y `NotaDebito`. Falta lógica para asociar facturas a Órdenes de Compra (OC).
   * **Justificación:** Se exigen validaciones automáticas al cargar facturas contra los precios de la OC. Faltan las alertas para que un supervisor autorice posibles diferencias de precios.
2. **Lógica Financiera de Retenciones e Impuestos**
   * **Qué falta:** Controladores y ventanas para parametrizar impuestos (IVA, IIBB, Ganancias) y cargar certificados de no retención.
   * **Justificación:** Requisito indispensable de cálculo lógico y matemático antes de emitir Órdenes de Pago.
3. **Módulo Funcional de Órdenes de Pago (OP)**
   * **Qué falta:** Existe la interfaz gráfica a medias, pero falta el modelo `OrdenDePago`, su controlador y las representaciones de los medios de pago.
   * **Justificación:** Obligatorio registrar datos específicos de cheques y permitir cancelaciones parciales o totales.
4. **Flujo de Autorización de Órdenes de Compra (OC)**
   * **Qué falta:** Lógica e interfaz para que el rol `SUPERVISOR` apruebe explícitamente las OC que superan límites de deuda (estado `PENDIENTE_AUTORIZACION`).
5. **Módulo de Consultas y Reportes**
   * **Qué falta:** Faltan todas las vistas y métodos en controladores para emitir reportes.
   * **Justificación:** El Requerimiento 7 exige Libro IVA Compras, deudas detalladas, total retenido, listado de impagos, etc.

### Clases completamente ausentes (Según Diagramas de Secuencia Fase 1)
El diseño exigido incluye las siguientes clases estructurales que no existen en el código y son obligatorias:
* `DetalleComprobante`: Línea de detalle de una factura (ítem, cantidad, precio facturado) - Pertenece a "Registrar Factura".
* `FormaDePago`: Clase abstracta/superclase de los medios de pago - Pertenece a "Emitir OP".
* `Cheque`: Extiende FormaDePago, agrega nro, fechas, firmante - Pertenece a "Emitir OP".
* `Transferencia`: Extiende FormaDePago - Pertenece a "Emitir OP".
* `Efectivo`: Extiende FormaDePago - Pertenece a "Emitir OP".
* `DetalleCancelacion`: Asocia una OP con un comprobante para una cancelación total o parcial - Pertenece a "Emitir OP".
* `OrdenDePago`: Modelo principal de la OP - Pertenece a "Emitir OP".
* `EstadoComprobante` (Enum): Este tipo enumerado es vital y es referenciado en comentarios de `Comprobante`, pero nunca fue creado. 

### DTOs Faltantes (Arquitectura de Transferencia Rota)
* **No existe `OrdenDeCompraDTO`**: El controlador `ControladorDeOrdenDeCompra` no tiene forma de devolver datos a la vista.
* **No existe `CuentaCorrienteDTO`**: Imposible mostrar el estado de la cuenta corriente al usuario.
* **No existe `ComprobanteDTO`**: Necesario para el módulo de recepción de facturas.
* **No existe `OrdenDePagoDTO`**: Necesario para el módulo de emisión de OP.

### Controladores faltantes (Según Diagramas de Secuencia Fase 1)
Los diagramas de secuencia dictan la existencia de dos orquestadores que no fueron programados:
* `ControladorDeComprobantesRecibidos`: Debe orquestar el flujo de registrar facturas, validar contra la OC, actualizar la cuenta corriente y pedir autorización en caso de diferencias de precio.
* `ControladorDeOrdenesDePago`: Debe orquestar el flujo de emitir la OP, calcular retenciones, crear el `DetalleCancelacion` y registrar los medios de pago.

### Vistas Completamente Inexistentes
* **Pantalla de Emisión de Órdenes de Compra (OC):** No hay forma de crear, agregar detalles, o aprobar OCs.
* **Pantalla de Recepción de Comprobantes (Facturas):** Imposible cargar facturas al sistema.
* **Gestión Impositiva:** Falta pantalla de Certificados de no retención.
* **Gestión de Usuarios:** No hay GUI para invocar `altaUsuario()`, modificarlos o listarlos.
* **Consulta de Cuenta Corriente:** No existe pantalla para que el proveedor vea su cuenta, a pesar de estar exigido en la Fase 1.
* **Registro de Precio por Proveedor:** No hay GUI para asociar un proveedor con un ítem y definir su precio. Afecta directamente al requerimiento de compulsa de precios.
* **Detalles de Medios de Pago:** Interfaz para cargar Cheques.
* **Consultas y Reportes:** No existen las pantallas de emisión de libros o compulsa de precios.
* **Modificar/Eliminar Ítems:** Solo existe alta de productos y servicios. No hay forma de editar ni borrar un ítem ya registrado desde la interfaz.
* **Modificar/Eliminar Rubros:** Solo existe alta de rubros. No hay forma de editar ni borrar un rubro existente.
* **Listado de Unidades de Medida:** No existe pantalla para consultar qué unidades están cargadas en el sistema.
* **Logout / Cambio de usuario:** Una vez logueado, no se puede cambiar de usuario sin cerrar la aplicación completa.

### Faltantes estructurales y de código
* Falta `registrarUnidadDeMedida()` en el controlador.
* Falta método para iterar la OC desde una factura y comparar los precios para autorizaciones.
* Falta la asociación referencial entre `OrdenDeCompra` y `Comprobante` en el modelo para lograr la trazabilidad completa.
* Falta método en `UtilDate` para hacer el parseo de `String` a `Date`, indispensable para recolectar datos de fechas desde las Vistas (ej: fecha inicio de actividades).
* **Regla de negocio no capturada en Cuenta Corriente:** `calcularDeuda()` carece de la lógica para diferenciar que una `Factura` o `NotaDebito` suma a la deuda, mientras que una `NotaCredito` resta.
* **Gestión de precios y asociaciones:** Faltan métodos `Proveedor.eliminarPrecioItem()` y `Item.eliminarPrecio()`. No se pueden revertir las asociaciones.
* **Falta de sobreescritura estructural:** `Rubro` carece de `equals()` y `hashCode()`, por lo que validaciones con `.contains()` pueden fallar y generar duplicados.
* **Método inútil en Orden de Compra:** `OrdenDeCompra.reporteOC()` devuelve un `List` que sólo contiene la instancia `this`, careciendo de sentido como reporte.
* **Faltan getters vitales en `CuentaCorriente`:** No posee `getComprobantes()`, por lo que son invisibles desde fuera de la clase.
* **Problemas de inicialización en impuestos:** `ImpuestoRetenible` no inicializa `minimoNoImponible` en su constructor. Además, `Proveedor` no tiene métodos como `agregarImpuesto()` o `agregarCertificado()` (viola el encapsulamiento accediendo a las listas directamente).
* **Visibilidad inaccesible:** Los métodos internos de `RangoDeRetencion` y `CertificadoNoRetencion` están declarados como `private`, lo que impide que otra capa de validación los llame cuando sean implementados.

### Faltantes adicionales de modelo (detectados en revisión posterior)
* **`Rubro` no tiene setters:** No se puede modificar un rubro existente (ni nombre, ni tipo). Falta también `eliminarRubro()` en el controlador.
* **`PrecioProveedor` no tiene setters:** El precio de un ítem para un proveedor no se puede actualizar una vez creado. Si el proveedor cambia de precio, hay que crear otro `PrecioProveedor` pero no hay lógica para invalidar/reemplazar el anterior.
* **`UnidadDeMedida` no tiene setters:** No se puede modificar una unidad existente.
* **`OrdenDeCompra` no tiene `getFechaEmision()`:** Impide mostrar la fecha de emisión en reportes o vistas, a pesar de que el campo `fechaEmision` existe en el modelo.
* **`Proveedor.setCuit()` no existe:** Si se registra un CUIT mal, no hay forma de corregirlo sin eliminar y volver a crear el proveedor entero.

### Faltantes vinculados directamente a la consigna del TP
* **Compulsa de Precios:** El requerimiento exige poder comparar precios de un mismo ítem entre distintos proveedores. No existe ni la lógica de consulta ni la vista para esto. Se necesita poder seleccionar un ítem y ver todos los `PrecioProveedor` asociados ordenados para elegir el mejor precio.
* **Consulta de Cuenta Corriente (Fase 1):** El método `cuentaCorriente()` del controlador devuelve la instancia del modelo directamente en vez de un DTO, violando el patrón de arquitectura. Además, no hay vista para mostrarlo.
* **Flujo completo de OC según diagrama de secuencia:** El diagrama exige: seleccionar proveedor → agregar ítems con cantidad → calcular total automáticamente → validar tope de deuda → si excede, solicitar autorización del supervisor → cambiar estado. Actualmente `emitirOC()` solo crea una OC vacía sin ítems ni creador.

### Faltantes de arquitectura y calidad
* **No hay manejo centralizado de excepciones:** Cada controlador lanza excepciones genéricas (`Exception`, `RuntimeException`). No existe una jerarquía de excepciones del dominio más allá de `UsuarioNoEncontradoException`.
* **Anti-patrón en `UtilDate`:** En `ControladorProveedores.toDTO()` se instancia `new UtilDate()` para llamar a `parseDate()` que es un método `static`. Funciona, pero es un anti-patrón que refleja uso incorrecto de métodos estáticos.

