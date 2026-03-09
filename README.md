# PlayerHider

A lightweight Minecraft plugin that allows players to hide or show other players using a hotbar item.

## Features

- Toggle visibility of all players, no players, or VIP-only players
- Hotbar item cycles through modes on right-click
- Player preferences are saved and restored on rejoin
- Supports LOCAL, H2, and MySQL storage backends
- Fully configurable items, sounds, and cooldown

## Modes

| Mode | Description                                   |
|------|-----------------------------------------------|
| All players visible | Default mode - all players are shown          |
| No players visible | All players are hidden                        |
| VIPs only visible | Only players with `playerhider.vip` are shown |

## Installation

1. Download the latest release from the [Releases](../../releases) page
2. Place the JAR into your `plugins/` folder
3. Restart your server
4. Configure `plugins/PlayerHider/config.yml` to your liking

## Configuration
```yaml
storage:
  type: LOCAL # LOCAL, H2, MYSQL

world: "world"
slot: 8
click-cooldown: 1000

items:
  all:
    material: LIME_DYE
    display-name: "&aAll players visible &7(Right-click)"
  none:
    material: GRAY_DYE
    display-name: "&cNo players visible &7(Right-click)"
  vip:
    material: PURPLE_DYE
    display-name: "&5VIPs only visible &7(Right-click)"
```

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `playerhider.vip` | Allows the player to be seen in VIP-only mode | false |

## Requirements

- Java 21+
- Spigot / Paper 1.21+

## License

MIT License — see [LICENSE](LICENSE) for details.