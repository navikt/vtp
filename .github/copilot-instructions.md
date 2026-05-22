# vtp — Virtuell Tjeneste Plattform

Super-mock for nearly all external integrations used by Team Foreldrepenger
backend apps (and shared with Team Sykdom-i-familien for k9). VTP virtualizes
REST/GraphQL/Kafka/LDAP endpoints, owns test data per family scenario, and
issues OAuth2 tokens.

## Context

- [fp-context](https://github.com/navikt/fp-context) — Team Foreldrepenger
  domain, architecture, conventions. Source of truth.
- Copilot Space: navikt / **TeamForeldrepenger**.
- Used by:
  - [fp-autotest](https://github.com/navikt/fp-autotest) — in GHA pipelines and
    `lokal-utvikling` Docker Compose stack
  - fp-sak and other apps when running locally in the IDE
    (`JettyDevServer` + `application-vtp.properties`), with VTP itself running
    as a container from the fp-autotest stack
- Cross-team: also consumed by k9-verdikjede (`k9.Dockerfile` builds the k9 variant).

## What VTP provides

| Capability | Notes |
|---|---|
| REST + GraphQL mocks | One module per external system under `mocks/` (~25 mocks) |
| Kafka producer | `mocks/kafka-producer` — emits events to apps' consumers |
| LDAP | For saksbehandler auth in test |
| OAuth2 | Issues tokens accepted by all FP/k9 apps |
| Test data REST API | `TestscenarioRestTjeneste` — programmatic scenario setup |
| Test data GUI | For manual exploration |

## Module layout

| Module | Purpose |
|---|---|
| `kontrakter` | DTOs / contracts for VTP's own REST/GraphQL APIs |
| `domene` | Internal domain model (TestscenarioRepository, indexer, family model) |
| `mocks/*` | One Maven module per mocked external system |
| `mocks/kafka-producer` | Embedded Kafka + helpers (`LocalKafkaProducer`) |
| `server` | Jersey + Jetty bootstrap, OAuth, Application config |
| `util` | Shared helpers |

## Mocks — by richness

| Rich (scenario-driven) | Thin/dummy |
|---|---|
| pdl-mock, inntekt-mock, arbeidsforhold-mock, saf-mock, journalpost-mock, medl-mock, sigrun-mock, organisasjon-mock, pesys-mock, infotrygd-mock, spokelse-mock, oppgave-mock, norg2-mock | dummy-mock, altinn-proxy-mock, dokdist-mock, digdir-mock, ldap-mock, fpwsproxy-mock, skjermet-person-mock, kelvin-mock, dpsak-mock, fager-mock |

Data is populated per **family scenario** (mor, far, barn, arbeidsgivere,
andre ytelser, inntekt) supplied by fp-autotest via `TestscenarioRestTjeneste`.

## Kafka event production

VTP also produces events that apps consume:

| Endpoint | Topic / consumer | Used for |
|---|---|---|
| `PdlLeesahRestTjeneste` | PDL Leesah | Person hendelser (fødsel, dødsfall, sivilstand, adresse) |
| `JournalforingRestTjeneste` | Joark (journalpost) + SAF | Dokument/journalpost-hendelser |

## Running

VTP is **not run from the IDE** in normal development. Typical patterns:

| Context | How VTP runs |
|---|---|
| GHA pipelines | `ghcr.io/navikt/vtp` image, started by fp-autotest |
| Local fp-autotest run | Same image, via `lokal-utvikling` Docker Compose |
| App development in IDE | VTP container from fp-autotest stack; the app (fp-sak etc.) runs in the IDE against it (`application-vtp.properties`) |

### Testing local VTP changes

```bash
mvn clean install            # build
docker build -t vtp .        # local image, tagged vtp:latest
```

Then in fp-autotest, set `VTP_IMAGE=vtp:latest` in `docker-compose-lokal/.env`
and start the stack as usual. See fp-autotest's copilot-instructions for the
build → deploy → test loop.

For k9 variant: `docker build -f k9.Dockerfile -t vtp-k9 .`

## When to update VTP

- New integration in fp-sak or other apps → add/extend a mock module
- New event consumer in an app → add Kafka producer endpoint
- External API version bump → upgrade matching mock
- New test data fields needed by fp-autotest → extend `domene` model + mock responses

Coordinate cross-team changes (k9 consumers) on Slack `#vtp-chatten`.

## Tech

Java 25, Maven, Jersey, Jetty, Weld, embedded Kafka, OAuth2. No Spring.
