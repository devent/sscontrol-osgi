#!/bin/bash
theCommand="$0"
echo "$theCommand $@"
echo "$@" >> $theCommand.out

if [[ "$1" == "list" ]] && [[ "$2" == "installed" ]]; then
  exit 1
fi

if [ "$#" -gt 0 ]; then
    cmd=($1)
    cmd=${cmd[0]}
    if command -v -- "$cmd">/dev/null; then
       $@
    fi
fi
