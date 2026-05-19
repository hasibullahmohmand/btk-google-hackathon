# CarbonAI TR: AI-Powered CBAM Financial Risk Intelligence

CarbonAI TR helps exporters understand, predict, and reduce the financial risk created by the European Union Carbon Border Adjustment Mechanism (CBAM).

CBAM is no longer only a reporting topic. The transitional period covered 2023 to 2025, but from 2026 onward it becomes a real financial compliance obligation. Companies exporting carbon-intensive goods such as steel, aluminium, cement, fertilisers, hydrogen, and electricity to the EU need to understand three things clearly:

`How much embedded emissions does my product have?`
`How much could CBAM cost my company?`
`How can we reduce this financial risk?`

This project answers those questions with a hybrid architecture that combines deterministic emissions and cost calculation with AI-powered regulatory intelligence.

## Why This Matters

CBAM turns carbon emissions into financial exposure.

For exporters, this creates a new class of business risk:

- higher embedded emissions can mean more CBAM certificates;
- more certificates can mean higher euro-denominated cost exposure;
- poor data quality can increase compliance uncertainty;
- relying on default values instead of actual plant data can lead to worse financial outcomes;
- finance, operations, sustainability, and trade compliance teams all need a shared view of the same risk.

That is why CarbonAI TR is highly relevant for the finance sector. It is not just an emissions calculator. It is a financial risk intelligence platform for companies exposed to CBAM.

## What The Product Does

Our product is an AI-powered CBAM financial risk intelligence platform for companies exporting carbon-intensive goods to the European Union.

It helps companies:

- estimate embedded emissions for CBAM-covered goods;
- convert those emissions into estimated CBAM financial exposure;
- compare default-value-based exposure versus actual-data-based exposure;
- understand which rules, product classifications, and reporting requirements apply;
- reduce uncertainty by connecting numerical outputs to official EU source documents.

In short, the platform helps exporters predict and reduce CBAM financial exposure using AI, while keeping all critical numerical calculations deterministic and auditable.

## Core Product Idea

The product is built around a simple principle:

`AI explains the rules. The backend calculates the numbers.`

This matters because CBAM is a high-trust use case. Companies need explainability, traceability, and confidence that the system is not inventing emissions values or financial numbers.

In practice, this means the agent asks for explanation after a calculation-related function call is completed. The numerical result comes from the backend function first, and then the AI layer explains what that result means, which rule it relates to, and how the user should interpret it.

Our approach separates responsibilities clearly:

- the AI layer supports reasoning, document retrieval, rule explanation, product classification, and compliance guidance;
- the Spring Boot backend remains the trusted source for formulas, emission factors, default values, and CBAM cost estimation.

## Architecture

The system uses a three-layer architecture.

### 1. Frontend: React + Vite

The frontend is developed with React and Vite and provides the main user interface.

It allows users to:

- upload production data through CSV files;
- enter activity data manually;
- run different CBAM calculation flows;
- view the latest backend results;
- ask follow-up compliance questions through the AI assistant.

The uploaded or entered data can include:

- fuel consumption;
- electricity usage;
- raw materials;
- production quantities;
- exported quantities.

The frontend lives in `frontend/`.

### 2. Deterministic Backend: Spring Boot

The backend is developed with Spring Boot and acts as the deterministic CBAM calculation engine.

It uses structured CSV reference data and fixed formulas to:

- calculate embedded emissions with default values;
- calculate embedded emissions from actual activity data;
- estimate CBAM-related financial exposure;
- compare default-value and actual-data scenarios;
- validate report structure and consistency;
- run carbon-price scenarios;
- support advanced certificate estimation logic.

The backend does not use AI for any calculation.

This is a critical product decision. AI does not invent emission numbers, cost values, or formula outputs. All numerical calculations are performed by verified backend tools.

The backend lives in `backend/`.

### 3. AI Agent Service: FastAPI + RAG

The AI agent service is developed with FastAPI and uses retrieval-augmented generation over an official CBAM document knowledge base.

The agent can:

- determine whether a product may fall under CBAM;
- help identify the relevant CN code;
- look up applicable default values;
- identify which emission categories are relevant;
- explain how a calculation is performed;
- explain CBAM terminology, product rules, and compliance requirements;
- retrieve relevant information from official CBAM documents in the RAG system.

The AI agent acts as a compliance assistant, not as a calculation engine.

The service lives in `agent_service/`.

## End-to-End Workflow

The general workflow is:

`Company data -> React frontend -> Spring Boot calculation engine -> Embedded emissions -> CBAM cost estimation -> AI compliance explanation and recommendations`

This workflow is especially valuable for finance-oriented decision making because it connects operational data to regulatory cost exposure.

After each relevant function call, the system can return not only the computed output but also an explanation layer generated by the agent. This helps users understand the result in business and compliance terms without replacing the deterministic calculation itself.

## Knowledge Base And Traceability

To support trustworthy AI outputs, we collected official CBAM documents from European Union sources, including:

- regulations;
- guidance documents;
- annexes;
- product-specific rules;
- default value tables;
- implementation and reporting materials.

All document sources and metadata are tracked in `pdfs-metadata.json`, making the knowledge base transparent and traceable.

The repository also contains:

- original source PDFs in `pdfs/`;
- extracted Markdown files in `pdfs/outputs/raw_markdown/`;
- processed chunks in `data/processed/`;
- the vector database in `data/vectorstore/`.

This traceability is important because compliance users need to know where the explanation came from, not just what the answer is.

## What Makes This Useful For A Finance Jury

CarbonAI TR sits at the intersection of:

- climate regulation;
- trade compliance;
- financial exposure analysis;
- AI-assisted decision support.

CBAM changes carbon from a sustainability metric into a cost driver. That means exporters need tools for:

- carbon cost forecasting;
- regulatory exposure analysis;
- trade compliance support;
- what-if scenario analysis;
- risk reduction planning.

This product addresses exactly that need.

## Main Product Capabilities

The backend currently supports APIs for:

- default-value emissions calculations;
- actual factory activity emissions calculations;
- advanced certificate estimation;
- default-vs-actual comparison;
- carbon price scenario analysis;
- CBAM-style report validation;
- demo data discovery.

Key endpoints:

- `POST /api/cbam/default-emissions`
- `POST /api/cbam/actual-emissions`
- `POST /api/cbam/advanced-certificates`
- `POST /api/cbam/compare-default-vs-actual`
- `POST /api/cbam/scenarios`
- `POST /api/cbam/validate-report`
- `GET /api/cbam/demo-data`

Swagger UI is available at:

- `http://localhost:8080/swagger-ui.html`

OpenAPI JSON is available at:

- `http://localhost:8080/v3/api-docs`

The frontend/backend API contract is documented in [FRONTEND_API_CONTRACT.md](backend/FRONTEND_API_CONTRACT.md).

## How The Calculations Work

### Default Value Mode

Use this mode when the exporter does not have actual factory emissions data.

The backend:

1. finds a seeded default value by country and CN code;
2. chooses the correct year-specific value;
3. multiplies export volume by that default value.

Formula:

`embeddedEmissions = exportVolumeTons x defaultValueTco2ePerTon`

### Actual Data Mode

Use this mode when the exporter has real factory activity data.

The backend:

1. looks up an emission factor for each activity;
2. converts activity amounts into `kgCO2e`;
3. converts `kgCO2e` into `tCO2e`;
4. sums direct and indirect emissions;
5. calculates specific emissions per ton;
6. applies that intensity to the exported quantity.

Main formulas:

- `activityEmissionsKg = amount x factorKgCo2ePerUnit`
- `activityEmissionsTco2e = activityEmissionsKg / 1000`
- `specificEmissions = totalFacilityEmissions / productionVolumeTons`
- `exportedEmbeddedEmissions = specificEmissions x exportVolumeTons`

### Advanced Certificate Logic

For more detailed estimation, the backend also supports advanced certificate logic using parameters such as:

- specific embedded emissions;
- free allocation adjustment;
- imported quantity;
- effective carbon price paid in the country of origin;
- EU ETS-linked certificate price.

Core formulas used by the project:

`certificatesBeforeCarbonPriceAdjustment = max(0, (specificEmbeddedEmissions - specificEmbeddedFreeAllocation) x importedQuantity)`

`carbonPriceReductionInCertificates = certificatesBeforeCarbonPriceAdjustment x effectiveCarbonPricePaidInCountryOfOrigin / euEtsWeeklyAveragePrice`

`certificatesToSurrender = max(0, certificatesBeforeCarbonPriceAdjustment - carbonPriceReductionInCertificates)`

`estimatedCostEur = certificatesToSurrender x euEtsWeeklyAveragePrice`

## Example Financial Exposure Scenario

A Turkish cement exporter ships `100` tons of aluminous cement to Germany in `2026` and does not have actual factory emissions data.

The backend:

1. finds the seeded Turkey + `25233000` record;
2. chooses the `2026` default value with markup: `2.145 tCO2e/t`;
3. calculates embedded emissions: `100 x 2.145 = 214.5 tCO2e`;
4. if the certificate-linked price is `76 EUR/tCO2e`, estimates exposure: `214.5 x 76 = 16,302 EUR`.

This simple scenario shows why CBAM must be treated not only as a sustainability issue, but also as a financial planning and risk management issue.

## Repository Structure

```text
.
|-- agent_service/          FastAPI AI agent and RAG workflow
|-- backend/                Spring Boot deterministic CBAM engine
|-- frontend/               React + Vite user interface
|-- csv/                    CBAM default value reference CSV files
|-- emission_tables_csv/    Emission factor tables
|-- data/                   Processed RAG data and vector store
|-- pdfs/                   Official CBAM source documents
|-- images/                 Project visuals
|-- pdfs-metadata.json      Source tracking for knowledge base documents
|-- requirements.txt        Python dependencies for the AI agent service
`-- README.md
```

## Local Setup

### Requirements

To run the full project locally, you need:

- Java `17`
- Maven
- Node.js `18+`
- Python `3.11+`
- Ollama for local embedding and optional local chat models

### 1. Create A Python Virtual Environment

From the project root:

```powershell
python -m venv .venv
.venv\Scripts\Activate.ps1
pip install -r requirements.txt
```

This installs the Python dependencies used by the FastAPI agent service and the RAG pipeline.

### 2. Configure Environment Variables

Copy `.env.example` to `.env` and fill in the values you want to use.

Example variables:

- `GOOGLE_API_KEY`
- `GEMINI_API_KEY`
- `CBAM_AGENT_MODEL`
- `CBAM_MODEL`
- `CBAM_WRITER_MODEL`
- `CORS_ALLOW_ORIGINS`

If you use Gemini models, provide a valid Google or Gemini API key.

### 3. Start The Spring Boot Backend

```powershell
cd backend
mvn spring-boot:run
```

The backend starts on:

- `http://localhost:8080`

### 4. Start The FastAPI Agent Service

In a new terminal from the project root:

```powershell
.venv\Scripts\Activate.ps1
uvicorn agent_service.app:app --reload --host 0.0.0.0 --port 8000
```

The agent service starts on:

- `http://localhost:8000`

### 5. Start The Frontend

In a new terminal:

```powershell
cd frontend
npm install
npm run dev
```

The frontend is typically available on:

- `http://localhost:5173`

## AI Agent Behavior

Every `/api/chat` request in the agent service follows this logic:

1. FastAPI receives the user message and optional `thread_id`.
2. The orchestrator routes the message into either normal chat or CBAM-specific workflow.
3. The task generation step extracts fields such as product name, CN code, year, country, and export volume.
4. The workflow can run:
   - product-to-CN lookup;
   - CN-code default-value lookup;
   - RAG retrieval over official CBAM documents;
   - backend calculation explanation.
5. The writer agent produces the final response from retrieved facts and tool outputs.

Important boundaries:

- the agent does not perform authoritative arithmetic;
- the agent does not replace backend calculations;
- the agent can request and produce explanation after a function call is completed, using the function output as the basis for the explanation;
- the agent explains rules, logic, documents, and likely applicability;
- the backend remains the source of numerical truth.

## Why The Design Is Strong

This design is strong for a regulated finance-related use case because it combines:

- deterministic computation for trust;
- AI assistance for usability;
- RAG traceability for compliance;
- modular architecture for maintainability;
- financial framing for real business value.

Instead of building a generic chatbot about carbon, the project focuses on a specific regulatory and financial pain point with a clear user workflow and a realistic enterprise architecture.

## Final Summary

CarbonAI TR helps exporters understand how CBAM affects them financially.

It combines:

- React for user interaction;
- Spring Boot for deterministic CBAM calculations;
- FastAPI and RAG for AI-powered compliance assistance;
- official EU documents for traceable regulatory intelligence.

The result is a practical AI system for answering the most important CBAM questions:

`How much embedded emissions does my product have?`
`How much could CBAM cost my company?`
`How can we reduce this financial risk?`
