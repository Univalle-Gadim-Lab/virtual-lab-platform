# VNC Integration Analysis

## Current Architecture

### Workspace Flow

1. User browses catalog (`GET /api/catalog`) and selects a workspace image
2. Frontend sends `POST /api/instances` with image config and resource specs
3. `InstanceController` -> `InstancesWsOps` -> `InstanceService.createInstance()`
4. `InstanceServiceOperation.createInstance()`:
   - Generates unique instance ID
   - Calls `WorkspaceProvisionerService.createWorkspace()` (creates AND starts Docker container)
   - Persists `InstanceJpa` with status `CREATED`
   - Creates `InstanceUserJpa` association
5. Frontend polls/re-fetches instance list to show status updates

### Container Lifecycle

```
createWorkspace() -> Docker createContainer + startContainer
                     |
                     v
              Instance status: CREATED
                     |
startInstance() -> startContainer (by container ID)
                     |
                     v
              Instance status: RUNNING
                     |
stopInstance() -> stopContainer (by container ID)
                     |
                     v
              Instance status: STOPPED
                     |
deleteInstance() -> soft delete (status: DELETED)
```

**Key observation**: `externalIp` field stores the Docker **container ID** (not an actual IP). `internalIp` is hardcoded to `127.0.0.1`. This means the current "Conectar" button (`http://${externalIp}:${exposedPort}`) is non-functional.

### API Flow

```
Browser -> React SPA -> apiFetch() -> Spring Boot REST API
                                         |
                                    Service Layer -> JPA Repository -> PostgreSQL
                                         |
                                    Docker Daemon (workspace provisioning)
```

### Current "Connect" Behavior

`WorkspaceCard.tsx:68-71`: Opens `http://${workspace.externalIp}:${workspace.exposedPort}` in a new tab. Since `externalIp` is actually the container ID, this is broken. The Docker container exposes port 8080 but has **no host port binding** (`withExposedPorts` without `withPortBindings`).

### Docker Integration Details

- `WorkspaceProvisionerOperation` uses `docker-java` library
- Container created with resource limits (CPU, memory, storage)
- Security: `no-new-privileges:true`
- Persistence: Named volume `vol_{userId}` mounted at `/home/labuser/projects`
- Container name: `workspace-{userId}` (one per user)
- No environment variables set
- No port mapping to host

---

## Integration Points

### Backend Changes Required

| Component | Change | Module |
|-----------|--------|--------|
| `WorkspaceProvisionerOperation` | Expose KasmVNC port (6901), add port binding or use container network for proxy | instances |
| `InstanceJpa` / `Instance` interface | Add `vncPort` field to track KasmVNC port per instance | instances |
| `InstanceResponse` DTO | Add `vncPort` to response | instances |
| `InstanceController` | Add VNC proxy endpoints (HTTP + WebSocket) | instances or boot |
| `SecurityConfig` | Add VNC proxy endpoints to security rules, support WebSocket upgrade | boot |
| Spring Boot dependencies | Add WebSocket support (`spring-boot-starter-websocket`) | boot |
| New: `VncProxyController` | HTTP reverse proxy for KasmVNC web client assets | instances |
| New: `VncWebSocketHandler` | WebSocket proxy forwarding browser <-> container KasmVNC | instances |
| `application.yml` | Add VNC configuration (default port, proxy settings) | boot |

### Frontend Changes Required

| Component | Change | Location |
|-----------|--------|----------|
| New route | `/workspace/:id/desktop` - Remote desktop view | `App.tsx` |
| New component | `RemoteDesktopPage` - Container page with toolbar | `features/workspaces/components/` |
| New component | `VncViewer` - iframe or embedded VNC client | `features/workspaces/components/` |
| `WorkspaceCard` | Update "Conectar" to navigate to desktop route | `features/workspaces/components/WorkspaceCard.tsx` |
| `Instance` type | Add `vncPort` field | `features/workspaces/types.ts` |

### Database Changes Required

| Table | Column | Type | Nullable | Purpose |
|-------|--------|------|----------|---------|
| `instances` | `vnc_port` | `INTEGER` | NULL | KasmVNC port inside the container (default 6901) |

---

## Recommended Solution

### Why KasmVNC

- **Built-in web client**: KasmVNC includes a modern HTML5 viewer served directly from the VNC server, no separate noVNC installation needed
- **WebSocket native**: Built-in WebSocket support without requiring a separate `websockify` layer
- **Better performance**: H.264 encoding support, clipboard sync, audio streaming
- **Security**: Built-in HTTPS support, token-based authentication
- **Active development**: Modern fork of TigerVNC with active maintenance

### How Browser Connection Will Work

```
Browser (React SPA)
    |
    | iframe src: /api/instances/{id}/vnc/
    | (loads KasmVNC web client via backend HTTP proxy)
    v
Spring Boot Backend
    |
    | HTTP proxy: /api/instances/{id}/vnc/** -> container:6901/**
    | WebSocket proxy: /api/instances/{id}/vnc/websockify -> container:6901/websockify
    v
Docker Container (KasmVNC on port 6901)
    |
    | VNC protocol (internal)
    v
X11 / LXDE Desktop (KiCad, etc.)
```

**Flow:**
1. User clicks "Conectar" on a RUNNING workspace card
2. Frontend navigates to `/workspace/:id/desktop`
3. `RemoteDesktopPage` component renders a toolbar + iframe
4. Iframe loads `/api/instances/{id}/vnc/` (backend proxies to container's KasmVNC web client)
5. KasmVNC web client inside iframe establishes WebSocket to `/api/instances/{id}/vnc/websockify`
6. Backend validates JWT (from cookie or query param), checks workspace ownership
7. Backend opens WebSocket to `container_internal_ip:6901/websockify`
8. Bidirectional frame forwarding begins - user sees and interacts with the Linux desktop

### Session Lifecycle

```
User clicks "Conectar"
    |
    v
Frontend: Navigate to /workspace/:id/desktop
    |
    v
Frontend: Verify instance is RUNNING (fetch instance by ID)
    |
    v
Frontend: Render iframe -> /api/instances/{id}/vnc/
    |
    v
Backend: Validate JWT + ownership -> proxy HTTP to container:6901
    |
    v
KasmVNC web client loads in iframe
    |
    v
KasmVNC client: WebSocket connect -> /api/instances/{id}/vnc/websockify
    |
    v
Backend: Validate + open WebSocket to container -> bidirectional proxy
    |
    v
User interacts with remote desktop
    |
    v
User clicks "Disconnect" or navigates away
    |
    v
Frontend: WebSocket closes -> backend closes container connection
    |
    v
Container continues running (workspace not affected)
```

### Docker Image Requirements

The Docker images need KasmVNC installed and configured. The plan includes:

1. **Verification step**: Check if `lab-kicad:latest` already has KasmVNC
2. **Base image creation** (if needed): Create a Dockerfile that installs:
   - Ubuntu 24.04 base
   - LXDE desktop environment
   - KasmVNC server
   - Target application (KiCad, Vivado, etc.)
3. **Entrypoint**: Container startup script that launches Xvfb + KasmVNC + window manager
4. **KasmVNC config**: Listen on port 6901, no password (auth handled by platform JWT)
