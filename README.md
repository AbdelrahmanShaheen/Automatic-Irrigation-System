# Automatic Irrigation System

<details>
<summary>Table of content</summary>

  - [Model the problem domain by identifying the Scenarios and Use Cases](#model-the-problem-domain-by-identifying-the-scenarios-and-use-cases)
  - [How i simulate this IOT system to work on web.](#how-i-simulate-this-iot-system-to-work-on-web)
  - [Architecture](#architecture)
  - [Installation 📥](#installation-)
  - [How to use](#how-to-use)
    - [Environment Variables](#environment-variables)
      - [Plots API endpoints](#plots-api-endpoints)
  - [Deployed web server \& API Docs](#deployed-web-server--api-docs)
    - [Add a Plot](#add-a-plot)
    - [List all plots](#list-all-plots)
    - [Configure a plot](#configure-a-plot)
    - [update a specific slot in a plot](#update-a-specific-slot-in-a-plot)
  - [Tech/Framework used 🧰](#techframework-used-)

</details>

## Model the problem domain by identifying the Scenarios and Use Cases

- Adding a new plot of land

  - A user can add a new plot of land by providing the necessary details such as plot location, crop type and soil type.
  - The system should generate a unique identifier for the plot of land.
  - System creates a new plot record in the database.
  - The user should be able to view the details of the newly added plot of land.

- Configuring a plot of land

  - A user selects a plot to configure.
  - A user can configure a plot of land by setting the irrigation time slots and the amount of water required for each slot.
  - The system should validate the input data to ensure that the time slots and water requirements are within acceptable limits.
  - System updates the plot record in the database with the new configuration.
  - The user should be able to view the configuration details for a plot of land.

- Editing a plot of land

  - User selects a plot to edit.
  - A user can edit the configuration of a plot of land.
  - The system should validate the input data to ensure that the time slots and water requirements are within acceptable limits.
  - System updates the plot record in the database with the new configuration and notifies the sensor device of the changes.
  - The user should be able to view the updated details of the plot of land.

- Listing all plots and their details

  - User requests to see a list of all plots.
  - A user can view a list of all the plots of land and their details such as name, location, size, crop type, and water requirements.
  - System retrieves all plot records from the database and returns them to the user.

- Integration interface with the sensor device

  - The system should have an integration interface with the sensor device to enable automatic irrigation of the plots of land.
  - The system periodically checks if any plots are due for irrigation.
  - If a plot is due for irrigation, the system sends a request to the sensor device with the required information, such as the plot ID and the irrigation time slots to start the irrigation process.
  - The sensor device uses this information to irrigate the plot of land.
  - The system updates the plot record in the database to reflect the status(NOT_STARTED, IN_PROGRESS, COMPLETED) of the irrigation process.

- Updating the status of the slot

  - Once the request to irrigate a plot of land has been successfully sent to the sensor device, the system should update the status of the time slot to reflect that irrigation is in progress.
  - The status indicates whether the slot has been irrigated or not.

- Retry calls to the sensor device:

  - If the sensor device is not available, the system retries the request a pre-configured number of times.
  - If the request is still not successful, the system triggers an alert.

- Alerting system

  - The system has an alerting system that notifies the user if the sensor device is not available after the pre-configured number of retries.
  - The system sends an alert to the user through email, SMS, or a push notification. (We need here Authentication & Authorization)

## How i simulate this IOT system to work on web.

- I assumed that we've a farm .I represented it as a 2D grid from(0,0) to (9,9)
- So we've 100 Cell each of them represents a Plot of land.
- I simulated the sensor by just giving the user an API that change the status of a slot.
- So the default slot's status is `NOT_STARTED` .If we change it to `IN_PROGRESS` ,the sensor will irrigate the plot and if we change it to `COMPLETED` ,the sensor will finish his irrigation.
- we can Predict the (slots time / amount of water) by using machine learning OR we can save mocking data in a map and configure the plot based on the input plot. The map has plots with it's corresponding configPlots. OR we can make math equations to predict the configs. [NOT DONE].

<details>
  <summary>Our farm</summary>

![FARM2](https://user-images.githubusercontent.com/77184432/227450923-c6c26059-43a0-427d-acf3-020a2ce96432.png)

</details>

## Architecture

<details>
<summary>N tier architecture</summary>

![arch2](https://user-images.githubusercontent.com/77184432/227481007-5ee7aced-fe64-41c9-a6ca-ecfcb8584c66.png)

</details>

<details>
<summary>Entity Class diagram</summary>

![Untitled Diagram drawio (3)](https://user-images.githubusercontent.com/77184432/227490832-f186fe9c-3501-498a-8e24-045370af3531.png)

</details>

## Installation 📥

To install and run the Spring Boot project, follow these steps:

```bash
> git clone https://github.com/AbdelrahmanShaheen/Automatic-Irrigation-System
> cd Automatic-Irrigation-System/
```

All the dependencies will be installed automatically from `pom.xml` file.

### How to use

To build and run the project, you can use any Java IDE like IntelliJ IDEA or Eclipse.\
OR\
Alternatively, you can build and run the project from the command line:

```bash
> mvn clean package
> mvn spring-boot:run
```

Once the project is running, you can access the REST API endpoints using a tool like curl, Postman, or a web browser.\
By default, the server is running on port `8081` .\
If you want to specify a different port, you can add the following to the application.properties file:

```bash
server.port=8080
```

### Environment Variables

To run this project, you need to set the following environment variables in the application.yml: \
\
`URL` : The URL of your MongoDB database.

```bash

spring:
  data:
    mongodb:
      uri : ${URL}
      database : Plot

```

- Once the server is running, you can use any HTTP client like Postman , cURL or a web browser to interact with the API.
- The available endpoints are:

#### Plots API endpoints

| Methods | Endpoints                  | Access   | Description                                        |
| :------ | :------------------------- | :------- | :------------------------------------------------- |
| `POST`  | `/plots`                   | `public` | `add a plot`                                       |
| `GET`   | `/plots`                   | `public` | `get all plots `                                   |
| `PUT`   | `/plots/{plotId}`          | `public` | `configure a plot`                                 |
| `PATCH` | `/{plotId}/slots/{slotId}` | `public` | `update a specific slot in a plot`                 |
| `PATCH` | `/{plotId}/slots/{slotId}` | `public` | `Act as a sensor by changing the status of a slot` |

## Deployed web server & API Docs

URL : https://automatic-irrigation-system-production.up.railway.app \

#### Add a Plot

```http
  POST /plots
```

| Body       | Type         | Description                                                               |
| :--------- | :----------- | :------------------------------------------------------------------------ |
| `location` | `string`     | **Required**. Its a unique cell inside the grid                           |
| `soilType` | `string`     | **Required**. Type of the soil                                            |
| `cropType` | `string`     | **Required**. Type of the crop                                            |
| `config`   | `PlotConfig` | **Not Required**. Configuration of a plot and it contains a list of slots |

<details>
<summary>
Responses
</summary>

1- status code `201` with these data

```json
{
  "id": "641cb1814f1af66e3919c60d",
  "location": "2,9",
  "soilType": "Loam",
  "cropType": "Corn",
  "config": {
    "slots": [
      {
        "id": "641cb1814f1af66e3919c60b",
        "startTime": "2022-01-01T12:00:00",
        "endTime": "2022-01-01T08:00:00",
        "waterAmount": 150.0,
        "status": "IN_PROGRESS"
      },
      {
        "id": "641cb1814f1af66e3919c60c",
        "startTime": "2022-01-01T09:00:00",
        "endTime": "2022-01-01T11:00:00",
        "waterAmount": 75.0,
        "status": "NOT_STARTED"
      }
    ]
  }
}
```

2- status code `400` when you provide an invalid data inside the body. ex: add a plot in the same location or enter an invalid types.

</details>

#### List all plots

```http
  GET /plots
```

<details>

<summary>
Responses
</summary>

1- status code of `200 ok` with these data :

```json
[
  {
    "id": "641d5debec4daa7d03700897",
    "location": "6,2",
    "soilType": "Clay",
    "cropType": "Soybean",
    "config": {
      "slots": [
        {
          "id": "641d5debec4daa7d03700896",
          "startTime": "2023-04-01T06:00:00",
          "endTime": "2023-04-01T08:00:00",
          "waterAmount": 40.0,
          "status": "NOT_STARTED"
        }
      ]
    }
  },
  {
    "id": "641d5e24ec4daa7d0370089a",
    "location": "2,7",
    "soilType": "Sandy loam",
    "cropType": "Corn",
    "config": {
      "slots": [
        {
          "id": "641d5e24ec4daa7d03700898",
          "startTime": "2023-04-01T09:00:00",
          "endTime": "2023-04-01T11:00:00",
          "waterAmount": 50.0,
          "status": "NOT_STARTED"
        },
        {
          "id": "641d5e24ec4daa7d03700899",
          "startTime": "2023-04-02T16:00:00",
          "endTime": "2023-04-02T18:00:00",
          "waterAmount": 75.0,
          "status": "NOT_STARTED"
        }
      ]
    }
  },
  {
    "id": "641d5e46ec4daa7d0370089b",
    "location": "4,8",
    "soilType": "Loam",
    "cropType": "Rice",
    "config": null
  }
]
```

2- status code of `500` Internal Server Error

</details>

#### Configure a plot

```http
  PUT /plots/plotId
```

| Parameter | Type     | Description                   |
| :-------- | :------- | :---------------------------- |
| `plotId`  | `string` | **Required**. ID of the plot. |

- PlotConfig is a List of slots and each slot has this body :

| Body          | Type            | Description                                                             |
| :------------ | :-------------- | :---------------------------------------------------------------------- |
| `startTime`   | `LocalDateTime` | **Required**.slot's start time                                          |
| `endTime`     | `LocalDateTime` | **Required**.slot's end time                                            |
| `waterAmount` | `double`        | **Required**.amount of water in a specific slot                         |
| `status`      | `enum`          | **Not Required**.status of a slot : NOT_STARTED, IN_PROGRESS, COMPLETED |

<details>
<summary>
Responses
</summary>

1- status code of `200` with these data :

```json
{
  "id": "641d5e46ec4daa7d0370089b",
  "location": "4,8",
  "soilType": "Loam",
  "cropType": "Rice",
  "config": {
    "slots": [
      {
        "id": "641d5debec4daa7d03700896",
        "startTime": "2023-04-01T06:00:00",
        "endTime": "2023-04-01T08:00:00",
        "waterAmount": 40.0,
        "status": "NOT_STARTED"
      }
    ]
  }
}
```

2- status code of `406` NOT_ACCEPTABLE (when you provide an invalid id) with these data :

```json
{
  "message": "Enter a valid plotId"
}
```

3- status code of `400` BAD_REQUEST. with these data :

```json
{
  "message": "slot startTime, endTime and waterAmount cannot be blank"
}
```

4- status code of `404` NOT_FOUND when we do not have a plot with this id with these data :

```json
{
  "message": "Plot not found with ID: 641d0cd0ec4daa7d03700895"
}
```

</details>

#### update a specific slot in a plot

```http
  PUT /plots/plotId/slots/slotId
```

| Parameter | Type     | Description                   |
| :-------- | :------- | :---------------------------- |
| `plotId`  | `string` | **Required**. ID of the plot. |
| `slotId`  | `string` | **Required**. ID of the slot. |

- PlotConfig is a List of slots and each slot has this body :

| Body          | Type            | Description                                                             |
| :------------ | :-------------- | :---------------------------------------------------------------------- |
| `startTime`   | `LocalDateTime` | **Not Required**.slot's start time                                      |
| `endTime`     | `LocalDateTime` | **Not Required**.slot's end time                                        |
| `waterAmount` | `double`        | **Not Required**.amount of water in a specific slot                     |
| `status`      | `enum`          | **Not Required**.status of a slot : NOT_STARTED, IN_PROGRESS, COMPLETED |

<details>
<summary>
Responses
</summary>

1- status code of `200` with these data :

```json
{
  "id": "641d5e46ec4daa7d0370089b",
  "location": "4,8",
  "soilType": "Loam",
  "cropType": "Rice",
  "config": {
    "slots": [
      {
        "id": "641d5debec4daa7d03700896",
        "startTime": "2023-04-01T06:00:00",
        "endTime": "2023-04-01T08:00:00",
        "waterAmount": 40.0,
        "status": "NOT_STARTED"
      }
    ]
  }
}
```

2- status code of `406` NOT_ACCEPTABLE (when you provide an invalid id) with these data :

```json
{
  "message": "Enter a valid slotId"
}
```

3- status code of `400` BAD_REQUEST. with these data :

```json
{
  "message": "Plot configuration cannot be null"
}
```

4- status code of `404` NOT_FOUND when we do not have a plot with this id with these data :

```json
{
  "message": "Plot not found with ID: 641d0cd0ec4daa7d03700895"
}
```

</details>

- Note: we can also use this route to act as a sensor by changing only the status of a specific slot.

## Tech/Framework used 🧰

- [Redux](https://redux.js.org/)
- [Java 17](https://docs.oracle.com/en/java/javase/17/)
- [Spring boot 3.0.4](https://docs.spring.io/spring-boot/docs/3.0.4/)
- [MongoDB](https://www.mongodb.com/)
- [Git](https://git-scm.com/)
- [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
- [Postman](https://www.postman.com/)
- [IntelliJ](https://www.jetbrains.com/idea/)
