# experimental-jenkins-plugin-quote

A Jenkins plugin that adds a build step to print a random motivational quote about AI, vibe coding, agentic systems, AI providers, inference, and AI models to the build log.

## Prerequisites

- [Docker](https://docs.docker.com/get-docker/) installed and running on your machine.
- [Java 11+](https://adoptium.net/) and [Maven](https://maven.apache.org/install.html) to compile the plugin (optional if a pre-built `.hpi` is available).

---

## Building the plugin

```bash
mvn clean package
```

This produces the plugin file at `target/quote.hpi`.

---

## Running Jenkins in Docker

Pull the latest Jenkins LTS image and start a container:

```bash
docker pull jenkins/jenkins:lts
docker run -d -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home --name jenkins jenkins/jenkins:lts
```

> **Note:** If port 8080 is already in use, change the host port, e.g. `-p 8081:8080`.

Retrieve the initial admin password:

```bash
docker logs jenkins 2>&1 | grep -i password
# or directly:
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

Open [http://localhost:8080](http://localhost:8080) (or your custom port) and complete the setup wizard — install suggested plugins and create an admin user.

---

## Installing the plugin

After compiling the plugin, copy the `.hpi` file into the running container:

```bash
docker cp target/quote.hpi jenkins:/var/jenkins_home/plugins/
```

Then restart Jenkins for the plugin to be loaded:

```bash
docker restart jenkins
```

Alternatively, you can load the plugin via the Jenkins UI:
1. Go to **Manage Jenkins** → **Manage Plugins** → **Advanced** tab.
2. Under **Upload Plugin**, choose the `target/quote.hpi` file and click **Upload**.
3. Restart Jenkins when prompted.

After restart, verify the plugin is installed:
- Go to **Manage Jenkins** → **Manage Plugins** → **Installed** tab.
- Search for "quote" — the plugin should appear in the list.

---

## Example Jenkins Job (Declarative Pipeline)

1. In Jenkins, click **New Item**, enter a name (e.g. `Test Quote Plugin`), select **Pipeline**, and click **OK**.
2. Scroll to the **Pipeline** section, select **Pipeline script** from the Definition dropdown, and paste the following:

```groovy
pipeline {
    agent any
    stages {
        stage('Print Random Quote') {
            steps {
                quote()
            }
        }
    }
}
```

3. Click **Save**, then **Build Now**.

### Expected Output

After the build completes, open the **Console Output** — you should see something like:

```
Started by user admin
[Pipeline] Start of Pipeline
[Pipeline] node
[Pipeline] {
[Pipeline] stage
[Pipeline] { (Print Random Quote)
[Pipeline] step
[Pipeline] quote
"The best way to predict the future is to invent it." - Alan Kay
[Pipeline] }
[Pipeline] // stage
[Pipeline] }
[Pipeline] // node
[Pipeline] End of Pipeline
Finished: SUCCESS
```

The exact quote will be different each time the job runs.

---

## Troubleshooting

| Problem | Solution |
|---|---|
| Plugin not listed after restart | Check container logs with `docker logs jenkins`. Make sure the `.hpi` filename matches the plugin artifact ID exactly (`quote.hpi`). |
| "No such method: quote" in pipeline | The plugin uses a build step (not a pipeline step). Use the **Freestyle project** type and add the **Print a random quote** build step instead. |
| Docker container fails to start | Run `docker logs jenkins` to see the error. Common cause: port conflict. Use a different host port. |

---

## What it does

On every Jenkins job run, the **Print a random quote** build step picks a random quote from a curated collection of 100+ quotes and writes it to the build log via `listener`.

## Prerequisites

- [Docker](https://docs.docker.com/get-docker/) installed and running on your machine.
- [Java 8 or 11](https://adoptium.net/) and [Maven](https://maven.apache.org/) to compile the plugin.
- Git to clone the repository.

## Compiling the plugin

```bash
mvn clean package
```

After a successful build, the plugin `.hpi` file will be in the `target/` directory:

```
target/quote.hpi
```

---

## Running Jenkins in Docker

### 1. Pull the Jenkins LTS image

```bash
docker pull jenkins/jenkins:lts
```

### 2. Start the Jenkins container

Run the container, mapping port `8080` (web UI) and `50000` (slave agents), and persisting Jenkins data in a named volume:

```bash
docker run -d \
  -p 8080:8080 \
  -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  --name jenkins \
  jenkins/jenkins:lts
```

> **Note:** If port 8080 is already in use on your host, change the left side mapping (e.g. `-p 8081:8080`) and access Jenkins at `http://localhost:8081`.

### 3. Get the initial admin password

```bash
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

Copy the password, open `http://localhost:8080` in your browser, and follow the setup wizard (install suggested plugins, create an admin user).

---

## Installing the Plugin

### Method 1: Copy the `.hpi` file into the container

Once Jenkins is running and you have compiled the plugin, copy the `.hpi` file into the `plugins` directory inside the container:

```bash
docker cp target/quote.hpi jenkins:/var/jenkins_home/plugins/
```

### 2. Restart Jenkins

```bash
docker restart jenkins
```

Wait a few seconds for Jenkins to restart, then refresh the browser.

### 3. Verify the plugin is installed

1. Go to **Manage Jenkins** > **Plugins** > **Installed plugins**.
2. Search for "Quote" – you should see the plugin listed.

Alternatively, create a new job and check that **Print a random quote** appears as a build step (see example below).

> **Troubleshooting:** If the plugin does not appear, check the Jenkins logs for errors:
> ```bash
> docker logs jenkins
> ```
> Make sure any required dependencies (e.g. `structs`) are installed via **Manage Jenkins** > **Plugins** > **Available plugins**.

---

## Example Jenkins Job

This example creates a **Pipeline** job that uses the `quote` step provided by the plugin.

### 1. Create a new Pipeline job

1. From the Jenkins dashboard, click **New Item**.
2. Enter a name (e.g. `Test Quote Plugin`), select **Pipeline**, and click **OK**.

### 2. Add the Pipeline script

In the job configuration page, scroll down to the **Pipeline** section. Select **Pipeline script** and paste the following:

```groovy
pipeline {
    agent any
    stages {
        stage('Get Quote') {
            steps {
                quote()
            }
        }
    }
}
```

Click **Save**.

### 3. Run the job

Click **Build Now**. Once the build finishes, open the build and check the **Console Output**. You should see something like:

```
[Pipeline] Start of Pipeline
[Pipeline] node
[Pipeline] {
[Pipeline] stage
[Pipeline] { (Get Quote)
[Pipeline] quote
"The best way to predict the future is to invent it." - Alan Kay
[Pipeline] }
[Pipeline] // stage
[Pipeline] }
[Pipeline] // node
[Pipeline] End of Pipeline
Finished: SUCCESS
```

Every run will display a different random quote.

### Alternative: Freestyle Job

If you prefer a freestyle job:

1. Create a **Freestyle project**.
2. In the **Build** section, click **Add build step** and select **Print a random quote**.
3. Save and build.

The quote will appear in the console log.

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| Docker container fails to start with port conflict | Change the host port mapping, e.g. `-p 8081:8080` |
| Plugin does not show after copy | Run `docker restart jenkins` and check logs with `docker logs jenkins` |
| Missing dependencies | Install required plugins via **Manage Jenkins** > **Plugins** |
| `mvn clean package` fails | Ensure Java 8/11 and Maven are correctly installed; run `mvn --version` to verify |r.getLogger().println()`.

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
