<div id="top"></div>


<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]



<!-- PROJECT LOGO -->
<br />
<div align="center">
  <img src="https://github.com/zhenxinglu/DicomBuddy/blob/master/images/logo.png" alt="Logo" width="100" height="100">

<h3 align="center">Dicom Buddy</h3>

  <p align="center">
    A tool to view and edit DICOM files!
    <br />
    <a href="https://github.com/zhenxinglu/DicomBuddy"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/zhenxinglu/DicomBuddy/issues">Report Bug</a>
    ·
    <a href="https://github.com/zhenxinglu/DicomBuddy/issues">Request Feature</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#build-the-installer">Build the installer</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

![screenshot](https://github.com/zhenxinglu/DicomBuddy/blob/master/images/screenshot.png)

This JavaFX-based application allows users to view and edit DICOM files. It supports the following features:

View DICOM Files: Open and display DICOM images with ease.
Edit DICOM Tags: Modify metadata and tags within DICOM files.
Search DICOM Tags: Search tags by tag, name, or value to quickly find relevant metadata.
Drag & Drop Support: Seamlessly drag and drop DICOM files into the application for quick access.
User-Friendly Interface: Intuitive design for effortless viewing and editing.
This project is aimed at medical imaging professionals and developers looking for a lightweight and efficient DICOM file manager.

<p align="right">(<a href="#top">back to top</a>)</p>


### Built With

DicomBuddy is a JavaFX application. 

* [JavaFX 23](https://openjfx.io/)



<p align="right">(<a href="#top">back to top</a>)</p>


<!-- GETTING STARTED -->
## Getting Started

Dicom Buddy is built with Maven. Because it builds the custom JRE as well so the local JavaFX 23 modues must 
be installed and the relevant environment values should be correctly setup.

### Prerequisites
* install [JavaFX 23+ modules](https://gluonhq.com/products/javafx) and set environment variable PATH_TO_FX_MODS.
  ```sh
  PATH_TO_FX_MODS=/path/to/JavaFX/modules
  ```
* install Maven
* install JDK 22+

### Build the installer
1. cd <project_home_dir>
2. ./build.sh

After the build we can find the DicomBuddy installer for Windows in the `output` directory under the home directory
of the project. 

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- ROADMAP -->
## Roadmap

- [x] First workable version:  view and edit DICOM files
- [ ] Fix editing issue
- [ ] Open multiple files
- [ ] Multi-language Support
    - [ ] Chinese
    - [x] English

See the [open issues](https://github.com/zhenxinglu/DicomBuddy/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- CONTACT -->
## Contact

Zhenxing Lu : zhenxinglu@gmail.com

Project Link: https://github.com/zhenxinglu/DicomBuddy

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/othneildrew/Best-README-Template.svg?style=for-the-badge
[contributors-url]: https://github.com/zhenxinglu/DicomBuddy/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/othneildrew/Best-README-Template.svg?style=for-the-badge
[forks-url]: https://github.com/zhenxinglu/DicomBuddy/network/members
[stars-shield]: https://img.shields.io/github/stars/othneildrew/Best-README-Template.svg?style=for-the-badge
[stars-url]: https://github.com/zhenxinglu/DicomBuddy/stargazers
[issues-shield]: https://img.shields.io/github/issues/othneildrew/Best-README-Template.svg?style=for-the-badge
[issues-url]: https://github.com/zhenxinglu/DicomBuddy/issues
[license-shield]: https://img.shields.io/github/license/othneildrew/Best-README-Template.svg?style=for-the-badge
[license-url]: https://github.com/zhenxinglu/DicomBuddy/blob/master/LICENSE.txt

