# castellanos-post2-u9

## Descripción
Sistema de gestión de estudiantes con seguridad avanzada implementando Spring Security.

## Características de Seguridad
- Autenticación con BCrypt (12 rondas)
- Autorización con @PreAuthorize
- Protección CSRF con cookies
- Cabeceras CSP configuradas
- Página 403 personalizada
- Manejo seguro de Thymeleaf

## Estructura
- `src/main/java` - Código fuente
- `src/main/resources/templates` - Vistas Thymeleaf
- `src/test` - Pruebas unitarias

## Tecnologías
- Java 17
- Spring Boot 3.4.5
- Spring Security
- Thymeleaf
- Maven
