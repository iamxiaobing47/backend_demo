// Script to create test users via the API instead of direct database insertion
const axios = require("axios");

async function createTestUser(email, password) {
  try {
    const response = await axios.post(
      "http://localhost:8080/api/auth/create-test-user",
      {
        email: email,
        password: password,
      },
      {
        headers: {
          "Content-Type": "application/json",
        },
      },
    );

    console.log(`✓ Successfully created user: ${email}`);
    return response.data;
  } catch (error) {
    console.error(
      `✗ Failed to create user: ${email}`,
      error.response?.data || error.message,
    );
  }
}

async function createBusinessUser(email, businessId, name, position) {
  const mysql = require("mysql2/promise");

  try {
    // Connect to database
    const connection = await mysql.createConnection({
      host: "localhost",
      user: "postgres", // Update this to your DB user
      password: "", // Update this to your DB password
      database: "backend_demo",
    });

    // Insert into business_users table
    const query = `
      INSERT INTO business_users (business_user_id, business_id, email, name, position, created_at, updated_at) 
      VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
    `;

    await connection.execute(query, [
      businessId,
      businessId,
      email,
      name,
      position,
    ]);
    console.log(`✓ Successfully created business user: ${email}`);

    await connection.end();
  } catch (error) {
    console.error(`✗ Failed to create business user: ${email}`, error.message);
  }
}

async function createStaffUser(email, locationId, name, position) {
  const mysql = require("mysql2/promise");

  try {
    // Connect to database
    const connection = await mysql.createConnection({
      host: "localhost",
      user: "postgres", // Update this to your DB user
      password: "", // Update this to your DB password
      database: "backend_demo",
    });

    // Insert into staff_users table
    const query = `
      INSERT INTO staff_users (staff_user_id, location_id, email, name, position, created_at, updated_at) 
      VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
    `;

    await connection.execute(query, [
      locationId,
      locationId,
      email,
      name,
      position,
    ]);
    console.log(`✓ Successfully created staff user: ${email}`);

    await connection.end();
  } catch (error) {
    console.error(`✗ Failed to create staff user: ${email}`, error.message);
  }
}

async function main() {
  console.log("Creating test users via API...\n");

  // First create the password-hashed users via API
  await createTestUser("owner_a@example.com", "password123");
  await createTestUser("owner_b@example.com", "password123");
  await createTestUser("staff_x@example.com", "password123");
  await createTestUser("staff_y@example.com", "password123");
  await createTestUser("test@example.com", "password123");

  console.log(
    "\nNow run the following SQL to create business and staff associations:",
  );
  console.log(`
-- Create business users (事业者)
INSERT INTO business_users (business_user_id, business_id, email, name, position, created_at, updated_at) VALUES
(1, 1, 'owner_a@example.com', 'Business Owner A', 'Owner', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 'owner_b@example.com', 'Business Owner B', 'Owner', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Create staff users (职员)  
INSERT INTO staff_users (staff_user_id, location_id, email, name, position, created_at, updated_at) VALUES
(1, 1, 'staff_x@example.com', 'Staff X', 'Manager', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 'staff_y@example.com', 'Staff Y', 'Employee', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
  `);
}

main().catch(console.error);
