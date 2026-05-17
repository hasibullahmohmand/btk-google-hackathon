# Application User Manual

## CBAM Declarant Portal ver 1.4.1.1

Date: 18/03/2025 Status: Submitted for information (SfI) Version: 1.81 EN Author: SOFT-DEV Approved by: DG TAXUD Reference number: DLV-746-7.3-202-1-10 Public: DG TAXUD external Confidentiality: Publicly available (PA)

## Document control information

| Property | Value |
| Title | Application User Manual |
| Subtitle | CBAM Declarant Portal ver 1.4.1.1 |
| Author| SOFT-DEV |
| Project owner| Head of Unit of DG TAXUD Unit C2 Indirect taxes other than VAT |
| Solution provider | DG TAXUD Unit B2 Architecture &Digital Operations |
| DG TAXUD Project Manager | DG TAXUD Unit B2 Architecture &Digital Operations |
| Version | 1.81 EN |
| Confidentiality | Publicly available (PA) |
| Date | 18/03/2025 |


# 1 INTRODUCTION

## 1.1 DOCUMENT PURPOSE

The purpose of this document is to provide directions to reporting declarants and CBAM (Carbon Border Adjustment Mechanism) declarants (referred to in this document CBAM declarants) on how to utilise the CBAM Declarant Portal (referred to in this document CBAM Declarant Portal).

## 1.2 TARGET AUDIENCE

The target audience for this document includes:

- Importers  of  CBAM goods into the  EU or their  authorised  indirect customs  representatives (reporting declarants or CBAM declarants);
- Directorate-General Taxation and Customs Union (DG TAXUD) Project team;
- Directorate General for Informatics (DIGIT);
- Directorate-General for Climate Action (DG CLIMA);
- EU Member states and their National Competent Authorities;
- SOFT-DEV Project team;
- Quality Assurance Contractor;
- Operational teams.

## 1.3 SCOPE

The scope of this document is to provide directions to CBAM Reporting Declarants on the effective utilisation of the CBAM Declarant Portal for the Transitional Registry. The features described comply with CBAM Release 1.4.

## 1.4 STRUCTURE

This document is organised as follows:

- Chapter 1 - Introduction: describes the scope and the objectives of the document;
- Chapter 2 - General Information: provides the practical and theoretical details for the topic treated in the document;
- Chapter 3 - Getting Started: details how to access the portal and introduces the basis system functions;
- Chapter  4  -  Using  the  System  as  CBAM  Reporting  Declarant: describes  the  CBAM Declarant Portal functionalities;
- Annex CBAM Report Structure in XSD format and a sample ZIP is provided in this section.

## 1.5 REFERENCE DOCUMENTS

The table below lists the documents that are referred to in the current document.

Table 1: Reference documents

| Ref.| Title | Originator|Version | Date|
|--------|---------------------------|--------------|-----------|--------|
| R01 | Access Management through | TBA | 0.2 | TBA |

## 1.6 APPLICABLE DOCUMENTS

The table below lists the documents to which the current document must be compliant (e.g. FWC, SC, RfA).

Table 2: Applicable documents

| Ref.| Title| Originator| Version| Date |
|--------|------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|-----------|------------|
| A01 | SOFT-DEV Framework Quality Plan| SOFT-DEV | 1.20| 17/06/2024 |
| A02 | SOFT-DEV Framework Contract, TAXUD/2021/CC/162 | DG TAXUD | N/A | 24/06/2021 |
| A03 | Specific Contract 13, TAXUD/2023/DE/134 | DG TAXUD | N/A | 05/04/2023 |
| A04 | RfA-746-CARBON BORDER ADJUSTMENT MECHANISM (CBAM) - Delivery of CBAM v1.4.0.0 (TP integration with CBAM Definitive System Phase 2) Ares(2024)6359497 | DG TAXUD | 1.10| 09/09/2024 |

## 1.7 ABBREVIATIONS &amp; ACRONYMS

For a better understanding of the present document, the following table provides a list of the principal abbreviations and acronyms used.

See also the 'list of acronyms' on TEMPO.

| Abbreviation/Acronym| Definition|
|------------------------|----------------------------------------------------|
| CBAM| Carbon Border Adjustment Mechanism |
| CN | Combined Nomenclature |
| CO2 | Carbon Dioxide |
| COM | European Commission|
| CRS | Customer Reference System |
| CS/RD2 | Central Services Reference Data |
| DG CLIMA| Directorate-General for Climate Action |
| DG TAXUD| Directorate General for Taxation and Customs Union |
| DIGIT | Directorate General for Informatics|
| DOC | Microsoft Word document|
| DOCX| Microsoft Word Open XML format document|
| EORI| Economic Operators Registration and Identification |
| ETS | Emissions Trading System |
| EU | European Union |
| GNSS| Global Navigation Satellite System |
| GPS | Global Positioning System |

Table 3: Abbreviations and acronyms

| Abbreviation/Acronym| Definition |
|------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| HS | Harmonized System Codes|
| ID | Identification number |
| IEA | International Energy Agency|
| JPEG| Joint Photographic Experts Group |
| MIME| Multipurpose Internet Mail Extensions |
| MS | Member State |
| MWH | Megawatt Hour |
| N/A | Not available |
| NCA | National Competent Authority |
| NIS | National Import System |
| O3CI| Operator of Third Country Installation registry |
| PDF | Portable Document Format|
| PSM | Public-Subscribe Mechanism |
| SPEED2ng| SPEED2 Gateway is used for exchanging messages with partner countries (for example MRAexchanges, NCTS TIR messages and in the future SSTL messages). The messages are exchanged via dedicated VPN links and the exchange protocols vary (Web Services,MQ Asynchronous Transport Protocol, AS2, AS3 and AS4 protocols). |
| UCC | Union Customs Code |
| UI | User Interface|
| URI | Universal Resource Identifier |
| URL | Uniform Resource Locator|
| UUM&DS | Uniform User Management and Digital Signatures|
| XLS | Microsoft Excel spreadsheet file |
| XLSX| Microsoft Excel Open XML spreadsheet file |
| XML | eXtensible Markup Language |
| XSD | XML Schema Definition |
| ZIP | Compressed file |

## 1.8 DEFINITIONS

For a better understanding of the present document, the following table provides a list of the principal terms used.

See also the 'glossary' on TEMPO.

| Term | Definition|
|---------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| CBAM Goods | Goods listed in Annex I of CBAM Regulation. |
| Third country | A country or territory outside the customs territory of the Union. |
| Default value | The Default value is the intensity of emissions. It expresses the quantity of CO2 emissions per measurement unit of product. Per legislation, the definition is a value, which is calculated or drawn from secondary data, which represents the embedded emissions in goods. |

Table 4: Definitions

| Term| Definition|
|---------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| NCA | National Competent Authority.|
| Operator | Any person who operates or controls an installation in a third country. |
| Installation | A stationary technical unit where a production process is carried out.|
| Indirect customs representative | The indirect customs representative declared in imports customs declaration. |
| Importer | Either the person lodging a customs declaration for release for free circulation of goods in its own name and on its own behalf or, where the customs declaration is lodged by an indirect customs representative in accordance with Article 18 of Regulation (EU) No 952/2013, the person on whose behalf such a declaration is lodged. |
| Carbon price | The monetary amount paid in a third country, under a carbon emissions reduction scheme, in the form of a tax, levy or fee or in the form of emission allowances under a greenhouse gas emissions trading system, calculated on greenhouse gases covered by such a measure, and released during the production of goods. |
| Indirect emissions | Means emissions from the production of electricity, which is consumed during the production processes of goods, irrespective of the location of the production of the consumed electricity. |

## 1.9 CBAM QUARTERLY REPORT DATA ELEMENTS

For a better understanding of the present document, the following table provides a list of the principal terms used.

See also the 'glossary' on TEMPO.

| Data element name | Definition |
|-------------------------------|------------------------------------------------------------------------------------------------------------|
| CBAMReport | The CBAM Quarterly Report that Declarants will submit. |
| Report issue date | The date that the CBAM report is submitted.|
| Draft report ID| The unique ID set by the system when the report is created to identify their report. |
| Report ID | This unique ID is allocated by the system once the report is submitted. |
| Reporting Period | The Quarter of the year. This is a CBAM CS/RD2 List that contains the Quarters of a year (Q1, Q2, Q3, Q4). |
| Year | The year for which the report is submitted.|
| Total goods imported | The total Goods that are declared in the CBAMreport |
| Total emissions| The total of the emissions for all the Goods declared in the CBAM report. |
| --Reporting declarant| The entity that contains information about the Declarant. |
| Identification number| The ID of the Declarant. This is their EORI number. |
| Name | The full name (person or company) of the Declarant. |
| Role | The role that the Declarant has. If they are the importer or an indirect customs representative. |
| ----Address| This element is used to hold the Report Declarant Address |
| Member State of establishment | The country where the Declarant is established |
| Sub-division | The sub-division where the Declarant is located. |
| City | The city where the Declarant is located.|
| Street | The street name where the Declarant is located.|
| Street additional line | The street name where the Declarant is located.|
| Number | The number of the street where the Declarant is located.|
| Postcode| The postcode of the location of the Declarant. |
| P.O. Box| The P.O. box of the address.|

| Data element name | Definition|
|------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| --Indirect customs representative | This entity is to define the information about the indirect customs representative in the Header level |
| Identification number | The ID of the indirect customs representative. This is their EORI number. |
| Name | The full name (person or company) of the indirect customs representative. |
| ----Address | This element is used to hold the indirect customs representative's address|
| Member State of establishment| The country where the Indirect customs representative is established|
| Sub-division | The sub-division where the indirect customs representative is located. |
| City | The city where the indirect customs representative is located.|
| Street| The street name where the indirect customs representative is located. |
| Street additional line| The street name where the indirect customs representative is located. |
| Number| The number of the street where the indirect customs representative is located.|
| Postcode | The postcode of the location of the indirect customs representative.|
| P.O. Box | The P.O. box of the address. |
| --Importer| The entity that contains information about the Importer that is represented by the Declarant. The Importer can either be declared at Header level, if the same importer is common for all Goods Imported, or in the Goods Imported level. |
| Identification number | The ID of the importer. This is their EORI number of European importers or TINT for International importers|
| Name | The full name (person or company) of the importer. |
| ----Address | This element is used to hold the importer's address |
| Member state or Country of establishment | The country where the Importer is established (either a Member state of the EU or a third country). |
| Sub-division | The sub-division where the importer is located. |
| City | The city where the importer is located.|
| Street| The street name where the importer is located.|
| Street additional line| The street name where the importer is located.|
| Number| The number of the street where the importer is located.|
| Postcode | The postcode of the location of the importer. |
| P.O. Box | The P.O. box of the address. |
| --Competent authority | This entity contains information regarding the Competent Authority where the Declarant is established. |
| Reference number| The Name or Reference number of the Competent Authority. |

| Data element name| Definition|
|----------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| --Signatures | There are two types of signatures in the CBAM Report. The first one is related to the information in the report. The Declarant must confirm that the information in theCBAM report is accurate and complete. The second is only applicable if the type of applicable rules selected is not CBAM-specific. In this case, the Declarant must confirm that the used applicable rules are comparable to theCBAM applicable rules |
| ----Report confirmation| This entity is used to hold the confirmation related to the contents of the CBAMReport.|
| Report global data confirmation | This element holds the confirmation of the Declarant regarding the information of the CBAM Report. |
| Use of data confirmation | This element holds the confirmation of the Declarant regarding his approval to use the data of the CBAM Report for the review process. |
| Date of signature| This element holds the date when the confirmation was signed.|
| Place of signature | This element holds the place where the confirmation was signed. |
| Signature | This element is used to hold the full name of the Declarant. |
| Position of person signing| This element is used to hold the Position of the Declarant|
| ----Type of applicable reporting methodology | This entity is used for the confirmation of the type of applicable reporting methodologies, in case that the selected one is not the CBAM-specific. |
| Other applicable reporting methodology | This element is used to hold the confirmation of the 'other' applicable reporting methodology used. |
| --Remarks | This is an entity that exists in all levels of the report in order the Declarant to insert additional information. |
| Additional information | In this element, the Declarant can write any additional information that applies in the location where this is positioned |
| --CBAM goods imported | This is the entity that contains the information for the CBAM Good. There may be up to 99999 CBAM Goods in a CBAMreport. |
| Goods item number| This element specifies the item number of every CBAM Good imported of the CBAM Report. It is a sequential number.|
| ----Indirect customs representative | This entity is to define the information about the indirect customs representative in the CBAM goods Imported level of theCBAM Report. |
| Identification number | The ID of the indirect customs representative. This is their EORI number. |
| Name| The full name (person or company) of the indirect customs representative. |
| ------Address| This element is used to hold the indirect customs representative's address|
| Member State of establishment| The country where the Indirect customs representative is established|

| Data element name | Definition|
|---------------------------------------------|---------------------------------------------------------------------------------------------------------------------------|
| Sub-division| The sub-division where the indirect customs representative is located. |
| City | The city where the indirect customs representative is located.|
| Street| The street name where the indirect customs representative is located. |
| Street additional line| The street name where the indirect customs representative is located. |
| Number| The number of the street where the indirect customs representative is located.|
| Postcode | The postcode of the location of the indirect customs representative.|
| P.O. Box | The P.O. box of the address. |
| ----Importer| This entity is to define the information about the importer in the CBAM goods Imported level of the CBAM Report |
| Identification number | The ID of the importer. This is their EORI number of European importers or TINT for International importers|
| Name | The full name (person or company) of the importer. |
| ------Address| This element is used to hold the importer's address |
| Member state or Country of establishment | The country where the importer is established |
| Sub-division| The sub-division where the importer is located. |
| City | The city where the importer is located.|
| Street| The street name where the importer is located.|
| Street additional line| The street name where the importer is located.|
| Number| The number of the street where the importer is located.|
| Postcode | The postcode of the location of the importer. |
| P.O. Box | The P.O. box of the address. |
| ----Commodity code | This is an entity that contains the HS Code and the CN Code of the CBAMGood. |
| Harmonized System sub-heading code | This is the HS Code of theCBAM Good. It is a 6-digit code.|
| Combined nomenclature code | This is the CN Code of theCBAM Good. It is a 2-digit code that is added in the HS code.|
| ------Commodity details | This is an entity that contains the description element of the CBAM Good. |
| Description of goods | This is an element that can be used in order to describe the CBAM Good.|
| ----Country of origin | This is an entity to declare the Country of Origin of theCBAM Good. |
| Country code| The two letter code of a country.|
| ----Imported quantity per customs procedure | This is the Entity that contains information regarding the Customs procedures and the related quantities of the CBAMgood. |
| Sequence number | |
| ------Procedure | This is an entity that contains the Customs Procedures.|

| Data element name | Definition |
|---------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Requested procedure | The Customs Requested Procedure. |
| Previous procedure| The Customs Previous Procedure. |
| --------Inward processing information| This entity contains information regarding the Inward processing procedure. The inward processing customs procedure is used for Goods that enter the EU territory to be processed by EU manufacturers. |
| Member State of inward processing authorisation| The Member State which authorised the inward processing.|
| Inward processing waiver for Bill of discharge | The Inward processing waiver for the Bill of discharge.|
| Authorisation | The Authorisation related to the inward processing. |
| Globalisation time start| The start date of the Globalisation timer. Each authorization comes with a specific globalization time frame depending on the calculation of the required time for processing.|
| Globalisation time end | The end date of the Globalisation timer. Each authorization comes with a specific globalization time frame depending on the calculation of the required time for processing. |
| Deadline for submission of Bill of discharge| The deadline for the submission of the Bill of discharge. Typically it will be one month after the end date of the Globalisation timer.|
| ------Area of import | This is an entity that contains the area of import element.|
| Area of import | The area where the CBAMGood was imported. Two possible values: EU by means of Customs import declaration and EU by other means. |
| ------Goods measure (per procedure) Actual products release / Actual product released to other products | This Data Group contains the quantity of the CBAMGood per Customs procedure. |
| Procedure indicator | The procedure indicator. Inward Processing = 1, Other = 0 |
| Net mass| The Net mass is to be used for the CBAM Goods that are measured in tonnes. Please use a dot (.) to separate decimals. |
| Supplementary units | The Supplementary units is to be used for the CBAMGoods that have a special measurement unit. |
| Type of measurement unit| The type of measurement unit of the quantity of theCBAM Good. |
| ------Special references for goods| This is an entity to use in case there are special references related to the CBAM Good.|
| Additional information | In this element, the Declarant can write any additional information that applies in the location where this is positioned. |
| ----Goods measure (imported)| This is the entity that contains the quantity of the CBAM goods imported|
| Net mass| The Net mass is to be used for the CBAM Goods that are measured in tonnes. |

| Data element name| Definition |
|----------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Supplementary units | The Supplementary units is to be used for the CBAMGoods that have a special measurement unit. |
| Type of measurement unit| The type of measurement unit of the quantity of theCBAM Good. |
| ----Goods imported total emissions | This is an entity to use in case there are special references related to the CBAM Good. |
| Goods emissions per unit of product | In this element, the Declarant can write any additional information that applies in the location where this is positioned. The value is the result of the following division: Goods total emissions / Goods measure (imported) - either the Net mass or the Supplementary units. |
| Goods total emissions | This element is used to hold the total emissions of both direct and indirect emissions. The value of this field must be equal to the sum of Good direct emissions + Good indirect emissions. |
| Goods direct emissions | This element is used to hold the total emissions of direct emissions. The value of this field must be equal to the sum of all the installation direct emissions. |
| Goods indirect emissions| This element is used to hold the total emissions of indirect emissions. The value of this field must be equal to the sum of all the installation indirect emissions. |
| Type of measurement unit for emissions | This element is to indicate that the emissions are measured in tonnes. |
| ----Supporting documents (for Goods)| This entity exists in theCBAM Goods imported level. It used to provide any supporting documents. |
| Sequence number | Supporting document sequence number. |
| Type| The type of supporting document (it is in the Supporting Documents (for Goods)). |
| Country of document issuance | The country where the supporting document was issued.|
| Reference number | The reference number of the supporting document. |
| Document line item number | The document line item number is the number of the Goods imported entry in which the supporting documents corresponding to.|
| Issuing authority name | The authority that issued the supporting document.|
| Validity start date | The start date of validity of the supporting document. |
| Validity end date| The end date of validity of the supporting document. |
| Description| A text element to describe the supporting document. |
| ------Attachments| This is an entity used to attach a file or provide a URL. |
| Filename| The file name of the attachment. |
| URI | The URI link of an attachment. (Starting from Q32024 reporting period, this element is no longer available)|
| MIME| The MIME of the attachment.|

| Data element name | Definition |
|--------------------------------------------|----------------------------------------------------------------------------------------------------------------------------|
| Included binary object | The actual binary object. |
| ----Remarks| This is an entity that exists in all levels of the report in order the Declarant to insert additional information.|
| Additional information | In this element, the Declarant can write any additional information that applies in the location where this is positioned. |
| ----CBAM Goods Emissions| This is the entity for the CBAM Good emissions. For each CBAM Good, there can be up to 999 entries for each CBAM Good. |
| Emissions sequence number | This element specifies the number of every CBAM Good emission. It is a sequential number. |
| Country of production| This element specified the country of production |
| ------The company name of the installation | This is an entity to define the information related to the company name of the installation.|
| Operator ID| The unique identification number of the operator. The operator ID is an alphanumeric value defined by the user.|
| Operator Name | The Name of the operator. |
| --------Address| This entity is to define the Address details of the company name of the Installation.|
| Country code| Installation's Country code|
| Sub-division| The sub-division where the operator is located.|
| City | The city where the operator is located. |
| Street | The street name where the operator is located. |
| Street additional line | The street name where the operator is located. |
| Number | The number of the street where the operator is located. |
| Postcode| The postcode of the location of the operator. |
| P.O. Box| The P.O. box of the address.|
| --------Contact Details | This entity is to define the Contact Details of the Installation Operator. |
| Name | The full name (person or company) of the Declarant. |
| Phone number| The phone number of the person that is assigned in the contact details of the operator. |
| e-mail | The email of the person that is assigned in the contact details of the operator. |
| ------Installation| This entity is to define the Installation (Factory) information where the CBAM Good is produced. |
| Installation ID| The unique ID of the Installation. The installation ID is an alphanumeric value defined by the user. |
| Installation name | The name of the Installation. |
| Economic activity | The definition of the economic activity of the installation.|

| Data element name| Definition |
|----------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| --------Address | The address of the Installation. |
| Country of establishment| The country where the installation that produced the good or the Declarant is established. |
| Sub-division | The sub-division where the installation is located. |
| City| The city where the installation is located.|
| Street | The street name where the installation is located. |
| Street additional line | The street name where the installation is located. |
| Number | The number of the street where the installation is located.|
| Postcode| The postcode of the location of the installation.|
| P.O. Box| The P.O. box of the address. |
| Plot or parcel number | The plot or parcel number. |
| UNLOCODE| The UNLOCODE as defined in UCC Annex B. |
| Latitude| The latitude of the coordinates. |
| Longitude | The longitude of the coordinates.|
| Type of coordinates | The type of coordinates (GPS/GNSS). |
| ------Goods measure (Produced)| This is the entity that contains the quantity of the CBAM Good that is produced in the specific installation. |
| Net mass| The Net mass is to be used for the CBAM Goods that are measured in tonnes. |
| Supplementary units | The Supplementary units is to be used for the CBAMGoods that have a special measurement unit. |
| Type of measurement unit| The type of measurement unit of the quantity of theCBAM Good. |
| ------Installation emissions | This is the entity that contains the emissions that were produced in the Goods emissions. |
| Installation total emissions | The total quantity of emissions produced for the particular Goods' quantity. The value of this field must be equal to the sum of Installation direct emissions + Installation indirect emissions.|
| Installation direct emissions | The quantity of Installation's direct emissions produced for the particular Goods' quantity. The value of this field must be equal with the result of the following formulae: Goods measure x Specific (direct) embedded emissions. |
| Installation indirect emissions | The quantity of indirect emissions produced for the specific particular Goods' quantity. The value of this field must be equal with the result of the following formulae: Goods measure x Specific (indirect) embedded emissions.|
| Type of measurement unit for emissions | This element is to indicate that the emissions are measured in tonnes. |
| ------Direct Embedded Emissions | This is the entity that contains the information related to the direct embedded emissions. |

| Data element name| Definition|
|--------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Type of determination| The type of determination of the direct embedded emissions. The possible values are: Actual data, Default value ('Default' is only applicable in Q423, Q124, Q224 reporting periods). Concerning the option 'Actual data not available', consider elaborations under Section 4.4.3.4.2. Emissions. |
| Type of determination (electricity) | The type of determination of the direct embedded emissions in case that the CBAM Good is electricity. |
| Type of applicable reporting methodology| The type of applicable reporting methodology used to calculate the emissions of the CBAM Good while being produced. (Starting in Q32024 reporting period, this element is not used for Good Electricity). |
| Applicable reporting methodology | The actual applicable reporting methodology used. |
| Specific (direct) embedded emissions | This is the element to declare the emissions per measurement unit of the product produced.|
| Other source indication | This element is used to declare a different source.|
| Source of emission factor (for electricity)| The source of emission factor in case that the CBAMGood is electricity.|
| Emission factor | This element is used only when CBAM Good is Electricity and expresses the tonnes of CO2 per MWh.|
| Electricity imported | The quantity of electricity imported. It is only applicable in the Direct embedded emissions when CBAM Good is electricity. |
| Total embedded emissions of electricity imported | The amount of CO2 tonnes produced for the quantity of the electricity imported. It is only applicable in the Direct embedded emissions when CBAM Good is electricity. |
| Type of measurement unit| The type of measurement unit of the quantity of theCBAM Good.|
| Source of emission factor value | This is used to declare any extra information regarding the source of the emissions value.|
| Justification | Field is no longer used|
| Fulfillment of conditionality | This is used to declare any information related to the actual data declared. It is only applicable in the Direct Embedded Emissions when CBAM Good is electricity. |
| ------Indirect Embedded Emissions| This is the entity that contains the information related to the indirect embedded emissions. Not applicable when Electricity is selected as a good"|
| Type of determination| The type of determination of the indirect embedded emissions. The possible values are: Actual data, Default value. ('Default' is only applicable in Q423, Q124, Q224 reporting periods). Concerning the option 'Actual data not available', consider elaborations under Section 4.4.3.4.2. Emissions. |
| Source of emission factor | This element is used to declare literature or published information by the statistics office.|
| Emission factor | This element is used to express the tonnes of CO2 perMWh |

| Data element name| Definition |
|--------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Specific (indirect) embedded emissions | This is the element to declare the emissions per measurement unit of the product produced. When automatically calculated, the value of this element is the result of the following formula: Electricity consumed (MWh/tonne of CBAM Good) * emissions factor (Tonnes CO2/MWh). |
| Type of measurement unit| The type of measurement unit of the quantity of theCBAM Good. |
| Electricity consumed | The electricity consumed (MWH per measurement unit of product).|
| Source of electricity| This element is used to declare the source of electricity. Possible values: 'Auto-produced', '(bilateral) power purchase agreement', 'received from the grid". |
| Other source indication | This element is used to declare a different source.|
| Source of emissions factor value | This is used to declare any extra information regarding the source of the emissions value. It is only applicable in the Direct embedded emissions when CBAM Good is electricity.|
| ------Production method &Qualifying parameters| This entity contains the information related to the Production method and the emissions qualifying parameters. |
| Sequence number | Production method sequence number |
| Method ID | The unique identity of the production method. |
| Method name| The production method name. |
| Identification number of the specific steel mill | Specific steel mill identification number (only applicable to steel goods) |
| Additional Information | In this element, the Declarant can write any additional information that applies in the location where this is positioned. |
| --------Direct Emissions qualifying parameters| This entity contains the information related to the direct emissions qualifying parameters. This entity has been renamed to 'Emissions qualifying parameters' starting in Q32024. |
| Sequence number | The sequential number of the parameter|
| Parameter name| Name of the parameter used |
| Description| A text element to describe the parameter |
| Type of parameter value | The type of the parameter (e.g., Percentage, numeric, text) |
| Parameter value | The actual value of the parameter |
| Additional information | In this element, the Declarant can write any additional information that applies in the location where this is positioned. |
| --------Indirect Emissions qualifying parameters | This entity contains the information related to the indirect emissions qualifying parameters. Only used in reporting period Q423, Q124,Q224.|
| Sequence number | The sequential number of the parameter|
| Type of determination| The type of determination of the direct embedded emissions. The possible values are: Actual data, Default value.|

| Data element name| Definition |
|--------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| Parameter ID | ID of the parameter used|
| Parameter name| Name of the parameter used |
| Description| A text element to describe the parameter|
| Type of parameter value| The type of the parameter (e.g., Percentage, numeric, text)|
| Parameter value | The actual value of the parameter |
| Additional information | In this element, the Declarant can write any additional information that applies in the location where this is positioned. |
| ------Supporting Documents (for emissions definition) | This entity exists in theCBAM Goods emissions level. It used to provide any supporting documents. |
| Sequence number | The document's sequential number |
| Type of emissions document | This element is used to define the type of emissions document that is declared. It is in the Supporting Documents (for emissions definition). |
| Country of document issuance | The country where the supporting document was issued.|
| Reference number | The reference number of the supporting document. |
| Document line-item number | The document line-item number is the number of the Goods imported entry in which the supporting documents corresponding to.|
| Issuing authority name | The authority that issued the supporting document.|
| Validity start date | The start date of validity of the supporting document. |
| Validity end date| The end date of validity of the supporting document. |
| Description| A text element to describe the supporting document. |
| --------Attachments | This is an entity used to attach a file or provide a URL. |
| Filename| The file name of the attachment. |
| URI | The URI link of an attachment (Starting from Q32024 reporting period, this element is no longer available) |
| MIME| The MIME of the attachment.|
| Included binary object | The actual binary object. |
| ------Carbon price due | This entity is used to provide information regarding CO2 emissions that were due. |
| Sequence number | The sequential number of the Carbon price due |
| Form of carbon price (previous description: Type of instrument) | This element is used to describe the type of instrument (form of carbon price) used. |
| Description and indication of legal act for the carbon price, and for possible rebate or other form of | This element is used to reference the description of the legal act. |

| Data element name | Definition |
|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| compensation obtained (Previous:Description and indication of legal act) ||
| Effective carbon price due (per produced t of goods or per MWh) (Q423-Q224 description: Amount of Carbon Price Due)| The amount of the CO2 emissions due. |
| Currency | The currency used for the declared amount to be paid.|
| Exchange rate | The exchange rate of the currency related to EURO. This is a baseline exchange rate defined by the EU commission. |
| Amount (EURO) | The Amount of the carbon price due expressed in EURO currency. |
| Country code| The country's code. |
| --------Goods covered under carbon price due| This entity is used to provide information regarding the goods for which the CO2 emissions were due. |
| Sequence number| Sequence number. |
| Type of goods covered | This element is used to declare the type of good that is covered under the carbon price due. It is a codified list with possible values: 'Actual product' or 'Other materials'. Starting in Q32024 reporting period, this field will be auto filled with 'Actual product'. |
| CN code of goods covered | The CN code of the product covered. This is used only for information purposes. Starting in Q32024 reporting period, this field will be auto filled with the good.|
| Embedded emissions covered by the carbon price (previous: Quantity of emissions covered) | The quantity of emissions covered.|
| Embedded emissions covered by rebate or any other form of compensation (Previous description: Quantity covered by free allocations, any rebate or any other form of compensation) | The quantity of emissions covered by any form of compensation. |
| Supplementary information| This element is used to enter any supplementary information related to the product covered under price paid.|
| Additional information| In this element, the Declarant can write any additional information that applies in the location where this is positioned. |
| ----------Goods measure (Covered) | This entity is used to provide the quantity of the Good for which the emissions are already paid. |
| Net mass | The Net mass is to be used for the CBAM Goods that are measured in tonnes. |
| Supplementary units| The Supplementary units is to be used for the CBAMGoods that have a special measurement unit. |

Table 5: Definitions

| Data element name | Definition|
|--------------------------|--------------------------------------------------------------------------------------------------------------------|
| Type of measurement unit | The type of measurement unit of the quantity of theCBAM Good.|
| ------Remarks| This is an entity that exists in all levels of the report in order the Declarant to insert additional information. |
| Sequence number | Remarks sequence number|
| Additional Information| Any additional information|

# 2 GENERAL INFORMATION

## 2.1 SYSTEM OVERVIEW

Carbon Border Adjustment Mechanism (CBAM) Declarant Portal is the interface offered to CBAM Reporting Declarants to submit &amp; manage the quarterly reports in the CBAM Transitional Registry.

CBAM Regulation started applying, without financial adjustments, as of 1 st  October 2023 followed by a transitional period until 1 st January 2026. During this period importers of goods falling within the scope of the CBAM only have to report greenhouse gas emissions embedded in their imports and there will be no financial adjustment.

Importers have to report on a quarterly basis the embedded emissions in goods imported during the previous quarter of the calendar year, declaring direct and indirect emissions as well as any carbon price effectively paid abroad. This reporting-only process will apply until the end of 2025, with the last CBAM report, for the fourth quarter of 2025 (1 st October - 31 st December) to be submitted by 31 st January 2026. The reports can start in CBAM Declarant portal once the reporting period has ended (e.g., for Q4 2023, the reporting can start on 1 st January 2024).

Any CBAM goods importer established in an EU Member State is liable to submit the reports in CBAM Declarant Portal. Per Article 2 Definitions in Implementing Regulation (17/8/2023), the CBAM Report can be submitted by one of the following:

(a) the importer who lodges a customs declaration for release for free circulation of goods in its own name and on its own behalf;

(b) the person, holding an authorisation to lodge a customs declaration referred to in Article 182(1) of Regulation (EU) No 952/2013, who declares the importation of goods;

(c) the indirect customs representative, where the customs declaration is lodged by the indirect customs representative  appointed  in  accordance  with  Article  18  of  Regulation  (EU)  No  952/2013,  when  the importer is established outside the Union or where the indirect customs representative has agreed to the reporting obligations in accordance with Article 32 of Regulation (EU) 2023/956.

A CBAM Reporting Declarant can submit a report for multiple importers if needed. One CBAM report per quarter can be submitted.

The  CBAM  Declarant  Portal  is  accessible  using  internet  browsers.  Authorisation  is  provided  by UUM&amp;DS.

## 2.2 AUTHORIZED USE PERMISSION

CBAM Declarant Portal is allowed to be used by CBAM Reporting Declarants who are registered in UUM&amp;DS.

## 2.3 USER SUPPORT

CBAM Reporting Declarants need to contact their respective National Competent Authority Service Desk both for business &amp; technical issues.

# 3 GETTING STARTED

## 3.1 ROLES AND RESPONSIBILITIES

Access to CBAM Declarant Portal is only allowed to Importers and Indirect Custom Representatives who have registered in UUM&amp;DS and have obtained the CBAM Reporting Declarant Business profile. Their respective NCA or National Customs Administration (depending on the user access management solution each NCA will opt for) will be responsible to assign the CBAM Reporting Declarant business profile in UUM&amp;DS where applicable. User registration procedure is defined in [R01] listed in 1.5 Reference documents.

## 3.2 ACCESS THE CBAM DECLARANT PORTAL

Access to the CBAM Reporting Declarant Portal is established via UUM&amp;DS. UUM&amp;DS is used to authenticate CBAM Reporting Declarants.

Step #1: The user accesses the following link: https://cbam.ec.europa.eu/declarant

Step #2: The user is redirected to the application's UUM&amp;DS authentication page, where the following information needs to be filled in: Country, Type of Actor, Type of ID, ID. The domain can  either  be  'Carbon  Border  Adjustment  Mechanism'  or  'Customs'  based  on  national decision. Delegation to Employees is supported, please refer to UUM&amp;DS Manual R01 for further information.

Figure 1: UUM&amp;DS authentication page



Step #3: The user is requested to provide the username and password in the next screen based on the authentication system defined in his UUM&amp;DS user profile.

Step #4: Upon successful login, the user is redirected to the main dashboard of the  CBAM Reporting Declarant Portal.

Figure 2: CBAM Declarant Portal - Main Dashboard



## 3.3 EXIT THE CBAM DECLARANT PORTAL

Select the menu 'Sign out' which is available on the upper right corner of the application.

Figure 3: Sign-out



The application will redirect the user to EU Logout page where the button 'Log me out' needs to be clicked in order to complete the sign out process.

Figure 4: Logout in EU Login page



A confirmation dialog will appear that confirms that the log out process completed successfully.

Figure 5: Logout confirmation



Also note that the user will be logged out automatically after a certain amount of time has passed and subsequently it will be required to re-authenticate in UUM&amp;DS.

## 3.4 CBAM -TP - SESSION TIME OUT

The session timeout defines the maximum period a user can remain logged in without activity before the system automatically logs them out. This feature enhances security by preventing unauthorized access if a user forgets to log out. In CBAM-TP, the default session duration is set to 30 minutes. A timer displayed on the information bar (left side) shows the remaining time before automatic logout (see Figure 6: Session Timer).

Figure 6: Session Timer



Declarants receive a prompt to extend the session shortly before expiration (5 minute beforehand) see Figure 7: Session expiration warning.

Figure 7: Session expiration warning



## 3.5 REPORTING DECLARANT HOME PAGE

Once a user is logged in to the CBAM Declarant Portal, the CBAM Portal home page is shown.

The available items are:

1. My quarterly reports: When clicked, the Reporting Declarant can see a table organised per quarters of the year. For each quarter, there might be only one CBAM report or not. If a CBAM report exists, then the Reporting Declarant can view it. If no CBAM report has been created, and the reporting period is open for submission of CBAM reports, then the Reporting Declarant can create their report or upload it in XML format with attachments packaged in a ZIP file.
2. My  Installations :  When  clicked,  the  Reporting  Declarant  can  see  the  registry  of  their Installations. They can view the list of Installations inserted, view a specific Installation, edit the  entry  of  an  Installation,  or  delete  an  Installation.  Every  Installation  has  one  Operator assigned.  It  is  advised  to  first  create  the  registry  of  Operators  and  subsequently  create  the registry of Installations.
3. My Operators: When clicked, the Reporting Declarant can see the Registry of their Operators. They can view their list of Operators inserted, view a specific Operator, edit the entry of an Operator, or delete an Operator.
4. My importers: When clicked, the Reporting Declarant who acts as an indirect representative, can see the list of their importers. The Reporting Declarant can select an Importer to view their profile and the associated goods Imported table.
5. Information: Clicking  on  "Information"  allows  the  Reporting  Declarant  access  to  several resources. These include the NCA's contact information, a list of CBAM goods, and CBAM legislation.
6. Requests: When clicked, the Reporting Declarants can see:
7. o Incoming Requests: These are requests sent from the NCA to the Reporting Declarant. The  Reporting  Declarant  can  employ  the  filter  options  to  review  outstanding  NCA requests for which the Declarant's response is pending.
8. o Outgoing Requests: These are requests initiated by the Reporting Declarant and sent to the NCA. The Reporting Declarant can use the available filter options to track the Declarant's requests for which the NCA's response is pending.
7. My profile information: When clicked, the Reporting Declarant can see the information of the logged-in user in the right side and the Reporting Declarant information on the left side.

Figure 8: CBAM Portal - Reporting Declarant Home Page



## 3.6 MAIN NAVIGATION BAR

The main navigation menu is located on the left side of the screen.

By clicking the icon on the left upper side, the navigation bar on the left is expanded (see Figure 9: CBAM Reporting Declarant Home Page with expanded navigation bar). The options are the same as the ones visible in the Home Page. 三

Figure 9: CBAM Reporting Declarant Home Page with expanded navigation bar



## 3.7 GENERIC USER INTERFACE BEHAVIOUR

An overview of the generic user interface behaviour is being provided below.

- a) Report - The fields marked with red asterisk are mandatory.
- b) Report - The number on the top-right corner of each field is the maximum number of characters that can be entered for the particular field.
- c) Report Validation errors - The validation errors are displayed in each tab. The number displayed within each tab indicates the number of validation errors that need to be addressed.

Figure 10: Mandatory fields indication



Figure 11: Maximum number of characters indication



Figure 12: Validation errors



## 3.8 BASIC SYSTEM FUNCTIONS

## 3.8.1 Change UI Language

Please follow the instructions in section  4.8 Preferences

## 3.8.2 Change Contact Information

Please follow the instructions in section 4.7 My Profile Information

## 3.8.1 Create an installation

- 1) Open the "My Installations Registry" either by clicking on "My Installations" icon from the Home Page dashboard or by clicking on "My Installations" link in the left side navigation menu.
- 2) Click the 'Create an installation' button on the upper right corner.
- 3) Fill  the  information  in  the  installation  screen  details  (see  Figure  14:  Installation  -  Details). Ensure to utilise Latin characters (alpha numeric).

Figure 13: My Installations Registry



Figure 14: Installation - Details



- 4) All mandatory fields marked with red asterisk need to be filled in.
- 5) An installation needs to be linked with an operator. To accomplish this, click the 'Search in third country installation operators registry'.
- 6) The operators already created in My Operators' registry will be listed. The list can be filtered by Operator ID, Operator name, Country code, City.

Figure 15: Installation-Search in my operator registry



Figure 16: Search in my operators registry





- 7) Select the required operator. Click on the bullet on the left and it will change to
- 8) Click the 'OK' button on the right bottom corner of the operator registry dialog.
- 9) The relationship between the installation and operator has been established and the operator's details will be listed.
- 10) The 'Create' button is now enabled on the upper right corner.
- 11) Once the installation is created, it will be listed in installations' registry.

Figure 17: Installation - Linked Operator Details



Figure 18: Installation - Create Button



## 3.9 REPORT STATES INFORMATION

This  section  describes  the  different  states  of  a  CBAM  quarterly  report  and  also  illustrates  the  state transitions throughout the CBAM quarterly report lifecycle.

## 3.9.1 States description

The  following  table  lists  all  states  that  a  report  can  have  throughout  its  lifecycle  along  with  their respective descriptions.

| States| Description |
|----------|------------------------------------------------------------------------------------------------|
| Draft | Initial state. The CBAM quarterly report is under preparation by the CBAM Reporting Declarant. |

Table 6: CBAM List of states

| States| Description |
|-----------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Submitted| The CBAM quarterly report has been submitted. |
| Under amendment | The CBAM quarterly report is under amendment by the CBAM Reporting Declarant. |
| Invalidated | The CBAMquarterly report was invalidated by the CBAM Reporting Declarant. A new CBAM quarterly report can be created for this reporting period.|
| Registered | The CBAM quarterly report has been officially registered and is available for further actionsbyEU & NCA Authorities. The CBAM quarterly report changes to Registered automatically upon expiration of the modification period timer. |
| Under assessment (COM)| The CBAMquarterly report is under assessment by European Commission. |
| Assigned to NCA | The CBAM quarterly report is assigned to the respective NCA.|
| Under review (NCA) | The review process has started by the NCA. |
| Discarded| The CBAMquarterly report has been Discarded by the NCA. This is a final state. |
| Under correction| The CBAM quarterly report is under correction by the CBAM Reporting Declarant (either upon request from NCA or CBAM Reporting Declarant when the state of the review is Under Review (NCA) or Registered). |
| Under Penalties /Litigation | The CBAM quarterly report is under penalties / litigation. |
| Finalized| The CBAM quarterly report evaluation has been completed. Future potential states are Revoked or Archived.|
| Revoked | Once a CBAMquarterly report is finalized the state can change to Revoked.|
| Archived | Once a CBAMquarterly report is finalized the state can change to Archived. |

## 3.9.2 States transition diagram

The following State Transition Diagram illustrates the full lifecycle of a CBAM quarterly report, the interaction between the Reporting Declarant and the CBAM System, as well as the interaction between European Commission (COM) and National Competent Authorities (NCAs) with the CBAM System in terms of the CBAM quarterly report review.

Figure 19: State Transition Diagram



# 4 USING THE SYSTEM AS CBAM REPORTING DECLARANT

In this section the main actions that a CBAM Reporting Declarant can perform in the CBAM Declarant Portal are being described.

## 4.1 MY OPERATORS REGISTRY

CBAM System allows to store information for operators of production installations in third countries. It is suggested to store operators in operator's registry to easily prefill the relevant information when a report is created.

## 4.1.1 Create an operator

- 1) Open the "Operators Registry" either by clicking on "Operators" icon from the Home Page dashboard or by clicking on "Operators" link in the left side navigation menu.
- 2) Click the 'Create an Operator' button on the upper right corner.
- 3) Fill  in  information  in  the  operator  screen  details  (see  Figure  21:  Operator  Details  Screen). Ensure to utilise Latin characters (alpha numeric).

Figure 20: Operator - Create



Figure 21: Operator Details Screen



- 4) Please ensure that mandatory fields (marked with a red asterisk) are filled in as a minimum. When this is done, the 'Create' button on the upper right corner will be enabled.
- 5) Once the operator is created, it will be listed in Operators Registry.

Figure 22: Create an Operator



Figure 23: My Operators Registry



## 4.1.2 Delete an operator

In the operator's registry,

Figure 24: Operator Registry - Delete action.



when the icon is clicked for a specific operator, a confirmation message will appear to confirm the action to delete the operator.

Figure 25: Operator Delete - Confirmation message





To proceed with the operator deletion,  the  'Delete' button  needs  to  be  clicked  in  the  confirmation message.

## 4.1.3 Edit an operator

In the operator's registry,

Figure 26: Operators' registry



when the icon is  clicked for a specific operator, the operator details screen will open. When changes are completed, the 'Save' button needs to be clicked to save the updated operator information. In case, changes are not required, the 'Cancel' button needs to be clicked.



Figure 27: Operation - Save &amp; Cancel



## 4.2 INSTALLATIONS REGISTRY

CBAM System allows to store information for production installations in third countries. It is suggested to store installations in Installation Registry to easily prefill the relevant information when a report is created.

## 4.2.1 Delete an installation

In the installations registry,

Figure 28: Installation Registry -Delete action



when the icon is clicked for a specific installation, a confirmation message will appear to confirm the action to delete the installation.

Pnstcnde



Figure 29: Installation Delete - Confirmation message



To proceed with the installation deletion, the 'Delete' button needs to be clicked in the confirmation message.

## 4.2.2 Edit an installation

In the installations registry,

Figure 30: Installation Registry - Edit Action



when the icon is clicked for a specific installation, the installation details screen will open. When changes are completed, the 'Save' button needs to be clicked to save the updated operator information. In case, changes are not required, the 'Cancel' button needs to be clicked.



Figure 31: Installation -Cancel and Save buttons



## 4.3 MY QUARTERLY REPORTS

In this section, each Reporting Declarant can see a table organised per quarters of the year. For each quarter, there might be a CBAM report or not. If a CBAM report exists, then the Reporting Declarant can view it. If no CBAM report has been created, and the reporting period is open for submission of CBAM reports, then the Reporting Declarant can create their report using the user interface (see 4.5.1 Creation of a report  or upload it in XML format with attachments packaged in a ZIP file (see 4.5.2 Uploading of a report)  or create it using a previous report (see 4.5.14 Duplicating a report ).

If no CBAM report has been created and the reporting period is closed for CBAM reports submission, please follow the process  4.5.11 Request to submit a report with a delay.

Figure 32: My Quarterly Reports



## 4.4 REPORT STRUCTURE &amp; NAVIGATION

The CBAM quarterly report structure consists of three main sections:

- The Report's header section where the basic information for the CBAM Reporting Declarant and the reporting period are entered.
- The CBAM goods imported section,  where  the  CBAM  goods  codes  imported  during  the reporting period, along with information regarding their quantities, countries of origin and areas of import are declared in an aggregated way per CN code.
- The goods  emissions section,  where  data  regarding  the  CO2  emissions  and  the  emissions qualifying parameters to produce each CBAM good added in the report and any supporting documents, are declared per installation and per production method.

Figure 33: CBAM Report Data Model



Each of the above sections is comprised of the data groups and of the data elements available to be filled in, along with their cardinality, optionality, rules, conditions and code-lists that are applicable for each data. It is to be noted that the hierarchical structure of the data element needed for the report is following the Customs declaration approach as used in the Trans-European Customs systems and provides the advantage to the CBAM Reporting Declarants to declare the emissions and the supporting information for the CBAM goods imported in a familiar and efficient way. The report's structure can also support the submission of supporting documents as binary attachments along with the report. The accepted file formats are: DOC, DOCX, XLS, XLSX, PDF and JPEG. The maximum size of the ZIP file is 20 MB.

Please note that the report can be filled in with one of the two following ways:

- a) Using the CBAM Reporting Declarant Portal user interface available.
- b) Uploading  a  ZIP  file  containing  the  XML  structure  of  the  report  along  with  the  binary attachments.

Instructions on how to fill each section of the report follow below.

## 4.4.1 Header level of the CBAM Report

Report general information is stored in the header.

Please save the header prior to start filling the goods &amp; emissions section. The draft report ID is filled in automatically (upper left corner). Please note that when the 'Validate' button is clicked, the relevant validation errors that need to be addressed are displayed in each tab.

Figure 34: Header - Validation



The 'Preview' button can be used to preview all values that are filled in header.

A detailed description of each tab is available below.

## 4.4.1.1 Reporting Declarant

Reporting  Declarant  Name,  Identification  number  and  Member  state  of  establishment,  are  filled  in automatically.

The rest Reporting Declarant information need to be filled in. As a minimum, the 'Name', 'Role' and 'City' fields need to be defined in 'Reporting Declarant tab'.

The Reporting Declarant role is a mandatory field. Here is an explanation for each option.

Importer for all goods - In case the Reporting Declarant acts as an importer for all goods, the 'Importer tab' in the header level and goods level is not required to be filled. Only the checkbox 'Declarant acts as an importer' is required to be checked in the Header level -&gt; Importer tab.

Indirect Customs Representative for all goods - In case the Reporting Declarant acts as an indirect customs representative for all goods, the 'Indirect customs representative tab' in the header level and goods level  is  not  required  to  be  filled.  Only  the  checkbox  'Declarant  acts  as  an  Indirect  Customs Representative' is required to be checked in the Header -&gt;Indirect Customs Representative tab.

Importer for some goods/ Indirect Customs Representative for some goods In case the Reporting Declarant acts as indirect customs representative for some goods and as an importer for some other goods, then the importer / indirect customs representative is required to be defined at each good level and the header importer &amp; indirect customs representative tabs are disabled.

Figure 35: Header - Reporting Declarant tab



## 4.4.1.2 Importer

If an importer is defined in Header level of CBAM Report, it will be applied for every good item. If the report includes multiple importers, they need to be defined separately for each good.

In case the Reporting Declarant is the importer for all goods, the checkbox 'Reporting Declarant acts as importer' could be checked to avoid filling in the same information again.

Figure 36: Declarant acts as importer Checkbox



However, if the importer is not the same as the Reporting Declarant, then the 'Add importer' button should be clicked to fill in importer's information.

Importer's details will be deleted from header level of CBAM Report if the 'Delete' button is clicked.

Figure 37: Header- Importer tab



## 4.4.1.3 Indirect customs representative

If an indirect customs representative is defined in Header level, it will be applied for every good item. If the report includes multiple indirect customs representatives, they need to be defined separately for each good.

In  case  the  Reporting  Declarant  is  the  importer's  indirect  customs  representative  for  all  goods,  the checkbox  'Reporting  Declarant  acts  as  indirect  customs  representative'  could  be  checked  to  avoid filling in the same information again.

Figure 38: Declarant acts as indirect customs representative checkbox



However, if the indirect customs representative is not the same as the Reporting Declarant, then the 'Add indirect customs representative' button should be clicked to fill in indirect customs representative 's information.

Indirect customs representative's details will be deleted from header if the 'Delete' button is clicked.

Figure 39: Header - Indirect customs representative Tab



## 4.4.1.4 Competent authority

Competent Authority reference number is automatically filled in based on the country the Reporting Declarant's EORI number belongs to.

In case it is not filled in automatically, please contact your respective National Competent Authority Service Desk.

## 4.4.1.5 Signatures

All fields of this tab are mandatory to be filled to allow report submission. Date of signature will be filled in by the system upon report submittal.

Figure 40: Header -Signatures tab



## 4.4.2 CBAM goods Level of the CBAM Report

By clicking on 'Goods imported', the user can add as many goods as desired or/and navigate through a list of all goods that have been added. Please note that at least one good must be declared and added in the report.

## 4.4.2.1 Addition of CBAM goods

The user can add a good by clicking the 'Add a good' button, either from the navigation tree on the left or using the button above the table.

Figure 41: Add good



## 4.4.2.2 Deletion of CBAM goods



The user can delete a good by clicking the delete icon in the action column in good's table.

Figure 42: Delete good



A confirmation message will appear to confirm the user action prior the good's deletion.

Figure 43: Delete good - Confirmation



## 4.4.2.3 Editing of CBAM goods

Once the user is in Editing mode in the report, the good can be edited either by clicking the respective good on the left navigation tree or by clicking the Edit icon on the good's table.

Figure 44: Edit good



## 4.4.2.4 Saving of CBAM goods

Please ensure to save each good prior to adding/editing other good by clicking the 'Save' button.

## 4.4.2.5 Validating of CBAM goods

The user shall validate separately every Good Imported along with its emissions. After completing the insertion of information, the "Validate" button in 'Action menu' must be clicked so that the validation is performed. Possible errors in the validation are shown with a red exclamation mark icon. In order to validate it, edit the respective good and click the Validate button on the Action Menu.

Figure 45: Non-validated good



When the 'Validate' button is clicked, validation errors are generated on the good's level and for each emission added.

Figure 46: Validate Good



In the navigation tree on the left the number of messages that need to be addressed are displayed.

Figure 47: Validate good - Navigation tree indication



Once the mandatory fields are filled in, the process will continue with business rules validation.

Validations and warnings are displayed in the Validation Results dialog (see Figure 104: Validations Results Dialog). All messages categorized under "Error Type" as "Errors" must be resolved before submitting the report. "Warnings" should be reviewed and addressed as needed.

Each validation and warning listed in the Validation Results dialog, provides guidance on resolving the issue:

- Message: Direction on how to resolve the issue. Using the action, you can view the full message on top of the dialog (see Figure 105: Validation Results - Action view).
- Pointer : Identifies the good and emission and field that triggered the error.
- Code : Refers to the Rule Code that generated the message, as defined in the published report structure (see 5.3 Report structure).

## 4.4.2.6 How to fill in the good

The following sections are describing how to fill in the details in each tab.

## 4.4.2.6.1 Importer

In the 'Importer' tab, the user can check the box appearing in case the Reporting Declarant acts as importer or click on the 'Add importer' button to add importer details. If an importer is defined in the Header level, ensure not to select an importer in good's level.

Figure 48: Good Level - Importer tab



If the user clicks the 'Add importer' button then he/she can add details of the importer on the appearing menu, where all fields marked with an asterisk are mandatory.

Figure 49: Good Level - Add Importer



If the user needs to delete an importer, he/she can click the 'Delete' button on the up-right corner of the menu.

## 4.4.2.6.2 Indirect customs representative

In the 'Indirect customs representative' tab, the user can check the box appearing in case the Reporting Declarant acts as indirect customs representative or click on the 'Add indirect customs representative' button to add indirect customs representative details.

Figure 50: Good's level - Indirect customs representative tab



If the user clicks the 'Add indirect customs representative' button then he/she can add details of the indirect customs representative on the appearing menu, where all fields marked with an asterisk are mandatory.

Figure 51: Good's level - Add Indirect customs representative



If the user needs to delete an indirect customs representative, he/she can click the 'Delete' button on the up-right corner of the menu.

## 4.4.2.6.3 Goods Imported

In 'Goods imported' tab, the user must fill in all the mandatory fields marked with an asterisk.

After adding the HS and the CN codes, the 'Description of goods' is automatically filled in.  Please note that a minimum of 2 characters are required in HS sub-heading code to allow the system to display the available CBAM goods.

Figure 52: Goods imported tab 1/3





Figure 53: Goods imported tab 2/3

Figure 54: Goods imported tab 3/3



Figure 55: HS sub-heading code selection



By clicking the 'Add new' button right below the 'Imported quantity per customs procedure' line, the user can add more than one quantity of goods imported in case the customs procedure differs from one import procedure to another import procedure.

Figure 56: Imported quantity per customs procedure



Please note that goods Imported total emissions, goods emissions per unit of product, goods Direct Emissions  and  goods  indirect  emissions  will  be  filled  in  automatically  based  on  the  emissions information to be added for each good.

In case the previous procedure is inward processing, the 51 (Inward processing procedure (suspension system)) and 54 (Inward processing (suspension system) in another Member State (without their being released for free circulation in that Member State)) codes are available. By clicking the 'Add new' button right below the 'Goods measure (per procedure)' line, the user can add more than one measure of goods. The checkbox 'Inward processing' that is present in this section has the following meaning:

- If  not  checked,  then  it  defines  the  quantity  of  the  CBAM  Good  that  is  released  for  free circulation as an unprocessed good.
- If checked, then it defines the quantity of the CBAM Good within processed goods (from Inward Processing) which are released for free circulation as a processed good .

Please note that it is possible to add until two instances of Goods measure per procedure to cover the above business cases.

Figure 57: Goods measure per procedure



Note: Starting from Q2-2024 reporting period, Net Mass should always be reported in Tonnes (Kg will no  longer  be  selectable).  Therefore,  for  all  goods  (except  electricity)  the  measurement  unit  will  be Tonnes.

## 4.4.2.6.4 Supplementary

In  'Supplementary'  tab,  the  user  can  add  information  about  the  good  in  the  textbox  or/and  add 'Supporting' documents by clicking the 'Add new' button.

Figure 58: Goods - Supplementary tab



By clicking the 'Add new' button, the user needs to fill in the fields marked with an asterisk as they are mandatory. If the user needs to delete the supporting document, he/she can do so by clicking the 'Delete' button on the upper right corner of the menu. Please note that the element 'URI' is no longer available for reporting periods Q3-2024, Q4-2024, Q1-2025,Q2-2025, Q3-2025, Q4-2025.

Figure 59: Goods - Add supporting documents



## 4.4.2.7 Duplicating of CBAM Goods

In cases where the emissions information is the same for different goods that are produced from the same installation, the 'Duplicate Good' feature can be used.



In Goods imported table, in the Actions column, the 'Duplicate Icon' can be used to duplicate the same contents of the good where the icon is clicked. 口

Figure 60: Goods - Duplicate Good



Once the new good has been selected, it can be edited as needed (see 4.4.2.3 Editing of CBAM goods).

## 4.4.3 Emissions level of CBAM report

For  each  CBAM  good  added  in  the  report,  its  respective  emissions  need  to  be  declared.  Multiple emissions can be added based on installation/country of production.

## 4.4.3.1 Addition of Emissions

Click the  'Add  an  emission'  button  the  left  side  of  the  screen  to  get  the  process  started.  For  each emission added, an incremental number will be shown automatically.



Figure 61: Add an emission



## 4.4.3.2 Deletion of Emissions

In order to delete an emission data elements group, click the next to emission sequence number. 盲

Figure 62: Delete emission.



## 4.4.3.3 Editing of Emissions

Emissions can be edited once the related good is in edit mode, see 4.4.2.3 Editing of CBAM good. In the left navigation tree, click the emission that needs to be edited. Please note that if 'Save' button is clicked from the 'Action menu' the good &amp; emission information is saved and the good will need to be reopened to continue with emissions editing.

Figure 63: Edit good



## 4.4.3.4 How to fill in the emissions

The following sections will describe each screen tab in detail.

## 4.4.3.4.1 Installations

In this tab the following information need to be defined: Country of production, Operator (Company that owns the installation), and Installation.

Operator &amp; Installation are optional elements, however it is advisable to define them per legislation.

Country  of  production  is  mandatory  to  be  defined  (it  should  match  the  country  of  installation establishment if defined).

Figure 64: Installation - Country of production



The company name that owns the installation (Operator) can be filled using one of the below methods

- a) Fill in directly in the report or
- b) Select an Operator from 'My Operators registry' (See section 4.1 My Operators Registry) or
- c) Select an Operator from the 'Operators of Third Country Installations Registry'. The Operator must have first shared at least one installation with the Declarant's EORI.

Click the 'Add New' button to proceed with the operator addition.

Figure 65: Emissions - Operator (The company name of the installation)



An operator can be selected from 'My operator's registry' by clicking the 'Search in My Operator Registry. In this case the fields are populated with the information available and can be edited if needed. See Figure 66: Emissions - Operator (Information filled in from My Operator Registry) or directly in the report.

Figure 66: Emissions - Operator (Information filled in from My Operator Registry) or directly in the report.



An operator can be selected from 'Operators of Third Country installations registry' by clicking the 'Search in Operators of Third Country Installation Registry'. Please note that the operators listed are the ones that:

- o The operator has shared an installation with Declarant's EORI;
- o An installation exists that is producing the good in the country of production defined in the report.

Figure 67: Operator selection from Operators of Third country Installations registry



In case an operator is selected from 'Operators of Third country Installation registry', the operator fields are populated with the information available and cannot be edited.

Figure 68: Emissions - Operator (Information filled in from Operators of Third Country Installation Registry)



The installation can be filled in using one of the below methods:

- a) Fill in directly in the report or
- b) Select an Installation from 'My Installations registry' (See section 4.2  Installations registry) or
- c) Select  an  Installation  from  the  'Operators  of  Third  Country  Installations  Registry'.  The Operator must have first shared at least one installation with the Declarant's EORI.

Click the 'Add New' button to proceed with the installation addition.

Figure 69: Emissions - Installation fields



An installation  can  be  selected  from  the  'My  Installations'  registry  by  clicking  the  'Search  in  My Installation registry'. Please note that the installation available to be selected are the ones that have been associated with the operator selected.

Installation

Figure 70: Emissions-Installation information filled in from Installations Registry or directly in the report



An installation can be selected from 'Operators of Third Country installations registry' by clicking the 'Search in Operators of Third Country Installation Registry'. Please note that the installation listed are the ones that:

- o The operator has shared an installation with Declarant's EORI;
- o An installation exists that is producing the good in the country of production defined in the report.

Figure 71: Installation selection from Operators of Third country Installations registry



In case an installation is selected from 'Operators of Third Country Installation registry', the installation fields are populated with the information available and cannot be edited.

Installation

Figure 72: Emissions -Installation information filled in from Operator of Third Country Installation Registry



## 4.4.3.4.2 Parameters

This screen varies based on the good selected. The information provided helps to justify the emissions calculated.

The first  section  is  related  to  the  production  methods  used  and  the  second  section  is  related  to  the emission qualifying parameters applicable for the production method selected.



Production Methods and Emission qualifying parameters are optional. However in case the emissions need to be obtained from O3CI , the production method must be selected.

When the 'Add new' button is clicked, based on the operator/installation selected the dialog in Figure 73 or  Figure 74 will be shown.

Figure 73: Production Method selection (without O3CI emissions selected)



Figure 74: Production Method selection from Operators of Third Country Installations registry



Choose a production method from the list and the relevant entry will be added in the 'Parameters' tab. If  you select an O3CI production method, the "Emissions" tab will be automatically populated. See 4.4.3.4.3Emissions  for  further  information  and  the  relevant  process  in  4.4.3.5  Process  -  CBAM Transitional Integration with 'Operator of Third Country Installation Registry' (O3CI).

Figure 75: Selected Production Method from Operators of Third Country Installation registry



Once a production method is selected, in emissions qualifying parameters section click the 'Add new' button to add the default qualifying parameters per production method (if available). When an emission qualifying parameter is added, its value needs to be filled in. The value can either be numeric or text or percentage. The type of the value is visible in the description.

Figure 76: Emissions qualifying parameters



Click the 'Add new' button to add other qualifying parameters.

Figure 77: Add new qualifying parameters



In reporting periods Q423, Q124 &amp; Q224 the indirect emissions qualifying parameters are not required to be filled in when electricity is selected as good.

Figure 78: Indirect Emissions qualifying parameters (applicable for Q423, Q124 &amp; Q224 reporting periods)



Figure 79: Direct Emission qualifying parameters (applicable for Q423, Q124 &amp; Q224 reporting periods



Starting from reporting periods Q324, the indirect emissions qualifying parameters section is no longer visible,  and  instead  the  'Direct  Emissions  Qualifying  Parameters'  is  now  renamed  to  'Emissions Qualifying Parameters' (see Figure 80).

Figure 80: Production Method and Emissions Qualifying Parameters



## 4.4.3.4.3 Emissions

The emissions tab will be differentiated when Electricity is selected in good's section. Ensure to fill in all mandatory fields.

Figure 81: Emissions tab - Applicable to Electricity good (Reporting Periods Q4-2023, Q1-2024, Q2-2024)



Figure  82:  Emissions  tab  -  Applicable  to  Electricity  good  (Reporting  periods  Q3-2024  and forward)



For all goods except electricity, both direct and indirect embedded emissions need to be defined. Based on type of determination, some fields may become read only.

Figure 83: Emissions tab- Applicable for all goods except electricity



Please note that default values for specific direct embedded emissions and specific indirect embedded emissions are displayed on screen (see Figure 84: Emissions-Default Values) based on good selected and provided the option 'Other  methods  including  default values' is selected in 'Type  of Determination'. (Note that the option to reporting default values is applicable only for reporting periods Q4-2023, Q1-2024, Q2-2024).

Figure 84: Emissions-Default Values



For imports as from 1 July 2024, reporting declarants are required to report actual emissions for each CBAM good imported into the EU (exception: Good is electricity). Reporting declarants are liable and may be subject to penalties where they fail to comply with the CBAM reporting obligation and where they have not taken the necessary steps to comply with the obligation to submit a complete and accurate CBAM report, following the correction procedure. Reporting declarants must undertake all possible efforts to obtain actual emission data from their supplier(s) or producer(s) of CBAM goods.

Where reporting declarants eventually fail to get data on actual emissions, reporting declarants shall select, in the field "Type of determination", the option "Actual data not available". This option exists for

both direct and indirect embedded emissions. Note that if this option is chosen, the CBAM report will be considered incorrect/incomplete.

If the option "Actual data not available" is chosen, reporting declarants should also follow these steps:

1. Use  the  "Additional  Information"  field  to  provide  justifications  on  why  the  actual emissions data is missing.
2. In the tab 'Supplementary', upload supporting documents attesting unsuccessful efforts and steps taken to obtain data from suppliers and/or producers."

Note that where the option "Actual data not available" is chosen, subsequent fields in the Emissions tab will become non-editable (i.e. for direct embedded emissions: the field "type of reporting methodology"; for indirect embedded emissions: the fields "Source of emission factor" and "Source of electricity") and numeric fields will be automatically filled with "0". See Figure 85: Emissions - Actual data not available.

Figure 85: Emissions - Actual data not available



Please  note  that  the  summation  of  all  Goods  measure  (produced)  net  mass  or  supplementary  units defined in emissions, should be the same value as the one declared in Goods level.

In case an O3CI Operator, Installation and Production Method are selected, the emissions are filled in automatically and are shown as read only on screen. See figures Figure 86 &amp; Figure 87. Please note that the declarant remains liable for these values. If you disagree with the provided emissions data, please remove the selected third-country operator and installation.

Figure 86: Emissions filled in from O3CI (all goods except electricity)



Figure 87: Emissions filled in from O3CI (good electricity)



- 4.4.3.4.4 Indirect Embedded  Emissions:  Distinction of the options available  in  the  CBAM Transitional Registry

Default values made available and published by the Commission ( please note that this option is only possible for Q4/23, Q1/24 and Q2/24 )

- -From the element  'Type of determination',  choose the option  'Estimated  values including  default values made available and published by the commission'; and
- -From the element 'Source of electricity', choose the applicable option to your case.

Once these options are selected, the default value for specific indirect embedded emissions is displayed in  the  registry.  It  is  expressed  in  t  CO2/unit  of  goods.  After  manually  entering  the  value  in  the corresponding field, it is automatically multiplied with the amount of goods (tonnes) to arrive at the total indirect emissions (tonnes).

Average emission factor of the country of origin electricity grid (based on IEA data provided by the Commission)

- -From the element 'Type of determination', choose the option 'Actual data'.

-From the element 'Source of emission factor', choose 'Commission based on IEA data'.

-From the element 'Source of electricity', choose received from the grid; and

-In the element 'Electricity consumed', provide the correct amount (expressed in MWh/unit of goods) of electricity consumed.

Once these steps are taken, the emission factor (expressed in t CO2/MWh) is displayed automatically in the registry. This value is automatically multiplied by the actual amount of electricity consumed in the production of a good (MWh/unit of goods). The result is a value of t CO2/unit of goods, which is automatically multiplied with the amount of goods (tonnes) to arrive at the total indirect emissions (tonnes).

## Any other emission factor of the country of origin electricity grid based on publicly available data

-From the element 'Type of determination', choose the option 'Actual data'.

-From the element 'Source of emission factor', choose 'Other';

-From the element 'Source of electricity', choose 'Received from the grid'.

-Under the elements 'Other source indication' and 'Source of emission factor value' clearly reference the publicly available and reliable source you are referring to (via explanation and a link to the dataset, for example); and

-In the element 'Electricity consumed', provide the correct amount (expressed in MWh/unit of goods) of electricity consumed; and

-In the element 'Emission factor', provide the other emission factor (expressed in t CO2/MWh).

Once these steps are taken, the emission factor is automatically multiplied by the actual amount of electricity consumed in the production of a good (MWh/unit of goods). The result is a value of t CO2/unit of goods, which is automatically multiplied with the amount of goods (tonnes) to arrive at the total indirect emissions (tonnes).

## Actual emission factor for indirect emissions,

-From the element 'Type of determination', choose the option 'Actual data'.

-From the element 'Source of emission factor', choose 'Other'.

-From the element 'Source of electricity', choose either 'Direct technical link to electricity generator' or the option '(Bilateral) power purchase agreement' (as applicable).

-Under the elements 'Other source indication' and 'Source of emission factor value' clearly specify and explain how exactly you fulfil the concrete requirements of the selected option and how the emission factor  value  has  been  calculated  (you  are  also  encouraged  to  upload  supporting  documents  in  the 'Supplementary' tab to do this);

-In the element 'Electricity consumed', provide the correct amount (expressed in MWh/unit of goods) of electricity consumed; and

-In the element 'Emission factor', provide the actual emission factor (expressed in t CO2/MWh).

Please note that to use the actual emission factor for indirect emissions, one of two conditions have to be fulfilled: either there is a direct technical link between the installation producing the goods and the generated electricity; or there is a Power Purchase Agreement between the installation and the electricity producer. In both cases, the electricity needs to be used to produce the relevant goods. If one of the conditions is fulfilled and the above-mentioned steps are taken, the emission factor is automatically

multiplied by the actual amount of electricity consumed in the production of a good (MWh/unit of goods). The result is a value of t CO2/unit of good, which is automatically multiplied with the amount of goods (tonnes) to arrive at the total indirect emissions (tonnes).

## 4.4.3.4.5 Carbon price due

This section is optional. In case CO2 emissions are due in other countries, in this section all relevant information will be required to be filled in. The most important information to be provided is the national legal basis of the carbon pricing scheme including the implementing provisions and their respective sources.

Click the 'Add New' button to start filling in the details. Please note that multiple sections can be added based on the country/type of instrument combinations in reporting periods Q4-2023, Q1-2024, Q2-2024.

Figure 88: Carbon price due tab



For each country/type of instrument combination, multiple goods covered under carbon price due can be defined by clicking the 'Add New' button below the 'Goods covered under carbon price due' label (only applicable in Q4-2023, Q1-2024, Q2-2024 reporting periods).

Carbon price due

Add new

Figure 89: Carbon price due - Details (Reporting Period Q4-2023, Q1-2024, Q2-2024)



Starting from Q3 2024, the 'Carbon Price Due' tab has been simplified, as illustrated in Figure 90. Only one entry can be added, which will correspond to the actual product. Note that the CN Code is prefilled with the relevant code for which the carbon price is applicable.

Figure 90 : Carbon price due details (Q3-2024 reporting period and forward)



Please fill in all mandatory fields. Regarding exchange rates, these will be defined by the European Commission.

## 4.4.3.4.6 Supplementary

In this tab additional information for emissions calculations can be added and all supporting documents. Multiple  additional  information  &amp;  supporting  documents  can  be  added  by  clicking  the  'Add  new' button.

Figure 91: Supplementary tab



For each emissions supporting document, the type and Reference number are mandatory to be filled in. A document can be added either by using the 'Choose File' or by dragging and dropping. In order to delete an uploaded file, the 'Delete' button needs to be clicked. Please note that the element 'URI' is no longer available for reporting periods Q3-2024, Q4-2024, Q1-2025,Q2-2025, Q3-2025, Q4-2025.

Figure 92: Emissions - Supplementary - Supporting Documents



## 4.4.3.5 Process - CBAM Transitional Integration with 'Operator of Third Country Installation Registry' (O3CI).

To use the O3CI emissions data, both the Operator and the respective installation must be registered in O3CI. Follow these steps to incorporate emissions data into your reporting:

1. Contact the Operator/Installation : Reach out to the Operator or Installation where the specific good was produced.
2. Verify Registration : Ensure the Operator has registered the installation that is producing the good in O3CI and disclosed the emissions data linked to the Declarant's EORI.

3. Add the Good :  Include  the  good  in  your  quarterly  report  (see  Section  4.4.2.1  Addition  of CBAM goods) and provide all required details.
4. Add  the  Emissions :  Enter  the  emission  data  in  your  quarterly  report  (see  Section  4.4.3.1 Addition of Emissions).
5. Specify Country of Production : Select the country where the good was produced.
6. Select the Operator : Choose the Operator from the 'Operator of Third Country Installation Registry' (see Section 4.4.3.4.1 Installations).
7. Select the Installation : Pick the relevant installation from the same registry (Section 4.4.3.4.1 Installations).
8. Choose the Production Method : Indicate the production method used (see Section 4.4.3.4.2 Parameters).
9. Enter Goods Measure : Provide the net mass or supplementary units of the produced goods.

Once these steps are completed, the emissions data will automatically populate in the emissions tab and be marked as read-only. The Declarant remains responsible for the accuracy of these values.

If you disagree with the provided emissions data, click the 'Edit Third-Country Operators Emissions Data' button at the top of the Emissions tab. This will clear the emissions data while retaining the Operator and Installation information.

Figure 93: Emission tab filled in with O3CI emissions information



## 4.5 QUARTERLY REPORT ACTIONS

In this section all the processes related with the report management that a CBAM Reporting Declarant can perform will be listed in detail.

## 4.5.1 Creation of a report

In Home Page, click 'My quarterly reports' and then click the 'Create' button for the reporting period where a report needs to be created.

The 'Create' button is visible only during the allowable reporting deadline. In the case a report needs to be created after the deadline expiration, please follow the process 4.5.11 Request to submit a report with a delay.

Figure 94: My Quarterly Reports - Create Report



## 4.5.2 Uploading of a report

Instead of using the user interface to fill in the report fields, an XML can be uploaded (in ZIP format containing all relevant attachments). The XML should be structured according to the XSD format listed in Annex 5.1 CBAM Report (XSD). Sample ZIP files are also included in 5.2 Sample ZIP file. The report structure is also provided in excel format along with the code lists in 5.3 Report structure .

Click the 'Upload' button of the report that needs to be filled in 'My Quarterly Reports'.

Figure 95: My Quarterly Reports - Upload a report



In the upload ZIP dialog, you can choose the file to be uploaded or you can drag &amp; drop the file.

Figure 96: Upload ZIP



The 'Upload' button is visible only for quarterly reports that their associated reporting period deadline has not expired. In case a report needs to be created after the deadline expiration, please follow the process 4.5.11 Request to submit a report with a delay.

Please note that once a report is uploaded it is not considered submitted. Please ensure to address any validation errors first.

When an XML is uploaded it is validated as follows:

- a) Codes used should be valid according to the published code list (see 5.3 Report structure ) where applicable.
- b) Fields that are mandatory need to be filled in.
- c) In addition, in the following business cases:
- Selected good is any good except electricity &amp; Indirect Emissions-&gt;Type of Determination ='Actual' &amp; Indirect Emissions-&gt; Source of emission factor='Commission based on IEA'
- Selected good is electricity &amp; Direct Emissions-&gt;Type of Determination = 'Default' &amp; Direct Emissions-&gt; Source of emission factor='Commission based on IEA'

The declarant is informed on the correct emission factor that should be used since the emission factors based on IEA are not publicly available.

Please note the 'Upload' button is also available in reports in 'Under Amendment' state. The existing report data will be deleted and replaced with the new data from XML if the XML to be uploaded is successfully validated.

## 4.5.3 Saving a draft report

Once a report has been created, the 'Save as draft' button needs to be clicked in order to save a draft Report.

Additional validation rules may be generated during this process; however, these can be addressed at a later stage and before report submission.

Save as draft can be used as often as needed to save the contents of the report under preparation.

Figure 97: Save a draft report



## 4.5.4 Deletion of a draft report

A draft report can be deleted. The 'Delete' button is available when the saved report is opened in view mode.

Figure 98: Delete a draft report



A confirmation dialog will appear when the 'Delete' button is clicked. The report will be deleted when the 'OK' button is clicked.

Figure 99: Delete draft report - Confirmation message



## 4.5.5 Editing of a report

Editing of a report is permitted when the report is in one of the following states.

- Draft
- Under Amendment
- Under Correction

Figure 100: Edit a report



In my quarterly reports page, click on the hyperlink in Report Column to open the report and then click the 'Edit' button.

Figure 101: Edit a report



## 4.5.6 Validation of a report

The report can be validated by clicking the 'Validate' button located in the Actions Menu.

Figure 102: Validate report



In case validation errors exist that are related with the header, (see Figure 103: Validate Report - Errors display),  they  will  appear  in  the  related  tab.  In  addition,  the  total  number  of  errors  that  need  to  be addressed are shown on the left report navigation tree.  Goods' level errors will need to be addressed at each individual good. Please follow the process 4.4.2.5 Validating of CBAM goods.

Figure 103: Validate Report - Errors display



Once the mandatory fields are filled in, the process will continue with business rules validation.

Validations and warnings are displayed in the Validation Results dialog (see Figure 104: Validations Results Dialog). All messages categorized under "Error Type" as "Errors" must be resolved before submitting the report. "Warnings" should be reviewed and addressed as needed.

Each validation and warning listed in the Validation Results dialog, provides guidance on resolving the issue:

- Message: Direction on how to resolve the issue. Using the action, you can view the full message on top of the dialog (see Figure 105: Validation Results - Action view).
- Pointer : Identifies the good and emission and field that triggered the error.
- Code : Refers to the Rule Code that generated the message, as defined in the published report structure (see 5.3 Report structure).

Figure 104: Validations Results Dialog



.

Figure 105: Validation Results - Action view



## 4.5.7 Submission of a report

Once the report is prepared, the CBAM  Reporting Declarant needs to click the 'Submit' button to officially submit the report to the CBAM Registry. CBAM System will run the validations and if no errors are found:

- a) The report state changes to SUBMITTED.
- b) The Report gets a Report ID assigned, which is the primary identified for the whole lifecycle of the report and will not be changed.

Figure 106: Submit report



In case of validation errors, the CBAM Reporting Declarant will need to edit the draft report and perform corrections as needed to clear the validations error messages and click the 'Submit' button again.

Prior to submission completion, the dialog shown in Figure 107: Modifications confirmation will be displayed. In case you are not planning to make modifications after the report submission, select the 'No' choice. In any case, modifications are allowed in all cases if they will be performed within the allowable modification period.

Figure 107: Modifications confirmation



Please note that a report needs to be submitted within the submission due date. In case the submission period closes and the CBAM Reporting Declarant did not submit the report, a request for delayed report submission needs to be issued. See the 4.5.11 Request to submit a report with a delay process.

## 4.5.8 Viewing of a report

A report can be viewed by clicking on the 'Preview' button on the Actions Menu.

Figure 108: View report



## 4.5.9 Amendment of a report

When  a  report  is  in  SUBMITTED  state  and  provided  the  associated  quarterly  report  modification deadline has not expired, a CBAM Reporting Declarant can perform changes to a report and resubmit it, once the validation process completes successfully.

To amend a report, locate the report with the status SUBMITTED and click the 'Amend' button.

Figure 109: Amend a report



The Report state will change to UNDER AMENDMENT. Changes in the report can be saved using the 'Save as Draft' button. The 'Upload' button is also available in 'My quarterly reports' view in case the report needs to be modified using the XML Upload (see 4.5.2 Uploading of a report).

Once the changes are completed, the 'Submit' button will need to be clicked.

In case of validation errors, the CBAM Reporting Declarant will need to edit the report and perform corrections as needed to clear the validations error messages and click the 'Submit' button again.

The status will change to SUBMITTED again until the report state changes to REGISTERED once the associated modification period deadline expires. Once a report is in REGISTERED state, corrections are allowed upon NCA's approval. Once NCA approves the request, the report state will change to "Under Correction" and the report corrections can be performed within 30days.

Please keep in mind that the amended report can only be submitted within the official deadline for report modification. In case the modification period closes and the CBAM Reporting Declarant did not submit the amended report, a request for delayed report submission needs to be issued. See the 4.5.11 Request to submit a report with a delay process.

## 4.5.10 Invalidation of a report

When a report is in SUBMITTED state, the report can be invalidated to allow a new report for the same reporting period to be created.

Figure 110: Invalidate a report



The button 'Invalidate' needs to be clicked. Upon confirming the action by clicking the 'OK' button, the report state will change to INVALIDATED.

Figure 111: Invalidate report confirmation message



## 4.5.11 Request to submit a report with a delay

The user can request to submit a report with a delay by clicking on the 'Request delayed submission' button. Please note once clicked, the button will not be available during the delayed submission period.

Figure 112: Request delayed submission



In the dialog that will be shown in Figure 113: Request delayed submission dialog, the reason for the request  of  the  delayed  submission  needs  to  be  provided.  In  case  NCA  has  approved  /requested  the

delayed submission, the NCA's reference number (e.g. email reference number or Request ID number ) needs to be provided as well (see Figure 114). Please note that the request due to a technical error is no longer available. In case you experience a technical error, please contact your respective NCA.

Figure 113: Request delayed submission dialog



Figure 114: Request delayed submission -Requested by NCA



Submission  and  modification  periods  for  delayed  submissions  varies  based  on  Table  7:  Delayed Submission - Deadlines.

Table 7: Delayed Submission - Deadlines



| Requested by| Submission| Modification |
|----------------|--------------|---------------------------------------------------------------------------|
| NCA| 30 days| 30 days or end of reporting period modification day whichever is greater. |

## 4.5.12 Download PDF

The button 'Download PDF' is available for all states. When the 'Download PDF' button is clicked, a PDF version of the report  is  created  asynchronously  which  is  saved  in  your  local  disk  drive  when prepared.

Figure 115: Download PDF



When the 'Download PDF' button is clicked, an information message appears. It is important not to close the browser tab during the PDF preparation process (see Figure 116 Download PDF Information message)

Figure 116: Download PDF Information message



When  the  PDF  file  is  prepared  the  following  message  appears  and  the  file  can  be  located  in  the downloads folder.

## 4.5.13 History

The 'History' button allows the user to see the history of the report.

Figure 118: History



Once the history button is clicked, the history view will include the actions performed, the associated report state along with a timestamp on when the action was performed.

Figure 119: History Details



Figure 117: Download PDF - File downloaded message



## 4.5.14 Duplicating a report

On the Home Page, click 'My quarterly reports' and then click the 'Create using Previous Report' button for the reporting period where a report needs to be created (see Figure 120: Create using Previous Report).

The 'Create using Previous Report' button is visible only during the allowable reporting deadline. When a report needs to be created after the deadline expiration, please follow the process 4.5.11 Request to submit a report with a delay. In addition, a previous report needs to be available that can be utilized. Previous reports in 'Draft' or 'Invalidated' state cannot be duplicated.

Figure 120: Create using Previous Report



The 'Duplicate Report from' dialog (see Figure 121: Duplicate report from), lists all previous quarters where a report was submitted. Previous reports in 'Draft' or 'Invalidated' state cannot be used for duplication.

Figure 121: Duplicate report from



Once the required reporting period is selected, the 'OK' is enabled and available to be clicked. (see Figure 122: Duplicate Report Form Selected).

Figure 122: Duplicate Report Form Selected



A confirmation dialog box will appear (see Figure 123: Duplicate Report - Confirmation dialog), click 'OK' to allow the creation of the report based on the previous report selected or click 'Cancel' to stop the process.

Figure 123: Duplicate Report - Confirmation dialog



When the duplicate report process is completed, a Draft report will be available in 'My quarterly reports' for the specific quarter. The report can now be edited per 4.5.5 Editing of a report.

When a Declarant duplicates an earlier report from Q42023 or Q12024 and the net mass was reported in kgs, the net mass values should be corrected manually to tonnes since in Q22024 reporting period and forward, kgs are no longer available.



## Please note that :

- a) Attachments in Goods or in Emissions are not duplicated.
- b) The Header-&gt;Signature tab needs to be filled in again.

## 4.6 MY IMPORTERS

In 'My importers' menu, accessed from Home Page or through the Navigation Bar, submitted CBAM reports goods data can be compared with the corresponding goods data from the associated customs declaration.

Figure 124: My Importers - Starting point



Once the menu is selected, the list of importers included in Submitted and Registered reports (does not include draft reports) are listed.

Figure 125: List of my importers



The list can be filtered either by the importer Identifier or the Importer Name. When the importer of ViewImporterProfile interest  is  located,  the  'View  Importer  Profile'  button can  be  clicked  to  view  the Importer's profile along with the quantity comparison of goods declared in the CBAM report vs goods in the associated Customs declarations.

Importer profile is obtained from CRS.

Figure 126: Importer Profile



The list of goods imported per importer, provides the comparison of CBAM Declared quantity and Customs declared quantity. In case multiple procedures are associated to review the discrepancies per procedure, you can click on the arrow to review the discrepancies per procedure.



Figure 127: Importer - Goods Imported



## 4.7 MY PROFILE INFORMATION

Information shown in 'My profile information' is obtained from UUM&amp;DS &amp; CRS and are displayed as read only fields. In case the information is not accurate and needs to be corrected, please contact your NCA Authority.

Figure 128: My profile information



Contact details can be added by editing the following section in 'My profile information'. Once all three fields (Contact person full name, phone, email address) have been populated, click on the 'Save' button. Details will be prefilled with CRS data (when available), however please ensure to update them with the latest contact information.

Figure 129: My Profile - Contact Details



## 4.8 PREFERENCES

The "Preferences" menu is positioned in the upper right corner. Currently, in the preferences section, the following options are available:

- a) Define your preferred language;
- b) Set the default email for communication;

- c) Enable/disable the transmission of CBAM Registry notifications to the email set as default for communications.

In future releases, the "Preferences" screen will undergo further enhancements, incorporating additional attributes related to user preferences.

Figure 130: Preferences menu



Click the 'Edit' button to fill in the required information.

Figure 131: Preferences



The email field can be populated using either the CRS email (if available), My Profile email (if defined), UUM&amp;DS email, or a user-defined email. If an email isn't associated with a particular option, that option will be greyed out. The email field becomes editable when the 'User Defined' option is selected. Please note, an email address is mandatory in all cases. Saving of preferences is not permitted without entering a valid email address.

Figure 132: Edit Preferences



To save the  updated  information,  click  the  'OK'  button.  When  the  'Cancel'  button  is  clicked,  no changes will be saved.

## 4.9 INFORMATION

Clicking on "Information" allows the Reporting Declarant access to several resources. These include the NCA's contact information, a list of CBAM goods, and CBAM legislation.

Figure 133: Information



## 4.9.1 NCA

In  "NCΑ" page, the user can see a list of all National Competent Authorities offices pre-sorted by 'Office Reference Number'. The user can also sort the list by 'Country' or 'Created'.

## NCA Management



Figure 134: Information-NCA



By clicking on the View icon in 'Actions' column, the user can see information about each NCA office organised in different tabs as shown in Figure 135. On the upper right corner of the shown menu,

the user can click on the Expand all icon to expand all tabs or the Collapse all icon to collapse all tabs at once. Collapse all Expand all

Figure 135: Information - NCA Details



## 4.9.2 CBAM Goods

Upon clicking the "CBAM Goods" page, you'll see a list of goods sectors per Figure 136.

Figure 136: Goods Sector Level



To expand a sector and view its associated aggregated goods categories, click on the plus icon next to it. +

Figure 137: Aggregated goods category Level



When you select a specific aggregated goods category, a list of goods belonging to that category will appear on the right side of the screen per Figure 138.

Figure 138: Goods code level



When the list of goods is displayed, you have the option to sort it by 'CN Code', 'HS Description', or 'CN Code Description'. Additionally, you can filter the list using the 'CN Code' field. The list will start filtering as soon as you begin typing.

Figure 139: Filtered CBAM Goods on CN Code



## 4.9.3 Legislation

Clicking  on  "Legislation"  redirects  the  user  to  the CBAM Legislation webpage , where all  relevant legislative documents can be viewed.

## 4.10 REQUESTS

The 'Requests' home page section is divided into two distinct lists.

- Incoming Requests: These are requests sent from the NCA to the Reporting Declarant. The Reporting Declarant can employ the filter options to review outstanding requests for which no response has been sent by the Declarant. When a new request is created from NCA, an email will be sent to the Declarant's email address, as specified in Preferences.
- Outgoing Requests: These are requests initiated by the Reporting Declarant and sent to the NCA. The Reporting Declarant can use the available filter options to track requests awaiting a response from the NCA .

In both lists, the status field receives one of the below values:

- Received: Initial status, when the request has been created;
- Answered: When a reply has been provided;
- Cancelled: Is set by the requestor when the request is cancelled;
- Rejected: Is set by the recipient of the request, when the request is declined.

Figure 140: Request



## 4.10.1 Viewing of incoming requests

You can sort and/or filter the list of incoming requests based on your preferences. To view requests that are waiting for your response, apply the Status filter and select the 'Received' status

Figure 141: Incoming requests



## 4.10.2 Replying to an incoming request

First view the request by clicking on the "eye" icon and then click on 'Reply' button.

Figure 142: Reply to a Request



At a minimum, you must complete the 'Response Title' and 'Response Message' fields in order to enable the 'Send' button. Due to limitation of length in the reply, please complement your response with an attachment.

Optionally, attachments can be included in the response. You can add files by either selecting them from your device or using the drag-and-drop feature.

Figure 143: Reply to a request -Details



In the references (optional) section, document references can be added. Fill in the Document reference and the additional information and click the 'Add' button.

Figure 144: Notifications - References



After filling in the required fields, click the "Send" button to send the request to the NCA. The request will appear in the 'Incoming Requests' list, where its status will be updated to 'Answered'.

## 4.10.3 Decline an incoming request

To reject an incoming request, click the "Decline" button and provide an appropriate justification in the designated field. Once completed, the request's status will be updated to "Rejected".

Figure 145: Decline Request



## 4.10.4 Create a request

To create a request, simply click the 'Create Request' button which is located on the upper right corner of both Incoming and Outgoing lists (see Figure 140).

Figure 146: Create Request



In the displayed dialog (See Figure 146), it is essential to complete all mandatory fields for the "Create Request" button to become active .

As a declarant, only the NCA type is available in the "To (Recipient) Organization" field. Once chosen, the member state of NCA will be filled in automatically.

In the "Title" field, provide a brief summary and select the 'Type of request'. In the "Your Message" field, provide a detailed description of the request.

After determining the priority of your message, input the expected response due date. This will help in organizing and addressing your request promptly.

On an optional basis, you can utilise the following fields:

- CBAM Reports: Here,  you  can  input  the  report  ID(s)  you  wish  to  reference  in  your  NCA request. Upon entering the Report ID, the "Add" button will become active, allowing you to include the reference in your request.

Figure 147: CBAM Reports (Optional)



- References :  You can add any relevant references. The "Add" button becomes enabled once both "Document Reference" and "Additional Information" fields are filled in .
- Attachments : To attach a document to the request, you can either use the "Choose File" option or simply drag and drop the file if needed .

Figure 148: References



Figure 149: Attachments



Once you have completed the form, you can click the 'Create Request' button. The request will be visible in the 'Outgoing list' with the status 'Received'.

For assistance with technical issues, please consult your respective NCA for guidance.

## 4.10.5 Cancel a request

If a mistake has been made or the request is no longer relevant, you can cancel a request as long as a response has not been provided. Simply access the request in question and click on the "Cancel" button.

Figure 150: Cancel a request



## 4.10.6 View outgoing requests

The list of outgoing requests can be sorted and/or filtered according to your needs. To view requests that are awaiting NCA's response, use the "Status" filter and select the "Received" status.



Figure 151: Outgoing requests



## 4.11 NOTIFICATIONS

On the upper right corner of the home page, the icon indicates the number of unread notifications. When the unread notifications exceed 10, the exact number is not displayed. 10+

Figure 152: Home Page-Notifications icon



reveals a list of the latest unread notifications, as shown below in Figure 153.



Clicking the icon



22/11/2023

Figure 153: List of latest unread notifications

From this view you can:

- a) Select to view one notification. Clicking on a notification marks it as read and displayed the notifications' details.
- b) View the full notifications' list . Click the button 'View all' on the upper right side to access all notifications.

Figure 154: View notification



The notifications list (see Figure 157), provides sorting and filtering options for Notification Type and Status. Advanced filtering is available on the upper right side of the notification table that allows for the following filtering criteria:

Filterby:



- Filter by Report ID

or

- Filter for a range of dates in Date Received. The default date range is set to one month.

Figure 156: Notification - Reception date to filter



Please note: When applying criteria, either specify a Report ID or set the Date Received range to a maximum of one month.

Figure 157: Notification List



Clicking the view icon in actions column, lets you view the notification (see Figure 154 ) and marks it as read. Use the 'Back' button to return to the list of notifications.

# 5 ANNEX

## 5.1 CBAM REPORT (XSD)

The following zip file contains:

- a) QReport\_ver23.00.xsd which describes the full structure of the CBAM quarterly report in an XSD file format.
- b) Stypes\_ver23.00.xsd which describes the data types used.
- c) XML Guidance document.

CBAM Quarterly Report structure XSD and 'stypes.xsd' (ZIP format)

## 5.2 SAMPLE ZIP FILES

The ZIP file must consist of:

- The  quarterly  report  XML  file  created  which  must  be  based  on  the  CBAM  report  XSD  as provided in section:5.1 CBAM Report (XSD).
- The set of documents to be provided as binary attachments (acceptable file formats are only DOC, DOCX, XLS, XLSX, PDF, JPEG).

The final constructed file must be uploaded to the CBAM Declarant portal by using the 'Upload' button as described in 4.5.2 Uploading of a report. Once the Declarant selects to create a quarterly report by uploading a file, the system fills the quarterly report's data according to the data and binary attachments retrieved from the particular ZIP file.

The CBAM declarant can crosscheck and confirm the correct report filling to the Declarant portal UI according to the data uploaded. Potential errors are presented by the system and the CBAM declarant has to correct them in order the report to be successfully validated and submitted to the system.

Here are some sample files:

CBAM Quarterly Report sample file for representatives CBAM Quarterly Report sample file for importers

## 5.3 REPORT STRUCTURE

The following file describes in detail the report structure. In addition, it contains the code lists utilized and the XML elements that correspond for each field.

CBAM Quarterly Report structure (XLS format)