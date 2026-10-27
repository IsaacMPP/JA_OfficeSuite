# JA OfficeSuite

Editor de documentos para Android (celulares y tablets), en desarrollo.

## Estado
**v0.1.0 — prototipo inicial.** Editor TXT con selector de archivos Android. Aún no hay importación/exportación DOCX, motor Rust ni interfaz Compose. No está listo para producción.

## Compilación
La acción [Android APK](../../actions/workflows/android.yml) ejecuta Gradle 8.9, Java 17 y SDK 35 y publica un APK debug como artefacto.

En local, con Android SDK y Java 17 configurados: `gradle :app:assembleDebug`.

## Desarrollo
Trabajamos en ramas feature y PR borrador. Seguimiento: [docs/CONTINUIDAD_CHATGPT.md](docs/CONTINUIDAD_CHATGPT.md).

## Origen e interoperabilidad
Proyecto independiente; WordCraft se considera como referencia técnica. No se ha incorporado ni redistribuido código de terceros. Cualquier integración futura requiere auditoría de licencias y dependencias.
