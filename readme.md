# MultiUserApplication

## Inhaltsverzeichnis

- [Über das Projekt](#über-das-projekt)
- [Technologien](#technologien)
- [Projektstruktur](#projektstruktur)
- [Installation](#installation)
- [Verwendung](#verwendung)
- [API-Endpunkte](#api-endpunkte)
    - [Benutzerregistrierung und Authentifizierung](#benutzerregistrierung-und-authentifizierung)
    - [Verwaltung eigener Buchungen als Mitglied](#verwaltung-eigener-buchungen-als-mitglied)
    - [Verwaltung der Buchungen durch Administratoren](#verwaltung-der-buchungen-durch-administratoren)
    - [Verwaltung von Mitgliedern durch Administratoren](#verwaltung-von-mitgliedern-durch-administratoren)
    - [Zusätzliche Anforderungen](#zusätzliche-anforderungen)
- [Sicherheit](#sicherheit)
- [End-to-End-Tests](#end-to-end-tests)
- [Autoren](#autoren)
- [Lizenz](#lizenz)

## Über das Projekt

MultiUserApplication ist eine Webanwendung, die es mehreren Benutzern ermöglicht, sich zu registrieren, zu authentifizieren und Buchungen zu verwalten. Administratoren haben zusätzliche Rechte zur Verwaltung von Benutzern und Buchungen.

## Technologien

- Java
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- JPA (Java Persistence API)
- H2 Database
- Maven
- SpringDoc OpenAPI (für Swagger-Dokumentation)

## Projektstruktur

## Inhaltsverzeichnis

- [Über das Projekt](#über-das-projekt)
- [Technologien](#technologien)
- [Projektstruktur](#projektstruktur)
- [Installation](#installation)
- [Verwendung](#verwendung)
- [API-Endpunkte](#api-endpunkte)
- [Sicherheit](#sicherheit)
- [End-to-End-Tests](#end-to-end-tests)
- [Autoren](#autoren)
- [Lizenz](#lizenz)

## Über das Projekt

MultiUserApplication ist eine Webanwendung, die es mehreren Benutzern ermöglicht, sich zu registrieren, zu authentifizieren und Buchungen zu verwalten. Administratoren haben zusätzliche Rechte zur Verwaltung von Benutzern und Buchungen.

## Technologien

- Java
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- JPA (Java Persistence API)
- H2 Database
- Maven
## Projektstruktur
src/
├── main/
│ ├── java/
│ │ ├── com/
│ │ │ ├── example/
│ │ │ │ ├── multiuserapplication/
│ │ │ │ │ ├── controller/
│ │ │ │ │ │ ├── BookingController.java
│ │ │ │ │ │ ├── TaskController.java
│ │ │ │ │ │ ├── UserController.java
│ │ │ │ │ ├── domain/
│ │ │ │ │ │ ├── Booking.java
│ │ │ │ │ │ ├── Day.java
│ │ │ │ │ │ ├── Room.java
│ │ │ │ │ │ ├── Task.java
│ │ │ │ │ │ ├── TasksUser.java
│ │ │ │ │ │ ├── TasksUserRoles.java
│ │ │ │ │ ├── dto/
│ │ │ │ │ │ ├── TaskDTO.java
│ │ │ │ │ │ ├── TasksUserDTO.java
│ │ │ │ │ ├── mapper/
│ │ │ │ │ │ ├── TaskMapper.java
│ │ │ │ │ ├── repositories/
│ │ │ │ │ │ ├── BookingRepository.java
│ │ │ │ │ │ ├── DayRepository.java
│ │ │ │ │ │ ├── RoomRepository.java
│ │ │ │ │ │ ├── TaskRepository.java
│ │ │ │ │ │ ├── UserRepository.java
│ │ │ │ │ ├── security/
│ │ │ │ │ │ ├── JwtAuthenticationFilter.java
│ │ │ │ │ │ ├── JwtAuthorizationFilter.java
│ │ │ │ │ │ ├── JwtTokenUtil.java
│ │ │ │ │ │ ├── TasksUserDetailsService.java
│ │ │ │ │ │ ├── WebSecurity.java
│ │ │ │ │ │ ├── WebSecurityConstants.java
│ │ │ │ │ ├── config/
│ │ │ │ │ │ ├── OpenApiConfig.java
│ ├── resources/
│ │ ├── application.properties
├── test/
│ ├── java/
│ │ ├── com/
│ │ │ ├── example/
│ │ │ │ ├── multiuserapplication/
│ │ │ │ │ ├── EndToEndTests.java
│ ├── resources/

## Installation

### Voraussetzungen

- Java 11 oder höher
- Maven
- Ein IDE wie IntelliJ IDEA oder Eclipse

### Schritte

1. Klone das Repository:
   ```bash
   git clone https://github.com/hereIsLucas/MultiUserApplication.git
Navigiere in das Projektverzeichnis:
bash
Code kopieren
cd MultiUserApplication
Baue das Projekt mit Maven:
bash
Code kopieren
mvn clean install
Starte die Anwendung:
bash
Code kopieren
mvn spring-boot:run
Verwendung
Nach dem Starten der Anwendung ist sie unter http://localhost:8080 erreichbar.

API-Dokumentation
Die API-Dokumentation kann mit Swagger aufgerufen werden. Navigiere zu http://localhost:8080/swagger-ui.html.

API-Endpunkte
Benutzerregistrierung und Authentifizierung
Registrierung:
POST /users/sign-up
Beispiel Payload:
json
Code kopieren
{
"username": "testuser",
"password": "password"
}

### Erklärung:

- **Über das Projekt**: Eine kurze Einführung in das Projekt.
- **Technologien**: Eine Liste der verwendeten Technologien.
- **Projektstruktur**: Ein Überblick über die Verzeichnisstruktur.
- **Installation**: Schritte zur Installation und zum Starten der Anwendung.
- **Verwendung**: Grundlegende Anweisungen zur Verwendung der Anwendung.
- **API-Endpunkte**: Eine Liste der wichtigsten API-Endpunkte.
- **Sicherheit**: Informationen zur Sicherheitskonfiguration.
- **End-to-End-Tests**: Details zu den End-to-End-Tests.
- **Autoren**: Eine Liste der Autoren des Projekts.
- **Lizenz**: Lizenzinformationen für das Projekt.

Diese README-Datei bietet einen umfassenden Überblick über das Projekt und hilft neuen Entwicklern, schnell zu verstehen, wie sie es installieren
