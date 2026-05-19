# Demo Data Files

This folder contains static CSV files for the CarbonAI TR backend demo. The files are designed to be simple, realistic, and easy for a Spring Boot backend to parse with standard CSV tooling.

## What each CSV file represents

### `turkish_steel_exporter_activity_data.csv`

This file represents one reporting month of actual factory activity data from a Turkish steel manufacturer exporting a CBAM-covered product to Germany in March 2026.

Use this file when the backend should calculate emissions from real factory activity inputs.

Main backend endpoint:

- `POST /api/cbam/actual-emissions`

Suggested mapping:

- `NATURAL_GAS`, `DIESEL`, and `ELECTRICITY` rows become activity inputs
- `PRODUCTION_VOLUME` provides `productionVolumeTons`
- `EXPORT_VOLUME` provides `exportVolumeTons`

### `turkish_cement_default_value_demo.csv`

This file represents a Turkish cement exporter that does not have actual factory activity data and wants to estimate CBAM exposure using a default value.

Use this file when the backend should calculate emissions from a seeded default CBAM value instead of real activity data.

Main backend endpoints:

- `POST /api/cbam/default-emissions`

## CSV column meanings

### Columns in `turkish_steel_exporter_activity_data.csv`

- `facility_id`: Unique identifier for the production facility
- `company_name`: Legal or business name of the exporter
- `country`: Country where the facility is located
- `reporting_period`: Reporting month in `YYYY-MM` format
- `product_name`: Human-readable product name
- `cbam_sector`: CBAM sector label used by the project
- `cn_code`: Customs CN code for the product
- `destination_country`: EU destination country for the export
- `activity_type`: Activity or quantity type such as `NATURAL_GAS`, `DIESEL`, `ELECTRICITY`, `PRODUCTION_VOLUME`, `EXPORT_VOLUME`
- `amount`: Numeric quantity for the activity or output
- `unit`: Unit of the amount such as `m3`, `liter`, `kWh`, or `ton`
- `emission_category`: High-level category such as `DIRECT`, `INDIRECT`, `PRODUCT_OUTPUT`, or `EXPORT_OUTPUT`
- `data_source`: Business source of the number such as meter, invoice, or ERP record
- `notes`: Free-text explanation for humans

### Columns in `turkish_cement_default_value_demo.csv`

- `company_name`: Legal or business name of the exporter
- `country`: Country where the exporter is located
- `reporting_year`: CBAM reporting year
- `product_name`: Human-readable product name
- `cbam_sector`: CBAM sector label used by the project
- `cn_code`: Customs CN code for the product
- `destination_country`: EU destination country for the export
- `calculation_mode`: Indicates that the file is for default-value mode
- `export_volume_tons`: Export quantity in tons
- `default_value_tco2e_per_ton`: Default emissions intensity in tCO2e per ton
- `default_value_year`: Year of the default value applied
- `certificate_price_eur_per_tco2e`: Example carbon price used for simple cost estimation
- `notes`: Free-text explanation for humans

## Example calculation from `turkish_steel_exporter_activity_data.csv`

Seeded emission factors used by the backend demo:

- Natural gas: `2.0196 kgCO2e/m3`
- Diesel: `2.676492 kgCO2e/liter`
- Electricity: `0.42 kgCO2e/kWh`

Step-by-step:

1. Natural gas emissions

`5000 m3 x 2.0196 kgCO2e/m3 = 10098 kgCO2e = 10.098 tCO2e`

2. Diesel emissions

`1200 liter x 2.676492 kgCO2e/liter = 3211.7904 kgCO2e = 3.2117904 tCO2e`

3. Electricity emissions

`25000 kWh x 0.42 kgCO2e/kWh = 10500 kgCO2e = 10.5 tCO2e`

4. Direct emissions

`10.098 + 3.2117904 = 13.3097904 tCO2e`

5. Total emissions including indirect

`13.3097904 + 10.5 = 23.8097904 tCO2e`

6. Specific emissions

`23.8097904 / 100 = 0.238097904 tCO2e/t`

7. Exported embedded emissions

`0.238097904 x 40 = 9.52391616 tCO2e`

8. Simple cost at `76 EUR/tCO2e`

`9.52391616 x 76 = 723.81762816 EUR`

Expected backend result:

- `directEmissionsTco2e = 13.3098`
- `indirectEmissionsTco2e = 10.5000`
- `totalFacilityEmissionsTco2e = 23.8098`
- `specificEmissionsTco2ePerTon = 0.2381`
- `exportedEmbeddedEmissionsTco2e = 9.5239`
- `estimatedCostEur = 723.82` if rounded to 2 decimals by the simple cost endpoint

## Example calculation from `turkish_cement_default_value_demo.csv`

Step-by-step:

1. Embedded emissions

`100 tons x 2.145 tCO2e/t = 214.5 tCO2e`

2. Simple cost at `76 EUR/tCO2e`

`214.5 x 76 = 16302 EUR`

Expected backend result:

- `embeddedEmissionsTco2e = 214.5000`
- `estimatedCostEur = 16302.00`

## Which backend endpoint should use each file

- `turkish_steel_exporter_activity_data.csv`
  Use with `POST /api/cbam/actual-emissions`

- `turkish_cement_default_value_demo.csv`
  Use with `POST /api/cbam/default-emissions`
