# tuntikirjaus-components-lib

A JavaFX component library used by the [tuntikirjaus](https://github.com/jaausi) project family.

Currently provides:

- **`AutoCompleteTextField<S>`** – a `TextField` subclass that shows a filtered popup of autocomplete suggestions as the user types. Supports case-sensitive/insensitive matching, a configurable max number of entries, custom highlight styles, and a "filter-only" mode that hides the popup and exposes the filtered list for binding to other controls (e.g. a `ListView`).

---

## Prerequisites

| Tool | Version |
|------|---------|
| JDK  | 25 (Temurin recommended) |
| Maven | 3.9+ |

---

## Build & test locally

```bash
# Clone the repository
git clone https://github.com/jaausi/tuntikirjaus-components-lib.git
cd tuntikirjaus-components-lib

# Compile and run all tests
mvn verify
```

---

## Releasing a new version

Releases are published automatically to **GitHub Packages** by the CI/CD pipeline whenever a version tag is pushed.

1. Decide on the new version number (e.g. `1.2.0`).
2. Push a tag in the format `v<version>`:

```bash
git tag v1.2.0
git push origin v1.2.0
```

The release workflow will:
- Strip the `v` prefix and set it as the Maven project version.
- Build and test the library.
- Publish `com.sirvja:tuntikirjaus-components-lib:<version>` to GitHub Packages.

---

## Using the library in another project

### 1. Configure Maven credentials

GitHub Packages requires authentication even for public packages. Add the following server entry to your `~/.m2/settings.xml`, replacing `YOUR_GITHUB_USERNAME` and `YOUR_PAT` with a GitHub Personal Access Token that has the `read:packages` scope:

```xml
<settings>
  <servers>
    <server>
      <id>github-tuntikirjaus</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_PAT</password>
    </server>
  </servers>
</settings>
```

### 2. Add the repository and dependency to your `pom.xml`

```xml
<repositories>
  <repository>
    <id>github-tuntikirjaus</id>
    <url>https://maven.pkg.github.com/jaausi/tuntikirjaus-components-lib</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.sirvja</groupId>
    <artifactId>tuntikirjaus-components-lib</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
```

### 3. Example usage

```java
import com.sirvja.textfield.AutoCompleteTextField;

import java.util.TreeSet;

// Create with a pre-populated entry set
var entries = new TreeSet<>(List.of("Alpha", "Beta", "Gamma", "Delta"));
        var field = new AutoCompleteTextField<>(entries);

// Listen for the item the user picks from the popup
field.

        getLastSelectedObject().

        addListener((obs, oldVal, newVal) ->{
        System.out.

        println("Selected: "+newVal);
});

// Filter-only mode: hide popup, bind filtered results to a ListView instead
        field.

        setPopupHidden(true);
listView.

        itemsProperty().

        bind(new SimpleListProperty<>(field.getFilteredEntries()));
```

---

## CI status

| Workflow | Trigger |
|----------|---------|
| CI | Push / PR to `main` or `master` |
| Release | Push of a `v*.*.*` tag |
