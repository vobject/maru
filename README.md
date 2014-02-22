Maru
====

Maru is an satellite mission planning application. Its main functionality is to show satellite orbits and create reports for further processing. It relies heavily on the excellent [Orekit](https://www.orekit.org/) library and other external resources.
The program is written in Java and is based on Eclipse RCP. Maru runs on Windows and Linux operating systems.

Its features include:
- Satellite orbits defined by
  * Cartesian,
  * Circular,
  * Equinoctial,
  * Keplerian, or
  * two-line elements (TLE)
- Satellite orbit propagation using
  * SGP4/SDP4
  * Two-Body
- Ground stations
- Textual report generation for
  * Propagation
  * Eclipse
  * Visibility (Satellite-to-Satellite or Satellite-to-Groundstation)
- Access to online and offline TLE sources (e.g. CelesTrak)
- Scenario playback and real-time mode
- Presentation mode
- User defined Earth textures

# Screenshots
[![alttext][010]][010]
[![alttext][030]][030]
[![alttext][040]][040]

[010]: https://github.com/vobject/maru/wiki/images/20140110_realtime_scenario.jpg (Scenario in real-time mode)
[020]: https://github.com/vobject/maru/wiki/images/20140110_scenario_nasabg.jpg (Alternative Earth image)
[030]: https://github.com/vobject/maru/wiki/images/20140110_realtime_scenario_pres.jpg (Presentation perspective)
[040]: https://github.com/vobject/maru/wiki/images/20140110_propagation_report.jpg (Propagation report)
[050]: https://github.com/vobject/maru/wiki/images/20140110_eclipse_report.jpg (Eclipse report)

# Download
Check out the project's [releases](https://github.com/vobject/maru/releases) for ready-to-use packages.

# Development
[Getting Started](https://github.com/vobject/maru/wiki/Getting-Started-Developing)  
[Coding Standards - TODO](https://github.com/vobject/maru/wiki/Coding-Standards)  
[Architecture - TODO](https://github.com/vobject/maru/wiki/Architecture)  

# Future
Plans for the next release:
- Project-specific preferences/properties
- Integrate more Propagators provided by Orekit
- Fix OpenGL issues under Linux (done)
- JUnit tests

Long term plans:
- NASA WorldWind integration
- Other central bodies besides Earth
- Port to Eclipse 4.x RCP

# Resources
This program includes software and media developed by the following third parties:
- Orekit: https://www.orekit.org
- Apache Commons Math: http://commons.apache.org/proper/commons-math
- JOGL: https://jogamp.org/jogl
- NASA World Wind: http://worldwind.arc.nasa.gov
- Oxygen Team: http://www.oxygen-icons.org
- Unrestrictedstock.com: http://unrestrictedstock.com
- JHT's Planetary Pixel Emporium: http://planetpixelemporium.com
- Natural Earth II: http://www.shadedrelief.com/natural2

# License
Maru is licensed under the terms of the [Apache License 2.0](http://apache.org/licenses/LICENSE-2.0.txt).
