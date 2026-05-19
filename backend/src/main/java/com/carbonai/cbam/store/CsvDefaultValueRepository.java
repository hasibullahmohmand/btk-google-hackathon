package com.carbonai.cbam.store;

import com.carbonai.cbam.model.CbamDefaultValue;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Loads CBAM default values from the real CSV files stored in the repository.
 */
@Component
public class CsvDefaultValueRepository {

    private static final Pattern CSV_SPLIT_PATTERN = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

    private List<CbamDefaultValue> transitionalValues = List.of();
    private List<CountryDefaultRecord> countryDefaults = List.of();
    private List<BenchmarkRecord> benchmarkDefaults = List.of();

    @PostConstruct
    public void load() {
        Path csvDirectory = resolveCsvDirectory();
        transitionalValues = loadTransitionalValues(csvDirectory.resolve("transitional_default_values_2023_2025.csv"));
        countryDefaults = loadCountryDefaults(csvDirectory.resolve("country_default_values_2026_plus.csv"));
        benchmarkDefaults = loadBenchmarkDefaults(csvDirectory.resolve("cbam_benchmarks_2026_plus.csv"));
    }

    public Optional<CbamDefaultValue> findDefaultValue(String country, String cnCode, Integer year) {
        if (year != null && year >= 2023 && year <= 2025) {
            return transitionalValues.stream()
                    .filter(value -> normalizeCnCode(value.getCnCode()).equals(normalizeCnCode(cnCode)))
                    .findFirst()
                    .map(value -> copyWithCountry(value, country));
        }

        int effectiveYear = normalizeFutureYear(year);
        Optional<CbamDefaultValue> countryValue = countryDefaults.stream()
                .filter(record -> record.year() == effectiveYear)
                .filter(record -> normalizeCnCode(record.cnCode()).equals(normalizeCnCode(cnCode)))
                .filter(record -> normalizeCountry(record.country()).equals(normalizeCountry(country)))
                .filter(CountryDefaultRecord::hasCountryValue)
                .filter(record -> record.adoptedDefaultTotalTco2ePerTon() != null)
                .findFirst()
                .map(this::toCbamDefaultValue);

        if (countryValue.isPresent()) {
            return countryValue;
        }

        return benchmarkDefaults.stream()
                .filter(record -> normalizeCnCode(record.cnCode()).equals(normalizeCnCode(cnCode)))
                .max(Comparator.comparing(BenchmarkRecord::bmTco2ePerTon))
                .map(record -> toBenchmarkDefaultValue(country, record));
    }

    public List<CbamDefaultValue> getAllDefaultValues() {
        List<CbamDefaultValue> allValues = new ArrayList<>(transitionalValues);
        countryDefaults.stream()
                .filter(CountryDefaultRecord::hasCountryValue)
                .map(this::toCbamDefaultValue)
                .forEach(allValues::add);
        return allValues;
    }

    private Path resolveCsvDirectory() {
        List<Path> candidates = List.of(
                Paths.get("csv"),
                Paths.get("..", "csv"),
                Paths.get(System.getProperty("user.dir"), "csv"),
                Paths.get(System.getProperty("user.dir"), "..", "csv")
        );

        return candidates.stream()
                .map(Path::toAbsolutePath)
                .filter(Files::isDirectory)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not locate repository csv directory"));
    }

    private List<CbamDefaultValue> loadTransitionalValues(Path path) {
        List<CbamDefaultValue> values = new ArrayList<>();
        forEachCsvRow(path, columns -> values.add(new CbamDefaultValue(
                "DEFAULT",
                columns[0],
                columns[1],
                columns[2],
                parseDecimal(columns[3]),
                parseDecimal(columns[4]),
                parseDecimal(columns[5]),
                parseDecimal(columns[5]),
                parseDecimal(columns[5]),
                parseDecimal(columns[5])
        )));
        return List.copyOf(values);
    }

    private List<CountryDefaultRecord> loadCountryDefaults(Path path) {
        List<CountryDefaultRecord> values = new ArrayList<>();
        forEachCsvRow(path, columns -> values.add(new CountryDefaultRecord(
                columns[0],
                columns[1],
                columns[2],
                columns[3],
                Integer.parseInt(columns[4]),
                parseDecimal(columns[5]),
                parseDecimal(columns[6]),
                parseDecimal(columns[7]),
                parseDecimal(columns[8]),
                columns[9],
                Boolean.parseBoolean(columns[10])
        )));
        return List.copyOf(values);
    }

    private List<BenchmarkRecord> loadBenchmarkDefaults(Path path) {
        List<BenchmarkRecord> values = new ArrayList<>();
        forEachCsvRow(path, columns -> values.add(new BenchmarkRecord(
                columns[0],
                columns[1],
                columns[2],
                columns[3],
                columns[4],
                parseDecimal(columns[5])
        )));
        return List.copyOf(values);
    }

    private void forEachCsvRow(Path path, CsvRowConsumer consumer) {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] columns = CSV_SPLIT_PATTERN.split(line, -1);
                for (int index = 0; index < columns.length; index++) {
                    columns[index] = unquote(columns[index]);
                }
                consumer.accept(columns);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load CSV file: " + path, exception);
        }
    }

    private CbamDefaultValue toCbamDefaultValue(CountryDefaultRecord record) {
        BigDecimal adoptedValue = record.adoptedDefaultTotalTco2ePerTon();
        return new CbamDefaultValue(
                record.country(),
                record.cnCode(),
                record.description(),
                record.productGroup(),
                record.defaultDirectTco2ePerTon(),
                record.defaultIndirectTco2ePerTon(),
                record.defaultTotalTco2ePerTon(),
                adoptedValue,
                adoptedValue,
                adoptedValue
        );
    }

    private CbamDefaultValue toBenchmarkDefaultValue(String country, BenchmarkRecord record) {
        BigDecimal benchmarkValue = record.bmTco2ePerTon();
        return new CbamDefaultValue(
                country,
                record.cnCode(),
                record.description(),
                record.productGroup(),
                null,
                null,
                benchmarkValue,
                benchmarkValue,
                benchmarkValue,
                benchmarkValue
        );
    }

    private CbamDefaultValue copyWithCountry(CbamDefaultValue value, String country) {
        return new CbamDefaultValue(
                country,
                value.getCnCode(),
                value.getProductDescription(),
                value.getSector(),
                value.getDirectDefaultTco2ePerTon(),
                value.getIndirectDefaultTco2ePerTon(),
                value.getTotalDefaultTco2ePerTon(),
                value.getDefault2026WithMarkup(),
                value.getDefault2027WithMarkup(),
                value.getDefault2028OnwardsWithMarkup()
        );
    }

    private int normalizeFutureYear(Integer year) {
        if (year == null) {
            return 2026;
        }
        if (year <= 2026) {
            return 2026;
        }
        if (year == 2027) {
            return 2027;
        }
        return 2028;
    }

    private BigDecimal parseDecimal(String value) {
        return value == null || value.isBlank() ? null : new BigDecimal(value.trim());
    }

    private String unquote(String value) {
        String trimmed = value == null ? "" : value.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"") && trimmed.length() >= 2) {
            return trimmed.substring(1, trimmed.length() - 1).replace("\"\"", "\"");
        }
        return trimmed;
    }

    private String normalizeCnCode(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeCountry(String value) {
        String normalized = normalizeText(value);
        if ("turkey".equals(normalized) || "turkiye".equals(normalized)) {
            return "turkiye";
        }
        return normalized;
    }

    private String normalizeText(String value) {
        if (value == null) {
            return "";
        }
        String normalized = Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalized.toLowerCase(Locale.ROOT);
    }

    @FunctionalInterface
    private interface CsvRowConsumer {
        void accept(String[] columns);
    }

    private record CountryDefaultRecord(
            String country,
            String cnCode,
            String description,
            String productGroup,
            int year,
            BigDecimal defaultDirectTco2ePerTon,
            BigDecimal defaultIndirectTco2ePerTon,
            BigDecimal defaultTotalTco2ePerTon,
            BigDecimal adoptedDefaultTotalTco2ePerTon,
            String underlyingCbamBenchmarkRoute,
            boolean hasCountryValue
    ) {
    }

    private record BenchmarkRecord(
            String cnCode,
            String description,
            String productGroup,
            String benchmarkColumn,
            String productionRoute,
            BigDecimal bmTco2ePerTon
    ) {
    }
}
