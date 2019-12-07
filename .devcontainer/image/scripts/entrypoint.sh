#!/usr/bin/zsh -e
# Documentation of `zsh` option: http://zsh.sourceforge.net/Doc/Release/Options.html
dirname=${0:a:h}

$dirname/link-vscode-base-config.sh

# Make /workspaces writable, to be able to create more workspaces
sudo chmod 777 /workspaces

$*
