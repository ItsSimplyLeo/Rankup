# Rankup
Simple Rankup system for Spigot in the works. Right now it is in a very basic state, will update periodically.

## Features

### Ranks GUI
- Able to view all the available ranks
- Able to buy all the available ranks in one go

### Permissions
You are able to set permissions based on the current rank of the user without needing to create a group on your permissions plugin.

## Default Configuration
```yaml
ranks:
  A:
    price: 0 # Does not matter on the first rank, as it's what you start with.
    permissions:
      - 'essentials.warp.a'
  B:
    price: 10000
    permissions:
      - 'essentials.warp.b'
  C:
    price: 25000
    permissions:
      - 'essentials.warp.c'
```