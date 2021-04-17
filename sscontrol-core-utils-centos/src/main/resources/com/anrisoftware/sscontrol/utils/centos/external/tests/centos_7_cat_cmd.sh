#!/bin/bash

/bin/cat <<EOF
NAME="CentOS Linux"
VERSION="7 (Core)"
ID="centos"
ID_LIKE="rhel fedora"
VERSION_ID="7"
PRETTY_NAME="CentOS Linux 7 (Core)"
ANSI_COLOR="0;31"
CPE_NAME="cpe:/o:centos:centos:7"
HOME_URL="https://www.centos.org/"
BUG_REPORT_URL="https://bugs.centos.org/"

CENTOS_MANTISBT_PROJECT="CentOS-7"
CENTOS_MANTISBT_PROJECT_VERSION="7"
REDHAT_SUPPORT_PRODUCT="centos"
REDHAT_SUPPORT_PRODUCT_VERSION="7"

EOF

theCommand="$0"
echo "$theCommand $@"
echo "$@" >> $theCommand.out
if [ "$#" -gt 0 ]; then
    cmd=($1)
    cmd=${cmd[0]}
    if command -v -- "$cmd">/dev/null; then
       $@
    fi
fi
