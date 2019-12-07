#!/usr/bin/env zsh

# Creates symlinks at the location of the first argument to destinations on a volume
# mounted at `~/vscode-base-config`. If destination does not exist, but there is something
# at the location of the symlink, that something will be moved to the destination. If
# neither the destination nor the something exists, a directory will be created at the
# location of the destination.
#
# Usage:
#
#   link_vscode_base_config <symlink_location> [dest_location]
#
# If not given, `dest_location` is set to
#   `~/vscode-base-config/<basename of symlink_location without a possibly leading dot>`.
#
# E.g. `link_vscode_base_config ~/.kube` will create a symlink at location `~/.kube`
# to a destination `~/vscode-base-config/kube`.
function link_vscode_base_config {
  local dest=$2
  if [ -z "$dest" ]
  then
    # If no `dest_location` was given, use basename of `symlink_location` without leading dot
    dest=~/vscode-base-config/${$(basename "$1")#.}
  fi

  # if `$dest_location` does not exist, but `$symlink_location` does, move symlink to dest
  if [[ ( ! -e "$dest" ) && ( -e "$1" ) ]]
  then
    echo "Move $1 -> $dest"
    mv "$1" "$dest"

  # If neither exists, create directory at dest
  elif [[ ( ! -e "$dest" )]]
  then
    echo "Create directory at $dest"
    mkdir -p "$dest"

  # If both exist, delete "thing" at symlink location
  elif [[ -e "$1" ]]
  then
    rm -rf "$1"
  fi 

  # Finally, create symlink
  ln -s "$dest" "$1"
}

# As we are just here, also check permissions on `~/vscode-base-config`
if [ "$(id -u)" != "$(stat -c "%u" ~/vscode-base-config)" ]
then
  echo Updating permissions on $(realpath ~/vscode-base-config) ...
  sudo chown -R node:node ~/vscode-base-config 
fi

# Use function defined above to create some links
link_vscode_base_config ~/.cache
link_vscode_base_config ~/.config
link_vscode_base_config ~/.docker
link_vscode_base_config ~/.kube
link_vscode_base_config ~/.pkg-cache
link_vscode_base_config ~/.ssh

touch ~/.zsh_history && link_vscode_base_config ~/.zsh_history