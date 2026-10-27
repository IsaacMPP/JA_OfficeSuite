# JA OfficeSuite — Migración controlada a WordCraft Android

## Base elegida
- Upstream: https://github.com/artcraft-android/wordcraft-android
- Commit verificado desde el ZIP entregado: `ee1d11b4439cd5f52fa476531df7d5825dcd8b66`.
- Integración por submódulo Git: `upstream/wordcraft-android`; **no** se copian ni reetiquetan fuentes ajenas.
- Licencia upstream: MIT OR Apache-2.0. Mantener LICENSE-MIT, LICENSE-APACHE, NOTICE, ATTRIBUTION y otras licencias de archivos.
- Marca, iconos y nombre WordCraft requieren auditoría antes de distribución rebrandeada.
- La aplicación original de JA OfficeSuite v0.3.0 sigue preservada en `feature/android-foundation`; `main` permanece sin cambios.

## Estado de esta rama
Esta rama es una **prueba de compilación del upstream** en Linux y Android arm64, aún **no** una versión renombrada ni una aplicación JA OfficeSuite lista para instalar. Se evita cambiar package/applicationId hasta lograr un APK íntegro.

## Validación
- CI: `.github/workflows/wordcraft-android.yml` compila desde el submódulo, instala Java 17, Rust, cargo-ndk y SDK/NDK.
- Revisar registro de Actions, artefactos y errores de dependencias/fuentes antes de declarar validación.
- Por tamaño/costo del compilado nativo, solo compila arm64 para esta prueba inicial.
- Si upstream no compila en Linux, corregir mediante cambios rastreables en una rama/fork propio, sin modificar indiscriminadamente la copia fuente.

## Próximos pasos
1. Obtener APK arm64 desde Actions y verificar instalación.
2. Confirmar funciones DOCX de ida/vuelta y fidelidad en dispositivos.
3. Preparar fork propio para modificar Kotlin/Rust; ajustar identificador y nombre JA OfficeSuite, sustituir recursos de marca con licencia dudosa.
4. Cerrar o etiquetar el PR anterior solo cuando la nueva base esté validada.
