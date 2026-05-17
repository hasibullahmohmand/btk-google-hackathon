# User Interface Manual CBAM - Declarant Portal

Date: 10/12/2024

Status: Submitted for acceptance (SfA)

Version: 2.00 EN

Author: SOFT-DEV

Approved by: DG TAXUD

Reference number: CBAM-UMN-DECL

Public: DG TAXUD external

Confidentiality: Publicly available (PA)

## Document control information

| Property | Value|
|--------------------------|------------------------------------------------------------------|
| Title | User Interface Manual|
| Subtitle | CBAM - Declarant Portal |
| Author| SOFT-DEV |
| Project owner | Head of Unit of DG TAXUD Unit C2 CBAM, Energy and Green Taxation |
| Solution provider | DG TAXUD Unit B2 Architecture &Digital Operations|
| DG TAXUD Project Manager | DG TAXUD Unit B2 Architecture &Digital Operations|
| Version | 2.00 EN |
| Confidentiality | Publicly available (PA) |
| Date | 10/12/2024 |

## Contract information

| Property| Value |
|--------------------|-------------------|
| Framework Contract | TAXUD/2021/CC/162 |
| Specific Contract | SC16|

## Document history

The document author is authorised to make the following types of changes to the document without requiring that the document be re-approved:

- Editorial, formatting, and spelling;
- Clarification.

To request a change to this document, contact the document author or project owner.

Changes to this document are summarised in the table in reverse chronological order (latest version first).

|Version | Date| Description | Action 1| Section|
|-----------|------------|-----------------------------------------------------------------------------|------------|---------------|
|2 | 10/12/2024 | Updated as per reviewer's comments. Document submitted for acceptance (SfA) | R | Where needed. |
|1.1 | 21/11/2024 | Updated for release 2.2. Document submitted for review (SfR)| I/R | Where needed. |
|1 | 11/10/2024 | Updated as per reviewer's comments. Document submitted for acceptance (SfA) | R | Where needed. |
|0.1 | 27/09/2024 | Document submitted for review (SfR)| I | All|
|0.01 | 13/09/2024 | Document submitted for information (SfI) | I | All|

1  Action: I=Insert R=Replace

## Configuration management: document location

The latest accepted version of this controlled document is stored on: DG TAXUD Sharepoint.

# 1 INTRODUCTION

## 1.1 DOCUMENT PURPOSE

The purpose of this document is to explain to the end-users of the CBAM declarant portal how to use it and benefit from its added value. This document provides help to get general information on the system, how to get started with the system and finally how to use the system with specific roles.

## 1.2 TARGET AUDIENCE

The target audience for this document includes:

- Importers of CBAM goods into the EU or their indirect representatives (CBAM declarants) 1 ;
- Directorate-General Taxation and Customs Union (DG TAXUD)'s Project team;
- DG TAXUD Unit C2 CBAM, Energy and Green Taxation;
- DG TAXUD Unit B3 Customs Systems;
- Directorate General for Informatics (DIGIT);
- Directorate-General for Climate Action (DG CLIMA);
- EU Member states and their National Competent Authorities;
- SOFT-DEV Project team;
- QA5 Project team;
- Operational teams.

## 1.3 SCOPE

The scope of this document is to provide directions to CBAM Declarants on the effective utilisation of the CBAM Declarant Portal. The features described comply with CBAM release 2.2.

## 1.4 STRUCTURE

This document is organised as follows:

- Chapter 1 - Introduction : describes the scope and the objectives of the document;
- Chapter 2 - General Information : provides the practical and theoretical details for the topics covered in the document;
- Chapter 3 - Getting Started : details how to access the portal and introduces the generic User Interface features and basic system functions;

## 1.5 REFERENCE DOCUMENTS

The table below lists the documents that are referred to in the current document.

Table 1: Reference documents

| Ref. Title| Originator| Version| Date|
|-------------------------|--------------|-----------|--------|
| No reference documents. ||| |

## 1.6 APPLICABLE DOCUMENTS

The table below lists the documents to which the current document must be compliant (e.g. FWC, SC, RfA).

| Ref.| Title | Originator | Version| Date|
|--------|---------------------------------|-------------------|-----------|------------|
| A01 | Framework Contract| TAXUD/2021/CC/162 | N/A| 24/06/2021 |
| A02 | SOFT-DEV Framework Quality Plan | SDEV-FQP | 1.00| 10/01/2023 |
| A03 | Specific Contract 19 | TAXUD/2024/DE/121 | N/A| 22/03/2024 |

## 1.8 DEFINITIONS

For a better understanding of the present document, the following table provides a list of the principal terms used.

| Term| Definition |
|---------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Applicant| Animporter established in a Member State or an indirect customs representative who applies for the status of an authorised CBAMdeclarant. |
| Business component | The CBAMsystem is separated in different business components depending on the type of business that an actor is involved. Examples of business components covered in CBAM are amongst others: Authorisation management module, certificate management or declaration lifecycle management. |
| CBAM Goods | Goods listed in Annex I of CBAM Regulation. More details are provided in: https://eur-lex.europa.eu/eli/reg/2023/956/oj. |
| Competent Authority | The authority of a Member State responsible for registering the authorised CBAM declarant in the CBAM registry, processing applications for authorisation, and maintaining relevant information. There is only one CBAM Competent Authority per Member State; therefore, it is identified by the country code. In the following, two types of Competent Authorities are distinguished: |

Table 2: Applicable documents

## 1.7 ABBREVIATIONS &amp; ACRONYMS

For a better understanding of the present document, the following table provides a list of the principal abbreviations and acronyms used.

Table 3: Abbreviations and acronyms

| Abbreviation/Acronym| Definition |
|------------------------|-----------------------------------------------------|
| AMM | Authorisation Management Module |
| CBAM| Carbon Border Adjustment Mechanism 2|
| CBAM TR| CBAM Transitional Registry|
| COM | The Commission|
| DTCA| Decision-taking Competent Authority |
| EORI| Economic Operator Registration and Identification 3 |
| EU | European Union|
| HRZ | Horizontal |
| MS | Member State |
| NCA | National Competent Authority |
| UI | User Interface|
| UUM&DS | Uniform User Management &Digital Signature 4 |
| QA | Quality Assurance|

Table 4: Definitions

| Term| Definition |
|---------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Declarant | Decision-taking Competent Authority : this is the Competent Authority of the country where the applicant or declarant is established. It holds the authority to make decisions regarding CBAM authorisations within its own country. Other Competent Authorities : these are Competent Authorities in the Member State, excluding the decision-taking Competent Authority. While they have supporting roles by providing information on the applicant during the consultation procedures, they do not have the final decision-taking authority for CBAM authorisations granted by the decision-taking Competent Authority. |
|| This means an authorised CBAMdeclarant, which is an importer established in a Member State or an indirect customs representative appointed by an importer, who is authorised to import goods into the customs territory of the European Union under the CBAMframework. |
| Economic Operator | Refers to a business or individual engaged in activities that fall under the regulations of the CBAM. This typically includes importers or producers of goods whose carbon emissions are subject to the CBAM regulations. These operators are responsible for complying with the CBAMs requirements, which may involve reporting the carbon content of their goods and paying carbon costs. |
| Importer | Either the person lodging a customs declaration for release for free circulation of goods in its own name and on its own behalf or, where the customs declaration is lodged by an indirect customs representative in accordance with Article 18 of Regulation (EU) No 952/2013, the person on whose behalf such a declaration is lodged.|
| Indirect customs representative | A customs representative appointed by an importer established in a Member State. If the importer appoints an indirect customs representative and they agree, they can act as an authorised CBAM declarant and submit the application for an authorisation on behalf of the importer. The indirect customs representative is liable for all operations made for the importers it represents. |
| NCA | National Competent Authority. |
| Operator | Any person who operates or controls an installation in a third country.|
| Representative | The indirect representative declared in imports customs declaration.|
| Third country| A country or territory outside the customs territory of the Union.|

# 2 GENERAL INFORMATION

## 2.1 SYSTEM OVERVIEW

The Declarant Portal User Interface facilitates the management of the CBAM Regulation, from the application lodging for an authorisation to the decision granting by the Competent Authority.

The overall CBAM system covers several business components and is also composed of a horizontal component which provides functionalities that can be used horizontally by all business components (e.g. notifications,  email  preferences,  document  management).  The  Declarant  portal  only  covers  the Authorisation management module (AMM) as business component.

In practice, several User Interface systems cooperate with the Declarant portal providing an integrated solution  allowing  interactions  between  economic  operators,  national  Competent  Authorities,  the commission and other IT applications.

More information about the CBAM system is available on https://taxationcustoms.ec.europa.eu/carbon-border-adjustment-mechanism\_en#cbam.

## 2.2 AUTHORISED USE PERMISSION

CBAM  Declarant  Portal  is  allowed  to  be  used  by  importers  of  CBAM  goods  (or  their  indirect representative) who are registered in UUM&amp;DS. Please contact your national CBAM helpdesk to get help to register and get permissions on UUM&amp;DS.

Further  details  on  the  required  roles  and  responsibilities  can  be  found  in  the  section  'Roles  and Responsibilities'.

## 2.3 USER SUPPORT

CBAM Declarants need to contact their respective National Competent Authority Service Desk both for business &amp; technical issues.

## 2.4 SUPPORTED BROWSERS

The application is relying on the browser compatibility of the latest and the 2 previous versions of common web browsers (Google Chrome, Mozilla Firefox, Microsoft Edge Chromium, Safari). More information  can  be  found  on  the  browser  compatibility  page  of  eUI  in  the  following  link: https://eui.ecdevops.eu/eui-showcase-dev-guide-17.x/docs/00b-general-infos/04-browsers-support.

# 3 GETTING STARTED

Access  to  CBAM  Declarant  Portal  is  only  allowed  to  economic  operators  who  have  registered  in UUM&amp;DS.

Their respective NCA are responsible to assign the allowed roles in UUM&amp;DS.

## 3.1 ACCESS THE CBAM DECLARANT PORTAL

Access to  the  Declarant  Portal  is  established  via  UUM&amp;DS.  UUM&amp;DS is  used  to  authenticate  the economic  operator.  Upon  successful  login  to  the  Declarant  Portal,  the  user  will  be  directed  to  the Homepage. Please contact your national CBAM helpdesk for information about the actual procedure to connect to the portal via UUM&amp;DS and for support about any issue with this matter.

## 3.2 NAVIGATION MAP

The Declarant Portal is used by the economic operator for interacting with the other CBAM actors (NCA and the COM) in the scope of CBAM activities.

The menu - available on the left-hand side of each page of the CBAM application - is given in Table 5.

Table 5: Declarant Portal menu

| Block | Action | Description |
|----------------|------------------------------------|-------------------------------------------------------------------|
| -| Homepage | Allows the user to access the home page.|
| -| CBAM TR| Allows the user to navigate to the CBAM Transitional Registry. |
| Authorisations | New application | Allows the user to create a new application.|
| Authorisations | My applications and authorisations | Allows the user to view all applications and authorisations.|
| Authorisations | My drafts| Allows the user to view their saved drafts. |
| Notifications | Notifications list | Allows the user to view all the notifications they have received. |
| Submissions | Submissions list| Allows the user to view all their submissions. |
| Account | Email preferences | Allows the user to view and configure the email preferences.|

## 3.3 ROLES AND RESPONSIBILITIES

Access to specific pages and actions is determined by defined roles. The table below outlines each role available in the CBAM Declarant Portal and its corresponding accessible pages and actions. Note that the 'AMM Administrate information' and 'AMM Submit information' roles give access to the same pages; only the available actions are different.

Table 6: Security roles

| Roles | Pages/functionalities | Applicable business component|
|------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------|
| HRZ View information| This role is needed for any user connecting to the Declarant portal in order to access the general pages of the system. | All |
| AMMView information | This role is proposed for any user needing to access the information of AMMwithout the ability to modify them. | AMM |
| AMM Administrate information | This role must be assigned to a user in charge of all actions that must be performed for AMMto prepare information to be communicated to the decision taking Competent Authority. However, this role gives no rights to submit the information. | AMM |
| AMM Submit information| This role must be assigned to a user in charge of the effective submission of the information relevant to AMMto a decision taking Competent Authority. In practice, this role is also authorised to prepare the information and is therefore a complete role with all rights. | AMM |

## 3.4 GENERIC USER INTERFACE FEATURES

This section provides an explanation on the different generic User Interface features that can be found on the CBAM portal. The following sections can be found: Header, Footer, Tooltips, Validation of a form, Read-only and editable modes, General error messages and Language.

## 3.4.1 Header

The header, which is displayed on each page, contains a button to display/hide the navigation menu, the European Commission logo, the title of the application, the logged-in username, the user icon and the language selector.

Figure 1: Header



When clicking on the user icon, the user can either view its user information (identification number, name, country…) or log out. To change the language, the user can click on the displayed language and select the desired one in the menu.



## 3.4.2 Footer

The footer, which is displayed on each page, contains the ' © European Commission' copyright, the indication of the current version of the system, a link to the privacy statement website of the European Commission, a link to the user manual and a link to the legal notice.

```
European Commission Version: 2.2 Privacy statement Usermanual Legal notice
```

Figure 3: Footer

## 3.4.3 Tooltips

The tooltips are used to provide extra information on some elements. An info icon indicates the availability of a tooltip, and the tooltip text appears when the user hovers over it with its cursor. 0

Figure 4: Tooltip



## 3.4.4 Validation of a form

When the user fills in a form, the system verifies that the data has been properly entered according to the given rules. The validation of a form is performed in two steps:

- Syntactic validation , performed at client-side;
- Semantic / business validation , performed at server-side.

The  process  is  depicted  in  the  figure  below.  Once  the  syntactic  validation  is  successful,  the semantic/business validation is performed (at submission of the form). If the latter is successful as well, the form is finally submitted.

Figure 5: Validation of a form



## Figure 2: Details when clicking on the user icon

## 3.4.4.1 Syntactic validation

With the syntactic validation, the following is checked: the cardinality (mandatory or optional) and the format of a field. A field is validated while the user types and when the user clicks on the "Submit" button. Such validation is directly visible in the User Interface, while the user completes a form.

In case such validation is not satisfied, a corresponding message is displayed below the field, marked as invalid and highlighted in red.

Figure 6: Text field in error due to syntactic validation



## 3.4.4.2 Semantic validation

With the semantic validation, the business rules are checked (e.g. one field out of two must be filled out, but  not  both).  A  field  is  validated  only  when  the  user  clicks  on  the  "Submit"  button.  Hence,  such validation is not directly visible when the user completes a form; they must submit the form in order to see the error message(s). In case a field is not correctly filled in according to the business rule(s), an error message in a red box appears under the data group or element.

Figure 7: Text field in error due to semantic validation



## 3.4.5 Read-only and editable modes

The system has two modes: read-only and editable. These modes control what users can do, ensuring that important information is protected while allowing necessary updates.

In read-only mode, users can look at information but cannot make changes. This mode is useful for reviewing without the risk of accidentally changing anything. Examples of elements displayed in readonly  mode:  list  pages  (notifications  list,  submissions  list,…),  view  pages  (view  application,  view notification, view process data, …).

In editable mode, users can add, change, or delete information, depending on their roles. This mode is designed on form elements on which input is needed from the users. Examples of elements displayed in editable-only  mode:  page  forms  (submit  application,  amendment  request,  reply  pages,…),  email preferences.

## 3.4.6 General error messages

In case of a generic error from the system, a generic error message will be displayed at the top-right corner of the page as a pop-up notification.

Figure 8: Example of an error message



The following error messages are displayed depending of the type of error that occurred:

Table 7: General error messages

| Error type| Error message |
|-------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Generic technical issue | A generic technical issue has occurred. Please try again later. If the issue persists, please contact the CBAM support (support@itsmtaxud.europa.eu) and provide the following: • a description of the situation where the error message appeared; • a screenshot of the page where the error message occurred; • the browser used and its version. |
| Access restriction| Access denied. You do not have permission to access this resource. If you believe this is an error, please contact the CBAM support (support@itsmtaxud.europa.eu).|
| Resource not found| Resource not found. Please try again later, and if the issue persists, please contact the CBAMsupport (support@itsmtaxud.europa.eu). |

## 3.4.7 Language

The system is available in the different EU languages, which allows the user to use the system in their own language. Note that the translations are applied for each label and tooltip separately in order to provide a maximum of textual information in the language of the user. Hence, some labels might not be translated in the selected languages. By default, the selected language is the one previously chosen in the header, or if none is specified, it defaults to the browser's language.

The user is able to manually change the language of the system from the header. More information is provided in the section 'Header'.

## 3.5 BASIC SYSTEM FUNCTIONS

## 3.5.1 List and tables

Several list pages are present in the application. The following features are available in order to enhance the results' consultation: pagination, sorting and filtering.

## 3.5.1.1 Pagination

The pagination capability can be available in editable and read only mode at the bottom of a list page or of a table.

The maximum number of results to display per page can be configured by the user. By default, 20 results are displayed per page. The user is allowed to choose between 10, 20, 50, 75 or 100 displayed results per page. A navigation bar allows navigating among the different pages.





## Figure 9: Pagination of a list

For data tables, the maximum number of entries shown is 5 per page of the data table.

Users  can  navigate  through  the  pages  using  a  navigation  bar  with  hyperlinks.  This  allows  moving sequentially (e.g., from page 1 to 2, then 2 to 3, etc.) using the previous and next buttons.  If the user is on the first or the last page, the corresponding link is not active.

## 3.5.1.2 Sorting

The sorting capability can be available in edit and read only mode. When it is available, a clickable sorting icon is displayed next to the column title and will allow the user to sort the result in ascending or descending order. When enabled, the sorting is applied to the results of all pages.

By default, records with an empty value will be displayed at the bottom of the list when sorting on ascending order and at the top when sorting on descending order.

When sorting is done on alphanumeric fields, the order does not depend on the use of upper- or lowercase letters. Criteria related to a code list are sorted according to the code and not the description of the code.

Figure 10: Sorting applied on the first column of a list



## 3.5.1.3 Filtering and wildcards

The filtering capability can be available in edit and read only mode. When it is available, an editable field box is displayed below the column title and will allow the user to filter the results based on the entered value. The filtering is case insensitive.

When wildcard search is activated, the user can search with the use of the asterisk (*) or the percent (%) symbols to match any number of characters. Therefore,  a  query  for  which the  reference number  is CBAM-DE* (or CBAM-DE%) will retrieve all reference numbers starting with "CBAM-DE".

Wildcard search is activated on the following pages:

- Notifications list
- Submissions list

Figure 11: Filtering applied with wildcard search



## 3.5.2 Homepage

When connecting to the system, the user is automatically directed to the homepage. However, to come back to this page, the user can click on the 'Homepage' button of the CBAM Declarant Portal menu (see section 'Navigation Map').

On this page, the user can see a set of widgets focusing on the elements requiring some attention.

## 3.5.2.1 Notifications widgets

The widgets 'Unanswered notifications' and 'Unread notifications' both provide a filtered view of received notifications by the economic operator.

The 'Unanswered notifications' widget includes all notifications for which an answer is expected (i.e. 'Request'  =  'Yes'  and  'Answered'  =  'No').  The  'Unread  notifications'  widget  includes  all notifications for which the status  is 'Unread'.

From these widgets, the user can directly access a specific notification detail or view the complete list of notification by clicking on the title of the widget.

Figure 12: Notification widgets



## 3.5.3 Attachments

When an attachment is expected in an editable form, the user can add a new attachment by clicking on the 'Add new' button above the attachments table.

Figure 13: Add new button



Then, the upload attachment capability allows the user to select a file using the dedicated button or drag and drop a file into the delimited upload area. The maximum allowed file size is 20MB, and java, python, and other executable scripts are not allowed.

Figure 14: Upload attachment area with drag and drop capability



Once uploaded, the file information is displayed below the upload area, allowing to delete the file before adding the attachment in the user form. The user can also enter additional document information before inserting it in the form.

Figure 15: Upload of an attachment



The attached documents are displayed in a data table. In editable mode, the user can edit the document information,  download  the  file  and  remove  the  document  and  its  associated  information.  It  is  also possible to remove all documents by clicking on the 'Remove all' button at the top of the table.

Figure 16: Editable view of the attachments



In read-only mode, the user can download the attached document, but they have no possibility to update the table.

## 3.5.4 Notifications management

To view the list of received notifications, the user can click on 'Notifications list' in the 'Notifications' drop-down of the CBAM declarant portal menu (see section 'Navigation Map').

Figure 17: Notifications list button in the CBAM declarant portal menu



The user is then redirected to the 'Notifications list' page, displaying the list of all received notifications, with different information given in each column.

Figure 18: Notifications list in the CBAM declarant portal



Notifications can be of two kinds:

- Simple notifications informing the economic operator about new information. Example: status changes, new decision…
- Requests  for  which  the  economic  operator  must  provide  an  answer.  Example:  request  for additional information.

In particular, the list of notifications contains amongst others:

- The sender information. In the context of the Declarant Portal it essentially corresponds to the country of the Competent Authority. Note that no identifier is provided when the sender is a country or the commission.
- The  reference  number  corresponds  to  the  reference  number  of  the  object  (e.g.  application, authorisation) related to the notification.
- The business context of the notification. In the case of the declarant portal, only authorisation management is supported for the time being.
- An  indication  if  the  notification  is  a  simple  information  or  a  request  to  be  answered  (see "Request" column);
- The status of the notification:
- o Unread: specifies that the notification has not been read by the user;
- o Read: specifies that the notification has been read by the user. Note that as soon as the notification is opened, the notification is automatically marked as read;
- In case of a request, it contains an indication if it is answered (see "Answered" column);

- o No: specifies that the request has not been answered yet;
- o Yes: specifies that the answer has been submitted. Note that even if the answer is saved as a draft, the status is not changed until the effective submission.

For each displayed notification, the user can click on the related 'View notification' button in the actions column of the table to view all the information of one specific notification.

Figure 19: View notification button



## 3.5.4.1 How to provide an answer to a notification?

On the 'Notifications list' page, some notifications are displayed with a 'Provide answer' button in the actions column of the table.

Figure 20: Provide answer button on the notifications list page



When this button is displayed for a received notification, it means that an answer to the notification is expected from the declarant.

To do so, the user can either directly click on the 'Provide answer' button in the list, or first consult the notification details and then click on the 'Provide answer' button at the top of the page.

Figure 21: Provide answer button on top of the notification page



The user is  then  redirected  to  the  'Reply  details  page',  where  the  user  must  provide  the  requested information.

## 3.5.5 Submissions management

To view the list of sent submissions, the user can click on 'Submissions list' in the 'Submissions' dropdown of the CBAM declarant portal menu (see section 'Navigation Map').

Figure 22: Submissions list button in the CBAM declarant portal menu



The user is then redirected to the 'Submissions list' page, displaying the list of all sent notifications, with different information given in each column.

Figure 23: Submissions list in the CBAM declarant portal



The submission can be related to the following contexts:

- An answer to a request;
- A request to trigger a new business process. Note that such possibility is defined in each business component.

In particular, the list of submissions contains:

- The  recipient  information  (type  and  identifier  when  applicable):  the  recipient  to  which  the notification has been sent. In case of a country or the commission, the identifier is not applicable.
- The  reference  number  corresponds  to  the  reference  number  of  the  object  (e.g.  application, authorisation) related to the sent notification.
- The subject of the submission.
- The  business  context  of  the  sent  notification.  In  the  case  of  the  declarant  portal,  only Authorisation management is supported for the time being.
- The sent time: the date and time at which the submission has been provided.

Note that for each displayed submission, the user can click on the related 'View submission' button in the actions column of the table to consult all the information of one specific submission.

Figure 24: View submission button



## 3.5.6 Email preferences page

The email preferences page is related to a functionality in the CBAM Declarant portal that allows the user to receive email notifications related to the notifications that they receive inside the portal itself.

To consult the email preferences page, the user can click on 'Email preferences' in the 'Account' dropdown of the CBAM declarant menu (see section 'Navigation Map').

Figure 25: Email preferences button in the CBAM declarant menu



On this page the user can provide several email addresses for each business context, and for each address they must provide the language in which they wish to receive the emails and also indicate if they want to receive the email alerts for new notifications with that address.

To add a new email preference, select 'Add new' in the table relevant for the business context in which you need to receive email alerts:

Figure 26: Add new email preference



Then, complete the information required. Set 'Receive email alerts for new notifications' as 'Yes' to activate the functionality and click on 'Ok'.

Figure 27: View/edit new email preference



At the bottom of the page, save the new preferences.

Figure 28: Save email preference



As soon as the indication is positive, all new notifications relevant for the indicated business context received by the economic operator identified with an EORI number will be notified by email with nonsensitive information.

Note that for each mentioned email address, the user can test the link with the email address by clicking on the 'Send test email' button.

Figure 29: Send test email



## 3.6 EXIT THE CBAM DECLARANT PORTAL

When the user wants to exit the CBAM Declarant Portal, they must click on the 'Logout' button located in the user drop-down, on the right-hand side of the header of the system.

Figure 30: Logout button