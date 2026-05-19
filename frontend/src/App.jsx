import { useEffect, useMemo, useState } from "react";
import { getDemoData, postCalculation } from "./api/cbamApi";
import { buildInitialValues, calculatorActions } from "./config/calculatorActions";
import { analyzeCsv, buildInitialActivitySelections } from "./utils/csvAggregation";

const actionAccents = {
  "default-emissions": { icon: "DV", tone: "mint" },
  "actual-emissions": { icon: "AE", tone: "blue" },
  "advanced-certificates": { icon: "AC", tone: "gold" },
  "compare-default-vs-actual": { icon: "CV", tone: "lavender" },
  scenarios: { icon: "PS", tone: "peach" },
  "validate-report": { icon: "VR", tone: "sage" }
};

const assistantPrompts = [
  {
    title: "What is CBAM?",
    body: "Get a simple explanation of the Carbon Border Adjustment Mechanism."
  },
  {
    title: "How are emissions calculated?",
    body: "Understand how default data and measured activity data affect exposure."
  },
  {
    title: "Explain default vs actual values",
    body: "See why measured plant data can reduce certificate estimates."
  }
];

function formatLabel(key) {
  return key
    .replace(/([a-z])([A-Z])/g, "$1 $2")
    .replace(/_/g, " ")
    .replace(/\b\w/g, (character) => character.toUpperCase());
}

function isCurrencyMetric(key) {
  const normalizedKey = key.toLowerCase();

  return ["cost", "price", "eur", "amount due", "total cost"].some((token) =>
    normalizedKey.includes(token)
  );
}

function formatValue(value, options = {}) {
  const { currency = false } = options;

  if (typeof value === "number") {
    const formatted = Number.isInteger(value) ? value.toString() : value.toFixed(4);
    return currency ? `${formatted} €` : formatted;
  }

  if (Array.isArray(value)) {
    return "";
  }

  if (typeof value === "boolean") {
    return value ? "Yes" : "No";
  }

  if (value === null || value === undefined || value === "") {
    return "-";
  }

  return currency ? `${String(value)} €` : String(value);
}

function ResultRenderer({ value, metricKey }) {
  if (Array.isArray(value)) {
    return (
      <div className="result-collection">
        {value.length === 0 ? (
          <div className="result-empty">No items returned.</div>
        ) : (
          value.map((item, index) => (
            <div className="result-card" key={`${index}-${JSON.stringify(item)}`}>
              {Object.entries(item).map(([key, nestedValue]) => (
                <div className="result-row" key={key}>
                  <span>{formatLabel(key)}</span>
                  <strong className={isCurrencyMetric(key) ? "currency-value" : ""}>
                    {formatValue(nestedValue, { currency: isCurrencyMetric(key) })}
                  </strong>
                </div>
              ))}
            </div>
          ))
        )}
      </div>
    );
  }

  return (
    <strong className={isCurrencyMetric(metricKey) ? "currency-value" : ""}>
      {formatValue(value, { currency: isCurrencyMetric(metricKey) })}
    </strong>
  );
}

function isImportantMetric(key, value) {
  if (typeof value !== "number") {
    return false;
  }

  const normalizedKey = key.toLowerCase();

  return [
    "cost",
    "price",
    "eur",
    "amount",
    "total",
    "certificate",
    "emission",
    "embedded",
    "exposure",
    "volume",
    "mass"
  ].some((token) => normalizedKey.includes(token));
}

function App() {
  const [activeActionId, setActiveActionId] = useState(calculatorActions[0].id);
  const [formValues, setFormValues] = useState(() =>
    calculatorActions.reduce((accumulator, action) => {
      accumulator[action.id] = buildInitialValues(action);
      return accumulator;
    }, {})
  );
  const [demoData, setDemoData] = useState(null);
  const [loadingDemoData, setLoadingDemoData] = useState(true);
  const [result, setResult] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [csvFileName, setCsvFileName] = useState("");
  const [csvAnalysis, setCsvAnalysis] = useState(null);
  const [activitySelections, setActivitySelections] = useState({});

  const activeAction = useMemo(
    () => calculatorActions.find((action) => action.id === activeActionId),
    [activeActionId]
  );

  const factorOptions = useMemo(() => {
    const source = demoData?.emissionFactors ?? [];
    const uniqueFactors = new Map();

    source
      .filter((factor) => factor.calculable !== false)
      .forEach((factor) => {
        const key = `${factor.activityType}|${factor.unit}`;
        if (!uniqueFactors.has(key)) {
          uniqueFactors.set(key, factor);
        }
      });

    return Array.from(uniqueFactors.values()).sort((left, right) =>
      left.activityType.localeCompare(right.activityType)
    );
  }, [demoData]);

  const activityOptions = useMemo(() => {
    const grouped = new Map();

    factorOptions.forEach((factor) => {
      const key = factor.activityType;
      const current = grouped.get(key) ?? {
        activityType: factor.activityType,
        units: []
      };

      if (!current.units.includes(factor.unit)) {
        current.units.push(factor.unit);
      }

      grouped.set(key, current);
    });

    return Array.from(grouped.values()).sort((left, right) =>
      left.activityType.localeCompare(right.activityType)
    );
  }, [factorOptions]);

  const activeTone = actionAccents[activeAction.id]?.tone ?? "mint";
  const latestResultEntries = result ? Object.entries(result) : [];
  const csvReadyCount = Object.values(activitySelections).filter((item) => item.enabled).length;
  useEffect(() => {
    let cancelled = false;

    async function loadDemoData() {
      try {
        const response = await getDemoData();
        if (!cancelled) {
          setDemoData(response);
        }
      } catch (error) {
        if (!cancelled) {
          setErrorMessage(
            error.payload?.message ??
              "Could not load backend demo data. Start the Spring app on localhost:8080."
          );
        }
      } finally {
        if (!cancelled) {
          setLoadingDemoData(false);
        }
      }
    }

    loadDemoData();

    return () => {
      cancelled = true;
    };
  }, []);

  function updateField(actionId, fieldName, nextValue) {
    setFormValues((current) => ({
      ...current,
      [actionId]: {
        ...current[actionId],
        [fieldName]: nextValue
      }
    }));
  }

  function buildPayload(actionId) {
    const sourceValues = formValues[actionId];
    const action = calculatorActions.find((item) => item.id === actionId);
    const payload = {};

    action.fields.forEach((field) => {
      const rawValue = sourceValues[field.name];

      if (field.type === "number") {
        payload[field.name] = rawValue === "" ? null : Number(rawValue);
        return;
      }

      if (field.type === "checkbox") {
        payload[field.name] = Boolean(rawValue);
        return;
      }

      if (field.type === "list") {
        payload[field.name] = String(rawValue)
          .split(",")
          .map((value) => Number(value.trim()))
          .filter((value) => Number.isFinite(value));
        return;
      }

      payload[field.name] = rawValue;
    });

    if (actionId === "actual-emissions") {
      const activities = (csvAnalysis?.numericColumns ?? [])
        .map((column) => ({
          column,
          selection: activitySelections[column.name]
        }))
        .filter(
          ({ selection }) =>
            selection?.enabled &&
            selection.activityType.trim() &&
            selection.unit.trim()
        )
        .map(({ column, selection }) => ({
          activityType: selection.activityType,
          amount: Number(column.sum),
          unit: selection.unit
        }));

      payload.activities = activities;
    }

    return payload;
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setSubmitting(true);
    setErrorMessage("");
    setResult(null);

    try {
      const payload = buildPayload(activeAction.id);

      if (activeAction.id === "actual-emissions" && (!payload.activities || payload.activities.length === 0)) {
        throw new Error("Upload a CSV and select at least one aggregated activity column.");
      }

      const response = await postCalculation(activeAction.endpoint, payload);
      setResult(response);
    } catch (error) {
      const details = error.payload?.details?.length
        ? ` ${error.payload.details.join(" | ")}`
        : "";
      setErrorMessage(
        `${error.payload?.message ?? error.message ?? "Calculation failed."}${details}`
      );
    } finally {
      setSubmitting(false);
    }
  }

  async function handleCsvUpload(event) {
    const [file] = event.target.files ?? [];
    if (!file) {
      return;
    }

    const text = await file.text();
    const nextAnalysis = analyzeCsv(text);
    setCsvFileName(file.name);
    setCsvAnalysis(nextAnalysis);
    setActivitySelections(
      buildInitialActivitySelections(nextAnalysis.numericColumns, factorOptions)
    );

    const currentValues = formValues["actual-emissions"];
    if (!currentValues.cnCode && nextAnalysis.records[0]?.cn_code) {
      updateField("actual-emissions", "cnCode", nextAnalysis.records[0].cn_code);
    }
    if (!currentValues.country && nextAnalysis.records[0]?.country) {
      updateField("actual-emissions", "country", nextAnalysis.records[0].country);
    }
  }

  function updateActivitySelection(columnName, nextActivityType) {
    const matchedOption = activityOptions.find(
      (option) => option.activityType === nextActivityType
    );

    setActivitySelections((current) => {
      const previous = current[columnName] ?? {
        enabled: false,
        activityType: "",
        unit: "",
        availableUnits: []
      };

      const availableUnits = matchedOption?.units ?? [];
      const nextUnit = availableUnits.includes(previous.unit)
        ? previous.unit
        : availableUnits[0] ?? "";

      return {
        ...current,
        [columnName]: {
          ...previous,
          activityType: nextActivityType,
          unit: nextUnit,
          availableUnits
        }
      };
    });
  }

  function updateActivityUnit(columnName, nextUnit) {
    setActivitySelections((current) => ({
      ...current,
      [columnName]: {
        ...current[columnName],
        unit: nextUnit
      }
    }));
  }

  function renderField(field) {
    const value = formValues[activeAction.id][field.name];

    if (field.type === "checkbox") {
      return (
        <label className="toggle-field" key={field.name}>
          <div>
            <span>{field.label}</span>
            <small>Include this option in the backend request.</small>
          </div>
          <input
            type="checkbox"
            checked={Boolean(value)}
            onChange={(event) =>
              updateField(activeAction.id, field.name, event.target.checked)
            }
          />
        </label>
      );
    }

    if (field.type === "list") {
      return (
        <label className="field" key={field.name}>
          <span>{field.label}</span>
          <textarea
            rows="3"
            value={value}
            onChange={(event) =>
              updateField(activeAction.id, field.name, event.target.value)
            }
            placeholder="76, 100, 120"
          />
        </label>
      );
    }

    return (
      <label className="field" key={field.name}>
        <span>{field.label}</span>
        <input
          type={field.type}
          min={field.min}
          step={field.step}
          value={value}
          onChange={(event) =>
            updateField(activeAction.id, field.name, event.target.value)
          }
        />
      </label>
    );
  }

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand-block">
          <div className="brand-mark">C</div>
          <div>
            <strong>CBAM Assistant</strong>
            <span>Compliance workspace</span>
          </div>
        </div>

        <nav className="sidebar-nav">
          {calculatorActions.map((action, index) => {
            const accent = actionAccents[action.id] ?? actionAccents["default-emissions"];

            return (
              <button
                className={`nav-item ${action.id === activeActionId ? "active" : ""}`}
                key={action.id}
                type="button"
                onClick={() => {
                  setActiveActionId(action.id);
                  setResult(null);
                  setErrorMessage("");
                }}
              >
                <span className={`nav-icon ${accent.tone}`}>{accent.icon}</span>
                <span className="nav-copy">
                  <strong>{action.label}</strong>
                  <small>Tool {index + 1}</small>
                </span>
              </button>
            );
          })}
        </nav>

        <div className="sidebar-card">
          <p className="sidebar-title">Drive impact.</p>
          <p>Measured data, default values, and certificate planning in one place.</p>
        </div>

        <div className="sidebar-footer">
          <div className="avatar-badge">CA</div>
          <div>
            <strong>CarbonAI Team</strong>
            <span>Workspace online</span>
          </div>
        </div>
      </aside>

      <main className="workspace">
        <section className="dashboard-column">
          <form className="form-shell" onSubmit={handleSubmit}>
            <div className="section-heading">
              <p className="eyebrow">Selected tool</p>
              <h1>{activeAction.label}</h1>
              <p>{activeAction.description}</p>
            </div>

            <div className="field-grid">
              {activeAction.fields.map((field) => renderField(field))}
            </div>

            {activeAction.id === "actual-emissions" ? (
              <div className="csv-zone">
                <div className="section-heading compact">
                  <h2>CSV activity ingestion</h2>
                  <p>
                    Upload measured plant data, auto-sum numeric columns, then map the
                    selected values to backend activities.
                  </p>
                </div>

                <label className="upload-box">
                  <input type="file" accept=".csv,text/csv" onChange={handleCsvUpload} />
                  <strong>{csvFileName || "Choose a CSV file"}</strong>
                  <small>
                    Example columns: production, natural gas, gas oil, electricity
                  </small>
                </label>

                {csvAnalysis ? (
                  <div className="csv-results">
                    <div className="csv-summary">
                      <div>
                        <strong>{csvAnalysis.records.length}</strong>
                        <span>Rows parsed</span>
                      </div>
                      <div>
                        <strong>{csvAnalysis.numericColumns.length}</strong>
                        <span>Numeric totals</span>
                      </div>
                      <div>
                        <strong>{csvReadyCount}</strong>
                        <span>Selected activities</span>
                      </div>
                    </div>

                    <div className="activity-list">
                      {csvAnalysis.numericColumns.map((column) => {
                        const selection = activitySelections[column.name] ?? {
                          enabled: false,
                          activityType: "",
                          unit: "",
                          availableUnits: []
                        };
                        const selectedOption = activityOptions.find(
                          (option) => option.activityType === selection.activityType
                        );
                        const availableUnits =
                          selectedOption?.units ?? selection.availableUnits ?? [];

                        return (
                          <div className="activity-card" key={column.name}>
                            <label className="checkbox-field">
                              <input
                                type="checkbox"
                                checked={selection.enabled}
                                onChange={(event) =>
                                  setActivitySelections((current) => ({
                                    ...current,
                                    [column.name]: {
                                      ...selection,
                                      enabled: event.target.checked
                                    }
                                  }))
                                }
                              />
                              <span>{column.name}</span>
                            </label>

                            <div className="activity-meta">
                              <span>Summed amount</span>
                              <strong>{column.sum}</strong>
                            </div>

                            <label className="field">
                              <span>Activity name from emission tables</span>
                              <select
                                value={selection.activityType}
                                onChange={(event) =>
                                  updateActivitySelection(column.name, event.target.value)
                                }
                              >
                                <option value="">Choose activity type</option>
                                {activityOptions.map((option) => (
                                  <option key={option.activityType} value={option.activityType}>
                                    {option.activityType}
                                  </option>
                                ))}
                              </select>
                            </label>

                            {availableUnits.length > 1 ? (
                              <label className="field">
                                <span>Unit</span>
                                <select
                                  value={selection.unit}
                                  onChange={(event) =>
                                    updateActivityUnit(column.name, event.target.value)
                                  }
                                >
                                  {availableUnits.map((unit) => (
                                    <option key={`${column.name}-${unit}`} value={unit}>
                                      {unit}
                                    </option>
                                  ))}
                                </select>
                              </label>
                            ) : (
                              <div className="activity-meta inline">
                                <span>Unit</span>
                                <strong>{selection.unit || "-"}</strong>
                              </div>
                            )}
                          </div>
                        );
                      })}
                    </div>
                  </div>
                ) : (
                  <div className="empty-state">CSV sums will appear here after upload.</div>
                )}
              </div>
            ) : null}

            <div className="submit-row">
              <button className={`primary-button ${activeTone}`} type="submit" disabled={submitting}>
                {submitting ? "Calculating..." : "Run calculation"}
              </button>
              <p className="hint">Requests go to `http://localhost:8080/api/cbam`.</p>
            </div>
          </form>

          <section className="recent-card">
            <div className="recent-header">
              <div>
                <h2>Latest response</h2>
                <p>The backend calculation result stays on the left with the selected tool.</p>
              </div>
            </div>

            {result ? (
              <div className="result-block">
                {latestResultEntries.map(([key, value]) => (
                  <div
                    className={`result-section ${isImportantMetric(key, value) ? "important" : ""}`}
                    key={key}
                  >
                    <div className="result-row">
                      <span>{formatLabel(key)}</span>
                      <ResultRenderer value={value} metricKey={key} />
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="empty-state">
                Run a calculation to render the latest backend response here.
              </div>
            )}
          </section>
        </section>

        <aside className="assistant-column">
          <section className="assistant-card">
            <div className="assistant-header">
              <div className="assistant-badge">AI</div>
              <div>
                <h2>What can I do?</h2>
                <p>
                  The frontend keeps the same logic, while this panel presents results in a
                  cleaner assistant workspace.
                </p>
              </div>
            </div>

            <div className="prompt-list">
              {assistantPrompts.map((prompt) => (
                <button className="prompt-card" key={prompt.title} type="button">
                  <strong>{prompt.title}</strong>
                  <span>{prompt.body}</span>
                </button>
              ))}
            </div>

            <div className="assistant-feed">
              <article className="message assistant intro">
                <span className="message-role">CBAM Assistant</span>
                <p>
                  Use the selected tool on the left for calculations. This right panel is
                  reserved for the AI chat experience.
                </p>
              </article>

              {errorMessage ? (
                <article className="message error">
                  <span className="message-role">System</span>
                  <p>{errorMessage}</p>
                </article>
              ) : null}
            </div>

            <div className="assistant-composer">
              <textarea
                rows="3"
                placeholder="Ask anything..."
                disabled
              />
              <button type="button" disabled>
                Send
              </button>
            </div>
          </section>
        </aside>
      </main>
    </div>
  );
}

export default App;
