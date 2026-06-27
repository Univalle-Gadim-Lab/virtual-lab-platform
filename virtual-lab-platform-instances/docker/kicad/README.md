# KiCad Workspace Image

Docker image for the Virtual Lab Platform. Provides a full XFCE desktop environment with KiCad EDA tools and KasmVNC for browser-based remote access.

## Prerequisites

- Docker 20.10+ (or Docker Desktop)
- ~5 GB of free disk space (image + layers)

## Build

```bash
cd virtual-lab-platform-instances/docker/kicad
docker build -t lab-kicad:latest .
```

## Run (standalone testing)

```bash
docker run -d \
  -p 6901:6901 \
  -e KASMVNC_PASSWORD=test123 \
  -e VNC_RESOLUTION=1920x1080 \
  --shm-size=2g \
  --name kicad-test \
  lab-kicad:latest
```

Then open `http://localhost:6901` and enter the password `test123`.

## Run (with docker-compose)

```bash
docker compose up -d
```

The compose file exposes port 6901 with a persistent volume for `/home/labuser/projects`.

## Architecture

```
entrypoint.sh
  ├─ Reads KASMVNC_PASSWORD from environment
  ├─ Configures VNC password via vncpasswd
  └─ Starts KasmVNC with XFCE desktop

KasmVNC (port 6901)
  ├─ WebSocket support for browser clients
  ├─ HTML5 web client (no plugins needed)
  └─ Per-instance password authentication
```

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `KASMVNC_PASSWORD` | (none) | VNC password for remote desktop access |
| `VNC_RESOLUTION` | `1920x1080` | Desktop resolution |

## Integration with Platform

The platform's `WorkspaceProvisionerOperation` builds this image as `lab-kicad:latest` if it doesn't exist locally. Each instance gets a unique VNC password passed via `KASMVNC_PASSWORD` environment variable.
