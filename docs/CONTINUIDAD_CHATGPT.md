# JA OfficeSuite — Continuidad

## Estado comprobado y rama
- Repo: IsaacMPP/JA_OfficeSuite; rama `feature/android-foundation`, PR #1 en borrador, mantener main intacta.
- La v0.1.0 compiló APK mediante Actions (run 40431995094).
- v0.2.0 sustituye la pantalla prototipo por interfaz Jetpack Compose adaptable a celular/tablet, lectura y escritura TXT, ajuste visual del tamaño del texto, título y avisos de error.
- No existe edición DOCX, formatos ricos, ni importación Rust en Android. No describirlos como disponibles.
- GitHub Actions `.github/workflows/android.yml` genera APK debug: confirmar resultado del nuevo commit antes de afirmarlo.
- El archivo TXT se guarda con UTF-8; requiere prueba real en un dispositivo, también al rotar y usando almacenamiento SAF.

## Auditoría preliminar de WordCraft proporcionado por el usuario
- ZIP `wordcraft-main.zip`, workspace 0.11.0, Rust edition 2024 y rust-version 1.95.
- Módulos relevantes: `crates/doc`, `crates/docx`, `crates/layout`, `crates/engine`, `crates/render`, `crates/pdf`. Separación modular útil, no equivale a compatibilidad Android demostrada.
- Workspace declarado MIT OR Apache-2.0; incluye `LICENSE-MIT`, `LICENSE-APACHE`, `NOTICE`, `ATTRIBUTION.md`; marca y ciertos recursos tienen licencia restringida (`docs/brand/LICENSE-brand.txt`). No copiar activos de marca.
- El workspace contiene dependencias git `craft-pdf`, `craft-update`, y pila egui/eframe de escritorio. NO trasladar workspace completo a Android sin revisar compilación y licencias.
- Próximo experimento: aislar subárbol `doc` y `docx`, revisar dependencias y construir un puente JNI/UniFFI con un fixture DOCX usando NDK. Antes de escribir documentos complejos, ejecutar pruebas de ida-vuelta y comprobar pérdida de formato.

## Pendientes ordenados
1. Confirmar CI v0.2.0 y resolver errores reales, descargar APK.
2. Pruebas en Android (abrir, editar, guardar TXT, rotación, tablet, accesibilidad).
3. Auditar dependencias Rust Android y prototipo separado de lectura DOCX.
4. Implementar edición visual rica y persistencia del documento una vez validado motor.
5. Pruebas de regresión y release firmada.

## Fase 2 — DOCX texto (commit 9467999)
- v0.3.0: importador provisional de `word/document.xml` mediante ZIP y XmlPullParser de Android.
- La importación conserva texto, saltos de párrafo, tabulaciones, saltos explícitos. No interpreta tablas estructuralmente, cabeceras, notas, imágenes, estilos, ni maquetación; no se puede guardar como DOCX.
- El botón de guardar exporta **TXT**, incluso cuando se abrió DOCX. Se muestra advertencia de pérdida de formato al importar.
- Límites básicos: máximo 5000 entradas ZIP, máximo 8 MiB del XML principal descomprimido.
- Validación en curso: workflows del commit `9467999`. Confirmar APK y probar en Android antes de afirmar que funciona.
- Próximo paso: pruebas instrumentadas con muestras reales de documentos y análisis de motor Rust/JNI; no confundir el prototipo con round-trip DOCX.
