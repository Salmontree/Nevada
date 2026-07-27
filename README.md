## Nevada

### Commands

- `/denick <skin|finals|beds> <value>` - Denick nicked players on Hypixel
- `/statcheck <username> <mode>` - Fetch player stats on certain Hypixel gamemodes (currently supported modes include bedwars (`bw`) and build battle (`bb`)
- `/view [username]` - Fetch player blacklist tags. Currently only supports Urchin.
- `/gexp [username] [daily|weekly|monthly]` - Fetch player's Guild experience obtained in a specified time period
- `/play <game>` - Shortcuts for Hypixel's /play. Shortcuts include:
  - `bw1` - Bedwars Solo
  - `bw2` - Bedwars Doubles
  - `bw3` - Bedwars 3v3v3v3
  - `bw4` - Bedwars 4v4v4v4
  - `bridge1` - Duels - Bridge Solo
  - `bridge2` - Duels - Bridge Doubles
  - `bridge3` - Duels - Bridge 3v3
  - `bridge4` - Duels - Bridge Teams

### Tab Stats

The tablist is (optionally) adapted to include stats and tags for each player. _Only works in Bedwars._
- `Bedwars Level`
- `Tags`
- `Name`
- One stat from the list: `Finals`, `FKDR`, `Wins`, `WLR`, `Beds`, `BBLR`

### Denicking

While in-game, players who are nicked but have their original skin are automatically denicked and their real names are shown in the tablist. _Only works in Bedwars._


### Anticheat

Checks for some common detectable cheats, which inform you of any flags. Checks include:
- `Eagle` (diagonal legitscaff)
- `Scaffold` (horizontal legitscaff)
- `Tower` (block-stacking too fast)
- `Autoblock` (hitting while blocking)

### Issues

Sometimes, APIs reach their rate limits and stop working. This can be seen very clearly when enabling Tab Stats in a large lobby (why would you even do this?). Just try loading stats again after a few minutes and it should work fine. Tab Stats automatically loads stats after the API starts returning values again.