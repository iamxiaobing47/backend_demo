# PostgreSQL Database Setup Instructions (Updated)

## Prerequisites

- PostgreSQL server must be installed and running
- PostgreSQL command line tools (psql) must be in your PATH
- Database user with appropriate permissions (typically 'postgres')
- Backend server running on port 8080

## Step 1: Create Database (if not exists)

```bash
createdb -U postgres backend_demo
```

## Step 2: Execute Schema Setup

```bash
psql -U postgres -d backend_demo -f "src/main/resources/db/postgresql_schema-setup.sql"
```

## Step 3: Start Backend Server

Before creating test users, make sure the backend server is running:

```bash
cd backend_demo
./gradlew bootRun
```

## Step 4: Create Test Users via API (Properly Encrypted)

Instead of manually inserting encrypted passwords into the database, use the API endpoint to create users with properly encrypted passwords:

### Option A: Use the automated script

```bash
# Run the batch script to create users via API
create-test-users-via-api.bat
```

### Option B: Manual API calls

Make these API calls to create users with properly encrypted passwords:

```bash
# Create business owner A
curl -X POST http://localhost:8080/api/auth/create-test-user \
-H "Content-Type: application/json" \
-d "{\"email\":\"owner_a@example.com\", \"password\":\"password123\"}"

# Create business owner B
curl -X POST http://localhost:8080/api/auth/create-test-user \
-H "Content-Type: application/json" \
-d "{\"email\":\"owner_b@example.com\", \"password\":\"password123\"}"

# Create staff at location X
curl -X POST http://localhost:8080/api/auth/create-test-user \
-H "Content-Type: application/json" \
-d "{\"email\":\"staff_x@example.com\", \"password\":\"password123\"}"

# Create staff at location Y
curl -X POST http://localhost:8080/api/auth/create-test-user \
-H "Content-Type: application/json" \
-d "{\"email\":\"staff_y@example.com\", \"password\":\"password123\"}"

# Create general test user
curl -X POST http://localhost:8080/api/auth/create-test-user \
-H "Content-Type: application/json" \
-d "{\"email\":\"test@example.com\", \"password\":\"password123\"}"
```

## Step 5: Execute Association Data Insertion

Now that the users exist with properly encrypted passwords, set up their business/staff associations:

```bash
psql -U postgres -d backend_demo -f "RESTORE_TEST_DATA.sql"
```

## Alternative: Execute Commands Directly in PostgreSQL

### Connect to PostgreSQL

```bash
psql -U postgres -d backend_demo
```

Then execute the content of RESTORE_TEST_DATA.sql manually by copying and pasting into the PostgreSQL prompt.

## Test Accounts (Ready to Use!)

After setup, the following test accounts will be available:

| User Type           | Email               | Password    | Access Level               |
| ------------------- | ------------------- | ----------- | -------------------------- |
| Business Owner A    | owner_a@example.com | password123 | Can access "项目列表"      |
| Business Owner B    | owner_b@example.com | password123 | Can access "文件模板下载"  |
| Staff at Location X | staff_x@example.com | password123 | Can access ALL menus       |
| Staff at Location Y | staff_y@example.com | password123 | Can access "项目列表" only |

## Verify Setup

To verify the data was inserted correctly, connect to the database and run:

```sql
-- Check users were created with encrypted passwords
SELECT email, LENGTH(password_hash) FROM passwords WHERE email LIKE '%@example.com';

-- Check business users
SELECT * FROM business_users;

-- Check staff users
SELECT * FROM staff_users;

-- Check navigations
SELECT * FROM navigations;
```

## Start Frontend

Once the database is set up and backend is running:

### Frontend

```bash
cd ../frontend_demo
npm run dev
```

The application will be available at http://localhost:5173

## Notes

- Passwords are now properly encrypted using the system's password encoder
- The create-test-user API endpoint ensures bcrypt encryption is done correctly
- Login should now work with the specified passwords
- The dynamic menu system will function based on user roles and associations
