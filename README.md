```markdown
# DummyJSON RestAssured API Tests

![CI](https://github.com/jmr85/dummyjson-restassured-tests/actions/workflows/deploy-report.yml/badge.svg)
[![Allure Report](https://img.shields.io/badge/Allure-Report-brightgreen.svg)](https://jmr85.github.io/dummyjson-restassured-tests/)

Proyecto de automatización de pruebas de API sobre **DummyJSON** utilizando **Java 21**, **RestAssured**, **TestNG**, **Gradle** y reportes **Allure**.  
El pipeline de CI/CD corre automáticamente todos los días y publica los resultados en GitHub Pages.

---

## 🚀 Características principales

- Pruebas de endpoints REST con validaciones exhaustivas.
- Ejecución automatizada diaria y manual vía [GitHub Actions](./.github/workflows/deploy-report.yml).
- Reportes Allure visuales, publicados automáticamente en [GitHub Pages](https://jmr85.github.io/dummyjson-restassured-tests/).
- Uso de TestNG con suite personalizada (`testNG.xml`).
- Ejemplo de adjuntos de respuestas JSON en el reporte.
- Separación clara de clases de prueba, utilidades y recursos.

---

## 📋 Estructura del proyecto

```
├── src
│   ├── test
│   │   ├── java
│   │   │   └── APITests
│   │   │        ├── AuthTests.java
│   │   │        ├── UsersTests.java
│   │   │        ├── ExampleTest.java
│   │   │        └── Utils/
│   │   └── resources
│   │        └── testNG.xml
├── build.gradle
├── .github/
│    └── workflows/
│         └── deploy-report.yml
└── README.md
```

---

## 🧪 Ejecución local

1. **Clonar el repositorio:**
   ```sh
   git clone https://github.com/jmr85/dummyjson-restassured-tests.git
   cd dummyjson-restassured-tests
   ```

2. **Ejecutar los tests:**
   ```sh
   ./gradlew clean test
   ```

3. **Ver el reporte Allure localmente (requiere [Allure Commandline](https://docs.qameta.io/allure/#_get_started)):**
   ```sh
   allure serve build/allure-results
   ```

---

## 📊 Ver el reporte online

Visualizá el último reporte Allure generado por CI/CD en:  
[https://jmr85.github.io/dummyjson-restassured-tests/](https://jmr85.github.io/dummyjson-restassured-tests/)

---

## ✍️ Contribuciones

¡Pull requests y sugerencias son bienvenidas!  
Si encontrás un bug o querés sumar tests, abrí un issue o mandá tu PR.

---

## 🛠️ Stack tecnológico

- Java 21
- Gradle
- TestNG
- RestAssured
- Allure Framework
- GitHub Actions

---

## 📃 Licencia

MIT

---

> Made with 💙 by [Juan Martin](https://github.com/jmr85)
```