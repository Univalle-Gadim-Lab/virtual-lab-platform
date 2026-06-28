FROM ubuntu:24.04

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y --no-install-recommends \
    xfce4 \
    xfce4-terminal \
    xfce4-goodies \
    dbus-x11 \
    wget \
    ca-certificates \
    libncurses5 \
    libstdc++6 \
    libgtk2.0-0 \
    libxtst6 \
    libglu1-mesa \
    && rm -rf /var/lib/apt/lists/*

ARG TARGETARCH
ARG KASMVNC_VERSION=1.4.0

RUN if [ "$TARGETARCH" = "arm64" ]; then \
        ARCH="arm64"; \
    else \
        ARCH="amd64"; \
    fi \
    && wget -q "https://github.com/kasmtech/KasmVNC/releases/download/v${KASMVNC_VERSION}/kasmvncserver_noble_${KASMVNC_VERSION}_${ARCH}.deb" \
    -O /tmp/kasmvnc.deb \
    && apt-get update \
    && apt-get install -y --no-install-recommends /tmp/kasmvnc.deb \
    && rm -f /tmp/kasmvnc.deb \
    && rm -rf /var/lib/apt/lists/*

# ============================================================
# VIVADO INSTALLATION (PROPRIETARY, OPERATOR-PROVIDED)
# ============================================================
#
# Vivado is a proprietary Xilinx tool. The Xilinx Unified Installer
# (e.g. Xilinx_Unified_<version>_<build>_Lin64.bin) must be supplied
# by the image builder due to licensing terms. This Dockerfile
# mounts the installer in /tmp/installer (via a build secret or
# bind-mount) and runs the batch installer with a config file.
#
# Expected preparation by the operator:
#   1. Download the Xilinx Unified Installer from xilinx.com using a
#      valid entitlement account.
#   2. Generate an unattended-install config (e.g. `vivado-installer.cfg`)
#      that selects the device families your FPGA lab needs and accepts
#      the license agreement.
#   3. Place both files under `docker-context/installer/` and pass the
#      build with:
#         docker build \
#             --build-context installer=docker-context/installer \
#             -t lab-vivado:2023.2 .
#   4. The installer location is exposed below as $VIVADO_INSTALLER
#      and the config as $VIVADO_INSTALL_CFG. Both must be present or
#      the build will FAIL FAST in `RUN` below — by design, to prevent
#      shipping an image without Vivado.
#
ARG VIVADO_VERSION=2023.2
ENV VIVADO_VERSION=${VIVADO_VERSION}

COPY --from=installer /installers/Xilinx_Unified_${VIVADO_VERSION} /tmp/xilinx-installer.bin
COPY --from=installer /installers/vivado-installer.cfg /tmp/vivado-installer.cfg

RUN chmod +x /tmp/xilinx-installer.bin \
    && /tmp/xilinx-installer.bin \
        --config /tmp/vivado-installer.cfg \
        --batch install \
    && rm -f /tmp/xilinx-installer.bin /tmp/vivado-installer.cfg \
    && /opt/Xilinx/Vivado/${VIVADO_VERSION}/bin/lmgr_check_token || true

ENV PATH="/opt/Xilinx/Vivado/${VIVADO_VERSION}/bin:${PATH}"
ENV XILINXD_LICENSE_FILE=/opt/Xilinx/license.lic

RUN useradd -m -s /bin/bash labuser \
    && usermod -a -G ssl-cert,dialout labuser \
    && mkdir -p /opt/Xilinx/license

COPY kasmvnc.yaml /home/labuser/.vnc/kasmvnc.yaml

COPY xstartup /home/labuser/.vnc/xstartup
RUN chown -R labuser:labuser /home/labuser/.vnc && chmod +x /home/labuser/.vnc/xstartup

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

USER labuser
WORKDIR /home/labuser

EXPOSE 6901

ENTRYPOINT ["/entrypoint.sh"]
