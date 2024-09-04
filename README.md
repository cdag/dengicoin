# Dengicoin

Dengicoin is a Java-based blockchain that I made for a mock cryptocurrency in order to learn the technology. The name comes from the russian word for money (Деньги).
Inspiration for this project came from [This Medium Article](https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa). This is currently a work in progress and not ready for implementation.
 This project uses [Maven](https://maven.apache.org/) for build automation and dependency management.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Running Tests](#running-tests)
- [Code Style](#code-style)
- [Contributing](#contributing)
- [License](#license)

## Prerequisites

Before you begin, ensure you have met the following requirements:

- You have installed [Java Development Kit (JDK) 22](https://www.oracle.com/java/technologies/javase-jdk22-downloads.html).
- You have installed [Apache Maven](https://maven.apache.org/install.html).

## Installation

To install Dengicoin, follow these steps:

1. Clone the repository:

    ```bash
    git clone https://github.com/yourusername/dengicoin.git
    ```

2. Navigate to the project directory:

    ```bash
    cd dengicoin
    ```

3. Build the project using Maven:

    ```bash
    mvn clean install
    ```

## Usage

To run the project, use the following command:

```bash
mvn exec:java -Dexec.mainClass="com.yourpackage.MainClass"
```

Replace `com.yourpackage.MainClass` with the actual main class of your project.

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

The report will be generated in the `target/site` directory.

## Contributing

To contribute to Dengicoin, follow these steps:

1. Fork this repository.
2. Create a branch:

    ```bash
    git checkout -b <branch_name>
    ```

3. Make your changes and commit them:

    ```bash
    git commit -m '<commit_message>'
    ```

4. Push to the original branch:

    ```bash
    git push origin <project_name>/<location>
    ```

5. Create the pull request.

Alternatively, see the GitHub documentation on [creating a pull request](https://help.github.com/articles/creating-a-pull-request/).

## License

This project is licensed under the [MIT License](LICENSE).
