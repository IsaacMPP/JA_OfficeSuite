# JA OfficeSuite — Continuidad
## Estado inicial
Proyecto Android separado del código fuente original WordCraft. Rama de desarrollo: `feature/android-foundation`. No fusionar sin revisión.

## Arquitectura prevista
- Android nativo; interfaz final con Jetpack Compose.
- Evaluar reutilización del motor Rust de WordCraft **después** de auditoría técnica y de licencias.
- Actualmente hay un prototipo Kotlin de edición de texto TXT, no un editor DOCX.
- Compilación reproducible por GitHub Actions; artefacto APK debug.
- Proteger main, usar PR en borrador, revisar logs y artefacto después de cada modificación.

## Próximos pasos
1. Confirmar que compila APK y se instala.
2. Auditar WordCraft original, dependencias, licencia y motor de documentos.
3. Introducir diseño Compose con interfaz adaptativa móvil/tablet.
4. Seleccionar motor DOCX e implementar lectura/escritura sin pérdida de formato.
5. Pruebas de ida y vuelta, documentos complejos y rendimiento.
