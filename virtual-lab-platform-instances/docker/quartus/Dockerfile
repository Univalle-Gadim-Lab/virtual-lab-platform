FROM ubuntu:24.04

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y --no-install-recommends \
    xfce4 \
    xfce4-terminal \
    xfce4-goodies \
    dbus-x11 \
    wget \
    ca-certificates \
    libsm6 \
    libice6 \
    libxtst6 \
    libnss3 \
    libfontconfig1 \
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
# QUARTUS PRIME INSTALLATION (PROPRIETARY, OPERATOR-PROVIDED)
# ============================================================
#
# Quartus Prime is a proprietary Intel/Altera tool. The Quartus
# Prime installer (a self-extracting .run script) must be supplied
# by the image builder due to licensing terms. This Dockerfile
# mounts the installer and components in /tmp/installer (via a
# build context) and runs them in silent/install mode.
#
# Expected preparation by the operator:
#   1. From intel.com, under the FPGA & SoC design tools section,
#      download both:
#        - QuartusPrimeSetup-<version>-linux.run
#        - the individual device support packages the lab needs
#          (Cyclone, Arria, Stratix, MAX, Agilex, e.t.c.)
#   2. Place them under `docker-context/installer/`.
#   3. Build using --build-context installer=docker-context/installer.
#   4. The installer files are exposed below as $QUARTUS_INSTALLER and
#      $QUARTUS_DEVICES; both must be present or the build will FAIL
#      FAST in `RUN` below — by design, to prevent shipping an image
#      without Quartus.
ARG QUARTUS_VERSION=22.1
ENV QUARTUS_VERSION=${QUARTUS_VERSION}

COPY --from=installer /installers/QuartusPrimeSetup-${QUARTUS_VERSION}-linux.run /tmp/quartus-installer.run
COPY --from=installer /installers/devices/ /tmp/quartus-devices/

RUN chmod +x /tmp/quartus-installer.run \
    && /tmp/quartus-installer.run \
        --mode unattended \
        --accept_eula 1 \
        --installdir /opt/intelFPGA/${QUARTUS_VERSION} \
        --unattendedmodeui none \
    && /opt/intelFPGA/${QUARTUS_VERSION}/quartus/bin/quartus --version \
        || (for d in /tmp/quartus-devices/*.qdz; do \
                /opt/intelFPGA/${QUARTUS_VERSION}/quartus/bin/quartus_pgm --install-devices "$d" || true; \
            done) \
    && rm -rf /tmp/quartus-installer.run /tmp/quartus-devices

ENV PATH="/opt/intelFPGA/${QUARTUS_VERSION}/quartus/bin:${PATH}"
ENV QUARTUS_ROOTDIR=/opt/intelFPGA/${QUARTUS_VERSION}/quartus

RUN useradd -m -s /bin/bash labuser \
    && usermod -a -G ssl-cert,dialout labuser

COPY kasmvnc.yaml /home/labuser/.vnc/kasmvnc.yaml

COPY xstartup /home/labuser/.vnc/xstartup
RUN chown -R labuser:labuser /home/labuser/.vnc && chmod +x /home/labuser/.vnc/xstartup

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

USER labuser
WORKDIR /home/labuser

EXPOSE 6901

ENTRYPOINT ["/entrypoint.sh"]
