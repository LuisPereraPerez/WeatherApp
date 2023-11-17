# Weather Data Capture from External API

## Assignment Details
- **Title:** Weather Data Capture
- **Subject:** Desarrollo de aplicaciones para ciencia de datos
- **Student:** Luis Perera Pérez
- **Year:** 2023/2024
- **Degree:** Grado de Ingeniería en Ciencia de Datos
- **School:** Escuela Universitaria de Informática
- **University:** Universidad de Las Palmas de Gran Canaria

## Summary of Functionality
This Java application periodically fetches weather data from the OpenWeatherMap API for eight locations (islands) and stores the acquired data in an SQLite database. The data includes temperature, precipitation probability, humidity, clouds, wind speed, and timestamps for the next five days at 12:00 PM.

## Resources Used
- **Development Environment:** IntelliJ IDEA
- **Version Control:** Git, GitHub
- **Database:** SQLite
- **Documentation:** Markdown

## Design
### Classes and Relationships
- `Weather` Class:
    - Attributes: temp, precipitation, humidity, clouds, windSpeed, ts, location.
- `Location` Class:
    - Represents a geographical location with latitude (`lat`), longitude (`lon`), and islands name (`island`).
- `WeatherProvider` Interface:
    - Method: `List<Weather> get(Location location)`
- `WeatherStore` Interface:
    - Method: `void storeWeather(String location, Weather weather)`
- `OpenWeatherMapProvider` Class (implements `WeatherProvider`):
    - Fetches weather data from OpenWeatherMap API.
- `SQLiteWeatherStore` Class (implements `WeatherStore`):
    - Stores weather data in an SQLite database.
- `WeatherController` Class:
    - Orchestrates the data retrieval and storage process.


### Design Principles and Patterns
- Each class has a clear and specific responsibility.
- Code is designed to be easily extended without modifying existing code.
- Interfaces are tailored to specific use cases.

### Diagram
- ![Diagrama UML](Weather/diagramaUML.png)

## How to Run

### Prerequisites
- Java 17 or later
- Maven
- OpenWeatherMap API Key

### Steps
1. Clone the repository.
2. Navigate to the project directory.
3. Build the project with Maven.
4. Navigate to the target directory.
5. Run the program, providing your OpenWeatherMap API Key as an argument.
---