# React Frontend API Contract

This backend does not ship a built-in Spring-served frontend anymore.

Frontend expectation:

- Run the UI as a separate React application.
- Call the backend over HTTP at `http://localhost:8080`.
- Use the API base path `http://localhost:8080/api/cbam`.
- Expect JSON responses for both success and error cases.

## CORS

The backend allows browser requests from these frontend dev origins by default:

- `http://localhost:3000`
- `http://localhost:5173`

You can override them with:

```bash
APP_CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173
```

## Common Rules

- Send `Content-Type: application/json` for all `POST` endpoints.
- Treat emissions values as decimal numbers in `tCO2e`, `kgCO2e`, or `tCO2e/t` depending on the field name.
- Treat money values as decimal numbers in EUR.
- Expect rounded response values:
  - emissions: 4 decimals
  - money: 2 decimals
  - percent: 2 decimals

## Success Response Pattern

Every endpoint returns a JSON object specific to that calculation.

## Error Response Pattern

On validation or business errors, expect:

```json
{
  "error": "VALIDATION_ERROR",
  "message": "Request validation failed",
  "details": [
    "fieldName: reason"
  ],
  "timestamp": "2026-05-19T19:00:00+03:00"
}
```

## Endpoints

### `POST /default-emissions`

Request:

```json
{
  "country": "Turkey",
  "cnCode": "25233000",
  "year": 2026,
  "exportVolumeTons": 100
}
```

Response fields:

- `country`
- `cnCode`
- `productDescription`
- `year`
- `exportVolumeTons`
- `selectedDefaultValueTco2ePerTon`
- `embeddedEmissionsTco2e`
- `calculationMode`
- `formula`

### `POST /actual-emissions`

Important:

- The real contract requires `cnCode`, `country`, and `year`.
- `activities[].activityType` and `activities[].unit` must match CSV-backed values.
- Do not send simplified placeholder enums unless they exist in the loaded factor catalogue.
- The frontend should usually fetch `GET /demo-data` first and let the user choose valid factor rows from there.

Request:

```json
{
  "cnCode": "72142000",
  "country": "Turkey",
  "year": 2026,
  "productionVolumeTons": 100,
  "exportVolumeTons": 40,
  "includeIndirectEmissions": true,
  "activities": [
    { "activityType": "Natural gas", "amount": 50, "unit": "t" },
    { "activityType": "Gas/Diesel oil", "amount": 5, "unit": "t" }
  ]
}
```

Response fields:

- `cnCode`
- `country`
- `year`
- `productionVolumeTons`
- `exportVolumeTons`
- `directEmissionsTco2e`
- `indirectEmissionsTco2e`
- `totalFacilityEmissionsTco2e`
- `specificEmissionsTco2ePerTon`
- `exportedEmbeddedEmissionsTco2e`
- `includeIndirectEmissions`
- `calculationMode`
- `activityBreakdown`
- `warnings`

Each `activityBreakdown` item contains:

- `activityType`
- `amount`
- `unit`
- `factor`
- `factorUnit`
- `emissionsTco2e`
- `emissionCategory`

### `POST /advanced-certificates`

Request:

```json
{
  "actualSpecificEmbeddedEmissionsTco2ePerTon": 2.145,
  "specificEmbeddedFreeAllocationTco2ePerTon": 1.4625,
  "effectiveCarbonPricePaidInCountryOfOriginEurPerTco2e": 0,
  "euEtsWeeklyAveragePriceEurPerTco2e": 76,
  "importedQuantityTons": 100
}
```

Main response fields:

- `totalEmbeddedEmissionsTco2e`
- `certificateSurrenderObligationBeforeCarbonPriceAdjustment`
- `carbonPriceReductionInCertificates`
- `certificatesToSurrender`
- `estimatedCostEur`

### `POST /compare-default-vs-actual`

Request:

```json
{
  "defaultSpecificEmbeddedEmissionsTco2ePerTon": 2.145,
  "actualSpecificEmbeddedEmissionsTco2ePerTon": 1.6,
  "exportVolumeTons": 100,
  "euEtsWeeklyAveragePriceEurPerTco2e": 76
}
```

Main response fields:

- `defaultCostEur`
- `actualCostEur`
- `potentialSavingsEur`
- `savingsPercent`
- `message`

### `POST /scenarios`

Request:

```json
{
  "embeddedEmissionsTco2e": 214.5,
  "euEtsWeeklyAveragePricesEurPerTco2e": [76, 100, 120]
}
```

Response fields:

- `embeddedEmissionsTco2e`
- `scenarios`

Each scenario item contains:

- `euEtsWeeklyAveragePriceEurPerTco2e`
- `estimatedCostEur`

### `POST /validate-report`

Request:

```json
{
  "goodsItemNumber": "1",
  "sequenceNumber": "1",
  "cnCode": "25233000",
  "country": "Turkey",
  "period": "2026",
  "directEmissionsTco2e": 1.82,
  "indirectEmissionsTco2e": 0.14,
  "totalEmissionsTco2e": 1.96,
  "netMassTons": 100
}
```

Response fields:

- `valid`
- `errors`
- `warnings`

Each error item contains:

- `code`
- `message`
- `expectedValue`
- `actualValue`

### `GET /demo-data`

Use this endpoint for frontend bootstrapping and assisted form building.

Response fields:

- `defaultValues`
- `emissionFactors`
- `demoCarbonPrices`
- `demoProducts`

Frontend guidance:

- Use `defaultValues` to prefill country and CN-code choices.
- Use `emissionFactors` to drive valid `activityType` and `unit` pairs.
- Filter out rows where `calculable` is `false` when building `/actual-emissions` forms.

