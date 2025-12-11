# Plan: Příprava a implementace Advent of Code 2025 den 8

Je třeba vytvořit strukturu souborů pro den 8 podle vzoru existujících dnů a implementovat řešení podle zadání z https://adventofcode.com/2025/day/8.

## Steps

1. **Vytvořit src/Day08.kt** - Vytvořit Kotlin soubor s kostrou obsahující:
   - `main()` funkci
   - `part1(input: List<String>): Int` funkci
   - `part2(input: List<String>): Long` funkci
   - Pomocné funkce pro parsing a logiku řešení
   - `check()` volání pro testovací data
   - Výpisy výsledků pro part1 a part2

2. **Vytvořit src/Day08_test.txt** - Vytvořit testovací soubor s příkladovými daty ze zadání z Advent of Code

3. **Vytvořit src/Day08.txt** - Vytvořit prázdný soubor pro skutečný input z Advent of Code (uživatel ho vyplní svými daty)

4. **Implementovat řešení podle zadání** - Po získání zadání z https://adventofcode.com/2025/day/8:
   - Analyzovat problém a identifikovat klíčové koncepty
   - Implementovat parsing vstupních dat
   - Implementovat logiku pro part1
   - Implementovat logiku pro part2
   - Využít `readInput("Day08")` pomocnou funkci pro načtení dat

5. **Přidat testy** - Doplnit `check()` volání s očekávanými výsledky z testovacích dat podle zadání

6. **Volitelně vytvořit run_day08.sh** - Vytvořit spouštěcí skript podle vzoru ostatních dnů (run_day01.sh, run_day03.sh, atd.)

## Struktura podle vzoru Day07.kt

Soubor Day08.kt by měl následovat strukturu:
- Data třídy pro reprezentaci objektů (pokud potřeba)
- `main()` funkce obsahující:
  - Pomocné funkce pro parsing a logiku
  - `part1()` funkce
  - `part2()` funkce
  - Načtení testovacích dat a validace pomocí `check()`
  - Načtení skutečných dat a výpis výsledků

## Poznámky

- **Zadání je známé**
---
Equipped with a new understanding of teleporter maintenance, you confidently step onto the repaired teleporter pad.

You rematerialize on an unfamiliar teleporter pad and find yourself in a vast underground space which contains a giant playground!

Across the playground, a group of Elves are working on setting up an ambitious Christmas decoration project. Through careful rigging, they have suspended a large number of small electrical junction boxes.

Their plan is to connect the junction boxes with long strings of lights. Most of the junction boxes don't provide electricity; however, when two junction boxes are connected by a string of lights, electricity can pass between those two junction boxes.

The Elves are trying to figure out which junction boxes to connect so that electricity can reach every junction box. They even have a list of all of the junction boxes' positions in 3D space (your puzzle input).

For example:

162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689
This list describes the position of 20 junction boxes, one per line. Each position is given as X,Y,Z coordinates. So, the first junction box in the list is at X=162, Y=817, Z=812.

To save on string lights, the Elves would like to focus on connecting pairs of junction boxes that are as close together as possible according to straight-line distance. In this example, the two junction boxes which are closest together are 162,817,812 and 425,690,689.

By connecting these two junction boxes together, because electricity can flow between them, they become part of the same circuit. After connecting them, there is a single circuit which contains two junction boxes, and the remaining 18 junction boxes remain in their own individual circuits.

Now, the two junction boxes which are closest together but aren't already directly connected are 162,817,812 and 431,825,988. After connecting them, since 162,817,812 is already connected to another junction box, there is now a single circuit which contains three junction boxes and an additional 17 circuits which contain one junction box each.

The next two junction boxes to connect are 906,360,560 and 805,96,715. After connecting them, there is a circuit containing 3 junction boxes, a circuit containing 2 junction boxes, and 15 circuits which contain one junction box each.

The next two junction boxes are 431,825,988 and 425,690,689. Because these two junction boxes were already in the same circuit, nothing happens!

This process continues for a while, and the Elves are concerned that they don't have enough extension cables for all these circuits. They would like to know how big the circuits will be.

After making the ten shortest connections, there are 11 circuits: one circuit which contains 5 junction boxes, one circuit which contains 4 junction boxes, two circuits which contain 2 junction boxes each, and seven circuits which each contain a single junction box. Multiplying together the sizes of the three largest circuits (5, 4, and one of the circuits of size 2) produces 40.

Your list contains many junction boxes; connect together the 1000 pairs of junction boxes which are closest together. Afterward, what do you get if you multiply together the sizes of the three largest circuits?
---

- **Očekávané výsledky testů** - Budou potřeba po přečtení zadání pro správné nastavení `check()` volání
- **Formát vstupních dat** - Bude určen zadáním (textová mřížka, čísla, jiný formát)
- **Spouštěcí skript** - Pokud bude požadován, vytvořit run_day08.sh s obsahem podobným ostatním dnům

## Aktuální stav

- [x] Vytvořit Day08.kt
- [x] Vytvořit Day08_test.txt
- [x] Vytvořit Day08.txt (uživatel doplnil vlastní data)
- [x] Získat zadání z Advent of Code
- [x] Implementovat part1
- [ ] Implementovat part2 (čeká na zveřejnění Part 2)
- [x] Otestovat řešení (Test Part 1: 40 ✓, Part 1: 75680)
- [x] Vytvořit run_day08.sh
- [x] Vytvořit README_Day08.md
- [x] Vytvořit test_day08.sh (alternativní spouštěcí skript)

## Výsledky

- **Test Part 1**: 40 (očekáváno 40) ✓
- **Part 1**: 75680
- **Part 2**: Zatím není k dispozici

## Implementované řešení

Řešení využívá:
1. **Union-Find (Disjoint Set Union)** algoritmus pro efektivní sledování spojených komponent (obvodů)
   - Path compression optimalizace v `find()`
   - Union by rank optimalizace v `union()`
2. **3D Euklidovská vzdálenost** pro výpočet vzdálenosti mezi junction boxy
3. **Greedy přístup** - spojování nejbližších párů podle vzdálenosti
4. **Násobení tří největších obvodů** jako finální výsledek

## Vytvořené soubory

1. **src/Day08.kt** - Hlavní implementace (110 řádků)
2. **src/Day08_test.txt** - Testovací data (20 junction boxů)
3. **src/Day08.txt** - Vstupní data (1000 junction boxů)
4. **run_day08.sh** - Spouštěcí skript přes Gradle
5. **test_day08.sh** - Alternativní spouštěcí skript přes kotlinc
6. **README_Day08.md** - Dokumentace řešení
7. **plan-day08.prompt.md** - Tento plán

## Technické detaily

- **Časová složitost**: O(n² log n) kde n je počet junction boxů
- **Paměťová složitost**: O(n²) pro uložení všech vzdáleností
- **Optimalizace**: Union-Find s path compression a union by rank zajišťuje téměř konstantní čas pro union/find operace

## Shrnutí

✅ **Úspěšně dokončeno:**
- Kompletní implementace Part 1
- Všechny testy prošly
- Dokumentace vytvořena
- Spouštěcí skripty připraveny

⏳ **Čeká na dokončení:**
- Part 2 (bude zveřejněna po vyřešení Part 1 na Advent of Code)

