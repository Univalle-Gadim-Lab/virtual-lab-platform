# Quartus Prime Workspace Image

Intel **Quartus Prime** is the FPGA design tool used in the lab. Because
Quartus is a proprietary tool distributed under Intel's licensing terms,
this folder contains a **build skeleton**: code that produces a containerised
XFCE desktop exposing the IDE through KasmVNC, but only once the operator
supplies a valid Intel/Altera installer.

## Layout

```
quartus/
├── Dockerfile              # build skeleton; expects an installer context
├── kasmvnc.yaml            # KasmVNC server configuration (port 6901)
├── xstartup                # XFCE session with dbus-launch
├── entrypoint.sh           # launches KasmVNC + sets the per-instance password
└── docker-compose.yml      # convenience wrapper for local validation
```

## Why a Skeleton (not a Ready-to-Build Image)?

The Quartus Prime installer is a binary artifact that:

- requires an Intel (formerly Altera) entitlement account
- carries product license terms that prohibit redistribution
- is large (~10 GB plus several device support pl.qdz files)

This repository therefore ships only the **layer that wires XFCE +
KasmVNC + Quartus together**. The installer and the required device
support packages are supplied at build time via a Docker
`--build-context`.

## Building the Image

### 1. Prepare the installer context

```
docker-context/installer/
├── QuartusPrimeSetup-22.1-linux.run          ← Quartus install script
└── devices/                                  ← device support packages (qdz)
    ├── cyclone10gx-22.1.qdz
    ├── cyclonev-22.1.qdz
    └── max10-22.1.qdz
```

Keep only the device families the platform will support; the resulting
image shrinks dramatically.

### 2. Build the image

```bash
docker build \
  --build-context installer=docker-context/installer \
  -t lab-quartus:22.1 \
  .
```

If the installer file is missing, the build fails fast inside the
`RUN ... /tmp/quartus-installer.run --mode unattended ...`
instruction — by design, to prevent shipping a non-functional image.

### 3. Run locally for smoke-testing

```bash
KASMVNC_PASSWORD=changeme docker compose up -d
docker logs -f quartus
```

Then point a VNC client at `http://localhost:6901` with the chosen
password.

## Integration with virtual-lab-platform

Once the image is built and pushed to a registry, register it in the
catalog by editing `application.yml`:

```yaml
workspace:
  catalog:
    images:
      - id: quartus
        name: Quartus Prime
        version: 22.1
        image: lab-quartus:22.1
        category: FPGA
```

The `WorkspaceProvisionerOperation` will provision a container from this
image every time a student requests a Quartus workspace. VNC access is
proxied through the `VncProxyController` and `VncWebSocketProxyHandler`
using the per-instance `KASMVNC_PASSWORD` injected by the backend.
