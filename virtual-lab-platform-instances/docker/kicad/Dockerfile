FROM ubuntu:24.04

# Prevent interactive prompts during apt installations
ENV DEBIAN_FRONTEND=noninteractive
ENV DISPLAY=:0
ENV RESOLUTION=1920x1080x24

# Install desktop environment, VNC, noVNC, KiCad, and Supervisor
RUN apt-get update && apt-get install -y --no-install-recommends \
    lxde-core \
    lxterminal \
    xvfb \
    x11vnc \
    novnc \
    websockify \
    kicad \
    kicad-packages3d \
    supervisor \
    && rm -rf /var/lib/apt/lists/*

# Create an unprivileged user for the workspace
RUN useradd -m -s /bin/bash labuser
COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

USER labuser
WORKDIR /home/labuser

# Expose the noVNC web port
EXPOSE 8080

# Start Supervisor to manage the X11 and VNC processes
CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]