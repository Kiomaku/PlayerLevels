
<a id="readme-top"></a>


[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]




<br />
<div align="center">
  <a href="https://github.com/kiomaku/PlayerLevel">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">PlayerLevels</h3>

  <p align="center">
    ❄🎄 Minecraft Player Levels Plugin ❄🎄 | 📝 Fully Configurable 📝 | 📅 Database Support [Mysql / Sqlite] 📅 | Messages / Custom Reward / Xp per Level and more!🔥 | Placeholder api support! |
    <br />
    <a href="https://www.frostborn.ir/plugins/playerlevels/doc"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/Kiomaku/PlayerLevels/releases/latest">Download Last Release</a>
    &middot;
    <a href="https://github.com/kiomaku/PlayerLevel/issues/new?labels=bug&template=bug-report---.md">Report Bug</a>
    &middot;
    <a href="https://github.com/kiomaku/PlayerLevel/issues/new?labels=enhancement&template=feature-request---.md">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-plugin">About The Plugin</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#Commands">Commands</a></li>
    <li><a href="#Permissions">Permissions</a></li>
    <li><a href="#PlaceholderApi">PlaceholderApi</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Plugin

[![Product Name Screen Shot][product-screenshot]](https://example.com)

this is the best player level plugin system that you can find out for free todays!

Here's why:
* Supports 2 type of Database [Mysql / Sqlite] 💨
* PlaceholderApi support with over 4 papi! 😲
* Vault support for money rewards! :smile:
* Item Reward system 💥
* Command reward system 💘
* all configurable [Messages / database / rewards / xp per level requirment / etc...] 🍃

Of course, the project still have some potential issues and please report any bugs / exploits in the issues section!


<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Built With

This section should list any major frameworks/libraries used to create the plugin.

* [![Kotlin][Kotlin]][Kotlin-url]
* [![Mysql][Mysql]][Mysql-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

this is an example for you to how get started working and using my plugin.


### Prerequisites

heres all the requirment listed for you to have the plugin installed on your own server.

* [Vault](https://www.spigotmc.org/resources/vault.34315/)
Soft Depends:
* [PlaceHolderApi](https://www.spigotmc.org/resources/placeholderapi.6245/)

### Installation

_Below is an example of how you can install the plugin on your server._

1. Simply Download the latest release at [here](https://github.com/Kiomaku/PlayerLevels/releases/tag/BETA)
2. Put the plugin on your server directory as [./plugins/]
3. Download [Vault](https://www.spigotmc.org/resources/vault.34315/) Plugin and put it in the same directory as the plugin.
4. Start your Server and enjoy the full configurable level system!
<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

Heres some examples to use the commands link down below:

_For more examples, please refer to the [Documentation](https://www.frostborn.ir/plugins/playerlevels/usage)_

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- Commands  -->

## Commands:
/playerlevels addlevel [player] [amount]  - > add level to the desire player 
/playerlevels addxp [player] [amount]  - > add xp to the desire player 
/playerlevels takelevel [player] [amount]  - > take level from the desire player 
/playerlevels takexp [player] [amount]  - > take xp to from desire player 
/playerlevels level - > see your current level

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- Permissions  -->

## Permissions:
   playerlevels.use:
    description: Permission to use the PlayerLevels commands
    default: op
  playerlevels.addxp:
    description: Allows adding XP to players
    default: op
  playerlevels.takexp:
    description: Allows removing XP from players
    default: op
  playerlevels.addlevel:
    description: Allows adding levels to players
    default: op
  playerlevels.takelevel:
    description: Allows removing levels from players
    default: op


    
<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- PAPI -->
## PlaceholderApi

- [⁉]  %playerlevels_level%  | Show player current level
- [⁉]  %playerlevels_xp%  | Show player current xp
- [⁉]  %playerlevels_toplevel_[number]%  | Show top [number] player level
- [⁉]  %playerlevels_topname_[number]%  | Show top [number] player name


Using [papi](https://www.spigotmc.org/resources/placeholderapi.6245/) plugin you are able to use this feature, simply [download](https://www.spigotmc.org/resources/placeholderapi.6245/) the plugin and drag and drop it into your plugins folder and restart the server.

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- ROADMAP -->
## Roadmap

- [x] Add sql Datbase support
- [x] Add Mysql database support
- [ ] Multi-language Support
    - [ ] Farsi
    - [ ] Russian

See the [open issues](https://github.com/kiomaku/PlayerLevels/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Top contributors:

<a href="https://github.com/kiomaku/playerlevels/contributors">
  <img src="https://contrib.rocks/image?repo=kiomaku/playerlevels" alt="contrib.rocks image" />
</a>

<p align="right">(<a href="#readme-top">back to top</a>)</p>




<!-- CONTACT -->
## Contact

BarsamBahrami - [Discord Link](https://discord.gg/huvJ2ECjxP) - barsambahramimc@gmail.com

Project Link: [https://github.com/kiomaku/playerlevels](https://github.com/kiomaku/playerlevels)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

Im an junior developer starting this project by myself alone, i have time to response the issues and solve them so feel free to report them as ill fix and answer your questions/plugin bugs as soon as possible!


a big thanks to the sponser of this plugin frost born mc! a roleplay mincraft server using this plugin right now! join and play a big commiunity.
* [FrostBorn Minecraft Server](https://frostborn.ir)
<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
[contributors-shield]: https://img.shields.io/github/contributors/kiomaku/playerlevels.svg?style=for-the-badge
[contributors-url]: https://github.com/kiomaku/playerlevels/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/kiomaku/playerlevels.svg?style=for-the-badge
[forks-url]: https://github.com/kiomaku/playerlevels/network/members
[stars-shield]: https://img.shields.io/github/stars/kiomaku/playerlevels.svg?style=for-the-badge
[stars-url]: https://github.com/kiomaku/playerlevels/stargazers
[issues-shield]: https://img.shields.io/github/issues/kiomaku/playerlevels.svg?style=for-the-badge
[issues-url]: https://github.com/kiomaku/playerlevels/issues
[license-shield]: https://img.shields.io/github/license/kiomaku/playerlevels.svg?style=for-the-badge
[license-url]: https://github.com/kiomaku/playerlevels/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/othneildrew
[product-screenshot]: images/screenshot.png
[logo]: images/logo.png
[Kotlin]: https://img.shields.io/badge/Kotlin-000000?style=for-the-badge&logo=nextdotjs&logoColor=white
[Kotlin-url]: https://kotlinlang.org/
[Mysql]: https://img.shields.io/badge/Mysql-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[Mysql-url]: https://www.mysql.com/
