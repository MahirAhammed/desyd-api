# Desyd API

A real-time group decision-making REST API built with Spring Boot. Supports multiple voting modes including points-based allocation, ranked choice, and veto voting for democratic group decisions.

### Project Overview

**Desyd** solves the common problem of group indecision (e.g., "Where should we eat?", "Which movie to watch?") by providing a structured voting system with unique session codes for easy participation.
<!--**Live Demo:** [API Documentation](https://vibevote-api.up.railway.app/api/v1/health)  -->

### Key Features

- **JWT Authentication** - Secure user registration and login with access/refresh tokens
- **Session Management** - Create voting sessions with unique 6-character codes
- **Multiple Voting Modes:**
  - **DEFAULT** - Simple yes/no voting (one choice per user)
  - **POINTS** - Distribute 10 points across options
  - **RANKED** - Rank all options by preference
  - **VETO** - Vote for one, veto another
- **Real-time Results** - Automatic calculation when session closes
- **Session Expiry** - Time-limited voting periods

### Tech Stack

| Category | Technologies |
|----------|-------------|
| **Framework** | Spring Boot 4.0.x, Java 21 |
| **Security** | Spring Security, JWT, BCrypt password hashing, input validation |
| **Database** | PostgreSQL 17, Flyway migrations |
| **ORM** | JPA/Hibernate |
| **Architecture** | RESTful API, DTO pattern, service layer |
| **Deployment** | Railway (auto-deploy from GitHub) |

### Database Schema

**8 Tables:**
- `users` - User accounts with BCrypt password hashing
- `user_profiles` - Extended user information
- `sessions` - Voting sessions with unique codes
- `session_options` - Available choices in each session
- `session_participants` - Many-to-many join table (composite key)
- `votes` - User votes with mode-specific validation (composite key)
- `session_results` - Calculated outcomes stored as JSONB
- `session_history` - Audit log *(planned)*

### API Endpoints

##### Authentication
```
POST   /api/v1/users/register      - Create new account
POST   /api/v1/users/login         - Get JWT tokens
POST   /api/v1/users/refresh       - Refresh access token
POST   /api/v1/users/logout        - Invalidate session
GET    /api/v1/users/me            - Get current user profile
```

##### Sessions
```
POST   /api/v1/sessions            - Create voting session
POST   /api/v1/sessions/join       - Join by session code
GET    /api/v1/sessions/{code}     - Get session details
GET    /api/v1/sessions/my-sessions - List user's sessions
POST   /api/v1/sessions/{id}/close - Close session (creator only)
DELETE /api/v1/sessions/{id}       - Delete session (creator only)
```

##### Voting
```
POST   /api/v1/sessions/{id}/votes    - Submit votes
GET    /api/v1/sessions/{id}/votes/me - Get user's votes
DELETE /api/v1/sessions/{id}/votes    - Delete votes (allows re-voting)
```

##### Results
```
GET    /api/v1/sessions/{id}/results  - Get calculated results
```

### Local Development Setup
##### Prerequisites
- Java 21+
- Docker

##### Quick Start
```bash
# Clone repository
git clone https://github.com/yourusername/desyd-api.git
cd desyd-api

# Start PostgreSQL (Docker)
docker compose up -d

# Run application
./mvnw spring-boot:run

# API available at http://localhost:8080
```

##### Environment Variables
```env
# Required
JWT_SECRET=
DATABASE_URL= 
DATABASE_USERNAME=
DATABASE_PASSWORD=

# Optional
JWT_EXPIRATION=86400000           # 24 hours
JWT_REFRESH_EXPIRATION=604800000  # 7 days
CORS_ALLOWED_ORIGINS=*            # Comma-separated origins
```
### Roadmap

**Completed:**
- User authentication with JWT
- Session management with unique codes
- Voting system with 4 modes
- Results calculation
- Exception handling
- CORS configuration

**In Progress:**
- Comprehensive test coverage
- Email verification
- Rate limiting

**Planned:**
- Anonymous voting
- WebSocket for real-time updates
- Session history and analytics
- Auto-expire scheduler for time-limited sessions
- Redis caching for high-traffic scenarios
  
**Note:** This backend is part of a full-stack application. The Flutter mobile frontend is under development.
