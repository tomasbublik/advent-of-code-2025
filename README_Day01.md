# Advent of Code 2025 - Den 1: Secret Entrance

## Zadání

Máme ciferník s čísly 0-99, který začíná na pozici **50**.

Vstupní data obsahují rotace ve formátu:
- `L<číslo>` - otočení doleva (odečítání)
- `R<číslo>` - otočení doprava (přičítání)

Ciferník je kruhový: po 99 následuje 0, před 0 je 99.

### Part 1
**Úkol:** Spočítat, kolikrát ciferník ukazuje na 0 po jakékoli rotaci v sekvenci.

### Příklad
Pro vstup:
```
L68
L30
R48
L5
R60
L55
L1
L99
R14
L82
```

Ciferník se pohybuje:
- Start: **50**
- L68 → 82
- L30 → 52
- R48 → **0** ✓ (count = 1)
- L5 → 95
- R60 → 55
- L55 → **0** ✓ (count = 2)
- L1 → 99
- L99 → **0** ✓ (count = 3)
- R14 → 14
- L82 → 32

**Výsledek: 3**

## Soubory

- **Day01_test.txt** - testovací vstupní data z příkladu (již vyplněno)
- **Day01.txt** - vaše osobní vstupní data (musíte zkopírovat z webu)
- **Day01.kt** - implementace řešení v Kotlinu

## Jak získat vstupní data

1. Přihlaste se na https://adventofcode.com/2025
2. Otevřete https://adventofcode.com/2025/day/1/input
3. Zkopírujte všechna data
4. Vložte je do souboru `src/Day01.txt` (nahraďte placeholder text)

## Jak spustit

### V IntelliJ IDEA
1. Otevřete soubor `src/Day01.kt`
2. Klikněte na zelenou šipku vedle funkce `main()`
3. Program nejprve zkontroluje test (měl by projít s výsledkem 3)
4. Pak vypíše výsledky pro vaše vstupní data

### Pomocí příkazové řádky
```bash
./gradlew build
kotlin -classpath build/classes/kotlin/main Day01Kt
```

## Implementace

Řešení simuluje otáčení ciferníku:
```kotlin
fun part1(input: List<String>): Int {
    var position = 50
    var count = 0

    for (line in input) {
        val direction = line[0]
        val distance = line.substring(1).toInt()

        position = when (direction) {
            'L' -> (position - distance).mod(100)
            'R' -> (position + distance).mod(100)
            else -> position
        }

        if (position == 0) {
            count++
        }
    }

    return count
}
```

## Stav

✅ Part 1 - Implementováno a otestováno
⏳ Part 2 - Odemkne se po vyřešení Part 1 na webu AoC

