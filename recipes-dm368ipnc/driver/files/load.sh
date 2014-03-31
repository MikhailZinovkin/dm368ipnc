#!/bin/sh
# Conflicts on 3.5.7 kernel
rmmod omap3_isp 2>/dev/null

# For when restarting this service
rmmod v4l2_driver 2>/dev/null

# For some reason we have to do it twice on boot
modprobe v4l2_driver
modprobe v4l2_driver
