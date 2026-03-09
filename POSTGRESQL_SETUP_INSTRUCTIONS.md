# PostgreSQL Database Setup Instructions

## Prerequisites

- PostgreSQL server must be installed and running
- PostgreSQL command line tools (psql, createdb) must be in your PATH
- Database user with appropriate permissions (typically 'postgres')

## Step 1: Create Database (if not exists)

```bash
createdb -U postgres backend_demo
```

## Step 2: Execute Schema Setup

```bash
psql -U postgres -d backend_demo -f "src/main/resources/db/postgresql_schema-setup.sql"
```

## Step 3: Execute Test Data Insertion

```bash
psql -U postgres -d backend_demo -f "src/main/resources/db/postgresql_insert-test-data.sql"
```

## Alternative: Execute Commands Directly in PostgreSQL

If you prefer to run the commands directly in PostgreSQL:

### Connect to PostgreSQL

```bash
psql -U postgres
```

Then execute each SQL file content manually by copying and pasting into the PostgreSQL prompt.

## Test Accounts

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
-- Check navigations
SELECT * FROM navigations;

-- Check business users
SELECT * FROM business_users;

-- Check staff users
SELECT * FROM staff_users;

-- Check passwords
SELECT email FROM passwords WHERE email LIKE '%@example.com';
```

## Start Application

Once the database is set up, start the backend and frontend:

### Backend

```bash
./gradlew bootRun
```

### Frontend

```bash
cd ../frontend_demo
npm run dev
```

The application will be available at http://localhost:5173
