# castellanos-post2-u9

## Sistema de Gestión de Estudiantes con Seguridad Avanzada

Aplicación web desarrollada con Spring Boot que implementa las mejores prácticas de seguridad.

## Características Implementadas

### Seguridad
- **@PreAuthorize**: Anotaciones de seguridad en capa de servicio
- **CSRF**: Protección contra ataques Cross-Site Request Forgery
- **CSP**: Content Security Policy configurada
- **XSS**: Manejo seguro con Thymeleaf
- **Página 403**: Personalizada para acceso denegado

### Funcionalidades
- Registro y autenticación de usuarios
- Gestión de estudiantes y cursos
- Panel de administración
- Roles (USER, ADMIN)

## Tecnologías
- Java 17
- Spring Boot 3.4.5
- Spring Security 6
- Thymeleaf
- Maven
- BCrypt para hashing

## Estructura del Proyecto
```
src/
├── main/
│   ├── java/com/universidad/estudiantes/
│   │   ├── config/      # Configuración de seguridad
│   │   ├── controller/  # Controladores MVC
│   │   ├── model/       # Entidades
│   │   ├── repository/  # Acceso a datos
│   │   └── service/     # Lógica de negocio
│   └── resources/
│       └── templates/   # Vistas Thymeleaf
└── test/                # Pruebas unitarias
```

## Ejecución
```bash
./mvnw spring-boot:run
```
