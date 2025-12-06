# Advent of Code 2025 - Den 3: Depth Sweep

## Zadání

Máme seznam celočíselných měření (jedno číslo na řádek).

### Part 1
Úkolem je spočítat, kolikrát je měření přísně větší než předchozí měření.

### Part 2
Místo jednotlivých měření porovnávejte součty posuvného okna o velikosti 3.
Spočítejte, kolikrát je součet aktuálního okna větší než součet předchozího okna.

### Příklad
Pro vstup:
```
199
200
208
210
200
207
240
269
260
263
```

Výpočty:
- Part 1: Zvýšení nastane 7×
- Part 2: Zvýšení součtu okna nastane 5×

**Výsledek:** Part 1 = 7, Part 2 = 5

## Soubory

- **Day03_test.txt** - testovací vstupní data z příkladu (již vyplněno)
- **Day03.txt** - vaše osobní vstupní data (musíte zkopírovat z webu)
- **Day03.kt** - implementace řešení v Kotlinu (obsahuje testy nad `Day03_test.txt`)

## Jak získat vstupní data

1. Přihlaste se na https://adventofcode.com/2025
2. Otevřete https://adventofcode.com/2025/day/3/input
3. Zkopírujte všechna data
4. Vložte je do souboru `src/Day03.txt` (nahraďte placeholder text)

## Jak spustit

### V IntelliJ IDEA
1. Otevřete soubor `src/Day03.kt`
2. Klikněte na zelenou šipku vedle funkce `main()`
3. Program nejprve zkontroluje test (měl by projít s výsledky 7 a 5)
4. Pak vypíše výsledky pro vaše vstupní data

### Pomocí příkazové řádky
```bash
./gradlew build
kotlin -classpath build/classes/kotlin/main Day03Kt
```

## Implementace

Základ řešení (počítání zvýšení):
```kotlin
fun countIncreases(numbers: List<Int>): Int = numbers.zipWithNext().count { (a, b) -> b > a }

fun countWindowIncreases(numbers: List<Int>, window: Int = 3): Int {
    if (numbers.size < window + 1) return 0
    var increases = 0
    var prev = numbers.subList(0, window).sum()
    for (i in 1..numbers.size - window) {
        val curr = prev - numbers[i - 1] + numbers[i + window - 1]
        if (curr > prev) increases++
        prev = curr
    }
    return increases
}
```

## Stav

✅ Part 1 - Implementováno a otestováno
✅ Part 2 - Implementováno a otestováno
