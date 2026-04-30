# Sistema de Gestión de Estudiantes - Seguridad Avanzada

Proyecto académico que implementa las mejores prácticas de seguridad con Spring Security 6 y Thymeleaf.

## Tecnologías
- **Java 17**
- **Spring Boot 3.4.5**
- **Spring Security 6** (CSRF, CSP, @PreAuthorize)
- **Thymeleaf** (Escape automático XSS)
- **Maven**
- **BCrypt** (12 rondas)

---

## Configuración de Seguridad Implementada

### 1. @PreAuthorize en Capa de Servicio
Se aplicaron anotaciones de seguridad en `UsuarioService.java` con lógica diferente:

| Método | Anotación | Propósito |
|--------|-----------|-----------|
| `listarTodos()` | `@PreAuthorize("hasRole('ADMIN')")` | Solo administradores |
| `buscarPorEmail(email)` | `@PreAuthorize("hasAnyRole('ADMIN','USER') and (#email == authentication.name or hasRole('ADMIN'))")` | Dueño del perfil o admin |
| `cambiarRol(id, rol)` | `@PreAuthorize("hasRole('ADMIN')")` | Solo administradores |
| `actualizarNombre(usuario)` | `@PreAuthorize("#usuario.email == authentication.name or hasRole('ADMIN')")` | Dueño o admin |

### 2. Protección XSS (Cross-Site Scripting)
Thymeleaf configurado con escape automático (`th:text` en lugar de `th:utext`):
```html
<p>Nombre: <span th:text="${usuario.nombre}"></span></p>
```

**Prueba XSS realizada:**
- **Inyección intentada:** `<script>alert('XSS')</script>` en el campo nombre
- **Resultado:** El navegador muestra el texto literal `<script>alert('XSS')</script>` sin ejecutar el script
- **Captura:** `capturas/xss-escapado-dashboard.png`

### 3. Protección CSRF (Cross-Site Request Forgery)
Configurado en `SecurityConfig.java`:
```java
.csrf(csrf -> csrf
    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
)
```

**Prueba CSRF realizada:**
- **Método:** Enviar petición POST sin token CSRF usando curl/Postman
- **Petición:** `curl -X POST http://localhost:8080/admin -d "dato=test"`
- **Respuesta del servidor:** `403 Forbidden` - Acceso denegado
- **Captura:** `capturas/csrf-403.png`

### 4. Content Security Policy (CSP)
Cabecera configurada para prevenir inyección de recursos maliciosos:
```java
.contentSecurityPolicy(csp -> csp
    .policyDirectives(
        "default-src 'self'; " +
        "script-src 'self'; " +
        "style-src 'self' 'unsafe-inline'; " +
        "img-src 'self' data:; " +
        "frame-ancestors 'none'"
    )
)
```

**Verificación CSP:**
- **Herramienta:** Inspector del navegador (F12) → Pestaña Network → Headers de respuesta
- **Cabecera presente:** `Content-Security-Policy: default-src 'self'; script-src 'self';...`
- **Captura:** `capturas/csp-header.png`

### 5. Página 403 Personalizada
Vista dedicada en `templates/error/403.html` con diseño profesional que muestra:
- Usuario autenticado actual (`sec:authentication="name"`)
- Mensaje claro de acceso denegado
- Botón de retorno al dashboard

**Prueba de acceso no autorizado:**
- **Usuario:** `user@test.com` (Rol: USER)
- **Acción:** Intentar acceder a `/admin`
- **Respuesta:** Redirección a `/error/403` con página personalizada
- **Captura:** `capturas/acceso-denegado-403.png`

---

## Evidencias de Seguridad

| Prueba | Descripción | Captura |
|--------|-------------|---------|
| XSS Mitigado | Inyección de script no ejecutada | `xss-escapado-dashboard.png` |
| CSRF Activo | POST sin token rechazado (403) | `csrf-403.png` |
| CSP Header | Cabecera presente en respuesta | `csp-header.png` |
| Acceso Denegado | @PreAuthorize bloquea acceso USER a /admin | `acceso-denegado-403.png` |

---

## Instrucciones de Ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/KeiverJ/castellanos-post2-u9.git
   cd castellanos-post2-u9
   ```

2. **Ejecutar la aplicación:**
   ```bash
   ./mvnw spring-boot:run
   ```
   O en Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```

3. **Acceder a la aplicación:**
   - URL: `http://localhost:8080`
   - Login: `/login`
   - Registro: `/registro`

4. **Usuarios de prueba:**
   - **Admin:** `admin@test.com` / `admin123` (Rol: ADMIN)
   - **Usuario:** `user@test.com` / `user123` (Rol: USER)

5. **Probar seguridad:**
   - Login como USER e intentar acceder a `/admin` → Debe mostrar 403
   - Verificar cabecera CSP en inspector (F12 → Network → Response Headers)
   - Intentar inyectar `<script>` en registro → Debe mostrarse como texto plano

---

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/universidad/estudiantes/
│   │   ├── config/
│   │   │   └── SecurityConfig.java      # Configuración de seguridad
│   │   ├── controller/
│   │   │   ├── AuthController.java
│   │   │   ├── ErrorController.java     # Manejo 403
│   │   │   └── ...
│   │   ├── service/
│   │   │   └── UsuarioService.java      # @PreAuthorize aplicado
│   │   └── ...
│   └── resources/
│       ├── templates/
│       │   ├── error/
│       │   │   └── 403.html             # Página personalizada
│       │   └── ...
│       └── application.properties
└── test/
    └── ...
```

---

## Commits de Seguridad

| Commit | Descripción |
|--------|-------------|
| `feat: aplicar @PreAuthorize en UsuarioService` | Anotaciones de seguridad en servicio |
| `feat: agregar pagina 403 personalizada` | Vista dedicada para acceso denegado |
| `fix: reforzar CSRF y manejo 403` | Protección CSRF activa y manejo de errores |
| `feat: configurar cabecera CSP` | Política de seguridad de contenido |
| `fix: actualizar SecurityConfig y pagina 403 restableciendo configuracion original de CSRF y diseño personalizado` | Ajustes de configuración |

---

## Notas de Seguridad

- ✅ @PreAuthorize habilitado con `@EnableMethodSecurity`
- ✅ CSRF protegido con tokens en cookies (HttpOnly false para JS)
- ✅ XSS mitigado por escape automático de Thymeleaf
- ✅ CSP previene carga de recursos externos no autorizados
- ✅ Página 403 personalizada con información del usuario autenticado
- ✅ Contraseñas hasheadas con BCrypt (12 rondas)
