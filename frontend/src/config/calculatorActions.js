export const calculatorActions = [
  {
    id: "default-emissions",
    label: "Default Emissions",
    endpoint: "/default-emissions",
    description: "Use fallback country and CN-code values for fast exposure estimates.",
    fields: [
      { name: "country", label: "Country", type: "text", defaultValue: "Turkey" },
      { name: "cnCode", label: "CN Code", type: "text", defaultValue: "25233000" },
      { name: "year", label: "Year", type: "number", defaultValue: 2026, min: 2023 },
      { name: "exportVolumeTons", label: "Export Volume (t)", type: "number", defaultValue: 100, min: 0, step: "0.01" }
    ]
  },
  {
    id: "actual-emissions",
    label: "Calculate With Actual Emissions",
    endpoint: "/actual-emissions",
    description: "Upload a CSV, aggregate measurable columns, and map them into valid backend activities.",
    fields: [
      { name: "country", label: "Country", type: "text", defaultValue: "Turkey" },
      { name: "cnCode", label: "CN Code", type: "text", defaultValue: "72142000" },
      { name: "year", label: "Year", type: "number", defaultValue: 2026, min: 2023 },
      { name: "productionVolumeTons", label: "Production Volume (t)", type: "number", defaultValue: 100, min: 0, step: "0.01" },
      { name: "exportVolumeTons", label: "Export Volume (t)", type: "number", defaultValue: 40, min: 0, step: "0.01" },
      { name: "includeIndirectEmissions", label: "Include Indirect Emissions", type: "checkbox", defaultValue: true }
    ]
  },
  {
    id: "advanced-certificates",
    label: "Advanced Certificates",
    endpoint: "/advanced-certificates",
    description: "Estimate surrender obligation and cost with a more financial lens.",
    fields: [
      { name: "actualSpecificEmbeddedEmissionsTco2ePerTon", label: "Actual Specific Emissions", type: "number", defaultValue: 2.145, min: 0, step: "0.0001" },
      { name: "specificEmbeddedFreeAllocationTco2ePerTon", label: "Free Allocation", type: "number", defaultValue: 1.4625, min: 0, step: "0.0001" },
      { name: "effectiveCarbonPricePaidInCountryOfOriginEurPerTco2e", label: "Origin Carbon Price (EUR)", type: "number", defaultValue: 0, min: 0, step: "0.01" },
      { name: "euEtsWeeklyAveragePriceEurPerTco2e", label: "EU ETS Price (EUR)", type: "number", defaultValue: 76, min: 0, step: "0.01" },
      { name: "importedQuantityTons", label: "Imported Quantity (t)", type: "number", defaultValue: 100, min: 0, step: "0.01" }
    ]
  },
  {
    id: "compare-default-vs-actual",
    label: "Compare Default vs Actual",
    endpoint: "/compare-default-vs-actual",
    description: "Show the potential savings from measured data versus default values.",
    fields: [
      { name: "defaultSpecificEmbeddedEmissionsTco2ePerTon", label: "Default Specific Emissions", type: "number", defaultValue: 2.145, min: 0, step: "0.0001" },
      { name: "actualSpecificEmbeddedEmissionsTco2ePerTon", label: "Actual Specific Emissions", type: "number", defaultValue: 1.6, min: 0, step: "0.0001" },
      { name: "exportVolumeTons", label: "Export Volume (t)", type: "number", defaultValue: 100, min: 0, step: "0.01" },
      { name: "euEtsWeeklyAveragePriceEurPerTco2e", label: "EU ETS Price (EUR)", type: "number", defaultValue: 76, min: 0, step: "0.01" }
    ]
  },
  {
    id: "scenarios",
    label: "Price Scenarios",
    endpoint: "/scenarios",
    description: "Run multiple ETS prices against the same emissions quantity.",
    fields: [
      { name: "embeddedEmissionsTco2e", label: "Embedded Emissions", type: "number", defaultValue: 214.5, min: 0, step: "0.0001" },
      { name: "euEtsWeeklyAveragePricesEurPerTco2e", label: "EU ETS Prices", type: "list", defaultValue: "76, 100, 120" }
    ]
  },
  {
    id: "validate-report",
    label: "Validate Report",
    endpoint: "/validate-report",
    description: "Check whether a CBAM report payload is internally consistent.",
    fields: [
      { name: "goodsItemNumber", label: "Goods Item Number", type: "text", defaultValue: "1" },
      { name: "sequenceNumber", label: "Sequence Number", type: "text", defaultValue: "1" },
      { name: "cnCode", label: "CN Code", type: "text", defaultValue: "25233000" },
      { name: "country", label: "Country", type: "text", defaultValue: "Turkey" },
      { name: "period", label: "Period", type: "text", defaultValue: "2026" },
      { name: "directEmissionsTco2e", label: "Direct Emissions", type: "number", defaultValue: 1.82, min: 0, step: "0.0001" },
      { name: "indirectEmissionsTco2e", label: "Indirect Emissions", type: "number", defaultValue: 0.14, min: 0, step: "0.0001" },
      { name: "totalEmissionsTco2e", label: "Total Emissions", type: "number", defaultValue: 1.96, min: 0, step: "0.0001" },
      { name: "netMassTons", label: "Net Mass (t)", type: "number", defaultValue: 100, min: 0, step: "0.01" }
    ]
  }
];

export function buildInitialValues(action) {
  return action.fields.reduce((accumulator, field) => {
    accumulator[field.name] = field.defaultValue;
    return accumulator;
  }, {});
}
