# RPG Inventory Management API

## Project Description
The RPG Inventory Management API is a mock RESTful for managing players, inventories, items, equipment, and more, while providing authentication and role-based access control. It is designed for demonstration purposes and can be used as a starting point for building more complex inventory management systems.

---

## Key Features

- **Inventory Management**: Manage player inventories, add/remove items, and handle transactions like buying and selling.
- **Equipment System**: Equip or unequip items to player characters.
- **Item Management**: Create, update, and categorize items by rarity or type.
- **Authentication and Security**: Includes authentication using JWT (JSON Web Tokens) via Spring Security and JJWT.
- **Admin Capabilities**: Administrative endpoints for managing players, items, slots, and more.
- **Mock Data Loading**: Configurable environment variable to load initial mock data on startup.

---

## Technologies Used

### Backend Technologies
- **Java 17**: The programming language for the project.
- **Spring Boot**: Core framework for building the API.
- **Spring Security**: Provides authentication and role-based access control.
- **JJWT (Java JWT)**: For generating and validating JWT tokens.
- **Spring Data JPA**: Simplifies interaction with the PostgreSQL database.
- **Hibernate**: ORM tool for mapping Java objects to database tables.
- **PostgreSQL**: Relational database for storing API data.
- **Docker**: For containerizing the application.
- **NGINX**: Reverse proxy and SSL termination.

---

## Endpoints Overview

The `/register` and `/login` endpoints are available for everyone, then it is required to provide the corresponding user token (provided by `/login`) for accessing the different resources of the API.
The `/admin` path can only be accessed if the token belongs to an admin user.
If an endpoint includes a `playerId` the provided token needs to belong to the user that created that player.  

### Authentication
- `POST /register`: Register a new user with the default USER role.
- `POST /login`: Authenticate a user and generate a JWT token.

### Admin Management
- `POST /admin/items/categories`: Creates a new item category.
- `POST /admin/items/rarities`: Creates a new item rarity.
- `POST /admin/slots`: Creates a new equipment slot.
- `POST /admin/items`: Creates a new item.
- `PUT /admin/items/categories/{itemCategoryId}`: Updates an item category.
- `PUT /admin/items/rarities/{itemRarityId}`: Updates an item rarity.
- `PUT /admin/slots/{slotId}`: Updates an equipment slot.
- `PUT /admin/items/{itemId}`: Updates an item.
- `DELETE /admin/items/categories/{itemCategoryId}`: Deletes an item category.
- `DELETE /admin/items/rarities/{itemRarityId}`: Deletes an item rarity.
- `DELETE /admin/slots`: Deletes an equipment slot.
- `DELETE /admin/items/{itemId}`: Deletes an item.
- `GET /admin/players`: Retrieve all players.
- `DELETE /admin/players/{playerId}`: Delete a player.
- `DELETE /admin/players/{playerId}/inventory`: Delete a player's inventory.
- `POST /admin/players/{playerId}/inventory/items`: Add an item to a player's inventory.
- `DELETE /admin/players/{playerId}/inventory/items`: Remove an item from the player's inventory.
- `DELETE /admin/players/{playerId}/inventory/items/clear`: Remove all items from the player's inventory.

### Player Management
- `POST /players`: Create a new player.
- `GET /players`: Retrieve all players created by the logged user.
- `GET /players/{playerId}`: Retrieve a specific player.

### Inventory Management
- `POST /players/{playerId}/inventory`: Create inventory for a player.
- `POST /players/{playerId}/inventory/buy`: Buy an item.
- `POST /players/{playerId}/inventory/sell`: Sell an item.
- `GET /players/{playerId}/inventory`: Get a player's inventory.
- `GET /players/{playerId}/inventory/items`: Get a list of all items in the player's inventory.

### Equipment Management
- `POST /players/{playerId}/equipment`: Equip an item.
- `POST /players/{playerId}/equipment/unequip`: Unequip an item.
- `GET /players/{playerId}/equipment`: Get equipped items.

### Item Management
- `GET /items/categories`: Lists all item categories.
- `GET /items/rarities`: Lists all item rarities.
- `GET /slots`: Lists all equipment slots available.
- `GET /items`: Lists all items.
- `GET /items/{itemId}`: Fetches a specific item.
- `GET /items`: Get all items (pageable and can be filtered with ?categoryId={itemCategoryId}&rarityId={itemRarityId}).
---


## Environment Variables

The application requires two key environment variables:

- **`ADMIN_PASSWORD`**: Specifies the admin password. If not provided, a random password is generated and logged.
- **`LOAD_MOCK_DATA`**: Set to `true` to load initial mock data into the database on startup.

---

## Build and Deployment Instructions

### Prerequisites
1. Docker and Docker Compose installed.
2. JDK 17 installed for local development.
### Build Instructions
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd rpg-inventory
   ```
2. Build the project using maven:
   ```bash
   ./mvnw install
   ```
3. Build the docker image:
   ```bash
   docker compose build
   ```
4. Optionally generate self-signed certificates:
   ```bash
   ./scripts/generate-certificates.sh
   ```
5. Start the application:
- Without HTTPS:
   ```bash
   docker compose up -d
   ```
- With HTTPS:
   ```bash
   docker compose -f docker-compose.yml -f docker-compose.https.yml up -d 
   ```
6. Access the API
- HTTP: `http://localhost:8080`
- HTTPS: `https://localhost:8443`

## Example Usage
- First authenticate and store the token.
  ```bash
  token=$(curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username": "user", "password": "test"}' | jq -r '.token')
  ```
- Get players for that user
  ```bash
  curl -X GET http://localhost:8080/players \
  -H "Authorization: Bearer $token"}
  ```
  Response:
  ```bash
  [
    {
      "id": 1,
      "name": "player",
      "gold": 9999
    }
  ]
  ```

- Buy item with id 1
  ```bash
  curl -X POST http://localhost:8080/players/1/inventory/items/buy \
  -H "Authorization: Bearer $token" \
  -H "Content-Type: application/json" \
  -d '{"itemId": 1}'
  ```
  Response:
  ```bash
  {
    "inventoryItemId": 3,
    "inventoryId": 1,
    "item": {
      "id": 1,
      "name": "Iron Sword",
      "description": "A sturdy sword forged from iron. Reliable for any beginner adventurer.",
      "itemRarity": {
        "id": 1,
        "name": "common"
      },
      "validSlot": {
        "id": 1,
        "name": "dominant hand"
      },
      "value": 150,
      "itemCategory": {
        "id": 1,
        "name": "weapon"
      }
    }
  }
  ```

## Request body format
Endpoints that are not in this section do not require a request body.

- `POST /admin/items/categories`
    ```
    {
      "name": String
    }
    ```

- `POST /admin/items/rarities`
    ```
    {
      "name": String
    }
    ```

- `POST /admin/slots`
    ```
    {
      "slotFields": String
    }
    ```

- `POST /admin/items`
    ```
    {
      "name": String,
      "description": String,
      "itemCategoryId": Long,
      "itemRarityId": Long,
      "validSlotId": Long,
      "value": Integer
    }
    ```

- `PUT /admin/items/categories/{itemCategoryId}`
    ```
    {
      "name": String
    }
    ```

- `PUT /admin/items/rarities/{itemRarityId}`
    ```
    {
      "name": String
    }
    ```

- `PUT /admin/slots/{slotId}`
    ```
    {
      "slotFields": String
    }
    ```

- `PUT /admin/items/{itemId}`
    ```
    {
      "name": String,
      "description": String,
      "itemCategoryId": Long,
      "itemRarityId": Long,
      "validSlotId": Long,
      "value": Integer
    }
    ```

- `POST /admin/players/{playerId}/inventory/items`
    ```
    {
      "itemId": Long
    }
    ```

- `DELETE /admin/players/{playerId}/inventory/items`
    ```
    {
      "inventoryItemId": Long
    }
    ```

- `POST /players`
    ```
    {
      "name": String
    }
    ```

- `POST /players/{playerId}/inventory/items/buy`
    ```
    {
      "itemId": Long
    }
    ```

- `POST /players/{playerId}/inventory/items/sell`
    ```
    {
      "inventoryItemId": Long
    }
    ```

- `POST /players/{playerId}/equipment`
    ```
    {
      "inventoryId": Long
    }
    ```

- `POST /players/{playerId}/equipment/unequip`
    ```
    {
      "inventoryItemId": Long
    }
    ``` 
