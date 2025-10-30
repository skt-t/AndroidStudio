# AndroidStudio
## ¡¡ATENCION!! - EL ARCHIVO REQUERIDO ES "WordGuesser2", POR FAVOR IGNORAR OTROS ARCHIVOS A MENOS QUE QUIERA PROBAR UNA VERSION TEMPRANA DEL JUEGO
# Word Guesser - Proyect

Este es un juego para Android, desarrollado en Kotlin y Jetpack Compose, como proyecto para el curso de desarrollo de aplicaciones moviles. El juego consiste en adivinar una palabra secreta basándose en una imagen y una pista de texto, utilizando reconocimiento de voz para recibir la respuesta.

## Integrantes

* Allan Nuñez
* Cory Leveke

---

## Funcionalidades Implementadas

El proyecto cuenta con las siguientes características:

Logica del juego: El juego consiste en completar una palabra "misteriosa" que aparece en pantalla, se cuenta con una imagen y la palabra semi completa como guia para resolver.
el usuario cuenta con un boton que dice "HABLAR" para poder hacer su "guess" si acierta sale un mensaje de logro, y si no, de derrota (aunque no se puede perder).
si el usuario lo desea puede pasar a la siguiente palabra para adivinar con el boton "SIGUIENTE PALABRA".

Interfaz de Usuario: El usuario cuenta con una interfaz muy simple, un menu con musica de fondo para ambientar y un boton que dice "JUGAR", cuando el usuario juega se puede ver una transisión al juego principal
para volver al menu simplemente se usa el boton de devolver del telefono movil.

Funcionalidades usadas del telefono:
Reconocimiento de Voz (Android SDK):
   - Implementación del `RecognizerIntent` de Android para capturar la voz del usuario.
   - Manejo de permisos en tiempo de ejecución para el `Manifest.permission.RECORD_AUDIO`.
   - Procesamiento del resultado de voz para compararlo con la palabra secreta.

Multimedia (MediaPlayer):
   - Música de fondo en la pantalla de menú que se gestiona automáticamente con el ciclo de vida de la app (`LifecycleEventObserver`).
   - La música se pausa al minimizar la app y se detiene y libera (`release`) al navegar a la pantalla de juego para optimizar recursos.

---

## Pasos para Ejecutar

Para poder jugar el juego necesitamos tener android studio.

1. primero hacemos un clon de este repositorio para traer los archivos necesarios del juego, los archivos requeridos son WordGuesser2
2. en android studio abrimos conectamos nuestro celular en modo desarrollador
3. ejecutamos la app
4. ¡¡YA PUEDES JUGAR!!
