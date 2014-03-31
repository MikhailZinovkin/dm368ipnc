#!/bin/sh
# For when restarting this service
rmmod g_ether 2>/dev/null

# Load the module
modprobe g_ether

# Configure a static IP address for the OTG USB networking port
ip addr add 10.3.14.15 dev usb0
ip link set usb0 up
ip route add 10.3.14.0/24 dev usb0
