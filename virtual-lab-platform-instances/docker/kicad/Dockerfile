FROM ubuntu:24.04

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y --no-install-recommends \
    xfce4 \
    xfce4-terminal \
    xfce4-goodies \
    kicad \
    kicad-packages3d \
    wget \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

RUN apt-get update && apt-get install -y --no-install-recommends \
    dbus-x11 \
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

RUN useradd -m -s /bin/bash labuser \
    && usermod -a -G ssl-cert labuser

RUN mkdir -p /home/labuser/.vnc

COPY kasmvnc.yaml /home/labuser/.vnc/kasmvnc.yaml

COPY xstartup /home/labuser/.vnc/xstartup
RUN chown -R labuser:labuser /home/labuser/.vnc && chmod +x /home/labuser/.vnc/xstartup

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

USER labuser
WORKDIR /home/labuser

EXPOSE 6901

ENTRYPOINT ["/entrypoint.sh"]
