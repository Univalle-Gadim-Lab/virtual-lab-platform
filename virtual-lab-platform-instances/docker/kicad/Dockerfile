FROM ubuntu:24.04

ENV DEBIAN_FRONTEND=noninteractive
ENV DISPLAY=:0
ENV RESOLUTION=1920x1080x24

RUN apt-get update && apt-get install -y --no-install-recommends \
    lxde-core \
    lxterminal \
    xvfb \
    kicad \
    kicad-packages3d \
    supervisor \
    wget \
    && rm -rf /var/lib/apt/lists/*

ARG KASMVNC_VERSION=1.20.0

RUN wget -q "https://github.com/kasmtech/KasmVNC/releases/download/v${KASMVNC_VERSION}/kasmvncserver_ubuntu_2404_${KASMVNC_VERSION}_amd64.deb" \
    -O /tmp/kasmvnc.deb \
    && apt-get update \
    && apt-get install -y --no-install-recommends /tmp/kasmvnc.deb \
    && rm -f /tmp/kasmvnc.deb \
    && rm -rf /var/lib/apt/lists/*

RUN useradd -m -s /bin/bash labuser
COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

USER labuser
WORKDIR /home/labuser

EXPOSE 6901

CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]
