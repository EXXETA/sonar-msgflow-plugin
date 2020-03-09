# sonar-msgflow-plugin
The SonarQube Message Flow Plugin is a tool for static code analysis of message flows / integration flows developed for the IBM Websphere Message Broker / IBM Integration Bus. The plugin analyzes msgflow files regarding configuration and wiring of message flow nodes for the IBM Websphere Message Broker / IBM Integration Bus.


## Build

1. mvn clean install

## Installation

1. Install SonarQube (https://docs.sonarqube.org/display/SONAR/Get+Started+in+Two+Minutes)
2. Place the latest msgflow-plugin-<version>.jar from msgflow-plugin/target (or downloaded from github) to the plugin directory of SonarQube(\extensions\plugins)

## Requirements

- SonarQube 7.9


## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request


## Credits

- [EXXETA AG](http://exxeta.com)
- See [Contributors](https://www.github.com/EXXETA/sonar-esql-plugin/graphs/contributors)

## License

Apache License v2.0
