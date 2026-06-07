# vtp

Virtuell Tjeneste Plattform for external integrations used by Team Foreldrepenger backend applications.

## Shared context

- Source of truth for shared domain, architecture, and conventions: `navikt/fp-context`
- Copilot Space: `navikt/TeamForeldrepenger`
- Main consumer repo for local and CI usage: `navikt/fp-autotest`

## Repo-specific context

| Topic      | Details                                                            |
|------------|--------------------------------------------------------------------|
| Role       | Test data platform                                                 |
| Tech stack | Java backend; `kontrakter` released as a SemVer library; no Spring |
| Consumers  | `fp-autotest` and fp backend apps                                  |
| Data       | In-memory storage of test scenario data used by mocks              |
| Scope      | External rest integrations and Kafka topics; Oidc/OAuth2 services  |

- REST and GraphQL mocks: One module per external system under `mocks/`
- Kafka: `mocks/kafka-producer` emits events to application consumers
- OAuth2 techs: `server` package `auth.rest` 

## Entry points

- `TestscenarioRestTjeneste`: create test scenario from a list of related persons 
- `PdlLeesahRestTjeneste`: Update test scenario PDL data and produce a PDL-event Kafka record
- `JournalforingRestTjeneste`: Emulates Joark/DokArkiv; updates archive test model and produce a Journal-event Kafka record
- `MicrosoftGraphApiMock`: test employees with info and AD groups
- `ApplicationConfigJersey`: comprehensive list of all mock and oids endpoints

## Module layout

| Module | Purpose |
|---|---|
| `kontrakter` | DTOs and contracts for VTP APIs |
| `domene` | Internal scenario model and repositories |
| `mocks/*` | One module per mocked external system |
| `mocks/kafka-producer` | Embedded Kafka and helpers |
| `server` | Jersey, Jetty, OAuth, application config |
| `util` | Shared helpers |

## When to update VTP

- Add or extend a mock when an application introduces a new external integration.
- Upgrading mocks on external API change: add new version and keep old one until all consumers have migrated.
- Extend `domene` and mock responses when `fp-autotest` needs new test data fields.
