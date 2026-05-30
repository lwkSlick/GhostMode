# 👻 GhostMode

A client-side privacy mod for Minecraft content creators, streamers, and anyone who values their privacy online.

## Features

- **Sentinel System** — auto-detects your IGN and fuzzy-matches it everywhere it could appear, then blurs or replaces it
- **Watchlist** — protect your friends' names too, each with their own alias
- **Stream Mode** — one keybind or safe combo activates full privacy instantly
- **Profiles** — create and name your own profiles per scenario (streaming, YouTube, private, etc.)
- **IP Masking** — server IPs replaced with `••••••••••` everywhere, including F3 and direct connect history
- **F3 Scrubber** — hides coordinates, local address, and memory info
- **Tab List & Scoreboard** — mask your entry, show player count only, or hide entirely
- **Chat Privacy** — suppress DMs, remove mention highlights, disable chat logs to disk
- **Screenshot Guard** — blurs matched names before saving to disk
- **Watermark** — overlay your main channel name so alt clips still get attributed to you
- **Session Log Wipe** — clears latest.log on disconnect so your IGN and server IPs don't sit on disk

## Requirements

- Minecraft 1.21.11
- Fabric Loader
- Fabric API
- [YACL](https://modrinth.com/mod/yacl)
- [Mod Menu](https://modrinth.com/mod/modmenu)

## Installation

1. Install [Fabric](https://fabricmc.net/use/)
2. Drop `ghostmode.jar` into your `.minecraft/mods` folder
3. Also drop in YACL and Mod Menu
4. Launch and configure via Mod Menu → GhostMode

## License

MIT © lwkslick