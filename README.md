# Seguridad en Aplicaciones Web - Unidad 9 (Post-Contenido 2)

**Universidad:** UDES - Ingenieria de Sistemas (2026)
**Asignatura:** Programacion Web
**Repositorio:** castellanos-post2-u9

## Descripcion del proyecto y objetivo de la unidad

Este proyecto es la continuacion del sistema de estudiantes, cursos y autenticacion. El objetivo de la unidad es verificar de forma activa la seguridad de la aplicacion implementando:

- Autorizacion a nivel de metodo con `@PreAuthorize` y expresiones SpEL.
- Mitigacion de XSS usando `th:text` en Thymeleaf y una politica CSP basica.
- Proteccion CSRF comprobando que un POST sin token es rechazado.

## Arquitectura del sistema

```
┌─────────────────────────────────────────────────────────┐
│                   Vista (Thymeleaf)                      │
│ login.html | registro.html | dashboard.html | panel.html │
│ error/403.html | estudiantes/* | cursos/*                │
└───────────────────────────┬─────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────┐
│                    Controladores                         │
│ AuthController | CursoController | EstudianteController  │
│ ErrorController                                        │
└───────────────────────────┬─────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────┐
│                      Servicios                           │
│ UsuarioService | UsuarioDetailsService | CursoService    │
│ EstudianteService                                       │
└───────────────────────────┬─────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────┐
│                    Repositorios                          │
│ UsuarioRepository | CursoRepository | EstudianteRepository│
└───────────────────────────┬─────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────┐
│                    Base de datos                          │
│ MySQL 9.6                                                │
└─────────────────────────────────────────────────────────┘

Capa transversal de seguridad:
- `SecurityConfig` define autenticacion, autorizacion, CSP y CSRF.
- `@PreAuthorize` aplica reglas de acceso en `UsuarioService`.
```

## Tecnologias utilizadas

| Tecnologia       | Version           | Proposito                              |
| ---------------- | ----------------- | -------------------------------------- |
| Java             | 21                | Lenguaje de programacion               |
| Spring Boot      | 3.5.13            | Framework principal                    |
| Spring Security  | 6.x               | Autenticacion, autorizacion, CSRF, CSP |
| Spring Data JPA  | 6.x               | Acceso a datos con Hibernate           |
| Thymeleaf        | 3.x               | Motor de plantillas HTML               |
| Thymeleaf Extras | Spring Security 6 | Tags de seguridad en vistas            |
| MySQL            | 9.6               | Base de datos relacional               |
| Maven            | 3.9.12            | Gestion de dependencias y build        |
| Tomcat embebido  | 10.1.x            | Servidor integrado en Spring Boot      |

## Estructura del proyecto

```
castellanos-post2-u9/
├── .mvn/
├── capturas/
│   ├── acceso-denegado-403.png
│   ├── csp-header.png
│   ├── csrf-403.png
│   └── xss-escapado-dashboard.png
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/universidad/estudiantes/
│   │   │   ├── EstudiantesApplication.java
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── CursoController.java
│   │   │   │   ├── ErrorController.java
│   │   │   │   └── EstudianteController.java
│   │   │   ├── model/
│   │   │   │   ├── Curso.java
│   │   │   │   ├── Estudiante.java
│   │   │   │   └── Usuario.java
│   │   │   ├── repository/
│   │   │   │   ├── CursoRepository.java
│   │   │   │   ├── EstudianteRepository.java
│   │   │   │   └── UsuarioRepository.java
│   │   │   └── service/
│   │   │       ├── CursoService.java
│   │   │       ├── EstudianteService.java
│   │   │       ├── UsuarioDetailsService.java
│   │   │       └── UsuarioService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           ├── admin/panel.html
│   │           ├── auth/login.html
│   │           ├── auth/registro.html
│   │           ├── cursos/formulario.html
│   │           ├── cursos/inscribir.html
│   │           ├── cursos/lista.html
│   │           ├── dashboard.html
│   │           ├── error/403.html
│   │           └── estudiantes/
│   │               ├── confirmar-eliminar.html
│   │               ├── formulario.html
│   │               └── lista.html
│   └── test/java/com/universidad/estudiantes/EstudiantesApplicationTests.java
└── target/ (salida de compilacion)
```

## Prerrequisitos

- Java 21 configurado en `JAVA_HOME`.
- Maven 3.9.12 (o usar `mvnw`).
- MySQL 9.6 en ejecucion.
- Puerto 8080 disponible.

## Configuracion inicial de base de datos

1. Crear la base de datos:

```sql
CREATE DATABASE IF NOT EXISTS estudiantes_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

2. Crear usuario de aplicacion (si no existe):

```sql
CREATE USER IF NOT EXISTS 'appuser'@'localhost'
  IDENTIFIED BY 'apppass';

GRANT ALL PRIVILEGES ON estudiantes_db.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;
```

3. Si necesitas un administrador, actualiza el rol de un usuario registrado:

```sql
UPDATE usuarios
SET rol = 'ROLE_ADMIN'
WHERE email = 'admin@universidad.edu';
```

## Instrucciones de ejecucion

1. Abrir una terminal en la raiz del proyecto.
2. Ejecutar la aplicacion:

```
./mvnw spring-boot:run
```

3. Abrir el navegador en:

```
http://localhost:8080/login
```

## Funcionalidades principales y endpoints

- Registro de usuarios y autenticacion por formulario.
- Dashboard con acceso segun rol.
- Panel de administracion para listar usuarios.
- Gestion de estudiantes y cursos.

Endpoints principales:

- `GET /login` y `POST /login`
- `GET /registro` y `POST /registro`
- `GET /dashboard`
- `GET /admin`
- `POST /logout`
- `GET /estudiantes`, `POST /estudiantes/guardar`, `POST /estudiantes/eliminar/{id}`
- `GET /cursos`, `POST /cursos/guardar`, `POST /cursos/{cursoId}/inscribir/{estudianteId}`

## Seguridad aplicada

- `@PreAuthorize` con expresiones distintas en `UsuarioService` para:
  - Listado exclusivo de administradores.
  - Lectura por el propio usuario o por ADMIN (incluye `hasAnyRole`).
  - Cambio de rol solo por ADMIN.
  - Actualizacion de nombre solo por su propietario o ADMIN.
- Vista 403 personalizada para accesos no autorizados.
- Mitigacion XSS con `th:text` en el dashboard.
- Politica CSP basica enviada por cabecera HTTP.
- CSRF activo y validado con rechazo de POST sin token.

## Pruebas de seguridad realizadas

- **Acceso denegado con roles:** un usuario con rol `USER` intento acceder a `/admin` y el servidor devolvio **403** con la vista personalizada que muestra el usuario autenticado.
- **XSS almacenado:** se registro un usuario con nombre `<script>alert("XSS")</script>` y en el dashboard se mostro el texto escapado (`&lt;script&gt;...`) sin ejecutar el script.
- **CSP:** se verifico que la respuesta HTTP incluye la cabecera `Content-Security-Policy` con las directivas definidas.
- **CSRF:** se envio un `POST /logout` sin token CSRF y el servidor respondio **403 Forbidden**.

## Evidencia visual

**Acceso denegado con 403 personalizado**

![Acceso denegado 403](capturas/acceso-denegado-403.png)

**Mitigacion XSS en dashboard (contenido escapado)**

![XSS escapado en dashboard](capturas/xss-escapado-dashboard.png)

**Cabecera Content-Security-Policy en respuesta**

![CSP Header](capturas/csp-header.png)

**Proteccion CSRF (POST sin token rechazado)**

![CSRF 403](capturas/csrf-403.png)

## Decisiones de diseno y justificacion tecnica

- Las reglas de acceso se aplican en la capa de servicio para separar la seguridad de la capa web.
- Se usa `th:text` para escapar contenido del usuario y evitar XSS almacenado.
- Se aplica CSP basico para limitar fuentes de scripts, estilos e imagenes.
- La pagina 403 es una vista dedicada que informa el usuario autenticado.

## Solucion de problemas frecuentes

- **Puerto 8080 ocupado:** cierra el proceso o libera el puerto antes de iniciar.
- **Error de conexion a MySQL:** verifica que `estudiantes_db` exista y que el usuario `appuser` tenga permisos.
- **No puedes ver el panel admin:** actualiza el rol del usuario a `ROLE_ADMIN` en la base de datos.
