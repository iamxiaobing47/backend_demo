# Agent Instructions for backend_demo Project

## Project Overview

This is a Spring Boot backend application with a Vue 3 + Vuetify frontend. The backend uses Java 21, Spring Boot 3.3.5, MyBatis-Plus, and JWT authentication. The frontend uses TypeScript, Vue 3, Pinia for state management, and Vuetify for UI components.

## Build Commands

### Backend (Spring Boot)
```bash
# Build the application
./gradlew build

# Run the application (development)
./gradlew bootRun

# Build and run as JAR
./gradlew build
java -jar build/libs/backend_demo-0.0.1-SNAPSHOT.jar

# Clean build artifacts
./gradlew clean
```

### Frontend (Vue 3)
```bash
# Install dependencies
npm install

# Development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

## Test Commands

### Backend Testing
```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "com.taco.backend_demo.BackendDemoApplicationTests"

# Run specific test method
./gradlew test --tests "com.taco.backend_demo.BackendDemoApplicationTests.testMethodName"

# Run tests with debug logging
./gradlew test --debug

# Generate test reports
./gradlew test --info
# Reports available at: build/reports/tests/test/index.html
```

### Frontend Testing
The project currently doesn't have frontend tests configured. When adding tests, use:
```bash
# For Vue Test Utils + Vitest (recommended)
npm run test:unit

# For E2E tests with Cypress
npm run test:e2e
```

## Linting and Formatting

### Backend
- Uses standard Java conventions with Lombok annotations
- No explicit linter configured, but follows Spring Boot best practices
- Use IDE auto-formatting with standard Java settings

### Frontend
```bash
# Format code with Prettier
npm run format

# Lint with ESLint (if configured)
npm run lint

# Type checking
npm run build  # vue-tsc is part of the build process
```

## Code Style Guidelines

### Java Backend

#### Imports
- Use specific imports, not wildcards (`import java.util.List` not `import java.util.*`)
- Organize imports in this order:
  1. Java standard library
  2. Third-party libraries (Spring, MyBatis, etc.)
  3. Project-specific imports
- Remove unused imports

#### Formatting
- Use 4-space indentation
- Line length: 120 characters maximum
- Braces on same line for control structures
- One statement per line
- Blank lines between methods and logical code blocks

#### Types and Naming
- **Classes**: PascalCase (`AuthController`, `PasswordEntity`)
- **Methods**: camelCase (`generateToken`, `validatePassword`)
- **Variables**: camelCase (`accessToken`, `refreshToken`)
- **Constants**: UPPER_SNAKE_CASE (`LOGIN_SUCCESS`, `MAX_RETRY_COUNT`)
- **Packages**: lowercase with dots (`com.taco.backend_demo.security`)

#### Annotations
- Use Lombok annotations extensively:
  - `@Data` for DTOs and entities
  - `@AllArgsConstructor`, `@NoArgsConstructor` as needed
  - `@RequiredArgsConstructor` for dependency injection
- Spring annotations:
  - `@RestController`, `@Service`, `@Component`, `@Repository`
  - `@Autowired` for dependency injection
  - `@Valid` for validation

#### Error Handling
- Use custom exception hierarchy:
  - `BaseException` as base class
  - `BusinessException` for business logic errors
- Global exception handling via `GlobalExceptionHandler`
- Use standardized error codes from `ErrorMessageCodes.java`
- Always log exceptions with appropriate log level

#### Validation
- Use JSR-380 validation annotations (`@NotBlank`, `@Email`, `@Size`)
- Create custom validators when needed (`@PasswordStrength`, `@PhoneNumber`)
- Handle validation errors in `GlobalExceptionHandler`
- Return field-level validation errors with message codes

#### Security
- JWT-based authentication
- Store refresh tokens in HttpOnly cookies
- Access tokens in Authorization header
- Use Spring Security for authentication/authorization
- Never log sensitive information (passwords, tokens)

### TypeScript Frontend

#### Imports
- Use absolute paths with `@` alias: `import { useAuthStore } from "@/stores/auth"`
- Group imports in this order:
  1. Vue core imports
  2. Third-party libraries
  3. Project-specific imports
- Remove unused imports

#### Formatting
- Use 2-space indentation
- Line length: 100 characters maximum
- Single quotes for strings
- Semicolons at end of statements
- Consistent spacing around operators and commas

#### Types and Naming
- **Interfaces**: PascalCase with `I` prefix or descriptive name (`UserInfo`, `ApiResponse`)
- **Functions**: camelCase (`handleLogin`, `refreshToken`)
- **Variables**: camelCase (`accessToken`, `isAuthenticated`)
- **Constants**: UPPER_SNAKE_CASE or camelCase depending on scope
- **Stores**: Use `use` prefix (`useAuthStore`, `useValidation`)

#### Composition API
- Use `<script setup>` syntax for Vue components
- Organize composables logically
- Return only necessary functions/variables from composables
- Use TypeScript interfaces for props and emits

#### Error Handling
- Use try-catch blocks for async operations
- Log errors to console during development
- Handle API errors gracefully with user feedback
- Use centralized error handling in axios interceptors

#### State Management
- Use Pinia for global state management
- Store sensitive data (tokens) in localStorage
- Keep UI state in component refs/reactive variables
- Clear state properly on logout

## API Generation

### Generate Frontend API Client
```bash
# Ensure backend is running on port 8080
cd ../backend_demo && ./gradlew bootRun

# In frontend directory, regenerate API
cd ../frontend_demo && npm run api:regenerate

# Or use the Gradle task
cd ../backend_demo && ./gradlew generateFrontendApi
```

### API Client Structure
- Generated files are in `src/services/generated/`
- Do not modify generated files directly
- Custom API logic should be in `src/services/` or composables

## Development Workflow

### Backend Development
1. Make changes to Java code
2. Run `./gradlew bootRun` for hot reload
3. Test endpoints via Swagger UI at `http://localhost:8080/swagger-ui.html`
4. Run tests with `./gradlew test`

### Frontend Development
1. Ensure backend is running
2. Run `npm run dev` to start development server
3. Access frontend at `http://localhost:3000` (or next available port)
4. API changes require regenerating client with `npm run api:regenerate`

### Port Management
- Backend: port 8080
- Frontend: ports 3000-3002
- Use provided Gradle tasks to clean ports:
  ```bash
  ./gradlew cleanAll    # Clean all related ports
  ./gradlew stopBackend # Stop backend service
  ```

## Special Considerations

### Message Codes
- All responses use standardized message codes
- Error codes start with `E` (E001, E014, etc.)
- Success codes start with `N` (N020, N024, etc.)
- Warning codes start with `W`
- Always use existing codes or add new ones to appropriate constants file

### Database Entities
- Use MyBatis-Plus for database operations
- Entities extend `BaseEntity` for common fields
- Use Lombok `@Data` for getters/setters
- Primary keys are handled by database sequences

### Authentication Flow
- Login returns both access and refresh tokens
- Access token stored in localStorage
- Refresh token stored in HttpOnly cookie
- Token refresh handled automatically by auth store
- Route guards protect authenticated routes

Follow these guidelines to maintain code consistency and quality across the project.