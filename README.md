# Berk Test Automation Library

## Overview

The Berk Test Automation Library is a powerful tool designed to simplify and streamline your mobile test automation process. It is written in Java and uses the Appium framework for interacting with mobile devices. The library provides a set of high-level APIs to interact with mobile elements and perform common automation tasks such as clicking, sending keys, and taking screenshots.

## Features

- **Element Interaction**: The library provides methods to interact with mobile elements such as clicking and sending keys.
- **Screenshot Capture**: The library can take screenshots of the current state of the WebDriver.
- **Element Location**: The library can return the center coordinates of a specified element.
- **Wait Functionality**: The library provides methods to wait until a specified element is displayed.
- **Scrolling**: The library provides a method to scroll until a specified element is displayed.

## Getting Started

To use the Berk Test Automation Library, you need to have Java and Maven installed on your machine. You also need to have Appium set up for mobile automation.

## Usage

Here is a basic example of how to use the Berk Test Automation Library:

```java
WebDriver driver = new AndroidDriver();
Berk berk = new Berk(driver);

// Click on an element
berk.click(driver, "path_to_element", 10);

// Send keys to an element
berk.sendKeys(driver, "path_to_element", "text_to_send", 10);

// Take a screenshot
berk.takeScreenshot(driver);

// Scroll until an element is displayed
berk.scrollUntilElementDisplayed(driver, "path_to_element");
```

## Contributing

Contributions are welcome! Please read the contributing guidelines before getting started.

## License

This project is licensed under the terms of the MIT license.
