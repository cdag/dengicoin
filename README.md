# Dengicoin

Dengicoin is a Java-based blockchain that I made for a mock cryptocurrency in order to learn the technology. The name comes from the russian word for money (Деньги).
Inspiration for this project came from [This Medium Article](https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa). The main difference being it uses [EdDSA](https://datatracker.ietf.org/doc/html/rfc8032) instead of [ECDSA](https://www.rfc-editor.org/rfc/rfc6979.html). This is currently a work in progress and not ready for implementation.

This project uses [Maven](https://maven.apache.org/) for build automation and dependency management.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running Tests](#running-tests)
- [Code Style](#code-style)
- [License](#license)

## Prerequisites

Before you begin, ensure you have met the following requirements:

- You have installed [Java Development Kit (JDK) 22](https://www.oracle.com/java/technologies/javase-jdk22-downloads.html).
- You have installed [Apache Maven](https://maven.apache.org/install.html).

## Installation

To install Dengicoin, follow these steps:

1. Clone the repository:

    ```bash
    git clone https://github.com/cdag/dengicoin.git
    ```

2. Navigate to the project directory:

    ```bash
    cd path/to/dengicoin
    ```

3. Build the project using Maven:

    ```bash
    mvn clean install
    ```

## Running Tests

This project uses the Maven Surefire Plugin for running tests. To execute the tests, run:

```bash
mvn test
```

## Code Style

This project uses the Maven Checkstyle Plugin to ensure code quality. To generate a Checkstyle report, run:

```bash
mvn checkstyle:checkstyle
```

The report will be generated in the `target` directory.

## License

This project is licensed under the [MIT License](LICENSE).
