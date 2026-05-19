package com.carbonai.cbam.store;

import com.carbonai.cbam.model.EmissionFactor;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Loads activity-factor rows from all CSV files in emission_tables_csv.
 *
 * Design note:
 * Only Tables 1-6 provide values that can be turned into direct calculation
 * factors for emissions. Annex IX contains reference efficiencies, so those
 * rows are loaded for discovery and year-aware lookup but are flagged as not
 * directly calculable for embedded-emissions arithmetic.
 */
@Component
public class EmissionTableRepository {

    private static final BigDecimal ONE_THOUSAND = new BigDecimal("1000");

    private List<EmissionFactor> factors = List.of();

    @PostConstruct
    public void load() {
        Path directory = resolveEmissionTableDirectory();
        List<EmissionFactor> loaded = new ArrayList<>();
        loadTable1(directory.resolve("table_1_fuel_emission_factors.csv"), loaded);
        loadTable2(directory.resolve("table_2_biomass_emission_factors.csv"), loaded);
        loadTable3(directory.resolve("table_3_process_emissions_carbonates_method_A.csv"), loaded);
        loadTable4(directory.resolve("table_4_process_emissions_alkali_earth_oxides_method_B.csv"), loaded);
        loadTable5(directory.resolve("table_5_process_emissions_iron_steel.csv"), loaded);
        loadTable6(directory.resolve("table_6_global_warming_potentials.csv"), loaded);
        loadAnnexIxTable1(directory.resolve("annex_ix_table_1_reference_efficiency_electricity.csv"), loaded);
        loadAnnexIxTable2(directory.resolve("annex_ix_table_2_reference_efficiency_heat_visible_rows.csv"), loaded);
        factors = List.copyOf(loaded);
    }

    public Optional<EmissionFactor> findEmissionFactor(String activityType, String unit, Integer year) {
        String normalizedActivityType = normalize(activityType);
        String normalizedUnit = normalize(unit);

        return factors.stream()
                .filter(value -> normalize(value.getActivityType()).equals(normalizedActivityType))
                .filter(value -> normalize(value.getUnit()).equals(normalizedUnit))
                .filter(value -> isApplicableForYear(value, year))
                .findFirst();
    }

    public List<EmissionFactor> getAllFactors() {
        return factors;
    }

    private boolean isApplicableForYear(EmissionFactor factor, Integer year) {
        if (factor.getSource() == null || !factor.getSource().contains("year-range=")) {
            return true;
        }
        String marker = factor.getSource().substring(factor.getSource().indexOf("year-range=") + "year-range=".length());
        String[] parts = marker.split("\\|", -1)[0].split(":", -1);
        int fromYear = Integer.parseInt(parts[0]);
        int toYear = Integer.parseInt(parts[1]);
        int effectiveYear = year == null ? 9999 : year;
        return effectiveYear >= fromYear && effectiveYear <= toYear;
    }

    private void loadTable1(Path path, List<EmissionFactor> loaded) {
        forEachRow(path, columns -> {
            if (columns.length < 4) {
                return;
            }
            BigDecimal efTco2PerTj = parseDecimal(columns[1]);
            BigDecimal ncvTjPerGg = parseDecimal(columns[2]);
            if (efTco2PerTj == null || ncvTjPerGg == null) {
                return;
            }
            BigDecimal kgCo2ePerTon = efTco2PerTj.multiply(ncvTjPerGg);
            loaded.add(new EmissionFactor(
                    columns[0].trim(),
                    "t",
                    kgCo2ePerTon,
                    "kgCO2e/t",
                    columns.length > 3 ? columns[3].trim() : "",
                    "Table 1",
                    true
            ));
            loaded.add(new EmissionFactor(
                    columns[0].trim(),
                    "Gg",
                    kgCo2ePerTon.multiply(ONE_THOUSAND),
                    "kgCO2e/Gg",
                    columns.length > 3 ? columns[3].trim() : "",
                    "Table 1",
                    true
            ));
        });
    }

    private void loadTable2(Path path, List<EmissionFactor> loaded) {
        forEachRow(path, columns -> {
            if (columns.length < 4) {
                return;
            }
            BigDecimal efTco2PerTj = parseDecimal(columns[1]);
            BigDecimal ncvGjPerTon = parseDecimal(columns[2]);
            if (efTco2PerTj == null || ncvGjPerTon == null) {
                return;
            }
            BigDecimal kgCo2ePerTon = efTco2PerTj.multiply(ncvGjPerTon);
            loaded.add(new EmissionFactor(
                    columns[0].trim(),
                    "t",
                    kgCo2ePerTon,
                    "kgCO2e/t",
                    columns.length > 3 ? columns[3].trim() : "",
                    "Table 2",
                    true
            ));
        });
    }

    private void loadTable3(Path path, List<EmissionFactor> loaded) {
        forEachRow(path, columns -> addDirectTableFactor(columns, loaded, "Table 3"));
    }

    private void loadTable4(Path path, List<EmissionFactor> loaded) {
        forEachRow(path, columns -> addDirectTableFactor(columns, loaded, "Table 4"));
    }

    private void loadTable5(Path path, List<EmissionFactor> loaded) {
        forEachRow(path, columns -> addDirectTableFactor(columns, loaded, "Table 5"));
    }

    private void loadTable6(Path path, List<EmissionFactor> loaded) {
        forEachRow(path, columns -> addDirectTableFactor(columns, loaded, "Table 6"));
    }

    private void addDirectTableFactor(String[] columns, List<EmissionFactor> loaded, String tableName) {
        if (columns.length < 3) {
            return;
        }
        BigDecimal factorTco2ePerTon = parseDecimal(columns[1]);
        if (factorTco2ePerTon == null) {
            return;
        }
        loaded.add(new EmissionFactor(
                columns[0].trim(),
                "t",
                factorTco2ePerTon.multiply(ONE_THOUSAND),
                "kgCO2e/t",
                columns.length > 2 ? columns[2].trim() : "",
                tableName,
                true
        ));
    }

    private void loadAnnexIxTable1(Path path, List<EmissionFactor> loaded) {
        forEachRow(path, columns -> {
            if (columns.length < 6) {
                return;
            }
            loadReferenceEntry(loaded, columns[2].trim(), "reference_efficiency_electricity_percent",
                    parseDecimal(columns[3]), "Annex IX Table 1", "year-range=0:2011");
            loadReferenceEntry(loaded, columns[2].trim(), "reference_efficiency_electricity_percent",
                    parseDecimal(columns[4]), "Annex IX Table 1", "year-range=2012:2015");
            loadReferenceEntry(loaded, columns[2].trim(), "reference_efficiency_electricity_percent",
                    parseDecimal(columns[5]), "Annex IX Table 1", "year-range=2016:9999");
        });
    }

    private void loadAnnexIxTable2(Path path, List<EmissionFactor> loaded) {
        forEachRow(path, columns -> {
            if (columns.length < 9) {
                return;
            }
            loadReferenceEntry(loaded, columns[2].trim(), "reference_efficiency_heat_hot_water_percent",
                    parseDecimal(columns[3]), "Annex IX Table 2", "year-range=0:2015");
            loadReferenceEntry(loaded, columns[2].trim(), "reference_efficiency_heat_steam_percent",
                    parseDecimal(columns[4]), "Annex IX Table 2", "year-range=0:2015");
            loadReferenceEntry(loaded, columns[2].trim(), "reference_efficiency_heat_direct_use_exhaust_gases_percent",
                    parseDecimal(columns[5]), "Annex IX Table 2", "year-range=0:2015");
            loadReferenceEntry(loaded, columns[2].trim(), "reference_efficiency_heat_hot_water_percent",
                    parseDecimal(columns[6]), "Annex IX Table 2", "year-range=2016:9999");
            loadReferenceEntry(loaded, columns[2].trim(), "reference_efficiency_heat_steam_percent",
                    parseDecimal(columns[7]), "Annex IX Table 2", "year-range=2016:9999");
            loadReferenceEntry(loaded, columns[2].trim(), "reference_efficiency_heat_direct_use_exhaust_gases_percent",
                    parseDecimal(columns[8]), "Annex IX Table 2", "year-range=2016:9999");
        });
    }

    private void loadReferenceEntry(List<EmissionFactor> loaded,
                                    String activityType,
                                    String unit,
                                    BigDecimal value,
                                    String tableName,
                                    String yearRangeMarker) {
        if (value == null) {
            return;
        }
        loaded.add(new EmissionFactor(
                activityType,
                unit,
                value,
                "%",
                yearRangeMarker + "|reference value only",
                tableName,
                false
        ));
    }

    private Path resolveEmissionTableDirectory() {
        List<Path> candidates = List.of(
                Paths.get("emission_tables_csv"),
                Paths.get("..", "emission_tables_csv"),
                Paths.get(System.getProperty("user.dir"), "emission_tables_csv"),
                Paths.get(System.getProperty("user.dir"), "..", "emission_tables_csv")
        );

        return candidates.stream()
                .map(Path::toAbsolutePath)
                .filter(Files::isDirectory)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not locate emission_tables_csv directory"));
    }

    private void forEachRow(Path path, CsvRowConsumer consumer) {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] columns = line.split(";", -1);
                consumer.accept(columns);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load emission table file: " + path, exception);
        }
    }

    private BigDecimal parseDecimal(String value) {
        String normalized = value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
        if (normalized.isBlank() || "n.a.".equals(normalized)) {
            return null;
        }
        if (normalized.startsWith("emission factor")) {
            return null;
        }
        return new BigDecimal(normalized.replace(",", "."));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    @FunctionalInterface
    private interface CsvRowConsumer {
        void accept(String[] columns);
    }
}
