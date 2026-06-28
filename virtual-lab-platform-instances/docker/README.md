# Workspace Images

This directory contains the **Dockerfiles and runtime configs** for every
workspace image that the `WorkspaceProvisionerOperation` can provision.

Each sub-directory is a self-contained image "kit". They all share the
same architecture:

| Layer | Purpose |
|-------|---------|
| Ubuntu 24.04 | Base OS |
| XFCE (panel, terminal) | The desktop session users see in the iframe |
| dbus + `dbus-launch` | Required to start XFCE inside a headless container |
| KasmVNC server (port 6901) | Web-based remote desktop; the only thing the backend proxies |
| `entrypoint.sh` | Wires KasmVNC, generates the per-instance password and tears down on signal |

The frontend interacts with these containers through
`VncProxyController` and `VncWebSocketProxyHandler`, which forward HTTP
assets and WebSocket frames to port 6901 over `localhost` after JWT +
ownership verification.

## Available Images

| Sub-directory | Status | Notes |
|---|---|---|
| `kicad/` | ✅ Ready to build | Open-source (KiCad from `apt`), builds end-to-end with `docker build` |
| `vivado/` | 🔒 Build skeleton | Xilinx proprietary; expects an installer supplied at build time |
| `quartus/` | 🔒 Build skeleton | Intel/Altera proprietary; expects an installer supplied at build time |

## Building a Workspace Image

For the open-source image (`kicad`):

```bash
cd kicad
docker build -t lab-kicad:latest .
docker compose up -d
docker logs -f kicad
```

For the proprietary images (`vivado`, `quartus`), follow the per-folder
`README.md`:

1. Download the installer + device support packages from the vendor's
   download portal under a valid entitlement account.
2. Place them under a `docker-context/installer/` tree.
3. Run `docker build --build-context installer=docker-context/installer …`.

The image labels in `application.yml` (`lab-kicad:latest`,
`lab-vivado:2023.2`, `lab-quartus:22.1`) must match the image tags you
push.

## Per-Container VNC Password

Every container is provisioned with a fresh 12-character random password
that the backend injects as `KASMVNC_PASSWORD` into the container's
environment. The `entrypoint.sh` script maps it into the KasmVNC password
file before starting the server, and the same password is exposed to the
browser via a `?password=` parameter on the VNC URL returned by the
remote-session endpoint.

This means:

- Each container has a unique password (rejected if reused).
- Operators do not have to provision or rotate passwords manually.
- The password is the only thing the browser needs; the JWT guards the
  `/api/instances/{id}/vnc/**` HTTP path and the websocket upgrade is
  authenticated via the `vnc_token` cookie/header.
