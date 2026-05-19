function parseCsv(text) {
  const rows = [];
  let currentRow = [];
  let currentCell = "";
  let inQuotes = false;

  for (let index = 0; index < text.length; index += 1) {
    const character = text[index];
    const nextCharacter = text[index + 1];

    if (character === "\"") {
      if (inQuotes && nextCharacter === "\"") {
        currentCell += "\"";
        index += 1;
      } else {
        inQuotes = !inQuotes;
      }
      continue;
    }

    if (character === "," && !inQuotes) {
      currentRow.push(currentCell);
      currentCell = "";
      continue;
    }

    if ((character === "\n" || character === "\r") && !inQuotes) {
      if (character === "\r" && nextCharacter === "\n") {
        index += 1;
      }
      currentRow.push(currentCell);
      rows.push(currentRow);
      currentRow = [];
      currentCell = "";
      continue;
    }

    currentCell += character;
  }

  if (currentCell.length > 0 || currentRow.length > 0) {
    currentRow.push(currentCell);
    rows.push(currentRow);
  }

  return rows.filter((row) => row.some((cell) => String(cell).trim() !== ""));
}

function cleanNumber(rawValue) {
  if (rawValue === null || rawValue === undefined) {
    return null;
  }

  const normalized = String(rawValue).trim();
  if (normalized === "") {
    return null;
  }

  const numericValue = Number(normalized);
  return Number.isFinite(numericValue) ? numericValue : null;
}

export function analyzeCsv(text) {
  const rows = parseCsv(text);
  if (rows.length < 2) {
    return {
      headers: [],
      records: [],
      numericColumns: []
    };
  }

  const headers = rows[0].map((header, index) => {
    const value = String(header ?? "").trim();
    return value || `column_${index + 1}`;
  });

  const records = rows.slice(1).map((row) => {
    const record = {};
    headers.forEach((header, index) => {
      record[header] = row[index] ?? "";
    });
    return record;
  });

  const numericColumns = headers
    .map((header) => {
      let sum = 0;
      let numericCount = 0;
      let nonEmptyCount = 0;

      records.forEach((record) => {
        const rawValue = record[header];
        const trimmedValue = String(rawValue ?? "").trim();
        if (trimmedValue === "") {
          return;
        }

        nonEmptyCount += 1;
        const numericValue = cleanNumber(trimmedValue);
        if (numericValue === null) {
          numericCount = -999999;
          return;
        }

        numericCount += 1;
        sum += numericValue;
      });

      const isNumeric = numericCount > 0 && numericCount === nonEmptyCount;

      return {
        name: header,
        sum: Number(sum.toFixed(6)),
        isNumeric,
        numericCount: isNumeric ? numericCount : 0,
        nonEmptyCount
      };
    })
    .filter((column) => column.isNumeric);

  return {
    headers,
    records,
    numericColumns
  };
}

export function buildInitialActivitySelections(numericColumns, factorOptions) {
  const factorMap = factorOptions.reduce((accumulator, factor) => {
    const key = factor.activityType.toLowerCase();
    const existing = accumulator.get(key) ?? [];
    existing.push(factor);
    accumulator.set(key, existing);
    return accumulator;
  }, new Map());

  return numericColumns.reduce((accumulator, column) => {
    const matchedFactors = factorMap.get(column.name.toLowerCase()) ?? [];
    const matchedFactor = matchedFactors[0] ?? null;
    accumulator[column.name] = {
      enabled: Boolean(matchedFactor),
      activityType: matchedFactor?.activityType ?? "",
      unit: matchedFactor?.unit ?? "",
      availableUnits: matchedFactors.map((factor) => factor.unit)
    };
    return accumulator;
  }, {});
}
