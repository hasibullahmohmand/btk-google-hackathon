# CarbonAI TR - CBAM Calculator Engine

This module is a Spring Boot backend for deterministic CBAM calculations. It is designed for CarbonAI TR, where an AI or RAG layer may explain regulations and call backend tools, but the calculations themselves must stay deterministic, transparent, and auditable.

The backend does not use AI for any calculation.

## What CBAM certificates are

CBAM certificates are units that EU importers or authorized CBAM declarants may need to buy and surrender to cover the embedded emissions of imported goods.

In simple words:

- A product has emissions attached to it.
- Those emissions can create a carbon-related obligation.
- CBAM certificates are the mechanism used to reflect that obligation.

This backend does not connect to the official EU registry and does not submit anything. It only performs calculations and validation checks.

## Why this is treated as financial exposure

Even though CBAM is not just a normal tax label, businesses often experience it as a cost exposure because:

- Higher embedded emissions can mean more certificates.
- More certificates can mean more EUR cost.
- Better actual emissions data can reduce estimated exposure compared with default values.

That is why this backend includes both emissions calculators and cost estimators.

## What this backend does

It exposes REST APIs for:

- Default-value emissions calculations
- Actual factory activity emissions calculations
- Simple carbon cost estimation
- Advanced certificate estimation
- Default-vs-actual cost comparison
- Carbon price scenario analysis
- CBAM-style report validation
- Demo data discovery

## Key CBAM concepts used in the code

- `tCO2e`: tonnes of CO2 equivalent
- `kgCO2e`: kilograms of CO2 equivalent
- `1 tCO2e = 1000 kgCO2e`
- Specific emissions: emissions per ton of product
- Embedded emissions: emissions associated with the traded goods
- Certificate price: EUR per tCO2e
- Export or import quantity: product amount in tons

## Calculation modes

### Default value mode

Use this when the exporter does not have actual factory emissions data.

The backend:

1. Finds a seeded default value by country and CN code.
2. Chooses the correct year-specific value.
3. Multiplies export volume by that default value.

Formula:

`embeddedEmissions = exportVolumeTons × defaultValueTco2ePerTon`

### Actual data mode

Use this when the exporter has actual factory activity data.

The backend:

1. Looks up an emission factor for each activity.
2. Converts activity amounts into kgCO2e.
3. Converts kgCO2e into tCO2e.
4. Sums direct and indirect emissions.
5. Calculates specific emissions per ton.
6. Applies that intensity to the exported quantity.

Main formulas:

- `activityEmissionsKg = amount × factorKgCo2ePerUnit`
- `activityEmissionsTco2e = activityEmissionsKg / 1000`
- `specificEmissions = totalFacilityEmissions / productionVolumeTons`
- `exportedEmbeddedEmissions = specificEmissions × exportVolumeTons`

### Simple cost mode

Use this when embedded emissions are already known and only a quick EUR estimate is needed.

Formula:

`estimatedCostEur = embeddedEmissionsTco2e × certificatePriceEurPerTco2e`

### Advanced certificate formula

Use this when more detailed inputs are available.

Definitions:

- `A = actual specific emissions`
- `B = free allowance deduction = cbamBenchmark × cbamFactor`
- `C = third-country carbon price deduction`
- `D = imported quantity`

Formula:

`certificates = max(0, (A - B - C) × D)`

Then:

`estimatedCostEur = certificates × cbamCertificatePriceEurPerTco2e`

## Seeded demo data

### Default values

- Country: `Turkey`
- CN Code: `25233000`
- Product: `Aluminous cement`
- Sector: `Cement`
- Direct default: `1.820 tCO2e/t`
- Indirect default: `0.140 tCO2e/t`
- Total default: `1.950 tCO2e/t`
- 2026 default with markup: `2.145 tCO2e/t`
- 2027 default with markup: `2.340 tCO2e/t`
- 2028 onwards default with markup: `2.535 tCO2e/t`

### Emission factors

- `NATURAL_GAS`: unit `m3`, factor `2.0 kgCO2e/m3`
- `DIESEL`: unit `liter`, factor `2.68 kgCO2e/liter`
- `ELECTRICITY`: unit `kWh`, factor `0.42 kgCO2e/kWh`

### Demo carbon prices

- `76`
- `100`
- `120`

## Demo scenario

A Turkish cement exporter ships `100` tons of aluminous cement to Germany in `2026` and does not have actual factory emissions data.

The backend:

1. Finds the seeded Turkey + `25233000` record.
2. Chooses the `2026` default value with markup: `2.145 tCO2e/t`.
3. Calculates embedded emissions:

`100 × 2.145 = 214.5 tCO2e`

4. If the price is `76 EUR/tCO2e`, it estimates financial exposure:

`214.5 × 76 = 16,302 EUR`

This is why the system treats CBAM as a financial exposure tool as well as an emissions calculator.

## Endpoints

- `POST /api/cbam/default-emissions`
- `POST /api/cbam/actual-emissions`
- `POST /api/cbam/simple-cost`
- `POST /api/cbam/advanced-certificates`
- `POST /api/cbam/compare-default-vs-actual`
- `POST /api/cbam/scenarios`
- `POST /api/cbam/validate-report`
- `GET /api/cbam/demo-data`

Swagger UI:

- `http://localhost:8080/swagger-ui.html`

OpenAPI JSON:

- `http://localhost:8080/v3/api-docs`

## Endpoint examples

### 1. Default emissions

```bash
curl -X POST http://localhost:8080/api/cbam/default-emissions \
  -H "Content-Type: application/json" \
  -d '{
    "country": "Turkey",
    "cnCode": "25233000",
    "year": 2026,
    "exportVolumeTons": 100
  }'
```

Response:

```json
{
  "country": "Turkey",
  "cnCode": "25233000",
  "productDescription": "Aluminous cement",
  "year": 2026,
  "exportVolumeTons": 100,
  "selectedDefaultValueTco2ePerTon": 2.1450,
  "embeddedEmissionsTco2e": 214.5000,
  "calculationMode": "DEFAULT_VALUE",
  "formula": "embeddedEmissions = exportVolumeTons × defaultValueTco2ePerTon"
}
```

### 2. Actual emissions

```bash
curl -X POST http://localhost:8080/api/cbam/actual-emissions \
  -H "Content-Type: application/json" \
  -d '{
    "product": "steel",
    "productionVolumeTons": 100,
    "exportVolumeTons": 40,
    "includeIndirectEmissions": true,
    "activities": [
      { "activityType": "NATURAL_GAS", "amount": 5000, "unit": "m3" },
      { "activityType": "DIESEL", "amount": 1200, "unit": "liter" },
      { "activityType": "ELECTRICITY", "amount": 25000, "unit": "kWh" }
    ]
  }'
```

Response:

```json
{
  "product": "steel",
  "productionVolumeTons": 100,
  "exportVolumeTons": 40,
  "directEmissionsTco2e": 13.2160,
  "indirectEmissionsTco2e": 10.5000,
  "totalFacilityEmissionsTco2e": 23.7160,
  "specificEmissionsTco2ePerTon": 0.2372,
  "exportedEmbeddedEmissionsTco2e": 9.4864,
  "includeIndirectEmissions": true,
  "calculationMode": "ACTUAL_DATA",
  "activityBreakdown": [
    {
      "activityType": "NATURAL_GAS",
      "amount": 5000,
      "unit": "m3",
      "factor": 2.0,
      "factorUnit": "kgCO2e/m3",
      "emissionsTco2e": 10.0000,
      "emissionCategory": "DIRECT"
    }
  ],
  "warnings": []
}
```

### 3. Simple cost

```bash
curl -X POST http://localhost:8080/api/cbam/simple-cost \
  -H "Content-Type: application/json" \
  -d '{
    "embeddedEmissionsTco2e": 214.5,
    "certificatePriceEurPerTco2e": 76
  }'
```

Response:

```json
{
  "embeddedEmissionsTco2e": 214.5000,
  "certificatePriceEurPerTco2e": 76,
  "estimatedCostEur": 16302.00,
  "formula": "estimatedCostEur = embeddedEmissionsTco2e × certificatePriceEurPerTco2e"
}
```

### 4. Advanced certificates

```bash
curl -X POST http://localhost:8080/api/cbam/advanced-certificates \
  -H "Content-Type: application/json" \
  -d '{
    "actualSpecificEmissionsTco2ePerTon": 2.145,
    "cbamBenchmarkTco2ePerTon": 1.5,
    "cbamFactor": 0.975,
    "thirdCountryCarbonPriceEurPerTco2e": 0,
    "cbamCertificatePriceEurPerTco2e": 76,
    "importedQuantityTons": 100
  }'
```

Response:

```json
{
  "actualSpecificEmissionsTco2ePerTon": 2.1450,
  "freeAllowanceDeductionTco2ePerTon": 1.4625,
  "thirdCountryCarbonPriceDeductionTco2ePerTon": 0.0000,
  "importedQuantityTons": 100,
  "certificatesToSurrender": 68.2500,
  "cbamCertificatePriceEurPerTco2e": 76,
  "estimatedCostEur": 5187.00,
  "formula": "certificates = max(0, (A - B - C) × D)"
}
```

### 5. Compare default vs actual

```bash
curl -X POST http://localhost:8080/api/cbam/compare-default-vs-actual \
  -H "Content-Type: application/json" \
  -d '{
    "defaultSpecificEmissionsTco2ePerTon": 2.145,
    "actualSpecificEmissionsTco2ePerTon": 1.6,
    "exportVolumeTons": 100,
    "certificatePriceEurPerTco2e": 76
  }'
```

Response:

```json
{
  "defaultCostEur": 16302.00,
  "actualCostEur": 12160.00,
  "potentialSavingsEur": 4142.00,
  "savingsPercent": 25.41,
  "message": "Using actual emissions data instead of default values may reduce estimated CBAM exposure."
}
```

### 6. Scenario analysis

```bash
curl -X POST http://localhost:8080/api/cbam/scenarios \
  -H "Content-Type: application/json" \
  -d '{
    "embeddedEmissionsTco2e": 214.5,
    "pricesEurPerTco2e": [76, 100, 120]
  }'
```

Response:

```json
{
  "embeddedEmissionsTco2e": 214.5000,
  "scenarios": [
    {
      "priceEurPerTco2e": 76,
      "estimatedCostEur": 16302.00
    },
    {
      "priceEurPerTco2e": 100,
      "estimatedCostEur": 21450.00
    },
    {
      "priceEurPerTco2e": 120,
      "estimatedCostEur": 25740.00
    }
  ]
}
```

### 7. Validate report

```bash
curl -X POST http://localhost:8080/api/cbam/validate-report \
  -H "Content-Type: application/json" \
  -d '{
    "goodsItemNumber": "1",
    "sequenceNumber": "1",
    "cnCode": "25233000",
    "country": "Turkey",
    "period": "2026",
    "directEmissionsTco2e": 1.82,
    "indirectEmissionsTco2e": 0.14,
    "totalEmissionsTco2e": 1.96,
    "netMassTons": 100
  }'
```

Valid response:

```json
{
  "valid": true,
  "errors": [],
  "warnings": []
}
```

Invalid response example:

```json
{
  "valid": false,
  "errors": [
    {
      "code": "R0010",
      "message": "Total emissions must equal direct emissions plus indirect emissions.",
      "expectedValue": 1.9600,
      "actualValue": 1.9500
    }
  ],
  "warnings": []
}
```

### 8. Demo data

```bash
curl http://localhost:8080/api/cbam/demo-data
```

Response:

```json
{
  "defaultValues": [
    {
      "country": "Turkey",
      "cnCode": "25233000",
      "productDescription": "Aluminous cement"
    }
  ],
  "emissionFactors": [
    {
      "activityType": "NATURAL_GAS",
      "unit": "m3",
      "factorKgCo2ePerUnit": 2.0
    }
  ],
  "demoCarbonPrices": [76, 100, 120],
  "demoProducts": ["Aluminous cement", "Steel", "Aluminium", "Fertiliser", "Hydrogen"]
}
```

## Example validation error shape

```json
{
  "error": "DEFAULT_VALUE_NOT_FOUND",
  "message": "No CBAM default value found for country=Turkey and cnCode=99999999",
  "details": [],
  "timestamp": "2026-05-17T04:00:00+03:00"
}
```

## Build and run

From the `backend/` folder:

```bash
mvn spring-boot:run
```

Run tests:

```bash
mvn test
```

## Important implementation note

The future AI or RAG layer may call these endpoints as tools, but this backend does not use AI to perform calculations. All formulas are deterministic, documented, and implemented with `BigDecimal`.
