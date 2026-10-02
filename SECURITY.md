# Security Policy

This repository is maintained by Vercom S.A. as part of the RedLink push
notification platform. We take the security of our SDKs seriously and
appreciate reports from the community and security researchers.

_Last updated: 2026-09-24. Reports accepted in English and Polish._

## Scope

**In scope:**

- The RedLink Android SDK (`pl.redlink:push`) as distributed via the JFrog
  Artifactory repository
  (`https://redlinkv1.jfrog.io/artifactory/default-maven-local`), including
  its published binaries and their integrity (supply-chain concerns, see
  below)
- The example application and the integration documentation in this
  repository

**Out of scope for this file:**

- The RedLink backend services, APIs, and web panel
- Vulnerabilities in third-party dependencies (for example Firebase Cloud
  Messaging) that do not specifically affect this SDK. Please report these
  upstream first.

**Please do not report:**

- Output of automated scanners without a proof of concept or analysis
- Questions about how to use or integrate the SDK (use regular support
  channels)
- Issues without security impact

## Supported Versions

| Version          | Supported          |
| ---------------- | ------------------ |
| 1.16.12 (latest) | :white_check_mark: |
| < 1.16.12        | :x:                |

> A formal support-period determination under Article 13(8) of Regulation
> (EU) 2024/2847 (the EU Cyber Resilience Act) is in progress. Until it is
> published here, treat only the latest released version as supported.
>
> When a version reaches end of support, we will announce it in this file
> and in the release notes, together with the recommended upgrade path.

## Reporting a Vulnerability or Incident

**Please do not open a public GitHub issue for security vulnerabilities.**

Report it by email to **soc@vercom.pl**. For sensitive exploit details,
encrypt your message with the CERT Vercom public key below.

**CERT Vercom PGP key**

| Field | Value |
| ------ | ------ |
| User ID | CERT Vercom &lt;cert@vercom.pl&gt; |
| Fingerprint | `3319 F968 FF79 42C8 4269 310C B374 25F1 88F1 C2CE` |
| Long key ID | `0xB37425F188F1C2CE` |
| Algorithm | RSA 4096 |
| Created | 2025-03-31 (no expiry date set) |
| Published at | <https://messageflow.com/pl/bezpieczenstwo/> |

Always verify the fingerprint above against the copy published on our
website before encrypting anything.

This channel also covers **security incidents**, in particular any
suspected compromise of our published artifacts in the JFrog Artifactory
repository or of the release process (supply-chain incidents). If you believe a published
`pl.redlink:push` artifact has been tampered with, report it immediately.

Please include in your report:

- A descriptive title and a description of the vulnerability and its
  potential impact
- Your name or handle and affiliation, if you wish to be credited
- Steps to reproduce, or a proof of concept if available
- The affected version(s) and platform(s)
- Whether you believe the vulnerability is being actively exploited
- **Disclosure status:** whether the details have already been shared with
  anyone else or published, and your plans for future disclosure (for
  example a conference talk)
- A suggested fix or mitigation (optional)

## What to Expect

- **Acknowledgement** within 3 business days of your report.
- **Initial assessment** (severity, affected versions) within 10 business days.
- We will coordinate a disclosure timeline with you before any public
  advisory is published. By default this is 90 days from acknowledgement or
  the moment a fix is available, whichever is sooner, unless we agree on a
  different timeline together.
- **Escalation:** if you do not receive an acknowledgement within 6
  business days, please resend your report with "ESCALATION" in the subject
  line, or contact CERT Polska (<https://cert.pl/en/report/>) as a
  coordinating party.
- Credit in the changelog entry, if you would like it.

## Where fixed vulnerabilities are announced

Fixed vulnerabilities are announced in this repository's
[CHANGELOG.md](CHANGELOG.md), under a `### Security` heading, alongside the
affected versions, severity, and the remediation steps you need to take.

## Safe Harbor

We will not initiate legal action against researchers who, in good faith:

- act within the scope described in this policy,
- avoid privacy violations, data destruction, and degradation of our
  services or our users' data,
- do not exploit a finding beyond what is necessary to demonstrate it, and
- give us a reasonable opportunity to remediate before public disclosure.

If you are unsure whether your planned testing is covered, ask us first at
**soc@vercom.pl**.

## Regulatory Context

Vercom S.A. is a manufacturer subject to Regulation (EU) 2024/2847 (the EU
Cyber Resilience Act). From **11 September 2026**, actively exploited
vulnerabilities and severe incidents affecting products already on the
market must be reported by us to the competent CSIRT and ENISA within 24
hours of becoming aware of them. Reporting a vulnerability to us promptly
and directly, rather than through a public channel, helps us meet that
obligation and protects users in the meantime.
