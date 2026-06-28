#!/bin/bash
set -e

rm -f /tmp/.X1-lock
rm -f /tmp/.X11-unix/X1

if [ -n "$KASMVNC_PASSWORD" ]; then
  mkdir -p /home/labuser/.vnc
  printf "${KASMVNC_PASSWORD}\n${KASMVNC_PASSWORD}\n" | vncpasswd -u labuser -w -r 2>/dev/null || true
  chown -R labuser:labuser /home/labuser/.vnc
fi

kasmvncserver :1 -select-de xfce

tail -f /home/labuser/.vnc/*.log &
TAIL_PID=$!

cleanup() {
  kasmvncserver -kill :1 2>/dev/null || true
  kill "$TAIL_PID" 2>/dev/null || true
  exit 0
}
trap cleanup SIGTERM SIGINT SIGQUIT

wait "$TAIL_PID"
