# QuickNick
QuickNick allows you to add nicknames to your server with little effort or if you prefer, much more effort.

## Explained config

```yaml
#
# QNick configuration guide
#

# Enables or disables the `nick` command (true = enabled, false = disabled)
# May be useful if using a plugin which uses the QNickAPI
command: true

# Changes a users name to their nick (if found) on join (true = enabled, false = disabled)
# May be useful if using a plugin which uses the QNickAPI
changenameonjoin: true

# SQL, if true uses an SQL database to store nicknames
# Set to false to use PDC (see below for an explanation)
sql: false

# These options only apply if SQL is enabled
sqloptions:
  host: "127.0.0.1"
  port: 3306
  db: "yourDB"
  username: "username"
  password: "password"

#
# Debug options
#

# Enable/disable API access (true = enabled, false = disabled)
# May be useful to server owners when debugging plugin issues
apienabled: true
```

## What is PDC?
PDC stands for PersistentDataContainer, this allows QuickNick to save the nickname of a user in the user's .dat file, however this means it is impossible to sync nicknames between servers.

## What is SQL?
SQL is a form of Database storage which can be accessed from multiple servers, if you have multiple servers connected to the SQL server then nicknames can be synced.

# GitHub
Here are some instructions for some GitHub things some people may want to mess with

## Getting development builds
Visit [here](https://github.com/CCSKore/QNick/releases/tag/latest) to get the latest development builds, however, BE WARNED

These builds are not stable whatsoever, do NOT use them on a production server, I am not responsible for any damage the development versions do