# Advent of Code 2025 - Den 8: Resonant Collinearity

## Zadání

V podzemním prostoru s obřím hřištěm pracují elfové na vánočním dekoračním projektu. Mají zavěšeno velké množství malých elektrických rozvodných krabic (junction boxes) ve 3D prostoru.

Plánují spojit junction boxy dlouhými světelnými řetězy. Elektřina může proudit mezi dvěma junction boxy, které jsou spojené řetězem světel.

### Part 1

**Úkol:** Spojit 1000 párů junction boxů, které jsou k sobě nejblíže (podle euklidovské vzdálenosti ve 3D). Poté vynásobit velikosti tří největších obvodů (circuits).

**Vstupní formát:**
```
X,Y,Z
X,Y,Z
...
```
Každý řádek obsahuje 3D souřadnice jednoho junction boxu.

**Algoritmus:**
1. Vypočítat vzdálenosti mezi všemi páry junction boxů
2. Seřadit páry podle vzdálenosti (od nejmenší)
3. Postupně spojovat 1000 nejbližších párů pomocí Union-Find algoritmu
4. Spočítat velikosti všech vzniklých obvodů
5. Vynásobit velikosti tří největších obvodů

### Příklad

Pro testovací vstup s 20 junction boxy:
```
162,817,812
57,618,57
906,360,560
...
```

Po spojení 10 nejbližších párů vznikne:
- 1 obvod s 5 junction boxy
- 1 obvod se 4 junction boxy  
- 2 obvody se 2 junction boxy
- 7 obvodů s 1 junction boxem

Výsledek: `5 × 4 × 2 = 40`

### Part 2

Zatím není k dispozici (odemkne se po vyřešení Part 1).

## Řešení

### Implementace

Řešení využívá:

1. **Union-Find (Disjoint Set Union)** - efektivní datová struktura pro sledování spojených komponent:
   - `find(x)` - najde kořen komponenty s path compression
   - `union(x, y)` - spojí dvě komponenty s union by rank
   - `getCircuitSizes()` - vrátí velikosti všech obvodů

2. **3D Euklidovská vzdálenost:**
   ```kotlin
   sqrt((x1-x2)² + (y1-y2)² + (z1-z2)²)
   ```

3. **Greedy přístup** - spojování nejbližších párů zajišťuje minimální spotřebu světelných řetězů

4. **Časová složitost:** 
   - Výpočet všech vzdáleností: O(n²)
   - Řazení: O(n² log n)
   - Union-Find operace: téměř O(1) s path compression a union by rank
   - Celková: O(n² log n)

### Výsledky

**Test data (20 junction boxů, 10 spojení):**
- Očekávaný výsledek: 40
- Skutečný výsledek: ✓ 40

**Reálná data (1000 spojení):**
- Part 1: **75680**
- Part 2: Zatím není k dispozici

## Spuštění

```bash
./run_day08.sh
```

Nebo přímo:
```bash
./gradlew run -PmainClass=Day08Kt --quiet
```

## Soubory

- `src/Day08.kt` - implementace řešení
- `src/Day08_test.txt` - testovací data
- `src/Day08.txt` - vstupní data
- `run_day08.sh` - spouštěcí skript

