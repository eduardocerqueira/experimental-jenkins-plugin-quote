# experimental-jenkins-plugin-quote

A Jenkins plugin that adds a build step to print a random motivational quote about AI, vibe coding, agentic systems, AI providers, inference, and AI models to the build log.

## What it does

On every Jenkins job run, the **Print a random quote** build step picks a random quote from a curated collection of 100+ quotes and writes it to the build log via `listener.getLogger().println()`.

## How to build

```bash
mvn clean package
```

This produces an `.hpi` file under `target/`.

## How to run locally

```bash
mvn hpi:run
```

This starts a development Jenkins instance on `http://localhost:8080/` with the plugin pre-installed.

## How to produce the `.hpi`

Run:

```bash
mvn clean package
```

The `.hpi` file will be at `target/random-quote-plugin.hpi` (or the artifact ID defined in `pom.xml`).
