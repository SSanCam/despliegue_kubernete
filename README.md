
# Nombre: LolNexuxApi

## Descripción

Esta API gestiona un sistema para el seguimiento de partidas de un juego competitivo,
enfocándose en los usuarios, los campeones utilizados y los resultados de las partidas.
Permite a los desarrolladores integrar funcionalidades relacionadas con el manejo de datos de usuarios, campeones y estadísticas de partidas.

---

## Justificación

La idea de hacer esta API surge porque en muchos juegos competitivos hay un montón de información interesante que los jugadores pueden querer analizar:
desde con qué campeones juegan mejor, hasta cómo han mejorado con el tiempo o qué tan bien les va contra ciertos tipos de campeones.

---

## Estructura de las entidades

Se trabajará con las siguientes tres entidades:

1. **Usuario**:
    - `id` (Long): Identificador único del usuario.
    - `username` (String): Nombre del usuario (debe ser único).
    - `password` (String): Contraseña del usuario (debe estar hasheada).
    - `rol` (String): Rol del usuario, que puede ser `USER` o `ADMIN`.


2. **Campeon**:
    - `id` (Long): Identificador único del campeón.
    - `nombre` (String): Nombre del campeón.
    - `tipo` (String): Tipo de campeón (asesino, mago, bruiser...).

3. **Partida**:
    - `id` (Long): Identificador único de la partida.
    - `fecha` (DateTime): Fecha y hora en que se jugó la partida.
    - `resultado` (String): Resultado de la partida (victoria, derrota).
    - `usuario_id` (Long): Identificador del usuario que participó en la partida (relación con la entidad **Usuario**).
    - `campeon_id` (Long): Identificador del campeón usado en la partida (relación con la entidad **Campeón**).
    - `duracion` (Integer): Duración de la partida en minutos.

---

## Endpoints

