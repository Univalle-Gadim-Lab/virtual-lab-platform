# Vivado Workspace Image

Xilinx **Vivado** is the FPGA design suite used in the lab. Because Vivado
is a proprietary tool distributed under Xilinx's licensing terms, this
folder contains a **build skeleton**: code that produces a containerised
XFCE desktop exposing the IDE through KasmVNC, but only once the operator
supplies a valid Xilinx installer.

## Layout

```
vivado/
├── Dockerfile              # build skeleton; expects an installer context
├── kasmvnc.yaml            # KasmVNC server configuration (port 6901)
├── xstartup                # XFCE session with dbus-launch
├── entrypoint.sh           # launches KasmVNC + sets the per-instance password
└── docker-compose.yml      # convenience wrapper for local validation
```

## Why a Skeleton (not a Ready-to-Build Image)?

The Xilinx Unified Installer is a binary artifact that:

- requires a Xilinx user account with a valid entitlement
- carries product license terms that prohibit redistribution
- is large (~30 GB for the full Vivado install)

This repository therefore ships only the **layer that wires XFCE +
KasmVNC + Vivado together**. The installer is supplied at build time via
a Docker `--build-context`.

## Building the Image

### 1. Prepare the installer context

```
docker-context/installer/
├── Xilinx_Unified_2023.2   ← rename to match the VIVADO_VERSION arg (default 2023.2)
└── vivado-installer.cfg    ← your unattended-install config
```

The `vivado-installer.cfg` file selects the device families your FPGA
lab actually needs. Example skeleton:

```
Edition=Vivado HL Design Edition
Product=Vivado
Version=2023.2
OS=Linux 64-bit
InstallDirectory=/opt/Xilinx
CreateProgramGroupShortcuts=false
CreateDesktopShortcuts=false
CreateFileAssociations=false
InstallOptions=Acquire or Manage a License Key
AcceptLicense=true
```

Keep only the device families the platform will support; the resulting
image shrinks dramatically.

### 2. Build the image

```bash
docker build \
  --build-context installer=docker-context/installer \
  -t lab-vivado:2023.2 \
  .
```

If the installer is missing, the build fails fast inside the
`RUN ... /tmp/xilinx-installer.bin --config ...` instruction — by
design, to prevent shipping a non-functional image.

### 3. Run locally for smoke-testing

```bash
KASMVNC_PASSWORD=changeme docker compose up -d
docker logs -f vivado
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
      - id: vivado
        name: Vivado
        version: 2023.2
        image: lab-vivado:2023.2
        category: FPGA
```

The `WorkspaceProvisionerOperation` will provision a container from this
image every time a student requests a Vivado workspace. VNC access is
proxied through the `VncProxyController` and `VncWebSocketProxyHandler`
using the per-instance `KASMVNC_PASSWORD` injected by the backend.
